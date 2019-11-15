package com.motorola.actions.settings.provider.p012v2.actions;

import android.content.UriMatcher;
import android.database.MatrixCursor;
import com.motorola.actions.C0504R;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.discovery.DiscoveryManager;
import com.motorola.actions.ltu.LiftToUnlockHelper;
import com.motorola.actions.settings.provider.ActionsSettingsProviderConstants;
import com.motorola.actions.settings.provider.ActionsSettingsProviderConstants.TableConstants;
import com.motorola.actions.settings.provider.ContainerProviderItem;
import com.motorola.actions.settings.provider.p012v2.LinkAction;
import java.util.List;

/* renamed from: com.motorola.actions.settings.provider.v2.actions.ContainerProviderItemLiftToUnlock */
public class ContainerProviderItemLiftToUnlock extends ContainerProviderItem {
    private static final int MATCHER_ID = TableConstants.ACTIONS_CONTAINER_LIFT_TO_UNLOCK.ordinal();
    public static final String PRIORITY_KEY = "card_priority_lift_to_unlock";
    public static final String TABLE_NAME = "actionscontainer/lift_to_unlock";

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
        int i = !LiftToUnlockHelper.isFeatureSupported() ? 2 : 1;
        boolean isEnabled = LiftToUnlockHelper.isEnabled();
        int i2 = SharedPreferenceManager.getInt(PRIORITY_KEY, FeaturePriorityActions.LIFT_TO_UNLOCK.ordinal());
        return fillData(strArr, i, C0504R.string.ltu_enabled, C0504R.string.ltu_summary_feature_list, C0504R.C0505drawable.ic_lift_to_unlock, 1, isEnabled ? 1 : 0, "content://com.motorola.actions.settings.provider/actionscontainer/lift_to_unlock", LinkAction.getLinkAction(), DiscoveryManager.getInstance().getDiscoveryStatus(FeatureKey.LIFT_TO_UNLOCK), i2, C0504R.C0505drawable.ic_feature_card_lift_to_unlock, C0504R.C0505drawable.ic_discovery_icon_lift_to_unlock, C0504R.string.moto_discovery_header_ltu, C0504R.string.moto_discovery_support_ltu, C0504R.string.moto_discovery_cta_ltu, C0504R.C0505drawable.ic_moto_icon_ltu).toArray();
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
