package com.motorola.actions.approach.p006us;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.annotation.NonNull;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.approach.GlanceConfiguration;
import com.motorola.actions.approach.p006us.AdspdLib.UltrasoundConfigListener;
import com.motorola.actions.approach.p006us.AdspdLib.UltrasoundConfigListener.Result;
import com.motorola.actions.eventsystem.EventSystem;
import com.motorola.actions.eventsystem.sensor.SensorConstants;
import com.motorola.actions.eventsystem.sensor.SensorNotFoundException;
import com.motorola.actions.reflect.PowerManagerPrivateProxy;
import com.motorola.actions.reflect.SensorPrivateProxy;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.MotoDisplayUtils;
import com.motorola.actions.utils.SensorhubServiceManager;
import com.motorola.actions.utils.SensorhubServiceManager.SensorhubUpdateListener;
import com.motorola.actions.utils.ServiceUtils;
import com.motorola.actions.utils.SettingsWrapper;
import java.util.concurrent.TimeUnit;
import p016io.reactivex.disposables.Disposable;

/* renamed from: com.motorola.actions.approach.us.USService */
public class USService extends Service implements UltrasoundConfigListener, USFirmwareUpdateCallback {
    private static final String KEY = "ir_aod_approach_enabled";
    /* access modifiers changed from: private */
    public static final MALogger LOGGER = new MALogger(USService.class);
    private static final long STOWED_TIMEOUT = TimeUnit.SECONDS.toMillis(5);
    private static final String US_STATE_KEY = "us_last_state";
    private static final long WAKELOCK_DURATION = TimeUnit.SECONDS.toMillis(7);
    private static final String WAKE_LOCK_TAG = "USWakeLock";
    private State mCurrentState = State.UNINITIALIZED;
    private Handler mHandler;
    private MotoDisplayObserver mMotoDisplayObserver;
    private PowerManager mPowerManager;
    private BroadcastReceiver mScreenOnOffReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String action = intent.getAction();
                MALogger access$200 = USService.LOGGER;
                StringBuilder sb = new StringBuilder();
                sb.append("Screen status changes: event=");
                sb.append(intent.getAction());
                access$200.mo11957d(sb.toString());
                if (USService.this.mStowedDetected) {
                    return;
                }
                if (PowerManagerPrivateProxy.ACTION_SCREEN_OFF_REASON.equals(action) || PowerManagerPrivateProxy.ACTION_SCREEN_DIM.equals(action)) {
                    USService.this.toggleUltrasound(true);
                } else if ("android.intent.action.SCREEN_ON".equals(action) || PowerManagerPrivateProxy.ACTION_SCREEN_BRIGHT.equals(action)) {
                    USService.this.toggleUltrasound(false);
                } else {
                    USService.LOGGER.mo11963w("Invalid action received in ScreenOnOffReceiver");
                }
            }
        }
    };
    private SensorhubServiceManager mSensorHubManager;
    private final SensorhubUpdateListener mSensorhubUpdateListener = new SensorhubUpdateListener() {
        public void onSensorhubReset() {
            USService.LOGGER.mo11961i("onSensorhubReset");
        }

        public void onSensorhubConnected() {
            USService.LOGGER.mo11961i("onSensorhubConnected");
        }
    };
    /* access modifiers changed from: private */
    public boolean mStowedDetected;
    private Disposable mStowedDisposable;
    private Runnable mStowedTimeout = new Runnable() {
        public void run() {
            if (USService.this.mStowedDetected) {
                USService.this.toggleUltrasound(false);
            }
        }
    };
    private USInstrumentation mUSInstrumentation;
    private WakeLock mWakeLock;

    /* renamed from: com.motorola.actions.approach.us.USService$MotoDisplayObserver */
    private static class MotoDisplayObserver {
        private final ContentObserver mDozeEnabledObserver = new ContentObserver(this.mHandler) {
            public void onChange(boolean z) {
                boolean isMotoDisplayEnabled = MotoDisplayUtils.isMotoDisplayEnabled();
                MALogger access$200 = USService.LOGGER;
                StringBuilder sb = new StringBuilder();
                sb.append("mMotoDisplayEnabledLastState - ");
                sb.append(MotoDisplayObserver.this.mMotoDisplayEnabledLastState);
                access$200.mo11957d(sb.toString());
                MALogger access$2002 = USService.LOGGER;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("isMotoDisplayEnabled         - ");
                sb2.append(isMotoDisplayEnabled);
                access$2002.mo11957d(sb2.toString());
                if (MotoDisplayObserver.this.mMotoDisplayEnabledLastState != isMotoDisplayEnabled) {
                    if (isMotoDisplayEnabled) {
                        USService.LOGGER.mo11957d("Moto Display was enabled. Toggle the service to US enable status");
                        USService.toggleService(ActionsApplication.getAppContext(), USService.isServiceEnabled());
                    } else {
                        USService.LOGGER.mo11957d("Moto Display was disabled. Toggle US service to false");
                        USService.toggleService(ActionsApplication.getAppContext(), false);
                    }
                }
                MotoDisplayObserver.this.mMotoDisplayEnabledLastState = isMotoDisplayEnabled;
            }
        };
        private Handler mHandler = new Handler();
        /* access modifiers changed from: private */
        public boolean mMotoDisplayEnabledLastState = MotoDisplayUtils.isMotoDisplayEnabled();
        private boolean mRegistered;

        MotoDisplayObserver() {
        }

        public void clean() {
            if (this.mRegistered && this.mDozeEnabledObserver != null) {
                ActionsApplication.getAppContext().getContentResolver().unregisterContentObserver(this.mDozeEnabledObserver);
            }
            this.mRegistered = false;
        }

        /* access modifiers changed from: 0000 */
        public void observe() {
            if (!this.mRegistered && this.mDozeEnabledObserver != null) {
                SettingsWrapper.getSecureUriFor(MotoDisplayUtils.SECURE_DOZE_ENABLED_KEY).ifPresent(new USService$MotoDisplayObserver$$Lambda$0(this));
            }
        }

        /* access modifiers changed from: 0000 */
        public final /* synthetic */ void lambda$observe$0$USService$MotoDisplayObserver(Uri uri) {
            ActionsApplication.getAppContext().getContentResolver().registerContentObserver(uri, true, this.mDozeEnabledObserver);
        }
    }

    /* renamed from: com.motorola.actions.approach.us.USService$State */
    enum State {
        UNINITIALIZED,
        INITIALIZED,
        ENABLED,
        DISABLED
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    /* access modifiers changed from: private */
    /* renamed from: releaseWakeLock */
    public void bridge$lambda$0$USService() {
        if (this.mWakeLock != null && this.mWakeLock.isHeld()) {
            LOGGER.mo11957d("WakeLock held, releasing it");
            this.mWakeLock.release();
        }
    }

    private void acquireWakeLock() {
        if (this.mWakeLock == null) {
            LOGGER.mo11957d("Creating WakeLock instance");
            this.mWakeLock = this.mPowerManager.newWakeLock(1, WAKE_LOCK_TAG);
        }
        if (this.mWakeLock != null && !this.mWakeLock.isHeld()) {
            this.mWakeLock.acquire(WAKELOCK_DURATION);
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("No WakeLock held, acquire new WakeLock for ");
            sb.append(WAKELOCK_DURATION);
            sb.append("ms");
            mALogger.mo11957d(sb.toString());
        }
    }

    public static boolean isUSSupported() {
        return isUSSupported(true);
    }

    public static boolean isUSSupported(boolean z) {
        boolean z2;
        boolean isUSGlanceSupported = isUSGlanceSupported();
        boolean isProxySensorAvailable = SensorPrivateProxy.isProxySensorAvailable(SensorPrivateProxy.TYPE_ULTRASOUND_GESTURE);
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("isUSSupported = ");
        sb.append(isProxySensorAvailable);
        mALogger.mo11957d(sb.toString());
        MALogger mALogger2 = LOGGER;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("isUSGlanceSupported = ");
        sb2.append(isUSGlanceSupported);
        mALogger2.mo11957d(sb2.toString());
        if (z) {
            z2 = MotoDisplayUtils.isApproachImplemented();
            MALogger mALogger3 = LOGGER;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("isUSMotoDisplay = ");
            sb3.append(z2);
            mALogger3.mo11957d(sb3.toString());
        } else {
            z2 = false;
        }
        if ((isProxySensorAvailable || isUSGlanceSupported) && !z2) {
            return true;
        }
        return false;
    }

    public static boolean isUSGlanceSupported() {
        return SensorPrivateProxy.isProxySensorAvailable(SensorPrivateProxy.TYPE_MOTO_GLANCE_APPROACH_GESTURE);
    }

    public void onCreate() {
        super.onCreate();
        LOGGER.mo11957d("onCreate");
        this.mSensorHubManager = new SensorhubServiceManager(ActionsApplication.getAppContext(), this.mSensorhubUpdateListener);
        this.mPowerManager = (PowerManager) getSystemService("power");
        this.mUSInstrumentation = new USInstrumentation();
        this.mHandler = new Handler();
    }

    public void onFinishUltrasoundConfig(String str, Result result) {
        switch (result) {
            case Success:
                MALogger mALogger = LOGGER;
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append(": successful");
                mALogger.mo11957d(sb.toString());
                if (MessageType.STOP_ULTRASOUND.toString().equals(str)) {
                    this.mHandler.post(new USService$$Lambda$0(this));
                    return;
                }
                return;
            case ConnectionFailure:
                MALogger mALogger2 = LOGGER;
                StringBuilder sb2 = new StringBuilder();
                sb2.append(str);
                sb2.append(": Error creating socket connection");
                mALogger2.mo11959e(sb2.toString());
                return;
            case ControlError:
                MALogger mALogger3 = LOGGER;
                StringBuilder sb3 = new StringBuilder();
                sb3.append(str);
                sb3.append(": Error calling control function");
                mALogger3.mo11959e(sb3.toString());
                return;
            default:
                LOGGER.mo11959e("Unexpected result on onFinishUltrasoundConfig()");
                return;
        }
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        LOGGER.mo11957d("onStartCommand");
        if (this.mMotoDisplayObserver == null) {
            this.mMotoDisplayObserver = new MotoDisplayObserver();
            this.mMotoDisplayObserver.observe();
        }
        boolean z = false;
        if (!MotoDisplayUtils.isMotoDisplayEnabled()) {
            toggleService(ActionsApplication.getAppContext(), false);
            LOGGER.mo11957d("Moto Display is not enabled. Stopping service");
        } else if (intent != null) {
            if (this.mCurrentState == State.UNINITIALIZED) {
                LOGGER.mo11957d("starting firmware update");
                new USFirmwareUpdater(this, this).executeSetupTask();
                this.mUSInstrumentation.retrieveInstrumentation(false);
            } else {
                USInstrumentation uSInstrumentation = this.mUSInstrumentation;
                if (this.mCurrentState == State.ENABLED) {
                    z = true;
                }
                uSInstrumentation.retrieveInstrumentation(z);
            }
        } else if (this.mCurrentState == State.UNINITIALIZED) {
            LOGGER.mo11957d("Restarting service. Trying to recover state");
            int i3 = SharedPreferenceManager.getInt(US_STATE_KEY, State.INITIALIZED.ordinal());
            if (i3 >= State.values().length || i3 < 0) {
                LOGGER.mo11959e("Invalid state saved");
                this.mCurrentState = State.INITIALIZED;
            } else {
                this.mCurrentState = State.values()[i3];
            }
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("Recovered state is:");
            sb.append(this.mCurrentState);
            mALogger.mo11957d(sb.toString());
            registerListeners();
        }
        return 1;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(12:0|(1:2)|3|4|7|8|10|11|12|(1:14)|15|16) */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0059, code lost:
        r5.mStowedDisposable = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x005b, code lost:
        throw r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0040, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:10:0x0042 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onDestroy() {
        /*
            r5 = this;
            super.onDestroy()
            com.motorola.actions.approach.us.USService$MotoDisplayObserver r0 = r5.mMotoDisplayObserver
            r1 = 0
            if (r0 == 0) goto L_0x000f
            com.motorola.actions.approach.us.USService$MotoDisplayObserver r0 = r5.mMotoDisplayObserver
            r0.clean()
            r5.mMotoDisplayObserver = r1
        L_0x000f:
            android.content.BroadcastReceiver r0 = r5.mScreenOnOffReceiver     // Catch:{ IllegalArgumentException -> 0x0015 }
            r5.unregisterReceiver(r0)     // Catch:{ IllegalArgumentException -> 0x0015 }
            goto L_0x0030
        L_0x0015:
            r0 = move-exception
            com.motorola.actions.utils.MALogger r2 = LOGGER
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "Error unregistering ScreenOnOffReceiver:"
            r3.append(r4)
            java.lang.String r0 = r0.toString()
            r3.append(r0)
            java.lang.String r0 = r3.toString()
            r2.mo11959e(r0)
        L_0x0030:
            com.motorola.actions.eventsystem.EventSystem r0 = com.motorola.actions.eventsystem.EventSystem.getInstance()     // Catch:{ IllegalArgumentException -> 0x0042 }
            int r2 = com.motorola.actions.reflect.SensorPrivateProxy.TYPE_STOWED     // Catch:{ IllegalArgumentException -> 0x0042 }
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)     // Catch:{ IllegalArgumentException -> 0x0042 }
            io.reactivex.disposables.Disposable r3 = r5.mStowedDisposable     // Catch:{ IllegalArgumentException -> 0x0042 }
            r0.unsubscribeSensorEvent(r2, r3)     // Catch:{ IllegalArgumentException -> 0x0042 }
            goto L_0x0049
        L_0x0040:
            r0 = move-exception
            goto L_0x0059
        L_0x0042:
            com.motorola.actions.utils.MALogger r0 = LOGGER     // Catch:{ all -> 0x0040 }
            java.lang.String r2 = "Could not unregister stowed sensor"
            r0.mo11959e(r2)     // Catch:{ all -> 0x0040 }
        L_0x0049:
            r5.mStowedDisposable = r1
            com.motorola.actions.utils.SensorhubServiceManager r0 = r5.mSensorHubManager
            if (r0 == 0) goto L_0x0054
            com.motorola.actions.utils.SensorhubServiceManager r0 = r5.mSensorHubManager
            r0.destroy()
        L_0x0054:
            com.motorola.actions.approach.us.USService$State r0 = com.motorola.actions.approach.p006us.USService.State.UNINITIALIZED
            r5.mCurrentState = r0
            return
        L_0x0059:
            r5.mStowedDisposable = r1
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.motorola.actions.approach.p006us.USService.onDestroy():void");
    }

    private void registerListeners() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(PowerManagerPrivateProxy.ACTION_SCREEN_DIM);
        intentFilter.addAction(PowerManagerPrivateProxy.ACTION_SCREEN_BRIGHT);
        intentFilter.addAction(PowerManagerPrivateProxy.ACTION_SCREEN_OFF_REASON);
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        registerReceiver(this.mScreenOnOffReceiver, intentFilter);
        try {
            this.mStowedDisposable = EventSystem.getInstance().subscribeSensorEvent(Integer.valueOf(SensorPrivateProxy.TYPE_STOWED), new USService$$Lambda$1(this));
        } catch (SensorNotFoundException unused) {
            LOGGER.mo11959e("Could not register to stowed sensor");
        }
    }

    public void onUpdateComplete(boolean z) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("firmware update completed. Result=");
        sb.append(z);
        mALogger.mo11957d(sb.toString());
        this.mCurrentState = State.INITIALIZED;
        toggleUltrasound(!this.mPowerManager.isInteractive());
        registerListeners();
    }

    /* access modifiers changed from: private */
    public void toggleUltrasound(boolean z) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("Toggle Ultrasound: activate=");
        sb.append(z);
        sb.append(" current state=");
        sb.append(this.mCurrentState);
        mALogger.mo11957d(sb.toString());
        if (this.mCurrentState != State.UNINITIALIZED) {
            if (!z) {
                if (this.mCurrentState == State.INITIALIZED || this.mCurrentState == State.ENABLED) {
                    LOGGER.mo11957d("Stopping Ultrasound");
                    if (this.mCurrentState == State.ENABLED) {
                        this.mUSInstrumentation.onStoppingUltrasound();
                    }
                    AdspdLib.stopUltrasound(this);
                    GlanceConfiguration.enableNudge(this.mSensorHubManager);
                }
                this.mCurrentState = State.DISABLED;
            } else if (this.mCurrentState == State.INITIALIZED || this.mCurrentState == State.DISABLED) {
                LOGGER.mo11957d("Starting Ultrasound");
                AdspdLib.startUltrasound(this);
                GlanceConfiguration.enableUs(this.mSensorHubManager);
                this.mCurrentState = State.ENABLED;
            }
            SharedPreferenceManager.putInt(US_STATE_KEY, this.mCurrentState.ordinal());
            return;
        }
        LOGGER.mo11959e("Trying to modify start/stop Ultrasound in uninitialized state.");
    }

    public static void toggleService(Context context, boolean z) {
        if (isUSSupported()) {
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("Ultrasound service: ");
            sb.append(z ? "starting" : "stopping");
            mALogger.mo11957d(sb.toString());
            Intent intent = new Intent(context, USService.class);
            if (z) {
                ServiceUtils.startServiceSafe(intent);
            } else {
                context.stopService(intent);
            }
        }
    }

    public static boolean isServiceEnabled() {
        return isServiceEnabled(true);
    }

    public static boolean isServiceEnabled(boolean z) {
        if (isUSSupported(z)) {
            return SharedPreferenceManager.getBoolean("ir_aod_approach_enabled", FeatureKey.APPROACH.getEnableDefaultState());
        }
        return false;
    }

    /* access modifiers changed from: private */
    /* renamed from: stowedEvent */
    public void bridge$lambda$1$USService(@NonNull Float f) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("Stowed status changes: event=");
        sb.append(f);
        mALogger.mo11957d(sb.toString());
        bridge$lambda$0$USService();
        this.mHandler.removeCallbacks(this.mStowedTimeout);
        if (SensorConstants.STOWED_ENABLED.equals(f)) {
            this.mStowedDetected = true;
        } else {
            this.mStowedDetected = false;
        }
        if (this.mPowerManager.isInteractive()) {
            return;
        }
        if (this.mStowedDetected) {
            acquireWakeLock();
            this.mHandler.postDelayed(this.mStowedTimeout, STOWED_TIMEOUT);
            return;
        }
        toggleUltrasound(true);
    }
}
