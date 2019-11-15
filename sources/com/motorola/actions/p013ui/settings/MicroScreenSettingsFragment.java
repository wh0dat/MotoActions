package com.motorola.actions.p013ui.settings;

import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.motorola.actions.C0504R;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.checkin.CommonCheckinAttributes;
import com.motorola.actions.microScreen.MicroScreenConstants;
import com.motorola.actions.microScreen.MicroScreenNotificationManager;
import com.motorola.actions.microScreen.MicroScreenReceiver;
import com.motorola.actions.microScreen.MicroScreenService;
import com.motorola.actions.microScreen.model.MicroScreenModel;
import com.motorola.actions.p013ui.tutorial.microscreen.MicroScreenTutorialActivity;
import com.motorola.actions.settings.updater.MicroScreenSettingsUpdater;
import com.motorola.actions.utils.MALogger;

/* renamed from: com.motorola.actions.ui.settings.MicroScreenSettingsFragment */
public class MicroScreenSettingsFragment extends SettingsDetailFragment {
    /* access modifiers changed from: private */
    public static final MALogger LOGGER = new MALogger(MicroScreenSettingsFragment.class);
    /* access modifiers changed from: private */
    public boolean mHasSingleHandOverlayPermission;
    private ReceiverOverlayPermission mReceiverOverlayPermission;
    private final OnSharedPreferenceChangeListener mSharedPreferenceListener = new OnSharedPreferenceChangeListener() {
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String str) {
            if (MicroScreenConstants.KEY_ENABLED.equals(str)) {
                MicroScreenSettingsFragment.LOGGER.mo11957d("Microscreen key changed");
                if (!MicroScreenSettingsFragment.this.mSingleHandOverlayPermissionWasSent) {
                    MicroScreenSettingsFragment.LOGGER.mo11957d("Waiting overlay permission state");
                    return;
                }
                MicroScreenSettingsFragment.this.updateStatus(MicroScreenModel.isMicroScreenEnabled() ? SettingStatus.ENABLED : SettingStatus.DISABLED);
                if (!MicroScreenModel.isMicroScreenEnabled()) {
                    MicroScreenNotificationManager.dismissNotification();
                }
                if (MicroScreenModel.isMicroScreenEnabled() && !MicroScreenSettingsFragment.this.mHasSingleHandOverlayPermission) {
                    MicroScreenSettingsFragment.this.popUpSingleHandPermisionsDialog(MicroScreenSettingsFragment.this.getString(C0504R.string.perms_rationale_diag_title), MicroScreenSettingsFragment.this.getString(C0504R.string.sh_notification_access_text));
                }
            }
        }
    };
    private final OnSharedPreferenceChangeListener mSharedPreferenceListenerTutorial = new OnSharedPreferenceChangeListener() {
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String str) {
            if (!MicroScreenConstants.KEY_ENABLED.equals(str)) {
                return;
            }
            if (!MicroScreenTutorialActivity.getHasSingleHandOverlayPermissionTutorial()) {
                MicroScreenSettingsFragment.this.mSingleHandOverlayPermissionWasSent = false;
                MicroScreenSettingsFragment.this.setTryItButtonListener(MicroScreenSettingsFragment.this.createOnClickListenerSingleHandPermission());
                if (MicroScreenModel.isMicroScreenEnabled()) {
                    MicroScreenModel.saveMicroScreenEnabled(false);
                    MicroScreenSettingsFragment.this.updateStatus(SettingStatus.DISABLED);
                    MicroScreenSettingsFragment.this.popUpSingleHandPermisionsDialog(MicroScreenSettingsFragment.this.getString(C0504R.string.perms_rationale_diag_title), MicroScreenSettingsFragment.this.getString(C0504R.string.sh_notification_access_text));
                    return;
                }
                return;
            }
            MicroScreenSettingsFragment.this.setTryItButtonListener(MicroScreenSettingsFragment.this.getDefaultTryItButtonListener());
        }
    };
    /* access modifiers changed from: private */
    public boolean mSingleHandOverlayPermissionWasSent;

    /* renamed from: com.motorola.actions.ui.settings.MicroScreenSettingsFragment$ReceiverOverlayPermission */
    private class ReceiverOverlayPermission extends BroadcastReceiver {
        private ReceiverOverlayPermission() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction() != null && intent.getExtras() != null && intent.getAction().equals(MicroScreenReceiver.MOTO_ACTION_MICROSCREEN_OVERLAY_PERMISSION_STATUS)) {
                MicroScreenSettingsFragment.LOGGER.mo11957d("MOTO_ACTION_MICROSCREEN_OVERLAY_PERMISSION_STATUS received");
                MicroScreenSettingsFragment.this.mHasSingleHandOverlayPermission = intent.getExtras().getBoolean(MicroScreenReceiver.EXTRA_OVERLAY_PERMISSION_STATUS, false);
                MicroScreenSettingsFragment.this.mSingleHandOverlayPermissionWasSent = true;
                MicroScreenTutorialActivity.setHasSingleHandOverlayPermissionTutorial(MicroScreenSettingsFragment.this.mHasSingleHandOverlayPermission);
                if (!MicroScreenSettingsFragment.this.mHasSingleHandOverlayPermission) {
                    MicroScreenSettingsFragment.LOGGER.mo11957d("tryButton:createOnClickListenerSingleHandPermission()");
                    MicroScreenSettingsFragment.this.setTryItButtonListener(MicroScreenSettingsFragment.this.createOnClickListenerSingleHandPermission());
                    MicroScreenModel.saveMicroScreenEnabled(false);
                    return;
                }
                MicroScreenSettingsFragment.LOGGER.mo11957d("tryButton:getDefaultTryItButtonListener()");
                MicroScreenNotificationManager.dismissNotification();
                MicroScreenSettingsFragment.this.setTryItButtonListener(MicroScreenSettingsFragment.this.getDefaultTryItButtonListener());
            }
        }
    }

    /* access modifiers changed from: protected */
    public int getDetailTextId() {
        return C0504R.string.sh_swipe_down_info;
    }

    /* access modifiers changed from: protected */
    public int getTitleTextId() {
        return C0504R.string.sh_enabled;
    }

    /* access modifiers changed from: protected */
    public int getVideoId() {
        return C0504R.raw.microscreen_settings_swipe_down;
    }

    private void unregisterReceiverOverlayPermission() {
        LOGGER.mo11957d("unregisterReceiverOverlayPermission");
        if (this.mReceiverOverlayPermission != null) {
            getActivity().unregisterReceiver(this.mReceiverOverlayPermission);
            this.mReceiverOverlayPermission = null;
        }
    }

    private void registerReceiverOverlayPermission() {
        LOGGER.mo11957d("registerReceiverOverlayPermission");
        if (this.mReceiverOverlayPermission == null) {
            this.mReceiverOverlayPermission = new ReceiverOverlayPermission();
            getActivity().registerReceiver(this.mReceiverOverlayPermission, new IntentFilter(MicroScreenReceiver.MOTO_ACTION_MICROSCREEN_OVERLAY_PERMISSION_STATUS), "com.motorola.permission.MICROSCREEN_RECEIVER", null);
        }
    }

    /* access modifiers changed from: protected */
    public SettingStatus getEnabledStatus() {
        if (MicroScreenModel.isMicroScreenEnabled()) {
            return SettingStatus.ENABLED;
        }
        return SettingStatus.DISABLED;
    }

    /* access modifiers changed from: protected */
    public void changeStatus(boolean z) {
        updateSettings(z);
    }

    /* access modifiers changed from: protected */
    public Class tutorialClass() {
        return MicroScreenTutorialActivity.class;
    }

    /* access modifiers changed from: protected */
    public FeatureKey getFeatureKey() {
        return FeatureKey.MICROSCREEN;
    }

    /* access modifiers changed from: private */
    public void popUpSingleHandPermisionsDialog(String str, String str2) {
        LOGGER.mo11957d("Microscreen popUpPermisionsDialog");
        MicroScreenNotificationManager.dismissNotification();
        Builder builder = new Builder(getContext());
        builder.setTitle(str);
        builder.setMessage(str2);
        builder.setCancelable(false).setPositiveButton(C0504R.string.notification_perm_to_allow, new MicroScreenSettingsFragment$$Lambda$0(this)).setNegativeButton(C0504R.string.exit, MicroScreenSettingsFragment$$Lambda$1.$instance);
        builder.create().show();
    }

    /* access modifiers changed from: 0000 */
    /* renamed from: lambda$popUpSingleHandPermisionsDialog$0$MicroScreenSettingsFragment */
    public final /* synthetic */ void mo11598x1c4bcebe(DialogInterface dialogInterface, int i) {
        startActivity(MicroScreenNotificationManager.createIntentOverlaySingleHand());
    }

    /* access modifiers changed from: private */
    public OnClickListener createOnClickListenerSingleHandPermission() {
        return new MicroScreenSettingsFragment$$Lambda$2(this);
    }

    /* access modifiers changed from: 0000 */
    /* renamed from: lambda$createOnClickListenerSingleHandPermission$2$MicroScreenSettingsFragment */
    public final /* synthetic */ void mo11597xbda4c877(View view) {
        LOGGER.mo11957d("onClickListenerSingleHandPermission");
        MicroScreenModel.saveMicroScreenEnabled(false);
        popUpSingleHandPermisionsDialog(getString(C0504R.string.perms_rationale_diag_title), getString(C0504R.string.sh_notification_access_text));
    }

    public void onCreate(Bundle bundle) {
        MicroScreenModel.saveMicroScreenTutorialIsActive(false);
        super.onCreate(bundle);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View onCreateView = super.onCreateView(layoutInflater, viewGroup, bundle);
        if (onCreateView != null) {
            LOGGER.mo11957d("onClickListenerSingleHandPermission");
            setTryItButtonListener(null);
        }
        return onCreateView;
    }

    public void onResume() {
        super.onResume();
        setTryItButtonListener(getDefaultTryItButtonListener());
        SharedPreferenceManager.registerOnSharedPreferenceChangeListener(this.mSharedPreferenceListener);
        SharedPreferenceManager.registerOnSharedPreferenceChangeListener(this.mSharedPreferenceListenerTutorial);
        if (!MicroScreenModel.isMicroScreenModeOn()) {
            registerReceiverOverlayPermission();
            MicroScreenService.sendCheckOverlayPermission();
        }
    }

    public void onPause() {
        super.onPause();
        unregisterReceiverOverlayPermission();
        SharedPreferenceManager.unregisterOnSharedPreferenceChangeListener(this.mSharedPreferenceListener);
        SharedPreferenceManager.unregisterOnSharedPreferenceChangeListener(this.mSharedPreferenceListenerTutorial);
    }

    private void updateSettings(boolean z) {
        updateStatus(z ? SettingStatus.ENABLED : SettingStatus.DISABLED);
        MicroScreenSettingsUpdater.getInstance().toggleStatus(z, true, CommonCheckinAttributes.KEY_DAILY_ENABLE_SOURCE_TYPE_SETTINGS);
    }
}
