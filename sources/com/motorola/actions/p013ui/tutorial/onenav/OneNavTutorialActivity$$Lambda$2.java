package com.motorola.actions.p013ui.tutorial.onenav;

import java.util.Arrays;
import java.util.function.Function;

/* renamed from: com.motorola.actions.ui.tutorial.onenav.OneNavTutorialActivity$$Lambda$2 */
final /* synthetic */ class OneNavTutorialActivity$$Lambda$2 implements Function {
    static final Function $instance = new OneNavTutorialActivity$$Lambda$2();

    private OneNavTutorialActivity$$Lambda$2() {
    }

    public Object apply(Object obj) {
        return Arrays.stream((int[]) obj).asLongStream().toArray();
    }
}
