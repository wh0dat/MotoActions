package com.motorola.actions.foc.gesture.state;

import com.motorola.actions.eventsystem.EventSystem;
import com.motorola.actions.eventsystem.sensor.SensorConstants;
import com.motorola.actions.eventsystem.sensor.SensorNotFoundException;
import com.motorola.actions.reflect.SensorPrivateProxy;
import com.motorola.actions.utils.MALogger;
import p016io.reactivex.disposables.Disposable;

public class StowedState implements IStateSource {
    private static final MALogger LOGGER = new MALogger(StowedState.class);
    private boolean mLastIsStowed;
    private Disposable mStowedDisposable;
    private boolean mStowedRegistered;

    public boolean isStateAcceptableToTurnOff() {
        return true;
    }

    /* access modifiers changed from: private */
    /* renamed from: stowedEvent */
    public void bridge$lambda$0$StowedState(Float f) {
        this.mLastIsStowed = SensorConstants.STOWED_ENABLED.equals(f);
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("Stowed state changed:");
        sb.append(this.mLastIsStowed);
        mALogger.mo11957d(sb.toString());
    }

    public StowedState() {
        start();
    }

    public void start() {
        LOGGER.mo11957d("start");
        if (!this.mStowedRegistered) {
            try {
                this.mStowedDisposable = EventSystem.getInstance().subscribeSensorEvent(Integer.valueOf(SensorPrivateProxy.TYPE_STOWED), new StowedState$$Lambda$0(this));
                this.mStowedRegistered = true;
            } catch (SensorNotFoundException unused) {
                this.mStowedRegistered = false;
                LOGGER.mo11959e("Could not register stowed.");
            }
        }
    }

    public boolean isStateAcceptableToTurnOn() {
        return !this.mLastIsStowed;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:?, code lost:
        LOGGER.mo11959e("Could not unregister stowed sensor");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x002f, code lost:
        r5.mStowedDisposable = null;
        r5.mLastIsStowed = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0033, code lost:
        throw r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0025, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0027 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void stop() {
        /*
            r5 = this;
            com.motorola.actions.utils.MALogger r0 = LOGGER
            java.lang.String r1 = "stop"
            r0.mo11957d(r1)
            boolean r0 = r5.mStowedRegistered
            if (r0 == 0) goto L_0x0034
            r0 = 0
            r1 = 0
            io.reactivex.disposables.Disposable r2 = r5.mStowedDisposable     // Catch:{ IllegalArgumentException -> 0x0027 }
            if (r2 == 0) goto L_0x0020
            com.motorola.actions.eventsystem.EventSystem r2 = com.motorola.actions.eventsystem.EventSystem.getInstance()     // Catch:{ IllegalArgumentException -> 0x0027 }
            int r3 = com.motorola.actions.reflect.SensorPrivateProxy.TYPE_STOWED     // Catch:{ IllegalArgumentException -> 0x0027 }
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)     // Catch:{ IllegalArgumentException -> 0x0027 }
            io.reactivex.disposables.Disposable r4 = r5.mStowedDisposable     // Catch:{ IllegalArgumentException -> 0x0027 }
            r2.unsubscribeSensorEvent(r3, r4)     // Catch:{ IllegalArgumentException -> 0x0027 }
        L_0x0020:
            r5.mStowedDisposable = r1
            r5.mLastIsStowed = r0
            goto L_0x0034
        L_0x0025:
            r2 = move-exception
            goto L_0x002f
        L_0x0027:
            com.motorola.actions.utils.MALogger r2 = LOGGER     // Catch:{ all -> 0x0025 }
            java.lang.String r3 = "Could not unregister stowed sensor"
            r2.mo11959e(r3)     // Catch:{ all -> 0x0025 }
            goto L_0x0020
        L_0x002f:
            r5.mStowedDisposable = r1
            r5.mLastIsStowed = r0
            throw r2
        L_0x0034:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.motorola.actions.foc.gesture.state.StowedState.stop():void");
    }
}
