package com.facebook.unity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.internal.ShareConstants;
import com.facebook.share.model.GameRequestContent;
import com.facebook.share.model.GameRequestContent.ActionType;
import com.facebook.share.model.GameRequestContent.Builder;
import com.facebook.share.model.GameRequestContent.Filters;
import com.facebook.share.widget.GameRequestDialog;
import com.facebook.share.widget.GameRequestDialog.Result;
import java.util.Arrays;
import java.util.Locale;

public class FBUnityGameRequestActivity extends BaseActivity {
    public static final String GAME_REQUEST_PARAMS = "game_request_params";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle params = getIntent().getBundleExtra(GAME_REQUEST_PARAMS);
        final UnityMessage response = new UnityMessage("OnAppRequestsComplete");
        if (params.containsKey(Constants.CALLBACK_ID_KEY)) {
            response.put(Constants.CALLBACK_ID_KEY, params.getString(Constants.CALLBACK_ID_KEY));
        }
        Builder contentBuilder = new Builder();
        if (params.containsKey(ShareConstants.WEB_DIALOG_PARAM_MESSAGE)) {
            contentBuilder.setMessage(params.getString(ShareConstants.WEB_DIALOG_PARAM_MESSAGE));
        }
        if (params.containsKey(ShareConstants.WEB_DIALOG_PARAM_ACTION_TYPE)) {
            String actionTypeStr = params.getString(ShareConstants.WEB_DIALOG_PARAM_ACTION_TYPE);
            try {
                contentBuilder.setActionType(ActionType.valueOf(actionTypeStr));
            } catch (IllegalArgumentException e) {
                response.sendError("Unknown action type: " + actionTypeStr);
                finish();
                return;
            }
        }
        if (params.containsKey("object_id")) {
            contentBuilder.setObjectId(params.getString("object_id"));
        }
        if (params.containsKey("to")) {
            contentBuilder.setRecipients(Arrays.asList(params.getString("to").split(",")));
        }
        if (params.containsKey(ShareConstants.WEB_DIALOG_PARAM_FILTERS)) {
            String filtersStr = params.getString(ShareConstants.WEB_DIALOG_PARAM_FILTERS).toUpperCase(Locale.ROOT);
            try {
                contentBuilder.setFilters(Filters.valueOf(filtersStr));
            } catch (IllegalArgumentException e2) {
                response.sendError("Unsupported filter type: " + filtersStr);
                finish();
                return;
            }
        }
        if (params.containsKey(ShareConstants.WEB_DIALOG_PARAM_DATA)) {
            contentBuilder.setData(params.getString(ShareConstants.WEB_DIALOG_PARAM_DATA));
        }
        if (params.containsKey(ShareConstants.WEB_DIALOG_PARAM_TITLE)) {
            contentBuilder.setTitle(params.getString(ShareConstants.WEB_DIALOG_PARAM_TITLE));
        }
        GameRequestContent content = contentBuilder.build();
        GameRequestDialog requestDialog = new GameRequestDialog((Activity) this);
        requestDialog.registerCallback(this.mCallbackManager, new FacebookCallback<Result>() {
            public void onSuccess(Result result) {
                response.put(ShareConstants.WEB_DIALOG_RESULT_PARAM_REQUEST_ID, result.getRequestId());
                response.put("to", TextUtils.join(",", result.getRequestRecipients()));
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
        try {
            requestDialog.show(content);
        } catch (IllegalArgumentException exception) {
            response.sendError("Unexpected exception encountered: " + exception.toString());
            finish();
        }
    }
}
