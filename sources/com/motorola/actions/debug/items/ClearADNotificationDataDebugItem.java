package com.motorola.actions.debug.items;

import android.widget.Toast;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.C0504R;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.attentivedisplay.notification.ADBatterySaverNotificationReceiver;
import com.motorola.actions.attentivedisplay.notification.ADModNotificationReceiver;

public class ClearADNotificationDataDebugItem extends DebugItem {
    public ClearADNotificationDataDebugItem() {
        setTitle(getString(C0504R.string.debug_clear_ad_notification_title));
        setDescription(getString(C0504R.string.debug_clear_ad_notification_description));
    }

    /* access modifiers changed from: protected */
    public void startBackgroundWork() {
        super.startBackgroundWork();
        SharedPreferenceManager.remove(ADModNotificationReceiver.SHOULD_NOT_SHOW_AGAIN_AD_MOD_PREFERENCE_KEY);
        SharedPreferenceManager.remove(ADBatterySaverNotificationReceiver.SHOULD_NOT_SHOW_AGAIN_AD_BATTERY_PREFERENCE_KEY);
    }

    /* access modifiers changed from: 0000 */
    public void startPostExecuteWork() {
        super.startPostExecuteWork();
        Toast.makeText(ActionsApplication.getAppContext(), getString(C0504R.string.debug_clear_ad_notification_toast), 0).show();
    }
}
