package com.motorola.actions.p010qc;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.discovery.DiscoveryManager;
import com.motorola.actions.p010qc.instrumentation.QuickCaptureInstrumentation;
import com.motorola.actions.reflect.SensorPrivateProxy;
import com.motorola.actions.settings.provider.ActionsSettingsProvider;
import com.motorola.actions.settings.provider.p012v2.actions.ContainerProviderItemQC;
import com.motorola.actions.utils.DFSquadUtils;
import com.motorola.actions.utils.EventOnlySensorListener;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.MotoResearchUtils;
import com.motorola.actions.utils.MotoSurveyUtils;
import com.motorola.actions.utils.SettingsWrapper;
import java.util.Optional;

/* renamed from: com.motorola.actions.qc.QuickCaptureService */
public class QuickCaptureService extends Service {
    public static final String KEY_ENABLED = "ACTIONS_QC_ENABLED";
    /* access modifiers changed from: private */
    public static final MALogger LOGGER = new MALogger(QuickCaptureService.class);
    /* access modifiers changed from: private */
    public boolean mLastEnabledStatus;
    private QuickCaptureObserver mQuickCaptureObserver;
    private QuickCaptureListener mSensorListener;

    /* renamed from: com.motorola.actions.qc.QuickCaptureService$QuickCaptureListener */
    private static class QuickCaptureListener extends EventOnlySensorListener {
        private QuickCaptureListener() {
        }

        public void onSensorChanged(SensorEvent sensorEvent) {
            QuickCaptureService.LOGGER.mo11957d("QuickCaptureListener: Quick capture gesture detected");
            if (sensorEvent.sensor.getType() == SensorPrivateProxy.TYPE_CAMERA_ACTIVATE) {
                MotoSurveyUtils.sendIntent(MotoResearchUtils.GESTURE_TYPE_QUICK_CAPTURE);
                DFSquadUtils.sendIntent(MotoResearchUtils.GESTURE_TYPE_QUICK_CAPTURE);
            }
            DiscoveryManager.getInstance().setDiscoveryStatus(FeatureKey.QUICK_CAPTURE, 0);
            DiscoveryManager.getInstance().cancelFDN(FeatureKey.QUICK_CAPTURE);
            QuickCaptureInstrumentation.recordQuickCaptureEvent();
        }
    }

    /* renamed from: com.motorola.actions.qc.QuickCaptureService$QuickCaptureObserver */
    private class QuickCaptureObserver extends ContentObserver {
        private boolean mRegistered;

        QuickCaptureObserver(Handler handler) {
            super(handler);
        }

        public void onChange(boolean z, Uri uri) {
            MALogger access$100 = QuickCaptureService.LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("QuickCaptureObserver:onChange() ");
            sb.append(uri.toString());
            access$100.mo11957d(sb.toString());
            boolean isEnabled = QuickDrawHelper.isEnabled();
            if (isEnabled) {
                QuickCaptureService.this.registerSensorListener();
            } else {
                QuickCaptureService.this.unregisterSensorListener();
            }
            ActionsSettingsProvider.hideCard(ContainerProviderItemQC.PRIORITY_KEY);
            ActionsSettingsProvider.notifyChange(ContainerProviderItemQC.TABLE_NAME);
            if (QuickCaptureService.this.mLastEnabledStatus != isEnabled) {
                QuickCaptureInstrumentation.recordDailyToggleEvent(isEnabled);
                QuickCaptureService.this.mLastEnabledStatus = isEnabled;
            }
        }

        public void register(Context context) {
            if (!this.mRegistered) {
                Optional secureUriFor = SettingsWrapper.getSecureUriFor("camera_gesture_disabled");
                secureUriFor.ifPresent(new QuickCaptureService$QuickCaptureObserver$$Lambda$0(this, context));
                MALogger access$100 = QuickCaptureService.LOGGER;
                StringBuilder sb = new StringBuilder();
                sb.append("QuickCaptureObserver:register() - uri:");
                sb.append(secureUriFor.orElse(Uri.EMPTY));
                access$100.mo11957d(sb.toString());
                this.mRegistered = true;
            }
        }

        /* access modifiers changed from: 0000 */
        public final /* synthetic */ void lambda$register$0$QuickCaptureService$QuickCaptureObserver(Context context, Uri uri) {
            context.getContentResolver().registerContentObserver(uri, true, this);
        }

        public void unregister(Context context) {
            if (this.mRegistered) {
                context.getContentResolver().unregisterContentObserver(this);
                this.mRegistered = false;
            }
        }
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, QuickCaptureService.class);
    }

    public void onCreate() {
        this.mQuickCaptureObserver = new QuickCaptureObserver(null);
        this.mLastEnabledStatus = QuickDrawHelper.isEnabled();
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        super.onStartCommand(intent, i, i2);
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("Starting QC service: ");
        sb.append(intent);
        mALogger.mo11957d(sb.toString());
        if (QuickDrawHelper.isEnabled()) {
            registerSensorListener();
        }
        this.mQuickCaptureObserver.register(this);
        return 1;
    }

    public void onDestroy() {
        super.onDestroy();
        LOGGER.mo11957d("Stopping QC Instrumentation service");
        unregisterSensorListener();
        this.mQuickCaptureObserver.unregister(this);
        this.mQuickCaptureObserver = null;
    }

    private static SensorManager getSensorManager(Context context) {
        return (SensorManager) context.getSystemService("sensor");
    }

    /* access modifiers changed from: private */
    public void registerSensorListener() {
        if (this.mSensorListener == null) {
            LOGGER.mo11957d("Registering listener");
            Sensor sensor = null;
            this.mSensorListener = new QuickCaptureListener();
            SensorManager sensorManager = getSensorManager(this);
            if (sensorManager != null) {
                sensor = sensorManager.getDefaultSensor(SensorPrivateProxy.TYPE_CAMERA_ACTIVATE);
            }
            if (sensor == null) {
                LOGGER.mo11963w("Could not find quick capture sensor");
            } else if (!sensorManager.registerListener(this.mSensorListener, sensor, 2)) {
                LOGGER.mo11959e("Error registering quickcapute sensor listener");
            }
        }
    }

    /* access modifiers changed from: private */
    public void unregisterSensorListener() {
        if (this.mSensorListener != null) {
            LOGGER.mo11957d("Unregistering listener");
            SensorManager sensorManager = getSensorManager(this);
            if (sensorManager != null) {
                sensorManager.unregisterListener(this.mSensorListener);
            }
            this.mSensorListener = null;
        }
    }
}
