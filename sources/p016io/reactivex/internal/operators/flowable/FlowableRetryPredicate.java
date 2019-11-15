package p016io.reactivex.internal.operators.flowable;

import java.util.concurrent.atomic.AtomicInteger;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.exceptions.CompositeException;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.Predicate;
import p016io.reactivex.internal.subscriptions.SubscriptionArbiter;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableRetryPredicate */
public final class FlowableRetryPredicate<T> extends AbstractFlowableWithUpstream<T, T> {
    final long count;
    final Predicate<? super Throwable> predicate;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableRetryPredicate$RepeatSubscriber */
    static final class RepeatSubscriber<T> extends AtomicInteger implements Subscriber<T> {
        private static final long serialVersionUID = -7098360935104053232L;
        final Subscriber<? super T> actual;
        final Predicate<? super Throwable> predicate;
        long remaining;

        /* renamed from: sa */
        final SubscriptionArbiter f309sa;
        final Publisher<? extends T> source;

        RepeatSubscriber(Subscriber<? super T> subscriber, long j, Predicate<? super Throwable> predicate2, SubscriptionArbiter subscriptionArbiter, Publisher<? extends T> publisher) {
            this.actual = subscriber;
            this.f309sa = subscriptionArbiter;
            this.source = publisher;
            this.predicate = predicate2;
            this.remaining = j;
        }

        public void onSubscribe(Subscription subscription) {
            this.f309sa.setSubscription(subscription);
        }

        public void onNext(T t) {
            this.actual.onNext(t);
            this.f309sa.produced(1);
        }

        public void onError(Throwable th) {
            long j = this.remaining;
            if (j != LongCompanionObject.MAX_VALUE) {
                this.remaining = j - 1;
            }
            if (j == 0) {
                this.actual.onError(th);
            } else {
                try {
                    if (!this.predicate.test(th)) {
                        this.actual.onError(th);
                        return;
                    }
                    subscribeNext();
                } catch (Throwable th2) {
                    Exceptions.throwIfFatal(th2);
                    this.actual.onError(new CompositeException(th, th2));
                }
            }
        }

        public void onComplete() {
            this.actual.onComplete();
        }

        /* access modifiers changed from: 0000 */
        public void subscribeNext() {
            if (getAndIncrement() == 0) {
                int i = 1;
                while (!this.f309sa.isCancelled()) {
                    this.source.subscribe(this);
                    i = addAndGet(-i);
                    if (i == 0) {
                    }
                }
            }
        }
    }

    public FlowableRetryPredicate(Publisher<T> publisher, long j, Predicate<? super Throwable> predicate2) {
        super(publisher);
        this.predicate = predicate2;
        this.count = j;
    }

    public void subscribeActual(Subscriber<? super T> subscriber) {
        SubscriptionArbiter subscriptionArbiter = new SubscriptionArbiter();
        subscriber.onSubscribe(subscriptionArbiter);
        RepeatSubscriber repeatSubscriber = new RepeatSubscriber(subscriber, this.count, this.predicate, subscriptionArbiter, this.source);
        repeatSubscriber.subscribeNext();
    }
}
