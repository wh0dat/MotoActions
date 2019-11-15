package com.motorola.actions;

import android.os.Bundle;
import com.motorola.actions.p013ui.ActionsBaseActivity;
import com.motorola.actions.utils.MotoAppUtils;

public class MotoActionsAppStoreLauncher extends ActionsBaseActivity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        MotoAppUtils.launchMoto(this);
        finish();
    }
}
