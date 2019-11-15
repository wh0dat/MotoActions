package com.motorola.actions.foc.gesture;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.discovery.DiscoveryManager;
import com.motorola.actions.foc.gesture.event.ChopChopEvent;
import com.motorola.actions.foc.gesture.event.IEventSource;
import com.motorola.actions.foc.gesture.event.TimeoutEvent;
import com.motorola.actions.foc.gesture.event.TorchEvent;
import com.motorola.actions.foc.gesture.event.listener.ChopEventListener;
import com.motorola.actions.foc.gesture.event.listener.TimeoutEventListener;
import com.motorola.actions.foc.gesture.event.listener.TorchEventListener;
import com.motorola.actions.foc.gesture.service.FlashOnChopService;
import com.motorola.actions.foc.gesture.state.CameraAvailabilityState;
import com.motorola.actions.foc.gesture.state.IStateSource;
import com.motorola.actions.foc.gesture.state.ModAccessState;
import com.motorola.actions.foc.gesture.state.ProvisionedState;
import com.motorola.actions.foc.gesture.state.StowedState;
import com.motorola.actions.foc.gesture.state.TorchState;
import com.motorola.actions.foc.gesture.state.WeChatState;
import com.motorola.actions.foc.gesture.torch.FlashlightAccess;
import com.motorola.actions.foc.gesture.torch.FlashlightAccessCameraManager;
import com.motorola.actions.foc.gesture.torch.FlashlightAccessFlashlightController;
import com.motorola.actions.foc.gesture.util.FlashOnChopExceptions.AlgorithmNotDefinedException;
import com.motorola.actions.foc.gesture.util.FlashOnChopUtils;
import com.motorola.actions.foc.instrumentation.FlashOnChopInstrumentation;
import com.motorola.actions.utils.DFSquadUtils;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.MotoResearchUtils;
import com.motorola.actions.utils.MotoSurveyUtils;
import com.motorola.actions.utils.SetupObserver;
import java.util.ArrayList;
import java.util.Iterator;

public class FlashOnChopGestureManager implements ChopEventListener, TimeoutEventListener, TorchEventListener {
    private static final long DELAY_BETWEEN_CHOPS_TIMEOUT_MS = 750;
    /* access modifiers changed from: private */
    public static final MALogger LOGGER = new MALogger(FlashOnChopGestureManager.class);
    private static final int TIME_DELAYED_MESSAGE_TURNOFF_TURNON_FLASHLIGHT = 800;
    private static final long TOGGLE_FEEDBACK_TIMEOUT_MS = 1500;
    private static final int TOGGLE_FLASHLIGHT = 1;
    public static final int TURNOFF_FLASHLIGHT = 2;
    private static boolean sIsFlashlightOn;
    /* access modifiers changed from: private */
    public String mCameraId;
    /* access modifiers changed from: private */
    public final CameraManager mCameraManager;
    /* access modifiers changed from: private */
    public final Context mContext;
    private FlashlightAccess mFlashlightAccess;
    /* access modifiers changed from: private */
    public FlashlightController mFlashlightController;
    /* access modifiers changed from: private */
    @SuppressLint({"HandlerLeak"})
    public final Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    FlashOnChopGestureManager.this.toggleFlashlight();
                    return;
                case 2:
                    FlashOnChopGestureManager.this.turnoffFlashlightAndHandle();
                    return;
                default:
                    FlashOnChopGestureManager.LOGGER.mo11959e("Invalid message");
                    return;
            }
        }
    };
    /* access modifiers changed from: private */
    public ArrayList<IEventSource> mIEventSources;
    /* access modifiers changed from: private */
    public ArrayList<IStateSource> mIStateSources;
    private boolean mIsRunning;
    private long mLastChopMovementTimeMs = 0;
    private long mLastToggleTimeMs = 0;
    private final OnSharedPreferenceChangeListener mSharedPreferenceListener = new FlashOnChopGestureManager$$Lambda$0(this);

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$new$0$FlashOnChopGestureManager(SharedPreferences sharedPreferences, String str) {
        if (FlashOnChopService.KEY_TURN_ON_BY_PERMISSIONS_GRANTED.equals(str)) {
            checkToTurnOnByPermissionGranted();
        }
    }

    public FlashOnChopGestureManager(Context context) {
        this.mContext = context;
        this.mCameraManager = (CameraManager) this.mContext.getSystemService("camera");
    }

    private boolean setTorchMode(boolean z) {
        boolean z2;
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("setTorchMode - START - enabled: ");
        sb.append(z);
        mALogger.mo11957d(sb.toString());
        boolean z3 = false;
        try {
            this.mFlashlightAccess = FlashlightAccess.create(this.mCameraManager, this.mCameraId, z, this.mFlashlightAccess);
            if (this.mFlashlightAccess.setTorch(z, this.mLastChopMovementTimeMs)) {
                z2 = true;
                try {
                    this.mLastChopMovementTimeMs = SystemClock.elapsedRealtime();
                } catch (AlgorithmNotDefinedException e) {
                    e = e;
                }
                z3 = z2;
            }
        } catch (AlgorithmNotDefinedException e2) {
            e = e2;
            z2 = false;
            FlashOnChopUtils.sendToForeground(false);
            LOGGER.mo11957d("Set Torch mode : Not a valid algorithm");
            MALogger mALogger2 = LOGGER;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Camera API Exception of ");
            sb2.append(e.getClass().getName());
            sb2.append(" : ");
            sb2.append(e.getMessage());
            mALogger2.mo11959e(sb2.toString());
            z3 = z2;
            LOGGER.mo11957d("setTorchMode: recordFlashlightEnabledApiUsage");
            recordFlashlightEnabledApiUsage(this.mFlashlightAccess);
            MALogger mALogger3 = LOGGER;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("setTorchMode - END - isSetTorchSuccess: ");
            sb3.append(z3);
            mALogger3.mo11957d(sb3.toString());
            return z3;
        }
        if (z3 && z) {
            LOGGER.mo11957d("setTorchMode: recordFlashlightEnabledApiUsage");
            recordFlashlightEnabledApiUsage(this.mFlashlightAccess);
        }
        MALogger mALogger32 = LOGGER;
        StringBuilder sb32 = new StringBuilder();
        sb32.append("setTorchMode - END - isSetTorchSuccess: ");
        sb32.append(z3);
        mALogger32.mo11957d(sb32.toString());
        return z3;
    }

    private void initEventSources() {
        this.mIEventSources = new ArrayList<>();
        this.mIEventSources.add(new ChopChopEvent(this));
    }

    private void initStateSources() {
        this.mIStateSources = new ArrayList<>();
        this.mIStateSources.add(new StowedState());
        this.mIStateSources.add(new ModAccessState());
        this.mIStateSources.add(new WeChatState());
        this.mIStateSources.add(new ProvisionedState());
    }

    /* access modifiers changed from: private */
    public IEventSource getEventSource(Class<?> cls) {
        if (this.mIEventSources != null) {
            Iterator it = this.mIEventSources.iterator();
            while (it.hasNext()) {
                IEventSource iEventSource = (IEventSource) it.next();
                if (cls.isInstance(iEventSource)) {
                    return iEventSource;
                }
            }
        }
        return null;
    }

    /* access modifiers changed from: private */
    public IStateSource getStateSource(Class<?> cls) {
        if (this.mIStateSources != null) {
            Iterator it = this.mIStateSources.iterator();
            while (it.hasNext()) {
                IStateSource iStateSource = (IStateSource) it.next();
                if (cls.isInstance(iStateSource)) {
                    return iStateSource;
                }
            }
        }
        return null;
    }

    public void sendMessage(int i) {
        sendMessage(this.mHandler, i);
    }

    private void sendMessage(Handler handler, int i) {
        Message message = new Message();
        message.what = i;
        handler.sendMessage(message);
    }

    private void sendDelayedMessage(Handler handler, int i, int i2) {
        Message message = new Message();
        message.what = i;
        handler.sendMessageDelayed(message, (long) i2);
    }

    public void start() {
        LOGGER.mo11957d("FlashOnChopGestureManager: start()");
        if (!this.mIsRunning) {
            this.mIsRunning = true;
            FlashOnChopInstrumentation.startRecordTimeEnabledSecs();
            initializeCameraDeviceStateListener();
            initEventSources();
            initStateSources();
            if (SetupObserver.isSetupFinished()) {
                checkToTurnOnByPermissionGranted();
            }
            SharedPreferenceManager.registerOnSharedPreferenceChangeListener(this.mSharedPreferenceListener);
        }
    }

    public void stop() {
        LOGGER.mo11957d("FlashOnChopGestureManager: stop()");
        if (this.mIsRunning) {
            if (SetupObserver.isSetupFinished()) {
                turnoffFlashlightAndHandle();
            }
            if (this.mIEventSources != null) {
                Iterator it = this.mIEventSources.iterator();
                while (it.hasNext()) {
                    ((IEventSource) it.next()).stop();
                }
            }
            if (this.mIStateSources != null) {
                Iterator it2 = this.mIStateSources.iterator();
                while (it2.hasNext()) {
                    ((IStateSource) it2.next()).stop();
                }
            }
            FlashOnChopInstrumentation.stopRecordTimeEnabledSecs();
            this.mIsRunning = false;
            this.mIEventSources = null;
            this.mIStateSources = null;
            SharedPreferenceManager.unregisterOnSharedPreferenceChangeListener(this.mSharedPreferenceListener);
        }
    }

    public static boolean isFlashlightOn() {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("isFlashlightOn: ");
        sb.append(sIsFlashlightOn);
        mALogger.mo11957d(sb.toString());
        return sIsFlashlightOn;
    }

    public static void setFlashlightOn(boolean z) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("setFlashlightOn: ");
        sb.append(z);
        mALogger.mo11957d(sb.toString());
        sIsFlashlightOn = z;
    }

    public void onChop(long j, double d) {
        boolean z;
        LOGGER.mo11957d("onChop");
        if (!SetupObserver.isSetupFinished()) {
            LOGGER.mo11959e("FOC should not work on OOBE");
        } else if (isMinimumValidTime()) {
            Iterator it = this.mIStateSources.iterator();
            while (it.hasNext()) {
                IStateSource iStateSource = (IStateSource) it.next();
                MALogger mALogger = LOGGER;
                StringBuilder sb = new StringBuilder();
                sb.append("Checking state from ");
                sb.append(iStateSource.getClass().getSimpleName());
                mALogger.mo11963w(sb.toString());
                if (isFlashlightOn()) {
                    z = iStateSource.isStateAcceptableToTurnOff();
                    continue;
                } else {
                    z = iStateSource.isStateAcceptableToTurnOn();
                    continue;
                }
                if (!z) {
                    MALogger mALogger2 = LOGGER;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Can't use flashlight due to ");
                    sb2.append(iStateSource.getClass().getSimpleName());
                    mALogger2.mo11963w(sb2.toString());
                    return;
                }
            }
            DFSquadUtils.sendIntent(MotoResearchUtils.GESTURE_TYPE_CHOP_TWICE, MotoResearchUtils.INTENT_EXTRA_FLASHLIGHT_NEW_STATE, isFlashlightOn() ? MotoResearchUtils.INTENT_EXTRA_FLASHLIGHT_NEW_STATE_ON : MotoResearchUtils.INTENT_EXTRA_FLASHLIGHT_NEW_STATE_OFF);
            updateValidTime();
            sendMessage(this.mHandler, 1);
            DiscoveryManager.getInstance().cancelFDN(FeatureKey.FLASH_ON_CHOP);
            DiscoveryManager.getInstance().setDiscoveryStatus(FeatureKey.FLASH_ON_CHOP, 0);
            MotoSurveyUtils.sendIntent(MotoResearchUtils.GESTURE_TYPE_CHOP_TWICE);
            FlashOnChopInstrumentation.recordChopEvent();
            int i = (int) d;
            if (i > 0) {
                FlashOnChopInstrumentation.recordGyroThresholdTriggeredEvents(i);
            }
        }
    }

    private boolean isMinimumValidTime() {
        LOGGER.mo11957d("isMinimumValidTime - START");
        if (this.mLastToggleTimeMs == 0 || SystemClock.elapsedRealtime() - this.mLastToggleTimeMs >= DELAY_BETWEEN_CHOPS_TIMEOUT_MS) {
            LOGGER.mo11957d("isMinimumValidTime - END - Valid: true");
            return true;
        }
        LOGGER.mo11957d("isMinimumValidTime: Ignoring gesture. The last state was preserved, delay between chops is smaller than 750ms");
        return false;
    }

    private void updateValidTime() {
        LOGGER.mo11957d("mLastToggleTimeMs time updated");
        this.mLastToggleTimeMs = SystemClock.elapsedRealtime();
    }

    /* access modifiers changed from: private */
    public void toggleFlashlight() {
        LOGGER.mo11957d("toggleFlashlight - START");
        try {
            if (isFlashlightOn()) {
                sendMessage(this.mHandler, 2);
            } else if (this.mCameraId == null) {
                LOGGER.mo11959e("CameraId not initialized");
            } else if (setTorchMode(true)) {
                handleFlashlightOn();
                setFlashlightOn(true);
            }
        } catch (Exception e) {
            LOGGER.mo11959e(e.getMessage());
        }
        LOGGER.mo11957d("toggleFlashlight - END");
    }

    /* access modifiers changed from: private */
    public void turnoffFlashlightAndHandle() {
        try {
            if (turnOffFlashlight()) {
                handleFlashlightOff();
            }
        } catch (Exception e) {
            LOGGER.mo11959e(e.getMessage());
        }
    }

    private boolean turnOffFlashlight() {
        LOGGER.mo11957d("turnOffFlashlight - START");
        if (getEventSource(TorchEvent.class) == null || this.mCameraId == null || !setTorchMode(false)) {
            return false;
        }
        setFlashlightOn(false);
        return true;
    }

    private boolean feedbackPlaybackRequired() {
        return SystemClock.elapsedRealtime() - this.mLastToggleTimeMs <= TOGGLE_FEEDBACK_TIMEOUT_MS;
    }

    public void handleFlashlightOn() {
        LOGGER.mo11957d("handleFlashlightOn - START");
        if (!isFlashlightOn() && feedbackPlaybackRequired()) {
            FlashOnChopUtils.playFlashlightOnFeedback();
            this.mIEventSources.add(new TimeoutEvent(this));
        }
    }

    public void handleFlashlightOff() {
        FlashOnChopInstrumentation.recordFlashlightOffEvents();
        if (feedbackPlaybackRequired()) {
            FlashOnChopUtils.playFlashlightOffFeedback();
        }
        IEventSource eventSource = getEventSource(TimeoutEvent.class);
        if (eventSource != null) {
            eventSource.stop();
            this.mIEventSources.remove(eventSource);
        }
    }

    /* access modifiers changed from: private */
    public String getCameraId() {
        try {
            String[] cameraIdList = this.mCameraManager.getCameraIdList();
            int length = cameraIdList.length;
            int i = 0;
            while (i < length) {
                String str = cameraIdList[i];
                CameraCharacteristics cameraCharacteristics = this.mCameraManager.getCameraCharacteristics(str);
                Boolean bool = (Boolean) cameraCharacteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
                Integer num = (Integer) cameraCharacteristics.get(CameraCharacteristics.LENS_FACING);
                if (bool == null || !bool.booleanValue() || num == null || num.intValue() != 1) {
                    i++;
                } else {
                    MALogger mALogger = LOGGER;
                    StringBuilder sb = new StringBuilder();
                    sb.append("getCameraId(): found the back camera which is with flashlight: ");
                    sb.append(str);
                    mALogger.mo11957d(sb.toString());
                    return str;
                }
            }
        } catch (CameraAccessException e) {
            MALogger mALogger2 = LOGGER;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("can't retrieve camera Id due to CameraAccessException: ");
            sb2.append(e.toString());
            mALogger2.mo11959e(sb2.toString());
        } catch (IllegalArgumentException e2) {
            MALogger mALogger3 = LOGGER;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("cameraId does not match any known camera device, e=");
            sb3.append(e2.toString());
            mALogger3.mo11959e(sb3.toString());
        } catch (AssertionError e3) {
            MALogger mALogger4 = LOGGER;
            StringBuilder sb4 = new StringBuilder();
            sb4.append("Internal error of camera device, e=");
            sb4.append(e3.toString());
            mALogger4.mo11959e(sb4.toString());
        } catch (RuntimeException e4) {
            MALogger mALogger5 = LOGGER;
            StringBuilder sb5 = new StringBuilder();
            sb5.append("Unexpected error while retrieving camera id due to RuntimeException: ");
            sb5.append(e4.toString());
            mALogger5.mo11959e(sb5.toString());
        }
        return null;
    }

    @SuppressLint({"StaticFieldLeak"})
    private void initializeCameraDeviceStateListener() {
        new AsyncTask<Void, Void, Void>() {
            /* access modifiers changed from: protected */
            public Void doInBackground(Void... voidArr) {
                FlashOnChopGestureManager.this.mCameraId = FlashOnChopGestureManager.this.getCameraId();
                return null;
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(Void voidR) {
                super.onPostExecute(voidR);
                if (FlashOnChopGestureManager.this.mIStateSources != null && FlashOnChopGestureManager.this.mIEventSources != null && FlashOnChopGestureManager.this.mCameraId != null) {
                    FlashOnChopUtils.sendToForeground(false);
                    FlashOnChopGestureManager.this.mFlashlightController = new FlashlightController(FlashOnChopGestureManager.this.mCameraManager, FlashOnChopGestureManager.this.mHandler, FlashOnChopGestureManager.this.mCameraId, FlashOnChopGestureManager.this.mContext.getApplicationContext());
                    FlashOnChopGestureManager.LOGGER.mo11957d("initializeCameraDeviceStateListener,now registerAvailabilityCallback");
                    if (FlashOnChopGestureManager.this.getStateSource(CameraAvailabilityState.class) == null) {
                        FlashOnChopGestureManager.this.mIStateSources.add(new CameraAvailabilityState(FlashOnChopGestureManager.this.mCameraManager, FlashOnChopGestureManager.this.mCameraId));
                    }
                    if (FlashOnChopGestureManager.this.getStateSource(TorchState.class) == null) {
                        FlashOnChopGestureManager.this.mIStateSources.add(new TorchState(FlashOnChopGestureManager.this.mCameraManager, FlashOnChopGestureManager.this.mCameraId));
                    }
                    if (FlashOnChopGestureManager.this.getEventSource(TorchEvent.class) == null) {
                        FlashOnChopGestureManager.this.mIEventSources.add(new TorchEvent(FlashOnChopGestureManager.this.mCameraManager, FlashOnChopGestureManager.this.mCameraId, FlashOnChopGestureManager.this));
                    }
                }
            }
        }.execute(new Void[0]);
    }

    private void checkToTurnOnByPermissionGranted() {
        if (SharedPreferenceManager.getBoolean(FlashOnChopService.KEY_TURN_ON_BY_PERMISSIONS_GRANTED, false)) {
            LOGGER.mo11957d("checkSharedPreference: Need to turn on flashlight because the permission");
            sendMessage(this.mHandler, 2);
            sendDelayedMessage(this.mHandler, 1, TIME_DELAYED_MESSAGE_TURNOFF_TURNON_FLASHLIGHT);
            SharedPreferenceManager.putBoolean(FlashOnChopService.KEY_TURN_ON_BY_PERMISSIONS_GRANTED, false);
        }
    }

    public void onTimeout() {
        sendMessage(this.mHandler, 2);
        FlashOnChopInstrumentation.recordFlashlightTimedOutEvent();
    }

    public void onFlashLightOffNotFromFOC() {
        handleFlashlightOff();
    }

    private void recordFlashlightEnabledApiUsage(FlashlightAccess flashlightAccess) {
        if (flashlightAccess instanceof FlashlightAccessFlashlightController) {
            LOGGER.mo11957d("recordFlashlightEnabledApiUsage: FlashlightController");
            FlashOnChopInstrumentation.recordFlashlightControllerEvent();
        } else if (flashlightAccess instanceof FlashlightAccessCameraManager) {
            LOGGER.mo11957d("recordFlashlightEnabledApiUsage: CameraManager");
            FlashOnChopInstrumentation.recordCameraManagerEvent();
        } else {
            LOGGER.mo11957d("Invalid FlashlightAccess");
        }
    }
}
