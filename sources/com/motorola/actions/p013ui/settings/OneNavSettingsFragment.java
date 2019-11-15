package com.motorola.actions.p013ui.settings;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import com.motorola.actions.C0504R;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.checkin.CommonCheckinAttributes;
import com.motorola.actions.onenav.OneNavHelper;
import com.motorola.actions.onenav.SwipeUpGestureHelper;
import com.motorola.actions.onenav.instrumentation.OneNavInstrumentation;
import com.motorola.actions.p013ui.settings.DropDownLayout.ActionListener;
import com.motorola.actions.p013ui.settings.DropDownLayout.InteractionListener;
import com.motorola.actions.p013ui.tutorial.TutorialPage;
import com.motorola.actions.p013ui.tutorial.onenav.OneNavTutorialActivity;
import com.motorola.actions.p013ui.tutorial.onenav.soft.SoftOneNavTutorialActivity;
import com.motorola.actions.reflect.MotorolaSettings;
import com.motorola.actions.settings.updater.OneNavSettingsUpdater;
import com.motorola.actions.utils.Device;
import com.motorola.actions.utils.MALogger;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/* renamed from: com.motorola.actions.ui.settings.OneNavSettingsFragment */
public class OneNavSettingsFragment extends SettingsDetailFragment implements ActionListener, InteractionListener {
    private static final int[][] DROPDOWN_OPTIONS;
    private static final int[][] DROPDOWN_OPTIONS_RTL;
    private static final boolean IS_SWITCH_APPS_GESTURE = OneNavHelper.isSoftOneNav();
    private static final MALogger LOGGER = new MALogger(OneNavSettingsFragment.class);
    public static final int ONENAV_DEFAULT = 0;
    public static final int ONENAV_REVERSE = 1;
    private static final int ONE_NAV_DELAY = 500;
    private static final int START_VIDEO_DELAY = 100;
    private final Handler mHandler = new Handler();
    private final Runnable mNegativeActionDialog = new OneNavSettingsFragment$$Lambda$0(this);
    private ProgressDialog mProgressDialog;
    private ViewGroup mVibrationGroup;
    private Switch mVibrationSwitch;
    private List<TutorialPage> mVideoDetailScreens = new ArrayList();

    /* access modifiers changed from: protected */
    public int getLayoutId() {
        return C0504R.layout.fragment_settings_onenav;
    }

    /* access modifiers changed from: protected */
    public int getTitleTextId() {
        return C0504R.string.onenav_enabled;
    }

    static {
        int[][] iArr = new int[2][];
        int[] iArr2 = new int[3];
        iArr2[0] = 0;
        iArr2[1] = C0504R.string.onenav_left_swipe_back_title;
        float f = 2131624284;
        iArr2[2] = IS_SWITCH_APPS_GESTURE ? C0504R.string.onenav_tutorial_step_soft_right_switch : C0504R.string.onenav_left_swipe_back_description;
        iArr[0] = iArr2;
        int[] iArr3 = new int[3];
        iArr3[0] = 1;
        float f2 = 2131624289;
        iArr3[1] = IS_SWITCH_APPS_GESTURE ? C0504R.string.onenav_tutorial_step_soft_left_switch : C0504R.string.onenav_right_swipe_back_title;
        iArr3[2] = C0504R.string.onenav_right_swipe_back_description;
        iArr[1] = iArr3;
        DROPDOWN_OPTIONS = iArr;
        int[][] iArr4 = new int[2][];
        int[] iArr5 = new int[3];
        iArr5[0] = 0;
        if (IS_SWITCH_APPS_GESTURE) {
            f2 = C0504R.string.onenav_tutorial_step_soft_left_switch;
        }
        iArr5[1] = f2;
        iArr5[2] = C0504R.string.onenav_right_swipe_back_description;
        iArr4[0] = iArr5;
        int[] iArr6 = new int[3];
        iArr6[0] = 1;
        iArr6[1] = C0504R.string.onenav_left_swipe_back_title;
        if (IS_SWITCH_APPS_GESTURE) {
            f = C0504R.string.onenav_tutorial_step_soft_right_switch;
        }
        iArr6[2] = f;
        iArr4[1] = iArr6;
        DROPDOWN_OPTIONS_RTL = iArr4;
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$new$0$OneNavSettingsFragment() {
        OneNavSettingsUpdater.getInstance().toggleStatus(false, true, CommonCheckinAttributes.KEY_DAILY_ENABLE_SOURCE_TYPE_MOTO_APP);
        updateSettings(false);
    }

    /* access modifiers changed from: protected */
    public int getDetailTextId() {
        return OneNavHelper.getResourceForType(C0504R.string.onenav_checkbox_summary, C0504R.string.onenav_checkbox_summary_soft);
    }

    public int getVideoId() {
        return OneNavHelper.getResourceForType(Device.is2017UIDevice() ? C0504R.raw.one_nav_settings : C0504R.raw.one_nav_settings_slim, C0504R.raw.softone_nav_settings);
    }

    public Class tutorialClass() {
        if (OneNavHelper.isSoftOneNav()) {
            return SoftOneNavTutorialActivity.class;
        }
        return OneNavTutorialActivity.class;
    }

    /* access modifiers changed from: protected */
    public FeatureKey getFeatureKey() {
        return FeatureKey.ONE_NAV;
    }

    /* access modifiers changed from: protected */
    public SettingStatus getEnabledStatus() {
        SettingStatus settingStatus = SettingStatus.DISABLED;
        if (!OneNavHelper.isOneNavPresent() || !OneNavHelper.isMotorolaPermissionGranted()) {
            return SettingStatus.UNAVAILABLE;
        }
        if (!OneNavHelper.isOneNavEnabled() || !OneNavHelper.getConflictServicesEnabled().isEmpty()) {
            return settingStatus;
        }
        return SettingStatus.ENABLED;
    }

    /* access modifiers changed from: protected */
    public void changeStatus(boolean z) {
        if (!OneNavHelper.isOneNavPresent() || !OneNavHelper.isMotorolaPermissionGranted()) {
            updateStatus(SettingStatus.UNAVAILABLE);
            updateOptions(false);
            return;
        }
        if (this.mViewPager != null) {
            this.mVideoListener = ((TutorialPage) getVideoDetailPages().get(this.mViewPager.getCurrentItem())).getVideoListener();
        }
        if (this.mVideoListener != null) {
            this.mVideoListener.stopVideo();
        }
        if (z) {
            Set conflictServicesEnabled = OneNavHelper.getConflictServicesEnabled();
            boolean z2 = !OneNavHelper.getConflictServicesEnabled().isEmpty();
            if (!z2 && !OneNavHelper.isSwipeUpConflicted()) {
                LOGGER.mo11957d("changeStatus - No FP gestures services enabled");
                updateSettings(true);
            } else if (z2) {
                showConflictedServicesDialog(conflictServicesEnabled);
            } else {
                checkSwipeUp();
            }
        } else {
            updateSettings(false);
        }
        this.mHandler.postDelayed(new OneNavSettingsFragment$$Lambda$1(this), 100);
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$changeStatus$1$OneNavSettingsFragment() {
        if (this.mVideoListener != null) {
            this.mVideoListener.startVideo();
        }
    }

    public void onPause() {
        super.onPause();
        if (this.mProgressDialog != null) {
            this.mProgressDialog.dismiss();
            this.mProgressDialog = null;
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        MotorolaSettings.setOneNavTutorialInactive();
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        loadSettingsDropdown(view, getEnabledStatus() == SettingStatus.ENABLED);
        loadVibrationSetting(view);
    }

    public void onResume() {
        super.onResume();
        updateOptions(getEnabledStatus() == SettingStatus.ENABLED);
        setTryItButtonListener(getDefaultTryItButtonListener());
    }

    public List<TutorialPage> getVideoDetailPages() {
        if (this.mVideoDetailScreens.isEmpty()) {
            this.mVideoDetailScreens.add(new TutorialPage(C0504R.raw.soft_onenav_tutorial_01, C0504R.layout.component_video_viewpager));
            this.mVideoDetailScreens.add(new TutorialPage(C0504R.raw.soft_onenav_tutorial_02, C0504R.layout.component_video_viewpager));
        }
        return this.mVideoDetailScreens;
    }

    /* access modifiers changed from: 0000 */
    public final void loadSettingsDropdown(View view, boolean z) {
        int oneNavBackSwipeDirection = MotorolaSettings.getOneNavBackSwipeDirection(0);
        if (view != null && this.mDropdownContainer != null) {
            this.mDropdownContainer.setActionListener(this);
            this.mDropdownContainer.setInteractionListener(this);
            int[][] iArr = DROPDOWN_OPTIONS;
            if (OneNavHelper.isRTL()) {
                iArr = DROPDOWN_OPTIONS_RTL;
            }
            for (int[] iArr2 : iArr) {
                int i = iArr2[0];
                boolean z2 = true;
                String string = getResources().getString(iArr2[1]);
                String string2 = getResources().getString(iArr2[2]);
                if (iArr2[0] != oneNavBackSwipeDirection) {
                    z2 = false;
                }
                this.mDropdownContainer.addSettingsOption(new DropDownSetting(i, string, string2, z2));
            }
            this.mDropdownContainer.setVisibility(0);
            this.mDropdownContainer.setSelectedSettings(oneNavBackSwipeDirection);
            dropDownItemSelected(oneNavBackSwipeDirection);
            updateOptions(z);
        }
    }

    private void updateOptions(boolean z) {
        if (this.mDropdownContainer != null) {
            if (z) {
                this.mDropdownContainer.setEnabled();
            } else {
                this.mDropdownContainer.setDisabled();
            }
        }
        if (this.mVibrationGroup == null) {
            return;
        }
        if (z) {
            this.mVibrationGroup.setAlpha(1.0f);
            this.mVibrationSwitch.setClickable(true);
            return;
        }
        this.mVibrationGroup.setAlpha(0.5f);
        this.mVibrationSwitch.setClickable(false);
    }

    public void dropDownItemSelected(int i) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("dropDownItemSelected, tag = ");
        sb.append(i);
        mALogger.mo11957d(sb.toString());
        OneNavInstrumentation.recordOneNavSwitchDirectionDailyEvent(i == 0);
        MotorolaSettings.setOneNavBackSwipeDirection(i);
    }

    public void dropDownOpened() {
        super.dropDownOpened();
        LOGGER.mo11957d("dropDownOpened");
        if (this.mVibrationGroup != null) {
            this.mVibrationGroup.setVisibility(8);
        }
    }

    public void dropDownClosed() {
        super.dropDownClosed();
        LOGGER.mo11957d("dropDownClosed");
        if (this.mVibrationGroup != null) {
            this.mVibrationGroup.setVisibility(0);
        }
    }

    private void updateSettings(boolean z) {
        updateStatus(z ? SettingStatus.ENABLED : SettingStatus.DISABLED);
        updateOptions(z);
        if (z != OneNavHelper.isOneNavEnabled()) {
            OneNavSettingsUpdater.getInstance().toggleStatus(z, true, CommonCheckinAttributes.KEY_DAILY_ENABLE_SOURCE_TYPE_SETTINGS);
            if (this.mProgressDialog == null) {
                this.mProgressDialog = ProgressDialog.show(getActivity(), "", getString(C0504R.string.onenav_waiting), true, false);
            } else {
                this.mProgressDialog.show();
            }
            this.mHandler.postDelayed(new OneNavSettingsFragment$$Lambda$2(this), 500);
        }
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$updateSettings$2$OneNavSettingsFragment() {
        if (this.mProgressDialog != null) {
            this.mProgressDialog.dismiss();
        }
    }

    private void loadVibrationSetting(@NonNull View view) {
        View view2 = getView();
        if (view2 != null) {
            this.mVibrationGroup = (ViewGroup) view2.findViewById(C0504R.C0506id.vibration_setting);
            this.mVibrationSwitch = (Switch) view.findViewById(C0504R.C0506id.toggle_element);
            this.mVibrationSwitch.setOnCheckedChangeListener(new OneNavSettingsFragment$$Lambda$3(this));
            boolean oneNavVibrationSetting = MotorolaSettings.getOneNavVibrationSetting();
            this.mVibrationSwitch.setChecked(oneNavVibrationSetting);
            setOneNavVibrationDetails(oneNavVibrationSetting);
            setOneNavVibrationTitle();
        }
    }

    private void setOneNavVibrationTitle() {
        View view = getView();
        if (view != null) {
            ((TextView) view.findViewById(C0504R.C0506id.toggle_text)).setText(OneNavHelper.getResourceForType(C0504R.string.onenav_vibration_setting_title, C0504R.string.onenav_vibration_setting_title_soft));
        }
    }

    private void setOneNavVibrationDetails(boolean z) {
        View view = getView();
        if (view != null) {
            ((TextView) view.findViewById(C0504R.C0506id.toggle_description)).setText(z ? C0504R.string.onenav_vibration_setting_on : C0504R.string.onenav_vibration_setting_off);
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: onVibrationChange */
    public void bridge$lambda$0$OneNavSettingsFragment(CompoundButton compoundButton, boolean z) {
        setOneNavVibrationDetails(z);
        MotorolaSettings.setOneNavVibrationSetting(z);
        OneNavInstrumentation.recordOneNavEnableVibrationDailyEvent(z);
    }

    private void checkSwipeUp() {
        OneNavSettingsFragment$$Lambda$4 oneNavSettingsFragment$$Lambda$4 = new OneNavSettingsFragment$$Lambda$4(this);
        if (OneNavHelper.isSwipeUpConflicted()) {
            OneNavConflictedServicesDialog.showSwipeUpConflictDialog(getContext(), oneNavSettingsFragment$$Lambda$4, this.mNegativeActionDialog);
        } else {
            updateSettings(true);
        }
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$checkSwipeUp$3$OneNavSettingsFragment() {
        SwipeUpGestureHelper.setEnabled(false);
        updateSettings(true);
    }

    private void showConflictedServicesDialog(Set<AccessibilityServiceInfo> set) {
        OneNavConflictedServicesDialog.showConflictedServicesTurnOffDialog(getContext(), set, new OneNavSettingsFragment$$Lambda$5(this, set), this.mNegativeActionDialog);
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$showConflictedServicesDialog$4$OneNavSettingsFragment(Set set) {
        OneNavHelper.turnOffAccessibilityServices(set);
        checkSwipeUp();
    }
}
