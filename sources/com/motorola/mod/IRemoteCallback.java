package com.motorola.mod;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IRemoteCallback extends IInterface {

    public static abstract class Stub extends Binder implements IRemoteCallback {

        /* renamed from: com.motorola.mod.IRemoteCallback$Stub$a */
        private static class C0645a implements IRemoteCallback {

            /* renamed from: a */
            private IBinder f54a;

            C0645a(IBinder iBinder) {
                this.f54a = iBinder;
            }

            public IBinder asBinder() {
                return this.f54a;
            }

            public void sendResult(Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.motorola.mod.IRemoteCallback");
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.f54a.transact(1, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, "com.motorola.mod.IRemoteCallback");
        }

        public static IRemoteCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.motorola.mod.IRemoteCallback");
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IRemoteCallback)) {
                return new C0645a(iBinder);
            }
            return (IRemoteCallback) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1) {
                parcel.enforceInterface("com.motorola.mod.IRemoteCallback");
                sendResult(parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null);
                return true;
            } else if (i != 1598968902) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                parcel2.writeString("com.motorola.mod.IRemoteCallback");
                return true;
            }
        }
    }

    void sendResult(Bundle bundle) throws RemoteException;
}
