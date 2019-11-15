package com.motorola.actions.nightdisplay;

import android.content.Context;
import android.os.Handler;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.RunningTasksUtils;
import com.motorola.actions.utils.ServiceUtils;
import com.motorola.actions.utils.SetupObserver;

public final class FeatureManager {
    private static final MALogger LOGGER = new MALogger(FeatureManager.class);

    public static void startDelayed(Context context, long j) {
        new Handler().postDelayed(new FeatureManager$$Lambda$0(context), j);
    }

    public static void start(Context context) {
        LOGGER.mo11957d("Night Display FeatureManager");
        if (NightDisplayService.isFeatureSupported()) {
            NDSecureSettingsUpdater.fixNightDisplayActivation();
            if (!SetupObserver.isSetupFinished()) {
                ServiceUtils.startServiceSafe(NightDisplaySetupService.createIntent(context));
            }
        }
        if (NightDisplayService.isServiceEnabled() && !RunningTasksUtils.isServiceRunning(context, NightDisplayService.class)) {
            ServiceUtils.startServiceSafe(NightDisplayService.createIntent(context));
        }
    }

    public static void stop(Context context) {
        if (NightDisplayService.isFeatureSupported() && RunningTasksUtils.isServiceRunning(context, NightDisplayService.class)) {
            LOGGER.mo11957d("stop : Night Display");
            context.stopService(NightDisplayService.createIntent(context));
        }
    }
}
