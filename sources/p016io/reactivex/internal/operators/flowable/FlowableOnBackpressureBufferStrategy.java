package p016io.reactivex.internal.operators.flowable;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.BackpressureOverflowStrategy;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.exceptions.MissingBackpressureException;
import p016io.reactivex.functions.Action;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;
import p016io.reactivex.internal.util.BackpressureHelper;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableOnBackpressureBufferStrategy */
public final class FlowableOnBackpressureBufferStrategy<T> extends AbstractFlowableWithUpstream<T, T> {
    final long bufferSize;
    final Action onOverflow;
    final BackpressureOverflowStrategy strategy;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableOnBackpressureBufferStrategy$OnBackpressureBufferStrategySubscriber */
    static final class OnBackpressureBufferStrategySubscriber<T> extends AtomicInteger implements Subscriber<T>, Subscription {
        private static final long serialVersionUID = 3240706908776709697L;
        final Subscriber<? super T> actual;
        final long bufferSize;
        volatile boolean cancelled;
        final Deque<T> deque = new ArrayDeque();
        volatile boolean done;
        Throwable error;
        final Action onOverflow;
        final AtomicLong requested = new AtomicLong();

        /* renamed from: s */
        Subscription f297s;
        final BackpressureOverflowStrategy strategy;

        OnBackpressureBufferStrategySubscriber(Subscriber<? super T> subscriber, Action action, BackpressureOverflowStrategy backpressureOverflowStrategy, long j) {
            this.actual = subscriber;
            this.onOverflow = action;
            this.strategy = backpressureOverflowStrategy;
            this.bufferSize = j;
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.f297s, subscription)) {
                this.f297s = subscription;
                this.actual.onSubscribe(this);
                subscription.request(LongCompanionObject.MAX_VALUE);
            }
        }

        public void onNext(T t) {
            boolean z;
            boolean z2;
            if (!this.done) {
                Deque<T> deque2 = this.deque;
                synchronized (deque2) {
                    z = false;
                    z2 = true;
                    if (((long) deque2.size()) == this.bufferSize) {
                        switch (this.strategy) {
                            case DROP_LATEST:
                                deque2.pollLast();
                                deque2.offer(t);
                                break;
                            case DROP_OLDEST:
                                deque2.poll();
                                deque2.offer(t);
                                break;
                        }
                        z2 = false;
                        z = true;
                    } else {
                        deque2.offer(t);
                        z2 = false;
                    }
                }
                if (z) {
                    if (this.onOverflow != null) {
                        try {
                            this.onOverflow.run();
                        } catch (Throwable th) {
                            Exceptions.throwIfFatal(th);
                            this.f297s.cancel();
                            onError(th);
                        }
                    }
                } else if (z2) {
                    this.f297s.cancel();
                    onError(new MissingBackpressureException());
                } else {
                    drain();
                }
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
            this.done = true;
            drain();
        }

        public void request(long j) {
            if (SubscriptionHelper.validate(j)) {
                BackpressureHelper.add(this.requested, j);
                drain();
            }
        }

        public void cancel() {
            this.cancelled = true;
            this.f297s.cancel();
            if (getAndIncrement() == 0) {
                clear(this.deque);
            }
        }

        /* access modifiers changed from: 0000 */
        public void clear(Deque<T> deque2) {
            synchronized (deque2) {
                deque2.clear();
            }
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            int i;
            boolean isEmpty;
            Object poll;
            if (getAndIncrement() == 0) {
                Deque<T> deque2 = this.deque;
                Subscriber<? super T> subscriber = this.actual;
                int i2 = 1;
                do {
                    long j = this.requested.get();
                    long j2 = 0;
                    while (true) {
                        i = (j2 > j ? 1 : (j2 == j ? 0 : -1));
                        if (i == 0) {
                            break;
                        } else if (this.cancelled) {
                            clear(deque2);
                            return;
                        } else {
                            boolean z = this.done;
                            synchronized (deque2) {
                                poll = deque2.poll();
                            }
                            boolean z2 = poll == null;
                            if (z) {
                                Throwable th = this.error;
                                if (th != null) {
                                    clear(deque2);
                                    subscriber.onError(th);
                                    return;
                                } else if (z2) {
                                    subscriber.onComplete();
                                    return;
                                }
                            }
                            if (z2) {
                                break;
                            }
                            subscriber.onNext(poll);
                            j2++;
                        }
                    }
                    if (i == 0) {
                        if (this.cancelled) {
                            clear(deque2);
                            return;
                        }
                        boolean z3 = this.done;
                        synchronized (deque2) {
                            isEmpty = deque2.isEmpty();
                        }
                        if (z3) {
                            Throwable th2 = this.error;
                            if (th2 != null) {
                                clear(deque2);
                                subscriber.onError(th2);
                                return;
                            } else if (isEmpty) {
                                subscriber.onComplete();
                                return;
                            }
                        }
                    }
                    if (j2 != 0) {
                        BackpressureHelper.produced(this.requested, j2);
                    }
                    i2 = addAndGet(-i2);
                } while (i2 != 0);
            }
        }
    }

    public FlowableOnBackpressureBufferStrategy(Publisher<T> publisher, long j, Action action, BackpressureOverflowStrategy backpressureOverflowStrategy) {
        super(publisher);
        this.bufferSize = j;
        this.onOverflow = action;
        this.strategy = backpressureOverflowStrategy;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> subscriber) {
        Publisher publisher = this.source;
        OnBackpressureBufferStrategySubscriber onBackpressureBufferStrategySubscriber = new OnBackpressureBufferStrategySubscriber(subscriber, this.onOverflow, this.strategy, this.bufferSize);
        publisher.subscribe(onBackpressureBufferStrategySubscriber);
    }
}
