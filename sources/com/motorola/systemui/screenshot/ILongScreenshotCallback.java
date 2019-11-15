package com.motorola.systemui.screenshot;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.KeyEvent;
import android.view.MotionEvent;

public interface ILongScreenshotCallback extends IInterface {

    public static abstract class Stub extends Binder implements ILongScreenshotCallback {
        private static final String DESCRIPTOR = "com.motorola.systemui.screenshot.ILongScreenshotCallback";
        static final int TRANSACTION_onKeyEvent = 2;
        static final int TRANSACTION_onMotionEvent = 3;
        static final int TRANSACTION_onScrollFinished = 1;

        private static class Proxy implements ILongScreenshotCallback {
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

            public void onScrollFinished() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onKeyEvent(KeyEvent keyEvent) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (keyEvent != null) {
                        obtain.writeInt(1);
                        keyEvent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onMotionEvent(MotionEvent motionEvent) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (motionEvent != null) {
                        obtain.writeInt(1);
                        motionEvent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(3, obtain, obtain2, 0);
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

        public static ILongScreenshotCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof ILongScreenshotCallback)) {
                return new Proxy(iBinder);
            }
            return (ILongScreenshotCallback) queryLocalInterface;
        }

        /* JADX WARNING: type inference failed for: r0v1 */
        /* JADX WARNING: type inference failed for: r0v2, types: [android.view.KeyEvent] */
        /* JADX WARNING: type inference failed for: r0v4, types: [android.view.KeyEvent] */
        /* JADX WARNING: type inference failed for: r0v5, types: [android.view.MotionEvent] */
        /* JADX WARNING: type inference failed for: r0v7, types: [android.view.MotionEvent] */
        /* JADX WARNING: type inference failed for: r0v8 */
        /* JADX WARNING: type inference failed for: r0v9 */
        /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r0v1
          assigns: [?[int, float, boolean, short, byte, char, OBJECT, ARRAY], android.view.MotionEvent, android.view.KeyEvent]
          uses: [android.view.KeyEvent, android.view.MotionEvent]
          mth insns count: 37
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
        /* JADX WARNING: Unknown variable types count: 3 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean onTransact(int r3, android.os.Parcel r4, android.os.Parcel r5, int r6) throws android.os.RemoteException {
            /*
                r2 = this;
                r0 = 1598968902(0x5f4e5446, float:1.4867585E19)
                r1 = 1
                if (r3 == r0) goto L_0x0051
                r0 = 0
                switch(r3) {
                    case 1: goto L_0x0045;
                    case 2: goto L_0x002a;
                    case 3: goto L_0x000f;
                    default: goto L_0x000a;
                }
            L_0x000a:
                boolean r2 = super.onTransact(r3, r4, r5, r6)
                return r2
            L_0x000f:
                java.lang.String r3 = "com.motorola.systemui.screenshot.ILongScreenshotCallback"
                r4.enforceInterface(r3)
                int r3 = r4.readInt()
                if (r3 == 0) goto L_0x0023
                android.os.Parcelable$Creator r3 = android.view.MotionEvent.CREATOR
                java.lang.Object r3 = r3.createFromParcel(r4)
                r0 = r3
                android.view.MotionEvent r0 = (android.view.MotionEvent) r0
            L_0x0023:
                r2.onMotionEvent(r0)
                r5.writeNoException()
                return r1
            L_0x002a:
                java.lang.String r3 = "com.motorola.systemui.screenshot.ILongScreenshotCallback"
                r4.enforceInterface(r3)
                int r3 = r4.readInt()
                if (r3 == 0) goto L_0x003e
                android.os.Parcelable$Creator r3 = android.view.KeyEvent.CREATOR
                java.lang.Object r3 = r3.createFromParcel(r4)
                r0 = r3
                android.view.KeyEvent r0 = (android.view.KeyEvent) r0
            L_0x003e:
                r2.onKeyEvent(r0)
                r5.writeNoException()
                return r1
            L_0x0045:
                java.lang.String r3 = "com.motorola.systemui.screenshot.ILongScreenshotCallback"
                r4.enforceInterface(r3)
                r2.onScrollFinished()
                r5.writeNoException()
                return r1
            L_0x0051:
                java.lang.String r2 = "com.motorola.systemui.screenshot.ILongScreenshotCallback"
                r5.writeString(r2)
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: com.motorola.systemui.screenshot.ILongScreenshotCallback.Stub.onTransact(int, android.os.Parcel, android.os.Parcel, int):boolean");
        }
    }

    void onKeyEvent(KeyEvent keyEvent) throws RemoteException;

    void onMotionEvent(MotionEvent motionEvent) throws RemoteException;

    void onScrollFinished() throws RemoteException;
}
