package com.motorola.actions.p013ui.tutorial.microscreen;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.motorola.actions.C0504R;
import com.motorola.actions.checkin.CommonCheckinAttributes;
import com.motorola.actions.microScreen.MicroScreenConstants;
import com.motorola.actions.microScreen.MicroScreenService;
import com.motorola.actions.microScreen.model.MicroScreenModel;
import com.motorola.actions.onenav.OneNavHelper;
import com.motorola.actions.settings.updater.MicroScreenSettingsUpdater;
import com.motorola.actions.utils.Device;

/* renamed from: com.motorola.actions.ui.tutorial.microscreen.MicroScreenSuccessTutorialFragment */
public class MicroScreenSuccessTutorialFragment extends MicroscreenBaseTutorialFragment {
    private static final String DIRECTION_GESTURE = "direction_gesture";
    private static final String DIRECTION_GESTURE_TO_LEFT = "left";

    /* access modifiers changed from: protected */
    public int getVideoResId() {
        return -1;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View onCreateView = super.onCreateView(layoutInflater, viewGroup, bundle);
        setupUI();
        return onCreateView;
    }

    private void setupUI() {
        String[] strArr;
        setTextTitle(C0504R.string.sh_success_title);
        setTextInfo(getSwipeTextInfo(getArguments().getString(DIRECTION_GESTURE, null)));
        if (OneNavHelper.isSoftOneNav() || !Device.hasFingerprintSensor(getContext()) || Device.hasBackFPSSensor()) {
            appendTextInfo(C0504R.string.sh_success_info_exit);
            strArr = getResources().getStringArray(C0504R.array.sh_tutorial_tip_list);
        } else {
            appendTextInfo(C0504R.string.sh_success_info_exit_include_fingerprint);
            strArr = getResources().getStringArray(C0504R.array.sh_tutorial_tip_list_include_fingerprint);
        }
        setBulletInfo(strArr);
        showFinalButtons(MicroScreenModel.isMicroScreenEnabled());
    }

    private int getSwipeTextInfo(String str) {
        if (TextUtils.isEmpty(str) || str.equals("left")) {
            return C0504R.string.sh_swipe_down_success_info_swipe_left;
        }
        return C0504R.string.sh_swipe_down_success_info_swipe_right;
    }

    public static MicroScreenSuccessTutorialFragment newInstance(String str) {
        MicroScreenSuccessTutorialFragment microScreenSuccessTutorialFragment = new MicroScreenSuccessTutorialFragment();
        Bundle bundle = new Bundle();
        bundle.putString(DIRECTION_GESTURE, str);
        microScreenSuccessTutorialFragment.setArguments(bundle);
        return microScreenSuccessTutorialFragment;
    }

    public void onPause() {
        if (!MicroScreenModel.isMicroScreenEnabled()) {
            MicroScreenService.stopMicroScreenService();
        }
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void leftButtonClicked(View view) {
        MicroScreenService.stopMicroScreenService();
        updateOnSetting(MicroScreenConstants.KEY_ENABLED, false);
        super.leftButtonClicked(view);
    }

    /* access modifiers changed from: protected */
    public void finish() {
        if (!isFeatureEnabled()) {
            MicroScreenSettingsUpdater.getInstance().toggleStatus(true, true, CommonCheckinAttributes.KEY_DAILY_ENABLE_SOURCE_TYPE_TUTORIAL);
        }
        super.finish();
    }
}
