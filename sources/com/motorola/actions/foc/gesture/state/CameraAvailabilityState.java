package com.motorola.actions.foc.gesture.state;

import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraManager.AvailabilityCallback;
import android.os.Handler;
import android.os.HandlerThread;
import com.motorola.actions.foc.gesture.FlashOnChopGestureManager;
import com.motorola.actions.foc.gesture.FlashlightController;
import com.motorola.actions.foc.gesture.util.FlashOnChopUtils;
import com.motorola.actions.utils.MALogger;

public class CameraAvailabilityState implements IStateSource {
    private static final String HANDLER_TAG;
    /* access modifiers changed from: private */
    public static final MALogger LOGGER = new MALogger(CameraAvailabilityState.class);
    private final AvailabilityCallback mCameraAvailabilityCallback = new AvailabilityCallback() {
        public void onCameraAvailable(String str) {
            MALogger access$000 = CameraAvailabilityState.LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("onCameraAvailable(");
            sb.append(str);
            sb.append(")");
            access$000.mo11957d(sb.toString());
            if (CameraAvailabilityState.this.isCameraFromDevice(str)) {
                if (str.equals(CameraAvailabilityState.this.mCameraId)) {
                    CameraAvailabilityState.this.mIsPrimaryCameraInUse = false;
                } else {
                    CameraAvailabilityState.this.mIsSecondaryCameraInUse = false;
                }
            }
        }

        public void onCameraUnavailable(String str) {
            MALogger access$000 = CameraAvailabilityState.LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("onCameraUnavailable(");
            sb.append(str);
            sb.append(")");
            access$000.mo11957d(sb.toString());
            if (CameraAvailabilityState.this.isCameraFromDevice(str)) {
                if (FlashlightController.getFlashlightControllerHasCamera()) {
                    if (str.equals(CameraAvailabilityState.this.mCameraId)) {
                        CameraAvailabilityState.this.mIsPrimaryCameraInUse = false;
                    } else {
                        CameraAvailabilityState.this.mIsSecondaryCameraInUse = false;
                    }
                } else if (str.equals(CameraAvailabilityState.this.mCameraId)) {
                    CameraAvailabilityState.this.mIsPrimaryCameraInUse = true;
                } else {
                    CameraAvailabilityState.this.mIsSecondaryCameraInUse = true;
                }
                if (CameraAvailabilityState.this.mCameraId.equals(str) && !FlashlightController.getFlashlightControllerHasCamera()) {
                    FlashlightController.setFlashlightControllerHasCamera(false);
                    FlashOnChopUtils.sendToForeground(false);
                }
            }
        }
    };
    private Handler mCameraDeviceHandler;
    /* access modifiers changed from: private */
    public final String mCameraId;
    private final CameraManager mCameraManager;
    /* access modifiers changed from: private */
    public boolean mIsPrimaryCameraInUse;
    /* access modifiers changed from: private */
    public boolean mIsSecondaryCameraInUse;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append("FOC.");
        sb.append(FlashOnChopGestureManager.class.getSimpleName());
        HANDLER_TAG = sb.toString();
    }

    public CameraAvailabilityState(CameraManager cameraManager, String str) {
        this.mCameraId = str;
        this.mCameraManager = cameraManager;
        start();
    }

    /* access modifiers changed from: private */
    public boolean isCameraFromDevice(String str) {
        try {
            if (Integer.parseInt(str) > 1) {
                return false;
            }
        } catch (NumberFormatException e) {
            LOGGER.mo11959e(e.toString());
        }
        return true;
    }

    public void start() {
        ensureCameraDeviceHandler();
        this.mCameraManager.registerAvailabilityCallback(this.mCameraAvailabilityCallback, this.mCameraDeviceHandler);
    }

    public boolean isStateAcceptableToTurnOn() {
        return !this.mIsPrimaryCameraInUse && !this.mIsSecondaryCameraInUse;
    }

    public boolean isStateAcceptableToTurnOff() {
        return !this.mIsPrimaryCameraInUse && !this.mIsSecondaryCameraInUse;
    }

    public void stop() {
        removeCameraDeviceStateListener();
    }

    private void removeCameraDeviceStateListener() {
        if (this.mCameraId != null) {
            LOGGER.mo11957d("removeCameraDeviceStateListener.");
            this.mCameraManager.unregisterAvailabilityCallback(this.mCameraAvailabilityCallback);
        }
    }

    private void ensureCameraDeviceHandler() {
        if (this.mCameraDeviceHandler == null) {
            HandlerThread handlerThread = new HandlerThread(HANDLER_TAG, 10);
            handlerThread.start();
            this.mCameraDeviceHandler = new Handler(handlerThread.getLooper());
        }
    }
}
