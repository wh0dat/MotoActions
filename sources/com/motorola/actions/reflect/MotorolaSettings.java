package com.motorola.actions.reflect;

import android.content.ContentResolver;
import android.net.Uri;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.enhancedscreenshot.EnhancedScreenshotHelper;
import com.motorola.actions.onenav.OneNavHelper;
import com.motorola.actions.onenav.instrumentation.InstrumentationTutorialStep;
import com.motorola.actions.onenav.instrumentation.OneNavInstrumentation;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.SetupObserver;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class MotorolaSettings {
    private static final String ACTIONS_ENHANCED_SCREENSHOT = "actions_enhanced_screenshot";
    private static final String ACTIONS_MUSIC_MODE = "actions_music_mode";
    private static final String AUTHORITY = "com.motorola.android.providers.settings";
    private static final String CONTENT_SCHEMA = "content://";
    private static final String FPS_LOCKS_SCREEN = "display_off_by_fps";
    private static final String FPS_ONENAV_ENABLED = "fps_onenav_enabled";
    private static final String FPS_ONENAV_HAPTIC_FEEDBACK_ENABLED = "fps_onenav_haptic_feedback_enabled";
    private static final String FPS_ONENAV_SWIPE_DIRECTION = "fps_onenav_swipe_direction";
    private static final String FPS_ONENAV_TUTORIAL_MODE = "fps_onenav_tutorial_mode";
    public static final String FPS_SIDE_GESTURE_APP_SCROLLING_ENABLED = "fps_slidegesture_app_scrolling_enabled";
    public static final String FPS_SIDE_GESTURE_DIRECTION_REVERSE = "fps_slidegesture_direction_reverse";
    private static final String FPS_SIDE_GESTURE_ENABLED = "fps_slidegesture_enabled";
    public static final String FPS_SIDE_GESTURE_SHOTCUT_ENABLED = "fps_slidegesture_shotcut_enabled";
    private static final String FTM_FDN_DND_TURNED_OFF = "ftm_fdn_dnd_turned_off";
    private static final String GLOBAL_SINGLE_HAND_ON = "global_single_hand_on";
    private static final String GLOBAL_TOUCH_LISTENER = "global_touch_listener";
    /* access modifiers changed from: private */
    public static final MALogger LOGGER = new MALogger(MotorolaSettings.class);
    private static final String PATH_SECURE = "/secure/";
    private static final String PIP_ACTIVE = "pip_active";
    private static final String PROPERTY_FACEUNLOCK_AVAILABLE = "property_faceunlock_available";
    private static final String PROPERTY_LIFT_TO_UNLOCK = "property_lift_to_unlock";
    private static final String QUICK_SCREENSHOT_TRIGGER_GESTURE = "screenshot_trigger_gesture";
    private static final String SOFTONENAV_DISCOVERY = "softonenav_discovery";
    private static final String SOFTONENAV_TUTORIAL_ANIMATION = "softonenav_tutorial_animation";
    private static final String SYSTEM_VIEW_VISIBILITY = "status_bar_visibility";

    public static final class Global {
        private static final String MOTO_SETTINGS_PROVIDER_CLASS_NAME_GLOBAL = "com.motorola.android.provider.MotorolaSettings$Global";
        private static Global sInstance;
        private Method mGetInt;

        /* access modifiers changed from: private */
        public static synchronized Global getInstance() {
            Global global;
            synchronized (Global.class) {
                if (sInstance == null) {
                    sInstance = new Global();
                }
                global = sInstance;
            }
            return global;
        }

        private Global() {
            try {
                this.mGetInt = Class.forName(MOTO_SETTINGS_PROVIDER_CLASS_NAME_GLOBAL).getMethod("getInt", new Class[]{ContentResolver.class, String.class, Integer.TYPE});
            } catch (ClassNotFoundException | NoSuchMethodException e) {
                MotorolaSettings.LOGGER.mo11960e("Motorola settings class error", e);
            }
        }

        /* access modifiers changed from: private */
        public int getMotoSettingValueAsInt(String str, int i) {
            try {
                if (this.mGetInt != null) {
                    return ((Integer) this.mGetInt.invoke(null, new Object[]{ActionsApplication.getAppContext().getContentResolver(), str, Integer.valueOf(i)})).intValue();
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                MotorolaSettings.LOGGER.mo11960e("getInt error: ", e);
            }
            return i;
        }
    }

    public static final class Secure {
        private static final String MOTO_SETTINGS_PROVIDER_CLASS_NAME_SECURE = "com.motorola.android.provider.MotorolaSettings$Secure";
        private static Secure sInstance;
        private Method mGetInt;
        private Method mPutInt;

        private Secure() {
            try {
                Class cls = Class.forName(MOTO_SETTINGS_PROVIDER_CLASS_NAME_SECURE);
                this.mGetInt = cls.getMethod("getInt", new Class[]{ContentResolver.class, String.class, Integer.TYPE});
                this.mPutInt = cls.getMethod("putInt", new Class[]{ContentResolver.class, String.class, Integer.TYPE});
            } catch (ClassNotFoundException | NoSuchMethodException e) {
                MotorolaSettings.LOGGER.mo11960e("Motorola settings class error", e);
            }
        }

        /* access modifiers changed from: private */
        public static synchronized Secure getInstance() {
            Secure secure;
            synchronized (Secure.class) {
                if (sInstance == null) {
                    sInstance = new Secure();
                }
                secure = sInstance;
            }
            return secure;
        }

        /* access modifiers changed from: private */
        public int getMotoSettingValueAsInt(String str, int i) {
            try {
                if (this.mGetInt != null) {
                    return ((Integer) this.mGetInt.invoke(null, new Object[]{ActionsApplication.getAppContext().getContentResolver(), str, Integer.valueOf(i)})).intValue();
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                MotorolaSettings.LOGGER.mo11960e("getInt error: ", e);
            }
            return i;
        }

        /* access modifiers changed from: private */
        public void setMotoSettingValueAsInt(String str, int i) {
            try {
                if (this.mPutInt != null) {
                    this.mPutInt.invoke(null, new Object[]{ActionsApplication.getAppContext().getContentResolver(), str, Integer.valueOf(i)});
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                MotorolaSettings.LOGGER.mo11960e("putInt error: ", e);
            }
        }

        /* access modifiers changed from: private */
        public boolean motoSettingsValueExists(String str) {
            try {
                if (this.mGetInt == null) {
                    return true;
                }
                this.mGetInt.invoke(null, new Object[]{ActionsApplication.getAppContext().getContentResolver(), str, Integer.valueOf(0)});
                return true;
            } catch (IllegalAccessException | InvocationTargetException unused) {
                return false;
            }
        }
    }

    public static int getOneNavEnabled(int i) {
        return Secure.getInstance().getMotoSettingValueAsInt(FPS_ONENAV_ENABLED, i);
    }

    public static void setOneNavEnabled(int i, boolean z) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("setOneNavEnabled: value = ");
        sb.append(i);
        sb.append(", needToRecordInstrumentation = ");
        sb.append(z);
        mALogger.mo11957d(sb.toString());
        Secure.getInstance().setMotoSettingValueAsInt(FPS_ONENAV_ENABLED, i);
        if (z) {
            boolean z2 = i == 1;
            OneNavInstrumentation.recordOneNavEnableStatusChanged(z2);
            if (z2) {
                OneNavInstrumentation.recordOneNavTutorialStep(InstrumentationTutorialStep.ON);
            }
        }
        if (OneNavHelper.shouldDisableFPSLock()) {
            setFingerPrintLockScreen(false);
        } else if (i == 1) {
            setFingerPrintLockScreen(true);
        }
    }

    public static int getOneNavBackSwipeDirection(int i) {
        return Secure.getInstance().getMotoSettingValueAsInt(FPS_ONENAV_SWIPE_DIRECTION, i);
    }

    public static void setOneNavBackSwipeDirection(int i) {
        Secure.getInstance().setMotoSettingValueAsInt(FPS_ONENAV_SWIPE_DIRECTION, i);
    }

    public static void setOneNavTutorialActive() {
        Secure.getInstance().setMotoSettingValueAsInt(FPS_ONENAV_TUTORIAL_MODE, 1);
    }

    public static void setOneNavTutorialInactive() {
        Secure.getInstance().setMotoSettingValueAsInt(FPS_ONENAV_TUTORIAL_MODE, 0);
    }

    public static void setSoftOneNavTutorialAnimation(int i) {
        Secure.getInstance().setMotoSettingValueAsInt(SOFTONENAV_TUTORIAL_ANIMATION, i);
    }

    public static void setQuickScreenshotMode(int i) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("setQuickScreenshotMode ");
        sb.append(i);
        mALogger.mo11957d(sb.toString());
        Secure.getInstance().setMotoSettingValueAsInt(QUICK_SCREENSHOT_TRIGGER_GESTURE, i);
    }

    public static boolean isQuickScreenshotEnabled() {
        boolean z = true;
        if (Secure.getInstance().getMotoSettingValueAsInt(QUICK_SCREENSHOT_TRIGGER_GESTURE, 1) == 0) {
            z = false;
        }
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("isQuickScreenshotEnabled = ");
        sb.append(z);
        mALogger.mo11957d(sb.toString());
        return z;
    }

    public static void setGlobalTouchListenerValue(int i) {
        Secure.getInstance().setMotoSettingValueAsInt(GLOBAL_TOUCH_LISTENER, i);
    }

    public static void setFingerPrintLockScreen(boolean z) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("setFingerPrintLockScreen ");
        sb.append(z);
        mALogger.mo11957d(sb.toString());
        Secure.getInstance().setMotoSettingValueAsInt(FPS_LOCKS_SCREEN, z ? 1 : 0);
    }

    public static boolean fingerPrintLockScreenExists() {
        return Secure.getInstance().motoSettingsValueExists(FPS_LOCKS_SCREEN);
    }

    public static boolean isPipActive() {
        return Secure.getInstance().getMotoSettingValueAsInt(PIP_ACTIVE, 0) == 1;
    }

    public static void setPipActive(boolean z) {
        Secure.getInstance().setMotoSettingValueAsInt(PIP_ACTIVE, z ? 1 : 0);
    }

    public static Uri getOneNavUri() {
        return Uri.parse("content://com.motorola.android.providers.settings/secure/fps_onenav_enabled");
    }

    public static Uri getFlipToDNDFDNUri() {
        return Uri.parse("content://com.motorola.android.providers.settings/secure/ftm_fdn_dnd_turned_off");
    }

    public static Uri getSystemViewVisibilityUri() {
        return Uri.parse("content://com.motorola.android.providers.settings/secure/status_bar_visibility");
    }

    public static Uri getSoftOneNavDiscoveryUri() {
        return Uri.parse("content://com.motorola.android.providers.settings/secure/softonenav_discovery");
    }

    public static Uri getLtuUri() {
        return Uri.parse("content://com.motorola.android.providers.settings/secure/property_lift_to_unlock");
    }

    public static Uri getFaceunlockUri() {
        return Uri.parse("content://com.motorola.android.providers.settings/secure/property_faceunlock_available");
    }

    public static int getSystemViewVisibility() {
        return Secure.getInstance().getMotoSettingValueAsInt(SYSTEM_VIEW_VISIBILITY, 0);
    }

    public static boolean getOneNavVibrationSetting() {
        if (Secure.getInstance().getMotoSettingValueAsInt(FPS_ONENAV_HAPTIC_FEEDBACK_ENABLED, 1) != 0) {
            return true;
        }
        return false;
    }

    public static void setEnhancedScreenshot(boolean z) {
        Secure.getInstance().setMotoSettingValueAsInt(ACTIONS_ENHANCED_SCREENSHOT, z ? 1 : 0);
    }

    public static boolean isEnhancedScreenshot() {
        int access$100 = Secure.getInstance().getMotoSettingValueAsInt(ACTIONS_ENHANCED_SCREENSHOT, -1);
        boolean z = false;
        if (access$100 == -1 && SetupObserver.isSetupFinished()) {
            if (EnhancedScreenshotHelper.isFeatureSupported()) {
                setEnhancedScreenshot(true);
                LOGGER.mo11957d("Enhanced Screenshot supported, set default true");
                access$100 = 1;
            } else {
                setEnhancedScreenshot(false);
                LOGGER.mo11957d("Enhanced Screenshot not supported, set default false");
                access$100 = 0;
            }
        }
        if (access$100 == 1) {
            z = true;
        }
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("isEnhancedScreenshot = ");
        sb.append(z);
        mALogger.mo11957d(sb.toString());
        return z;
    }

    public static void setOneNavVibrationSetting(boolean z) {
        Secure.getInstance().setMotoSettingValueAsInt(FPS_ONENAV_HAPTIC_FEEDBACK_ENABLED, z ? 1 : 0);
    }

    public static int getFlipToDNDFDNTurnedOffValue(int i) {
        return Secure.getInstance().getMotoSettingValueAsInt(FTM_FDN_DND_TURNED_OFF, i);
    }

    public static void setFlipToDNDFDNTurnedOffValue(int i) {
        Secure.getInstance().setMotoSettingValueAsInt(FTM_FDN_DND_TURNED_OFF, i);
    }

    public static void setMusicMode(int i) {
        Secure.getInstance().setMotoSettingValueAsInt(ACTIONS_MUSIC_MODE, i);
    }

    public static int getSoftOneNavDiscovery() {
        return Secure.getInstance().getMotoSettingValueAsInt(SOFTONENAV_DISCOVERY, 0);
    }

    public static void setSoftOneNavDiscovery(int i) {
        Secure.getInstance().setMotoSettingValueAsInt(SOFTONENAV_DISCOVERY, i);
    }

    public static int getSingleHandOnValue() {
        return Global.getInstance().getMotoSettingValueAsInt(GLOBAL_SINGLE_HAND_ON, 0);
    }

    public static boolean isFPSSlideEnabled() {
        return Secure.getInstance().getMotoSettingValueAsInt(FPS_SIDE_GESTURE_ENABLED, 0) == 1;
    }

    public static void setFPSSlideEnabled(boolean z, boolean z2) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("setFPSSlideEnabled: status = ");
        sb.append(z);
        sb.append(", needToRecordInstrumentation = ");
        sb.append(z2);
        mALogger.mo11957d(sb.toString());
        Secure.getInstance().setMotoSettingValueAsInt(FPS_SIDE_GESTURE_ENABLED, z ? 1 : 0);
    }

    public static boolean isFPSSlideReversed() {
        return Secure.getInstance().getMotoSettingValueAsInt(FPS_SIDE_GESTURE_DIRECTION_REVERSE, 0) == 1;
    }

    public static void setFPSSlideReversed(boolean z) {
        Secure.getInstance().setMotoSettingValueAsInt(FPS_SIDE_GESTURE_DIRECTION_REVERSE, z ? 1 : 0);
    }

    public static boolean isFPSSlideOnHomeEnabled() {
        return Secure.getInstance().getMotoSettingValueAsInt(FPS_SIDE_GESTURE_SHOTCUT_ENABLED, 0) == 1;
    }

    public static void setFPSSlideOnHomeEnabled(boolean z) {
        Secure.getInstance().setMotoSettingValueAsInt(FPS_SIDE_GESTURE_SHOTCUT_ENABLED, z ? 1 : 0);
    }

    public static boolean isFPSSlideOnAppEnabled() {
        return Secure.getInstance().getMotoSettingValueAsInt(FPS_SIDE_GESTURE_APP_SCROLLING_ENABLED, 0) == 1;
    }

    public static void setFPSSlideOnAppEnabled(boolean z) {
        Secure.getInstance().setMotoSettingValueAsInt(FPS_SIDE_GESTURE_APP_SCROLLING_ENABLED, z ? 1 : 0);
    }

    public static boolean isLiftToUnlockEnabled() {
        boolean z = true;
        if (Secure.getInstance().getMotoSettingValueAsInt(PROPERTY_LIFT_TO_UNLOCK, 0) != 1) {
            z = false;
        }
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("isLiftToUnlockEnabled: ");
        sb.append(z);
        mALogger.mo11957d(sb.toString());
        return z;
    }

    public static void setLiftToUnlockEnabled(boolean z) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("setLiftToUnlockEnabled: status = ");
        sb.append(z);
        mALogger.mo11957d(sb.toString());
        Secure.getInstance().setMotoSettingValueAsInt(PROPERTY_LIFT_TO_UNLOCK, z ? 1 : 0);
    }

    public static boolean isFaceEnrolled() {
        boolean z = true;
        if (Secure.getInstance().getMotoSettingValueAsInt(PROPERTY_FACEUNLOCK_AVAILABLE, 0) != 1) {
            z = false;
        }
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("isFaceEnrolled: ");
        sb.append(z);
        mALogger.mo11957d(sb.toString());
        return z;
    }
}
