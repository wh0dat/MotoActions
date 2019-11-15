package p016io.reactivex.internal.operators.flowable;

import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.internal.subscriptions.DeferredScalarSubscription;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableTakeLastOne */
public final class FlowableTakeLastOne<T> extends AbstractFlowableWithUpstream<T, T> {

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableTakeLastOne$TakeLastOneSubscriber */
    static final class TakeLastOneSubscriber<T> extends DeferredScalarSubscription<T> implements Subscriber<T> {
        private static final long serialVersionUID = -5467847744262967226L;

        /* renamed from: s */
        Subscription f329s;

        TakeLastOneSubscriber(Subscriber<? super T> subscriber) {
            super(subscriber);
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.f329s, subscription)) {
                this.f329s = subscription;
                this.actual.onSubscribe(this);
                subscription.request(LongCompanionObject.MAX_VALUE);
            }
        }

        public void onNext(T t) {
            this.value = t;
        }

        public void onError(Throwable th) {
            this.value = null;
            this.actual.onError(th);
        }

        public void onComplete() {
            Object obj = this.value;
            if (obj != null) {
                complete(obj);
            } else {
                this.actual.onComplete();
            }
        }

        public void cancel() {
            super.cancel();
            this.f329s.cancel();
        }
    }

    public FlowableTakeLastOne(Publisher<T> publisher) {
        super(publisher);
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> subscriber) {
        this.source.subscribe(new TakeLastOneSubscriber(subscriber));
    }
}
