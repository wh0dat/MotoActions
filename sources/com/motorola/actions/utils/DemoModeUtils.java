package com.motorola.actions.utils;

import android.app.admin.DevicePolicyManager;
import com.motorola.actions.ActionsApplication;

public class DemoModeUtils {
    public static final String DEMOMODE_PACKAGE_NAME = "com.motorola.demo";
    private static final MALogger LOGGER = new MALogger(DemoModeUtils.class);

    public static boolean isDemoModeEnabled() {
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) ActionsApplication.getAppContext().getSystemService(DevicePolicyManager.class);
        boolean isDeviceOwnerApp = devicePolicyManager.isDeviceOwnerApp(DEMOMODE_PACKAGE_NAME);
        boolean isProfileOwnerApp = devicePolicyManager.isProfileOwnerApp(DEMOMODE_PACKAGE_NAME);
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("isDemoModeEnabled: isDemoModeDeviceOwnerApp = ");
        sb.append(isDeviceOwnerApp);
        sb.append(", isDemoModeProfileOwnerApp = ");
        sb.append(isProfileOwnerApp);
        mALogger.mo11957d(sb.toString());
        return isDeviceOwnerApp || isProfileOwnerApp;
    }
}
