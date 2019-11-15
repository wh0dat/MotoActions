package com.motorola.actions.nightdisplay;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.nightdisplay.p008pd.PDNightDisplayController;
import com.motorola.actions.utils.AndroidResourceAccess;
import com.motorola.actions.utils.MALogger;
import java.util.Optional;

public class NightDisplayService extends Service {
    public static final String KEY_ENABLED = "actions_night_shade_enabled";
    private static final MALogger LOGGER = new MALogger(NightDisplayService.class);
    private static final String PLATFORM_NIGHT_DISPLAY_AVAILABLE_O = "config_nightDisplayAvailableMoto";
    private PDNightDisplayController mPDNightDisplayController;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public static boolean isServiceEnabled() {
        if (isFeatureSupported()) {
            return SharedPreferenceManager.getBoolean(KEY_ENABLED, FeatureKey.NIGHT_DISPLAY.getEnableDefaultState());
        }
        return false;
    }

    public static boolean isFeatureSupported() {
        return isFrameworkEnabled();
    }

    private static boolean isFrameworkEnabled() {
        Optional boolResource = AndroidResourceAccess.getBoolResource(PLATFORM_NIGHT_DISPLAY_AVAILABLE_O);
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("config_nightDisplayAvailableMoto: ");
        sb.append(boolResource);
        mALogger.mo11957d(sb.toString());
        return ((Boolean) boolResource.orElse(Boolean.valueOf(false))).booleanValue();
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, NightDisplayService.class);
    }

    public void onCreate() {
        super.onCreate();
        this.mPDNightDisplayController = new PDNightDisplayController();
        LOGGER.mo11957d("onCreate");
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        LOGGER.mo11957d("starting...");
        this.mPDNightDisplayController.start(intent != null ? intent.getExtras() : null);
        return 1;
    }

    public void onDestroy() {
        super.onDestroy();
        LOGGER.mo11957d("onDestroy");
        this.mPDNightDisplayController.destroy();
    }
}
