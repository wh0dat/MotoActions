package com.motorola.actions.debug.items;

import android.widget.Toast;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.C0504R;
import com.motorola.actions.utils.LogRecorder;

public class StopLoggingToFileDebugItem extends DebugItem {
    public StopLoggingToFileDebugItem() {
        setTitle(getString(C0504R.string.debug_stop_record_logging_title));
        setDescription(getString(C0504R.string.debug_stop_record_logging_description));
    }

    /* access modifiers changed from: protected */
    public void startBackgroundWork() {
        super.startBackgroundWork();
        LogRecorder.getInstance().stopRecording();
    }

    /* access modifiers changed from: protected */
    public void startPostExecuteWork() {
        super.startPostExecuteWork();
        Toast.makeText(ActionsApplication.getAppContext(), getString(C0504R.string.debug_stop_record_logging_toast), 0).show();
    }
}
