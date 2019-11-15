package p016io.reactivex.subscribers;

import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;

/* renamed from: io.reactivex.subscribers.DefaultSubscriber */
public abstract class DefaultSubscriber<T> implements Subscriber<T> {

    /* renamed from: s */
    private Subscription f567s;

    public final void onSubscribe(Subscription subscription) {
        if (SubscriptionHelper.validate(this.f567s, subscription)) {
            this.f567s = subscription;
            onStart();
        }
    }

    /* access modifiers changed from: protected */
    public final void request(long j) {
        Subscription subscription = this.f567s;
        if (subscription != null) {
            subscription.request(j);
        }
    }

    /* access modifiers changed from: protected */
    public final void cancel() {
        Subscription subscription = this.f567s;
        this.f567s = SubscriptionHelper.CANCELLED;
        subscription.cancel();
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        request(LongCompanionObject.MAX_VALUE);
    }
}
