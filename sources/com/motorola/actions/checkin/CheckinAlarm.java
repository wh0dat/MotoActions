package com.motorola.actions.checkin;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.approach.p006us.USAnalytics;
import com.motorola.actions.attentivedisplay.instrumentation.AttentiveDisplayAnalytics;
import com.motorola.actions.enhancedscreenshot.instrumentation.EnhancedScreenshotAnalytics;
import com.motorola.actions.foc.instrumentation.FlashOnChopAnalytics;
import com.motorola.actions.ftm.instrumentation.FlipToMuteAnalytics;
import com.motorola.actions.lts.instrumentation.LiftToSilenceAnalytics;
import com.motorola.actions.ltu.instrumentation.LiftToUnlockAnalytics;
import com.motorola.actions.mediacontrol.instrumentation.MediaControlAnalytics;
import com.motorola.actions.microScreen.instrumentation.MicroScreenAnalytics;
import com.motorola.actions.nightdisplay.instrumentation.NightDisplayAnalytics;
import com.motorola.actions.onenav.instrumentation.OneNavAnalytics;
import com.motorola.actions.p010qc.instrumentation.QuickCaptureAnalytics;
import com.motorola.actions.quickscreenshot.instrumentation.QuickScreenshotAnalytics;
import com.motorola.actions.sleepPattern.instrumentation.SleepPatternAnalytics;
import com.motorola.actions.utils.MALogger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CheckinAlarm {
    private static final MALogger LOGGER = new MALogger(CheckinAlarm.class);
    private static CheckinAlarm sInstance;
    protected final AlarmManager mAlarmManager = ((AlarmManager) ActionsApplication.getAppContext().getSystemService("alarm"));
    private List<BaseAnalytics> mAnalyticsList = new ArrayList();

    public static synchronized CheckinAlarm getInstance() {
        CheckinAlarm checkinAlarm;
        synchronized (CheckinAlarm.class) {
            if (sInstance == null) {
                sInstance = new CheckinAlarm();
                sInstance.setAlarm();
            }
            checkinAlarm = sInstance;
        }
        return checkinAlarm;
    }

    protected CheckinAlarm() {
    }

    private void setAlarm() {
        Context appContext = ActionsApplication.getAppContext();
        Intent intent = new Intent();
        intent.setClass(appContext, DailyBroadcastReceiver.class);
        intent.setAction(DailyBroadcastReceiver.ACTION_ALARM);
        PendingIntent broadcast = PendingIntent.getBroadcast(appContext, 0, intent, 0);
        LOGGER.mo11957d("SetAlarm");
        this.mAlarmManager.cancel(broadcast);
        this.mAlarmManager.setExactAndAllowWhileIdle(0, getNextMidnightInMs(), broadcast);
    }

    private long getNextMidnightInMs() {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(System.currentTimeMillis());
        instance.set(11, 0);
        instance.set(12, 0);
        instance.set(13, 0);
        instance.set(14, 0);
        instance.add(5, 1);
        return instance.getTimeInMillis();
    }

    public void performDailyCheckin() {
        LOGGER.mo11957d("onReceiveAlarm");
        updateAnalyticsList();
        for (BaseAnalytics baseAnalytics : this.mAnalyticsList) {
            baseAnalytics.updateEnabledStatus();
            baseAnalytics.updateDailyInformation();
            baseAnalytics.performDailyCheckin();
        }
        setAlarm();
    }

    /* access modifiers changed from: 0000 */
    public void conditionalAdd(@NonNull BaseAnalytics baseAnalytics) {
        if (baseAnalytics.isFeatureSupported()) {
            this.mAnalyticsList.add(baseAnalytics);
        }
    }

    private synchronized void updateAnalyticsList() {
        Context appContext = ActionsApplication.getAppContext();
        if (this.mAnalyticsList.size() == 0) {
            conditionalAdd(new QuickCaptureAnalytics(appContext));
            conditionalAdd(new USAnalytics(appContext));
            conditionalAdd(new LiftToSilenceAnalytics(appContext));
            conditionalAdd(new FlashOnChopAnalytics(appContext));
            conditionalAdd(new MicroScreenAnalytics(appContext));
            conditionalAdd(new FlipToMuteAnalytics(appContext));
            conditionalAdd(new NightDisplayAnalytics(appContext));
            conditionalAdd(new OneNavAnalytics(appContext));
            conditionalAdd(new AttentiveDisplayAnalytics(appContext));
            conditionalAdd(new QuickScreenshotAnalytics(appContext));
            conditionalAdd(new SleepPatternAnalytics(appContext));
            conditionalAdd(new LiftToUnlockAnalytics(appContext));
            conditionalAdd(new MediaControlAnalytics(appContext));
            conditionalAdd(new EnhancedScreenshotAnalytics(appContext));
        }
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("updateAnalyticsList - mAnalyticsList size: ");
        sb.append(this.mAnalyticsList.size());
        mALogger.mo11957d(sb.toString());
    }

    public BaseAnalytics getAnalytics(Class<?> cls) {
        updateAnalyticsList();
        for (BaseAnalytics baseAnalytics : this.mAnalyticsList) {
            if (cls.isInstance(baseAnalytics)) {
                MALogger mALogger = LOGGER;
                StringBuilder sb = new StringBuilder();
                sb.append("Returning analytics for: ");
                sb.append(cls.getSimpleName());
                mALogger.mo11957d(sb.toString());
                return baseAnalytics;
            }
        }
        return null;
    }

    public List<BaseAnalytics> getAnalyticsList() {
        updateAnalyticsList();
        return this.mAnalyticsList;
    }
}
