package com.motorola.actions.foc.gesture.service;

import android.content.Context;
import com.motorola.actions.foc.config.FlashOnChopInitService;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.RunningTasksUtils;
import com.motorola.actions.utils.ServiceUtils;

public final class FeatureManager {
    private static final MALogger LOGGER = new MALogger(FeatureManager.class);

    public static void start(Context context) {
        if (!RunningTasksUtils.isServiceRunning(context, FlashOnChopService.class)) {
            ServiceUtils.startServiceSafe(FlashOnChopService.createIntent(context));
        }
        if (!RunningTasksUtils.isServiceRunning(context, FlashOnChopInitService.class)) {
            ServiceUtils.startServiceSafe(FlashOnChopInitService.createIntent(context));
        }
    }

    public static void stop(Context context) {
        if (FlashOnChopService.isFeatureSupported(context) && RunningTasksUtils.isServiceRunning(context, FlashOnChopInitService.class)) {
            LOGGER.mo11957d("stop : FlashOnChopInitService");
            context.stopService(FlashOnChopInitService.createIntent(context));
        }
        if (FlashOnChopService.isFeatureSupported(context) && RunningTasksUtils.isServiceRunning(context, FlashOnChopService.class)) {
            LOGGER.mo11957d("stop : FlashOnChopService");
            context.stopService(FlashOnChopService.createIntent(context));
        }
    }
}
