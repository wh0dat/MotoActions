package com.motorola.actions.debug.items;

import android.content.Intent;
import android.widget.Toast;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.C0504R;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.discovery.DiscoveryManager;
import com.motorola.actions.discovery.fdn.attentivedisplay.DiscoveryConditionsListener;
import com.motorola.actions.discovery.fdn.instrumentation.DiscoveryInstrumentation;
import com.motorola.actions.discovery.fdn.onenav.SoftOneNavDiscoveryObserver;
import com.motorola.actions.discovery.fdn.p007qc.QuickCaptureFDNManager;
import com.motorola.actions.nightdisplay.common.Persistence;
import com.motorola.actions.onenav.instrumentation.SoftOneNavInstrumentationObserver;
import com.motorola.actions.p010qc.FeatureManager;
import com.motorola.actions.utils.Constants;

public class ResetFDNShowNotificationsItem extends DebugItem {
    public ResetFDNShowNotificationsItem() {
        setTitle(getString(C0504R.string.debug_fdn_reset_title));
        setDescription(getString(C0504R.string.debug_fdn_reset_description));
    }

    /* access modifiers changed from: protected */
    public void startBackgroundWork() {
        super.startBackgroundWork();
        SharedPreferenceManager.putBoolean(Constants.PREFERENCE_ACTIONS_TUTORIAL_PLAYED, false);
        DiscoveryManager.getInstance().reset();
        SharedPreferenceManager.clear(DiscoveryManager.DISCOVERY_STATUS_PREFERENCE);
        SharedPreferenceManager.putBoolean(Constants.FPS_LOCK_SCREEN_SHOWN, false);
        SharedPreferenceManager.putInt(DiscoveryConditionsListener.KEY_NUMBER_OF_SCREENS_DIM_OUT, 0);
        SharedPreferenceManager.clear(DiscoveryInstrumentation.FDN_CHANGED_FEATURES);
        SharedPreferenceManager.putLong(DiscoveryManager.KEY_TIME_SINCE_ACTIVATION, 0);
        DiscoveryManager.getInstance().restartFDNDelay();
        FeatureManager.sendIntentToCamera(new Intent(QuickCaptureFDNManager.ACTIONS_RESET_STOP_COUNT));
        Persistence.setIsSleepPatternReady(false);
        SharedPreferenceManager.remove(SoftOneNavDiscoveryObserver.KEY_HOURS_PAST_HINT);
        SharedPreferenceManager.remove(SoftOneNavInstrumentationObserver.KEY_SOFT_ONENAV_LAST_STATE);
        SoftOneNavInstrumentationObserver.registerObserver();
    }

    /* access modifiers changed from: protected */
    public void startPostExecuteWork() {
        super.startPostExecuteWork();
        DiscoveryManager.getInstance().start();
        Toast.makeText(ActionsApplication.getAppContext(), C0504R.string.debug_fdn_reset_finished, 0).show();
    }
}
