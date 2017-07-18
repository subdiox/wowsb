package com.customsdk.wargaming.util;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.IntentSender.SendIntentException;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;

public class BaseGameUtils {
    public static void showAlert(Activity activity, String message) {
        new Builder(activity).setMessage(message).setNeutralButton(17039370, null).create().show();
    }

    public static boolean resolveConnectionFailure(Activity activity, GoogleApiClient client, ConnectionResult result, int requestCode, String fallbackErrorMessage) {
        if (result.hasResolution()) {
            try {
                result.startResolutionForResult(activity, requestCode);
                return true;
            } catch (SendIntentException e) {
                client.connect();
                return false;
            }
        }
        Dialog dialog = GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), activity, requestCode);
        if (dialog != null) {
            dialog.show();
            return false;
        }
        showAlert(activity, fallbackErrorMessage);
        return false;
    }

    public static boolean verifySampleSetup(Activity activity, int... resIds) {
        StringBuilder problems = new StringBuilder();
        boolean problemFound = false;
        problems.append("The following set up problems were found:\n\n");
        if (activity.getPackageName().startsWith("com.google.example.games")) {
            problemFound = true;
            problems.append("- Package name cannot be com.google.*. You need to change the sample's package name to your own package.").append("\n");
        }
        for (int i : resIds) {
            if (activity.getString(i).toLowerCase().contains("replaceme")) {
                problemFound = true;
                problems.append("- You must replace all placeholder IDs in the ids.xml file by your project's IDs.").append("\n");
                break;
            }
        }
        if (!problemFound) {
            return true;
        }
        problems.append("\n\nThese problems may prevent the app from working properly.");
        showAlert(activity, problems.toString());
        return false;
    }

    public static void showActivityResultError(Activity activity, int requestCode, int actResp, int errorDescription) {
        if (activity == null) {
            Log.e("BaseGameUtils", "*** No Activity. Can't show failure dialog!");
            return;
        }
        Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity), activity, requestCode, null);
        if (errorDialog == null) {
            Log.e("BaseGamesUtils", "No standard error dialog available. Making fallback dialog.");
            errorDialog = makeSimpleDialog(activity, activity.getString(errorDescription));
        }
        errorDialog.show();
    }

    public static Dialog makeSimpleDialog(Activity activity, String text) {
        return new Builder(activity).setMessage(text).setNeutralButton(17039370, null).create();
    }

    public static Dialog makeSimpleDialog(Activity activity, String title, String text) {
        return new Builder(activity).setTitle(title).setMessage(text).setNeutralButton(17039370, null).create();
    }
}
