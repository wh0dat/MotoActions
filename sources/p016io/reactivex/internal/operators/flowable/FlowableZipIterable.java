package p016io.reactivex.internal.operators.flowable;

import java.util.Iterator;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.Flowable;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.BiFunction;
import p016io.reactivex.internal.functions.ObjectHelper;
import p016io.reactivex.internal.subscriptions.EmptySubscription;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableZipIterable */
public final class FlowableZipIterable<T, U, V> extends Flowable<V> {
    final Iterable<U> other;
    final Publisher<? extends T> source;
    final BiFunction<? super T, ? super U, ? extends V> zipper;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableZipIterable$ZipIterableSubscriber */
    static final class ZipIterableSubscriber<T, U, V> implements Subscriber<T>, Subscription {
        final Subscriber<? super V> actual;
        boolean done;
        final Iterator<U> iterator;

        /* renamed from: s */
        Subscription f359s;
        final BiFunction<? super T, ? super U, ? extends V> zipper;

        ZipIterableSubscriber(Subscriber<? super V> subscriber, Iterator<U> it, BiFunction<? super T, ? super U, ? extends V> biFunction) {
            this.actual = subscriber;
            this.iterator = it;
            this.zipper = biFunction;
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.f359s, subscription)) {
                this.f359s = subscription;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                try {
                    try {
                        this.actual.onNext(ObjectHelper.requireNonNull(this.zipper.apply(t, ObjectHelper.requireNonNull(this.iterator.next(), "The iterator returned a null value")), "The zipper function returned a null value"));
                        try {
                            if (!this.iterator.hasNext()) {
                                this.done = true;
                                this.f359s.cancel();
                                this.actual.onComplete();
                            }
                        } catch (Throwable th) {
                            error(th);
                        }
                    } catch (Throwable th2) {
                        error(th2);
                    }
                } catch (Throwable th3) {
                    error(th3);
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void error(Throwable th) {
            Exceptions.throwIfFatal(th);
            this.done = true;
            this.f359s.cancel();
            this.actual.onError(th);
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            this.actual.onError(th);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.actual.onComplete();
            }
        }

        public void request(long j) {
            this.f359s.request(j);
        }

        public void cancel() {
            this.f359s.cancel();
        }
    }

    public FlowableZipIterable(Publisher<? extends T> publisher, Iterable<U> iterable, BiFunction<? super T, ? super U, ? extends V> biFunction) {
        this.source = publisher;
        this.other = iterable;
        this.zipper = biFunction;
    }

    public void subscribeActual(Subscriber<? super V> subscriber) {
        try {
            Iterator it = (Iterator) ObjectHelper.requireNonNull(this.other.iterator(), "The iterator returned by other is null");
            try {
                if (!it.hasNext()) {
                    EmptySubscription.complete(subscriber);
                } else {
                    this.source.subscribe(new ZipIterableSubscriber(subscriber, it, this.zipper));
                }
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                EmptySubscription.error(th, subscriber);
            }
        } catch (Throwable th2) {
            Exceptions.throwIfFatal(th2);
            EmptySubscription.error(th2, subscriber);
        }
    }
}
