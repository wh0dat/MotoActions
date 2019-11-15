package p016io.reactivex.subscribers;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.annotations.Experimental;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.functions.Consumer;
import p016io.reactivex.internal.fuseable.QueueSubscription;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;
import p016io.reactivex.internal.util.ExceptionHelper;
import p016io.reactivex.observers.BaseTestConsumer;

/* renamed from: io.reactivex.subscribers.TestSubscriber */
public class TestSubscriber<T> extends BaseTestConsumer<T, TestSubscriber<T>> implements Subscriber<T>, Subscription, Disposable {
    private final Subscriber<? super T> actual;
    private volatile boolean cancelled;
    private final AtomicLong missedRequested;

    /* renamed from: qs */
    private QueueSubscription<T> f571qs;
    private final AtomicReference<Subscription> subscription;

    /* renamed from: io.reactivex.subscribers.TestSubscriber$EmptySubscriber */
    enum EmptySubscriber implements Subscriber<Object> {
        INSTANCE;

        public void onComplete() {
        }

        public void onError(Throwable th) {
        }

        public void onNext(Object obj) {
        }

        public void onSubscribe(Subscription subscription) {
        }
    }

    /* access modifiers changed from: protected */
    public void onStart() {
    }

    public static <T> TestSubscriber<T> create() {
        return new TestSubscriber<>();
    }

    public static <T> TestSubscriber<T> create(long j) {
        return new TestSubscriber<>(j);
    }

    public static <T> TestSubscriber<T> create(Subscriber<? super T> subscriber) {
        return new TestSubscriber<>(subscriber);
    }

    public TestSubscriber() {
        this(EmptySubscriber.INSTANCE, LongCompanionObject.MAX_VALUE);
    }

    public TestSubscriber(long j) {
        this(EmptySubscriber.INSTANCE, j);
    }

    public TestSubscriber(Subscriber<? super T> subscriber) {
        this(subscriber, LongCompanionObject.MAX_VALUE);
    }

    public TestSubscriber(Subscriber<? super T> subscriber, long j) {
        this.actual = subscriber;
        this.subscription = new AtomicReference<>();
        this.missedRequested = new AtomicLong(j);
    }

    public void onSubscribe(Subscription subscription2) {
        this.lastThread = Thread.currentThread();
        if (subscription2 == null) {
            this.errors.add(new NullPointerException("onSubscribe received a null Subscription"));
        } else if (!this.subscription.compareAndSet(null, subscription2)) {
            subscription2.cancel();
            if (this.subscription.get() != SubscriptionHelper.CANCELLED) {
                List list = this.errors;
                StringBuilder sb = new StringBuilder();
                sb.append("onSubscribe received multiple subscriptions: ");
                sb.append(subscription2);
                list.add(new IllegalStateException(sb.toString()));
            }
        } else {
            if (this.initialFusionMode != 0 && (subscription2 instanceof QueueSubscription)) {
                this.f571qs = (QueueSubscription) subscription2;
                int requestFusion = this.f571qs.requestFusion(this.initialFusionMode);
                this.establishedFusionMode = requestFusion;
                if (requestFusion == 1) {
                    this.checkSubscriptionOnce = true;
                    this.lastThread = Thread.currentThread();
                    while (true) {
                        try {
                            Object poll = this.f571qs.poll();
                            if (poll == null) {
                                break;
                            }
                            this.values.add(poll);
                        } catch (Throwable th) {
                            this.errors.add(th);
                        }
                    }
                    this.completions++;
                    return;
                }
            }
            this.actual.onSubscribe(subscription2);
            long andSet = this.missedRequested.getAndSet(0);
            if (andSet != 0) {
                subscription2.request(andSet);
            }
            onStart();
        }
    }

    public void onNext(T t) {
        if (!this.checkSubscriptionOnce) {
            this.checkSubscriptionOnce = true;
            if (this.subscription.get() == null) {
                this.errors.add(new IllegalStateException("onSubscribe not called in proper order"));
            }
        }
        this.lastThread = Thread.currentThread();
        if (this.establishedFusionMode == 2) {
            while (true) {
                try {
                    Object poll = this.f571qs.poll();
                    if (poll == null) {
                        break;
                    }
                    this.values.add(poll);
                } catch (Throwable th) {
                    this.errors.add(th);
                }
            }
            return;
        }
        this.values.add(t);
        if (t == null) {
            this.errors.add(new NullPointerException("onNext received a null Subscription"));
        }
        this.actual.onNext(t);
    }

    public void onError(Throwable th) {
        if (!this.checkSubscriptionOnce) {
            this.checkSubscriptionOnce = true;
            if (this.subscription.get() == null) {
                this.errors.add(new NullPointerException("onSubscribe not called in proper order"));
            }
        }
        try {
            this.lastThread = Thread.currentThread();
            this.errors.add(th);
            if (th == null) {
                this.errors.add(new IllegalStateException("onError received a null Subscription"));
            }
            this.actual.onError(th);
        } finally {
            this.done.countDown();
        }
    }

    public void onComplete() {
        if (!this.checkSubscriptionOnce) {
            this.checkSubscriptionOnce = true;
            if (this.subscription.get() == null) {
                this.errors.add(new IllegalStateException("onSubscribe not called in proper order"));
            }
        }
        try {
            this.lastThread = Thread.currentThread();
            this.completions++;
            this.actual.onComplete();
        } finally {
            this.done.countDown();
        }
    }

    public final void request(long j) {
        SubscriptionHelper.deferredRequest(this.subscription, this.missedRequested, j);
    }

    public final void cancel() {
        if (!this.cancelled) {
            this.cancelled = true;
            SubscriptionHelper.cancel(this.subscription);
        }
    }

    public final boolean isCancelled() {
        return this.cancelled;
    }

    public final void dispose() {
        cancel();
    }

    public final boolean isDisposed() {
        return this.cancelled;
    }

    public final boolean hasSubscription() {
        return this.subscription.get() != null;
    }

    public final TestSubscriber<T> assertSubscribed() {
        if (this.subscription.get() != null) {
            return this;
        }
        throw fail("Not subscribed!");
    }

    public final TestSubscriber<T> assertNotSubscribed() {
        if (this.subscription.get() != null) {
            throw fail("Subscribed!");
        } else if (this.errors.isEmpty()) {
            return this;
        } else {
            throw fail("Not subscribed but errors found");
        }
    }

    /* access modifiers changed from: 0000 */
    public final TestSubscriber<T> setInitialFusionMode(int i) {
        this.initialFusionMode = i;
        return this;
    }

    /* access modifiers changed from: 0000 */
    public final TestSubscriber<T> assertFusionMode(int i) {
        int i2 = this.establishedFusionMode;
        if (i2 == i) {
            return this;
        }
        if (this.f571qs != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Fusion mode different. Expected: ");
            sb.append(fusionModeToString(i));
            sb.append(", actual: ");
            sb.append(fusionModeToString(i2));
            throw new AssertionError(sb.toString());
        }
        throw fail("Upstream is not fuseable");
    }

    static String fusionModeToString(int i) {
        switch (i) {
            case 0:
                return "NONE";
            case 1:
                return "SYNC";
            case 2:
                return "ASYNC";
            default:
                StringBuilder sb = new StringBuilder();
                sb.append("Unknown(");
                sb.append(i);
                sb.append(")");
                return sb.toString();
        }
    }

    /* access modifiers changed from: 0000 */
    public final TestSubscriber<T> assertFuseable() {
        if (this.f571qs != null) {
            return this;
        }
        throw new AssertionError("Upstream is not fuseable.");
    }

    /* access modifiers changed from: 0000 */
    public final TestSubscriber<T> assertNotFuseable() {
        if (this.f571qs == null) {
            return this;
        }
        throw new AssertionError("Upstream is fuseable.");
    }

    public final TestSubscriber<T> assertOf(Consumer<? super TestSubscriber<T>> consumer) {
        try {
            consumer.accept(this);
            return this;
        } catch (Throwable th) {
            throw ExceptionHelper.wrapOrThrow(th);
        }
    }

    @Experimental
    public final TestSubscriber<T> requestMore(long j) {
        request(j);
        return this;
    }
}
