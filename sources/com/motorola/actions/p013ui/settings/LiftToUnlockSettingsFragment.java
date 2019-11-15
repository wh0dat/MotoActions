package com.motorola.actions.p013ui.settings;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;
import com.google.android.gms.common.util.CrashUtils.ErrorDialogData;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.C0504R;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.checkin.CommonCheckinAttributes;
import com.motorola.actions.ltu.LiftToUnlockHelper;
import com.motorola.actions.reflect.MotorolaSettings;
import com.motorola.actions.settings.provider.ActionsSettingsProvider;
import com.motorola.actions.settings.provider.p012v2.actions.ContainerProviderItemLiftToUnlock;
import com.motorola.actions.settings.updater.LiftToUnlockSettingsUpdater;
import com.motorola.actions.utils.MALogger;

/* renamed from: com.motorola.actions.ui.settings.LiftToUnlockSettingsFragment */
public class LiftToUnlockSettingsFragment extends SettingsDetailFragment {
    private static final String FACE_UNLOCK_ACTIVITY = "com.motorola.faceunlock.FaceUnlockSettings";
    private static final String FACE_UNLOCK_PACKAGE = "com.motorola.faceunlock";
    private static final MALogger LOGGER = new MALogger(LiftToUnlockSettingsFragment.class);
    private AlertDialog mDialog;

    /* access modifiers changed from: protected */
    public int getDetailTextId() {
        return C0504R.string.ltu_summary;
    }

    /* access modifiers changed from: protected */
    public int getTitleTextId() {
        return C0504R.string.ltu_enabled;
    }

    public int getVideoId() {
        return C0504R.raw.lift_to_unlock;
    }

    public Class tutorialClass() {
        return null;
    }

    /* access modifiers changed from: protected */
    public FeatureKey getFeatureKey() {
        return FeatureKey.LIFT_TO_UNLOCK;
    }

    /* access modifiers changed from: protected */
    public SettingStatus getEnabledStatus() {
        if (LiftToUnlockHelper.isEnabled()) {
            return SettingStatus.ENABLED;
        }
        return SettingStatus.DISABLED;
    }

    /* access modifiers changed from: protected */
    public void changeStatus(boolean z) {
        if (!z || LiftToUnlockHelper.isEnrolled()) {
            updateStatus(z ? SettingStatus.ENABLED : SettingStatus.DISABLED);
            LiftToUnlockSettingsUpdater.getInstance().toggleStatus(z, true, CommonCheckinAttributes.KEY_DAILY_ENABLE_SOURCE_TYPE_SETTINGS);
            return;
        }
        showFaceUnlockDialog();
    }

    public void onResume() {
        super.onResume();
        if (!MotorolaSettings.isFaceEnrolled() && MotorolaSettings.isLiftToUnlockEnabled()) {
            LiftToUnlockSettingsUpdater.getInstance().toggleStatus(false, true, CommonCheckinAttributes.KEY_DAILY_ENABLE_SOURCE_TYPE_SETTINGS);
        }
        ActionsSettingsProvider.notifyChange(ContainerProviderItemLiftToUnlock.TABLE_NAME);
    }

    private void showFaceUnlockDialog() {
        if (this.mDialog == null) {
            this.mDialog = new Builder(getActivity()).setTitle(C0504R.string.ltu_face_enroll_title).setMessage(getString(C0504R.string.ltu_face_enroll_summary)).setCancelable(false).setPositiveButton(getString(C0504R.string.enable), new LiftToUnlockSettingsFragment$$Lambda$0(this)).setNegativeButton(getString(17039360), new LiftToUnlockSettingsFragment$$Lambda$1(this)).create();
        }
        if (!this.mDialog.isShowing()) {
            this.mDialog.show();
        }
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$showFaceUnlockDialog$0$LiftToUnlockSettingsFragment(DialogInterface dialogInterface, int i) {
        startFaceUnlockActivity();
        LiftToUnlockSettingsUpdater.getInstance().toggleStatus(true, true, CommonCheckinAttributes.KEY_DAILY_ENABLE_SOURCE_TYPE_SETTINGS);
        dialogInterface.dismiss();
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$showFaceUnlockDialog$1$LiftToUnlockSettingsFragment(DialogInterface dialogInterface, int i) {
        changeStatus(false);
        dialogInterface.dismiss();
    }

    private void startFaceUnlockActivity() {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setComponent(new ComponentName(FACE_UNLOCK_PACKAGE, FACE_UNLOCK_ACTIVITY));
        intent.setFlags(ErrorDialogData.BINDER_CRASH);
        try {
            ActionsApplication.getAppContext().startActivity(intent);
        } catch (ActivityNotFoundException | IllegalStateException e) {
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("Could not start Face Unlock activity: ");
            sb.append(e);
            mALogger.mo11959e(sb.toString());
            changeStatus(false);
            Toast.makeText(ActionsApplication.getAppContext(), C0504R.string.ltu_face_unlock_fail, 1).show();
        }
    }
}
