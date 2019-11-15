package com.motorola.actions.mediacontrol;

import android.app.Service;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.IBinder;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.utils.Device;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.ServiceUtils;

public class MediaControlService extends Service {
    private static final MALogger LOGGER = new MALogger(MediaControlService.class);
    private VolumeEventHandler mVolumeEventHandler;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public static boolean isServiceEnabled() {
        if (isFeatureSupported()) {
            return MediaControlModel.isServiceEnabled();
        }
        return false;
    }

    public static boolean isFeatureSupported() {
        return VERSION.SDK_INT > 26 && !Device.isIbisFiDevice() && !Device.isRobustaNoteDevice() && !Device.isRobustaSDevice() && !Device.isZuiDevice() && !Device.isRobusta2Device();
    }

    public static Intent createIntent() {
        return new Intent(ActionsApplication.getAppContext(), MediaControlService.class);
    }

    public void onCreate() {
        super.onCreate();
        LOGGER.mo11957d("onCreate");
        this.mVolumeEventHandler = new VolumeEventHandler();
        this.mVolumeEventHandler.start();
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        LOGGER.mo11957d("starting...");
        return 1;
    }

    public void onDestroy() {
        super.onDestroy();
        LOGGER.mo11957d("onDestroy");
        if (this.mVolumeEventHandler != null) {
            this.mVolumeEventHandler.stop();
        }
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
