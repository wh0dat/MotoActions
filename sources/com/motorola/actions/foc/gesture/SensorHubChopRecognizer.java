package com.motorola.actions.foc.gesture;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.foc.gesture.event.listener.ChopEventListener;
import com.motorola.actions.reflect.SensorPrivateProxy;
import com.motorola.actions.utils.EventOnlySensorListener;
import com.motorola.actions.utils.MALogger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class SensorHubChopRecognizer {
    private static final MALogger LOGGER = new MALogger(SensorHubChopRecognizer.class);
    private static final long WAKELOCK_DURATION_MS = TimeUnit.SECONDS.toMillis(1);
    private static final String WAKE_LOCK_TAG = "FOCWakeLock";
    private final EventOnlySensorListener mChopChopListener = new EventOnlySensorListener() {
        public void onSensorChanged(SensorEvent sensorEvent) {
            SensorHubChopRecognizer.this.onChopDetected(sensorEvent.timestamp, (double) sensorEvent.values[0]);
        }
    };
    private final ArrayList<ChopEventListener> mChopEventListenerList = new ArrayList<>();
    private final Context mContext;
    private WakeLock mWakeLock;

    public SensorHubChopRecognizer(Context context) {
        this.mContext = context;
    }

    public void startSampling() {
        SensorManager sensorManager = (SensorManager) this.mContext.getSystemService("sensor");
        Sensor defaultSensor = sensorManager.getDefaultSensor(SensorPrivateProxy.TYPE_CHOPCHOP_GESTURE);
        if (defaultSensor != null && !sensorManager.registerListener(this.mChopChopListener, defaultSensor, 0)) {
            LOGGER.mo11959e("Unable to register ChopChop listener");
        }
    }

    public void stopSampling() {
        SensorManager sensorManager = (SensorManager) this.mContext.getSystemService("sensor");
        if (sensorManager != null) {
            sensorManager.unregisterListener(this.mChopChopListener);
        }
    }

    public void addChopListener(ChopEventListener chopEventListener) {
        this.mChopEventListenerList.add(chopEventListener);
    }

    public void removeChopListener(ChopEventListener chopEventListener) {
        this.mChopEventListenerList.remove(chopEventListener);
    }

    /* access modifiers changed from: private */
    public void onChopDetected(long j, double d) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("onChopDetected, current listeners ");
        sb.append(this.mChopEventListenerList.size());
        mALogger.mo11957d(sb.toString());
        releaseWakeLock();
        acquireWakeLock();
        Iterator it = this.mChopEventListenerList.iterator();
        while (it.hasNext()) {
            ChopEventListener chopEventListener = (ChopEventListener) it.next();
            if (chopEventListener != null) {
                chopEventListener.onChop(j, d);
            }
        }
    }

    public static boolean isRecognizerAvailable(Context context) {
        boolean isProxySensorAvailable = context.getPackageManager().hasSystemFeature("android.hardware.camera.flash") ? SensorPrivateProxy.isProxySensorAvailable(SensorPrivateProxy.TYPE_CHOPCHOP_GESTURE) : false;
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("isRecognizerAvailable: ");
        sb.append(isProxySensorAvailable);
        mALogger.mo11957d(sb.toString());
        return isProxySensorAvailable;
    }

    private void acquireWakeLock() {
        PowerManager powerManager = (PowerManager) ActionsApplication.getAppContext().getSystemService("power");
        if (this.mWakeLock == null && powerManager != null) {
            LOGGER.mo11957d("Creating WakeLock instance");
            this.mWakeLock = powerManager.newWakeLock(1, WAKE_LOCK_TAG);
        }
        if (this.mWakeLock != null && !this.mWakeLock.isHeld()) {
            this.mWakeLock.acquire(WAKELOCK_DURATION_MS);
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("No WakeLock held, acquire new WakeLock for ");
            sb.append(WAKELOCK_DURATION_MS);
            sb.append("ms");
            mALogger.mo11957d(sb.toString());
        }
    }

    private void releaseWakeLock() {
        if (this.mWakeLock != null && this.mWakeLock.isHeld()) {
            LOGGER.mo11957d("WakeLock held, releasing it");
            this.mWakeLock.release();
        }
    }
}
