package com.motorola.actions.settings.provider.p012v2.display;

import android.content.UriMatcher;
import android.database.MatrixCursor;
import com.motorola.actions.C0504R;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.MultiUserManager;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.discovery.DiscoveryManager;
import com.motorola.actions.nightdisplay.NightDisplayService;
import com.motorola.actions.settings.provider.ActionsSettingsProviderConstants;
import com.motorola.actions.settings.provider.ActionsSettingsProviderConstants.TableConstants;
import com.motorola.actions.settings.provider.ContainerProviderItem;
import com.motorola.actions.settings.provider.p012v2.LinkAction;
import java.util.List;

/* renamed from: com.motorola.actions.settings.provider.v2.display.ContainerProviderItemNightDisplay */
public class ContainerProviderItemNightDisplay extends ContainerProviderItem {
    private static final int MATCHER_ID = TableConstants.DISPLAY_CONTAINER_NIGHT_DISPLAY.ordinal();
    public static final String PRIORITY_KEY = "card_priority_night_display";
    public static final String TABLE_NAME = "displaycontainer/night_display";

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

    public static Object[] getItem(String[] strArr) {
        int i = (!NightDisplayService.isFeatureSupported() || !MultiUserManager.isSupportedForCurrentUser()) ? 2 : 1;
        boolean isServiceEnabled = NightDisplayService.isServiceEnabled();
        int i2 = SharedPreferenceManager.getInt(PRIORITY_KEY, FeaturePriorityDisplay.NIGHT_DISPLAY.ordinal());
        return fillData(strArr, i, C0504R.string.night_shade_enabled, C0504R.string.night_display_enabled_summary_feature_list, C0504R.C0505drawable.ic_actions_nightdisplay_list, 1, isServiceEnabled ? 1 : 0, "content://com.motorola.actions.settings.provider/displaycontainer/night_display", LinkAction.getLinkAction(), DiscoveryManager.getInstance().getDiscoveryStatus(FeatureKey.NIGHT_DISPLAY), i2, C0504R.C0505drawable.ic_moto_feature_icon_nd, C0504R.C0505drawable.ic_discovery_icon_nd, C0504R.string.moto_discovery_header_nd, C0504R.string.moto_discovery_support_nd, C0504R.string.moto_discovery_cta_nd, C0504R.C0505drawable.ic_moto_icon_nd).toArray();
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
