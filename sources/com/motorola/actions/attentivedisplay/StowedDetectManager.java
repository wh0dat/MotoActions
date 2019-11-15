package com.motorola.actions.attentivedisplay;

import com.motorola.actions.eventsystem.EventSystem;
import com.motorola.actions.eventsystem.sensor.SensorConstants;
import com.motorola.actions.eventsystem.sensor.SensorNotFoundException;
import com.motorola.actions.reflect.SensorPrivateProxy;
import com.motorola.actions.utils.MALogger;
import p016io.reactivex.disposables.Disposable;

public class StowedDetectManager {
    private static final MALogger LOGGER = new MALogger(StowedDetectManager.class);
    private boolean mIsRegistered;
    private boolean mIsRunning;
    private StowedChangeListener mListener;
    private Disposable mStowedDisposable;
    private StowedState mStowedState = StowedState.UNKNOWN;

    private static class SingletonHolder {
        static final StowedDetectManager INSTANCE = new StowedDetectManager();

        private SingletonHolder() {
        }
    }

    public interface StowedChangeListener {
        void onStowedChange(boolean z);
    }

    public enum StowedState {
        UNKNOWN,
        STOWED,
        NOT_STOWED
    }

    public static StowedDetectManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public boolean start(StowedChangeListener stowedChangeListener) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("start, mIsRunning = ");
        sb.append(this.mIsRunning);
        sb.append(", listener = ");
        sb.append(stowedChangeListener);
        sb.append(", mStowedState = ");
        sb.append(this.mStowedState);
        mALogger.mo11957d(sb.toString());
        this.mListener = stowedChangeListener;
        this.mIsRunning = true;
        if (this.mListener != null) {
            this.mListener.onStowedChange(this.mStowedState != StowedState.NOT_STOWED);
        }
        return true;
    }

    public void stop() {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("stop(), mIsRunning = ");
        sb.append(this.mIsRunning);
        mALogger.mo11957d(sb.toString());
        if (this.mIsRunning) {
            this.mListener = null;
            this.mIsRunning = false;
        }
    }

    public boolean register() {
        if (!this.mIsRegistered) {
            LOGGER.mo11957d("register");
            try {
                this.mIsRegistered = true;
                this.mStowedDisposable = EventSystem.getInstance().subscribeSensorEvent(Integer.valueOf(SensorPrivateProxy.TYPE_STOWED), new StowedDetectManager$$Lambda$0(this));
            } catch (SensorNotFoundException unused) {
                LOGGER.mo11957d("Unable to register for stowed detection");
                this.mIsRegistered = false;
                return false;
            }
        }
        return true;
    }

    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:6:0x001e */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void unregister() {
        /*
            r4 = this;
            boolean r0 = r4.mIsRegistered
            if (r0 == 0) goto L_0x002e
            com.motorola.actions.utils.MALogger r0 = LOGGER
            java.lang.String r1 = "unregister"
            r0.mo11957d(r1)
            r0 = 0
            com.motorola.actions.eventsystem.EventSystem r1 = com.motorola.actions.eventsystem.EventSystem.getInstance()     // Catch:{ IllegalArgumentException -> 0x001e }
            int r2 = com.motorola.actions.reflect.SensorPrivateProxy.TYPE_STOWED     // Catch:{ IllegalArgumentException -> 0x001e }
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)     // Catch:{ IllegalArgumentException -> 0x001e }
            io.reactivex.disposables.Disposable r3 = r4.mStowedDisposable     // Catch:{ IllegalArgumentException -> 0x001e }
            r1.unsubscribeSensorEvent(r2, r3)     // Catch:{ IllegalArgumentException -> 0x001e }
            goto L_0x0025
        L_0x001c:
            r1 = move-exception
            goto L_0x002b
        L_0x001e:
            com.motorola.actions.utils.MALogger r1 = LOGGER     // Catch:{ all -> 0x001c }
            java.lang.String r2 = "Error unregistering stowed event."
            r1.mo11957d(r2)     // Catch:{ all -> 0x001c }
        L_0x0025:
            r4.mStowedDisposable = r0
            r0 = 0
            r4.mIsRegistered = r0
            goto L_0x002e
        L_0x002b:
            r4.mStowedDisposable = r0
            throw r1
        L_0x002e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.motorola.actions.attentivedisplay.StowedDetectManager.unregister():void");
    }

    /* access modifiers changed from: private */
    /* renamed from: stowedEvent */
    public void bridge$lambda$0$StowedDetectManager(Float f) {
        StowedState stowedState;
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("stowedEvent (");
        sb.append(f);
        sb.append(")");
        mALogger.mo11957d(sb.toString());
        if (this.mIsRegistered) {
            if (SensorConstants.STOWED_ENABLED.equals(f)) {
                LOGGER.mo11957d("Prox: Stowed");
                stowedState = StowedState.STOWED;
            } else {
                LOGGER.mo11957d("Prox: Not Stowed");
                stowedState = StowedState.NOT_STOWED;
            }
            if (this.mStowedState != stowedState) {
                this.mStowedState = stowedState;
                if (this.mIsRunning && this.mListener != null) {
                    this.mListener.onStowedChange(this.mStowedState == StowedState.STOWED);
                }
            }
        }
    }
}
