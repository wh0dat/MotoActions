package com.motorola.mod;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IModBattery extends IInterface {

    public static abstract class Stub extends Binder implements IModBattery {

        /* renamed from: com.motorola.mod.IModBattery$Stub$a */
        private static class C0640a implements IModBattery {

            /* renamed from: a */
            private IBinder f49a;

            C0640a(IBinder iBinder) {
                this.f49a = iBinder;
            }

            public IBinder asBinder() {
                return this.f49a;
            }

            public int getIntProperty(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.motorola.mod.IModBattery");
                    obtain.writeInt(i);
                    this.f49a.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setIntProperty(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.motorola.mod.IModBattery");
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.f49a.transact(2, obtain, obtain2, 0);
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
            attachInterface(this, "com.motorola.mod.IModBattery");
        }

        public static IModBattery asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.motorola.mod.IModBattery");
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IModBattery)) {
                return new C0640a(iBinder);
            }
            return (IModBattery) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i != 1598968902) {
                switch (i) {
                    case 1:
                        parcel.enforceInterface("com.motorola.mod.IModBattery");
                        int intProperty = getIntProperty(parcel.readInt());
                        parcel2.writeNoException();
                        parcel2.writeInt(intProperty);
                        return true;
                    case 2:
                        parcel.enforceInterface("com.motorola.mod.IModBattery");
                        setIntProperty(parcel.readInt(), parcel.readInt());
                        parcel2.writeNoException();
                        return true;
                    default:
                        return super.onTransact(i, parcel, parcel2, i2);
                }
            } else {
                parcel2.writeString("com.motorola.mod.IModBattery");
                return true;
            }
        }
    }

    int getIntProperty(int i) throws RemoteException;

    void setIntProperty(int i, int i2) throws RemoteException;
}
