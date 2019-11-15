package com.motorola.actions.foc.gesture.state;

import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraManager.TorchCallback;
import android.os.Handler;
import android.text.TextUtils;
import com.motorola.actions.foc.gesture.FlashlightController;
import com.motorola.actions.utils.MALogger;

public class TorchState extends TorchCallback implements IStateSource {
    private static final MALogger LOGGER = new MALogger(TorchState.class);
    private String mCameraId;
    private CameraManager mCameraManager;
    private boolean mTorchModeUnavailable;

    public TorchState(CameraManager cameraManager, String str) {
        this.mCameraId = str;
        this.mCameraManager = cameraManager;
        start();
    }

    public void onTorchModeUnavailable(String str) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("onTorchModeUnavailable, cameraId=");
        sb.append(str);
        mALogger.mo11957d(sb.toString());
        super.onTorchModeUnavailable(str);
        if (!TextUtils.isEmpty(str) && str.equals(this.mCameraId)) {
            if (!FlashlightController.getFlashlightControllerHasCamera()) {
                LOGGER.mo11957d("Torch Mode unavailable");
                this.mTorchModeUnavailable = true;
                return;
            }
            LOGGER.mo11957d("Torch Mode is not unavailable, because FlashlightController");
            this.mTorchModeUnavailable = false;
        }
    }

    public void onTorchModeChanged(String str, boolean z) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("onTorchModeChanged, cameraId=");
        sb.append(str);
        mALogger.mo11957d(sb.toString());
        super.onTorchModeChanged(str, z);
        if (TextUtils.equals(str, this.mCameraId)) {
            LOGGER.mo11957d("Torch Mode available");
            this.mTorchModeUnavailable = false;
        }
    }

    public void start() {
        this.mCameraManager.registerTorchCallback(this, new Handler());
    }

    public boolean isStateAcceptableToTurnOn() {
        return !this.mTorchModeUnavailable;
    }

    public boolean isStateAcceptableToTurnOff() {
        return !this.mTorchModeUnavailable || FlashlightController.getFlashlightControllerHasCamera();
    }

    public void stop() {
        this.mCameraManager.unregisterTorchCallback(this);
    }
}
