package com.motorola.mod;

import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import com.motorola.mod.ModProtocol.Protocol;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class ModDevice implements Parcelable {
    public static final int CAPABILITY_LEVEL_DISABLED = 2;
    public static final int CAPABILITY_LEVEL_FULL = 0;
    public static final int CAPABILITY_LEVEL_REDUCED = 1;
    public static final int CAPABILITY_REASON_BATTERY = 2;
    public static final int CAPABILITY_REASON_CURRENT = 4;
    public static final int CAPABILITY_REASON_TEMP = 1;
    public static final int CAPABILITY_REASON_UNKNOWN = 0;
    public static final int CONFIG_OFFSET_CATEGORY = 0;
    public static final int CONFIG_OFFSET_DISABLE_FPS_NAV_DISPLAY = 3;
    public static final int CONFIG_OFFSET_DISABLE_FPS_NAV_VIEWFINDER = 4;
    public static final int CONFIG_OFFSET_DISABLE_NO_FOLLOW_DISPLAY = 5;
    public static final int CONFIG_OFFSET_DISABLE_REAR_CAMERA = 2;
    public static final int CONFIG_OFFSET_DISABLE_REAR_FLASH = 1;
    public static final int CONFIG_OFFSET_EFFICIENCY_MODE = 0;
    public static final int CONFIG_OFFSET_FIRMWARE_UNLOCKED = 2;
    public static final int CONFIG_OFFSET_INTERNAL_COUNT = 3;
    public static final int CONFIG_OFFSET_LAUNCH_CAR_DOCK_INTENT = 6;
    public static final int CONFIG_OFFSET_NO_CURRENT_LIMIT = 7;
    public static final int CONFIG_OFFSET_TYPE = 1;
    public static final Creator<ModDevice> CREATOR = new Creator<ModDevice>() {
        /* renamed from: a */
        public ModDevice createFromParcel(Parcel parcel) {
            return new ModDevice(parcel);
        }

        /* renamed from: a */
        public ModDevice[] newArray(int i) {
            return new ModDevice[i];
        }
    };
    public static final String ENG = "eng";
    public static final String USER = "user";

    /* renamed from: A */
    private boolean f70A;

    /* renamed from: B */
    private String f71B;

    /* renamed from: a */
    private byte f72a;

    /* renamed from: b */
    private byte f73b;

    /* renamed from: c */
    private int f74c;

    /* renamed from: d */
    private int f75d;

    /* renamed from: e */
    private String f76e;

    /* renamed from: f */
    private String f77f;

    /* renamed from: g */
    private ParcelUuid f78g;

    /* renamed from: h */
    private String f79h;

    /* renamed from: i */
    private short f80i;

    /* renamed from: j */
    private ArrayList<Subclass> f81j;

    /* renamed from: k */
    private ArrayList<Protocol> f82k;

    /* renamed from: l */
    private ArrayList<InterfaceInfo> f83l;

    /* renamed from: m */
    private String f84m;

    /* renamed from: n */
    private String f85n;

    /* renamed from: o */
    private String f86o;

    /* renamed from: p */
    private String f87p;

    /* renamed from: q */
    private ArrayList<Interface> f88q;

    /* renamed from: r */
    private int f89r;

    /* renamed from: s */
    private int f90s;

    /* renamed from: t */
    private int f91t;

    /* renamed from: u */
    private int f92u;

    /* renamed from: v */
    private int f93v;

    /* renamed from: w */
    private byte[] f94w;

    /* renamed from: x */
    private byte[] f95x;

    /* renamed from: y */
    private ModCategory f96y;

    /* renamed from: z */
    private Enum<? extends ModType> f97z;

    public static class Interface implements InterfaceInfo {
        public static final Creator<Interface> CREATOR = new Creator<Interface>() {
            /* renamed from: a */
            public Interface createFromParcel(Parcel parcel) {
                return new Interface(parcel);
            }

            /* renamed from: a */
            public Interface[] newArray(int i) {
                return new Interface[i];
            }
        };

        /* renamed from: a */
        private byte f98a;

        /* renamed from: b */
        private Subclass f99b;

        /* renamed from: c */
        private String f100c;

        /* renamed from: d */
        private short f101d;

        /* renamed from: e */
        private ArrayList<Protocol> f102e;

        public int describeContents() {
            return 0;
        }

        public Interface(byte b, short s, Subclass subclass, String str, ArrayList<Protocol> arrayList) {
            this.f98a = b;
            this.f101d = s;
            this.f99b = subclass;
            this.f100c = str;
            this.f102e = arrayList;
        }

        public Interface(Interface interfaceR) {
            this.f98a = interfaceR.f98a;
            this.f99b = interfaceR.f99b;
            this.f100c = interfaceR.f100c;
            this.f102e = interfaceR.f102e;
        }

        public byte getInterfaceId() {
            return this.f98a;
        }

        public short getModId() {
            return this.f101d;
        }

        public Subclass getSubclass() {
            return this.f99b;
        }

        public String getSysPath() {
            return this.f100c;
        }

        public ArrayList<Protocol> getDeclaredProtocols() {
            return this.f102e;
        }

        public boolean hasDeclaredProtocol(Protocol protocol) {
            return this.f102e != null && this.f102e.contains(protocol);
        }

        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeByte(this.f98a);
            parcel.writeInt(this.f101d);
            parcel.writeInt(this.f99b.getValue());
            Utils.writeNullableString(parcel, this.f100c);
            if (this.f102e == null) {
                parcel.writeInt(-1);
            } else {
                int size = this.f102e.size();
                parcel.writeInt(size);
                for (int i2 = 0; i2 < size; i2++) {
                    parcel.writeInt(((Protocol) this.f102e.get(i2)).getValue());
                }
            }
            int dataPosition = parcel.dataPosition();
            parcel.writeInt(0);
            int dataPosition2 = parcel.dataPosition();
            parcel.setDataPosition(dataPosition);
            parcel.writeInt(dataPosition2 - dataPosition);
            parcel.setDataPosition(dataPosition2);
        }

        private Interface(Parcel parcel) {
            this.f98a = parcel.readByte();
            this.f101d = (short) parcel.readInt();
            this.f99b = Subclass.toSubclass(parcel.readInt());
            this.f100c = Utils.readNullableString(parcel);
            int readInt = parcel.readInt();
            if (readInt >= 0) {
                this.f102e = new ArrayList<>();
                for (int i = 0; i < readInt; i++) {
                    this.f102e.add(Protocol.toProtocol(parcel.readInt()));
                }
            }
            parcel.setDataPosition(parcel.dataPosition() + parcel.readInt());
        }

        public boolean equals(Object obj) {
            boolean z = false;
            if (obj == null || !(obj instanceof Interface)) {
                return false;
            }
            Interface interfaceR = (Interface) obj;
            if (this.f98a == interfaceR.f98a && this.f101d == interfaceR.f101d && this.f99b == interfaceR.f99b && TextUtils.equals(this.f100c, interfaceR.f100c)) {
                z = true;
            }
            return z;
        }

        public String toString() {
            String sb;
            synchronized (this) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Interface{");
                sb2.append("interfaceId = ");
                sb2.append(this.f98a);
                sb2.append(',');
                sb2.append("class = ");
                sb2.append(this.f99b);
                sb2.append(',');
                sb2.append("protocols = ");
                sb2.append(ModDevice.m61b(this.f102e));
                sb2.append('}');
                sb = sb2.toString();
            }
            return sb;
        }
    }

    public interface InterfaceInfo extends Parcelable {
        ArrayList<Protocol> getDeclaredProtocols();

        byte getInterfaceId();

        Subclass getSubclass();
    }

    public static class InterfaceInfoImpl implements InterfaceInfo {
        public static final Creator<InterfaceInfoImpl> CREATOR = new Creator<InterfaceInfoImpl>() {
            /* renamed from: a */
            public InterfaceInfoImpl createFromParcel(Parcel parcel) {
                return new InterfaceInfoImpl(parcel);
            }

            /* renamed from: a */
            public InterfaceInfoImpl[] newArray(int i) {
                return new InterfaceInfoImpl[i];
            }
        };

        /* renamed from: a */
        private byte f103a;

        /* renamed from: b */
        private Subclass f104b;

        /* renamed from: c */
        private ArrayList<Protocol> f105c;

        public int describeContents() {
            return 0;
        }

        public byte getInterfaceId() {
            return this.f103a;
        }

        public Subclass getSubclass() {
            return this.f104b;
        }

        public ArrayList<Protocol> getDeclaredProtocols() {
            return this.f105c;
        }

        public InterfaceInfoImpl(byte b, Subclass subclass, ArrayList<Protocol> arrayList) {
            this.f103a = b;
            this.f104b = subclass;
            this.f105c = arrayList;
        }

        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeByte(this.f103a);
            parcel.writeInt(this.f104b.getValue());
            if (this.f105c == null) {
                parcel.writeInt(-1);
            } else {
                int size = this.f105c.size();
                parcel.writeInt(size);
                for (int i2 = 0; i2 < size; i2++) {
                    parcel.writeInt(((Protocol) this.f105c.get(i2)).getValue());
                }
            }
            int dataPosition = parcel.dataPosition();
            parcel.writeInt(0);
            int dataPosition2 = parcel.dataPosition();
            parcel.setDataPosition(dataPosition);
            parcel.writeInt(dataPosition2 - dataPosition);
            parcel.setDataPosition(dataPosition2);
        }

        private InterfaceInfoImpl(Parcel parcel) {
            this.f103a = parcel.readByte();
            this.f104b = Subclass.toSubclass(parcel.readInt());
            int readInt = parcel.readInt();
            if (readInt >= 0) {
                this.f105c = new ArrayList<>();
                for (int i = 0; i < readInt; i++) {
                    this.f105c.add(Protocol.toProtocol(parcel.readInt()));
                }
            }
            parcel.setDataPosition(parcel.dataPosition() + parcel.readInt());
        }

        public boolean equals(Object obj) {
            boolean z = false;
            if (obj == null || !(obj instanceof InterfaceInfoImpl)) {
                return false;
            }
            InterfaceInfoImpl interfaceInfoImpl = (InterfaceInfoImpl) obj;
            synchronized (this) {
                if (this.f103a == interfaceInfoImpl.f103a && this.f104b == interfaceInfoImpl.f104b && ModDevice.m62b(this.f105c, interfaceInfoImpl.f105c)) {
                    z = true;
                }
            }
            return z;
        }

        public String toString() {
            String sb;
            synchronized (this) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("InterfaceInfo{");
                sb2.append("interfaceId = ");
                sb2.append(this.f103a);
                sb2.append(',');
                sb2.append("class = ");
                sb2.append(this.f104b);
                sb2.append(',');
                sb2.append("protocols = ");
                sb2.append(ModDevice.m61b(this.f105c));
                sb2.append('}');
                sb = sb2.toString();
            }
            return sb;
        }
    }

    public enum ModCategory {
        POWER(0, "Power", ModTypePower.values()),
        AUDIO(1, "Audio", ModTypeAudio.values()),
        COMMUNICATIONS(2, "Communications", ModTypeCommunications.values()),
        INPUT(3, "Input", ModTypeInput.values()),
        DOCK(4, "Dock & Mounts & Covers", ModTypeDock.values()),
        SENSOR(5, "Sensors & Physical devices", ModTypeSensor.values()),
        IMAGING(6, "Imaging device", ModTypeImaging.values()),
        PRINTER(7, "Printer", ModTypePrinter.values()),
        STORAGE(8, "Storage device", ModTypeStorage.values()),
        HEALTH(15, "Fitness, Personal Healthcare & Medical", ModTypeHealth.values()),
        OUTPUT(16, "Output", ModTypeOutput.values()),
        ADVANCED(17, "Advanced Devices", ModTypeAdvanced.values()),
        SECURITY(18, "Security device", ModTypeSecurity.values()),
        HOME(19, "Home device", ModTypeHome.values()),
        RESERVED(255, "RESERVED NONE", ModTypeReserved.values());
        

        /* renamed from: a */
        private int f107a;

        /* renamed from: b */
        private String f108b;

        /* renamed from: c */
        private Enum<? extends ModType>[] f109c;

        private ModCategory(int i, String str, Enum<? extends ModType>... enumArr) {
            this.f107a = i;
            this.f108b = str;
            this.f109c = enumArr;
        }

        public int getValue() {
            return this.f107a;
        }

        public Enum<? extends ModType>[] getTypes() {
            return this.f109c;
        }

        public Enum<? extends ModType> getType(int i) {
            Enum<? extends ModType>[] enumArr;
            for (Enum<? extends ModType> enumR : this.f109c) {
                if (((ModType) enumR).getValue() == i) {
                    return enumR;
                }
            }
            return null;
        }

        public String toString() {
            return this.f108b;
        }

        public static ModCategory valueOf(int i) {
            ModCategory[] values;
            for (ModCategory modCategory : values()) {
                if (modCategory.getValue() == i) {
                    return modCategory;
                }
            }
            return null;
        }
    }

    public interface ModType {
        int getValue();
    }

    public enum ModTypeAdvanced implements ModType {
        ADVANCED(0, "Unknown advanced device"),
        MDK(1, "Development Board (MDK)"),
        ROBOT(2, "Robot"),
        DRONE(3, "Drone"),
        VR(4, "Virtual reality"),
        AR(5, "Augmented reality"),
        CAR(6, "Car");
        

        /* renamed from: a */
        private int f113a;

        /* renamed from: b */
        private String f114b;

        private ModTypeAdvanced(int i, String str) {
            this.f113a = i;
            this.f114b = str;
        }

        public int getValue() {
            return this.f113a;
        }

        public String toString() {
            return this.f114b;
        }

        public static ModTypeAdvanced valueOf(int i) {
            ModTypeAdvanced[] values;
            for (ModTypeAdvanced modTypeAdvanced : values()) {
                if (modTypeAdvanced.getValue() == i) {
                    return modTypeAdvanced;
                }
            }
            return null;
        }
    }

    public enum ModTypeAudio implements ModType {
        AUDIO(0, "Generic audio device"),
        SPEAKER(1, "Speaker"),
        HOME_THEATER(2, "Home theater"),
        MICROPHONE(3, "Microphone"),
        HEADSET(4, "Headset (audio in and audio out)"),
        HEADPHONE(5, "Headphone (audio out)"),
        CONVERTER(6, "External DAC"),
        RECORDER(7, "Voice/sound recorder"),
        COMBO(8, "Audio input/output combo"),
        INSTRUMENT(9, "Musical instrument"),
        PRO(10, "Pro-audio device"),
        VIDEO(11, "Audio/video device"),
        MIDI(12, "Midi controller"),
        FM_TUNER(13, "FM tuner"),
        HEADSET_ADAPTER(14, "Headset audio (e.g. output jack)");
        

        /* renamed from: a */
        private int f116a;

        /* renamed from: b */
        private String f117b;

        private ModTypeAudio(int i, String str) {
            this.f116a = i;
            this.f117b = str;
        }

        public int getValue() {
            return this.f116a;
        }

        public String toString() {
            return this.f117b;
        }

        public static ModTypeAudio valueOf(int i) {
            ModTypeAudio[] values;
            for (ModTypeAudio modTypeAudio : values()) {
                if (modTypeAudio.getValue() == i) {
                    return modTypeAudio;
                }
            }
            return null;
        }
    }

    public enum ModTypeCommunications implements ModType {
        COMMUNICATIONS(0, "Generic communication device"),
        MODEM(1, "Data modem"),
        WIFI(2, "Wifi radio"),
        GPS(3, "GPS radio"),
        BLUETOOTH(4, "Bluetooth radio"),
        NFC(5, "NFC reader"),
        RFID(6, "RFID reader"),
        AMFM(7, "AM/FM radio"),
        TV(8, "TV Broadcasting Radio / Antenna"),
        GAMING(9, "Gaming Controller radio"),
        SPECIALITY(10, "Speciality radio"),
        IOT(11, "Radio for short range IOT usage"),
        INFRARED(12, "Infrared adapter"),
        NETWORK(13, "Network adapter"),
        SHORT_RANGE(14, "Short range communication radio"),
        LONG_RANGE(15, "Long range communication radio"),
        OTHER(16, "Other types");
        

        /* renamed from: a */
        private int f120a;

        /* renamed from: b */
        private String f121b;

        private ModTypeCommunications(int i, String str) {
            this.f120a = i;
            this.f121b = str;
        }

        public int getValue() {
            return this.f120a;
        }

        public String toString() {
            return this.f121b;
        }

        public static ModTypeCommunications valueOf(int i) {
            ModTypeCommunications[] values;
            for (ModTypeCommunications modTypeCommunications : values()) {
                if (modTypeCommunications.getValue() == i) {
                    return modTypeCommunications;
                }
            }
            return null;
        }
    }

    public enum ModTypeDock implements ModType {
        DOCK(0, "Generic dock"),
        CAR(1, "Car dock"),
        DESK(2, "Desk dock"),
        AUDIO(3, "Dock with speaker inside"),
        MULTI_FUNCTION(4, "Multi-function dock"),
        MOUNT(5, "Mount"),
        SELFIE_STICK(6, "Selfi stick"),
        BIKE_MOUNT(7, "Bike mount"),
        CASE(8, "Case"),
        FOLIO(9, "Folio"),
        BACK_COVER(10, "Back cover"),
        VR_ADAPTOR(11, "VR adapter"),
        STRAP(12, "Strap");
        

        /* renamed from: a */
        private int f123a;

        /* renamed from: b */
        private String f124b;

        private ModTypeDock(int i, String str) {
            this.f123a = i;
            this.f124b = str;
        }

        public int getValue() {
            return this.f123a;
        }

        public String toString() {
            return this.f124b;
        }

        public static ModTypeDock valueOf(int i) {
            ModTypeDock[] values;
            for (ModTypeDock modTypeDock : values()) {
                if (modTypeDock.getValue() == i) {
                    return modTypeDock;
                }
            }
            return null;
        }
    }

    public enum ModTypeHealth implements ModType {
        MEDICAL(0, "Generic medical device"),
        BLOODPRESSURE(1, "Blood pressure monitor"),
        GLUCOSE(2, "Glucose monitor"),
        OTOSCOPE(3, "Otoscope"),
        EKGEGC(4, "EKG / ECG reader"),
        SKIN(5, "Skin scanner"),
        ULTRASOUND(6, "Ultrasound"),
        MICROSCOPE(7, "Microscope / Spectrometer"),
        PETRI(8, "Petri dish"),
        AIRSTRIP(9, "AirStrip monitoring"),
        BRAIN(10, "Brain scanner"),
        EYE(11, "Eye scanner"),
        THERMOMETER(12, "Thermometer"),
        FITNESS(13, "Fitness device"),
        ACCESSIBILITY(14, "Accessibility");
        

        /* renamed from: a */
        private int f126a;

        /* renamed from: b */
        private String f127b;

        private ModTypeHealth(int i, String str) {
            this.f126a = i;
            this.f127b = str;
        }

        public int getValue() {
            return this.f126a;
        }

        public String toString() {
            return this.f127b;
        }

        public static ModTypeHealth valueOf(int i) {
            ModTypeHealth[] values;
            for (ModTypeHealth modTypeHealth : values()) {
                if (modTypeHealth.getValue() == i) {
                    return modTypeHealth;
                }
            }
            return null;
        }
    }

    public enum ModTypeHome implements ModType {
        HOME(0, "Generic home device"),
        APPLIANCE(1, "Appliance"),
        MONITORING(2, "Home monitoring"),
        SECURITY(3, "Home security"),
        TOOL(4, "Tool");
        

        /* renamed from: a */
        private int f129a;

        /* renamed from: b */
        private String f130b;

        private ModTypeHome(int i, String str) {
            this.f129a = i;
            this.f130b = str;
        }

        public int getValue() {
            return this.f129a;
        }

        public String toString() {
            return this.f130b;
        }

        public static ModTypeHome valueOf(int i) {
            ModTypeHome[] values;
            for (ModTypeHome modTypeHome : values()) {
                if (modTypeHome.getValue() == i) {
                    return modTypeHome;
                }
            }
            return null;
        }
    }

    public enum ModTypeImaging implements ModType {
        IMAGING(0, "Generic image device"),
        CAMERA(1, "Camera"),
        LENS(2, "Add-on Lens"),
        SCANNER(3, "Scanner"),
        THERMAL(4, "Thermal"),
        PANORAMA(5, "360 camera"),
        DEPTH(6, "Depth camera"),
        PRO(7, "Pro camera"),
        FLASH(8, "Flash");
        

        /* renamed from: a */
        private int f132a;

        /* renamed from: b */
        private String f133b;

        private ModTypeImaging(int i, String str) {
            this.f132a = i;
            this.f133b = str;
        }

        public int getValue() {
            return this.f132a;
        }

        public String toString() {
            return this.f133b;
        }

        public static ModTypeImaging valueOf(int i) {
            ModTypeImaging[] values;
            for (ModTypeImaging modTypeImaging : values()) {
                if (modTypeImaging.getValue() == i) {
                    return modTypeImaging;
                }
            }
            return null;
        }
    }

    public enum ModTypeInput implements ModType {
        INPUT(0, "Generic input device"),
        SIMULATION(1, "Simulation controls"),
        VR(2, "VR controller"),
        SPORT(3, "Sport controller"),
        GAMING(4, "Game controller"),
        CONTROL(5, "Generic device control"),
        KEYBOARD(6, "Keyboard or keypad"),
        LED(7, "LEDs"),
        BUTTON(8, "Button"),
        DIGITIZER(9, "Digitizer"),
        PID(10, "Physical Input Device"),
        MOUSE(11, "Mouse"),
        TOUCHPAD(12, "Touchpad/Trackpad"),
        STYLUS(13, "Stylus"),
        TRACKBALL(14, "Trackballs & Scrollwheels"),
        PROGRAMMABLE(15, "Programmable hardware buttons"),
        SWITCH(16, "Physical switch input"),
        TVREMOTE(17, "TV remote control"),
        SPECIALITY_GRAMING(18, "Specialty gaming controller"),
        GESTURE(19, "Gesture-based input"),
        SCANNER(20, "Scanner"),
        FINGERPRINT(21, "Fingerprint reader"),
        RETINA(22, "Retina / Iris scanner"),
        BARCODE(23, "Barcode reader"),
        CREDIT(24, "Credit card reader"),
        VOICE(25, "Voice input"),
        FACE(26, "Face recognition"),
        OTHER(27, "Other");
        

        /* renamed from: a */
        private int f136a;

        /* renamed from: b */
        private String f137b;

        private ModTypeInput(int i, String str) {
            this.f136a = i;
            this.f137b = str;
        }

        public int getValue() {
            return this.f136a;
        }

        public String toString() {
            return this.f137b;
        }

        public static ModTypeInput valueOf(int i) {
            ModTypeInput[] values;
            for (ModTypeInput modTypeInput : values()) {
                if (modTypeInput.getValue() == i) {
                    return modTypeInput;
                }
            }
            return null;
        }
    }

    public enum ModTypeOutput implements ModType {
        DISPLAY(0, "Generic display"),
        PROJECTOR(1, "Projector"),
        EINK(2, "E-ink"),
        TV(3, "TV"),
        MONITOR(4, "Monitor"),
        HEADSUP(5, "Heads Up Display"),
        CAR(6, "Car display"),
        BILLBOARD(7, "Billboard"),
        LED(8, "LED panel"),
        ALPHANUMERIC(9, "Alphanumeric display"),
        HOLOGRAPHIC(10, "Holographic");
        

        /* renamed from: a */
        private int f140a;

        /* renamed from: b */
        private String f141b;

        private ModTypeOutput(int i, String str) {
            this.f140a = i;
            this.f141b = str;
        }

        public int getValue() {
            return this.f140a;
        }

        public String toString() {
            return this.f141b;
        }

        public static ModTypeOutput valueOf(int i) {
            ModTypeOutput[] values;
            for (ModTypeOutput modTypeOutput : values()) {
                if (modTypeOutput.getValue() == i) {
                    return modTypeOutput;
                }
            }
            return null;
        }
    }

    public enum ModTypePower implements ModType {
        ACCESSORY(0, "Power accessory"),
        BATTERY_PACK(1, "Battery pack"),
        BATTERY_SINGLEUSE(2, "Non-rechargeable battery"),
        CHARGER_CABLE(3, "Charger with capability to charge the phone from a cable"),
        CHARGER_WIRELESS(4, "Charger with capability to charge the phone wirelessly"),
        CHARGER_OTHER(5, "Other type of charge, Solar, etc.");
        

        /* renamed from: a */
        private int f143a;

        /* renamed from: b */
        private String f144b;

        private ModTypePower(int i, String str) {
            this.f143a = i;
            this.f144b = str;
        }

        public int getValue() {
            return this.f143a;
        }

        public String toString() {
            return this.f144b;
        }

        public static ModTypePower valueOf(int i) {
            ModTypePower[] values;
            for (ModTypePower modTypePower : values()) {
                if (modTypePower.getValue() == i) {
                    return modTypePower;
                }
            }
            return null;
        }
    }

    public enum ModTypePrinter implements ModType {
        PRINTER(0, "Generic printer"),
        PHOTO(1, "Photo printer"),
        LABEL(2, "Label printer"),
        OFFICE(3, "Office printer"),
        INDUSTRIAL(4, "Industrial printer");
        

        /* renamed from: a */
        private int f146a;

        /* renamed from: b */
        private String f147b;

        private ModTypePrinter(int i, String str) {
            this.f146a = i;
            this.f147b = str;
        }

        public int getValue() {
            return this.f146a;
        }

        public String toString() {
            return this.f147b;
        }

        public static ModTypePrinter valueOf(int i) {
            ModTypePrinter[] values;
            for (ModTypePrinter modTypePrinter : values()) {
                if (modTypePrinter.getValue() == i) {
                    return modTypePrinter;
                }
            }
            return null;
        }
    }

    public enum ModTypeReserved implements ModType {
        RESERVED(255, "THIS INDICATES EMPTY - NOTHING");
        

        /* renamed from: a */
        private int f149a;

        /* renamed from: b */
        private String f150b;

        private ModTypeReserved(int i, String str) {
            this.f149a = i;
            this.f150b = str;
        }

        public int getValue() {
            return this.f149a;
        }

        public String toString() {
            return this.f150b;
        }

        public static ModTypeReserved valueOf(int i) {
            ModTypeReserved[] values;
            for (ModTypeReserved modTypeReserved : values()) {
                if (modTypeReserved.getValue() == i) {
                    return modTypeReserved;
                }
            }
            return null;
        }
    }

    public enum ModTypeSecurity implements ModType {
        SECURITY(0, "Generic security device"),
        CAMERA(1, "Security camera");
        

        /* renamed from: a */
        private int f152a;

        /* renamed from: b */
        private String f153b;

        private ModTypeSecurity(int i, String str) {
            this.f152a = i;
            this.f153b = str;
        }

        public int getValue() {
            return this.f152a;
        }

        public String toString() {
            return this.f153b;
        }

        public static ModTypeSecurity valueOf(int i) {
            ModTypeSecurity[] values;
            for (ModTypeSecurity modTypeSecurity : values()) {
                if (modTypeSecurity.getValue() == i) {
                    return modTypeSecurity;
                }
            }
            return null;
        }
    }

    public enum ModTypeSensor implements ModType {
        SENSOR(0, "Generic Sensor"),
        ACOUSTIC(1, "Acoustic sensor"),
        AUTOMOTIVE(2, "Automotive sensor"),
        ENVIRONMENTAL(3, "Environmental sensor"),
        ELECTRIC(4, "Electric sensor"),
        NAVIGATION(5, "Navigation sensor"),
        OPTICAL(6, "Optical / Light sensor"),
        PRESSURE(7, "Pressure sensor"),
        THERMAL(8, "Thermal sensor"),
        PROXIMITY(9, "Proximity sensor"),
        IMAGING(10, "Imaging / Camera"),
        SPECIALTY(11, "Specialty sensor"),
        INDUSTRIAL(12, "Industrial sensor");
        

        /* renamed from: a */
        private int f155a;

        /* renamed from: b */
        private String f156b;

        private ModTypeSensor(int i, String str) {
            this.f155a = i;
            this.f156b = str;
        }

        public int getValue() {
            return this.f155a;
        }

        public String toString() {
            return this.f156b;
        }

        public static ModTypeSensor valueOf(int i) {
            ModTypeSensor[] values;
            for (ModTypeSensor modTypeSensor : values()) {
                if (modTypeSensor.getValue() == i) {
                    return modTypeSensor;
                }
            }
            return null;
        }
    }

    public enum ModTypeStorage implements ModType {
        MEDIA(0, "Generic storage"),
        MASS(1, "Mass storage"),
        ADAPTOR(2, "Adaptor storage");
        

        /* renamed from: a */
        private int f158a;

        /* renamed from: b */
        private String f159b;

        private ModTypeStorage(int i, String str) {
            this.f158a = i;
            this.f159b = str;
        }

        public int getValue() {
            return this.f158a;
        }

        public String toString() {
            return this.f159b;
        }

        public static ModTypeStorage valueOf(int i) {
            ModTypeStorage[] values;
            for (ModTypeStorage modTypeStorage : values()) {
                if (modTypeStorage.getValue() == i) {
                    return modTypeStorage;
                }
            }
            return null;
        }
    }

    public enum Subclass {
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
        I2S(10),
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
        UNKNOWN(-1);
        

        /* renamed from: a */
        private int f162a;

        private Subclass(int i) {
            this.f162a = i;
        }

        public int getValue() {
            return this.f162a;
        }

        public static Subclass toSubclass(int i) {
            Subclass[] values;
            for (Subclass subclass : values()) {
                if (subclass.getValue() == i) {
                    return subclass;
                }
            }
            return UNKNOWN;
        }
    }

    public int describeContents() {
        return 0;
    }

    public ModDevice(byte b, byte b2, int i, int i2, String str, String str2, UUID uuid, short s, String str3, ArrayList<Interface> arrayList) {
        this.f72a = b;
        this.f73b = b2;
        this.f74c = i;
        this.f75d = i2;
        this.f76e = str;
        this.f77f = str2;
        this.f78g = new ParcelUuid(uuid);
        this.f80i = s;
        this.f79h = str3;
        this.f88q = arrayList;
    }

    public ModDevice(ModDevice modDevice) {
        this.f72a = modDevice.f72a;
        this.f73b = modDevice.f73b;
        this.f74c = modDevice.f74c;
        this.f75d = modDevice.f75d;
        this.f76e = modDevice.f76e;
        this.f77f = modDevice.f77f;
        this.f78g = modDevice.f78g;
        this.f80i = modDevice.f80i;
        this.f79h = modDevice.f79h;
        this.f81j = new ArrayList<>(modDevice.f81j);
        this.f82k = new ArrayList<>(modDevice.f82k);
        this.f84m = modDevice.f84m;
        this.f86o = modDevice.f86o;
        this.f87p = modDevice.f87p;
        this.f88q = m59a(modDevice.f88q);
        this.f83l = new ArrayList<>(modDevice.f83l);
        this.f85n = modDevice.f85n;
        this.f91t = modDevice.f91t;
        this.f92u = modDevice.f92u;
        this.f93v = modDevice.f93v;
        this.f89r = modDevice.f89r;
        this.f90s = modDevice.f90s;
        this.f94w = modDevice.f94w;
        this.f95x = modDevice.f95x;
        this.f71B = modDevice.f71B;
        this.f96y = modDevice.f96y;
        this.f97z = modDevice.f97z;
        this.f70A = modDevice.f70A;
    }

    public void writeToParcel(Parcel parcel, int i) {
        int i2;
        int i3;
        parcel.writeByte(this.f72a);
        parcel.writeByte(this.f73b);
        parcel.writeInt(this.f74c);
        parcel.writeInt(this.f75d);
        parcel.writeInt(this.f89r);
        parcel.writeInt(this.f90s);
        Utils.writeNullableString(parcel, this.f76e);
        Utils.writeNullableString(parcel, this.f77f);
        Utils.writeNullableString(parcel, this.f78g.toString());
        parcel.writeInt(this.f80i);
        Utils.writeNullableString(parcel, this.f79h);
        if (this.f81j == null) {
            parcel.writeInt(-1);
        } else {
            int size = this.f81j.size();
            parcel.writeInt(size);
            for (int i4 = 0; i4 < size; i4++) {
                parcel.writeInt(((Subclass) this.f81j.get(i4)).getValue());
            }
        }
        if (this.f82k == null) {
            parcel.writeInt(-1);
        } else {
            int size2 = this.f82k.size();
            parcel.writeInt(size2);
            for (int i5 = 0; i5 < size2; i5++) {
                parcel.writeInt(((Protocol) this.f82k.get(i5)).getValue());
            }
        }
        Utils.writeNullableString(parcel, this.f84m);
        Utils.writeNullableString(parcel, this.f86o);
        Utils.writeNullableString(parcel, this.f87p);
        synchronized (this) {
            if (this.f88q == null) {
                parcel.writeInt(0);
            } else {
                parcel.writeInt(this.f88q.size());
                Iterator it = this.f88q.iterator();
                while (it.hasNext()) {
                    ((Interface) it.next()).writeToParcel(parcel, i);
                }
            }
        }
        if (this.f83l == null) {
            parcel.writeInt(0);
        } else {
            parcel.writeInt(this.f83l.size());
            Iterator it2 = this.f83l.iterator();
            while (it2.hasNext()) {
                ((InterfaceInfo) it2.next()).writeToParcel(parcel, i);
            }
        }
        Utils.writeNullableString(parcel, this.f85n);
        parcel.writeInt(this.f91t);
        parcel.writeInt(this.f92u);
        parcel.writeInt(this.f93v);
        byte[] bArr = new byte[((this.f94w == null ? 0 : this.f94w.length) + 3)];
        if (this.f96y != null) {
            i2 = this.f96y.getValue();
        } else {
            i2 = ModCategory.RESERVED.getValue();
        }
        bArr[0] = (byte) i2;
        if (this.f97z != null) {
            i3 = ((ModType) this.f97z).getValue();
        } else {
            i3 = ModTypeReserved.RESERVED.getValue();
        }
        bArr[1] = (byte) i3;
        bArr[2] = this.f70A ? (byte) 1 : 0;
        if (this.f94w != null) {
            System.arraycopy(this.f94w, 0, bArr, 3, this.f94w.length);
        }
        parcel.writeByteArray(bArr);
        parcel.writeByteArray(this.f95x);
        Utils.writeNullableString(parcel, this.f71B);
        int dataPosition = parcel.dataPosition();
        parcel.writeInt(0);
        int dataPosition2 = parcel.dataPosition();
        parcel.setDataPosition(dataPosition);
        parcel.writeInt(dataPosition2 - dataPosition);
        parcel.setDataPosition(dataPosition2);
    }

    private ModDevice(Parcel parcel) {
        this.f72a = parcel.readByte();
        this.f73b = parcel.readByte();
        this.f74c = parcel.readInt();
        this.f75d = parcel.readInt();
        this.f89r = parcel.readInt();
        this.f90s = parcel.readInt();
        this.f76e = Utils.readNullableString(parcel);
        this.f77f = Utils.readNullableString(parcel);
        this.f78g = ParcelUuid.fromString(Utils.readNullableString(parcel));
        this.f80i = (short) parcel.readInt();
        this.f79h = Utils.readNullableString(parcel);
        int readInt = parcel.readInt();
        if (readInt >= 0) {
            this.f81j = new ArrayList<>();
            for (int i = 0; i < readInt; i++) {
                this.f81j.add(Subclass.toSubclass(parcel.readInt()));
            }
        }
        int readInt2 = parcel.readInt();
        if (readInt2 >= 0) {
            this.f82k = new ArrayList<>();
            for (int i2 = 0; i2 < readInt2; i2++) {
                this.f82k.add(Protocol.toProtocol(parcel.readInt()));
            }
        }
        this.f84m = Utils.readNullableString(parcel);
        this.f86o = Utils.readNullableString(parcel);
        this.f87p = Utils.readNullableString(parcel);
        int readInt3 = parcel.readInt();
        Enum<? extends ModType> enumR = null;
        if (readInt3 > 0) {
            this.f88q = new ArrayList<>(readInt3);
            for (int i3 = 0; i3 < readInt3; i3++) {
                this.f88q.add(Interface.CREATOR.createFromParcel(parcel));
            }
        } else {
            this.f88q = null;
        }
        int readInt4 = parcel.readInt();
        if (readInt4 > 0) {
            this.f83l = new ArrayList<>(readInt4);
            for (int i4 = 0; i4 < readInt4; i4++) {
                this.f83l.add(InterfaceInfoImpl.CREATOR.createFromParcel(parcel));
            }
        } else {
            this.f83l = null;
        }
        this.f85n = Utils.readNullableString(parcel);
        this.f91t = parcel.readInt();
        this.f92u = parcel.readInt();
        this.f93v = parcel.readInt();
        byte[] createByteArray = parcel.createByteArray();
        if (createByteArray == null || createByteArray.length < 3) {
            this.f94w = null;
            this.f96y = ModCategory.RESERVED;
            this.f97z = ModTypeReserved.RESERVED;
            this.f70A = false;
        } else {
            this.f96y = ModCategory.valueOf((int) createByteArray[0]);
            boolean z = true;
            if (this.f96y != null) {
                enumR = this.f96y.getType(createByteArray[1]);
            }
            this.f97z = enumR;
            this.f94w = new byte[(createByteArray.length - 3)];
            if (createByteArray[2] != 1) {
                z = false;
            }
            this.f70A = z;
            System.arraycopy(createByteArray, 3, this.f94w, 0, this.f94w.length);
        }
        this.f95x = parcel.createByteArray();
        this.f71B = Utils.readNullableString(parcel);
        parcel.setDataPosition(parcel.dataPosition() + parcel.readInt());
    }

    public byte getVersionMajor() {
        return this.f72a;
    }

    public byte getVersionMinor() {
        return this.f73b;
    }

    public int getVendorId() {
        return this.f74c;
    }

    public int getProductId() {
        return this.f75d;
    }

    public String getVendorString() {
        return this.f76e;
    }

    public String getProductString() {
        return this.f77f;
    }

    public UUID getUniqueId() {
        return this.f78g.getUuid();
    }

    public ArrayList<Subclass> getDeclaredClass() {
        return this.f81j;
    }

    public ArrayList<Protocol> getDeclaredProtocols() {
        return this.f82k;
    }

    public boolean hasDeclaredProtocol(Protocol protocol) {
        return this.f82k != null && this.f82k.contains(protocol);
    }

    public ArrayList<InterfaceInfo> getDeclaredInterfaces() {
        return this.f83l;
    }

    public String getFirmwareVersion() {
        return this.f84m;
    }

    public String getFirmwareType() {
        return this.f85n;
    }

    public String getPackage() {
        return this.f86o;
    }

    public String getMinSdk() {
        return this.f87p;
    }

    public short getId() {
        return this.f80i;
    }

    public int getCapabilityLevel() {
        return this.f91t;
    }

    public int getCapabilityReason() {
        return this.f92u;
    }

    public int getCapabilityVendor() {
        return this.f93v;
    }

    public byte[] getBuiltinConfigs() {
        return this.f94w;
    }

    public byte[] getVendorConfigs() {
        return this.f95x;
    }

    public String getLegalURI() {
        return this.f71B;
    }

    public ModCategory getCategory() {
        return this.f96y;
    }

    public Enum<? extends ModType> getType() {
        return this.f97z;
    }

    public boolean getAllowVendorUpdate() {
        return this.f70A;
    }

    public int getUniproVendorId() {
        return this.f89r;
    }

    public int getUniproProductId() {
        return this.f90s;
    }

    public String getSysPath() {
        return this.f79h;
    }

    public void setVendorId(int i) {
        this.f74c = i;
    }

    public void setProductId(int i) {
        this.f75d = i;
    }

    public void setUniqueId(UUID uuid) {
        this.f78g = new ParcelUuid(uuid);
    }

    public void setDeclaredClass(ArrayList<Subclass> arrayList) {
        this.f81j = arrayList;
    }

    public void setDeclaredProtocols(ArrayList<Protocol> arrayList) {
        this.f82k = arrayList;
    }

    public void setDeclaredInterfaces(ArrayList<InterfaceInfo> arrayList) {
        this.f83l = arrayList;
    }

    public void setFirmwareVersion(String str) {
        this.f84m = str;
    }

    public void setPackage(String str) {
        this.f86o = str;
    }

    public void setMinSdk(String str) {
        this.f87p = str;
    }

    public void setFirmwareType(String str) {
        this.f85n = str;
    }

    public void setCapLevel(int i) {
        this.f91t = i;
    }

    public void setCapReason(int i) {
        this.f92u = i;
    }

    public void setCapVendor(int i) {
        this.f93v = i;
    }

    public void setUniproVendorId(int i) {
        this.f89r = i;
    }

    public void setUniproProductId(int i) {
        this.f90s = i;
    }

    public void setBuiltInConfigs(byte[] bArr) {
        this.f94w = bArr;
    }

    public void setVendorConfigs(byte[] bArr) {
        this.f95x = bArr;
    }

    public void setLegalURI(String str) {
        this.f71B = str;
    }

    public void setCategory(ModCategory modCategory) {
        this.f96y = modCategory;
    }

    public void setType(Enum<? extends ModType> enumR) {
        this.f97z = enumR;
    }

    public void setAllowFlashing(boolean z) {
        this.f70A = z;
    }

    public List<Interface> getInterfaceList() {
        if (this.f88q == null) {
            return null;
        }
        return (List) this.f88q.clone();
    }

    public void addInterface(Interface interfaceR) {
        synchronized (this) {
            if (this.f88q == null) {
                this.f88q = new ArrayList<>();
            }
            if (!this.f88q.contains(interfaceR)) {
                this.f88q.add(interfaceR);
            }
        }
    }

    public Interface findInterfaceById(byte b) {
        synchronized (this) {
            if (this.f88q == null) {
                return null;
            }
            Iterator it = this.f88q.iterator();
            while (it.hasNext()) {
                Interface interfaceR = (Interface) it.next();
                if (interfaceR.getInterfaceId() == b) {
                    return interfaceR;
                }
            }
            return null;
        }
    }

    public void removeInterface(Interface interfaceR) {
        synchronized (this) {
            this.f88q.remove(interfaceR);
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: b */
    public static boolean m62b(List<?> list, List<?> list2) {
        if (list == null || list2 == null) {
            return list == null && list2 == null;
        }
        for (Object contains : list) {
            if (!list2.contains(contains)) {
                return false;
            }
        }
        return true;
    }

    /* access modifiers changed from: private */
    /* renamed from: b */
    public static <T> String m61b(List<T> list) {
        String str = "";
        if (list != null) {
            for (Object next : list) {
                if (str.length() > 0) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(str);
                    sb.append(",");
                    str = sb.toString();
                }
                StringBuilder sb2 = new StringBuilder();
                sb2.append(str);
                sb2.append(next.toString());
                str = sb2.toString();
            }
        }
        return str;
    }

    /* renamed from: a */
    private ArrayList<Interface> m59a(ArrayList<Interface> arrayList) {
        if (arrayList == null) {
            return null;
        }
        ArrayList<Interface> arrayList2 = new ArrayList<>();
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            arrayList2.add(new Interface((Interface) it.next()));
        }
        return arrayList2;
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (obj == null || !(obj instanceof ModDevice)) {
            return false;
        }
        ModDevice modDevice = (ModDevice) obj;
        synchronized (this) {
            if (this.f72a == modDevice.f72a && this.f73b == modDevice.f73b && this.f74c == modDevice.f74c && this.f75d == modDevice.f75d && TextUtils.equals(this.f76e, modDevice.f76e) && TextUtils.equals(this.f77f, modDevice.f77f) && TextUtils.equals(this.f79h, modDevice.f79h) && this.f78g.equals(modDevice.f78g) && this.f80i == modDevice.f80i) {
                z = true;
            }
        }
        return z;
    }

    /* renamed from: a */
    private String m57a() {
        String str;
        String str2;
        String str3 = "capability level = ";
        switch (getCapabilityLevel()) {
            case 0:
                StringBuilder sb = new StringBuilder();
                sb.append(str3);
                sb.append("full");
                str = sb.toString();
                break;
            case 1:
                StringBuilder sb2 = new StringBuilder();
                sb2.append(str3);
                sb2.append("reduced");
                str = sb2.toString();
                break;
            case 2:
                StringBuilder sb3 = new StringBuilder();
                sb3.append(str3);
                sb3.append("disabled");
                str = sb3.toString();
                break;
            default:
                StringBuilder sb4 = new StringBuilder();
                sb4.append(str3);
                sb4.append("unknown");
                str = sb4.toString();
                break;
        }
        StringBuilder sb5 = new StringBuilder();
        sb5.append(str);
        sb5.append(", reason = ");
        String sb6 = sb5.toString();
        int capabilityReason = getCapabilityReason();
        if ((capabilityReason & 1) != 0) {
            StringBuilder sb7 = new StringBuilder();
            sb7.append(sb6);
            sb7.append("temperature");
            str2 = sb7.toString();
        } else if ((capabilityReason & 2) != 0) {
            StringBuilder sb8 = new StringBuilder();
            sb8.append(sb6);
            sb8.append("battery");
            str2 = sb8.toString();
        } else if ((capabilityReason & 4) != 0) {
            StringBuilder sb9 = new StringBuilder();
            sb9.append(sb6);
            sb9.append("current");
            str2 = sb9.toString();
        } else {
            StringBuilder sb10 = new StringBuilder();
            sb10.append(sb6);
            sb10.append("unknown");
            str2 = sb10.toString();
        }
        StringBuilder sb11 = new StringBuilder();
        sb11.append(str2);
        sb11.append(String.format(", vendor code = 0x%x", new Object[]{Integer.valueOf(getCapabilityVendor())}));
        return sb11.toString();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ModDevice{");
        sb.append("vendorId = ");
        sb.append(this.f74c);
        sb.append(',');
        sb.append("productId = ");
        sb.append(this.f75d);
        sb.append(',');
        sb.append("vendorString = ");
        sb.append(this.f76e);
        sb.append(',');
        sb.append("productString = ");
        sb.append(this.f77f);
        sb.append(',');
        sb.append("uniqueId = ");
        sb.append(this.f78g);
        sb.append(',');
        sb.append("firmwareVersion = ");
        sb.append(this.f84m);
        sb.append(',');
        sb.append("firmwareType = ");
        sb.append(this.f85n);
        sb.append(',');
        sb.append("package = ");
        sb.append(this.f86o);
        sb.append(',');
        sb.append("minSdk = ");
        sb.append(this.f87p);
        sb.append(',');
        sb.append(m57a());
        sb.append('}');
        return sb.toString();
    }
}
