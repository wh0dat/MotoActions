package com.motorola.actions.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import com.motorola.actions.ActionsApplication;

public class ServiceUtils {
    private static final MALogger LOGGER = new MALogger(ServiceUtils.class);

    private static void startServiceAsSafe(Intent intent, boolean z) {
        ComponentName componentName;
        Context appContext = ActionsApplication.getAppContext();
        if (z) {
            try {
                componentName = appContext.startForegroundService(intent);
            } catch (IllegalStateException | SecurityException e) {
                MALogger mALogger = LOGGER;
                StringBuilder sb = new StringBuilder();
                sb.append("Error starting service Intent='");
                sb.append(intent);
                sb.append("': ");
                sb.append(e);
                mALogger.mo11959e(sb.toString());
                e.printStackTrace();
                return;
            }
        } else {
            componentName = appContext.startService(intent);
        }
        if (componentName == null) {
            MALogger mALogger2 = LOGGER;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Could not start service:'");
            sb2.append(intent);
            sb2.append("'");
            mALogger2.mo11959e(sb2.toString());
        }
    }

    public static void startServiceSafe(Intent intent) {
        startServiceAsSafe(intent, false);
    }

    public static void startForegroundServiceSafe(Intent intent) {
        startServiceAsSafe(intent, true);
    }
}
