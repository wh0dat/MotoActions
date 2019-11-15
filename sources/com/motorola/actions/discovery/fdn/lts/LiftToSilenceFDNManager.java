package com.motorola.actions.discovery.fdn.lts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.discovery.DiscoveryManager;
import com.motorola.actions.discovery.fdn.BaseDiscoveryNotification;
import com.motorola.actions.discovery.fdn.FDNManager;
import com.motorola.actions.lts.LiftToSilenceService;
import com.motorola.actions.utils.MALogger;

public class LiftToSilenceFDNManager extends FDNManager {
    /* access modifiers changed from: private */
    public static final MALogger LOGGER = new MALogger(LiftToSilenceFDNManager.class);
    private static final String LTS_DISCOVERY_CANCEL = "lts_discovery_cancel";
    private static final String LTS_DISCOVERY_VISIBLE = "lts_discovery_visible";
    private static final String VOLUME_DOWN_ACTION_DURING_CALL = "com.motorola.intent.action.VOLUME_DOWN_DURING_INC_CALL";
    private SilenceRingerReceiver mSilenceRingerReceiver;
    /* access modifiers changed from: private */
    public TelephonyListener mTelephonyListener;
    /* access modifiers changed from: private */
    public TelephonyManager mTelephonyManager;

    private class SilenceRingerReceiver extends BroadcastReceiver {
        private SilenceRingerReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent == null) {
                return;
            }
            if (LiftToSilenceFDNManager.VOLUME_DOWN_ACTION_DURING_CALL.equals(intent.getAction()) || ("android.intent.action.SCREEN_OFF".equals(intent.getAction()) && LiftToSilenceFDNManager.this.mTelephonyManager.getCallState() == 1)) {
                LiftToSilenceFDNManager.this.mTelephonyListener = new TelephonyListener();
                LiftToSilenceFDNManager.this.mTelephonyManager.listen(LiftToSilenceFDNManager.this.mTelephonyListener, 32);
            }
        }

        /* access modifiers changed from: private */
        public void register() {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(LiftToSilenceFDNManager.VOLUME_DOWN_ACTION_DURING_CALL);
            intentFilter.addAction("android.intent.action.SCREEN_OFF");
            ActionsApplication.getAppContext().registerReceiver(this, intentFilter);
        }

        /* access modifiers changed from: private */
        public void unregister() {
            try {
                ActionsApplication.getAppContext().unregisterReceiver(this);
            } catch (IllegalArgumentException e) {
                LiftToSilenceFDNManager.LOGGER.mo11958d("Could not unregister receiver", e);
            }
        }
    }

    private class TelephonyListener extends PhoneStateListener {
        private TelephonyListener() {
        }

        public void onCallStateChanged(int i, String str) {
            super.onCallStateChanged(i, str);
            MALogger access$600 = LiftToSilenceFDNManager.LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("onCallStateChanged, callState = ");
            sb.append(i);
            access$600.mo11957d(sb.toString());
            if (i == 0) {
                DiscoveryManager.getInstance().onFDNEvent(FeatureKey.PICKUP_TO_STOP_RINGING);
                LiftToSilenceFDNManager.this.mTelephonyManager.listen(LiftToSilenceFDNManager.this.mTelephonyListener, 0);
            }
        }
    }

    /* access modifiers changed from: protected */
    public String getCancelId() {
        return LTS_DISCOVERY_CANCEL;
    }

    /* access modifiers changed from: protected */
    public String getVisibleId() {
        return LTS_DISCOVERY_VISIBLE;
    }

    public boolean registerTriggerReceiver() {
        Context appContext = ActionsApplication.getAppContext();
        if (!LiftToSilenceService.isFeatureSupported() || LiftToSilenceService.isLiftToSilenceEnabled() || getDiscoveryCancel()) {
            return false;
        }
        this.mTelephonyManager = (TelephonyManager) appContext.getSystemService("phone");
        this.mSilenceRingerReceiver = new SilenceRingerReceiver();
        this.mSilenceRingerReceiver.register();
        return true;
    }

    public void unregisterTriggerReceiver() {
        if (this.mSilenceRingerReceiver != null) {
            this.mSilenceRingerReceiver.unregister();
            this.mSilenceRingerReceiver = null;
        }
    }

    public BaseDiscoveryNotification createNotification() {
        return new LiftToSilenceDiscoveryNotification(getDiscoveryId());
    }

    public FeatureKey getDiscoveryId() {
        return FeatureKey.PICKUP_TO_STOP_RINGING;
    }
}
