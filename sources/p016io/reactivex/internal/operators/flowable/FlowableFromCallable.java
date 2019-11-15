package p016io.reactivex.internal.operators.flowable;

import java.util.concurrent.Callable;
import org.reactivestreams.Subscriber;
import p016io.reactivex.Flowable;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.internal.functions.ObjectHelper;
import p016io.reactivex.internal.subscriptions.DeferredScalarSubscription;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableFromCallable */
public final class FlowableFromCallable<T> extends Flowable<T> implements Callable<T> {
    final Callable<? extends T> callable;

    public FlowableFromCallable(Callable<? extends T> callable2) {
        this.callable = callable2;
    }

    public void subscribeActual(Subscriber<? super T> subscriber) {
        DeferredScalarSubscription deferredScalarSubscription = new DeferredScalarSubscription(subscriber);
        subscriber.onSubscribe(deferredScalarSubscription);
        try {
            deferredScalarSubscription.complete(ObjectHelper.requireNonNull(this.callable.call(), "The callable returned a null value"));
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            subscriber.onError(th);
        }
    }

    public T call() throws Exception {
        return ObjectHelper.requireNonNull(this.callable.call(), "The callable returned a null value");
    }
}
