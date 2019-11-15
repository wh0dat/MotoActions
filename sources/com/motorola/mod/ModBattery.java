package com.motorola.mod;

import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;

public abstract class ModBattery {
    @Deprecated
    public static final int BATTERY_ATTACH_START_SOC = 0;
    @Deprecated
    public static final int BATTERY_ATTACH_STOP_SOC = 1;
    public static final int BATTERY_EFFICIENCY_MODE = 8;
    public static final int BATTERY_EFFICIENCY_OFF = 0;
    public static final int BATTERY_EFFICIENCY_ON = 1;
    public static final int BATTERY_LOW_START_SOC = 4;
    public static final int BATTERY_LOW_STOP_SOC = 5;
    public static final int BATTERY_ON_SW = 7;
    public static final int BATTERY_RECHARGE_START_SOC = 2;
    public static final int BATTERY_RECHARGE_STOP_SOC = 3;
    public static final int BATTERY_USAGE_TYPE = 6;
    public static final int BATTERY_USAGE_TYPE_EMERGENCY = 3;
    public static final int BATTERY_USAGE_TYPE_REMOTE = 1;
    public static final int BATTERY_USAGE_TYPE_SUPPLEMENTAL = 2;
    public static final int BATTERY_USAGE_TYPE_UNKNOWN = 0;
    public static final int MOD_POWER_SOURCE = 9;
    public static final int MOD_POWER_SOURCE_BATTERY = 2;
    public static final int MOD_POWER_SOURCE_BATTERY_TURBO = 6;
    public static final int MOD_POWER_SOURCE_NONE = 1;
    public static final int MOD_POWER_SOURCE_NONE_TURBO = 5;
    public static final int MOD_POWER_SOURCE_UNKNOWN = 0;
    public static final int MOD_POWER_SOURCE_WIRED = 3;
    public static final int MOD_POWER_SOURCE_WIRED_TURBO = 7;
    public static final int MOD_POWER_SOURCE_WIRELESS = 4;
    public static final int MOD_POWER_SOURCE_WIRELESS_TURBO = 8;

    public abstract int getIntProperty(int i);

    public abstract void setIntProperty(int i, int i2);

    public static int getBatteryLevel(Intent intent) {
        if (intent != null) {
            return intent.getIntExtra("mod_level", -1);
        }
        return -1;
    }

    public static int getBatteryStatus(Intent intent) {
        if (intent != null) {
            return intent.getIntExtra("mod_status", 1);
        }
        return 1;
    }

    public static int getBatteryType(Intent intent) {
        if (intent != null) {
            return intent.getIntExtra("mod_type", 0);
        }
        return 0;
    }

    public static int getPowerSource(Intent intent) {
        if (intent != null) {
            return intent.getIntExtra("mod_psrc", 1);
        }
        return 1;
    }

    public static boolean isPlugTypeMod(Intent intent) {
        if ((intent != null ? intent.getIntExtra("plugged_raw", intent.getIntExtra("plugged", 0)) : 0) == 8) {
            return true;
        }
        return false;
    }

    public static long getBatteryCapacity(Context context) {
        long longProperty = context != null ? ((BatteryManager) context.getSystemService("batterymanager")).getLongProperty(100) : Long.MIN_VALUE;
        return longProperty != Long.MIN_VALUE ? longProperty / 1000 : longProperty;
    }
}
