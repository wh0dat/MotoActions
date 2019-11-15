package com.motorola.actions.discovery.fdn.nightdisplay.sleeppattern;

import com.motorola.actions.FeatureKey;
import com.motorola.actions.discovery.fdn.BaseDiscoveryNotification;
import com.motorola.actions.discovery.fdn.FDNManager;
import com.motorola.actions.nightdisplay.NightDisplayService;
import com.motorola.actions.nightdisplay.common.Persistence;
import com.motorola.actions.sleepPattern.SleepPatternService;
import com.motorola.actions.utils.MALogger;

public class SleepPatternFDNManager extends FDNManager {
    private static final String DISCOVERY_CANCEL = "sleep_pattern_discovery_cancel";
    private static final String DISCOVERY_VISIBLE = "sleep_pattern_discovery_visible";
    private static final MALogger LOGGER = new MALogger(SleepPatternFDNManager.class);

    /* access modifiers changed from: protected */
    public String getCancelId() {
        return DISCOVERY_CANCEL;
    }

    /* access modifiers changed from: protected */
    public String getVisibleId() {
        return DISCOVERY_VISIBLE;
    }

    public boolean registerTriggerReceiver() {
        if (!SleepPatternService.isFeatureSupported() || Persistence.getMode(SleepPatternService.getDefaultState()) != 1 || !NightDisplayService.isServiceEnabled() || getDiscoveryCancel()) {
            return false;
        }
        return true;
    }

    public void unregisterTriggerReceiver() {
        LOGGER.mo11957d("unregisterTriggerReceiver");
    }

    public BaseDiscoveryNotification createNotification() {
        return new SleepPatternDiscoveryNotification(getDiscoveryId());
    }

    public FeatureKey getDiscoveryId() {
        return FeatureKey.SLEEP_PATTERN;
    }

    public boolean shouldIgnoreFdnDaysCount(int i) {
        LOGGER.mo11957d("shouldIgnoreFdnDaysCount: true");
        return true;
    }
}
