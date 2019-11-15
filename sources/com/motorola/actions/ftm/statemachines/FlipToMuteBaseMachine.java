package com.motorola.actions.ftm.statemachines;

import android.content.Context;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.ftm.FlipToMuteConstants;
import com.motorola.actions.ftm.FlipToMuteHelper;
import com.motorola.actions.ftm.FlipToMuteService;
import com.motorola.actions.ftm.instrumentation.FlipToMuteInstrumentation;
import com.motorola.actions.reflect.NotificationManagerPrivateProxy;
import com.motorola.actions.utils.DFSquadUtils;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.MotoResearchUtils;
import com.motorola.actions.utils.MotoSurveyUtils;
import com.motorola.actions.zenmode.AutomaticRulesConfigObserver;
import com.motorola.actions.zenmode.AutomaticRulesListener;
import com.motorola.actions.zenmode.ZenModeAccess;

abstract class FlipToMuteBaseMachine implements FlipToMuteBaseMachineInterface {
    private static final int EXIT_VIBRATION_DELAY_MS = 100;
    private static final MALogger LOGGER = new MALogger(FlipToMuteBaseMachine.class);
    private static final int VIBRATION_DURATION_MS = 100;
    private static final int VIBRATION_PAUSE_MS = 50;
    private AutomaticRulesConfigObserver mAutomaticRulesConfigObserver;
    protected Context mContext;
    private final Handler mHandler = new Handler();
    private Vibrator mVibrator;

    FlipToMuteBaseMachine(Context context, AutomaticRulesConfigObserver automaticRulesConfigObserver) {
        this.mContext = context;
        this.mAutomaticRulesConfigObserver = automaticRulesConfigObserver;
        this.mVibrator = (Vibrator) ActionsApplication.getAppContext().getSystemService("vibrator");
    }

    /* access modifiers changed from: 0000 */
    public synchronized void changeDoNotDisturbToOriginalState() {
        LOGGER.mo11957d("changeDoNotDisturbToOriginalState");
        boolean z = SharedPreferenceManager.getBoolean(FlipToMuteConstants.DND_ENABLED_BY_SERVICE, false);
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("sharedPref(DND_ENABLED_BY_SERVICE)=");
        sb.append(z);
        mALogger.mo11957d(sb.toString());
        if (z) {
            if (!NotificationManagerPrivateProxy.isAtLeastOneAutomaticRuleActive(this.mContext)) {
                SharedPreferenceManager.putBoolean(FlipToMuteConstants.DND_ENABLED_BY_SERVICE, false);
                unregisterAutomaticRulesListener();
                ZenModeAccess.exitZenMode();
                this.mHandler.postDelayed(new FlipToMuteBaseMachine$$Lambda$0(this), 100);
            }
            int usageTime = FlipToMuteService.getUsageTime();
            FlipToMuteInstrumentation.recordTotalDuration(usageTime);
            try {
                FlipToMuteInstrumentation.publishEvents(usageTime);
            } catch (SecurityException e) {
                e.printStackTrace();
                this.mContext.stopService(FlipToMuteService.createIntent(this.mContext));
            }
        }
        return;
    }

    private synchronized void registerAutomaticRulesListener() {
        LOGGER.mo11957d("registerAutomaticRulesListener");
        AutomaticRulesListener.getInstance().register(this.mAutomaticRulesConfigObserver);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void unregisterAutomaticRulesListener() {
        LOGGER.mo11957d("unregisterAutomaticRulesListener");
        AutomaticRulesListener.getInstance().unregister(this.mAutomaticRulesConfigObserver);
    }

    private void triggerFeedbackToMotorolaResearch() {
        MotoSurveyUtils.sendIntent(MotoResearchUtils.GESTURE_TYPE_FLIP_TO_MUTE);
        DFSquadUtils.sendIntent(MotoResearchUtils.GESTURE_TYPE_FLIP_TO_MUTE);
    }

    /* access modifiers changed from: 0000 */
    public void enterDND() {
        LOGGER.mo11957d("enterDND()");
        lambda$changeDoNotDisturbToOriginalState$0$FlipToMuteBaseMachine();
        registerAutomaticRulesListener();
        FlipToMuteService.enterZenMode();
        FlipToMuteService.broadcastFlipToMuteTriggered();
        FlipToMuteService.setDNDEnterSystemTime(false);
        triggerFeedbackToMotorolaResearch();
    }

    /* access modifiers changed from: 0000 */
    public boolean shouldEnterDNDMode() {
        boolean z = NotificationManagerPrivateProxy.isAtLeastOneAutomaticRuleActive(this.mContext) || !ZenModeAccess.isZenModeOn();
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("shouldEnterDNDMode: ");
        sb.append(z);
        mALogger.mo11957d(sb.toString());
        return z;
    }

    /* access modifiers changed from: protected */
    /* renamed from: vibrate */
    public void lambda$changeDoNotDisturbToOriginalState$0$FlipToMuteBaseMachine() {
        boolean isVibrationEnabled = FlipToMuteHelper.isVibrationEnabled();
        if (this.mVibrator != null && isVibrationEnabled) {
            this.mVibrator.vibrate(VibrationEffect.createWaveform(new long[]{50, 100, 50, 100, 50, 100}, -1));
        }
    }
}
