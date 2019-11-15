package com.motorola.actions.ltu;

import android.content.Context;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.RunningTasksUtils;
import com.motorola.actions.utils.ServiceUtils;

public final class FeatureManager {
    private static final MALogger LOGGER = new MALogger(FeatureManager.class);

    public static void start() {
        if (LiftToUnlockHelper.isFeatureSupported()) {
            ServiceUtils.startServiceSafe(LiftToUnlockService.createIntent());
        }
    }

    public static void stop() {
        Context appContext = ActionsApplication.getAppContext();
        if (LiftToUnlockHelper.isFeatureSupported() && RunningTasksUtils.isServiceRunning(appContext, LiftToUnlockService.class)) {
            LOGGER.mo11957d("stop : Lift To Unlock");
            appContext.stopService(LiftToUnlockService.createIntent());
        }
    }
}
