package com.motorola.actions.p013ui.tutorial.foc;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.p001v4.content.LocalBroadcastManager;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.C0504R;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.foc.gesture.service.FlashOnChopService;
import com.motorola.actions.foc.gesture.util.FlashOnChopUtils;
import com.motorola.actions.p013ui.tutorial.TutorialActivity;

/* renamed from: com.motorola.actions.ui.tutorial.foc.FlashOnChopTutorialActivity */
public class FlashOnChopTutorialActivity extends TutorialActivity {
    public static final String ACTION_FOC_TUTORIAL_ENTER = "ACTION_FOC_TUTORIAL_ENTER";
    public static final String ACTION_FOC_TUTORIAL_LEAVE = "ACTION_FOC_TUTORIAL_LEAVE";
    private static final String TAG_FRAGMENT = "foc_fragment";
    private static final String TAG_SUCCESS_FRAGMENT = "foc_success_fragment";
    public static final String TUTORIAL_LIGHT_ON_KEY = "actions_foc_tutorial_light_on";
    public static final String TUTORIAL_SUCCESS_KEY = "actions_foc_tutorial_success";
    private boolean mPreviousFOCState = false;
    private boolean mShouldRestoreFOCState;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (!FlashOnChopService.isFeatureSupported(this)) {
            finish();
            return;
        }
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getBooleanExtra(TUTORIAL_LIGHT_ON_KEY, false)) {
                showLightOnResultFragment();
            } else if (intent.getBooleanExtra(TUTORIAL_SUCCESS_KEY, false)) {
                showSuccessResultFragment();
            } else if (bundle == null) {
                showFlashOnChopFragment();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        this.mPreviousFOCState = SharedPreferenceManager.getBoolean(FlashOnChopService.KEY_ENABLED, true);
        if (!this.mPreviousFOCState && !isFragmentVisible(TAG_SUCCESS_FRAGMENT)) {
            this.mShouldRestoreFOCState = true;
            SharedPreferenceManager.putBoolean(FlashOnChopService.KEY_ENABLED, true);
        }
        FlashOnChopUtils.sendToForeground(false);
        LocalBroadcastManager.getInstance(ActionsApplication.getAppContext()).sendBroadcast(new Intent(ACTION_FOC_TUTORIAL_ENTER));
    }

    private void restoreFOCState() {
        SharedPreferenceManager.putBoolean(FlashOnChopService.KEY_ENABLED, this.mPreviousFOCState);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        if (this.mShouldRestoreFOCState) {
            restoreFOCState();
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(ACTION_FOC_TUTORIAL_LEAVE));
        super.onPause();
    }

    /* access modifiers changed from: 0000 */
    public final void showFragment(Fragment fragment) {
        showFragment(fragment, TAG_FRAGMENT);
    }

    /* access modifiers changed from: 0000 */
    public final void showFragment(Fragment fragment, String str) {
        getFragmentManager().beginTransaction().replace(C0504R.C0506id.layout_tutorial, fragment, str).commit();
    }

    public void showLightOnResultFragment() {
        showFragment(new FlashOnChopLightOnTutorialFragment());
    }

    public void showSuccessResultFragment() {
        showFragment(new FlashOnChopSuccessTutorialFragment(), TAG_SUCCESS_FRAGMENT);
        restoreFOCState();
        this.mShouldRestoreFOCState = false;
    }

    private void showFlashOnChopFragment() {
        showFragment(new FlashOnChopStartTutorialFragment());
    }

    private boolean isFragmentVisible(String str) {
        Fragment findFragmentByTag = getFragmentManager().findFragmentByTag(str);
        return findFragmentByTag != null && findFragmentByTag.isVisible();
    }

    public boolean getPreviousFOCState() {
        return this.mPreviousFOCState;
    }
}
