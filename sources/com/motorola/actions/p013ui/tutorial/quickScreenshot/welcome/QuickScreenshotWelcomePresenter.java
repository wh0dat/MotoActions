package com.motorola.actions.p013ui.tutorial.quickScreenshot.welcome;

import android.view.View;
import android.view.View.OnClickListener;
import com.motorola.actions.C0504R;

/* renamed from: com.motorola.actions.ui.tutorial.quickScreenshot.welcome.QuickScreenshotWelcomePresenter */
public class QuickScreenshotWelcomePresenter {
    private WelcomeScreenButtonListener mListener;

    public int getDescription() {
        return C0504R.string.quick_screenshot_info;
    }

    public int getLeftButtonText() {
        return C0504R.string.onenav_back_tutorial;
    }

    public int getRightButtonText() {
        return C0504R.string.try_it_out;
    }

    public int getTitle() {
        return C0504R.string.quick_screenshot_enabled;
    }

    public int getVideoResId() {
        return C0504R.raw.screenshot_settings;
    }

    public void setListener(WelcomeScreenButtonListener welcomeScreenButtonListener) {
        this.mListener = welcomeScreenButtonListener;
    }

    public OnClickListener getOnLeftButtonClick() {
        return new QuickScreenshotWelcomePresenter$$Lambda$0(this);
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$getOnLeftButtonClick$0$QuickScreenshotWelcomePresenter(View view) {
        if (this.mListener != null) {
            this.mListener.onLeftButtonClick();
        }
    }

    public OnClickListener getOnRightButtonClick() {
        return new QuickScreenshotWelcomePresenter$$Lambda$1(this);
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$getOnRightButtonClick$1$QuickScreenshotWelcomePresenter(View view) {
        if (this.mListener != null) {
            this.mListener.onRightButtonClick();
        }
    }
}
