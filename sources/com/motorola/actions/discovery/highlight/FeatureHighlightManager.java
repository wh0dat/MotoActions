package com.motorola.actions.discovery.highlight;

import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.utils.MALogger;

public abstract class FeatureHighlightManager {
    private static final MALogger LOGGER = new MALogger(FeatureHighlightManager.class);

    /* access modifiers changed from: protected */
    public abstract String getCancelId();

    /* access modifiers changed from: protected */
    public abstract FeatureHighlightKey getHighlightId();

    /* access modifiers changed from: protected */
    public abstract String getReadyId();

    public abstract boolean registerTriggerReceiver();

    public abstract void unregisterTriggerReceiver();

    public void resetHighlight() {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("resetHighlight for ");
        sb.append(getCancelId());
        mALogger.mo11957d(sb.toString());
        SharedPreferenceManager.putBoolean(getCancelId(), false);
    }

    /* access modifiers changed from: protected */
    public final boolean getHighlightCancel() {
        return SharedPreferenceManager.getBoolean(getCancelId(), false);
    }

    private void setHighlightCancel() {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("setHighlightCancel for '");
        sb.append(getCancelId());
        sb.append("'");
        mALogger.mo11957d(sb.toString());
        SharedPreferenceManager.putBoolean(getCancelId(), true);
    }

    public void cancelHighlight() {
        unregisterTriggerReceiver();
        if (!getHighlightCancel()) {
            setHighlightCancel();
            setHighlightReady(false);
        }
    }

    public void onEvent() {
        if (!getHighlightCancel()) {
            setHighlightCancel();
            unregisterTriggerReceiver();
        }
    }

    public void setHighlightReady(boolean z) {
        SharedPreferenceManager.putBoolean(getReadyId(), z);
    }

    public boolean isHighlightReady() {
        return SharedPreferenceManager.getBoolean(getReadyId(), false);
    }
}
