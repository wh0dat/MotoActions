package com.motorola.actions.p013ui.settings;

import android.app.AlertDialog.Builder;
import android.content.Context;
import com.motorola.actions.C0504R;
import com.motorola.actions.utils.MALogger;

/* renamed from: com.motorola.actions.ui.settings.OneNavConflictedServicesDialog */
public class OneNavConflictedServicesDialog {
    private static final MALogger LOGGER = new MALogger(OneNavConflictedServicesDialog.class);
    private static final String PACKAGE_NAME_SEPARATOR = "/";

    /* JADX WARNING: Removed duplicated region for block: B:11:0x0072  */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x0032 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void showConflictedServicesTurnOffDialog(android.content.Context r9, java.util.Set<android.accessibilityservice.AccessibilityServiceInfo> r10, java.lang.Runnable r11, java.lang.Runnable r12) {
        /*
            com.motorola.actions.utils.MALogger r0 = LOGGER
            java.lang.String r1 = "showConflictedServicesTurnOffDialog"
            r0.mo11957d(r1)
            android.app.AlertDialog$Builder r0 = new android.app.AlertDialog$Builder
            r0.<init>(r9)
            r1 = 2131624300(0x7f0e016c, float:1.8875776E38)
            r0.setTitle(r1)
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            android.content.res.Resources r2 = r9.getResources()
            r3 = 2131624299(0x7f0e016b, float:1.8875774E38)
            java.lang.CharSequence r2 = r2.getText(r3)
            r1.append(r2)
            java.lang.String r2 = "\n"
            r1.append(r2)
            android.content.pm.PackageManager r2 = r9.getPackageManager()
            java.util.Iterator r10 = r10.iterator()
        L_0x0032:
            boolean r3 = r10.hasNext()
            r4 = 0
            if (r3 == 0) goto L_0x0092
            java.lang.Object r3 = r10.next()
            android.accessibilityservice.AccessibilityServiceInfo r3 = (android.accessibilityservice.AccessibilityServiceInfo) r3
            r5 = 0
            java.lang.String r6 = r3.getId()
            java.lang.String r7 = "/"
            java.lang.String[] r6 = r6.split(r7)
            int r7 = r6.length
            r8 = 1
            if (r7 < r8) goto L_0x006f
            r6 = r6[r4]     // Catch:{ NameNotFoundException -> 0x0055 }
            android.content.pm.ApplicationInfo r4 = r2.getApplicationInfo(r6, r4)     // Catch:{ NameNotFoundException -> 0x0055 }
            goto L_0x0070
        L_0x0055:
            com.motorola.actions.utils.MALogger r4 = LOGGER
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "Accessibility service name not found: "
            r6.append(r7)
            java.lang.String r3 = r3.getId()
            r6.append(r3)
            java.lang.String r3 = r6.toString()
            r4.mo11959e(r3)
        L_0x006f:
            r4 = r5
        L_0x0070:
            if (r4 == 0) goto L_0x0032
            java.lang.String r3 = "\n\t"
            r1.append(r3)
            android.content.res.Resources r3 = r9.getResources()
            r5 = 2131624005(0x7f0e0045, float:1.8875177E38)
            java.lang.CharSequence r3 = r3.getText(r5)
            r1.append(r3)
            java.lang.String r3 = " "
            r1.append(r3)
            java.lang.CharSequence r3 = r2.getApplicationLabel(r4)
            r1.append(r3)
            goto L_0x0032
        L_0x0092:
            java.lang.String r9 = r1.toString()
            r0.setMessage(r9)
            android.app.AlertDialog$Builder r9 = r0.setCancelable(r4)
            r10 = 2131624297(0x7f0e0169, float:1.887577E38)
            com.motorola.actions.ui.settings.OneNavConflictedServicesDialog$$Lambda$0 r1 = new com.motorola.actions.ui.settings.OneNavConflictedServicesDialog$$Lambda$0
            r1.<init>(r11)
            android.app.AlertDialog$Builder r9 = r9.setPositiveButton(r10, r1)
            r10 = 2131624298(0x7f0e016a, float:1.8875772E38)
            com.motorola.actions.ui.settings.OneNavConflictedServicesDialog$$Lambda$1 r11 = new com.motorola.actions.ui.settings.OneNavConflictedServicesDialog$$Lambda$1
            r11.<init>(r12)
            r9.setNegativeButton(r10, r11)
            android.app.AlertDialog r9 = r0.create()
            r9.show()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.motorola.actions.p013ui.settings.OneNavConflictedServicesDialog.showConflictedServicesTurnOffDialog(android.content.Context, java.util.Set, java.lang.Runnable, java.lang.Runnable):void");
    }

    public static void showSwipeUpConflictDialog(Context context, Runnable runnable, Runnable runnable2) {
        LOGGER.mo11957d("showSwipeUpConflictDialog");
        new Builder(context).setTitle(C0504R.string.onenav_turn_on_dialog_title).setMessage(context.getResources().getText(C0504R.string.softonenav_on_dialog_text)).setCancelable(false).setPositiveButton(C0504R.string.onenav_turn_on_dialog_accept, new OneNavConflictedServicesDialog$$Lambda$2(runnable)).setNegativeButton(C0504R.string.onenav_turn_on_dialog_cancel, new OneNavConflictedServicesDialog$$Lambda$3(runnable2)).create().show();
    }
}
