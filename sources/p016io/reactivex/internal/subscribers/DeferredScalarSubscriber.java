package p016io.reactivex.internal.subscribers;

import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.internal.subscriptions.DeferredScalarSubscription;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;

/* renamed from: io.reactivex.internal.subscribers.DeferredScalarSubscriber */
public abstract class DeferredScalarSubscriber<T, R> extends DeferredScalarSubscription<R> implements Subscriber<T> {
    private static final long serialVersionUID = 2984505488220891551L;
    protected boolean hasValue;

    /* renamed from: s */
    protected Subscription f528s;

    public DeferredScalarSubscriber(Subscriber<? super R> subscriber) {
        super(subscriber);
    }

    public void onSubscribe(Subscription subscription) {
        if (SubscriptionHelper.validate(this.f528s, subscription)) {
            this.f528s = subscription;
            this.actual.onSubscribe(this);
            subscription.request(LongCompanionObject.MAX_VALUE);
        }
    }

    public void onError(Throwable th) {
        this.value = null;
        this.actual.onError(th);
    }

    public void onComplete() {
        if (this.hasValue) {
            complete(this.value);
        } else {
            this.actual.onComplete();
        }
    }

    public void cancel() {
        super.cancel();
        this.f528s.cancel();
    }
}
