package com.motorola.actions.settings.provider.p012v2.actions;

import android.content.UriMatcher;
import android.database.MatrixCursor;
import com.motorola.actions.C0504R;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.discovery.DiscoveryManager;
import com.motorola.actions.mediacontrol.MediaControlModel;
import com.motorola.actions.mediacontrol.MediaControlService;
import com.motorola.actions.settings.provider.ActionsSettingsProviderConstants;
import com.motorola.actions.settings.provider.ActionsSettingsProviderConstants.TableConstants;
import com.motorola.actions.settings.provider.ContainerProviderItem;
import com.motorola.actions.settings.provider.p012v2.LinkAction;
import java.util.List;

/* renamed from: com.motorola.actions.settings.provider.v2.actions.ContainerProviderItemMediaControl */
public class ContainerProviderItemMediaControl extends ContainerProviderItem {
    private static final int MATCHER_ID = TableConstants.ACTIONS_CONTAINER_MEDIA_CONTROL.ordinal();
    public static final String PRIORITY_KEY = "card_priority_media_control";
    public static final String TABLE_NAME = "actionscontainer/media_control";

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
        int i = !MediaControlService.isFeatureSupported() ? 2 : 1;
        boolean isServiceEnabled = MediaControlModel.isServiceEnabled();
        int i2 = SharedPreferenceManager.getInt(PRIORITY_KEY, FeaturePriorityActions.MEDIA_CONTROL.ordinal());
        return fillData(strArr, i, C0504R.string.media_control_enabled, C0504R.string.media_control_summary_feature_list, C0504R.C0505drawable.ic_media_control, 1, isServiceEnabled ? 1 : 0, "content://com.motorola.actions.settings.provider/actionscontainer/media_control", LinkAction.getLinkAction(), DiscoveryManager.getInstance().getDiscoveryStatus(FeatureKey.MEDIA_CONTROL), i2, C0504R.C0505drawable.ic_feature_card_media_controls, C0504R.C0505drawable.ic_discovery_icon_mc, C0504R.string.moto_discovery_header_mc, C0504R.string.moto_discovery_support_mc, C0504R.string.moto_discovery_cta_mc, C0504R.C0505drawable.ic_moto_icon_media_control).toArray();
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
