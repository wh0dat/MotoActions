package com.motorola.actions.p013ui.tutorial.foc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.p001v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.motorola.actions.C0504R;
import com.motorola.actions.foc.gesture.event.TorchEvent;
import com.motorola.actions.utils.ActivityUtils;
import com.motorola.actions.utils.MALogger;

/* renamed from: com.motorola.actions.ui.tutorial.foc.FlashOnChopStartTutorialFragment */
public class FlashOnChopStartTutorialFragment extends FlashOnChopBaseTutorial {
    private static final MALogger LOGGER = new MALogger(FlashOnChopStartTutorialFragment.class);
    private static final int MAX_TIME_TO_CHOP_MS = 5000;
    private final BroadcastReceiver mFlashlightEventReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                if (TorchEvent.ACTION_TUTORIAL_TORCH_ON.equals(intent.getAction())) {
                    FlashOnChopTutorialActivity flashOnChopTutorialActivity = (FlashOnChopTutorialActivity) FlashOnChopStartTutorialFragment.this.getActivity();
                    if (intent.getBooleanExtra(TorchEvent.EXTRA_FLASHLIGHT_FROM_ACTIONS, false)) {
                        flashOnChopTutorialActivity.showLightOnResultFragment();
                    } else {
                        flashOnChopTutorialActivity.finish();
                    }
                }
            }
        }
    };
    private final Runnable mTimeout = new FlashOnChopStartTutorialFragment$$Lambda$0(this);

    /* access modifiers changed from: protected */
    public int getVideoResId() {
        return C0504R.raw.chopchop;
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$new$0$FlashOnChopStartTutorialFragment() {
        if (getContext() != null) {
            setTextTitle(C0504R.string.foc_timeout_title);
            setTextInfo(C0504R.string.foc_timeout_info);
            setBulletInfo(getContext().getResources().getStringArray(C0504R.array.foc_tutorial_tip_list));
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View onCreateView = super.onCreateView(layoutInflater, viewGroup, bundle);
        setTextTitle(C0504R.string.foc_title);
        setTextInfo(C0504R.string.foc_info);
        hideButtons();
        return onCreateView;
    }

    public void onViewCreated(View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        ActivityUtils.stretchVideo(this.mTextureView);
    }

    public void onPause() {
        super.onPause();
        this.mRootView.removeCallbacks(this.mTimeout);
        try {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(this.mFlashlightEventReceiver);
        } catch (IllegalArgumentException e) {
            LOGGER.mo11960e("Unable to unregister receiver.", e);
        }
    }

    public void onResume() {
        super.onResume();
        this.mRootView.postDelayed(this.mTimeout, 5000);
        registerFOCLightOnReceiver();
    }

    private void registerFOCLightOnReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(TorchEvent.ACTION_TUTORIAL_TORCH_ON);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(this.mFlashlightEventReceiver, intentFilter);
    }
}
