package com.motorola.actions.p013ui.settings;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.BootReceiver;
import com.motorola.actions.C0504R;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.ServiceStartReason;
import com.motorola.actions.approach.p005ir.IRService;
import com.motorola.actions.approach.p006us.USService;
import com.motorola.actions.attentivedisplay.AttentiveDisplayService;
import com.motorola.actions.foc.gesture.service.FlashOnChopService;
import com.motorola.actions.ftm.FlipToMuteService;
import com.motorola.actions.lts.LiftToSilenceService;
import com.motorola.actions.microScreen.MicroScreenService;
import com.motorola.actions.nightdisplay.NightDisplayService;
import com.motorola.actions.onenav.OneNavHelper;
import com.motorola.actions.p010qc.QuickCaptureConfig;
import java.util.ArrayList;
import java.util.List;

/* renamed from: com.motorola.actions.ui.settings.SettingsActivity */
public class SettingsActivity extends BaseSettingsActivity {
    public static final String START_SETTING_ACTION = "com.motorola.actions.ui.settings.START";

    /* renamed from: com.motorola.actions.ui.settings.SettingsActivity$MenuArrayAdapter */
    private static class MenuArrayAdapter extends ArrayAdapter<MenuContent> {

        /* renamed from: com.motorola.actions.ui.settings.SettingsActivity$MenuArrayAdapter$ViewHolder */
        private static class ViewHolder {
            TextView mDetailText;
            ImageView mIcon;
            TextView mTitle;

            private ViewHolder() {
            }
        }

        MenuArrayAdapter(Context context, int i, List<MenuContent> list) {
            super(context, i, list);
        }

        @NonNull
        public View getView(int i, View view, @NonNull ViewGroup viewGroup) {
            View view2;
            ViewHolder viewHolder;
            if (view == null) {
                viewHolder = new ViewHolder();
                view2 = ((Activity) getContext()).getLayoutInflater().inflate(C0504R.layout.menu_card, viewGroup, false);
                viewHolder.mIcon = (ImageView) view2.findViewById(C0504R.C0506id.icon);
                viewHolder.mTitle = (TextView) view2.findViewById(C0504R.C0506id.title);
                viewHolder.mDetailText = (TextView) view2.findViewById(C0504R.C0506id.detail_text);
                view2.setTag(viewHolder);
            } else {
                view2 = view;
                viewHolder = (ViewHolder) view.getTag();
            }
            MenuContent menuContent = (MenuContent) getItem(i);
            if (menuContent != null) {
                viewHolder.mIcon.setImageResource(menuContent.mIconId);
                viewHolder.mTitle.setText(menuContent.mTitleId);
                viewHolder.mDetailText.setText(menuContent.mDetailTextId);
            }
            return view2;
        }
    }

    /* renamed from: com.motorola.actions.ui.settings.SettingsActivity$MenuContent */
    private static class MenuContent {
        public MenuContentListener listener;
        int mDetailTextId;
        int mIconId;
        int mTitleId;

        /* renamed from: com.motorola.actions.ui.settings.SettingsActivity$MenuContent$MenuContentListener */
        private interface MenuContentListener {
            void onMenuItemClick();
        }

        private MenuContent() {
        }
    }

    /* renamed from: com.motorola.actions.ui.settings.SettingsActivity$SettingsFragment */
    public static class SettingsFragment extends Fragment implements OnItemClickListener {
        private ArrayAdapter<MenuContent> mArrayAdapter;

        public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
            this.mArrayAdapter = new MenuArrayAdapter(getActivity(), C0504R.layout.menu_card, new ArrayList());
            ListView listView = (ListView) layoutInflater.inflate(C0504R.layout.fragment_menu, viewGroup, false);
            listView.setDivider(null);
            listView.setDividerHeight(0);
            listView.setOnItemClickListener(this);
            listView.setAdapter(this.mArrayAdapter);
            updateContent();
            return listView;
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
            MenuContent menuContent = (MenuContent) this.mArrayAdapter.getItem(i);
            if (menuContent != null) {
                menuContent.listener.onMenuItemClick();
            }
        }

        private MenuContent getQuickDrawCard() {
            if (!QuickCaptureConfig.isSupported()) {
                return null;
            }
            MenuContent menuContent = new MenuContent();
            menuContent.mIconId = C0504R.C0505drawable.ic_actions_twist;
            menuContent.mTitleId = C0504R.string.camera_qd_enabled;
            menuContent.mDetailTextId = C0504R.string.camera_qd_enabled_summary;
            menuContent.listener = new SettingsActivity$SettingsFragment$$Lambda$0(this);
            return menuContent;
        }

        /* access modifiers changed from: 0000 */
        public final /* synthetic */ void lambda$getQuickDrawCard$0$SettingsActivity$SettingsFragment() {
            SettingsActivity.startFeatureSettingActivity(SettingsActivity.getFeatureLaunchIntent(FeatureKey.QUICK_CAPTURE.ordinal()), getActivity());
        }

        private MenuContent getApproachCard() {
            if (!isApproachSupported()) {
                return null;
            }
            MenuContent menuContent = new MenuContent();
            menuContent.mIconId = C0504R.C0505drawable.ic_actions_reach;
            menuContent.mTitleId = C0504R.string.ir_aod_approach_enabled;
            menuContent.mDetailTextId = C0504R.string.ir_aod_approach_enabled_summary;
            menuContent.listener = new SettingsActivity$SettingsFragment$$Lambda$1(this);
            return menuContent;
        }

        /* access modifiers changed from: 0000 */
        public final /* synthetic */ void lambda$getApproachCard$1$SettingsActivity$SettingsFragment() {
            SettingsActivity.startFeatureSettingActivity(SettingsActivity.getFeatureLaunchIntent(FeatureKey.APPROACH.ordinal()), getActivity());
        }

        private MenuContent getFlashOnChopCard() {
            if (!FlashOnChopService.isFeatureSupported(ActionsApplication.getAppContext())) {
                return null;
            }
            MenuContent menuContent = new MenuContent();
            menuContent.mIconId = C0504R.C0505drawable.ic_actions_chop;
            menuContent.mTitleId = C0504R.string.foc_enabled;
            menuContent.mDetailTextId = C0504R.string.foc_summary;
            menuContent.listener = new SettingsActivity$SettingsFragment$$Lambda$2(this);
            return menuContent;
        }

        /* access modifiers changed from: 0000 */
        public final /* synthetic */ void lambda$getFlashOnChopCard$2$SettingsActivity$SettingsFragment() {
            SettingsActivity.startFeatureSettingActivity(SettingsActivity.getFeatureLaunchIntent(FeatureKey.FLASH_ON_CHOP.ordinal()), getActivity());
        }

        private MenuContent getMicroScreenCard() {
            if (!MicroScreenService.isFeatureSupported()) {
                return null;
            }
            MenuContent menuContent = new MenuContent();
            menuContent.mIconId = C0504R.C0505drawable.ic_actions_singlehand_swipedown;
            menuContent.mTitleId = C0504R.string.sh_enabled;
            menuContent.mDetailTextId = C0504R.string.sh_swipe_down_checkbox_summary;
            menuContent.listener = new SettingsActivity$SettingsFragment$$Lambda$3(this);
            return menuContent;
        }

        /* access modifiers changed from: 0000 */
        public final /* synthetic */ void lambda$getMicroScreenCard$3$SettingsActivity$SettingsFragment() {
            SettingsActivity.startFeatureSettingActivity(SettingsActivity.getFeatureLaunchIntent(FeatureKey.MICROSCREEN.ordinal()), getActivity());
        }

        private MenuContent getAttentiveDisplayCard() {
            if (!AttentiveDisplayService.isFeatureSupported(ActionsApplication.getAppContext())) {
                return null;
            }
            MenuContent menuContent = new MenuContent();
            menuContent.mIconId = C0504R.C0505drawable.ic_actions_attentivedisplay_list;
            menuContent.mTitleId = C0504R.string.ad_enabled;
            menuContent.mDetailTextId = C0504R.string.ad_summary_listview;
            menuContent.listener = new SettingsActivity$SettingsFragment$$Lambda$4(this);
            return menuContent;
        }

        /* access modifiers changed from: 0000 */
        /* renamed from: lambda$getAttentiveDisplayCard$4$SettingsActivity$SettingsFragment */
        public final /* synthetic */ void mo11650xd3ca72ce() {
            SettingsActivity.startFeatureSettingActivity(SettingsActivity.getFeatureLaunchIntent(FeatureKey.ATTENTIVE_DISPLAY.ordinal()), getActivity());
        }

        private MenuContent getLiftToSilenceCard() {
            if (!LiftToSilenceService.isFeatureSupported()) {
                return null;
            }
            MenuContent menuContent = new MenuContent();
            menuContent.mIconId = C0504R.C0505drawable.ic_actions_lifttosilence_list;
            menuContent.mTitleId = C0504R.string.lts_enabled;
            menuContent.mDetailTextId = C0504R.string.lts_checkbox_summary;
            menuContent.listener = new SettingsActivity$SettingsFragment$$Lambda$5(this);
            return menuContent;
        }

        /* access modifiers changed from: 0000 */
        public final /* synthetic */ void lambda$getLiftToSilenceCard$5$SettingsActivity$SettingsFragment() {
            SettingsActivity.startFeatureSettingActivity(SettingsActivity.getFeatureLaunchIntent(FeatureKey.PICKUP_TO_STOP_RINGING.ordinal()), getActivity());
        }

        private MenuContent getFlipToMuteCard() {
            if (!FlipToMuteService.isFeatureSupported()) {
                return null;
            }
            MenuContent menuContent = new MenuContent();
            menuContent.mIconId = C0504R.C0505drawable.ic_actions_flip;
            menuContent.mTitleId = C0504R.string.ftm_enabled;
            menuContent.mDetailTextId = C0504R.string.ftm_enabled_summary;
            menuContent.listener = new SettingsActivity$SettingsFragment$$Lambda$6(this);
            return menuContent;
        }

        /* access modifiers changed from: 0000 */
        public final /* synthetic */ void lambda$getFlipToMuteCard$6$SettingsActivity$SettingsFragment() {
            SettingsActivity.startFeatureSettingActivity(SettingsActivity.getFeatureLaunchIntent(FeatureKey.FLIP_TO_DND.ordinal()), getActivity());
        }

        private MenuContent getNightShadeCard() {
            if (!NightDisplayService.isFeatureSupported()) {
                return null;
            }
            MenuContent menuContent = new MenuContent();
            menuContent.mIconId = C0504R.C0505drawable.ic_actions_nightdisplay_list;
            menuContent.mTitleId = C0504R.string.night_shade_enabled;
            menuContent.mDetailTextId = C0504R.string.night_shade_enabled_summary;
            menuContent.listener = new SettingsActivity$SettingsFragment$$Lambda$7(this);
            return menuContent;
        }

        /* access modifiers changed from: 0000 */
        public final /* synthetic */ void lambda$getNightShadeCard$7$SettingsActivity$SettingsFragment() {
            SettingsActivity.startFeatureSettingActivity(SettingsActivity.getFeatureLaunchIntent(FeatureKey.NIGHT_DISPLAY.ordinal()), getActivity());
        }

        private MenuContent getOneNavCard() {
            if (!OneNavHelper.isOneNavPresent() || !OneNavHelper.isMotorolaPermissionGranted()) {
                return null;
            }
            MenuContent menuContent = new MenuContent();
            menuContent.mIconId = C0504R.C0505drawable.moto_actions_one_nav;
            menuContent.mTitleId = C0504R.string.onenav_enabled;
            menuContent.mDetailTextId = C0504R.string.onenav_summary;
            menuContent.listener = new SettingsActivity$SettingsFragment$$Lambda$8(this);
            return menuContent;
        }

        /* access modifiers changed from: 0000 */
        public final /* synthetic */ void lambda$getOneNavCard$8$SettingsActivity$SettingsFragment() {
            SettingsActivity.startFeatureSettingActivity(SettingsActivity.getFeatureLaunchIntent(FeatureKey.ONE_NAV.ordinal()), getActivity());
        }

        private boolean isApproachSupported() {
            if (((SettingsActivity) getActivity()) == null) {
                return false;
            }
            if (IRService.isIRSupported() || USService.isUSSupported()) {
                return true;
            }
            return false;
        }

        private void updateContent() {
            if (this.mArrayAdapter != null && isAdded()) {
                MenuContent[] menuContentArr = {getOneNavCard(), getFlashOnChopCard(), getQuickDrawCard(), getMicroScreenCard(), getApproachCard(), getAttentiveDisplayCard(), getNightShadeCard(), getLiftToSilenceCard(), getFlipToMuteCard()};
                this.mArrayAdapter.clear();
                for (MenuContent addCard : menuContentArr) {
                    addCard(addCard);
                }
            }
        }

        private void addCard(MenuContent menuContent) {
            if (menuContent != null) {
                this.mArrayAdapter.add(menuContent);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C0504R.layout.activity_settings);
        setupToolbar(C0504R.string.app_name);
        getFragmentManager().beginTransaction().add(C0504R.C0506id.layout_menu, new SettingsFragment()).commit();
        BootReceiver.startAllFeatures(this, ServiceStartReason.ENTER_SETTINGS);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
    }

    public boolean onNavigateUp() {
        finish();
        return true;
    }

    public void finish() {
        super.finish();
        overridePendingTransition(C0504R.anim.splash_slide_in_anim_reverse_set, C0504R.anim.splash_slide_out_anim_reverse_set);
    }

    public void onBackPressed() {
        super.onBackPressed();
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        }
    }

    public static Intent getFeatureLaunchIntent(int i) {
        Intent intent = new Intent();
        intent.setAction(START_SETTING_ACTION);
        intent.putExtra(SettingsDetailActivity.KEY_SETTINGS, i);
        intent.setClass(ActionsApplication.getAppContext(), SettingsDetailActivity.class);
        intent.setPackage(ActionsApplication.getAppContext().getPackageName());
        intent.setFlags(337772544);
        return intent;
    }

    /* access modifiers changed from: private */
    public static void startFeatureSettingActivity(Intent intent, Activity activity) {
        activity.startActivity(intent);
        activity.overridePendingTransition(C0504R.anim.splash_slide_in_anim_set, C0504R.anim.splash_slide_out_anim_set);
    }
}
