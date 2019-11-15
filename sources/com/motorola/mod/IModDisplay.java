package com.motorola.mod;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IModDisplay extends IInterface {

    public static abstract class Stub extends Binder implements IModDisplay {

        /* renamed from: com.motorola.mod.IModDisplay$Stub$a */
        private static class C0642a implements IModDisplay {

            /* renamed from: a */
            private IBinder f51a;

            C0642a(IBinder iBinder) {
                this.f51a = iBinder;
            }

            public IBinder asBinder() {
                return this.f51a;
            }

            public boolean setModDisplayState(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.motorola.mod.IModDisplay");
                    obtain.writeInt(i);
                    boolean z = false;
                    this.f51a.transact(1, obtain, obtain2, 0);
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

            public int getModDisplayState() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.motorola.mod.IModDisplay");
                    this.f51a.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean setModDisplayFollowState(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.motorola.mod.IModDisplay");
                    obtain.writeInt(i);
                    boolean z = false;
                    this.f51a.transact(3, obtain, obtain2, 0);
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

            public int getModDisplayFollowState() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.motorola.mod.IModDisplay");
                    this.f51a.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean setPrivacyMode(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.motorola.mod.IModDisplay");
                    obtain.writeInt(i);
                    boolean z = false;
                    this.f51a.transact(5, obtain, obtain2, 0);
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

            public int getPrivacyMode() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.motorola.mod.IModDisplay");
                    this.f51a.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
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
            attachInterface(this, "com.motorola.mod.IModDisplay");
        }

        public static IModDisplay asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.motorola.mod.IModDisplay");
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IModDisplay)) {
                return new C0642a(iBinder);
            }
            return (IModDisplay) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i != 1598968902) {
                switch (i) {
                    case 1:
                        parcel.enforceInterface("com.motorola.mod.IModDisplay");
                        boolean modDisplayState = setModDisplayState(parcel.readInt());
                        parcel2.writeNoException();
                        parcel2.writeInt(modDisplayState ? 1 : 0);
                        return true;
                    case 2:
                        parcel.enforceInterface("com.motorola.mod.IModDisplay");
                        int modDisplayState2 = getModDisplayState();
                        parcel2.writeNoException();
                        parcel2.writeInt(modDisplayState2);
                        return true;
                    case 3:
                        parcel.enforceInterface("com.motorola.mod.IModDisplay");
                        boolean modDisplayFollowState = setModDisplayFollowState(parcel.readInt());
                        parcel2.writeNoException();
                        parcel2.writeInt(modDisplayFollowState ? 1 : 0);
                        return true;
                    case 4:
                        parcel.enforceInterface("com.motorola.mod.IModDisplay");
                        int modDisplayFollowState2 = getModDisplayFollowState();
                        parcel2.writeNoException();
                        parcel2.writeInt(modDisplayFollowState2);
                        return true;
                    case 5:
                        parcel.enforceInterface("com.motorola.mod.IModDisplay");
                        boolean privacyMode = setPrivacyMode(parcel.readInt());
                        parcel2.writeNoException();
                        parcel2.writeInt(privacyMode ? 1 : 0);
                        return true;
                    case 6:
                        parcel.enforceInterface("com.motorola.mod.IModDisplay");
                        int privacyMode2 = getPrivacyMode();
                        parcel2.writeNoException();
                        parcel2.writeInt(privacyMode2);
                        return true;
                    default:
                        return super.onTransact(i, parcel, parcel2, i2);
                }
            } else {
                parcel2.writeString("com.motorola.mod.IModDisplay");
                return true;
            }
        }
    }

    int getModDisplayFollowState() throws RemoteException;

    int getModDisplayState() throws RemoteException;

    int getPrivacyMode() throws RemoteException;

    boolean setModDisplayFollowState(int i) throws RemoteException;

    boolean setModDisplayState(int i) throws RemoteException;

    boolean setPrivacyMode(int i) throws RemoteException;
}
