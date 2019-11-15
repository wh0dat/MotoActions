package com.motorola.actions.ftm;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.ftm.statemachines.FlipToMuteBaseMachineInterface;
import com.motorola.actions.ftm.statemachines.FlipToMuteSensorMachine;
import com.motorola.actions.ftm.statemachines.FlipToMuteStateMachine;
import com.motorola.actions.reflect.NotificationManagerPrivateProxy;
import com.motorola.actions.reflect.SensorPrivateProxy;
import com.motorola.actions.utils.Device;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.zenmode.AutomaticRulesConfigObserver;
import com.motorola.actions.zenmode.AutomaticRulesListener;
import com.motorola.actions.zenmode.ZenModeAccess;
import java.util.concurrent.TimeUnit;

public class FlipToMuteService extends Service implements AutomaticRulesConfigObserver {
    private static final String ACTION_FTM_GESTURE_TRIGGERED = "com.motorola.actions.FTM_GESTURE_TRIGGERED";
    public static final String AOV_PACKAGE = "com.motorola.audiomonitor";
    private static final String AOV_PERMISSION = "com.motorola.audiomonitor.permission.AOV_TTS_PLAY";
    private static final String KEY_DND_ENTERED_SYSTEM_TIME = "actions_ftm_dnd_time";
    private static final MALogger LOGGER = new MALogger(FlipToMuteService.class);
    public static final String TTM_PACKAGE = "com.motorola.motovoicelite";
    private static final String TTM_PERMISSION = "com.motorola.ttm.TTM_INTERACTION";
    private static boolean sIsDNDState;
    private FlipToMuteBaseMachineInterface mFlipToMuteMachine;
    private NotificationManager mNotificationManager;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        LOGGER.mo11957d("onCreate");
        super.onCreate();
        this.mNotificationManager = (NotificationManager) getSystemService("notification");
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("sharedPref(DND_ENABLED_BY_SERVICE)=");
        sb.append(SharedPreferenceManager.getBoolean(FlipToMuteConstants.DND_ENABLED_BY_SERVICE, false));
        mALogger.mo11957d(sb.toString());
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("onStartCommand, intent: ");
        sb.append(intent == null ? "null" : intent.toString());
        mALogger.mo11957d(sb.toString());
        if (!ZenModeAccess.hasMotorolaManageNotificationsPermission() && !this.mNotificationManager.isNotificationPolicyAccessGranted()) {
            SharedPreferenceManager.putBoolean(FlipToMuteConstants.KEY_ENABLED, false);
            stopSelf();
        }
        if (intent != null) {
            setDNDEnterSystemTime(true);
        }
        this.mFlipToMuteMachine = createStateMachine();
        this.mFlipToMuteMachine.start();
        return 1;
    }

    private FlipToMuteBaseMachineInterface createStateMachine() {
        if (!SensorPrivateProxy.isProxySensorAvailable(SensorPrivateProxy.TYPE_MOTO_FLIP_TO_MUTE_GESTURE)) {
            LOGGER.mo11957d("Sensor Not Detected!!...Creating the FTM Machine");
            return new FlipToMuteStateMachine(getApplicationContext(), this);
        }
        LOGGER.mo11957d("Sensor Detected!!...Creating the Sensor Machine");
        return new FlipToMuteSensorMachine(getApplicationContext(), this);
    }

    public void onDestroy() {
        LOGGER.mo11957d("onDestroy");
        super.onDestroy();
        if (this.mFlipToMuteMachine != null) {
            this.mFlipToMuteMachine.stop();
        }
        clearFlipToMuteSharedPrefs();
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, FlipToMuteService.class);
    }

    public static boolean isFeatureSupported() {
        return !Device.isJudoDevice() && !Device.isAndyDevice() && !Device.isGeorgeDevice() && !Device.isRobustaSDevice() && !Device.isRobustaNoteDevice() && !Device.isRobusta2Device();
    }

    public static boolean isServiceEnabled() {
        if (isFeatureSupported()) {
            return SharedPreferenceManager.getBoolean(FlipToMuteConstants.KEY_ENABLED, FeatureKey.FLIP_TO_DND.getEnableDefaultState());
        }
        return false;
    }

    private static long getDNDEnterSystemTime() {
        return SharedPreferenceManager.getLong(KEY_DND_ENTERED_SYSTEM_TIME, 0);
    }

    public static void setDNDEnterSystemTime(boolean z) {
        SharedPreferenceManager.putLong(KEY_DND_ENTERED_SYSTEM_TIME, z ? 0 : SystemClock.elapsedRealtime());
    }

    public static void broadcastFlipToMuteTriggered() {
        LOGGER.mo11957d("FlipToMute gesture triggered");
        sendFlipToMuteBroadcast(AOV_PACKAGE, AOV_PERMISSION);
        sendFlipToMuteBroadcast(TTM_PACKAGE, TTM_PERMISSION);
    }

    private static void sendFlipToMuteBroadcast(String str, String str2) {
        Intent intent = new Intent();
        intent.setPackage(str);
        intent.setAction(ACTION_FTM_GESTURE_TRIGGERED);
        ActionsApplication.getAppContext().sendBroadcast(intent, str2);
    }

    public static int getUsageTime() {
        int i;
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long dNDEnterSystemTime = getDNDEnterSystemTime();
        if (dNDEnterSystemTime != 0) {
            i = (int) TimeUnit.MILLISECONDS.toSeconds(elapsedRealtime - dNDEnterSystemTime);
        } else {
            LOGGER.mo11959e("DND timestamp invalid, but trying to calculate duration.");
            i = 0;
        }
        setDNDEnterSystemTime(true);
        return i;
    }

    private void clearFlipToMuteSharedPrefs() {
        SharedPreferenceManager.remove(FlipToMuteConstants.DND_ENABLED_BY_SERVICE);
        SharedPreferenceManager.remove(KEY_DND_ENTERED_SYSTEM_TIME);
        SharedPreferenceManager.remove(ZenModeAccess.ACTIONS_PREVIOUS_INTERRUPTION_FILTER);
    }

    public static synchronized boolean isDNDState() {
        boolean z;
        synchronized (FlipToMuteService.class) {
            z = sIsDNDState;
        }
        return z;
    }

    public static synchronized void setIsDNDState(boolean z) {
        synchronized (FlipToMuteService.class) {
            sIsDNDState = z;
        }
    }

    public static void enterZenMode() {
        int enterZenMode = ZenModeAccess.enterZenMode(SharedPreferenceManager.getInt(FlipToMuteConstants.DND_OPTION_SELECTED, 4));
        switch (enterZenMode) {
            case 1:
                LOGGER.mo11957d("enterZenMode, success to set interruption filter");
                SharedPreferenceManager.putBoolean(FlipToMuteConstants.DND_ENABLED_BY_SERVICE, true);
                return;
            case 2:
                LOGGER.mo11957d("enterZenMode, interruption filter already set");
                return;
            case 3:
                LOGGER.mo11957d("enterZenMode, failure to set interruption filter");
                Context appContext = ActionsApplication.getAppContext();
                appContext.stopService(createIntent(appContext));
                return;
            default:
                MALogger mALogger = LOGGER;
                StringBuilder sb = new StringBuilder();
                sb.append("enterZenMode, unhandled response: ");
                sb.append(enterZenMode);
                mALogger.mo11963w(sb.toString());
                return;
        }
    }

    public static void exitZenMode() {
        int exitZenMode = ZenModeAccess.exitZenMode();
        switch (exitZenMode) {
            case 1:
                LOGGER.mo11957d("exitZenMode, success to execute exitZenMode");
                return;
            case 2:
                LOGGER.mo11957d("exitZenMode, fail to execute exitZenMode");
                Context appContext = ActionsApplication.getAppContext();
                appContext.stopService(createIntent(appContext));
                return;
            default:
                MALogger mALogger = LOGGER;
                StringBuilder sb = new StringBuilder();
                sb.append("exitZenMode, unhandled response [");
                sb.append(exitZenMode);
                sb.append("] ");
                sb.append("for exitZenMode()");
                mALogger.mo11963w(sb.toString());
                return;
        }
    }

    public void onAutomaticRulesUpdate() {
        boolean isAtLeastOneAutomaticRuleActive = NotificationManagerPrivateProxy.isAtLeastOneAutomaticRuleActive(getApplicationContext());
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("onAutomaticRulesUpdate, One aut. rule active=");
        sb.append(isAtLeastOneAutomaticRuleActive);
        mALogger.mo11957d(sb.toString());
        if (!isDNDState()) {
            if (isAtLeastOneAutomaticRuleActive) {
                enterZenMode();
                return;
            }
            exitZenMode();
            AutomaticRulesListener.getInstance().unregister(this);
        } else if (!isAtLeastOneAutomaticRuleActive) {
            enterZenMode();
        }
    }
}
