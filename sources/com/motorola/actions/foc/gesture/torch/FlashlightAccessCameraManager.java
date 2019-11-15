package com.motorola.actions.foc.gesture.torch;

import com.motorola.actions.utils.MALogger;

public class FlashlightAccessCameraManager extends FlashlightAccess {
    private static final MALogger LOGGER = new MALogger(FlashlightAccessCameraManager.class);

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0070 A[ADDED_TO_REGION] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean setTorch(boolean r4, long r5) {
        /*
            r3 = this;
            com.motorola.actions.utils.MALogger r0 = LOGGER
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "setTorch - START - enabled: "
            r1.append(r2)
            r1.append(r4)
            java.lang.String r1 = r1.toString()
            r0.mo11957d(r1)
            boolean r5 = super.setTorch(r4, r5)
            r6 = 1
            r0 = 0
            if (r5 == 0) goto L_0x006e
            android.hardware.camera2.CameraManager r5 = r3.mCameraManager     // Catch:{ CameraAccessException -> 0x0043 }
            java.lang.String r1 = r3.mCameraId     // Catch:{ CameraAccessException -> 0x0043 }
            r5.setTorchMode(r1, r4)     // Catch:{ CameraAccessException -> 0x0043 }
            com.motorola.actions.utils.MALogger r5 = LOGGER     // Catch:{ CameraAccessException -> 0x0043 }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ CameraAccessException -> 0x0043 }
            r1.<init>()     // Catch:{ CameraAccessException -> 0x0043 }
            java.lang.String r2 = "Set Torch mode : Using CameraManager to "
            r1.append(r2)     // Catch:{ CameraAccessException -> 0x0043 }
            r1.append(r4)     // Catch:{ CameraAccessException -> 0x0043 }
            java.lang.String r1 = r1.toString()     // Catch:{ CameraAccessException -> 0x0043 }
            r5.mo11957d(r1)     // Catch:{ CameraAccessException -> 0x0043 }
            r3.onPostTorch(r4)     // Catch:{ CameraAccessException -> 0x0040 }
            r0 = r6
            goto L_0x006e
        L_0x0040:
            r3 = move-exception
            r0 = r6
            goto L_0x0044
        L_0x0043:
            r3 = move-exception
        L_0x0044:
            com.motorola.actions.utils.MALogger r5 = LOGGER
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Camera API Exception of "
            r1.append(r2)
            java.lang.Class r2 = r3.getClass()
            java.lang.String r2 = r2.getName()
            r1.append(r2)
            java.lang.String r2 = " : "
            r1.append(r2)
            java.lang.String r3 = r3.getMessage()
            r1.append(r3)
            java.lang.String r3 = r1.toString()
            r5.mo11959e(r3)
        L_0x006e:
            if (r0 == 0) goto L_0x007d
            if (r4 == 0) goto L_0x007d
            com.motorola.actions.utils.MALogger r3 = LOGGER
            java.lang.String r4 = "recordFlashlightOnEvents - From ChopChop"
            r3.mo11957d(r4)
            com.motorola.actions.foc.instrumentation.FlashOnChopInstrumentation.recordFlashlightOnEvents(r6)
            goto L_0x0087
        L_0x007d:
            com.motorola.actions.utils.MALogger r3 = LOGGER
            java.lang.String r4 = "recordFlashlightOffEvents"
            r3.mo11957d(r4)
            com.motorola.actions.foc.instrumentation.FlashOnChopInstrumentation.recordFlashlightOffEvents()
        L_0x0087:
            com.motorola.actions.utils.MALogger r3 = LOGGER
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "setTorch - END - isSetTorchSuccess: "
            r4.append(r5)
            r4.append(r0)
            java.lang.String r4 = r4.toString()
            r3.mo11957d(r4)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.motorola.actions.foc.gesture.torch.FlashlightAccessCameraManager.setTorch(boolean, long):boolean");
    }
}
