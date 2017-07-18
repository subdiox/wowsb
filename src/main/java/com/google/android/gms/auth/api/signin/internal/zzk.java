package com.google.android.gms.auth.api.signin.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public interface zzk extends IInterface {

    public static abstract class zza extends Binder implements zzk {

        private static class zza implements zzk {
            private IBinder zzrk;

            zza(IBinder iBinder) {
                this.zzrk = iBinder;
            }

            public IBinder asBinder() {
                return this.zzrk;
            }

            public void zza(zzj com_google_android_gms_auth_api_signin_internal_zzj, GoogleSignInOptions googleSignInOptions) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.auth.api.signin.internal.ISignInService");
                    obtain.writeStrongBinder(com_google_android_gms_auth_api_signin_internal_zzj != null ? com_google_android_gms_auth_api_signin_internal_zzj.asBinder() : null);
                    if (googleSignInOptions != null) {
                        obtain.writeInt(1);
                        googleSignInOptions.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzrk.transact(101, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zzb(zzj com_google_android_gms_auth_api_signin_internal_zzj, GoogleSignInOptions googleSignInOptions) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.auth.api.signin.internal.ISignInService");
                    obtain.writeStrongBinder(com_google_android_gms_auth_api_signin_internal_zzj != null ? com_google_android_gms_auth_api_signin_internal_zzj.asBinder() : null);
                    if (googleSignInOptions != null) {
                        obtain.writeInt(1);
                        googleSignInOptions.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzrk.transact(102, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zzc(zzj com_google_android_gms_auth_api_signin_internal_zzj, GoogleSignInOptions googleSignInOptions) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.auth.api.signin.internal.ISignInService");
                    obtain.writeStrongBinder(com_google_android_gms_auth_api_signin_internal_zzj != null ? com_google_android_gms_auth_api_signin_internal_zzj.asBinder() : null);
                    if (googleSignInOptions != null) {
                        obtain.writeInt(1);
                        googleSignInOptions.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzrk.transact(103, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public static zzk zzaM(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.auth.api.signin.internal.ISignInService");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof zzk)) ? new zza(iBinder) : (zzk) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            GoogleSignInOptions googleSignInOptions = null;
            zzj zzaL;
            switch (i) {
                case 101:
                    parcel.enforceInterface("com.google.android.gms.auth.api.signin.internal.ISignInService");
                    zzaL = com.google.android.gms.auth.api.signin.internal.zzj.zza.zzaL(parcel.readStrongBinder());
                    if (parcel.readInt() != 0) {
                        googleSignInOptions = (GoogleSignInOptions) GoogleSignInOptions.CREATOR.createFromParcel(parcel);
                    }
                    zza(zzaL, googleSignInOptions);
                    parcel2.writeNoException();
                    return true;
                case 102:
                    parcel.enforceInterface("com.google.android.gms.auth.api.signin.internal.ISignInService");
                    zzaL = com.google.android.gms.auth.api.signin.internal.zzj.zza.zzaL(parcel.readStrongBinder());
                    if (parcel.readInt() != 0) {
                        googleSignInOptions = (GoogleSignInOptions) GoogleSignInOptions.CREATOR.createFromParcel(parcel);
                    }
                    zzb(zzaL, googleSignInOptions);
                    parcel2.writeNoException();
                    return true;
                case 103:
                    parcel.enforceInterface("com.google.android.gms.auth.api.signin.internal.ISignInService");
                    zzaL = com.google.android.gms.auth.api.signin.internal.zzj.zza.zzaL(parcel.readStrongBinder());
                    if (parcel.readInt() != 0) {
                        googleSignInOptions = (GoogleSignInOptions) GoogleSignInOptions.CREATOR.createFromParcel(parcel);
                    }
                    zzc(zzaL, googleSignInOptions);
                    parcel2.writeNoException();
                    return true;
                case 1598968902:
                    parcel2.writeString("com.google.android.gms.auth.api.signin.internal.ISignInService");
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    void zza(zzj com_google_android_gms_auth_api_signin_internal_zzj, GoogleSignInOptions googleSignInOptions) throws RemoteException;

    void zzb(zzj com_google_android_gms_auth_api_signin_internal_zzj, GoogleSignInOptions googleSignInOptions) throws RemoteException;

    void zzc(zzj com_google_android_gms_auth_api_signin_internal_zzj, GoogleSignInOptions googleSignInOptions) throws RemoteException;
}
