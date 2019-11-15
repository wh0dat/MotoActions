package com.motorola.actions.attentivedisplay;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.support.annotation.NonNull;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.attentivedisplay.util.Orientation;
import com.motorola.actions.debug.items.ADSensorDebugItem;
import com.motorola.actions.utils.EventOnlySensorListener;
import com.motorola.actions.utils.MALogger;

public class MovementDetectManager extends EventOnlySensorListener {
    private static final int GYRO_MULT_VALUE = 1000;
    private static final int GYRO_THRESHOLD = 5;
    private static final MALogger LOGGER = new MALogger(MovementDetectManager.class);
    private static final float X_AXIS = 3.7f;
    private static final float X_LAND_RIGHT = -3.7f;
    private static final int Y_LAND = 6;
    private static final int Y_PORT = 4;
    private static final int Z_LAND = 9;
    private static final float Z_PORT = 9.0f;
    private boolean mIsRunning;
    private final MovementChangeListener mListener;
    private int mScreenOrientation = -1;
    private Sensor mSensor;
    private final SensorManager mSensorManager;

    public interface MovementChangeListener {
        void onMovementChange(boolean z);
    }

    private boolean checkLandscape(float f, float f2, float f3) {
        return (f > X_AXIS || f < X_LAND_RIGHT) && f2 < 6.0f && f3 < Z_PORT;
    }

    private boolean checkPortrait(float f, float f2, float f3) {
        return f < X_AXIS && f2 > 4.0f && f3 < Z_PORT;
    }

    public MovementDetectManager(@NonNull Context context, @NonNull MovementChangeListener movementChangeListener) {
        this.mListener = movementChangeListener;
        this.mSensorManager = (SensorManager) context.getSystemService("sensor");
    }

    public boolean start(boolean z) {
        LOGGER.mo11957d("start");
        if (this.mSensorManager != null) {
            this.mSensor = this.mSensorManager.getDefaultSensor(getSensorType());
            if (this.mSensor != null) {
                if (this.mSensorManager.registerListener(this, this.mSensor, z ? 3 : 2)) {
                    this.mIsRunning = true;
                    return true;
                }
            }
        }
        LOGGER.mo11957d("Unable to register for movement detection");
        return false;
    }

    private int getSensorType() {
        boolean z = false;
        if (SharedPreferenceManager.getInt(ADSensorDebugItem.KEY_TEST_USE_SENSOR, 0) == 1) {
            z = true;
        }
        return z ? 4 : 1;
    }

    public void onSensorChanged(SensorEvent sensorEvent) {
        LOGGER.mo11957d("onSensorChanged");
        if (this.mIsRunning && sensorEvent != null && sensorEvent.sensor != null && sensorEvent.sensor == this.mSensor) {
            int type = sensorEvent.sensor.getType();
            if (type == 1) {
                checkAccelValues(sensorEvent);
            } else if (type != 4) {
                LOGGER.mo11957d("Not valid sensor type...");
            } else {
                checkGyroValues(sensorEvent);
            }
        }
    }

    private void checkGyroValues(SensorEvent sensorEvent) {
        if (sensorEvent.values != null && sensorEvent.values.length == 3) {
            float f = sensorEvent.values[0] * 1000.0f;
            float f2 = sensorEvent.values[1] * 1000.0f;
            float f3 = sensorEvent.values[2] * 1000.0f;
            if (f > 5.0f || f2 > 5.0f || f3 > 5.0f) {
                LOGGER.mo11957d("checkGyroValues: movement detected!");
                movementDetected(true);
                return;
            }
            movementDetected(false);
        }
    }

    private void checkAccelValues(SensorEvent sensorEvent) {
        if (sensorEvent.values != null && sensorEvent.values.length == 3) {
            float f = sensorEvent.values[0];
            float f2 = sensorEvent.values[1];
            float f3 = sensorEvent.values[2];
            if (checkPortrait(f, f2, f3) || checkLandscape(f, f2, f3)) {
                this.mScreenOrientation = getPosition(f, f2, f3);
                movementDetected(true);
                return;
            }
            movementDetected(false);
        }
    }

    private int getPosition(float f, float f2, float f3) {
        int i;
        String str = "";
        if (checkPortrait(f, f2, f3)) {
            i = Orientation.PORTRAIT;
            str = "Port";
        } else if (f > X_AXIS && f2 < 6.0f && f3 < Z_PORT) {
            i = 0;
            str = "Land L";
        } else if (f >= X_LAND_RIGHT || f2 >= 6.0f || f3 >= Z_PORT) {
            i = -1;
        } else {
            i = Orientation.LANDSCAPE_RIGHT;
            str = "Land R";
        }
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("onSensorChanged: checkAccelValues: true - ");
        sb.append(str);
        mALogger.mo11957d(sb.toString());
        return i;
    }

    private void movementDetected(boolean z) {
        this.mListener.onMovementChange(z);
    }

    public void stop() {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("stop - mIsRunning: ");
        sb.append(this.mIsRunning);
        mALogger.mo11957d(sb.toString());
        if (this.mIsRunning) {
            this.mIsRunning = false;
            this.mSensorManager.unregisterListener(this);
        }
    }

    public int getScreenOrientation() {
        return this.mScreenOrientation;
    }
}
