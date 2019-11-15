package com.motorola.actions.sleepPattern.preprocessing;

import com.motorola.actions.sleepPattern.repository.entities.AccelEntity;
import java.util.function.Function;

final /* synthetic */ class EventsReader$$Lambda$1 implements Function {
    static final Function $instance = new EventsReader$$Lambda$1();

    private EventsReader$$Lambda$1() {
    }

    public Object apply(Object obj) {
        return Integer.valueOf(((AccelEntity) obj).getEventCount());
    }
}
