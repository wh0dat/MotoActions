package com.motorola.actions.settings.provider.p012v2.actions;

import android.content.UriMatcher;
import android.database.MatrixCursor;
import com.motorola.actions.C0504R;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.discovery.DiscoveryManager;
import com.motorola.actions.enhancedscreenshot.EnhancedScreenshotModel;
import com.motorola.actions.enhancedscreenshot.EnhancedScreenshotService;
import com.motorola.actions.settings.provider.ActionsSettingsProviderConstants;
import com.motorola.actions.settings.provider.ActionsSettingsProviderConstants.TableConstants;
import com.motorola.actions.settings.provider.ContainerProviderItem;
import com.motorola.actions.settings.provider.p012v2.LinkAction;
import java.util.List;

/* renamed from: com.motorola.actions.settings.provider.v2.actions.ContainerProviderItemEnhancedScreenshot */
public class ContainerProviderItemEnhancedScreenshot extends ContainerProviderItem {
    private static final int MATCHER_ID = TableConstants.ACTIONS_CONTAINER_ENHANCED_SCREENSHOT.ordinal();
    public static final String PRIORITY_KEY = "card_priority_enhanced_screenshot";
    public static final String TABLE_NAME = "actionscontainer/enhanced_screenshot";

    public static void registerUri(UriMatcher uriMatcher) {
        uriMatcher.addURI(ActionsSettingsProviderConstants.AUTHORITY_NAME, TABLE_NAME, MATCHER_ID);
    }

    private static MatrixCursor getCursorActionsContainer(String[] strArr) {
        if (strArr == null || strArr.length == 0) {
            strArr = (String[]) ActionsSettingsProviderConstants.SETTINGS_COLUMNS_V2.toArray();
        }
        MatrixCursor matrixCursor = new MatrixCursor(strArr);
        addRow(matrixCursor, getItem(strArr));
        return matrixCursor;
    }

    static Object[] getItem(String[] strArr) {
        int i = !EnhancedScreenshotService.isFeatureSupported() ? 2 : 1;
        boolean isServiceEnabled = EnhancedScreenshotModel.isServiceEnabled();
        int i2 = SharedPreferenceManager.getInt(PRIORITY_KEY, FeaturePriorityActions.ENHANCED_SREENSHOT.ordinal());
        return fillData(strArr, i, C0504R.string.enhanced_screenshot_enabled, C0504R.string.enhanced_screenshot_summary, C0504R.C0505drawable.ic_actions_enhanced_screenshot, 1, isServiceEnabled ? 1 : 0, "content://com.motorola.actions.settings.provider/actionscontainer/enhanced_screenshot", LinkAction.getLinkAction(), DiscoveryManager.getInstance().getDiscoveryStatus(FeatureKey.ENHANCED_SCREENSHOT), i2, C0504R.C0505drawable.ic_feature_card_screenshot_editor, C0504R.C0505drawable.ic_discovery_icon_es, C0504R.string.moto_discovery_header_es, C0504R.string.moto_discovery_support_es, C0504R.string.moto_discovery_cta_es, C0504R.C0505drawable.ic_moto_icon_enhanced_screenshot).toArray();
    }

    /* access modifiers changed from: protected */
    public List<String> getColumns() {
        return ActionsSettingsProviderConstants.SETTINGS_COLUMNS_V2;
    }

    /* access modifiers changed from: protected */
    public MatrixCursor getMatrixCursor(String[] strArr) {
        return getCursorActionsContainer(strArr);
    }
}
