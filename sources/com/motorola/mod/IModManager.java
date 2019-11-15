package com.motorola.mod;

import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import java.util.List;

public interface IModManager extends IInterface {

    public static abstract class Stub extends Binder implements IModManager {

        /* renamed from: com.motorola.mod.IModManager$Stub$a */
        private static class C0644a implements IModManager {

            /* renamed from: a */
            private IBinder f53a;

            C0644a(IBinder iBinder) {
                this.f53a = iBinder;
            }

            public IBinder asBinder() {
                return this.f53a;
            }

            public boolean registerModListener(IModListener iModListener, int[] iArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.motorola.mod.IModManager");
                    obtain.writeStrongBinder(iModListener != null ? iModListener.asBinder() : null);
                    obtain.writeIntArray(iArr);
                    boolean z = false;
                    this.f53a.transact(1, obtain, obtain2, 0);
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

            public List<ModDevice> getModList(int[] iArr, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.motorola.mod.IModManager");
                    obtain.writeIntArray(iArr);
                    obtain.writeInt(z ? 1 : 0);
                    this.f53a.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.createTypedArrayList(ModDevice.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public IModAuthenticator getAuthenticator() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.motorola.mod.IModManager");
                    this.f53a.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                    return com.motorola.mod.IModAuthenticator.Stub.asInterface(obtain2.readStrongBinder());
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public List<ModConnection> getConnectionsByDevice(ModDevice modDevice) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.motorola.mod.IModManager");
                    if (modDevice != null) {
                        obtain.writeInt(1);
                        modDevice.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.f53a.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.createTypedArrayList(ModConnection.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void checkForModFirmwareUpdates(int i, int i2, String str, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.motorola.mod.IModManager");
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeString(str);
                    obtain.writeInt(z ? 1 : 0);
                    this.f53a.transact(5, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void downloadModFirmware(int i, int i2, String str, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.motorola.mod.IModManager");
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeString(str);
                    obtain.writeInt(z ? 1 : 0);
                    this.f53a.transact(6, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void cancelModFirmwareUpdate(int i, int i2, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.motorola.mod.IModManager");
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeString(str);
                    this.f53a.transact(7, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public int getDownloadPercent(int i, int i2, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.motorola.mod.IModManager");
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeString(str);
                    this.f53a.transact(8, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int installOtaPackage(ModDevice modDevice, Uri uri, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.motorola.mod.IModManager");
                    if (modDevice != null) {
                        obtain.writeInt(1);
                        modDevice.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (uri != null) {
                        obtain.writeInt(1);
                        uri.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeInt(z ? 1 : 0);
                    this.f53a.transact(9, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int requestUpdateFirmware(ModDevice modDevice, List<Uri> list) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.motorola.mod.IModManager");
                    if (modDevice != null) {
                        obtain.writeInt(1);
                        modDevice.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeTypedList(list);
                    this.f53a.transact(10, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public ParcelFileDescriptor openModInterface(ModInterfaceDelegation modInterfaceDelegation, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.motorola.mod.IModManager");
                    if (modInterfaceDelegation != null) {
                        obtain.writeInt(1);
                        modInterfaceDelegation.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeInt(i);
                    this.f53a.transact(11, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0 ? (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(obtain2) : null;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public List<ModInterfaceDelegation> getModInterfaceDelegationsByProtocol(ModDevice modDevice, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.motorola.mod.IModManager");
                    if (modDevice != null) {
                        obtain.writeInt(1);
                        modDevice.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeInt(i);
                    this.f53a.transact(12, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.createTypedArrayList(ModInterfaceDelegation.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public IBinder getClassManager(ModDevice modDevice, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.motorola.mod.IModManager");
                    if (modDevice != null) {
                        obtain.writeInt(1);
                        modDevice.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeInt(i);
                    this.f53a.transact(13, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readStrongBinder();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean initSetupComplete(ModDevice modDevice) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.motorola.mod.IModManager");
                    boolean z = true;
                    if (modDevice != null) {
                        obtain.writeInt(1);
                        modDevice.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.f53a.transact(14, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() == 0) {
                        z = false;
                    }
                    return z;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean setCurrentLimit(ModDevice modDevice, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.motorola.mod.IModManager");
                    boolean z = true;
                    if (modDevice != null) {
                        obtain.writeInt(1);
                        modDevice.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeInt(i);
                    this.f53a.transact(15, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() == 0) {
                        z = false;
                    }
                    return z;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void changeDownloadPreference(int i, int i2, String str, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.motorola.mod.IModManager");
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeString(str);
                    obtain.writeInt(z ? 1 : 0);
                    this.f53a.transact(16, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean setUsbPriority(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.motorola.mod.IModManager");
                    obtain.writeInt(i);
                    boolean z = false;
                    this.f53a.transact(17, obtain, obtain2, 0);
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

            public void reset(ModDevice modDevice) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.motorola.mod.IModManager");
                    if (modDevice != null) {
                        obtain.writeInt(1);
                        modDevice.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.f53a.transact(18, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int[] getSupportedProtocols() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.motorola.mod.IModManager");
                    this.f53a.transact(19, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.createIntArray();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isProtocolSupported(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.motorola.mod.IModManager");
                    obtain.writeInt(i);
                    boolean z = false;
                    this.f53a.transact(20, obtain, obtain2, 0);
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
        }

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, "com.motorola.mod.IModManager");
        }

        public static IModManager asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.motorola.mod.IModManager");
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IModManager)) {
                return new C0644a(iBinder);
            }
            return (IModManager) queryLocalInterface;
        }

        /* JADX WARNING: type inference failed for: r2v0 */
        /* JADX WARNING: type inference failed for: r2v1, types: [android.os.IBinder] */
        /* JADX WARNING: type inference failed for: r2v2, types: [android.os.IBinder] */
        /* JADX WARNING: type inference failed for: r2v3, types: [com.motorola.mod.ModDevice] */
        /* JADX WARNING: type inference failed for: r2v5, types: [com.motorola.mod.ModDevice] */
        /* JADX WARNING: type inference failed for: r2v6, types: [android.net.Uri] */
        /* JADX WARNING: type inference failed for: r2v8, types: [android.net.Uri] */
        /* JADX WARNING: type inference failed for: r2v9, types: [com.motorola.mod.ModDevice] */
        /* JADX WARNING: type inference failed for: r2v11, types: [com.motorola.mod.ModDevice] */
        /* JADX WARNING: type inference failed for: r2v12, types: [com.motorola.mod.ModInterfaceDelegation] */
        /* JADX WARNING: type inference failed for: r2v14, types: [com.motorola.mod.ModInterfaceDelegation] */
        /* JADX WARNING: type inference failed for: r2v15, types: [com.motorola.mod.ModDevice] */
        /* JADX WARNING: type inference failed for: r2v17, types: [com.motorola.mod.ModDevice] */
        /* JADX WARNING: type inference failed for: r2v18, types: [com.motorola.mod.ModDevice] */
        /* JADX WARNING: type inference failed for: r2v20, types: [com.motorola.mod.ModDevice] */
        /* JADX WARNING: type inference failed for: r2v21, types: [com.motorola.mod.ModDevice] */
        /* JADX WARNING: type inference failed for: r2v23, types: [com.motorola.mod.ModDevice] */
        /* JADX WARNING: type inference failed for: r2v24, types: [com.motorola.mod.ModDevice] */
        /* JADX WARNING: type inference failed for: r2v26, types: [com.motorola.mod.ModDevice] */
        /* JADX WARNING: type inference failed for: r2v28, types: [com.motorola.mod.ModDevice] */
        /* JADX WARNING: type inference failed for: r2v30, types: [com.motorola.mod.ModDevice] */
        /* JADX WARNING: type inference failed for: r2v31 */
        /* JADX WARNING: type inference failed for: r2v32 */
        /* JADX WARNING: type inference failed for: r2v33 */
        /* JADX WARNING: type inference failed for: r2v34 */
        /* JADX WARNING: type inference failed for: r2v35 */
        /* JADX WARNING: type inference failed for: r2v36 */
        /* JADX WARNING: type inference failed for: r2v37 */
        /* JADX WARNING: type inference failed for: r2v38 */
        /* JADX WARNING: type inference failed for: r2v39 */
        /* JADX WARNING: type inference failed for: r2v40 */
        /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r2v0
          assigns: [?[int, float, boolean, short, byte, char, OBJECT, ARRAY], com.motorola.mod.ModDevice, android.os.IBinder, android.net.Uri, com.motorola.mod.ModInterfaceDelegation]
          uses: [android.os.IBinder, com.motorola.mod.ModDevice, android.net.Uri, com.motorola.mod.ModInterfaceDelegation]
          mth insns count: 238
        	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$0(DepthTraversal.java:13)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:13)
        	at jadx.core.ProcessClass.process(ProcessClass.java:30)
        	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
        	at jadx.core.ProcessClass.process(ProcessClass.java:35)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
         */
        /* JADX WARNING: Unknown variable types count: 11 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean onTransact(int r4, android.os.Parcel r5, android.os.Parcel r6, int r7) throws android.os.RemoteException {
            /*
                r3 = this;
                r0 = 1598968902(0x5f4e5446, float:1.4867585E19)
                r1 = 1
                if (r4 == r0) goto L_0x0266
                r0 = 0
                r2 = 0
                switch(r4) {
                    case 1: goto L_0x024a;
                    case 2: goto L_0x022f;
                    case 3: goto L_0x0219;
                    case 4: goto L_0x01fa;
                    case 5: goto L_0x01de;
                    case 6: goto L_0x01c2;
                    case 7: goto L_0x01ad;
                    case 8: goto L_0x0191;
                    case 9: goto L_0x015b;
                    case 10: goto L_0x0136;
                    case 11: goto L_0x010a;
                    case 12: goto L_0x00e7;
                    case 13: goto L_0x00c4;
                    case 14: goto L_0x00a5;
                    case 15: goto L_0x0082;
                    case 16: goto L_0x0063;
                    case 17: goto L_0x004f;
                    case 18: goto L_0x0034;
                    case 19: goto L_0x0024;
                    case 20: goto L_0x0010;
                    default: goto L_0x000b;
                }
            L_0x000b:
                boolean r3 = super.onTransact(r4, r5, r6, r7)
                return r3
            L_0x0010:
                java.lang.String r4 = "com.motorola.mod.IModManager"
                r5.enforceInterface(r4)
                int r4 = r5.readInt()
                boolean r3 = r3.isProtocolSupported(r4)
                r6.writeNoException()
                r6.writeInt(r3)
                return r1
            L_0x0024:
                java.lang.String r4 = "com.motorola.mod.IModManager"
                r5.enforceInterface(r4)
                int[] r3 = r3.getSupportedProtocols()
                r6.writeNoException()
                r6.writeIntArray(r3)
                return r1
            L_0x0034:
                java.lang.String r4 = "com.motorola.mod.IModManager"
                r5.enforceInterface(r4)
                int r4 = r5.readInt()
                if (r4 == 0) goto L_0x0048
                android.os.Parcelable$Creator<com.motorola.mod.ModDevice> r4 = com.motorola.mod.ModDevice.CREATOR
                java.lang.Object r4 = r4.createFromParcel(r5)
                r2 = r4
                com.motorola.mod.ModDevice r2 = (com.motorola.mod.ModDevice) r2
            L_0x0048:
                r3.reset(r2)
                r6.writeNoException()
                return r1
            L_0x004f:
                java.lang.String r4 = "com.motorola.mod.IModManager"
                r5.enforceInterface(r4)
                int r4 = r5.readInt()
                boolean r3 = r3.setUsbPriority(r4)
                r6.writeNoException()
                r6.writeInt(r3)
                return r1
            L_0x0063:
                java.lang.String r4 = "com.motorola.mod.IModManager"
                r5.enforceInterface(r4)
                int r4 = r5.readInt()
                int r7 = r5.readInt()
                java.lang.String r2 = r5.readString()
                int r5 = r5.readInt()
                if (r5 == 0) goto L_0x007b
                r0 = r1
            L_0x007b:
                r3.changeDownloadPreference(r4, r7, r2, r0)
                r6.writeNoException()
                return r1
            L_0x0082:
                java.lang.String r4 = "com.motorola.mod.IModManager"
                r5.enforceInterface(r4)
                int r4 = r5.readInt()
                if (r4 == 0) goto L_0x0096
                android.os.Parcelable$Creator<com.motorola.mod.ModDevice> r4 = com.motorola.mod.ModDevice.CREATOR
                java.lang.Object r4 = r4.createFromParcel(r5)
                r2 = r4
                com.motorola.mod.ModDevice r2 = (com.motorola.mod.ModDevice) r2
            L_0x0096:
                int r4 = r5.readInt()
                boolean r3 = r3.setCurrentLimit(r2, r4)
                r6.writeNoException()
                r6.writeInt(r3)
                return r1
            L_0x00a5:
                java.lang.String r4 = "com.motorola.mod.IModManager"
                r5.enforceInterface(r4)
                int r4 = r5.readInt()
                if (r4 == 0) goto L_0x00b9
                android.os.Parcelable$Creator<com.motorola.mod.ModDevice> r4 = com.motorola.mod.ModDevice.CREATOR
                java.lang.Object r4 = r4.createFromParcel(r5)
                r2 = r4
                com.motorola.mod.ModDevice r2 = (com.motorola.mod.ModDevice) r2
            L_0x00b9:
                boolean r3 = r3.initSetupComplete(r2)
                r6.writeNoException()
                r6.writeInt(r3)
                return r1
            L_0x00c4:
                java.lang.String r4 = "com.motorola.mod.IModManager"
                r5.enforceInterface(r4)
                int r4 = r5.readInt()
                if (r4 == 0) goto L_0x00d8
                android.os.Parcelable$Creator<com.motorola.mod.ModDevice> r4 = com.motorola.mod.ModDevice.CREATOR
                java.lang.Object r4 = r4.createFromParcel(r5)
                r2 = r4
                com.motorola.mod.ModDevice r2 = (com.motorola.mod.ModDevice) r2
            L_0x00d8:
                int r4 = r5.readInt()
                android.os.IBinder r3 = r3.getClassManager(r2, r4)
                r6.writeNoException()
                r6.writeStrongBinder(r3)
                return r1
            L_0x00e7:
                java.lang.String r4 = "com.motorola.mod.IModManager"
                r5.enforceInterface(r4)
                int r4 = r5.readInt()
                if (r4 == 0) goto L_0x00fb
                android.os.Parcelable$Creator<com.motorola.mod.ModDevice> r4 = com.motorola.mod.ModDevice.CREATOR
                java.lang.Object r4 = r4.createFromParcel(r5)
                r2 = r4
                com.motorola.mod.ModDevice r2 = (com.motorola.mod.ModDevice) r2
            L_0x00fb:
                int r4 = r5.readInt()
                java.util.List r3 = r3.getModInterfaceDelegationsByProtocol(r2, r4)
                r6.writeNoException()
                r6.writeTypedList(r3)
                return r1
            L_0x010a:
                java.lang.String r4 = "com.motorola.mod.IModManager"
                r5.enforceInterface(r4)
                int r4 = r5.readInt()
                if (r4 == 0) goto L_0x011e
                android.os.Parcelable$Creator<com.motorola.mod.ModInterfaceDelegation> r4 = com.motorola.mod.ModInterfaceDelegation.CREATOR
                java.lang.Object r4 = r4.createFromParcel(r5)
                r2 = r4
                com.motorola.mod.ModInterfaceDelegation r2 = (com.motorola.mod.ModInterfaceDelegation) r2
            L_0x011e:
                int r4 = r5.readInt()
                android.os.ParcelFileDescriptor r3 = r3.openModInterface(r2, r4)
                r6.writeNoException()
                if (r3 == 0) goto L_0x0132
                r6.writeInt(r1)
                r3.writeToParcel(r6, r1)
                goto L_0x0135
            L_0x0132:
                r6.writeInt(r0)
            L_0x0135:
                return r1
            L_0x0136:
                java.lang.String r4 = "com.motorola.mod.IModManager"
                r5.enforceInterface(r4)
                int r4 = r5.readInt()
                if (r4 == 0) goto L_0x014a
                android.os.Parcelable$Creator<com.motorola.mod.ModDevice> r4 = com.motorola.mod.ModDevice.CREATOR
                java.lang.Object r4 = r4.createFromParcel(r5)
                r2 = r4
                com.motorola.mod.ModDevice r2 = (com.motorola.mod.ModDevice) r2
            L_0x014a:
                android.os.Parcelable$Creator r4 = android.net.Uri.CREATOR
                java.util.ArrayList r4 = r5.createTypedArrayList(r4)
                int r3 = r3.requestUpdateFirmware(r2, r4)
                r6.writeNoException()
                r6.writeInt(r3)
                return r1
            L_0x015b:
                java.lang.String r4 = "com.motorola.mod.IModManager"
                r5.enforceInterface(r4)
                int r4 = r5.readInt()
                if (r4 == 0) goto L_0x016f
                android.os.Parcelable$Creator<com.motorola.mod.ModDevice> r4 = com.motorola.mod.ModDevice.CREATOR
                java.lang.Object r4 = r4.createFromParcel(r5)
                com.motorola.mod.ModDevice r4 = (com.motorola.mod.ModDevice) r4
                goto L_0x0170
            L_0x016f:
                r4 = r2
            L_0x0170:
                int r7 = r5.readInt()
                if (r7 == 0) goto L_0x017f
                android.os.Parcelable$Creator r7 = android.net.Uri.CREATOR
                java.lang.Object r7 = r7.createFromParcel(r5)
                r2 = r7
                android.net.Uri r2 = (android.net.Uri) r2
            L_0x017f:
                int r5 = r5.readInt()
                if (r5 == 0) goto L_0x0186
                r0 = r1
            L_0x0186:
                int r3 = r3.installOtaPackage(r4, r2, r0)
                r6.writeNoException()
                r6.writeInt(r3)
                return r1
            L_0x0191:
                java.lang.String r4 = "com.motorola.mod.IModManager"
                r5.enforceInterface(r4)
                int r4 = r5.readInt()
                int r7 = r5.readInt()
                java.lang.String r5 = r5.readString()
                int r3 = r3.getDownloadPercent(r4, r7, r5)
                r6.writeNoException()
                r6.writeInt(r3)
                return r1
            L_0x01ad:
                java.lang.String r4 = "com.motorola.mod.IModManager"
                r5.enforceInterface(r4)
                int r4 = r5.readInt()
                int r6 = r5.readInt()
                java.lang.String r5 = r5.readString()
                r3.cancelModFirmwareUpdate(r4, r6, r5)
                return r1
            L_0x01c2:
                java.lang.String r4 = "com.motorola.mod.IModManager"
                r5.enforceInterface(r4)
                int r4 = r5.readInt()
                int r6 = r5.readInt()
                java.lang.String r7 = r5.readString()
                int r5 = r5.readInt()
                if (r5 == 0) goto L_0x01da
                r0 = r1
            L_0x01da:
                r3.downloadModFirmware(r4, r6, r7, r0)
                return r1
            L_0x01de:
                java.lang.String r4 = "com.motorola.mod.IModManager"
                r5.enforceInterface(r4)
                int r4 = r5.readInt()
                int r6 = r5.readInt()
                java.lang.String r7 = r5.readString()
                int r5 = r5.readInt()
                if (r5 == 0) goto L_0x01f6
                r0 = r1
            L_0x01f6:
                r3.checkForModFirmwareUpdates(r4, r6, r7, r0)
                return r1
            L_0x01fa:
                java.lang.String r4 = "com.motorola.mod.IModManager"
                r5.enforceInterface(r4)
                int r4 = r5.readInt()
                if (r4 == 0) goto L_0x020e
                android.os.Parcelable$Creator<com.motorola.mod.ModDevice> r4 = com.motorola.mod.ModDevice.CREATOR
                java.lang.Object r4 = r4.createFromParcel(r5)
                r2 = r4
                com.motorola.mod.ModDevice r2 = (com.motorola.mod.ModDevice) r2
            L_0x020e:
                java.util.List r3 = r3.getConnectionsByDevice(r2)
                r6.writeNoException()
                r6.writeTypedList(r3)
                return r1
            L_0x0219:
                java.lang.String r4 = "com.motorola.mod.IModManager"
                r5.enforceInterface(r4)
                com.motorola.mod.IModAuthenticator r3 = r3.getAuthenticator()
                r6.writeNoException()
                if (r3 == 0) goto L_0x022b
                android.os.IBinder r2 = r3.asBinder()
            L_0x022b:
                r6.writeStrongBinder(r2)
                return r1
            L_0x022f:
                java.lang.String r4 = "com.motorola.mod.IModManager"
                r5.enforceInterface(r4)
                int[] r4 = r5.createIntArray()
                int r5 = r5.readInt()
                if (r5 == 0) goto L_0x023f
                r0 = r1
            L_0x023f:
                java.util.List r3 = r3.getModList(r4, r0)
                r6.writeNoException()
                r6.writeTypedList(r3)
                return r1
            L_0x024a:
                java.lang.String r4 = "com.motorola.mod.IModManager"
                r5.enforceInterface(r4)
                android.os.IBinder r4 = r5.readStrongBinder()
                com.motorola.mod.IModListener r4 = com.motorola.mod.IModListener.Stub.asInterface(r4)
                int[] r5 = r5.createIntArray()
                boolean r3 = r3.registerModListener(r4, r5)
                r6.writeNoException()
                r6.writeInt(r3)
                return r1
            L_0x0266:
                java.lang.String r3 = "com.motorola.mod.IModManager"
                r6.writeString(r3)
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: com.motorola.mod.IModManager.Stub.onTransact(int, android.os.Parcel, android.os.Parcel, int):boolean");
        }
    }

    void cancelModFirmwareUpdate(int i, int i2, String str) throws RemoteException;

    void changeDownloadPreference(int i, int i2, String str, boolean z) throws RemoteException;

    void checkForModFirmwareUpdates(int i, int i2, String str, boolean z) throws RemoteException;

    void downloadModFirmware(int i, int i2, String str, boolean z) throws RemoteException;

    IModAuthenticator getAuthenticator() throws RemoteException;

    IBinder getClassManager(ModDevice modDevice, int i) throws RemoteException;

    List<ModConnection> getConnectionsByDevice(ModDevice modDevice) throws RemoteException;

    int getDownloadPercent(int i, int i2, String str) throws RemoteException;

    List<ModInterfaceDelegation> getModInterfaceDelegationsByProtocol(ModDevice modDevice, int i) throws RemoteException;

    List<ModDevice> getModList(int[] iArr, boolean z) throws RemoteException;

    int[] getSupportedProtocols() throws RemoteException;

    boolean initSetupComplete(ModDevice modDevice) throws RemoteException;

    int installOtaPackage(ModDevice modDevice, Uri uri, boolean z) throws RemoteException;

    boolean isProtocolSupported(int i) throws RemoteException;

    ParcelFileDescriptor openModInterface(ModInterfaceDelegation modInterfaceDelegation, int i) throws RemoteException;

    boolean registerModListener(IModListener iModListener, int[] iArr) throws RemoteException;

    int requestUpdateFirmware(ModDevice modDevice, List<Uri> list) throws RemoteException;

    void reset(ModDevice modDevice) throws RemoteException;

    boolean setCurrentLimit(ModDevice modDevice, int i) throws RemoteException;

    boolean setUsbPriority(int i) throws RemoteException;
}
