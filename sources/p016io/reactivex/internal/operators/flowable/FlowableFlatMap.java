package p016io.reactivex.internal.operators.flowable;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.exceptions.MissingBackpressureException;
import p016io.reactivex.functions.Function;
import p016io.reactivex.internal.functions.ObjectHelper;
import p016io.reactivex.internal.fuseable.QueueSubscription;
import p016io.reactivex.internal.fuseable.SimplePlainQueue;
import p016io.reactivex.internal.fuseable.SimpleQueue;
import p016io.reactivex.internal.queue.SpscArrayQueue;
import p016io.reactivex.internal.queue.SpscLinkedArrayQueue;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;
import p016io.reactivex.internal.util.AtomicThrowable;
import p016io.reactivex.internal.util.BackpressureHelper;
import p016io.reactivex.internal.util.ExceptionHelper;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableFlatMap */
public final class FlowableFlatMap<T, U> extends AbstractFlowableWithUpstream<T, U> {
    final int bufferSize;
    final boolean delayErrors;
    final Function<? super T, ? extends Publisher<? extends U>> mapper;
    final int maxConcurrency;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableFlatMap$InnerSubscriber */
    static final class InnerSubscriber<T, U> extends AtomicReference<Subscription> implements Subscriber<U>, Disposable {
        private static final long serialVersionUID = -4606175640614850599L;
        final int bufferSize;
        volatile boolean done;
        int fusionMode;

        /* renamed from: id */
        final long f278id;
        final int limit = (this.bufferSize >> 2);
        final MergeSubscriber<T, U> parent;
        long produced;
        volatile SimpleQueue<U> queue;

        InnerSubscriber(MergeSubscriber<T, U> mergeSubscriber, long j) {
            this.f278id = j;
            this.parent = mergeSubscriber;
            this.bufferSize = mergeSubscriber.bufferSize;
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.setOnce(this, subscription)) {
                if (subscription instanceof QueueSubscription) {
                    QueueSubscription queueSubscription = (QueueSubscription) subscription;
                    int requestFusion = queueSubscription.requestFusion(3);
                    if (requestFusion == 1) {
                        this.fusionMode = requestFusion;
                        this.queue = queueSubscription;
                        this.done = true;
                        this.parent.drain();
                        return;
                    } else if (requestFusion == 2) {
                        this.fusionMode = requestFusion;
                        this.queue = queueSubscription;
                    }
                }
                subscription.request((long) this.bufferSize);
            }
        }

        public void onNext(U u) {
            if (this.fusionMode != 2) {
                this.parent.tryEmit(u, this);
            } else {
                this.parent.drain();
            }
        }

        public void onError(Throwable th) {
            if (this.parent.errs.addThrowable(th)) {
                this.done = true;
                this.parent.drain();
                return;
            }
            RxJavaPlugins.onError(th);
        }

        public void onComplete() {
            this.done = true;
            this.parent.drain();
        }

        /* access modifiers changed from: 0000 */
        public void requestMore(long j) {
            if (this.fusionMode != 1) {
                long j2 = this.produced + j;
                if (j2 >= ((long) this.limit)) {
                    this.produced = 0;
                    ((Subscription) get()).request(j2);
                    return;
                }
                this.produced = j2;
            }
        }

        public void dispose() {
            SubscriptionHelper.cancel(this);
        }

        public boolean isDisposed() {
            return get() == SubscriptionHelper.CANCELLED;
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableFlatMap$MergeSubscriber */
    static final class MergeSubscriber<T, U> extends AtomicInteger implements Subscription, Subscriber<T> {
        static final InnerSubscriber<?, ?>[] CANCELLED = new InnerSubscriber[0];
        static final InnerSubscriber<?, ?>[] EMPTY = new InnerSubscriber[0];
        private static final long serialVersionUID = -2117620485640801370L;
        final Subscriber<? super U> actual;
        final int bufferSize;
        volatile boolean cancelled;
        final boolean delayErrors;
        volatile boolean done;
        final AtomicThrowable errs = new AtomicThrowable();
        long lastId;
        int lastIndex;
        final Function<? super T, ? extends Publisher<? extends U>> mapper;
        final int maxConcurrency;
        volatile SimplePlainQueue<U> queue;
        final AtomicLong requested = new AtomicLong();

        /* renamed from: s */
        Subscription f279s;
        int scalarEmitted;
        final int scalarLimit;
        final AtomicReference<InnerSubscriber<?, ?>[]> subscribers = new AtomicReference<>();
        long uniqueId;

        MergeSubscriber(Subscriber<? super U> subscriber, Function<? super T, ? extends Publisher<? extends U>> function, boolean z, int i, int i2) {
            this.actual = subscriber;
            this.mapper = function;
            this.delayErrors = z;
            this.maxConcurrency = i;
            this.bufferSize = i2;
            this.scalarLimit = Math.max(1, i >> 1);
            this.subscribers.lazySet(EMPTY);
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.f279s, subscription)) {
                this.f279s = subscription;
                this.actual.onSubscribe(this);
                if (this.cancelled) {
                    return;
                }
                if (this.maxConcurrency == Integer.MAX_VALUE) {
                    subscription.request(LongCompanionObject.MAX_VALUE);
                } else {
                    subscription.request((long) this.maxConcurrency);
                }
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                try {
                    Publisher publisher = (Publisher) ObjectHelper.requireNonNull(this.mapper.apply(t), "The mapper returned a null Publisher");
                    if (publisher instanceof Callable) {
                        try {
                            Object call = ((Callable) publisher).call();
                            if (call != null) {
                                tryEmitScalar(call);
                            } else if (this.maxConcurrency != Integer.MAX_VALUE && !this.cancelled) {
                                int i = this.scalarEmitted + 1;
                                this.scalarEmitted = i;
                                if (i == this.scalarLimit) {
                                    this.scalarEmitted = 0;
                                    this.f279s.request((long) this.scalarLimit);
                                }
                            }
                        } catch (Throwable th) {
                            Exceptions.throwIfFatal(th);
                            this.errs.addThrowable(th);
                            drain();
                        }
                    } else {
                        long j = this.uniqueId;
                        this.uniqueId = 1 + j;
                        InnerSubscriber innerSubscriber = new InnerSubscriber(this, j);
                        addInner(innerSubscriber);
                        publisher.subscribe(innerSubscriber);
                    }
                } catch (Throwable th2) {
                    Exceptions.throwIfFatal(th2);
                    this.f279s.cancel();
                    onError(th2);
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void addInner(InnerSubscriber<T, U> innerSubscriber) {
            InnerSubscriber<?, ?>[] innerSubscriberArr;
            InnerSubscriber[] innerSubscriberArr2;
            do {
                innerSubscriberArr = (InnerSubscriber[]) this.subscribers.get();
                if (innerSubscriberArr == CANCELLED) {
                    innerSubscriber.dispose();
                    return;
                }
                int length = innerSubscriberArr.length;
                innerSubscriberArr2 = new InnerSubscriber[(length + 1)];
                System.arraycopy(innerSubscriberArr, 0, innerSubscriberArr2, 0, length);
                innerSubscriberArr2[length] = innerSubscriber;
            } while (!this.subscribers.compareAndSet(innerSubscriberArr, innerSubscriberArr2));
        }

        /* access modifiers changed from: 0000 */
        public void removeInner(InnerSubscriber<T, U> innerSubscriber) {
            InnerSubscriber<?, ?>[] innerSubscriberArr;
            Object obj;
            do {
                innerSubscriberArr = (InnerSubscriber[]) this.subscribers.get();
                if (innerSubscriberArr != CANCELLED && innerSubscriberArr != EMPTY) {
                    int length = innerSubscriberArr.length;
                    int i = -1;
                    int i2 = 0;
                    while (true) {
                        if (i2 >= length) {
                            break;
                        } else if (innerSubscriberArr[i2] == innerSubscriber) {
                            i = i2;
                            break;
                        } else {
                            i2++;
                        }
                    }
                    if (i >= 0) {
                        if (length == 1) {
                            obj = EMPTY;
                        } else {
                            InnerSubscriber[] innerSubscriberArr2 = new InnerSubscriber[(length - 1)];
                            System.arraycopy(innerSubscriberArr, 0, innerSubscriberArr2, 0, i);
                            System.arraycopy(innerSubscriberArr, i + 1, innerSubscriberArr2, i, (length - i) - 1);
                            obj = innerSubscriberArr2;
                        }
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            } while (!this.subscribers.compareAndSet(innerSubscriberArr, obj));
        }

        /* access modifiers changed from: 0000 */
        public SimpleQueue<U> getMainQueue() {
            SimplePlainQueue<U> simplePlainQueue = this.queue;
            if (simplePlainQueue == null) {
                if (this.maxConcurrency == Integer.MAX_VALUE) {
                    simplePlainQueue = new SpscLinkedArrayQueue<>(this.bufferSize);
                } else {
                    simplePlainQueue = new SpscArrayQueue<>(this.maxConcurrency);
                }
                this.queue = simplePlainQueue;
            }
            return simplePlainQueue;
        }

        /* access modifiers changed from: 0000 */
        public void tryEmitScalar(U u) {
            if (get() == 0 && compareAndSet(0, 1)) {
                long j = this.requested.get();
                SimpleQueue simpleQueue = this.queue;
                if (j == 0 || (simpleQueue != null && !simpleQueue.isEmpty())) {
                    if (simpleQueue == null) {
                        simpleQueue = getMainQueue();
                    }
                    if (!simpleQueue.offer(u)) {
                        onError(new IllegalStateException("Scalar queue full?!"));
                        return;
                    }
                } else {
                    this.actual.onNext(u);
                    if (j != LongCompanionObject.MAX_VALUE) {
                        this.requested.decrementAndGet();
                    }
                    if (this.maxConcurrency != Integer.MAX_VALUE && !this.cancelled) {
                        int i = this.scalarEmitted + 1;
                        this.scalarEmitted = i;
                        if (i == this.scalarLimit) {
                            this.scalarEmitted = 0;
                            this.f279s.request((long) this.scalarLimit);
                        }
                    }
                }
                if (decrementAndGet() == 0) {
                    return;
                }
            } else if (!getMainQueue().offer(u)) {
                onError(new IllegalStateException("Scalar queue full?!"));
                return;
            } else if (getAndIncrement() != 0) {
                return;
            }
            drainLoop();
        }

        /* access modifiers changed from: 0000 */
        public SimpleQueue<U> getInnerQueue(InnerSubscriber<T, U> innerSubscriber) {
            SimpleQueue<U> simpleQueue = innerSubscriber.queue;
            if (simpleQueue != null) {
                return simpleQueue;
            }
            SpscArrayQueue spscArrayQueue = new SpscArrayQueue(this.bufferSize);
            innerSubscriber.queue = spscArrayQueue;
            return spscArrayQueue;
        }

        /* access modifiers changed from: 0000 */
        public void tryEmit(U u, InnerSubscriber<T, U> innerSubscriber) {
            if (get() != 0 || !compareAndSet(0, 1)) {
                SimpleQueue simpleQueue = innerSubscriber.queue;
                if (simpleQueue == null) {
                    simpleQueue = new SpscArrayQueue(this.bufferSize);
                    innerSubscriber.queue = simpleQueue;
                }
                if (!simpleQueue.offer(u)) {
                    onError(new MissingBackpressureException("Inner queue full?!"));
                    return;
                } else if (getAndIncrement() != 0) {
                    return;
                }
            } else {
                long j = this.requested.get();
                SimpleQueue<U> simpleQueue2 = innerSubscriber.queue;
                if (j == 0 || (simpleQueue2 != null && !simpleQueue2.isEmpty())) {
                    if (simpleQueue2 == null) {
                        simpleQueue2 = getInnerQueue(innerSubscriber);
                    }
                    if (!simpleQueue2.offer(u)) {
                        onError(new MissingBackpressureException("Inner queue full?!"));
                        return;
                    }
                } else {
                    this.actual.onNext(u);
                    if (j != LongCompanionObject.MAX_VALUE) {
                        this.requested.decrementAndGet();
                    }
                    innerSubscriber.requestMore(1);
                }
                if (decrementAndGet() == 0) {
                    return;
                }
            }
            drainLoop();
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            if (this.errs.addThrowable(th)) {
                this.done = true;
                drain();
            } else {
                RxJavaPlugins.onError(th);
            }
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                drain();
            }
        }

        public void request(long j) {
            if (SubscriptionHelper.validate(j)) {
                BackpressureHelper.add(this.requested, j);
                drain();
            }
        }

        public void cancel() {
            if (!this.cancelled) {
                this.cancelled = true;
                this.f279s.cancel();
                disposeAll();
                if (getAndIncrement() == 0) {
                    SimplePlainQueue<U> simplePlainQueue = this.queue;
                    if (simplePlainQueue != null) {
                        simplePlainQueue.clear();
                    }
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            if (getAndIncrement() == 0) {
                drainLoop();
            }
        }

        /* access modifiers changed from: 0000 */
        public void drainLoop() {
            long j;
            Subscriber<? super U> subscriber;
            int i;
            boolean z;
            long j2;
            int i2;
            int i3;
            int i4;
            long j3;
            Subscriber<? super U> subscriber2 = this.actual;
            int i5 = 1;
            while (!checkTerminate()) {
                SimplePlainQueue<U> simplePlainQueue = this.queue;
                long j4 = this.requested.get();
                boolean z2 = j4 == LongCompanionObject.MAX_VALUE;
                if (simplePlainQueue != null) {
                    j = 0;
                    while (true) {
                        long j5 = 0;
                        Object obj = null;
                        while (true) {
                            if (j4 == 0) {
                                break;
                            }
                            Object poll = simplePlainQueue.poll();
                            if (!checkTerminate()) {
                                if (poll == null) {
                                    obj = poll;
                                    break;
                                }
                                subscriber2.onNext(poll);
                                j++;
                                j5++;
                                j4--;
                                obj = poll;
                            } else {
                                return;
                            }
                        }
                        if (j5 != 0) {
                            if (z2) {
                                j4 = LongCompanionObject.MAX_VALUE;
                            } else {
                                j4 = this.requested.addAndGet(-j5);
                            }
                        }
                        if (j4 == 0 || obj == null) {
                            break;
                        }
                    }
                } else {
                    j = 0;
                }
                boolean z3 = this.done;
                SimplePlainQueue<U> simplePlainQueue2 = this.queue;
                InnerSubscriber[] innerSubscriberArr = (InnerSubscriber[]) this.subscribers.get();
                int length = innerSubscriberArr.length;
                if (!z3 || ((simplePlainQueue2 != null && !simplePlainQueue2.isEmpty()) || length != 0)) {
                    if (length != 0) {
                        i = i5;
                        long j6 = this.lastId;
                        int i6 = this.lastIndex;
                        if (length <= i6 || innerSubscriberArr[i6].f278id != j6) {
                            if (length <= i6) {
                                i6 = 0;
                            }
                            int i7 = i6;
                            for (int i8 = 0; i8 < length && innerSubscriberArr[i7].f278id != j6; i8++) {
                                i7++;
                                if (i7 == length) {
                                    i7 = 0;
                                }
                            }
                            this.lastIndex = i7;
                            this.lastId = innerSubscriberArr[i7].f278id;
                            i6 = i7;
                        }
                        int i9 = 0;
                        z = false;
                        while (true) {
                            if (i9 >= length) {
                                subscriber = subscriber2;
                                break;
                            } else if (!checkTerminate()) {
                                InnerSubscriber innerSubscriber = innerSubscriberArr[i6];
                                Object obj2 = null;
                                while (!checkTerminate()) {
                                    SimpleQueue<U> simpleQueue = innerSubscriber.queue;
                                    if (simpleQueue == null) {
                                        subscriber = subscriber2;
                                        i2 = i9;
                                        i3 = length;
                                    } else {
                                        i3 = length;
                                        Object obj3 = obj2;
                                        long j7 = 0;
                                        while (j4 != 0) {
                                            try {
                                                obj3 = simpleQueue.poll();
                                                if (obj3 == null) {
                                                    break;
                                                }
                                                subscriber2.onNext(obj3);
                                                if (!checkTerminate()) {
                                                    j4--;
                                                    j7++;
                                                } else {
                                                    return;
                                                }
                                            } catch (Throwable th) {
                                                Exceptions.throwIfFatal(th);
                                                innerSubscriber.dispose();
                                                this.errs.addThrowable(th);
                                                if (!checkTerminate()) {
                                                    removeInner(innerSubscriber);
                                                    subscriber = subscriber2;
                                                    i2 = i9 + 1;
                                                    i4 = i3;
                                                    z = true;
                                                } else {
                                                    return;
                                                }
                                            }
                                        }
                                        if (j7 != 0) {
                                            if (!z2) {
                                                subscriber = subscriber2;
                                                i2 = i9;
                                                j3 = this.requested.addAndGet(-j7);
                                            } else {
                                                subscriber = subscriber2;
                                                i2 = i9;
                                                j3 = LongCompanionObject.MAX_VALUE;
                                            }
                                            innerSubscriber.requestMore(j7);
                                            j4 = j3;
                                        } else {
                                            subscriber = subscriber2;
                                            i2 = i9;
                                        }
                                        if (!(j4 == 0 || obj3 == null)) {
                                            obj2 = obj3;
                                            length = i3;
                                            subscriber2 = subscriber;
                                            i9 = i2;
                                        }
                                    }
                                    boolean z4 = innerSubscriber.done;
                                    SimpleQueue<U> simpleQueue2 = innerSubscriber.queue;
                                    if (z4 && (simpleQueue2 == null || simpleQueue2.isEmpty())) {
                                        removeInner(innerSubscriber);
                                        if (!checkTerminate()) {
                                            j++;
                                            z = true;
                                        } else {
                                            return;
                                        }
                                    }
                                    if (j4 == 0) {
                                        break;
                                    }
                                    int i10 = i6 + 1;
                                    i4 = i3;
                                    i6 = i10 == i4 ? 0 : i10;
                                    length = i4;
                                    i9 = i2 + 1;
                                    subscriber2 = subscriber;
                                }
                                return;
                            } else {
                                return;
                            }
                        }
                        this.lastIndex = i6;
                        this.lastId = innerSubscriberArr[i6].f278id;
                        j2 = j;
                    } else {
                        subscriber = subscriber2;
                        i = i5;
                        j2 = j;
                        z = false;
                    }
                    if (j2 != 0 && !this.cancelled) {
                        this.f279s.request(j2);
                    }
                    if (z) {
                        i5 = i;
                    } else {
                        i5 = addAndGet(-i);
                        if (i5 == 0) {
                            return;
                        }
                    }
                    subscriber2 = subscriber;
                } else {
                    Throwable terminate = this.errs.terminate();
                    if (terminate == null) {
                        subscriber2.onComplete();
                    } else {
                        subscriber2.onError(terminate);
                    }
                    return;
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public boolean checkTerminate() {
            if (this.cancelled) {
                SimplePlainQueue<U> simplePlainQueue = this.queue;
                if (simplePlainQueue != null) {
                    simplePlainQueue.clear();
                }
                return true;
            } else if (this.delayErrors || this.errs.get() == null) {
                return false;
            } else {
                this.actual.onError(this.errs.terminate());
                return true;
            }
        }

        /* access modifiers changed from: 0000 */
        public void disposeAll() {
            if (((InnerSubscriber[]) this.subscribers.get()) != CANCELLED) {
                InnerSubscriber<?, ?>[] innerSubscriberArr = (InnerSubscriber[]) this.subscribers.getAndSet(CANCELLED);
                if (innerSubscriberArr != CANCELLED) {
                    for (InnerSubscriber<?, ?> dispose : innerSubscriberArr) {
                        dispose.dispose();
                    }
                    Throwable terminate = this.errs.terminate();
                    if (terminate != null && terminate != ExceptionHelper.TERMINATED) {
                        RxJavaPlugins.onError(terminate);
                    }
                }
            }
        }
    }

    public FlowableFlatMap(Publisher<T> publisher, Function<? super T, ? extends Publisher<? extends U>> function, boolean z, int i, int i2) {
        super(publisher);
        this.mapper = function;
        this.delayErrors = z;
        this.maxConcurrency = i;
        this.bufferSize = i2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super U> subscriber) {
        if (!FlowableScalarXMap.tryScalarXMapSubscribe(this.source, subscriber, this.mapper)) {
            Publisher publisher = this.source;
            MergeSubscriber mergeSubscriber = new MergeSubscriber(subscriber, this.mapper, this.delayErrors, this.maxConcurrency, this.bufferSize);
            publisher.subscribe(mergeSubscriber);
        }
    }
}
