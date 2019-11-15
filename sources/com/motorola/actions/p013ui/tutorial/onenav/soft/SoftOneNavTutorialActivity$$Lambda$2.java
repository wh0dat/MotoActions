package com.motorola.actions.p013ui.tutorial.onenav.soft;

import java.util.Arrays;
import java.util.function.Function;

/* renamed from: com.motorola.actions.ui.tutorial.onenav.soft.SoftOneNavTutorialActivity$$Lambda$2 */
final /* synthetic */ class SoftOneNavTutorialActivity$$Lambda$2 implements Function {
    static final Function $instance = new SoftOneNavTutorialActivity$$Lambda$2();

    private SoftOneNavTutorialActivity$$Lambda$2() {
    }

    public Object apply(Object obj) {
        return Arrays.stream((int[]) obj).asLongStream().toArray();
    }
}
