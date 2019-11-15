package com.motorola.actions.lts;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.lts.instrumentation.LiftToSilenceInstrumentation;
import com.motorola.actions.lts.sensoraccess.LiftToSilenceSensorListener;
import com.motorola.actions.reflect.SensorPrivateProxy;
import com.motorola.actions.utils.Device;
import com.motorola.actions.utils.MALogger;

public class LiftToSilenceService extends Service {
    public static final String INTENT_ACTION_SEND_STATE_TUTORIAL = "com.motorola.actions.lts.SEND_STATE_TUTORIAL";
    public static final String KEY_ENABLED = "ACTIONS_LTS_ENABLED";
    /* access modifiers changed from: private */
    public static final MALogger LOGGER = new MALogger(LiftToSilenceService.class);
    public static final String TUTORIAL_EXTRAS = "tutorial_lts";
    private static final String VOLUME_DOWN_ACTION_DURING_CALL = "com.motorola.intent.action.VOLUME_DOWN_DURING_INC_CALL";
    /* access modifiers changed from: private */
    public LiftToSilenceActivation mActivation;
    /* access modifiers changed from: private */
    public boolean mIsOnCall;
    private boolean mIsOnTutorialMode;
    private BroadcastReceiver mScreenOffBroadcastReceiver;
    /* access modifiers changed from: private */
    public LiftToSilenceSensorListener mSensorListeners;
    private SensorManager mSensorManager;
    private TelephonyListener mTelListener;
    private TelephonyManager mTelephonyManager;
    private BroadcastReceiver mVolumeDownBroadcastReceiver;

    private class ScreenOffBroadcastReceiver extends BroadcastReceiver {
        private ScreenOffBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent != null && "android.intent.action.SCREEN_OFF".equals(intent.getAction())) {
                LiftToSilenceInstrumentation.powerButtonPressed();
                if (!SensorPrivateProxy.isProxySensorAvailable(SensorPrivateProxy.TYPE_MOTO_LIFT_TO_SILENCE_GESTURE)) {
                    LiftToSilenceService.this.unRegisterSensorListener();
                }
                LiftToSilenceService.this.unregisterReceivers();
            }
        }
    }

    private class TelephonyListener extends PhoneStateListener {
        private TelephonyListener() {
        }

        public void onCallStateChanged(int i, String str) {
            super.onCallStateChanged(i, str);
            boolean z = SharedPreferenceManager.getBoolean(TTMActivatedStateChangeReceiver.TTM_ACTIVATED_STATE, false);
            MALogger access$500 = LiftToSilenceService.LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("onCallStateChanged, callState=");
            sb.append(i);
            sb.append(" ttmState=");
            sb.append(z);
            access$500.mo11957d(sb.toString());
            if (i != 1 || z) {
                if (!SensorPrivateProxy.isProxySensorAvailable(SensorPrivateProxy.TYPE_MOTO_LIFT_TO_SILENCE_GESTURE)) {
                    LiftToSilenceService.this.unRegisterSensorListener();
                }
                LiftToSilenceService.this.mActivation.finish();
                LiftToSilenceService.this.unregisterReceivers();
                LiftToSilenceInstrumentation.setRingingEnded();
                if (i == 0) {
                    LiftToSilenceService.this.mIsOnCall = false;
                } else if (i == 2) {
                    LiftToSilenceService.this.mIsOnCall = true;
                }
            } else {
                LiftToSilenceInstrumentation.triggerStartTimeCounter();
                LiftToSilenceService.this.registerReceivers();
                LiftToSilenceInstrumentation.setScreenStateBeforeCall();
                if (LiftToSilenceService.this.mSensorListeners == null && !LiftToSilenceService.this.mIsOnCall) {
                    LiftToSilenceService.this.registerSensorListener();
                }
            }
        }
    }

    private class VolumeDownInCallBroadcastReceiver extends BroadcastReceiver {
        private VolumeDownInCallBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent != null && LiftToSilenceService.VOLUME_DOWN_ACTION_DURING_CALL.equals(intent.getAction())) {
                LiftToSilenceService.LOGGER.mo11957d("Volume down received");
                LiftToSilenceService.this.mActivation.stopVibration();
            }
        }
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, LiftToSilenceService.class);
    }

    public void onCreate() {
        super.onCreate();
        LOGGER.mo11957d("Creating LTS service");
        this.mTelephonyManager = (TelephonyManager) getSystemService("phone");
        this.mActivation = new LiftToSilenceActivation(getApplicationContext());
        this.mTelListener = new TelephonyListener();
        if (SensorPrivateProxy.isProxySensorAvailable(SensorPrivateProxy.TYPE_MOTO_LIFT_TO_SILENCE_GESTURE)) {
            registerSensorListener();
        }
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        LOGGER.mo11957d("Starting LTS service");
        this.mIsOnCall = this.mTelephonyManager.getCallState() == 2;
        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras == null || !extras.getBoolean(TUTORIAL_EXTRAS)) {
                this.mIsOnTutorialMode = false;
            } else {
                LOGGER.mo11957d("Starting tutorial");
                this.mIsOnTutorialMode = true;
                unRegisterSensorListener();
                registerSensorListener();
            }
        }
        if (!this.mIsOnTutorialMode) {
            this.mTelephonyManager.listen(this.mTelListener, 32);
        }
        return 1;
    }

    public void onDestroy() {
        super.onDestroy();
        LOGGER.mo11957d("Destroying LTS service");
        unRegisterSensorListener();
        this.mActivation.stopVibration();
        this.mTelephonyManager.listen(this.mTelListener, 0);
        unregisterReceivers();
    }

    public static boolean isFeatureSupported() {
        if (Device.isJudoDevice() || Device.isAndyDevice() || Device.isGeorgeDevice() || Device.isRobustaSDevice() || Device.isRobustaNoteDevice() || Device.isRobusta2Device()) {
            return false;
        }
        SensorManager createSensorManagerInstance = createSensorManagerInstance();
        if (createSensorManagerInstance.getDefaultSensor(SensorPrivateProxy.TYPE_FLAT_UP) == null || createSensorManagerInstance.getDefaultSensor(SensorPrivateProxy.TYPE_FLAT_DOWN) == null) {
            return false;
        }
        return true;
    }

    public static boolean isLiftToSilenceEnabled() {
        return isFeatureSupported() && SharedPreferenceManager.getBoolean(KEY_ENABLED, FeatureKey.PICKUP_TO_STOP_RINGING.getEnableDefaultState());
    }

    private SensorManager getSensorManager() {
        if (this.mSensorManager == null) {
            this.mSensorManager = createSensorManagerInstance();
        }
        return this.mSensorManager;
    }

    private static SensorManager createSensorManagerInstance() {
        return (SensorManager) ActionsApplication.getAppContext().getSystemService("sensor");
    }

    /* access modifiers changed from: private */
    public void registerSensorListener() {
        if (this.mSensorListeners == null) {
            LOGGER.mo11957d("Registering listener");
            this.mSensorListeners = new LiftToSilenceSensorListener(this.mActivation);
            this.mSensorListeners.registerSensorListeners(getSensorManager(), this.mIsOnTutorialMode);
        }
    }

    /* access modifiers changed from: private */
    public void unRegisterSensorListener() {
        if (this.mSensorListeners != null) {
            LOGGER.mo11957d("Unregistering listener");
            this.mSensorListeners.unregisterSensorListeners(getSensorManager());
            this.mSensorListeners = null;
        }
    }

    /* access modifiers changed from: private */
    public void registerReceivers() {
        this.mScreenOffBroadcastReceiver = new ScreenOffBroadcastReceiver();
        registerReceiver(this.mScreenOffBroadcastReceiver, new IntentFilter("android.intent.action.SCREEN_OFF"));
        this.mVolumeDownBroadcastReceiver = new VolumeDownInCallBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(VOLUME_DOWN_ACTION_DURING_CALL);
        registerReceiver(this.mVolumeDownBroadcastReceiver, intentFilter);
    }

    /* access modifiers changed from: private */
    public void unregisterReceivers() {
        LOGGER.mo11957d("unregisterReceivers");
        if (this.mScreenOffBroadcastReceiver != null) {
            unregisterReceiver(this.mScreenOffBroadcastReceiver);
            this.mScreenOffBroadcastReceiver = null;
        }
        if (this.mVolumeDownBroadcastReceiver != null) {
            unregisterReceiver(this.mVolumeDownBroadcastReceiver);
            this.mVolumeDownBroadcastReceiver = null;
        }
    }
}
