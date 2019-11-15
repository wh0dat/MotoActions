package p016io.reactivex.internal.subscribers;

import java.util.NoSuchElementException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.subscribers.FutureSubscriber */
public final class FutureSubscriber<T> extends CountDownLatch implements Subscriber<T>, Future<T>, Subscription {
    Throwable error;

    /* renamed from: s */
    final AtomicReference<Subscription> f530s = new AtomicReference<>();
    T value;

    public void cancel() {
    }

    public void request(long j) {
    }

    public FutureSubscriber() {
        super(1);
    }

    public boolean cancel(boolean z) {
        Subscription subscription;
        do {
            subscription = (Subscription) this.f530s.get();
            if (subscription == this || subscription == SubscriptionHelper.CANCELLED) {
                return false;
            }
        } while (!this.f530s.compareAndSet(subscription, SubscriptionHelper.CANCELLED));
        if (subscription != null) {
            subscription.cancel();
        }
        countDown();
        return true;
    }

    public boolean isCancelled() {
        return SubscriptionHelper.isCancelled((Subscription) this.f530s.get());
    }

    public boolean isDone() {
        return getCount() == 0;
    }

    public T get() throws InterruptedException, ExecutionException {
        if (getCount() != 0) {
            await();
        }
        if (isCancelled()) {
            throw new CancellationException();
        }
        Throwable th = this.error;
        if (th == null) {
            return this.value;
        }
        throw new ExecutionException(th);
    }

    public T get(long j, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
        if (getCount() != 0 && !await(j, timeUnit)) {
            throw new TimeoutException();
        } else if (isCancelled()) {
            throw new CancellationException();
        } else {
            Throwable th = this.error;
            if (th == null) {
                return this.value;
            }
            throw new ExecutionException(th);
        }
    }

    public void onSubscribe(Subscription subscription) {
        if (SubscriptionHelper.setOnce(this.f530s, subscription)) {
            subscription.request(LongCompanionObject.MAX_VALUE);
        }
    }

    public void onNext(T t) {
        if (this.value != null) {
            ((Subscription) this.f530s.get()).cancel();
            onError(new IndexOutOfBoundsException("More than one element received"));
            return;
        }
        this.value = t;
    }

    public void onError(Throwable th) {
        Subscription subscription;
        do {
            subscription = (Subscription) this.f530s.get();
            if (subscription == this || subscription == SubscriptionHelper.CANCELLED) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.error = th;
        } while (!this.f530s.compareAndSet(subscription, this));
        countDown();
    }

    public void onComplete() {
        Subscription subscription;
        if (this.value == null) {
            onError(new NoSuchElementException("The source is empty"));
            return;
        }
        do {
            subscription = (Subscription) this.f530s.get();
            if (subscription == this || subscription == SubscriptionHelper.CANCELLED) {
                return;
            }
        } while (!this.f530s.compareAndSet(subscription, this));
        countDown();
    }
}
