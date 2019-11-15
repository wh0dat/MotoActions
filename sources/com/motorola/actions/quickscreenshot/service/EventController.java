package com.motorola.actions.quickscreenshot.service;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.eventsystem.EventSystem;
import com.motorola.actions.eventsystem.sensor.SensorConstants;
import com.motorola.actions.eventsystem.sensor.SensorNotFoundException;
import com.motorola.actions.quickscreenshot.PowerOffReceiver;
import com.motorola.actions.quickscreenshot.QuickScreenshotModel;
import com.motorola.actions.quickscreenshot.UserLockUnlockReceiver;
import com.motorola.actions.reflect.MotorolaSettings;
import com.motorola.actions.reflect.SensorPrivateProxy;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.SecurityUtils;
import p016io.reactivex.disposables.Disposable;

public class EventController implements EventRegistration, EventControlListener {
    /* access modifiers changed from: private */
    public static final MALogger LOGGER = new MALogger(EventController.class);
    private boolean mIsStowed;
    /* access modifiers changed from: private */
    public final UserLockUnlockReceiver mLockedReceiver = new UserLockUnlockReceiver(this);
    private final PowerOffReceiver mPowerOffReceiver = new PowerOffReceiver();
    private Disposable mStowedDisposable;
    private boolean mStowedRegistered;
    private final TelephonyListener mTelephonyListener = new TelephonyListener();
    private final TelephonyManager mTelephonyManager = ((TelephonyManager) ActionsApplication.getAppContext().getSystemService("phone"));

    private class TelephonyListener extends PhoneStateListener {
        private TelephonyListener() {
        }

        public void onCallStateChanged(int i, String str) {
            super.onCallStateChanged(i, str);
            MALogger access$100 = EventController.LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("onCallStateChanged, callState = ");
            sb.append(i);
            access$100.mo11957d(sb.toString());
            if (i == 2) {
                if (EventController.this.mLockedReceiver != null) {
                    EventController.this.mLockedReceiver.unregister();
                }
                EventController.this.unregisterStowedListener();
                MotorolaSettings.setQuickScreenshotMode(0);
                return;
            }
            if (EventController.this.mLockedReceiver != null) {
                EventController.this.mLockedReceiver.register();
            }
            EventController.this.registerStowedListener();
            EventController.this.updateStatus();
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: stowedEvent */
    public void bridge$lambda$0$EventController(Float f) {
        this.mIsStowed = SensorConstants.STOWED_ENABLED.equals(f);
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("Stowed state changed: ");
        sb.append(this.mIsStowed);
        mALogger.mo11957d(sb.toString());
        updateStatus();
    }

    EventController() {
    }

    public void start() {
        LOGGER.mo11957d("start");
        this.mLockedReceiver.register();
        this.mPowerOffReceiver.register();
        if (!SecurityUtils.isUserUnlocked()) {
            LOGGER.mo11957d("start(): Disable Quick Screenshot");
            MotorolaSettings.setQuickScreenshotMode(0);
            return;
        }
        registerStowedListener();
        this.mTelephonyManager.listen(this.mTelephonyListener, 32);
    }

    public void stop() {
        LOGGER.mo11957d("stop");
        this.mLockedReceiver.unregister();
        this.mPowerOffReceiver.unregister();
        unregisterStowedListener();
        this.mTelephonyManager.listen(this.mTelephonyListener, 0);
        MotorolaSettings.setQuickScreenshotMode(0);
    }

    public void onUserLocked() {
        LOGGER.mo11957d("onUserLocked");
        unregisterStowedListener();
        this.mTelephonyManager.listen(this.mTelephonyListener, 0);
    }

    public void onUserUnlocked() {
        LOGGER.mo11957d("onUserUnlocked");
        registerStowedListener();
        this.mTelephonyManager.listen(this.mTelephonyListener, 32);
        updateStatus();
    }

    /* access modifiers changed from: private */
    public void registerStowedListener() {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("registerStowedListener. Current: ");
        sb.append(this.mStowedRegistered);
        mALogger.mo11957d(sb.toString());
        if (!this.mStowedRegistered) {
            try {
                this.mStowedDisposable = EventSystem.getInstance().subscribeSensorEvent(Integer.valueOf(SensorPrivateProxy.TYPE_STOWED), new EventController$$Lambda$0(this));
                this.mStowedRegistered = true;
            } catch (SensorNotFoundException unused) {
                this.mStowedRegistered = false;
                LOGGER.mo11959e("Could not register stowed.");
            }
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Can't wrap try/catch for region: R(2:9|10) */
    /* JADX WARNING: Code restructure failed: missing block: B:10:?, code lost:
        LOGGER.mo11959e("Could not unregister stowed sensor");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0031, code lost:
        r5.mStowedDisposable = null;
        r5.mIsStowed = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0035, code lost:
        throw r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0027, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0029 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void unregisterStowedListener() {
        /*
            r5 = this;
            com.motorola.actions.utils.MALogger r0 = LOGGER
            java.lang.String r1 = "unregisterStowedListener"
            r0.mo11957d(r1)
            boolean r0 = r5.mStowedRegistered
            if (r0 == 0) goto L_0x0036
            r0 = 0
            r1 = 0
            r5.mStowedRegistered = r1     // Catch:{ IllegalArgumentException -> 0x0029 }
            io.reactivex.disposables.Disposable r2 = r5.mStowedDisposable     // Catch:{ IllegalArgumentException -> 0x0029 }
            if (r2 == 0) goto L_0x0022
            com.motorola.actions.eventsystem.EventSystem r2 = com.motorola.actions.eventsystem.EventSystem.getInstance()     // Catch:{ IllegalArgumentException -> 0x0029 }
            int r3 = com.motorola.actions.reflect.SensorPrivateProxy.TYPE_STOWED     // Catch:{ IllegalArgumentException -> 0x0029 }
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)     // Catch:{ IllegalArgumentException -> 0x0029 }
            io.reactivex.disposables.Disposable r4 = r5.mStowedDisposable     // Catch:{ IllegalArgumentException -> 0x0029 }
            r2.unsubscribeSensorEvent(r3, r4)     // Catch:{ IllegalArgumentException -> 0x0029 }
        L_0x0022:
            r5.mStowedDisposable = r0
            r5.mIsStowed = r1
            goto L_0x0036
        L_0x0027:
            r2 = move-exception
            goto L_0x0031
        L_0x0029:
            com.motorola.actions.utils.MALogger r2 = LOGGER     // Catch:{ all -> 0x0027 }
            java.lang.String r3 = "Could not unregister stowed sensor"
            r2.mo11959e(r3)     // Catch:{ all -> 0x0027 }
            goto L_0x0022
        L_0x0031:
            r5.mStowedDisposable = r0
            r5.mIsStowed = r1
            throw r2
        L_0x0036:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.motorola.actions.quickscreenshot.service.EventController.unregisterStowedListener():void");
    }

    public void updateStatus() {
        boolean z = !this.mIsStowed && SecurityUtils.isUserUnlocked() && this.mTelephonyManager.getCallState() != 2 && QuickScreenshotModel.isServiceEnabled();
        MotorolaSettings.setQuickScreenshotMode(z ? 1 : 0);
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("updateStatus: ");
        sb.append(z);
        mALogger.mo11957d(sb.toString());
    }
}
