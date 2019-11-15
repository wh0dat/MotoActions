package com.motorola.actions;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ComponentInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.widget.Toast;
import com.motorola.actions.approach.FeatureManager;
import com.motorola.actions.checkin.CheckinAlarm;
import com.motorola.actions.discovery.DiscoveryManager;
import com.motorola.actions.notificationchannel.NotificationChannelManager;
import com.motorola.actions.p010qc.QuickCaptureConfig;
import com.motorola.actions.p013ui.settings.NightDisplaySettingsActivity;
import com.motorola.actions.utils.ActivityUtils;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.MotoAppUtils;
import com.motorola.actions.utils.SecurityUtils;
import com.motorola.actions.utils.ServiceUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class BootReceiver extends BroadcastReceiver {
    private static final MALogger LOGGER = new MALogger(BootReceiver.class);
    private static boolean sIsLockedServicesStarted;
    private static boolean sIsUnlockedServicesStarted;
    private final Handler mHandler = new Handler();

    public void onReceive(Context context, Intent intent) {
        String packageName = context.getPackageName();
        PackageManager packageManager = context.getPackageManager();
        boolean isSupportedForCurrentUser = MultiUserManager.isSupportedForCurrentUser();
        if (packageManager == null || !isSupportedForCurrentUser) {
            if (packageManager != null && !isSupportedForCurrentUser && packageManager.hasSystemFeature(packageName)) {
                QuickCaptureConfig.disableOnAndroidOne();
            }
        } else if (packageManager.hasSystemFeature(packageName)) {
            ServiceUtils.startServiceSafe(UserSwitchService.createIntent());
            NightDisplaySettingsActivity.updateEnabledStatus();
            boolean z = true;
            if (!MultiUserManager.isSystemUser() && SecurityUtils.isUserInDirectBoot()) {
                LOGGER.mo11957d("Not system user but User locked");
                z = false;
            }
            PackageStatusReceiver.getInstance().register();
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("intent: ");
            sb.append(intent);
            mALogger.mo11957d(sb.toString());
            if ("android.intent.action.MY_PACKAGE_REPLACED".equals(intent.getAction())) {
                startAllFeatures(context, ServiceStartReason.REPLACEMENT);
            } else if (!z) {
                MALogger mALogger2 = LOGGER;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Received intent: '");
                sb2.append(intent);
                sb2.append("' but conditions to start services are not fulfilled");
                mALogger2.mo11959e(sb2.toString());
            } else if ("android.intent.action.LOCKED_BOOT_COMPLETED".equals(intent.getAction())) {
                startLockedFeatures(context, ServiceStartReason.BOOT);
            } else if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
                CheckinAlarm.getInstance().performDailyCheckin();
                startUnlockedFeatures(context, ServiceStartReason.BOOT);
            } else {
                MALogger mALogger3 = LOGGER;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Received intent: '");
                sb3.append(intent);
                sb3.append("' but conditions to start services are not fulfilled");
                mALogger3.mo11959e(sb3.toString());
            }
            if (!MotoAppUtils.isMotoEnabled()) {
                LOGGER.mo11957d("Moto was disabled by user. No need to start FDNs.");
                DiscoveryManager.getInstance().unregisterFDNListeners();
            }
        } else {
            Toast.makeText(ActionsApplication.getAppContext(), C0504R.string.moto_actions_not_supported, 0).show();
            ActivityUtils.openMotorolaSite();
            disableMotoActionsComponents(context, packageManager);
        }
    }

    private static void startLockedFeatures(Context context, ServiceStartReason serviceStartReason) {
        try {
            LOGGER.mo11957d("startLockedFeatures");
            ForegroundNotificationService.start();
            FeatureManager.start(context, serviceStartReason);
            com.motorola.actions.foc.gesture.service.FeatureManager.start(context);
            com.motorola.actions.p010qc.FeatureManager.start(context);
            com.motorola.actions.lts.FeatureManager.start(context);
            com.motorola.actions.ftm.FeatureManager.start(context, serviceStartReason);
            com.motorola.actions.quickscreenshot.service.FeatureManager.start(context);
            com.motorola.actions.onenav.FeatureManager.start();
            com.motorola.actions.microScreen.FeatureManager.start(serviceStartReason);
            com.motorola.actions.mediacontrol.FeatureManager.start();
            com.motorola.actions.nightdisplay.FeatureManager.startDelayed(context, TimeUnit.SECONDS.toMillis(5));
            NotificationChannelManager.getInstance().start();
            if (MotoAppUtils.isMotoEnabled()) {
                DiscoveryManager.getInstance().start();
            }
            setIsLockedServicesStarted(true);
        } catch (Exception e) {
            LOGGER.mo11959e(e.getMessage());
        }
    }

    private static void startUnlockedFeatures(Context context, ServiceStartReason serviceStartReason) {
        try {
            if (!getIsLockedServicesStarted()) {
                startLockedFeatures(context, serviceStartReason);
            }
            if (!getIsUnlockedServicesStarted()) {
                LOGGER.mo11957d("startUnlockedFeatures");
                com.motorola.actions.enhancedscreenshot.FeatureManager.start();
                com.motorola.actions.attentivedisplay.FeatureManager.start(context);
                com.motorola.actions.sleepPattern.FeatureManager.start(context);
                com.motorola.actions.ltu.FeatureManager.start();
                setIsUnlockedServicesStarted(true);
                if (MultiUserManager.isSystemUser()) {
                    ForegroundNotificationService.stop();
                }
            }
        } catch (Exception e) {
            LOGGER.mo11959e(e.getMessage());
        }
    }

    public static void startAllFeatures(Context context, ServiceStartReason serviceStartReason) {
        LOGGER.mo11957d("startAllFeatures");
        startLockedFeatures(context, serviceStartReason);
        if (!SecurityUtils.isUserInDirectBoot()) {
            startUnlockedFeatures(context, serviceStartReason);
        }
    }

    private static void setIsLockedServicesStarted(boolean z) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("setIsLockedServicesStarted: ");
        sb.append(z);
        mALogger.mo11957d(sb.toString());
        sIsLockedServicesStarted = z;
    }

    private static boolean getIsLockedServicesStarted() {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("isLockedServicesStarted: ");
        sb.append(sIsLockedServicesStarted);
        mALogger.mo11957d(sb.toString());
        return sIsLockedServicesStarted;
    }

    private static void setIsUnlockedServicesStarted(boolean z) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("setIsUnlockedServicesStarted: ");
        sb.append(z);
        mALogger.mo11957d(sb.toString());
        sIsUnlockedServicesStarted = z;
    }

    private static boolean getIsUnlockedServicesStarted() {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("getIsUnlockedServicesStarted: ");
        sb.append(sIsUnlockedServicesStarted);
        mALogger.mo11957d(sb.toString());
        return sIsUnlockedServicesStarted;
    }

    public static void stopAllFeatures(Context context) {
        try {
            LOGGER.mo11957d("Stop all features");
            com.motorola.actions.microScreen.FeatureManager.stop();
            FeatureManager.stop(context);
            com.motorola.actions.attentivedisplay.FeatureManager.stop(context);
            com.motorola.actions.foc.gesture.service.FeatureManager.stop(context);
            com.motorola.actions.p010qc.FeatureManager.stop(context);
            com.motorola.actions.lts.FeatureManager.stop(context);
            com.motorola.actions.nightdisplay.FeatureManager.stop(context);
            com.motorola.actions.ftm.FeatureManager.stop(context);
            com.motorola.actions.quickscreenshot.service.FeatureManager.stop(context);
            com.motorola.actions.onenav.FeatureManager.stop();
            com.motorola.actions.enhancedscreenshot.FeatureManager.stop();
            com.motorola.actions.mediacontrol.FeatureManager.stop();
            com.motorola.actions.ltu.FeatureManager.stop();
            DiscoveryManager.getInstance().unregisterFDNListeners();
            com.motorola.actions.sleepPattern.FeatureManager.stop(context);
            NotificationChannelManager.getInstance().stop();
            setIsLockedServicesStarted(false);
            setIsUnlockedServicesStarted(false);
            ForegroundNotificationService.stop();
        } catch (Exception e) {
            LOGGER.mo11959e(e.getMessage());
        }
    }

    private void disableMotoActionsComponents(Context context, PackageManager packageManager) {
        LOGGER.mo11957d("Disable Moto Actions components");
        ArrayList arrayList = new ArrayList();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 15);
            if (packageInfo != null) {
                if (packageInfo.activities != null) {
                    arrayList.addAll(Arrays.asList(packageInfo.activities));
                }
                if (packageInfo.services != null) {
                    arrayList.addAll(Arrays.asList(packageInfo.services));
                }
                if (packageInfo.providers != null) {
                    arrayList.addAll(Arrays.asList(packageInfo.providers));
                }
                if (packageInfo.receivers != null) {
                    arrayList.addAll(Arrays.asList(packageInfo.receivers));
                }
            }
        } catch (NameNotFoundException e) {
            LOGGER.mo11959e(e.getMessage());
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            ComponentInfo componentInfo = (ComponentInfo) it.next();
            if (!componentInfo.name.equals(getClass().getName())) {
                this.mHandler.post(new BootReceiver$$Lambda$0(componentInfo, packageManager, context));
            }
        }
    }

    static final /* synthetic */ void lambda$disableMotoActionsComponents$0$BootReceiver(ComponentInfo componentInfo, PackageManager packageManager, Context context) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("Disabling ");
        sb.append(componentInfo.name);
        mALogger.mo11957d(sb.toString());
        packageManager.setComponentEnabledSetting(new ComponentName(context, componentInfo.name), 2, 1);
    }
}
