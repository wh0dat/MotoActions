package p016io.reactivex.internal.operators.flowable;

import org.reactivestreams.Publisher;
import p016io.reactivex.Flowable;
import p016io.reactivex.internal.functions.ObjectHelper;
import p016io.reactivex.internal.fuseable.HasUpstreamPublisher;

/* renamed from: io.reactivex.internal.operators.flowable.AbstractFlowableWithUpstream */
abstract class AbstractFlowableWithUpstream<T, R> extends Flowable<R> implements HasUpstreamPublisher<T> {
    protected final Publisher<T> source;

    AbstractFlowableWithUpstream(Publisher<T> publisher) {
        this.source = (Publisher) ObjectHelper.requireNonNull(publisher, "source is null");
    }

    public final Publisher<T> source() {
        return this.source;
    }
}
