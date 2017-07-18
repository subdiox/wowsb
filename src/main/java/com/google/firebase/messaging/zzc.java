package com.google.firebase.messaging;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.internal.zzbxs;
import com.google.android.gms.internal.zzbxz.zza;
import com.google.android.gms.internal.zzbxz.zzb;
import com.google.android.gms.measurement.AppMeasurement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class zzc {
    @Nullable
    static zzb zzU(@NonNull byte[] bArr) {
        try {
            return zzb.zzak(bArr);
        } catch (zzbxs e) {
            return null;
        }
    }

    static int zza(@NonNull zzb com_google_android_gms_internal_zzbxz_zzb, int i) {
        return com_google_android_gms_internal_zzbxz_zzb.zzcwa != 0 ? com_google_android_gms_internal_zzbxz_zzb.zzcwa : i == 0 ? 1 : i;
    }

    static Bundle zza(@NonNull zzb com_google_android_gms_internal_zzbxz_zzb) {
        return zzam(com_google_android_gms_internal_zzbxz_zzb.zzcvP, com_google_android_gms_internal_zzbxz_zzb.zzcvQ);
    }

    @Nullable
    static Object zza(@NonNull zzb com_google_android_gms_internal_zzbxz_zzb, @NonNull String str, @NonNull zzb com_google_firebase_messaging_zzb) {
        Object newInstance;
        Throwable e;
        Object obj = null;
        try {
            Class cls = Class.forName("com.google.android.gms.measurement.AppMeasurement$ConditionalUserProperty");
            Bundle zza = zza(com_google_android_gms_internal_zzbxz_zzb);
            newInstance = cls.getConstructor(new Class[0]).newInstance(new Object[0]);
            try {
                cls.getField("mOrigin").set(newInstance, str);
                cls.getField("mCreationTimestamp").set(newInstance, Long.valueOf(com_google_android_gms_internal_zzbxz_zzb.zzcvR));
                cls.getField("mName").set(newInstance, com_google_android_gms_internal_zzbxz_zzb.zzcvP);
                cls.getField("mValue").set(newInstance, com_google_android_gms_internal_zzbxz_zzb.zzcvQ);
                if (!TextUtils.isEmpty(com_google_android_gms_internal_zzbxz_zzb.zzcvS)) {
                    obj = com_google_android_gms_internal_zzbxz_zzb.zzcvS;
                }
                cls.getField("mTriggerEventName").set(newInstance, obj);
                cls.getField("mTimedOutEventName").set(newInstance, zzd(com_google_android_gms_internal_zzbxz_zzb, com_google_firebase_messaging_zzb));
                cls.getField("mTimedOutEventParams").set(newInstance, zza);
                cls.getField("mTriggerTimeout").set(newInstance, Integer.valueOf(com_google_android_gms_internal_zzbxz_zzb.zzcvT));
                cls.getField("mTriggeredEventName").set(newInstance, zzb(com_google_android_gms_internal_zzbxz_zzb, com_google_firebase_messaging_zzb));
                cls.getField("mTriggeredEventParams").set(newInstance, zza);
                cls.getField("mTimeToLive").set(newInstance, Integer.valueOf(com_google_android_gms_internal_zzbxz_zzb.zzcvU));
                cls.getField("mExpiredEventName").set(newInstance, zze(com_google_android_gms_internal_zzbxz_zzb, com_google_firebase_messaging_zzb));
                cls.getField("mExpiredEventParams").set(newInstance, zza);
            } catch (ClassNotFoundException e2) {
                e = e2;
                Log.e("FirebaseAbtUtil", "Could not complete the operation due to an internal error.", e);
                return newInstance;
            } catch (NoSuchMethodException e3) {
                e = e3;
                Log.e("FirebaseAbtUtil", "Could not complete the operation due to an internal error.", e);
                return newInstance;
            } catch (IllegalAccessException e4) {
                e = e4;
                Log.e("FirebaseAbtUtil", "Could not complete the operation due to an internal error.", e);
                return newInstance;
            } catch (InvocationTargetException e5) {
                e = e5;
                Log.e("FirebaseAbtUtil", "Could not complete the operation due to an internal error.", e);
                return newInstance;
            } catch (NoSuchFieldException e6) {
                e = e6;
                Log.e("FirebaseAbtUtil", "Could not complete the operation due to an internal error.", e);
                return newInstance;
            } catch (InstantiationException e7) {
                e = e7;
                Log.e("FirebaseAbtUtil", "Could not complete the operation due to an internal error.", e);
                return newInstance;
            }
        } catch (ClassNotFoundException e8) {
            e = e8;
            newInstance = null;
            Log.e("FirebaseAbtUtil", "Could not complete the operation due to an internal error.", e);
            return newInstance;
        } catch (NoSuchMethodException e9) {
            e = e9;
            newInstance = null;
            Log.e("FirebaseAbtUtil", "Could not complete the operation due to an internal error.", e);
            return newInstance;
        } catch (IllegalAccessException e10) {
            e = e10;
            newInstance = null;
            Log.e("FirebaseAbtUtil", "Could not complete the operation due to an internal error.", e);
            return newInstance;
        } catch (InvocationTargetException e11) {
            e = e11;
            newInstance = null;
            Log.e("FirebaseAbtUtil", "Could not complete the operation due to an internal error.", e);
            return newInstance;
        } catch (NoSuchFieldException e12) {
            e = e12;
            newInstance = null;
            Log.e("FirebaseAbtUtil", "Could not complete the operation due to an internal error.", e);
            return newInstance;
        } catch (InstantiationException e13) {
            e = e13;
            newInstance = null;
            Log.e("FirebaseAbtUtil", "Could not complete the operation due to an internal error.", e);
            return newInstance;
        }
        return newInstance;
    }

    static String zza(@NonNull zzb com_google_android_gms_internal_zzbxz_zzb, @NonNull zzb com_google_firebase_messaging_zzb) {
        return !TextUtils.isEmpty(com_google_android_gms_internal_zzbxz_zzb.zzcvV) ? com_google_android_gms_internal_zzbxz_zzb.zzcvV : com_google_firebase_messaging_zzb.zzUZ();
    }

    public static void zza(@NonNull Context context, @NonNull String str, @NonNull String str2, @NonNull String str3, @NonNull String str4) {
        Throwable e;
        if (Log.isLoggable("FirebaseAbtUtil", 2)) {
            String str5 = "FirebaseAbtUtil";
            String str6 = "_CE(experimentId) called by ";
            String valueOf = String.valueOf(str);
            Log.v(str5, valueOf.length() != 0 ? str6.concat(valueOf) : new String(str6));
        }
        if (zzco(context)) {
            AppMeasurement zzbj = zzbj(context);
            try {
                Method declaredMethod = AppMeasurement.class.getDeclaredMethod("clearConditionalUserProperty", new Class[]{String.class, String.class, Bundle.class});
                declaredMethod.setAccessible(true);
                if (Log.isLoggable("FirebaseAbtUtil", 2)) {
                    Log.v("FirebaseAbtUtil", new StringBuilder((String.valueOf(str2).length() + 17) + String.valueOf(str3).length()).append("Clearing _E: [").append(str2).append(", ").append(str3).append("]").toString());
                }
                declaredMethod.invoke(zzbj, new Object[]{str2, str4, zzam(str2, str3)});
            } catch (NoSuchMethodException e2) {
                e = e2;
                Log.e("FirebaseAbtUtil", "Could not complete the operation due to an internal error.", e);
            } catch (IllegalAccessException e3) {
                e = e3;
                Log.e("FirebaseAbtUtil", "Could not complete the operation due to an internal error.", e);
            } catch (InvocationTargetException e4) {
                e = e4;
                Log.e("FirebaseAbtUtil", "Could not complete the operation due to an internal error.", e);
            }
        }
    }

    public static void zza(@NonNull Context context, @NonNull String str, @NonNull byte[] bArr, @NonNull zzb com_google_firebase_messaging_zzb, int i) {
        Throwable e;
        if (Log.isLoggable("FirebaseAbtUtil", 2)) {
            String str2 = "FirebaseAbtUtil";
            String str3 = "_SE called by ";
            String valueOf = String.valueOf(str);
            Log.v(str2, valueOf.length() != 0 ? str3.concat(valueOf) : new String(str3));
        }
        if (zzco(context)) {
            AppMeasurement zzbj = zzbj(context);
            zzb zzU = zzU(bArr);
            if (zzU != null) {
                try {
                    Class.forName("com.google.android.gms.measurement.AppMeasurement$ConditionalUserProperty");
                    Object obj = null;
                    for (Object next : zzb(zzbj, str)) {
                        Object next2;
                        String zzab = zzab(next2);
                        String zzac = zzac(next2);
                        long zzaI = zzaI(next2);
                        if (zzU.zzcvP.equals(zzab) && zzU.zzcvQ.equals(zzac)) {
                            if (Log.isLoggable("FirebaseAbtUtil", 2)) {
                                Log.v("FirebaseAbtUtil", new StringBuilder((String.valueOf(zzab).length() + 23) + String.valueOf(zzac).length()).append("_E is already set. [").append(zzab).append(", ").append(zzac).append("]").toString());
                            }
                            obj = 1;
                        } else {
                            next2 = null;
                            zza[] com_google_android_gms_internal_zzbxz_zzaArr = zzU.zzcwb;
                            int length = com_google_android_gms_internal_zzbxz_zzaArr.length;
                            int i2 = 0;
                            while (i2 < length) {
                                if (com_google_android_gms_internal_zzbxz_zzaArr[i2].zzcvP.equals(zzab)) {
                                    if (Log.isLoggable("FirebaseAbtUtil", 2)) {
                                        Log.v("FirebaseAbtUtil", new StringBuilder((String.valueOf(zzab).length() + 33) + String.valueOf(zzac).length()).append("_E is found in the _OE list. [").append(zzab).append(", ").append(zzac).append("]").toString());
                                    }
                                    next2 = 1;
                                    if (next2 != null) {
                                        continue;
                                    } else if (zzU.zzcvR > zzaI) {
                                        if (Log.isLoggable("FirebaseAbtUtil", 2)) {
                                            Log.v("FirebaseAbtUtil", new StringBuilder((String.valueOf(zzab).length() + 115) + String.valueOf(zzac).length()).append("Clearing _E as it was not in the _OE list, andits start time is older than the start time of the _E to be set. [").append(zzab).append(", ").append(zzac).append("]").toString());
                                        }
                                        zza(context, str, zzab, zzac, zzc(zzU, com_google_firebase_messaging_zzb));
                                    } else if (Log.isLoggable("FirebaseAbtUtil", 2)) {
                                        Log.v("FirebaseAbtUtil", new StringBuilder((String.valueOf(zzab).length() + 109) + String.valueOf(zzac).length()).append("_E was not found in the _OE list, but not clearing it as it has a new start time than the _E to be set.  [").append(zzab).append(", ").append(zzac).append("]").toString());
                                    }
                                } else {
                                    i2++;
                                }
                            }
                            if (next2 != null) {
                                continue;
                            } else if (zzU.zzcvR > zzaI) {
                                if (Log.isLoggable("FirebaseAbtUtil", 2)) {
                                    Log.v("FirebaseAbtUtil", new StringBuilder((String.valueOf(zzab).length() + 115) + String.valueOf(zzac).length()).append("Clearing _E as it was not in the _OE list, andits start time is older than the start time of the _E to be set. [").append(zzab).append(", ").append(zzac).append("]").toString());
                                }
                                zza(context, str, zzab, zzac, zzc(zzU, com_google_firebase_messaging_zzb));
                            } else if (Log.isLoggable("FirebaseAbtUtil", 2)) {
                                Log.v("FirebaseAbtUtil", new StringBuilder((String.valueOf(zzab).length() + 109) + String.valueOf(zzac).length()).append("_E was not found in the _OE list, but not clearing it as it has a new start time than the _E to be set.  [").append(zzab).append(", ").append(zzac).append("]").toString());
                            }
                        }
                    }
                    if (obj == null) {
                        zza(zzbj, context, str, zzU, com_google_firebase_messaging_zzb, i);
                        return;
                    } else if (Log.isLoggable("FirebaseAbtUtil", 2)) {
                        str2 = String.valueOf(zzU.zzcvP);
                        str3 = String.valueOf(zzU.zzcvQ);
                        Log.v("FirebaseAbtUtil", new StringBuilder((String.valueOf(str2).length() + 44) + String.valueOf(str3).length()).append("_E is already set. Not setting it again [").append(str2).append(", ").append(str3).append("]").toString());
                        return;
                    } else {
                        return;
                    }
                } catch (ClassNotFoundException e2) {
                    e = e2;
                } catch (IllegalAccessException e3) {
                    e = e3;
                } catch (NoSuchFieldException e4) {
                    e = e4;
                }
            } else if (Log.isLoggable("FirebaseAbtUtil", 2)) {
                Log.v("FirebaseAbtUtil", "_SE failed; either _P was not set, or we couldn't deserialize the _P.");
                return;
            } else {
                return;
            }
        }
        return;
        Log.e("FirebaseAbtUtil", "Could not complete the operation due to an internal error.", e);
    }

    static void zza(@NonNull AppMeasurement appMeasurement, @NonNull Context context, @NonNull String str, @NonNull zzb com_google_android_gms_internal_zzbxz_zzb, @NonNull zzb com_google_firebase_messaging_zzb, int i) {
        Throwable e;
        if (Log.isLoggable("FirebaseAbtUtil", 2)) {
            String valueOf = String.valueOf(com_google_android_gms_internal_zzbxz_zzb.zzcvP);
            String valueOf2 = String.valueOf(com_google_android_gms_internal_zzbxz_zzb.zzcvQ);
            Log.v("FirebaseAbtUtil", new StringBuilder((String.valueOf(valueOf).length() + 7) + String.valueOf(valueOf2).length()).append("_SEI: ").append(valueOf).append(" ").append(valueOf2).toString());
        }
        try {
            Object obj;
            Class.forName("com.google.android.gms.measurement.AppMeasurement$ConditionalUserProperty");
            List zzb = zzb(appMeasurement, str);
            if (zza(appMeasurement, str)) {
                if (zza(com_google_android_gms_internal_zzbxz_zzb, i) == 1) {
                    obj = zzb.get(0);
                    valueOf2 = zzab(obj);
                    valueOf = zzac(obj);
                    if (Log.isLoggable("FirebaseAbtUtil", 2)) {
                        Log.v("FirebaseAbtUtil", new StringBuilder(String.valueOf(valueOf2).length() + 38).append("Clearing _E due to overflow policy: [").append(valueOf2).append("]").toString());
                    }
                    zza(context, str, valueOf2, valueOf, zzc(com_google_android_gms_internal_zzbxz_zzb, com_google_firebase_messaging_zzb));
                } else if (Log.isLoggable("FirebaseAbtUtil", 2)) {
                    valueOf = String.valueOf(com_google_android_gms_internal_zzbxz_zzb.zzcvP);
                    valueOf2 = String.valueOf(com_google_android_gms_internal_zzbxz_zzb.zzcvQ);
                    Log.v("FirebaseAbtUtil", new StringBuilder((String.valueOf(valueOf).length() + 44) + String.valueOf(valueOf2).length()).append("_E won't be set due to overflow policy. [").append(valueOf).append(", ").append(valueOf2).append("]").toString());
                    return;
                } else {
                    return;
                }
            }
            for (Object obj2 : zzb) {
                valueOf2 = zzab(obj2);
                valueOf = zzac(obj2);
                if (valueOf2.equals(com_google_android_gms_internal_zzbxz_zzb.zzcvP) && !valueOf.equals(com_google_android_gms_internal_zzbxz_zzb.zzcvQ) && Log.isLoggable("FirebaseAbtUtil", 2)) {
                    Log.v("FirebaseAbtUtil", new StringBuilder((String.valueOf(valueOf2).length() + 77) + String.valueOf(valueOf).length()).append("Clearing _E, as only one _V of the same _E can be set atany given time: [").append(valueOf2).append(", ").append(valueOf).append("].").toString());
                    zza(context, str, valueOf2, valueOf, zzc(com_google_android_gms_internal_zzbxz_zzb, com_google_firebase_messaging_zzb));
                }
            }
            Object zza = zza(com_google_android_gms_internal_zzbxz_zzb, str, com_google_firebase_messaging_zzb);
            if (zza != null) {
                zza(appMeasurement, com_google_android_gms_internal_zzbxz_zzb, com_google_firebase_messaging_zzb, zza, str);
            } else if (Log.isLoggable("FirebaseAbtUtil", 2)) {
                valueOf = String.valueOf(com_google_android_gms_internal_zzbxz_zzb.zzcvP);
                valueOf2 = String.valueOf(com_google_android_gms_internal_zzbxz_zzb.zzcvQ);
                Log.v("FirebaseAbtUtil", new StringBuilder((String.valueOf(valueOf).length() + 42) + String.valueOf(valueOf2).length()).append("Could not create _CUP for: [").append(valueOf).append(", ").append(valueOf2).append("]. Skipping.").toString());
            }
        } catch (ClassNotFoundException e2) {
            e = e2;
            Log.e("FirebaseAbtUtil", "Could not complete the operation due to an internal error.", e);
        } catch (IllegalAccessException e3) {
            e = e3;
            Log.e("FirebaseAbtUtil", "Could not complete the operation due to an internal error.", e);
        } catch (NoSuchFieldException e4) {
            e = e4;
            Log.e("FirebaseAbtUtil", "Could not complete the operation due to an internal error.", e);
        }
    }

    static void zza(@NonNull AppMeasurement appMeasurement, @NonNull zzb com_google_android_gms_internal_zzbxz_zzb, @NonNull zzb com_google_firebase_messaging_zzb, @NonNull Object obj, @NonNull String str) {
        Throwable e;
        if (Log.isLoggable("FirebaseAbtUtil", 2)) {
            String valueOf = String.valueOf(com_google_android_gms_internal_zzbxz_zzb.zzcvP);
            String valueOf2 = String.valueOf(com_google_android_gms_internal_zzbxz_zzb.zzcvQ);
            String valueOf3 = String.valueOf(com_google_android_gms_internal_zzbxz_zzb.zzcvS);
            Log.v("FirebaseAbtUtil", new StringBuilder(((String.valueOf(valueOf).length() + 27) + String.valueOf(valueOf2).length()) + String.valueOf(valueOf3).length()).append("Setting _CUP for _E: [").append(valueOf).append(", ").append(valueOf2).append(", ").append(valueOf3).append("]").toString());
        }
        try {
            Method declaredMethod = AppMeasurement.class.getDeclaredMethod("setConditionalUserProperty", new Class[]{Class.forName("com.google.android.gms.measurement.AppMeasurement$ConditionalUserProperty")});
            declaredMethod.setAccessible(true);
            appMeasurement.logEventInternal(str, zza(com_google_android_gms_internal_zzbxz_zzb, com_google_firebase_messaging_zzb), zza(com_google_android_gms_internal_zzbxz_zzb));
            declaredMethod.invoke(appMeasurement, new Object[]{obj});
            return;
        } catch (ClassNotFoundException e2) {
            e = e2;
        } catch (NoSuchMethodException e3) {
            e = e3;
        } catch (IllegalAccessException e4) {
            e = e4;
        } catch (InvocationTargetException e5) {
            e = e5;
        }
        Log.e("FirebaseAbtUtil", "Could not complete the operation due to an internal error.", e);
    }

    static boolean zza(@NonNull AppMeasurement appMeasurement, @NonNull String str) {
        return zzb(appMeasurement, str).size() >= zzc(appMeasurement, str);
    }

    static long zzaI(@NonNull Object obj) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        return ((Long) Class.forName("com.google.android.gms.measurement.AppMeasurement$ConditionalUserProperty").getField("mCreationTimestamp").get(obj)).longValue();
    }

    static String zzab(@NonNull Object obj) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        return (String) Class.forName("com.google.android.gms.measurement.AppMeasurement$ConditionalUserProperty").getField("mName").get(obj);
    }

    static String zzac(@NonNull Object obj) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        return (String) Class.forName("com.google.android.gms.measurement.AppMeasurement$ConditionalUserProperty").getField("mValue").get(obj);
    }

    static Bundle zzam(@NonNull String str, @NonNull String str2) {
        Bundle bundle = new Bundle();
        bundle.putString(str, str2);
        return bundle;
    }

    static String zzb(@NonNull zzb com_google_android_gms_internal_zzbxz_zzb, @NonNull zzb com_google_firebase_messaging_zzb) {
        return !TextUtils.isEmpty(com_google_android_gms_internal_zzbxz_zzb.zzcvW) ? com_google_android_gms_internal_zzbxz_zzb.zzcvW : com_google_firebase_messaging_zzb.zzVa();
    }

    static List<Object> zzb(@NonNull AppMeasurement appMeasurement, @NonNull String str) {
        List<Object> list;
        Throwable e;
        Object obj;
        ArrayList arrayList = new ArrayList();
        try {
            Method declaredMethod = AppMeasurement.class.getDeclaredMethod("getConditionalUserProperties", new Class[]{String.class, String.class});
            declaredMethod.setAccessible(true);
            list = (List) declaredMethod.invoke(appMeasurement, new Object[]{str, ""});
        } catch (NoSuchMethodException e2) {
            e = e2;
            Log.e("FirebaseAbtUtil", "Could not complete the operation due to an internal error.", e);
            obj = arrayList;
            if (Log.isLoggable("FirebaseAbtUtil", 2)) {
                Log.v("FirebaseAbtUtil", new StringBuilder(String.valueOf(str).length() + 55).append("Number of currently set _Es for origin: ").append(str).append(" is ").append(list.size()).toString());
            }
            return list;
        } catch (IllegalAccessException e3) {
            e = e3;
            Log.e("FirebaseAbtUtil", "Could not complete the operation due to an internal error.", e);
            obj = arrayList;
            if (Log.isLoggable("FirebaseAbtUtil", 2)) {
                Log.v("FirebaseAbtUtil", new StringBuilder(String.valueOf(str).length() + 55).append("Number of currently set _Es for origin: ").append(str).append(" is ").append(list.size()).toString());
            }
            return list;
        } catch (InvocationTargetException e4) {
            e = e4;
            Log.e("FirebaseAbtUtil", "Could not complete the operation due to an internal error.", e);
            obj = arrayList;
            if (Log.isLoggable("FirebaseAbtUtil", 2)) {
                Log.v("FirebaseAbtUtil", new StringBuilder(String.valueOf(str).length() + 55).append("Number of currently set _Es for origin: ").append(str).append(" is ").append(list.size()).toString());
            }
            return list;
        }
        if (Log.isLoggable("FirebaseAbtUtil", 2)) {
            Log.v("FirebaseAbtUtil", new StringBuilder(String.valueOf(str).length() + 55).append("Number of currently set _Es for origin: ").append(str).append(" is ").append(list.size()).toString());
        }
        return list;
    }

    @Nullable
    static AppMeasurement zzbj(Context context) {
        try {
            return AppMeasurement.getInstance(context);
        } catch (NoClassDefFoundError e) {
            return null;
        }
    }

    static int zzc(@NonNull AppMeasurement appMeasurement, @NonNull String str) {
        Throwable e;
        try {
            Method declaredMethod = AppMeasurement.class.getDeclaredMethod("getMaxUserProperties", new Class[]{String.class});
            declaredMethod.setAccessible(true);
            return ((Integer) declaredMethod.invoke(appMeasurement, new Object[]{str})).intValue();
        } catch (NoSuchMethodException e2) {
            e = e2;
            Log.e("FirebaseAbtUtil", "Could not complete the operation due to an internal error.", e);
            return 20;
        } catch (IllegalAccessException e3) {
            e = e3;
            Log.e("FirebaseAbtUtil", "Could not complete the operation due to an internal error.", e);
            return 20;
        } catch (InvocationTargetException e4) {
            e = e4;
            Log.e("FirebaseAbtUtil", "Could not complete the operation due to an internal error.", e);
            return 20;
        }
    }

    static String zzc(@Nullable zzb com_google_android_gms_internal_zzbxz_zzb, @NonNull zzb com_google_firebase_messaging_zzb) {
        return (com_google_android_gms_internal_zzbxz_zzb == null || TextUtils.isEmpty(com_google_android_gms_internal_zzbxz_zzb.zzcvX)) ? com_google_firebase_messaging_zzb.zzVd() : com_google_android_gms_internal_zzbxz_zzb.zzcvX;
    }

    private static boolean zzco(Context context) {
        if (zzbj(context) != null) {
            try {
                Class.forName("com.google.android.gms.measurement.AppMeasurement$ConditionalUserProperty");
                return true;
            } catch (ClassNotFoundException e) {
                if (!Log.isLoggable("FirebaseAbtUtil", 2)) {
                    return false;
                }
                Log.v("FirebaseAbtUtil", "Firebase Analytics library is missing support for abt. Please update to a more recent version.");
                return false;
            }
        } else if (!Log.isLoggable("FirebaseAbtUtil", 2)) {
            return false;
        } else {
            Log.v("FirebaseAbtUtil", "Firebase Analytics not available");
            return false;
        }
    }

    static String zzd(@NonNull zzb com_google_android_gms_internal_zzbxz_zzb, @NonNull zzb com_google_firebase_messaging_zzb) {
        return !TextUtils.isEmpty(com_google_android_gms_internal_zzbxz_zzb.zzcvY) ? com_google_android_gms_internal_zzbxz_zzb.zzcvY : com_google_firebase_messaging_zzb.zzVb();
    }

    static String zze(@NonNull zzb com_google_android_gms_internal_zzbxz_zzb, @NonNull zzb com_google_firebase_messaging_zzb) {
        return !TextUtils.isEmpty(com_google_android_gms_internal_zzbxz_zzb.zzcvZ) ? com_google_android_gms_internal_zzbxz_zzb.zzcvZ : com_google_firebase_messaging_zzb.zzVc();
    }
}
