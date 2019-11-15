package com.motorola.actions.p013ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.p001v4.view.PagerAdapter;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.motorola.actions.C0504R;
import com.motorola.actions.p013ui.tutorial.TutorialPage;
import com.motorola.actions.p013ui.tutorial.VideoListener;
import java.util.List;

/* renamed from: com.motorola.actions.ui.VideoPagerAdapter */
public abstract class VideoPagerAdapter extends PagerAdapter {
    private Context mContext;
    private boolean mIsTutorial;
    private List<TutorialPage> mVideoDetailPages;

    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    public abstract void loadText(View view, int i, int i2);

    public VideoPagerAdapter(Context context, List<TutorialPage> list, boolean z) {
        this.mContext = context;
        this.mVideoDetailPages = list;
        this.mIsTutorial = z;
    }

    public int getCount() {
        return this.mVideoDetailPages.size();
    }

    public Object instantiateItem(ViewGroup viewGroup, int i) {
        if (i < 0 || ((TutorialPage) this.mVideoDetailPages.get(i)).getLayoutResId() == 0 || i >= this.mVideoDetailPages.size()) {
            return null;
        }
        TutorialPage tutorialPage = (TutorialPage) this.mVideoDetailPages.get(i);
        View inflate = View.inflate(this.mContext, tutorialPage.getLayoutResId(), null);
        viewGroup.addView(inflate);
        setupView(inflate, tutorialPage);
        tutorialPage.setVideoListener(new VideoListener(inflate.getContext(), (TextureView) inflate.findViewById(C0504R.C0506id.video), tutorialPage.getElementResId()));
        return inflate;
    }

    private void setupView(@NonNull View view, TutorialPage tutorialPage) {
        initVideo(view);
        if (this.mIsTutorial) {
            loadText(view, tutorialPage.getTitleResId(), tutorialPage.getDescriptionResId());
        }
    }

    private void initVideo(View view) {
        ((TextureView) view.findViewById(C0504R.C0506id.video)).setVisibility(0);
        if (this.mIsTutorial) {
            ((ImageView) view.findViewById(C0504R.C0506id.tutorial_image)).setVisibility(8);
        }
    }

    public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
        viewGroup.removeView((View) obj);
    }
}
