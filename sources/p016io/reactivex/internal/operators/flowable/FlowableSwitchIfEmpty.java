package p016io.reactivex.internal.operators.flowable;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.internal.subscriptions.SubscriptionArbiter;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableSwitchIfEmpty */
public final class FlowableSwitchIfEmpty<T> extends AbstractFlowableWithUpstream<T, T> {
    final Publisher<? extends T> other;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableSwitchIfEmpty$SwitchIfEmptySubscriber */
    static final class SwitchIfEmptySubscriber<T> implements Subscriber<T> {
        final Subscriber<? super T> actual;
        final SubscriptionArbiter arbiter = new SubscriptionArbiter();
        boolean empty = true;
        final Publisher<? extends T> other;

        SwitchIfEmptySubscriber(Subscriber<? super T> subscriber, Publisher<? extends T> publisher) {
            this.actual = subscriber;
            this.other = publisher;
        }

        public void onSubscribe(Subscription subscription) {
            this.arbiter.setSubscription(subscription);
        }

        public void onNext(T t) {
            if (this.empty) {
                this.empty = false;
            }
            this.actual.onNext(t);
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
        }

        public void onComplete() {
            if (this.empty) {
                this.empty = false;
                this.other.subscribe(this);
                return;
            }
            this.actual.onComplete();
        }
    }

    public FlowableSwitchIfEmpty(Publisher<T> publisher, Publisher<? extends T> publisher2) {
        super(publisher);
        this.other = publisher2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> subscriber) {
        SwitchIfEmptySubscriber switchIfEmptySubscriber = new SwitchIfEmptySubscriber(subscriber, this.other);
        subscriber.onSubscribe(switchIfEmptySubscriber.arbiter);
        this.source.subscribe(switchIfEmptySubscriber);
    }
}
