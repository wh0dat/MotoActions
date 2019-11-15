package com.motorola.actions.settings.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.settings.provider.ActionsSettingsProviderConstants.TableConstants;
import com.motorola.actions.settings.provider.contract.Column;
import com.motorola.actions.settings.provider.p011v1.ActionsContainerProviderItem;
import com.motorola.actions.settings.provider.p011v1.AttentiveDisplayProviderItem;
import com.motorola.actions.settings.provider.p012v2.actions.ActionsContainerProviderItemSettings;
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
import com.motorola.actions.settings.provider.p012v2.display.DisplayContainerProviderItemSettings;
import com.motorola.actions.settings.updater.ApproachSettingsUpdater;
import com.motorola.actions.settings.updater.AttentiveDisplaySettingsUpdater;
import com.motorola.actions.settings.updater.EnhancedScreenshotSettingsUpdater;
import com.motorola.actions.settings.updater.FOCSettingsUpdater;
import com.motorola.actions.settings.updater.FPSSlideAppSettingsUpdater;
import com.motorola.actions.settings.updater.FPSSlideHomeSettingsUpdater;
import com.motorola.actions.settings.updater.FTMSettingsUpdater;
import com.motorola.actions.settings.updater.LTSSettingsUpdater;
import com.motorola.actions.settings.updater.LiftToUnlockSettingsUpdater;
import com.motorola.actions.settings.updater.MediaControlSettingsUpdater;
import com.motorola.actions.settings.updater.MicroScreenSettingsUpdater;
import com.motorola.actions.settings.updater.NDSettingsUpdater;
import com.motorola.actions.settings.updater.OneNavSettingsUpdater;
import com.motorola.actions.settings.updater.QCSettingsUpdater;
import com.motorola.actions.settings.updater.QuickScreenshotSettingsUpdater;
import com.motorola.actions.utils.MALogger;

public class ActionsSettingsProvider extends ContentProvider {
    private static final String ACTIONS_WRITE_MODE_PERMISSION = "com.motorola.actions.provider.permission.WRITE_MODES";
    private static final int FEATURE_CARD_PRIORITY_HIDE = -1;
    private static final MALogger LOGGER = new MALogger(ActionsSettingsProvider.class);
    private static final UriMatcher URI_MATCHER = new UriMatcher(-1);

    public boolean onCreate() {
        registerURIs();
        return true;
    }

    private void registerURIs() {
        registerURIsV1();
        registerURIsV2AllSettings();
        registerURIsV2EachSetting();
    }

    private void registerURIsV1() {
        ActionsContainerProviderItem.registerUri(URI_MATCHER);
        AttentiveDisplayProviderItem.registerUri(URI_MATCHER);
    }

    private void registerURIsV2AllSettings() {
        ActionsContainerProviderItemSettings.registerUri(URI_MATCHER);
        DisplayContainerProviderItemSettings.registerUri(URI_MATCHER);
    }

    private void registerURIsV2EachSetting() {
        ContainerProviderItemApproach.registerUri(URI_MATCHER);
        ContainerProviderItemFOC.registerUri(URI_MATCHER);
        ContainerProviderItemFTM.registerUri(URI_MATCHER);
        ContainerProviderItemLTS.registerUri(URI_MATCHER);
        ContainerProviderItemMicroscreen.registerUri(URI_MATCHER);
        ContainerProviderItemOneNav.registerUri(URI_MATCHER);
        ContainerProviderItemQC.registerUri(URI_MATCHER);
        ContainerProviderItemQuickScreenshot.registerUri(URI_MATCHER);
        ContainerProviderItemEnhancedScreenshot.registerUri(URI_MATCHER);
        ContainerProviderItemMediaControl.registerUri(URI_MATCHER);
        ContainerProviderItemFPSSlideHome.registerUri(URI_MATCHER);
        ContainerProviderItemFPSSlideApp.registerUri(URI_MATCHER);
        ContainerProviderItemLiftToUnlock.registerUri(URI_MATCHER);
        ContainerProviderItemNightDisplay.registerUri(URI_MATCHER);
        ContainerProviderItemAttentiveDisplay.registerUri(URI_MATCHER);
    }

    public int update(@NonNull Uri uri, ContentValues contentValues, String str, String[] strArr) {
        int i;
        try {
            int match = URI_MATCHER.match(uri);
            if (match < 0) {
                MALogger mALogger = LOGGER;
                StringBuilder sb = new StringBuilder();
                sb.append("Could not match uri ");
                sb.append(uri);
                mALogger.mo11957d(sb.toString());
                return 0;
            }
            MALogger mALogger2 = LOGGER;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Match uri ");
            sb2.append(uri);
            sb2.append(", ");
            sb2.append(match);
            mALogger2.mo11957d(sb2.toString());
            boolean booleanValue = ((Boolean) contentValues.get(Column.ENABLED)).booleanValue();
            TableConstants tableConstants = TableConstants.values()[match];
            MALogger mALogger3 = LOGGER;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Table: ");
            sb3.append(tableConstants);
            mALogger3.mo11957d(sb3.toString());
            i = updateSettings(tableConstants, booleanValue, uri);
            return i;
        } catch (Throwable th) {
            LOGGER.mo11960e("Error handling query", th);
            i = 0;
        }
    }

    public Bundle call(@NonNull String str, String str2, Bundle bundle) {
        LOGGER.mo11957d("call invoked");
        if (ActionsApplication.getAppContext().checkCallingPermission(ACTIONS_WRITE_MODE_PERMISSION) != 0) {
            LOGGER.mo11959e("Call: Client has not appropriate permission");
        } else if ("factory_reset".equals(str)) {
            LOGGER.mo11957d("Resetting MotoActions settings");
            resetAllSettings();
        } else {
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("Call: Unknown operation: ");
            sb.append(str);
            mALogger.mo11959e(sb.toString());
        }
        return null;
    }

    public int delete(@NonNull Uri uri, String str, String[] strArr) {
        LOGGER.mo11959e("Delete called unexpectedly.");
        return 0;
    }

    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {
        LOGGER.mo11959e("Insert called unexpectedly.");
        return null;
    }

    public String getType(@NonNull Uri uri) {
        LOGGER.mo11959e("Get type called unexpectedly.");
        return null;
    }

    public Cursor query(@NonNull Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        MatrixCursor matrixCursor;
        try {
            int match = URI_MATCHER.match(uri);
            if (match < 0) {
                MALogger mALogger = LOGGER;
                StringBuilder sb = new StringBuilder();
                sb.append("Could not match uri ");
                sb.append(uri);
                mALogger.mo11957d(sb.toString());
                return null;
            }
            MALogger mALogger2 = LOGGER;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Match uri ");
            sb2.append(uri);
            sb2.append(", ");
            sb2.append(match);
            mALogger2.mo11957d(sb2.toString());
            TableConstants tableConstants = TableConstants.values()[match];
            MALogger mALogger3 = LOGGER;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Table: ");
            sb3.append(tableConstants);
            mALogger3.mo11957d(sb3.toString());
            matrixCursor = getMatrixCursor(tableConstants, strArr, uri);
            if (matrixCursor != null) {
                matrixCursor.setNotificationUri(ActionsApplication.getAppContext().getContentResolver(), uri);
            }
            MALogger mALogger4 = LOGGER;
            StringBuilder sb4 = new StringBuilder();
            sb4.append("matrixCursor = ");
            sb4.append(matrixCursor);
            mALogger4.mo11957d(sb4.toString());
            return matrixCursor;
        } catch (Throwable th) {
            LOGGER.mo11960e("Error handling query", th);
            matrixCursor = null;
        }
    }

    public static void notifyAdObservers() {
        LOGGER.mo11957d("Notify AD Observers");
        ActionsApplication.getAppContext().getContentResolver().notifyChange(AttentiveDisplayProviderItem.URI, null);
        notifyChange(ContainerProviderItemAttentiveDisplay.TABLE_NAME);
    }

    public static void notifyChange(String str) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("Notify observer for ");
        sb.append(str);
        mALogger.mo11957d(sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append("content://com.motorola.actions.settings.provider/");
        sb2.append(str);
        ActionsApplication.getAppContext().getContentResolver().notifyChange(Uri.parse(sb2.toString()), null);
    }

    public static void notifyChange(FeatureKey featureKey) {
        notifyChange(getTableNameByFeature(featureKey));
    }

    public static void hideCard(String str) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("Hide card:");
        sb.append(str);
        mALogger.mo11957d(sb.toString());
        SharedPreferenceManager.putInt(str, -1);
    }

    private void resetAllSettings() {
        FOCSettingsUpdater.getInstance().resetToDefault();
        FTMSettingsUpdater.getInstance().resetToDefault();
        QCSettingsUpdater.getInstance().resetToDefault();
        OneNavSettingsUpdater.getInstance().resetToDefault();
        MicroScreenSettingsUpdater.getInstance().resetToDefault();
        LTSSettingsUpdater.getInstance().resetToDefault();
        ApproachSettingsUpdater.getInstance().resetToDefault();
        NDSettingsUpdater.getInstance().resetToDefault();
        AttentiveDisplaySettingsUpdater.getInstance().resetToDefault();
        QuickScreenshotSettingsUpdater.getInstance().resetToDefault();
        EnhancedScreenshotSettingsUpdater.getInstance().resetToDefault();
        MediaControlSettingsUpdater.getInstance().resetToDefault();
        FPSSlideHomeSettingsUpdater.getInstance().resetToDefault();
        FPSSlideAppSettingsUpdater.getInstance().resetToDefault();
        LiftToUnlockSettingsUpdater.getInstance().resetToDefault();
    }

    private static String getTableNameByFeature(FeatureKey featureKey) {
        switch (featureKey) {
            case QUICK_CAPTURE:
                return ContainerProviderItemQC.TABLE_NAME;
            case APPROACH:
                return ContainerProviderItemApproach.TABLE_NAME;
            case FLASH_ON_CHOP:
                return ContainerProviderItemFOC.TABLE_NAME;
            case ATTENTIVE_DISPLAY:
                return ContainerProviderItemAttentiveDisplay.TABLE_NAME;
            case MICROSCREEN:
                return ContainerProviderItemMicroscreen.TABLE_NAME;
            case PICKUP_TO_STOP_RINGING:
                return ContainerProviderItemLTS.TABLE_NAME;
            case FLIP_TO_DND:
                return ContainerProviderItemFTM.TABLE_NAME;
            case NIGHT_DISPLAY:
                return ContainerProviderItemNightDisplay.TABLE_NAME;
            case ONE_NAV:
                return ContainerProviderItemOneNav.TABLE_NAME;
            case QUICK_SCREENSHOT:
                return ContainerProviderItemQuickScreenshot.TABLE_NAME;
            case ENHANCED_SCREENSHOT:
                return ContainerProviderItemEnhancedScreenshot.TABLE_NAME;
            case MEDIA_CONTROL:
                return ContainerProviderItemMediaControl.TABLE_NAME;
            case FPS_SLIDE_HOME:
                return ContainerProviderItemFPSSlideHome.TABLE_NAME;
            case FPS_SLIDE_APP:
                return ContainerProviderItemFPSSlideApp.TABLE_NAME;
            case LIFT_TO_UNLOCK:
                return ContainerProviderItemLiftToUnlock.TABLE_NAME;
            default:
                String str = "";
                LOGGER.mo11957d("Not valid feature option.");
                return str;
        }
    }

    private int updateSettings(TableConstants tableConstants, boolean z, Uri uri) {
        switch (tableConstants) {
            case ACTIONS_CONTAINER_ID:
                LOGGER.mo11957d("Update not available");
                break;
            case ATTENTIVE_DISPLAY_ID:
                LOGGER.mo11957d("Update not available");
                break;
            case ACTIONS_CONTAINER_ALL_SETTINGS:
                LOGGER.mo11957d("Update not available");
                break;
            case DISPLAY_CONTAINER_ALL_SETTINGS:
                LOGGER.mo11957d("Update not available");
                break;
            case ACTIONS_CONTAINER_FOC:
                return FOCSettingsUpdater.getInstance().updateFromMoto(z);
            case ACTIONS_CONTAINER_FTM:
                return FTMSettingsUpdater.getInstance().updateFromMoto(z);
            case ACTIONS_CONTAINER_QC:
                return QCSettingsUpdater.getInstance().updateFromMoto(z);
            case ACTIONS_CONTAINER_ONENAV:
                return OneNavSettingsUpdater.getInstance().updateFromMoto(z);
            case ACTIONS_CONTAINER_MICROSCREEN:
                return MicroScreenSettingsUpdater.getInstance().updateFromMoto(z);
            case ACTIONS_CONTAINER_LTS:
                return LTSSettingsUpdater.getInstance().updateFromMoto(z);
            case ACTIONS_CONTAINER_APPROACH:
                return ApproachSettingsUpdater.getInstance().updateFromMoto(z);
            case ACTIONS_CONTAINER_SCREENSHOT:
                return QuickScreenshotSettingsUpdater.getInstance().updateFromMoto(z);
            case ACTIONS_CONTAINER_ENHANCED_SCREENSHOT:
                return EnhancedScreenshotSettingsUpdater.getInstance().updateFromMoto(z);
            case ACTIONS_CONTAINER_MEDIA_CONTROL:
                return MediaControlSettingsUpdater.getInstance().updateFromMoto(z);
            case ACTIONS_CONTAINER_FPS_SLIDE_HOME:
                return FPSSlideHomeSettingsUpdater.getInstance().updateFromMoto(z);
            case ACTIONS_CONTAINER_FPS_SLIDE_APP:
                return FPSSlideAppSettingsUpdater.getInstance().updateFromMoto(z);
            case ACTIONS_CONTAINER_LIFT_TO_UNLOCK:
                return LiftToUnlockSettingsUpdater.getInstance().updateFromMoto(z);
            case DISPLAY_CONTAINER_NIGHT_DISPLAY:
                return NDSettingsUpdater.getInstance().updateFromMoto(z);
            case DISPLAY_CONTAINER_ATTENTIVE_DISPLAY:
                return AttentiveDisplaySettingsUpdater.getInstance().updateFromMoto(z);
            default:
                MALogger mALogger = LOGGER;
                StringBuilder sb = new StringBuilder();
                sb.append("throw IllegalArgumentException - Unknown URI: ");
                sb.append(uri);
                mALogger.mo11957d(sb.toString());
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Unknown URI: ");
                sb2.append(uri);
                throw new IllegalArgumentException(sb2.toString());
        }
        return 0;
    }

    private MatrixCursor getMatrixCursor(TableConstants tableConstants, String[] strArr, Uri uri) {
        switch (tableConstants) {
            case ACTIONS_CONTAINER_ID:
                return new ActionsContainerProviderItem().query(strArr);
            case ATTENTIVE_DISPLAY_ID:
                return new AttentiveDisplayProviderItem().query(strArr);
            case ACTIONS_CONTAINER_ALL_SETTINGS:
                return new ActionsContainerProviderItemSettings().query(strArr);
            case DISPLAY_CONTAINER_ALL_SETTINGS:
                return new DisplayContainerProviderItemSettings().query(strArr);
            case ACTIONS_CONTAINER_FOC:
                return new ContainerProviderItemFOC().query(strArr);
            case ACTIONS_CONTAINER_FTM:
                return new ContainerProviderItemFTM().query(strArr);
            case ACTIONS_CONTAINER_QC:
                return new ContainerProviderItemQC().query(strArr);
            case ACTIONS_CONTAINER_ONENAV:
                return new ContainerProviderItemOneNav().query(strArr);
            case ACTIONS_CONTAINER_MICROSCREEN:
                return new ContainerProviderItemMicroscreen().query(strArr);
            case ACTIONS_CONTAINER_LTS:
                return new ContainerProviderItemLTS().query(strArr);
            case ACTIONS_CONTAINER_APPROACH:
                return new ContainerProviderItemApproach().query(strArr);
            case ACTIONS_CONTAINER_SCREENSHOT:
                return new ContainerProviderItemQuickScreenshot().query(strArr);
            case ACTIONS_CONTAINER_ENHANCED_SCREENSHOT:
                return new ContainerProviderItemEnhancedScreenshot().query(strArr);
            case ACTIONS_CONTAINER_MEDIA_CONTROL:
                return new ContainerProviderItemMediaControl().query(strArr);
            case ACTIONS_CONTAINER_FPS_SLIDE_HOME:
                return new ContainerProviderItemFPSSlideHome().query(strArr);
            case ACTIONS_CONTAINER_FPS_SLIDE_APP:
                return new ContainerProviderItemFPSSlideApp().query(strArr);
            case ACTIONS_CONTAINER_LIFT_TO_UNLOCK:
                return new ContainerProviderItemLiftToUnlock().query(strArr);
            case DISPLAY_CONTAINER_NIGHT_DISPLAY:
                return new ContainerProviderItemNightDisplay().query(strArr);
            case DISPLAY_CONTAINER_ATTENTIVE_DISPLAY:
                return new ContainerProviderItemAttentiveDisplay().query(strArr);
            default:
                MALogger mALogger = LOGGER;
                StringBuilder sb = new StringBuilder();
                sb.append("throw IllegalArgumentException - Unknown URI: ");
                sb.append(uri);
                mALogger.mo11957d(sb.toString());
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Unknown URI: ");
                sb2.append(uri);
                throw new IllegalArgumentException(sb2.toString());
        }
    }
}
