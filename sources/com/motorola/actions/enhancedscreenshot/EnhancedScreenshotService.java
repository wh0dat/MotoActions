package com.motorola.actions.enhancedscreenshot;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.ServiceUtils;

public class EnhancedScreenshotService extends Service {
    private static final MALogger LOGGER = new MALogger(EnhancedScreenshotService.class);

    public IBinder onBind(Intent intent) {
        return null;
    }

    public static boolean isServiceEnabled() {
        if (isFeatureSupported()) {
            return EnhancedScreenshotHelper.isEnabled();
        }
        return false;
    }

    public static boolean isFeatureSupported() {
        return EnhancedScreenshotHelper.isFeatureSupported();
    }

    private static Intent createIntent() {
        return new Intent(ActionsApplication.getAppContext(), EnhancedScreenshotService.class);
    }

    public static void startService() {
        LOGGER.mo11957d("Starting service");
        ServiceUtils.startServiceSafe(createIntent());
    }

    public static void stopService() {
        LOGGER.mo11957d("Stopping service");
        ActionsApplication.getAppContext().stopService(createIntent());
    }
}
