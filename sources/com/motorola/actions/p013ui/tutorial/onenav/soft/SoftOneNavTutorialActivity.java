package com.motorola.actions.p013ui.tutorial.onenav.soft;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.AudioAttributes.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.BadTokenException;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import com.motorola.actions.C0504R;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.discovery.DiscoveryManager;
import com.motorola.actions.discovery.highlight.FeatureHighlightKey;
import com.motorola.actions.onenav.OneNavHelper;
import com.motorola.actions.onenav.instrumentation.InstrumentationTutorialStep;
import com.motorola.actions.onenav.instrumentation.OneNavInstrumentation;
import com.motorola.actions.p013ui.ActionsBaseActivity;
import com.motorola.actions.p013ui.tutorial.onenav.StepStatus;
import com.motorola.actions.p013ui.tutorial.onenav.TutorialStepFeedbackListener;
import com.motorola.actions.p013ui.tutorial.onenav.TutorialType;
import com.motorola.actions.reflect.KeyEventPrivateProxy;
import com.motorola.actions.reflect.MotorolaSettings;
import com.motorola.actions.utils.AccessibilityUtils;
import com.motorola.actions.utils.ActivityUtils;
import com.motorola.actions.utils.AndroidResourceAccess;
import com.motorola.actions.utils.MALogger;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

/* renamed from: com.motorola.actions.ui.tutorial.onenav.soft.SoftOneNavTutorialActivity */
public class SoftOneNavTutorialActivity extends ActionsBaseActivity implements TutorialStepFeedbackListener {
    private static final int DELAY_LONG_HOLD = 500;
    private static final MALogger LOGGER = new MALogger(SoftOneNavTutorialActivity.class);
    private static final int MAX_STEPS_LARGE_DISPLAY = 3;
    private int mCurrentStepIndex = 0;
    private SoftOneNavTutorialStep mCurrentTutorialStep;
    private AlertDialog mDialog;
    private final Handler mHandler = new Handler();
    private ImageView mImageViewFeedback;
    private final Runnable mNowOnTapRunnable = new Runnable() {
        public void run() {
            AudioAttributes build = new Builder().setContentType(4).setUsage(13).build();
            if (!(SoftOneNavTutorialActivity.this.mOneNavLongHoldVibePattern == null || SoftOneNavTutorialActivity.this.mVibrator == null)) {
                SoftOneNavTutorialActivity.this.mVibrator.vibrate(SoftOneNavTutorialActivity.this.mOneNavLongHoldVibePattern, -1, build);
            }
            SoftOneNavTutorialActivity.this.updateStepStatus();
        }
    };
    /* access modifiers changed from: private */
    public long[] mOneNavLongHoldVibePattern;
    private AlertDialog mTalkBackDialog;
    private TextView mTextViewFeedback;
    private TextView mTextViewTitle;
    private ArrayList<SoftOneNavTutorialStep> mTutorialSteps;
    /* access modifiers changed from: private */
    public Vibrator mVibrator;

    public void onBackPressed() {
    }

    public void onLongTimeout() {
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mDialog = null;
        setContentView((int) C0504R.layout.activity_tutorial_softonenav);
        View findViewById = findViewById(C0504R.C0506id.tutorial_onenav_root);
        setupCancelButton(findViewById);
        DiscoveryManager.getInstance().cancelHighlight(FeatureHighlightKey.SOFTONENAV);
        this.mTextViewTitle = (TextView) findViewById.findViewById(C0504R.C0506id.softonenav_tutorial_title);
        this.mTextViewFeedback = (TextView) findViewById.findViewById(C0504R.C0506id.softonenav_tutorial_feedback_text);
        this.mImageViewFeedback = (ImageView) findViewById.findViewById(C0504R.C0506id.softonenav_tutorial_feedback_image);
        this.mOneNavLongHoldVibePattern = getOneNavLongHoldVibePattern();
        if (this.mOneNavLongHoldVibePattern != null) {
            this.mVibrator = (Vibrator) getSystemService("vibrator");
        }
        if (getFDNSession().isActive()) {
            DiscoveryManager.getInstance().cancelFDN(FeatureKey.ONE_NAV);
        }
        initializeSteps();
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("onCreate: is FDN active = ");
        sb.append(getFDNSession().isActive());
        mALogger.mo11957d(sb.toString());
    }

    private void showTalkbackDialog() {
        this.mCurrentTutorialStep.onDeselected();
        if (this.mTalkBackDialog == null || !this.mTalkBackDialog.isShowing()) {
            this.mTalkBackDialog = ActivityUtils.showWarningDialog(C0504R.string.talkback_turn_off_title, C0504R.string.talkback_turn_off_description, C0504R.string.talkback_turn_off_button, C0504R.string.no_thanks, new SoftOneNavTutorialActivity$$Lambda$0(this), new SoftOneNavTutorialActivity$$Lambda$1(this), this);
        }
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$showTalkbackDialog$0$SoftOneNavTutorialActivity(DialogInterface dialogInterface, int i) {
        dialogInterface.dismiss();
        AccessibilityUtils.openAccessibilitySettings(this);
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$showTalkbackDialog$1$SoftOneNavTutorialActivity(DialogInterface dialogInterface, int i) {
        dialogInterface.dismiss();
        finish();
    }

    private long[] getOneNavLongHoldVibePattern() {
        return (long[]) AndroidResourceAccess.getArrayResource("config_oneNavLongHoldVibePattern").map(SoftOneNavTutorialActivity$$Lambda$2.$instance).orElse(null);
    }

    private void initializeSteps() {
        this.mTutorialSteps = new ArrayList<>();
        this.mTutorialSteps.add(createStepHome());
        this.mTutorialSteps.add(createStepBack());
        this.mTutorialSteps.add(createStepSwitch());
        this.mTutorialSteps.add(createStepRecents());
        this.mTutorialSteps.add(createStepNowOnTap());
        Iterator it = this.mTutorialSteps.iterator();
        while (it.hasNext()) {
            ((SoftOneNavTutorialStep) it.next()).init();
        }
        this.mCurrentTutorialStep = (SoftOneNavTutorialStep) this.mTutorialSteps.get(this.mCurrentStepIndex);
        this.mCurrentTutorialStep.onSelected();
        updateFeedbackText(this.mCurrentTutorialStep.getActionMessageId(), 0, C0504R.color.moto_text_title_color);
    }

    private SoftOneNavTutorialStep createStepHome() {
        return new SoftOneNavTutorialStep().setStepStatus(StepStatus.WAITING_INPUT).setDescriptionId(C0504R.string.onenav_tutorial_step_soft_home).setSuccessMessageId(C0504R.string.onenav_tutorial_success_message_1).setActionMessageId(C0504R.string.onenav_tutorial_step_soft_action_tap).setTutorialType(TutorialType.HOME).setInstrumentationTutorialStep(InstrumentationTutorialStep.HOME).setTutorialAnimation(1).setTutorialStepFeedbackListener(new WeakReference(this)).setView(findViewById(C0504R.C0506id.softonenav_tutorial_step_home)).setExpectedKeycode(KeyEventPrivateProxy.KEYCODE_FPS_ONENAV_TAP);
    }

    private SoftOneNavTutorialStep createStepBack() {
        return new SoftOneNavTutorialStep().setStepStatus(StepStatus.WAITING_INPUT).setDescriptionId(!OneNavHelper.isRTL() ? C0504R.string.onenav_tutorial_step_soft_left_back : C0504R.string.onenav_tutorial_step_soft_right_back).setSuccessMessageId(C0504R.string.onenav_tutorial_success_message_2).setActionMessageId(!OneNavHelper.isRTL() ? C0504R.string.onenav_tutorial_step_soft_action_swipe_left : C0504R.string.onenav_tutorial_step_soft_action_swipe_right).setTutorialType(TutorialType.BACK).setInstrumentationTutorialStep(InstrumentationTutorialStep.BACK).setTutorialAnimation(!OneNavHelper.isRTL() ? 2 : 3).setTutorialStepFeedbackListener(new WeakReference(this)).setView(findViewById(C0504R.C0506id.softonenav_tutorial_step_left)).setExpectedKeycode(!OneNavHelper.isRTL() ? 282 : 283);
    }

    private SoftOneNavTutorialStep createStepSwitch() {
        return new SoftOneNavTutorialStep().setStepStatus(StepStatus.WAITING_INPUT).setDescriptionId(!OneNavHelper.isRTL() ? C0504R.string.onenav_tutorial_step_soft_right_switch : C0504R.string.onenav_tutorial_step_soft_left_switch).setSuccessMessageId(C0504R.string.onenav_tutorial_success_message_6).setActionMessageId(!OneNavHelper.isRTL() ? C0504R.string.onenav_tutorial_step_soft_action_swipe_right : C0504R.string.onenav_tutorial_step_soft_action_swipe_left).setTutorialType(TutorialType.SWITCH).setInstrumentationTutorialStep(InstrumentationTutorialStep.SWITCH).setTutorialAnimation(!OneNavHelper.isRTL() ? 3 : 2).setTutorialStepFeedbackListener(new WeakReference(this)).setView(findViewById(C0504R.C0506id.softonenav_tutorial_step_up)).setExpectedKeycode(!OneNavHelper.isRTL() ? 283 : 282);
    }

    private SoftOneNavTutorialStep createStepRecents() {
        return new SoftOneNavTutorialStep().setStepStatus(StepStatus.WAITING_INPUT).setDescriptionId(C0504R.string.onenav_tutorial_step_soft_up_recents).setSuccessMessageId(C0504R.string.onenav_tutorial_success_message_3).setActionMessageId(C0504R.string.onenav_tutorial_step_soft_action_swipe_up).setTutorialType(TutorialType.RECENTS).setInstrumentationTutorialStep(InstrumentationTutorialStep.RECENTS).setTutorialAnimation(5).setTutorialStepFeedbackListener(new WeakReference(this)).setView(findViewById(C0504R.C0506id.softonenav_tutorial_step_right)).setExpectedKeycode(280);
    }

    private SoftOneNavTutorialStep createStepNowOnTap() {
        return new SoftOneNavTutorialStep().setStepStatus(StepStatus.WAITING_INPUT).setDescriptionId(C0504R.string.onenav_tutorial_step_soft_assistant).setSuccessMessageId(C0504R.string.onenav_tutorial_success_message_5).setActionMessageId(C0504R.string.onenav_tutorial_step_soft_action_hold).setTutorialType(TutorialType.NOW_ON_TAP).setInstrumentationTutorialStep(InstrumentationTutorialStep.NOW_ON_TAP).setTutorialAnimation(4).setTutorialStepFeedbackListener(new WeakReference(this)).setView(findViewById(C0504R.C0506id.softonenav_tutorial_step_assistant)).setExpectedKeycode(KeyEventPrivateProxy.KEYCODE_FPS_ONENAV_HOLD);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        MotorolaSettings.setOneNavTutorialActive();
    }

    public void onPause() {
        super.onPause();
        MotorolaSettings.setOneNavTutorialInactive();
    }

    public void onStart() {
        super.onStart();
        if (this.mCurrentTutorialStep != null) {
            this.mCurrentTutorialStep.onSelected();
            updateFeedbackText(this.mCurrentTutorialStep.getActionMessageId(), 0, C0504R.color.moto_text_title_color);
        }
        if (AccessibilityUtils.isTalkbackActive()) {
            showTalkbackDialog();
        }
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        MotorolaSettings.setOneNavTutorialInactive();
        MotorolaSettings.setSoftOneNavTutorialAnimation(0);
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
        if (this.mCurrentTutorialStep.getStepStatus() == StepStatus.WAITING_INPUT && ((this.mDialog == null || !this.mDialog.isShowing()) && isFPSKeyCode(i))) {
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
        return i == 282 || i == 283 || i == 280 || i == KeyEventPrivateProxy.KEYCODE_FPS_ONENAV_TAP || i == KeyEventPrivateProxy.KEYCODE_FPS_ONENAV_UP || i == KeyEventPrivateProxy.KEYCODE_FPS_ONENAV_HOLD;
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
        if (this.mVibrator != null) {
            this.mVibrator.vibrate(new long[]{0, 100, 100, 100}, -1);
        }
        this.mCurrentTutorialStep.onError();
        updateFeedbackText(C0504R.string.onenav_tutorial_soft_error, 2, C0504R.color.moto_text_title_color);
    }

    /* access modifiers changed from: private */
    public void updateStepStatus() {
        LOGGER.mo11957d("updateStepStatus");
        this.mCurrentTutorialStep.nextStatus();
    }

    private void goToNextStep() {
        LOGGER.mo11957d("goToNextStep");
        this.mCurrentTutorialStep.nextStatus();
    }

    public void onConsecutiveErrors() {
        SoftOneNavTutorialActivity$$Lambda$3 softOneNavTutorialActivity$$Lambda$3 = new SoftOneNavTutorialActivity$$Lambda$3(this);
        SoftOneNavTutorialActivity$$Lambda$4 softOneNavTutorialActivity$$Lambda$4 = new SoftOneNavTutorialActivity$$Lambda$4(this);
        MotorolaSettings.setSoftOneNavTutorialAnimation(0);
        showFeedbackDialog(C0504R.string.onenav_skip_step_error_description, C0504R.string.actions_tutorial_next, C0504R.string.onenav_skip_step_error_keep_trying, softOneNavTutorialActivity$$Lambda$3, softOneNavTutorialActivity$$Lambda$4);
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$onConsecutiveErrors$3$SoftOneNavTutorialActivity(DialogInterface dialogInterface, int i) {
        dialogInterface.dismiss();
        goToNextStep();
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$onConsecutiveErrors$4$SoftOneNavTutorialActivity(DialogInterface dialogInterface, int i) {
        MotorolaSettings.setSoftOneNavTutorialAnimation(this.mCurrentTutorialStep.getTutorialAnimation());
        dialogInterface.dismiss();
    }

    public void onSuccess() {
        if (this.mCurrentStepIndex == this.mTutorialSteps.size() - 1) {
            this.mTextViewTitle.setText(C0504R.string.onenav_tutorial_optin_title);
        }
        this.mImageViewFeedback.setVisibility(0);
        updateFeedbackText(this.mCurrentTutorialStep.getSuccessMessageId(), 0, C0504R.color.parasailing_500);
        if (this.mCurrentStepIndex == 2) {
            ScrollView scrollView = (ScrollView) findViewById(C0504R.C0506id.softonenav_tutorial_steps_scroll);
            scrollView.setSmoothScrollingEnabled(true);
            scrollView.post(new SoftOneNavTutorialActivity$$Lambda$5(scrollView));
        }
    }

    public void onDone() {
        this.mCurrentTutorialStep.onDeselected();
        this.mCurrentStepIndex++;
        OneNavInstrumentation.recordOneNavTutorialStep(this.mCurrentTutorialStep.getInstrumentationTutorialStep());
        if (this.mCurrentStepIndex < this.mTutorialSteps.size()) {
            this.mImageViewFeedback.setVisibility(4);
            this.mCurrentTutorialStep = (SoftOneNavTutorialStep) this.mTutorialSteps.get(this.mCurrentStepIndex);
            this.mCurrentTutorialStep.onSelected();
            updateFeedbackText(this.mCurrentTutorialStep.getActionMessageId(), 0, C0504R.color.moto_text_title_color);
            return;
        }
        startActivity(new Intent(this, SoftOneNavOptinActivity.class));
        overridePendingTransition(C0504R.anim.splash_slide_in_anim_set, C0504R.anim.splash_slide_out_anim_set);
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

    private void showTutorialDialog() {
        Window window = this.mDialog.getWindow();
        if (window != null) {
            window.setFlags(8, 8);
            this.mDialog.show();
        }
    }

    private void setupCancelButton(View view) {
        ((FrameLayout) view.findViewById(C0504R.C0506id.frame_cancel_button)).setOnClickListener(new SoftOneNavTutorialActivity$$Lambda$6(this));
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$setupCancelButton$6$SoftOneNavTutorialActivity(View view) {
        LOGGER.mo11957d("onCancelButtonClicked");
        finish();
    }

    private void updateFeedbackText(int i, int i2, int i3) {
        this.mTextViewFeedback.setText(i);
        this.mTextViewFeedback.setTypeface(Typeface.defaultFromStyle(i2), i2);
        this.mTextViewFeedback.setTextColor(getColor(i3));
    }
}
