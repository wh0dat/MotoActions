package p016io.reactivex.internal.operators.flowable;

import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.internal.fuseable.QueueSubscription;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableIgnoreElements */
public final class FlowableIgnoreElements<T> extends AbstractFlowableWithUpstream<T, T> {

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableIgnoreElements$IgnoreElementsSubscriber */
    static final class IgnoreElementsSubscriber<T> implements QueueSubscription<T>, Subscriber<T> {
        final Subscriber<? super T> actual;

        /* renamed from: s */
        Subscription f290s;

        public void clear() {
        }

        public boolean isEmpty() {
            return true;
        }

        public void onNext(T t) {
        }

        public T poll() {
            return null;
        }

        public void request(long j) {
        }

        public int requestFusion(int i) {
            return i & 2;
        }

        IgnoreElementsSubscriber(Subscriber<? super T> subscriber) {
            this.actual = subscriber;
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.f290s, subscription)) {
                this.f290s = subscription;
                this.actual.onSubscribe(this);
                subscription.request(LongCompanionObject.MAX_VALUE);
            }
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
        }

        public void onComplete() {
            this.actual.onComplete();
        }

        public boolean offer(T t) {
            throw new UnsupportedOperationException("Should not be called!");
        }

        public boolean offer(T t, T t2) {
            throw new UnsupportedOperationException("Should not be called!");
        }

        public void cancel() {
            this.f290s.cancel();
        }
    }

    public FlowableIgnoreElements(Publisher<T> publisher) {
        super(publisher);
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> subscriber) {
        this.source.subscribe(new IgnoreElementsSubscriber(subscriber));
    }
}
