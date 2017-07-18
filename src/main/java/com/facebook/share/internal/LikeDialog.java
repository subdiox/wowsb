package com.facebook.share.internal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import com.facebook.FacebookCallback;
import com.facebook.internal.AppCall;
import com.facebook.internal.CallbackManagerImpl;
import com.facebook.internal.CallbackManagerImpl.Callback;
import com.facebook.internal.CallbackManagerImpl.RequestCodeOffset;
import com.facebook.internal.DialogFeature;
import com.facebook.internal.DialogPresenter;
import com.facebook.internal.DialogPresenter.ParameterProvider;
import com.facebook.internal.FacebookDialogBase;
import com.facebook.internal.FragmentWrapper;
import java.util.ArrayList;
import java.util.List;

public class LikeDialog extends FacebookDialogBase<LikeContent, Result> {
    private static final int DEFAULT_REQUEST_CODE = RequestCodeOffset.Like.toRequestCode();
    private static final String TAG = "LikeDialog";

    private class NativeHandler extends ModeHandler {
        private NativeHandler() {
            super();
        }

        public boolean canShow(LikeContent content, boolean isBestEffort) {
            return content != null && LikeDialog.canShowNativeDialog();
        }

        public AppCall createAppCall(final LikeContent content) {
            AppCall appCall = LikeDialog.this.createBaseAppCall();
            DialogPresenter.setupAppCallForNativeDialog(appCall, new ParameterProvider() {
                public Bundle getParameters() {
                    return LikeDialog.createParameters(content);
                }

                public Bundle getLegacyParameters() {
                    Log.e(LikeDialog.TAG, "Attempting to present the Like Dialog with an outdated Facebook app on the device");
                    return new Bundle();
                }
            }, LikeDialog.getFeature());
            return appCall;
        }
    }

    public static final class Result {
        private final Bundle bundle;

        public Result(Bundle bundle) {
            this.bundle = bundle;
        }

        public Bundle getData() {
            return this.bundle;
        }
    }

    private class WebFallbackHandler extends ModeHandler {
        private WebFallbackHandler() {
            super();
        }

        public boolean canShow(LikeContent content, boolean isBestEffort) {
            return content != null && LikeDialog.canShowWebFallback();
        }

        public AppCall createAppCall(LikeContent content) {
            AppCall appCall = LikeDialog.this.createBaseAppCall();
            DialogPresenter.setupAppCallForWebFallbackDialog(appCall, LikeDialog.createParameters(content), LikeDialog.getFeature());
            return appCall;
        }
    }

    public static boolean canShowNativeDialog() {
        return DialogPresenter.canPresentNativeDialogWithFeature(getFeature());
    }

    public static boolean canShowWebFallback() {
        return DialogPresenter.canPresentWebFallbackDialogWithFeature(getFeature());
    }

    public LikeDialog(Activity activity) {
        super(activity, DEFAULT_REQUEST_CODE);
    }

    public LikeDialog(Fragment fragment) {
        this(new FragmentWrapper(fragment));
    }

    public LikeDialog(android.app.Fragment fragment) {
        this(new FragmentWrapper(fragment));
    }

    public LikeDialog(FragmentWrapper fragmentWrapper) {
        super(fragmentWrapper, DEFAULT_REQUEST_CODE);
    }

    protected AppCall createBaseAppCall() {
        return new AppCall(getRequestCode());
    }

    protected List<ModeHandler> getOrderedModeHandlers() {
        ArrayList<ModeHandler> handlers = new ArrayList();
        handlers.add(new NativeHandler());
        handlers.add(new WebFallbackHandler());
        return handlers;
    }

    protected void registerCallbackImpl(CallbackManagerImpl callbackManager, final FacebookCallback<Result> callback) {
        final ResultProcessor resultProcessor = callback == null ? null : new ResultProcessor(callback) {
            public void onSuccess(AppCall appCall, Bundle results) {
                callback.onSuccess(new Result(results));
            }
        };
        callbackManager.registerCallback(getRequestCode(), new Callback() {
            public boolean onActivityResult(int resultCode, Intent data) {
                return ShareInternalUtility.handleActivityResult(LikeDialog.this.getRequestCode(), resultCode, data, resultProcessor);
            }
        });
    }

    private static DialogFeature getFeature() {
        return LikeDialogFeature.LIKE_DIALOG;
    }

    private static Bundle createParameters(LikeContent likeContent) {
        Bundle params = new Bundle();
        params.putString("object_id", likeContent.getObjectId());
        params.putString("object_type", likeContent.getObjectType());
        return params;
    }
}
