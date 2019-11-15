package com.motorola.actions.discovery.fdn.ftm;

import android.os.Handler;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.discovery.fdn.BaseDiscoveryNotification;
import com.motorola.actions.discovery.fdn.FDNManager;
import com.motorola.actions.ftm.FlipToMuteService;
import com.motorola.actions.reflect.MotorolaSettings;
import com.motorola.actions.utils.MALogger;

public class FlipToDNDFDNManager extends FDNManager {
    private static final String FLIP_TO_DND_DISCOVERY_CANCEL = "flip_to_dnd_discovery_cancel";
    private static final String FLIP_TO_DND_DISCOVERY_VISIBLE = "flip_to_dnd_discovery_visible";
    private static final MALogger LOGGER = new MALogger(FlipToDNDFDNManager.class);
    private DoNotDisturbTurnOffObserver mDoNotDisturbObserver;

    /* access modifiers changed from: protected */
    public String getCancelId() {
        return FLIP_TO_DND_DISCOVERY_CANCEL;
    }

    /* access modifiers changed from: protected */
    public String getVisibleId() {
        return FLIP_TO_DND_DISCOVERY_VISIBLE;
    }

    public void resetFDN() {
        super.resetFDN();
        MotorolaSettings.setFlipToDNDFDNTurnedOffValue(0);
    }

    public boolean registerTriggerReceiver() {
        if (!FlipToMuteService.isFeatureSupported() || FlipToMuteService.isServiceEnabled() || getDiscoveryCancel() || this.mDoNotDisturbObserver != null) {
            return false;
        }
        this.mDoNotDisturbObserver = new DoNotDisturbTurnOffObserver(new Handler());
        this.mDoNotDisturbObserver.observe();
        return true;
    }

    public void unregisterTriggerReceiver() {
        if (this.mDoNotDisturbObserver != null) {
            this.mDoNotDisturbObserver.stop();
            this.mDoNotDisturbObserver = null;
        }
    }

    public BaseDiscoveryNotification createNotification() {
        return new FlipToDNDDiscoveryNotification(getDiscoveryId());
    }

    public FeatureKey getDiscoveryId() {
        return FeatureKey.FLIP_TO_DND;
    }

    public void onInvalidTrigger() {
        super.onInvalidTrigger();
        boolean z = true;
        if (MotorolaSettings.getFlipToDNDFDNTurnedOffValue(0) != 1) {
            z = false;
        }
        if (z) {
            LOGGER.mo11957d("DND trigger was lost. Needs to reset DND flag");
            MotorolaSettings.setFlipToDNDFDNTurnedOffValue(0);
        }
    }
}
