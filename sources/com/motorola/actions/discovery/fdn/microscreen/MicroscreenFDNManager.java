package com.motorola.actions.discovery.fdn.microscreen;

import android.os.Handler;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.discovery.fdn.BaseDiscoveryNotification;
import com.motorola.actions.discovery.fdn.FDNManager;
import com.motorola.actions.microScreen.MicroScreenService;
import com.motorola.actions.utils.MALogger;
import java.util.concurrent.TimeUnit;

public class MicroscreenFDNManager extends FDNManager {
    private static final MALogger LOGGER = new MALogger(MicroscreenFDNManager.class);
    private static final String MICROSCREEN_DISCOVERY_CANCEL = "microscreen_discovery_cancel";
    private static final String MICROSCREEN_DISCOVERY_VISIBLE = "microscreen_discovery_visible";
    private MicroscreenDiscoveryConditionsObserver mMicroscreenDiscoveryConditionsObserver;

    /* access modifiers changed from: protected */
    public String getCancelId() {
        return MICROSCREEN_DISCOVERY_CANCEL;
    }

    /* access modifiers changed from: protected */
    public String getVisibleId() {
        return MICROSCREEN_DISCOVERY_VISIBLE;
    }

    public boolean registerTriggerReceiver() {
        if (!MicroScreenService.isFeatureSupported() || MicroScreenService.isServiceEnabled() || getDiscoveryCancel() || this.mMicroscreenDiscoveryConditionsObserver != null) {
            return false;
        }
        LOGGER.mo11957d("registerTriggerReceiver");
        this.mMicroscreenDiscoveryConditionsObserver = new MicroscreenDiscoveryConditionsObserver(new Handler());
        this.mMicroscreenDiscoveryConditionsObserver.register();
        return true;
    }

    public void unregisterTriggerReceiver() {
        if (this.mMicroscreenDiscoveryConditionsObserver != null) {
            LOGGER.mo11957d("unregisterTriggerReceiver");
            this.mMicroscreenDiscoveryConditionsObserver.unregister();
            this.mMicroscreenDiscoveryConditionsObserver = null;
        }
    }

    public BaseDiscoveryNotification createNotification() {
        return new MicroscreenDiscoveryNotification(getDiscoveryId());
    }

    public FeatureKey getDiscoveryId() {
        return FeatureKey.MICROSCREEN;
    }

    public long getNotificationDelay() {
        return TimeUnit.SECONDS.toMillis(3);
    }
}
