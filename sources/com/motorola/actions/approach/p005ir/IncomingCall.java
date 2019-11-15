package com.motorola.actions.approach.p005ir;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import com.motorola.actions.reflect.TelephonyIntentsProxy;
import com.motorola.actions.reflect.TelephonyManagerPrivateProxy;
import com.motorola.actions.utils.MotoResearchUtils;
import com.motorola.actions.utils.MotoSurveyUtils;

/* renamed from: com.motorola.actions.approach.ir.IncomingCall */
public class IncomingCall extends IRFeature {
    public static final String FEATURE_NAME = "incomingcall";
    private static final String GESTURE_TYPE_WAVE_TO_DISMISS = "wave_dismiss_call";
    private final BroadcastReceiver mCallEndBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (IncomingCall.this.mCallState == 1) {
                IncomingCall.this.ringingEnded();
            }
        }
    };
    /* access modifiers changed from: private */
    public int mCallState;
    private final SensorEventListener mGestureListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int i) {
        }

        public void onSensorChanged(SensorEvent sensorEvent) {
            if (IRFeature.isValidSwipe(sensorEvent) && !IncomingCall.this.mSilencedBySwipe) {
                MotoSurveyUtils.sendIntent(MotoResearchUtils.GESTURE_TYPE_WAVE_TO_DISMISS);
                IncomingCall.this.stopRinging();
            }
        }
    };
    private final PhoneStateListener mPhoneStateListener = new PhoneStateListener() {
        public void onCallStateChanged(int i, String str) {
            switch (i) {
                case 1:
                    IncomingCall.this.resetState();
                    IncomingCall.this.mCallState = i;
                    IncomingCall.this.registerListeners();
                    IncomingCall.this.mIRService.callReceived();
                    return;
                case 2:
                    if (IncomingCall.this.mCallState == 1) {
                        IncomingCall.this.ringingEnded();
                    }
                    IncomingCall.this.mCallState = i;
                    return;
                default:
                    return;
            }
        }
    };
    private final BroadcastReceiver mScreenOffBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.SCREEN_OFF".equals(intent.getAction()) && IncomingCall.this.mCallState == 1 && !IncomingCall.this.mSilencedByPowerButton) {
                IncomingCall.this.mSilencedByPowerButton = true;
            }
        }
    };
    /* access modifiers changed from: private */
    public boolean mSilencedByPowerButton;
    /* access modifiers changed from: private */
    public boolean mSilencedBySwipe;
    private final TelephonyManager mTelephonyManager = ((TelephonyManager) this.mIRService.getSystemService("phone"));
    private final TelephonyManagerPrivateProxy mTelephonyManagerPrivateProxy = new TelephonyManagerPrivateProxy(this.mIRService);

    public String getEnabledPrefKey() {
        return IRService.IR_SWIPE_ENABLED;
    }

    /* access modifiers changed from: protected */
    public int getGestureSensorDelay() {
        return IRService.SENSOR_DELAY_IR_SWIPE;
    }

    public String getName() {
        return FEATURE_NAME;
    }

    public IncomingCall(IRService iRService) {
        super(iRService);
    }

    public void start() {
        if (TelephonyIntentsProxy.ACTION_CALL_DISCONNECTED != null) {
            this.mTelephonyManager.listen(this.mPhoneStateListener, 32);
        }
    }

    public void stop() {
        if (TelephonyIntentsProxy.ACTION_CALL_DISCONNECTED != null) {
            this.mTelephonyManager.listen(this.mPhoneStateListener, 0);
            if (this.mCallState == 1) {
                unregisterListeners();
            }
        }
    }

    public void enable() {
        if (TelephonyIntentsProxy.ACTION_CALL_DISCONNECTED != null) {
            super.enable();
        }
    }

    public void disable() {
        if (this.mCallState == 1) {
            unregisterIRGestureListener();
        }
        super.disable();
    }

    /* access modifiers changed from: protected */
    public SensorEventListener getIRGestureListener() {
        return this.mGestureListener;
    }

    /* access modifiers changed from: private */
    public void resetState() {
        this.mSilencedBySwipe = false;
        this.mSilencedByPowerButton = false;
        this.mCallState = 0;
    }

    /* access modifiers changed from: private */
    public void ringingEnded() {
        resetState();
        unregisterListeners();
    }

    private void unregisterListeners() {
        if (TelephonyIntentsProxy.ACTION_CALL_DISCONNECTED != null) {
            this.mIRService.unregisterReceiver(this.mScreenOffBroadcastReceiver);
            this.mIRService.unregisterReceiver(this.mCallEndBroadcastReceiver);
            unregisterIRGestureListener();
        }
    }

    /* access modifiers changed from: private */
    public void registerListeners() {
        if (TelephonyIntentsProxy.ACTION_CALL_DISCONNECTED != null) {
            this.mIRService.registerReceiver(this.mScreenOffBroadcastReceiver, new IntentFilter("android.intent.action.SCREEN_OFF"));
            this.mIRService.registerReceiver(this.mCallEndBroadcastReceiver, new IntentFilter(TelephonyIntentsProxy.ACTION_CALL_DISCONNECTED));
            registerIRGestureListener();
        }
    }

    /* access modifiers changed from: private */
    public void stopRinging() {
        this.mTelephonyManagerPrivateProxy.silenceRinger();
    }
}
