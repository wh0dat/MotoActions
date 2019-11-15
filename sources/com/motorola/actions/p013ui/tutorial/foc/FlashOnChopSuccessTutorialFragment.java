package com.motorola.actions.p013ui.tutorial.foc;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.motorola.actions.C0504R;
import com.motorola.actions.checkin.CommonCheckinAttributes;
import com.motorola.actions.settings.updater.FOCSettingsUpdater;
import com.motorola.actions.utils.ActivityUtils;

/* renamed from: com.motorola.actions.ui.tutorial.foc.FlashOnChopSuccessTutorialFragment */
public class FlashOnChopSuccessTutorialFragment extends FlashOnChopBaseTutorial {
    /* access modifiers changed from: protected */
    public int getVideoResId() {
        return -1;
    }

    /* access modifiers changed from: protected */
    public void leftButtonClicked(View view) {
        FOCSettingsUpdater.getInstance().toggleStatus(false, true, CommonCheckinAttributes.KEY_DAILY_ENABLE_SOURCE_TYPE_TUTORIAL);
        super.leftButtonClicked(view);
    }

    /* access modifiers changed from: protected */
    public void finish() {
        if (!isFeatureEnabled()) {
            FOCSettingsUpdater.getInstance().toggleStatus(true, true, CommonCheckinAttributes.KEY_DAILY_ENABLE_SOURCE_TYPE_TUTORIAL);
        }
        super.finish();
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View onCreateView = super.onCreateView(layoutInflater, viewGroup, bundle);
        setTextTitle(C0504R.string.foc_success_title);
        setTextInfo(C0504R.string.foc_phone_success_info);
        setBulletInfo(getContext().getResources().getStringArray(C0504R.array.foc_tutorial_tip_list));
        showFinalButtons(((FlashOnChopTutorialActivity) getActivity()).getPreviousFOCState());
        return onCreateView;
    }

    public void onViewCreated(View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        ActivityUtils.stretchVideo(this.mTextureView);
    }
}
