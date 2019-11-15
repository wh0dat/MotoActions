package com.motorola.actions.p013ui.tutorial.onenav.soft;

import android.widget.ScrollView;

/* renamed from: com.motorola.actions.ui.tutorial.onenav.soft.SoftOneNavTutorialActivity$$Lambda$5 */
final /* synthetic */ class SoftOneNavTutorialActivity$$Lambda$5 implements Runnable {
    private final ScrollView arg$1;

    SoftOneNavTutorialActivity$$Lambda$5(ScrollView scrollView) {
        this.arg$1 = scrollView;
    }

    public void run() {
        this.arg$1.fullScroll(130);
    }
}
