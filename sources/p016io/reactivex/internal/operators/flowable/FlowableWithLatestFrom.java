package p016io.reactivex.internal.operators.flowable;

import java.util.concurrent.atomic.AtomicReference;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.BiFunction;
import p016io.reactivex.internal.subscriptions.EmptySubscription;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;
import p016io.reactivex.plugins.RxJavaPlugins;
import p016io.reactivex.subscribers.SerializedSubscriber;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableWithLatestFrom */
public final class FlowableWithLatestFrom<T, U, R> extends AbstractFlowableWithUpstream<T, R> {
    final BiFunction<? super T, ? super U, ? extends R> combiner;
    final Publisher<? extends U> other;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableWithLatestFrom$WithLatestFromSubscriber */
    static final class WithLatestFromSubscriber<T, U, R> extends AtomicReference<U> implements Subscriber<T>, Subscription {
        private static final long serialVersionUID = -312246233408980075L;
        final Subscriber<? super R> actual;
        final BiFunction<? super T, ? super U, ? extends R> combiner;
        final AtomicReference<Subscription> other = new AtomicReference<>();

        /* renamed from: s */
        final AtomicReference<Subscription> f357s = new AtomicReference<>();

        WithLatestFromSubscriber(Subscriber<? super R> subscriber, BiFunction<? super T, ? super U, ? extends R> biFunction) {
            this.actual = subscriber;
            this.combiner = biFunction;
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.setOnce(this.f357s, subscription)) {
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            Object obj = get();
            if (obj != null) {
                try {
                    this.actual.onNext(this.combiner.apply(t, obj));
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    cancel();
                    this.actual.onError(th);
                }
            }
        }

        public void onError(Throwable th) {
            SubscriptionHelper.cancel(this.other);
            this.actual.onError(th);
        }

        public void onComplete() {
            SubscriptionHelper.cancel(this.other);
            this.actual.onComplete();
        }

        public void request(long j) {
            ((Subscription) this.f357s.get()).request(j);
        }

        public void cancel() {
            ((Subscription) this.f357s.get()).cancel();
            SubscriptionHelper.cancel(this.other);
        }

        public boolean setOther(Subscription subscription) {
            return SubscriptionHelper.setOnce(this.other, subscription);
        }

        public void otherError(Throwable th) {
            if (this.f357s.compareAndSet(null, SubscriptionHelper.CANCELLED)) {
                EmptySubscription.error(th, this.actual);
            } else if (this.f357s.get() != SubscriptionHelper.CANCELLED) {
                cancel();
                this.actual.onError(th);
            } else {
                RxJavaPlugins.onError(th);
            }
        }
    }

    public FlowableWithLatestFrom(Publisher<T> publisher, BiFunction<? super T, ? super U, ? extends R> biFunction, Publisher<? extends U> publisher2) {
        super(publisher);
        this.combiner = biFunction;
        this.other = publisher2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super R> subscriber) {
        final WithLatestFromSubscriber withLatestFromSubscriber = new WithLatestFromSubscriber(new SerializedSubscriber(subscriber), this.combiner);
        this.other.subscribe(new Subscriber<U>() {
            public void onComplete() {
            }

            public void onSubscribe(Subscription subscription) {
                if (withLatestFromSubscriber.setOther(subscription)) {
                    subscription.request(LongCompanionObject.MAX_VALUE);
                }
            }

            public void onNext(U u) {
                withLatestFromSubscriber.lazySet(u);
            }

            public void onError(Throwable th) {
                withLatestFromSubscriber.otherError(th);
            }
        });
        this.source.subscribe(withLatestFromSubscriber);
    }
}
