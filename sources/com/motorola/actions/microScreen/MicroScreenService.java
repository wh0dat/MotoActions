package com.motorola.actions.microScreen;

import android.app.IntentService;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Handler;
import android.support.p001v4.content.LocalBroadcastManager;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.C0504R;
import com.motorola.actions.microScreen.instrumentation.MicroScreenInstrumentation;
import com.motorola.actions.microScreen.model.MicroScreenModel;
import com.motorola.actions.onenav.OneNavHelper;
import com.motorola.actions.reflect.MotorolaSettings;
import com.motorola.actions.utils.DFSquadUtils;
import com.motorola.actions.utils.Device;
import com.motorola.actions.utils.DisplayUtils;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.MotoResearchUtils;
import com.motorola.actions.utils.MotoSurveyUtils;
import com.motorola.actions.utils.RunningTasksUtils;
import com.motorola.actions.utils.ServiceUtils;

public class MicroScreenService extends IntentService {
    private static final int DEFAULT_SHOW_EXIT_HINT_TIME = 5000;
    public static final String EXTRA_CHECK_OVERLAY_PERMISSION = "checkOverlayPermission";
    private static final String EXTRA_EXIT_BY_INTERACTION = "exitByInteraction";
    private static final String EXTRA_GESTURE_DOWN_X = "downX";
    private static final String EXTRA_GESTURE_UP_X = "upX";
    private static final String EXTRA_RUNNING_NOTIFICATION_CONTENT = "runningNotificationContent";
    private static final String EXTRA_RUNNING_NOTIFICATION_TITLE = "runningNotificationTitle";
    private static final String EXTRA_SCREEN_POSITION = "screenPosition";
    private static final String EXTRA_SCREEN_SIZE = "screenSize";
    private static final String EXTRA_SHOW_EXIT_HINT = "showExitHint";
    private static final String EXTRA_SHOW_EXIT_HINT_TIME = "showExitHintTime";
    public static final String GESTURE_PERFORMED = "actions_microscreen_gesture_performed";
    public static final String GESTURE_PERFORMED_DIRECTION = "microscreen_gesture_performed_direction";
    private static final MALogger LOGGER = new MALogger(MicroScreenService.class);
    private static final int MAXIMUM_SHOW_EXIT_HINT_COUNT = 5;
    private static final String MICRO_SCREEN_SERVICE_ACTION_NAME = "com.motorola.frameworks.START_GLOBAL_SH_MODE";
    private static final String MICRO_SCREEN_SERVICE_CLASS_NAME = "com.motorola.frameworks.singlehand.SingleHandService";
    public static final String MICRO_SCREEN_SERVICE_PACKAGE_NAME = "com.motorola.frameworks.singlehand";
    public static final String SCREEN_POSITION_CENTER = "center";
    public static final String SCREEN_POSITION_LEFT = "left";
    public static final String SCREEN_POSITION_RIGHT = "right";
    private static final int SCREEN_SIZE_FOR_INCHES_40 = 3;
    private Handler mHandler;

    public MicroScreenService() {
        super("MicroScreenService");
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, MicroScreenService.class);
    }

    public void onCreate() {
        super.onCreate();
        this.mHandler = new Handler();
    }

    /* access modifiers changed from: protected */
    public void onHandleIntent(Intent intent) {
        MicroScreenReceiver.setCheckingPermissionFromMoto(false);
        if (isServiceEnabled()) {
            boolean isMicroScreenTutorialIsActive = MicroScreenModel.isMicroScreenTutorialIsActive();
            boolean isMicroScreenModeOn = MicroScreenModel.isMicroScreenModeOn();
            KeyguardManager keyguardManager = (KeyguardManager) getSystemService("keyguard");
            int orientation = getOrientation();
            if (keyguardManager == null) {
                LOGGER.mo11959e("Unable to retrieve access to keyguard manager");
            } else if (orientation == 1 && !keyguardManager.inKeyguardRestrictedInputMode()) {
                if (MotorolaSettings.isPipActive()) {
                    this.mHandler.post(MicroScreenService$$Lambda$0.$instance);
                    return;
                }
                Intent microScreenServiceIntent = getMicroScreenServiceIntent();
                if (microScreenServiceIntent != null) {
                    UserLockedReceiver.getInstance().register();
                    String screenPosition = getScreenPosition(intent);
                    microScreenServiceIntent.putExtra(EXTRA_SCREEN_POSITION, screenPosition);
                    microScreenServiceIntent.putExtra(EXTRA_SCREEN_SIZE, 3);
                    putMicroscreenNotificationExtras(microScreenServiceIntent);
                    if (!MicroScreenModel.isMicroScreenModeOn() && shouldShowExitHint()) {
                        microScreenServiceIntent.putExtra(EXTRA_SHOW_EXIT_HINT, getResources().getString(C0504R.string.sh_show_exit_hint));
                        microScreenServiceIntent.putExtra(EXTRA_SHOW_EXIT_HINT_TIME, 5000);
                    }
                    recordInstrumentationActivation(isMicroScreenTutorialIsActive, isMicroScreenModeOn);
                    recordInstrumentationActivationWithOneNav(isMicroScreenTutorialIsActive, isMicroScreenModeOn);
                    try {
                        if (isSingleHandPersistent()) {
                            ServiceUtils.startServiceSafe(microScreenServiceIntent);
                        } else {
                            ServiceUtils.startForegroundServiceSafe(microScreenServiceIntent);
                        }
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                    sendTutorialGesturePerformed(isMicroScreenTutorialIsActive, screenPosition);
                    recordInstrumentationTopAppPackageName(isMicroScreenTutorialIsActive, isMicroScreenModeOn, screenPosition);
                    MotoSurveyUtils.sendIntent(MotoResearchUtils.GESTURE_TYPE_MICROSCREEN);
                    DFSquadUtils.sendIntent(MotoResearchUtils.GESTURE_TYPE_MICROSCREEN);
                }
            }
        } else {
            LOGGER.mo11957d("Service disabled. Clean UserLockedReceiver");
            UserLockedReceiver.clean();
        }
    }

    private static boolean isSingleHandPersistent() {
        try {
            if (ActionsApplication.getAppContext().getPackageManager().getPackageInfo(MICRO_SCREEN_SERVICE_PACKAGE_NAME, 0).versionCode == 1) {
                return true;
            }
            return false;
        } catch (NameNotFoundException e) {
            LOGGER.mo11960e("Couldn't find single hand package", e);
            return false;
        }
    }

    public static Intent getMicroScreenServiceIntent() {
        Intent intent = new Intent();
        intent.setClassName(MICRO_SCREEN_SERVICE_PACKAGE_NAME, MICRO_SCREEN_SERVICE_CLASS_NAME);
        intent.setAction(MICRO_SCREEN_SERVICE_ACTION_NAME);
        return intent;
    }

    public static void stopMicroScreenService() {
        try {
            ActionsApplication.getAppContext().stopService(getMicroScreenServiceIntent());
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public static boolean isFeatureSupported() {
        return !Device.isZuiDevice() && !Device.isRobustaSDevice() && !Device.isRobustaNoteDevice() && !Device.isGolfLiteGoDevice() && !Device.isRobusta2Device();
    }

    public static boolean isServiceEnabled() {
        return isFeatureSupported() && MicroScreenModel.isMicroScreenEnabled();
    }

    private boolean shouldShowExitHint() {
        int microScreenHintCount = MicroScreenModel.getMicroScreenHintCount();
        if (microScreenHintCount >= 5) {
            return false;
        }
        MicroScreenModel.saveMicroScreenHintCounter(microScreenHintCount + 1);
        return true;
    }

    private String getScreenPosition(Intent intent) {
        String str = SCREEN_POSITION_CENTER;
        if (intent == null || !intent.hasExtra(EXTRA_GESTURE_DOWN_X) || !intent.hasExtra(EXTRA_GESTURE_UP_X)) {
            return str;
        }
        return intent.getFloatExtra(EXTRA_GESTURE_UP_X, 0.0f) < ((float) (DisplayUtils.getRealDisplayWidth(this) / 2)) ? SCREEN_POSITION_LEFT : SCREEN_POSITION_RIGHT;
    }

    public static void sendCheckOverlayPermission() {
        LOGGER.mo11957d("Check Overlay Permission of SingleHand Service");
        Intent microScreenServiceIntent = getMicroScreenServiceIntent();
        microScreenServiceIntent.putExtra(EXTRA_CHECK_OVERLAY_PERMISSION, true);
        putMicroscreenNotificationExtras(microScreenServiceIntent);
        ServiceUtils.startForegroundServiceSafe(microScreenServiceIntent);
    }

    public static void sendExitByInteractionIntent() {
        LOGGER.mo11957d("Send Exit By Interaction Intent");
        Intent microScreenServiceIntent = getMicroScreenServiceIntent();
        microScreenServiceIntent.putExtra(EXTRA_EXIT_BY_INTERACTION, true);
        putMicroscreenNotificationExtras(microScreenServiceIntent);
        ServiceUtils.startServiceSafe(microScreenServiceIntent);
    }

    private static void putMicroscreenNotificationExtras(Intent intent) {
        intent.putExtra(EXTRA_RUNNING_NOTIFICATION_TITLE, ActionsApplication.getAppContext().getString(C0504R.string.microscreen_running_notification_title));
        intent.putExtra(EXTRA_RUNNING_NOTIFICATION_CONTENT, ActionsApplication.getAppContext().getString(C0504R.string.microscreen_running_notification_content));
    }

    private void recordInstrumentationActivationWithOneNav(boolean z, boolean z2) {
        if (OneNavHelper.isOneNavPresent() && OneNavHelper.isOneNavEnabled() && !z2 && !z) {
            LOGGER.mo11957d("Instrumentation. OneNav is enabled and received Microscreen gesture");
            MicroScreenInstrumentation.recordActivationWithOneNavDailyEvent();
        }
    }

    private void recordInstrumentationActivation(boolean z, boolean z2) {
        if (!z2 && !z) {
            LOGGER.mo11957d("Instrumentation. Record microscreen activation");
            MicroScreenInstrumentation.recordActivationDailyEvent();
        }
    }

    private void recordInstrumentationTopAppPackageName(boolean z, boolean z2, String str) {
        if (!z2 && !z) {
            String topTaskPackageName = RunningTasksUtils.getTopTaskPackageName();
            MicroScreenInstrumentation.registerMicroScreenOn(topTaskPackageName, str);
            LOGGER.mo11957d("Instrumentation. Record package name of the foreground application");
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("packageName = ");
            sb.append(topTaskPackageName);
            mALogger.mo11957d(sb.toString());
        }
    }

    private void sendTutorialGesturePerformed(boolean z, String str) {
        if (z) {
            Intent intent = new Intent(GESTURE_PERFORMED);
            intent.setPackage(getBaseContext().getPackageName());
            intent.putExtra(GESTURE_PERFORMED_DIRECTION, str);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
    }

    private int getOrientation() {
        Resources resources = getResources();
        if (resources != null) {
            Configuration configuration = resources.getConfiguration();
            if (configuration != null) {
                return configuration.orientation;
            }
        }
        return 0;
    }
}
