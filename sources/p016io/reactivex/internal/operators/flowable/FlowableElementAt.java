package p016io.reactivex.internal.operators.flowable;

import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.internal.subscriptions.DeferredScalarSubscription;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableElementAt */
public final class FlowableElementAt<T> extends AbstractFlowableWithUpstream<T, T> {
    final T defaultValue;
    final long index;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableElementAt$ElementAtSubscriber */
    static final class ElementAtSubscriber<T> extends DeferredScalarSubscription<T> implements Subscriber<T> {
        private static final long serialVersionUID = 4066607327284737757L;
        long count;
        final T defaultValue;
        boolean done;
        final long index;

        /* renamed from: s */
        Subscription f275s;

        ElementAtSubscriber(Subscriber<? super T> subscriber, long j, T t) {
            super(subscriber);
            this.index = j;
            this.defaultValue = t;
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.f275s, subscription)) {
                this.f275s = subscription;
                this.actual.onSubscribe(this);
                subscription.request(LongCompanionObject.MAX_VALUE);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                long j = this.count;
                if (j == this.index) {
                    this.done = true;
                    this.f275s.cancel();
                    complete(t);
                    return;
                }
                this.count = j + 1;
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
                T t = this.defaultValue;
                if (t == null) {
                    this.actual.onComplete();
                } else {
                    complete(t);
                }
            }
        }

        public void cancel() {
            super.cancel();
            this.f275s.cancel();
        }
    }

    public FlowableElementAt(Publisher<T> publisher, long j, T t) {
        super(publisher);
        this.index = j;
        this.defaultValue = t;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> subscriber) {
        this.source.subscribe(new ElementAtSubscriber(subscriber, this.index, this.defaultValue));
    }
}
