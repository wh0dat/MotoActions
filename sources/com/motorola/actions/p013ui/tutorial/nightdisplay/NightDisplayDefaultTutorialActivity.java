package com.motorola.actions.p013ui.tutorial.nightdisplay;

import com.motorola.actions.C0504R;
import com.motorola.actions.p013ui.tutorial.TutorialPage;
import java.util.List;

/* renamed from: com.motorola.actions.ui.tutorial.nightdisplay.NightDisplayDefaultTutorialActivity */
public class NightDisplayDefaultTutorialActivity extends NightDisplayBaseTutorialActivity {
    /* access modifiers changed from: 0000 */
    public List<TutorialPage> getTutorialPages() {
        if (getList().size() == 0) {
            getList().add(new TutorialPage(C0504R.string.night_display_tutorial_day_title, C0504R.string.night_display_tutorial_day_summary, C0504R.C0505drawable.night_display_tutorial_idle, C0504R.layout.fragment_tutorial_view_pager));
            getList().add(new TutorialPage(C0504R.string.night_display_tutorial_night_display_title, C0504R.string.night_display_tutorial_night_display_summary, C0504R.C0505drawable.night_display_tutorial_active_full, C0504R.layout.fragment_tutorial_view_pager));
        }
        return getList();
    }
}
