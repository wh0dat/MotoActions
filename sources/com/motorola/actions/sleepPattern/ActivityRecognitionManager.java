package com.motorola.actions.sleepPattern;

import android.app.Application;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.annotation.NonNull;
import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.sleepPattern.common.Settings;
import com.motorola.actions.sleepPattern.common.TimeSlot;
import com.motorola.actions.sleepPattern.repository.SleepPatternRepository;
import com.motorola.actions.utils.MALogger;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.inject.Inject;

public class ActivityRecognitionManager {
    public static final String ACTION_ACTIVITY_UPDATE_RECEIVED = "com.motorola.actions.sleepPattern.START_ACTIVITY_UPDATE_RECEIVED";
    public static final String ACTION_END_ACTIVITY_UPDATES = "com.motorola.actions.sleepPattern.END_ACTIVITY_UPDATES";
    /* access modifiers changed from: private */
    public static final MALogger LOGGER = new MALogger(ActivityRecognitionManager.class);
    private ActivityRecognitionClient mActivityRecognitionClient;
    private ActivityRecognitionReceiver mActivityRecognitionReceiver;
    private final Context mContext;
    private final Runnable mEndActivityUpdatesRunnable = new ActivityRecognitionManager$$Lambda$0(this);
    /* access modifiers changed from: private */
    public Handler mHandler;
    /* access modifiers changed from: private */
    public SleepPatternRepository mSleepPatternRepository;

    private class ActivityRecognitionReceiver extends BroadcastReceiver {
        /* access modifiers changed from: private */
        public Map<TimeSlot, Integer> mEvents;
        private boolean mRegistered;
        private final Runnable mSaveActivityLogs;

        private ActivityRecognitionReceiver() {
            this.mEvents = new HashMap();
            this.mSaveActivityLogs = new Runnable() {
                public void run() {
                    ActivityRecognitionManager.LOGGER.mo11957d("Saving activity logs if existent");
                    if (!ActivityRecognitionReceiver.this.mEvents.isEmpty()) {
                        for (Entry entry : ActivityRecognitionReceiver.this.mEvents.entrySet()) {
                            MALogger access$100 = ActivityRecognitionManager.LOGGER;
                            StringBuilder sb = new StringBuilder();
                            sb.append("saveActivityLogs - events[");
                            sb.append(((TimeSlot) entry.getKey()).getHalfTime().getTime());
                            sb.append("] = ");
                            sb.append(entry.getValue());
                            access$100.mo11957d(sb.toString());
                            ActivityRecognitionManager.this.mSleepPatternRepository.addOrUpdateAccelerometerLog((TimeSlot) entry.getKey(), ((Integer) entry.getValue()).intValue());
                        }
                        ActivityRecognitionReceiver.this.mEvents.clear();
                    }
                    ActivityRecognitionReceiver.this.unregister();
                }
            };
        }

        public void onReceive(Context context, Intent intent) {
            MALogger access$100 = ActivityRecognitionManager.LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("mEvents size: ");
            sb.append(this.mEvents.size());
            access$100.mo11957d(sb.toString());
            if (intent == null) {
                return;
            }
            if (!intent.getAction().equals(ActivityRecognitionManager.ACTION_ACTIVITY_UPDATE_RECEIVED)) {
                ActivityRecognitionManager.this.mHandler.post(this.mSaveActivityLogs);
            } else if (ActivityRecognitionResult.hasResult(intent)) {
                DetectedActivity mostProbableActivity = ActivityRecognitionResult.extractResult(intent).getMostProbableActivity();
                MALogger access$1002 = ActivityRecognitionManager.LOGGER;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Current activity detected: ");
                sb2.append(mostProbableActivity.toString());
                access$1002.mo11957d(sb2.toString());
                if (!isDeviceStill(mostProbableActivity)) {
                    ActivityRecognitionManager.LOGGER.mo11957d("Movement detected");
                    mergeEvents();
                }
            }
        }

        private boolean isDeviceStill(DetectedActivity detectedActivity) {
            return detectedActivity.getType() == 3 && detectedActivity.getConfidence() > 75;
        }

        private synchronized void mergeEvents() {
            this.mEvents.merge(new TimeSlot(Calendar.getInstance()), Integer.valueOf(1), ActivityRecognitionManager$ActivityRecognitionReceiver$$Lambda$0.$instance);
            MALogger access$100 = ActivityRecognitionManager.LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("Number of mEvents timeslots: ");
            sb.append(this.mEvents.size());
            access$100.mo11957d(sb.toString());
        }

        public void unregister() {
            ActivityRecognitionManager.LOGGER.mo11957d("Unregister activity recognition receiver");
            if (this.mRegistered) {
                try {
                    ActionsApplication.getAppContext().unregisterReceiver(this);
                } catch (IllegalArgumentException e) {
                    ActivityRecognitionManager.LOGGER.mo11960e("Unable to unregister activity recognition receiver", e);
                } catch (Throwable th) {
                    this.mRegistered = false;
                    throw th;
                }
                this.mRegistered = false;
            }
        }

        public void register() {
            ActivityRecognitionManager.LOGGER.mo11957d("Register activity recognition receiver");
            if (!this.mRegistered) {
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(ActivityRecognitionManager.ACTION_ACTIVITY_UPDATE_RECEIVED);
                intentFilter.addAction(ActivityRecognitionManager.ACTION_END_ACTIVITY_UPDATES);
                ActionsApplication.getAppContext().registerReceiver(this, intentFilter);
                this.mRegistered = true;
            }
        }
    }

    @Inject
    ActivityRecognitionManager(Application application, SleepPatternRepository sleepPatternRepository) {
        this.mContext = application;
        this.mSleepPatternRepository = sleepPatternRepository;
        this.mActivityRecognitionReceiver = new ActivityRecognitionReceiver();
    }

    /* access modifiers changed from: 0000 */
    public void startActivityData(Handler handler) {
        this.mActivityRecognitionClient = new ActivityRecognitionClient(this.mContext);
        this.mHandler = handler;
        this.mActivityRecognitionReceiver.register();
        LOGGER.mo11957d("Start gathering activity data");
        this.mHandler.postDelayed(this.mEndActivityUpdatesRunnable, Settings.FIVE_MINUTES_IN_MILLISECONDS);
        this.mActivityRecognitionClient.requestActivityUpdates(0, getActivityDetectionPendingIntent()).addOnSuccessListener(new OnSuccessListener<Void>() {
            public void onSuccess(Void voidR) {
                ActivityRecognitionManager.LOGGER.mo11957d("Activity recognition start request successful");
            }
        }).addOnFailureListener(new OnFailureListener() {
            public void onFailure(@NonNull Exception exc) {
                ActivityRecognitionManager.LOGGER.mo11957d("Activity recognition start request failed");
                ActivityRecognitionManager.this.shutDown();
            }
        });
    }

    private PendingIntent getActivityDetectionPendingIntent() {
        Intent intent = new Intent(ACTION_ACTIVITY_UPDATE_RECEIVED);
        intent.setPackage(this.mContext.getPackageName());
        return PendingIntent.getBroadcast(this.mContext, 0, intent, 134217728);
    }

    /* access modifiers changed from: 0000 */
    public void shutDown() {
        LOGGER.mo11957d("Activity updates shutdown");
        if (this.mHandler != null) {
            this.mHandler.removeCallbacks(this.mEndActivityUpdatesRunnable);
        }
        this.mHandler = null;
        if (this.mActivityRecognitionClient != null) {
            bridge$lambda$0$ActivityRecognitionManager();
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: cleanUp */
    public void bridge$lambda$0$ActivityRecognitionManager() {
        LOGGER.mo11957d("Cleaning up");
        this.mActivityRecognitionClient.removeActivityUpdates(getActivityDetectionPendingIntent());
        Intent intent = new Intent(ACTION_END_ACTIVITY_UPDATES);
        intent.setPackage(this.mContext.getPackageName());
        this.mContext.sendBroadcast(intent);
    }
}
