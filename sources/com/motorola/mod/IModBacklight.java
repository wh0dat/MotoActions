package com.motorola.mod;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IModBacklight extends IInterface {

    public static abstract class Stub extends Binder implements IModBacklight {

        /* renamed from: com.motorola.mod.IModBacklight$Stub$a */
        private static class C0639a implements IModBacklight {

            /* renamed from: a */
            private IBinder f48a;

            C0639a(IBinder iBinder) {
                this.f48a = iBinder;
            }

            public IBinder asBinder() {
                return this.f48a;
            }

            public boolean setModBacklightMode(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.motorola.mod.IModBacklight");
                    obtain.writeInt(i);
                    boolean z = false;
                    this.f48a.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        z = true;
                    }
                    return z;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int getModBacklightMode() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.motorola.mod.IModBacklight");
                    this.f48a.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean setModBacklightBrightness(byte b) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.motorola.mod.IModBacklight");
                    obtain.writeByte(b);
                    boolean z = false;
                    this.f48a.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        z = true;
                    }
                    return z;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public byte getModBacklightBrightness() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.motorola.mod.IModBacklight");
                    this.f48a.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readByte();
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
            attachInterface(this, "com.motorola.mod.IModBacklight");
        }

        public static IModBacklight asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.motorola.mod.IModBacklight");
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IModBacklight)) {
                return new C0639a(iBinder);
            }
            return (IModBacklight) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i != 1598968902) {
                switch (i) {
                    case 1:
                        parcel.enforceInterface("com.motorola.mod.IModBacklight");
                        boolean modBacklightMode = setModBacklightMode(parcel.readInt());
                        parcel2.writeNoException();
                        parcel2.writeInt(modBacklightMode ? 1 : 0);
                        return true;
                    case 2:
                        parcel.enforceInterface("com.motorola.mod.IModBacklight");
                        int modBacklightMode2 = getModBacklightMode();
                        parcel2.writeNoException();
                        parcel2.writeInt(modBacklightMode2);
                        return true;
                    case 3:
                        parcel.enforceInterface("com.motorola.mod.IModBacklight");
                        boolean modBacklightBrightness = setModBacklightBrightness(parcel.readByte());
                        parcel2.writeNoException();
                        parcel2.writeInt(modBacklightBrightness ? 1 : 0);
                        return true;
                    case 4:
                        parcel.enforceInterface("com.motorola.mod.IModBacklight");
                        byte modBacklightBrightness2 = getModBacklightBrightness();
                        parcel2.writeNoException();
                        parcel2.writeByte(modBacklightBrightness2);
                        return true;
                    default:
                        return super.onTransact(i, parcel, parcel2, i2);
                }
            } else {
                parcel2.writeString("com.motorola.mod.IModBacklight");
                return true;
            }
        }
    }

    byte getModBacklightBrightness() throws RemoteException;

    int getModBacklightMode() throws RemoteException;

    boolean setModBacklightBrightness(byte b) throws RemoteException;

    boolean setModBacklightMode(int i) throws RemoteException;
}
