package p016io.reactivex.internal.subscribers;

import java.util.concurrent.CountDownLatch;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;
import p016io.reactivex.internal.util.ExceptionHelper;

/* renamed from: io.reactivex.internal.subscribers.BlockingBaseSubscriber */
public abstract class BlockingBaseSubscriber<T> extends CountDownLatch implements Subscriber<T> {
    volatile boolean cancelled;
    Throwable error;

    /* renamed from: s */
    Subscription f527s;
    T value;

    public BlockingBaseSubscriber() {
        super(1);
    }

    public final void onSubscribe(Subscription subscription) {
        if (SubscriptionHelper.validate(this.f527s, subscription)) {
            this.f527s = subscription;
            if (!this.cancelled) {
                subscription.request(LongCompanionObject.MAX_VALUE);
                if (this.cancelled) {
                    this.f527s = SubscriptionHelper.CANCELLED;
                    subscription.cancel();
                }
            }
        }
    }

    public final void onComplete() {
        countDown();
    }

    public final T blockingGet() {
        if (getCount() != 0) {
            try {
                await();
            } catch (InterruptedException e) {
                Subscription subscription = this.f527s;
                this.f527s = SubscriptionHelper.CANCELLED;
                if (subscription != null) {
                    subscription.cancel();
                }
                throw ExceptionHelper.wrapOrThrow(e);
            }
        }
        Throwable th = this.error;
        if (th == null) {
            return this.value;
        }
        throw ExceptionHelper.wrapOrThrow(th);
    }
}
