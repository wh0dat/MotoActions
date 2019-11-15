package com.motorola.actions.discovery.fdn.quickscreenshot;

import com.motorola.actions.FeatureKey;
import com.motorola.actions.discovery.DiscoveryManager;
import com.motorola.actions.discovery.fdn.BaseDiscoveryNotification;
import com.motorola.actions.discovery.fdn.FDNManager;
import com.motorola.actions.quickscreenshot.QuickScreenshotHelper;
import com.motorola.actions.quickscreenshot.ScreenshotReceiver;
import com.motorola.actions.quickscreenshot.ScreenshotReceiver.ScreenshotReceiverCallback;
import com.motorola.actions.utils.MALogger;
import java.util.concurrent.TimeUnit;

public class QuickScreenshotFDNManager extends FDNManager implements ScreenshotReceiverCallback {
    private static final MALogger LOGGER = new MALogger(QuickScreenshotFDNManager.class);
    private static final String QUICKSCREENSHOT_DISCOVERY_CANCEL = "quickscreenshot_discovery_cancel";
    private static final String QUICKSCREENSHOT_DISCOVERY_VISIBLE = "quickscreenshot_discovery_visible";
    private ScreenshotReceiver mScreenshotReceiver;

    /* access modifiers changed from: protected */
    public String getCancelId() {
        return QUICKSCREENSHOT_DISCOVERY_CANCEL;
    }

    /* access modifiers changed from: protected */
    public String getVisibleId() {
        return QUICKSCREENSHOT_DISCOVERY_VISIBLE;
    }

    public void run(boolean z) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("run, isQuickScreenshotType = ");
        sb.append(z);
        mALogger.mo11957d(sb.toString());
        if (z) {
            DiscoveryManager.getInstance().cancelFDN(FeatureKey.QUICK_SCREENSHOT);
        } else {
            DiscoveryManager.getInstance().onFDNEvent(FeatureKey.QUICK_SCREENSHOT);
        }
    }

    public boolean registerTriggerReceiver() {
        if (!QuickScreenshotHelper.isFeatureSupported() || getDiscoveryCancel() || this.mScreenshotReceiver != null) {
            return false;
        }
        LOGGER.mo11957d("registerTriggerReceiver");
        this.mScreenshotReceiver = new ScreenshotReceiver(this);
        this.mScreenshotReceiver.register();
        return true;
    }

    public void unregisterTriggerReceiver() {
        if (this.mScreenshotReceiver != null) {
            LOGGER.mo11957d("unregisterTriggerReceiver");
            this.mScreenshotReceiver.unregister();
            this.mScreenshotReceiver = null;
        }
    }

    public BaseDiscoveryNotification createNotification() {
        return new QuickScreenshotDiscoveryNotification(getDiscoveryId());
    }

    public FeatureKey getDiscoveryId() {
        return FeatureKey.QUICK_SCREENSHOT;
    }

    public long getNotificationDelay() {
        return TimeUnit.SECONDS.toMillis(3);
    }
}
