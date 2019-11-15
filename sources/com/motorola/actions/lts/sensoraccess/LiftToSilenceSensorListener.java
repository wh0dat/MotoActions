package com.motorola.actions.lts.sensoraccess;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.approach.p005ir.SensorHub;
import com.motorola.actions.eventsystem.EventSystem;
import com.motorola.actions.eventsystem.sensor.SensorConstants;
import com.motorola.actions.eventsystem.sensor.SensorNotFoundException;
import com.motorola.actions.reflect.SensorPrivateProxy;
import com.motorola.actions.utils.EventOnlySensorListener;
import com.motorola.actions.utils.MALogger;
import java.util.List;
import p016io.reactivex.disposables.Disposable;

public class LiftToSilenceSensorListener {
    /* access modifiers changed from: private */
    public static final MALogger LOGGER = new MALogger(LiftToSilenceSensorListener.class);
    private static final int MAX_NUMBER_OF_ACCEPT_MOTIONS = 3;
    private static final int MOTION_HISTORY_TUTORIAL_MASK = 15;
    private SensorEventListener mFlatDownSensorListener = new EventOnlySensorListener() {
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent.sensor.getType() != SensorPrivateProxy.TYPE_FLAT_DOWN) {
                return;
            }
            if (sensorEvent.values[0] == 0.0f) {
                LiftToSilenceSensorListener.this.mFlatDownStateMachine.fireEvent(Event.FLAT_DOWN, false);
            } else {
                LiftToSilenceSensorListener.this.mFlatDownStateMachine.fireEvent(Event.FLAT_DOWN, true);
            }
        }
    };
    /* access modifiers changed from: private */
    public FlatDownStateMachine mFlatDownStateMachine;
    private SensorEventListener mFlatUpSensorListener = new EventOnlySensorListener() {
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent.sensor.getType() != SensorPrivateProxy.TYPE_FLAT_UP) {
                return;
            }
            if (sensorEvent.values[0] == 0.0f) {
                LiftToSilenceSensorListener.this.mFlatUpStateMachine.fireEvent(Event.FLAT_UP, false);
            } else {
                LiftToSilenceSensorListener.this.mFlatUpStateMachine.fireEvent(Event.FLAT_UP, true);
            }
        }
    };
    /* access modifiers changed from: private */
    public FlatUpStateMachine mFlatUpStateMachine;
    private final SensorEventListener mLiftToSilenceListener = new EventOnlySensorListener() {
        private static final double LTS_GESTURE_PERFORMED_FLAT_UP = 1.0d;

        public void onSensorChanged(SensorEvent sensorEvent) {
            MALogger access$200 = LiftToSilenceSensorListener.LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("Lift to Silence gesture performed (new sensor) ");
            sb.append(((double) sensorEvent.values[0]) == LTS_GESTURE_PERFORMED_FLAT_UP ? "FLAT UP" : "FLAT DOWN");
            access$200.mo11957d(sb.toString());
            TelephonyManager telephonyManager = (TelephonyManager) ActionsApplication.getAppContext().getSystemService("phone");
            if (telephonyManager == null) {
                LiftToSilenceSensorListener.LOGGER.mo11959e("Could not retrieve telephone manager");
            } else if (telephonyManager.getCallState() == 1) {
                LiftToSilenceSensorListener.this.mSensorObserver.onLiftToSilence();
            }
        }
    };
    /* access modifiers changed from: private */
    public ActionsSensorObserver mSensorObserver;
    private Disposable mStowedDisposable;

    private static int numberOfMotions(int i) {
        int i2 = 0;
        for (int i3 = 0; i3 < 8; i3++) {
            i2 += (i >> i3) & 1;
        }
        return i2;
    }

    /* access modifiers changed from: private */
    /* renamed from: stowedEvent */
    public void bridge$lambda$0$LiftToSilenceSensorListener(Float f) {
        if (SensorConstants.STOWED_ENABLED.equals(f)) {
            this.mFlatUpStateMachine.fireEvent(Event.STOWED, true);
            this.mFlatDownStateMachine.fireEvent(Event.STOWED, true);
            return;
        }
        this.mFlatUpStateMachine.fireEvent(Event.STOWED, false);
        this.mFlatDownStateMachine.fireEvent(Event.STOWED, false);
    }

    public LiftToSilenceSensorListener(@NonNull ActionsSensorObserver actionsSensorObserver) {
        this.mSensorObserver = actionsSensorObserver;
        this.mFlatUpStateMachine = new FlatUpStateMachine(actionsSensorObserver);
        this.mFlatDownStateMachine = new FlatDownStateMachine(actionsSensorObserver);
    }

    public void registerSensorListeners(SensorManager sensorManager, boolean z) {
        if (z || !SensorPrivateProxy.isProxySensorAvailable(SensorPrivateProxy.TYPE_MOTO_LIFT_TO_SILENCE_GESTURE)) {
            registerDefaultSensorListeners(sensorManager, z);
        } else {
            registerLiftToSilenceSensorListener(sensorManager);
        }
    }

    private void registerLiftToSilenceSensorListener(SensorManager sensorManager) {
        Sensor defaultSensor = sensorManager.getDefaultSensor(SensorPrivateProxy.TYPE_MOTO_LIFT_TO_SILENCE_GESTURE);
        if (defaultSensor == null) {
            List sensorList = sensorManager.getSensorList(SensorPrivateProxy.TYPE_MOTO_LIFT_TO_SILENCE_GESTURE);
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("getDefaultSensor returned null, try getting list: ");
            sb.append(sensorList);
            mALogger.mo11957d(sb.toString());
            if (sensorList != null && !sensorList.isEmpty()) {
                defaultSensor = (Sensor) sensorList.get(0);
                LOGGER.mo11957d("Sensor loaded from list");
            }
        }
        boolean registerListener = sensorManager.registerListener(this.mLiftToSilenceListener, defaultSensor, 3);
        MALogger mALogger2 = LOGGER;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("registering Lift to Silence listener - ");
        sb2.append(registerListener);
        mALogger2.mo11957d(sb2.toString());
    }

    private void registerDefaultSensorListeners(SensorManager sensorManager, boolean z) {
        LOGGER.mo11957d("registering default listeners (flatdown, flatup, stowed)");
        this.mFlatUpStateMachine.registerConditions();
        this.mFlatDownStateMachine.registerConditions();
        sensorManager.registerListener(this.mFlatDownSensorListener, sensorManager.getDefaultSensor(SensorPrivateProxy.TYPE_FLAT_DOWN), 3);
        sensorManager.registerListener(this.mFlatUpSensorListener, sensorManager.getDefaultSensor(SensorPrivateProxy.TYPE_FLAT_UP), 3);
        try {
            this.mStowedDisposable = EventSystem.getInstance().subscribeSensorEvent(Integer.valueOf(SensorPrivateProxy.TYPE_STOWED), new LiftToSilenceSensorListener$$Lambda$0(this));
        } catch (SensorNotFoundException unused) {
            LOGGER.mo11959e("Could not register stowed.");
        }
        if (!z || !SensorPrivateProxy.isProxySensorAvailable(SensorPrivateProxy.TYPE_MOTO_LIFT_TO_SILENCE_GESTURE)) {
            SensorHub.getMotionHistory(new LiftToSilenceSensorListener$$Lambda$1(this, z));
            return;
        }
        this.mFlatUpStateMachine.fireEvent(Event.MOTION, false);
        this.mFlatDownStateMachine.fireEvent(Event.MOTION, false);
    }

    /* access modifiers changed from: 0000 */
    /* renamed from: lambda$registerDefaultSensorListeners$0$LiftToSilenceSensorListener */
    public final /* synthetic */ void mo11153x93f71535(boolean z, int i) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("onMotionHistory: ");
        sb.append(i);
        mALogger.mo11957d(sb.toString());
        MALogger mALogger2 = LOGGER;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("onMotionHistory: numberOfMotions(motionHistory) = ");
        sb2.append(numberOfMotions(i));
        mALogger2.mo11957d(sb2.toString());
        if (z) {
            i &= 15;
            MALogger mALogger3 = LOGGER;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("onMotionHistory: Tutorial mode - numberOfMotions = ");
            sb3.append(numberOfMotions(i));
            mALogger3.mo11957d(sb3.toString());
        }
        if (numberOfMotions(i) <= 3) {
            this.mFlatUpStateMachine.fireEvent(Event.MOTION, false);
            this.mFlatDownStateMachine.fireEvent(Event.MOTION, false);
            return;
        }
        this.mFlatUpStateMachine.fireEvent(Event.MOTION, true);
        this.mFlatDownStateMachine.fireEvent(Event.MOTION, true);
    }

    public void unregisterSensorListeners(SensorManager sensorManager) {
        unregisterLiftToSilenceSensorListener(sensorManager);
        unregisterDefaultSensorListeners(sensorManager);
    }

    private void unregisterLiftToSilenceSensorListener(SensorManager sensorManager) {
        LOGGER.mo11957d("unregistering Lift to Silence listener");
        sensorManager.unregisterListener(this.mLiftToSilenceListener);
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(2:13|14) */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0030, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:?, code lost:
        LOGGER.mo11959e("Could not unregister stowed sensor");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0045, code lost:
        r3.mStowedDisposable = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0047, code lost:
        throw r0;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0032 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void unregisterDefaultSensorListeners(android.hardware.SensorManager r4) {
        /*
            r3 = this;
            com.motorola.actions.utils.MALogger r0 = LOGGER
            java.lang.String r1 = "unregistering default listeners (flatdown, flatup, stowed)"
            r0.mo11957d(r1)
            android.hardware.SensorEventListener r0 = r3.mFlatDownSensorListener
            if (r0 == 0) goto L_0x0010
            android.hardware.SensorEventListener r0 = r3.mFlatDownSensorListener
            r4.unregisterListener(r0)
        L_0x0010:
            android.hardware.SensorEventListener r0 = r3.mFlatUpSensorListener
            if (r0 == 0) goto L_0x0019
            android.hardware.SensorEventListener r0 = r3.mFlatUpSensorListener
            r4.unregisterListener(r0)
        L_0x0019:
            r4 = 0
            io.reactivex.disposables.Disposable r0 = r3.mStowedDisposable     // Catch:{ IllegalArgumentException -> 0x0032 }
            if (r0 == 0) goto L_0x002d
            com.motorola.actions.eventsystem.EventSystem r0 = com.motorola.actions.eventsystem.EventSystem.getInstance()     // Catch:{ IllegalArgumentException -> 0x0032 }
            int r1 = com.motorola.actions.reflect.SensorPrivateProxy.TYPE_STOWED     // Catch:{ IllegalArgumentException -> 0x0032 }
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch:{ IllegalArgumentException -> 0x0032 }
            io.reactivex.disposables.Disposable r2 = r3.mStowedDisposable     // Catch:{ IllegalArgumentException -> 0x0032 }
            r0.unsubscribeSensorEvent(r1, r2)     // Catch:{ IllegalArgumentException -> 0x0032 }
        L_0x002d:
            r3.mStowedDisposable = r4
            goto L_0x003a
        L_0x0030:
            r0 = move-exception
            goto L_0x0045
        L_0x0032:
            com.motorola.actions.utils.MALogger r0 = LOGGER     // Catch:{ all -> 0x0030 }
            java.lang.String r1 = "Could not unregister stowed sensor"
            r0.mo11959e(r1)     // Catch:{ all -> 0x0030 }
            goto L_0x002d
        L_0x003a:
            com.motorola.actions.lts.sensoraccess.FlatUpStateMachine r4 = r3.mFlatUpStateMachine
            r4.reset()
            com.motorola.actions.lts.sensoraccess.FlatDownStateMachine r3 = r3.mFlatDownStateMachine
            r3.reset()
            return
        L_0x0045:
            r3.mStowedDisposable = r4
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.motorola.actions.lts.sensoraccess.LiftToSilenceSensorListener.unregisterDefaultSensorListeners(android.hardware.SensorManager):void");
    }
}
