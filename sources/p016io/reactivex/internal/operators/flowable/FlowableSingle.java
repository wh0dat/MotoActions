package p016io.reactivex.internal.operators.flowable;

import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.internal.subscriptions.DeferredScalarSubscription;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableSingle */
public final class FlowableSingle<T> extends AbstractFlowableWithUpstream<T, T> {
    final T defaultValue;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableSingle$SingleElementSubscriber */
    static final class SingleElementSubscriber<T> extends DeferredScalarSubscription<T> implements Subscriber<T> {
        private static final long serialVersionUID = -5526049321428043809L;
        final T defaultValue;
        boolean done;

        /* renamed from: s */
        Subscription f317s;

        SingleElementSubscriber(Subscriber<? super T> subscriber, T t) {
            super(subscriber);
            this.defaultValue = t;
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.f317s, subscription)) {
                this.f317s = subscription;
                this.actual.onSubscribe(this);
                subscription.request(LongCompanionObject.MAX_VALUE);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                if (this.value != null) {
                    this.done = true;
                    this.f317s.cancel();
                    this.actual.onError(new IllegalArgumentException("Sequence contains more than one element!"));
                    return;
                }
                this.value = t;
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            this.actual.onError(th);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                T t = this.value;
                this.value = null;
                if (t == null) {
                    t = this.defaultValue;
                }
                if (t == null) {
                    this.actual.onComplete();
                } else {
                    complete(t);
                }
            }
        }

        public void cancel() {
            super.cancel();
            this.f317s.cancel();
        }
    }

    public FlowableSingle(Publisher<T> publisher, T t) {
        super(publisher);
        this.defaultValue = t;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> subscriber) {
        this.source.subscribe(new SingleElementSubscriber(subscriber, this.defaultValue));
    }
}
