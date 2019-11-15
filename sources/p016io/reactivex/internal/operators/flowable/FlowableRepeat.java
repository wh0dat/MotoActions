package p016io.reactivex.internal.operators.flowable;

import java.util.concurrent.atomic.AtomicInteger;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.internal.subscriptions.SubscriptionArbiter;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableRepeat */
public final class FlowableRepeat<T> extends AbstractFlowableWithUpstream<T, T> {
    final long count;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableRepeat$RepeatSubscriber */
    static final class RepeatSubscriber<T> extends AtomicInteger implements Subscriber<T> {
        private static final long serialVersionUID = -7098360935104053232L;
        final Subscriber<? super T> actual;
        long remaining;

        /* renamed from: sa */
        final SubscriptionArbiter f306sa;
        final Publisher<? extends T> source;

        RepeatSubscriber(Subscriber<? super T> subscriber, long j, SubscriptionArbiter subscriptionArbiter, Publisher<? extends T> publisher) {
            this.actual = subscriber;
            this.f306sa = subscriptionArbiter;
            this.source = publisher;
            this.remaining = j;
        }

        public void onSubscribe(Subscription subscription) {
            this.f306sa.setSubscription(subscription);
        }

        public void onNext(T t) {
            this.actual.onNext(t);
            this.f306sa.produced(1);
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
        }

        public void onComplete() {
            long j = this.remaining;
            if (j != LongCompanionObject.MAX_VALUE) {
                this.remaining = j - 1;
            }
            if (j != 0) {
                subscribeNext();
            } else {
                this.actual.onComplete();
            }
        }

        /* access modifiers changed from: 0000 */
        public void subscribeNext() {
            if (getAndIncrement() == 0) {
                int i = 1;
                while (!this.f306sa.isCancelled()) {
                    this.source.subscribe(this);
                    i = addAndGet(-i);
                    if (i == 0) {
                    }
                }
            }
        }
    }

    public FlowableRepeat(Publisher<T> publisher, long j) {
        super(publisher);
        this.count = j;
    }

    public void subscribeActual(Subscriber<? super T> subscriber) {
        SubscriptionArbiter subscriptionArbiter = new SubscriptionArbiter();
        subscriber.onSubscribe(subscriptionArbiter);
        long j = this.count;
        long j2 = LongCompanionObject.MAX_VALUE;
        if (j != LongCompanionObject.MAX_VALUE) {
            j2 = this.count - 1;
        }
        RepeatSubscriber repeatSubscriber = new RepeatSubscriber(subscriber, j2, subscriptionArbiter, this.source);
        repeatSubscriber.subscribeNext();
    }
}
