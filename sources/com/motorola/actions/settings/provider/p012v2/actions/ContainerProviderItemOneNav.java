package com.motorola.actions.settings.provider.p012v2.actions;

import android.content.UriMatcher;
import android.database.MatrixCursor;
import com.motorola.actions.C0504R;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.MultiUserManager;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.discovery.DiscoveryManager;
import com.motorola.actions.onenav.OneNavHelper;
import com.motorola.actions.settings.provider.ActionsSettingsProviderConstants;
import com.motorola.actions.settings.provider.ActionsSettingsProviderConstants.TableConstants;
import com.motorola.actions.settings.provider.ContainerProviderItem;
import com.motorola.actions.settings.provider.p012v2.LinkAction;
import java.util.List;

/* renamed from: com.motorola.actions.settings.provider.v2.actions.ContainerProviderItemOneNav */
public class ContainerProviderItemOneNav extends ContainerProviderItem {
    private static final int MATCHER_ID = TableConstants.ACTIONS_CONTAINER_ONENAV.ordinal();
    public static final String PRIORITY_KEY = "card_priority_one_nav";
    public static final String TABLE_NAME = "actionscontainer/one_nav";

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
        int i = 1;
        int i2 = (!OneNavHelper.isOneNavPresent() || !MultiUserManager.isSupportedForCurrentUser()) ? 2 : 1;
        if (!OneNavHelper.isOneNavEnabled() || !OneNavHelper.getConflictServicesEnabled().isEmpty()) {
            i = 0;
        }
        return fillData(strArr, i2, C0504R.string.onenav_enabled, OneNavHelper.getResourceForType(C0504R.string.onenav_summary, C0504R.string.onenav_summary_soft), OneNavHelper.getResourceForType(C0504R.C0505drawable.moto_actions_one_nav, C0504R.C0505drawable.ic_moto_actions_one_nav_soft), 1, i, "content://com.motorola.actions.settings.provider/actionscontainer/one_nav", LinkAction.getLinkAction(), DiscoveryManager.getInstance().getDiscoveryStatus(FeatureKey.ONE_NAV), SharedPreferenceManager.getInt(PRIORITY_KEY, FeaturePriorityActions.ONE_NAV.ordinal()), C0504R.C0505drawable.ic_feature_card_one_button_nav, C0504R.C0505drawable.ic_discovery_icon_onenav, C0504R.string.moto_discovery_header_onenav, C0504R.string.moto_discovery_support_onenav, C0504R.string.moto_discovery_cta_onenav, C0504R.C0505drawable.ic_moto_icon_onenav).toArray();
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
