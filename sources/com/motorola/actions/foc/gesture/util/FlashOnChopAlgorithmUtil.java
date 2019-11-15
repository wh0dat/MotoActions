package com.motorola.actions.foc.gesture.util;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.utils.MALogger;

public class FlashOnChopAlgorithmUtil {
    public static final String KEY_TEST_USE_ONLY_FOC_ALGO = "TEST_USE_FOC_ALGO";
    public static final String KEY_TEST_USE_ONLY_FOC_ALGO_CAMERA_MANAGER = "CAMERA_MANAGER";
    public static final String KEY_TEST_USE_ONLY_FOC_ALGO_FLASHLIGHTCONTROLLER = "FLASHLIGHTCONTROLLER";
    public static final String KEY_TEST_USE_ONLY_FOC_ALGO_UNKNOWN = "UNKNOWN";
    private static final MALogger LOGGER = new MALogger(FlashOnChopAlgorithmUtil.class);

    public enum FlashOnChopAlgorithmUsedType {
        FOC_ALGO_UNKNOWN,
        FOC_ALGO_CAMERA_MANAGER,
        FOC_ALGO_FLASHLIGHTCONTROLLER
    }

    public static FlashOnChopAlgorithmUsedType getAlgorithmUsed(CameraManager cameraManager, String str, boolean z) {
        FlashOnChopAlgorithmUsedType flashOnChopAlgorithmUsedType = FlashOnChopAlgorithmUsedType.FOC_ALGO_UNKNOWN;
        if (SharedPreferenceManager.contains(KEY_TEST_USE_ONLY_FOC_ALGO)) {
            LOGGER.mo11957d("Shared preference has KEY_TEST_USE_ONLY_FOC_ALGO. Remove if it is not for testing!");
            String string = SharedPreferenceManager.getString(KEY_TEST_USE_ONLY_FOC_ALGO, KEY_TEST_USE_ONLY_FOC_ALGO_UNKNOWN);
            if (string.equals(KEY_TEST_USE_ONLY_FOC_ALGO_FLASHLIGHTCONTROLLER)) {
                LOGGER.mo11957d("Test algorithm selected: FlashlightController");
                flashOnChopAlgorithmUsedType = FlashOnChopAlgorithmUsedType.FOC_ALGO_FLASHLIGHTCONTROLLER;
            } else if (string.equals(KEY_TEST_USE_ONLY_FOC_ALGO_CAMERA_MANAGER)) {
                LOGGER.mo11957d("Test algorithm selected: CameraManager");
                flashOnChopAlgorithmUsedType = FlashOnChopAlgorithmUsedType.FOC_ALGO_CAMERA_MANAGER;
            } else {
                LOGGER.mo11957d("Invalid value of KEY_TEST_USE_ONLY_FOC_ALGO. It will be removed from SharedPreferences");
                SharedPreferenceManager.remove(KEY_TEST_USE_ONLY_FOC_ALGO);
            }
        }
        if (!FlashOnChopAlgorithmUsedType.FOC_ALGO_UNKNOWN.equals(flashOnChopAlgorithmUsedType)) {
            return flashOnChopAlgorithmUsedType;
        }
        try {
            cameraManager.setTorchMode(str, z);
            return FlashOnChopAlgorithmUsedType.FOC_ALGO_CAMERA_MANAGER;
        } catch (CameraAccessException e) {
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("setTorchMode fails in the first time. Uses now FlashlightController. Reason: ");
            sb.append(e.getReason());
            mALogger.mo11959e(sb.toString());
            return FlashOnChopAlgorithmUsedType.FOC_ALGO_FLASHLIGHTCONTROLLER;
        }
    }
}
