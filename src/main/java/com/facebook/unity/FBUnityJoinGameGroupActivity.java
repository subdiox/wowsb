package com.facebook.unity;

import android.app.Activity;
import android.os.Bundle;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.internal.NativeProtocol;
import com.facebook.share.widget.JoinAppGroupDialog;
import com.facebook.share.widget.JoinAppGroupDialog.Result;

public class FBUnityJoinGameGroupActivity extends BaseActivity {
    public static String JOIN_GAME_GROUP_PARAMS = "join_game_group_params";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle params = getIntent().getBundleExtra(JOIN_GAME_GROUP_PARAMS);
        final UnityMessage response = new UnityMessage("OnJoinGroupComplete");
        if (params.containsKey(Constants.CALLBACK_ID_KEY)) {
            response.put(Constants.CALLBACK_ID_KEY, params.getString(Constants.CALLBACK_ID_KEY));
        }
        String groupId = "";
        if (params.containsKey("id")) {
            groupId = params.getString("id");
        }
        JoinAppGroupDialog dialog = new JoinAppGroupDialog((Activity) this);
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
                response.sendError(e.getLocalizedMessage());
            }
        });
        dialog.show(groupId);
    }
}
