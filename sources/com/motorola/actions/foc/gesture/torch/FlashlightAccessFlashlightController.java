package com.motorola.actions.foc.gesture.torch;

import android.os.Handler;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.foc.gesture.FlashlightController;
import com.motorola.actions.foc.gesture.util.FlashOnChopUtils;
import com.motorola.actions.foc.instrumentation.FlashOnChopInstrumentation;
import com.motorola.actions.utils.MALogger;

public class FlashlightAccessFlashlightController extends FlashlightAccess {
    private static final MALogger LOGGER = new MALogger(FlashlightAccessFlashlightController.class);
    private FlashlightController mFlashlightController;

    public boolean setTorch(boolean z, long j) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("setTorch - START - enabled: ");
        sb.append(z);
        mALogger.mo11957d(sb.toString());
        boolean z2 = false;
        if (super.setTorch(z, j)) {
            if (ActionsApplication.getAppContext().checkSelfPermission("android.permission.CAMERA") != 0) {
                FlashOnChopUtils.sendToForeground(false);
                LOGGER.mo11957d("setTorchMode: No camera permission. Send To Background");
            }
            this.mFlashlightController.setFlashlight(z);
            MALogger mALogger2 = LOGGER;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Set Torch mode : Using FlashlightController to ");
            sb2.append(z);
            mALogger2.mo11957d(sb2.toString());
            onPostTorch(z);
            z2 = true;
        }
        if (!z2 || !z) {
            LOGGER.mo11957d("recordFlashlightOffEvents");
            FlashOnChopInstrumentation.recordFlashlightOffEvents();
        } else {
            LOGGER.mo11957d("recordFlashlightOnEvents - From ChopChop");
            FlashOnChopInstrumentation.recordFlashlightOnEvents(true);
        }
        MALogger mALogger3 = LOGGER;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("setTorch - END - isSetTorchSuccess: ");
        sb3.append(z2);
        mALogger3.mo11957d(sb3.toString());
        return z2;
    }

    public void setup() {
        if (this.mFlashlightController == null) {
            this.mFlashlightController = new FlashlightController(this.mCameraManager, new Handler(), this.mCameraId, ActionsApplication.getAppContext());
        }
    }
}
