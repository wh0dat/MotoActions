package com.motorola.actions.debug.items;

import android.content.Context;
import android.widget.Toast;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.C0504R;
import com.motorola.actions.nightdisplay.common.Persistence;

public class SleepPatternToggleSourceDebugItem extends DebugItem {
    final Context mContext = ActionsApplication.getAppContext();

    public SleepPatternToggleSourceDebugItem() {
        setTitle(getString(C0504R.string.debug_toggle_sleep_pattern_source_title));
        setDescription(getString(C0504R.string.debug_toggle_sleep_pattern_main_description));
    }

    /* access modifiers changed from: protected */
    public void startBackgroundWork() {
        super.startBackgroundWork();
        if (!Persistence.isActivityRecognitionEnabled()) {
            Persistence.setActivityRecognitionEnabled(true);
        } else {
            Persistence.setActivityRecognitionEnabled(false);
        }
    }

    /* access modifiers changed from: protected */
    public void startPostExecuteWork() {
        String str;
        super.startPostExecuteWork();
        if (!Persistence.isActivityRecognitionEnabled()) {
            str = getString(C0504R.string.debug_toggle_sleep_pattern_accel_toast);
        } else {
            str = getString(C0504R.string.debug_toggle_sleep_pattern_api_toast);
        }
        Toast.makeText(this.mContext, str, 0).show();
        setDescription(getItemDescription());
    }

    private String getItemDescription() {
        return getString(!Persistence.isActivityRecognitionEnabled() ? C0504R.string.debug_toggle_sleep_pattern_accel_description : C0504R.string.debug_toggle_sleep_pattern_api_description);
    }
}
