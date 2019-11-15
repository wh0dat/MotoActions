package com.motorola.actions.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import com.motorola.actions.ActionsApplication;

public class MotoDisplayUtils {
    private static final String ADSP_PERMISSION = "com.motorola.audiomonitor.permission.LOCAL";
    private static final MALogger LOGGER = new MALogger(MotoDisplayUtils.class);
    public static final String MOTO_DISPLAY_PACKAGE_NAME = "com.motorola.motodisplay";
    public static final String SECURE_DOZE_ENABLED_KEY = "doze_enabled";
    private static final int SECURE_DOZE_ENABLED_MOTO_DISPLAY_OFF = 0;
    private static final int SECURE_DOZE_ENABLED_MOTO_DISPLAY_ON = 2;

    public static boolean isMotoDisplayEnabled() {
        boolean z = false;
        int intValue = ((Integer) SettingsWrapper.getSecureInt(SECURE_DOZE_ENABLED_KEY).orElse(Integer.valueOf(0))).intValue();
        if (intValue == 2) {
            z = true;
        }
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("isMotoDisplayEnabled: dozeValue = ");
        sb.append(intValue);
        sb.append(", isMotoDisplayEnabled = ");
        sb.append(z);
        mALogger.mo11957d(sb.toString());
        return z;
    }

    public static boolean isApproachImplemented() {
        return hasADSPPermissionGranted();
    }

    private static boolean hasADSPPermissionGranted() {
        PackageInfo packageInfo = getPackageInfo(MOTO_DISPLAY_PACKAGE_NAME);
        boolean z = false;
        if (packageInfo != null) {
            int i = 0;
            while (true) {
                if (i >= packageInfo.requestedPermissions.length) {
                    break;
                } else if (ADSP_PERMISSION.equals(packageInfo.requestedPermissions[i])) {
                    if ((packageInfo.requestedPermissionsFlags[i] & 2) != 0) {
                        z = true;
                    }
                    MALogger mALogger = LOGGER;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Found desired permission: com.motorola.audiomonitor.permission.LOCAL, ");
                    sb.append(packageInfo.requestedPermissionsFlags[i]);
                    mALogger.mo11957d(sb.toString());
                } else {
                    i++;
                }
            }
        }
        MALogger mALogger2 = LOGGER;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("hasADSPPermissionGranted: ");
        sb2.append(z);
        mALogger2.mo11957d(sb2.toString());
        return z;
    }

    private static PackageInfo getPackageInfo(String str) {
        try {
            return ActionsApplication.getAppContext().getPackageManager().getPackageInfo(str, 4096);
        } catch (NameNotFoundException e) {
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("Error getting permissions: ");
            sb.append(e);
            mALogger.mo11959e(sb.toString());
            return null;
        }
    }
}
