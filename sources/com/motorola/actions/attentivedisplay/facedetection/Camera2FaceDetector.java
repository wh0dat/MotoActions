package com.motorola.actions.attentivedisplay.facedetection;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCaptureSession.CaptureCallback;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraDevice.StateCallback;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureRequest.Builder;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.Face;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.media.ImageReader.OnImageAvailableListener;
import android.support.annotation.NonNull;
import android.util.Size;
import android.view.Surface;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.attentivedisplay.face.FaceDetector;
import com.motorola.actions.attentivedisplay.face.FaceDetector.FaceDetectionListener;
import com.motorola.actions.utils.MALogger;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class Camera2FaceDetector implements FaceDetector {
    private static final long FACE_DETECTION_TIMEOUT_MS = 300;
    /* access modifiers changed from: private */
    public static final MALogger LOGGER = new MALogger(Camera2FaceDetector.class);
    /* access modifiers changed from: private */
    public CameraDevice mCameraDevice;
    private String mCameraId;
    private CameraManager mCameraManager = ((CameraManager) ActionsApplication.getAppContext().getSystemService("camera"));
    /* access modifiers changed from: private */
    public final CaptureCallback mCaptureCallback = new CaptureCallback() {
        public void onCaptureCompleted(@NonNull CameraCaptureSession cameraCaptureSession, @NonNull CaptureRequest captureRequest, @NonNull TotalCaptureResult totalCaptureResult) {
            Camera2FaceDetector.LOGGER.mo11957d("onCaptureCompleted()");
            process(totalCaptureResult);
        }

        private void process(CaptureResult captureResult) {
            Camera2FaceDetector.LOGGER.mo11957d("process()");
            if (Camera2FaceDetector.this.mFirstDetectionAttemptTimestamp == 0) {
                Camera2FaceDetector.this.mFirstDetectionAttemptTimestamp = System.currentTimeMillis();
            }
            Integer num = (Integer) captureResult.get(CaptureResult.STATISTICS_FACE_DETECT_MODE);
            Face[] faceArr = (Face[]) captureResult.get(CaptureResult.STATISTICS_FACES);
            if (faceArr != null && num != null && Camera2FaceDetector.this.mImage != null) {
                MALogger access$000 = Camera2FaceDetector.LOGGER;
                StringBuilder sb = new StringBuilder();
                sb.append("faces: ");
                sb.append(faceArr.length);
                sb.append(", mode: ");
                sb.append(num);
                access$000.mo11957d(sb.toString());
                boolean z = faceArr.length > 0;
                long currentTimeMillis = System.currentTimeMillis() - Camera2FaceDetector.this.mFirstDetectionAttemptTimestamp;
                if (z || currentTimeMillis > Camera2FaceDetector.FACE_DETECTION_TIMEOUT_MS) {
                    Camera2FaceDetector.this.mFaceDetectionListener.onFaceDetection(z, Camera2FaceDetector.this.mImage);
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public CaptureRequest mCaptureRequest;
    /* access modifiers changed from: private */
    public Builder mCaptureRequestBuilder;
    /* access modifiers changed from: private */
    public CameraCaptureSession mCaptureSession;
    /* access modifiers changed from: private */
    public FaceDetectionListener mFaceDetectionListener;
    /* access modifiers changed from: private */
    public long mFirstDetectionAttemptTimestamp;
    /* access modifiers changed from: private */
    public byte[] mImage;
    /* access modifiers changed from: private */
    public ImageReader mImageReader;
    /* access modifiers changed from: private */
    public boolean mIsShuttingDown = false;
    private final OnImageAvailableListener mOnImageAvailableListener = new OnImageAvailableListener() {
        public void onImageAvailable(ImageReader imageReader) {
            Image acquireLatestImage = imageReader.acquireLatestImage();
            if (acquireLatestImage != null) {
                ByteBuffer buffer = acquireLatestImage.getPlanes()[0].getBuffer();
                Camera2FaceDetector.this.mImage = new byte[buffer.remaining()];
                buffer.get(Camera2FaceDetector.this.mImage);
                acquireLatestImage.close();
            }
        }
    };
    private final StateCallback mStateCallback = new StateCallback() {
        public void onOpened(@NonNull CameraDevice cameraDevice) {
            Camera2FaceDetector.LOGGER.mo11957d("onOpened()");
            Camera2FaceDetector.this.mCameraDevice = cameraDevice;
            if (!Camera2FaceDetector.this.mIsShuttingDown) {
                Camera2FaceDetector.this.createCameraPreviewSession();
            } else {
                Camera2FaceDetector.this.stopDetection();
            }
        }

        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            Camera2FaceDetector.LOGGER.mo11957d("onDisconnected()");
            cameraDevice.close();
            if (Camera2FaceDetector.this.mCameraDevice != null) {
                Camera2FaceDetector.this.mCameraDevice.close();
                Camera2FaceDetector.this.mCameraDevice = null;
            }
        }

        public void onError(@NonNull CameraDevice cameraDevice, int i) {
            MALogger access$000 = Camera2FaceDetector.LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("onError(), error = ");
            sb.append(i);
            access$000.mo11959e(sb.toString());
            cameraDevice.close();
            if (Camera2FaceDetector.this.mCameraDevice != null) {
                Camera2FaceDetector.this.mCameraDevice.close();
                Camera2FaceDetector.this.mCameraDevice = null;
            }
        }
    };

    private static class CompareSizesByArea implements Comparator<Size>, Serializable {
        private CompareSizesByArea() {
        }

        public int compare(Size size, Size size2) {
            return Long.signum((((long) size.getWidth()) * ((long) size.getHeight())) - (((long) size2.getWidth()) * ((long) size2.getHeight())));
        }
    }

    public Camera2FaceDetector() {
        setUpCamera();
    }

    private void setUpCamera() {
        String[] cameraIdList;
        LOGGER.mo11957d("setUpCamera()");
        try {
            for (String str : this.mCameraManager.getCameraIdList()) {
                CameraCharacteristics cameraCharacteristics = this.mCameraManager.getCameraCharacteristics(str);
                if (((Integer) cameraCharacteristics.get(CameraCharacteristics.LENS_FACING)).intValue() == 0) {
                    StreamConfigurationMap streamConfigurationMap = (StreamConfigurationMap) cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                    if (streamConfigurationMap != null) {
                        this.mCameraId = str;
                        Size size = (Size) Collections.min(Arrays.asList(streamConfigurationMap.getOutputSizes(256)), new CompareSizesByArea());
                        this.mImageReader = ImageReader.newInstance(size.getWidth(), size.getHeight(), 256, 2);
                        this.mImageReader.setOnImageAvailableListener(this.mOnImageAvailableListener, null);
                        return;
                    }
                }
            }
        } catch (CameraAccessException e) {
            LOGGER.mo11960e("Error on camera set up", e);
            this.mFaceDetectionListener.onDetectionError();
        }
    }

    /* access modifiers changed from: private */
    public void createCameraPreviewSession() {
        LOGGER.mo11957d("createCameraPreviewSession()");
        try {
            if (this.mImageReader != null) {
                this.mCameraDevice.createCaptureSession(Arrays.asList(new Surface[]{this.mImageReader.getSurface()}), new CameraCaptureSession.StateCallback() {
                    public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                        Camera2FaceDetector.LOGGER.mo11957d("onConfigured()");
                        if (Camera2FaceDetector.this.mCameraDevice != null) {
                            try {
                                Camera2FaceDetector.this.mCaptureRequestBuilder = Camera2FaceDetector.this.mCameraDevice.createCaptureRequest(1);
                                Camera2FaceDetector.this.mCaptureRequestBuilder.addTarget(Camera2FaceDetector.this.mImageReader.getSurface());
                                Camera2FaceDetector.this.mCaptureRequestBuilder.set(CaptureRequest.STATISTICS_FACE_DETECT_MODE, Integer.valueOf(2));
                                Camera2FaceDetector.this.mCaptureRequest = Camera2FaceDetector.this.mCaptureRequestBuilder.build();
                                Camera2FaceDetector.this.mCaptureSession = cameraCaptureSession;
                                Camera2FaceDetector.this.mCaptureSession.setRepeatingRequest(Camera2FaceDetector.this.mCaptureRequest, Camera2FaceDetector.this.mCaptureCallback, null);
                            } catch (CameraAccessException e) {
                                Camera2FaceDetector.LOGGER.mo11960e("Error on configuring CameraCaptureSession", e);
                                Camera2FaceDetector.this.mFaceDetectionListener.onDetectionError();
                            }
                        }
                    }

                    public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                        Camera2FaceDetector.LOGGER.mo11959e("Error on configuring CameraCaptureSession");
                        Camera2FaceDetector.this.mFaceDetectionListener.onDetectionError();
                    }
                }, null);
            }
        } catch (CameraAccessException | IllegalArgumentException | SecurityException e) {
            LOGGER.mo11960e("Error on createCameraPreviewSession", e);
            this.mFaceDetectionListener.onDetectionError();
        }
    }

    public void startDetection(FaceDetectionListener faceDetectionListener) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("startDetection(), mCameraId = ");
        sb.append(this.mCameraId);
        mALogger.mo11957d(sb.toString());
        this.mFaceDetectionListener = faceDetectionListener;
        if (ActionsApplication.getAppContext().checkSelfPermission("android.permission.CAMERA") == 0 && this.mCameraId != null) {
            try {
                this.mCameraManager.openCamera(this.mCameraId, this.mStateCallback, null);
            } catch (CameraAccessException e) {
                LOGGER.mo11960e("Error starting detection", e);
                this.mFaceDetectionListener.onDetectionError();
            }
        }
    }

    public void stopDetection() {
        LOGGER.mo11957d("stopDetection()");
        this.mIsShuttingDown = true;
        if (this.mCaptureSession != null) {
            try {
                LOGGER.mo11957d("stopDetection() - mCaptureSession.close()");
                this.mCaptureSession.stopRepeating();
                this.mCaptureSession.close();
                this.mCaptureSession = null;
            } catch (CameraAccessException | IllegalStateException e) {
                LOGGER.mo11964w("Error closing capture session", e);
            }
        }
        if (this.mCameraDevice != null) {
            LOGGER.mo11957d("stopDetection() - mCameraDevice.close()");
            this.mCameraDevice.close();
            this.mCameraDevice = null;
        }
        if (this.mImageReader != null) {
            LOGGER.mo11957d("stopDetection() - mImageReader.close()");
            this.mImageReader.close();
            this.mImageReader = null;
        }
        this.mCameraManager = null;
        this.mFirstDetectionAttemptTimestamp = 0;
    }
}
