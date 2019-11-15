package com.motorola.actions.lts;

import android.content.Context;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.RunningTasksUtils;
import com.motorola.actions.utils.ServiceUtils;

public final class FeatureManager {
    private static final MALogger LOGGER = new MALogger(FeatureManager.class);

    public static void start(Context context) {
        if (LiftToSilenceService.isLiftToSilenceEnabled()) {
            ServiceUtils.startServiceSafe(LiftToSilenceService.createIntent(context));
        }
    }

    public static void stop(Context context) {
        if (LiftToSilenceService.isFeatureSupported() && RunningTasksUtils.isServiceRunning(context, LiftToSilenceService.class)) {
            LOGGER.mo11957d("stop : Lift To Silence");
            context.stopService(LiftToSilenceService.createIntent(context));
        }
    }
}
