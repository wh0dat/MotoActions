package com.motorola.actions.p013ui.tutorial.quickScreenshot;

import android.annotation.SuppressLint;
import android.media.MediaActionSound;
import android.os.Bundle;
import android.os.Handler;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.C0504R;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.p013ui.tutorial.TutorialFragment;
import com.motorola.actions.quickscreenshot.QuickScreenshotModel;
import com.motorola.actions.quickscreenshot.service.FeatureManager;
import com.motorola.actions.utils.MALogger;

/* renamed from: com.motorola.actions.ui.tutorial.quickScreenshot.QuickScreenshotTutorialFragment */
public class QuickScreenshotTutorialFragment extends TutorialFragment {
    private static final int LEFT_FINGER_ID = 0;
    /* access modifiers changed from: private */
    public static final MALogger LOGGER = new MALogger(QuickScreenshotTutorialFragment.class);
    private static final int LONGPRESS_TIMEOUT = 400;
    private static final int MAX_INTERVAL_BETWEEN_FINGERS_MS = 150;
    private static final int MIDDLE_FINGER_ID = 1;
    private static final int RIGHT_FINGER_ID = 2;
    /* access modifiers changed from: private */
    public boolean mAbortGesture;
    private Runnable mFingerSyncErrorRunnable = new QuickScreenshotTutorialFragment$$Lambda$1(this);
    private SparseArray<FingerView> mFingerViews = new SparseArray<>();
    private boolean mHadThreeFingers;
    private Handler mHandler = new Handler();
    private boolean mIsEnabled;
    private View mScrollView;
    private Runnable mSuccessRunnable = new QuickScreenshotTutorialFragment$$Lambda$0(this);

    /* renamed from: com.motorola.actions.ui.tutorial.quickScreenshot.QuickScreenshotTutorialFragment$FingerView */
    private final class FingerView {
        private boolean mIsPressed;
        private OnTouchListener mOnTouchListener;
        private ImageView mView;

        /* access modifiers changed from: 0000 */
        public final /* synthetic */ boolean lambda$new$0$QuickScreenshotTutorialFragment$FingerView(View view, MotionEvent motionEvent) {
            view.performClick();
            if (motionEvent.getPointerCount() > 1 && !QuickScreenshotTutorialFragment.this.mAbortGesture) {
                QuickScreenshotTutorialFragment.this.onMultiTouch();
                return true;
            } else if (motionEvent.getAction() == 0 && !this.mIsPressed) {
                MALogger access$300 = QuickScreenshotTutorialFragment.LOGGER;
                StringBuilder sb = new StringBuilder();
                sb.append("Button press: ");
                sb.append(this.mView.getId());
                access$300.mo11957d(sb.toString());
                this.mIsPressed = true;
                QuickScreenshotTutorialFragment.this.onButtonPress();
                return true;
            } else if (motionEvent.getAction() != 1) {
                return false;
            } else {
                MALogger access$3002 = QuickScreenshotTutorialFragment.LOGGER;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Button release: ");
                sb2.append(this.mView.getId());
                access$3002.mo11957d(sb2.toString());
                this.mIsPressed = false;
                QuickScreenshotTutorialFragment.this.onButtonRelease();
                return true;
            }
        }

        public boolean isPressed() {
            return this.mIsPressed;
        }

        @SuppressLint({"ClickableViewAccessibility"})
        private FingerView(ImageView imageView) {
            this.mOnTouchListener = new QuickScreenshotTutorialFragment$FingerView$$Lambda$0(this);
            this.mView = imageView;
            this.mView.setOnTouchListener(this.mOnTouchListener);
        }
    }

    /* access modifiers changed from: protected */
    public int getLayoutId() {
        return C0504R.layout.fragment_tutorial_quick_screenshot;
    }

    /* access modifiers changed from: protected */
    public int getVideoResId() {
        return -1;
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$new$1$QuickScreenshotTutorialFragment() {
        onError(C0504R.string.quick_screenshot_tutorial_error_finger_sync);
    }

    /* access modifiers changed from: protected */
    public FeatureKey getFeatureKey() {
        return FeatureKey.QUICK_SCREENSHOT;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View onCreateView = super.onCreateView(layoutInflater, viewGroup, bundle);
        setTextTitle(C0504R.string.quick_screenshot_tutorial_title);
        setTextInfo(C0504R.string.quick_screenshot_tutorial_description);
        this.mScrollView = this.mRootView.findViewById(C0504R.C0506id.scroller_body);
        setupImageInteraction(onCreateView);
        hideButtons();
        this.mIsEnabled = QuickScreenshotModel.isServiceEnabled();
        return onCreateView;
    }

    public void onResume() {
        super.onResume();
        if (this.mIsEnabled) {
            LOGGER.mo11957d("onResume. Quick Screenshot is active, must disable service");
            FeatureManager.stop(getActivity());
        }
    }

    public void onPause() {
        super.onPause();
        if (this.mIsEnabled) {
            LOGGER.mo11957d("onPause. Quick Screenshot was active, must re-enable service");
            FeatureManager.start(getActivity());
        }
    }

    private void setupImageInteraction(View view) {
        this.mFingerViews.put(0, new FingerView((ImageView) view.findViewById(C0504R.C0506id.image_left_finger)));
        this.mFingerViews.put(1, new FingerView((ImageView) view.findViewById(C0504R.C0506id.image_middle_finger)));
        this.mFingerViews.put(2, new FingerView((ImageView) view.findViewById(C0504R.C0506id.image_right_finger)));
    }

    private void onError(int i) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("Error: ");
        sb.append(getString(i));
        mALogger.mo11957d(sb.toString());
        this.mAbortGesture = true;
        Animation loadAnimation = AnimationUtils.loadAnimation(ActionsApplication.getAppContext(), C0504R.anim.shake);
        loadAnimation.setDuration(60);
        this.mTitleTextView.startAnimation(loadAnimation);
        this.mScrollView.startAnimation(loadAnimation);
        setTextInfo(i);
        setTextTitle(C0504R.string.quick_screenshot_tutorial_error_title);
    }

    /* access modifiers changed from: private */
    /* renamed from: onSuccess */
    public void lambda$new$0$QuickScreenshotTutorialFragment() {
        LOGGER.mo11957d("Success!");
        this.mAbortGesture = true;
        playSound();
        ((QuickScreenshotTutorialActivity) getActivity()).showSuccessResultFragment();
    }

    /* access modifiers changed from: private */
    public void onButtonRelease() {
        this.mHandler.removeCallbacks(this.mSuccessRunnable);
        if (getPressedCount() == 0) {
            if (!this.mAbortGesture && this.mHadThreeFingers) {
                onError(C0504R.string.quick_screenshot_tutorial_error_finger_timing);
            }
            this.mAbortGesture = false;
            this.mHadThreeFingers = false;
        }
    }

    private void playSound() {
        MediaActionSound mediaActionSound = new MediaActionSound();
        mediaActionSound.load(0);
        mediaActionSound.play(0);
    }

    /* access modifiers changed from: private */
    public void onButtonPress() {
        if (!this.mAbortGesture) {
            this.mHandler.removeCallbacks(this.mFingerSyncErrorRunnable);
            this.mHandler.removeCallbacks(this.mSuccessRunnable);
            if (getPressedCount() == this.mFingerViews.size()) {
                this.mHandler.postDelayed(this.mSuccessRunnable, 400);
                this.mHadThreeFingers = true;
            } else {
                this.mHandler.postDelayed(this.mFingerSyncErrorRunnable, 150);
            }
        }
    }

    /* access modifiers changed from: private */
    public void onMultiTouch() {
        this.mAbortGesture = true;
        this.mHandler.removeCallbacks(this.mSuccessRunnable);
        this.mHandler.removeCallbacks(this.mFingerSyncErrorRunnable);
        onError(C0504R.string.quick_screenshot_tutorial_error_finger_position);
    }

    private int getPressedCount() {
        return (((FingerView) this.mFingerViews.get(0)).isPressed() ? 1 : 0) + (((FingerView) this.mFingerViews.get(1)).isPressed() ? 1 : 0) + (((FingerView) this.mFingerViews.get(2)).isPressed() ? 1 : 0);
    }
}
