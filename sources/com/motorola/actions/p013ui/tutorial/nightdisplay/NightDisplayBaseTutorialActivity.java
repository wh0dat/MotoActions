package com.motorola.actions.p013ui.tutorial.nightdisplay;

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
import com.motorola.actions.nightdisplay.common.Persistence;
import com.motorola.actions.p013ui.ImagePagerAdapter;
import com.motorola.actions.p013ui.settings.NightDisplaySettingsFragment;
import com.motorola.actions.p013ui.tutorial.TutorialActivity;
import com.motorola.actions.p013ui.tutorial.TutorialPage;
import com.motorola.actions.settings.updater.NDSettingsUpdater;
import com.motorola.actions.utils.Device;
import com.motorola.actions.utils.MALogger;
import java.util.ArrayList;
import java.util.List;

/* renamed from: com.motorola.actions.ui.tutorial.nightdisplay.NightDisplayBaseTutorialActivity */
public abstract class NightDisplayBaseTutorialActivity extends TutorialActivity implements OnPageChangeListener {
    private static final MALogger LOGGER = new MALogger(NightDisplaySettingsFragment.class);
    private ViewPager mPager;
    private TabLayout mTabDots;
    private ArrayList mTutorialScreens = new ArrayList();

    /* renamed from: com.motorola.actions.ui.tutorial.nightdisplay.NightDisplayBaseTutorialActivity$PagerAdapter */
    private class PagerAdapter extends ImagePagerAdapter {
        PagerAdapter(Context context, List<TutorialPage> list) {
            super(context, list);
        }

        public void loadText(View view, int i, int i2, boolean z) {
            TextView textView = (TextView) view.findViewById(C0504R.C0506id.text_title);
            textView.setText(i);
            if (Device.isNewMoto()) {
                textView.setTextColor(NightDisplayBaseTutorialActivity.this.getResources().getColor(C0504R.color.wave, null));
            }
            TextView textView2 = (TextView) view.findViewById(C0504R.C0506id.text_info);
            textView2.setVisibility(0);
            textView2.setText(ActionsApplication.getAppContext().getString(i2));
        }
    }

    /* access modifiers changed from: 0000 */
    public abstract List<TutorialPage> getTutorialPages();

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

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (Device.isNewMoto()) {
            setTheme(C0504R.style.Theme_Tutorial_Display);
        }
        setContentView((int) C0504R.layout.activity_tutorial_view_pager_bottom_bar);
        setupButtons(false, Persistence.isFeatureEnabled());
        this.mTabDots = (TabLayout) findViewById(C0504R.C0506id.tab_steps);
        this.mPager = (ViewPager) findViewById(C0504R.C0506id.view_pager);
        this.mPager.setAdapter(new PagerAdapter(this, getTutorialPages()));
        this.mPager.addOnPageChangeListener(this);
        this.mPager.setOffscreenPageLimit(getTutorialPages().size());
        this.mTabDots.setupWithViewPager(this.mPager);
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt("pos", this.mPager.getCurrentItem());
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
            setupButtons(true, Persistence.isFeatureEnabled());
        } else {
            setupButtons(false, Persistence.isFeatureEnabled());
        }
    }

    /* access modifiers changed from: protected */
    public void setupOptinTwoButtons() {
        super.setupOptinTwoButtons();
        this.mLayoutLeftBtn.setOnClickListener(new NightDisplayBaseTutorialActivity$$Lambda$0(this));
        this.mRightButton.setOnClickListener(new NightDisplayBaseTutorialActivity$$Lambda$1(this));
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$setupOptinTwoButtons$0$NightDisplayBaseTutorialActivity(View view) {
        NDSettingsUpdater.getInstance().toggleStatus(false, true, CommonCheckinAttributes.KEY_DAILY_ENABLE_SOURCE_TYPE_TUTORIAL);
        finish();
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$setupOptinTwoButtons$1$NightDisplayBaseTutorialActivity(View view) {
        if (!this.mIsCurrentEnabled) {
            getFDNSession().recordChange(FeatureKey.NIGHT_DISPLAY);
            NDSettingsUpdater.getInstance().toggleStatus(true, true, CommonCheckinAttributes.KEY_DAILY_ENABLE_SOURCE_TYPE_TUTORIAL);
        }
        finish();
    }

    /* access modifiers changed from: protected */
    public void setupNextButton() {
        super.setupNextButton();
        this.mSingleButton.setOnClickListener(new NightDisplayBaseTutorialActivity$$Lambda$2(this));
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$setupNextButton$2$NightDisplayBaseTutorialActivity(View view) {
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
