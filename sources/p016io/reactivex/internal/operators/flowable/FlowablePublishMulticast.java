package p016io.reactivex.internal.operators.flowable;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.Flowable;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.exceptions.MissingBackpressureException;
import p016io.reactivex.functions.Function;
import p016io.reactivex.internal.functions.ObjectHelper;
import p016io.reactivex.internal.fuseable.QueueSubscription;
import p016io.reactivex.internal.fuseable.SimpleQueue;
import p016io.reactivex.internal.subscriptions.EmptySubscription;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;
import p016io.reactivex.internal.util.BackpressureHelper;
import p016io.reactivex.internal.util.QueueDrainHelper;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowablePublishMulticast */
public final class FlowablePublishMulticast<T, R> extends AbstractFlowableWithUpstream<T, R> {
    final boolean delayError;
    final int prefetch;
    final Function<? super Flowable<T>, ? extends Publisher<? extends R>> selector;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowablePublishMulticast$MulticastProcessor */
    static final class MulticastProcessor<T> extends Flowable<T> implements Subscriber<T>, Disposable {
        static final MulticastSubscription[] EMPTY = new MulticastSubscription[0];
        static final MulticastSubscription[] TERMINATED = new MulticastSubscription[0];
        final boolean delayError;
        volatile boolean done;
        Throwable error;
        final int prefetch;
        volatile SimpleQueue<T> queue;

        /* renamed from: s */
        final AtomicReference<Subscription> f302s = new AtomicReference<>();
        int sourceMode;
        final AtomicReference<MulticastSubscription<T>[]> subscribers = new AtomicReference<>(EMPTY);
        final AtomicInteger wip = new AtomicInteger();

        MulticastProcessor(int i, boolean z) {
            this.prefetch = i;
            this.delayError = z;
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.setOnce(this.f302s, subscription)) {
                if (subscription instanceof QueueSubscription) {
                    QueueSubscription queueSubscription = (QueueSubscription) subscription;
                    int requestFusion = queueSubscription.requestFusion(3);
                    if (requestFusion == 1) {
                        this.sourceMode = requestFusion;
                        this.queue = queueSubscription;
                        this.done = true;
                        drain();
                        return;
                    } else if (requestFusion == 2) {
                        this.sourceMode = requestFusion;
                        this.queue = queueSubscription;
                        QueueDrainHelper.request(subscription, this.prefetch);
                        return;
                    }
                }
                this.queue = QueueDrainHelper.createQueue(this.prefetch);
                QueueDrainHelper.request(subscription, this.prefetch);
            }
        }

        public void dispose() {
            SubscriptionHelper.cancel(this.f302s);
            if (this.wip.getAndIncrement() == 0) {
                SimpleQueue<T> simpleQueue = this.queue;
                if (simpleQueue != null) {
                    simpleQueue.clear();
                }
            }
        }

        public boolean isDisposed() {
            return SubscriptionHelper.isCancelled((Subscription) this.f302s.get());
        }

        public void onNext(T t) {
            if (!this.done) {
                if (this.sourceMode != 0 || this.queue.offer(t)) {
                    drain();
                    return;
                }
                ((Subscription) this.f302s.get()).cancel();
                onError(new MissingBackpressureException());
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.error = th;
            this.done = true;
            drain();
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                drain();
            }
        }

        /* access modifiers changed from: 0000 */
        public boolean add(MulticastSubscription<T> multicastSubscription) {
            MulticastSubscription[] multicastSubscriptionArr;
            MulticastSubscription[] multicastSubscriptionArr2;
            do {
                multicastSubscriptionArr = (MulticastSubscription[]) this.subscribers.get();
                if (multicastSubscriptionArr == TERMINATED) {
                    return false;
                }
                int length = multicastSubscriptionArr.length;
                multicastSubscriptionArr2 = new MulticastSubscription[(length + 1)];
                System.arraycopy(multicastSubscriptionArr, 0, multicastSubscriptionArr2, 0, length);
                multicastSubscriptionArr2[length] = multicastSubscription;
            } while (!this.subscribers.compareAndSet(multicastSubscriptionArr, multicastSubscriptionArr2));
            return true;
        }

        /* access modifiers changed from: 0000 */
        public void remove(MulticastSubscription<T> multicastSubscription) {
            MulticastSubscription<T>[] multicastSubscriptionArr;
            MulticastSubscription[] multicastSubscriptionArr2;
            do {
                multicastSubscriptionArr = (MulticastSubscription[]) this.subscribers.get();
                if (multicastSubscriptionArr != TERMINATED && multicastSubscriptionArr != EMPTY) {
                    int length = multicastSubscriptionArr.length;
                    int i = -1;
                    int i2 = 0;
                    while (true) {
                        if (i2 >= length) {
                            break;
                        } else if (multicastSubscriptionArr[i2] == multicastSubscription) {
                            i = i2;
                            break;
                        } else {
                            i2++;
                        }
                    }
                    if (i >= 0) {
                        if (length == 1) {
                            multicastSubscriptionArr2 = EMPTY;
                        } else {
                            MulticastSubscription[] multicastSubscriptionArr3 = new MulticastSubscription[(length - 1)];
                            System.arraycopy(multicastSubscriptionArr, 0, multicastSubscriptionArr3, 0, i);
                            System.arraycopy(multicastSubscriptionArr, i + 1, multicastSubscriptionArr3, i, (length - i) - 1);
                            multicastSubscriptionArr2 = multicastSubscriptionArr3;
                        }
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            } while (!this.subscribers.compareAndSet(multicastSubscriptionArr, multicastSubscriptionArr2));
        }

        /* access modifiers changed from: protected */
        public void subscribeActual(Subscriber<? super T> subscriber) {
            MulticastSubscription multicastSubscription = new MulticastSubscription(subscriber, this);
            subscriber.onSubscribe(multicastSubscription);
            if (!add(multicastSubscription)) {
                Throwable th = this.error;
                if (th != null) {
                    subscriber.onError(th);
                } else {
                    subscriber.onComplete();
                }
            } else if (multicastSubscription.isCancelled()) {
                remove(multicastSubscription);
            } else {
                drain();
            }
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            int i;
            if (this.wip.getAndIncrement() == 0) {
                SimpleQueue<T> simpleQueue = this.queue;
                int i2 = 1;
                while (true) {
                    MulticastSubscription[] multicastSubscriptionArr = (MulticastSubscription[]) this.subscribers.get();
                    int length = multicastSubscriptionArr.length;
                    if (!(simpleQueue == null || length == 0)) {
                        long j = Long.MAX_VALUE;
                        for (MulticastSubscription multicastSubscription : multicastSubscriptionArr) {
                            long j2 = multicastSubscription.get();
                            if (j2 != Long.MIN_VALUE && j > j2) {
                                j = j2;
                            }
                        }
                        long j3 = 0;
                        while (true) {
                            i = (j3 > j ? 1 : (j3 == j ? 0 : -1));
                            if (i == 0) {
                                break;
                            } else if (isDisposed()) {
                                simpleQueue.clear();
                                return;
                            } else {
                                boolean z = this.done;
                                if (z && !this.delayError) {
                                    Throwable th = this.error;
                                    if (th != null) {
                                        errorAll(th);
                                        return;
                                    }
                                }
                                try {
                                    Object poll = simpleQueue.poll();
                                    boolean z2 = poll == null;
                                    if (z && z2) {
                                        Throwable th2 = this.error;
                                        if (th2 != null) {
                                            errorAll(th2);
                                        } else {
                                            completeAll();
                                        }
                                        return;
                                    } else if (z2) {
                                        break;
                                    } else {
                                        for (MulticastSubscription multicastSubscription2 : multicastSubscriptionArr) {
                                            if (multicastSubscription2.get() != Long.MIN_VALUE) {
                                                multicastSubscription2.actual.onNext(poll);
                                            }
                                        }
                                        j3++;
                                    }
                                } catch (Throwable th3) {
                                    Exceptions.throwIfFatal(th3);
                                    SubscriptionHelper.cancel(this.f302s);
                                    errorAll(th3);
                                    return;
                                }
                            }
                        }
                        if (i == 0) {
                            if (isDisposed()) {
                                simpleQueue.clear();
                                return;
                            }
                            boolean z3 = this.done;
                            if (z3 && !this.delayError) {
                                Throwable th4 = this.error;
                                if (th4 != null) {
                                    errorAll(th4);
                                    return;
                                }
                            }
                            if (z3 && simpleQueue.isEmpty()) {
                                Throwable th5 = this.error;
                                if (th5 != null) {
                                    errorAll(th5);
                                } else {
                                    completeAll();
                                }
                                return;
                            }
                        }
                        for (MulticastSubscription produced : multicastSubscriptionArr) {
                            BackpressureHelper.produced(produced, j3);
                        }
                    }
                    i2 = this.wip.addAndGet(-i2);
                    if (i2 != 0) {
                        if (simpleQueue == null) {
                            simpleQueue = this.queue;
                        }
                    } else {
                        return;
                    }
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void errorAll(Throwable th) {
            MulticastSubscription[] multicastSubscriptionArr;
            for (MulticastSubscription multicastSubscription : (MulticastSubscription[]) this.subscribers.getAndSet(TERMINATED)) {
                if (multicastSubscription.get() != Long.MIN_VALUE) {
                    multicastSubscription.actual.onError(th);
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void completeAll() {
            MulticastSubscription[] multicastSubscriptionArr;
            for (MulticastSubscription multicastSubscription : (MulticastSubscription[]) this.subscribers.getAndSet(TERMINATED)) {
                if (multicastSubscription.get() != Long.MIN_VALUE) {
                    multicastSubscription.actual.onComplete();
                }
            }
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowablePublishMulticast$MulticastSubscription */
    static final class MulticastSubscription<T> extends AtomicLong implements Subscription {
        private static final long serialVersionUID = 8664815189257569791L;
        final Subscriber<? super T> actual;
        final MulticastProcessor<T> parent;

        MulticastSubscription(Subscriber<? super T> subscriber, MulticastProcessor<T> multicastProcessor) {
            this.actual = subscriber;
            this.parent = multicastProcessor;
        }

        public void request(long j) {
            if (SubscriptionHelper.validate(j)) {
                BackpressureHelper.addCancel(this, j);
                this.parent.drain();
            }
        }

        public void cancel() {
            getAndSet(Long.MIN_VALUE);
            this.parent.remove(this);
        }

        public boolean isCancelled() {
            return get() == Long.MIN_VALUE;
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowablePublishMulticast$OutputCanceller */
    static final class OutputCanceller<R> implements Subscriber<R>, Subscription {
        final Subscriber<? super R> actual;
        final MulticastProcessor<?> processor;

        /* renamed from: s */
        Subscription f303s;

        OutputCanceller(Subscriber<? super R> subscriber, MulticastProcessor<?> multicastProcessor) {
            this.actual = subscriber;
            this.processor = multicastProcessor;
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.f303s, subscription)) {
                this.f303s = subscription;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(R r) {
            this.actual.onNext(r);
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
            this.processor.dispose();
        }

        public void onComplete() {
            this.actual.onComplete();
            this.processor.dispose();
        }

        public void request(long j) {
            this.f303s.request(j);
        }

        public void cancel() {
            this.f303s.cancel();
            this.processor.dispose();
        }
    }

    public FlowablePublishMulticast(Publisher<T> publisher, Function<? super Flowable<T>, ? extends Publisher<? extends R>> function, int i, boolean z) {
        super(publisher);
        this.selector = function;
        this.prefetch = i;
        this.delayError = z;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super R> subscriber) {
        MulticastProcessor multicastProcessor = new MulticastProcessor(this.prefetch, this.delayError);
        try {
            ((Publisher) ObjectHelper.requireNonNull(this.selector.apply(multicastProcessor), "selector returned a null Publisher")).subscribe(new OutputCanceller(subscriber, multicastProcessor));
            this.source.subscribe(multicastProcessor);
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            EmptySubscription.error(th, subscriber);
        }
    }
}
