package com.motorola.actions.debug.items;

import android.widget.Toast;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.C0504R;
import com.motorola.actions.checkin.BaseAnalytics;
import com.motorola.actions.checkin.CheckinAlarm;

public class InstrumentationClearDataDebugItem extends DebugItem {
    public InstrumentationClearDataDebugItem() {
        setTitle(getString(C0504R.string.debug_instrumentation_clear_data_title));
        setDescription(getString(C0504R.string.debug_instrumentation_clear_data_description));
    }

    /* access modifiers changed from: protected */
    public void startBackgroundWork() {
        super.startBackgroundWork();
        for (BaseAnalytics baseAnalytics : CheckinAlarm.getInstance().getAnalyticsList()) {
            baseAnalytics.getDatastore(baseAnalytics.getDatastoreName()).resetValues();
        }
    }

    /* access modifiers changed from: protected */
    public void startPostExecuteWork() {
        super.startPostExecuteWork();
        Toast.makeText(ActionsApplication.getAppContext(), C0504R.string.debug_instrumentation_data_clear_toast, 0).show();
    }
}
