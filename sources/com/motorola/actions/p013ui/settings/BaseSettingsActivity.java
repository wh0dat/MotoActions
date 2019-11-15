package com.motorola.actions.p013ui.settings;

import android.support.p004v7.app.ActionBar;
import android.support.p004v7.widget.Toolbar;
import android.widget.RelativeLayout.LayoutParams;
import com.motorola.actions.C0504R;
import com.motorola.actions.p013ui.ActionsBaseActivity;
import com.motorola.actions.utils.AndroidResourceAccess;

/* renamed from: com.motorola.actions.ui.settings.BaseSettingsActivity */
public class BaseSettingsActivity extends ActionsBaseActivity {
    private int mToolbarOriginalHeight;

    /* access modifiers changed from: protected */
    public void setupToolbar(int i) {
        getWindow().addFlags(67108864);
        Toolbar toolbar = (Toolbar) findViewById(C0504R.C0506id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayShowHomeEnabled(true);
            supportActionBar.setHomeButtonEnabled(true);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (i != -1) {
            getSupportActionBar().setTitle(i);
        }
        LayoutParams layoutParams = (LayoutParams) toolbar.getLayoutParams();
        if (this.mToolbarOriginalHeight == 0) {
            this.mToolbarOriginalHeight = layoutParams.height;
        }
        layoutParams.height = this.mToolbarOriginalHeight + getStatusBarHeight();
        toolbar.setLayoutParams(layoutParams);
        toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
        toolbar.setBackgroundColor(getColor(C0504R.color.parasailing_500));
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private int getStatusBarHeight() {
        return ((Integer) AndroidResourceAccess.getDimResource("status_bar_height").orElse(Integer.valueOf(0))).intValue();
    }
}
