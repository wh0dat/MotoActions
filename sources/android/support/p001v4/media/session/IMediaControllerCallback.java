package android.support.p001v4.media.session;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.p001v4.media.MediaMetadataCompat;
import android.support.p001v4.media.session.MediaSessionCompat.QueueItem;
import android.text.TextUtils;
import java.util.List;

/* renamed from: android.support.v4.media.session.IMediaControllerCallback */
public interface IMediaControllerCallback extends IInterface {

    /* renamed from: android.support.v4.media.session.IMediaControllerCallback$Stub */
    public static abstract class Stub extends Binder implements IMediaControllerCallback {
        private static final String DESCRIPTOR = "android.support.v4.media.session.IMediaControllerCallback";
        static final int TRANSACTION_onCaptioningEnabledChanged = 11;
        static final int TRANSACTION_onEvent = 1;
        static final int TRANSACTION_onExtrasChanged = 7;
        static final int TRANSACTION_onMetadataChanged = 4;
        static final int TRANSACTION_onPlaybackStateChanged = 3;
        static final int TRANSACTION_onQueueChanged = 5;
        static final int TRANSACTION_onQueueTitleChanged = 6;
        static final int TRANSACTION_onRepeatModeChanged = 9;
        static final int TRANSACTION_onSessionDestroyed = 2;
        static final int TRANSACTION_onShuffleModeChanged = 12;
        static final int TRANSACTION_onShuffleModeChangedDeprecated = 10;
        static final int TRANSACTION_onVolumeInfoChanged = 8;

        /* renamed from: android.support.v4.media.session.IMediaControllerCallback$Stub$Proxy */
        private static class Proxy implements IMediaControllerCallback {
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

            public void onEvent(String str, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(1, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onSessionDestroyed() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(2, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onPlaybackStateChanged(PlaybackStateCompat playbackStateCompat) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (playbackStateCompat != null) {
                        obtain.writeInt(1);
                        playbackStateCompat.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(3, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onMetadataChanged(MediaMetadataCompat mediaMetadataCompat) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (mediaMetadataCompat != null) {
                        obtain.writeInt(1);
                        mediaMetadataCompat.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(4, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onQueueChanged(List<QueueItem> list) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedList(list);
                    this.mRemote.transact(5, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onQueueTitleChanged(CharSequence charSequence) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (charSequence != null) {
                        obtain.writeInt(1);
                        TextUtils.writeToParcel(charSequence, obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(6, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onExtrasChanged(Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(7, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onVolumeInfoChanged(ParcelableVolumeInfo parcelableVolumeInfo) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (parcelableVolumeInfo != null) {
                        obtain.writeInt(1);
                        parcelableVolumeInfo.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(8, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onRepeatModeChanged(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(9, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onShuffleModeChangedDeprecated(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(z ? 1 : 0);
                    this.mRemote.transact(10, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onCaptioningEnabledChanged(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(z ? 1 : 0);
                    this.mRemote.transact(11, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onShuffleModeChanged(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(12, obtain, null, 1);
                } finally {
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

        public static IMediaControllerCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IMediaControllerCallback)) {
                return new Proxy(iBinder);
            }
            return (IMediaControllerCallback) queryLocalInterface;
        }

        /* JADX WARNING: type inference failed for: r2v0 */
        /* JADX WARNING: type inference failed for: r2v1, types: [android.os.Bundle] */
        /* JADX WARNING: type inference failed for: r2v3, types: [android.os.Bundle] */
        /* JADX WARNING: type inference failed for: r2v4, types: [android.support.v4.media.session.PlaybackStateCompat] */
        /* JADX WARNING: type inference failed for: r2v6, types: [android.support.v4.media.session.PlaybackStateCompat] */
        /* JADX WARNING: type inference failed for: r2v7, types: [android.support.v4.media.MediaMetadataCompat] */
        /* JADX WARNING: type inference failed for: r2v9, types: [android.support.v4.media.MediaMetadataCompat] */
        /* JADX WARNING: type inference failed for: r2v10, types: [java.lang.CharSequence] */
        /* JADX WARNING: type inference failed for: r2v12, types: [java.lang.CharSequence] */
        /* JADX WARNING: type inference failed for: r2v13, types: [android.os.Bundle] */
        /* JADX WARNING: type inference failed for: r2v15, types: [android.os.Bundle] */
        /* JADX WARNING: type inference failed for: r2v16, types: [android.support.v4.media.session.ParcelableVolumeInfo] */
        /* JADX WARNING: type inference failed for: r2v18, types: [android.support.v4.media.session.ParcelableVolumeInfo] */
        /* JADX WARNING: type inference failed for: r2v19 */
        /* JADX WARNING: type inference failed for: r2v20 */
        /* JADX WARNING: type inference failed for: r2v21 */
        /* JADX WARNING: type inference failed for: r2v22 */
        /* JADX WARNING: type inference failed for: r2v23 */
        /* JADX WARNING: type inference failed for: r2v24 */
        /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r2v0
          assigns: [?[int, float, boolean, short, byte, char, OBJECT, ARRAY], android.support.v4.media.session.PlaybackStateCompat, android.os.Bundle, android.support.v4.media.MediaMetadataCompat, java.lang.CharSequence, android.support.v4.media.session.ParcelableVolumeInfo]
          uses: [android.os.Bundle, android.support.v4.media.session.PlaybackStateCompat, android.support.v4.media.MediaMetadataCompat, java.lang.CharSequence, android.support.v4.media.session.ParcelableVolumeInfo]
          mth insns count: 110
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
        /* JADX WARNING: Unknown variable types count: 7 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean onTransact(int r4, android.os.Parcel r5, android.os.Parcel r6, int r7) throws android.os.RemoteException {
            /*
                r3 = this;
                r0 = 1598968902(0x5f4e5446, float:1.4867585E19)
                r1 = 1
                if (r4 == r0) goto L_0x00f6
                r0 = 0
                r2 = 0
                switch(r4) {
                    case 1: goto L_0x00da;
                    case 2: goto L_0x00d1;
                    case 3: goto L_0x00b9;
                    case 4: goto L_0x00a1;
                    case 5: goto L_0x0092;
                    case 6: goto L_0x007a;
                    case 7: goto L_0x0062;
                    case 8: goto L_0x004a;
                    case 9: goto L_0x003d;
                    case 10: goto L_0x002d;
                    case 11: goto L_0x001d;
                    case 12: goto L_0x0010;
                    default: goto L_0x000b;
                }
            L_0x000b:
                boolean r3 = super.onTransact(r4, r5, r6, r7)
                return r3
            L_0x0010:
                java.lang.String r4 = "android.support.v4.media.session.IMediaControllerCallback"
                r5.enforceInterface(r4)
                int r4 = r5.readInt()
                r3.onShuffleModeChanged(r4)
                return r1
            L_0x001d:
                java.lang.String r4 = "android.support.v4.media.session.IMediaControllerCallback"
                r5.enforceInterface(r4)
                int r4 = r5.readInt()
                if (r4 == 0) goto L_0x0029
                r0 = r1
            L_0x0029:
                r3.onCaptioningEnabledChanged(r0)
                return r1
            L_0x002d:
                java.lang.String r4 = "android.support.v4.media.session.IMediaControllerCallback"
                r5.enforceInterface(r4)
                int r4 = r5.readInt()
                if (r4 == 0) goto L_0x0039
                r0 = r1
            L_0x0039:
                r3.onShuffleModeChangedDeprecated(r0)
                return r1
            L_0x003d:
                java.lang.String r4 = "android.support.v4.media.session.IMediaControllerCallback"
                r5.enforceInterface(r4)
                int r4 = r5.readInt()
                r3.onRepeatModeChanged(r4)
                return r1
            L_0x004a:
                java.lang.String r4 = "android.support.v4.media.session.IMediaControllerCallback"
                r5.enforceInterface(r4)
                int r4 = r5.readInt()
                if (r4 == 0) goto L_0x005e
                android.os.Parcelable$Creator<android.support.v4.media.session.ParcelableVolumeInfo> r4 = android.support.p001v4.media.session.ParcelableVolumeInfo.CREATOR
                java.lang.Object r4 = r4.createFromParcel(r5)
                r2 = r4
                android.support.v4.media.session.ParcelableVolumeInfo r2 = (android.support.p001v4.media.session.ParcelableVolumeInfo) r2
            L_0x005e:
                r3.onVolumeInfoChanged(r2)
                return r1
            L_0x0062:
                java.lang.String r4 = "android.support.v4.media.session.IMediaControllerCallback"
                r5.enforceInterface(r4)
                int r4 = r5.readInt()
                if (r4 == 0) goto L_0x0076
                android.os.Parcelable$Creator r4 = android.os.Bundle.CREATOR
                java.lang.Object r4 = r4.createFromParcel(r5)
                r2 = r4
                android.os.Bundle r2 = (android.os.Bundle) r2
            L_0x0076:
                r3.onExtrasChanged(r2)
                return r1
            L_0x007a:
                java.lang.String r4 = "android.support.v4.media.session.IMediaControllerCallback"
                r5.enforceInterface(r4)
                int r4 = r5.readInt()
                if (r4 == 0) goto L_0x008e
                android.os.Parcelable$Creator r4 = android.text.TextUtils.CHAR_SEQUENCE_CREATOR
                java.lang.Object r4 = r4.createFromParcel(r5)
                r2 = r4
                java.lang.CharSequence r2 = (java.lang.CharSequence) r2
            L_0x008e:
                r3.onQueueTitleChanged(r2)
                return r1
            L_0x0092:
                java.lang.String r4 = "android.support.v4.media.session.IMediaControllerCallback"
                r5.enforceInterface(r4)
                android.os.Parcelable$Creator<android.support.v4.media.session.MediaSessionCompat$QueueItem> r4 = android.support.p001v4.media.session.MediaSessionCompat.QueueItem.CREATOR
                java.util.ArrayList r4 = r5.createTypedArrayList(r4)
                r3.onQueueChanged(r4)
                return r1
            L_0x00a1:
                java.lang.String r4 = "android.support.v4.media.session.IMediaControllerCallback"
                r5.enforceInterface(r4)
                int r4 = r5.readInt()
                if (r4 == 0) goto L_0x00b5
                android.os.Parcelable$Creator<android.support.v4.media.MediaMetadataCompat> r4 = android.support.p001v4.media.MediaMetadataCompat.CREATOR
                java.lang.Object r4 = r4.createFromParcel(r5)
                r2 = r4
                android.support.v4.media.MediaMetadataCompat r2 = (android.support.p001v4.media.MediaMetadataCompat) r2
            L_0x00b5:
                r3.onMetadataChanged(r2)
                return r1
            L_0x00b9:
                java.lang.String r4 = "android.support.v4.media.session.IMediaControllerCallback"
                r5.enforceInterface(r4)
                int r4 = r5.readInt()
                if (r4 == 0) goto L_0x00cd
                android.os.Parcelable$Creator<android.support.v4.media.session.PlaybackStateCompat> r4 = android.support.p001v4.media.session.PlaybackStateCompat.CREATOR
                java.lang.Object r4 = r4.createFromParcel(r5)
                r2 = r4
                android.support.v4.media.session.PlaybackStateCompat r2 = (android.support.p001v4.media.session.PlaybackStateCompat) r2
            L_0x00cd:
                r3.onPlaybackStateChanged(r2)
                return r1
            L_0x00d1:
                java.lang.String r4 = "android.support.v4.media.session.IMediaControllerCallback"
                r5.enforceInterface(r4)
                r3.onSessionDestroyed()
                return r1
            L_0x00da:
                java.lang.String r4 = "android.support.v4.media.session.IMediaControllerCallback"
                r5.enforceInterface(r4)
                java.lang.String r4 = r5.readString()
                int r6 = r5.readInt()
                if (r6 == 0) goto L_0x00f2
                android.os.Parcelable$Creator r6 = android.os.Bundle.CREATOR
                java.lang.Object r5 = r6.createFromParcel(r5)
                r2 = r5
                android.os.Bundle r2 = (android.os.Bundle) r2
            L_0x00f2:
                r3.onEvent(r4, r2)
                return r1
            L_0x00f6:
                java.lang.String r3 = "android.support.v4.media.session.IMediaControllerCallback"
                r6.writeString(r3)
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: android.support.p001v4.media.session.IMediaControllerCallback.Stub.onTransact(int, android.os.Parcel, android.os.Parcel, int):boolean");
        }
    }

    void onCaptioningEnabledChanged(boolean z) throws RemoteException;

    void onEvent(String str, Bundle bundle) throws RemoteException;

    void onExtrasChanged(Bundle bundle) throws RemoteException;

    void onMetadataChanged(MediaMetadataCompat mediaMetadataCompat) throws RemoteException;

    void onPlaybackStateChanged(PlaybackStateCompat playbackStateCompat) throws RemoteException;

    void onQueueChanged(List<QueueItem> list) throws RemoteException;

    void onQueueTitleChanged(CharSequence charSequence) throws RemoteException;

    void onRepeatModeChanged(int i) throws RemoteException;

    void onSessionDestroyed() throws RemoteException;

    void onShuffleModeChanged(int i) throws RemoteException;

    void onShuffleModeChangedDeprecated(boolean z) throws RemoteException;

    void onVolumeInfoChanged(ParcelableVolumeInfo parcelableVolumeInfo) throws RemoteException;
}
