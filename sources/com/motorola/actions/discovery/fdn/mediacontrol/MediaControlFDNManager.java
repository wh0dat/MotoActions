package com.motorola.actions.discovery.fdn.mediacontrol;

import com.motorola.actions.FeatureKey;
import com.motorola.actions.discovery.fdn.BaseDiscoveryNotification;
import com.motorola.actions.discovery.fdn.FDNManager;
import com.motorola.actions.mediacontrol.MediaControlService;
import com.motorola.actions.utils.MALogger;

public class MediaControlFDNManager extends FDNManager {
    private static final MALogger LOGGER = new MALogger(MediaControlFDNManager.class);
    private static final String MEDIA_CONTROL_DISCOVERY_CANCEL = "media_control_discovery_cancel";
    private static final String MEDIA_CONTROL_DISCOVERY_VISIBLE = "media_control_discovery_visible";
    private HeadsetPlugReceiver mHeadsetPlugReceiver;

    /* access modifiers changed from: protected */
    public String getCancelId() {
        return MEDIA_CONTROL_DISCOVERY_CANCEL;
    }

    /* access modifiers changed from: protected */
    public String getVisibleId() {
        return MEDIA_CONTROL_DISCOVERY_VISIBLE;
    }

    public boolean registerTriggerReceiver() {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("registerTriggerReceiver ");
        sb.append(this.mHeadsetPlugReceiver);
        mALogger.mo11957d(sb.toString());
        if (!MediaControlService.isFeatureSupported() || getDiscoveryCancel() || this.mHeadsetPlugReceiver != null) {
            return false;
        }
        this.mHeadsetPlugReceiver = new HeadsetPlugReceiver();
        this.mHeadsetPlugReceiver.register();
        return true;
    }

    public void unregisterTriggerReceiver() {
        if (this.mHeadsetPlugReceiver != null) {
            this.mHeadsetPlugReceiver.unregister();
            this.mHeadsetPlugReceiver = null;
        }
    }

    public BaseDiscoveryNotification createNotification() {
        return new MediaControlDiscoveryNotification(getDiscoveryId());
    }

    public FeatureKey getDiscoveryId() {
        return FeatureKey.MEDIA_CONTROL;
    }
}
