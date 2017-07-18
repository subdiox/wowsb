package com.facebook.unity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer.Result;
import com.facebook.share.model.ShareContent;
import com.facebook.share.widget.ShareDialog;
import com.facebook.share.widget.ShareDialog.Mode;
import java.util.Locale;

public class FBUnityDialogsActivity extends BaseActivity {
    public static final String DIALOG_TYPE = "dialog_type";
    public static final String FEED_DIALOG_PARAMS = "feed_dialog_params";
    public static final String SHARE_DIALOG_PARAMS = "share_dialog_params";
    private static String TAG = FBUnityDialogsActivity.class.getName();

    protected void onCreate(Bundle savedInstanceState) {
        Bundle params;
        ShareContent shareContent;
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent.hasExtra(SHARE_DIALOG_PARAMS)) {
            params = intent.getBundleExtra(SHARE_DIALOG_PARAMS);
            shareContent = FBDialogUtils.createShareContentBuilder(params).build();
        } else if (intent.hasExtra(FEED_DIALOG_PARAMS)) {
            params = intent.getBundleExtra(FEED_DIALOG_PARAMS);
            shareContent = FBDialogUtils.createFeedContentBuilder(params).build();
        } else {
            Log.e(TAG, String.format(Locale.ROOT, "Failed to find extra %s or %s", new Object[]{SHARE_DIALOG_PARAMS, FEED_DIALOG_PARAMS}));
            finish();
            return;
        }
        ShareDialog dialog = new ShareDialog((Activity) this);
        final UnityMessage response = new UnityMessage("OnShareLinkComplete");
        String callbackID = params.getString(Constants.CALLBACK_ID_KEY);
        if (callbackID != null) {
            response.put(Constants.CALLBACK_ID_KEY, callbackID);
        }
        dialog.registerCallback(this.mCallbackManager, new FacebookCallback<Result>() {
            public void onSuccess(Result result) {
                if (result.getPostId() != null) {
                    response.putID(result.getPostId());
                }
                response.put("posted", Boolean.valueOf(true));
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
        dialog.show(shareContent, (Mode) getIntent().getSerializableExtra(DIALOG_TYPE));
    }
}
