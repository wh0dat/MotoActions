package com.motorola.mod;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import com.motorola.mod.ModDevice.Interface;
import com.motorola.mod.ModProtocol.Protocol;

public class ModInterfaceDelegation implements Parcelable {
    public static final Creator<ModInterfaceDelegation> CREATOR = new Creator<ModInterfaceDelegation>() {
        /* renamed from: a */
        public ModInterfaceDelegation createFromParcel(Parcel parcel) {
            return new ModInterfaceDelegation(parcel);
        }

        /* renamed from: a */
        public ModInterfaceDelegation[] newArray(int i) {
            return new ModInterfaceDelegation[i];
        }
    };

    /* renamed from: a */
    private String f163a;

    /* renamed from: b */
    private String f164b;

    /* renamed from: c */
    private int f165c;

    /* renamed from: d */
    private int f166d;

    /* renamed from: e */
    private Protocol f167e;

    /* renamed from: f */
    private Interface f168f;

    public int describeContents() {
        return 0;
    }

    public ModInterfaceDelegation(Context context, String str, String str2, int i, int i2, Protocol protocol, Interface interfaceR) {
        context.enforceCallingOrSelfPermission(ModManager.PERMISSION_MOD_INTERNAL, "No permission to create ModInterfaceDelegation, requires: com.motorola.mod.permission.MOD_INTERNAL");
        this.f163a = str;
        this.f164b = str2;
        this.f165c = i;
        this.f166d = i2;
        this.f167e = protocol;
        this.f168f = interfaceR;
    }

    public String getDevPath() {
        return this.f164b;
    }

    public String getSysPath() {
        return this.f163a;
    }

    public int getMajor() {
        return this.f165c;
    }

    public int getMinor() {
        return this.f166d;
    }

    public Protocol getProtocol() {
        return this.f167e;
    }

    public Interface getInterface() {
        return this.f168f;
    }

    public void writeToParcel(Parcel parcel, int i) {
        Utils.writeNullableString(parcel, this.f163a);
        Utils.writeNullableString(parcel, this.f164b);
        parcel.writeInt(this.f165c);
        parcel.writeInt(this.f166d);
        parcel.writeInt(this.f167e.getValue());
        if (this.f168f != null) {
            parcel.writeInt(1);
            this.f168f.writeToParcel(parcel, i);
        } else {
            parcel.writeInt(0);
        }
        int dataPosition = parcel.dataPosition();
        parcel.writeInt(0);
        int dataPosition2 = parcel.dataPosition();
        parcel.setDataPosition(dataPosition);
        parcel.writeInt(dataPosition2 - dataPosition);
        parcel.setDataPosition(dataPosition2);
    }

    private ModInterfaceDelegation(Parcel parcel) {
        this.f163a = Utils.readNullableString(parcel);
        this.f164b = Utils.readNullableString(parcel);
        this.f165c = parcel.readInt();
        this.f166d = parcel.readInt();
        this.f167e = Protocol.toProtocol(parcel.readInt());
        if (parcel.readInt() == 1) {
            this.f168f = (Interface) Interface.CREATOR.createFromParcel(parcel);
        }
        parcel.setDataPosition(parcel.dataPosition() + parcel.readInt());
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (obj == null || !(obj instanceof ModInterfaceDelegation)) {
            return false;
        }
        ModInterfaceDelegation modInterfaceDelegation = (ModInterfaceDelegation) obj;
        if (TextUtils.equals(this.f163a, modInterfaceDelegation.f163a) && TextUtils.equals(this.f164b, modInterfaceDelegation.f164b) && this.f165c == modInterfaceDelegation.f165c && this.f166d == modInterfaceDelegation.f166d && this.f167e == modInterfaceDelegation.f167e) {
            z = true;
        }
        return z;
    }
}
