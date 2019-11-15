package p016io.reactivex.processors;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.exceptions.MissingBackpressureException;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;
import p016io.reactivex.internal.util.BackpressureHelper;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.processors.PublishProcessor */
public final class PublishProcessor<T> extends FlowableProcessor<T> {
    static final PublishSubscription[] EMPTY = new PublishSubscription[0];
    static final PublishSubscription[] TERMINATED = new PublishSubscription[0];
    Throwable error;
    final AtomicReference<PublishSubscription<T>[]> subscribers = new AtomicReference<>(EMPTY);

    /* renamed from: io.reactivex.processors.PublishProcessor$PublishSubscription */
    static final class PublishSubscription<T> extends AtomicLong implements Subscription {
        private static final long serialVersionUID = 3562861878281475070L;
        final Subscriber<? super T> actual;
        final PublishProcessor<T> parent;

        PublishSubscription(Subscriber<? super T> subscriber, PublishProcessor<T> publishProcessor) {
            this.actual = subscriber;
            this.parent = publishProcessor;
        }

        public void onNext(T t) {
            long j = get();
            if (j != Long.MIN_VALUE) {
                if (j != 0) {
                    this.actual.onNext(t);
                    if (j != LongCompanionObject.MAX_VALUE) {
                        decrementAndGet();
                    }
                } else {
                    cancel();
                    this.actual.onError(new MissingBackpressureException("Could not emit value due to lack of requests"));
                }
            }
        }

        public void onError(Throwable th) {
            if (get() != Long.MIN_VALUE) {
                this.actual.onError(th);
            } else {
                RxJavaPlugins.onError(th);
            }
        }

        public void onComplete() {
            if (get() != Long.MIN_VALUE) {
                this.actual.onComplete();
            }
        }

        public void request(long j) {
            if (SubscriptionHelper.validate(j)) {
                BackpressureHelper.addCancel(this, j);
            }
        }

        public void cancel() {
            if (getAndSet(Long.MIN_VALUE) != Long.MIN_VALUE) {
                this.parent.remove(this);
            }
        }

        public boolean isCancelled() {
            return get() == Long.MIN_VALUE;
        }
    }

    public static <T> PublishProcessor<T> create() {
        return new PublishProcessor<>();
    }

    PublishProcessor() {
    }

    public void subscribeActual(Subscriber<? super T> subscriber) {
        PublishSubscription publishSubscription = new PublishSubscription(subscriber, this);
        subscriber.onSubscribe(publishSubscription);
        if (!add(publishSubscription)) {
            Throwable th = this.error;
            if (th != null) {
                subscriber.onError(th);
            } else {
                subscriber.onComplete();
            }
        } else if (publishSubscription.isCancelled()) {
            remove(publishSubscription);
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean add(PublishSubscription<T> publishSubscription) {
        PublishSubscription[] publishSubscriptionArr;
        PublishSubscription[] publishSubscriptionArr2;
        do {
            publishSubscriptionArr = (PublishSubscription[]) this.subscribers.get();
            if (publishSubscriptionArr == TERMINATED) {
                return false;
            }
            int length = publishSubscriptionArr.length;
            publishSubscriptionArr2 = new PublishSubscription[(length + 1)];
            System.arraycopy(publishSubscriptionArr, 0, publishSubscriptionArr2, 0, length);
            publishSubscriptionArr2[length] = publishSubscription;
        } while (!this.subscribers.compareAndSet(publishSubscriptionArr, publishSubscriptionArr2));
        return true;
    }

    /* access modifiers changed from: 0000 */
    public void remove(PublishSubscription<T> publishSubscription) {
        PublishSubscription<T>[] publishSubscriptionArr;
        PublishSubscription[] publishSubscriptionArr2;
        do {
            publishSubscriptionArr = (PublishSubscription[]) this.subscribers.get();
            if (publishSubscriptionArr != TERMINATED && publishSubscriptionArr != EMPTY) {
                int length = publishSubscriptionArr.length;
                int i = -1;
                int i2 = 0;
                while (true) {
                    if (i2 >= length) {
                        break;
                    } else if (publishSubscriptionArr[i2] == publishSubscription) {
                        i = i2;
                        break;
                    } else {
                        i2++;
                    }
                }
                if (i >= 0) {
                    if (length == 1) {
                        publishSubscriptionArr2 = EMPTY;
                    } else {
                        PublishSubscription[] publishSubscriptionArr3 = new PublishSubscription[(length - 1)];
                        System.arraycopy(publishSubscriptionArr, 0, publishSubscriptionArr3, 0, i);
                        System.arraycopy(publishSubscriptionArr, i + 1, publishSubscriptionArr3, i, (length - i) - 1);
                        publishSubscriptionArr2 = publishSubscriptionArr3;
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        } while (!this.subscribers.compareAndSet(publishSubscriptionArr, publishSubscriptionArr2));
    }

    public void onSubscribe(Subscription subscription) {
        if (this.subscribers.get() == TERMINATED) {
            subscription.cancel();
        } else {
            subscription.request(LongCompanionObject.MAX_VALUE);
        }
    }

    public void onNext(T t) {
        if (this.subscribers.get() != TERMINATED) {
            if (t == null) {
                onError(new NullPointerException("onNext called with null. Null values are generally not allowed in 2.x operators and sources."));
                return;
            }
            for (PublishSubscription onNext : (PublishSubscription[]) this.subscribers.get()) {
                onNext.onNext(t);
            }
        }
    }

    public void onError(Throwable th) {
        if (this.subscribers.get() == TERMINATED) {
            RxJavaPlugins.onError(th);
            return;
        }
        if (th == null) {
            th = new NullPointerException("onError called with null. Null values are generally not allowed in 2.x operators and sources.");
        }
        this.error = th;
        for (PublishSubscription onError : (PublishSubscription[]) this.subscribers.getAndSet(TERMINATED)) {
            onError.onError(th);
        }
    }

    public void onComplete() {
        if (this.subscribers.get() != TERMINATED) {
            for (PublishSubscription onComplete : (PublishSubscription[]) this.subscribers.getAndSet(TERMINATED)) {
                onComplete.onComplete();
            }
        }
    }

    public boolean hasSubscribers() {
        return ((PublishSubscription[]) this.subscribers.get()).length != 0;
    }

    public Throwable getThrowable() {
        if (this.subscribers.get() == TERMINATED) {
            return this.error;
        }
        return null;
    }

    public boolean hasThrowable() {
        return this.subscribers.get() == TERMINATED && this.error != null;
    }

    public boolean hasComplete() {
        return this.subscribers.get() == TERMINATED && this.error == null;
    }
}
