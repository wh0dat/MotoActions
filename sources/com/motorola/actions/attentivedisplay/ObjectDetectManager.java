package com.motorola.actions.attentivedisplay;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.TriggerEvent;
import android.hardware.TriggerEventListener;
import com.motorola.actions.reflect.SensorPrivateProxy;
import com.motorola.actions.utils.MALogger;

public class ObjectDetectManager extends TriggerEventListener implements SensorEventListener {
    private static final MALogger LOGGER = new MALogger(ObjectDetectManager.class);
    private static final int OBJECT_DETECTED = 1;
    private static final int OBJECT_NOT_DETECTED = 2;
    private final Context mContext;
    private final Sensor mIrGestureSensor = this.mSensorManager.getDefaultSensor(SensorPrivateProxy.TYPE_IR_GESTURE);
    private final Sensor mIrObjectSensor = this.mSensorManager.getDefaultSensor(SensorPrivateProxy.TYPE_IR_OBJECT);
    private boolean mIsRunning;
    private final ObjectDetectListener mListener;
    private ObjectState mObjectState;
    private final SensorManager mSensorManager = ((SensorManager) this.mContext.getSystemService("sensor"));

    public interface ObjectDetectListener {
        void onObjectDetection(boolean z);
    }

    public enum ObjectState {
        UNKNOWN,
        DETECTED,
        NOT_DETECTED
    }

    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    public ObjectDetectManager(Context context, ObjectDetectListener objectDetectListener) {
        this.mContext = context;
        this.mListener = objectDetectListener;
    }

    public boolean start() {
        if (!(this.mIrObjectSensor == null || this.mIrGestureSensor == null || !this.mSensorManager.registerListener(this, this.mIrGestureSensor, 2))) {
            if (this.mSensorManager.requestTriggerSensor(this, this.mIrObjectSensor)) {
                this.mIsRunning = true;
                this.mObjectState = ObjectState.UNKNOWN;
                return true;
            }
            this.mSensorManager.unregisterListener(this);
        }
        LOGGER.mo11957d("unable to register for object detection");
        return false;
    }

    public void stop() {
        if (this.mIsRunning) {
            this.mSensorManager.cancelTriggerSensor(this, this.mIrObjectSensor);
            this.mSensorManager.unregisterListener(this);
            this.mIsRunning = false;
        }
    }

    public void onTrigger(TriggerEvent triggerEvent) {
        if (this.mIsRunning && this.mObjectState == ObjectState.UNKNOWN) {
            boolean z = false;
            if (triggerEvent.values.length >= 1 && 1 == ((int) triggerEvent.values[0])) {
                z = true;
            }
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("Trigger: ");
            sb.append(z ? "Object" : "No Object");
            mALogger.mo11957d(sb.toString());
            this.mListener.onObjectDetection(z);
        }
    }

    public void onSensorChanged(SensorEvent sensorEvent) {
        if (this.mIsRunning && sensorEvent.sensor == this.mIrGestureSensor && sensorEvent.values != null && sensorEvent.values.length >= 2) {
            ObjectState objectState = null;
            boolean z = true;
            if (sensorEvent.values[1] == 1.0f) {
                LOGGER.mo11957d("Sensor: Object");
                objectState = ObjectState.DETECTED;
            } else if (sensorEvent.values[1] == 2.0f) {
                LOGGER.mo11957d("Sensor: No Object");
                objectState = ObjectState.NOT_DETECTED;
            }
            if (objectState != null && this.mObjectState != objectState) {
                this.mObjectState = objectState;
                ObjectDetectListener objectDetectListener = this.mListener;
                if (this.mObjectState != ObjectState.DETECTED) {
                    z = false;
                }
                objectDetectListener.onObjectDetection(z);
            }
        }
    }
}
