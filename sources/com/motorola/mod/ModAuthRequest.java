package com.motorola.mod;

import android.os.Bundle;
import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.UUID;

public class ModAuthRequest implements Parcelable {
    public static final Creator<ModAuthRequest> CREATOR = new Creator<ModAuthRequest>() {
        /* renamed from: a */
        public ModAuthRequest createFromParcel(Parcel parcel) {
            return new ModAuthRequest(parcel);
        }

        /* renamed from: a */
        public ModAuthRequest[] newArray(int i) {
            return new ModAuthRequest[i];
        }
    };
    public static final String KEY_CERTIFIED = "certified";
    public static final String KEY_CONSENT = "consent";
    public static final String KEY_PRIVATE_DATA = "private";
    public static final String KEY_RESULT = "result";
    public static final String KEY_TYPE = "type";

    /* renamed from: a */
    private byte[] f55a;

    /* renamed from: b */
    private int f56b;

    /* renamed from: c */
    private int f57c;

    /* renamed from: d */
    private ParcelUuid f58d;

    /* renamed from: e */
    private RemoteCallback f59e;

    /* renamed from: f */
    private Bundle f60f;

    /* renamed from: g */
    private Bundle f61g;

    public int describeContents() {
        return 0;
    }

    public ModAuthRequest(byte[] bArr, int i, int i2, ParcelUuid parcelUuid, Bundle bundle, RemoteCallback remoteCallback, Bundle bundle2) {
        this.f55a = bArr;
        this.f56b = i;
        this.f57c = i2;
        this.f58d = parcelUuid;
        this.f60f = bundle;
        this.f59e = remoteCallback;
        this.f61g = bundle2;
    }

    public byte[] getModMetaData() {
        return this.f55a;
    }

    public int getVid() {
        return this.f56b;
    }

    public int getPid() {
        return this.f57c;
    }

    public UUID getUid() {
        if (this.f58d == null) {
            return null;
        }
        return this.f58d.getUuid();
    }

    public Bundle getManifest() {
        return this.f60f;
    }

    public RemoteCallback getResultReceiver() {
        return this.f59e;
    }

    public Bundle getPrivateData() {
        return this.f61g;
    }

    public void writeToParcel(Parcel parcel, int i) {
        if (this.f55a != null) {
            parcel.writeInt(this.f55a.length);
            parcel.writeByteArray(this.f55a);
        } else {
            parcel.writeInt(0);
        }
        if (this.f59e != null) {
            parcel.writeInt(1);
            this.f59e.writeToParcel(parcel, i);
        } else {
            parcel.writeInt(0);
        }
        if (this.f61g != null) {
            parcel.writeInt(1);
            this.f61g.writeToParcel(parcel, i);
        } else {
            parcel.writeInt(0);
        }
        parcel.writeInt(this.f56b);
        parcel.writeInt(this.f57c);
        if (this.f58d != null) {
            parcel.writeInt(1);
            this.f58d.writeToParcel(parcel, i);
        } else {
            parcel.writeInt(0);
        }
        if (this.f60f != null) {
            parcel.writeInt(1);
            this.f60f.writeToParcel(parcel, i);
            return;
        }
        parcel.writeInt(0);
    }

    private ModAuthRequest(Parcel parcel) {
        int readInt = parcel.readInt();
        if (readInt > 0) {
            byte[] bArr = new byte[readInt];
            parcel.readByteArray(bArr);
            this.f55a = bArr;
        }
        if (parcel.readInt() == 1) {
            this.f59e = (RemoteCallback) RemoteCallback.CREATOR.createFromParcel(parcel);
        }
        if (parcel.readInt() == 1) {
            this.f61g = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
        }
        this.f56b = parcel.readInt();
        this.f57c = parcel.readInt();
        if (parcel.readInt() == 1) {
            this.f58d = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(parcel);
        }
        if (parcel.readInt() == 1) {
            this.f60f = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
        }
    }
}
