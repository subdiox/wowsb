package com.facebook.share.model;

import android.os.Parcel;
import android.os.Parcelable.Creator;

public class ShareHashtag implements ShareModel {
    public static final Creator<ShareHashtag> CREATOR = new Creator<ShareHashtag>() {
        public ShareHashtag createFromParcel(Parcel in) {
            return new ShareHashtag(in);
        }

        public ShareHashtag[] newArray(int size) {
            return new ShareHashtag[size];
        }
    };
    private final String hashtag;

    public static class Builder implements ShareModelBuilder<ShareHashtag, Builder> {
        private String hashtag;

        public Builder setHashtag(String hashtag) {
            this.hashtag = hashtag;
            return this;
        }

        public String getHashtag() {
            return this.hashtag;
        }

        public Builder readFrom(ShareHashtag model) {
            return model == null ? this : setHashtag(model.getHashtag());
        }

        Builder readFrom(Parcel parcel) {
            return readFrom((ShareHashtag) parcel.readParcelable(ShareHashtag.class.getClassLoader()));
        }

        public ShareHashtag build() {
            return new ShareHashtag();
        }
    }

    private ShareHashtag(Builder builder) {
        this.hashtag = builder.hashtag;
    }

    ShareHashtag(Parcel in) {
        this.hashtag = in.readString();
    }

    public String getHashtag() {
        return this.hashtag;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.hashtag);
    }
}
