package com.motorola.actions.settings.provider.p011v1;

import android.content.UriMatcher;
import android.database.MatrixCursor;
import android.net.Uri;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.attentivedisplay.AttentiveDisplayService;
import com.motorola.actions.p013ui.settings.AttentiveDisplaySettingsFragment;
import com.motorola.actions.settings.provider.ActionsSettingsProviderConstants;
import com.motorola.actions.settings.provider.ActionsSettingsProviderConstants.TableConstants;
import com.motorola.actions.settings.provider.ContainerProviderItem;
import com.motorola.actions.utils.MALogger;
import java.util.Collections;
import java.util.List;

/* renamed from: com.motorola.actions.settings.provider.v1.AttentiveDisplayProviderItem */
public class AttentiveDisplayProviderItem extends ContainerProviderItem {
    private static final int ATTENTIVE_DISPLAY_COLUMN_ENABLE_FALSE = 0;
    private static final int ATTENTIVE_DISPLAY_COLUMN_ENABLE_TRUE = 1;
    private static final MALogger LOGGER = new MALogger(AttentiveDisplayProviderItem.class);
    private static final int MATCHER_ID = TableConstants.ATTENTIVE_DISPLAY_ID.ordinal();
    private static final List<String> SETTINGS_COLUMNS = Collections.unmodifiableList(Collections.singletonList("enabled"));
    private static final String TABLE_NAME = "attentiveDisplay";
    private static final int TABLE_NAME_ATTENTIVE_DISPLAY_COLUMN_ENABLE = 0;
    public static final Uri URI = Uri.parse("content://com.motorola.actions.settings.provider/attentiveDisplay");

    public static void registerUri(UriMatcher uriMatcher) {
        uriMatcher.addURI(ActionsSettingsProviderConstants.AUTHORITY_NAME, TABLE_NAME, MATCHER_ID);
    }

    private static MatrixCursor getCursorAttentiveDisplay(String[] strArr) {
        if (strArr == null || strArr.length == 0) {
            return null;
        }
        MatrixCursor matrixCursor = new MatrixCursor(strArr);
        for (String str : strArr) {
            if (((String) SETTINGS_COLUMNS.get(0)).equals(str)) {
                int i = (!AttentiveDisplayService.isFeatureSupported(ActionsApplication.getAppContext()) || !AttentiveDisplaySettingsFragment.isStayOnEnabled()) ? 0 : 1;
                MALogger mALogger = LOGGER;
                StringBuilder sb = new StringBuilder();
                sb.append("Add row to matrixCursor - column: ");
                sb.append(str);
                sb.append(" - value = ");
                sb.append(i);
                mALogger.mo11957d(sb.toString());
                matrixCursor.addRow(new Object[]{Integer.valueOf(i)});
            }
        }
        return matrixCursor;
    }

    /* access modifiers changed from: protected */
    public List<String> getColumns() {
        return SETTINGS_COLUMNS;
    }

    /* access modifiers changed from: protected */
    public MatrixCursor getMatrixCursor(String[] strArr) {
        return getCursorAttentiveDisplay(strArr);
    }
}
