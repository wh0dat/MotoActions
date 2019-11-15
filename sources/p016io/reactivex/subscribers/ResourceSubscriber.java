package p016io.reactivex.subscribers;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.disposables.ListCompositeDisposable;
import p016io.reactivex.internal.functions.ObjectHelper;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;

/* renamed from: io.reactivex.subscribers.ResourceSubscriber */
public abstract class ResourceSubscriber<T> implements Subscriber<T>, Disposable {
    private final AtomicLong missedRequested = new AtomicLong();
    private final ListCompositeDisposable resources = new ListCompositeDisposable();

    /* renamed from: s */
    private final AtomicReference<Subscription> f569s = new AtomicReference<>();

    public final void add(Disposable disposable) {
        ObjectHelper.requireNonNull(disposable, "resource is null");
        this.resources.add(disposable);
    }

    public final void onSubscribe(Subscription subscription) {
        if (SubscriptionHelper.deferredSetOnce(this.f569s, this.missedRequested, subscription)) {
            onStart();
        }
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        request(LongCompanionObject.MAX_VALUE);
    }

    /* access modifiers changed from: protected */
    public final void request(long j) {
        SubscriptionHelper.deferredRequest(this.f569s, this.missedRequested, j);
    }

    public final void dispose() {
        if (SubscriptionHelper.cancel(this.f569s)) {
            this.resources.dispose();
        }
    }

    public final boolean isDisposed() {
        return SubscriptionHelper.isCancelled((Subscription) this.f569s.get());
    }
}
