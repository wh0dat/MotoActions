package com.motorola.actions.settings.updater;

import android.content.Intent;
import com.google.android.gms.common.util.CrashUtils.ErrorDialogData;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.checkin.CommonCheckinAttributes;
import com.motorola.actions.discovery.DiscoveryManager;
import com.motorola.actions.discovery.highlight.FeatureHighlightKey;
import com.motorola.actions.onenav.OneNavHelper;
import com.motorola.actions.onenav.instrumentation.OneNavInstrumentation;
import com.motorola.actions.p013ui.settings.SettingsActivity;
import com.motorola.actions.p013ui.settings.SettingsDetailActivity;
import com.motorola.actions.reflect.MotorolaSettings;
import com.motorola.actions.settings.provider.ActionsSettingsProvider;
import com.motorola.actions.settings.provider.p012v2.actions.ContainerProviderItemOneNav;
import com.motorola.actions.utils.ActivityUtils;
import com.motorola.actions.utils.MALogger;

public class OneNavSettingsUpdater extends SettingsUpdater {
    private static final MALogger LOGGER = new MALogger(OneNavSettingsUpdater.class);
    private static final String ONE_NAV_ENABLE_SOURCE = "actions_one_nav_enable_source";

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final SettingsUpdater INSTANCE = new OneNavSettingsUpdater();

        private SingletonHolder() {
        }
    }

    /* access modifiers changed from: protected */
    public String getEnableSourceKey() {
        return ONE_NAV_ENABLE_SOURCE;
    }

    public static SettingsUpdater getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public int updateFromMoto(boolean z) {
        int i = 0;
        if (OneNavHelper.shouldShowWelcomeScreen()) {
            Intent intent = new Intent();
            intent.setAction(SettingsActivity.START_SETTING_ACTION);
            intent.setFlags(ErrorDialogData.BINDER_CRASH);
            intent.putExtra(SettingsDetailActivity.KEY_SETTINGS, FeatureKey.ONE_NAV.ordinal());
            ActionsApplication.getAppContext().startActivity(intent);
            ActionsSettingsProvider.notifyChange(ContainerProviderItemOneNav.TABLE_NAME);
            DiscoveryManager.getInstance().cancelFDN(FeatureKey.ONE_NAV);
            DiscoveryManager.getInstance().cancelHighlight(FeatureHighlightKey.SOFTONENAV);
            return 0;
        } else if (z) {
            boolean isSwipeUpConflicted = OneNavHelper.isSwipeUpConflicted();
            boolean z2 = !OneNavHelper.getConflictServicesEnabled().isEmpty();
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("changeStatus - isSwipeUpConflicted = ");
            sb.append(isSwipeUpConflicted);
            sb.append(" , isConflictedServicesEnabled = ");
            sb.append(z2);
            mALogger.mo11957d(sb.toString());
            if (z2 || isSwipeUpConflicted) {
                ActivityUtils.openFragmentForUserAction(FeatureKey.ONE_NAV.ordinal());
            } else {
                LOGGER.mo11957d("changeStatus - No FP gestures services enabled");
                toggleStatus(true, true, CommonCheckinAttributes.KEY_DAILY_ENABLE_SOURCE_TYPE_MOTO_APP);
                i = 1;
            }
            DiscoveryManager.getInstance().cancelFDN(FeatureKey.ONE_NAV);
            DiscoveryManager.getInstance().cancelHighlight(FeatureHighlightKey.SOFTONENAV);
            return i;
        } else {
            toggleStatus(false, true, CommonCheckinAttributes.KEY_DAILY_ENABLE_SOURCE_TYPE_MOTO_APP);
            return 1;
        }
    }

    public void toggleStatus(boolean z, boolean z2, String str) {
        ignoreDiscoveryStatus(FeatureKey.ONE_NAV, z);
        setEnabledSource(z, str);
        updateHighlight(FeatureHighlightKey.SOFTONENAV, z);
        MotorolaSettings.setOneNavEnabled(OneNavHelper.getOneNavStatus(z), z2);
        ActionsSettingsProvider.hideCard(ContainerProviderItemOneNav.PRIORITY_KEY);
        ActionsSettingsProvider.notifyChange(ContainerProviderItemOneNav.TABLE_NAME);
        if (z2) {
            OneNavInstrumentation.recordOneNavFirstEnableEvent(z, str);
        }
    }

    public boolean getDefaultSettingsValue() {
        return FeatureKey.ONE_NAV.getEnableDefaultState();
    }
}
