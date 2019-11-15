package com.motorola.actions.utils;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import com.motorola.mod.ModDevice;

public final class Constants {
    public static final int APPROACH_VIDEO = 2131558401;
    public static final int ATNT_DSP_VIDEO = 2131558439;
    public static final boolean DEBUG = (!ModDevice.USER.equals(Build.TYPE));
    public static final int ENHANCED_SCREENSHOT_VIDEO = 2131558433;
    public static final String FLASH_ON_CHOP_TAG = "FOC";
    public static final int FLASH_ON_CHOP_VIDEO = 2131558402;
    public static final boolean FOC_TURN_ON_BY_PERMISSIONS_GRANTED_DEFAULT_VALUE = false;
    public static final String FPS_LOCK_SCREEN_SHOWN = "fps_lockscreen_shown";
    public static final int FPS_SLIDE_APP_VIDEO = 2131558404;
    public static final int FPS_SLIDE_HOME_VIDEO = 2131558405;
    public static final int FTM_VIDEO = 2131558403;
    public static final float FULL_ALPHA = 1.0f;
    public static final float HALF_ALPHA = 0.5f;
    public static final String INSTRUMENTATION_INSTANCE_TAG = "Instance";
    public static final int LIFT_TO_SILENCE_VIDEO = 2131558406;
    public static final int LIFT_TO_UNLOCK_VIDEO = 2131558407;
    public static final int MEDIA_CONTROL_VIDEO = 2131558410;
    public static final int MICROSCREEN_SWIPE_DOWN_SETTINGS_VIDEO = 2131558412;
    public static final int MICROSCREEN_SWIPE_DOWN_TUTORIAL_VIDEO = 2131558413;
    public static final String ND_SCREEN_ALREADY_SHOWN = "nd_screen_already_shown";
    public static final int NIGHT_DISPLAY_VIDEO = 2131558414;
    public static final String ONENAV_SCREEN_ALREADY_SHOWN = "onenav_screen_already_shown";
    public static final int ONENAV_SLIM_VIDEO = 2131558416;
    public static final int ONENAV_VIDEO = 2131558415;
    public static final String PREFERENCE_ACTIONS_TUTORIAL_PLAYED = "actions_tutorial_played";
    public static final boolean PRODUCTION_MODE;
    public static final int QUICKDRAW_VIDEO = 2131558437;
    public static final int QUICKDRAW_VIDEO_LOWRES = 2131558438;
    public static final String QUICK_SCREENSHOT_SCREEN_ALREADY_SHOWN = "qs_screen_already_shown";
    public static final int SCREENSHOT_VIDEO = 2131558433;
    public static final int SOFTONENAV_VIDEO = 2131558436;
    public static final boolean WAVE_TO_SILENCE_DEFAULT_SETTING_VALUE = true;

    static {
        boolean z = true;
        if (!ModDevice.USER.equals(Build.TYPE) || Build.TAGS.contains("intcfg")) {
            z = false;
        }
        PRODUCTION_MODE = z;
    }

    public static String getApkVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException unused) {
            return "unknown";
        }
    }
}
