package com.motorola.mod;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IModAuthenticator extends IInterface {

    public static abstract class Stub extends Binder implements IModAuthenticator {

        /* renamed from: com.motorola.mod.IModAuthenticator$Stub$a */
        private static class C0638a implements IModAuthenticator {

            /* renamed from: a */
            private IBinder f47a;

            C0638a(IBinder iBinder) {
                this.f47a = iBinder;
            }

            public IBinder asBinder() {
                return this.f47a;
            }

            public void requestAuth(ModAuthRequest modAuthRequest) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.motorola.mod.IModAuthenticator");
                    if (modAuthRequest != null) {
                        obtain.writeInt(1);
                        modAuthRequest.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.f47a.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, "com.motorola.mod.IModAuthenticator");
        }

        public static IModAuthenticator asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.motorola.mod.IModAuthenticator");
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IModAuthenticator)) {
                return new C0638a(iBinder);
            }
            return (IModAuthenticator) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1) {
                parcel.enforceInterface("com.motorola.mod.IModAuthenticator");
                requestAuth(parcel.readInt() != 0 ? (ModAuthRequest) ModAuthRequest.CREATOR.createFromParcel(parcel) : null);
                parcel2.writeNoException();
                return true;
            } else if (i != 1598968902) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                parcel2.writeString("com.motorola.mod.IModAuthenticator");
                return true;
            }
        }
    }

    void requestAuth(ModAuthRequest modAuthRequest) throws RemoteException;
}
