package com.motorola.actions.sleepPattern;

import android.content.Context;
import com.motorola.actions.sleepPattern.preprocessing.Utils;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.RunningTasksUtils;
import com.motorola.actions.utils.ServiceUtils;
import java.util.Calendar;

public final class FeatureManager {
    private static final MALogger LOGGER = new MALogger(FeatureManager.class);

    public static void start(Context context) {
        LOGGER.mo11957d("SleepPattern FeatureManager - start");
        if (SleepPatternService.isFeatureSupported() && !RunningTasksUtils.isServiceRunning(context, SleepPatternService.class)) {
            if (Utils.isValidAlarmInterval(Calendar.getInstance())) {
                ServiceUtils.startServiceSafe(SleepPatternService.createIntent(context));
            } else {
                SleepPatternService.scheduleSleepPatternServiceStart(context);
            }
            TimeChangedReceiver.getInstance().register();
        }
    }

    public static void stop(Context context) {
        if (SleepPatternService.isFeatureSupported() && RunningTasksUtils.isServiceRunning(context, SleepPatternService.class)) {
            context.stopService(SleepPatternService.createIntent(context));
            TimeChangedReceiver.getInstance().unregister();
        }
    }
}
