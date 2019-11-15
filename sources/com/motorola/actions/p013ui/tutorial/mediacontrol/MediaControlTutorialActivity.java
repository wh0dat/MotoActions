package com.motorola.actions.p013ui.tutorial.mediacontrol;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.p001v4.view.ViewPager;
import android.support.p001v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.TextView;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.C0504R;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.checkin.CommonCheckinAttributes;
import com.motorola.actions.mediacontrol.MediaControlModel;
import com.motorola.actions.p013ui.VideoPagerAdapter;
import com.motorola.actions.p013ui.tutorial.TutorialActivity;
import com.motorola.actions.p013ui.tutorial.TutorialPage;
import com.motorola.actions.p013ui.tutorial.VideoListener;
import com.motorola.actions.settings.updater.MediaControlSettingsUpdater;
import com.motorola.actions.utils.MALogger;
import java.util.ArrayList;
import java.util.List;

/* renamed from: com.motorola.actions.ui.tutorial.mediacontrol.MediaControlTutorialActivity */
public class MediaControlTutorialActivity extends TutorialActivity implements OnPageChangeListener {
    private static final String CURRENT_PAGE = "pos";
    private static final MALogger LOGGER = new MALogger(MediaControlTutorialActivity.class);
    private ViewPager mPager;
    private TabLayout mTabDots;
    private ArrayList mTutorialScreens = new ArrayList();
    private VideoListener mVideoListener;

    /* renamed from: com.motorola.actions.ui.tutorial.mediacontrol.MediaControlTutorialActivity$PagerAdapter */
    private static class PagerAdapter extends VideoPagerAdapter {
        PagerAdapter(Context context, List<TutorialPage> list, boolean z) {
            super(context, list, z);
        }

        public void loadText(View view, int i, int i2) {
            ((TextView) view.findViewById(C0504R.C0506id.text_title)).setText(i);
            TextView textView = (TextView) view.findViewById(C0504R.C0506id.text_info);
            textView.setVisibility(0);
            textView.setText(ActionsApplication.getAppContext().getString(i2));
        }
    }

    public void onPageScrollStateChanged(int i) {
    }

    public void onPageScrolled(int i, float f, int i2) {
    }

    /* access modifiers changed from: protected */
    public List<TutorialPage> getList() {
        if (this.mTutorialScreens == null) {
            this.mTutorialScreens = new ArrayList();
        }
        return this.mTutorialScreens;
    }

    private List<TutorialPage> getTutorialPages() {
        if (getList().isEmpty()) {
            getList().add(new TutorialPage(C0504R.string.media_control_tutorial_next_title, C0504R.string.media_control_tutorial_next_description, C0504R.raw.media_control_next, C0504R.layout.fragment_tutorial_view_pager));
            getList().add(new TutorialPage(C0504R.string.media_control_tutorial_previous_title, C0504R.string.media_control_tutorial_previous_description, C0504R.raw.media_control_previous, C0504R.layout.fragment_tutorial_view_pager));
            getList().add(new TutorialPage(C0504R.string.media_control_tutorial_volume_title, C0504R.string.media_control_tutorial_volume_description, C0504R.raw.media_control_volume, C0504R.layout.fragment_tutorial_view_pager));
        }
        return getList();
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C0504R.layout.activity_tutorial_view_pager_bottom_bar);
        setupButtons(false, MediaControlModel.isServiceEnabled());
        this.mTabDots = (TabLayout) findViewById(C0504R.C0506id.tab_steps);
        this.mPager = (ViewPager) findViewById(C0504R.C0506id.view_pager);
        this.mPager.setAdapter(new PagerAdapter(this, getTutorialPages(), true));
        this.mPager.addOnPageChangeListener(this);
        this.mPager.setOffscreenPageLimit(getTutorialPages().size());
        this.mTabDots.setupWithViewPager(this.mPager);
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt(CURRENT_PAGE, this.mPager.getCurrentItem());
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
    }

    public void onBackPressed() {
        if (this.mPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            this.mPager.setCurrentItem(this.mPager.getCurrentItem() - 1);
        }
    }

    public void onPageSelected(int i) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("onPageSelected ");
        sb.append(i);
        mALogger.mo11957d(sb.toString());
        if (this.mPager.getCurrentItem() == getTutorialPages().size() - 1) {
            setupButtons(true, MediaControlModel.isServiceEnabled());
        } else {
            setupButtons(false, MediaControlModel.isServiceEnabled());
        }
        if (this.mVideoListener != null && this.mVideoListener.isPlayingVideo()) {
            this.mVideoListener.pauseVideo();
        }
        this.mVideoListener = ((TutorialPage) getTutorialPages().get(i)).getVideoListener();
        if (this.mVideoListener != null) {
            this.mVideoListener.playVideo();
        }
    }

    private void loadText(View view, int i, int i2) {
        ((TextView) view.findViewById(C0504R.C0506id.text_title)).setText(i);
        TextView textView = (TextView) view.findViewById(C0504R.C0506id.text_info);
        textView.setVisibility(0);
        textView.setText(ActionsApplication.getAppContext().getString(i2));
    }

    /* access modifiers changed from: protected */
    public void setupOptinTwoButtons() {
        super.setupOptinTwoButtons();
        this.mLayoutLeftBtn.setOnClickListener(new MediaControlTutorialActivity$$Lambda$0(this));
        this.mRightButton.setOnClickListener(new MediaControlTutorialActivity$$Lambda$1(this));
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$setupOptinTwoButtons$0$MediaControlTutorialActivity(View view) {
        MediaControlSettingsUpdater.getInstance().toggleStatus(false, true, CommonCheckinAttributes.KEY_DAILY_ENABLE_SOURCE_TYPE_TUTORIAL);
        finish();
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$setupOptinTwoButtons$1$MediaControlTutorialActivity(View view) {
        if (!this.mIsCurrentEnabled) {
            getFDNSession().recordChange(FeatureKey.MEDIA_CONTROL);
            MediaControlSettingsUpdater.getInstance().toggleStatus(true, true, CommonCheckinAttributes.KEY_DAILY_ENABLE_SOURCE_TYPE_TUTORIAL);
        }
        finish();
    }

    /* access modifiers changed from: protected */
    public void setupNextButton() {
        super.setupNextButton();
        this.mSingleButton.setOnClickListener(new MediaControlTutorialActivity$$Lambda$2(this));
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$setupNextButton$2$MediaControlTutorialActivity(View view) {
        int currentItem = this.mPager.getCurrentItem();
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("onNextButtonClicked ");
        sb.append(currentItem);
        mALogger.mo11957d(sb.toString());
        if (currentItem < getTutorialPages().size() - 1) {
            this.mPager.setCurrentItem(currentItem + 1);
        }
    }
}
