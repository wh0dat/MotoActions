package com.motorola.mod;

public abstract class ModDisplay {
    public static final String ACTION_DISPLAY_OFF = "com.motorola.mod.action.display.OFF";
    public static final String ACTION_DISPLAY_OFF_INTERNAL = "com.motorola.mod.action.display.OFF_INTERNAL";
    public static final String ACTION_DISPLAY_ON = "com.motorola.mod.action.display.ON";
    public static final String ACTION_DISPLAY_ON_INTERNAL = "com.motorola.mod.action.display.ON_INTERNAL";
    public static final String ACTION_PRIVACY_MODE_OFF = "com.motorola.mod.action.privacy.OFF";
    public static final String ACTION_PRIVACY_MODE_ON = "com.motorola.mod.action.privacy.ON";
    public static final String EXTRA_FPS_NAV_EC_BIT = "fpsNavEcBit";
    public static final String EXTRA_POWER_MODE = "powerMode";
    public static final int POWER_MODE_FOLLOW = 0;
    public static final int POWER_MODE_NOFOLLOW = 1;
    public static final int POWER_MODE_UNKNOWN = -1;
    public static final int STATE_OFF = 0;
    public static final int STATE_ON = 2;
    public static final int STATE_STANDBY = 1;
    public static final int STATE_UNKNOWN = -1;

    public abstract int getModDisplayFollowState();

    public abstract int getModDisplayState();

    public abstract int getPrivacyMode();

    public abstract boolean setModDisplayFollowState(int i);

    public abstract boolean setModDisplayState(int i);

    public abstract boolean setPrivacyMode(int i);
}
