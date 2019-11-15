package com.motorola.actions.utils;

public class MotoCameraUtils {
    public static final String CAMERA_ONE_PACKAGE = "com.motorola.cameraone";
    public static final String CAMERA_TWO_PACKAGE = "com.motorola.camera2";

    public static boolean isMotoCameraEnabled() {
        return PackageManagerUtils.isAppEnabled(CAMERA_TWO_PACKAGE) || PackageManagerUtils.isAppEnabled(CAMERA_ONE_PACKAGE);
    }
}
