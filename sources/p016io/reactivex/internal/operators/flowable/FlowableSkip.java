package p016io.reactivex.internal.operators.flowable;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableSkip */
public final class FlowableSkip<T> extends AbstractFlowableWithUpstream<T, T> {

    /* renamed from: n */
    final long f320n;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableSkip$SkipSubscriber */
    static final class SkipSubscriber<T> implements Subscriber<T>, Subscription {
        final Subscriber<? super T> actual;
        long remaining;

        /* renamed from: s */
        Subscription f321s;

        SkipSubscriber(Subscriber<? super T> subscriber, long j) {
            this.actual = subscriber;
            this.remaining = j;
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.f321s, subscription)) {
                long j = this.remaining;
                this.f321s = subscription;
                this.actual.onSubscribe(this);
                subscription.request(j);
            }
        }

        public void onNext(T t) {
            if (this.remaining != 0) {
                this.remaining--;
            } else {
                this.actual.onNext(t);
            }
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
        }

        public void onComplete() {
            this.actual.onComplete();
        }

        public void request(long j) {
            this.f321s.request(j);
        }

        public void cancel() {
            this.f321s.cancel();
        }
    }

    public FlowableSkip(Publisher<T> publisher, long j) {
        super(publisher);
        this.f320n = j;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> subscriber) {
        this.source.subscribe(new SkipSubscriber(subscriber, this.f320n));
    }
}
