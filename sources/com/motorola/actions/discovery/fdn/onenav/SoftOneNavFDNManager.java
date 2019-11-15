package com.motorola.actions.discovery.fdn.onenav;

import android.os.Handler;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.discovery.fdn.BaseDiscoveryNotification;
import com.motorola.actions.discovery.fdn.FDNManager;
import com.motorola.actions.onenav.OneNavHelper;
import com.motorola.actions.utils.MALogger;

public class SoftOneNavFDNManager extends FDNManager {
    private static final MALogger LOGGER = new MALogger(SoftOneNavFDNManager.class);
    private static final String ONENAV_DISCOVERY_CANCEL = "softonenav_discovery_cancel";
    private static final String ONENAV_DISCOVERY_VISIBLE = "softonenav_discovery_visible";
    private SoftOneNavDiscoveryObserver mSoftOneNavDiscoveryObserver;

    /* access modifiers changed from: protected */
    public String getCancelId() {
        return ONENAV_DISCOVERY_CANCEL;
    }

    /* access modifiers changed from: protected */
    public String getVisibleId() {
        return ONENAV_DISCOVERY_VISIBLE;
    }

    public boolean registerTriggerReceiver() {
        boolean z;
        if (!OneNavHelper.isOneNavPresent() || OneNavHelper.isOneNavEnabled() || !OneNavHelper.isSoftOneNav() || getDiscoveryCancel()) {
            z = false;
        } else {
            if (this.mSoftOneNavDiscoveryObserver == null) {
                this.mSoftOneNavDiscoveryObserver = new SoftOneNavDiscoveryObserver(new Handler());
            }
            this.mSoftOneNavDiscoveryObserver.observe();
            z = true;
        }
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("registerTriggerReceiver ");
        sb.append(z);
        mALogger.mo11957d(sb.toString());
        return z;
    }

    public void unregisterTriggerReceiver() {
        if (this.mSoftOneNavDiscoveryObserver != null) {
            LOGGER.mo11957d("unregisterTriggerReceiver");
            this.mSoftOneNavDiscoveryObserver.stop();
            this.mSoftOneNavDiscoveryObserver = null;
        }
    }

    public BaseDiscoveryNotification createNotification() {
        return new SoftOneNavDiscoveryNotification(getDiscoveryId());
    }

    public FeatureKey getDiscoveryId() {
        return FeatureKey.ONE_NAV;
    }
}
