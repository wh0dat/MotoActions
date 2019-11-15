package p016io.reactivex.internal.operators.flowable;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.exceptions.CompositeException;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.Function;
import p016io.reactivex.internal.subscriptions.SubscriptionArbiter;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableOnErrorNext */
public final class FlowableOnErrorNext<T> extends AbstractFlowableWithUpstream<T, T> {
    final boolean allowFatal;
    final Function<? super Throwable, ? extends Publisher<? extends T>> nextSupplier;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableOnErrorNext$OnErrorNextSubscriber */
    static final class OnErrorNextSubscriber<T> implements Subscriber<T> {
        final Subscriber<? super T> actual;
        final boolean allowFatal;
        final SubscriptionArbiter arbiter = new SubscriptionArbiter();
        boolean done;
        final Function<? super Throwable, ? extends Publisher<? extends T>> nextSupplier;
        boolean once;

        OnErrorNextSubscriber(Subscriber<? super T> subscriber, Function<? super Throwable, ? extends Publisher<? extends T>> function, boolean z) {
            this.actual = subscriber;
            this.nextSupplier = function;
            this.allowFatal = z;
        }

        public void onSubscribe(Subscription subscription) {
            this.arbiter.setSubscription(subscription);
        }

        public void onNext(T t) {
            if (!this.done) {
                this.actual.onNext(t);
                if (!this.once) {
                    this.arbiter.produced(1);
                }
            }
        }

        public void onError(Throwable th) {
            if (!this.once) {
                this.once = true;
                if (!this.allowFatal || (th instanceof Exception)) {
                    try {
                        Publisher publisher = (Publisher) this.nextSupplier.apply(th);
                        if (publisher == null) {
                            NullPointerException nullPointerException = new NullPointerException("Publisher is null");
                            nullPointerException.initCause(th);
                            this.actual.onError(nullPointerException);
                            return;
                        }
                        publisher.subscribe(this);
                    } catch (Throwable th2) {
                        Exceptions.throwIfFatal(th2);
                        this.actual.onError(new CompositeException(th, th2));
                    }
                } else {
                    this.actual.onError(th);
                }
            } else if (this.done) {
                RxJavaPlugins.onError(th);
            } else {
                this.actual.onError(th);
            }
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.once = true;
                this.actual.onComplete();
            }
        }
    }

    public FlowableOnErrorNext(Publisher<T> publisher, Function<? super Throwable, ? extends Publisher<? extends T>> function, boolean z) {
        super(publisher);
        this.nextSupplier = function;
        this.allowFatal = z;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> subscriber) {
        OnErrorNextSubscriber onErrorNextSubscriber = new OnErrorNextSubscriber(subscriber, this.nextSupplier, this.allowFatal);
        subscriber.onSubscribe(onErrorNextSubscriber.arbiter);
        this.source.subscribe(onErrorNextSubscriber);
    }
}
