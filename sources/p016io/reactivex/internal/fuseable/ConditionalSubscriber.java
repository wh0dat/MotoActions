package p016io.reactivex.internal.fuseable;

import org.reactivestreams.Subscriber;

/* renamed from: io.reactivex.internal.fuseable.ConditionalSubscriber */
public interface ConditionalSubscriber<T> extends Subscriber<T> {
    boolean tryOnNext(T t);
}
