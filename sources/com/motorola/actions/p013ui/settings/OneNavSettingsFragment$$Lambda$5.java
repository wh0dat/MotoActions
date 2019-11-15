package com.motorola.actions.p013ui.settings;

import java.util.Set;

/* renamed from: com.motorola.actions.ui.settings.OneNavSettingsFragment$$Lambda$5 */
final /* synthetic */ class OneNavSettingsFragment$$Lambda$5 implements Runnable {
    private final OneNavSettingsFragment arg$1;
    private final Set arg$2;

    OneNavSettingsFragment$$Lambda$5(OneNavSettingsFragment oneNavSettingsFragment, Set set) {
        this.arg$1 = oneNavSettingsFragment;
        this.arg$2 = set;
    }

    public void run() {
        this.arg$1.lambda$showConflictedServicesDialog$4$OneNavSettingsFragment(this.arg$2);
    }
}
