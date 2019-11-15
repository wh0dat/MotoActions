package p016io.reactivex;

import org.reactivestreams.Subscriber;

/* renamed from: io.reactivex.FlowableOperator */
public interface FlowableOperator<Downstream, Upstream> {
    Subscriber<? super Upstream> apply(Subscriber<? super Downstream> subscriber) throws Exception;
}
