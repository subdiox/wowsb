package com.facebook.login;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.facebook.AccessToken;
import com.facebook.AccessTokenSource;
import com.facebook.FacebookActivity;
import com.facebook.FacebookException;
import com.facebook.FacebookRequestError;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequest.Callback;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.R;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.devicerequests.internal.DeviceRequestsHelper;
import com.facebook.internal.AnalyticsEvents;
import com.facebook.internal.FetchedAppSettings;
import com.facebook.internal.FetchedAppSettingsManager;
import com.facebook.internal.ImageDownloader;
import com.facebook.internal.ImageRequest;
import com.facebook.internal.ImageRequest.Builder;
import com.facebook.internal.ImageResponse;
import com.facebook.internal.ServerProtocol;
import com.facebook.internal.SmartLoginOption;
import com.facebook.internal.Utility;
import com.facebook.internal.Utility.PermissionsPair;
import com.facebook.internal.Validate;
import com.facebook.login.LoginClient.Request;
import java.util.Date;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.json.JSONObject;

public class DeviceAuthDialog extends DialogFragment {
    private static final String DEVICE_LOGIN_ENDPOINT = "device/login";
    private static final String DEVICE_LOGIN_STATUS_ENDPOINT = "device/login_status";
    private static final int LOGIN_ERROR_SUBCODE_AUTHORIZATION_DECLINED = 1349173;
    private static final int LOGIN_ERROR_SUBCODE_AUTHORIZATION_PENDING = 1349174;
    private static final int LOGIN_ERROR_SUBCODE_CODE_EXPIRED = 1349152;
    private static final int LOGIN_ERROR_SUBCODE_EXCESSIVE_POLLING = 1349172;
    private static final String REQUEST_STATE_KEY = "request_state";
    private AtomicBoolean completed = new AtomicBoolean();
    private TextView confirmationCode;
    private volatile GraphRequestAsyncTask currentGraphRequestPoll;
    private volatile RequestState currentRequestState;
    private DeviceAuthMethodHandler deviceAuthMethodHandler;
    private Dialog dialog;
    private boolean isBeingDestroyed = false;
    private boolean isRetry = false;
    private Request mRequest = null;
    private ProgressBar progressBar;
    private volatile ScheduledFuture scheduledPoll;

    private static class RequestState implements Parcelable {
        public static final Creator<RequestState> CREATOR = new Creator<RequestState>() {
            public RequestState createFromParcel(Parcel in) {
                return new RequestState(in);
            }

            public RequestState[] newArray(int size) {
                return new RequestState[size];
            }
        };
        private long interval;
        private long lastPoll;
        private String requestCode;
        private String userCode;

        RequestState() {
        }

        public String getUserCode() {
            return this.userCode;
        }

        public void setUserCode(String userCode) {
            this.userCode = userCode;
        }

        public String getRequestCode() {
            return this.requestCode;
        }

        public void setRequestCode(String requestCode) {
            this.requestCode = requestCode;
        }

        public long getInterval() {
            return this.interval;
        }

        public void setInterval(long interval) {
            this.interval = interval;
        }

        public void setLastPoll(long lastPoll) {
            this.lastPoll = lastPoll;
        }

        protected RequestState(Parcel in) {
            this.userCode = in.readString();
            this.requestCode = in.readString();
            this.interval = in.readLong();
            this.lastPoll = in.readLong();
        }

        public boolean withinLastRefreshWindow() {
            if (this.lastPoll != 0 && (new Date().getTime() - this.lastPoll) - (this.interval * 1000) < 0) {
                return true;
            }
            return false;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.userCode);
            dest.writeString(this.requestCode);
            dest.writeLong(this.interval);
            dest.writeLong(this.lastPoll);
        }
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        this.deviceAuthMethodHandler = (DeviceAuthMethodHandler) ((LoginFragment) ((FacebookActivity) getActivity()).getCurrentFragment()).getLoginClient().getCurrentHandler();
        if (savedInstanceState != null) {
            RequestState requestState = (RequestState) savedInstanceState.getParcelable(REQUEST_STATE_KEY);
            if (requestState != null) {
                setCurrentRequestState(requestState);
            }
        }
        return view;
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.dialog = new Dialog(getActivity(), R.style.com_facebook_auth_dialog);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        boolean z = DeviceRequestsHelper.isAvailable() && !this.isRetry;
        this.dialog.setContentView(initializeContentView(z));
        return this.dialog;
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (!this.isBeingDestroyed) {
            onCancel();
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (this.currentRequestState != null) {
            outState.putParcelable(REQUEST_STATE_KEY, this.currentRequestState);
        }
    }

    public void onDestroy() {
        this.isBeingDestroyed = true;
        this.completed.set(true);
        super.onDestroy();
        if (this.currentGraphRequestPoll != null) {
            this.currentGraphRequestPoll.cancel(true);
        }
        if (this.scheduledPoll != null) {
            this.scheduledPoll.cancel(true);
        }
    }

    public void startLogin(Request request) {
        this.mRequest = request;
        Bundle parameters = new Bundle();
        parameters.putString("scope", TextUtils.join(",", request.getPermissions()));
        String redirectUriString = request.getDeviceRedirectUriString();
        if (redirectUriString != null) {
            parameters.putString(ServerProtocol.DIALOG_PARAM_REDIRECT_URI, redirectUriString);
        }
        parameters.putString("access_token", Validate.hasAppID() + "|" + Validate.hasClientToken());
        parameters.putString(DeviceRequestsHelper.DEVICE_INFO_PARAM, DeviceRequestsHelper.getDeviceInfo());
        new GraphRequest(null, DEVICE_LOGIN_ENDPOINT, parameters, HttpMethod.POST, new Callback() {
            public void onCompleted(GraphResponse response) {
                if (response.getError() != null) {
                    DeviceAuthDialog.this.onError(response.getError().getException());
                    return;
                }
                JSONObject jsonObject = response.getJSONObject();
                RequestState requestState = new RequestState();
                try {
                    requestState.setUserCode(jsonObject.getString("user_code"));
                    requestState.setRequestCode(jsonObject.getString("code"));
                    requestState.setInterval(jsonObject.getLong("interval"));
                    DeviceAuthDialog.this.setCurrentRequestState(requestState);
                } catch (Throwable ex) {
                    DeviceAuthDialog.this.onError(new FacebookException(ex));
                }
            }
        }).executeAsync();
    }

    private void setCurrentRequestState(RequestState currentRequestState) {
        this.currentRequestState = currentRequestState;
        this.confirmationCode.setText(currentRequestState.getUserCode());
        this.confirmationCode.setVisibility(0);
        this.progressBar.setVisibility(8);
        if (!this.isRetry && DeviceRequestsHelper.startAdvertisementService(currentRequestState.getUserCode())) {
            AppEventsLogger.newLogger(getContext()).logSdkEvent(AnalyticsEvents.EVENT_SMART_LOGIN_SERVICE, null, null);
        }
        if (currentRequestState.withinLastRefreshWindow()) {
            schedulePoll();
        } else {
            poll();
        }
    }

    private void appendIconToTextView(final TextView textView, String iconUriString) {
        ImageDownloader.downloadAsync(new Builder(getContext(), Uri.parse(iconUriString)).setCallback(new ImageRequest.Callback() {
            public void onCompleted(ImageResponse response) {
                if (response.getBitmap() != null) {
                    textView.setCompoundDrawablesWithIntrinsicBounds(null, null, new BitmapDrawable(DeviceAuthDialog.this.getResources(), Bitmap.createScaledBitmap(response.getBitmap(), 24, 24, false)), null);
                }
            }
        }).build());
    }

    private View initializeContentView(boolean isSmartLogin) {
        View view;
        LayoutInflater inflater = getActivity().getLayoutInflater();
        if (isSmartLogin) {
            view = inflater.inflate(R.layout.com_facebook_smart_device_dialog_fragment, null);
            FetchedAppSettings settings = FetchedAppSettingsManager.getAppSettingsWithoutQuery(FacebookSdk.getApplicationId());
            if (settings.getSmartLoginBookmarkIconURL() != null) {
                appendIconToTextView((TextView) view.findViewById(R.id.com_facebook_smart_instructions_2), settings.getSmartLoginBookmarkIconURL());
            }
            if (settings.getSmartLoginMenuIconURL() != null) {
                appendIconToTextView((TextView) view.findViewById(R.id.com_facebook_smart_instructions_1), settings.getSmartLoginMenuIconURL());
            }
        } else {
            view = inflater.inflate(R.layout.com_facebook_device_auth_dialog_fragment, null);
        }
        this.progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        this.confirmationCode = (TextView) view.findViewById(R.id.confirmation_code);
        ((Button) view.findViewById(R.id.cancel_button)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                DeviceAuthDialog.this.onCancel();
            }
        });
        ((TextView) view.findViewById(R.id.com_facebook_device_auth_instructions)).setText(Html.fromHtml(getString(R.string.com_facebook_device_auth_instructions)));
        return view;
    }

    private void poll() {
        this.currentRequestState.setLastPoll(new Date().getTime());
        this.currentGraphRequestPoll = getPollRequest().executeAsync();
    }

    private void schedulePoll() {
        this.scheduledPoll = DeviceAuthMethodHandler.getBackgroundExecutor().schedule(new Runnable() {
            public void run() {
                DeviceAuthDialog.this.poll();
            }
        }, this.currentRequestState.getInterval(), TimeUnit.SECONDS);
    }

    private GraphRequest getPollRequest() {
        Bundle parameters = new Bundle();
        parameters.putString("code", this.currentRequestState.getRequestCode());
        return new GraphRequest(null, DEVICE_LOGIN_STATUS_ENDPOINT, parameters, HttpMethod.POST, new Callback() {
            public void onCompleted(GraphResponse response) {
                if (!DeviceAuthDialog.this.completed.get()) {
                    FacebookRequestError error = response.getError();
                    if (error != null) {
                        switch (error.getSubErrorCode()) {
                            case DeviceAuthDialog.LOGIN_ERROR_SUBCODE_CODE_EXPIRED /*1349152*/:
                            case DeviceAuthDialog.LOGIN_ERROR_SUBCODE_AUTHORIZATION_DECLINED /*1349173*/:
                                DeviceAuthDialog.this.onCancel();
                                return;
                            case DeviceAuthDialog.LOGIN_ERROR_SUBCODE_EXCESSIVE_POLLING /*1349172*/:
                            case DeviceAuthDialog.LOGIN_ERROR_SUBCODE_AUTHORIZATION_PENDING /*1349174*/:
                                DeviceAuthDialog.this.schedulePoll();
                                return;
                            default:
                                DeviceAuthDialog.this.onError(response.getError().getException());
                                return;
                        }
                    }
                    try {
                        DeviceAuthDialog.this.onSuccess(response.getJSONObject().getString("access_token"));
                    } catch (Throwable ex) {
                        DeviceAuthDialog.this.onError(new FacebookException(ex));
                    }
                }
            }
        });
    }

    private void presentConfirmation(final String userId, final PermissionsPair permissions, final String accessToken, String name) {
        String message = getResources().getString(R.string.com_facebook_smart_login_confirmation_title);
        String continueFormat = getResources().getString(R.string.com_facebook_smart_login_confirmation_continue_as);
        String cancel = getResources().getString(R.string.com_facebook_smart_login_confirmation_cancel);
        String continueText = String.format(continueFormat, new Object[]{name});
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(message).setCancelable(true).setNegativeButton(continueText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface alertDialog, int which) {
                DeviceAuthDialog.this.completeLogin(userId, permissions, accessToken);
            }
        }).setPositiveButton(cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface alertDialog, int which) {
                DeviceAuthDialog.this.dialog.setContentView(DeviceAuthDialog.this.initializeContentView(false));
                DeviceAuthDialog.this.startLogin(DeviceAuthDialog.this.mRequest);
            }
        });
        builder.create().show();
    }

    private void onSuccess(final String accessToken) {
        Bundle parameters = new Bundle();
        parameters.putString(GraphRequest.FIELDS_PARAM, "id,permissions,name");
        new GraphRequest(new AccessToken(accessToken, FacebookSdk.getApplicationId(), "0", null, null, null, null, null), "me", parameters, HttpMethod.GET, new Callback() {
            public void onCompleted(GraphResponse response) {
                if (!DeviceAuthDialog.this.completed.get()) {
                    if (response.getError() != null) {
                        DeviceAuthDialog.this.onError(response.getError().getException());
                        return;
                    }
                    try {
                        JSONObject jsonObject = response.getJSONObject();
                        String userId = jsonObject.getString("id");
                        PermissionsPair permissions = Utility.handlePermissionResponse(jsonObject);
                        String name = jsonObject.getString("name");
                        DeviceRequestsHelper.cleanUpAdvertisementService(DeviceAuthDialog.this.currentRequestState.getUserCode());
                        if (!FetchedAppSettingsManager.getAppSettingsWithoutQuery(FacebookSdk.getApplicationId()).getSmartLoginOptions().contains(SmartLoginOption.RequireConfirm) || DeviceAuthDialog.this.isRetry) {
                            DeviceAuthDialog.this.completeLogin(userId, permissions, accessToken);
                            return;
                        }
                        DeviceAuthDialog.this.isRetry = true;
                        DeviceAuthDialog.this.presentConfirmation(userId, permissions, accessToken, name);
                    } catch (Throwable ex) {
                        DeviceAuthDialog.this.onError(new FacebookException(ex));
                    }
                }
            }
        }).executeAsync();
    }

    private void completeLogin(String userId, PermissionsPair permissions, String accessToken) {
        this.deviceAuthMethodHandler.onSuccess(accessToken, FacebookSdk.getApplicationId(), userId, permissions.getGrantedPermissions(), permissions.getDeclinedPermissions(), AccessTokenSource.DEVICE_AUTH, null, null);
        this.dialog.dismiss();
    }

    private void onError(FacebookException ex) {
        if (this.completed.compareAndSet(false, true)) {
            if (this.currentRequestState != null) {
                DeviceRequestsHelper.cleanUpAdvertisementService(this.currentRequestState.getUserCode());
            }
            this.deviceAuthMethodHandler.onError(ex);
            this.dialog.dismiss();
        }
    }

    private void onCancel() {
        if (this.completed.compareAndSet(false, true)) {
            DeviceRequestsHelper.cleanUpAdvertisementService(this.currentRequestState.getUserCode());
            if (this.deviceAuthMethodHandler != null) {
                this.deviceAuthMethodHandler.onCancel();
            }
            this.dialog.dismiss();
        }
    }
}
