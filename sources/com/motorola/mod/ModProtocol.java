package com.motorola.mod;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.motorola.actions.foc.gesture.util.FlashOnChopAlgorithmUtil;

public class ModProtocol implements Parcelable {
    public static final Creator<ModProtocol> CREATOR = new Creator<ModProtocol>() {
        /* renamed from: a */
        public ModProtocol createFromParcel(Parcel parcel) {
            return new ModProtocol(parcel);
        }

        /* renamed from: a */
        public ModProtocol[] newArray(int i) {
            return new ModProtocol[i];
        }
    };
    public static final int PROTOCOL_ALL = 256;

    /* renamed from: a */
    private Protocol f188a;

    /* renamed from: b */
    private short f189b;

    /* renamed from: c */
    private byte f190c;

    /* renamed from: d */
    private byte f191d;

    /* renamed from: e */
    private String f192e;

    public enum Protocol {
        CONTROL(0),
        AP(1),
        GPIO(2),
        I2C(3),
        UART(4),
        HID(5),
        USB(6),
        SDIO(7),
        BATTERY(8),
        PWM(9),
        I2S_MGMT(10),
        SPI(11),
        DISPLAY(12),
        CAMERA(13),
        SENSOR(14),
        LIGHTS(15),
        VIBRATOR(16),
        LOOPBACK(17),
        I2S_RECEIVER(18),
        I2S_TRANSMITTER(19),
        SVC(20),
        FIRMWARE(21),
        CAMERA_DATA(22),
        SYSTEM_EXT(234),
        SENSORS_EXT(235),
        USB_EXT(236),
        CAMERA_EXT(237),
        MODS_DISPLAY(238),
        PTP(239),
        MODS_AUDIO(240),
        RAW(254),
        VENDOR(255),
        INVALID(-1);
        

        /* renamed from: a */
        private int f196a;

        private Protocol(int i) {
            this.f196a = i;
        }

        public int getValue() {
            return this.f196a;
        }

        public static Protocol toProtocol(int i) {
            Protocol[] values;
            for (Protocol protocol : values()) {
                if (protocol.getValue() == i) {
                    return protocol;
                }
            }
            return INVALID;
        }
    }

    public int describeContents() {
        return 0;
    }

    public Protocol getProtocol() {
        return this.f188a;
    }

    public short getRawProtocolId() {
        return this.f189b;
    }

    public byte getVersionMajor() {
        return this.f190c;
    }

    public byte getVersionMinor() {
        return this.f191d;
    }

    public String getProtocolName() {
        return this.f192e;
    }

    public void writeToParcel(Parcel parcel, int i) {
        if (this.f188a != null) {
            parcel.writeInt(1);
            parcel.writeInt(this.f188a.getValue());
        } else {
            parcel.writeInt(0);
        }
        parcel.writeInt(this.f189b);
        parcel.writeByte(this.f190c);
        parcel.writeByte(this.f191d);
        Utils.writeNullableString(parcel, this.f192e);
        int dataPosition = parcel.dataPosition();
        parcel.writeInt(0);
        int dataPosition2 = parcel.dataPosition();
        parcel.setDataPosition(dataPosition);
        parcel.writeInt(dataPosition2 - dataPosition);
        parcel.setDataPosition(dataPosition2);
    }

    private ModProtocol(Parcel parcel) {
        if (parcel.readInt() == 1) {
            this.f188a = Protocol.toProtocol(parcel.readInt());
        }
        this.f189b = (short) parcel.readInt();
        this.f190c = parcel.readByte();
        this.f191d = parcel.readByte();
        this.f192e = Utils.readNullableString(parcel);
        parcel.setDataPosition(parcel.dataPosition() + parcel.readInt());
    }

    public ModProtocol(Protocol protocol, short s) {
        this.f188a = protocol;
        this.f189b = s;
        if (protocol == null) {
            this.f192e = FlashOnChopAlgorithmUtil.KEY_TEST_USE_ONLY_FOC_ALGO_UNKNOWN;
            return;
        }
        switch (protocol) {
            case CONTROL:
                this.f192e = "CONTROL";
                break;
            case AP:
                this.f192e = "AP";
                break;
            case GPIO:
                this.f192e = "GPIO";
                break;
            case I2C:
                this.f192e = "I2C";
                break;
            case UART:
                this.f192e = "UART";
                break;
            case HID:
                this.f192e = "HID";
                break;
            case USB:
                this.f192e = "USB";
                break;
            case SDIO:
                this.f192e = "SDIO";
                break;
            case BATTERY:
                this.f192e = "BATTERY";
                break;
            case PWM:
                this.f192e = "PWM";
                break;
            case I2S_MGMT:
                this.f192e = "I2S_MGMT";
                break;
            case SPI:
                this.f192e = "SPI";
                break;
            case DISPLAY:
                this.f192e = "DISPLAY";
                break;
            case CAMERA:
                this.f192e = "CAMERA";
                break;
            case SENSOR:
                this.f192e = "SENSOR";
                break;
            case LIGHTS:
                this.f192e = "LIGHTS";
                break;
            case VIBRATOR:
                this.f192e = "VIBRATOR";
                break;
            case LOOPBACK:
                this.f192e = "LOOPBACK";
                break;
            case I2S_RECEIVER:
                this.f192e = "I2S_RECEIVER";
                break;
            case I2S_TRANSMITTER:
                this.f192e = "I2S_TRANSMITTER";
                break;
            case SVC:
                this.f192e = "SVC";
                break;
            case CAMERA_DATA:
                this.f192e = "CAMERA_DATA";
                break;
            case SYSTEM_EXT:
                this.f192e = "SYSTEM_EXT";
                break;
            case SENSORS_EXT:
                this.f192e = "SENSORS_EXT";
                break;
            case USB_EXT:
                this.f192e = "USB_EXT";
                break;
            case CAMERA_EXT:
                this.f192e = "CAMERA_EXT";
                break;
            case MODS_DISPLAY:
                this.f192e = "MODS_DISPLAY";
                break;
            case PTP:
                this.f192e = "PTP";
                break;
            case MODS_AUDIO:
                this.f192e = "MODS_AUDIO";
                break;
            case RAW:
                this.f192e = "RAW";
                break;
            case VENDOR:
                this.f192e = "VENDOR";
                break;
        }
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (obj == null || !(obj instanceof ModProtocol)) {
            return false;
        }
        ModProtocol modProtocol = (ModProtocol) obj;
        if (this.f188a == modProtocol.f188a && this.f189b == modProtocol.f189b && this.f190c == modProtocol.f190c && this.f191d == modProtocol.f191d) {
            z = true;
        }
        return z;
    }

    public String toString() {
        if (this.f188a == null) {
            return null;
        }
        return this.f188a.toString();
    }
}
