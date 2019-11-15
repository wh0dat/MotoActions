package com.motorola.actions.p013ui.tutorial.onenav;

import android.support.p001v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import com.motorola.actions.C0504R;
import com.motorola.actions.onenav.OneNavHelper;
import com.motorola.actions.reflect.KeyEventPrivateProxy;
import com.motorola.actions.utils.Device;
import com.motorola.actions.utils.MALogger;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

/* renamed from: com.motorola.actions.ui.tutorial.onenav.OneNavPageAdapter */
class OneNavPageAdapter extends PagerAdapter {
    private static final MALogger LOGGER = new MALogger(OneNavPageAdapter.class);
    private WeakReference<TutorialStepClickListener> mTutorialStepClickListener;
    private WeakReference<TutorialStepFeedbackListener> mTutorialStepFeedbackListener;
    private ArrayList<OneNavTutorialStep> mTutorialSteps = new ArrayList<>();

    public int getItemPosition(Object obj) {
        return -2;
    }

    /* access modifiers changed from: 0000 */
    public int getStepBackDescriptionFps(boolean z) {
        return !z ? C0504R.string.onenav_tutorial_swipe_left : C0504R.string.onenav_tutorial_swipe_right;
    }

    /* access modifiers changed from: 0000 */
    public int getStepBackDescriptionSoft(boolean z) {
        return !z ? C0504R.string.onenav_tutorial_swipe_left_soft : C0504R.string.onenav_tutorial_swipe_right_soft;
    }

    /* access modifiers changed from: 0000 */
    public int getStepRecentsDescriptionFps(boolean z) {
        return !z ? C0504R.string.onenav_tutorial_swipe_right : C0504R.string.onenav_tutorial_swipe_left;
    }

    /* access modifiers changed from: 0000 */
    public int getStepRecentsDescriptionSoft(boolean z) {
        return !z ? C0504R.string.onenav_tutorial_swipe_right_soft : C0504R.string.onenav_tutorial_swipe_left_soft;
    }

    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    OneNavPageAdapter(WeakReference<TutorialStepClickListener> weakReference, WeakReference<TutorialStepFeedbackListener> weakReference2) {
        this.mTutorialStepClickListener = weakReference;
        this.mTutorialStepFeedbackListener = weakReference2;
        this.mTutorialSteps.add(createStepHome());
        this.mTutorialSteps.add(createStepBack());
        this.mTutorialSteps.add(createStepRecents());
        this.mTutorialSteps.add(createStepTurnOffScreen());
        this.mTutorialSteps.add(createStepNowOnTap());
        this.mTutorialSteps.add(createStepOptIn());
    }

    public int getCount() {
        return this.mTutorialSteps.size();
    }

    public Object instantiateItem(ViewGroup viewGroup, int i) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("instantiateItem ");
        sb.append(i);
        mALogger.mo11957d(sb.toString());
        OneNavTutorialStep oneNavTutorialStep = (OneNavTutorialStep) this.mTutorialSteps.get(i);
        if (oneNavTutorialStep == null) {
            return null;
        }
        View view = oneNavTutorialStep.getView();
        viewGroup.addView(view);
        return view;
    }

    public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
        viewGroup.removeView((View) obj);
    }

    private OneNavTutorialStep createStepHome() {
        return new OneNavTutorialStep().setStepStatus(StepStatus.WAITING_INPUT).setVideoId(Device.is2017UIDevice() ? C0504R.raw.one_nav_tutorial_home : C0504R.raw.one_nav_tutorial_home_slim).setTitleId(C0504R.string.onenav_tutorial_home_title).setDescriptionId(OneNavHelper.getResourceForType(C0504R.string.onenav_tutorial_home_description, C0504R.string.onenav_tutorial_home_description_soft)).setSuccessMessageId(C0504R.string.onenav_tutorial_success_message_1).setTimeoutMessageId(OneNavHelper.getResourceForType(C0504R.string.onenav_tutorial_timeout_message_1, C0504R.string.onenav_tutorial_timeout_message_1_soft)).setTutorialType(TutorialType.HOME).setExpectedKeycode(KeyEventPrivateProxy.KEYCODE_FPS_ONENAV_TAP).setTutorialStepFeedbackListener(this.mTutorialStepFeedbackListener).setTutorialStepClickListener(this.mTutorialStepClickListener);
    }

    private OneNavTutorialStep createStepBack() {
        boolean isRTL = OneNavHelper.isRTL();
        int i = !isRTL ? Device.is2017UIDevice() ? C0504R.raw.one_nav_tutorial_back : C0504R.raw.one_nav_tutorial_back_slim : Device.is2017UIDevice() ? C0504R.raw.one_nav_tutorial_back_rtl : C0504R.raw.one_nav_tutorial_back_rtl_slim;
        return new OneNavTutorialStep().setStepStatus(StepStatus.WAITING_INPUT).setVideoId(i).setTitleId(C0504R.string.onenav_tutorial_back_title).setDescriptionId(OneNavHelper.getResourceForType(getStepBackDescriptionFps(isRTL), getStepBackDescriptionSoft(isRTL))).setSuccessMessageId(C0504R.string.onenav_tutorial_success_message_2).setTimeoutMessageId(C0504R.string.onenav_tutorial_timeout_message_2).setTutorialType(TutorialType.BACK).setExpectedKeycode(!isRTL ? 282 : 283).setTutorialStepFeedbackListener(this.mTutorialStepFeedbackListener).setTutorialStepClickListener(this.mTutorialStepClickListener);
    }

    private OneNavTutorialStep createStepRecents() {
        boolean isRTL = OneNavHelper.isRTL();
        int i = !isRTL ? Device.is2017UIDevice() ? C0504R.raw.one_nav_tutorial_recents : C0504R.raw.one_nav_tutorial_recents_slim : Device.is2017UIDevice() ? C0504R.raw.one_nav_tutorial_recents_rtl : C0504R.raw.one_nav_tutorial_recents_rtl_slim;
        return new OneNavTutorialStep().setStepStatus(StepStatus.WAITING_INPUT).setVideoId(i).setTitleId(C0504R.string.onenav_tutorial_recents_title).setDescriptionId(OneNavHelper.getResourceForType(getStepRecentsDescriptionFps(isRTL), getStepBackDescriptionSoft(isRTL))).setSuccessMessageId(C0504R.string.onenav_tutorial_success_message_3).setTimeoutMessageId(C0504R.string.onenav_tutorial_timeout_message_3).setTutorialType(TutorialType.RECENTS).setExpectedKeycode(!isRTL ? 283 : 282).setTutorialStepFeedbackListener(this.mTutorialStepFeedbackListener).setTutorialStepClickListener(this.mTutorialStepClickListener);
    }

    private OneNavTutorialStep createStepTurnOffScreen() {
        return new OneNavTutorialStep().setStepStatus(StepStatus.WAITING_INPUT).setVideoId(Device.is2017UIDevice() ? C0504R.raw.one_nav_tutorial_turn_off_screen : C0504R.raw.one_nav_tutorial_turn_off_screen_slim).setTitleId(C0504R.string.onenav_tutorial_turn_off_screen_title).setDescriptionId(OneNavHelper.getResourceForType(C0504R.string.onenav_tutorial_turn_off_screen_description, C0504R.string.onenav_tutorial_turn_off_screen_description_soft)).setSuccessMessageId(C0504R.string.onenav_tutorial_success_message_4).setTimeoutMessageId(C0504R.string.onenav_tutorial_timeout_message_4).setTutorialType(TutorialType.TURN_OFF_SCREEN).setExpectedKeycode(KeyEventPrivateProxy.KEYCODE_FPS_ONENAV_HOLD).setTutorialStepFeedbackListener(this.mTutorialStepFeedbackListener).setTutorialStepClickListener(this.mTutorialStepClickListener);
    }

    private OneNavTutorialStep createStepNowOnTap() {
        return new OneNavTutorialStep().setStepStatus(StepStatus.WAITING_INPUT).setVideoId(Device.is2017UIDevice() ? C0504R.raw.one_nav_tutorial_now_on_tap : C0504R.raw.one_nav_tutorial_now_on_tap_slim).setTitleId(C0504R.string.onenav_tutorial_google_now_title).setDescriptionId(OneNavHelper.getResourceForType(C0504R.string.onenav_tutorial_google_now_description, C0504R.string.onenav_tutorial_google_now_description_soft)).setSuccessMessageId(C0504R.string.onenav_tutorial_success_message_5).setTimeoutMessageId(C0504R.string.onenav_tutorial_timeout_message_5).setTutorialType(TutorialType.NOW_ON_TAP).setExpectedKeycode(KeyEventPrivateProxy.KEYCODE_FPS_ONENAV_HOLD).setTutorialStepFeedbackListener(this.mTutorialStepFeedbackListener).setTutorialStepClickListener(this.mTutorialStepClickListener);
    }

    private OneNavTutorialStep createStepOptIn() {
        return new OneNavTutorialStep().setStepStatus(StepStatus.DONE).setTitleId(C0504R.string.onenav_tutorial_optin_title).setDescriptionId(C0504R.string.onenav_tutorial_optin_description).setTutorialType(TutorialType.OPT_IN).setTutorialStepFeedbackListener(this.mTutorialStepFeedbackListener).setTutorialStepClickListener(this.mTutorialStepClickListener);
    }

    public OneNavTutorialStep getItem(int i) {
        return (OneNavTutorialStep) this.mTutorialSteps.get(i);
    }
}
