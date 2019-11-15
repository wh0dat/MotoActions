package com.motorola.actions.utils;

import android.content.pm.PackageManager.NameNotFoundException;
import android.support.annotation.NonNull;
import com.motorola.actions.ActionsApplication;

public class PackageManagerUtils {
    private static final MALogger LOGGER = new MALogger(PackageManagerUtils.class);

    public static boolean isAppEnabled(@NonNull String str) {
        boolean z = false;
        try {
            z = ActionsApplication.getAppContext().getPackageManager().getApplicationInfo(str, 0).enabled;
        } catch (NameNotFoundException e) {
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("Error getting ");
            sb.append(str);
            sb.append(", info: ");
            sb.append(e.getMessage());
            mALogger.mo11963w(sb.toString());
        }
        MALogger mALogger2 = LOGGER;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("isAppEnabled - ");
        sb2.append(str);
        sb2.append(" : ");
        sb2.append(z);
        mALogger2.mo11957d(sb2.toString());
        return z;
    }

    public static boolean isInstalled(@NonNull String str) {
        try {
            if (ActionsApplication.getAppContext().getPackageManager().getPackageInfo(str, 0) != null) {
                return true;
            }
            return false;
        } catch (NameNotFoundException e) {
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("Error getting ");
            sb.append(str);
            sb.append(", info: ");
            sb.append(e.getMessage());
            mALogger.mo11963w(sb.toString());
            return false;
        }
    }
}
