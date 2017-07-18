package com.google.android.gms.auth.api.credentials;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.annotation.NonNull;
import android.support.v4.media.TransportMediator;
import android.text.TextUtils;
import com.google.android.gms.common.internal.ReflectedParcelable;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

public final class PasswordSpecification extends com.google.android.gms.common.internal.safeparcel.zza implements ReflectedParcelable {
    public static final Creator<PasswordSpecification> CREATOR = new zzf();
    public static final PasswordSpecification zzajC = new zza().zzm(12, 16).zzcs("abcdefghijkmnopqrstxyzABCDEFGHJKLMNPQRSTXY3456789").zzf("abcdefghijkmnopqrstxyz", 1).zzf("ABCDEFGHJKLMNPQRSTXY", 1).zzf("3456789", 1).zzqZ();
    public static final PasswordSpecification zzajD = new zza().zzm(12, 16).zzcs("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890").zzf("abcdefghijklmnopqrstuvwxyz", 1).zzf("ABCDEFGHIJKLMNOPQRSTUVWXYZ", 1).zzf("1234567890", 1).zzqZ();
    final int zzaiI;
    final String zzajE;
    final List<String> zzajF;
    final List<Integer> zzajG;
    final int zzajH;
    final int zzajI;
    private final int[] zzajJ = zzqY();
    private final Random zzzW = new SecureRandom();

    public static class zza {
        private final List<String> zzajF = new ArrayList();
        private final List<Integer> zzajG = new ArrayList();
        private int zzajH = 12;
        private int zzajI = 16;
        private final TreeSet<Character> zzajK = new TreeSet();

        private void zzra() {
            int i = 0;
            for (Integer intValue : this.zzajG) {
                i = intValue.intValue() + i;
            }
            if (i > this.zzajI) {
                throw new zzb("required character count cannot be greater than the max password size");
            }
        }

        private void zzrb() {
            boolean[] zArr = new boolean[95];
            for (String toCharArray : this.zzajF) {
                for (char c : toCharArray.toCharArray()) {
                    if (zArr[c - 32]) {
                        throw new zzb("character " + c + " occurs in more than one required character set");
                    }
                    zArr[c - 32] = true;
                }
            }
        }

        private TreeSet<Character> zzw(String str, String str2) {
            if (TextUtils.isEmpty(str)) {
                throw new zzb(String.valueOf(str2).concat(" cannot be null or empty"));
            }
            TreeSet<Character> treeSet = new TreeSet();
            for (char c : str.toCharArray()) {
                if (PasswordSpecification.zzc(c, 32, TransportMediator.KEYCODE_MEDIA_PLAY)) {
                    throw new zzb(String.valueOf(str2).concat(" must only contain ASCII printable characters"));
                }
                treeSet.add(Character.valueOf(c));
            }
            return treeSet;
        }

        public zza zzcs(@NonNull String str) {
            this.zzajK.addAll(zzw(str, "allowedChars"));
            return this;
        }

        public zza zzf(@NonNull String str, int i) {
            if (i < 1) {
                throw new zzb("count must be at least 1");
            }
            this.zzajF.add(PasswordSpecification.zzb(zzw(str, "requiredChars")));
            this.zzajG.add(Integer.valueOf(i));
            return this;
        }

        public zza zzm(int i, int i2) {
            if (i < 1) {
                throw new zzb("minimumSize must be at least 1");
            } else if (i > i2) {
                throw new zzb("maximumSize must be greater than or equal to minimumSize");
            } else {
                this.zzajH = i;
                this.zzajI = i2;
                return this;
            }
        }

        public PasswordSpecification zzqZ() {
            if (this.zzajK.isEmpty()) {
                throw new zzb("no allowed characters specified");
            }
            zzra();
            zzrb();
            return new PasswordSpecification(1, PasswordSpecification.zzb(this.zzajK), this.zzajF, this.zzajG, this.zzajH, this.zzajI);
        }
    }

    public static class zzb extends Error {
        public zzb(String str) {
            super(str);
        }
    }

    PasswordSpecification(int i, String str, List<String> list, List<Integer> list2, int i2, int i3) {
        this.zzaiI = i;
        this.zzajE = str;
        this.zzajF = Collections.unmodifiableList(list);
        this.zzajG = Collections.unmodifiableList(list2);
        this.zzajH = i2;
        this.zzajI = i3;
    }

    private int zza(char c) {
        return c - 32;
    }

    private static String zzb(Collection<Character> collection) {
        char[] cArr = new char[collection.size()];
        int i = 0;
        for (Character charValue : collection) {
            int i2 = i + 1;
            cArr[i] = charValue.charValue();
            i = i2;
        }
        return new String(cArr);
    }

    private static boolean zzc(int i, int i2, int i3) {
        return i < i2 || i > i3;
    }

    private int[] zzqY() {
        int[] iArr = new int[95];
        Arrays.fill(iArr, -1);
        int i = 0;
        for (String toCharArray : this.zzajF) {
            for (char zza : toCharArray.toCharArray()) {
                iArr[zza(zza)] = i;
            }
            i++;
        }
        return iArr;
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzf.zza(this, parcel, i);
    }
}
