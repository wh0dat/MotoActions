package com.motorola.actions.debug.items;

import android.widget.Toast;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.C0504R;
import com.motorola.actions.sleepPattern.FeatureManager;
import com.motorola.actions.sleepPattern.repository.SleepPatternDatabase;

public class DeleteSleepPatternDatabase extends DebugItem {
    public DeleteSleepPatternDatabase() {
        setTitle(getString(C0504R.string.debug_sp_delete_db_title));
        setDescription(getString(C0504R.string.debug_sp_delete_db_description));
    }

    /* access modifiers changed from: protected */
    public void startBackgroundWork() {
        super.startBackgroundWork();
        FeatureManager.stop(ActionsApplication.getAppContext());
        SleepPatternDatabase.closeInstance();
        ActionsApplication.getAppContext().deleteDatabase(SleepPatternDatabase.DATABASE_NAME);
    }

    /* access modifiers changed from: protected */
    public void startPostExecuteWork() {
        super.startPostExecuteWork();
        FeatureManager.start(ActionsApplication.getAppContext());
        Toast.makeText(ActionsApplication.getAppContext(), C0504R.string.debug_sp_delete_db_finished, 0).show();
    }
}
