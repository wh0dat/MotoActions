package p016io.reactivex.internal.operators.flowable;

import java.util.concurrent.Callable;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.BiFunction;
import p016io.reactivex.internal.functions.ObjectHelper;
import p016io.reactivex.internal.subscribers.SinglePostCompleteSubscriber;
import p016io.reactivex.internal.subscriptions.EmptySubscription;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableScanSeed */
public final class FlowableScanSeed<T, R> extends AbstractFlowableWithUpstream<T, R> {
    final BiFunction<R, ? super T, R> accumulator;
    final Callable<R> seedSupplier;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableScanSeed$ScanSeedSubscriber */
    static final class ScanSeedSubscriber<T, R> extends SinglePostCompleteSubscriber<T, R> {
        private static final long serialVersionUID = -1776795561228106469L;
        final BiFunction<R, ? super T, R> accumulator;

        ScanSeedSubscriber(Subscriber<? super R> subscriber, BiFunction<R, ? super T, R> biFunction, R r) {
            super(subscriber);
            this.accumulator = biFunction;
            this.value = r;
        }

        public void onNext(T t) {
            Object obj = this.value;
            try {
                this.value = ObjectHelper.requireNonNull(this.accumulator.apply(obj, t), "The accumulator returned a null value");
                this.produced++;
                this.actual.onNext(obj);
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                this.f549s.cancel();
                onError(th);
            }
        }

        public void onError(Throwable th) {
            this.value = null;
            this.actual.onError(th);
        }

        public void onComplete() {
            complete(this.value);
        }
    }

    public FlowableScanSeed(Publisher<T> publisher, Callable<R> callable, BiFunction<R, ? super T, R> biFunction) {
        super(publisher);
        this.accumulator = biFunction;
        this.seedSupplier = callable;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super R> subscriber) {
        try {
            this.source.subscribe(new ScanSeedSubscriber(subscriber, this.accumulator, ObjectHelper.requireNonNull(this.seedSupplier.call(), "The seed supplied is null")));
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            EmptySubscription.error(th, subscriber);
        }
    }
}
