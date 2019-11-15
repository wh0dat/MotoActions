package p016io.reactivex.processors;

import org.reactivestreams.Processor;
import p016io.reactivex.Flowable;

/* renamed from: io.reactivex.processors.FlowableProcessor */
public abstract class FlowableProcessor<T> extends Flowable<T> implements Processor<T, T> {
    public abstract Throwable getThrowable();

    public abstract boolean hasComplete();

    public abstract boolean hasSubscribers();

    public abstract boolean hasThrowable();

    public final FlowableProcessor<T> toSerialized() {
        if (this instanceof SerializedProcessor) {
            return this;
        }
        return new SerializedProcessor(this);
    }
}
