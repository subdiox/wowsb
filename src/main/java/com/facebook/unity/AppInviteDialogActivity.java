package com.facebook.unity;

import android.app.Activity;
import android.os.Bundle;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.internal.NativeProtocol;
import com.facebook.share.model.AppInviteContent.Builder;
import com.facebook.share.widget.AppInviteDialog;
import com.facebook.share.widget.AppInviteDialog.Result;

public class AppInviteDialogActivity extends BaseActivity {
    public static final String DIALOG_PARAMS = "dialog_params";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final UnityMessage response = new UnityMessage("OnAppInviteComplete");
        Bundle params = getIntent().getBundleExtra(DIALOG_PARAMS);
        Builder contentBuilder = new Builder();
        if (params.containsKey(Constants.CALLBACK_ID_KEY)) {
            response.put(Constants.CALLBACK_ID_KEY, params.getString(Constants.CALLBACK_ID_KEY));
        }
        if (params.containsKey("appLinkUrl")) {
            contentBuilder.setApplinkUrl(params.getString("appLinkUrl"));
        }
        if (params.containsKey("previewImageUrl")) {
            contentBuilder.setPreviewImageUrl(params.getString("previewImageUrl"));
        }
        AppInviteDialog dialog = new AppInviteDialog((Activity) this);
        dialog.registerCallback(this.mCallbackManager, new FacebookCallback<Result>() {
            public void onSuccess(Result result) {
                response.put(NativeProtocol.RESULT_ARGS_DIALOG_COMPLETE_KEY, Boolean.valueOf(true));
                response.send();
            }

            public void onCancel() {
                response.putCancelled();
                response.send();
            }

            public void onError(FacebookException e) {
                response.sendError(e.getMessage());
            }
        });
        dialog.show(contentBuilder.build());
    }
}
