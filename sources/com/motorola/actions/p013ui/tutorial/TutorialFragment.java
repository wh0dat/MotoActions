package com.motorola.actions.p013ui.tutorial;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.motorola.actions.C0504R;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.discovery.fdn.instrumentation.DiscoveryInstrumentation;
import com.motorola.actions.p013ui.FDNAwareFragment;
import com.motorola.actions.utils.MALogger;

/* renamed from: com.motorola.actions.ui.tutorial.TutorialFragment */
public abstract class TutorialFragment extends FDNAwareFragment {
    private static final MALogger LOGGER = new MALogger(TutorialFragment.class);
    protected static final int NO_VIDEO = -1;
    protected Context mContext;
    private boolean mIsFeatureEnabled;
    protected View mRootView;
    protected TextureView mTextureView;
    protected TextView mTitleTextView;
    private VideoListener mVideoListener;

    /* renamed from: com.motorola.actions.ui.tutorial.TutorialFragment$ButtonState */
    private enum ButtonState {
        NO_BUTTON,
        SINGLE_BUTTON,
        TWO_BUTTON
    }

    /* access modifiers changed from: protected */
    public int getDoneBtnTextId() {
        return C0504R.string.done;
    }

    /* access modifiers changed from: protected */
    public abstract FeatureKey getFeatureKey();

    /* access modifiers changed from: protected */
    public int getLayoutId() {
        return C0504R.layout.fragment_tutorial;
    }

    /* access modifiers changed from: protected */
    public int getLeftBtnTextId() {
        return C0504R.string.no_thanks;
    }

    /* access modifiers changed from: protected */
    public int getRightBtnTextId() {
        return C0504R.string.turn_it_on;
    }

    /* access modifiers changed from: protected */
    public abstract int getVideoResId();

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.mContext = getActivity();
        this.mRootView = layoutInflater.inflate(getLayoutId(), viewGroup, false);
        ((TextView) this.mRootView.findViewById(C0504R.C0506id.leftBtn)).setText(getLeftBtnTextId());
        ((FrameLayout) this.mRootView.findViewById(C0504R.C0506id.frame_left_button)).setOnClickListener(new TutorialFragment$$Lambda$0(this));
        Button button = (Button) this.mRootView.findViewById(C0504R.C0506id.rightBtn);
        button.setText(getRightBtnTextId());
        button.setOnClickListener(new TutorialFragment$$Lambda$1(this));
        Button button2 = (Button) this.mRootView.findViewById(C0504R.C0506id.singleButton);
        button2.setText(getDoneBtnTextId());
        button2.setOnClickListener(new TutorialFragment$$Lambda$2(this));
        this.mTextureView = (TextureView) this.mRootView.findViewById(C0504R.C0506id.video);
        if (getVideoResId() != -1) {
            this.mVideoListener = new VideoListener(getActivity().getApplicationContext(), this.mTextureView, getVideoResId());
        } else if (this.mTextureView != null) {
            this.mTextureView.setVisibility(8);
        }
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("onCreateView: is FDN active = ");
        sb.append(getFDNSession().isActive());
        mALogger.mo11957d(sb.toString());
        return this.mRootView;
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$onCreateView$0$TutorialFragment(View view) {
        finish();
    }

    public void onPause() {
        super.onPause();
        if (this.mVideoListener != null) {
            this.mVideoListener.stopVideo();
        }
    }

    public void onResume() {
        super.onResume();
        if (this.mVideoListener != null) {
            this.mVideoListener.startVideo();
        }
    }

    /* access modifiers changed from: protected */
    public void finish() {
        getActivity().finish();
    }

    /* access modifiers changed from: protected */
    public void leftButtonClicked(View view) {
        getActivity().finish();
    }

    /* access modifiers changed from: protected */
    public void rightButtonClicked(View view) {
        if (getFDNSession().isActive()) {
            LOGGER.mo11957d("rightButtonClicked: mIsFDNActive = true");
            DiscoveryInstrumentation.recordChangeFDN(getFeatureKey().ordinal());
        }
        finish();
    }

    /* access modifiers changed from: protected */
    public void updateOnSetting(String str, boolean z) {
        SharedPreferenceManager.putBoolean(str, z);
    }

    /* access modifiers changed from: protected */
    public void setTextTitle(int i) {
        this.mTitleTextView = (TextView) this.mRootView.findViewById(C0504R.C0506id.text_title);
        this.mTitleTextView.setText(i);
    }

    /* access modifiers changed from: protected */
    public void setTextInfo(int i) {
        if (i != -1) {
            TextView textView = (TextView) this.mRootView.findViewById(C0504R.C0506id.text_info);
            textView.setText(i);
            textView.setVisibility(0);
        }
    }

    /* access modifiers changed from: protected */
    public void appendTextInfo(int i) {
        ((TextView) this.mRootView.findViewById(C0504R.C0506id.text_info)).append(getString(i));
    }

    /* access modifiers changed from: protected */
    public void setTextInfo2(int i) {
        if (i != -1) {
            TextView textView = (TextView) this.mRootView.findViewById(C0504R.C0506id.text_info2);
            textView.setText(i);
            textView.setVisibility(0);
        }
    }

    /* access modifiers changed from: protected */
    public void setBulletInfo(String[] strArr) {
        populateBulletInfoItems(this.mContext, (LinearLayout) this.mRootView.findViewById(C0504R.C0506id.tip_list), strArr);
    }

    /* access modifiers changed from: protected */
    public void populateBulletInfoItems(Context context, LinearLayout linearLayout, String[] strArr) {
        linearLayout.removeAllViewsInLayout();
        int length = strArr.length;
        for (int i = 0; i < length; i++) {
            TextView textView = new TextView(context);
            textView.setText(Html.fromHtml(context.getString(C0504R.string.bullet_tutorial_tip, new Object[]{strArr[i]})));
            textView.setTextAppearance(C0504R.style.BulletedText);
            linearLayout.addView(textView);
            setMarginBottom(textView);
        }
        linearLayout.setVisibility(0);
    }

    private static void setMarginBottom(View view) {
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, 10);
        view.setLayoutParams(layoutParams);
    }

    private void setupButtons(ButtonState buttonState) {
        View findViewById = this.mRootView.findViewById(C0504R.C0506id.bottom_bar);
        View findViewById2 = this.mRootView.findViewById(C0504R.C0506id.layout_cmd_two_buttons);
        View findViewById3 = this.mRootView.findViewById(C0504R.C0506id.layout_cmd_single_button);
        switch (buttonState) {
            case NO_BUTTON:
                findViewById.setVisibility(8);
                return;
            case SINGLE_BUTTON:
                findViewById.setVisibility(0);
                findViewById2.setVisibility(8);
                findViewById3.setVisibility(0);
                return;
            case TWO_BUTTON:
                findViewById.setVisibility(0);
                findViewById2.setVisibility(0);
                findViewById3.setVisibility(8);
                return;
            default:
                LOGGER.mo11959e("unknown button state.");
                return;
        }
    }

    /* access modifiers changed from: protected */
    public void showFinalButtons(boolean z) {
        setupButtons(z ? ButtonState.SINGLE_BUTTON : ButtonState.TWO_BUTTON);
        this.mIsFeatureEnabled = z;
    }

    /* access modifiers changed from: protected */
    public void hideButtons() {
        setupButtons(ButtonState.NO_BUTTON);
    }

    /* access modifiers changed from: protected */
    public boolean isFeatureEnabled() {
        return this.mIsFeatureEnabled;
    }
}
