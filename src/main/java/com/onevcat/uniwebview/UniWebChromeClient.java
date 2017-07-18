package com.onevcat.uniwebview;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient.FileChooserParams;
import android.webkit.WebView;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UniWebChromeClient extends VideoEnabledWebChromeClient {
    public UniWebChromeClient(View activityNonVideoView, ViewGroup activityVideoView, View loadingView, VideoEnabledWebView webView) {
        super(activityNonVideoView, activityVideoView, loadingView, webView);
    }

    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
        if (AndroidPlugin._uploadMessages != null) {
            AndroidPlugin._uploadMessages.onReceiveValue(null);
        }
        AndroidPlugin.setUploadMessage(uploadMsg);
        startFileChooserActivity();
    }

    public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
        if (AndroidPlugin._uploadMessages != null) {
            AndroidPlugin._uploadMessages.onReceiveValue(null);
        }
        AndroidPlugin.setUploadMessage(uploadMsg);
        startFileChooserActivity();
    }

    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
        if (AndroidPlugin._uploadMessages != null) {
            AndroidPlugin._uploadMessages.onReceiveValue(null);
        }
        AndroidPlugin.setUploadMessage(uploadMsg);
        startFileChooserActivity();
    }

    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
        if (AndroidPlugin._uploadCallback != null) {
            AndroidPlugin._uploadCallback.onReceiveValue(null);
        }
        AndroidPlugin.setUploadCallback(filePathCallback);
        startFileChooserActivity();
        return true;
    }

    private void startFileChooserActivity() {
        Activity activity = AndroidPlugin.getUnityActivity_();
        Intent takePictureIntent = new Intent("android.media.action.IMAGE_CAPTURE");
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            File file = null;
            try {
                file = createImageFile();
                takePictureIntent.putExtra("PhotoPath", AndroidPlugin._cameraPhotoPath);
            } catch (IOException ex) {
                Log.e("UniWebView", "Unable to create Image File", ex);
            }
            if (file != null) {
                AndroidPlugin._cameraPhotoPath = "file:" + file.getAbsolutePath();
                takePictureIntent.putExtra("output", Uri.fromFile(file));
            } else {
                takePictureIntent = null;
            }
        }
        Intent contentSelectionIntent = new Intent("android.intent.action.GET_CONTENT");
        contentSelectionIntent.addCategory("android.intent.category.OPENABLE");
        contentSelectionIntent.setType("image/*");
        Intent[] intentArray = takePictureIntent != null ? new Intent[]{takePictureIntent} : new Intent[0];
        Intent chooserIntent = new Intent("android.intent.action.CHOOSER");
        chooserIntent.putExtra("android.intent.extra.INTENT", contentSelectionIntent);
        chooserIntent.putExtra("android.intent.extra.TITLE", "Image Chooser");
        chooserIntent.putExtra("android.intent.extra.INITIAL_INTENTS", intentArray);
        activity.startActivityForResult(chooserIntent, AndroidPlugin.FILECHOOSER_RESULTCODE);
    }

    public void onGeolocationPermissionsShowPrompt(String origin, Callback callback) {
        callback.invoke(origin, true, false);
    }

    private File createImageFile() throws IOException {
        return File.createTempFile("JPEG_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + "_", ".jpg", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));
    }
}
