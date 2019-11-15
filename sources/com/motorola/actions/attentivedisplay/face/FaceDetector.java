package com.motorola.actions.attentivedisplay.face;

public interface FaceDetector {

    public interface FaceDetectionListener {
        void onDetectionError();

        void onFaceDetection(boolean z, byte[] bArr);
    }

    void startDetection(FaceDetectionListener faceDetectionListener);

    void stopDetection();
}
