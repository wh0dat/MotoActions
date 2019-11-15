package p016io.reactivex.internal.operators.flowable;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableHide */
public final class FlowableHide<T> extends AbstractFlowableWithUpstream<T, T> {

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableHide$HideSubscriber */
    static final class HideSubscriber<T> implements Subscriber<T>, Subscription {
        final Subscriber<? super T> actual;

        /* renamed from: s */
        Subscription f289s;

        HideSubscriber(Subscriber<? super T> subscriber) {
            this.actual = subscriber;
        }

        public void request(long j) {
            this.f289s.request(j);
        }

        public void cancel() {
            this.f289s.cancel();
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.f289s, subscription)) {
                this.f289s = subscription;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            this.actual.onNext(t);
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
        }

        public void onComplete() {
            this.actual.onComplete();
        }
    }

    public FlowableHide(Publisher<T> publisher) {
        super(publisher);
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> subscriber) {
        this.source.subscribe(new HideSubscriber(subscriber));
    }
}
