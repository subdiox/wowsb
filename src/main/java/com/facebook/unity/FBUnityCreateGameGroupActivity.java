package com.facebook.unity;

import android.app.Activity;
import android.os.Bundle;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.internal.ShareConstants;
import com.facebook.share.model.AppGroupCreationContent.AppGroupPrivacy;
import com.facebook.share.model.AppGroupCreationContent.Builder;
import com.facebook.share.widget.CreateAppGroupDialog;
import com.facebook.share.widget.CreateAppGroupDialog.Result;
import java.util.Locale;

public class FBUnityCreateGameGroupActivity extends BaseActivity {
    public static String CREATE_GAME_GROUP_PARAMS = "create_game_group_params";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Builder contentBuilder = new Builder();
        Bundle params = getIntent().getBundleExtra(CREATE_GAME_GROUP_PARAMS);
        final UnityMessage response = new UnityMessage("OnGroupCreateComplete");
        if (params.containsKey(Constants.CALLBACK_ID_KEY)) {
            response.put(Constants.CALLBACK_ID_KEY, params.getString(Constants.CALLBACK_ID_KEY));
        }
        if (params.containsKey("name")) {
            contentBuilder.setName(params.getString("name"));
        }
        if (params.containsKey("description")) {
            contentBuilder.setDescription(params.getString("name"));
        }
        if (params.containsKey(ShareConstants.WEB_DIALOG_PARAM_PRIVACY)) {
            String privacyStr = params.getString(ShareConstants.WEB_DIALOG_PARAM_PRIVACY);
            AppGroupPrivacy privacy = AppGroupPrivacy.Closed;
            if (privacyStr.equalsIgnoreCase("closed")) {
                privacy = AppGroupPrivacy.Closed;
            } else if (privacyStr.equalsIgnoreCase("open")) {
                privacy = AppGroupPrivacy.Open;
            } else {
                response.sendError(String.format(Locale.ROOT, "Unknown privacy setting for group creation: %s", new Object[]{privacyStr}));
                finish();
            }
            contentBuilder.setAppGroupPrivacy(privacy);
        }
        CreateAppGroupDialog dialog = new CreateAppGroupDialog((Activity) this);
        dialog.registerCallback(this.mCallbackManager, new FacebookCallback<Result>() {
            public void onSuccess(Result result) {
                response.put("id", result.getId());
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
        dialog.show(contentBuilder.build());
    }
}
