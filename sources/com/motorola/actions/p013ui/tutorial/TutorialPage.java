package com.motorola.actions.p013ui.tutorial;

/* renamed from: com.motorola.actions.ui.tutorial.TutorialPage */
public class TutorialPage {
    private int mDescriptionResId;
    private int mElementResId;
    private int mLayoutResId;
    private int mTitleResId;
    private VideoListener mVideoListener;

    public TutorialPage(int i, int i2, int i3, int i4) {
        this.mTitleResId = i;
        this.mDescriptionResId = i2;
        this.mElementResId = i3;
        this.mLayoutResId = i4;
    }

    public TutorialPage(int i, int i2) {
        this.mElementResId = i;
        this.mLayoutResId = i2;
    }

    public int getTitleResId() {
        return this.mTitleResId;
    }

    public int getDescriptionResId() {
        return this.mDescriptionResId;
    }

    public int getElementResId() {
        return this.mElementResId;
    }

    public int getLayoutResId() {
        return this.mLayoutResId;
    }

    public void setVideoListener(VideoListener videoListener) {
        this.mVideoListener = videoListener;
    }

    public VideoListener getVideoListener() {
        return this.mVideoListener;
    }
}
