package com.motorola.actions.mediacontrol;

import android.content.Context;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.RunningTasksUtils;

public final class FeatureManager {
    private static final MALogger LOGGER = new MALogger(FeatureManager.class);

    public static void start() {
        Context appContext = ActionsApplication.getAppContext();
        if (MediaControlService.isServiceEnabled() && !RunningTasksUtils.isServiceRunning(appContext, MediaControlService.class)) {
            LOGGER.mo11957d("Start : MediaControl service");
            MediaControlService.startService();
        }
    }

    public static void stop() {
        Context appContext = ActionsApplication.getAppContext();
        if (MediaControlService.isFeatureSupported() && RunningTasksUtils.isServiceRunning(appContext, MediaControlService.class)) {
            LOGGER.mo11957d("Stop : MediaControl service");
            MediaControlService.stopService();
        }
    }
}
