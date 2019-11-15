package com.motorola.actions.eventsystem.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import com.motorola.actions.utils.EventOnlySensorListener;
import p016io.reactivex.subjects.BehaviorSubject;

final class SensorListenerSubjectPair {
    /* access modifiers changed from: private */
    public static final Float DEFAULT_SENSOR_VALUE = Float.valueOf(0.0f);
    private final EventOnlySensorListener mListener;
    private final BehaviorSubject<Float> mSubject;

    private SensorListenerSubjectPair(EventOnlySensorListener eventOnlySensorListener, BehaviorSubject<Float> behaviorSubject) {
        this.mListener = eventOnlySensorListener;
        this.mSubject = behaviorSubject;
    }

    static SensorListenerSubjectPair create(Integer num, SensorManager sensorManager) throws SensorNotFoundException {
        final BehaviorSubject create = BehaviorSubject.create();
        C05481 r1 = new EventOnlySensorListener() {
            public void onSensorChanged(SensorEvent sensorEvent) {
                create.onNext(sensorEvent.values.length > 0 ? Float.valueOf(sensorEvent.values[0]) : SensorListenerSubjectPair.DEFAULT_SENSOR_VALUE);
            }
        };
        Sensor defaultSensor = sensorManager.getDefaultSensor(num.intValue());
        if (defaultSensor == null) {
            throw new SensorNotFoundException();
        }
        sensorManager.registerListener(r1, defaultSensor, 3);
        return new SensorListenerSubjectPair(r1, create);
    }

    /* access modifiers changed from: 0000 */
    public BehaviorSubject<Float> getSubject() {
        return this.mSubject;
    }

    /* access modifiers changed from: 0000 */
    public void unregister(SensorManager sensorManager) {
        sensorManager.unregisterListener(this.mListener);
    }
}
