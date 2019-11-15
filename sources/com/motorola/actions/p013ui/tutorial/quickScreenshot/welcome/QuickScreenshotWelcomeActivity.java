package com.motorola.actions.p013ui.tutorial.quickScreenshot.welcome;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.C0504R;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.databinding.ActivityWelcomeQuickscreenshotBinding;
import com.motorola.actions.p013ui.ActionsBaseActivity;
import com.motorola.actions.p013ui.tutorial.VideoListener;
import com.motorola.actions.p013ui.tutorial.quickScreenshot.QuickScreenshotTutorialActivity;
import com.motorola.actions.utils.ActivityUtils;
import com.motorola.actions.utils.Constants;

/* renamed from: com.motorola.actions.ui.tutorial.quickScreenshot.welcome.QuickScreenshotWelcomeActivity */
public class QuickScreenshotWelcomeActivity extends ActionsBaseActivity implements WelcomeScreenButtonListener {
    private ActivityWelcomeQuickscreenshotBinding mBinding;
    private QuickScreenshotWelcomePresenter mPresenter;
    private VideoListener mVideoListener;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ActivityUtils.setFullscreenMode(this);
        this.mPresenter = new QuickScreenshotWelcomePresenter();
        this.mPresenter.setListener(this);
        this.mBinding = (ActivityWelcomeQuickscreenshotBinding) DataBindingUtil.setContentView(this, C0504R.layout.activity_welcome_quickscreenshot);
        this.mBinding.setPresenter(this.mPresenter);
        setupVideo();
        SharedPreferenceManager.putBoolean(Constants.QUICK_SCREENSHOT_SCREEN_ALREADY_SHOWN, true);
    }

    public void onPause() {
        super.onPause();
        stopVideo();
    }

    public void onResume() {
        super.onResume();
        ActivityUtils.setSetupWizardMode(findViewById(C0504R.C0506id.welcome_root), this);
        playVideo();
    }

    public static void openWelcomeActivity(@NonNull Activity activity, Bundle bundle) {
        Intent intent = new Intent(activity, QuickScreenshotWelcomeActivity.class);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    private void setupVideo() {
        this.mVideoListener = new VideoListener(ActionsApplication.getAppContext(), this.mBinding.welcomeVideo, this.mPresenter.getVideoResId());
    }

    private void playVideo() {
        if (this.mVideoListener != null) {
            this.mVideoListener.playVideo();
        }
    }

    private void stopVideo() {
        if (this.mVideoListener != null) {
            this.mVideoListener.stopVideo();
        }
    }

    public void onLeftButtonClick() {
        finish();
        animateSlideInReverse();
    }

    public void onRightButtonClick() {
        startTutorial();
        finish();
        animateSlideIn();
    }

    private void startTutorial() {
        Intent intent = new Intent(this, QuickScreenshotTutorialActivity.class);
        intent.putExtras(getIntent().getExtras());
        startActivity(intent);
    }
}
