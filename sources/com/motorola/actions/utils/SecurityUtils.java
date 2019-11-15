package com.motorola.actions.utils;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.PowerManager;
import android.os.UserManager;
import com.motorola.actions.ActionsApplication;

public class SecurityUtils {
    private static final MALogger LOGGER = new MALogger(SecurityUtils.class);

    public static boolean isUserUnlocked() {
        Context appContext = ActionsApplication.getAppContext();
        return ((PowerManager) appContext.getSystemService("power")).isInteractive() && !((KeyguardManager) appContext.getSystemService("keyguard")).inKeyguardRestrictedInputMode();
    }

    public static boolean isUserInDirectBoot() {
        UserManager userManager = (UserManager) ActionsApplication.getAppContext().getSystemService(UserManager.class);
        boolean z = userManager != null && !userManager.isUserUnlocked();
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("isUserInDirectBoot = ");
        sb.append(z);
        mALogger.mo11957d(sb.toString());
        return z;
    }
}
