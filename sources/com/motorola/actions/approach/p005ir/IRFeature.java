package com.motorola.actions.approach.p005ir;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.approach.p005ir.IRService.Gestures;
import com.motorola.actions.reflect.SensorPrivateProxy;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.MotoResearchUtils;

/* renamed from: com.motorola.actions.approach.ir.IRFeature */
public abstract class IRFeature {
    private static final MALogger LOGGER = new MALogger(IRFeature.class);
    private static final long TIMEOUT_MILLIS = 900000;
    private boolean mEnabled;
    private boolean mFolioOpen;
    private Sensor mGestureSensor;
    private final Handler mHandler = new Handler();
    private boolean mIRPresent;
    protected final IRService mIRService;
    /* access modifiers changed from: private */
    public boolean mListening;
    private SensorManager mSensorManager;
    private final Runnable mTimeoutRunnable = new Runnable() {
        public void run() {
            if (IRFeature.this.mListening) {
                IRFeature.this.unregisterIRGestureListener();
            }
        }
    };
    private boolean mWantToListen;

    protected static int cap(int i, int i2) {
        return i > i2 + -1 ? i2 : i;
    }

    public abstract String getEnabledPrefKey();

    /* access modifiers changed from: protected */
    public abstract int getGestureSensorDelay();

    /* access modifiers changed from: protected */
    public abstract SensorEventListener getIRGestureListener();

    public abstract String getName();

    public abstract void start();

    public abstract void stop();

    public IRFeature(IRService iRService) {
        this.mIRService = iRService;
        this.mEnabled = false;
        this.mIRPresent = false;
        this.mListening = false;
        this.mWantToListen = false;
        this.mFolioOpen = true;
    }

    public void folioStateChanged(boolean z) {
        this.mFolioOpen = z;
        if (z && this.mWantToListen) {
            registerIRGestureListener();
            this.mWantToListen = false;
        } else if (!z && this.mListening) {
            unregisterIRGestureListener();
            this.mWantToListen = true;
        }
    }

    public void enable() {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("Enabling ");
        sb.append(getName());
        mALogger.mo11957d(sb.toString());
        this.mEnabled = true;
    }

    public void disable() {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("Disabling ");
        sb.append(getName());
        mALogger.mo11957d(sb.toString());
        this.mEnabled = false;
    }

    public boolean isEnabled() {
        return this.mEnabled;
    }

    public boolean canEnable() {
        return SharedPreferenceManager.getBoolean(getEnabledPrefKey(), true);
    }

    public static boolean isValidSwipe(SensorEvent sensorEvent) {
        return Gestures.GESTURE_SWIPE.equals(sensorEvent.values[1]);
    }

    public static boolean isEnabledInPrefs(Context context, String str) {
        return SharedPreferenceManager.getBoolean(str, true);
    }

    private void cancelTimeout() {
        if (this.mHandler != null) {
            this.mHandler.removeCallbacks(this.mTimeoutRunnable);
        }
    }

    private void startTimeout() {
        cancelTimeout();
        if (this.mHandler != null) {
            this.mHandler.postDelayed(this.mTimeoutRunnable, TIMEOUT_MILLIS);
        }
    }

    /* access modifiers changed from: protected */
    public String getFeatureInstrumentationState() {
        return (!this.mIRPresent || !isEnabled()) ? MotoResearchUtils.INTENT_EXTRA_FLASHLIGHT_NEW_STATE_OFF : MotoResearchUtils.INTENT_EXTRA_FLASHLIGHT_NEW_STATE_ON;
    }

    /* access modifiers changed from: protected */
    public void registerIRGestureListener() {
        boolean z;
        if (isEnabled() && !this.mListening) {
            if (this.mSensorManager == null) {
                this.mSensorManager = (SensorManager) this.mIRService.getSystemService("sensor");
            }
            if (!this.mFolioOpen) {
                this.mWantToListen = true;
                return;
            }
            this.mWantToListen = false;
            if (SensorPrivateProxy.isDefined(SensorPrivateProxy.TYPE_IR_GESTURE)) {
                this.mGestureSensor = this.mSensorManager.getDefaultSensor(SensorPrivateProxy.TYPE_IR_GESTURE);
                z = this.mSensorManager.registerListener(getIRGestureListener(), this.mGestureSensor, getGestureSensorDelay());
            } else {
                z = false;
            }
            if (z) {
                this.mIRPresent = true;
                this.mListening = true;
                startTimeout();
            } else {
                LOGGER.mo11957d("Could not register IR listener.");
                this.mIRPresent = false;
                this.mSensorManager = null;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void unregisterIRGestureListener() {
        cancelTimeout();
        if (this.mSensorManager != null) {
            if (this.mGestureSensor != null) {
                this.mSensorManager.unregisterListener(getIRGestureListener());
            }
            this.mSensorManager = null;
        }
        this.mListening = false;
        this.mWantToListen = false;
    }
}
