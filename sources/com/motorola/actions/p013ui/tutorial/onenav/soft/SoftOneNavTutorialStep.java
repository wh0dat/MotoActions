package com.motorola.actions.p013ui.tutorial.onenav.soft;

import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.motorola.actions.C0504R;
import com.motorola.actions.onenav.instrumentation.InstrumentationTutorialStep;
import com.motorola.actions.p013ui.tutorial.onenav.StepStatus;
import com.motorola.actions.p013ui.tutorial.onenav.TutorialStepFeedbackListener;
import com.motorola.actions.p013ui.tutorial.onenav.TutorialType;
import com.motorola.actions.reflect.MotorolaSettings;
import java.lang.ref.WeakReference;

/* renamed from: com.motorola.actions.ui.tutorial.onenav.soft.SoftOneNavTutorialStep */
public class SoftOneNavTutorialStep {
    private static final long AUTO_ADVANCE_DELAY = 800;
    private static final int SKIP_STEP_ERROR_COUNT = 5;
    private int mActionMessageId;
    private int mDescriptionId;
    private int mErrorCount;
    private int mExpectedKeycode;
    private final Handler mHandler = new Handler();
    private ImageView mImageView;
    private InstrumentationTutorialStep mInstrumentationTutorialStep;
    private Runnable mRunnableSuccess = new SoftOneNavTutorialStep$$Lambda$0(this);
    private StepStatus mStepStatus;
    private int mSuccessMessageId;
    private TextView mTextDescription;
    private int mTutorialAnimation;
    private WeakReference<TutorialStepFeedbackListener> mTutorialStepFeedbackListener;
    private TutorialType mTutorialType;
    private View mView;

    /* access modifiers changed from: 0000 */
    public TutorialType getTutorialType() {
        return this.mTutorialType;
    }

    /* access modifiers changed from: 0000 */
    public InstrumentationTutorialStep getInstrumentationTutorialStep() {
        return this.mInstrumentationTutorialStep;
    }

    /* access modifiers changed from: 0000 */
    public SoftOneNavTutorialStep setTutorialType(TutorialType tutorialType) {
        this.mTutorialType = tutorialType;
        return this;
    }

    /* access modifiers changed from: 0000 */
    public SoftOneNavTutorialStep setInstrumentationTutorialStep(InstrumentationTutorialStep instrumentationTutorialStep) {
        this.mInstrumentationTutorialStep = instrumentationTutorialStep;
        return this;
    }

    /* access modifiers changed from: 0000 */
    public int getTutorialAnimation() {
        return this.mTutorialAnimation;
    }

    /* access modifiers changed from: 0000 */
    public SoftOneNavTutorialStep setTutorialAnimation(int i) {
        this.mTutorialAnimation = i;
        return this;
    }

    /* access modifiers changed from: 0000 */
    public SoftOneNavTutorialStep setStepStatus(StepStatus stepStatus) {
        this.mStepStatus = stepStatus;
        return this;
    }

    /* access modifiers changed from: 0000 */
    public SoftOneNavTutorialStep setDescriptionId(int i) {
        this.mDescriptionId = i;
        return this;
    }

    /* access modifiers changed from: 0000 */
    public SoftOneNavTutorialStep setSuccessMessageId(int i) {
        this.mSuccessMessageId = i;
        return this;
    }

    /* access modifiers changed from: 0000 */
    public SoftOneNavTutorialStep setActionMessageId(int i) {
        this.mActionMessageId = i;
        return this;
    }

    /* access modifiers changed from: 0000 */
    public SoftOneNavTutorialStep setExpectedKeycode(int i) {
        this.mExpectedKeycode = i;
        return this;
    }

    private void loadText(View view) {
        if (this.mTextDescription == null) {
            this.mTextDescription = (TextView) view.findViewById(C0504R.C0506id.softonenav_tutorial_description);
        }
        this.mTextDescription.setVisibility(0);
        this.mTextDescription.setText(this.mDescriptionId);
    }

    private void loadImage(View view) {
        if (this.mImageView == null) {
            this.mImageView = (ImageView) view.findViewById(C0504R.C0506id.softonenav_image);
        }
        this.mImageView.setVisibility(0);
    }

    /* access modifiers changed from: 0000 */
    public SoftOneNavTutorialStep setTutorialStepFeedbackListener(WeakReference<TutorialStepFeedbackListener> weakReference) {
        this.mTutorialStepFeedbackListener = weakReference;
        return this;
    }

    /* access modifiers changed from: 0000 */
    public void nextStatus() {
        this.mErrorCount = 0;
        this.mStepStatus = this.mStepStatus == StepStatus.WAITING_INPUT ? StepStatus.SUCCESS : StepStatus.DONE;
        TutorialStepFeedbackListener tutorialStepFeedbackListener = (TutorialStepFeedbackListener) this.mTutorialStepFeedbackListener.get();
        if (this.mStepStatus == StepStatus.SUCCESS) {
            this.mHandler.postDelayed(this.mRunnableSuccess, AUTO_ADVANCE_DELAY);
            loadImage(this.mView);
            MotorolaSettings.setSoftOneNavTutorialAnimation(0);
            if (tutorialStepFeedbackListener != null) {
                tutorialStepFeedbackListener.onSuccess();
            }
        } else if (tutorialStepFeedbackListener != null) {
            tutorialStepFeedbackListener.onDone();
        }
    }

    /* access modifiers changed from: 0000 */
    public void onError() {
        if (this.mStepStatus == StepStatus.WAITING_INPUT) {
            this.mErrorCount++;
            if (this.mErrorCount >= 5) {
                this.mErrorCount = 0;
                TutorialStepFeedbackListener tutorialStepFeedbackListener = (TutorialStepFeedbackListener) this.mTutorialStepFeedbackListener.get();
                if (tutorialStepFeedbackListener != null) {
                    tutorialStepFeedbackListener.onConsecutiveErrors();
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void onSelected() {
        if (this.mView != null && this.mTextDescription != null) {
            this.mTextDescription.setTextAppearance(C0504R.style.SoftOneNavCurrentStep);
            this.mTextDescription.setAlpha(1.0f);
            MotorolaSettings.setSoftOneNavTutorialAnimation(this.mTutorialAnimation);
        }
    }

    /* access modifiers changed from: 0000 */
    public void onDeselected() {
        if (this.mStepStatus == StepStatus.DONE) {
            this.mTextDescription.setTextAppearance(C0504R.style.SoftOneNavStepDone);
            this.mTextDescription.setAlpha(0.5f);
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean isExpectedGesture(int i) {
        return i == this.mExpectedKeycode;
    }

    /* access modifiers changed from: 0000 */
    public StepStatus getStepStatus() {
        return this.mStepStatus;
    }

    public SoftOneNavTutorialStep setView(View view) {
        this.mView = view;
        return this;
    }

    public View getView() {
        return this.mView;
    }

    public void init() {
        loadText(this.mView);
    }

    /* access modifiers changed from: 0000 */
    public int getSuccessMessageId() {
        return this.mSuccessMessageId;
    }

    /* access modifiers changed from: 0000 */
    public int getActionMessageId() {
        return this.mActionMessageId;
    }
}
