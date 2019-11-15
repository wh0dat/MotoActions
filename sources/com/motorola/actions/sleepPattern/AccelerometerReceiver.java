package com.motorola.actions.sleepPattern;

import android.app.Application;
import android.content.Context;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import com.motorola.actions.sleepPattern.common.Settings;
import com.motorola.actions.sleepPattern.common.TimeSlot;
import com.motorola.actions.sleepPattern.repository.SleepPatternRepository;
import com.motorola.actions.utils.EventOnlySensorListener;
import com.motorola.actions.utils.MALogger;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.inject.Inject;

public class AccelerometerReceiver extends EventOnlySensorListener {
    private static final MALogger LOGGER = new MALogger(AccelerometerReceiver.class);
    private final Runnable mCleanUpRunnable = new AccelerometerReceiver$$Lambda$0(this);
    private final Context mContext;
    private Map<TimeSlot, Integer> mEvents = new HashMap();
    private Handler mHandler;
    private int mLastX = 0;
    private int mLastY = 0;
    private int mLastZ = 0;
    private boolean mRegistered;
    private SensorManager mSensorManager;
    private SleepPatternRepository mSleepPatternRepository;

    @Inject
    AccelerometerReceiver(Application application, SleepPatternRepository sleepPatternRepository) {
        this.mContext = application;
        this.mSleepPatternRepository = sleepPatternRepository;
    }

    public void onSensorChanged(SensorEvent sensorEvent) {
        int round = Math.round(sensorEvent.values[0]);
        int round2 = Math.round(sensorEvent.values[1]);
        int round3 = Math.round(sensorEvent.values[2]);
        if (round != this.mLastX && round2 != this.mLastY && round3 != this.mLastZ) {
            this.mEvents.merge(new TimeSlot(Calendar.getInstance()), Integer.valueOf(1), AccelerometerReceiver$$Lambda$1.$instance);
            this.mLastX = round;
            this.mLastY = round2;
            this.mLastZ = round3;
        }
    }

    /* access modifiers changed from: 0000 */
    public void startAccelData(@NonNull Handler handler) {
        this.mHandler = handler;
        LOGGER.mo11957d("startAccelData");
        register();
        this.mHandler.removeCallbacks(this.mCleanUpRunnable);
        this.mHandler.postDelayed(this.mCleanUpRunnable, Settings.FIVE_MINUTES_IN_MILLISECONDS);
    }

    private void register() {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("register - mRegistered: ");
        sb.append(this.mRegistered);
        mALogger.mo11957d(sb.toString());
        if (!this.mRegistered && this.mHandler != null) {
            this.mSensorManager = (SensorManager) this.mContext.getSystemService("sensor");
            if (this.mSensorManager != null) {
                this.mSensorManager.registerListener(this, this.mSensorManager.getDefaultSensor(1), 3, this.mHandler);
                this.mRegistered = true;
            }
        }
    }

    private void unregister() {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("unregister - mRegistered: ");
        sb.append(this.mRegistered);
        mALogger.mo11957d(sb.toString());
        if (this.mRegistered) {
            this.mSensorManager.unregisterListener(this);
            this.mRegistered = false;
            this.mHandler = null;
        }
    }

    private void saveAccelerometerLogs() {
        for (Entry entry : this.mEvents.entrySet()) {
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("saveAccelerometerLogs - events[");
            sb.append(((TimeSlot) entry.getKey()).getHalfTime().getTime());
            sb.append("] = ");
            sb.append(entry.getValue());
            mALogger.mo11957d(sb.toString());
            this.mSleepPatternRepository.addOrUpdateAccelerometerLog((TimeSlot) entry.getKey(), ((Integer) entry.getValue()).intValue());
        }
    }

    /* access modifiers changed from: 0000 */
    public void shutDown() {
        LOGGER.mo11957d("shutdown");
        if (this.mHandler != null) {
            this.mHandler.removeCallbacks(this.mCleanUpRunnable);
            bridge$lambda$0$AccelerometerReceiver();
        }
        this.mHandler = null;
    }

    /* access modifiers changed from: private */
    /* renamed from: cleanUp */
    public void bridge$lambda$0$AccelerometerReceiver() {
        unregister();
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("run(): mEventCounter = ");
        sb.append(this.mEvents.size());
        mALogger.mo11957d(sb.toString());
        saveAccelerometerLogs();
        this.mEvents.clear();
    }
}
