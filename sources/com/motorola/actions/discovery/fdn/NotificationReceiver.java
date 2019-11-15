package com.motorola.actions.discovery.fdn;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.google.android.gms.common.util.CrashUtils.ErrorDialogData;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.discovery.DiscoveryManager;
import com.motorola.actions.p013ui.settings.SettingsActivity;
import com.motorola.actions.utils.MALogger;
import java.util.EnumSet;

public class NotificationReceiver extends BroadcastReceiver {
    static final String ACTION_DISMISS_SWIPE = "com.motorola.actions.discovery.ACTION_DISMISS_SWIPE";
    static final String ACTION_NO_THANKS = "com.motorola.actions.discovery.ACTION_NO_THANKS";
    static final String ACTION_OPEN_SETTINGS_CLICK = "com.motorola.actions.discovery.ACTION_OPEN_SETTINGS_CLICK";
    static final String ACTION_OPEN_SETTINGS_TRY_IT_NOW = "com.motorola.actions.discovery.ACTION_OPEN_SETTINGS_TRY_IT_NOW";
    public static final String EXTRA_FDN_ID = "com.motorola.actions.discovery.EXTRA_FDN_ID";
    private static final MALogger LOGGER = new MALogger(NotificationReceiver.class);
    private final EnumSet<FeatureKey> mActiveDiscoveries = EnumSet.noneOf(FeatureKey.class);
    private boolean mRegistered;

    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            FeatureKey featureKey = (FeatureKey) intent.getSerializableExtra(EXTRA_FDN_ID);
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("onReceive - featureKey: ");
            sb.append(featureKey);
            sb.append(" action=");
            sb.append(intent.getAction());
            mALogger.mo11957d(sb.toString());
            doInstrumentationFDN(intent.getAction(), featureKey);
            DiscoveryManager.getInstance().restartFDNDelay();
            DiscoveryManager.getInstance().setFDNLaunchedFlag(false);
            DiscoveryManager.getInstance().cancelFDN(featureKey);
            unregister(featureKey);
            if (ACTION_OPEN_SETTINGS_CLICK.equals(intent.getAction()) || ACTION_OPEN_SETTINGS_TRY_IT_NOW.equals(intent.getAction())) {
                Intent featureLaunchIntent = SettingsActivity.getFeatureLaunchIntent(featureKey.ordinal());
                featureLaunchIntent.putExtra(FDNSession.EXTRA_IS_FROM_FDN, true);
                featureLaunchIntent.setFlags(ErrorDialogData.BINDER_CRASH);
                ActionsApplication.getAppContext().startActivity(featureLaunchIntent);
                closeNotificationBar(context);
            }
        }
    }

    private void closeNotificationBar(Context context) {
        context.sendBroadcast(new Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x0045  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x004d  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0055  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x005d  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0065  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void doInstrumentationFDN(java.lang.String r2, com.motorola.actions.FeatureKey r3) {
        /*
            r1 = this;
            int r1 = r2.hashCode()
            r0 = -1639226375(0xffffffff9e4b63f9, float:-1.0767404E-20)
            if (r1 == r0) goto L_0x0037
            r0 = 744487125(0x2c5ff8d5, float:3.1828336E-12)
            if (r1 == r0) goto L_0x002d
            r0 = 1217219615(0x488d4c1f, float:289376.97)
            if (r1 == r0) goto L_0x0023
            r0 = 1335615005(0x4f9bde1d, float:5.2300498E9)
            if (r1 == r0) goto L_0x0019
            goto L_0x0041
        L_0x0019:
            java.lang.String r1 = "com.motorola.actions.discovery.ACTION_DISMISS_SWIPE"
            boolean r1 = r2.equals(r1)
            if (r1 == 0) goto L_0x0041
            r1 = 1
            goto L_0x0042
        L_0x0023:
            java.lang.String r1 = "com.motorola.actions.discovery.ACTION_NO_THANKS"
            boolean r1 = r2.equals(r1)
            if (r1 == 0) goto L_0x0041
            r1 = 0
            goto L_0x0042
        L_0x002d:
            java.lang.String r1 = "com.motorola.actions.discovery.ACTION_OPEN_SETTINGS_TRY_IT_NOW"
            boolean r1 = r2.equals(r1)
            if (r1 == 0) goto L_0x0041
            r1 = 3
            goto L_0x0042
        L_0x0037:
            java.lang.String r1 = "com.motorola.actions.discovery.ACTION_OPEN_SETTINGS_CLICK"
            boolean r1 = r2.equals(r1)
            if (r1 == 0) goto L_0x0041
            r1 = 2
            goto L_0x0042
        L_0x0041:
            r1 = -1
        L_0x0042:
            switch(r1) {
                case 0: goto L_0x0065;
                case 1: goto L_0x005d;
                case 2: goto L_0x0055;
                case 3: goto L_0x004d;
                default: goto L_0x0045;
            }
        L_0x0045:
            com.motorola.actions.utils.MALogger r1 = LOGGER
            java.lang.String r2 = "Invalid action, no instrumentation was done on FDN"
            r1.mo11959e(r2)
            goto L_0x006c
        L_0x004d:
            int r1 = r3.ordinal()
            com.motorola.actions.discovery.fdn.instrumentation.DiscoveryInstrumentation.recordTryItNowFDN(r1)
            goto L_0x006c
        L_0x0055:
            int r1 = r3.ordinal()
            com.motorola.actions.discovery.fdn.instrumentation.DiscoveryInstrumentation.recordClickFDN(r1)
            goto L_0x006c
        L_0x005d:
            int r1 = r3.ordinal()
            com.motorola.actions.discovery.fdn.instrumentation.DiscoveryInstrumentation.recordDismissSwipeFDN(r1)
            goto L_0x006c
        L_0x0065:
            int r1 = r3.ordinal()
            com.motorola.actions.discovery.fdn.instrumentation.DiscoveryInstrumentation.recordDismissNoThanksFDN(r1)
        L_0x006c:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.motorola.actions.discovery.fdn.NotificationReceiver.doInstrumentationFDN(java.lang.String, com.motorola.actions.FeatureKey):void");
    }

    public void register(FeatureKey featureKey) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("Register - mRegistered: ");
        sb.append(this.mRegistered);
        sb.append(", discoveryId: ");
        sb.append(featureKey);
        mALogger.mo11957d(sb.toString());
        this.mActiveDiscoveries.add(featureKey);
        if (!this.mRegistered) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ACTION_NO_THANKS);
            intentFilter.addAction(ACTION_OPEN_SETTINGS_CLICK);
            intentFilter.addAction(ACTION_OPEN_SETTINGS_TRY_IT_NOW);
            intentFilter.addAction(ACTION_DISMISS_SWIPE);
            ActionsApplication.getAppContext().registerReceiver(this, intentFilter);
            this.mRegistered = true;
        }
    }

    public void unregister(FeatureKey featureKey) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("unregister - mRegistered: ");
        sb.append(this.mRegistered);
        sb.append(", discoveryId: ");
        sb.append(featureKey);
        mALogger.mo11957d(sb.toString());
        this.mActiveDiscoveries.remove(featureKey);
        if (this.mRegistered && this.mActiveDiscoveries.size() == 0) {
            try {
                ActionsApplication.getAppContext().unregisterReceiver(this);
                LOGGER.mo11957d("Receiver unregistered");
            } catch (IllegalArgumentException e) {
                LOGGER.mo11960e("Unable to unregister notification receiver.", e);
            } catch (Throwable th) {
                this.mRegistered = false;
                throw th;
            }
            this.mRegistered = false;
        }
    }
}
