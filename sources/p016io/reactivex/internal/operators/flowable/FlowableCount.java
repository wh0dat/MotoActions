package p016io.reactivex.internal.operators.flowable;

import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.internal.subscriptions.DeferredScalarSubscription;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableCount */
public final class FlowableCount<T> extends AbstractFlowableWithUpstream<T, Long> {

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableCount$CountSubscriber */
    static final class CountSubscriber extends DeferredScalarSubscription<Long> implements Subscriber<Object> {
        private static final long serialVersionUID = 4973004223787171406L;
        long count;

        /* renamed from: s */
        Subscription f262s;

        CountSubscriber(Subscriber<? super Long> subscriber) {
            super(subscriber);
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.f262s, subscription)) {
                this.f262s = subscription;
                this.actual.onSubscribe(this);
                subscription.request(LongCompanionObject.MAX_VALUE);
            }
        }

        public void onNext(Object obj) {
            this.count++;
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
        }

        public void onComplete() {
            complete(Long.valueOf(this.count));
        }

        public void cancel() {
            super.cancel();
            this.f262s.cancel();
        }
    }

    public FlowableCount(Publisher<T> publisher) {
        super(publisher);
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super Long> subscriber) {
        this.source.subscribe(new CountSubscriber(subscriber));
    }
}
