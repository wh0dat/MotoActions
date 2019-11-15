package com.motorola.actions.p013ui;

import android.content.Context;
import android.support.p001v4.view.PagerAdapter;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.motorola.actions.C0504R;
import com.motorola.actions.p013ui.tutorial.TutorialPage;
import java.util.List;

/* renamed from: com.motorola.actions.ui.ImagePagerAdapter */
public abstract class ImagePagerAdapter extends PagerAdapter {
    private Context mContext;
    private List<TutorialPage> mTutorialPageList;

    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    public abstract void loadText(View view, int i, int i2, boolean z);

    public ImagePagerAdapter(Context context, List<TutorialPage> list) {
        this.mContext = context;
        this.mTutorialPageList = list;
    }

    public int getCount() {
        return this.mTutorialPageList.size();
    }

    public Object instantiateItem(ViewGroup viewGroup, int i) {
        View view = null;
        if (i >= 0 && ((TutorialPage) this.mTutorialPageList.get(i)).getLayoutResId() != 0 && i < this.mTutorialPageList.size()) {
            TutorialPage tutorialPage = (TutorialPage) this.mTutorialPageList.get(i);
            view = View.inflate(this.mContext, tutorialPage.getLayoutResId(), null);
            viewGroup.addView(view);
            setupView(view, this.mTutorialPageList.size() - 1 == i, tutorialPage.getElementResId(), tutorialPage.getTitleResId(), tutorialPage.getDescriptionResId());
        }
        return view;
    }

    private void initImage(View view, int i) {
        ((TextureView) view.findViewById(C0504R.C0506id.video)).setVisibility(8);
        ImageView imageView = (ImageView) view.findViewById(C0504R.C0506id.tutorial_image);
        imageView.setVisibility(0);
        imageView.setImageResource(i);
    }

    private void setupView(View view, boolean z, int i, int i2, int i3) {
        loadText(view, i2, i3, z);
        initImage(view, i);
    }

    public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
        viewGroup.removeView((View) obj);
    }
}
