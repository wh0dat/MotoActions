package com.motorola.slpc;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ISensorhubService extends IInterface {

    public static abstract class Stub extends Binder implements ISensorhubService {
        private static final String DESCRIPTOR = "com.motorola.slpc.ISensorhubService";
        static final int TRANSACTION_addGenericListener = 4;
        static final int TRANSACTION_addSensorhubListener = 6;
        static final int TRANSACTION_getFeatureVersion = 3;
        static final int TRANSACTION_read = 1;
        static final int TRANSACTION_removeGenericListener = 5;
        static final int TRANSACTION_removeSensorhubListener = 7;
        static final int TRANSACTION_write = 2;

        private static class Proxy implements ISensorhubService {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public int read(byte[] bArr, byte[] bArr2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeByteArray(bArr);
                    if (bArr2 == null) {
                        obtain.writeInt(-1);
                    } else {
                        obtain.writeInt(bArr2.length);
                    }
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    obtain2.readByteArray(bArr2);
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int write(byte[] bArr, byte[] bArr2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeByteArray(bArr);
                    obtain.writeByteArray(bArr2);
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int getFeatureVersion(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void addGenericListener(IGenericListener iGenericListener, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iGenericListener != null ? iGenericListener.asBinder() : null);
                    obtain.writeInt(i);
                    this.mRemote.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void removeGenericListener(IGenericListener iGenericListener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iGenericListener != null ? iGenericListener.asBinder() : null);
                    this.mRemote.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void addSensorhubListener(ISensorhubListener iSensorhubListener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iSensorhubListener != null ? iSensorhubListener.asBinder() : null);
                    this.mRemote.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void removeSensorhubListener(ISensorhubListener iSensorhubListener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iSensorhubListener != null ? iSensorhubListener.asBinder() : null);
                    this.mRemote.transact(7, obtain, obtain2, 0);
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
            attachInterface(this, DESCRIPTOR);
        }

        public static ISensorhubService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof ISensorhubService)) {
                return new Proxy(iBinder);
            }
            return (ISensorhubService) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            byte[] bArr;
            if (i != 1598968902) {
                switch (i) {
                    case 1:
                        parcel.enforceInterface(DESCRIPTOR);
                        byte[] createByteArray = parcel.createByteArray();
                        int readInt = parcel.readInt();
                        if (readInt < 0) {
                            bArr = null;
                        } else {
                            bArr = new byte[readInt];
                        }
                        int read = read(createByteArray, bArr);
                        parcel2.writeNoException();
                        parcel2.writeInt(read);
                        parcel2.writeByteArray(bArr);
                        return true;
                    case 2:
                        parcel.enforceInterface(DESCRIPTOR);
                        int write = write(parcel.createByteArray(), parcel.createByteArray());
                        parcel2.writeNoException();
                        parcel2.writeInt(write);
                        return true;
                    case 3:
                        parcel.enforceInterface(DESCRIPTOR);
                        int featureVersion = getFeatureVersion(parcel.readInt());
                        parcel2.writeNoException();
                        parcel2.writeInt(featureVersion);
                        return true;
                    case 4:
                        parcel.enforceInterface(DESCRIPTOR);
                        addGenericListener(com.motorola.slpc.IGenericListener.Stub.asInterface(parcel.readStrongBinder()), parcel.readInt());
                        parcel2.writeNoException();
                        return true;
                    case 5:
                        parcel.enforceInterface(DESCRIPTOR);
                        removeGenericListener(com.motorola.slpc.IGenericListener.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        return true;
                    case 6:
                        parcel.enforceInterface(DESCRIPTOR);
                        addSensorhubListener(com.motorola.slpc.ISensorhubListener.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        return true;
                    case 7:
                        parcel.enforceInterface(DESCRIPTOR);
                        removeSensorhubListener(com.motorola.slpc.ISensorhubListener.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        return true;
                    default:
                        return super.onTransact(i, parcel, parcel2, i2);
                }
            } else {
                parcel2.writeString(DESCRIPTOR);
                return true;
            }
        }
    }

    void addGenericListener(IGenericListener iGenericListener, int i) throws RemoteException;

    void addSensorhubListener(ISensorhubListener iSensorhubListener) throws RemoteException;

    int getFeatureVersion(int i) throws RemoteException;

    int read(byte[] bArr, byte[] bArr2) throws RemoteException;

    void removeGenericListener(IGenericListener iGenericListener) throws RemoteException;

    void removeSensorhubListener(ISensorhubListener iSensorhubListener) throws RemoteException;

    int write(byte[] bArr, byte[] bArr2) throws RemoteException;
}
