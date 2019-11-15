package com.motorola.actions.foc.gesture.event;

import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraManager.TorchCallback;
import android.os.Handler;
import android.text.TextUtils;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.discovery.DiscoveryManager;
import com.motorola.actions.foc.gesture.FlashOnChopGestureManager;
import com.motorola.actions.foc.gesture.FlashlightController;
import com.motorola.actions.foc.gesture.event.listener.TorchEventListener;
import com.motorola.actions.foc.gesture.util.FlashOnChopUtils;
import com.motorola.actions.foc.instrumentation.FlashOnChopInstrumentation;
import com.motorola.actions.utils.MALogger;

public class TorchEvent implements IEventSource {
    public static final String ACTION_TUTORIAL_TORCH_OFF = "com.motorola.ACTION_TUTORIAL_TORCH_OFF";
    public static final String ACTION_TUTORIAL_TORCH_ON = "com.motorola.ACTION_TUTORIAL_TORCH_ON";
    public static final String EXTRA_FLASHLIGHT_FROM_ACTIONS = "EXTRA_FLASHLIGHT_FROM_ACTIONS";
    /* access modifiers changed from: private */
    public static final MALogger LOGGER = new MALogger(TorchEvent.class);
    /* access modifiers changed from: private */
    public final String mCameraId;
    private final CameraManager mCameraManager;
    private final TorchCallback mTorchCallback = new TorchCallback() {
        private boolean mIsFirstCheck = true;

        public void onTorchModeChanged(String str, boolean z) {
            boolean z2;
            super.onTorchModeChanged(str, z);
            MALogger access$000 = TorchEvent.LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("TorchCallback camera : ");
            sb.append(str);
            sb.append(", onChanged enabled : ");
            sb.append(z);
            access$000.mo11957d(sb.toString());
            if (str.equals(TorchEvent.this.mCameraId)) {
                TorchEvent.LOGGER.mo11957d("TorchCallback is from the cameraId used");
                if (FlashOnChopGestureManager.isFlashlightOn() != z) {
                    if (FlashOnChopGestureManager.isFlashlightOn()) {
                        FlashOnChopUtils.sendToForeground(false);
                        if (TorchEvent.this.mTorchEventListener != null) {
                            TorchEvent.this.mTorchEventListener.onFlashLightOffNotFromFOC();
                        }
                    }
                    if (z) {
                        FlashOnChopInstrumentation.recordFlashlightOnEvents(false);
                    } else {
                        DiscoveryManager.getInstance().onFDNEvent(FeatureKey.FLASH_ON_CHOP);
                    }
                    z2 = false;
                } else {
                    z2 = true;
                }
                FlashOnChopGestureManager.setFlashlightOn(z);
                if (this.mIsFirstCheck) {
                    TorchEvent.LOGGER.mo11957d("TorchCallback mIsFirstCheck = true, skipping tutorial message");
                    this.mIsFirstCheck = false;
                    return;
                }
                FlashOnChopUtils.sendMsgTorchModeTutorial(z, z2);
            }
        }

        public void onTorchModeUnavailable(String str) {
            super.onTorchModeUnavailable(str);
            MALogger access$000 = TorchEvent.LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("onTorchModeUnavailable on camera ");
            sb.append(str);
            access$000.mo11957d(sb.toString());
            if (!TextUtils.isEmpty(str) && FlashOnChopGestureManager.isFlashlightOn() && str.equals(TorchEvent.this.mCameraId) && !FlashlightController.getFlashlightControllerHasCamera()) {
                FlashOnChopUtils.sendToForeground(false);
                FlashOnChopGestureManager.setFlashlightOn(false);
                if (TorchEvent.this.mTorchEventListener != null) {
                    TorchEvent.this.mTorchEventListener.onFlashLightOffNotFromFOC();
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public final TorchEventListener mTorchEventListener;

    public TorchEvent(CameraManager cameraManager, String str, TorchEventListener torchEventListener) {
        this.mCameraManager = cameraManager;
        this.mCameraId = str;
        this.mTorchEventListener = torchEventListener;
        start();
    }

    public void start() {
        if (this.mCameraManager != null) {
            this.mCameraManager.registerTorchCallback(this.mTorchCallback, new Handler());
        }
    }

    public void stop() {
        if (this.mCameraManager != null) {
            this.mCameraManager.unregisterTorchCallback(this.mTorchCallback);
        }
    }
}
