package com.motorola.actions.discovery.fdn.p007qc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.discovery.DiscoveryManager;
import com.motorola.actions.utils.MALogger;

/* renamed from: com.motorola.actions.discovery.fdn.qc.CameraIntentReceiver */
public class CameraIntentReceiver extends BroadcastReceiver {
    private static final String ACTIONS_QC_FDN = "com.motorola.actions.qc.FDN";
    private static final MALogger LOGGER = new MALogger(CameraIntentReceiver.class);
    private boolean mRegistered;

    public void onReceive(Context context, Intent intent) {
        if (intent != null && ACTIONS_QC_FDN.equals(intent.getAction())) {
            DiscoveryManager.getInstance().onFDNEvent(FeatureKey.QUICK_CAPTURE);
        }
    }

    /* access modifiers changed from: 0000 */
    public void register() {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("register - mRegistered: ");
        sb.append(this.mRegistered);
        mALogger.mo11957d(sb.toString());
        if (!this.mRegistered) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ACTIONS_QC_FDN);
            ActionsApplication.getAppContext().registerReceiver(this, intentFilter);
            this.mRegistered = true;
        }
    }

    /* access modifiers changed from: 0000 */
    public void unregister() {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("unregister - mRegistered: ");
        sb.append(this.mRegistered);
        mALogger.mo11957d(sb.toString());
        if (this.mRegistered) {
            try {
                ActionsApplication.getAppContext().unregisterReceiver(this);
            } catch (IllegalArgumentException e) {
                LOGGER.mo11960e("Unable to unregister QuickCapture notification receiver.", e);
            } catch (Throwable th) {
                this.mRegistered = false;
                throw th;
            }
            this.mRegistered = false;
        }
    }
}
