package com.motorola.actions.foc.gesture.torch;

import android.hardware.camera2.CameraManager;
import com.motorola.actions.foc.gesture.util.FlashOnChopAlgorithmUtil;
import com.motorola.actions.foc.gesture.util.FlashOnChopAlgorithmUtil.FlashOnChopAlgorithmUsedType;
import com.motorola.actions.foc.gesture.util.FlashOnChopExceptions.AlgorithmNotDefinedException;
import com.motorola.actions.foc.gesture.util.FlashOnChopExceptions.NullCameraManagerException;
import com.motorola.actions.foc.gesture.util.FlashOnChopUtils;
import com.motorola.actions.utils.MALogger;

public class FlashlightAccess {
    private static final MALogger LOGGER = new MALogger(FlashlightAccess.class);
    String mCameraId;
    CameraManager mCameraManager;

    public void setup() {
    }

    public boolean setTorch(boolean z, long j) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("setTorch - START - enabled: ");
        sb.append(z);
        mALogger.mo11957d(sb.toString());
        setup();
        boolean z2 = true;
        try {
            if (this.mCameraManager == null) {
                LOGGER.mo11957d("mCameraManager is null");
                throw new NullCameraManagerException();
            }
            if (z) {
                FlashOnChopUtils.sendToForeground(true);
                LOGGER.mo11957d("setTorchMode: Send To Foreground");
            }
            MALogger mALogger2 = LOGGER;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("setTorch - END - isSetTorchSuccess: ");
            sb2.append(z2);
            mALogger2.mo11957d(sb2.toString());
            return z2;
        } catch (NullCameraManagerException | IllegalArgumentException e) {
            FlashOnChopUtils.sendToForeground(false);
            MALogger mALogger3 = LOGGER;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Camera API Exception of ");
            sb3.append(e.getClass().getName());
            sb3.append(" : ");
            sb3.append(e.getMessage());
            mALogger3.mo11959e(sb3.toString());
            z2 = false;
        }
    }

    public void onPostTorch(boolean z) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("Set Torch mode : ");
        sb.append(this.mCameraId);
        sb.append(", onChanged enabled : ");
        sb.append(z);
        mALogger.mo11957d(sb.toString());
        if (!z) {
            FlashOnChopUtils.sendToForeground(false);
            LOGGER.mo11957d("setTorchMode: Send To Background");
        }
    }

    public static FlashlightAccess create(CameraManager cameraManager, String str, boolean z, FlashlightAccess flashlightAccess) throws AlgorithmNotDefinedException {
        FlashlightAccess flashlightAccess2;
        FlashOnChopAlgorithmUsedType algorithmUsed = FlashOnChopAlgorithmUtil.getAlgorithmUsed(cameraManager, str, z);
        if (notChangedFlashlightController(algorithmUsed, flashlightAccess) || notChangedCameraManager(algorithmUsed, flashlightAccess)) {
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("FlashlightAccess create - use same instance, algorithm not changed: ");
            sb.append(algorithmUsed);
            mALogger.mo11957d(sb.toString());
            return flashlightAccess;
        }
        switch (algorithmUsed) {
            case FOC_ALGO_FLASHLIGHTCONTROLLER:
                flashlightAccess2 = new FlashlightAccessFlashlightController();
                flashlightAccess2.mCameraManager = cameraManager;
                flashlightAccess2.mCameraId = str;
                break;
            case FOC_ALGO_CAMERA_MANAGER:
                flashlightAccess2 = new FlashlightAccessCameraManager();
                flashlightAccess2.mCameraManager = cameraManager;
                flashlightAccess2.mCameraId = str;
                break;
            default:
                throw new AlgorithmNotDefinedException();
        }
        MALogger mALogger2 = LOGGER;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("FlashlightAccess create - algorithm: ");
        sb2.append(algorithmUsed);
        mALogger2.mo11957d(sb2.toString());
        return flashlightAccess2;
    }

    private static boolean notChangedFlashlightController(FlashOnChopAlgorithmUsedType flashOnChopAlgorithmUsedType, FlashlightAccess flashlightAccess) {
        return (flashlightAccess instanceof FlashlightAccessFlashlightController) && flashOnChopAlgorithmUsedType == FlashOnChopAlgorithmUsedType.FOC_ALGO_FLASHLIGHTCONTROLLER;
    }

    private static boolean notChangedCameraManager(FlashOnChopAlgorithmUsedType flashOnChopAlgorithmUsedType, FlashlightAccess flashlightAccess) {
        return (flashlightAccess instanceof FlashlightAccessCameraManager) && flashOnChopAlgorithmUsedType == FlashOnChopAlgorithmUsedType.FOC_ALGO_CAMERA_MANAGER;
    }
}
