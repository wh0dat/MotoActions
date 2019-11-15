package com.motorola.actions.settings.provider.p012v2.actions;

import android.content.UriMatcher;
import android.database.MatrixCursor;
import com.motorola.actions.C0504R;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.MultiUserManager;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.approach.p005ir.IRService;
import com.motorola.actions.approach.p006us.USService;
import com.motorola.actions.discovery.DiscoveryManager;
import com.motorola.actions.settings.provider.ActionsSettingsProviderConstants;
import com.motorola.actions.settings.provider.ActionsSettingsProviderConstants.TableConstants;
import com.motorola.actions.settings.provider.ContainerProviderItem;
import com.motorola.actions.settings.provider.p012v2.LinkAction;
import java.util.List;

/* renamed from: com.motorola.actions.settings.provider.v2.actions.ContainerProviderItemApproach */
public class ContainerProviderItemApproach extends ContainerProviderItem {
    private static final int MATCHER_ID = TableConstants.ACTIONS_CONTAINER_APPROACH.ordinal();
    public static final String PRIORITY_KEY = "card_priority_approach";
    public static final String TABLE_NAME = "actionscontainer/approach";

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
        return fillData(strArr, ((IRService.isIRSupported() || USService.isUSSupported()) && MultiUserManager.isSupportedForCurrentUser()) ? 1 : 2, C0504R.string.ir_aod_approach_enabled, C0504R.string.ir_aod_approach_enabled_summary, C0504R.C0505drawable.ic_actions_reach, 1, (IRService.isServiceEnabled() || USService.isServiceEnabled(false)) ? 1 : 0, "content://com.motorola.actions.settings.provider/actionscontainer/approach", LinkAction.getLinkAction(), DiscoveryManager.getInstance().getDiscoveryStatus(FeatureKey.APPROACH), SharedPreferenceManager.getInt(PRIORITY_KEY, FeaturePriorityActions.APPROACH.ordinal()), C0504R.C0505drawable.ic_moto_feature_icon_generic, -1, -1, -1, -1, C0504R.C0505drawable.ic_moto_icon_approach).toArray();
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
