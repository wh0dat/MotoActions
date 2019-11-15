package p016io.reactivex.internal.operators.flowable;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.internal.fuseable.ConditionalSubscriber;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;
import p016io.reactivex.internal.util.AtomicThrowable;
import p016io.reactivex.internal.util.HalfSerializer;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableSkipUntil */
public final class FlowableSkipUntil<T, U> extends AbstractFlowableWithUpstream<T, T> {
    final Publisher<U> other;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableSkipUntil$SkipUntilMainSubscriber */
    static final class SkipUntilMainSubscriber<T> extends AtomicInteger implements ConditionalSubscriber<T>, Subscription {
        private static final long serialVersionUID = -6270983465606289181L;
        final Subscriber<? super T> actual;
        final AtomicThrowable error = new AtomicThrowable();
        volatile boolean gate;
        final OtherSubscriber other = new OtherSubscriber<>();
        final AtomicLong requested = new AtomicLong();

        /* renamed from: s */
        final AtomicReference<Subscription> f324s = new AtomicReference<>();

        /* renamed from: io.reactivex.internal.operators.flowable.FlowableSkipUntil$SkipUntilMainSubscriber$OtherSubscriber */
        final class OtherSubscriber extends AtomicReference<Subscription> implements Subscriber<Object> {
            private static final long serialVersionUID = -5592042965931999169L;

            OtherSubscriber() {
            }

            public void onSubscribe(Subscription subscription) {
                if (SubscriptionHelper.setOnce(this, subscription)) {
                    subscription.request(LongCompanionObject.MAX_VALUE);
                }
            }

            public void onNext(Object obj) {
                SkipUntilMainSubscriber.this.gate = true;
                ((Subscription) get()).cancel();
            }

            public void onError(Throwable th) {
                SubscriptionHelper.cancel(SkipUntilMainSubscriber.this.f324s);
                HalfSerializer.onError(SkipUntilMainSubscriber.this.actual, th, (AtomicInteger) SkipUntilMainSubscriber.this, SkipUntilMainSubscriber.this.error);
            }

            public void onComplete() {
                SkipUntilMainSubscriber.this.gate = true;
            }
        }

        SkipUntilMainSubscriber(Subscriber<? super T> subscriber) {
            this.actual = subscriber;
        }

        public void onSubscribe(Subscription subscription) {
            SubscriptionHelper.deferredSetOnce(this.f324s, this.requested, subscription);
        }

        public void onNext(T t) {
            if (!tryOnNext(t)) {
                ((Subscription) this.f324s.get()).request(1);
            }
        }

        public boolean tryOnNext(T t) {
            if (!this.gate) {
                return false;
            }
            HalfSerializer.onNext(this.actual, t, (AtomicInteger) this, this.error);
            return true;
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
            SubscriptionHelper.deferredRequest(this.f324s, this.requested, j);
        }

        public void cancel() {
            SubscriptionHelper.cancel(this.f324s);
            SubscriptionHelper.cancel(this.other);
        }
    }

    public FlowableSkipUntil(Publisher<T> publisher, Publisher<U> publisher2) {
        super(publisher);
        this.other = publisher2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> subscriber) {
        SkipUntilMainSubscriber skipUntilMainSubscriber = new SkipUntilMainSubscriber(subscriber);
        subscriber.onSubscribe(skipUntilMainSubscriber);
        this.other.subscribe(skipUntilMainSubscriber.other);
        this.source.subscribe(skipUntilMainSubscriber);
    }
}
