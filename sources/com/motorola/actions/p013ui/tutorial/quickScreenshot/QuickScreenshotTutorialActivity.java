package com.motorola.actions.p013ui.tutorial.quickScreenshot;

import android.app.Fragment;
import android.os.Bundle;
import com.motorola.actions.C0504R;
import com.motorola.actions.p013ui.tutorial.TutorialActivity;
import com.motorola.actions.quickscreenshot.QuickScreenshotHelper;

/* renamed from: com.motorola.actions.ui.tutorial.quickScreenshot.QuickScreenshotTutorialActivity */
public class QuickScreenshotTutorialActivity extends TutorialActivity {
    private static final String TAG_SUCCESS_FRAGMENT = "tag_success";
    private static final String TAG_TUTORIAL_FRAGMENT = "tag_tutorial";

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (!QuickScreenshotHelper.isFeatureSupported()) {
            finish();
        } else {
            showFragment(new QuickScreenshotTutorialFragment(), TAG_TUTORIAL_FRAGMENT);
        }
    }

    public void showSuccessResultFragment() {
        showFragment(new QuickScreenshotSuccessTutorialFragment(), TAG_SUCCESS_FRAGMENT);
    }

    private void showFragment(Fragment fragment, String str) {
        getFragmentManager().beginTransaction().replace(C0504R.C0506id.layout_tutorial, fragment, str).commit();
    }
}
