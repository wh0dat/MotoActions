package com.motorola.actions.discovery.highlight.onenav;

import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.discovery.highlight.FeatureHighlightKey;
import com.motorola.actions.discovery.highlight.FeatureHighlightManager;
import com.motorola.actions.onenav.OneNavHelper;
import com.motorola.actions.reflect.MotorolaSettings;
import com.motorola.actions.utils.MALogger;

public class SoftOneNavHighlightManager extends FeatureHighlightManager {
    private static final MALogger LOGGER = new MALogger(SoftOneNavHighlightManager.class);
    private static final String SOFTONENAV_HIGHLIGHT_CANCEL = "softonenav_discovery_cancel";
    private static final String SOFTONENAV_HIGHLIGHT_READY = "softonenav_discovery_ready";
    private UserLockReceiver mUserLockReceiver;

    /* access modifiers changed from: protected */
    public String getCancelId() {
        return SOFTONENAV_HIGHLIGHT_CANCEL;
    }

    /* access modifiers changed from: protected */
    public String getReadyId() {
        return SOFTONENAV_HIGHLIGHT_READY;
    }

    public boolean registerTriggerReceiver() {
        if (!OneNavHelper.isOneNavPresent() || OneNavHelper.isOneNavEnabled() || !OneNavHelper.isSoftOneNav() || getHighlightCancel() || this.mUserLockReceiver != null) {
            return false;
        }
        LOGGER.mo11957d("registerTriggerReceiver");
        this.mUserLockReceiver = new UserLockReceiver();
        this.mUserLockReceiver.register();
        return true;
    }

    public void unregisterTriggerReceiver() {
        if (this.mUserLockReceiver != null) {
            LOGGER.mo11957d("unregisterTriggerReceiver");
            this.mUserLockReceiver.unregister();
            this.mUserLockReceiver = null;
        }
    }

    /* access modifiers changed from: protected */
    public FeatureHighlightKey getHighlightId() {
        return FeatureHighlightKey.SOFTONENAV;
    }

    public void onEvent() {
        LOGGER.mo11957d("onEvent");
        super.onEvent();
        MotorolaSettings.setSoftOneNavDiscovery(1);
    }

    public void cancelHighlight() {
        super.cancelHighlight();
        LOGGER.mo11957d("cancelHighlight");
        MotorolaSettings.setSoftOneNavDiscovery(0);
    }

    public void resetHighlight() {
        super.resetHighlight();
        SharedPreferenceManager.remove(UserLockReceiver.KEY_SOFT_ONE_NAV_UNLOCK);
        MotorolaSettings.setSoftOneNavDiscovery(0);
    }
}
