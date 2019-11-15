package com.motorola.mod;

public abstract class ModBacklight {
    public static final int MODE_COREAUTO = 1;
    public static final int MODE_MODAUTO = 2;
    public static final int MODE_UNKNOWN = -1;
    public static final int MODE_USER = 0;

    public abstract byte getModBacklightBrightness();

    public abstract int getModBacklightMode();

    public abstract boolean setModBacklightBrightness(byte b);

    public abstract boolean setModBacklightMode(int i);
}
