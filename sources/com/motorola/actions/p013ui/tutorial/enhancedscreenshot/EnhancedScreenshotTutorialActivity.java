package com.motorola.actions.p013ui.tutorial.enhancedscreenshot;

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
import com.motorola.actions.enhancedscreenshot.EnhancedScreenshotModel;
import com.motorola.actions.p013ui.ImagePagerAdapter;
import com.motorola.actions.p013ui.tutorial.TutorialActivity;
import com.motorola.actions.p013ui.tutorial.TutorialPage;
import com.motorola.actions.settings.updater.EnhancedScreenshotSettingsUpdater;
import com.motorola.actions.utils.MALogger;
import java.util.ArrayList;
import java.util.List;

/* renamed from: com.motorola.actions.ui.tutorial.enhancedscreenshot.EnhancedScreenshotTutorialActivity */
public class EnhancedScreenshotTutorialActivity extends TutorialActivity implements OnPageChangeListener {
    private static final String CURRENT_PAGE = "pos";
    private static final MALogger LOGGER = new MALogger(EnhancedScreenshotTutorialActivity.class);
    private ViewPager mPager;
    private TabLayout mTabDots;
    private ArrayList mTutorialScreens = new ArrayList();

    /* renamed from: com.motorola.actions.ui.tutorial.enhancedscreenshot.EnhancedScreenshotTutorialActivity$PagerAdapter */
    private static class PagerAdapter extends ImagePagerAdapter {
        PagerAdapter(Context context, List<TutorialPage> list) {
            super(context, list);
        }

        public void loadText(View view, int i, int i2, boolean z) {
            ((TextView) view.findViewById(C0504R.C0506id.text_title)).setText(i);
            TextView textView = (TextView) view.findViewById(C0504R.C0506id.text_info);
            if (z) {
                TextView textView2 = (TextView) view.findViewById(C0504R.C0506id.text_info_bottom);
                textView.setVisibility(8);
                textView2.setVisibility(0);
                textView2.setText(ActionsApplication.getAppContext().getString(i2));
                return;
            }
            textView.setVisibility(0);
            textView.setText(ActionsApplication.getAppContext().getString(i2));
        }
    }

    public void onPageScrollStateChanged(int i) {
    }

    public void onPageScrolled(int i, float f, int i2) {
    }

    /* access modifiers changed from: protected */
    public List<TutorialPage> getTutorialPageList() {
        if (this.mTutorialScreens == null) {
            this.mTutorialScreens = new ArrayList();
        }
        return this.mTutorialScreens;
    }

    private List<TutorialPage> getTutorialPages() {
        List tutorialPageList = getTutorialPageList();
        if (tutorialPageList.isEmpty()) {
            tutorialPageList.add(new TutorialPage(C0504R.string.enhanced_screenshot_tutorial_long, C0504R.string.enhanced_screenshot_tutorial_long_description, C0504R.C0505drawable.screenshot_editor_tutorial_long, C0504R.layout.fragment_tutorial_view_pager));
            tutorialPageList.add(new TutorialPage(C0504R.string.enhanced_screenshot_tutorial_custom_tools, C0504R.string.enhanced_screenshot_tutorial_custom_tools_description, C0504R.C0505drawable.screenshot_editor_tutorial_custom_tools, C0504R.layout.fragment_tutorial_view_pager));
            tutorialPageList.add(new TutorialPage(C0504R.string.enhanced_screenshot_tutorial_share_delete, C0504R.string.enhanced_screenshot_tutorial_share_delete_description, C0504R.C0505drawable.screenshot_editor_tutorial_share_delete, C0504R.layout.fragment_tutorial_view_pager));
            tutorialPageList.add(new TutorialPage(C0504R.string.enhanced_screenshot_tutorial_finished, C0504R.string.enhanced_screenshot_tutorial_finished_description, C0504R.C0505drawable.tutorial_finish, C0504R.layout.fragment_tutorial_view_pager_finish));
        }
        return getTutorialPageList();
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C0504R.layout.activity_tutorial_view_pager_bottom_bar);
        setupButtons(false, EnhancedScreenshotModel.isServiceEnabled());
        this.mTabDots = (TabLayout) findViewById(C0504R.C0506id.tab_steps);
        this.mPager = (ViewPager) findViewById(C0504R.C0506id.view_pager);
        this.mPager.setAdapter(new PagerAdapter(this, getTutorialPages()));
        this.mPager.addOnPageChangeListener(this);
        this.mPager.setOffscreenPageLimit(getTutorialPages().size());
        this.mTabDots.setupWithViewPager(this.mPager);
        if (bundle != null) {
            this.mPager.setCurrentItem(bundle.getInt(CURRENT_PAGE));
        }
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt(CURRENT_PAGE, this.mPager.getCurrentItem());
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
            setupButtons(true, EnhancedScreenshotModel.isServiceEnabled());
        } else {
            setupButtons(false, EnhancedScreenshotModel.isServiceEnabled());
        }
    }

    /* access modifiers changed from: protected */
    public void setupOptinTwoButtons() {
        super.setupOptinTwoButtons();
        this.mLayoutLeftBtn.setOnClickListener(new EnhancedScreenshotTutorialActivity$$Lambda$0(this));
        this.mRightButton.setOnClickListener(new EnhancedScreenshotTutorialActivity$$Lambda$1(this));
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$setupOptinTwoButtons$0$EnhancedScreenshotTutorialActivity(View view) {
        EnhancedScreenshotSettingsUpdater.getInstance().toggleStatus(false, true, CommonCheckinAttributes.KEY_DAILY_ENABLE_SOURCE_TYPE_TUTORIAL);
        finish();
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$setupOptinTwoButtons$1$EnhancedScreenshotTutorialActivity(View view) {
        if (!this.mIsCurrentEnabled) {
            getFDNSession().recordChange(FeatureKey.ENHANCED_SCREENSHOT);
            EnhancedScreenshotSettingsUpdater.getInstance().toggleStatus(true, true, CommonCheckinAttributes.KEY_DAILY_ENABLE_SOURCE_TYPE_TUTORIAL);
        }
        finish();
    }

    /* access modifiers changed from: protected */
    public void setupNextButton() {
        super.setupNextButton();
        this.mSingleButton.setOnClickListener(new EnhancedScreenshotTutorialActivity$$Lambda$2(this));
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$setupNextButton$2$EnhancedScreenshotTutorialActivity(View view) {
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
