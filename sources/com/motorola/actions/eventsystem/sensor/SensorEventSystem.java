package com.motorola.actions.eventsystem.sensor;

import android.hardware.SensorManager;
import android.support.annotation.NonNull;
import android.util.SparseArray;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.utils.MALogger;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.subjects.BehaviorSubject;

public final class SensorEventSystem {
    private static final MALogger LOGGER = new MALogger(SensorEventSystem.class);
    private SparseArray<SensorListenerSubjectPair> mSensorEvents = new SparseArray<>();
    private SensorManager mSensorManager = ((SensorManager) ActionsApplication.getAppContext().getSystemService("sensor"));

    @NonNull
    public BehaviorSubject<Float> getSensorEvent(@NonNull Integer num) throws SensorNotFoundException {
        if (this.mSensorEvents.get(num.intValue()) != null) {
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("Using existing sensor entry of type:");
            sb.append(num);
            mALogger.mo11957d(sb.toString());
            return ((SensorListenerSubjectPair) this.mSensorEvents.get(num.intValue())).getSubject();
        }
        MALogger mALogger2 = LOGGER;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Creating new sensor entry of type:");
        sb2.append(num);
        mALogger2.mo11957d(sb2.toString());
        SensorListenerSubjectPair create = SensorListenerSubjectPair.create(num, this.mSensorManager);
        this.mSensorEvents.put(num.intValue(), create);
        return create.getSubject();
    }

    public void release(@NonNull Integer num, @NonNull Disposable disposable) throws IllegalArgumentException {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("release sensor type:");
        sb.append(num);
        mALogger.mo11957d(sb.toString());
        if (!disposable.isDisposed()) {
            disposable.dispose();
        } else {
            LOGGER.mo11963w("release: disposable is already cleared");
        }
        SensorListenerSubjectPair sensorListenerSubjectPair = (SensorListenerSubjectPair) this.mSensorEvents.get(num.intValue());
        if (sensorListenerSubjectPair == null) {
            MALogger mALogger2 = LOGGER;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Trying to release unregistered sensor:");
            sb2.append(num);
            mALogger2.mo11963w(sb2.toString());
            throw new IllegalArgumentException();
        } else if (!sensorListenerSubjectPair.getSubject().hasObservers()) {
            LOGGER.mo11957d("release: unregister from SensorManager");
            sensorListenerSubjectPair.unregister(this.mSensorManager);
            this.mSensorEvents.remove(num.intValue());
        }
    }

    public boolean isEmpty() {
        return this.mSensorEvents.size() == 0;
    }
}
