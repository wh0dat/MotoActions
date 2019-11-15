package com.motorola.actions.p013ui;

import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import com.motorola.actions.p013ui.SnackbarHelper.SnackbarButtonListener;

/* renamed from: com.motorola.actions.ui.SnackbarHelper$$Lambda$0 */
final /* synthetic */ class SnackbarHelper$$Lambda$0 implements OnClickListener {
    private final Dialog arg$1;
    private final SnackbarButtonListener arg$2;
    private final int arg$3;

    SnackbarHelper$$Lambda$0(Dialog dialog, SnackbarButtonListener snackbarButtonListener, int i) {
        this.arg$1 = dialog;
        this.arg$2 = snackbarButtonListener;
        this.arg$3 = i;
    }

    public void onClick(View view) {
        SnackbarHelper.lambda$showSnackbar$0$SnackbarHelper(this.arg$1, this.arg$2, this.arg$3, view);
    }
}
