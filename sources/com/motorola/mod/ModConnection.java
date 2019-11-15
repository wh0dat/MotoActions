package com.motorola.mod;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import com.motorola.mod.ModDevice.Interface;

public class ModConnection implements Parcelable {
    public static final Creator<ModConnection> CREATOR = new Creator<ModConnection>() {
        /* renamed from: a */
        public ModConnection createFromParcel(Parcel parcel) {
            return new ModConnection(parcel);
        }

        /* renamed from: a */
        public ModConnection[] newArray(int i) {
            return new ModConnection[i];
        }
    };

    /* renamed from: a */
    private ModProtocol f62a;

    /* renamed from: b */
    private State f63b;

    /* renamed from: c */
    private String f64c;

    /* renamed from: d */
    private int f65d;

    /* renamed from: e */
    private Interface f66e;

    public enum State {
        INVALID(0),
        DISABLED(1),
        ENABLED(2),
        ERROR(3),
        DESTROYING(4);
        

        /* renamed from: a */
        private int f68a;

        public int getValue() {
            return this.f68a;
        }

        private State(int i) {
            this.f68a = i;
        }

        public static State toState(int i) {
            State[] values;
            for (State state : values()) {
                if (state.getValue() == i) {
                    return state;
                }
            }
            return INVALID;
        }
    }

    public int describeContents() {
        return 0;
    }

    public ModConnection(ModProtocol modProtocol, State state, Interface interfaceR, String str, int i) {
        this.f62a = modProtocol;
        this.f63b = state;
        this.f66e = interfaceR;
        this.f64c = str;
        this.f65d = i;
    }

    public ModProtocol getProtocol() {
        return this.f62a;
    }

    public void setProtocol(ModProtocol modProtocol) {
        this.f62a = modProtocol;
    }

    public State getState() {
        return this.f63b;
    }

    public void setState(State state) {
        this.f63b = state;
    }

    public Interface getInterface() {
        return this.f66e;
    }

    public String getSysPath() {
        return this.f64c;
    }

    public int getConnectionId() {
        return this.f65d;
    }

    public void writeToParcel(Parcel parcel, int i) {
        if (this.f62a != null) {
            parcel.writeInt(1);
            this.f62a.writeToParcel(parcel, i);
        } else {
            parcel.writeInt(0);
        }
        parcel.writeInt(this.f63b.getValue());
        if (this.f66e != null) {
            parcel.writeInt(1);
            this.f66e.writeToParcel(parcel, i);
        } else {
            parcel.writeInt(0);
        }
        Utils.writeNullableString(parcel, this.f64c);
        parcel.writeByte((byte) this.f65d);
        int dataPosition = parcel.dataPosition();
        parcel.writeInt(0);
        parcel.writeInt(this.f65d);
        int dataPosition2 = parcel.dataPosition();
        parcel.setDataPosition(dataPosition);
        parcel.writeInt(dataPosition2 - dataPosition);
        parcel.setDataPosition(dataPosition2);
    }

    private ModConnection(Parcel parcel) {
        Interface interfaceR = null;
        this.f62a = parcel.readInt() == 0 ? null : (ModProtocol) ModProtocol.CREATOR.createFromParcel(parcel);
        this.f63b = State.toState(parcel.readInt());
        if (parcel.readInt() != 0) {
            interfaceR = (Interface) Interface.CREATOR.createFromParcel(parcel);
        }
        this.f66e = interfaceR;
        this.f64c = Utils.readNullableString(parcel);
        this.f65d = parcel.readByte();
        int readInt = parcel.readInt();
        if (readInt >= 4) {
            this.f65d = parcel.readInt();
            readInt -= 4;
        }
        parcel.setDataPosition(parcel.dataPosition() + readInt);
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (obj == null || !(obj instanceof ModConnection)) {
            return false;
        }
        ModConnection modConnection = (ModConnection) obj;
        if (this.f62a != null ? this.f62a.equals(modConnection.f62a) : this.f62a == modConnection.f62a) {
            if (this.f63b == modConnection.f63b && TextUtils.equals(this.f64c, modConnection.f64c) && this.f65d == modConnection.f65d) {
                z = true;
            }
        }
        return z;
    }

    public boolean equalsIgnoreState(Object obj) {
        boolean z = false;
        if (obj == null || !(obj instanceof ModConnection)) {
            return false;
        }
        ModConnection modConnection = (ModConnection) obj;
        if (this.f62a != null ? this.f62a.equals(modConnection.f62a) : this.f62a == modConnection.f62a) {
            if (TextUtils.equals(this.f64c, modConnection.f64c) && this.f65d == modConnection.f65d) {
                z = true;
            }
        }
        return z;
    }

    public String toString() {
        Object obj;
        StringBuilder sb = new StringBuilder();
        sb.append("ModConnection{id=");
        sb.append(this.f65d);
        sb.append(",interface=");
        if (this.f66e == null) {
            obj = "unknown";
        } else {
            obj = Byte.valueOf(this.f66e.getInterfaceId());
        }
        sb.append(obj);
        sb.append(",protocol=");
        sb.append(this.f62a);
        sb.append(",state=");
        sb.append(this.f63b);
        sb.append("}");
        return sb.toString();
    }
}
