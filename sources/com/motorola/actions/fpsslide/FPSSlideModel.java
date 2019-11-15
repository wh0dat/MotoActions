package com.motorola.actions.fpsslide;

import com.motorola.actions.reflect.MotorolaSettings;
import com.motorola.actions.settings.provider.ActionsSettingsProvider;
import com.motorola.actions.settings.provider.p012v2.actions.ContainerProviderItemFPSSlideApp;
import com.motorola.actions.settings.provider.p012v2.actions.ContainerProviderItemFPSSlideHome;
import com.motorola.actions.utils.MALogger;

public class FPSSlideModel {
    private static final MALogger LOGGER = new MALogger(FPSSlideModel.class);

    public static void setServiceEnabled(boolean z) {
        MotorolaSettings.setFPSSlideEnabled(z, false);
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("setServiceEnabled = ");
        sb.append(z);
        mALogger.mo11957d(sb.toString());
    }

    public static boolean isServiceEnabled() {
        boolean isFPSSlideEnabled = MotorolaSettings.isFPSSlideEnabled();
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("isServiceEnabled = ");
        sb.append(isFPSSlideEnabled);
        mALogger.mo11957d(sb.toString());
        return isFPSSlideEnabled;
    }

    public static void setReversedScroll(boolean z) {
        MotorolaSettings.setFPSSlideReversed(z);
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("setReversedScroll = ");
        sb.append(z);
        mALogger.mo11957d(sb.toString());
    }

    public static boolean isReversedScroll() {
        boolean isFPSSlideReversed = MotorolaSettings.isFPSSlideReversed();
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("isReversedScroll = ");
        sb.append(isFPSSlideReversed);
        mALogger.mo11957d(sb.toString());
        return isFPSSlideReversed;
    }

    public static void setScrollOnHome(boolean z) {
        MotorolaSettings.setFPSSlideOnHomeEnabled(z);
        ActionsSettingsProvider.notifyChange(ContainerProviderItemFPSSlideHome.TABLE_NAME);
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("setScrollOnHome = ");
        sb.append(z);
        mALogger.mo11957d(sb.toString());
    }

    public static boolean isScrollOnHomeEnabled() {
        boolean isFPSSlideOnHomeEnabled = MotorolaSettings.isFPSSlideOnHomeEnabled();
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("isScrollOnHomeEnabled = ");
        sb.append(isFPSSlideOnHomeEnabled);
        mALogger.mo11957d(sb.toString());
        return isFPSSlideOnHomeEnabled;
    }

    public static void setScrollOnApp(boolean z) {
        MotorolaSettings.setFPSSlideOnAppEnabled(z);
        ActionsSettingsProvider.notifyChange(ContainerProviderItemFPSSlideApp.TABLE_NAME);
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("setScrollOnApp = ");
        sb.append(z);
        mALogger.mo11957d(sb.toString());
    }

    public static boolean isScrollOnAppEnabled() {
        boolean isFPSSlideOnAppEnabled = MotorolaSettings.isFPSSlideOnAppEnabled();
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("isScrollOnAppEnabled = ");
        sb.append(isFPSSlideOnAppEnabled);
        mALogger.mo11957d(sb.toString());
        return isFPSSlideOnAppEnabled;
    }
}
