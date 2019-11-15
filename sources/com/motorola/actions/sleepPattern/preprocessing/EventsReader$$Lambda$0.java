package com.motorola.actions.sleepPattern.preprocessing;

import com.motorola.actions.sleepPattern.repository.entities.AccelEntity;
import java.util.TimeZone;
import java.util.function.Function;

final /* synthetic */ class EventsReader$$Lambda$0 implements Function {
    private final TimeZone arg$1;

    EventsReader$$Lambda$0(TimeZone timeZone) {
        this.arg$1 = timeZone;
    }

    public Object apply(Object obj) {
        return EventsReader.lambda$readAccelerometerLogs$0$EventsReader(this.arg$1, (AccelEntity) obj);
    }
}
