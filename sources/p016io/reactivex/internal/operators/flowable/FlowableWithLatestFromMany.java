package p016io.reactivex.internal.operators.flowable;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.Function;
import p016io.reactivex.internal.functions.ObjectHelper;
import p016io.reactivex.internal.subscriptions.EmptySubscription;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;
import p016io.reactivex.internal.util.AtomicThrowable;
import p016io.reactivex.internal.util.HalfSerializer;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableWithLatestFromMany */
public final class FlowableWithLatestFromMany<T, R> extends AbstractFlowableWithUpstream<T, R> {
    final Function<? super Object[], R> combiner;
    final Publisher<?>[] otherArray;
    final Iterable<? extends Publisher<?>> otherIterable;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableWithLatestFromMany$WithLatestFromSubscriber */
    static final class WithLatestFromSubscriber<T, R> extends AtomicInteger implements Subscriber<T>, Subscription {
        private static final long serialVersionUID = 1577321883966341961L;
        final Subscriber<? super R> actual;
        final Function<? super Object[], R> combiner;
        volatile boolean done;
        final AtomicThrowable error;
        final AtomicLong requested;

        /* renamed from: s */
        final AtomicReference<Subscription> f358s;
        final WithLatestInnerSubscriber[] subscribers;
        final AtomicReferenceArray<Object> values;

        WithLatestFromSubscriber(Subscriber<? super R> subscriber, Function<? super Object[], R> function, int i) {
            this.actual = subscriber;
            this.combiner = function;
            WithLatestInnerSubscriber[] withLatestInnerSubscriberArr = new WithLatestInnerSubscriber[i];
            for (int i2 = 0; i2 < i; i2++) {
                withLatestInnerSubscriberArr[i2] = new WithLatestInnerSubscriber(this, i2);
            }
            this.subscribers = withLatestInnerSubscriberArr;
            this.values = new AtomicReferenceArray<>(i);
            this.f358s = new AtomicReference<>();
            this.requested = new AtomicLong();
            this.error = new AtomicThrowable();
        }

        /* access modifiers changed from: 0000 */
        public void subscribe(Publisher<?>[] publisherArr, int i) {
            WithLatestInnerSubscriber[] withLatestInnerSubscriberArr = this.subscribers;
            AtomicReference<Subscription> atomicReference = this.f358s;
            for (int i2 = 0; i2 < i && !SubscriptionHelper.isCancelled((Subscription) atomicReference.get()) && !this.done; i2++) {
                publisherArr[i2].subscribe(withLatestInnerSubscriberArr[i2]);
            }
        }

        public void onSubscribe(Subscription subscription) {
            SubscriptionHelper.deferredSetOnce(this.f358s, this.requested, subscription);
        }

        public void onNext(T t) {
            if (!this.done) {
                AtomicReferenceArray<Object> atomicReferenceArray = this.values;
                int length = atomicReferenceArray.length();
                Object[] objArr = new Object[(length + 1)];
                int i = 0;
                objArr[0] = t;
                while (i < length) {
                    Object obj = atomicReferenceArray.get(i);
                    if (obj == null) {
                        ((Subscription) this.f358s.get()).request(1);
                        return;
                    } else {
                        i++;
                        objArr[i] = obj;
                    }
                }
                try {
                    HalfSerializer.onNext(this.actual, ObjectHelper.requireNonNull(this.combiner.apply(objArr), "combiner returned a null value"), (AtomicInteger) this, this.error);
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    cancel();
                    onError(th);
                }
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            cancelAllBut(-1);
            HalfSerializer.onError(this.actual, th, (AtomicInteger) this, this.error);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                cancelAllBut(-1);
                HalfSerializer.onComplete(this.actual, (AtomicInteger) this, this.error);
            }
        }

        public void request(long j) {
            SubscriptionHelper.deferredRequest(this.f358s, this.requested, j);
        }

        public void cancel() {
            SubscriptionHelper.cancel(this.f358s);
            for (WithLatestInnerSubscriber dispose : this.subscribers) {
                dispose.dispose();
            }
        }

        /* access modifiers changed from: 0000 */
        public void innerNext(int i, Object obj) {
            this.values.set(i, obj);
        }

        /* access modifiers changed from: 0000 */
        public void innerError(int i, Throwable th) {
            this.done = true;
            SubscriptionHelper.cancel(this.f358s);
            cancelAllBut(i);
            HalfSerializer.onError(this.actual, th, (AtomicInteger) this, this.error);
        }

        /* access modifiers changed from: 0000 */
        public void innerComplete(int i, boolean z) {
            if (!z) {
                this.done = true;
                cancelAllBut(i);
                HalfSerializer.onComplete(this.actual, (AtomicInteger) this, this.error);
            }
        }

        /* access modifiers changed from: 0000 */
        public void cancelAllBut(int i) {
            WithLatestInnerSubscriber[] withLatestInnerSubscriberArr = this.subscribers;
            for (int i2 = 0; i2 < withLatestInnerSubscriberArr.length; i2++) {
                if (i2 != i) {
                    withLatestInnerSubscriberArr[i2].dispose();
                }
            }
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableWithLatestFromMany$WithLatestInnerSubscriber */
    static final class WithLatestInnerSubscriber extends AtomicReference<Subscription> implements Subscriber<Object>, Disposable {
        private static final long serialVersionUID = 3256684027868224024L;
        boolean hasValue;
        final int index;
        final WithLatestFromSubscriber<?, ?> parent;

        WithLatestInnerSubscriber(WithLatestFromSubscriber<?, ?> withLatestFromSubscriber, int i) {
            this.parent = withLatestFromSubscriber;
            this.index = i;
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.setOnce(this, subscription)) {
                subscription.request(LongCompanionObject.MAX_VALUE);
            }
        }

        public void onNext(Object obj) {
            if (!this.hasValue) {
                this.hasValue = true;
            }
            this.parent.innerNext(this.index, obj);
        }

        public void onError(Throwable th) {
            this.parent.innerError(this.index, th);
        }

        public void onComplete() {
            this.parent.innerComplete(this.index, this.hasValue);
        }

        public boolean isDisposed() {
            return SubscriptionHelper.isCancelled((Subscription) get());
        }

        public void dispose() {
            SubscriptionHelper.cancel(this);
        }
    }

    public FlowableWithLatestFromMany(Publisher<T> publisher, Publisher<?>[] publisherArr, Function<? super Object[], R> function) {
        super(publisher);
        this.otherArray = publisherArr;
        this.otherIterable = null;
        this.combiner = function;
    }

    public FlowableWithLatestFromMany(Publisher<T> publisher, Iterable<? extends Publisher<?>> iterable, Function<? super Object[], R> function) {
        super(publisher);
        this.otherArray = null;
        this.otherIterable = iterable;
        this.combiner = function;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super R> subscriber) {
        int i;
        Publisher<?>[] publisherArr = this.otherArray;
        if (publisherArr == null) {
            publisherArr = new Publisher[8];
            try {
                i = 0;
                for (Publisher<?> publisher : this.otherIterable) {
                    if (i == publisherArr.length) {
                        publisherArr = (Publisher[]) Arrays.copyOf(publisherArr, (i >> 1) + i);
                    }
                    int i2 = i + 1;
                    publisherArr[i] = publisher;
                    i = i2;
                }
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                EmptySubscription.error(th, subscriber);
                return;
            }
        } else {
            i = publisherArr.length;
        }
        if (i == 0) {
            new FlowableMap(this.source, new Function<T, R>() {
                public R apply(T t) throws Exception {
                    return FlowableWithLatestFromMany.this.combiner.apply(new Object[]{t});
                }
            }).subscribeActual(subscriber);
            return;
        }
        WithLatestFromSubscriber withLatestFromSubscriber = new WithLatestFromSubscriber(subscriber, this.combiner, i);
        subscriber.onSubscribe(withLatestFromSubscriber);
        withLatestFromSubscriber.subscribe(publisherArr, i);
        this.source.subscribe(withLatestFromSubscriber);
    }
}
