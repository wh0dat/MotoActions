package com.motorola.actions.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.KeyguardManager;
import android.content.Context;
import android.text.TextUtils;
import com.motorola.actions.ActionsApplication;
import java.util.List;

public class RunningTasksUtils {
    private static final MALogger LOGGER = new MALogger(RunningTasksUtils.class);

    public static boolean isForegroundAppPackageName(Context context, String str) {
        if (!TextUtils.isEmpty(str) && context != null) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
            if (activityManager != null) {
                List<RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
                if (runningAppProcesses != null) {
                    for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
                        if (runningAppProcessInfo.importance == 100 && runningAppProcessInfo.processName.equals(str)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static boolean isServiceRunning(Context context, Class<?> cls) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        if (activityManager != null) {
            for (RunningServiceInfo runningServiceInfo : activityManager.getRunningServices(Integer.MAX_VALUE)) {
                if (cls.getName().equals(runningServiceInfo.service.getClassName())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isTopTaskPackageName(String str) {
        if (!TextUtils.isEmpty(str)) {
            return TextUtils.equals(str, getTopTaskPackageName());
        }
        return false;
    }

    public static String getTopTaskPackageName() {
        RunningTaskInfo topTaskInfo = getTopTaskInfo();
        if (topTaskInfo == null || topTaskInfo.topActivity == null || !isScreenUnlocked()) {
            return null;
        }
        return topTaskInfo.topActivity.getPackageName();
    }

    public static boolean isPermissionDialogVisible() {
        RunningTaskInfo topTaskInfo = getTopTaskInfo();
        boolean equals = (topTaskInfo == null || topTaskInfo.topActivity == null) ? false : "com.android.packageinstaller.permission.ui.GrantPermissionsActivity".equals(topTaskInfo.topActivity.getClassName());
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("isPermissionDialogVisible: ");
        sb.append(equals);
        mALogger.mo11957d(sb.toString());
        return equals;
    }

    private static RunningTaskInfo getTopTaskInfo() {
        ActivityManager activityManager = (ActivityManager) ActionsApplication.getAppContext().getSystemService("activity");
        if (activityManager != null) {
            List runningTasks = activityManager.getRunningTasks(1);
            if (runningTasks != null && runningTasks.size() > 0) {
                return (RunningTaskInfo) runningTasks.get(0);
            }
        }
        return null;
    }

    private static boolean isScreenUnlocked() {
        KeyguardManager keyguardManager = (KeyguardManager) ActionsApplication.getAppContext().getSystemService("keyguard");
        if (keyguardManager != null) {
            return !keyguardManager.inKeyguardRestrictedInputMode();
        }
        return false;
    }
}
