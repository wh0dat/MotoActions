package com.motorola.actions.utils;

import android.content.ComponentName;
import android.content.Context;
import com.motorola.actions.ActionsApplication;

public class StaticBroadcastUtils {
    private static final MALogger LOGGER = new MALogger(StaticBroadcastUtils.class);

    public static void enableStaticBroadcast(Class<?> cls) {
        Context appContext = ActionsApplication.getAppContext();
        ComponentName componentName = new ComponentName(appContext, cls);
        if (appContext.getPackageManager().getComponentEnabledSetting(componentName) == 2) {
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("Enabling ");
            sb.append(cls);
            sb.append(" broadcast");
            mALogger.mo11957d(sb.toString());
            appContext.getPackageManager().setComponentEnabledSetting(componentName, 1, 1);
        }
    }

    public static void disableStaticBroadcast(Class<?> cls) {
        Context appContext = ActionsApplication.getAppContext();
        ComponentName componentName = new ComponentName(appContext, cls);
        int componentEnabledSetting = appContext.getPackageManager().getComponentEnabledSetting(componentName);
        if (componentEnabledSetting == 1 || componentEnabledSetting == 0) {
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("Disabling ");
            sb.append(cls);
            sb.append(" broadcast");
            mALogger.mo11957d(sb.toString());
            appContext.getPackageManager().setComponentEnabledSetting(componentName, 2, 1);
        }
    }
}
