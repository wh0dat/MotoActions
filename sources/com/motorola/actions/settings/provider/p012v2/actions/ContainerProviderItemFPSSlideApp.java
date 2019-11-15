package com.motorola.actions.settings.provider.p012v2.actions;

import android.content.UriMatcher;
import android.database.MatrixCursor;
import com.motorola.actions.C0504R;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.discovery.DiscoveryManager;
import com.motorola.actions.fpsslide.FPSSlideHelper;
import com.motorola.actions.settings.provider.ActionsSettingsProviderConstants;
import com.motorola.actions.settings.provider.ActionsSettingsProviderConstants.TableConstants;
import com.motorola.actions.settings.provider.ContainerProviderItem;
import com.motorola.actions.settings.provider.p012v2.LinkAction;
import java.util.List;

/* renamed from: com.motorola.actions.settings.provider.v2.actions.ContainerProviderItemFPSSlideApp */
public class ContainerProviderItemFPSSlideApp extends ContainerProviderItem {
    private static final int MATCHER_ID = TableConstants.ACTIONS_CONTAINER_FPS_SLIDE_APP.ordinal();
    public static final String PRIORITY_KEY = "card_priority_fps_slide_app";
    public static final String TABLE_NAME = "actionscontainer/fps_slide_app";

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
        int i = !FPSSlideHelper.isFeatureSupported() ? 2 : 1;
        boolean isScrollOnAppEnabled = FPSSlideHelper.isScrollOnAppEnabled();
        int i2 = SharedPreferenceManager.getInt(PRIORITY_KEY, FeaturePriorityActions.FPS_SLIDE_APP.ordinal());
        return fillData(strArr, i, C0504R.string.fps_slide_app_enabled, C0504R.string.fps_slide_app_summary, C0504R.C0505drawable.ic_actions_fps_slide_app, 1, isScrollOnAppEnabled ? 1 : 0, "content://com.motorola.actions.settings.provider/actionscontainer/fps_slide_app", LinkAction.getLinkAction(), DiscoveryManager.getInstance().getDiscoveryStatus(FeatureKey.FPS_SLIDE_APP), i2, C0504R.C0505drawable.ic_moto_feature_icon_generic, -1, -1, -1, -1, C0504R.C0505drawable.ic_moto_icon_fps_scroll).toArray();
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
