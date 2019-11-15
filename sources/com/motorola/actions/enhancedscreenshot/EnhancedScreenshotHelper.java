package com.motorola.actions.enhancedscreenshot;

import com.motorola.actions.utils.Device;
import com.motorola.actions.utils.MALogger;

public class EnhancedScreenshotHelper {
    private static final MALogger LOGGER = new MALogger(EnhancedScreenshotHelper.class);

    public static boolean isFeatureSupported() {
        boolean z = (!Device.isZuiDevice() && (Device.isShelbyPlusDevice() || Device.isShelbyDevice() || Device.isZachDevice() || Device.isAffinityDevice() || Device.isCopperfieldDevice() || Device.isD52Device() || Device.isDanteDevice() || Device.isRioDevice() || Device.isOsloDevice() || Device.isIbisDevice() || Device.isTellerDevice() || Device.isBlaineDevice())) || Device.isStingrayDevice() || Device.isVoyagerDevice();
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("isFeatureSupported=");
        sb.append(z);
        mALogger.mo11957d(sb.toString());
        return z;
    }

    public static boolean isEnabled() {
        boolean isServiceEnabled = EnhancedScreenshotModel.isServiceEnabled();
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("enabled - ");
        sb.append(isServiceEnabled);
        mALogger.mo11957d(sb.toString());
        return isServiceEnabled;
    }
}
