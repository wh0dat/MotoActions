package com.motorola.actions.attentivedisplay;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.provider.Settings;
import com.motorola.actions.foc.gesture.FlashOnChopGestureManager;
import com.motorola.actions.p013ui.settings.AttentiveDisplaySettingsFragment;
import com.motorola.actions.reflect.PowerManagerPrivateProxy;
import com.motorola.actions.settings.updater.AttentiveDisplaySettingsUpdater;
import com.motorola.actions.utils.Device;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.ServiceUtils;

public class AttentiveDisplayService extends Service {
    private static final MALogger LOGGER = new MALogger(AttentiveDisplayService.class);
    private AttentiveDisplay mAttentiveDisplay;

    public static void start(Context context) {
        LOGGER.mo11957d("start");
        ServiceUtils.startServiceSafe(new Intent(context, AttentiveDisplayService.class));
    }

    public static void stop(Context context) {
        LOGGER.mo11957d("stop");
        if (!context.stopService(new Intent(context, AttentiveDisplayService.class))) {
            LOGGER.mo11957d("Attentive display service not found");
        }
    }

    public static boolean isFeatureSupported(Context context) {
        if (Device.isAndroidOne()) {
            return false;
        }
        if (!context.getPackageManager().hasSystemFeature("android.hardware.camera.front")) {
            LOGGER.mo11957d("No front-facing camera found");
            return false;
        } else if (Device.isAndyDevice() || Device.isAndyNADevice() || Device.isGeorgeDevice() || Device.isVertexDevice() || Device.isHoudiniDevice() || Device.isGoldenEagleDevice()) {
            LOGGER.mo11957d("Device not supported.");
            return false;
        } else if (PowerManagerPrivateProxy.hasHooks()) {
            return true;
        } else {
            LOGGER.mo11957d("No framework hooks found");
            return false;
        }
    }

    public void onCreate() {
        LOGGER.mo11957d("onCreate()");
        super.onCreate();
    }

    public IBinder onBind(Intent intent) {
        LOGGER.mo11957d("onBind()");
        return null;
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        LOGGER.mo11957d("onStartCommand()");
        if (!isFeatureSupported(this)) {
            LOGGER.mo11957d("Attentive display not supported");
            stopSelf(i2);
        } else if (!AttentiveDisplaySettingsFragment.isStayOnEnabled()) {
            LOGGER.mo11957d("Attentive display is disabled");
            stopSelf(i2);
        } else if (intent == null) {
            LOGGER.mo11957d("Intent received was null");
            stopSelf(i2);
        } else if (FlashOnChopGestureManager.isFlashlightOn()) {
            LOGGER.mo11957d("When flashlight is ON, won't start Attentive display.");
            stopSelf(i2);
        } else if (!Settings.canDrawOverlays(getApplicationContext())) {
            LOGGER.mo11957d("When DrawOnOtherApps denied, won't start Attentive display.");
            AttentiveDisplaySettingsUpdater.getInstance().toggleStatus(false, false, "");
            stopSelf(i2);
        } else {
            if (this.mAttentiveDisplay == null) {
                this.mAttentiveDisplay = new AttentiveDisplay(this);
            }
            this.mAttentiveDisplay.onScreenPreDim();
        }
        return 2;
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, AttentiveDisplayService.class);
    }

    public void onDestroy() {
        LOGGER.mo11957d("onDestroy()");
        super.onDestroy();
        if (this.mAttentiveDisplay != null) {
            this.mAttentiveDisplay.destroy();
            this.mAttentiveDisplay = null;
        }
    }
}
