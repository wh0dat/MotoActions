package com.motorola.actions.eventsystem;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.motorola.actions.eventsystem.sensor.SensorEventSystem;
import com.motorola.actions.eventsystem.sensor.SensorNotFoundException;
import com.motorola.actions.utils.MALogger;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.functions.Consumer;

public final class EventSystem {
    private static final MALogger LOGGER = new MALogger(EventSystem.class);
    private SensorEventSystem mSensorEventSystem;

    private static class SingletonHolder {
        static final EventSystem INSTANCE = new EventSystem();

        private SingletonHolder() {
        }
    }

    private EventSystem() {
    }

    @NonNull
    public static EventSystem getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @NonNull
    public Disposable subscribeSensorEvent(@NonNull Integer num, @NonNull Consumer<Float> consumer) throws SensorNotFoundException {
        Disposable subscribe;
        synchronized (this) {
            if (this.mSensorEventSystem == null) {
                this.mSensorEventSystem = new SensorEventSystem();
            }
            subscribe = this.mSensorEventSystem.getSensorEvent(num).subscribe((Consumer) consumer);
        }
        return subscribe;
    }

    public void unsubscribeSensorEvent(@NonNull Integer num, @Nullable Disposable disposable) throws IllegalArgumentException {
        if (disposable == null) {
            throw new IllegalArgumentException("parameter disposable is null");
        }
        synchronized (this) {
            if (this.mSensorEventSystem != null) {
                this.mSensorEventSystem.release(num, disposable);
                if (this.mSensorEventSystem.isEmpty()) {
                    LOGGER.mo11957d("Releasing sensor event system");
                    this.mSensorEventSystem = null;
                }
            } else {
                LOGGER.mo11959e("Unsubscribe called without prior call to subscribe()");
            }
        }
    }
}
