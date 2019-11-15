package com.motorola.actions.reflect;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.utils.MALogger;

public class SensorPrivateProxy {
    private static final MALogger LOGGER = new MALogger(SensorPrivateProxy.class);
    public static final int TYPE_CAMERA_ACTIVATE;
    public static final int TYPE_CHOPCHOP_GESTURE;
    public static final int TYPE_FLAT_DOWN;
    public static final int TYPE_FLAT_UP;
    public static final int TYPE_IR_GESTURE;
    public static final int TYPE_IR_OBJECT;
    public static final int TYPE_MOTO_FLIP_TO_MUTE_GESTURE;
    public static final int TYPE_MOTO_GLANCE_APPROACH_GESTURE;
    public static final int TYPE_MOTO_LIFT_TO_SILENCE_GESTURE;
    public static final int TYPE_PROXIMITY;
    public static final int TYPE_STOWED;
    public static final int TYPE_ULTRASOUND_GESTURE;

    public static boolean isDefined(int i) {
        return i != -1;
    }

    static {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        int i12;
        try {
            i = Sensor.class.getDeclaredField("TYPE_IR_OBJECT").getInt(null);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            LOGGER.mo11964w("Reflection error. TYPE_IR_OBJECT", e);
            i = -1;
        }
        try {
            i2 = Sensor.class.getDeclaredField("TYPE_IR_GESTURE").getInt(null);
        } catch (IllegalAccessException | NoSuchFieldException e2) {
            LOGGER.mo11964w("Reflection error. TYPE_IR_GESTURE", e2);
            i2 = -1;
        }
        try {
            i3 = Sensor.class.getDeclaredField("TYPE_CAMERA_ACTIVATE").getInt(null);
        } catch (IllegalAccessException | NoSuchFieldException e3) {
            LOGGER.mo11964w("Reflection error. TYPE_CAMERA_ACTIVATE", e3);
            i3 = -1;
        }
        try {
            i4 = Sensor.class.getDeclaredField("TYPE_CHOPCHOP_GESTURE").getInt(null);
        } catch (IllegalAccessException | NoSuchFieldException e4) {
            LOGGER.mo11964w("Reflection error. TYPE_CHOPCHOP_GESTURE", e4);
            i4 = -1;
        }
        try {
            i5 = Sensor.class.getDeclaredField("TYPE_FLAT_UP").getInt(null);
        } catch (IllegalAccessException | NoSuchFieldException e5) {
            LOGGER.mo11964w("Reflection error. TYPE_FLAT_UP", e5);
            i5 = -1;
        }
        try {
            i6 = Sensor.class.getDeclaredField("TYPE_FLAT_DOWN").getInt(null);
        } catch (IllegalAccessException | NoSuchFieldException e6) {
            LOGGER.mo11964w("Reflection error. TYPE_FLAT_DOWN", e6);
            i6 = -1;
        }
        try {
            i7 = Sensor.class.getDeclaredField("TYPE_STOWED").getInt(null);
        } catch (IllegalAccessException | NoSuchFieldException e7) {
            LOGGER.mo11964w("Reflection error. TYPE_STOWED", e7);
            i7 = -1;
        }
        try {
            i8 = Sensor.class.getDeclaredField("TYPE_PROXIMITY").getInt(null);
        } catch (IllegalAccessException | NoSuchFieldException e8) {
            LOGGER.mo11964w("Reflection error. TYPE_PROXIMITY", e8);
            i8 = -1;
        }
        try {
            i9 = Sensor.class.getDeclaredField("TYPE_ULTRASOUND_GESTURE").getInt(null);
        } catch (IllegalAccessException | NoSuchFieldException e9) {
            LOGGER.mo11964w("Reflection error. TYPE_ULTRASOUND_GESTURE", e9);
            i9 = -1;
        }
        try {
            i10 = Sensor.class.getDeclaredField("TYPE_MOTO_FLIP_TO_MUTE_GESTURE").getInt(null);
        } catch (IllegalAccessException | NoSuchFieldException e10) {
            LOGGER.mo11964w("Reflection error. TYPE_MOTO_FLIP_TO_MUTE_GESTURE", e10);
            i10 = -1;
        }
        try {
            i11 = Sensor.class.getDeclaredField("TYPE_MOTO_LIFT_TO_SILENCE_GESTURE").getInt(null);
        } catch (IllegalAccessException | NoSuchFieldException e11) {
            LOGGER.mo11964w("Reflection error. TYPE_MOTO_LIFT_TO_SILENCE_GESTURE", e11);
            i11 = -1;
        }
        try {
            i12 = Sensor.class.getDeclaredField("TYPE_MOTO_GLANCE_APPROACH_GESTURE").getInt(null);
        } catch (IllegalAccessException | NoSuchFieldException e12) {
            LOGGER.mo11964w("Reflection error. TYPE_MOTO_GLANCE_APPROACH_GESTURE", e12);
            i12 = -1;
        }
        TYPE_IR_OBJECT = i;
        TYPE_IR_GESTURE = i2;
        TYPE_CAMERA_ACTIVATE = i3;
        TYPE_CHOPCHOP_GESTURE = i4;
        TYPE_FLAT_UP = i5;
        TYPE_FLAT_DOWN = i6;
        TYPE_STOWED = i7;
        TYPE_PROXIMITY = i8;
        TYPE_ULTRASOUND_GESTURE = i9;
        TYPE_MOTO_FLIP_TO_MUTE_GESTURE = i10;
        TYPE_MOTO_LIFT_TO_SILENCE_GESTURE = i11;
        TYPE_MOTO_GLANCE_APPROACH_GESTURE = i12;
    }

    public static boolean isProxySensorAvailable(int i) {
        try {
            SensorManager sensorManager = (SensorManager) ActionsApplication.getAppContext().getSystemService("sensor");
            if (!isDefined(i) || sensorManager == null) {
                return false;
            }
            if (sensorManager.getDefaultSensor(i) != null || !sensorManager.getSensorList(i).isEmpty()) {
                return true;
            }
            return false;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }
}
