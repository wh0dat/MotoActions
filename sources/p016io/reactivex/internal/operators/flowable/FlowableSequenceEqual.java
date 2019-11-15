package p016io.reactivex.internal.operators.flowable;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.Flowable;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.exceptions.MissingBackpressureException;
import p016io.reactivex.functions.BiPredicate;
import p016io.reactivex.internal.fuseable.QueueSubscription;
import p016io.reactivex.internal.fuseable.SimpleQueue;
import p016io.reactivex.internal.queue.SpscArrayQueue;
import p016io.reactivex.internal.subscriptions.DeferredScalarSubscription;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;
import p016io.reactivex.internal.util.AtomicThrowable;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableSequenceEqual */
public final class FlowableSequenceEqual<T> extends Flowable<Boolean> {
    final BiPredicate<? super T, ? super T> comparer;
    final Publisher<? extends T> first;
    final int prefetch;
    final Publisher<? extends T> second;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableSequenceEqual$EqualCoordinator */
    static final class EqualCoordinator<T> extends DeferredScalarSubscription<Boolean> implements EqualCoordinatorHelper {
        private static final long serialVersionUID = -6178010334400373240L;
        final BiPredicate<? super T, ? super T> comparer;
        final AtomicThrowable error;
        final EqualSubscriber<T> first;
        final EqualSubscriber<T> second;

        /* renamed from: v1 */
        T f313v1;

        /* renamed from: v2 */
        T f314v2;
        final AtomicInteger wip = new AtomicInteger();

        EqualCoordinator(Subscriber<? super Boolean> subscriber, int i, BiPredicate<? super T, ? super T> biPredicate) {
            super(subscriber);
            this.comparer = biPredicate;
            this.first = new EqualSubscriber<>(this, i);
            this.second = new EqualSubscriber<>(this, i);
            this.error = new AtomicThrowable();
        }

        /* access modifiers changed from: 0000 */
        public void subscribe(Publisher<? extends T> publisher, Publisher<? extends T> publisher2) {
            publisher.subscribe(this.first);
            publisher2.subscribe(this.second);
        }

        public void cancel() {
            super.cancel();
            this.first.cancel();
            this.second.cancel();
            if (this.wip.getAndIncrement() == 0) {
                this.first.clear();
                this.second.clear();
            }
        }

        /* access modifiers changed from: 0000 */
        public void cancelAndClear() {
            this.first.cancel();
            this.first.clear();
            this.second.cancel();
            this.second.clear();
        }

        public void drain() {
            if (this.wip.getAndIncrement() == 0) {
                int i = 1;
                do {
                    SimpleQueue<T> simpleQueue = this.first.queue;
                    SimpleQueue<T> simpleQueue2 = this.second.queue;
                    if (simpleQueue != null && simpleQueue2 != null) {
                        while (!isCancelled()) {
                            if (((Throwable) this.error.get()) != null) {
                                cancelAndClear();
                                this.actual.onError(this.error.terminate());
                                return;
                            }
                            boolean z = this.first.done;
                            T t = this.f313v1;
                            if (t == null) {
                                try {
                                    t = simpleQueue.poll();
                                    this.f313v1 = t;
                                } catch (Throwable th) {
                                    Exceptions.throwIfFatal(th);
                                    cancelAndClear();
                                    this.error.addThrowable(th);
                                    this.actual.onError(this.error.terminate());
                                    return;
                                }
                            }
                            boolean z2 = t == null;
                            boolean z3 = this.second.done;
                            T t2 = this.f314v2;
                            if (t2 == null) {
                                try {
                                    t2 = simpleQueue2.poll();
                                    this.f314v2 = t2;
                                } catch (Throwable th2) {
                                    Exceptions.throwIfFatal(th2);
                                    cancelAndClear();
                                    this.error.addThrowable(th2);
                                    this.actual.onError(this.error.terminate());
                                    return;
                                }
                            }
                            boolean z4 = t2 == null;
                            if (z && z3 && z2 && z4) {
                                complete(Boolean.valueOf(true));
                                return;
                            } else if (z && z3 && z2 != z4) {
                                cancelAndClear();
                                complete(Boolean.valueOf(false));
                                return;
                            } else if (!z2 && !z4) {
                                try {
                                    if (!this.comparer.test(t, t2)) {
                                        cancelAndClear();
                                        complete(Boolean.valueOf(false));
                                        return;
                                    }
                                    this.f313v1 = null;
                                    this.f314v2 = null;
                                    this.first.request();
                                    this.second.request();
                                } catch (Throwable th3) {
                                    Exceptions.throwIfFatal(th3);
                                    cancelAndClear();
                                    this.error.addThrowable(th3);
                                    this.actual.onError(this.error.terminate());
                                    return;
                                }
                            }
                        }
                        this.first.clear();
                        this.second.clear();
                        return;
                    } else if (isCancelled()) {
                        this.first.clear();
                        this.second.clear();
                        return;
                    } else if (((Throwable) this.error.get()) != null) {
                        cancelAndClear();
                        this.actual.onError(this.error.terminate());
                        return;
                    }
                    i = this.wip.addAndGet(-i);
                } while (i != 0);
            }
        }

        public void innerError(Throwable th) {
            if (this.error.addThrowable(th)) {
                drain();
            } else {
                RxJavaPlugins.onError(th);
            }
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableSequenceEqual$EqualCoordinatorHelper */
    interface EqualCoordinatorHelper {
        void drain();

        void innerError(Throwable th);
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableSequenceEqual$EqualSubscriber */
    static final class EqualSubscriber<T> extends AtomicReference<Subscription> implements Subscriber<T> {
        private static final long serialVersionUID = 4804128302091633067L;
        volatile boolean done;
        final int limit;
        final EqualCoordinatorHelper parent;
        final int prefetch;
        long produced;
        volatile SimpleQueue<T> queue;
        int sourceMode;

        EqualSubscriber(EqualCoordinatorHelper equalCoordinatorHelper, int i) {
            this.parent = equalCoordinatorHelper;
            this.limit = i - (i >> 2);
            this.prefetch = i;
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.setOnce(this, subscription)) {
                if (subscription instanceof QueueSubscription) {
                    QueueSubscription queueSubscription = (QueueSubscription) subscription;
                    int requestFusion = queueSubscription.requestFusion(3);
                    if (requestFusion == 1) {
                        this.sourceMode = requestFusion;
                        this.queue = queueSubscription;
                        this.done = true;
                        this.parent.drain();
                        return;
                    } else if (requestFusion == 2) {
                        this.sourceMode = requestFusion;
                        this.queue = queueSubscription;
                        subscription.request((long) this.prefetch);
                        return;
                    }
                }
                this.queue = new SpscArrayQueue(this.prefetch);
                subscription.request((long) this.prefetch);
            }
        }

        public void onNext(T t) {
            if (this.sourceMode != 0 || this.queue.offer(t)) {
                this.parent.drain();
            } else {
                onError(new MissingBackpressureException());
            }
        }

        public void onError(Throwable th) {
            this.parent.innerError(th);
        }

        public void onComplete() {
            this.done = true;
            this.parent.drain();
        }

        public void request() {
            if (this.sourceMode != 1) {
                long j = this.produced + 1;
                if (j >= ((long) this.limit)) {
                    this.produced = 0;
                    ((Subscription) get()).request(j);
                    return;
                }
                this.produced = j;
            }
        }

        public void cancel() {
            SubscriptionHelper.cancel(this);
        }

        /* access modifiers changed from: 0000 */
        public void clear() {
            SimpleQueue<T> simpleQueue = this.queue;
            if (simpleQueue != null) {
                simpleQueue.clear();
            }
        }
    }

    public FlowableSequenceEqual(Publisher<? extends T> publisher, Publisher<? extends T> publisher2, BiPredicate<? super T, ? super T> biPredicate, int i) {
        this.first = publisher;
        this.second = publisher2;
        this.comparer = biPredicate;
        this.prefetch = i;
    }

    public void subscribeActual(Subscriber<? super Boolean> subscriber) {
        EqualCoordinator equalCoordinator = new EqualCoordinator(subscriber, this.prefetch, this.comparer);
        subscriber.onSubscribe(equalCoordinator);
        equalCoordinator.subscribe(this.first, this.second);
    }
}
