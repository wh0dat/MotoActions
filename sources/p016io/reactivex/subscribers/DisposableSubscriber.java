package p016io.reactivex.subscribers;

import java.util.concurrent.atomic.AtomicReference;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;

/* renamed from: io.reactivex.subscribers.DisposableSubscriber */
public abstract class DisposableSubscriber<T> implements Subscriber<T>, Disposable {

    /* renamed from: s */
    final AtomicReference<Subscription> f568s = new AtomicReference<>();

    public final void onSubscribe(Subscription subscription) {
        if (SubscriptionHelper.setOnce(this.f568s, subscription)) {
            onStart();
        }
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        ((Subscription) this.f568s.get()).request(LongCompanionObject.MAX_VALUE);
    }

    /* access modifiers changed from: protected */
    public final void request(long j) {
        ((Subscription) this.f568s.get()).request(j);
    }

    /* access modifiers changed from: protected */
    public final void cancel() {
        dispose();
    }

    public final boolean isDisposed() {
        return this.f568s.get() == SubscriptionHelper.CANCELLED;
    }

    public final void dispose() {
        SubscriptionHelper.cancel(this.f568s);
    }
}
