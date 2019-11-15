package com.motorola.actions.p013ui.tutorial.microscreen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.p001v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.motorola.actions.C0504R;
import com.motorola.actions.microScreen.MicroScreenService;
import com.motorola.actions.microScreen.model.MicroScreenModel;

/* renamed from: com.motorola.actions.ui.tutorial.microscreen.MicroScreenTutorialFragment */
public class MicroScreenTutorialFragment extends MicroscreenBaseTutorialFragment {
    /* access modifiers changed from: private */
    public MicroScreenTutorialActivity mActivity;
    private final BroadcastReceiver mMicroscreenEventReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                if (MicroScreenService.GESTURE_PERFORMED.equals(intent.getAction())) {
                    MicroScreenTutorialFragment.this.mActivity.showSuccessResultFragment(intent.getStringExtra(MicroScreenService.GESTURE_PERFORMED_DIRECTION));
                    MicroScreenTutorialFragment.this.restoreMSState();
                }
            }
        }
    };
    private boolean mPreviousMSState;
    private boolean mShouldRestoreMSState;

    /* access modifiers changed from: protected */
    public int getVideoResId() {
        return C0504R.raw.microscreen_tutorial_swipe_down;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View onCreateView = super.onCreateView(layoutInflater, viewGroup, bundle);
        this.mActivity = (MicroScreenTutorialActivity) getActivity();
        this.mPreviousMSState = MicroScreenModel.isMicroScreenEnabled();
        setTextTitle(C0504R.string.sh_title);
        setTextInfo(C0504R.string.sh_swipe_down_tutorial_info);
        hideButtons();
        return onCreateView;
    }

    public void onPause() {
        if (this.mShouldRestoreMSState) {
            restoreMSState();
        }
        LocalBroadcastManager.getInstance(this.mActivity).unregisterReceiver(this.mMicroscreenEventReceiver);
        super.onPause();
    }

    /* access modifiers changed from: private */
    public void restoreMSState() {
        MicroScreenModel.saveMicroScreenEnabled(this.mPreviousMSState);
        this.mShouldRestoreMSState = false;
    }

    public void onResume() {
        super.onResume();
        MicroScreenService.stopMicroScreenService();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MicroScreenService.GESTURE_PERFORMED);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(this.mMicroscreenEventReceiver, intentFilter);
        if (!this.mPreviousMSState) {
            MicroScreenModel.saveMicroScreenEnabled(true);
            this.mShouldRestoreMSState = true;
        }
    }
}
