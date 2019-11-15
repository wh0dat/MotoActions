package com.motorola.actions.settings.provider.p012v2.display;

import android.content.UriMatcher;
import android.database.MatrixCursor;
import com.motorola.actions.settings.provider.ActionsSettingsProviderConstants;
import com.motorola.actions.settings.provider.ActionsSettingsProviderConstants.TableConstants;
import com.motorola.actions.settings.provider.ContainerProviderItem;
import java.util.List;

/* renamed from: com.motorola.actions.settings.provider.v2.display.DisplayContainerProviderItemSettings */
public class DisplayContainerProviderItemSettings extends ContainerProviderItem {
    private static final int MATCHER_ID = TableConstants.DISPLAY_CONTAINER_ALL_SETTINGS.ordinal();
    private static final String TABLE_NAME = "displaycontainer/SETTINGS";

    public static void registerUri(UriMatcher uriMatcher) {
        uriMatcher.addURI(ActionsSettingsProviderConstants.AUTHORITY_NAME, TABLE_NAME, MATCHER_ID);
    }

    private static MatrixCursor getCursorActionsContainer(String[] strArr) {
        if (strArr == null || strArr.length == 0) {
            strArr = (String[]) ActionsSettingsProviderConstants.SETTINGS_COLUMNS_V2.toArray();
        }
        MatrixCursor matrixCursor = new MatrixCursor(strArr);
        addRow(matrixCursor, ContainerProviderItemNightDisplay.getItem(strArr));
        addRow(matrixCursor, ContainerProviderItemAttentiveDisplay.getItem(strArr));
        return matrixCursor;
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
