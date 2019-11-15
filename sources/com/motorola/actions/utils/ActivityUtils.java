package com.motorola.actions.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.net.Uri;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.BadTokenException;
import android.widget.RelativeLayout.LayoutParams;
import com.google.android.gms.common.util.CrashUtils.ErrorDialogData;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.p013ui.settings.SettingsActivity;
import com.motorola.actions.p013ui.settings.SettingsDetailActivity;

public class ActivityUtils {
    private static final MALogger LOGGER = new MALogger(ActivityUtils.class);

    public static void setSetupWizardMode(View view, Activity activity) {
        if (view != null) {
            view.setSystemUiVisibility(4198914);
        }
        if (activity != null) {
            activity.getWindow().setNavigationBarColor(0);
            activity.getWindow().setStatusBarColor(0);
        }
    }

    public static void setFullscreenMode(Activity activity) {
        activity.requestWindowFeature(1);
        activity.getWindow().setFlags(1024, 1024);
    }

    public static void openFragmentForUserAction(int i) {
        Intent featureLaunchIntent = SettingsActivity.getFeatureLaunchIntent(i);
        featureLaunchIntent.putExtra(SettingsDetailActivity.EXTRA_SET_STATUS, true);
        featureLaunchIntent.setFlags(ErrorDialogData.BINDER_CRASH);
        ActionsApplication.getAppContext().startActivity(featureLaunchIntent);
    }

    public static void setStatusBarColor(Activity activity, int i) {
        if (activity != null) {
            Window window = activity.getWindow();
            if (window != null) {
                try {
                    window.setStatusBarColor(ActionsApplication.getAppContext().getResources().getColor(i, null));
                } catch (NotFoundException unused) {
                    LOGGER.mo11957d("Just ignoring setStatusBarColor() since resource color is not valid.");
                }
            }
        }
    }

    public static void setImmersiveFlags(View view) {
        view.setSystemUiVisibility(4610);
    }

    public static AlertDialog showWarningDialog(int i, int i2, int i3, int i4, OnClickListener onClickListener, OnClickListener onClickListener2, Activity activity) {
        Context appContext = ActionsApplication.getAppContext();
        AlertDialog create = new Builder(activity).setCancelable(false).create();
        create.setTitle(appContext.getString(i));
        create.setMessage(appContext.getString(i2));
        create.setButton(-1, appContext.getString(i3), onClickListener);
        if (create.getButton(-1) != null) {
            create.getButton(-1).setText(appContext.getString(i3));
        }
        create.setButton(-2, appContext.getString(i4), onClickListener2);
        if (create.getButton(-2) != null) {
            create.getButton(-2).setText(appContext.getString(i4));
        }
        if (!create.isShowing()) {
            try {
                create.show();
            } catch (BadTokenException e) {
                LOGGER.mo11959e(e.getMessage());
            }
        }
        return create;
    }

    public static void openMotorolaSite() {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("https://www.motorola.com"));
        intent.setFlags(ErrorDialogData.BINDER_CRASH);
        try {
            ActionsApplication.getAppContext().startActivity(intent);
        } catch (ActivityNotFoundException unused) {
            LOGGER.mo11959e("Couldn't find activity to open URL");
        }
    }

    public static void stretchVideo(TextureView textureView) {
        LayoutParams layoutParams = (LayoutParams) textureView.getLayoutParams();
        layoutParams.setMargins(0, layoutParams.topMargin, 0, layoutParams.bottomMargin);
        textureView.setLayoutParams(layoutParams);
    }
}
