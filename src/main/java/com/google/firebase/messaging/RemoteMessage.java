package com.google.firebase.messaging;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.firebase.iid.FirebaseInstanceId;
import java.util.Map;
import java.util.Map.Entry;

public final class RemoteMessage extends zza {
    public static final Creator<RemoteMessage> CREATOR = new zze();
    private Map<String, String> zzacc;
    Bundle zzaic;
    private Notification zzclL;

    public static class Builder {
        private final Map<String, String> zzacc = new ArrayMap();
        private final Bundle zzaic = new Bundle();

        public Builder(String str) {
            if (TextUtils.isEmpty(str)) {
                String str2 = "Invalid to: ";
                String valueOf = String.valueOf(str);
                throw new IllegalArgumentException(valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2));
            }
            this.zzaic.putString("google.to", str);
        }

        public Builder addData(String str, String str2) {
            this.zzacc.put(str, str2);
            return this;
        }

        public RemoteMessage build() {
            Bundle bundle = new Bundle();
            for (Entry entry : this.zzacc.entrySet()) {
                bundle.putString((String) entry.getKey(), (String) entry.getValue());
            }
            bundle.putAll(this.zzaic);
            String token = FirebaseInstanceId.getInstance().getToken();
            if (token != null) {
                this.zzaic.putString("from", token);
            } else {
                this.zzaic.remove("from");
            }
            return new RemoteMessage(bundle);
        }

        public Builder clearData() {
            this.zzacc.clear();
            return this;
        }

        public Builder setCollapseKey(String str) {
            this.zzaic.putString("collapse_key", str);
            return this;
        }

        public Builder setData(Map<String, String> map) {
            this.zzacc.clear();
            this.zzacc.putAll(map);
            return this;
        }

        public Builder setMessageId(String str) {
            this.zzaic.putString("google.message_id", str);
            return this;
        }

        public Builder setMessageType(String str) {
            this.zzaic.putString("message_type", str);
            return this;
        }

        public Builder setTtl(int i) {
            this.zzaic.putString("google.ttl", String.valueOf(i));
            return this;
        }
    }

    public static class Notification {
        private final String mTag;
        private final String zzGr;
        private final String zzaQM;
        private final String zzamJ;
        private final String zzclM;
        private final String[] zzclN;
        private final String zzclO;
        private final String[] zzclP;
        private final String zzclQ;
        private final String zzclR;
        private final String zzclS;
        private final Uri zzclT;

        private Notification(Bundle bundle) {
            this.zzamJ = zza.zzf(bundle, "gcm.n.title");
            this.zzclM = zza.zzi(bundle, "gcm.n.title");
            this.zzclN = zzk(bundle, "gcm.n.title");
            this.zzGr = zza.zzf(bundle, "gcm.n.body");
            this.zzclO = zza.zzi(bundle, "gcm.n.body");
            this.zzclP = zzk(bundle, "gcm.n.body");
            this.zzclQ = zza.zzf(bundle, "gcm.n.icon");
            this.zzclR = zza.zzU(bundle);
            this.mTag = zza.zzf(bundle, "gcm.n.tag");
            this.zzaQM = zza.zzf(bundle, "gcm.n.color");
            this.zzclS = zza.zzf(bundle, "gcm.n.click_action");
            this.zzclT = zza.zzT(bundle);
        }

        private String[] zzk(Bundle bundle, String str) {
            Object[] zzj = zza.zzj(bundle, str);
            if (zzj == null) {
                return null;
            }
            String[] strArr = new String[zzj.length];
            for (int i = 0; i < zzj.length; i++) {
                strArr[i] = String.valueOf(zzj[i]);
            }
            return strArr;
        }

        @Nullable
        public String getBody() {
            return this.zzGr;
        }

        @Nullable
        public String[] getBodyLocalizationArgs() {
            return this.zzclP;
        }

        @Nullable
        public String getBodyLocalizationKey() {
            return this.zzclO;
        }

        @Nullable
        public String getClickAction() {
            return this.zzclS;
        }

        @Nullable
        public String getColor() {
            return this.zzaQM;
        }

        @Nullable
        public String getIcon() {
            return this.zzclQ;
        }

        @Nullable
        public Uri getLink() {
            return this.zzclT;
        }

        @Nullable
        public String getSound() {
            return this.zzclR;
        }

        @Nullable
        public String getTag() {
            return this.mTag;
        }

        @Nullable
        public String getTitle() {
            return this.zzamJ;
        }

        @Nullable
        public String[] getTitleLocalizationArgs() {
            return this.zzclN;
        }

        @Nullable
        public String getTitleLocalizationKey() {
            return this.zzclM;
        }
    }

    RemoteMessage(Bundle bundle) {
        this.zzaic = bundle;
    }

    public String getCollapseKey() {
        return this.zzaic.getString("collapse_key");
    }

    public Map<String, String> getData() {
        if (this.zzacc == null) {
            this.zzacc = new ArrayMap();
            for (String str : this.zzaic.keySet()) {
                Object obj = this.zzaic.get(str);
                if (obj instanceof String) {
                    String str2 = (String) obj;
                    if (!(str.startsWith("google.") || str.startsWith("gcm.") || str.equals("from") || str.equals("message_type") || str.equals("collapse_key"))) {
                        this.zzacc.put(str, str2);
                    }
                }
            }
        }
        return this.zzacc;
    }

    public String getFrom() {
        return this.zzaic.getString("from");
    }

    public String getMessageId() {
        String string = this.zzaic.getString("google.message_id");
        return string == null ? this.zzaic.getString("message_id") : string;
    }

    public String getMessageType() {
        return this.zzaic.getString("message_type");
    }

    public Notification getNotification() {
        if (this.zzclL == null && zza.zzE(this.zzaic)) {
            this.zzclL = new Notification(this.zzaic);
        }
        return this.zzclL;
    }

    public long getSentTime() {
        Object obj = this.zzaic.get("google.sent_time");
        if (obj instanceof Long) {
            return ((Long) obj).longValue();
        }
        if (obj instanceof String) {
            try {
                return Long.parseLong((String) obj);
            } catch (NumberFormatException e) {
                String valueOf = String.valueOf(obj);
                Log.w("FirebaseMessaging", new StringBuilder(String.valueOf(valueOf).length() + 19).append("Invalid sent time: ").append(valueOf).toString());
            }
        }
        return 0;
    }

    public String getTo() {
        return this.zzaic.getString("google.to");
    }

    public int getTtl() {
        Object obj = this.zzaic.get("google.ttl");
        if (obj instanceof Integer) {
            return ((Integer) obj).intValue();
        }
        if (obj instanceof String) {
            try {
                return Integer.parseInt((String) obj);
            } catch (NumberFormatException e) {
                String valueOf = String.valueOf(obj);
                Log.w("FirebaseMessaging", new StringBuilder(String.valueOf(valueOf).length() + 13).append("Invalid TTL: ").append(valueOf).toString());
            }
        }
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        zze.zza(this, parcel, i);
    }

    void zzK(Intent intent) {
        intent.putExtras(this.zzaic);
    }
}
