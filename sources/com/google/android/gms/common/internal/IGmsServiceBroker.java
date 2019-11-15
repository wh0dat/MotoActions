package com.google.android.gms.common.internal;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IGmsServiceBroker extends IInterface {

    public static abstract class Stub extends Binder implements IGmsServiceBroker {

        private static class zza implements IGmsServiceBroker {
            private final IBinder zza;

            zza(IBinder iBinder) {
                this.zza = iBinder;
            }

            public final IBinder asBinder() {
                return this.zza;
            }

            public final void getService(IGmsCallbacks iGmsCallbacks, GetServiceRequest getServiceRequest) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                    obtain.writeStrongBinder(iGmsCallbacks != null ? iGmsCallbacks.asBinder() : null);
                    if (getServiceRequest != null) {
                        obtain.writeInt(1);
                        getServiceRequest.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zza.transact(46, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, "com.google.android.gms.common.internal.IGmsServiceBroker");
        }

        public static IGmsServiceBroker asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof IGmsServiceBroker)) ? new zza(iBinder) : (IGmsServiceBroker) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        /* access modifiers changed from: protected */
        public void getLegacyService(int i, IGmsCallbacks iGmsCallbacks, int i2, String str, String str2, String[] strArr, Bundle bundle, IBinder iBinder, String str3, String str4) throws RemoteException {
            throw new UnsupportedOperationException();
        }

        /* JADX WARNING: type inference failed for: r0v1 */
        /* JADX WARNING: type inference failed for: r10v0, types: [java.lang.String] */
        /* JADX WARNING: type inference failed for: r9v0, types: [java.lang.String] */
        /* JADX WARNING: type inference failed for: r8v0, types: [android.os.IBinder] */
        /* JADX WARNING: type inference failed for: r7v0, types: [android.os.Bundle] */
        /* JADX WARNING: type inference failed for: r6v0, types: [java.lang.String[]] */
        /* JADX WARNING: type inference failed for: r9v1 */
        /* JADX WARNING: type inference failed for: r8v1 */
        /* JADX WARNING: type inference failed for: r7v1 */
        /* JADX WARNING: type inference failed for: r6v1 */
        /* JADX WARNING: type inference failed for: r10v1 */
        /* JADX WARNING: type inference failed for: r8v2 */
        /* JADX WARNING: type inference failed for: r7v2 */
        /* JADX WARNING: type inference failed for: r6v2 */
        /* JADX WARNING: type inference failed for: r9v2 */
        /* JADX WARNING: type inference failed for: r7v3 */
        /* JADX WARNING: type inference failed for: r6v3 */
        /* JADX WARNING: type inference failed for: r8v3 */
        /* JADX WARNING: type inference failed for: r6v4 */
        /* JADX WARNING: type inference failed for: r7v4 */
        /* JADX WARNING: type inference failed for: r6v5 */
        /* JADX WARNING: type inference failed for: r13v2, types: [android.os.Bundle] */
        /* JADX WARNING: type inference failed for: r7v5 */
        /* JADX WARNING: type inference failed for: r6v6 */
        /* JADX WARNING: type inference failed for: r8v4 */
        /* JADX WARNING: type inference failed for: r15v12, types: [java.lang.String[]] */
        /* JADX WARNING: type inference failed for: r8v5 */
        /* JADX WARNING: type inference failed for: r7v6 */
        /* JADX WARNING: type inference failed for: r6v7 */
        /* JADX WARNING: type inference failed for: r9v3 */
        /* JADX WARNING: type inference failed for: r10v2 */
        /* JADX WARNING: type inference failed for: r6v8 */
        /* JADX WARNING: type inference failed for: r7v7 */
        /* JADX WARNING: type inference failed for: r8v6 */
        /* JADX WARNING: type inference failed for: r13v4, types: [android.os.Bundle] */
        /* JADX WARNING: type inference failed for: r7v8 */
        /* JADX WARNING: type inference failed for: r6v9 */
        /* JADX WARNING: type inference failed for: r8v7 */
        /* JADX WARNING: type inference failed for: r6v10 */
        /* JADX WARNING: type inference failed for: r10v3 */
        /* JADX WARNING: type inference failed for: r9v4 */
        /* JADX WARNING: type inference failed for: r8v8 */
        /* JADX WARNING: type inference failed for: r7v9 */
        /* JADX WARNING: type inference failed for: r1v2 */
        /* JADX WARNING: type inference failed for: r6v11 */
        /* JADX WARNING: type inference failed for: r15v16, types: [java.lang.String] */
        /* JADX WARNING: type inference failed for: r1v3, types: [java.lang.String[]] */
        /* JADX WARNING: type inference failed for: r9v5 */
        /* JADX WARNING: type inference failed for: r8v9 */
        /* JADX WARNING: type inference failed for: r7v10 */
        /* JADX WARNING: type inference failed for: r10v4 */
        /* JADX WARNING: type inference failed for: r9v6 */
        /* JADX WARNING: type inference failed for: r7v11 */
        /* JADX WARNING: type inference failed for: r8v10 */
        /* JADX WARNING: type inference failed for: r13v7, types: [android.os.Bundle] */
        /* JADX WARNING: type inference failed for: r7v12 */
        /* JADX WARNING: type inference failed for: r9v7 */
        /* JADX WARNING: type inference failed for: r8v11 */
        /* JADX WARNING: type inference failed for: r1v4, types: [java.lang.String[]] */
        /* JADX WARNING: type inference failed for: r5v13, types: [java.lang.String] */
        /* JADX WARNING: type inference failed for: r6v14, types: [android.os.IBinder] */
        /* JADX WARNING: type inference failed for: r7v13, types: [java.lang.String] */
        /* JADX WARNING: type inference failed for: r9v8 */
        /* JADX WARNING: type inference failed for: r8v13 */
        /* JADX WARNING: type inference failed for: r10v5 */
        /* JADX WARNING: type inference failed for: r7v14 */
        /* JADX WARNING: type inference failed for: r13v9, types: [android.os.Bundle] */
        /* JADX WARNING: type inference failed for: r9v9 */
        /* JADX WARNING: type inference failed for: r8v14 */
        /* JADX WARNING: type inference failed for: r10v6 */
        /* JADX WARNING: type inference failed for: r7v15 */
        /* JADX WARNING: type inference failed for: r13v10, types: [java.lang.String[]] */
        /* JADX WARNING: type inference failed for: r6v15 */
        /* JADX WARNING: type inference failed for: r7v16 */
        /* JADX WARNING: type inference failed for: r15v19, types: [android.os.IBinder] */
        /* JADX WARNING: type inference failed for: r8v15 */
        /* JADX WARNING: type inference failed for: r6v16 */
        /* JADX WARNING: type inference failed for: r7v17 */
        /* JADX WARNING: type inference failed for: r9v10 */
        /* JADX WARNING: type inference failed for: r13v12, types: [android.os.Bundle] */
        /* JADX WARNING: type inference failed for: r7v18 */
        /* JADX WARNING: type inference failed for: r8v16 */
        /* JADX WARNING: type inference failed for: r6v17 */
        /* JADX WARNING: type inference failed for: r9v11 */
        /* JADX WARNING: type inference failed for: r0v4, types: [com.google.android.gms.common.internal.ValidateAccountRequest] */
        /* JADX WARNING: type inference failed for: r0v6, types: [com.google.android.gms.common.internal.ValidateAccountRequest] */
        /* JADX WARNING: type inference failed for: r0v7, types: [com.google.android.gms.common.internal.GetServiceRequest] */
        /* JADX WARNING: type inference failed for: r0v9, types: [com.google.android.gms.common.internal.GetServiceRequest] */
        /* JADX WARNING: type inference failed for: r9v12 */
        /* JADX WARNING: type inference failed for: r8v17 */
        /* JADX WARNING: type inference failed for: r7v19 */
        /* JADX WARNING: type inference failed for: r6v18 */
        /* JADX WARNING: type inference failed for: r8v18 */
        /* JADX WARNING: type inference failed for: r7v20 */
        /* JADX WARNING: type inference failed for: r6v19 */
        /* JADX WARNING: type inference failed for: r7v21 */
        /* JADX WARNING: type inference failed for: r6v20 */
        /* JADX WARNING: type inference failed for: r6v21 */
        /* JADX WARNING: type inference failed for: r8v19 */
        /* JADX WARNING: type inference failed for: r7v22 */
        /* JADX WARNING: type inference failed for: r6v22 */
        /* JADX WARNING: type inference failed for: r9v13 */
        /* JADX WARNING: type inference failed for: r10v7 */
        /* JADX WARNING: type inference failed for: r9v14 */
        /* JADX WARNING: type inference failed for: r8v20 */
        /* JADX WARNING: type inference failed for: r7v23 */
        /* JADX WARNING: type inference failed for: r1v7 */
        /* JADX WARNING: type inference failed for: r9v15 */
        /* JADX WARNING: type inference failed for: r8v21 */
        /* JADX WARNING: type inference failed for: r7v24 */
        /* JADX WARNING: type inference failed for: r1v8 */
        /* JADX WARNING: type inference failed for: r1v9 */
        /* JADX WARNING: type inference failed for: r0v10 */
        /* JADX WARNING: type inference failed for: r0v11 */
        /* JADX WARNING: Code restructure failed: missing block: B:51:0x00fa, code lost:
            r6 = r1;
            r10 = r10;
            r9 = r9;
            r8 = r8;
            r7 = r7;
         */
        /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r0v1
          assigns: [?[int, float, boolean, short, byte, char, OBJECT, ARRAY], com.google.android.gms.common.internal.GetServiceRequest, com.google.android.gms.common.internal.ValidateAccountRequest]
          uses: [com.google.android.gms.common.internal.ValidateAccountRequest, com.google.android.gms.common.internal.GetServiceRequest]
          mth insns count: 161
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
        /* JADX WARNING: Unknown variable types count: 47 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean onTransact(int r12, android.os.Parcel r13, android.os.Parcel r14, int r15) throws android.os.RemoteException {
            /*
                r11 = this;
                r0 = 16777215(0xffffff, float:2.3509886E-38)
                if (r12 <= r0) goto L_0x000a
                boolean r11 = super.onTransact(r12, r13, r14, r15)
                return r11
            L_0x000a:
                java.lang.String r15 = "com.google.android.gms.common.internal.IGmsServiceBroker"
                r13.enforceInterface(r15)
                android.os.IBinder r15 = r13.readStrongBinder()
                com.google.android.gms.common.internal.IGmsCallbacks r2 = com.google.android.gms.common.internal.IGmsCallbacks.Stub.asInterface(r15)
                r15 = 46
                r0 = 0
                if (r12 != r15) goto L_0x0030
                int r12 = r13.readInt()
                if (r12 == 0) goto L_0x002b
                android.os.Parcelable$Creator<com.google.android.gms.common.internal.GetServiceRequest> r12 = com.google.android.gms.common.internal.GetServiceRequest.CREATOR
                java.lang.Object r12 = r12.createFromParcel(r13)
                r0 = r12
                com.google.android.gms.common.internal.GetServiceRequest r0 = (com.google.android.gms.common.internal.GetServiceRequest) r0
            L_0x002b:
                r11.getService(r2, r0)
                goto L_0x0142
            L_0x0030:
                r15 = 47
                if (r12 != r15) goto L_0x0048
                int r12 = r13.readInt()
                if (r12 == 0) goto L_0x0043
                android.os.Parcelable$Creator<com.google.android.gms.common.internal.ValidateAccountRequest> r12 = com.google.android.gms.common.internal.ValidateAccountRequest.CREATOR
                java.lang.Object r12 = r12.createFromParcel(r13)
                r0 = r12
                com.google.android.gms.common.internal.ValidateAccountRequest r0 = (com.google.android.gms.common.internal.ValidateAccountRequest) r0
            L_0x0043:
                r11.validateAccount(r2, r0)
                goto L_0x0142
            L_0x0048:
                int r3 = r13.readInt()
                r15 = 4
                if (r12 == r15) goto L_0x0055
                java.lang.String r15 = r13.readString()
                r4 = r15
                goto L_0x0056
            L_0x0055:
                r4 = r0
            L_0x0056:
                r15 = 23
                if (r12 == r15) goto L_0x0124
                r15 = 25
                if (r12 == r15) goto L_0x0124
                r15 = 27
                if (r12 == r15) goto L_0x0124
                r15 = 30
                if (r12 == r15) goto L_0x0103
                r15 = 34
                if (r12 == r15) goto L_0x00fc
                r15 = 41
                if (r12 == r15) goto L_0x0124
                r15 = 43
                if (r12 == r15) goto L_0x0124
                switch(r12) {
                    case 1: goto L_0x00d8;
                    case 2: goto L_0x0124;
                    default: goto L_0x0075;
                }
            L_0x0075:
                switch(r12) {
                    case 5: goto L_0x0124;
                    case 6: goto L_0x0124;
                    case 7: goto L_0x0124;
                    case 8: goto L_0x0124;
                    case 9: goto L_0x00aa;
                    case 10: goto L_0x009d;
                    case 11: goto L_0x0124;
                    case 12: goto L_0x0124;
                    case 13: goto L_0x0124;
                    case 14: goto L_0x0124;
                    case 15: goto L_0x0124;
                    case 16: goto L_0x0124;
                    case 17: goto L_0x0124;
                    case 18: goto L_0x0124;
                    case 19: goto L_0x007d;
                    case 20: goto L_0x0103;
                    default: goto L_0x0078;
                }
            L_0x0078:
                switch(r12) {
                    case 37: goto L_0x0124;
                    case 38: goto L_0x0124;
                    default: goto L_0x007b;
                }
            L_0x007b:
                goto L_0x0137
            L_0x007d:
                android.os.IBinder r15 = r13.readStrongBinder()
                int r1 = r13.readInt()
                if (r1 == 0) goto L_0x0096
                android.os.Parcelable$Creator r1 = android.os.Bundle.CREATOR
                java.lang.Object r13 = r1.createFromParcel(r13)
                android.os.Bundle r13 = (android.os.Bundle) r13
                r7 = r13
                r8 = r15
                r5 = r0
                r6 = r5
                r9 = r6
                goto L_0x013c
            L_0x0096:
                r8 = r15
                r5 = r0
                r6 = r5
                r7 = r6
                r9 = r7
                goto L_0x013c
            L_0x009d:
                java.lang.String r15 = r13.readString()
                java.lang.String[] r13 = r13.createStringArray()
                r6 = r13
                r5 = r15
                r7 = r0
                goto L_0x013a
            L_0x00aa:
                java.lang.String r15 = r13.readString()
                java.lang.String[] r1 = r13.createStringArray()
                java.lang.String r5 = r13.readString()
                android.os.IBinder r6 = r13.readStrongBinder()
                java.lang.String r7 = r13.readString()
                int r8 = r13.readInt()
                if (r8 == 0) goto L_0x00d2
                android.os.Parcelable$Creator r0 = android.os.Bundle.CREATOR
                java.lang.Object r13 = r0.createFromParcel(r13)
                android.os.Bundle r13 = (android.os.Bundle) r13
                r9 = r5
                r8 = r6
                r10 = r7
                r7 = r13
                r5 = r15
                goto L_0x00fa
            L_0x00d2:
                r9 = r5
                r8 = r6
                r10 = r7
                r5 = r15
                r7 = r0
                goto L_0x00fa
            L_0x00d8:
                java.lang.String r15 = r13.readString()
                java.lang.String[] r1 = r13.createStringArray()
                java.lang.String r5 = r13.readString()
                int r6 = r13.readInt()
                if (r6 == 0) goto L_0x00f6
                android.os.Parcelable$Creator r6 = android.os.Bundle.CREATOR
                java.lang.Object r13 = r6.createFromParcel(r13)
                android.os.Bundle r13 = (android.os.Bundle) r13
                r7 = r13
                r9 = r15
                r8 = r0
                goto L_0x00f9
            L_0x00f6:
                r9 = r15
                r7 = r0
                r8 = r7
            L_0x00f9:
                r10 = r8
            L_0x00fa:
                r6 = r1
                goto L_0x013d
            L_0x00fc:
                java.lang.String r13 = r13.readString()
                r5 = r13
                r6 = r0
                goto L_0x0139
            L_0x0103:
                java.lang.String[] r15 = r13.createStringArray()
                java.lang.String r1 = r13.readString()
                int r5 = r13.readInt()
                if (r5 == 0) goto L_0x011d
                android.os.Parcelable$Creator r5 = android.os.Bundle.CREATOR
                java.lang.Object r13 = r5.createFromParcel(r13)
                android.os.Bundle r13 = (android.os.Bundle) r13
                r7 = r13
                r6 = r15
                r8 = r0
                goto L_0x0120
            L_0x011d:
                r6 = r15
                r7 = r0
                r8 = r7
            L_0x0120:
                r9 = r8
                r10 = r9
                r5 = r1
                goto L_0x013d
            L_0x0124:
                int r15 = r13.readInt()
                if (r15 == 0) goto L_0x0137
                android.os.Parcelable$Creator r15 = android.os.Bundle.CREATOR
                java.lang.Object r13 = r15.createFromParcel(r13)
                android.os.Bundle r13 = (android.os.Bundle) r13
                r7 = r13
                r5 = r0
                r6 = r5
                r8 = r6
                goto L_0x013b
            L_0x0137:
                r5 = r0
                r6 = r5
            L_0x0139:
                r7 = r6
            L_0x013a:
                r8 = r7
            L_0x013b:
                r9 = r8
            L_0x013c:
                r10 = r9
            L_0x013d:
                r0 = r11
                r1 = r12
                r0.getLegacyService(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10)
            L_0x0142:
                r14.writeNoException()
                r11 = 1
                return r11
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.internal.IGmsServiceBroker.Stub.onTransact(int, android.os.Parcel, android.os.Parcel, int):boolean");
        }

        /* access modifiers changed from: protected */
        public void validateAccount(IGmsCallbacks iGmsCallbacks, ValidateAccountRequest validateAccountRequest) throws RemoteException {
            throw new UnsupportedOperationException();
        }
    }

    void getService(IGmsCallbacks iGmsCallbacks, GetServiceRequest getServiceRequest) throws RemoteException;
}
