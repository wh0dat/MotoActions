package com.motorola.actions.enhancedscreenshot;

import android.content.Context;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.RunningTasksUtils;
import com.motorola.actions.utils.SetupObserver;

public final class FeatureManager {
    private static final MALogger LOGGER = new MALogger(FeatureManager.class);
    private static SetupObserver sSetupObserver;

    public static void start() {
        Context appContext = ActionsApplication.getAppContext();
        if (EnhancedScreenshotService.isServiceEnabled() && !RunningTasksUtils.isServiceRunning(appContext, EnhancedScreenshotService.class)) {
            LOGGER.mo11957d("Start : EnhancedScreenshotService");
            EnhancedScreenshotService.startService();
        }
        if (!SetupObserver.isSetupFinished() && EnhancedScreenshotService.isFeatureSupported()) {
            LOGGER.mo11957d("Registering SetupObserver");
            sSetupObserver = new SetupObserver();
            sSetupObserver.observe(FeatureManager$$Lambda$0.$instance);
        }
    }

    public static void stop() {
        Context appContext = ActionsApplication.getAppContext();
        if (EnhancedScreenshotService.isFeatureSupported() && RunningTasksUtils.isServiceRunning(appContext, EnhancedScreenshotService.class)) {
            LOGGER.mo11957d("Stop : EnhancedScreenshotService");
            EnhancedScreenshotService.stopService();
        }
    }
}
