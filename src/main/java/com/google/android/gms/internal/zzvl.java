package com.google.android.gms.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.auth.api.credentials.CredentialRequest;

public interface zzvl extends IInterface {

    public static abstract class zza extends Binder implements zzvl {

        private static class zza implements zzvl {
            private IBinder zzrk;

            zza(IBinder iBinder) {
                this.zzrk = iBinder;
            }

            public IBinder asBinder() {
                return this.zzrk;
            }

            public void zza(zzvk com_google_android_gms_internal_zzvk) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.auth.api.credentials.internal.ICredentialsService");
                    obtain.writeStrongBinder(com_google_android_gms_internal_zzvk != null ? com_google_android_gms_internal_zzvk.asBinder() : null);
                    this.zzrk.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(zzvk com_google_android_gms_internal_zzvk, CredentialRequest credentialRequest) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.auth.api.credentials.internal.ICredentialsService");
                    obtain.writeStrongBinder(com_google_android_gms_internal_zzvk != null ? com_google_android_gms_internal_zzvk.asBinder() : null);
                    if (credentialRequest != null) {
                        obtain.writeInt(1);
                        credentialRequest.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzrk.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(zzvk com_google_android_gms_internal_zzvk, zzvg com_google_android_gms_internal_zzvg) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.auth.api.credentials.internal.ICredentialsService");
                    obtain.writeStrongBinder(com_google_android_gms_internal_zzvk != null ? com_google_android_gms_internal_zzvk.asBinder() : null);
                    if (com_google_android_gms_internal_zzvg != null) {
                        obtain.writeInt(1);
                        com_google_android_gms_internal_zzvg.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzrk.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(zzvk com_google_android_gms_internal_zzvk, zzvi com_google_android_gms_internal_zzvi) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.auth.api.credentials.internal.ICredentialsService");
                    obtain.writeStrongBinder(com_google_android_gms_internal_zzvk != null ? com_google_android_gms_internal_zzvk.asBinder() : null);
                    if (com_google_android_gms_internal_zzvi != null) {
                        obtain.writeInt(1);
                        com_google_android_gms_internal_zzvi.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzrk.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(zzvk com_google_android_gms_internal_zzvk, zzvm com_google_android_gms_internal_zzvm) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.auth.api.credentials.internal.ICredentialsService");
                    obtain.writeStrongBinder(com_google_android_gms_internal_zzvk != null ? com_google_android_gms_internal_zzvk.asBinder() : null);
                    if (com_google_android_gms_internal_zzvm != null) {
                        obtain.writeInt(1);
                        com_google_android_gms_internal_zzvm.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzrk.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public static zzvl zzaG(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.auth.api.credentials.internal.ICredentialsService");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof zzvl)) ? new zza(iBinder) : (zzvl) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            zzvi com_google_android_gms_internal_zzvi = null;
            zzvk zzaF;
            switch (i) {
                case 1:
                    CredentialRequest credentialRequest;
                    parcel.enforceInterface("com.google.android.gms.auth.api.credentials.internal.ICredentialsService");
                    zzaF = com.google.android.gms.internal.zzvk.zza.zzaF(parcel.readStrongBinder());
                    if (parcel.readInt() != 0) {
                        credentialRequest = (CredentialRequest) CredentialRequest.CREATOR.createFromParcel(parcel);
                    }
                    zza(zzaF, credentialRequest);
                    parcel2.writeNoException();
                    return true;
                case 2:
                    zzvm com_google_android_gms_internal_zzvm;
                    parcel.enforceInterface("com.google.android.gms.auth.api.credentials.internal.ICredentialsService");
                    zzaF = com.google.android.gms.internal.zzvk.zza.zzaF(parcel.readStrongBinder());
                    if (parcel.readInt() != 0) {
                        com_google_android_gms_internal_zzvm = (zzvm) zzvm.CREATOR.createFromParcel(parcel);
                    }
                    zza(zzaF, com_google_android_gms_internal_zzvm);
                    parcel2.writeNoException();
                    return true;
                case 3:
                    zzvg com_google_android_gms_internal_zzvg;
                    parcel.enforceInterface("com.google.android.gms.auth.api.credentials.internal.ICredentialsService");
                    zzaF = com.google.android.gms.internal.zzvk.zza.zzaF(parcel.readStrongBinder());
                    if (parcel.readInt() != 0) {
                        com_google_android_gms_internal_zzvg = (zzvg) zzvg.CREATOR.createFromParcel(parcel);
                    }
                    zza(zzaF, com_google_android_gms_internal_zzvg);
                    parcel2.writeNoException();
                    return true;
                case 4:
                    parcel.enforceInterface("com.google.android.gms.auth.api.credentials.internal.ICredentialsService");
                    zza(com.google.android.gms.internal.zzvk.zza.zzaF(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 5:
                    parcel.enforceInterface("com.google.android.gms.auth.api.credentials.internal.ICredentialsService");
                    zzaF = com.google.android.gms.internal.zzvk.zza.zzaF(parcel.readStrongBinder());
                    if (parcel.readInt() != 0) {
                        com_google_android_gms_internal_zzvi = (zzvi) zzvi.CREATOR.createFromParcel(parcel);
                    }
                    zza(zzaF, com_google_android_gms_internal_zzvi);
                    parcel2.writeNoException();
                    return true;
                case 1598968902:
                    parcel2.writeString("com.google.android.gms.auth.api.credentials.internal.ICredentialsService");
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    void zza(zzvk com_google_android_gms_internal_zzvk) throws RemoteException;

    void zza(zzvk com_google_android_gms_internal_zzvk, CredentialRequest credentialRequest) throws RemoteException;

    void zza(zzvk com_google_android_gms_internal_zzvk, zzvg com_google_android_gms_internal_zzvg) throws RemoteException;

    void zza(zzvk com_google_android_gms_internal_zzvk, zzvi com_google_android_gms_internal_zzvi) throws RemoteException;

    void zza(zzvk com_google_android_gms_internal_zzvk, zzvm com_google_android_gms_internal_zzvm) throws RemoteException;
}
