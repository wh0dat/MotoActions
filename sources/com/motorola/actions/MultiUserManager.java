package com.motorola.actions;

import android.os.Build.VERSION;
import android.os.UserManager;
import com.motorola.actions.utils.MALogger;
import com.motorola.mod.ModDevice;

public class MultiUserManager {
    private static final MALogger LOGGER = new MALogger(MultiUserManager.class);

    public static boolean isSystemUser() {
        boolean z = false;
        try {
            UserManager userManager = (UserManager) ActionsApplication.getAppContext().getSystemService(ModDevice.USER);
            if (userManager != null && userManager.isSystemUser()) {
                z = true;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("isSystemUser: ");
        sb.append(z);
        mALogger.mo11957d(sb.toString());
        return z;
    }

    public static boolean isSupportedForCurrentUser() {
        boolean z = isSystemUser() || VERSION.SDK_INT != 27;
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("isSupportedForCurrentUser = ");
        sb.append(z);
        mALogger.mo11957d(sb.toString());
        return z;
    }
}
