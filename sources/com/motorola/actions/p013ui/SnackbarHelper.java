package com.motorola.actions.p013ui;

import android.app.Dialog;
import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;
import com.motorola.actions.C0504R;
import com.motorola.actions.utils.MALogger;

/* renamed from: com.motorola.actions.ui.SnackbarHelper */
public final class SnackbarHelper {
    private static final MALogger LOGGER = new MALogger(SnackbarHelper.class);

    /* renamed from: com.motorola.actions.ui.SnackbarHelper$SnackbarButtonListener */
    public interface SnackbarButtonListener {
        void onPositiveButtonClick(int i);
    }

    private SnackbarHelper() {
    }

    public static void showSnackbar(int i, String str, int i2, SnackbarButtonListener snackbarButtonListener, Context context, int i3, SparseArray<Dialog> sparseArray) {
        if (sparseArray == null || sparseArray.get(i3) == null) {
            Dialog dialog = new Dialog(context, C0504R.style.PermissionDialog);
            View inflate = View.inflate(context, C0504R.layout.permission_dialog_layout, null);
            ((TextView) inflate.findViewById(C0504R.C0506id.title)).setText(i);
            ((TextView) inflate.findViewById(C0504R.C0506id.message)).setText(str);
            Button button = (Button) inflate.findViewById(C0504R.C0506id.accept_button);
            button.setAllCaps(true);
            button.setText(i2);
            button.setOnClickListener(new SnackbarHelper$$Lambda$0(dialog, snackbarButtonListener, i3));
            dialog.setContentView(inflate);
            dialog.setCancelable(false);
            dialog.show();
            Window window = dialog.getWindow();
            try {
                window.setLayout(-1, -2);
                LayoutParams attributes = window.getAttributes();
                window.setGravity(81);
                window.setAttributes(attributes);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("Dialog for ");
            sb.append(i3);
            sb.append(" created");
            mALogger.mo11957d(sb.toString());
            if (sparseArray != null) {
                sparseArray.put(i3, dialog);
            }
            return;
        }
        MALogger mALogger2 = LOGGER;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Dialog for ");
        sb2.append(i3);
        sb2.append(" already exists");
        mALogger2.mo11957d(sb2.toString());
    }

    static final /* synthetic */ void lambda$showSnackbar$0$SnackbarHelper(Dialog dialog, SnackbarButtonListener snackbarButtonListener, int i, View view) {
        dialog.dismiss();
        if (snackbarButtonListener != null) {
            snackbarButtonListener.onPositiveButtonClick(i);
        }
    }

    public static void dismissDialogs(SparseArray<Dialog> sparseArray) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("dismissDialogs: ");
        sb.append(sparseArray.size());
        mALogger.mo11957d(sb.toString());
        for (int i = 0; i < sparseArray.size(); i++) {
            ((Dialog) sparseArray.get(sparseArray.keyAt(i))).dismiss();
        }
        sparseArray.clear();
    }
}
