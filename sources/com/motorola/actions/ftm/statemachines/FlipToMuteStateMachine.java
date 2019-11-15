package com.motorola.actions.ftm.statemachines;

import android.content.Context;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import com.motorola.actions.approach.p005ir.SensorHub;
import com.motorola.actions.eventsystem.EventSystem;
import com.motorola.actions.eventsystem.sensor.SensorConstants;
import com.motorola.actions.eventsystem.sensor.SensorNotFoundException;
import com.motorola.actions.ftm.FlipToMuteService;
import com.motorola.actions.reflect.ModalityManagerProxy;
import com.motorola.actions.reflect.ModalityManagerProxy.ModalityListenerProxy;
import com.motorola.actions.reflect.SensorPrivateProxy;
import com.motorola.actions.reflect.TransitionProxy;
import com.motorola.actions.utils.EventOnlySensorListener;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.zenmode.AutomaticRulesConfigObserver;
import p016io.reactivex.disposables.Disposable;

public class FlipToMuteStateMachine extends FlipToMuteBaseMachine {
    private static final double FLAT_DOWN_EVENT_DETECTED = 1.0d;
    /* access modifiers changed from: private */
    public static final MALogger LOGGER = new MALogger(FlipToMuteStateMachine.class);
    private static final int MACRO_MOTION_CHECK_DELAY = 3000;
    private static final int MODALITY_END_DURATION = 1000;
    private static final int MODALITY_START_DURATION = 0;
    /* access modifiers changed from: private */
    public static FlipToMuteStateEnum sCurrentState = FlipToMuteStateEnum.STARTED;
    /* access modifiers changed from: private */
    public Runnable mCheckMotionHistoryRunnable = new Runnable() {
        public void run() {
            SensorHub.getMotionHistory(new FlipToMuteStateMachine$2$$Lambda$0(this));
        }

        /* access modifiers changed from: 0000 */
        public final /* synthetic */ void lambda$run$0$FlipToMuteStateMachine$2(int i) {
            MALogger access$200 = FlipToMuteStateMachine.LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("getMotionHistory: ");
            sb.append(i);
            access$200.mo11957d(sb.toString());
            if ((i & 7) == 7) {
                FlipToMuteStateMachine.LOGGER.mo11957d("Relevant motion detected.");
                FlipToMuteStateMachine.this.returnToState(FlipToMuteStateEnum.FLAT);
                FlipToMuteStateMachine.this.mHandler.removeCallbacks(FlipToMuteStateMachine.this.mCheckMotionHistoryRunnable);
            }
        }
    };
    private final SensorEventListener mFlatListener = new EventOnlySensorListener() {
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (((double) sensorEvent.values[0]) == FlipToMuteStateMachine.FLAT_DOWN_EVENT_DETECTED) {
                if (FlipToMuteStateMachine.sCurrentState == FlipToMuteStateEnum.STOWED) {
                    FlipToMuteStateMachine.this.changeState(FlipToMuteStateEnum.FLAT);
                    FlipToMuteStateMachine.this.mModalityManager.addModalityListener(FlipToMuteStateMachine.this.mModalityListener, 2, 0, 1000);
                    return;
                }
                MALogger access$200 = FlipToMuteStateMachine.LOGGER;
                StringBuilder sb = new StringBuilder();
                sb.append("Flat Down state unexpected behavior. State=");
                sb.append(FlipToMuteStateMachine.sCurrentState.toString());
                access$200.mo11959e(sb.toString());
                FlipToMuteStateMachine.this.returnToInitialState();
            } else if (FlipToMuteStateEnum.FLAT.ordinal() <= FlipToMuteStateMachine.sCurrentState.ordinal()) {
                FlipToMuteStateMachine.this.returnToState(FlipToMuteStateEnum.STOWED);
            }
        }
    };
    protected Handler mHandler = new Handler();
    /* access modifiers changed from: private */
    public ModalityListenerProxy mModalityListener = new ModalityListenerProxy() {
        public void onModalityChange(TransitionProxy transitionProxy) {
            if (transitionProxy.getNewState() == 2) {
                if (FlipToMuteStateMachine.sCurrentState != FlipToMuteStateEnum.FLAT) {
                    MALogger access$200 = FlipToMuteStateMachine.LOGGER;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Motionless received while state is: ");
                    sb.append(FlipToMuteStateMachine.sCurrentState);
                    sb.append(". Ignoring.");
                    access$200.mo11963w(sb.toString());
                } else if (FlipToMuteStateMachine.this.shouldEnterDNDMode()) {
                    FlipToMuteStateMachine.this.changeState(FlipToMuteStateEnum.DND_ENABLED);
                    FlipToMuteStateMachine.this.enterDND();
                }
            } else if (FlipToMuteStateMachine.sCurrentState == FlipToMuteStateEnum.DND_ENABLED) {
                FlipToMuteStateMachine.this.mHandler.postDelayed(FlipToMuteStateMachine.this.mCheckMotionHistoryRunnable, 3000);
            }
        }
    };
    /* access modifiers changed from: private */
    public ModalityManagerProxy mModalityManager;
    private SensorManager mSensorManager;
    private Disposable mStowedDisposable;

    private enum FlipToMuteStateEnum {
        STARTED,
        INITIAL,
        STOWED,
        FLAT,
        DND_ENABLED
    }

    public FlipToMuteStateMachine(Context context, AutomaticRulesConfigObserver automaticRulesConfigObserver) {
        super(context, automaticRulesConfigObserver);
    }

    /* access modifiers changed from: private */
    /* renamed from: stowedEvent */
    public void bridge$lambda$0$FlipToMuteStateMachine(Float f) {
        if (!SensorConstants.STOWED_ENABLED.equals(f)) {
            LOGGER.mo11957d("mStowedListener: return to initial state");
            returnToInitialState();
        } else if (sCurrentState == FlipToMuteStateEnum.INITIAL) {
            changeState(FlipToMuteStateEnum.STOWED);
            this.mSensorManager.registerListener(this.mFlatListener, this.mSensorManager.getDefaultSensor(SensorPrivateProxy.TYPE_FLAT_DOWN), 3);
        } else {
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("Stowed state unexpected behavior. State=");
            sb.append(sCurrentState.toString());
            mALogger.mo11959e(sb.toString());
            returnToInitialState();
        }
    }

    /* access modifiers changed from: private */
    public void returnToInitialState() {
        LOGGER.mo11957d("returnToInitialState");
        if (sCurrentState != FlipToMuteStateEnum.INITIAL) {
            changeState(FlipToMuteStateEnum.INITIAL);
            unregisterAllSensorListeners();
            try {
                this.mStowedDisposable = EventSystem.getInstance().subscribeSensorEvent(Integer.valueOf(SensorPrivateProxy.TYPE_STOWED), new FlipToMuteStateMachine$$Lambda$0(this));
            } catch (SensorNotFoundException unused) {
                LOGGER.mo11959e("Stowed sensor not found");
            }
        }
        changeDoNotDisturbToOriginalState();
    }

    /* access modifiers changed from: private */
    public void returnToState(FlipToMuteStateEnum flipToMuteStateEnum) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("Returning from state ");
        sb.append(sCurrentState.toString());
        sb.append(" to ");
        sb.append(flipToMuteStateEnum.toString());
        mALogger.mo11957d(sb.toString());
        if (flipToMuteStateEnum.ordinal() >= sCurrentState.ordinal()) {
            MALogger mALogger2 = LOGGER;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Internal state error, currentState=");
            sb2.append(sCurrentState.toString());
            sb2.append(" destState=");
            sb2.append(flipToMuteStateEnum.toString());
            mALogger2.mo11959e(sb2.toString());
        } else if (FlipToMuteStateEnum.INITIAL == flipToMuteStateEnum) {
            returnToInitialState();
        } else {
            switch (sCurrentState) {
                case DND_ENABLED:
                    if (FlipToMuteStateEnum.STOWED == flipToMuteStateEnum) {
                        this.mModalityManager.removeModalityListener();
                    }
                    changeDoNotDisturbToOriginalState();
                    break;
                case FLAT:
                    if (FlipToMuteStateEnum.STOWED == flipToMuteStateEnum) {
                        this.mModalityManager.removeModalityListener();
                        break;
                    }
                    break;
                default:
                    MALogger mALogger3 = LOGGER;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("Invalid currentState. sCurrentState = ");
                    sb3.append(sCurrentState);
                    mALogger3.mo11963w(sb3.toString());
                    break;
            }
            changeState(flipToMuteStateEnum);
            if (flipToMuteStateEnum.ordinal() <= FlipToMuteStateEnum.FLAT.ordinal()) {
                this.mHandler.removeCallbacks(this.mCheckMotionHistoryRunnable);
            }
        }
    }

    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:10:0x002e */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void unregisterAllSensorListeners() {
        /*
            r4 = this;
            com.motorola.actions.utils.MALogger r0 = LOGGER
            java.lang.String r1 = "unregisterAllSensorListeners"
            r0.mo11957d(r1)
            android.hardware.SensorManager r0 = r4.mSensorManager
            if (r0 == 0) goto L_0x0012
            android.hardware.SensorManager r0 = r4.mSensorManager
            android.hardware.SensorEventListener r1 = r4.mFlatListener
            r0.unregisterListener(r1)
        L_0x0012:
            com.motorola.actions.reflect.ModalityManagerProxy r0 = r4.mModalityManager
            if (r0 == 0) goto L_0x001b
            com.motorola.actions.reflect.ModalityManagerProxy r0 = r4.mModalityManager
            r0.removeModalityListener()
        L_0x001b:
            r0 = 0
            com.motorola.actions.eventsystem.EventSystem r1 = com.motorola.actions.eventsystem.EventSystem.getInstance()     // Catch:{ IllegalArgumentException -> 0x002e }
            int r2 = com.motorola.actions.reflect.SensorPrivateProxy.TYPE_STOWED     // Catch:{ IllegalArgumentException -> 0x002e }
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)     // Catch:{ IllegalArgumentException -> 0x002e }
            io.reactivex.disposables.Disposable r3 = r4.mStowedDisposable     // Catch:{ IllegalArgumentException -> 0x002e }
            r1.unsubscribeSensorEvent(r2, r3)     // Catch:{ IllegalArgumentException -> 0x002e }
            goto L_0x0035
        L_0x002c:
            r1 = move-exception
            goto L_0x0038
        L_0x002e:
            com.motorola.actions.utils.MALogger r1 = LOGGER     // Catch:{ all -> 0x002c }
            java.lang.String r2 = "Unable to unregister stowed sensor."
            r1.mo11959e(r2)     // Catch:{ all -> 0x002c }
        L_0x0035:
            r4.mStowedDisposable = r0
            return
        L_0x0038:
            r4.mStowedDisposable = r0
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.motorola.actions.ftm.statemachines.FlipToMuteStateMachine.unregisterAllSensorListeners():void");
    }

    /* access modifiers changed from: private */
    public void changeState(FlipToMuteStateEnum flipToMuteStateEnum) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("oldState: ");
        sb.append(sCurrentState.name());
        sb.append(", newState: ");
        sb.append(flipToMuteStateEnum.name());
        mALogger.mo11957d(sb.toString());
        sCurrentState = flipToMuteStateEnum;
        FlipToMuteService.setIsDNDState(flipToMuteStateEnum == FlipToMuteStateEnum.DND_ENABLED);
    }

    public void start() {
        LOGGER.mo11957d("start");
        changeState(FlipToMuteStateEnum.STARTED);
        this.mSensorManager = (SensorManager) this.mContext.getSystemService("sensor");
        this.mModalityManager = new ModalityManagerProxy(this.mContext);
        returnToInitialState();
        this.mModalityManager.resetModalityManager(this.mContext);
    }

    public void stop() {
        LOGGER.mo11957d("stop");
        unregisterAllSensorListeners();
        this.mHandler.removeCallbacks(this.mCheckMotionHistoryRunnable);
        changeState(FlipToMuteStateEnum.STARTED);
        unregisterAutomaticRulesListener();
    }
}
