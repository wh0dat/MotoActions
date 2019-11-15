package com.motorola.actions.debug.items;

import android.widget.Toast;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.C0504R;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.utils.Constants;

public class ResetWelcomeActivitiesDebugItem extends DebugItem {
    public ResetWelcomeActivitiesDebugItem() {
        setTitle(getString(C0504R.string.debug_welcome_screen_title));
        setDescription(getString(C0504R.string.debug_welcome_screen_description));
    }

    /* access modifiers changed from: protected */
    public void startBackgroundWork() {
        super.startBackgroundWork();
        SharedPreferenceManager.remove(Constants.ONENAV_SCREEN_ALREADY_SHOWN);
        SharedPreferenceManager.remove(Constants.ND_SCREEN_ALREADY_SHOWN);
        SharedPreferenceManager.remove(Constants.QUICK_SCREENSHOT_SCREEN_ALREADY_SHOWN);
    }

    /* access modifiers changed from: protected */
    public void startPostExecuteWork() {
        super.startPostExecuteWork();
        Toast.makeText(ActionsApplication.getAppContext(), C0504R.string.debug_welcome_screen_message, 0).show();
    }
}
