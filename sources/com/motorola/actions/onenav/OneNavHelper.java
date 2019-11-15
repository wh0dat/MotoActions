package com.motorola.actions.onenav;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.ComponentName;
import android.net.Uri;
import android.os.Handler;
import android.view.accessibility.AccessibilityManager;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.C0504R;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.reflect.MotorolaSettings;
import com.motorola.actions.reflect.SystemPropertiesProxy;
import com.motorola.actions.utils.AccessibilityUtils;
import com.motorola.actions.utils.AndroidResourceAccess;
import com.motorola.actions.utils.Constants;
import com.motorola.actions.utils.Device;
import com.motorola.actions.utils.MALogger;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

public class OneNavHelper {
    private static final String FPS_HW_SYS_PROPERTY = "ro.hw.fps";
    private static final String FPS_HW_SYS_PROPERTY_P = "ro.vendor.hw.fps";
    private static final MALogger LOGGER = new MALogger(OneNavHelper.class);
    private static final String MOTOROLA_WRITE_SECURE_SETTINGS = "com.motorola.permission.WRITE_SECURE_SETTINGS";
    public static final int ONENAV_FPS = 1;
    public static final int ONENAV_INVALID = 0;
    public static final int ONENAV_SOFTWARE = 2;
    private static final String PLATFORM_ENABLE_FINGERPRINT_ONENAV = "config_enableFingerprintOneNav";
    private static final String PLATFORM_ENABLE_SOFTWARE_ONENAV = "config_enableSoftOneNav";
    private static final String TALKBACK_PACKAGE = "com.google.android.marvin.talkback/.TalkBackService";
    private static OneNavContentObserver sOneNavContentObserver;

    @Retention(RetentionPolicy.SOURCE)
    private @interface OneNavType {
    }

    public static int getOneNavStatus(boolean z) {
        return z ? 1 : 0;
    }

    static final /* synthetic */ boolean lambda$isAccessibilityServiceFingerprint$0$OneNavHelper(int i) {
        return i == 64;
    }

    public static boolean isOneNavPresent() {
        boolean z = false;
        if (Device.isAndroidOne() || isUTouchPresent()) {
            return false;
        }
        if ((SystemPropertiesProxy.getBoolean(FPS_HW_SYS_PROPERTY, false) || SystemPropertiesProxy.getBoolean(FPS_HW_SYS_PROPERTY_P, false)) && (((Boolean) AndroidResourceAccess.getBoolResource(PLATFORM_ENABLE_FINGERPRINT_ONENAV).orElse(Boolean.valueOf(false))).booleanValue() || ((Boolean) AndroidResourceAccess.getBoolResource(PLATFORM_ENABLE_SOFTWARE_ONENAV).orElse(Boolean.valueOf(false))).booleanValue())) {
            z = true;
        }
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("isOneNavPresent - ");
        sb.append(z);
        mALogger.mo11957d(sb.toString());
        return z;
    }

    public static int getOneNavType() {
        int i = 0;
        if (((Boolean) AndroidResourceAccess.getBoolResource(PLATFORM_ENABLE_FINGERPRINT_ONENAV).orElse(Boolean.valueOf(false))).booleanValue()) {
            i = 1;
        } else if (((Boolean) AndroidResourceAccess.getBoolResource(PLATFORM_ENABLE_SOFTWARE_ONENAV).orElse(Boolean.valueOf(false))).booleanValue()) {
            i = 2;
        }
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("oneNavType - ");
        sb.append(i);
        mALogger.mo11957d(sb.toString());
        return i;
    }

    public static boolean isSoftOneNav() {
        return getOneNavType() == 2;
    }

    private static boolean isUTouchPresent() {
        Optional boolResource = AndroidResourceAccess.getBoolResource("config_utouch_enabled");
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("isUTouchPresent - ");
        sb.append(boolResource);
        mALogger.mo11957d(sb.toString());
        return ((Boolean) boolResource.orElse(Boolean.valueOf(false))).booleanValue();
    }

    public static boolean isMotorolaPermissionGranted() {
        boolean z = ActionsApplication.getAppContext().checkSelfPermission(MOTOROLA_WRITE_SECURE_SETTINGS) == 0;
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("isMotorolaPermissionGranted - ");
        sb.append(z);
        mALogger.mo11957d(sb.toString());
        return z;
    }

    public static boolean isOneNavEnabled() {
        boolean z = false;
        if (MotorolaSettings.getOneNavEnabled(0) != 0) {
            z = true;
        }
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("isOneNavEnabled - ");
        sb.append(z);
        mALogger.mo11957d(sb.toString());
        return z;
    }

    public static boolean isRTL() {
        return ActionsApplication.getAppContext().getResources().getBoolean(C0504R.bool.is_right_to_left);
    }

    static void registerObserver() {
        Uri oneNavUri = MotorolaSettings.getOneNavUri();
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("OneNav Content Observer registerObserver ");
        sb.append(oneNavUri.toString());
        mALogger.mo11957d(sb.toString());
        if (sOneNavContentObserver == null) {
            sOneNavContentObserver = new OneNavContentObserver(new Handler());
        }
        ActionsApplication.getAppContext().getContentResolver().registerContentObserver(oneNavUri, false, sOneNavContentObserver);
    }

    static void unregisterObserver() {
        LOGGER.mo11957d("OneNav Content Observer unregisterObserver ");
        if (sOneNavContentObserver != null) {
            ActionsApplication.getAppContext().getContentResolver().unregisterContentObserver(sOneNavContentObserver);
            sOneNavContentObserver = null;
        }
    }

    public static boolean shouldShowWelcomeScreen() {
        return !SharedPreferenceManager.getBoolean(Constants.ONENAV_SCREEN_ALREADY_SHOWN, false);
    }

    public static Set<AccessibilityServiceInfo> getConflictServicesEnabled() {
        HashSet hashSet = new HashSet();
        Set enabledServicesFromSettings = AccessibilityUtils.getEnabledServicesFromSettings();
        AccessibilityManager accessibilityManager = (AccessibilityManager) ActionsApplication.getAppContext().getSystemService("accessibility");
        if (accessibilityManager != null) {
            for (AccessibilityServiceInfo accessibilityServiceInfo : accessibilityManager.getInstalledAccessibilityServiceList()) {
                if (enabledServicesFromSettings.contains(ComponentName.unflattenFromString(accessibilityServiceInfo.getId())) && (isAccessibilityServiceFingerprint(accessibilityServiceInfo) || (isSoftOneNav() && isAccessibilityServiceTalkBack(accessibilityServiceInfo)))) {
                    hashSet.add(accessibilityServiceInfo);
                }
            }
        } else {
            LOGGER.mo11959e("Could not retrieve access to accessibility manager.");
        }
        return hashSet;
    }

    private static boolean isAccessibilityServiceFingerprint(AccessibilityServiceInfo accessibilityServiceInfo) {
        boolean anyMatch = IntStream.of(accessibilityServiceInfo.getCapabilities()).anyMatch(OneNavHelper$$Lambda$0.$instance);
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("Service: ");
        sb.append(accessibilityServiceInfo.getId());
        sb.append(" isAccessibilityServiceFingerprint = ");
        sb.append(String.valueOf(anyMatch));
        mALogger.mo11957d(sb.toString());
        return anyMatch;
    }

    private static boolean isAccessibilityServiceTalkBack(AccessibilityServiceInfo accessibilityServiceInfo) {
        boolean equals = accessibilityServiceInfo.getId().equals(TALKBACK_PACKAGE);
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("Service: ");
        sb.append(accessibilityServiceInfo.getId());
        sb.append(" isAccessibilityServiceTalkBack = ");
        sb.append(String.valueOf(equals));
        mALogger.mo11957d(sb.toString());
        return equals;
    }

    public static void turnOffAccessibilityServices(Set<AccessibilityServiceInfo> set) {
        for (AccessibilityServiceInfo id : set) {
            AccessibilityUtils.setAccessibilityServiceState(ComponentName.unflattenFromString(id.getId()), false);
        }
    }

    public static int getResourceForType(int i, int i2) {
        return getOneNavType() == 1 ? i : i2;
    }

    public static boolean isSwipeUpConflicted() {
        boolean z = isSoftOneNav() && SwipeUpGestureHelper.isGestureAvailable() && SwipeUpGestureHelper.isEnabled();
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("isSwipeUpConflicted: ");
        sb.append(z);
        mALogger.mo11957d(sb.toString());
        return z;
    }

    public static boolean shouldDisableFPSLock() {
        return !Device.isVoyagerDevice() && (Device.hasBackFPSSensor() || Device.hasSideFPSSensor() || isSoftOneNav());
    }
}
