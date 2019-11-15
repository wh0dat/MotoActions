package com.motorola.actions.approach;

import com.motorola.actions.utils.Device;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.SensorhubServiceManager;
import java.nio.ByteBuffer;

public class GlanceConfiguration {
    private static final int ENABLE_IR = 0;
    private static final int ENABLE_NUDGE = 2;
    private static final int ENABLE_ULTRASOUND = 1;
    private static final int GESTURES_BITMASK = 56;
    private static final int GLANCE_IR_GESTURE = 8;
    private static final int GLANCE_MOVEMENT_GESTURE = 16;
    static final byte[] GLANCE_REGISTER = {0, 50, 0, 10};
    private static final int GLANCE_REGISTER_NUM = 50;
    private static final int GLANCE_REGISTER_SIZE = 10;
    private static final int GLANCE_ULTRASOUND_GESTURE = 32;
    private static final MALogger LOGGER = new MALogger(GlanceConfiguration.class);

    private static void config(int i, SensorhubServiceManager sensorhubServiceManager) {
        short s;
        byte[] bArr = new byte[10];
        ByteBuffer wrap = ByteBuffer.wrap(bArr);
        sensorhubServiceManager.read(GLANCE_REGISTER, bArr);
        short s2 = wrap.getShort();
        short s3 = (short) (s2 & -57);
        switch (i) {
            case 0:
                s = (short) (s3 | 8);
                break;
            case 1:
                s = (short) (s3 | 32);
                break;
            case 2:
                s = (short) (s3 | 16);
                break;
            default:
                LOGGER.mo11959e("Invalid argument");
                s = s2;
                break;
        }
        if (s != s2) {
            wrap.rewind();
            wrap.putShort(s);
            sensorhubServiceManager.write(GLANCE_REGISTER, bArr);
            LOGGER.mo11957d("configureFeatures: glance register value changed");
        }
    }

    public static void enableIr(SensorhubServiceManager sensorhubServiceManager) {
        if (sensorhubServiceManager == null) {
            LOGGER.mo11957d("mSensorHubManager is null");
        } else if (!Device.isVectorDevice()) {
            LOGGER.mo11957d("This device does not allow access to glance register");
        } else {
            LOGGER.mo11957d("Enable IR on Glance register");
            config(0, sensorhubServiceManager);
        }
    }

    public static void enableUs(SensorhubServiceManager sensorhubServiceManager) {
        if (sensorhubServiceManager == null) {
            LOGGER.mo11957d("mSensorHubManager is null");
        } else if (!Device.isVertexDevice()) {
            LOGGER.mo11957d("This device does not allow access to glance register");
        } else {
            LOGGER.mo11957d("Enable US on Glance register");
            config(1, sensorhubServiceManager);
        }
    }

    public static void enableNudge(SensorhubServiceManager sensorhubServiceManager) {
        if (sensorhubServiceManager == null) {
            LOGGER.mo11957d("mSensorHubManager is null");
        } else if (Device.isVectorDevice() || Device.isVertexDevice()) {
            LOGGER.mo11957d("Enable Nudge on Glance register");
            config(2, sensorhubServiceManager);
        } else {
            LOGGER.mo11957d("This device does not allow access to glance register");
        }
    }
}
