package com.motorola.actions.p010qc;

import com.motorola.actions.reflect.SensorPrivateProxy;
import com.motorola.actions.utils.Device;
import com.motorola.actions.utils.MotoCameraUtils;

/* renamed from: com.motorola.actions.qc.QuickCaptureConfig */
public class QuickCaptureConfig {
    public static boolean isSupported() {
        return SensorPrivateProxy.isProxySensorAvailable(SensorPrivateProxy.TYPE_CAMERA_ACTIVATE);
    }

    public static void disableOnAndroidOne() {
        if (Device.isAndroidOne() && isSupported() && !MotoCameraUtils.isMotoCameraEnabled()) {
            QuickDrawHelper.setEnabled(false);
        }
    }
}
