package com.motorola.actions.p013ui.tutorial.quickScreenshot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.motorola.actions.C0504R;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.checkin.CommonCheckinAttributes;
import com.motorola.actions.p013ui.tutorial.TutorialFragment;
import com.motorola.actions.quickscreenshot.QuickScreenshotModel;
import com.motorola.actions.settings.updater.QuickScreenshotSettingsUpdater;

/* renamed from: com.motorola.actions.ui.tutorial.quickScreenshot.QuickScreenshotSuccessTutorialFragment */
public class QuickScreenshotSuccessTutorialFragment extends TutorialFragment {
    /* access modifiers changed from: protected */
    public int getVideoResId() {
        return -1;
    }

    /* access modifiers changed from: protected */
    public FeatureKey getFeatureKey() {
        return FeatureKey.QUICK_SCREENSHOT;
    }

    /* access modifiers changed from: protected */
    public void leftButtonClicked(View view) {
        QuickScreenshotSettingsUpdater.getInstance().toggleStatus(false, true, CommonCheckinAttributes.KEY_DAILY_ENABLE_SOURCE_TYPE_TUTORIAL);
        super.leftButtonClicked(view);
    }

    /* access modifiers changed from: protected */
    public void finish() {
        if (!isFeatureEnabled()) {
            QuickScreenshotSettingsUpdater.getInstance().toggleStatus(true, true, CommonCheckinAttributes.KEY_DAILY_ENABLE_SOURCE_TYPE_TUTORIAL);
        }
        super.finish();
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View onCreateView = super.onCreateView(layoutInflater, viewGroup, bundle);
        setTextTitle(C0504R.string.quick_screenshot_tutorial_success_title);
        setTextInfo(C0504R.string.quick_screenshot_tutorial_success);
        if (!QuickScreenshotModel.isServiceEnabled()) {
            setTextInfo2(C0504R.string.quick_screenshot_tutorial_success_turn_on);
        }
        showFinalButtons(QuickScreenshotModel.isServiceEnabled());
        return onCreateView;
    }
}
