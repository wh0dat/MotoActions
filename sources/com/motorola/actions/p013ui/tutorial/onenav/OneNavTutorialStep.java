package com.motorola.actions.p013ui.tutorial.onenav;

import android.os.Handler;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.C0504R;
import com.motorola.actions.checkin.CommonCheckinAttributes;
import com.motorola.actions.onenav.OneNavHelper;
import com.motorola.actions.p013ui.tutorial.VideoListener;
import com.motorola.actions.settings.updater.OneNavSettingsUpdater;
import com.motorola.actions.utils.MALogger;
import java.lang.ref.WeakReference;

/* renamed from: com.motorola.actions.ui.tutorial.onenav.OneNavTutorialStep */
public class OneNavTutorialStep {
    /* access modifiers changed from: private */
    public static final MALogger LOGGER = new MALogger(OneNavTutorialStep.class);
    private static final int SKIP_STEP_ERROR_COUNT = 5;
    private static final long TUTORIAL_LONG_TIMEOUT = 15000;
    private static final long TUTORIAL_TIMEOUT = 5000;
    private int mDescriptionId;
    private int mErrorCount;
    private int mExpectedKeycode;
    private final Handler mHandler = new Handler();
    private Runnable mRunnableLongTimeout = new Runnable() {
        public void run() {
            TutorialStepFeedbackListener tutorialStepFeedbackListener = (TutorialStepFeedbackListener) OneNavTutorialStep.this.mTutorialStepFeedbackListener.get();
            if (tutorialStepFeedbackListener != null) {
                tutorialStepFeedbackListener.onLongTimeout();
            }
        }
    };
    private Runnable mRunnableTimeout = new Runnable() {
        public void run() {
            OneNavTutorialStep.this.onTimeout();
        }
    };
    private boolean mShouldScheduleTimeouts = true;
    private StepStatus mStepStatus;
    private int mSuccessMessageId;
    private int mTimeoutMessageId;
    private int mTitleId;
    /* access modifiers changed from: private */
    public WeakReference<TutorialStepClickListener> mTutorialStepClickListener;
    /* access modifiers changed from: private */
    public WeakReference<TutorialStepFeedbackListener> mTutorialStepFeedbackListener;
    /* access modifiers changed from: private */
    public TutorialType mTutorialType;
    private int mVideoId;
    private VideoListener mVideoListener;
    private View mView;
    private View mViewStep;
    private View mViewSuccess;
    private int mWhiteColor = ActionsApplication.getAppContext().getColor(17170443);

    public TutorialType getTutorialType() {
        return this.mTutorialType;
    }

    public OneNavTutorialStep setTutorialType(TutorialType tutorialType) {
        this.mTutorialType = tutorialType;
        return this;
    }

    public OneNavTutorialStep setStepStatus(StepStatus stepStatus) {
        this.mStepStatus = stepStatus;
        return this;
    }

    public OneNavTutorialStep setVideoId(int i) {
        this.mVideoId = i;
        return this;
    }

    public OneNavTutorialStep setTitleId(int i) {
        this.mTitleId = i;
        return this;
    }

    public OneNavTutorialStep setDescriptionId(int i) {
        this.mDescriptionId = i;
        return this;
    }

    public OneNavTutorialStep setSuccessMessageId(int i) {
        this.mSuccessMessageId = i;
        return this;
    }

    public OneNavTutorialStep setTimeoutMessageId(int i) {
        this.mTimeoutMessageId = i;
        return this;
    }

    public OneNavTutorialStep setExpectedKeycode(int i) {
        this.mExpectedKeycode = i;
        return this;
    }

    public View getView() {
        View view;
        if (this.mStepStatus == StepStatus.SUCCESS) {
            if (this.mViewSuccess == null) {
                MALogger mALogger = LOGGER;
                StringBuilder sb = new StringBuilder();
                sb.append("inflating ");
                sb.append(this.mTutorialType);
                sb.append(" success layout");
                mALogger.mo11957d(sb.toString());
                this.mViewSuccess = View.inflate(ActionsApplication.getAppContext(), C0504R.layout.fragment_onenav_success, null);
            }
            view = this.mViewSuccess;
            setupButtons(view);
            TextView textView = (TextView) view.findViewById(C0504R.C0506id.text_feedback_success);
            textView.setText(this.mSuccessMessageId);
            textView.setVisibility(0);
            this.mViewSuccess = view;
        } else {
            if (this.mViewStep == null) {
                MALogger mALogger2 = LOGGER;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("inflating ");
                sb2.append(this.mTutorialType);
                sb2.append(" step layout");
                mALogger2.mo11957d(sb2.toString());
                if (this.mTutorialType != TutorialType.OPT_IN) {
                    this.mViewStep = View.inflate(ActionsApplication.getAppContext(), C0504R.layout.fragment_onenav_tutorial, null);
                } else {
                    this.mViewStep = View.inflate(ActionsApplication.getAppContext(), C0504R.layout.fragment_onenav_optin, null);
                }
            }
            view = this.mViewStep;
            if (this.mTutorialType == TutorialType.OPT_IN) {
                setupButtons(view);
            } else {
                loadText(view);
                initVideo(view);
                setupCancelButton(view);
                this.mStepStatus = StepStatus.WAITING_INPUT;
            }
        }
        this.mView = view;
        return view;
    }

    private void initVideo(View view) {
        if (this.mStepStatus != StepStatus.SUCCESS) {
            View findViewById = view.findViewById(C0504R.C0506id.video_placeholder);
            findViewById.setVisibility(0);
            if (this.mVideoListener == null) {
                this.mVideoListener = new VideoListener(ActionsApplication.getAppContext(), (TextureView) view.findViewById(C0504R.C0506id.video), this.mVideoId, true);
                this.mVideoListener.setPlaceholder(findViewById);
            }
        }
    }

    private void loadText(View view) {
        ((TextView) view.findViewById(C0504R.C0506id.text_title)).setText(this.mTitleId);
        TextView textView = (TextView) view.findViewById(C0504R.C0506id.text_info);
        textView.setVisibility(0);
        textView.setText(this.mDescriptionId);
        TextView textView2 = (TextView) view.findViewById(C0504R.C0506id.text_feedback);
        textView2.setText(C0504R.string.onenav_tutorial_try_it);
        textView2.setVisibility(0);
    }

    public VideoListener getVideoListener() {
        return this.mVideoListener;
    }

    private void setupButtons(View view) {
        View findViewById = view.findViewById(C0504R.C0506id.layout_cmd_single_button);
        View findViewById2 = view.findViewById(C0504R.C0506id.layout_cmd_two_buttons);
        if (!OneNavHelper.isOneNavEnabled() || this.mTutorialType != TutorialType.OPT_IN) {
            setupLeftButton(view);
            setupRightButton(view);
            if (findViewById2 != null) {
                findViewById2.setVisibility(0);
            }
            if (findViewById != null) {
                findViewById.setVisibility(8);
                return;
            }
            return;
        }
        setupSingleButton(view);
        if (findViewById2 != null) {
            findViewById2.setVisibility(8);
        }
        if (findViewById != null) {
            findViewById.setVisibility(0);
        }
    }

    private void setupLeftButton(View view) {
        TextView textView = (TextView) view.findViewById(C0504R.C0506id.leftBtn);
        if (textView != null) {
            textView.setBackground(ActionsApplication.getAppContext().getDrawable(C0504R.C0505drawable.button_round_ripple_no_background_onenav_optin));
            textView.setTextColor(this.mWhiteColor);
            textView.setText(this.mTutorialType == TutorialType.OPT_IN ? C0504R.string.no_thanks : C0504R.string.onenav_back_tutorial);
            ((FrameLayout) view.findViewById(C0504R.C0506id.frame_left_button)).setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    MALogger access$200 = OneNavTutorialStep.LOGGER;
                    StringBuilder sb = new StringBuilder();
                    sb.append("onLeftButtonClicked ");
                    sb.append(OneNavTutorialStep.this.mTutorialType);
                    access$200.mo11957d(sb.toString());
                    TutorialStepClickListener tutorialStepClickListener = (TutorialStepClickListener) OneNavTutorialStep.this.mTutorialStepClickListener.get();
                    if (tutorialStepClickListener != null) {
                        tutorialStepClickListener.onLeftButtonClicked(OneNavTutorialStep.this.mTutorialType);
                    }
                }
            });
        }
    }

    private void setupRightButton(View view) {
        Button button = (Button) view.findViewById(C0504R.C0506id.rightBtn);
        if (button != null) {
            button.setBackground(ActionsApplication.getAppContext().getDrawable(C0504R.C0505drawable.button_round_ripple_onenav_optin));
            button.setTextColor(this.mWhiteColor);
            button.setText(this.mTutorialType == TutorialType.OPT_IN ? C0504R.string.turn_it_on : C0504R.string.actions_tutorial_next);
            button.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    MALogger access$200 = OneNavTutorialStep.LOGGER;
                    StringBuilder sb = new StringBuilder();
                    sb.append("onRightButtonClicked ");
                    sb.append(OneNavTutorialStep.this.mTutorialType);
                    access$200.mo11957d(sb.toString());
                    TutorialStepClickListener tutorialStepClickListener = (TutorialStepClickListener) OneNavTutorialStep.this.mTutorialStepClickListener.get();
                    if (tutorialStepClickListener != null) {
                        if (OneNavTutorialStep.this.mTutorialType == TutorialType.OPT_IN) {
                            OneNavSettingsUpdater.getInstance().setEnabledSource(true, CommonCheckinAttributes.KEY_DAILY_ENABLE_SOURCE_TYPE_TUTORIAL);
                        }
                        tutorialStepClickListener.onRightButtonClicked(OneNavTutorialStep.this.mTutorialType);
                    }
                }
            });
        }
    }

    private void setupSingleButton(View view) {
        Button button = (Button) view.findViewById(C0504R.C0506id.singleButton);
        if (button != null) {
            button.setBackground(ActionsApplication.getAppContext().getDrawable(C0504R.C0505drawable.button_round_ripple_onenav_optin));
            button.setTextColor(this.mWhiteColor);
            button.setText(C0504R.string.done);
            button.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    MALogger access$200 = OneNavTutorialStep.LOGGER;
                    StringBuilder sb = new StringBuilder();
                    sb.append("onSingleButtonClicked ");
                    sb.append(OneNavTutorialStep.this.mTutorialType);
                    access$200.mo11957d(sb.toString());
                    TutorialStepClickListener tutorialStepClickListener = (TutorialStepClickListener) OneNavTutorialStep.this.mTutorialStepClickListener.get();
                    if (tutorialStepClickListener != null) {
                        tutorialStepClickListener.onDoneButtonClicked();
                    }
                }
            });
        }
    }

    private void setupCancelButton(View view) {
        ((FrameLayout) view.findViewById(C0504R.C0506id.frame_cancel_button)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                MALogger access$200 = OneNavTutorialStep.LOGGER;
                StringBuilder sb = new StringBuilder();
                sb.append("onCancelButtonClicked ");
                sb.append(OneNavTutorialStep.this.mTutorialType);
                access$200.mo11957d(sb.toString());
                TutorialStepClickListener tutorialStepClickListener = (TutorialStepClickListener) OneNavTutorialStep.this.mTutorialStepClickListener.get();
                if (tutorialStepClickListener != null) {
                    tutorialStepClickListener.onCancelButtonClicked();
                }
            }
        });
    }

    public OneNavTutorialStep setTutorialStepClickListener(WeakReference<TutorialStepClickListener> weakReference) {
        this.mTutorialStepClickListener = weakReference;
        return this;
    }

    public OneNavTutorialStep setTutorialStepFeedbackListener(WeakReference<TutorialStepFeedbackListener> weakReference) {
        this.mTutorialStepFeedbackListener = weakReference;
        return this;
    }

    public void nextStatus() {
        this.mErrorCount = 0;
        this.mStepStatus = this.mStepStatus == StepStatus.WAITING_INPUT ? StepStatus.SUCCESS : StepStatus.DONE;
        if (this.mStepStatus == StepStatus.SUCCESS) {
            removeTimeoutRunnables();
        }
    }

    public void onTimeout() {
        if (this.mStepStatus == StepStatus.WAITING_INPUT) {
            ((TextView) this.mView.findViewById(C0504R.C0506id.text_feedback)).setText(this.mTimeoutMessageId);
        }
    }

    public void onError(CharSequence charSequence) {
        if (this.mStepStatus == StepStatus.WAITING_INPUT) {
            this.mErrorCount++;
            this.mShouldScheduleTimeouts = false;
            if (this.mErrorCount >= 5) {
                this.mErrorCount = 0;
                TutorialStepFeedbackListener tutorialStepFeedbackListener = (TutorialStepFeedbackListener) this.mTutorialStepFeedbackListener.get();
                if (tutorialStepFeedbackListener != null) {
                    tutorialStepFeedbackListener.onConsecutiveErrors();
                }
            }
            if (this.mStepStatus == StepStatus.WAITING_INPUT) {
                ((TextView) this.mView.findViewById(C0504R.C0506id.text_feedback)).setText(charSequence);
                this.mView.findViewById(C0504R.C0506id.video).startAnimation(AnimationUtils.loadAnimation(ActionsApplication.getAppContext(), C0504R.anim.shake));
            }
            removeTimeoutRunnables();
        }
    }

    public void onSelected() {
        if (this.mTutorialType != TutorialType.OPT_IN && this.mShouldScheduleTimeouts) {
            removeTimeoutRunnables();
            postTimeoutRunnables();
        }
        if (this.mView != null) {
            TextView textView = (TextView) this.mView.findViewById(C0504R.C0506id.text_feedback);
            if (textView != null) {
                if (this.mStepStatus == StepStatus.WAITING_INPUT) {
                    textView.setText(C0504R.string.onenav_tutorial_try_it);
                }
                if (this.mTutorialType == TutorialType.OPT_IN) {
                    textView.setVisibility(8);
                } else {
                    textView.setVisibility(0);
                }
            }
        }
    }

    public void onDeselected() {
        removeTimeoutRunnables();
    }

    public boolean isExpectedGesture(int i) {
        return i == this.mExpectedKeycode;
    }

    public StepStatus getStepStatus() {
        return this.mStepStatus;
    }

    private void removeTimeoutRunnables() {
        this.mHandler.removeCallbacks(this.mRunnableTimeout);
        this.mHandler.removeCallbacks(this.mRunnableLongTimeout);
    }

    private void postTimeoutRunnables() {
        this.mHandler.postDelayed(this.mRunnableTimeout, TUTORIAL_TIMEOUT);
        this.mHandler.postDelayed(this.mRunnableLongTimeout, TUTORIAL_LONG_TIMEOUT);
    }
}
