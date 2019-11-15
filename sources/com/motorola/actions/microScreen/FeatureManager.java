package com.motorola.actions.microScreen;

import com.motorola.actions.ServiceStartReason;
import com.motorola.actions.microScreen.discovery.MicroScreenGestureChangeNotification;
import com.motorola.actions.reflect.MotorolaSettings;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.StaticBroadcastUtils;

public class FeatureManager {
    private static final MALogger LOGGER = new MALogger(FeatureManager.class);
    private static MicroScreenGestureChangeNotification sGestureChangeNotification;

    public static void start(ServiceStartReason serviceStartReason) {
        if (MicroScreenService.isFeatureSupported()) {
            if (!serviceStartReason.equals(ServiceStartReason.ENTER_SETTINGS)) {
                MicroScreenService.stopMicroScreenService();
            }
            MotorolaSettings.setPipActive(false);
            startGlobalTouchListenerIfNeeded();
            StaticBroadcastUtils.enableStaticBroadcast(MicroScreenReceiver.class);
            if (MicroScreenGestureChangeNotification.isNeeded()) {
                if (sGestureChangeNotification == null) {
                    sGestureChangeNotification = new MicroScreenGestureChangeNotification();
                }
                sGestureChangeNotification.createMicroScreenGestureChangedNotification();
            }
        }
    }

    public static void stop() {
        LOGGER.mo11957d("stop: MicroScreen");
        if (MicroScreenService.isFeatureSupported()) {
            MotorolaSettings.setGlobalTouchListenerValue(0);
            StaticBroadcastUtils.disableStaticBroadcast(MicroScreenReceiver.class);
            if (sGestureChangeNotification != null) {
                sGestureChangeNotification.dismissMicroScreenGestureChangeNotification();
                sGestureChangeNotification = null;
            }
            MicroScreenService.stopMicroScreenService();
        }
    }

    public static void startGlobalTouchListenerIfNeeded() {
        if (MicroScreenService.isServiceEnabled()) {
            MotorolaSettings.setGlobalTouchListenerValue(1);
        }
    }
}
