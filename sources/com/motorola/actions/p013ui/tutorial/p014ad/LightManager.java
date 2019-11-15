package com.motorola.actions.p013ui.tutorial.p014ad;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.support.annotation.NonNull;
import com.motorola.actions.utils.EventOnlySensorListener;
import com.motorola.actions.utils.MALogger;

/* renamed from: com.motorola.actions.ui.tutorial.ad.LightManager */
public class LightManager extends EventOnlySensorListener {
    private static final MALogger LOGGER = new MALogger(LightManager.class);
    private static final float MIN_ACCEPTABLE_LIGHT = 10.0f;
    private boolean mIsRunning;
    private final LightChangeListener mListener;
    private Sensor mSensor;
    private final SensorManager mSensorManager;

    /* renamed from: com.motorola.actions.ui.tutorial.ad.LightManager$LightChangeListener */
    public interface LightChangeListener {
        void onLightChange(boolean z);
    }

    public LightManager(@NonNull Context context, @NonNull LightChangeListener lightChangeListener) {
        this.mListener = lightChangeListener;
        this.mSensorManager = (SensorManager) context.getSystemService("sensor");
    }

    public boolean start() {
        LOGGER.mo11957d("start");
        if (this.mSensorManager != null && !this.mIsRunning) {
            this.mSensor = this.mSensorManager.getDefaultSensor(5);
            if (this.mSensor != null && this.mSensorManager.registerListener(this, this.mSensor, 3)) {
                this.mIsRunning = true;
                return true;
            }
        }
        LOGGER.mo11957d("Unable to register for light detection");
        return false;
    }

    public void onSensorChanged(SensorEvent sensorEvent) {
        LOGGER.mo11957d("onSensorChanged");
        if (this.mIsRunning && sensorEvent != null && sensorEvent.sensor != null && sensorEvent.sensor == this.mSensor && this.mListener != null) {
            LightChangeListener lightChangeListener = this.mListener;
            boolean z = false;
            if (sensorEvent.values[0] > MIN_ACCEPTABLE_LIGHT) {
                z = true;
            }
            lightChangeListener.onLightChange(z);
        }
    }

    public void stop() {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("stop - mIsRunning: ");
        sb.append(this.mIsRunning);
        mALogger.mo11957d(sb.toString());
        if (this.mSensorManager != null) {
            this.mSensorManager.unregisterListener(this);
        }
        this.mIsRunning = false;
    }
}
