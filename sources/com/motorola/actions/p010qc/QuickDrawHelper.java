package com.motorola.actions.p010qc;

import android.content.Context;
import com.motorola.actions.reflect.SystemPropertiesProxy;
import com.motorola.actions.utils.AndroidResourceAccess;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.SettingsWrapper;
import java.util.Optional;

/* renamed from: com.motorola.actions.qc.QuickDrawHelper */
public class QuickDrawHelper {
    static final String CAMERA_GESTURE_DISABLED = "camera_gesture_disabled";
    private static final int CAMERA_GESTURE_IS_DISABLED = 1;
    private static final int CAMERA_GESTURE_IS_ENABLED = 0;
    private static final MALogger LOGGER = new MALogger(QuickDrawHelper.class);
    private static final String PLATFORM_CAMERA_GESTURE_DISABLED = "gesture.disable_camera_launch";
    private static final String PLATFORM_CAMERA_LAUNCH_SENSOR_ID = "config_cameraLaunchGestureSensorType";

    public static boolean isQuickCaptureSupported(Context context) {
        boolean z = false;
        if (context.checkSelfPermission("android.permission.WRITE_SECURE_SETTINGS") != 0) {
            LOGGER.mo11957d("Permission denied - android.permission.WRITE_SECURE_SETTINGS");
            return false;
        }
        Boolean valueOf = Boolean.valueOf(SystemPropertiesProxy.getBoolean(PLATFORM_CAMERA_GESTURE_DISABLED, true));
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("gesture.disable_camera_launch: ");
        sb.append(valueOf);
        mALogger.mo11957d(sb.toString());
        if (!valueOf.booleanValue()) {
            return true;
        }
        Optional intResource = AndroidResourceAccess.getIntResource(PLATFORM_CAMERA_LAUNCH_SENSOR_ID);
        MALogger mALogger2 = LOGGER;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("config_cameraLaunchGestureSensorType: ");
        sb2.append(intResource);
        mALogger2.mo11957d(sb2.toString());
        if (intResource.isPresent() && ((Integer) intResource.get()).intValue() != -1) {
            z = true;
        }
        return z;
    }

    public static boolean isEnabled() {
        if (((Integer) SettingsWrapper.getSecureInt(CAMERA_GESTURE_DISABLED, true).orElse(Integer.valueOf(0))).intValue() == 0) {
            return true;
        }
        return false;
    }

    public static void setEnabled(boolean z) {
        SettingsWrapper.putSecureInt(CAMERA_GESTURE_DISABLED, z ^ true ? 1 : 0);
    }
}
