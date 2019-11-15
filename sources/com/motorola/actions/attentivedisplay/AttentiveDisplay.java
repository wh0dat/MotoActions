package com.motorola.actions.attentivedisplay;

import android.app.KeyguardManager;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraManager.AvailabilityCallback;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.PowerManager;
import android.os.SystemClock;
import com.motorola.actions.attentivedisplay.MovementDetectManager.MovementChangeListener;
import com.motorola.actions.attentivedisplay.ObjectDetectManager.ObjectDetectListener;
import com.motorola.actions.attentivedisplay.ScreenStatusReceiver.ScreenStatusListener;
import com.motorola.actions.attentivedisplay.StowedDetectManager.StowedChangeListener;
import com.motorola.actions.attentivedisplay.face.FaceDetector;
import com.motorola.actions.attentivedisplay.face.FaceDetector.FaceDetectionListener;
import com.motorola.actions.attentivedisplay.face.FaceEngineFactory;
import com.motorola.actions.attentivedisplay.facedetection.SavePhotoMode;
import com.motorola.actions.attentivedisplay.instrumentation.AttentiveDisplayInstrumentation;
import com.motorola.actions.attentivedisplay.util.ScreenTimeoutControl;
import com.motorola.actions.p013ui.settings.AttentiveDisplaySettingsFragment;
import com.motorola.actions.reflect.PowerManagerPrivateProxy;
import com.motorola.actions.utils.Constants;
import com.motorola.actions.utils.DFSquadUtils;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.MotoResearchUtils;
import com.motorola.actions.utils.MotoSurveyUtils;

public class AttentiveDisplay implements ScreenStatusListener, FaceDetectionListener, ObjectDetectListener, StowedChangeListener, MovementChangeListener {
    public static final int CAMERA_NOT_STARTED = -1;
    private static final long DELAY_STOP_SERVICE = 50;
    private static final int EXECUTION_TIMEOUT_MILLIS = 9000;
    private static final int INACTIVITY_TIMEOUT_MIN_MILLIS = 600000;
    private static final float INACTIVITY_TIMEOUT_PERCENTAGE = 1.5f;
    /* access modifiers changed from: private */
    public static final MALogger LOGGER = new MALogger(AttentiveDisplay.class);
    public static final int OBJECT_NOT_DETECTED = -1;
    /* access modifiers changed from: private */
    public AttentiveDisplayInstrumentation mAttentiveDisplayInstrumentation;
    private final AvailabilityCallback mCameraAvailabilityCallback = new AvailabilityCallback() {
        public void onCameraAvailable(String str) {
            MALogger access$000 = AttentiveDisplay.LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("onCameraAvailable ");
            sb.append(str);
            access$000.mo11957d(sb.toString());
            AttentiveDisplay.this.mCameraManager.unregisterAvailabilityCallback(this);
            AttentiveDisplay.this.mNumberOfCamerasAvailable = AttentiveDisplay.this.mNumberOfCamerasAvailable + 1;
            if (AttentiveDisplay.this.mNumberOfCamerasAvailable >= AttentiveDisplay.this.mNumberOfCameras) {
                AttentiveDisplay.LOGGER.mo11957d("All cameras available, don't dim screen");
                AttentiveDisplay.this.executePreDimWork();
            }
        }

        public void onCameraUnavailable(String str) {
            MALogger access$000 = AttentiveDisplay.LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("onCameraUnavailable ");
            sb.append(str);
            access$000.mo11957d(sb.toString());
            AttentiveDisplay.this.mCameraManager.unregisterAvailabilityCallback(this);
            AttentiveDisplay.this.goToIdle();
        }
    };
    /* access modifiers changed from: private */
    public CameraManager mCameraManager;
    private long mCameraStartTime;
    private final Context mContext;
    private int mFaceCount;
    private FaceDetector mFaceDetector;
    private final Handler mHandler;
    private final KeyguardManager mKeyguardManager;
    private final MovementDetectManager mMovementDetectManager;
    private boolean mMovementStarted;
    /* access modifiers changed from: private */
    public int mNumberOfCameras;
    /* access modifiers changed from: private */
    public int mNumberOfCamerasAvailable;
    private long mObjectDetectionStartTime;
    private int mObjectDetectionTime;
    private final ObjectDetectManager mObjectManager;
    private boolean mObjectStarted;
    private Boolean mObjectState;
    private final PowerManager mPowerManager;
    private final ScreenStatusReceiver mReceiver;
    private final ScreenControlOverlay mScreenControl;
    private long mStartTime;
    private State mState;
    private boolean mStowedStarted;
    private Boolean mStowedState;
    private final Runnable mTimeoutRunnable = new Runnable() {
        public void run() {
            if (AttentiveDisplay.this.isADNotIdle()) {
                AttentiveDisplay.LOGGER.mo11957d("Execution Timeout");
                AttentiveDisplay.this.mAttentiveDisplayInstrumentation.onSessionAbortedCommon(AttentiveDisplay.this.getObjectDetectionTime(), AttentiveDisplay.this.getCameraOnTime());
                AttentiveDisplay.this.mAttentiveDisplayInstrumentation.recordDailySessionsAbortedTimoutEvent();
                AttentiveDisplay.this.goToIdle();
            }
        }
    };
    private final AttentiveDisplayTracker mTracker;

    private static final class SavePhotoTask extends AsyncTask<Void, Void, Void> {
        private boolean mDetected;
        private byte[] mImage;

        private SavePhotoTask(byte[] bArr, boolean z) {
            this.mImage = bArr;
            this.mDetected = z;
        }

        /* access modifiers changed from: protected */
        public Void doInBackground(Void... voidArr) {
            AttentiveDisplay.saveDebugPhoto(this.mDetected, this.mImage);
            return null;
        }
    }

    private enum State {
        IDLE,
        REGISTERING_STOWED,
        WAIT_FOR_STOWED_OBJECT,
        WAIT_FOR_MOVEMENT,
        WAIT_FOR_FRAME,
        SUSPENDED
    }

    public AttentiveDisplay(Context context) {
        LOGGER.mo11957d("constructor");
        this.mContext = context;
        this.mHandler = new Handler();
        this.mKeyguardManager = (KeyguardManager) this.mContext.getSystemService("keyguard");
        this.mPowerManager = (PowerManager) this.mContext.getSystemService("power");
        this.mReceiver = new ScreenStatusReceiver(this.mContext, this);
        this.mObjectManager = new ObjectDetectManager(this.mContext, this);
        this.mMovementDetectManager = new MovementDetectManager(this.mContext, this);
        this.mScreenControl = new ScreenControlOverlay(this.mContext);
        this.mTracker = AttentiveDisplayTracker.getInstance();
        this.mAttentiveDisplayInstrumentation = new AttentiveDisplayInstrumentation();
        this.mState = State.IDLE;
    }

    public void destroy() {
        LOGGER.mo11957d("destroy()");
        this.mState = State.IDLE;
        this.mScreenControl.destroy();
        this.mObjectManager.stop();
        StowedDetectManager.getInstance().stop();
        this.mMovementDetectManager.stop();
        this.mReceiver.unregister();
        if (this.mFaceDetector != null) {
            this.mFaceDetector.stopDetection();
            FaceEngineFactory.releaseInstance();
            this.mFaceDetector = null;
        }
    }

    public void onObjectDetection(boolean z) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("onObjectDetection(detected=");
        sb.append(z);
        sb.append(")");
        mALogger.mo11957d(sb.toString());
        checkScreenOff();
        if (this.mState == State.WAIT_FOR_STOWED_OBJECT) {
            this.mObjectState = Boolean.valueOf(z);
            if (!z) {
                return;
            }
            if (!this.mStowedStarted || (this.mStowedState != null && !this.mStowedState.booleanValue())) {
                this.mObjectManager.stop();
                this.mObjectDetectionTime = getObjectDetectionTime();
                StowedDetectManager.getInstance().stop();
                this.mMovementDetectManager.stop();
                goToWaitForFrame();
            }
        }
    }

    public void onStowedChange(boolean z) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("onStowedChange(stowed=");
        sb.append(z);
        sb.append("), mState = ");
        sb.append(this.mState);
        mALogger.mo11957d(sb.toString());
        checkScreenOff();
        if (this.mState == State.REGISTERING_STOWED) {
            this.mState = State.WAIT_FOR_STOWED_OBJECT;
        }
        if (this.mState == State.WAIT_FOR_STOWED_OBJECT) {
            this.mStowedState = Boolean.valueOf(z);
            if (z) {
                return;
            }
            if (this.mObjectStarted && (this.mObjectState == null || !this.mObjectState.booleanValue())) {
                return;
            }
            if (!this.mObjectStarted) {
                goToWaitForMovement();
                return;
            }
            this.mObjectManager.stop();
            this.mObjectDetectionTime = getObjectDetectionTime();
            StowedDetectManager.getInstance().stop();
            this.mMovementDetectManager.stop();
            goToWaitForFrame();
        }
    }

    public void onMovementChange(boolean z) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("onMovementChange() - isMoving: ");
        sb.append(z);
        mALogger.mo11957d(sb.toString());
        if (this.mState == State.WAIT_FOR_MOVEMENT && z) {
            this.mMovementDetectManager.stop();
            this.mObjectManager.stop();
            StowedDetectManager.getInstance().stop();
            goToWaitForFrame();
        }
    }

    public void onFaceDetection(boolean z, byte[] bArr) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("onFaceDetection(detected=");
        sb.append(z);
        sb.append(")");
        mALogger.mo11957d(sb.toString());
        checkScreenOff();
        if (this.mState == State.WAIT_FOR_FRAME) {
            this.mFaceCount++;
            this.mAttentiveDisplayInstrumentation.onAttentiveDisplayResult(true);
            if (shouldSavePhoto(bArr, z)) {
                new SavePhotoTask(bArr, z).execute(new Void[0]);
            }
            if (z) {
                MALogger mALogger2 = LOGGER;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Found Face after ");
                sb2.append(this.mFaceCount);
                sb2.append(" attempts");
                mALogger2.mo11957d(sb2.toString());
                this.mScreenControl.userActivity();
                triggerFeedbackToMotorolaResearch();
                this.mAttentiveDisplayInstrumentation.onSessionExtended(getObjectDetectionTime(), getCameraOnTime());
                goToIdle();
            }
        }
    }

    public void onDetectionError() {
        LOGGER.mo11957d("onDetectionError()");
        if (this.mState == State.WAIT_FOR_FRAME) {
            this.mAttentiveDisplayInstrumentation.onSessionAbortedCommon(getObjectDetectionTime(), getCameraOnTime());
            this.mAttentiveDisplayInstrumentation.recordDailySessionsAbortedErrorEvent();
            goToIdle();
        }
    }

    private void idleDebug() {
        if (Constants.DEBUG) {
            long uptimeMillis = SystemClock.uptimeMillis() - this.mStartTime;
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("Idle after ");
            sb.append(uptimeMillis);
            sb.append("ms, ");
            sb.append(this.mFaceCount);
            sb.append(" detect results");
            mALogger.mo11957d(sb.toString());
        }
    }

    /* access modifiers changed from: private */
    public void goToIdle() {
        LOGGER.mo11957d("goToIdle");
        if (isADNotIdle()) {
            idleDebug();
        }
        stopDetection();
        this.mHandler.removeCallbacks(this.mTimeoutRunnable);
        this.mState = State.IDLE;
        this.mHandler.postDelayed(new AttentiveDisplay$$Lambda$0(this), DELAY_STOP_SERVICE);
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$goToIdle$0$AttentiveDisplay() {
        AttentiveDisplayService.stop(this.mContext);
    }

    private void goToWaitForStowedObject() {
        LOGGER.mo11957d("goToWaitForStowedObject");
        this.mState = State.REGISTERING_STOWED;
        this.mStowedStarted = StowedDetectManager.getInstance().start(this);
        this.mObjectStarted = this.mObjectManager.start();
        if (this.mObjectStarted || this.mStowedStarted) {
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("stowed ");
            sb.append(this.mStowedStarted ? "" : "not ");
            sb.append("started, ");
            sb.append("object ");
            sb.append(this.mObjectStarted ? "" : "not ");
            sb.append("started");
            mALogger.mo11957d(sb.toString());
            this.mObjectDetectionStartTime = SystemClock.uptimeMillis();
            return;
        }
        LOGGER.mo11957d("going right to camera");
        this.mObjectDetectionTime = 0;
        goToWaitForFrame();
    }

    private void goToWaitForMovement() {
        LOGGER.mo11957d("goToWaitForMovement");
        if (!this.mMovementStarted) {
            this.mMovementStarted = this.mMovementDetectManager.start(false);
        }
        if (this.mMovementStarted) {
            this.mState = State.WAIT_FOR_MOVEMENT;
            return;
        }
        LOGGER.mo11957d("going right to camera");
        goToWaitForFrame();
    }

    private void goToWaitForFrame() {
        LOGGER.mo11957d("goToWaitForFrame");
        this.mFaceDetector = FaceEngineFactory.acquireInstance();
        if (this.mFaceDetector != null) {
            this.mState = State.WAIT_FOR_FRAME;
            this.mFaceDetector.startDetection(this);
            this.mCameraStartTime = SystemClock.uptimeMillis();
            return;
        }
        LOGGER.mo11963w("Unable to start face detection");
    }

    private void checkScreenOff() {
        if (this.mState != State.IDLE && !this.mPowerManager.isScreenOn()) {
            LOGGER.mo11957d("isScreenOn() is false");
            stopDetection();
            this.mState = State.SUSPENDED;
        }
    }

    private void stopDetection() {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("stopDetection - mState: ");
        sb.append(this.mState);
        mALogger.mo11957d(sb.toString());
        switch (this.mState) {
            case WAIT_FOR_STOWED_OBJECT:
            case WAIT_FOR_MOVEMENT:
                this.mObjectManager.stop();
                StowedDetectManager.getInstance().stop();
                this.mMovementDetectManager.stop();
                return;
            case WAIT_FOR_FRAME:
                if (this.mFaceDetector != null) {
                    this.mFaceDetector.stopDetection();
                    FaceEngineFactory.releaseInstance();
                    this.mFaceDetector = null;
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void onScreenOn() {
        LOGGER.mo11957d("onScreenOn()");
        this.mTracker.resetInactivityTimeout();
        this.mAttentiveDisplayInstrumentation.onScreenOn();
        goToIdle();
    }

    public void onScreenOff(int i) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("onScreenOff(reason: ");
        sb.append(i);
        sb.append(")");
        mALogger.mo11957d(sb.toString());
        this.mTracker.resetInactivityTimeout();
        this.mAttentiveDisplayInstrumentation.onScreenOff(i);
        if (isADNotIdle()) {
            if (i == PowerManagerPrivateProxy.GO_TO_SLEEP_REASON_TIMEOUT) {
                this.mAttentiveDisplayInstrumentation.onSessionNotExtended(getObjectDetectionTime(), getCameraOnTime());
            } else {
                this.mAttentiveDisplayInstrumentation.onSessionAbortedCommon(getObjectDetectionTime(), getCameraOnTime());
                this.mAttentiveDisplayInstrumentation.recordDailySessionsAbortedUserActivityEvent();
            }
        }
        goToIdle();
    }

    public void onScreenPreDim() {
        LOGGER.mo11957d("onScreenPreDim()");
        if (this.mCameraManager == null) {
            this.mCameraManager = (CameraManager) this.mContext.getSystemService("camera");
            try {
                this.mNumberOfCameras = this.mCameraManager.getCameraIdList().length;
            } catch (CameraAccessException e) {
                MALogger mALogger = LOGGER;
                StringBuilder sb = new StringBuilder();
                sb.append("Error getting number of cameras: ");
                sb.append(e.toString());
                mALogger.mo11959e(sb.toString());
                this.mNumberOfCameras = Camera.getNumberOfCameras();
            }
        }
        if (this.mNumberOfCameras > 0) {
            this.mCameraManager.registerAvailabilityCallback(this.mCameraAvailabilityCallback, this.mHandler);
            this.mNumberOfCamerasAvailable = 0;
        }
    }

    /* access modifiers changed from: private */
    public void executePreDimWork() {
        LOGGER.mo11957d("executePreDimWork");
        this.mAttentiveDisplayInstrumentation.onScreenPreDim();
        if (this.mState == State.IDLE && !this.mKeyguardManager.inKeyguardRestrictedInputMode()) {
            this.mStartTime = SystemClock.uptimeMillis();
            int reducedScreenTimeout = ScreenTimeoutControl.getReducedScreenTimeout(this.mContext);
            if (reducedScreenTimeout != -1) {
                if (this.mStartTime - this.mTracker.getInactivityLastTime() > ((long) reducedScreenTimeout)) {
                    this.mTracker.resetInactivityTimeout();
                } else {
                    if (this.mStartTime - this.mTracker.getInactivityStartTime() > ((long) Math.max(INACTIVITY_TIMEOUT_MIN_MILLIS, (int) (((float) reducedScreenTimeout) * INACTIVITY_TIMEOUT_PERCENTAGE)))) {
                        LOGGER.mo11957d("Skipping execution due to inactivity timeout");
                        this.mTracker.resetInactivityTimeout();
                        return;
                    }
                    this.mTracker.setInactivityLastTime(this.mStartTime);
                }
            }
            this.mHandler.postDelayed(this.mTimeoutRunnable, 9000);
            this.mObjectState = null;
            this.mStowedState = null;
            this.mObjectStarted = false;
            this.mStowedStarted = false;
            this.mMovementStarted = false;
            this.mFaceCount = 0;
            goToWaitForStowedObject();
        } else if (this.mKeyguardManager.inKeyguardRestrictedInputMode()) {
            goToIdle();
        }
    }

    public void onScreenDim() {
        LOGGER.mo11957d("onScreenDim()");
        this.mAttentiveDisplayInstrumentation.onScreenDim();
        if (AttentiveDisplaySettingsFragment.isGoToSleepEnabled()) {
            new PowerManagerPrivateProxy(this.mContext).setQuickDim(ADConstants.QUICK_DIM_TIME_MILLIS);
        }
    }

    public void onScreenBright() {
        LOGGER.mo11957d("onScreenBright()");
        boolean isADNotIdle = isADNotIdle();
        this.mAttentiveDisplayInstrumentation.onScreenBright(isADNotIdle);
        if (isADNotIdle) {
            this.mTracker.resetInactivityTimeout();
            this.mAttentiveDisplayInstrumentation.onSessionAbortedCommon(getObjectDetectionTime(), getCameraOnTime());
            this.mAttentiveDisplayInstrumentation.recordDailySessionsAbortedUserActivityEvent();
            goToIdle();
        }
    }

    /* access modifiers changed from: private */
    public int getObjectDetectionTime() {
        int i = C05313.f37x7404e558[this.mState.ordinal()];
        if (i == 1) {
            return (int) (SystemClock.uptimeMillis() - this.mObjectDetectionStartTime);
        }
        if (i != 3) {
            return -1;
        }
        return this.mObjectDetectionTime;
    }

    /* access modifiers changed from: private */
    public int getCameraOnTime() {
        if (this.mState == State.WAIT_FOR_FRAME) {
            return (int) (SystemClock.uptimeMillis() - this.mCameraStartTime);
        }
        return -1;
    }

    /* access modifiers changed from: private */
    public boolean isADNotIdle() {
        return this.mState != State.IDLE;
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x008a A[SYNTHETIC, Splitter:B:19:0x008a] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x00b2 A[SYNTHETIC, Splitter:B:28:0x00b2] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x00d1 A[SYNTHETIC, Splitter:B:35:0x00d1] */
    /* JADX WARNING: Removed duplicated region for block: B:42:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:43:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:16:0x0072=Splitter:B:16:0x0072, B:25:0x009a=Splitter:B:25:0x009a} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void saveDebugPhoto(boolean r4, byte[] r5) {
        /*
            java.text.SimpleDateFormat r0 = new java.text.SimpleDateFormat
            java.lang.String r1 = "yyyyMMdd-HHmmss"
            java.util.Locale r2 = java.util.Locale.getDefault()
            r0.<init>(r1, r2)
            java.util.Date r1 = new java.util.Date
            long r2 = java.lang.System.currentTimeMillis()
            r1.<init>(r2)
            java.lang.String r0 = r0.format(r1)
            java.lang.String r1 = "ad_debug_photo_%b_%s.jpg"
            r2 = 2
            java.lang.Object[] r2 = new java.lang.Object[r2]
            java.lang.Boolean r4 = java.lang.Boolean.valueOf(r4)
            r3 = 0
            r2[r3] = r4
            r4 = 1
            r2[r4] = r0
            java.lang.String r4 = java.lang.String.format(r1, r2)
            java.io.File r0 = new java.io.File
            java.io.File r1 = com.motorola.actions.attentivedisplay.AttentiveDisplayDebugHelper.getPhotosDirectory()
            r0.<init>(r1, r4)
            r4 = 0
            java.io.FileOutputStream r1 = new java.io.FileOutputStream     // Catch:{ FileNotFoundException -> 0x0097, IOException -> 0x006f, all -> 0x006b }
            r1.<init>(r0)     // Catch:{ FileNotFoundException -> 0x0097, IOException -> 0x006f, all -> 0x006b }
            r1.write(r5)     // Catch:{ FileNotFoundException -> 0x0069, IOException -> 0x0067 }
            com.motorola.actions.utils.MALogger r4 = LOGGER     // Catch:{ FileNotFoundException -> 0x0069, IOException -> 0x0067 }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ FileNotFoundException -> 0x0069, IOException -> 0x0067 }
            r5.<init>()     // Catch:{ FileNotFoundException -> 0x0069, IOException -> 0x0067 }
            java.lang.String r2 = "ADDBG - Photo saved with success: "
            r5.append(r2)     // Catch:{ FileNotFoundException -> 0x0069, IOException -> 0x0067 }
            java.lang.String r0 = r0.getPath()     // Catch:{ FileNotFoundException -> 0x0069, IOException -> 0x0067 }
            r5.append(r0)     // Catch:{ FileNotFoundException -> 0x0069, IOException -> 0x0067 }
            java.lang.String r5 = r5.toString()     // Catch:{ FileNotFoundException -> 0x0069, IOException -> 0x0067 }
            r4.mo11957d(r5)     // Catch:{ FileNotFoundException -> 0x0069, IOException -> 0x0067 }
            if (r1 == 0) goto L_0x00cd
            r1.close()     // Catch:{ IOException -> 0x005e }
            goto L_0x00cd
        L_0x005e:
            r4 = move-exception
            com.motorola.actions.utils.MALogger r5 = LOGGER
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            goto L_0x00be
        L_0x0067:
            r4 = move-exception
            goto L_0x0072
        L_0x0069:
            r4 = move-exception
            goto L_0x009a
        L_0x006b:
            r5 = move-exception
            r1 = r4
            r4 = r5
            goto L_0x00cf
        L_0x006f:
            r5 = move-exception
            r1 = r4
            r4 = r5
        L_0x0072:
            com.motorola.actions.utils.MALogger r5 = LOGGER     // Catch:{ all -> 0x00ce }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x00ce }
            r0.<init>()     // Catch:{ all -> 0x00ce }
            java.lang.String r2 = "ADDBG - Error saving photo:"
            r0.append(r2)     // Catch:{ all -> 0x00ce }
            r0.append(r4)     // Catch:{ all -> 0x00ce }
            java.lang.String r4 = r0.toString()     // Catch:{ all -> 0x00ce }
            r5.mo11959e(r4)     // Catch:{ all -> 0x00ce }
            if (r1 == 0) goto L_0x00cd
            r1.close()     // Catch:{ IOException -> 0x008e }
            goto L_0x00cd
        L_0x008e:
            r4 = move-exception
            com.motorola.actions.utils.MALogger r5 = LOGGER
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            goto L_0x00be
        L_0x0097:
            r5 = move-exception
            r1 = r4
            r4 = r5
        L_0x009a:
            com.motorola.actions.utils.MALogger r5 = LOGGER     // Catch:{ all -> 0x00ce }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x00ce }
            r0.<init>()     // Catch:{ all -> 0x00ce }
            java.lang.String r2 = "ADDBG - Error saving photo:"
            r0.append(r2)     // Catch:{ all -> 0x00ce }
            r0.append(r4)     // Catch:{ all -> 0x00ce }
            java.lang.String r4 = r0.toString()     // Catch:{ all -> 0x00ce }
            r5.mo11959e(r4)     // Catch:{ all -> 0x00ce }
            if (r1 == 0) goto L_0x00cd
            r1.close()     // Catch:{ IOException -> 0x00b6 }
            goto L_0x00cd
        L_0x00b6:
            r4 = move-exception
            com.motorola.actions.utils.MALogger r5 = LOGGER
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
        L_0x00be:
            java.lang.String r1 = "ADDBG - Error closing FileOutputStream:"
            r0.append(r1)
            r0.append(r4)
            java.lang.String r4 = r0.toString()
            r5.mo11959e(r4)
        L_0x00cd:
            return
        L_0x00ce:
            r4 = move-exception
        L_0x00cf:
            if (r1 == 0) goto L_0x00ec
            r1.close()     // Catch:{ IOException -> 0x00d5 }
            goto L_0x00ec
        L_0x00d5:
            r5 = move-exception
            com.motorola.actions.utils.MALogger r0 = LOGGER
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "ADDBG - Error closing FileOutputStream:"
            r1.append(r2)
            r1.append(r5)
            java.lang.String r5 = r1.toString()
            r0.mo11959e(r5)
        L_0x00ec:
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.motorola.actions.attentivedisplay.AttentiveDisplay.saveDebugPhoto(boolean, byte[]):void");
    }

    private boolean shouldSavePhoto(byte[] bArr, boolean z) {
        String savePhotoMode = AttentiveDisplayDebugHelper.getSavePhotoMode();
        boolean z2 = true;
        if (!(savePhotoMode.equals(SavePhotoMode.ALL.toString()) || (z && savePhotoMode.equals(SavePhotoMode.SUCCESS.toString())) || (!z && savePhotoMode.equals(SavePhotoMode.FAILURE.toString()))) || bArr == null || Constants.PRODUCTION_MODE) {
            z2 = false;
        }
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("ADDBG - Should save picture: ");
        sb.append(z2);
        sb.append(", ");
        sb.append(savePhotoMode);
        mALogger.mo11957d(sb.toString());
        return z2;
    }

    private void triggerFeedbackToMotorolaResearch() {
        MotoSurveyUtils.sendIntent(MotoResearchUtils.GESTURE_TYPE_ATTENTIVE_DISPLAY);
        DFSquadUtils.sendIntent(MotoResearchUtils.GESTURE_TYPE_ATTENTIVE_DISPLAY);
    }
}
