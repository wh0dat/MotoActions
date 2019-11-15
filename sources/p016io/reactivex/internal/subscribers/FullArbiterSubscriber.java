package p016io.reactivex.internal.subscribers;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.internal.subscriptions.FullArbiter;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;

/* renamed from: io.reactivex.internal.subscribers.FullArbiterSubscriber */
public final class FullArbiterSubscriber<T> implements Subscriber<T> {
    final FullArbiter<T> arbiter;

    /* renamed from: s */
    Subscription f529s;

    public FullArbiterSubscriber(FullArbiter<T> fullArbiter) {
        this.arbiter = fullArbiter;
    }

    public void onSubscribe(Subscription subscription) {
        if (SubscriptionHelper.validate(this.f529s, subscription)) {
            this.f529s = subscription;
            this.arbiter.setSubscription(subscription);
        }
    }

    public void onNext(T t) {
        this.arbiter.onNext(t, this.f529s);
    }

    public void onError(Throwable th) {
        this.arbiter.onError(th, this.f529s);
    }

    public void onComplete() {
        this.arbiter.onComplete(this.f529s);
    }
}
