package com.motorola.actions.attentivedisplay.face;

import com.motorola.actions.attentivedisplay.facedetection.Camera2FaceDetector;

public class FaceEngineFactory {
    private static Camera2FaceDetector sFaceDetector;

    private static synchronized void setInstance() {
        synchronized (FaceEngineFactory.class) {
            if (sFaceDetector == null) {
                sFaceDetector = new Camera2FaceDetector();
            }
        }
    }

    public static synchronized Camera2FaceDetector acquireInstance() {
        synchronized (FaceEngineFactory.class) {
            setInstance();
            if (sFaceDetector == null) {
                return null;
            }
            Camera2FaceDetector camera2FaceDetector = sFaceDetector;
            return camera2FaceDetector;
        }
    }

    public static synchronized void releaseInstance() {
        synchronized (FaceEngineFactory.class) {
            if (sFaceDetector != null) {
                sFaceDetector = null;
            }
        }
    }
}
