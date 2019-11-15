package com.motorola.mod;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.motorola.mod.ModDevice.Interface;

public interface IModListener extends IInterface {

    public static abstract class Stub extends Binder implements IModListener {

        /* renamed from: com.motorola.mod.IModListener$Stub$a */
        private static class C0643a implements IModListener {

            /* renamed from: a */
            private IBinder f52a;

            C0643a(IBinder iBinder) {
                this.f52a = iBinder;
            }

            public IBinder asBinder() {
                return this.f52a;
            }

            public void onDeviceHotplug(ModDevice modDevice, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.motorola.mod.IModListener");
                    if (modDevice != null) {
                        obtain.writeInt(1);
                        modDevice.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeInt(z ? 1 : 0);
                    this.f52a.transact(1, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onInterfaceUpDown(ModDevice modDevice, Interface interfaceR, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.motorola.mod.IModListener");
                    if (modDevice != null) {
                        obtain.writeInt(1);
                        modDevice.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (interfaceR != null) {
                        obtain.writeInt(1);
                        interfaceR.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeInt(z ? 1 : 0);
                    this.f52a.transact(2, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onConnectionStatusChanged(ModDevice modDevice, ModConnection modConnection) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.motorola.mod.IModListener");
                    if (modDevice != null) {
                        obtain.writeInt(1);
                        modDevice.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (modConnection != null) {
                        obtain.writeInt(1);
                        modConnection.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.f52a.transact(3, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onLinuxDeviceChanged(ModDevice modDevice, ModInterfaceDelegation modInterfaceDelegation, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.motorola.mod.IModListener");
                    if (modDevice != null) {
                        obtain.writeInt(1);
                        modDevice.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (modInterfaceDelegation != null) {
                        obtain.writeInt(1);
                        modInterfaceDelegation.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeInt(z ? 1 : 0);
                    this.f52a.transact(4, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onEnumerationDoneD(ModDevice modDevice) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.motorola.mod.IModListener");
                    if (modDevice != null) {
                        obtain.writeInt(1);
                        modDevice.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.f52a.transact(5, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onEnumerationDoneI(ModDevice modDevice, Interface interfaceR, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.motorola.mod.IModListener");
                    if (modDevice != null) {
                        obtain.writeInt(1);
                        modDevice.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (interfaceR != null) {
                        obtain.writeInt(1);
                        interfaceR.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeInt(z ? 1 : 0);
                    this.f52a.transact(6, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onCapabilityChanged(ModDevice modDevice) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.motorola.mod.IModListener");
                    if (modDevice != null) {
                        obtain.writeInt(1);
                        modDevice.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.f52a.transact(7, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, "com.motorola.mod.IModListener");
        }

        public static IModListener asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.motorola.mod.IModListener");
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IModListener)) {
                return new C0643a(iBinder);
            }
            return (IModListener) queryLocalInterface;
        }

        /* JADX WARNING: type inference failed for: r2v0 */
        /* JADX WARNING: type inference failed for: r2v1, types: [com.motorola.mod.ModDevice] */
        /* JADX WARNING: type inference failed for: r2v3, types: [com.motorola.mod.ModDevice] */
        /* JADX WARNING: type inference failed for: r2v4, types: [com.motorola.mod.ModDevice$Interface] */
        /* JADX WARNING: type inference failed for: r2v6, types: [com.motorola.mod.ModDevice$Interface] */
        /* JADX WARNING: type inference failed for: r2v7, types: [com.motorola.mod.ModConnection] */
        /* JADX WARNING: type inference failed for: r2v9, types: [com.motorola.mod.ModConnection] */
        /* JADX WARNING: type inference failed for: r2v10, types: [com.motorola.mod.ModInterfaceDelegation] */
        /* JADX WARNING: type inference failed for: r2v12, types: [com.motorola.mod.ModInterfaceDelegation] */
        /* JADX WARNING: type inference failed for: r2v13, types: [com.motorola.mod.ModDevice] */
        /* JADX WARNING: type inference failed for: r2v15, types: [com.motorola.mod.ModDevice] */
        /* JADX WARNING: type inference failed for: r2v16, types: [com.motorola.mod.ModDevice$Interface] */
        /* JADX WARNING: type inference failed for: r2v18, types: [com.motorola.mod.ModDevice$Interface] */
        /* JADX WARNING: type inference failed for: r2v19, types: [com.motorola.mod.ModDevice] */
        /* JADX WARNING: type inference failed for: r2v21, types: [com.motorola.mod.ModDevice] */
        /* JADX WARNING: type inference failed for: r2v22 */
        /* JADX WARNING: type inference failed for: r2v23 */
        /* JADX WARNING: type inference failed for: r2v24 */
        /* JADX WARNING: type inference failed for: r2v25 */
        /* JADX WARNING: type inference failed for: r2v26 */
        /* JADX WARNING: type inference failed for: r2v27 */
        /* JADX WARNING: type inference failed for: r2v28 */
        /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r2v0
          assigns: [?[int, float, boolean, short, byte, char, OBJECT, ARRAY], com.motorola.mod.ModDevice$Interface, com.motorola.mod.ModDevice, com.motorola.mod.ModConnection, com.motorola.mod.ModInterfaceDelegation]
          uses: [com.motorola.mod.ModDevice, com.motorola.mod.ModDevice$Interface, com.motorola.mod.ModConnection, com.motorola.mod.ModInterfaceDelegation]
          mth insns count: 122
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
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
         */
        /* JADX WARNING: Unknown variable types count: 8 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean onTransact(int r4, android.os.Parcel r5, android.os.Parcel r6, int r7) throws android.os.RemoteException {
            /*
                r3 = this;
                r0 = 1598968902(0x5f4e5446, float:1.4867585E19)
                r1 = 1
                if (r4 == r0) goto L_0x0114
                r0 = 0
                r2 = 0
                switch(r4) {
                    case 1: goto L_0x00f5;
                    case 2: goto L_0x00c6;
                    case 3: goto L_0x009e;
                    case 4: goto L_0x006f;
                    case 5: goto L_0x0057;
                    case 6: goto L_0x0028;
                    case 7: goto L_0x0010;
                    default: goto L_0x000b;
                }
            L_0x000b:
                boolean r3 = super.onTransact(r4, r5, r6, r7)
                return r3
            L_0x0010:
                java.lang.String r4 = "com.motorola.mod.IModListener"
                r5.enforceInterface(r4)
                int r4 = r5.readInt()
                if (r4 == 0) goto L_0x0024
                android.os.Parcelable$Creator<com.motorola.mod.ModDevice> r4 = com.motorola.mod.ModDevice.CREATOR
                java.lang.Object r4 = r4.createFromParcel(r5)
                r2 = r4
                com.motorola.mod.ModDevice r2 = (com.motorola.mod.ModDevice) r2
            L_0x0024:
                r3.onCapabilityChanged(r2)
                return r1
            L_0x0028:
                java.lang.String r4 = "com.motorola.mod.IModListener"
                r5.enforceInterface(r4)
                int r4 = r5.readInt()
                if (r4 == 0) goto L_0x003c
                android.os.Parcelable$Creator<com.motorola.mod.ModDevice> r4 = com.motorola.mod.ModDevice.CREATOR
                java.lang.Object r4 = r4.createFromParcel(r5)
                com.motorola.mod.ModDevice r4 = (com.motorola.mod.ModDevice) r4
                goto L_0x003d
            L_0x003c:
                r4 = r2
            L_0x003d:
                int r6 = r5.readInt()
                if (r6 == 0) goto L_0x004c
                android.os.Parcelable$Creator<com.motorola.mod.ModDevice$Interface> r6 = com.motorola.mod.ModDevice.Interface.CREATOR
                java.lang.Object r6 = r6.createFromParcel(r5)
                r2 = r6
                com.motorola.mod.ModDevice$Interface r2 = (com.motorola.mod.ModDevice.Interface) r2
            L_0x004c:
                int r5 = r5.readInt()
                if (r5 == 0) goto L_0x0053
                r0 = r1
            L_0x0053:
                r3.onEnumerationDoneI(r4, r2, r0)
                return r1
            L_0x0057:
                java.lang.String r4 = "com.motorola.mod.IModListener"
                r5.enforceInterface(r4)
                int r4 = r5.readInt()
                if (r4 == 0) goto L_0x006b
                android.os.Parcelable$Creator<com.motorola.mod.ModDevice> r4 = com.motorola.mod.ModDevice.CREATOR
                java.lang.Object r4 = r4.createFromParcel(r5)
                r2 = r4
                com.motorola.mod.ModDevice r2 = (com.motorola.mod.ModDevice) r2
            L_0x006b:
                r3.onEnumerationDoneD(r2)
                return r1
            L_0x006f:
                java.lang.String r4 = "com.motorola.mod.IModListener"
                r5.enforceInterface(r4)
                int r4 = r5.readInt()
                if (r4 == 0) goto L_0x0083
                android.os.Parcelable$Creator<com.motorola.mod.ModDevice> r4 = com.motorola.mod.ModDevice.CREATOR
                java.lang.Object r4 = r4.createFromParcel(r5)
                com.motorola.mod.ModDevice r4 = (com.motorola.mod.ModDevice) r4
                goto L_0x0084
            L_0x0083:
                r4 = r2
            L_0x0084:
                int r6 = r5.readInt()
                if (r6 == 0) goto L_0x0093
                android.os.Parcelable$Creator<com.motorola.mod.ModInterfaceDelegation> r6 = com.motorola.mod.ModInterfaceDelegation.CREATOR
                java.lang.Object r6 = r6.createFromParcel(r5)
                r2 = r6
                com.motorola.mod.ModInterfaceDelegation r2 = (com.motorola.mod.ModInterfaceDelegation) r2
            L_0x0093:
                int r5 = r5.readInt()
                if (r5 == 0) goto L_0x009a
                r0 = r1
            L_0x009a:
                r3.onLinuxDeviceChanged(r4, r2, r0)
                return r1
            L_0x009e:
                java.lang.String r4 = "com.motorola.mod.IModListener"
                r5.enforceInterface(r4)
                int r4 = r5.readInt()
                if (r4 == 0) goto L_0x00b2
                android.os.Parcelable$Creator<com.motorola.mod.ModDevice> r4 = com.motorola.mod.ModDevice.CREATOR
                java.lang.Object r4 = r4.createFromParcel(r5)
                com.motorola.mod.ModDevice r4 = (com.motorola.mod.ModDevice) r4
                goto L_0x00b3
            L_0x00b2:
                r4 = r2
            L_0x00b3:
                int r6 = r5.readInt()
                if (r6 == 0) goto L_0x00c2
                android.os.Parcelable$Creator<com.motorola.mod.ModConnection> r6 = com.motorola.mod.ModConnection.CREATOR
                java.lang.Object r5 = r6.createFromParcel(r5)
                r2 = r5
                com.motorola.mod.ModConnection r2 = (com.motorola.mod.ModConnection) r2
            L_0x00c2:
                r3.onConnectionStatusChanged(r4, r2)
                return r1
            L_0x00c6:
                java.lang.String r4 = "com.motorola.mod.IModListener"
                r5.enforceInterface(r4)
                int r4 = r5.readInt()
                if (r4 == 0) goto L_0x00da
                android.os.Parcelable$Creator<com.motorola.mod.ModDevice> r4 = com.motorola.mod.ModDevice.CREATOR
                java.lang.Object r4 = r4.createFromParcel(r5)
                com.motorola.mod.ModDevice r4 = (com.motorola.mod.ModDevice) r4
                goto L_0x00db
            L_0x00da:
                r4 = r2
            L_0x00db:
                int r6 = r5.readInt()
                if (r6 == 0) goto L_0x00ea
                android.os.Parcelable$Creator<com.motorola.mod.ModDevice$Interface> r6 = com.motorola.mod.ModDevice.Interface.CREATOR
                java.lang.Object r6 = r6.createFromParcel(r5)
                r2 = r6
                com.motorola.mod.ModDevice$Interface r2 = (com.motorola.mod.ModDevice.Interface) r2
            L_0x00ea:
                int r5 = r5.readInt()
                if (r5 == 0) goto L_0x00f1
                r0 = r1
            L_0x00f1:
                r3.onInterfaceUpDown(r4, r2, r0)
                return r1
            L_0x00f5:
                java.lang.String r4 = "com.motorola.mod.IModListener"
                r5.enforceInterface(r4)
                int r4 = r5.readInt()
                if (r4 == 0) goto L_0x0109
                android.os.Parcelable$Creator<com.motorola.mod.ModDevice> r4 = com.motorola.mod.ModDevice.CREATOR
                java.lang.Object r4 = r4.createFromParcel(r5)
                r2 = r4
                com.motorola.mod.ModDevice r2 = (com.motorola.mod.ModDevice) r2
            L_0x0109:
                int r4 = r5.readInt()
                if (r4 == 0) goto L_0x0110
                r0 = r1
            L_0x0110:
                r3.onDeviceHotplug(r2, r0)
                return r1
            L_0x0114:
                java.lang.String r3 = "com.motorola.mod.IModListener"
                r6.writeString(r3)
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: com.motorola.mod.IModListener.Stub.onTransact(int, android.os.Parcel, android.os.Parcel, int):boolean");
        }
    }

    void onCapabilityChanged(ModDevice modDevice) throws RemoteException;

    void onConnectionStatusChanged(ModDevice modDevice, ModConnection modConnection) throws RemoteException;

    void onDeviceHotplug(ModDevice modDevice, boolean z) throws RemoteException;

    void onEnumerationDoneD(ModDevice modDevice) throws RemoteException;

    void onEnumerationDoneI(ModDevice modDevice, Interface interfaceR, boolean z) throws RemoteException;

    void onInterfaceUpDown(ModDevice modDevice, Interface interfaceR, boolean z) throws RemoteException;

    void onLinuxDeviceChanged(ModDevice modDevice, ModInterfaceDelegation modInterfaceDelegation, boolean z) throws RemoteException;
}
