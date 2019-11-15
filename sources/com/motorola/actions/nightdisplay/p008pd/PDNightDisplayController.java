package com.motorola.actions.nightdisplay.p008pd;

import android.os.Bundle;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.discovery.DiscoveryManager;
import com.motorola.actions.nightdisplay.common.PlatformAccess;
import com.motorola.actions.nightdisplay.instrumentation.NightDisplayInstrumentation;
import com.motorola.actions.nightdisplay.p009pi.PIEvent;
import com.motorola.actions.nightdisplay.p009pi.PIEvent.Event;
import com.motorola.actions.sleepPattern.SleepPatternService;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.SecurityUtils;
import com.motorola.actions.utils.SetupObserver;
import java.util.Calendar;

/* renamed from: com.motorola.actions.nightdisplay.pd.PDNightDisplayController */
public class PDNightDisplayController implements PlatformAccess {
    private static final MALogger LOGGER = new MALogger(PDNightDisplayController.class);
    private final ConfigManager mConfigManager = new ConfigManager(this.mHandlerThread);
    private final PdHandlerThread mHandlerThread = new PdHandlerThread(this);
    private final RunningNotificationManager mNightDisplayNotificationManager = new RunningNotificationManager();
    private final NightDisplayUpdateReceiver mNightDisplayUpdateReceiver = new NightDisplayUpdateReceiver(this.mHandlerThread);
    private final NotificationActionReceiver mNotificationActionReceiver = new NotificationActionReceiver(this.mHandlerThread);
    private final ScreenOnOffReceiver mScreenOnOffReceiver = new ScreenOnOffReceiver(this.mHandlerThread);
    private final SetupObserver mSetupObserver = new SetupObserver();
    private final TimeChangedReceiver mTimeChangedReceiver = new TimeChangedReceiver(this.mHandlerThread);
    private final UnlockReceiver mUnlockReceiver = new UnlockReceiver(this.mHandlerThread);
    private final UserChangedReceiver mUserChangedReceiver = new UserChangedReceiver(this.mHandlerThread);
    private final WakeUpAlarmUpdateReceiver mWakeUpAlarmUpdateReceiver = new WakeUpAlarmUpdateReceiver(this.mHandlerThread);

    public void enableTwilight(int i) {
        TwilightAccess.enable(i);
        if (i != 0) {
            NightDisplayInstrumentation.recordDailyFilterActivated();
        }
    }

    public void disableTwilight() {
        TwilightAccess.enable(0);
    }

    public void registerTimeStampReceiver() {
        this.mTimeChangedReceiver.register();
    }

    public void unregisterTimeStampReceiver() {
        this.mTimeChangedReceiver.unregister();
    }

    public void registerScreenOnOffReceiver() {
        this.mScreenOnOffReceiver.register();
    }

    public void unregisterScreenOnOffReceiver() {
        this.mScreenOnOffReceiver.unregister();
    }

    public void registerWakeUpAlarmUpdateReceiver() {
        this.mWakeUpAlarmUpdateReceiver.register();
    }

    public void unregisterWakeUpAlarmUpdateReceiver() {
        this.mWakeUpAlarmUpdateReceiver.unregister();
    }

    public void showModeUpgradeNotification() {
        LOGGER.mo11957d("showModeUpgradeNotification");
        DiscoveryManager.getInstance().onFDNEvent(FeatureKey.SLEEP_PATTERN);
    }

    public void createRunningServiceNotification(Calendar calendar, boolean z) {
        if (SecurityUtils.isUserUnlocked()) {
            this.mNightDisplayNotificationManager.createServiceRunningNotification(calendar, z);
        }
    }

    public void removeRunningServiceNotification() {
        this.mNightDisplayNotificationManager.dismissServiceRunningNotification();
    }

    public void registerNotificationActionReceiver() {
        this.mNotificationActionReceiver.register();
    }

    public void unregisterNotificationActionReceiver() {
        this.mNotificationActionReceiver.unregister();
    }

    public void start(Bundle bundle) {
        this.mHandlerThread.start();
        this.mConfigManager.register();
        this.mSetupObserver.observe(new PDNightDisplayController$$Lambda$0(this, bundle));
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$start$0$PDNightDisplayController(Bundle bundle) {
        this.mHandlerThread.event(new PIEvent(Event.INIT, bundle));
    }

    public void destroy() {
        this.mSetupObserver.clean();
        this.mConfigManager.unregister();
        this.mHandlerThread.event(new PIEvent(Event.DESTROY));
        this.mHandlerThread.quitSafely();
    }

    public void registerUserChangedReceiver() {
        this.mUserChangedReceiver.register();
    }

    public void unregisterUserChangedReceiver() {
        this.mUserChangedReceiver.unregister();
    }

    public void registerNightDisplayUpdateReceiver() {
        this.mNightDisplayUpdateReceiver.register();
    }

    public void unregisterNightDisplayUpdateReceiver() {
        this.mNightDisplayUpdateReceiver.unregister();
    }

    public void registerUnlockReceiver() {
        this.mUnlockReceiver.register();
    }

    public void unregisterUnlockReceiver() {
        this.mUnlockReceiver.unregister();
    }

    public int getColorTemperature() {
        return TwilightAccess.getNightDisplayIntensity();
    }

    public int getDefaultState() {
        return SleepPatternService.isFeatureSupported() ? 4 : 1;
    }
}
