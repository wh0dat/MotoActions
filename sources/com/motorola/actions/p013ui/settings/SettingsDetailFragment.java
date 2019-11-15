package com.motorola.actions.p013ui.settings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.p001v4.view.ViewPager;
import android.support.p001v4.view.ViewPager.OnPageChangeListener;
import android.support.p004v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Switch;
import android.widget.TextView;
import com.motorola.actions.C0504R;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.p013ui.FDNAwareFragment;
import com.motorola.actions.p013ui.VideoPagerAdapter;
import com.motorola.actions.p013ui.settings.DropDownLayout.InteractionListener;
import com.motorola.actions.p013ui.tutorial.TutorialPage;
import com.motorola.actions.p013ui.tutorial.VideoListener;
import com.motorola.actions.settings.provider.ActionsSettingsProvider;
import com.motorola.actions.settings.provider.p012v2.actions.ContainerProviderItemApproach;
import com.motorola.actions.settings.provider.p012v2.actions.ContainerProviderItemEnhancedScreenshot;
import com.motorola.actions.settings.provider.p012v2.actions.ContainerProviderItemFOC;
import com.motorola.actions.settings.provider.p012v2.actions.ContainerProviderItemFPSSlideApp;
import com.motorola.actions.settings.provider.p012v2.actions.ContainerProviderItemFPSSlideHome;
import com.motorola.actions.settings.provider.p012v2.actions.ContainerProviderItemFTM;
import com.motorola.actions.settings.provider.p012v2.actions.ContainerProviderItemLTS;
import com.motorola.actions.settings.provider.p012v2.actions.ContainerProviderItemLiftToUnlock;
import com.motorola.actions.settings.provider.p012v2.actions.ContainerProviderItemMediaControl;
import com.motorola.actions.settings.provider.p012v2.actions.ContainerProviderItemMicroscreen;
import com.motorola.actions.settings.provider.p012v2.actions.ContainerProviderItemOneNav;
import com.motorola.actions.settings.provider.p012v2.actions.ContainerProviderItemQC;
import com.motorola.actions.settings.provider.p012v2.actions.ContainerProviderItemQuickScreenshot;
import com.motorola.actions.settings.provider.p012v2.display.ContainerProviderItemAttentiveDisplay;
import com.motorola.actions.settings.provider.p012v2.display.ContainerProviderItemNightDisplay;
import com.motorola.actions.utils.Device;
import com.motorola.actions.utils.MALogger;
import java.util.Collections;
import java.util.List;

/* renamed from: com.motorola.actions.ui.settings.SettingsDetailFragment */
public abstract class SettingsDetailFragment extends FDNAwareFragment implements OnItemClickListener, InteractionListener, OnPageChangeListener {
    private static final int CLOSE_BUTTON_ELEVATION = 2;
    private static final MALogger LOGGER = new MALogger(SettingsDetailFragment.class);
    private static final int OPEN_BUTTON_ELEVATION = 0;
    protected View mButtonShadow;
    protected Context mContext;
    private OnClickListener mDefaultTryItButtonListener = new SettingsDetailFragment$$Lambda$0(this);
    protected TextView mDetailText;
    protected DropDownLayout mDropdownContainer;
    protected View mDropdownShadow;
    protected TextView mStatusText;
    private boolean mStatusToBeSet;
    private Switch mSwitchButton;
    private TabLayout mTabDots;
    protected TextureView mTextureView;
    protected Button mTryItButton;
    private boolean mUpdateStatusOnStart;
    protected VideoListener mVideoListener;
    protected ViewPager mViewPager;

    /* renamed from: com.motorola.actions.ui.settings.SettingsDetailFragment$PagerAdapter */
    private static class PagerAdapter extends VideoPagerAdapter {
        public void loadText(View view, int i, int i2) {
        }

        PagerAdapter(Context context, List<TutorialPage> list, boolean z) {
            super(context, list, z);
        }
    }

    /* renamed from: com.motorola.actions.ui.settings.SettingsDetailFragment$SettingStatus */
    protected enum SettingStatus {
        ENABLED,
        DISABLED,
        UNAVAILABLE
    }

    /* access modifiers changed from: protected */
    public abstract void changeStatus(boolean z);

    /* access modifiers changed from: protected */
    public abstract int getDetailTextId();

    /* access modifiers changed from: protected */
    public abstract SettingStatus getEnabledStatus();

    /* access modifiers changed from: protected */
    public abstract FeatureKey getFeatureKey();

    /* access modifiers changed from: protected */
    public int getLayoutId() {
        return C0504R.layout.fragment_settings_detail;
    }

    /* access modifiers changed from: protected */
    public abstract int getTitleTextId();

    /* access modifiers changed from: protected */
    public abstract int getVideoId();

    public void onPageScrollStateChanged(int i) {
    }

    public void onPageScrolled(int i, float f, int i2) {
    }

    /* access modifiers changed from: protected */
    public abstract Class tutorialClass();

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$new$0$SettingsDetailFragment(View view) {
        LOGGER.mo11957d("Tutorial started");
        startTutorial();
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        LOGGER.mo11957d("onCreateView");
        Activity activity = getActivity();
        if (activity != null) {
            this.mContext = activity.getApplicationContext();
        }
        View inflate = layoutInflater.inflate(getLayoutId(), viewGroup, false);
        this.mStatusText = (TextView) inflate.findViewById(C0504R.C0506id.title);
        this.mSwitchButton = (Switch) inflate.findViewById(C0504R.C0506id.enabled_box);
        RelativeLayout relativeLayout = (RelativeLayout) inflate.findViewById(C0504R.C0506id.setting_id);
        this.mTryItButton = (Button) inflate.findViewById(C0504R.C0506id.text_try_button);
        this.mViewPager = (ViewPager) inflate.findViewById(C0504R.C0506id.video_detail_viewpager);
        if (this.mViewPager == null || activity == null) {
            this.mTextureView = (TextureView) inflate.findViewById(C0504R.C0506id.video);
            this.mVideoListener = new VideoListener(inflate.getContext(), this.mTextureView, getVideoId());
        } else {
            this.mTabDots = (TabLayout) inflate.findViewById(C0504R.C0506id.tab_steps);
            this.mViewPager.addOnPageChangeListener(this);
            this.mViewPager.setAdapter(new PagerAdapter(activity, getVideoDetailPages(), false));
            this.mViewPager.setOffscreenPageLimit(getVideoDetailPages().size());
            this.mTabDots.setupWithViewPager(this.mViewPager);
        }
        this.mDetailText = (TextView) inflate.findViewById(C0504R.C0506id.detail_text);
        this.mDetailText.setText(getDetailTextId());
        configureDropdownViews(inflate);
        if (tutorialClass() == null) {
            this.mTryItButton.setVisibility(8);
            inflate.findViewById(C0504R.C0506id.bottom_bar).setVisibility(8);
            setDetailTextBottomMargin();
        } else {
            if (tutorialClass().equals(FlipToMuteSettingsFragment.class)) {
                this.mTryItButton.setVisibility(8);
            }
            setDefaultTryItButtonListener();
        }
        relativeLayout.setOnClickListener(new SettingsDetailFragment$$Lambda$1(this));
        hideCard();
        return inflate;
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$onCreateView$1$SettingsDetailFragment(View view) {
        this.mSwitchButton.toggle();
    }

    public List<TutorialPage> getVideoDetailPages() {
        return Collections.emptyList();
    }

    private void hideCard() {
        String str;
        switch (getFeatureKey()) {
            case APPROACH:
                str = ContainerProviderItemApproach.PRIORITY_KEY;
                break;
            case QUICK_CAPTURE:
                str = ContainerProviderItemQC.PRIORITY_KEY;
                break;
            case FLASH_ON_CHOP:
                str = ContainerProviderItemFOC.PRIORITY_KEY;
                break;
            case ATTENTIVE_DISPLAY:
                str = ContainerProviderItemAttentiveDisplay.PRIORITY_KEY;
                break;
            case MICROSCREEN:
                str = ContainerProviderItemMicroscreen.PRIORITY_KEY;
                break;
            case FLIP_TO_DND:
                str = ContainerProviderItemFTM.PRIORITY_KEY;
                break;
            case PICKUP_TO_STOP_RINGING:
                str = ContainerProviderItemLTS.PRIORITY_KEY;
                break;
            case NIGHT_DISPLAY:
                str = ContainerProviderItemNightDisplay.PRIORITY_KEY;
                break;
            case ONE_NAV:
                str = ContainerProviderItemOneNav.PRIORITY_KEY;
                break;
            case QUICK_SCREENSHOT:
                str = ContainerProviderItemQuickScreenshot.PRIORITY_KEY;
                break;
            case ENHANCED_SCREENSHOT:
                str = ContainerProviderItemEnhancedScreenshot.PRIORITY_KEY;
                break;
            case MEDIA_CONTROL:
                str = ContainerProviderItemMediaControl.PRIORITY_KEY;
                break;
            case FPS_SLIDE_HOME:
                str = ContainerProviderItemFPSSlideHome.PRIORITY_KEY;
                break;
            case FPS_SLIDE_APP:
                str = ContainerProviderItemFPSSlideApp.PRIORITY_KEY;
                break;
            case LIFT_TO_UNLOCK:
                str = ContainerProviderItemLiftToUnlock.PRIORITY_KEY;
                break;
            default:
                MALogger mALogger = LOGGER;
                StringBuilder sb = new StringBuilder();
                sb.append("Couldn't hide card for ");
                sb.append(getFeatureKey());
                mALogger.mo11959e(sb.toString());
                str = null;
                break;
        }
        if (str != null) {
            ActionsSettingsProvider.hideCard(str);
        }
    }

    private void configureDropdownViews(View view) {
        this.mDropdownContainer = (DropDownLayout) view.findViewById(C0504R.C0506id.dropdown_settings);
        setDropdownInteractionListener();
        this.mDropdownShadow = view.findViewById(C0504R.C0506id.dropdown_shadow);
        this.mButtonShadow = view.findViewById(C0504R.C0506id.button_shadow);
        setShadowClickListener();
    }

    private void setDetailTextBottomMargin() {
        LayoutParams layoutParams = (LayoutParams) this.mDetailText.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, (int) getResources().getDimension(C0504R.dimen.settings_tutorial_button_layout_height));
        this.mDetailText.setLayoutParams(layoutParams);
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        this.mSwitchButton.toggle();
    }

    public void onPause() {
        LOGGER.mo11957d("onPause");
        super.onPause();
        this.mSwitchButton.setOnCheckedChangeListener(null);
        if (this.mViewPager != null) {
            this.mVideoListener = ((TutorialPage) getVideoDetailPages().get(this.mViewPager.getCurrentItem())).getVideoListener();
        }
        if (this.mVideoListener != null) {
            this.mVideoListener.stopVideo();
        }
    }

    public void onResume() {
        LOGGER.mo11957d("onResume");
        super.onResume();
        updateStatus(getEnabledStatus(), false);
        if (this.mVideoListener != null) {
            this.mVideoListener.startVideo();
        }
        this.mSwitchButton.setOnCheckedChangeListener(new SettingsDetailFragment$$Lambda$2(this));
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$onResume$2$SettingsDetailFragment(CompoundButton compoundButton, boolean z) {
        changeStatus(z);
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        if (this.mUpdateStatusOnStart) {
            changeStatus(this.mStatusToBeSet);
        }
    }

    /* access modifiers changed from: protected */
    public void updateStatus(SettingStatus settingStatus) {
        updateStatus(settingStatus, true);
    }

    private void updateStatus(SettingStatus settingStatus, boolean z) {
        if (z) {
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append(getClass().getSimpleName());
            sb.append(" ");
            sb.append(getString(C0504R.string.log_feature_enable_status_changed));
            sb.append(settingStatus);
            sb.append(", is FDN active=");
            sb.append(getFDNSession().isActive());
            mALogger.mo11957d(sb.toString());
        }
        boolean z2 = settingStatus == SettingStatus.ENABLED;
        if (z2) {
            this.mSwitchButton.setChecked(true);
            this.mStatusText.setText(C0504R.string.settings_status_on);
        } else {
            this.mSwitchButton.setChecked(false);
            this.mStatusText.setText(C0504R.string.settings_status_off);
        }
        if (z2 != getFeatureKey().getEnableDefaultState()) {
            getFDNSession().recordChange(getFeatureKey());
        }
    }

    /* access modifiers changed from: protected */
    public void startTutorial() {
        if (tutorialClass() == null) {
            LOGGER.mo11957d("tutorialClass is null");
            return;
        }
        Intent intent = new Intent(getActivity(), tutorialClass());
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("startTutorial(): is FDN active = ");
        sb.append(getFDNSession().isActive());
        mALogger.mo11957d(sb.toString());
        getFDNSession().setFDNExtra(intent);
        startActivity(intent);
    }

    private void setDefaultTryItButtonListener() {
        setTryItButtonListener(this.mDefaultTryItButtonListener);
    }

    /* access modifiers changed from: protected */
    public OnClickListener getDefaultTryItButtonListener() {
        return this.mDefaultTryItButtonListener;
    }

    /* access modifiers changed from: protected */
    public void setTryItButtonListener(OnClickListener onClickListener) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("mTryItButton (old) = ");
        sb.append(String.valueOf(this.mTryItButton));
        mALogger.mo11957d(sb.toString());
        MALogger mALogger2 = LOGGER;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("listener = ");
        sb2.append(String.valueOf(onClickListener));
        mALogger2.mo11957d(sb2.toString());
        this.mTryItButton.setOnClickListener(onClickListener);
    }

    public void setUpdateStatusOnStart(boolean z) {
        this.mUpdateStatusOnStart = z;
    }

    public void setStatusToBeSet(boolean z) {
        this.mStatusToBeSet = z;
    }

    public void dropDownOpened() {
        if (this.mButtonShadow != null) {
            this.mButtonShadow.setVisibility(0);
            this.mTryItButton.setElevation(0.0f);
        }
        if (this.mDropdownShadow != null) {
            this.mDropdownShadow.setVisibility(0);
        }
    }

    public void dropDownClosed() {
        if (this.mButtonShadow != null) {
            this.mButtonShadow.setVisibility(8);
            this.mTryItButton.setElevation(2.0f);
        }
        if (this.mDropdownShadow != null) {
            this.mDropdownShadow.setVisibility(8);
        }
    }

    private void setShadowClickListener() {
        SettingsDetailFragment$$Lambda$3 settingsDetailFragment$$Lambda$3 = new SettingsDetailFragment$$Lambda$3(this);
        if (this.mButtonShadow != null) {
            this.mButtonShadow.setOnClickListener(settingsDetailFragment$$Lambda$3);
        }
        if (this.mDropdownShadow != null) {
            this.mDropdownShadow.setOnClickListener(settingsDetailFragment$$Lambda$3);
        }
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$setShadowClickListener$3$SettingsDetailFragment(View view) {
        if (this.mDropdownContainer != null) {
            this.mDropdownContainer.closeDropdown();
        }
    }

    private void setDropdownInteractionListener() {
        if (this.mDropdownContainer != null) {
            this.mDropdownContainer.setInteractionListener(this);
        }
    }

    /* access modifiers changed from: protected */
    public void setDisplayFamilyColor() {
        if (Device.isNewMoto()) {
            ((Toolbar) getActivity().findViewById(C0504R.C0506id.toolbar)).setBackgroundColor(getActivity().getColor(C0504R.color.wave));
            getActivity().setTheme(C0504R.style.Theme_MotoAppNoBackground_Display);
        }
    }

    public void onPageSelected(int i) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("onPageSelected ");
        sb.append(i);
        mALogger.mo11957d(sb.toString());
        if (this.mVideoListener != null && this.mVideoListener.isPlayingVideo()) {
            this.mVideoListener.stopVideo();
        }
        this.mVideoListener = ((TutorialPage) getVideoDetailPages().get(i)).getVideoListener();
        if (this.mVideoListener != null) {
            this.mVideoListener.startVideo();
        }
    }
}
