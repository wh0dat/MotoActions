package com.motorola.actions.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.support.annotation.NonNull;
import com.motorola.actions.ActionsApplication;

public class MotoAppUtils {
    private static final MALogger LOGGER = new MALogger(MotoAppUtils.class);
    public static final String MOTO_PACKAGE_NAME = "com.motorola.moto";

    public static boolean isMotoEnabled() {
        boolean z = false;
        try {
            z = ActionsApplication.getAppContext().getPackageManager().getApplicationInfo(MOTO_PACKAGE_NAME, 0).enabled;
        } catch (NameNotFoundException e) {
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("Error getting app info: ");
            sb.append(e.getMessage());
            mALogger.mo11957d(sb.toString());
        }
        MALogger mALogger2 = LOGGER;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("isMotoEnabled: ");
        sb2.append(z);
        mALogger2.mo11957d(sb2.toString());
        return z;
    }

    public static void launchMoto(@NonNull Activity activity) {
        try {
            Intent launchIntentForPackage = activity.getPackageManager().getLaunchIntentForPackage(MOTO_PACKAGE_NAME);
            if (launchIntentForPackage != null) {
                activity.startActivity(launchIntentForPackage);
                LOGGER.mo11957d("launchMoto: launching Moto App");
                return;
            }
            LOGGER.mo11957d("launchMoto: intent is null, ignoring it...");
        } catch (ActivityNotFoundException e) {
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("Unable to start Moto: ");
            sb.append(e.getMessage());
            mALogger.mo11959e(sb.toString());
        }
    }
}
