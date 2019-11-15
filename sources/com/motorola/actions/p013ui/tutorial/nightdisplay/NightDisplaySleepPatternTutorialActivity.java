package com.motorola.actions.p013ui.tutorial.nightdisplay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import com.motorola.actions.C0504R;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.p013ui.tutorial.TutorialPage;
import com.motorola.actions.sleepPattern.SleepPatternService;
import com.motorola.actions.utils.Constants;
import java.util.List;

/* renamed from: com.motorola.actions.ui.tutorial.nightdisplay.NightDisplaySleepPatternTutorialActivity */
public class NightDisplaySleepPatternTutorialActivity extends NightDisplayBaseTutorialActivity {
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        SharedPreferenceManager.putBoolean(Constants.ND_SCREEN_ALREADY_SHOWN, true);
    }

    /* access modifiers changed from: 0000 */
    public List<TutorialPage> getTutorialPages() {
        if (getList().size() == 0) {
            getList().add(new TutorialPage(C0504R.string.night_display_tutorial_day_title, C0504R.string.night_display_tutorial_day_summary, C0504R.C0505drawable.night_display_tutorial_idle, C0504R.layout.fragment_tutorial_view_pager));
            getList().add(new TutorialPage(C0504R.string.night_display_tutorial_light_filter_title, C0504R.string.night_display_tutorial_light_filter_summary, C0504R.C0505drawable.night_display_tutorial_active_full, C0504R.layout.fragment_tutorial_view_pager));
            getList().add(new TutorialPage(C0504R.string.night_display_tutorial_learn_sleep_title, C0504R.string.night_display_tutorial_learn_sleep_summary, C0504R.C0505drawable.night_display_tutorial_sleep_pattern, C0504R.layout.fragment_tutorial_view_pager));
            getList().add(new TutorialPage(C0504R.string.night_display_tutorial_manual_mode_title, C0504R.string.night_display_tutorial_manual_mode_summary, C0504R.C0505drawable.night_display_tutorial_manual_mode, C0504R.layout.fragment_tutorial_view_pager));
        }
        return getList();
    }

    public static boolean shouldOpenTutorial() {
        if (!SleepPatternService.isFeatureSupported() || SharedPreferenceManager.getBoolean(Constants.ND_SCREEN_ALREADY_SHOWN, false)) {
            return false;
        }
        return true;
    }

    public static void openActivity(@NonNull Activity activity, Bundle bundle) {
        Intent intent = new Intent(activity, NightDisplaySleepPatternTutorialActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        activity.startActivity(intent);
    }
}
