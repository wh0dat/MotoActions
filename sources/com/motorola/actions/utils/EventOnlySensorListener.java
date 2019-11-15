package com.motorola.actions.utils;

import android.hardware.Sensor;
import android.hardware.SensorEventListener;

public abstract class EventOnlySensorListener implements SensorEventListener {
    public void onAccuracyChanged(Sensor sensor, int i) {
    }
}
