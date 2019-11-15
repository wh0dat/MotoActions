package com.motorola.actions.discovery.fdn.p007qc;

import android.content.Intent;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.discovery.fdn.BaseDiscoveryNotification;
import com.motorola.actions.discovery.fdn.FDNManager;
import com.motorola.actions.p010qc.FeatureManager;
import com.motorola.actions.p010qc.QuickCaptureConfig;
import com.motorola.actions.utils.MALogger;

/* renamed from: com.motorola.actions.discovery.fdn.qc.QuickCaptureFDNManager */
public class QuickCaptureFDNManager extends FDNManager {
    public static final String ACTIONS_RESET_STOP_COUNT = "com.motorola.actions.camera.RESET_STOP_COUNT";
    private static final MALogger LOGGER = new MALogger(QuickCaptureFDNManager.class);
    private static final String QUICK_CAPTURE_DISCOVERY_CANCEL = "quick_capture_discovery_cancel";
    private static final String QUICK_CAPTURE_DISCOVERY_VISIBLE = "quick_capture_discovery_visible";
    private CameraIntentReceiver mCameraIntentReceiver;

    /* access modifiers changed from: protected */
    public String getCancelId() {
        return QUICK_CAPTURE_DISCOVERY_CANCEL;
    }

    /* access modifiers changed from: protected */
    public String getVisibleId() {
        return QUICK_CAPTURE_DISCOVERY_VISIBLE;
    }

    public boolean registerTriggerReceiver() {
        if (!QuickCaptureConfig.isSupported() || getDiscoveryCancel() || this.mCameraIntentReceiver != null) {
            return false;
        }
        this.mCameraIntentReceiver = new CameraIntentReceiver();
        this.mCameraIntentReceiver.register();
        return true;
    }

    public void unregisterTriggerReceiver() {
        if (this.mCameraIntentReceiver != null) {
            this.mCameraIntentReceiver.unregister();
            this.mCameraIntentReceiver = null;
        }
    }

    public BaseDiscoveryNotification createNotification() {
        return new QuickCaptureDiscoveryNotification(getDiscoveryId());
    }

    public FeatureKey getDiscoveryId() {
        return FeatureKey.QUICK_CAPTURE;
    }

    public void onInvalidTrigger() {
        super.onInvalidTrigger();
        LOGGER.mo11957d("resetCameraCount");
        FeatureManager.sendIntentToCamera(new Intent(ACTIONS_RESET_STOP_COUNT));
    }
}
