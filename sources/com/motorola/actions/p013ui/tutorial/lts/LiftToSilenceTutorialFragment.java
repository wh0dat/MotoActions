package com.motorola.actions.p013ui.tutorial.lts;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioAttributes.Builder;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.p001v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.motorola.actions.C0504R;
import com.motorola.actions.lts.LiftToSilenceService;
import com.motorola.actions.utils.ActivityUtils;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.ServiceUtils;
import java.io.IOException;

/* renamed from: com.motorola.actions.ui.tutorial.lts.LiftToSilenceTutorialFragment */
public class LiftToSilenceTutorialFragment extends Fragment {
    private static final int DELAY_TO_SHOW_SUCCESS_FRAGMENT = 1000;
    private static final MALogger LOGGER = new MALogger(LiftToSilenceTutorialFragment.class);
    private static final int MAX_TIME_TO_WAVE_MS = 8000;
    private static final int STREAM_TYPE = 2;
    private static final int VIBRATION_DURATION = 750;
    /* access modifiers changed from: private */
    public LiftToSilenceTutorialActivity mActivity;
    /* access modifiers changed from: private */
    public final Runnable mFakeCallTimeOutRunnable = new Runnable() {
        public void run() {
            if (LiftToSilenceTutorialFragment.this.mActivity != null) {
                LiftToSilenceTutorialFragment.this.mActivity.showFailureNoSwipeResultFragment();
            }
        }
    };
    private int mInitialVolume;
    private BroadcastReceiver mLiftToSilenceTutorialReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            LiftToSilenceTutorialFragment.this.mRootView.removeCallbacks(LiftToSilenceTutorialFragment.this.mFakeCallTimeOutRunnable);
            LiftToSilenceTutorialFragment.this.stopRingtone();
            Vibrator vibrator = (Vibrator) LiftToSilenceTutorialFragment.this.getActivity().getSystemService("vibrator");
            if (vibrator != null) {
                vibrator.vibrate(750);
            }
            LiftToSilenceTutorialFragment.this.mRootView.postDelayed(LiftToSilenceTutorialFragment.this.mShowSuccessTimeOutRunnable, 1000);
        }
    };
    private MediaPlayer mMediaPlayer;
    /* access modifiers changed from: private */
    public View mRootView;
    /* access modifiers changed from: private */
    public Runnable mShowSuccessTimeOutRunnable = new Runnable() {
        public void run() {
            if (LiftToSilenceTutorialFragment.this.mActivity != null) {
                LiftToSilenceTutorialFragment.this.mActivity.showSuccessResultFragment();
            }
        }
    };

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.mRootView = layoutInflater.inflate(C0504R.layout.fragment_demo_phone, viewGroup, false);
        this.mActivity = (LiftToSilenceTutorialActivity) getActivity();
        ActivityUtils.setStatusBarColor(getActivity(), C0504R.color.phone_blue_primary_dark);
        this.mRootView.findViewById(C0504R.C0506id.layout_phone).setOnTouchListener(new LiftToSilenceTutorialFragment$$Lambda$0(this));
        ((TextView) this.mRootView.findViewById(C0504R.C0506id.phone_action_description)).setText(C0504R.string.lts_phone_demo_title);
        return this.mRootView;
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ boolean lambda$onCreateView$0$LiftToSilenceTutorialFragment(View view, MotionEvent motionEvent) {
        if (this.mActivity != null) {
            this.mActivity.showFailureTouchResultFragment();
            view.performClick();
        }
        return true;
    }

    public void onDestroy() {
        LOGGER.mo11957d("onDestroy");
        super.onDestroy();
    }

    public void onResume() {
        super.onResume();
        playRingtone();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(this.mLiftToSilenceTutorialReceiver, new IntentFilter(LiftToSilenceService.INTENT_ACTION_SEND_STATE_TUTORIAL));
        Intent intent = new Intent(getActivity(), LiftToSilenceService.class);
        getActivity().stopService(intent);
        intent.putExtra(LiftToSilenceService.TUTORIAL_EXTRAS, true);
        ServiceUtils.startServiceSafe(intent);
        this.mRootView.postDelayed(this.mFakeCallTimeOutRunnable, 8000);
        getActivity().getWindow().addFlags(128);
    }

    public void onPause() {
        super.onPause();
        restartService();
        stopRingtone();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(this.mLiftToSilenceTutorialReceiver);
        this.mRootView.setSystemUiVisibility(0);
        this.mRootView.removeCallbacks(this.mShowSuccessTimeOutRunnable);
        this.mRootView.removeCallbacks(this.mFakeCallTimeOutRunnable);
        getActivity().getWindow().clearFlags(128);
    }

    private void restartService() {
        if (this.mActivity != null) {
            Intent intent = new Intent(this.mActivity, LiftToSilenceService.class);
            this.mActivity.stopService(intent);
            if (LiftToSilenceService.isLiftToSilenceEnabled()) {
                ServiceUtils.startServiceSafe(intent);
            }
        }
    }

    private void playRingtone() {
        AudioManager audioManager = (AudioManager) getActivity().getSystemService("audio");
        if (audioManager != null) {
            this.mInitialVolume = audioManager.getStreamVolume(2);
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("Initial stream volume = ");
            sb.append(this.mInitialVolume);
            mALogger.mo11957d(sb.toString());
            if (this.mInitialVolume == 0) {
                audioManager.setStreamVolume(2, (audioManager.getStreamMaxVolume(2) * 2) / 3, 0);
                LOGGER.mo11957d("Volume is mute. Needs to set a volume.");
            }
        } else {
            LOGGER.mo11959e("Unable to retrieve access to audio manager");
        }
        try {
            this.mMediaPlayer = new MediaPlayer();
            this.mMediaPlayer.setDataSource(getActivity(), RingtoneManager.getDefaultUri(1));
            this.mMediaPlayer.setAudioAttributes(new Builder().setUsage(6).build());
            this.mMediaPlayer.prepare();
            this.mMediaPlayer.setLooping(false);
            this.mMediaPlayer.start();
        } catch (IOException e) {
            LOGGER.mo11960e("Unable to play default ringtone", e);
        }
    }

    /* access modifiers changed from: private */
    public void stopRingtone() {
        AudioManager audioManager = (AudioManager) getActivity().getSystemService("audio");
        if (audioManager != null) {
            audioManager.setStreamVolume(2, this.mInitialVolume, 0);
        } else {
            LOGGER.mo11959e("Unable to retrieve access to audio manager");
        }
        if (this.mMediaPlayer != null) {
            if (this.mMediaPlayer.isPlaying()) {
                this.mMediaPlayer.stop();
            }
            this.mMediaPlayer.reset();
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
        }
    }
}
