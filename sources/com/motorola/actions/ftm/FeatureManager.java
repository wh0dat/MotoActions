package com.motorola.actions.ftm;

import android.content.Context;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.ServiceStartReason;
import com.motorola.actions.ftm.receiver.SlpcSystemBootReceiver;
import com.motorola.actions.reflect.SensorPrivateProxy;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.RunningTasksUtils;
import com.motorola.actions.utils.SensorhubServiceManager;
import com.motorola.actions.utils.ServiceUtils;
import com.motorola.actions.utils.StaticBroadcastUtils;

public final class FeatureManager {
    private static final MALogger LOGGER = new MALogger(FeatureManager.class);

    public static void start(Context context, ServiceStartReason serviceStartReason) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("Flip to Mute FeatureManager receives : ");
        sb.append(serviceStartReason);
        mALogger.mo11957d(sb.toString());
        switch (serviceStartReason) {
            case BOOT:
            case USER_SWITCH_FOREGROUND:
            case MOTO_SERVICE:
                if (SensorPrivateProxy.isProxySensorAvailable(SensorPrivateProxy.TYPE_MOTO_FLIP_TO_MUTE_GESTURE) && FlipToMuteService.isServiceEnabled() && !RunningTasksUtils.isServiceRunning(context, FlipToMuteService.class)) {
                    ServiceUtils.startServiceSafe(FlipToMuteService.createIntent(context));
                }
                disableSlpcReceiverIfNecessary();
                return;
            case REPLACEMENT:
                disableSlpcReceiverIfNecessary();
                startFTMServiceIfNecessary();
                return;
            case SENSORHUB_BOOT:
                startFTMServiceIfNecessary();
                return;
            case ENTER_SETTINGS:
                return;
            default:
                MALogger mALogger2 = LOGGER;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Unkown start reason at FTMLauncher:");
                sb2.append(serviceStartReason);
                mALogger2.mo11959e(sb2.toString());
                return;
        }
    }

    private static void disableSlpcReceiverIfNecessary() {
        if (!SensorhubServiceManager.packageExists()) {
            LOGGER.mo11957d("Disabling SlpcSystemBootReceiver");
            StaticBroadcastUtils.disableStaticBroadcast(SlpcSystemBootReceiver.class);
            return;
        }
        LOGGER.mo11957d("Not disabling SlpcSystemBootReceiver");
    }

    private static void startFTMServiceIfNecessary() {
        if (FlipToMuteService.isServiceEnabled() && !isFTMRunning()) {
            ServiceUtils.startServiceSafe(FlipToMuteService.createIntent(ActionsApplication.getAppContext()));
        }
    }

    public static void stop(Context context) {
        if (FlipToMuteService.isFeatureSupported() && RunningTasksUtils.isServiceRunning(context, FlipToMuteService.class)) {
            LOGGER.mo11957d("stop : Flip to Mute");
            disableSlpcReceiverIfNecessary();
            context.stopService(FlipToMuteService.createIntent(context));
        }
    }

    private static boolean isFTMRunning() {
        boolean isServiceRunning = RunningTasksUtils.isServiceRunning(ActionsApplication.getAppContext(), FlipToMuteService.class);
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("isFTMRunning: ");
        sb.append(isServiceRunning);
        mALogger.mo11957d(sb.toString());
        return isServiceRunning;
    }
}
