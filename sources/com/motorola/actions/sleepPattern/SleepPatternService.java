package com.motorola.actions.sleepPattern;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import com.motorola.actions.ActionsApplicationContext;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.dagger.components.DaggerAppComponent;
import com.motorola.actions.dagger.modules.AppModule;
import com.motorola.actions.dagger.modules.SleepPatternModule;
import com.motorola.actions.nightdisplay.NightDisplayService;
import com.motorola.actions.sleepPattern.preprocessing.Utils;
import com.motorola.actions.utils.Device;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.SecurityUtils;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;

public class SleepPatternService extends Service {
    private static final int ALARM_RECEIVER_PERIODICITY_MS = ((int) TimeUnit.MINUTES.toMillis(30));
    private static final MALogger LOGGER = new MALogger(SleepPatternService.class);
    private static final String SERVICE_STARTED_KEY = "monitor_service_started";
    @Inject
    protected ProcessSleepEventThread mProcessSleepEventThread;
    private boolean mServiceStarted;
    @Inject
    protected SleepEventsAlarmReceiver mSleepEventsAlarmReceiver;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        this.mServiceStarted = false;
        DaggerAppComponent.builder().appModule(new AppModule(ActionsApplicationContext.getInstance().getApplication())).sleepPatternModule(new SleepPatternModule(ActionsApplicationContext.getInstance().getApplication())).build().inject(this);
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, SleepPatternService.class);
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        LOGGER.mo11957d("onStartCommand");
        if (this.mServiceStarted || SecurityUtils.isUserInDirectBoot()) {
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("Service started = ");
            sb.append(this.mServiceStarted);
            mALogger.mo11963w(sb.toString());
        } else {
            this.mServiceStarted = true;
            SharedPreferenceManager.putBoolean(SERVICE_STARTED_KEY, true);
            this.mProcessSleepEventThread.start();
            this.mSleepEventsAlarmReceiver.register();
            setSleepEventsAlarm(this);
        }
        return 1;
    }

    public void onDestroy() {
        LOGGER.mo11957d("onDestroy");
        this.mServiceStarted = false;
        SharedPreferenceManager.putBoolean(SERVICE_STARTED_KEY, false);
        this.mSleepEventsAlarmReceiver.unregister();
        this.mProcessSleepEventThread.shutDown();
        this.mProcessSleepEventThread.quitSafely();
    }

    public static boolean isServiceEnabled() {
        boolean z = SharedPreferenceManager.getBoolean(SERVICE_STARTED_KEY, FeatureKey.SLEEP_PATTERN.getEnableDefaultState());
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("isServiceEnabled: ");
        sb.append(z);
        mALogger.mo11957d(sb.toString());
        return z;
    }

    private static void setSleepEventsAlarm(Context context) {
        LOGGER.mo11957d("setSleepEventsAlarm");
        if (Utils.isValidAlarmInterval(Calendar.getInstance())) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService("alarm");
            if (alarmManager != null) {
                alarmManager.setRepeating(1, System.currentTimeMillis(), (long) ALARM_RECEIVER_PERIODICITY_MS, SleepEventsAlarmReceiver.getSleepPatternPendingIntent(context));
            }
        }
    }

    public static boolean isFeatureSupported() {
        return (!Device.isAndroidOne() || Device.isIbisFiDevice()) && NightDisplayService.isFeatureSupported();
    }

    public static void scheduleSleepPatternServiceStart(Context context) {
        context.stopService(createIntent(context));
        PendingIntent service = PendingIntent.getService(context, 0, new Intent(context, SleepPatternService.class), 134217728);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService("alarm");
        Calendar instance = Calendar.getInstance();
        instance.set(11, 18);
        instance.set(12, 0);
        instance.set(13, 0);
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("scheduleSleepPatternServiceStart: ");
        sb.append(instance.getTime());
        mALogger.mo11957d(sb.toString());
        if (alarmManager != null) {
            alarmManager.setExact(1, instance.getTimeInMillis(), service);
        }
    }

    public static int getDefaultState() {
        return isFeatureSupported() ? 4 : 1;
    }
}
