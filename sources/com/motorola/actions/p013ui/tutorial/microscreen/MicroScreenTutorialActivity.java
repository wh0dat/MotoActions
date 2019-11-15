package com.motorola.actions.p013ui.tutorial.microscreen;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import com.motorola.actions.C0504R;
import com.motorola.actions.microScreen.MicroScreenReceiver;
import com.motorola.actions.microScreen.MicroScreenService;
import com.motorola.actions.microScreen.model.MicroScreenModel;
import com.motorola.actions.p013ui.tutorial.TutorialActivity;
import com.motorola.actions.utils.MALogger;

/* renamed from: com.motorola.actions.ui.tutorial.microscreen.MicroScreenTutorialActivity */
public class MicroScreenTutorialActivity extends TutorialActivity {
    /* access modifiers changed from: private */
    public static final MALogger LOGGER = new MALogger(MicroScreenTutorialActivity.class);
    private static final String TAG_FRAGMENT = "microscreen_fragment";
    private static boolean sHasSingleHandOverlayPermissionTutorial;
    private ReceiverOverlayPermissionTutorial mReceiverOverlayPermissionTutorial;

    /* renamed from: com.motorola.actions.ui.tutorial.microscreen.MicroScreenTutorialActivity$ReceiverOverlayPermissionTutorial */
    private class ReceiverOverlayPermissionTutorial extends BroadcastReceiver {
        private ReceiverOverlayPermissionTutorial() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction() != null && intent.getExtras() != null && intent.getAction().equals(MicroScreenReceiver.MOTO_ACTION_MICROSCREEN_OVERLAY_PERMISSION_STATUS)) {
                MicroScreenTutorialActivity.LOGGER.mo11957d("MOTO_ACTION_MICROSCREEN_OVERLAY_PERMISSION_STATUS received");
                boolean z = intent.getExtras().getBoolean(MicroScreenReceiver.EXTRA_OVERLAY_PERMISSION_STATUS, false);
                MicroScreenTutorialActivity.setHasSingleHandOverlayPermissionTutorial(z);
                if (!z) {
                    MicroScreenModel.saveMicroScreenEnabled(false);
                    MicroScreenTutorialActivity.this.finish();
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (!MicroScreenService.isFeatureSupported()) {
            finish();
            return;
        }
        LOGGER.mo11957d("MicroScreenTutorialActivity: onCreate");
        showFragment(new MicroScreenTutorialFragment());
        MicroScreenModel.saveMicroScreenTutorialIsActive(true);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        LOGGER.mo11957d("MicroScreenTutorialActivity: onResume");
        registerReceiverOverlayPermissionTutorial();
        MicroScreenService.sendCheckOverlayPermission();
        MicroScreenService.sendExitByInteractionIntent();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        LOGGER.mo11957d("MicroScreenTutorialActivity: onPause");
        unregisterReceiverOverlayPermissionTutorial();
    }

    /* access modifiers changed from: 0000 */
    public final void showFragment(Fragment fragment) {
        getFragmentManager().beginTransaction().replace(C0504R.C0506id.layout_tutorial, fragment, TAG_FRAGMENT).commit();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        LOGGER.mo11957d("MicroScreenTutorialActivity: onDestroy");
        MicroScreenModel.saveMicroScreenTutorialIsActive(false);
        super.onDestroy();
    }

    public void showSuccessResultFragment(String str) {
        showFragment(MicroScreenSuccessTutorialFragment.newInstance(str));
    }

    private void unregisterReceiverOverlayPermissionTutorial() {
        LOGGER.mo11957d("unregisterReceiverOverlayPermissionTutorial");
        if (this.mReceiverOverlayPermissionTutorial != null) {
            unregisterReceiver(this.mReceiverOverlayPermissionTutorial);
            this.mReceiverOverlayPermissionTutorial = null;
        }
    }

    private void registerReceiverOverlayPermissionTutorial() {
        LOGGER.mo11957d("registerReceiverOverlayPermissionTutorial");
        if (this.mReceiverOverlayPermissionTutorial == null) {
            this.mReceiverOverlayPermissionTutorial = new ReceiverOverlayPermissionTutorial();
            registerReceiver(this.mReceiverOverlayPermissionTutorial, new IntentFilter(MicroScreenReceiver.MOTO_ACTION_MICROSCREEN_OVERLAY_PERMISSION_STATUS), "com.motorola.permission.MICROSCREEN_RECEIVER", null);
        }
    }

    public static void setHasSingleHandOverlayPermissionTutorial(boolean z) {
        sHasSingleHandOverlayPermissionTutorial = z;
    }

    public static boolean getHasSingleHandOverlayPermissionTutorial() {
        return sHasSingleHandOverlayPermissionTutorial;
    }
}
