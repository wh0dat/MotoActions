package p016io.reactivex.internal.subscribers;

import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;

/* renamed from: io.reactivex.internal.subscribers.SubscriberResourceWrapper */
public final class SubscriberResourceWrapper<T> extends AtomicReference<Disposable> implements Subscriber<T>, Disposable, Subscription {
    private static final long serialVersionUID = -8612022020200669122L;
    final Subscriber<? super T> actual;
    final AtomicReference<Subscription> subscription = new AtomicReference<>();

    public SubscriberResourceWrapper(Subscriber<? super T> subscriber) {
        this.actual = subscriber;
    }

    public void onSubscribe(Subscription subscription2) {
        do {
            Subscription subscription3 = (Subscription) this.subscription.get();
            if (subscription3 == SubscriptionHelper.CANCELLED) {
                subscription2.cancel();
                return;
            } else if (subscription3 != null) {
                subscription2.cancel();
                SubscriptionHelper.reportSubscriptionSet();
                return;
            }
        } while (!this.subscription.compareAndSet(null, subscription2));
        this.actual.onSubscribe(this);
    }

    public void onNext(T t) {
        this.actual.onNext(t);
    }

    public void onError(Throwable th) {
        dispose();
        this.actual.onError(th);
    }

    public void onComplete() {
        dispose();
        this.actual.onComplete();
    }

    public void request(long j) {
        if (SubscriptionHelper.validate(j)) {
            ((Subscription) this.subscription.get()).request(j);
        }
    }

    public void dispose() {
        SubscriptionHelper.cancel(this.subscription);
        DisposableHelper.dispose(this);
    }

    public boolean isDisposed() {
        return this.subscription.get() == SubscriptionHelper.CANCELLED;
    }

    public void cancel() {
        dispose();
    }

    public void setResource(Disposable disposable) {
        DisposableHelper.set(this, disposable);
    }
}
