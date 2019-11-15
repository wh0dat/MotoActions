package com.motorola.actions.lts;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Vibrator;
import android.support.p001v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import com.motorola.actions.lts.instrumentation.LiftToSilenceInstrumentation;
import com.motorola.actions.lts.sensoraccess.ActionsSensorObserver;
import com.motorola.actions.reflect.TelephonyManagerPrivateProxy;
import com.motorola.actions.utils.DFSquadUtils;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.MotoResearchUtils;
import com.motorola.actions.utils.MotoSurveyUtils;

public class LiftToSilenceActivation implements ActionsSensorObserver {
    private static final MALogger LOGGER = new MALogger(LiftToSilenceActivation.class);
    private static final long[] VIBRATION_PATTERN = {0, 1000, 1000};
    private static final int VIBRATION_PATTERN_REPEAT = 1;
    private Context mContext;
    private boolean mIsTriggered;
    TelephonyManagerPrivateProxy mTelephonyManagerPrivateProxy;
    private Vibrator mVibrator;

    LiftToSilenceActivation(Context context) {
        this.mTelephonyManagerPrivateProxy = new TelephonyManagerPrivateProxy(context);
        this.mContext = context;
    }

    public void onLiftToSilence() {
        if (!this.mIsTriggered) {
            this.mIsTriggered = true;
            TelephonyManager telephonyManager = (TelephonyManager) this.mContext.getSystemService("phone");
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("LTS detected. Call state: ");
            sb.append(telephonyManager != null ? Integer.valueOf(telephonyManager.getCallState()) : "null");
            mALogger.mo11957d(sb.toString());
            if (telephonyManager != null && telephonyManager.getCallState() == 1) {
                AudioManager audioManager = (AudioManager) this.mContext.getSystemService("audio");
                MALogger mALogger2 = LOGGER;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("AudioManager: ");
                sb2.append(audioManager);
                sb2.append(", mode: ");
                sb2.append(audioManager != null ? Integer.valueOf(audioManager.getMode()) : "null");
                mALogger2.mo11957d(sb2.toString());
                if (audioManager != null && audioManager.getMode() == 1) {
                    this.mVibrator = (Vibrator) this.mContext.getSystemService("vibrator");
                    this.mVibrator.vibrate(VIBRATION_PATTERN, 1);
                    this.mTelephonyManagerPrivateProxy.silenceRinger();
                    LiftToSilenceInstrumentation.setTimeVolumeDecreased();
                    LiftToSilenceInstrumentation.setSilencedCause(LiftToSilenceInstrumentation.KEY_LIFT_SILENCED);
                    LiftToSilenceInstrumentation.recordLiftToSilenceDailyEvents();
                    MotoSurveyUtils.sendIntent(MotoResearchUtils.GESTURE_TYPE_LIFT_TO_SILENCE);
                    DFSquadUtils.sendIntent(MotoResearchUtils.GESTURE_TYPE_LIFT_TO_SILENCE);
                }
            }
            LocalBroadcastManager.getInstance(this.mContext).sendBroadcast(new Intent(LiftToSilenceService.INTENT_ACTION_SEND_STATE_TUTORIAL));
            return;
        }
        LOGGER.mo11963w("LTS detected again");
    }

    /* access modifiers changed from: 0000 */
    public void stopVibration() {
        if (this.mVibrator != null) {
            this.mVibrator.cancel();
        }
    }

    /* access modifiers changed from: 0000 */
    public void finish() {
        stopVibration();
        this.mIsTriggered = false;
    }
}
