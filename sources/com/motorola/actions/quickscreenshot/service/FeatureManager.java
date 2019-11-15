package com.motorola.actions.quickscreenshot.service;

import android.content.Context;
import com.motorola.actions.quickscreenshot.QuickScreenshotHelper;
import com.motorola.actions.quickscreenshot.QuickScreenshotModel;
import com.motorola.actions.quickscreenshot.ScreenshotReceiver;
import com.motorola.actions.quickscreenshot.instrumentation.QuickScreenshotInstrumentation;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.RunningTasksUtils;

public final class FeatureManager {
    private static final MALogger LOGGER = new MALogger(FeatureManager.class);
    private static ScreenshotReceiver sScreenshotReceiver;

    public static void start(Context context) {
        if (QuickScreenshotHelper.isFeatureSupported()) {
            QuickScreenshotModel.setInitialValue();
            if (QuickScreenshotModel.isServiceEnabled() && !RunningTasksUtils.isServiceRunning(context, QuickScreenshotService.class)) {
                QuickScreenshotService.startService();
            }
        }
        if (sScreenshotReceiver == null) {
            sScreenshotReceiver = new ScreenshotReceiver(QuickScreenshotInstrumentation.getInstrumentationCallback());
        }
        sScreenshotReceiver.register();
    }

    public static void stop(Context context) {
        if (QuickScreenshotHelper.isFeatureSupported() && RunningTasksUtils.isServiceRunning(context, QuickScreenshotService.class)) {
            LOGGER.mo11957d("stop : Quick Screenshot");
            QuickScreenshotService.stopService();
        }
        if (sScreenshotReceiver != null) {
            sScreenshotReceiver.unregister();
        }
        sScreenshotReceiver = null;
    }
}
