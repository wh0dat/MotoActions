package com.motorola.actions.approach.p005ir;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.IBinder;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.approach.GlanceConfiguration;
import com.motorola.actions.approach.p005ir.tuning.Tuning;
import com.motorola.actions.reflect.SensorPrivateProxy;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.SensorhubServiceManager;
import com.motorola.actions.utils.SensorhubServiceManager.SensorhubUpdateListener;
import java.util.HashMap;
import java.util.Map;

/* renamed from: com.motorola.actions.approach.ir.IRService */
public class IRService extends Service implements OnSharedPreferenceChangeListener {
    private static final String INTENT_FOLIO_CHANGED = "com.motorola.intent.ACTION_LID_CHANGED";
    public static final String IR_AOD_APPROACH_ENABLED = "ir_aod_approach_enabled";
    public static final String IR_SWIPE_ENABLED = "ir_swipe_enabled";
    /* access modifiers changed from: private */
    public static final MALogger LOGGER = new MALogger(IRService.class);
    public static final int SENSOR_DELAY_IR_SWIPE = 10000;
    /* access modifiers changed from: private */
    public Map<String, IRFeature> mFeatures = new HashMap();
    private final BroadcastReceiver mFolioChangedReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (IRService.INTENT_FOLIO_CHANGED.equals(intent.getAction())) {
                boolean booleanExtra = intent.getBooleanExtra("state", true);
                for (IRFeature folioStateChanged : IRService.this.mFeatures.values()) {
                    folioStateChanged.folioStateChanged(booleanExtra);
                }
            }
        }
    };
    private SensorhubServiceManager mSensorHubManager;
    SensorhubUpdateListener mSensorhubUpdateListener = new SensorhubUpdateListener() {
        public void onSensorhubReset() {
            IRService.LOGGER.mo11961i("onSensorhubReset");
            IRService.this.configureFeatures();
        }

        public void onSensorhubConnected() {
            IRService.LOGGER.mo11961i("onSensorhubConnected");
            IRService.this.configureFeatures();
        }
    };
    private Tuning mTuning;

    /* renamed from: com.motorola.actions.approach.ir.IRService$Gestures */
    public enum Gestures {
        GESTURE_NONE,
        GESTURE_OBJECT_DETECTED,
        GESTURE_OBJECT_NOT_DETECTED,
        GESTURE_SWIPE,
        GESTURE_APPROACH,
        GESTURE_COVER,
        GESTURE_DEPART,
        GESTURE_HOVER,
        GESTURE_HOVER_PULSE,
        GESTURE_PROXIMITY_NONE,
        GESTURE_HOVER_FIST;

        public boolean equals(float f) {
            return ((float) ordinal()) == f;
        }
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, IRService.class);
    }

    public static boolean isIRSupported() {
        boolean isProxySensorAvailable = SensorPrivateProxy.isProxySensorAvailable(SensorPrivateProxy.TYPE_IR_GESTURE);
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("isIRSupported = ");
        sb.append(isProxySensorAvailable);
        mALogger.mo11957d(sb.toString());
        return isProxySensorAvailable;
    }

    private void addFeature(IRFeature iRFeature) {
        String name = iRFeature.getName();
        if (this.mFeatures.containsKey(name)) {
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("Tried to register duplicate feature ");
            sb.append(name);
            mALogger.mo11959e(sb.toString());
            return;
        }
        this.mFeatures.put(name, iRFeature);
        iRFeature.start();
    }

    public void onCreate() {
        super.onCreate();
        LOGGER.mo11957d("onCreate");
        if (isIRSupported()) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(INTENT_FOLIO_CHANGED);
            registerReceiver(this.mFolioChangedReceiver, intentFilter);
            SharedPreferenceManager.registerOnSharedPreferenceChangeListener(this);
            configureIR();
            this.mTuning = new Tuning();
            this.mTuning.configure();
            configureFeatures();
        }
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        if (!isIRSupported()) {
            stopSelf();
            return 2;
        }
        if (intent != null && this.mSensorHubManager == null) {
            this.mSensorHubManager = new SensorhubServiceManager(this, this.mSensorhubUpdateListener);
        }
        return 1;
    }

    public void onDestroy() {
        super.onDestroy();
        LOGGER.mo11957d("onDestroy");
        if (isIRSupported()) {
            if (this.mSensorHubManager != null) {
                this.mSensorHubManager.destroy();
            }
            for (IRFeature stop : this.mFeatures.values()) {
                stop.stop();
            }
            unregisterReceiver(this.mFolioChangedReceiver);
            SharedPreferenceManager.unregisterOnSharedPreferenceChangeListener(this);
        }
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String str) {
        configureFeatures();
    }

    public void configureFeatures() {
        for (IRFeature iRFeature : this.mFeatures.values()) {
            if (iRFeature.canEnable() && !iRFeature.isEnabled()) {
                iRFeature.enable();
            } else if (!iRFeature.canEnable() && iRFeature.isEnabled()) {
                iRFeature.disable();
            }
        }
        if (SharedPreferenceManager.getBoolean("ir_aod_approach_enabled", FeatureKey.APPROACH.getEnableDefaultState())) {
            GlanceConfiguration.enableIr(this.mSensorHubManager);
        } else {
            GlanceConfiguration.enableNudge(this.mSensorHubManager);
        }
    }

    public void callReceived() {
        LOGGER.mo11957d("callReceived.");
    }

    public void alarmDismissed() {
        LOGGER.mo11957d("alarmDismissed");
    }

    private void configureIR() {
        SensorHub.setIRDisabled(false);
    }

    public static boolean isServiceEnabled() {
        if (isIRSupported()) {
            return SharedPreferenceManager.getBoolean("ir_aod_approach_enabled", FeatureKey.APPROACH.getEnableDefaultState());
        }
        return false;
    }
}
