package p016io.reactivex.internal.operators.flowable;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;
import p016io.reactivex.internal.util.AtomicThrowable;
import p016io.reactivex.internal.util.HalfSerializer;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableTakeUntil */
public final class FlowableTakeUntil<T, U> extends AbstractFlowableWithUpstream<T, T> {
    final Publisher<? extends U> other;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableTakeUntil$TakeUntilMainSubscriber */
    static final class TakeUntilMainSubscriber<T> extends AtomicInteger implements Subscriber<T>, Subscription {
        private static final long serialVersionUID = -4945480365982832967L;
        final Subscriber<? super T> actual;
        final AtomicThrowable error = new AtomicThrowable();
        final OtherSubscriber other = new OtherSubscriber<>();
        final AtomicLong requested = new AtomicLong();

        /* renamed from: s */
        final AtomicReference<Subscription> f331s = new AtomicReference<>();

        /* renamed from: io.reactivex.internal.operators.flowable.FlowableTakeUntil$TakeUntilMainSubscriber$OtherSubscriber */
        final class OtherSubscriber extends AtomicReference<Subscription> implements Subscriber<Object> {
            private static final long serialVersionUID = -3592821756711087922L;

            OtherSubscriber() {
            }

            public void onSubscribe(Subscription subscription) {
                if (SubscriptionHelper.setOnce(this, subscription)) {
                    subscription.request(LongCompanionObject.MAX_VALUE);
                }
            }

            public void onNext(Object obj) {
                SubscriptionHelper.cancel(this);
                onComplete();
            }

            public void onError(Throwable th) {
                SubscriptionHelper.cancel(TakeUntilMainSubscriber.this.f331s);
                HalfSerializer.onError(TakeUntilMainSubscriber.this.actual, th, (AtomicInteger) TakeUntilMainSubscriber.this, TakeUntilMainSubscriber.this.error);
            }

            public void onComplete() {
                SubscriptionHelper.cancel(TakeUntilMainSubscriber.this.f331s);
                HalfSerializer.onComplete(TakeUntilMainSubscriber.this.actual, (AtomicInteger) TakeUntilMainSubscriber.this, TakeUntilMainSubscriber.this.error);
            }
        }

        TakeUntilMainSubscriber(Subscriber<? super T> subscriber) {
            this.actual = subscriber;
        }

        public void onSubscribe(Subscription subscription) {
            SubscriptionHelper.deferredSetOnce(this.f331s, this.requested, subscription);
        }

        public void onNext(T t) {
            HalfSerializer.onNext(this.actual, t, (AtomicInteger) this, this.error);
        }

        public void onError(Throwable th) {
            SubscriptionHelper.cancel(this.other);
            HalfSerializer.onError(this.actual, th, (AtomicInteger) this, this.error);
        }

        public void onComplete() {
            SubscriptionHelper.cancel(this.other);
            HalfSerializer.onComplete(this.actual, (AtomicInteger) this, this.error);
        }

        public void request(long j) {
            SubscriptionHelper.deferredRequest(this.f331s, this.requested, j);
        }

        public void cancel() {
            SubscriptionHelper.cancel(this.f331s);
            SubscriptionHelper.cancel(this.other);
        }
    }

    public FlowableTakeUntil(Publisher<T> publisher, Publisher<? extends U> publisher2) {
        super(publisher);
        this.other = publisher2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> subscriber) {
        TakeUntilMainSubscriber takeUntilMainSubscriber = new TakeUntilMainSubscriber(subscriber);
        subscriber.onSubscribe(takeUntilMainSubscriber);
        this.other.subscribe(takeUntilMainSubscriber.other);
        this.source.subscribe(takeUntilMainSubscriber);
    }
}
