package com.motorola.actions.zenmode;

import android.net.Uri;
import java.util.function.Consumer;

final /* synthetic */ class AutomaticRulesListener$$Lambda$0 implements Consumer {
    private final AutomaticRulesListener arg$1;

    AutomaticRulesListener$$Lambda$0(AutomaticRulesListener automaticRulesListener) {
        this.arg$1 = automaticRulesListener;
    }

    public void accept(Object obj) {
        this.arg$1.lambda$registerZenModeConfigObserver$0$AutomaticRulesListener((Uri) obj);
    }
}
