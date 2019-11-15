package com.motorola.actions.ltu;

import android.content.pm.PackageManager;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.reflect.MotorolaSettings;
import com.motorola.actions.reflect.SystemPropertiesProxy;
import com.motorola.actions.utils.Device;
import com.motorola.actions.utils.MALogger;

public class LiftToUnlockHelper {
    private static final String LIFT_TO_VIEW_SYS_PROP = "ro.vendor.sensors.mot_ltv";
    private static final MALogger LOGGER = new MALogger(LiftToUnlockHelper.class);
    private static final String SYSTEM_FEATURE_NAME = "com.motorola.faceunlock";

    public static boolean isFeatureSupported() {
        PackageManager packageManager = ActionsApplication.getAppContext().getPackageManager();
        boolean z = false;
        if (!isFaceUnlockDisabledByDevicePolicyManager() && !Device.isZuiDevice() && packageManager != null && SystemPropertiesProxy.getBoolean(LIFT_TO_VIEW_SYS_PROP, false)) {
            z = packageManager.hasSystemFeature(SYSTEM_FEATURE_NAME);
        }
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("isFeatureSupported=");
        sb.append(z);
        mALogger.mo11957d(sb.toString());
        return z;
    }

    public static boolean isEnabled() {
        return isEnrolled() && LiftToUnlockModel.isServiceEnabled();
    }

    public static void setServiceEnabled(boolean z) {
        LiftToUnlockModel.setServiceEnabled(z);
    }

    public static boolean isEnrolled() {
        return MotorolaSettings.isFaceEnrolled();
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x002e  */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x0030  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean isFaceUnlockDisabledByDevicePolicyManager() {
        /*
            android.content.Context r0 = com.motorola.actions.ActionsApplication.getAppContext()
            java.lang.String r1 = "device_policy"
            java.lang.Object r0 = r0.getSystemService(r1)
            android.app.admin.DevicePolicyManager r0 = (android.app.admin.DevicePolicyManager) r0
            r1 = 0
            r2 = 1
            if (r0 == 0) goto L_0x0032
            r3 = 0
            int r4 = r0.getPasswordQuality(r3)     // Catch:{ SecurityException -> 0x001d }
            r5 = 32768(0x8000, float:4.5918E-41)
            if (r4 <= r5) goto L_0x001b
            goto L_0x0025
        L_0x001b:
            r4 = r1
            goto L_0x0026
        L_0x001d:
            r4 = move-exception
            com.motorola.actions.utils.MALogger r5 = LOGGER
            java.lang.String r6 = "isFaceUnlockDisabledByDevicePolicyManager error:"
            r5.mo11960e(r6, r4)
        L_0x0025:
            r4 = r2
        L_0x0026:
            int r0 = r0.getKeyguardDisabledFeatures(r3)
            r0 = r0 & 16
            if (r0 == 0) goto L_0x0030
            r0 = r2
            goto L_0x0034
        L_0x0030:
            r0 = r1
            goto L_0x0034
        L_0x0032:
            r0 = r2
            r4 = r0
        L_0x0034:
            com.motorola.actions.utils.MALogger r3 = LOGGER
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r6 = "FaceUnlockDisabledByDevicePolicyManager:passwordQualityTooHigh="
            r5.append(r6)
            r5.append(r4)
            java.lang.String r5 = r5.toString()
            r3.mo11957d(r5)
            com.motorola.actions.utils.MALogger r3 = LOGGER
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r6 = "FaceUnlockDisabledByDevicePolicyManager:disableTrustAgents="
            r5.append(r6)
            r5.append(r0)
            java.lang.String r5 = r5.toString()
            r3.mo11957d(r5)
            if (r4 != 0) goto L_0x0064
            if (r0 == 0) goto L_0x0065
        L_0x0064:
            r1 = r2
        L_0x0065:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.motorola.actions.ltu.LiftToUnlockHelper.isFaceUnlockDisabledByDevicePolicyManager():boolean");
    }
}
