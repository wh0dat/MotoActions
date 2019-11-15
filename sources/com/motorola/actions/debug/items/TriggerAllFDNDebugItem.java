package com.motorola.actions.debug.items;

import android.app.NotificationManager;
import android.widget.Toast;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.C0504R;
import com.motorola.actions.discovery.DiscoveryManager;
import com.motorola.actions.discovery.fdn.FDNManager;
import com.motorola.actions.utils.MALogger;
import java.util.Map.Entry;

public class TriggerAllFDNDebugItem extends DebugItem {
    private static final MALogger LOGGER = new MALogger(TriggerAllFDNDebugItem.class);
    private int mMsg;

    public TriggerAllFDNDebugItem() {
        setTitle(getString(C0504R.string.debug_trigger_fdn_title));
        setDescription(getString(C0504R.string.debug_trigger_fdn_description));
    }

    /* access modifiers changed from: protected */
    public void startBackgroundWork() {
        super.startBackgroundWork();
        if (((NotificationManager) ActionsApplication.getAppContext().getSystemService("notification")) != null) {
            for (Entry entry : DiscoveryManager.getInstance().getAllFDNManagers().entrySet()) {
                ((FDNManager) entry.getValue()).createNotification().show(false);
                DiscoveryManager.getInstance().registerReceiver(((FDNManager) entry.getValue()).getDiscoveryId());
            }
            this.mMsg = C0504R.string.debug_trigger_fdn_message;
            return;
        }
        LOGGER.mo11959e("Notification Manager is null. Unable to trigger FDN.");
        this.mMsg = C0504R.string.debug_trigger_fdn_error;
    }

    /* access modifiers changed from: protected */
    public void startPostExecuteWork() {
        super.startPostExecuteWork();
        Toast.makeText(ActionsApplication.getAppContext(), this.mMsg, 0).show();
    }
}
