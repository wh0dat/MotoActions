package com.motorola.actions.settings.provider.p012v2.actions;

import android.content.UriMatcher;
import android.database.MatrixCursor;
import com.motorola.actions.C0504R;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.MultiUserManager;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.discovery.DiscoveryManager;
import com.motorola.actions.microScreen.MicroScreenService;
import com.motorola.actions.settings.provider.ActionsSettingsProviderConstants;
import com.motorola.actions.settings.provider.ActionsSettingsProviderConstants.TableConstants;
import com.motorola.actions.settings.provider.ContainerProviderItem;
import com.motorola.actions.settings.provider.p012v2.LinkAction;
import java.util.List;

/* renamed from: com.motorola.actions.settings.provider.v2.actions.ContainerProviderItemMicroscreen */
public class ContainerProviderItemMicroscreen extends ContainerProviderItem {
    private static final int MATCHER_ID = TableConstants.ACTIONS_CONTAINER_MICROSCREEN.ordinal();
    public static final String PRIORITY_KEY = "card_priority_microscreen";
    public static final String TABLE_NAME = "actionscontainer/microscreen";

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
        int i = (!MicroScreenService.isFeatureSupported() || !MultiUserManager.isSupportedForCurrentUser()) ? 2 : 1;
        boolean isServiceEnabled = MicroScreenService.isServiceEnabled();
        int i2 = SharedPreferenceManager.getInt(PRIORITY_KEY, FeaturePriorityActions.MICROSCREEN.ordinal());
        return fillData(strArr, i, C0504R.string.sh_enabled, C0504R.string.sh_swipe_down_summary, C0504R.C0505drawable.ic_actions_singlehand_swipedown, 1, isServiceEnabled ? 1 : 0, "content://com.motorola.actions.settings.provider/actionscontainer/microscreen", LinkAction.getLinkAction(), DiscoveryManager.getInstance().getDiscoveryStatus(FeatureKey.MICROSCREEN), i2, C0504R.C0505drawable.ic_feature_card_swipe_to_shrink, C0504R.C0505drawable.ic_discovery_icon_microscreen, C0504R.string.moto_discovery_header_microscreen, C0504R.string.moto_discovery_support_microscreen, C0504R.string.moto_discovery_cta_microscreen, C0504R.C0505drawable.ic_moto_icon_microscreen).toArray();
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
