package com.motorola.actions.p013ui.tutorial.onenav.soft;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.p001v4.view.ViewPager;
import android.support.p001v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.motorola.actions.C0504R;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.onenav.instrumentation.OneNavInstrumentation;
import com.motorola.actions.onenav.instrumentation.SoftOneNavInstrumentationObserver;
import com.motorola.actions.p013ui.ActionsBaseActivity;
import com.motorola.actions.p013ui.VideoPagerAdapter;
import com.motorola.actions.p013ui.tutorial.TutorialPage;
import com.motorola.actions.p013ui.tutorial.VideoListener;
import com.motorola.actions.reflect.MotorolaSettings;
import com.motorola.actions.utils.Constants;
import com.motorola.actions.utils.MALogger;
import java.util.ArrayList;
import java.util.List;

/* renamed from: com.motorola.actions.ui.tutorial.onenav.soft.SoftOneNavWelcomeActivity */
public class SoftOneNavWelcomeActivity extends ActionsBaseActivity implements OnPageChangeListener {
    private static final MALogger LOGGER = new MALogger(SoftOneNavWelcomeActivity.class);
    private TabLayout mTabDots;
    private VideoListener mVideoListener;
    private ArrayList mVideoScreens = new ArrayList();
    private ViewPager mViewPager;

    /* renamed from: com.motorola.actions.ui.tutorial.onenav.soft.SoftOneNavWelcomeActivity$PagerAdapter */
    private static class PagerAdapter extends VideoPagerAdapter {
        public void loadText(View view, int i, int i2) {
        }

        PagerAdapter(Context context, List<TutorialPage> list, boolean z) {
            super(context, list, z);
        }
    }

    public void onPageScrollStateChanged(int i) {
    }

    public void onPageScrolled(int i, float f, int i2) {
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C0504R.layout.activity_welcome_softonenav);
        SharedPreferenceManager.putBoolean(Constants.ONENAV_SCREEN_ALREADY_SHOWN, true);
        MotorolaSettings.setSoftOneNavDiscovery(3);
        this.mViewPager = (ViewPager) findViewById(C0504R.C0506id.video_detail_viewpager);
        this.mTabDots = (TabLayout) findViewById(C0504R.C0506id.tab_steps);
        this.mViewPager.addOnPageChangeListener(this);
        this.mViewPager.setAdapter(new PagerAdapter(this, getVideoDetailPages(), false));
        this.mViewPager.setOffscreenPageLimit(getVideoDetailPages().size());
        this.mTabDots.setupWithViewPager(this.mViewPager);
        setupButton();
    }

    private List<TutorialPage> getVideoDetailPages() {
        if (this.mVideoScreens.isEmpty()) {
            this.mVideoScreens.add(new TutorialPage(C0504R.raw.soft_onenav_tutorial_01, C0504R.layout.full_screen_video));
            this.mVideoScreens.add(new TutorialPage(C0504R.raw.soft_onenav_tutorial_02, C0504R.layout.full_screen_video));
        }
        return this.mVideoScreens;
    }

    private void setupButton() {
        LOGGER.mo11957d("setupButton");
        ((TextView) findViewById(C0504R.C0506id.leftBtn)).setText(C0504R.string.no_thanks);
        Button button = (Button) findViewById(C0504R.C0506id.rightBtn);
        button.setText(C0504R.string.try_it_out);
        ((FrameLayout) findViewById(C0504R.C0506id.frame_left_button)).setOnClickListener(new SoftOneNavWelcomeActivity$$Lambda$0(this));
        button.setOnClickListener(new SoftOneNavWelcomeActivity$$Lambda$1(this));
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$setupButton$0$SoftOneNavWelcomeActivity(View view) {
        SoftOneNavInstrumentationObserver.unregisterObserver();
        OneNavInstrumentation.recordOneNavDiscoveryDismissFullSheet();
        finish();
        overridePendingTransition(C0504R.anim.splash_slide_in_anim_reverse_set, C0504R.anim.splash_slide_out_anim_reverse_set);
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$setupButton$1$SoftOneNavWelcomeActivity(View view) {
        Intent intent = new Intent(this, SoftOneNavTutorialActivity.class);
        if (getIntent().getExtras() != null) {
            intent.putExtras(getIntent().getExtras());
        }
        SoftOneNavInstrumentationObserver.unregisterObserver();
        OneNavInstrumentation.recordOneNavDiscoveryTryItOutFullSheet();
        startActivity(intent);
        overridePendingTransition(C0504R.anim.splash_slide_in_anim_set, C0504R.anim.splash_slide_out_anim_set);
        finish();
    }

    public void onPause() {
        super.onPause();
        if (this.mViewPager != null) {
            this.mVideoListener = ((TutorialPage) getVideoDetailPages().get(this.mViewPager.getCurrentItem())).getVideoListener();
        }
        if (this.mVideoListener != null) {
            this.mVideoListener.stopVideo();
        }
    }

    public void onResume() {
        super.onResume();
        if (this.mVideoListener != null) {
            this.mVideoListener.startVideo();
        }
    }

    public static void openWelcomeActivity(@NonNull Activity activity, Bundle bundle) {
        Intent intent = new Intent(activity, SoftOneNavWelcomeActivity.class);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    public void onPageSelected(int i) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("onPageSelected ");
        sb.append(i);
        mALogger.mo11957d(sb.toString());
        if (this.mVideoListener != null && this.mVideoListener.isPlayingVideo()) {
            this.mVideoListener.pauseVideo();
        }
        this.mVideoListener = ((TutorialPage) getVideoDetailPages().get(i)).getVideoListener();
        if (this.mVideoListener != null) {
            this.mVideoListener.playVideo();
        }
    }
}
