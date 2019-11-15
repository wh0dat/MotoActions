package com.motorola.actions.p013ui.settings;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.C0504R;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.attentivedisplay.util.AttentiveDisplayHelper;
import com.motorola.actions.checkin.CommonCheckinAttributes;
import com.motorola.actions.p013ui.SnackbarHelper;
import com.motorola.actions.p013ui.SnackbarHelper.SnackbarButtonListener;
import com.motorola.actions.p013ui.tutorial.p014ad.TourDemoActivity;
import com.motorola.actions.settings.provider.ActionsSettingsProvider;
import com.motorola.actions.settings.updater.AttentiveDisplaySettingsUpdater;
import com.motorola.actions.utils.ActivityUtils;
import com.motorola.actions.utils.MALogger;

/* renamed from: com.motorola.actions.ui.settings.AttentiveDisplaySettingsFragment */
public class AttentiveDisplaySettingsFragment extends SettingsDetailFragment implements SnackbarButtonListener {
    private static final boolean GOTO_SLEEP_DEFAULT_VALUE = true;
    private static final String GOTO_SLEEP_KEY = "ad_go_to_sleep";
    private static final MALogger LOGGER = new MALogger(AttentiveDisplaySettingsFragment.class);
    private static final int PERMISSION_REQ_CODE = 101;
    private static final int PERMISSION_REQ_CODE_TUTORIAL = 102;
    private final SparseArray<Dialog> mSnackbarMap = new SparseArray<>();

    /* access modifiers changed from: protected */
    public int getDetailTextId() {
        return C0504R.string.ad_checkbox_summary;
    }

    /* access modifiers changed from: protected */
    public int getTitleTextId() {
        return C0504R.string.ad_enabled;
    }

    public int getVideoId() {
        return C0504R.raw.video_attentive_display;
    }

    public Class tutorialClass() {
        return TourDemoActivity.class;
    }

    /* access modifiers changed from: protected */
    public FeatureKey getFeatureKey() {
        return FeatureKey.ATTENTIVE_DISPLAY;
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        ActivityUtils.stretchVideo(this.mTextureView);
        setDisplayFamilyColor();
    }

    /* access modifiers changed from: protected */
    public SettingStatus getEnabledStatus() {
        if (SharedPreferenceManager.getBoolean(AttentiveDisplayHelper.STAY_ON_KEY, FeatureKey.ATTENTIVE_DISPLAY.getEnableDefaultState())) {
            return SettingStatus.ENABLED;
        }
        return SettingStatus.DISABLED;
    }

    /* access modifiers changed from: protected */
    public void changeStatus(boolean z) {
        boolean z2;
        boolean z3;
        AttentiveDisplaySettingsUpdater.getInstance().setEnabledSource(z, CommonCheckinAttributes.KEY_DAILY_ENABLE_SOURCE_TYPE_SETTINGS);
        if (z) {
            boolean z4 = SharedPreferenceManager.getBoolean(AttentiveDisplayHelper.STAY_ON_KEY, FeatureKey.ATTENTIVE_DISPLAY.getEnableDefaultState());
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("Stay on pref changed to: ");
            sb.append(z4);
            mALogger.mo11957d(sb.toString());
            if (!z4) {
                if (getActivity().checkSelfPermission("android.permission.CAMERA") != 0) {
                    LOGGER.mo11957d("Permission denied");
                    if (shouldShowRequestPermissionRationale("android.permission.CAMERA")) {
                        showPermissionRationaleDialog(1);
                    } else {
                        requestPermissions(new String[]{"android.permission.CAMERA"}, 101);
                    }
                    z2 = false;
                } else {
                    z2 = true;
                }
                if (Settings.canDrawOverlays(getActivity().getApplicationContext())) {
                    LOGGER.mo11957d("SYSTEM_ALERT_WINDOW permission granted");
                    z3 = true;
                } else {
                    LOGGER.mo11957d("SYSTEM_ALERT_WINDOW permission denied, now show the dialog.");
                    showPermissionDialogForSystemAlertWindow();
                    z3 = false;
                }
                if (!z2 || !z3) {
                    z = false;
                } else {
                    LOGGER.mo11957d("Both Camera and DrawOverOthers permissions granted");
                }
            }
        }
        AttentiveDisplaySettingsUpdater.getInstance().toggleStatus(z, true, CommonCheckinAttributes.KEY_DAILY_ENABLE_SOURCE_TYPE_SETTINGS);
        updateStatus(SharedPreferenceManager.getBoolean(AttentiveDisplayHelper.STAY_ON_KEY, FeatureKey.ATTENTIVE_DISPLAY.getEnableDefaultState()) ? SettingStatus.ENABLED : SettingStatus.DISABLED);
        ActionsSettingsProvider.notifyAdObservers();
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        if (iArr.length > 0) {
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("onRequestPermissionsResult - grantResults: ");
            boolean z = false;
            sb.append(iArr[0]);
            sb.append(" - requestCode: ");
            sb.append(i);
            mALogger.mo11957d(sb.toString());
            switch (i) {
                case 101:
                    if (iArr[0] == 0) {
                        if (Settings.canDrawOverlays(getActivity().getApplicationContext())) {
                            updateStatus(SettingStatus.ENABLED);
                            z = true;
                        } else {
                            showPermissionDialogForSystemAlertWindow();
                        }
                    } else if (!shouldShowRequestPermissionRationale("android.permission.CAMERA")) {
                        showPermissionDialogForAppInfoSettings();
                    }
                    AttentiveDisplaySettingsUpdater.getInstance().toggleStatus(z, true, CommonCheckinAttributes.KEY_DAILY_ENABLE_SOURCE_TYPE_SETTINGS);
                    return;
                case 102:
                    if (iArr[0] == 0) {
                        startAttentiveDisplayTutorial();
                        return;
                    } else if (!shouldShowRequestPermissionRationale("android.permission.CAMERA")) {
                        showPermissionDialogForAppInfoSettings();
                        return;
                    } else {
                        return;
                    }
                default:
                    LOGGER.mo11957d("Discard permission request.");
                    return;
            }
        } else {
            LOGGER.mo11957d("Not valid grantResults - Discard permission request.");
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return super.onCreateView(layoutInflater, viewGroup, bundle);
    }

    public void onResume() {
        super.onResume();
        if (getActivity().checkSelfPermission("android.permission.CAMERA") != 0) {
            LOGGER.mo11957d("onResume, Camera permission is denied.");
            AttentiveDisplaySettingsUpdater.getInstance().toggleStatus(false, false, CommonCheckinAttributes.KEY_DAILY_ENABLE_SOURCE_TYPE_DEFAULT);
            updateStatus(SettingStatus.DISABLED);
            return;
        }
        LOGGER.mo11957d("onResume, Camera permission is granted, do nothing.");
    }

    /* access modifiers changed from: protected */
    public void startTutorial() {
        LOGGER.mo11957d("startTutorial");
        Context context = getContext();
        if (context != null) {
            if (!Settings.canDrawOverlays(context)) {
                showPermissionDialogForSystemAlertWindow();
            } else if (context.checkSelfPermission("android.permission.CAMERA") != 0) {
                LOGGER.mo11957d("Camera Permission denied");
                if (shouldShowRequestPermissionRationale("android.permission.CAMERA")) {
                    showPermissionRationaleDialog(4);
                } else {
                    requestPermissions(new String[]{"android.permission.CAMERA"}, 102);
                }
            } else {
                LOGGER.mo11957d("Permission GRANTED");
                startAttentiveDisplayTutorial();
            }
        }
    }

    private void startAttentiveDisplayTutorial() {
        Activity activity = getActivity();
        if (activity != null) {
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("startAttentiveDisplayTutorial - is FDN active = ");
            sb.append(getFDNSession().isActive());
            mALogger.mo11957d(sb.toString());
            Intent intent = new Intent(activity, tutorialClass());
            getFDNSession().setFDNExtra(intent);
            startActivity(intent);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        SnackbarHelper.dismissDialogs(this.mSnackbarMap);
    }

    private void showPermissionRationaleDialog(int i) {
        if (this.mSnackbarMap.size() <= 0) {
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("Trigger source: ");
            sb.append(i);
            mALogger.mo11957d(sb.toString());
            SnackbarHelper.showSnackbar(C0504R.string.perms_rationale_diag_title, getString(C0504R.string.perms_attentive_display_camera), C0504R.string.perms_rationale_diag_btn, this, getActivity(), i, this.mSnackbarMap);
        }
    }

    private void showPermissionDialogForAppInfoSettings() {
        if (this.mSnackbarMap.size() <= 0) {
            SnackbarHelper.showSnackbar(C0504R.string.perms_rationale_diag_title, getString(C0504R.string.perms_navigate_to_app_info_settings_content), C0504R.string.perms_navigate_to_app_info_settings_diag_btn, this, getActivity(), 2, this.mSnackbarMap);
        }
    }

    private void showPermissionDialogForSystemAlertWindow() {
        if (this.mSnackbarMap.size() <= 0) {
            SnackbarHelper.showSnackbar(C0504R.string.perms_rationale_diag_title, getString(C0504R.string.perms_navigate_to_app_info_settings_draw_over_other_apps_content), C0504R.string.perms_navigate_to_app_info_settings_diag_btn, this, getActivity(), 3, this.mSnackbarMap);
        }
    }

    public static boolean isStayOnEnabled() {
        return SharedPreferenceManager.getBoolean(AttentiveDisplayHelper.STAY_ON_KEY, FeatureKey.ATTENTIVE_DISPLAY.getEnableDefaultState());
    }

    public static boolean isGoToSleepEnabled() {
        if (!SharedPreferenceManager.getBoolean(AttentiveDisplayHelper.STAY_ON_KEY, FeatureKey.ATTENTIVE_DISPLAY.getEnableDefaultState()) || !SharedPreferenceManager.getBoolean(GOTO_SLEEP_KEY, true)) {
            return false;
        }
        return true;
    }

    public static boolean isDependencyResolved() {
        if (!isStayOnEnabled()) {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            boolean canDrawOverlays = Settings.canDrawOverlays(ActionsApplication.getAppContext());
            Binder.restoreCallingIdentity(clearCallingIdentity);
            if (ActionsApplication.getAppContext().checkSelfPermission("android.permission.CAMERA") != 0 || !canDrawOverlays) {
                return false;
            }
        }
        return true;
    }

    public void onPositiveButtonClick(int i) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("onPositiveButtonClick: ");
        sb.append(i);
        mALogger.mo11957d(sb.toString());
        Activity activity = getActivity();
        this.mSnackbarMap.remove(i);
        switch (i) {
            case 1:
                requestPermissions(new String[]{"android.permission.CAMERA"}, 101);
                return;
            case 2:
                if (activity != null) {
                    AttentiveDisplayHelper.openAppSettings(activity);
                    return;
                }
                return;
            case 3:
                if (activity != null) {
                    AttentiveDisplayHelper.openOverlayPermissionSetting(activity);
                    return;
                }
                return;
            case 4:
                requestPermissions(new String[]{"android.permission.CAMERA"}, 102);
                return;
            default:
                return;
        }
    }
}
