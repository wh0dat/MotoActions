package com.motorola.actions.p013ui.tutorial.lts;

import android.app.Fragment;
import android.os.Bundle;
import com.motorola.actions.C0504R;
import com.motorola.actions.p013ui.settings.BaseSettingsActivity;

/* renamed from: com.motorola.actions.ui.tutorial.lts.LiftToSilenceTutorialActivity */
public class LiftToSilenceTutorialActivity extends BaseSettingsActivity {
    private static final String TAG_FRAGMENT = "fragment";

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C0504R.layout.activity_tutorial);
        if (bundle == null) {
            restartDemo();
        }
    }

    public void showDemoFragment() {
        showFragment(new LiftToSilenceTutorialFragment());
    }

    public void showSuccessResultFragment() {
        showPhoneResultFragment(true, true);
    }

    public void showFailureNoSwipeResultFragment() {
        showPhoneResultFragment(false, true);
    }

    public void showFailureTouchResultFragment() {
        showPhoneResultFragment(false, true);
    }

    public void showFailureNotPutOnTableFragment() {
        showPhoneResultFragment(false, false);
    }

    private void showPhoneResultFragment(boolean z, boolean z2) {
        showFragment(LiftToSilenceTutorialResultFragment.getPhoneResultInstance(z, z2));
    }

    public void restartDemo() {
        showPhoneIntroFragment();
    }

    private void showPhoneIntroFragment() {
        showFragment(new LiftToSilenceIntroTutorialFragment());
    }

    /* access modifiers changed from: 0000 */
    public final void finishDemo() {
        setResult(-1);
        finish();
    }

    private void showFragment(Fragment fragment) {
        getFragmentManager().beginTransaction().replace(C0504R.C0506id.layout_tutorial, fragment, TAG_FRAGMENT).commit();
    }
}
