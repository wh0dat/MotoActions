package com.motorola.actions.attentivedisplay;

import android.content.Context;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.RunningTasksUtils;

public final class FeatureManager {
    private static final MALogger LOGGER = new MALogger(FeatureManager.class);

    public static void start(Context context) {
        AttentiveDisplayInitService.onConfigUpdated();
        if (AttentiveDisplayService.isFeatureSupported(context)) {
            AttentiveDisplayTracker.getInstance().resetInactivityTimeout();
        }
    }

    public static void stop(Context context) {
        LOGGER.mo11957d("stop : Attentive Display");
        if (AttentiveDisplayService.isFeatureSupported(context) && RunningTasksUtils.isServiceRunning(context, AttentiveDisplayService.class)) {
            context.stopService(AttentiveDisplayService.createIntent(context));
        }
        PreDimReceiver.getInstance().unregister();
        StowedDetectManager.getInstance().unregister();
    }
}
