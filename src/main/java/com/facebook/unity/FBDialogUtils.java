package com.facebook.unity;

import android.net.Uri;
import android.os.Bundle;
import com.facebook.share.internal.ShareFeedContent;
import com.facebook.share.model.ShareLinkContent.Builder;
import com.facebook.share.widget.ShareDialog.Mode;

class FBDialogUtils {
    FBDialogUtils() {
    }

    public static Builder createShareContentBuilder(Bundle params) {
        Builder builder = new Builder();
        if (params.containsKey("content_title")) {
            builder.setContentTitle(params.getString("content_title"));
        }
        if (params.containsKey("content_description")) {
            builder.setContentDescription(params.getString("content_description"));
        }
        if (params.containsKey("content_url")) {
            builder.setContentUrl(Uri.parse(params.getString("content_url")));
        }
        if (params.containsKey("photo_url")) {
            builder.setImageUrl(Uri.parse(params.getString("photo_url")));
        }
        return builder;
    }

    public static ShareFeedContent.Builder createFeedContentBuilder(Bundle params) {
        ShareFeedContent.Builder builder = new ShareFeedContent.Builder();
        if (params.containsKey("toId")) {
            builder.setToId(params.getString("toId"));
        }
        if (params.containsKey("link")) {
            builder.setLink(params.getString("link"));
        }
        if (params.containsKey("linkName")) {
            builder.setLinkName(params.getString("linkName"));
        }
        if (params.containsKey("linkCaption")) {
            builder.setLinkCaption(params.getString("linkCaption"));
        }
        if (params.containsKey("linkDescription")) {
            builder.setLinkDescription(params.getString("linkDescription"));
        }
        if (params.containsKey("picture")) {
            builder.setPicture(params.getString("picture"));
        }
        if (params.containsKey("mediaSource")) {
            builder.setMediaSource(params.getString("mediaSource"));
        }
        return builder;
    }

    public static Mode intToMode(int mode) {
        switch (mode) {
            case 0:
                return Mode.AUTOMATIC;
            case 1:
                return Mode.NATIVE;
            case 2:
                return Mode.WEB;
            case 3:
                return Mode.FEED;
            default:
                return null;
        }
    }
}
