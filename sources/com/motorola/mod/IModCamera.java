package com.motorola.mod;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IModCamera extends IInterface {

    public static abstract class Stub extends Binder implements IModCamera {

        /* renamed from: com.motorola.mod.IModCamera$Stub$a */
        private static class C0641a implements IModCamera {

            /* renamed from: a */
            private IBinder f50a;

            C0641a(IBinder iBinder) {
                this.f50a = iBinder;
            }

            public IBinder asBinder() {
                return this.f50a;
            }

            public String getProperty(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.motorola.mod.IModCamera");
                    obtain.writeString(str);
                    this.f50a.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readString();
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
            attachInterface(this, "com.motorola.mod.IModCamera");
        }

        public static IModCamera asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.motorola.mod.IModCamera");
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IModCamera)) {
                return new C0641a(iBinder);
            }
            return (IModCamera) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1) {
                parcel.enforceInterface("com.motorola.mod.IModCamera");
                String property = getProperty(parcel.readString());
                parcel2.writeNoException();
                parcel2.writeString(property);
                return true;
            } else if (i != 1598968902) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                parcel2.writeString("com.motorola.mod.IModCamera");
                return true;
            }
        }
    }

    String getProperty(String str) throws RemoteException;
}
