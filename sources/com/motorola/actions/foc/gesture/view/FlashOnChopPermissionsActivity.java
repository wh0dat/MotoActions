package com.motorola.actions.foc.gesture.view;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.motorola.actions.C0504R;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.foc.gesture.service.FlashOnChopService;
import com.motorola.actions.p013ui.ActionsBaseActivity;
import com.motorola.actions.p013ui.settings.PermissionsManager;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.ServiceUtils;
import java.util.ArrayList;

public class FlashOnChopPermissionsActivity extends ActionsBaseActivity {
    private static final MALogger LOGGER = new MALogger(FlashOnChopPermissionsActivity.class);
    private static final String URI_PACKAGE = "package:";
    private static boolean sHasDialogInstance;
    private boolean mReturnFromSettings;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        LOGGER.mo11957d("onCreate");
        ArrayList notGrantedPermissions = PermissionsManager.getInstance().getNotGrantedPermissions(getApplicationContext(), 1);
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("onCreate: getNotGrantedPermissions.size() = ");
        sb.append(notGrantedPermissions.size());
        mALogger.mo11957d(sb.toString());
        if (notGrantedPermissions.size() > 0) {
            requestPermissions((String[]) notGrantedPermissions.toArray(new String[notGrantedPermissions.size()]), 1);
            setHasDialogInstance(true);
            return;
        }
        finishAndRemoveTask();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        LOGGER.mo11957d("onStop called. Should finish activity to avoid flow problems.");
        super.onStop();
        if (checkSelfPermission("android.permission.CAMERA") != 0) {
            startFOCService();
        }
        finishAndRemoveTask();
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x003f  */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x0051  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onRequestPermissionsResult(int r4, @android.support.annotation.NonNull java.lang.String[] r5, @android.support.annotation.NonNull int[] r6) {
        /*
            r3 = this;
            int r0 = r6.length
            if (r0 <= 0) goto L_0x005a
            com.motorola.actions.utils.MALogger r0 = LOGGER
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "onRequestPermissionsResult requestCode:"
            r1.append(r2)
            r1.append(r4)
            java.lang.String r1 = r1.toString()
            r0.mo11957d(r1)
            r0 = 1
            if (r4 == r0) goto L_0x0028
            com.motorola.actions.utils.MALogger r1 = LOGGER
            java.lang.String r2 = "onRequestPermissionsResult requestCode: Default"
            r1.mo11957d(r2)
            super.onRequestPermissionsResult(r4, r5, r6)
        L_0x0026:
            r1 = r0
            goto L_0x003d
        L_0x0028:
            r4 = 0
            r1 = r0
        L_0x002a:
            int r2 = r5.length
            if (r4 >= r2) goto L_0x003d
            r1 = r5[r4]
            boolean r1 = r3.shouldShowRequestPermissionRationale(r1)
            if (r1 != 0) goto L_0x0026
            r2 = r6[r4]
            if (r2 == 0) goto L_0x003a
            goto L_0x0026
        L_0x003a:
            int r4 = r4 + 1
            goto L_0x002a
        L_0x003d:
            if (r1 == 0) goto L_0x0051
            r4 = 2131624374(0x7f0e01b6, float:1.8875926E38)
            java.lang.String r4 = r3.getString(r4)
            r5 = 2131624368(0x7f0e01b0, float:1.8875914E38)
            java.lang.String r5 = r3.getString(r5)
            r3.popUpPermisionsDialog(r4, r5)
            goto L_0x0061
        L_0x0051:
            java.lang.String r4 = "actions_foc_turn_on_by_permission_granted"
            com.motorola.actions.SharedPreferenceManager.putBoolean(r4, r0)
            r3.finishAndRemoveTask()
            goto L_0x0061
        L_0x005a:
            com.motorola.actions.utils.MALogger r3 = LOGGER
            java.lang.String r4 = "Not valid grantResults - Discard permission request."
            r3.mo11957d(r4)
        L_0x0061:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.motorola.actions.foc.gesture.view.FlashOnChopPermissionsActivity.onRequestPermissionsResult(int, java.lang.String[], int[]):void");
    }

    private void popUpPermisionsDialog(String str, String str2) {
        Builder builder = new Builder(this);
        builder.setTitle(str);
        builder.setMessage(str2);
        builder.setCancelable(false).setPositiveButton(C0504R.string.perm_required_alert_button_app_info, new FlashOnChopPermissionsActivity$$Lambda$0(this)).setNegativeButton(C0504R.string.exit, new FlashOnChopPermissionsActivity$$Lambda$1(this));
        builder.create().show();
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$popUpPermisionsDialog$0$FlashOnChopPermissionsActivity(DialogInterface dialogInterface, int i) {
        goToSettings();
        dialogInterface.dismiss();
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$popUpPermisionsDialog$1$FlashOnChopPermissionsActivity(DialogInterface dialogInterface, int i) {
        startFOCService();
        finishAndRemoveTask();
    }

    private void goToSettings() {
        LOGGER.mo11957d("goToSettings");
        this.mReturnFromSettings = true;
        Intent intent = new Intent();
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.addCategory("android.intent.category.DEFAULT");
        StringBuilder sb = new StringBuilder();
        sb.append(URI_PACKAGE);
        sb.append(getApplicationContext().getPackageName());
        intent.setData(Uri.parse(sb.toString()));
        startActivity(intent);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (LOGGER.canLogDebug()) {
            ArrayList notGrantedPermissions = PermissionsManager.getInstance().getNotGrantedPermissions(getApplicationContext(), 1);
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("onResume: getNotGrantedPermissions.size() = ");
            sb.append(notGrantedPermissions.size());
            mALogger.mo11957d(sb.toString());
        }
        if (this.mReturnFromSettings) {
            this.mReturnFromSettings = false;
            ArrayList notGrantedPermissions2 = PermissionsManager.getInstance().getNotGrantedPermissions(getApplicationContext(), 1);
            LOGGER.mo11957d("onResume: finishRequest finished!");
            if (notGrantedPermissions2.size() == 0) {
                LOGGER.mo11957d("onResume: All permissions were granted!");
                SharedPreferenceManager.putBoolean(FlashOnChopService.KEY_TURN_ON_BY_PERMISSIONS_GRANTED, true);
            } else {
                LOGGER.mo11957d("onResume: Need permission!");
                startFOCService();
            }
            finishAndRemoveTask();
        } else if (PermissionsManager.getInstance().getNotGrantedPermissions(getApplicationContext(), 1).size() == 0) {
            finishAndRemoveTask();
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        setHasDialogInstance(false);
    }

    public static boolean getHasDialogInstance() {
        return sHasDialogInstance;
    }

    private static void setHasDialogInstance(boolean z) {
        sHasDialogInstance = z;
    }

    private void startFOCService() {
        ServiceUtils.startServiceSafe(FlashOnChopService.createIntent(getApplicationContext()));
    }
}
