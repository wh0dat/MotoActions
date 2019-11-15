package com.motorola.actions.discovery.fdn.mediacontrol;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.utils.MALogger;

public class HeadsetPlugReceiver extends BroadcastReceiver {
    private static final String EXTRA_HEADSET_STATE = "state";
    private static final int HEADSET_STATE_PLUGGED = 1;
    private static final int HEADSET_STATE_UNPLUGGED = 0;
    private static final MALogger LOGGER = new MALogger(HeadsetPlugReceiver.class);
    private boolean mRegistered;

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0042, code lost:
        if (r3 == 1) goto L_0x0044;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x006b, code lost:
        if (r3 == 2) goto L_0x0044;
     */
    /* JADX WARNING: Removed duplicated region for block: B:11:0x0070  */
    /* JADX WARNING: Removed duplicated region for block: B:14:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onReceive(android.content.Context r4, android.content.Intent r5) {
        /*
            r3 = this;
            if (r5 == 0) goto L_0x0079
            java.lang.String r3 = r5.getAction()
            com.motorola.actions.utils.MALogger r4 = LOGGER
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "Action "
            r0.append(r1)
            r0.append(r3)
            java.lang.String r0 = r0.toString()
            r4.mo11957d(r0)
            java.lang.String r4 = "android.intent.action.HEADSET_PLUG"
            boolean r4 = r4.equals(r3)
            r0 = 1
            r1 = 0
            if (r4 == 0) goto L_0x0046
            java.lang.String r3 = "state"
            int r3 = r5.getIntExtra(r3, r1)
            com.motorola.actions.utils.MALogger r4 = LOGGER
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r2 = "Wired Headset state: "
            r5.append(r2)
            r5.append(r3)
            java.lang.String r5 = r5.toString()
            r4.mo11957d(r5)
            if (r3 != r0) goto L_0x006e
        L_0x0044:
            r1 = r0
            goto L_0x006e
        L_0x0046:
            java.lang.String r4 = "android.bluetooth.adapter.action.CONNECTION_STATE_CHANGED"
            boolean r3 = r4.equals(r3)
            if (r3 == 0) goto L_0x006e
            java.lang.String r3 = "android.bluetooth.adapter.extra.CONNECTION_STATE"
            int r3 = r5.getIntExtra(r3, r1)
            com.motorola.actions.utils.MALogger r4 = LOGGER
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r2 = "BT Headset state: "
            r5.append(r2)
            r5.append(r3)
            java.lang.String r5 = r5.toString()
            r4.mo11957d(r5)
            r4 = 2
            if (r3 != r4) goto L_0x006e
            goto L_0x0044
        L_0x006e:
            if (r1 == 0) goto L_0x0079
            com.motorola.actions.discovery.DiscoveryManager r3 = com.motorola.actions.discovery.DiscoveryManager.getInstance()
            com.motorola.actions.FeatureKey r4 = com.motorola.actions.FeatureKey.MEDIA_CONTROL
            r3.onFDNEvent(r4)
        L_0x0079:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.motorola.actions.discovery.fdn.mediacontrol.HeadsetPlugReceiver.onReceive(android.content.Context, android.content.Intent):void");
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
            intentFilter.addAction("android.intent.action.HEADSET_PLUG");
            intentFilter.addAction("android.bluetooth.adapter.action.CONNECTION_STATE_CHANGED");
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
                LOGGER.mo11960e("Unable to unregister trigger receiver.", e);
            } catch (Throwable th) {
                this.mRegistered = false;
                throw th;
            }
            this.mRegistered = false;
        }
    }
}
