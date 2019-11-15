package p016io.reactivex.internal.operators.flowable;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;
import p016io.reactivex.internal.util.EmptyComponent;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableDetach */
public final class FlowableDetach<T> extends AbstractFlowableWithUpstream<T, T> {

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableDetach$DetachSubscriber */
    static final class DetachSubscriber<T> implements Subscriber<T>, Subscription {
        Subscriber<? super T> actual;

        /* renamed from: s */
        Subscription f269s;

        DetachSubscriber(Subscriber<? super T> subscriber) {
            this.actual = subscriber;
        }

        public void request(long j) {
            this.f269s.request(j);
        }

        public void cancel() {
            Subscription subscription = this.f269s;
            this.f269s = EmptyComponent.INSTANCE;
            this.actual = EmptyComponent.asSubscriber();
            subscription.cancel();
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.f269s, subscription)) {
                this.f269s = subscription;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            this.actual.onNext(t);
        }

        public void onError(Throwable th) {
            Subscriber<? super T> subscriber = this.actual;
            this.f269s = EmptyComponent.INSTANCE;
            this.actual = EmptyComponent.asSubscriber();
            subscriber.onError(th);
        }

        public void onComplete() {
            Subscriber<? super T> subscriber = this.actual;
            this.f269s = EmptyComponent.INSTANCE;
            this.actual = EmptyComponent.asSubscriber();
            subscriber.onComplete();
        }
    }

    public FlowableDetach(Publisher<T> publisher) {
        super(publisher);
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> subscriber) {
        this.source.subscribe(new DetachSubscriber(subscriber));
    }
}
