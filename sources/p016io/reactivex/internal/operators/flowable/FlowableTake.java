package p016io.reactivex.internal.operators.flowable;

import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.internal.subscriptions.EmptySubscription;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableTake */
public final class FlowableTake<T> extends AbstractFlowableWithUpstream<T, T> {
    final long limit;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableTake$TakeSubscriber */
    static final class TakeSubscriber<T> extends AtomicBoolean implements Subscriber<T>, Subscription {
        private static final long serialVersionUID = -5636543848937116287L;
        final Subscriber<? super T> actual;
        boolean done;
        final long limit;
        long remaining;
        Subscription subscription;

        TakeSubscriber(Subscriber<? super T> subscriber, long j) {
            this.actual = subscriber;
            this.limit = j;
            this.remaining = j;
        }

        public void onSubscribe(Subscription subscription2) {
            if (SubscriptionHelper.validate(this.subscription, subscription2)) {
                this.subscription = subscription2;
                if (this.limit == 0) {
                    subscription2.cancel();
                    this.done = true;
                    EmptySubscription.complete(this.actual);
                    return;
                }
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                long j = this.remaining;
                this.remaining = j - 1;
                if (j > 0) {
                    boolean z = this.remaining == 0;
                    this.actual.onNext(t);
                    if (z) {
                        this.subscription.cancel();
                        onComplete();
                    }
                }
            }
        }

        public void onError(Throwable th) {
            if (!this.done) {
                this.done = true;
                this.subscription.cancel();
                this.actual.onError(th);
            }
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.actual.onComplete();
            }
        }

        public void request(long j) {
            if (SubscriptionHelper.validate(j)) {
                if (get() || !compareAndSet(false, true) || j < this.limit) {
                    this.subscription.request(j);
                } else {
                    this.subscription.request(LongCompanionObject.MAX_VALUE);
                }
            }
        }

        public void cancel() {
            this.subscription.cancel();
        }
    }

    public FlowableTake(Publisher<T> publisher, long j) {
        super(publisher);
        this.limit = j;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> subscriber) {
        this.source.subscribe(new TakeSubscriber(subscriber, this.limit));
    }
}
