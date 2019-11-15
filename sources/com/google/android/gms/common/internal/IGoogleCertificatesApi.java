package com.google.android.gms.common.internal;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import com.google.android.gms.common.GoogleCertificatesQuery;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.internal.stable.zza;
import com.google.android.gms.internal.stable.zzb;
import com.google.android.gms.internal.stable.zzc;

public interface IGoogleCertificatesApi extends IInterface {

    public static abstract class Stub extends zzb implements IGoogleCertificatesApi {

        public static class Proxy extends zza implements IGoogleCertificatesApi {
            Proxy(IBinder iBinder) {
                super(iBinder, "com.google.android.gms.common.internal.IGoogleCertificatesApi");
            }

            public IObjectWrapper getGoogleCertificates() throws RemoteException {
                Parcel transactAndReadException = transactAndReadException(1, obtainAndWriteInterfaceToken());
                IObjectWrapper asInterface = com.google.android.gms.dynamic.IObjectWrapper.Stub.asInterface(transactAndReadException.readStrongBinder());
                transactAndReadException.recycle();
                return asInterface;
            }

            public IObjectWrapper getGoogleReleaseCertificates() throws RemoteException {
                Parcel transactAndReadException = transactAndReadException(2, obtainAndWriteInterfaceToken());
                IObjectWrapper asInterface = com.google.android.gms.dynamic.IObjectWrapper.Stub.asInterface(transactAndReadException.readStrongBinder());
                transactAndReadException.recycle();
                return asInterface;
            }

            public boolean isGoogleOrPlatformSigned(GoogleCertificatesQuery googleCertificatesQuery, IObjectWrapper iObjectWrapper) throws RemoteException {
                Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
                zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) googleCertificatesQuery);
                zzc.zza(obtainAndWriteInterfaceToken, (IInterface) iObjectWrapper);
                Parcel transactAndReadException = transactAndReadException(5, obtainAndWriteInterfaceToken);
                boolean zza = zzc.zza(transactAndReadException);
                transactAndReadException.recycle();
                return zza;
            }

            public boolean isGoogleReleaseSigned(String str, IObjectWrapper iObjectWrapper) throws RemoteException {
                Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
                obtainAndWriteInterfaceToken.writeString(str);
                zzc.zza(obtainAndWriteInterfaceToken, (IInterface) iObjectWrapper);
                Parcel transactAndReadException = transactAndReadException(3, obtainAndWriteInterfaceToken);
                boolean zza = zzc.zza(transactAndReadException);
                transactAndReadException.recycle();
                return zza;
            }

            public boolean isGoogleSigned(String str, IObjectWrapper iObjectWrapper) throws RemoteException {
                Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
                obtainAndWriteInterfaceToken.writeString(str);
                zzc.zza(obtainAndWriteInterfaceToken, (IInterface) iObjectWrapper);
                Parcel transactAndReadException = transactAndReadException(4, obtainAndWriteInterfaceToken);
                boolean zza = zzc.zza(transactAndReadException);
                transactAndReadException.recycle();
                return zza;
            }
        }

        public Stub() {
            super("com.google.android.gms.common.internal.IGoogleCertificatesApi");
        }

        public static IGoogleCertificatesApi asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.common.internal.IGoogleCertificatesApi");
            return queryLocalInterface instanceof IGoogleCertificatesApi ? (IGoogleCertificatesApi) queryLocalInterface : new Proxy(iBinder);
        }

        /* JADX INFO: used method not loaded: com.google.android.gms.internal.stable.zzc.zza(android.os.Parcel, boolean):null, types can be incorrect */
        /* JADX INFO: used method not loaded: com.google.android.gms.internal.stable.zzc.zza(android.os.Parcel, android.os.IInterface):null, types can be incorrect */
        /* access modifiers changed from: protected */
        /* JADX WARNING: Code restructure failed: missing block: B:11:0x0052, code lost:
            return true;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:6:0x003b, code lost:
            r3.writeNoException();
            com.google.android.gms.internal.stable.zzc.zza(r3, r0);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:9:0x004b, code lost:
            r3.writeNoException();
            com.google.android.gms.internal.stable.zzc.zza(r3, (android.os.IInterface) r0);
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean dispatchTransaction(int r1, android.os.Parcel r2, android.os.Parcel r3, int r4) throws android.os.RemoteException {
            /*
                r0 = this;
                switch(r1) {
                    case 1: goto L_0x0047;
                    case 2: goto L_0x0042;
                    case 3: goto L_0x002b;
                    case 4: goto L_0x001a;
                    case 5: goto L_0x0005;
                    default: goto L_0x0003;
                }
            L_0x0003:
                r0 = 0
                return r0
            L_0x0005:
                android.os.Parcelable$Creator<com.google.android.gms.common.GoogleCertificatesQuery> r1 = com.google.android.gms.common.GoogleCertificatesQuery.CREATOR
                android.os.Parcelable r1 = com.google.android.gms.internal.stable.zzc.zza(r2, r1)
                com.google.android.gms.common.GoogleCertificatesQuery r1 = (com.google.android.gms.common.GoogleCertificatesQuery) r1
                android.os.IBinder r2 = r2.readStrongBinder()
                com.google.android.gms.dynamic.IObjectWrapper r2 = com.google.android.gms.dynamic.IObjectWrapper.Stub.asInterface(r2)
                boolean r0 = r0.isGoogleOrPlatformSigned(r1, r2)
                goto L_0x003b
            L_0x001a:
                java.lang.String r1 = r2.readString()
                android.os.IBinder r2 = r2.readStrongBinder()
                com.google.android.gms.dynamic.IObjectWrapper r2 = com.google.android.gms.dynamic.IObjectWrapper.Stub.asInterface(r2)
                boolean r0 = r0.isGoogleSigned(r1, r2)
                goto L_0x003b
            L_0x002b:
                java.lang.String r1 = r2.readString()
                android.os.IBinder r2 = r2.readStrongBinder()
                com.google.android.gms.dynamic.IObjectWrapper r2 = com.google.android.gms.dynamic.IObjectWrapper.Stub.asInterface(r2)
                boolean r0 = r0.isGoogleReleaseSigned(r1, r2)
            L_0x003b:
                r3.writeNoException()
                com.google.android.gms.internal.stable.zzc.zza(r3, r0)
                goto L_0x0051
            L_0x0042:
                com.google.android.gms.dynamic.IObjectWrapper r0 = r0.getGoogleReleaseCertificates()
                goto L_0x004b
            L_0x0047:
                com.google.android.gms.dynamic.IObjectWrapper r0 = r0.getGoogleCertificates()
            L_0x004b:
                r3.writeNoException()
                com.google.android.gms.internal.stable.zzc.zza(r3, r0)
            L_0x0051:
                r0 = 1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.internal.IGoogleCertificatesApi.Stub.dispatchTransaction(int, android.os.Parcel, android.os.Parcel, int):boolean");
        }
    }

    IObjectWrapper getGoogleCertificates() throws RemoteException;

    IObjectWrapper getGoogleReleaseCertificates() throws RemoteException;

    boolean isGoogleOrPlatformSigned(GoogleCertificatesQuery googleCertificatesQuery, IObjectWrapper iObjectWrapper) throws RemoteException;

    boolean isGoogleReleaseSigned(String str, IObjectWrapper iObjectWrapper) throws RemoteException;

    boolean isGoogleSigned(String str, IObjectWrapper iObjectWrapper) throws RemoteException;
}
