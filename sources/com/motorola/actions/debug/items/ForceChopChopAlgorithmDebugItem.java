package com.motorola.actions.debug.items;

import android.widget.Toast;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.foc.gesture.util.FlashOnChopAlgorithmUtil;

public class ForceChopChopAlgorithmDebugItem extends DebugItem {
    private final String mAlgorithm;
    private final int mToastMsg;

    public ForceChopChopAlgorithmDebugItem(String str, int i, int i2, int i3) {
        setTitle(getString(i));
        setDescription(getString(i2));
        this.mAlgorithm = str;
        this.mToastMsg = i3;
    }

    /* access modifiers changed from: protected */
    public void startBackgroundWork() {
        super.startBackgroundWork();
        SharedPreferenceManager.putString(FlashOnChopAlgorithmUtil.KEY_TEST_USE_ONLY_FOC_ALGO, this.mAlgorithm);
    }

    /* access modifiers changed from: protected */
    public void startPostExecuteWork() {
        super.startPostExecuteWork();
        Toast.makeText(ActionsApplication.getAppContext(), this.mToastMsg, 0).show();
    }
}
