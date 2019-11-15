package com.motorola.actions.debug.items;

import android.widget.Toast;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.C0504R;
import com.motorola.actions.SharedPreferenceManager;

public class ClearDataDebugItem extends DebugItem {
    public ClearDataDebugItem() {
        setTitle(getString(C0504R.string.debug_clear_all_data_title));
        setDescription(getString(C0504R.string.debug_clear_all_data_description));
    }

    /* access modifiers changed from: protected */
    public void startBackgroundWork() {
        super.startBackgroundWork();
        SharedPreferenceManager.clear();
    }

    /* access modifiers changed from: 0000 */
    public void startPostExecuteWork() {
        super.startPostExecuteWork();
        Toast.makeText(ActionsApplication.getAppContext(), getString(C0504R.string.debug_clear_all_data_toast), 0).show();
    }
}
