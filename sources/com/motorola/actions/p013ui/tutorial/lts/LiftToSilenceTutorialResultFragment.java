package com.motorola.actions.p013ui.tutorial.lts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.motorola.actions.C0504R;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.checkin.CommonCheckinAttributes;
import com.motorola.actions.lts.LiftToSilenceService;
import com.motorola.actions.p013ui.tutorial.TutorialFragment;
import com.motorola.actions.settings.updater.LTSSettingsUpdater;
import com.motorola.actions.utils.ActivityUtils;

/* renamed from: com.motorola.actions.ui.tutorial.lts.LiftToSilenceTutorialResultFragment */
public class LiftToSilenceTutorialResultFragment extends TutorialFragment {
    private static final String KEY_RESULT = "result";
    private static final int RESULT_PHONE_FAILURE_NOT_PUT_ON_TABLE = 3;
    private static final int RESULT_PHONE_FAILURE_TIMEOUT = 2;
    private static final int RESULT_PHONE_SUCCESS = 1;
    private static final int TAG_RESTART_DEMO = 1;
    private int mTutorialResult = -1;
    private int mVideoId = C0504R.raw.lift_to_silence;

    /* access modifiers changed from: protected */
    public FeatureKey getFeatureKey() {
        return FeatureKey.PICKUP_TO_STOP_RINGING;
    }

    protected static LiftToSilenceTutorialResultFragment getPhoneResultInstance(boolean z, boolean z2) {
        LiftToSilenceTutorialResultFragment liftToSilenceTutorialResultFragment = new LiftToSilenceTutorialResultFragment();
        Bundle bundle = new Bundle();
        if (z) {
            bundle.putInt("result", 1);
        } else if (z2) {
            bundle.putInt("result", 2);
        } else {
            bundle.putInt("result", 3);
        }
        liftToSilenceTutorialResultFragment.setArguments(bundle);
        return liftToSilenceTutorialResultFragment;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        ActivityUtils.setStatusBarColor(getActivity(), C0504R.color.moto_actions_primary_color);
        int i8 = getArguments().getInt("result");
        this.mTutorialResult = i8;
        int i9 = 2131624425;
        int i10 = 2131624119;
        if (i8 == 1) {
            i7 = C0504R.string.lts_success_title;
            this.mVideoId = -1;
            i = C0504R.string.done;
            i2 = -1;
            i5 = C0504R.string.no_thanks;
            i6 = C0504R.string.turn_it_on;
            i4 = C0504R.string.lts_phone_success_info;
            i3 = -1;
        } else if (i8 != 3) {
            i7 = C0504R.string.lts_failure_title;
            i3 = C0504R.string.lts_failure_bullet1;
            i2 = C0504R.string.lts_failure_bullet2;
            i = -1;
            i6 = i9;
            i5 = i10;
            i4 = 2131624178;
        } else {
            i7 = C0504R.string.lts_place_on_table;
            i3 = -1;
            i2 = -1;
            i = -1;
            i6 = i9;
            i5 = i10;
            i4 = 2131624179;
        }
        View onCreateView = super.onCreateView(layoutInflater, viewGroup, bundle);
        ((TextView) onCreateView.findViewById(C0504R.C0506id.text_title)).setText(i7);
        setTextInfo(i4);
        setTextInfo2(-1);
        if (!(i3 == -1 || i2 == -1)) {
            setBulletInfo(new String[]{getString(i3), getString(i2)});
        }
        setupButtons(onCreateView, i5, i6, i);
        this.mRootView = onCreateView;
        return onCreateView;
    }

    private void setupButtons(View view, int i, int i2, int i3) {
        ((TextView) view.findViewById(C0504R.C0506id.leftBtn)).setText(i);
        View findViewById = view.findViewById(C0504R.C0506id.frame_left_button);
        if (i == C0504R.string.try_it_again) {
            findViewById.setTag(Integer.valueOf(1));
        }
        Button button = (Button) view.findViewById(C0504R.C0506id.rightBtn);
        button.setText(i2);
        if (i2 == C0504R.string.try_it_again) {
            button.setTag(Integer.valueOf(1));
        }
        if (i3 != -1) {
            Button button2 = (Button) view.findViewById(C0504R.C0506id.singleButton);
            button2.setText(i3);
            button2.setOnClickListener(new LiftToSilenceTutorialResultFragment$$Lambda$0(this));
        }
        View findViewById2 = view.findViewById(C0504R.C0506id.layout_cmd_two_buttons);
        View findViewById3 = view.findViewById(C0504R.C0506id.layout_cmd_single_button);
        if (this.mTutorialResult != 1) {
            return;
        }
        if (SharedPreferenceManager.getBoolean(LiftToSilenceService.KEY_ENABLED, false)) {
            setViewVisibilitySafe(findViewById2, 8);
            setViewVisibilitySafe(findViewById3, 0);
            return;
        }
        setViewVisibilitySafe(findViewById2, 0);
        setViewVisibilitySafe(findViewById3, 8);
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$setupButtons$0$LiftToSilenceTutorialResultFragment(View view) {
        ((LiftToSilenceTutorialActivity) getActivity()).finishDemo();
    }

    private void setViewVisibilitySafe(View view, int i) {
        if (view != null) {
            view.setVisibility(i);
        }
    }

    /* access modifiers changed from: protected */
    public void leftButtonClicked(View view) {
        if (view.getTag() != null) {
            ((LiftToSilenceTutorialActivity) getActivity()).restartDemo();
            return;
        }
        if (1 == this.mTutorialResult) {
            LTSSettingsUpdater.getInstance().toggleStatus(false, true, CommonCheckinAttributes.KEY_DAILY_ENABLE_SOURCE_TYPE_TUTORIAL);
        }
        ((LiftToSilenceTutorialActivity) getActivity()).finishDemo();
    }

    /* access modifiers changed from: protected */
    public void rightButtonClicked(View view) {
        if (view.getTag() != null) {
            ((LiftToSilenceTutorialActivity) getActivity()).restartDemo();
            return;
        }
        LTSSettingsUpdater.getInstance().updateFromTutorial();
        getFDNSession().recordChange(FeatureKey.PICKUP_TO_STOP_RINGING);
        ((LiftToSilenceTutorialActivity) getActivity()).finishDemo();
    }

    /* access modifiers changed from: protected */
    public int getVideoResId() {
        return this.mVideoId;
    }
}
