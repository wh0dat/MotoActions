package com.google.android.gms.common.internal;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.internal.stable.zza;
import com.google.android.gms.internal.stable.zzb;
import com.google.android.gms.internal.stable.zzc;

public interface ISignInButtonCreator extends IInterface {

    public static abstract class Stub extends zzb implements ISignInButtonCreator {

        public static class Proxy extends zza implements ISignInButtonCreator {
            Proxy(IBinder iBinder) {
                super(iBinder, "com.google.android.gms.common.internal.ISignInButtonCreator");
            }

            public IObjectWrapper newSignInButton(IObjectWrapper iObjectWrapper, int i, int i2) throws RemoteException {
                Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
                zzc.zza(obtainAndWriteInterfaceToken, (IInterface) iObjectWrapper);
                obtainAndWriteInterfaceToken.writeInt(i);
                obtainAndWriteInterfaceToken.writeInt(i2);
                Parcel transactAndReadException = transactAndReadException(1, obtainAndWriteInterfaceToken);
                IObjectWrapper asInterface = com.google.android.gms.dynamic.IObjectWrapper.Stub.asInterface(transactAndReadException.readStrongBinder());
                transactAndReadException.recycle();
                return asInterface;
            }

            public IObjectWrapper newSignInButtonFromConfig(IObjectWrapper iObjectWrapper, SignInButtonConfig signInButtonConfig) throws RemoteException {
                Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
                zzc.zza(obtainAndWriteInterfaceToken, (IInterface) iObjectWrapper);
                zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) signInButtonConfig);
                Parcel transactAndReadException = transactAndReadException(2, obtainAndWriteInterfaceToken);
                IObjectWrapper asInterface = com.google.android.gms.dynamic.IObjectWrapper.Stub.asInterface(transactAndReadException.readStrongBinder());
                transactAndReadException.recycle();
                return asInterface;
            }
        }

        public Stub() {
            super("com.google.android.gms.common.internal.ISignInButtonCreator");
        }

        public static ISignInButtonCreator asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.common.internal.ISignInButtonCreator");
            return queryLocalInterface instanceof ISignInButtonCreator ? (ISignInButtonCreator) queryLocalInterface : new Proxy(iBinder);
        }

        /* access modifiers changed from: protected */
        public boolean dispatchTransaction(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            IObjectWrapper iObjectWrapper;
            switch (i) {
                case 1:
                    iObjectWrapper = newSignInButton(com.google.android.gms.dynamic.IObjectWrapper.Stub.asInterface(parcel.readStrongBinder()), parcel.readInt(), parcel.readInt());
                    break;
                case 2:
                    iObjectWrapper = newSignInButtonFromConfig(com.google.android.gms.dynamic.IObjectWrapper.Stub.asInterface(parcel.readStrongBinder()), (SignInButtonConfig) zzc.zza(parcel, SignInButtonConfig.CREATOR));
                    break;
                default:
                    return false;
            }
            parcel2.writeNoException();
            zzc.zza(parcel2, (IInterface) iObjectWrapper);
            return true;
        }
    }

    IObjectWrapper newSignInButton(IObjectWrapper iObjectWrapper, int i, int i2) throws RemoteException;

    IObjectWrapper newSignInButtonFromConfig(IObjectWrapper iObjectWrapper, SignInButtonConfig signInButtonConfig) throws RemoteException;
}
