package com.motorola.actions.discovery.fdn;

import android.content.Intent;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.discovery.fdn.instrumentation.DiscoveryInstrumentation;
import com.motorola.actions.p013ui.settings.SettingsDetailActivity;
import com.motorola.actions.utils.MALogger;

public class FDNSession {
    public static final String EXTRA_IS_FROM_FDN = "isFromFDN";
    private static final MALogger LOGGER = new MALogger(FDNSession.class);
    private boolean mFDNSessionActive;
    private FeatureKey mFeatureKey;

    public void enterSession(Intent intent) {
        if (intent != null && intent.getBooleanExtra(EXTRA_IS_FROM_FDN, false)) {
            this.mFDNSessionActive = true;
            this.mFeatureKey = FeatureKey.getFeatureKey(intent.getIntExtra(SettingsDetailActivity.KEY_SETTINGS, FeatureKey.NOT_VALID.ordinal()));
        }
    }

    public boolean isActive() {
        return this.mFDNSessionActive;
    }

    public void setFDNExtra(Intent intent) {
        if (this.mFDNSessionActive && intent != null) {
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("Forwarding FDN session to +'");
            sb.append(intent);
            sb.append("'");
            mALogger.mo11957d(sb.toString());
            intent.putExtra(EXTRA_IS_FROM_FDN, true);
            intent.putExtra(SettingsDetailActivity.KEY_SETTINGS, this.mFeatureKey.ordinal());
        }
    }

    public void recordChange(FeatureKey featureKey) {
        if (this.mFDNSessionActive && featureKey != FeatureKey.NOT_VALID && featureKey == this.mFeatureKey) {
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("recordChange(): submitting change for ");
            sb.append(featureKey);
            mALogger.mo11957d(sb.toString());
            DiscoveryInstrumentation.recordChangeFDN(featureKey.ordinal());
        }
    }
}
