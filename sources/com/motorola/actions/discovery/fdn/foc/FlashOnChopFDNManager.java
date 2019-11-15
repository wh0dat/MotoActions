package com.motorola.actions.discovery.fdn.foc;

import com.motorola.actions.ActionsApplication;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.discovery.fdn.BaseDiscoveryNotification;
import com.motorola.actions.discovery.fdn.FDNManager;
import com.motorola.actions.foc.gesture.service.FlashOnChopService;
import java.util.concurrent.TimeUnit;

public class FlashOnChopFDNManager extends FDNManager {
    private static final String CHOPTWICE_DISCOVERY_CANCEL = "choptwice_discovery_cancel";
    private static final String CHOPTWICE_DISCOVERY_VISIBLE = "choptwice_discovery_visible";

    /* access modifiers changed from: protected */
    public String getCancelId() {
        return CHOPTWICE_DISCOVERY_CANCEL;
    }

    /* access modifiers changed from: protected */
    public String getVisibleId() {
        return CHOPTWICE_DISCOVERY_VISIBLE;
    }

    public void unregisterTriggerReceiver() {
    }

    public boolean registerTriggerReceiver() {
        return FlashOnChopService.isFeatureSupported(ActionsApplication.getAppContext()) && !getDiscoveryCancel();
    }

    public BaseDiscoveryNotification createNotification() {
        return new FlashOnChopDiscoveryNotification(getDiscoveryId());
    }

    public FeatureKey getDiscoveryId() {
        return FeatureKey.FLASH_ON_CHOP;
    }

    public long getNotificationDelay() {
        return TimeUnit.SECONDS.toMillis(5);
    }
}
