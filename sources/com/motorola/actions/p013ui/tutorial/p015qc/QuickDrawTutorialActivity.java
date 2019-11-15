package com.motorola.actions.p013ui.tutorial.p015qc;

import android.os.Bundle;
import com.motorola.actions.C0504R;
import com.motorola.actions.p013ui.tutorial.TutorialActivity;

/* renamed from: com.motorola.actions.ui.tutorial.qc.QuickDrawTutorialActivity */
public class QuickDrawTutorialActivity extends TutorialActivity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle == null) {
            showQuickDrawFragment();
        }
    }

    private void showQuickDrawFragment() {
        getFragmentManager().beginTransaction().add(C0504R.C0506id.layout_tutorial, new QuickDrawTutorialFragment()).commit();
    }
}
