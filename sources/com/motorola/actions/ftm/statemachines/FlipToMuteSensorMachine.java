package com.motorola.actions.ftm.statemachines;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import com.motorola.actions.ftm.FlipToMuteService;
import com.motorola.actions.reflect.SensorPrivateProxy;
import com.motorola.actions.utils.EventOnlySensorListener;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.zenmode.AutomaticRulesConfigObserver;

public class FlipToMuteSensorMachine extends FlipToMuteBaseMachine {
    private static final double FLIP_TO_MUTE_GESTURE_EVENT_DETECTED = 1.0d;
    /* access modifiers changed from: private */
    public static final MALogger LOGGER = new MALogger(FlipToMuteSensorMachine.class);
    private final SensorEventListener mFTMGestureListener = new EventOnlySensorListener() {
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (((double) sensorEvent.values[0]) == FlipToMuteSensorMachine.FLIP_TO_MUTE_GESTURE_EVENT_DETECTED) {
                FlipToMuteSensorMachine.LOGGER.mo11957d("FLIP_TO_MUTE_GESTURE_EVENT_TRUE");
                if (FlipToMuteSensorMachine.this.shouldEnterDNDMode()) {
                    FlipToMuteSensorMachine.this.enterDND();
                    FlipToMuteService.setIsDNDState(true);
                    return;
                }
                return;
            }
            FlipToMuteSensorMachine.LOGGER.mo11957d("FLIP_TO_MUTE_GESTURE_EVENT_FALSE");
            FlipToMuteSensorMachine.this.changeDoNotDisturbToOriginalState();
            FlipToMuteService.setIsDNDState(false);
        }
    };
    private SensorManager mSensorManager;

    public FlipToMuteSensorMachine(Context context, AutomaticRulesConfigObserver automaticRulesConfigObserver) {
        super(context, automaticRulesConfigObserver);
    }

    public void start() {
        LOGGER.mo11957d("start");
        this.mSensorManager = (SensorManager) this.mContext.getSystemService("sensor");
        if (this.mSensorManager != null) {
            Sensor defaultSensor = this.mSensorManager.getDefaultSensor(SensorPrivateProxy.TYPE_MOTO_FLIP_TO_MUTE_GESTURE);
            if (defaultSensor != null) {
                LOGGER.mo11957d("register Flip To Mute Gesture sensor");
                this.mSensorManager.registerListener(this.mFTMGestureListener, defaultSensor, 3);
                return;
            }
            return;
        }
        LOGGER.mo11959e("Unable to retrieve access to sensor manager");
    }

    public void stop() {
        LOGGER.mo11957d("stop");
        LOGGER.mo11957d("unregister Flip To Mute Gesture sensor");
        this.mSensorManager.unregisterListener(this.mFTMGestureListener);
        unregisterAutomaticRulesListener();
    }
}
