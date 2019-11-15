package com.motorola.actions.debug.items;

import android.content.Context;
import android.widget.Toast;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.C0504R;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.discovery.DiscoveryManager;
import com.motorola.actions.discovery.DiscoveryManager.FDNDelayMode;
import com.motorola.actions.discovery.fdn.onenav.SoftOneNavDiscoveryObserver;
import java.util.concurrent.TimeUnit;

public class ChangeFDNDelayDebugItem extends DebugItem {
    public ChangeFDNDelayDebugItem() {
        setTitle(getString(C0504R.string.debug_fdn_change_title));
        setDescription(getString(C0504R.string.debug_fdn_change_description));
    }

    /* access modifiers changed from: protected */
    public void startBackgroundWork() {
        int i;
        long j;
        super.startBackgroundWork();
        if (FDNDelayMode.NORMAL.ordinal() == DiscoveryManager.getInstance().getFDNDelayMode()) {
            i = FDNDelayMode.DEBUG.ordinal();
            j = TimeUnit.DAYS.toMillis(8);
        } else {
            i = FDNDelayMode.NORMAL.ordinal();
            j = 0;
        }
        SharedPreferenceManager.putInt(DiscoveryManager.KEY_FDN_DELAY_MODE, i);
        SharedPreferenceManager.putLong(DiscoveryManager.KEY_TIME_SINCE_ACTIVATION, j);
        SharedPreferenceManager.putInt(SoftOneNavDiscoveryObserver.KEY_HOURS_PAST_HINT, 0);
    }

    /* access modifiers changed from: protected */
    public void startPostExecuteWork() {
        String str;
        super.startPostExecuteWork();
        DiscoveryManager.getInstance().registerFDNListeners();
        Context appContext = ActionsApplication.getAppContext();
        StringBuilder sb = new StringBuilder();
        sb.append("FDN delay changed to: ");
        if (FDNDelayMode.NORMAL.ordinal() == DiscoveryManager.getInstance().getFDNDelayMode()) {
            str = FDNDelayMode.NORMAL.toString();
        } else {
            str = FDNDelayMode.DEBUG.toString();
        }
        sb.append(str);
        Toast.makeText(appContext, sb.toString(), 0).show();
    }
}
