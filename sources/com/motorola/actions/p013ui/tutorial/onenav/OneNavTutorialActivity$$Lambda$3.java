package com.motorola.actions.p013ui.tutorial.onenav;

import java.util.Set;

/* renamed from: com.motorola.actions.ui.tutorial.onenav.OneNavTutorialActivity$$Lambda$3 */
final /* synthetic */ class OneNavTutorialActivity$$Lambda$3 implements Runnable {
    private final OneNavTutorialActivity arg$1;
    private final Set arg$2;

    OneNavTutorialActivity$$Lambda$3(OneNavTutorialActivity oneNavTutorialActivity, Set set) {
        this.arg$1 = oneNavTutorialActivity;
        this.arg$2 = set;
    }

    public void run() {
        this.arg$1.lambda$onResume$3$OneNavTutorialActivity(this.arg$2);
    }
}
