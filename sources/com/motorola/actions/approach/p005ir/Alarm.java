package com.motorola.actions.approach.p005ir;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import com.motorola.actions.utils.MotoResearchUtils;
import com.motorola.actions.utils.MotoSurveyUtils;

/* renamed from: com.motorola.actions.approach.ir.Alarm */
public class Alarm extends IRFeature {
    private static final String ALARM_ALERT_ACTION = "com.android.deskclock.ALARM_ALERT";
    private static final String ALARM_DONE_ACTION = "com.android.deskclock.ALARM_DONE";
    private static final String ALARM_SNOOZE_ACTION = "com.android.deskclock.ALARM_SNOOZE";
    public static final String FEATURE_NAME = "alarm";
    private final BroadcastReceiver mAlarmReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Alarm.ALARM_ALERT_ACTION.equals(action)) {
                Alarm.this.mIsAlarmOn = true;
                Alarm.this.registerIRGestureListener();
            } else if (Alarm.ALARM_DONE_ACTION.equals(action) && Alarm.this.mIsAlarmOn) {
                Alarm.this.mIsAlarmOn = false;
                Alarm.this.unregisterIRGestureListener();
                Alarm.this.mIRService.alarmDismissed();
            }
        }
    };
    private final SensorEventListener mGestureListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int i) {
        }

        public void onSensorChanged(SensorEvent sensorEvent) {
            if (Alarm.this.mIsAlarmOn && IRFeature.isValidSwipe(sensorEvent)) {
                Alarm.this.mIRService.sendBroadcast(new Intent(Alarm.ALARM_SNOOZE_ACTION));
                MotoSurveyUtils.sendIntent(MotoResearchUtils.GESTURE_TYPE_WAVE_TO_DISMISS);
            }
        }
    };
    /* access modifiers changed from: private */
    public boolean mIsAlarmOn;

    public String getEnabledPrefKey() {
        return IRService.IR_SWIPE_ENABLED;
    }

    /* access modifiers changed from: protected */
    public int getGestureSensorDelay() {
        return IRService.SENSOR_DELAY_IR_SWIPE;
    }

    public String getName() {
        return "alarm";
    }

    public Alarm(IRService iRService) {
        super(iRService);
    }

    public void start() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ALARM_ALERT_ACTION);
        intentFilter.addAction(ALARM_DONE_ACTION);
        this.mIRService.registerReceiver(this.mAlarmReceiver, intentFilter);
    }

    public void stop() {
        this.mIRService.unregisterReceiver(this.mAlarmReceiver);
        if (this.mIsAlarmOn) {
            unregisterIRGestureListener();
        }
    }

    public void disable() {
        if (this.mIsAlarmOn) {
            unregisterIRGestureListener();
        }
        super.disable();
    }

    /* access modifiers changed from: protected */
    public SensorEventListener getIRGestureListener() {
        return this.mGestureListener;
    }
}
