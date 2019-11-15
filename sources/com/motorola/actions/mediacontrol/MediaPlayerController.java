package com.motorola.actions.mediacontrol;

import android.media.AudioManager;
import android.view.KeyEvent;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.mediacontrol.instrumentation.MediaControlInstrumentation;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.MotoResearchUtils;
import com.motorola.actions.utils.MotoSurveyUtils;

public final class MediaPlayerController {
    private static final MALogger LOGGER = new MALogger(MediaPlayerController.class);
    private AudioManager mAudioManager;

    private static class SingletonHolder {
        static final MediaPlayerController INSTANCE = new MediaPlayerController();

        private SingletonHolder() {
        }
    }

    private MediaPlayerController() {
        this.mAudioManager = (AudioManager) ActionsApplication.getAppContext().getSystemService("audio");
    }

    public static MediaPlayerController getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void next() {
        LOGGER.mo11957d("next");
        sendMediaIntent(87);
        MediaControlInstrumentation.recordMusicChangeEvent();
    }

    public void previous() {
        LOGGER.mo11957d("previous");
        sendMediaIntent(88);
        MediaControlInstrumentation.recordMusicChangeEvent();
    }

    private void sendMediaIntent(int i) {
        if (this.mAudioManager != null) {
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("Dispatching media key event ");
            sb.append(i);
            mALogger.mo11957d(sb.toString());
            this.mAudioManager.dispatchMediaKeyEvent(new KeyEvent(0, i));
            this.mAudioManager.dispatchMediaKeyEvent(new KeyEvent(1, i));
            MotoSurveyUtils.sendIntent(MotoResearchUtils.GESTURE_TYPE_MEDIA_CONTROL);
            return;
        }
        LOGGER.mo11957d("Couldn't dispatch media key event.");
    }
}
