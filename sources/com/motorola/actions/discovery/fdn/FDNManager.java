package com.motorola.actions.discovery.fdn;

import android.os.Handler;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.discovery.DiscoveryManager;
import com.motorola.actions.utils.DemoModeUtils;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.MotoAppUtils;

public abstract class FDNManager {
    private static final MALogger LOGGER = new MALogger(FDNManager.class);
    private BaseDiscoveryNotification mNotification;

    public abstract BaseDiscoveryNotification createNotification();

    /* access modifiers changed from: protected */
    public abstract String getCancelId();

    public abstract FeatureKey getDiscoveryId();

    /* access modifiers changed from: protected */
    public long getNotificationDelay() {
        return 0;
    }

    /* access modifiers changed from: protected */
    public abstract String getVisibleId();

    public void onInvalidTrigger() {
    }

    public abstract boolean registerTriggerReceiver();

    public boolean shouldIgnoreFdnDaysCount(int i) {
        return false;
    }

    public abstract void unregisterTriggerReceiver();

    private void registerNotificationReceiver() {
        DiscoveryManager.getInstance().registerReceiver(getDiscoveryId());
    }

    public void resetFDN() {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("resetFDN for '");
        sb.append(getCancelId());
        sb.append("'");
        mALogger.mo11957d(sb.toString());
        SharedPreferenceManager.putBoolean(getCancelId(), false);
    }

    /* access modifiers changed from: protected */
    public final boolean getDiscoveryCancel() {
        return SharedPreferenceManager.getBoolean(getCancelId(), false);
    }

    private void setDiscoveryCancel() {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("setDiscoveryCancel for '");
        sb.append(getCancelId());
        sb.append("'");
        mALogger.mo11957d(sb.toString());
        SharedPreferenceManager.putBoolean(getCancelId(), true);
    }

    public void cancelFDN() {
        unregisterTriggerReceiver();
        if (!getDiscoveryCancel()) {
            setDiscoveryCancel();
        }
        dismissNotification();
    }

    public void onEvent() {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("onEvent: ");
        sb.append(getDiscoveryCancel());
        mALogger.mo11957d(sb.toString());
        if (!getDiscoveryCancel()) {
            setDiscoveryCancel();
            unregisterTriggerReceiver();
            if (!DemoModeUtils.isDemoModeEnabled() && MotoAppUtils.isMotoEnabled()) {
                new Handler().postDelayed(new FDNManager$$Lambda$0(this), getNotificationDelay());
                registerNotificationReceiver();
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$onEvent$0$FDNManager() {
        showNotification(true);
    }

    public void showFDNAgain() {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("showFDNAgain for ");
        sb.append(getDiscoveryId());
        sb.append(", getDiscoveryCancel ");
        sb.append(getDiscoveryCancel());
        sb.append(", isFDNVisible ");
        sb.append(isFDNVisible());
        mALogger.mo11957d(sb.toString());
        if (getDiscoveryCancel() && isFDNVisible()) {
            setDiscoveryCancel();
            unregisterTriggerReceiver();
            new Handler().postDelayed(new FDNManager$$Lambda$1(this), getNotificationDelay());
            registerNotificationReceiver();
        }
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$showFDNAgain$1$FDNManager() {
        showNotification(false);
    }

    private void showNotification(boolean z) {
        if (this.mNotification == null) {
            this.mNotification = createNotification();
        }
        this.mNotification.show(z);
        setFDNVisible();
        DiscoveryManager.getInstance().setFDNLaunchedFlag(true);
    }

    private void dismissNotification() {
        if (this.mNotification != null) {
            this.mNotification.dismiss();
            this.mNotification = null;
            setFDNDismissed();
        }
    }

    private void setFDNVisible() {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("setFDNVisible for ");
        sb.append(getVisibleId());
        mALogger.mo11957d(sb.toString());
        SharedPreferenceManager.putBoolean(getVisibleId(), true);
    }

    public boolean isFDNVisible() {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("getFDNVisible for ");
        sb.append(getVisibleId());
        mALogger.mo11957d(sb.toString());
        return SharedPreferenceManager.getBoolean(getVisibleId(), false);
    }

    public void setFDNDismissed() {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("setFDNDismissed for ");
        sb.append(getVisibleId());
        mALogger.mo11957d(sb.toString());
        SharedPreferenceManager.putBoolean(getVisibleId(), false);
    }
}
