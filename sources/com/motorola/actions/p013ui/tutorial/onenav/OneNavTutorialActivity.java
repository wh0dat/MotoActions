package com.motorola.actions.p013ui.tutorial.onenav;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.media.AudioAttributes;
import android.media.AudioAttributes.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.p001v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.BadTokenException;
import com.motorola.actions.C0504R;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.discovery.DiscoveryManager;
import com.motorola.actions.onenav.OneNavHelper;
import com.motorola.actions.p013ui.ActionsBaseActivity;
import com.motorola.actions.p013ui.settings.OneNavConflictedServicesDialog;
import com.motorola.actions.p013ui.tutorial.VideoListener;
import com.motorola.actions.reflect.KeyEventPrivateProxy;
import com.motorola.actions.reflect.MotorolaSettings;
import com.motorola.actions.utils.AccessibilityUtils;
import com.motorola.actions.utils.ActivityUtils;
import com.motorola.actions.utils.AndroidResourceAccess;
import com.motorola.actions.utils.MALogger;
import java.lang.ref.WeakReference;
import java.util.Set;

/* renamed from: com.motorola.actions.ui.tutorial.onenav.OneNavTutorialActivity */
public class OneNavTutorialActivity extends ActionsBaseActivity implements OnPageChangeListener, TutorialStepClickListener, TutorialStepFeedbackListener {
    private static final int DELAY_LONG_HOLD = 500;
    private static final MALogger LOGGER = new MALogger(OneNavTutorialActivity.class);
    private OneNavTutorialStep mCurrentTutorialStep;
    private AlertDialog mDialog;
    private int mErrorMessageIndex = 0;
    private CharSequence[] mErrorMessages;
    private final Handler mHandler = new Handler();
    private OneNavPageAdapter mHelpPagerAdapter;
    private boolean mIsOneNavEnabledBeforeTutorial;
    private final Runnable mNowOnTapRunnable = new Runnable() {
        public void run() {
            AudioAttributes build = new Builder().setContentType(4).setUsage(13).build();
            if (!(OneNavTutorialActivity.this.mOneNavLongHoldVibePattern == null || OneNavTutorialActivity.this.mVibrator == null)) {
                OneNavTutorialActivity.this.mVibrator.vibrate(OneNavTutorialActivity.this.mOneNavLongHoldVibePattern, -1, build);
            }
            OneNavTutorialActivity.this.updateStepStatus();
        }
    };
    /* access modifiers changed from: private */
    public long[] mOneNavLongHoldVibePattern;
    private OneNavViewPager mPager;
    private View mRootView;
    private AlertDialog mTalkBackDialog;
    /* access modifiers changed from: private */
    public Vibrator mVibrator;
    private VideoListener mVideoListener;

    public void onBackPressed() {
    }

    public void onDone() {
    }

    public void onPageScrollStateChanged(int i) {
    }

    public void onPageScrolled(int i, float f, int i2) {
    }

    public void onSuccess() {
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mDialog = null;
        ActivityUtils.setFullscreenMode(this);
        setContentView((int) C0504R.layout.activity_tutorial_onenav);
        this.mRootView = findViewById(C0504R.C0506id.tutorial_onenav_root);
        initializeViewPager();
        this.mIsOneNavEnabledBeforeTutorial = OneNavHelper.isOneNavEnabled();
        this.mOneNavLongHoldVibePattern = getOneNavLongHoldVibePattern();
        if (this.mOneNavLongHoldVibePattern != null) {
            this.mVibrator = (Vibrator) getSystemService("vibrator");
        }
        if (getFDNSession().isActive()) {
            DiscoveryManager.getInstance().cancelFDN(FeatureKey.ONE_NAV);
        }
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("onCreate: is FDN active = ");
        sb.append(getFDNSession().isActive());
        mALogger.mo11957d(sb.toString());
    }

    private void showTalkbackDialog() {
        this.mCurrentTutorialStep.onDeselected();
        if (this.mTalkBackDialog == null || !this.mTalkBackDialog.isShowing()) {
            this.mTalkBackDialog = ActivityUtils.showWarningDialog(C0504R.string.talkback_turn_off_title, C0504R.string.talkback_turn_off_description, C0504R.string.talkback_turn_off_button, C0504R.string.no_thanks, new OneNavTutorialActivity$$Lambda$0(this), new OneNavTutorialActivity$$Lambda$1(this), this);
        }
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$showTalkbackDialog$0$OneNavTutorialActivity(DialogInterface dialogInterface, int i) {
        dialogInterface.dismiss();
        AccessibilityUtils.openAccessibilitySettings(this);
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$showTalkbackDialog$1$OneNavTutorialActivity(DialogInterface dialogInterface, int i) {
        dialogInterface.dismiss();
        finish();
    }

    private long[] getOneNavLongHoldVibePattern() {
        return (long[]) AndroidResourceAccess.getArrayResource("config_oneNavLongHoldVibePattern").map(OneNavTutorialActivity$$Lambda$2.$instance).orElse(null);
    }

    private void initializeViewPager() {
        this.mHelpPagerAdapter = new OneNavPageAdapter(new WeakReference(this), new WeakReference(this));
        this.mPager = (OneNavViewPager) findViewById(C0504R.C0506id.onenav_view_pager);
        this.mPager.setAdapter(this.mHelpPagerAdapter);
        this.mPager.addOnPageChangeListener(this);
        this.mPager.setOffscreenPageLimit(this.mHelpPagerAdapter.getCount());
        this.mPager.setAllowSwiping(false);
        this.mCurrentTutorialStep = this.mHelpPagerAdapter.getItem(0);
        this.mCurrentTutorialStep.onSelected();
        this.mVideoListener = this.mCurrentTutorialStep.getVideoListener();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        Set conflictServicesEnabled = OneNavHelper.getConflictServicesEnabled();
        if (conflictServicesEnabled.isEmpty()) {
            LOGGER.mo11957d("changeStatus - No FP gestures services enabled");
            setupTutorialMode();
            return;
        }
        OneNavConflictedServicesDialog.showConflictedServicesTurnOffDialog(this, conflictServicesEnabled, new OneNavTutorialActivity$$Lambda$3(this, conflictServicesEnabled), new OneNavTutorialActivity$$Lambda$4(this));
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$onResume$3$OneNavTutorialActivity(Set set) {
        OneNavHelper.turnOffAccessibilityServices(set);
        setupTutorialMode();
    }

    private void setupTutorialMode() {
        MotorolaSettings.setOneNavTutorialActive();
        if (OneNavHelper.getOneNavType() == 1) {
            ActivityUtils.setSetupWizardMode(this.mRootView, this);
        }
        setupTutorialDialog();
    }

    public void onPageSelected(int i) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("onPageSelected ");
        sb.append(i);
        sb.append(" mVideoListener ");
        sb.append(this.mVideoListener);
        mALogger.mo11957d(sb.toString());
        this.mErrorMessageIndex = 0;
        if (this.mCurrentTutorialStep != null) {
            this.mCurrentTutorialStep.onDeselected();
        }
        this.mCurrentTutorialStep = this.mHelpPagerAdapter.getItem(i);
        if (this.mCurrentTutorialStep != null) {
            this.mCurrentTutorialStep.onSelected();
            if (this.mVideoListener != null && this.mVideoListener.isPlayingVideo()) {
                this.mVideoListener.pauseVideo();
            }
            this.mVideoListener = this.mCurrentTutorialStep.getVideoListener();
            if (this.mVideoListener != null) {
                this.mVideoListener.setPlaceholderDelayedVisibility(0, 0);
                this.mVideoListener.setPlaceholderDelayedVisibility(8, 400);
                this.mVideoListener.playVideo();
            }
        }
    }

    public void onPause() {
        super.onPause();
        if (this.mVideoListener != null) {
            this.mVideoListener.stopVideo();
        }
        MotorolaSettings.setOneNavTutorialInactive();
    }

    public void onStart() {
        super.onStart();
        if (this.mVideoListener != null) {
            this.mVideoListener.startVideo();
        }
        if (this.mCurrentTutorialStep != null) {
            this.mCurrentTutorialStep.onSelected();
        }
        if (OneNavHelper.isSoftOneNav() && AccessibilityUtils.isTalkbackActive()) {
            showTalkbackDialog();
        }
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        MotorolaSettings.setOneNavTutorialInactive();
        if (this.mCurrentTutorialStep != null) {
            this.mCurrentTutorialStep.onDeselected();
        }
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("keycode ");
        sb.append(i);
        mALogger.mo11957d(sb.toString());
        if (this.mCurrentTutorialStep.getStepStatus() == StepStatus.WAITING_INPUT && isFPSKeyCode(i)) {
            if (this.mCurrentTutorialStep.getTutorialType() == TutorialType.NOW_ON_TAP) {
                handleNowOnTap(i);
            } else if (i != KeyEventPrivateProxy.KEYCODE_FPS_ONENAV_UP) {
                if (this.mCurrentTutorialStep.isExpectedGesture(i)) {
                    updateStepStatus();
                } else {
                    showErrorMessage();
                }
            }
        }
        return super.onKeyDown(i, keyEvent);
    }

    private boolean isFPSKeyCode(int i) {
        return i == 282 || i == 283 || i == KeyEventPrivateProxy.KEYCODE_FPS_ONENAV_TAP || i == KeyEventPrivateProxy.KEYCODE_FPS_ONENAV_UP || i == KeyEventPrivateProxy.KEYCODE_FPS_ONENAV_HOLD;
    }

    private void handleNowOnTap(int i) {
        if (i != KeyEventPrivateProxy.KEYCODE_FPS_ONENAV_HOLD) {
            showErrorMessage();
            this.mHandler.removeCallbacks(this.mNowOnTapRunnable);
            return;
        }
        this.mHandler.postDelayed(this.mNowOnTapRunnable, 500);
    }

    private void showErrorMessage() {
        if (this.mErrorMessages == null) {
            this.mErrorMessages = getResources().getTextArray(C0504R.array.onenav_tutorial_error_messages);
        }
        this.mCurrentTutorialStep.onError(this.mErrorMessages[this.mErrorMessageIndex]);
        this.mErrorMessageIndex++;
        if (this.mErrorMessageIndex >= this.mErrorMessages.length) {
            this.mErrorMessageIndex = 0;
        }
    }

    /* access modifiers changed from: private */
    public void updateStepStatus() {
        LOGGER.mo11957d("updateStepStatus");
        this.mCurrentTutorialStep.nextStatus();
        this.mHelpPagerAdapter.notifyDataSetChanged();
        this.mPager.setAllowSwiping(false);
    }

    private void updateOneNavAndExit(int i) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("updateOneNavAndExit - OneNav status: ");
        sb.append(i);
        mALogger.mo11957d(sb.toString());
        MALogger mALogger2 = LOGGER;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("updateOneNavAndExit - mIsOneNavEnabledBeforeTutorial: ");
        sb2.append(this.mIsOneNavEnabledBeforeTutorial);
        mALogger2.mo11957d(sb2.toString());
        boolean z = this.mIsOneNavEnabledBeforeTutorial != (i == 1);
        MotorolaSettings.setOneNavEnabled(i, z);
        if (i == 1) {
            DiscoveryManager.getInstance().setDiscoveryStatus(FeatureKey.ONE_NAV, 0);
        }
        if (z) {
            getFDNSession().recordChange(FeatureKey.ONE_NAV);
        }
        finish();
    }

    public void onLeftButtonClicked(TutorialType tutorialType) {
        if (tutorialType == TutorialType.OPT_IN) {
            updateOneNavAndExit(0);
            return;
        }
        this.mCurrentTutorialStep.setStepStatus(StepStatus.WAITING_INPUT);
        this.mHelpPagerAdapter.notifyDataSetChanged();
    }

    public void onRightButtonClicked(TutorialType tutorialType) {
        if (tutorialType != TutorialType.OPT_IN) {
            goToNextStep();
        } else {
            updateOneNavAndExit(1);
        }
    }

    public void onCancelButtonClicked() {
        finish();
    }

    public void onDoneButtonClicked() {
        finish();
    }

    private void goToNextStep() {
        LOGGER.mo11957d("goToNextStep");
        this.mCurrentTutorialStep.nextStatus();
        this.mPager.setCurrentItem(this.mPager.getCurrentItem() + 1);
    }

    public void onConsecutiveErrors() {
        showFeedbackDialog(C0504R.string.onenav_skip_step_error_description, C0504R.string.actions_tutorial_next, C0504R.string.onenav_skip_step_error_keep_trying, new OneNavTutorialActivity$$Lambda$5(this), new OneNavTutorialActivity$$Lambda$6(this));
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$onConsecutiveErrors$4$OneNavTutorialActivity(DialogInterface dialogInterface, int i) {
        dialogInterface.dismiss();
        goToNextStep();
        setupTutorialMode();
        if (this.mVideoListener != null) {
            LOGGER.mo11957d("onConsecutiveErrors(): Set placeholder as gone");
            this.mVideoListener.setPlaceholderDelayedVisibility(8, 0);
        }
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$onConsecutiveErrors$5$OneNavTutorialActivity(DialogInterface dialogInterface, int i) {
        dialogInterface.dismiss();
        setupTutorialMode();
    }

    public void onLongTimeout() {
        showFeedbackDialog(OneNavHelper.getResourceForType(C0504R.string.onenav_skip_tutorial_description, C0504R.string.onenav_skip_tutorial_description_soft), C0504R.string.actions_dialog_ok, C0504R.string.onenav_skip_tutorial_try_again_later, new OneNavTutorialActivity$$Lambda$7(this), new OneNavTutorialActivity$$Lambda$8(this));
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$onLongTimeout$6$OneNavTutorialActivity(DialogInterface dialogInterface, int i) {
        dialogInterface.dismiss();
        setupTutorialMode();
        this.mCurrentTutorialStep.onSelected();
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$onLongTimeout$7$OneNavTutorialActivity(DialogInterface dialogInterface, int i) {
        dialogInterface.dismiss();
        finish();
    }

    private void showFeedbackDialog(int i, int i2, int i3, OnClickListener onClickListener, OnClickListener onClickListener2) {
        if (this.mDialog == null) {
            this.mDialog = new AlertDialog.Builder(this).setCancelable(false).create();
        }
        this.mDialog.setMessage(getString(i));
        this.mDialog.setButton(-1, getString(i2), onClickListener);
        if (this.mDialog.getButton(-1) != null) {
            this.mDialog.getButton(-1).setText(getString(i2));
        }
        this.mDialog.setButton(-2, getString(i3), onClickListener2);
        if (this.mDialog.getButton(-2) != null) {
            this.mDialog.getButton(-2).setText(getString(i3));
        }
        if (!this.mDialog.isShowing()) {
            try {
                showTutorialDialog();
            } catch (BadTokenException e) {
                LOGGER.mo11959e(e.getMessage());
            }
        }
    }

    private void setupTutorialDialog() {
        if (this.mDialog != null && this.mDialog.isShowing()) {
            applyUiVisibilityToDialog();
        }
    }

    private void showTutorialDialog() {
        Window window = this.mDialog.getWindow();
        if (window != null) {
            window.setFlags(8, 8);
            this.mDialog.show();
            applyUiVisibilityToDialog();
            window.clearFlags(8);
        }
    }

    private void applyUiVisibilityToDialog() {
        Window window = this.mDialog.getWindow();
        if (window != null) {
            window.getDecorView().setSystemUiVisibility(this.mRootView.getSystemUiVisibility());
        }
    }
}
