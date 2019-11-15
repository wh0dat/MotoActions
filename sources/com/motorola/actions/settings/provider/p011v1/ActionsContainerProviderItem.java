package com.motorola.actions.settings.provider.p011v1;

import android.content.UriMatcher;
import android.database.MatrixCursor;
import com.motorola.actions.C0504R;
import com.motorola.actions.MultiUserManager;
import com.motorola.actions.settings.provider.ActionsSettingsProviderConstants;
import com.motorola.actions.settings.provider.ActionsSettingsProviderConstants.TableConstants;
import com.motorola.actions.settings.provider.ContainerProviderItem;
import com.motorola.actions.settings.provider.contract.Column;
import java.util.ArrayList;
import java.util.List;

/* renamed from: com.motorola.actions.settings.provider.v1.ActionsContainerProviderItem */
public class ActionsContainerProviderItem extends ContainerProviderItem {
    private static final int MATCHER_ID = TableConstants.ACTIONS_CONTAINER_ID.ordinal();
    private static final String TABLE_NAME = "actionscontainer";

    public static void registerUri(UriMatcher uriMatcher) {
        uriMatcher.addURI(ActionsSettingsProviderConstants.AUTHORITY_NAME, TABLE_NAME, MATCHER_ID);
    }

    private static MatrixCursor getCursorActionsContainer(String[] strArr) {
        if (strArr == null || strArr.length == 0) {
            strArr = (String[]) ActionsSettingsProviderConstants.CONTAINER_COLUMNS.toArray();
        }
        MatrixCursor matrixCursor = new MatrixCursor(strArr);
        ArrayList arrayList = new ArrayList();
        int i = MultiUserManager.isSupportedForCurrentUser() ? 1 : 2;
        for (String str : strArr) {
            if (Column.ENABLED.equals(str)) {
                arrayList.add(Integer.valueOf(i));
            } else if (Column.TITLE.equals(str)) {
                arrayList.add(Integer.valueOf(C0504R.string.app_name));
            } else if (Column.DESCRIPTION.equals(str)) {
                arrayList.add(Integer.valueOf(C0504R.string.actions_container_setting_description));
            } else if (Column.ICON.equals(str)) {
                arrayList.add(Integer.valueOf(C0504R.C0505drawable.icon_actions_moto));
            } else if (Column.TYPE.equals(str)) {
                arrayList.add(Integer.valueOf(0));
            } else if (Column.VALUE.equals(str)) {
                arrayList.add(Integer.valueOf(0));
            } else if (Column.FAMILY_IMAGE.equals(str)) {
                arrayList.add(Integer.valueOf(C0504R.C0505drawable.ic_moto_feature_family_moto_actions));
            } else if (Column.FAMILY_HEADER_TEXT.equals(str)) {
                arrayList.add(Integer.valueOf(C0504R.string.actions_container_family_header));
            } else if (Column.FAMILY_SUPPORT_TEXT.equals(str)) {
                arrayList.add(Integer.valueOf(C0504R.string.actions_container_family_support));
            } else if (Column.DRAWER_FAMILY_ICON.equals(str)) {
                arrayList.add(Integer.valueOf(C0504R.C0505drawable.ic_moto_actions_drawer));
            }
        }
        matrixCursor.addRow(arrayList.toArray());
        return matrixCursor;
    }

    /* access modifiers changed from: protected */
    public List<String> getColumns() {
        return ActionsSettingsProviderConstants.CONTAINER_COLUMNS;
    }

    /* access modifiers changed from: protected */
    public MatrixCursor getMatrixCursor(String[] strArr) {
        return getCursorActionsContainer(strArr);
    }
}
