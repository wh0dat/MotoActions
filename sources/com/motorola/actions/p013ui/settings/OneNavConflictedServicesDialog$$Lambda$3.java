package com.motorola.actions.p013ui.settings;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

/* renamed from: com.motorola.actions.ui.settings.OneNavConflictedServicesDialog$$Lambda$3 */
final /* synthetic */ class OneNavConflictedServicesDialog$$Lambda$3 implements OnClickListener {
    private final Runnable arg$1;

    OneNavConflictedServicesDialog$$Lambda$3(Runnable runnable) {
        this.arg$1 = runnable;
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        this.arg$1.run();
    }
}
