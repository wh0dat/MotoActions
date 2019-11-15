package com.motorola.actions.utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import com.motorola.actions.ActionsApplication;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class MotoResearchUtils {
    public static final String GESTURE_TYPE_ATTENTIVE_DISPLAY = "attn_disp";
    public static final String GESTURE_TYPE_CHOP_TWICE = "chop_twice";
    public static final String GESTURE_TYPE_ENHANCED_SCREENSHOT = "enhanced_screenshot";
    public static final String GESTURE_TYPE_FLIP_TO_MUTE = "flip_to_mute";
    public static final String GESTURE_TYPE_LIFT_TO_SILENCE = "lift_to_silence";
    public static final String GESTURE_TYPE_MEDIA_CONTROL = "media_control";
    public static final String GESTURE_TYPE_MICROSCREEN = "microscreen";
    public static final String GESTURE_TYPE_QUICK_CAPTURE = "quick_capture";
    public static final String GESTURE_TYPE_WAVE_TO_DISMISS = "wave_dismiss_alarm";
    private static final String INTENT_ACTION = "com.motorola.internal.intent.action.GESTURE_TRIGGERED";
    public static final String INTENT_EXTRA_FLASHLIGHT_NEW_STATE = "flashlightNewState";
    public static final String INTENT_EXTRA_FLASHLIGHT_NEW_STATE_OFF = "OFF";
    public static final String INTENT_EXTRA_FLASHLIGHT_NEW_STATE_ON = "ON";
    private static final String INTENT_EXTRA_TYPE = "type";

    @Retention(RetentionPolicy.SOURCE)
    public @interface GestureType {
    }

    protected static void sendIntent(@NonNull String str, String str2) {
        Context appContext = ActionsApplication.getAppContext();
        if (!Constants.PRODUCTION_MODE && PackageManagerUtils.isInstalled(str)) {
            appContext.sendBroadcast(getIntent(str, str2));
        }
    }

    protected static void sendIntent(@NonNull String str, String str2, @NonNull String str3, @NonNull String str4) {
        Context appContext = ActionsApplication.getAppContext();
        if (PackageManagerUtils.isInstalled(str) && !Constants.PRODUCTION_MODE) {
            Intent intent = getIntent(str, str2);
            intent.putExtra(str3, str4);
            appContext.sendBroadcast(intent);
        }
    }

    protected static Intent getIntent(@NonNull String str, String str2) {
        Intent intent = new Intent();
        intent.setPackage(str);
        intent.setAction(INTENT_ACTION);
        intent.putExtra("type", str2);
        return intent;
    }
}
