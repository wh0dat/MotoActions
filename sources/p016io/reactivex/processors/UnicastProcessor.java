package p016io.reactivex.processors;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.internal.functions.ObjectHelper;
import p016io.reactivex.internal.queue.SpscLinkedArrayQueue;
import p016io.reactivex.internal.subscriptions.BasicIntQueueSubscription;
import p016io.reactivex.internal.subscriptions.EmptySubscription;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;
import p016io.reactivex.internal.util.BackpressureHelper;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.processors.UnicastProcessor */
public final class UnicastProcessor<T> extends FlowableProcessor<T> {
    final AtomicReference<Subscriber<? super T>> actual;
    volatile boolean cancelled;
    volatile boolean done;
    boolean enableOperatorFusion;
    Throwable error;
    final AtomicReference<Runnable> onTerminate;
    final AtomicBoolean once;
    final SpscLinkedArrayQueue<T> queue;
    final AtomicLong requested;
    final BasicIntQueueSubscription<T> wip;

    /* renamed from: io.reactivex.processors.UnicastProcessor$UnicastQueueSubscription */
    final class UnicastQueueSubscription extends BasicIntQueueSubscription<T> {
        private static final long serialVersionUID = -4896760517184205454L;

        UnicastQueueSubscription() {
        }

        public T poll() {
            return UnicastProcessor.this.queue.poll();
        }

        public boolean isEmpty() {
            return UnicastProcessor.this.queue.isEmpty();
        }

        public void clear() {
            UnicastProcessor.this.queue.clear();
        }

        public int requestFusion(int i) {
            if ((i & 2) == 0) {
                return 0;
            }
            UnicastProcessor.this.enableOperatorFusion = true;
            return 2;
        }

        public void request(long j) {
            if (SubscriptionHelper.validate(j)) {
                BackpressureHelper.add(UnicastProcessor.this.requested, j);
                UnicastProcessor.this.drain();
            }
        }

        public void cancel() {
            if (!UnicastProcessor.this.cancelled) {
                UnicastProcessor.this.cancelled = true;
                UnicastProcessor.this.doTerminate();
                if (!UnicastProcessor.this.enableOperatorFusion && UnicastProcessor.this.wip.getAndIncrement() == 0) {
                    UnicastProcessor.this.queue.clear();
                    UnicastProcessor.this.actual.lazySet(null);
                }
            }
        }
    }

    public static <T> UnicastProcessor<T> create() {
        return new UnicastProcessor<>(bufferSize());
    }

    public static <T> UnicastProcessor<T> create(int i) {
        return new UnicastProcessor<>(i);
    }

    public static <T> UnicastProcessor<T> create(int i, Runnable runnable) {
        return new UnicastProcessor<>(i, runnable);
    }

    UnicastProcessor(int i) {
        this.queue = new SpscLinkedArrayQueue<>(ObjectHelper.verifyPositive(i, "capacityHint"));
        this.onTerminate = new AtomicReference<>();
        this.actual = new AtomicReference<>();
        this.once = new AtomicBoolean();
        this.wip = new UnicastQueueSubscription();
        this.requested = new AtomicLong();
    }

    UnicastProcessor(int i, Runnable runnable) {
        this.queue = new SpscLinkedArrayQueue<>(ObjectHelper.verifyPositive(i, "capacityHint"));
        this.onTerminate = new AtomicReference<>(ObjectHelper.requireNonNull(runnable, "onTerminate"));
        this.actual = new AtomicReference<>();
        this.once = new AtomicBoolean();
        this.wip = new UnicastQueueSubscription();
        this.requested = new AtomicLong();
    }

    /* access modifiers changed from: 0000 */
    public void doTerminate() {
        Runnable runnable = (Runnable) this.onTerminate.get();
        if (runnable != null && this.onTerminate.compareAndSet(runnable, null)) {
            runnable.run();
        }
    }

    /* access modifiers changed from: 0000 */
    public void drainRegular(Subscriber<? super T> subscriber) {
        int i;
        SpscLinkedArrayQueue<T> spscLinkedArrayQueue = this.queue;
        int i2 = 1;
        do {
            long j = this.requested.get();
            long j2 = 0;
            while (true) {
                i = (j > j2 ? 1 : (j == j2 ? 0 : -1));
                if (i == 0) {
                    break;
                }
                boolean z = this.done;
                Object poll = spscLinkedArrayQueue.poll();
                boolean z2 = poll == null;
                if (!checkTerminated(z, z2, subscriber, spscLinkedArrayQueue)) {
                    if (z2) {
                        break;
                    }
                    subscriber.onNext(poll);
                    j2++;
                } else {
                    return;
                }
            }
            if (i != 0 || !checkTerminated(this.done, spscLinkedArrayQueue.isEmpty(), subscriber, spscLinkedArrayQueue)) {
                if (!(j2 == 0 || j == LongCompanionObject.MAX_VALUE)) {
                    this.requested.addAndGet(-j2);
                }
                i2 = this.wip.addAndGet(-i2);
            } else {
                return;
            }
        } while (i2 != 0);
    }

    /* access modifiers changed from: 0000 */
    public void drainFused(Subscriber<? super T> subscriber) {
        SpscLinkedArrayQueue<T> spscLinkedArrayQueue = this.queue;
        int i = 1;
        while (!this.cancelled) {
            boolean z = this.done;
            subscriber.onNext(null);
            if (z) {
                this.actual.lazySet(null);
                Throwable th = this.error;
                if (th != null) {
                    subscriber.onError(th);
                } else {
                    subscriber.onComplete();
                }
                return;
            }
            i = this.wip.addAndGet(-i);
            if (i == 0) {
                return;
            }
        }
        spscLinkedArrayQueue.clear();
        this.actual.lazySet(null);
    }

    /* access modifiers changed from: 0000 */
    public void drain() {
        if (this.wip.getAndIncrement() == 0) {
            int i = 1;
            Subscriber subscriber = (Subscriber) this.actual.get();
            while (subscriber == null) {
                i = this.wip.addAndGet(-i);
                if (i != 0) {
                    subscriber = (Subscriber) this.actual.get();
                } else {
                    return;
                }
            }
            if (this.enableOperatorFusion) {
                drainFused(subscriber);
            } else {
                drainRegular(subscriber);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean checkTerminated(boolean z, boolean z2, Subscriber<? super T> subscriber, SpscLinkedArrayQueue<T> spscLinkedArrayQueue) {
        if (this.cancelled) {
            spscLinkedArrayQueue.clear();
            this.actual.lazySet(null);
            return true;
        } else if (!z || !z2) {
            return false;
        } else {
            Throwable th = this.error;
            this.actual.lazySet(null);
            if (th != null) {
                subscriber.onError(th);
            } else {
                subscriber.onComplete();
            }
            return true;
        }
    }

    public void onSubscribe(Subscription subscription) {
        if (this.done || this.cancelled) {
            subscription.cancel();
        } else {
            subscription.request(LongCompanionObject.MAX_VALUE);
        }
    }

    public void onNext(T t) {
        if (!this.done && !this.cancelled) {
            if (t == null) {
                onError(new NullPointerException("onNext called with null. Null values are generally not allowed in 2.x operators and sources."));
                return;
            }
            this.queue.offer(t);
            drain();
        }
    }

    public void onError(Throwable th) {
        if (this.done || this.cancelled) {
            RxJavaPlugins.onError(th);
            return;
        }
        if (th == null) {
            th = new NullPointerException("onError called with null. Null values are generally not allowed in 2.x operators and sources.");
        }
        this.error = th;
        this.done = true;
        doTerminate();
        drain();
    }

    public void onComplete() {
        if (!this.done && !this.cancelled) {
            this.done = true;
            doTerminate();
            drain();
        }
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> subscriber) {
        if (this.once.get() || !this.once.compareAndSet(false, true)) {
            EmptySubscription.error(new IllegalStateException("This processor allows only a single Subscriber"), subscriber);
            return;
        }
        subscriber.onSubscribe(this.wip);
        this.actual.set(subscriber);
        if (this.cancelled) {
            this.actual.lazySet(null);
        } else {
            drain();
        }
    }

    public boolean hasSubscribers() {
        return this.actual.get() != null;
    }

    public Throwable getThrowable() {
        if (this.done) {
            return this.error;
        }
        return null;
    }

    public boolean hasComplete() {
        return this.done && this.error == null;
    }

    public boolean hasThrowable() {
        return this.done && this.error != null;
    }
}
