package com.motorola.actions.p013ui.tutorial;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.view.Surface;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
import android.view.View;
import com.motorola.actions.utils.MALogger;

/* renamed from: com.motorola.actions.ui.tutorial.VideoListener */
public class VideoListener implements SurfaceTextureListener {
    /* access modifiers changed from: private */
    public static final MALogger LOGGER = new MALogger(VideoListener.class);
    public static final long TIME_TO_UPDATE_PLACEHOLDER = 400;
    private Context mContext;
    private final OnPreparedListener mListener = new OnPreparedListener() {
        public void onPrepared(MediaPlayer mediaPlayer) {
            VideoListener.LOGGER.mo11957d("onPrepared");
            if (VideoListener.this.mSurface != null) {
                mediaPlayer.setSurface(VideoListener.this.mSurface);
                VideoListener.this.mTextureView.setFocusable(false);
                VideoListener.this.mTextureView.setFocusableInTouchMode(false);
                mediaPlayer.setLooping(VideoListener.this.mLooping);
                mediaPlayer.start();
                VideoListener.this.setPlaceholderDelayedVisibility(8, 400);
            }
        }
    };
    /* access modifiers changed from: private */
    public boolean mLooping = true;
    private MediaPlayer mMediaPlayer;
    /* access modifiers changed from: private */
    public View mPlaceholder;
    /* access modifiers changed from: private */
    public Surface mSurface = null;
    /* access modifiers changed from: private */
    public TextureView mTextureView;
    private int mVideoId;

    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {
    }

    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
    }

    public void setPlaceholderDelayedVisibility(final int i, long j) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("setPlaceholderDelayedVisibility - visibility: ");
        sb.append(i);
        sb.append(" - delay: ");
        sb.append(j);
        sb.append(" - mPlaceholder: ");
        sb.append(this.mPlaceholder);
        mALogger.mo11957d(sb.toString());
        if (this.mPlaceholder != null) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (VideoListener.this.mPlaceholder != null) {
                        VideoListener.this.mPlaceholder.setVisibility(i);
                    }
                }
            }, j);
        }
    }

    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2) {
        LOGGER.mo11957d("onSurfaceTextureAvailable");
        this.mSurface = new Surface(surfaceTexture);
        setupMediaPlayer();
    }

    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        LOGGER.mo11957d("onSurfaceTextureDestroyed");
        if (this.mSurface != null) {
            this.mSurface.release();
        }
        this.mSurface = null;
        stopVideo();
        return true;
    }

    public VideoListener(Context context, TextureView textureView, int i, boolean z) {
        this.mContext = context;
        textureView.setSurfaceTextureListener(this);
        this.mTextureView = textureView;
        this.mVideoId = i;
        this.mLooping = z;
    }

    public VideoListener(Context context, TextureView textureView, int i) {
        this.mContext = context;
        textureView.setSurfaceTextureListener(this);
        this.mTextureView = textureView;
        this.mVideoId = i;
    }

    private void fixAspectRatio() {
        int i;
        LOGGER.mo11957d("fixAspectRatio");
        if (this.mTextureView != null && this.mMediaPlayer != null) {
            int width = this.mTextureView.getWidth();
            int height = this.mTextureView.getHeight();
            double videoHeight = ((double) this.mMediaPlayer.getVideoHeight()) / ((double) this.mMediaPlayer.getVideoWidth());
            int i2 = (int) (((double) width) * videoHeight);
            if (height > i2) {
                i = width;
            } else {
                i = (int) (((double) height) / videoHeight);
                i2 = height;
            }
            int i3 = (width - i) / 2;
            int i4 = (height - i2) / 2;
            Matrix matrix = new Matrix();
            this.mTextureView.getTransform(matrix);
            matrix.setScale(((float) i) / ((float) width), ((float) i2) / ((float) height));
            matrix.postTranslate((float) i3, (float) i4);
            this.mTextureView.setTransform(matrix);
        }
    }

    private void setupMediaPlayer() {
        LOGGER.mo11957d("setupMediaPlayer");
        this.mMediaPlayer = MediaPlayer.create(this.mContext, this.mVideoId);
        fixAspectRatio();
        this.mMediaPlayer.setOnPreparedListener(this.mListener);
    }

    public void startVideo() {
        LOGGER.mo11957d("startVideo");
        if (this.mMediaPlayer == null && this.mSurface != null) {
            setupMediaPlayer();
        }
    }

    public void stopVideo() {
        LOGGER.mo11957d("stopVideo");
        if (this.mMediaPlayer != null) {
            if (this.mMediaPlayer.isPlaying()) {
                this.mMediaPlayer.stop();
            }
            this.mMediaPlayer.reset();
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
        }
    }

    public void playVideo() {
        LOGGER.mo11957d("playVideo");
        if (this.mMediaPlayer != null && this.mSurface != null) {
            this.mMediaPlayer.seekTo(0);
            this.mMediaPlayer.start();
        }
    }

    public boolean isPlayingVideo() {
        LOGGER.mo11957d("isPlayingVideo");
        if (this.mMediaPlayer == null || this.mSurface == null) {
            return false;
        }
        return this.mMediaPlayer.isPlaying();
    }

    public void pauseVideo() {
        LOGGER.mo11957d("pauseVideo");
        if (this.mMediaPlayer != null && this.mSurface != null) {
            this.mMediaPlayer.pause();
            this.mMediaPlayer.seekTo(0);
        }
    }

    public void setPlaceholder(View view) {
        this.mPlaceholder = view;
    }
}
