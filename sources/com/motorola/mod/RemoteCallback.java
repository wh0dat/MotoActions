package com.motorola.mod;

import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import com.motorola.mod.IRemoteCallback.Stub;

public abstract class RemoteCallback implements Parcelable {
    public static final Creator<RemoteCallback> CREATOR = new Creator<RemoteCallback>() {
        /* renamed from: a */
        public RemoteCallback createFromParcel(Parcel parcel) {
            IBinder readStrongBinder = parcel.readStrongBinder();
            if (readStrongBinder != null) {
                return new C0662c(Stub.asInterface(readStrongBinder));
            }
            return null;
        }

        /* renamed from: a */
        public RemoteCallback[] newArray(int i) {
            return new RemoteCallback[i];
        }
    };

    /* renamed from: a */
    final Handler f197a;

    /* renamed from: b */
    final IRemoteCallback f198b;

    /* renamed from: com.motorola.mod.RemoteCallback$a */
    class C0660a implements Runnable {

        /* renamed from: a */
        final Bundle f199a;

        C0660a(Bundle bundle) {
            this.f199a = bundle;
        }

        public void run() {
            RemoteCallback.this.onResult(this.f199a);
        }
    }

    /* renamed from: com.motorola.mod.RemoteCallback$b */
    class C0661b extends Stub {
        C0661b() {
        }

        public void sendResult(Bundle bundle) {
            RemoteCallback.this.f197a.post(new C0660a(bundle));
        }
    }

    /* renamed from: com.motorola.mod.RemoteCallback$c */
    static class C0662c extends RemoteCallback {
        /* access modifiers changed from: protected */
        public void onResult(Bundle bundle) {
        }

        C0662c(IRemoteCallback iRemoteCallback) {
            super(iRemoteCallback);
        }
    }

    public int describeContents() {
        return 0;
    }

    /* access modifiers changed from: protected */
    public abstract void onResult(Bundle bundle);

    public RemoteCallback(Handler handler) {
        this.f197a = handler;
        this.f198b = new C0661b();
    }

    RemoteCallback(IRemoteCallback iRemoteCallback) {
        this.f197a = null;
        this.f198b = iRemoteCallback;
    }

    public void sendResult(Bundle bundle) throws RemoteException {
        this.f198b.sendResult(bundle);
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        try {
            return this.f198b.asBinder().equals(((RemoteCallback) obj).f198b.asBinder());
        } catch (ClassCastException unused) {
            return false;
        }
    }

    public int hashCode() {
        return this.f198b.asBinder().hashCode();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStrongBinder(this.f198b.asBinder());
    }
}
