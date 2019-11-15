package com.motorola.actions.quickscreenshot.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.ServiceUtils;

public class QuickScreenshotService extends Service {
    private static final MALogger LOGGER = new MALogger(QuickScreenshotService.class);
    private EventController mEventController;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        LOGGER.mo11957d("onStartCommand.");
        return 1;
    }

    public void onCreate() {
        LOGGER.mo11957d("onCreate()");
        super.onCreate();
        startEventHandler();
    }

    private void startEventHandler() {
        if (this.mEventController == null) {
            this.mEventController = new EventController();
        }
        this.mEventController.start();
    }

    public void onDestroy() {
        LOGGER.mo11957d("onDestroy()");
        if (this.mEventController != null) {
            this.mEventController.stop();
        }
        super.onDestroy();
    }

    private static Intent createIntent() {
        return new Intent(ActionsApplication.getAppContext(), QuickScreenshotService.class);
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
