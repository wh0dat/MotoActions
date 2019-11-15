package com.motorola.actions.mediacontrol;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.KeyEvent;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.mediacontrol.ScreenOnOffReceiver.ScreenEventListener;
import com.motorola.actions.utils.MALogger;

public class VolumeEventHandler implements ScreenEventListener {
    /* access modifiers changed from: private */
    public static final MALogger LOGGER = new MALogger(VolumeEventHandler.class);
    private static final String MOTO_ACTIONS_EXTRA_KEYEVENT = "keyevent";
    private static final String MOTO_ACTIONS_MUSIC_CONTROL_ACTION = "com.motorola.intent.action.VOLUME_KEY";
    private ScreenOnOffReceiver mScreenReceiver;
    private VolumeKeyReceiver mVolumeKeyReceiver;

    private static class VolumeKeyReceiver extends BroadcastReceiver {
        private static final int BOTH_BUTTONS_PRESS_DELAY_MS = 100;
        private static final int PRESS_INTERVAL_MS = 300;
        private static final long VIBRATION_DURATION_MS = 100;
        /* access modifiers changed from: private */
        public AudioManager mAudioManager = ((AudioManager) ActionsApplication.getAppContext().getSystemService(AudioManager.class));
        /* access modifiers changed from: private */
        public Handler mHandler = new Handler();
        boolean mRegistered;
        /* access modifiers changed from: private */
        public Runnable mRunnableNext = new Runnable() {
            public void run() {
                if (VolumeKeyReceiver.this.mAudioManager.isMusicActive()) {
                    VolumeKeyReceiver.this.mHandler.removeCallbacks(VolumeKeyReceiver.this.mRunnablePrevious);
                    MediaPlayerController.getInstance().next();
                    VolumeKeyReceiver.this.vibrate();
                }
            }
        };
        /* access modifiers changed from: private */
        public Runnable mRunnablePrevious = new Runnable() {
            public void run() {
                if (VolumeKeyReceiver.this.mAudioManager.isMusicActive()) {
                    VolumeKeyReceiver.this.mHandler.removeCallbacks(VolumeKeyReceiver.this.mRunnableNext);
                    MediaPlayerController.getInstance().previous();
                    VolumeKeyReceiver.this.vibrate();
                }
            }
        };
        private Runnable mRunnableVolumeDown = new VolumeEventHandler$VolumeKeyReceiver$$Lambda$1(this);
        /* access modifiers changed from: private */
        public Runnable mRunnableVolumeDownHold = new Runnable() {
            public void run() {
                if (VolumeKeyReceiver.this.mVolumeDownPressed) {
                    VolumeKeyReceiver.this.decreaseVolume();
                    VolumeKeyReceiver.this.mHandler.postDelayed(VolumeKeyReceiver.this.mRunnableVolumeDownHold, 300);
                }
            }
        };
        private Runnable mRunnableVolumeUp = new VolumeEventHandler$VolumeKeyReceiver$$Lambda$0(this);
        /* access modifiers changed from: private */
        public Runnable mRunnableVolumeUpHold = new Runnable() {
            public void run() {
                if (VolumeKeyReceiver.this.mVolumeUpPressed) {
                    VolumeKeyReceiver.this.increaseVolume();
                    VolumeKeyReceiver.this.mHandler.postDelayed(VolumeKeyReceiver.this.mRunnableVolumeUpHold, 300);
                }
            }
        };
        private Vibrator mVibrator = ((Vibrator) ActionsApplication.getAppContext().getSystemService("vibrator"));
        private KeyEvent mVolumeDownEvent;
        /* access modifiers changed from: private */
        public boolean mVolumeDownPressed = false;
        private KeyEvent mVolumeUpEvent;
        /* access modifiers changed from: private */
        public boolean mVolumeUpPressed = false;

        /* access modifiers changed from: 0000 */
        public final /* synthetic */ void lambda$new$0$VolumeEventHandler$VolumeKeyReceiver() {
            if (this.mAudioManager.isMusicActive()) {
                increaseVolume();
            }
        }

        /* access modifiers changed from: 0000 */
        public final /* synthetic */ void lambda$new$1$VolumeEventHandler$VolumeKeyReceiver() {
            if (this.mAudioManager.isMusicActive()) {
                decreaseVolume();
            }
        }

        VolumeKeyReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            handleLongPress(intent);
        }

        private void handleLongPress(Intent intent) {
            KeyEvent keyEvent = (KeyEvent) intent.getParcelableExtra(VolumeEventHandler.MOTO_ACTIONS_EXTRA_KEYEVENT);
            boolean z = keyEvent.getAction() == 0;
            int keyCode = keyEvent.getKeyCode();
            boolean isMusicActive = this.mAudioManager.isMusicActive();
            MALogger access$1100 = VolumeEventHandler.LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("Received ");
            sb.append(intent.getAction());
            sb.append(", keycode: ");
            sb.append(keyCode);
            sb.append(", isDown: ");
            sb.append(z);
            sb.append(", music active: ");
            sb.append(isMusicActive);
            sb.append(", isDown timestamp: ");
            sb.append(keyEvent.getDownTime());
            sb.append(", event timestamp: ");
            sb.append(keyEvent.getEventTime());
            access$1100.mo11957d(sb.toString());
            switch (keyCode) {
                case 24:
                    this.mVolumeUpEvent = keyEvent;
                    if (z) {
                        this.mHandler.postDelayed(this.mRunnableNext, 300);
                        return;
                    } else if (!isValidInterval(this.mVolumeUpEvent)) {
                        this.mHandler.removeCallbacks(this.mRunnableNext);
                        this.mHandler.postDelayed(this.mRunnableVolumeUp, VIBRATION_DURATION_MS);
                        return;
                    } else {
                        return;
                    }
                case 25:
                    this.mVolumeDownEvent = keyEvent;
                    if (z) {
                        this.mHandler.postDelayed(this.mRunnablePrevious, 300);
                        return;
                    } else if (!isValidInterval(this.mVolumeDownEvent)) {
                        this.mHandler.removeCallbacks(this.mRunnablePrevious);
                        this.mHandler.postDelayed(this.mRunnableVolumeDown, VIBRATION_DURATION_MS);
                        return;
                    } else {
                        return;
                    }
                default:
                    VolumeEventHandler.LOGGER.mo11957d("Ignore event.");
                    return;
            }
        }

        /* access modifiers changed from: private */
        public void increaseVolume() {
            setVolume(1);
        }

        /* access modifiers changed from: private */
        public void decreaseVolume() {
            setVolume(-1);
        }

        private void setVolume(int i) {
            int streamVolume = this.mAudioManager.getStreamVolume(3);
            int i2 = i + streamVolume;
            this.mAudioManager.setStreamVolume(3, i2, 0);
            MALogger access$1100 = VolumeEventHandler.LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("Volume changed from ");
            sb.append(streamVolume);
            sb.append(" to ");
            sb.append(i2);
            access$1100.mo11957d(sb.toString());
        }

        private boolean isValidInterval(KeyEvent keyEvent) {
            return keyEvent != null && keyEvent.getEventTime() - keyEvent.getDownTime() > 300;
        }

        /* access modifiers changed from: private */
        public void vibrate() {
            this.mVibrator.vibrate(VibrationEffect.createOneShot(VIBRATION_DURATION_MS, -1));
        }

        /* access modifiers changed from: 0000 */
        public void register() {
            if (!this.mRegistered) {
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(VolumeEventHandler.MOTO_ACTIONS_MUSIC_CONTROL_ACTION);
                ActionsApplication.getAppContext().registerReceiver(this, intentFilter);
                this.mRegistered = true;
            }
        }

        /* access modifiers changed from: 0000 */
        public void unregister() {
            if (this.mRegistered) {
                try {
                    ActionsApplication.getAppContext().unregisterReceiver(this);
                } catch (IllegalArgumentException e) {
                    VolumeEventHandler.LOGGER.mo11960e("Unable to unregister timechange-receiver", e);
                } catch (Throwable th) {
                    this.mRegistered = false;
                    throw th;
                }
                this.mRegistered = false;
            }
        }
    }

    public void start() {
        if (this.mScreenReceiver == null) {
            this.mScreenReceiver = new ScreenOnOffReceiver(this);
        }
        this.mScreenReceiver.register();
    }

    public void stop() {
        if (this.mScreenReceiver != null) {
            this.mScreenReceiver.unregister();
        }
    }

    private void registerVolumeKeyReceiver() {
        if (this.mVolumeKeyReceiver == null) {
            this.mVolumeKeyReceiver = new VolumeKeyReceiver();
        }
        this.mVolumeKeyReceiver.register();
    }

    private void unregisterVolumeKeyReceiver() {
        if (this.mVolumeKeyReceiver != null) {
            this.mVolumeKeyReceiver.unregister();
        }
    }

    public void onScreenOn() {
        MediaControlModel.setMusicMode(false);
        unregisterVolumeKeyReceiver();
    }

    public void onScreenOff() {
        MediaControlModel.setMusicMode(true);
        registerVolumeKeyReceiver();
    }
}
