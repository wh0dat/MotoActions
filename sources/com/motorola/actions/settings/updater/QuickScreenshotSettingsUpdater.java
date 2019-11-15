package com.motorola.actions.settings.updater;

import android.content.Intent;
import com.google.android.gms.common.util.CrashUtils.ErrorDialogData;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.checkin.CommonCheckinAttributes;
import com.motorola.actions.discovery.DiscoveryManager;
import com.motorola.actions.p013ui.settings.SettingsActivity;
import com.motorola.actions.p013ui.settings.SettingsDetailActivity;
import com.motorola.actions.quickscreenshot.QuickScreenshotHelper;
import com.motorola.actions.quickscreenshot.QuickScreenshotModel;
import com.motorola.actions.quickscreenshot.service.QuickScreenshotService;
import com.motorola.actions.settings.provider.ActionsSettingsProvider;
import com.motorola.actions.settings.provider.p012v2.actions.ContainerProviderItemQuickScreenshot;

public class QuickScreenshotSettingsUpdater extends SettingsUpdater {
    private static final String QS_ENABLE_SOURCE = "actions_qs_enable_source";
    public static final int SCREENSHOT_DISABLED = 0;
    public static final int SCREENSHOT_ENABLED = 1;

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final SettingsUpdater INSTANCE = new QuickScreenshotSettingsUpdater();

        private SingletonHolder() {
        }
    }

    /* access modifiers changed from: protected */
    public String getEnableSourceKey() {
        return QS_ENABLE_SOURCE;
    }

    public static SettingsUpdater getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public int updateFromMoto(boolean z) {
        if (QuickScreenshotHelper.shouldShowWelcomeScreen()) {
            Intent intent = new Intent();
            intent.setAction(SettingsActivity.START_SETTING_ACTION);
            intent.setFlags(ErrorDialogData.BINDER_CRASH);
            intent.putExtra(SettingsDetailActivity.KEY_SETTINGS, FeatureKey.QUICK_SCREENSHOT.ordinal());
            ActionsApplication.getAppContext().startActivity(intent);
            ActionsSettingsProvider.notifyChange(ContainerProviderItemQuickScreenshot.TABLE_NAME);
            DiscoveryManager.getInstance().cancelFDN(FeatureKey.QUICK_SCREENSHOT);
            return 0;
        }
        toggleStatus(z, true, CommonCheckinAttributes.KEY_DAILY_ENABLE_SOURCE_TYPE_MOTO_APP);
        return 1;
    }

    public void toggleStatus(boolean z, boolean z2, String str) {
        ignoreDiscoveryStatus(FeatureKey.QUICK_SCREENSHOT, z);
        setEnabledSource(z, str);
        QuickScreenshotModel.setServiceEnabled(z);
        if (z) {
            DiscoveryManager.getInstance().cancelFDN(FeatureKey.QUICK_SCREENSHOT);
        }
        ActionsSettingsProvider.hideCard(ContainerProviderItemQuickScreenshot.PRIORITY_KEY);
        ActionsSettingsProvider.notifyChange(ContainerProviderItemQuickScreenshot.TABLE_NAME);
        if (!z) {
            QuickScreenshotService.stopService();
        } else {
            QuickScreenshotService.startService();
        }
    }

    public boolean getDefaultSettingsValue() {
        return FeatureKey.QUICK_SCREENSHOT.getEnableDefaultState();
    }
}
