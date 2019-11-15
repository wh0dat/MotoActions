package com.motorola.actions.discovery.fdn.nightdisplay;

import com.motorola.actions.FeatureKey;
import com.motorola.actions.discovery.fdn.BaseDiscoveryNotification;
import com.motorola.actions.discovery.fdn.FDNManager;
import com.motorola.actions.nightdisplay.NightDisplayService;
import com.motorola.actions.sleepPattern.SleepPatternService;
import com.motorola.actions.utils.MALogger;

public class NightDisplayFDNManager extends FDNManager {
    private static final MALogger LOGGER = new MALogger(NightDisplayFDNManager.class);
    private static final String NIGHT_DISPLAY_DISCOVERY_CANCEL = "night_display_discovery_cancel";
    private static final String NIGHT_DISPLAY_DISCOVERY_VISIBLE = "night_display_discovery_visible";

    /* access modifiers changed from: protected */
    public String getCancelId() {
        return NIGHT_DISPLAY_DISCOVERY_CANCEL;
    }

    /* access modifiers changed from: protected */
    public String getVisibleId() {
        return NIGHT_DISPLAY_DISCOVERY_VISIBLE;
    }

    public boolean registerTriggerReceiver() {
        return SleepPatternService.isFeatureSupported() && !NightDisplayService.isServiceEnabled() && !getDiscoveryCancel();
    }

    public void unregisterTriggerReceiver() {
        LOGGER.mo11957d("unregisterTriggerReceiver");
    }

    public BaseDiscoveryNotification createNotification() {
        return new NightDisplayDiscoveryNotification(getDiscoveryId());
    }

    public FeatureKey getDiscoveryId() {
        return FeatureKey.NIGHT_DISPLAY;
    }
}
