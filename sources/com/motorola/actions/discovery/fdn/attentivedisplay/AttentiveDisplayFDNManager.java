package com.motorola.actions.discovery.fdn.attentivedisplay;

import com.motorola.actions.ActionsApplication;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.attentivedisplay.AttentiveDisplayService;
import com.motorola.actions.discovery.DiscoveryManager;
import com.motorola.actions.discovery.fdn.BaseDiscoveryNotification;
import com.motorola.actions.discovery.fdn.FDNManager;
import com.motorola.actions.p013ui.settings.AttentiveDisplaySettingsFragment;
import com.motorola.actions.utils.MALogger;

public class AttentiveDisplayFDNManager extends FDNManager implements AttentiveDisplayDiscoveryEventsCallback {
    private static final String ATTENTIVE_DISPLAY_DISCOVERY_CANCEL = "attentive_display_discovery_cancel";
    private static final String ATTENTIVE_DISPLAY_DISCOVERY_VISIBLE = "attentive_display_discovery_visible";
    private static final MALogger LOGGER = new MALogger(AttentiveDisplayFDNManager.class);
    public static final int TRIGGER_SCREEN_DIM_ID = 1;
    public static final int TRIGGER_SETTINGS_ID = 2;
    private DiscoveryConditionsListener mDiscoveryConditionsListener;

    /* access modifiers changed from: protected */
    public String getCancelId() {
        return ATTENTIVE_DISPLAY_DISCOVERY_CANCEL;
    }

    /* access modifiers changed from: protected */
    public String getVisibleId() {
        return ATTENTIVE_DISPLAY_DISCOVERY_VISIBLE;
    }

    public void run(int i) {
        DiscoveryManager.getInstance().onFDNEvent(FeatureKey.ATTENTIVE_DISPLAY, i);
    }

    public boolean registerTriggerReceiver() {
        if (!AttentiveDisplayService.isFeatureSupported(ActionsApplication.getAppContext()) || AttentiveDisplaySettingsFragment.isStayOnEnabled() || getDiscoveryCancel()) {
            return false;
        }
        this.mDiscoveryConditionsListener = new DiscoveryConditionsListener(this);
        this.mDiscoveryConditionsListener.register();
        return true;
    }

    public void unregisterTriggerReceiver() {
        if (this.mDiscoveryConditionsListener != null) {
            this.mDiscoveryConditionsListener.unregister();
            this.mDiscoveryConditionsListener = null;
        }
    }

    public BaseDiscoveryNotification createNotification() {
        return new AttentiveDisplayDiscoveryNotification(getDiscoveryId());
    }

    public FeatureKey getDiscoveryId() {
        return FeatureKey.ATTENTIVE_DISPLAY;
    }

    public boolean shouldIgnoreFdnDaysCount(int i) {
        boolean z = i == 2;
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("shouldIgnoreFdnDaysCount: ");
        sb.append(z);
        sb.append(" - ");
        sb.append(i);
        mALogger.mo11957d(sb.toString());
        return z;
    }
}
