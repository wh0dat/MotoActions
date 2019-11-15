package p016io.reactivex.internal.operators.flowable;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.BiFunction;
import p016io.reactivex.internal.functions.ObjectHelper;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableScan */
public final class FlowableScan<T> extends AbstractFlowableWithUpstream<T, T> {
    final BiFunction<T, T, T> accumulator;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableScan$ScanSubscriber */
    static final class ScanSubscriber<T> implements Subscriber<T>, Subscription {
        final BiFunction<T, T, T> accumulator;
        final Subscriber<? super T> actual;

        /* renamed from: s */
        Subscription f312s;
        T value;

        ScanSubscriber(Subscriber<? super T> subscriber, BiFunction<T, T, T> biFunction) {
            this.actual = subscriber;
            this.accumulator = biFunction;
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.f312s, subscription)) {
                this.f312s = subscription;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            Subscriber<? super T> subscriber = this.actual;
            T t2 = this.value;
            if (t2 == null) {
                this.value = t;
                subscriber.onNext(t);
            } else {
                try {
                    T requireNonNull = ObjectHelper.requireNonNull(this.accumulator.apply(t2, t), "The value returned by the accumulator is null");
                    this.value = requireNonNull;
                    subscriber.onNext(requireNonNull);
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    this.f312s.cancel();
                    subscriber.onError(th);
                }
            }
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
        }

        public void onComplete() {
            this.actual.onComplete();
        }

        public void request(long j) {
            this.f312s.request(j);
        }

        public void cancel() {
            this.f312s.cancel();
        }
    }

    public FlowableScan(Publisher<T> publisher, BiFunction<T, T, T> biFunction) {
        super(publisher);
        this.accumulator = biFunction;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> subscriber) {
        this.source.subscribe(new ScanSubscriber(subscriber, this.accumulator));
    }
}
