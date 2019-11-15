package com.motorola.actions.settings.provider;

import android.database.MatrixCursor;
import com.motorola.actions.utils.MALogger;
import java.util.List;

public abstract class ContainerProviderItem {
    private static final MALogger LOGGER = new MALogger(ContainerProviderItem.class);
    protected static final int RESOURCE_NOT_AVAILABLE = -1;

    protected enum FeaturePriorityActions {
        QUICK_CAPTURE,
        FLASH_ON_CHOP,
        ONE_NAV,
        QUICK_SCREENSHOT,
        LIFT_TO_SILENCE,
        FLIP_TO_MUTE,
        MICROSCREEN,
        ENHANCED_SREENSHOT,
        MEDIA_CONTROL,
        LIFT_TO_UNLOCK,
        FPS_SLIDE_APP,
        FPS_SLIDE_HOME,
        APPROACH
    }

    protected enum FeaturePriorityDisplay {
        MOTO_DISPLAY,
        ATTENTIVE_DISPLAY,
        NIGHT_DISPLAY
    }

    /* access modifiers changed from: protected */
    public abstract List<String> getColumns();

    /* access modifiers changed from: protected */
    public abstract MatrixCursor getMatrixCursor(String[] strArr);

    protected static void addRow(MatrixCursor matrixCursor, Object[] objArr) {
        if (objArr != null) {
            matrixCursor.addRow(objArr);
        }
    }

    /* access modifiers changed from: protected */
    public MatrixCursor query(String[] strArr) {
        if (checkProjection(strArr)) {
            return getMatrixCursor(strArr);
        }
        return null;
    }

    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected static java.util.ArrayList fillData(java.lang.String[] r8, int r9, int r10, int r11, int r12, int r13, int r14, java.lang.String r15, int r16, int r17, int r18, int r19, int r20, int r21, int r22, int r23, int r24) {
        /*
            r0 = r8
            java.util.ArrayList r1 = new java.util.ArrayList
            r1.<init>()
            int r2 = r0.length
            r3 = 0
            r4 = r3
        L_0x0009:
            if (r4 >= r2) goto L_0x015b
            r5 = r0[r4]
            r6 = -1
            int r7 = r5.hashCode()
            switch(r7) {
                case -1931822565: goto L_0x00bc;
                case -1897872310: goto L_0x00b1;
                case -891611359: goto L_0x00a7;
                case -814793204: goto L_0x009c;
                case -758656481: goto L_0x0091;
                case -739263422: goto L_0x0086;
                case 84300: goto L_0x007c;
                case 2241657: goto L_0x0072;
                case 2590522: goto L_0x0068;
                case 79833656: goto L_0x005d;
                case 81434961: goto L_0x0052;
                case 428414940: goto L_0x0047;
                case 957174216: goto L_0x003b;
                case 1023003009: goto L_0x002f;
                case 1374833520: goto L_0x0023;
                case 1748304875: goto L_0x0017;
                default: goto L_0x0015;
            }
        L_0x0015:
            goto L_0x00c6
        L_0x0017:
            java.lang.String r7 = "DISCOVERY_CTA_TEXT"
            boolean r5 = r5.equals(r7)
            if (r5 == 0) goto L_0x00c6
            r5 = 14
            goto L_0x00c7
        L_0x0023:
            java.lang.String r7 = "DISCOVERY_HEADER_TEXT"
            boolean r5 = r5.equals(r7)
            if (r5 == 0) goto L_0x00c6
            r5 = 12
            goto L_0x00c7
        L_0x002f:
            java.lang.String r7 = "DISCOVERY_STATUS"
            boolean r5 = r5.equals(r7)
            if (r5 == 0) goto L_0x00c6
            r5 = 8
            goto L_0x00c7
        L_0x003b:
            java.lang.String r7 = "DISCOVERY_ICON"
            boolean r5 = r5.equals(r7)
            if (r5 == 0) goto L_0x00c6
            r5 = 11
            goto L_0x00c7
        L_0x0047:
            java.lang.String r7 = "DESCRIPTION"
            boolean r5 = r5.equals(r7)
            if (r5 == 0) goto L_0x00c6
            r5 = 2
            goto L_0x00c7
        L_0x0052:
            java.lang.String r7 = "VALUE"
            boolean r5 = r5.equals(r7)
            if (r5 == 0) goto L_0x00c6
            r5 = 5
            goto L_0x00c7
        L_0x005d:
            java.lang.String r7 = "TITLE"
            boolean r5 = r5.equals(r7)
            if (r5 == 0) goto L_0x00c6
            r5 = 1
            goto L_0x00c7
        L_0x0068:
            java.lang.String r7 = "TYPE"
            boolean r5 = r5.equals(r7)
            if (r5 == 0) goto L_0x00c6
            r5 = 4
            goto L_0x00c7
        L_0x0072:
            java.lang.String r7 = "ICON"
            boolean r5 = r5.equals(r7)
            if (r5 == 0) goto L_0x00c6
            r5 = 3
            goto L_0x00c7
        L_0x007c:
            java.lang.String r7 = "URI"
            boolean r5 = r5.equals(r7)
            if (r5 == 0) goto L_0x00c6
            r5 = 6
            goto L_0x00c7
        L_0x0086:
            java.lang.String r7 = "FEATURE_ICON"
            boolean r5 = r5.equals(r7)
            if (r5 == 0) goto L_0x00c6
            r5 = 15
            goto L_0x00c7
        L_0x0091:
            java.lang.String r7 = "FEATURE_CARD_ICON"
            boolean r5 = r5.equals(r7)
            if (r5 == 0) goto L_0x00c6
            r5 = 10
            goto L_0x00c7
        L_0x009c:
            java.lang.String r7 = "DISCOVERY_SUPPORT_TEXT"
            boolean r5 = r5.equals(r7)
            if (r5 == 0) goto L_0x00c6
            r5 = 13
            goto L_0x00c7
        L_0x00a7:
            java.lang.String r7 = "ENABLED"
            boolean r5 = r5.equals(r7)
            if (r5 == 0) goto L_0x00c6
            r5 = r3
            goto L_0x00c7
        L_0x00b1:
            java.lang.String r7 = "FEATURE_CARD_PRIORITY"
            boolean r5 = r5.equals(r7)
            if (r5 == 0) goto L_0x00c6
            r5 = 9
            goto L_0x00c7
        L_0x00bc:
            java.lang.String r7 = "LINK_ACTION"
            boolean r5 = r5.equals(r7)
            if (r5 == 0) goto L_0x00c6
            r5 = 7
            goto L_0x00c7
        L_0x00c6:
            r5 = r6
        L_0x00c7:
            switch(r5) {
                case 0: goto L_0x014f;
                case 1: goto L_0x0146;
                case 2: goto L_0x013d;
                case 3: goto L_0x0134;
                case 4: goto L_0x012b;
                case 5: goto L_0x0122;
                case 6: goto L_0x011d;
                case 7: goto L_0x0114;
                case 8: goto L_0x010c;
                case 9: goto L_0x0104;
                case 10: goto L_0x00fc;
                case 11: goto L_0x00f4;
                case 12: goto L_0x00ec;
                case 13: goto L_0x00e4;
                case 14: goto L_0x00dc;
                case 15: goto L_0x00d4;
                default: goto L_0x00ca;
            }
        L_0x00ca:
            r5 = r15
            com.motorola.actions.utils.MALogger r6 = LOGGER
            java.lang.String r7 = "invalid column"
            r6.mo11957d(r7)
            goto L_0x0157
        L_0x00d4:
            java.lang.Integer r5 = java.lang.Integer.valueOf(r24)
            r1.add(r5)
            goto L_0x011b
        L_0x00dc:
            java.lang.Integer r5 = java.lang.Integer.valueOf(r23)
            r1.add(r5)
            goto L_0x011b
        L_0x00e4:
            java.lang.Integer r5 = java.lang.Integer.valueOf(r22)
            r1.add(r5)
            goto L_0x011b
        L_0x00ec:
            java.lang.Integer r5 = java.lang.Integer.valueOf(r21)
            r1.add(r5)
            goto L_0x011b
        L_0x00f4:
            java.lang.Integer r5 = java.lang.Integer.valueOf(r20)
            r1.add(r5)
            goto L_0x011b
        L_0x00fc:
            java.lang.Integer r5 = java.lang.Integer.valueOf(r19)
            r1.add(r5)
            goto L_0x011b
        L_0x0104:
            java.lang.Integer r5 = java.lang.Integer.valueOf(r18)
            r1.add(r5)
            goto L_0x011b
        L_0x010c:
            java.lang.Integer r5 = java.lang.Integer.valueOf(r17)
            r1.add(r5)
            goto L_0x011b
        L_0x0114:
            java.lang.Integer r5 = java.lang.Integer.valueOf(r16)
            r1.add(r5)
        L_0x011b:
            r5 = r15
            goto L_0x0157
        L_0x011d:
            r5 = r15
            r1.add(r5)
            goto L_0x0157
        L_0x0122:
            r5 = r15
            java.lang.Integer r6 = java.lang.Integer.valueOf(r14)
            r1.add(r6)
            goto L_0x0157
        L_0x012b:
            r5 = r15
            java.lang.Integer r6 = java.lang.Integer.valueOf(r13)
            r1.add(r6)
            goto L_0x0157
        L_0x0134:
            r5 = r15
            java.lang.Integer r6 = java.lang.Integer.valueOf(r12)
            r1.add(r6)
            goto L_0x0157
        L_0x013d:
            r5 = r15
            java.lang.Integer r6 = java.lang.Integer.valueOf(r11)
            r1.add(r6)
            goto L_0x0157
        L_0x0146:
            r5 = r15
            java.lang.Integer r6 = java.lang.Integer.valueOf(r10)
            r1.add(r6)
            goto L_0x0157
        L_0x014f:
            r5 = r15
            java.lang.Integer r6 = java.lang.Integer.valueOf(r9)
            r1.add(r6)
        L_0x0157:
            int r4 = r4 + 1
            goto L_0x0009
        L_0x015b:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.motorola.actions.settings.provider.ContainerProviderItem.fillData(java.lang.String[], int, int, int, int, int, int, java.lang.String, int, int, int, int, int, int, int, int, int):java.util.ArrayList");
    }

    private boolean checkProjection(String[] strArr) {
        if (!(strArr == null || strArr.length == 0)) {
            for (String contains : strArr) {
                if (!getColumns().contains(contains)) {
                    LOGGER.mo11957d("Error: column not found");
                    return false;
                }
            }
        }
        return true;
    }
}
