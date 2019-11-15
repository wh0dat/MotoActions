package p016io.reactivex.internal.operators.flowable;

import java.util.Iterator;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Subscriber;
import p016io.reactivex.Flowable;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.internal.functions.ObjectHelper;
import p016io.reactivex.internal.fuseable.ConditionalSubscriber;
import p016io.reactivex.internal.subscriptions.BasicQueueSubscription;
import p016io.reactivex.internal.subscriptions.EmptySubscription;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;
import p016io.reactivex.internal.util.BackpressureHelper;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableFromIterable */
public final class FlowableFromIterable<T> extends Flowable<T> {
    final Iterable<? extends T> source;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableFromIterable$BaseRangeSubscription */
    static abstract class BaseRangeSubscription<T> extends BasicQueueSubscription<T> {
        private static final long serialVersionUID = -2252972430506210021L;
        volatile boolean cancelled;

        /* renamed from: it */
        Iterator<? extends T> f285it;
        boolean once;

        /* access modifiers changed from: 0000 */
        public abstract void fastPath();

        public final int requestFusion(int i) {
            return i & 1;
        }

        /* access modifiers changed from: 0000 */
        public abstract void slowPath(long j);

        BaseRangeSubscription(Iterator<? extends T> it) {
            this.f285it = it;
        }

        public final T poll() {
            if (this.f285it == null) {
                return null;
            }
            if (!this.once) {
                this.once = true;
            } else if (!this.f285it.hasNext()) {
                return null;
            }
            return ObjectHelper.requireNonNull(this.f285it.next(), "Iterator.next() returned a null value");
        }

        public final boolean isEmpty() {
            return this.f285it == null || !this.f285it.hasNext();
        }

        public final void clear() {
            this.f285it = null;
        }

        public final void request(long j) {
            if (SubscriptionHelper.validate(j) && BackpressureHelper.add(this, j) == 0) {
                if (j == LongCompanionObject.MAX_VALUE) {
                    fastPath();
                } else {
                    slowPath(j);
                }
            }
        }

        public final void cancel() {
            this.cancelled = true;
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableFromIterable$IteratorConditionalSubscription */
    static final class IteratorConditionalSubscription<T> extends BaseRangeSubscription<T> {
        private static final long serialVersionUID = -6022804456014692607L;
        final ConditionalSubscriber<? super T> actual;

        IteratorConditionalSubscription(ConditionalSubscriber<? super T> conditionalSubscriber, Iterator<? extends T> it) {
            super(it);
            this.actual = conditionalSubscriber;
        }

        /* access modifiers changed from: 0000 */
        public void fastPath() {
            Iterator it = this.f285it;
            ConditionalSubscriber<? super T> conditionalSubscriber = this.actual;
            while (!this.cancelled) {
                try {
                    Object next = it.next();
                    if (!this.cancelled) {
                        if (next == null) {
                            conditionalSubscriber.onError(new NullPointerException("Iterator.next() returned a null value"));
                            return;
                        }
                        conditionalSubscriber.tryOnNext(next);
                        if (!this.cancelled) {
                            try {
                                if (!it.hasNext()) {
                                    if (!this.cancelled) {
                                        conditionalSubscriber.onComplete();
                                    }
                                    return;
                                }
                            } catch (Throwable th) {
                                Exceptions.throwIfFatal(th);
                                conditionalSubscriber.onError(th);
                                return;
                            }
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                } catch (Throwable th2) {
                    Exceptions.throwIfFatal(th2);
                    conditionalSubscriber.onError(th2);
                    return;
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void slowPath(long j) {
            Iterator it = this.f285it;
            ConditionalSubscriber<? super T> conditionalSubscriber = this.actual;
            long j2 = j;
            do {
                long j3 = 0;
                while (true) {
                    if (j3 == j2) {
                        j2 = get();
                        if (j3 == j2) {
                            j2 = addAndGet(-j3);
                        }
                    } else if (!this.cancelled) {
                        try {
                            Object next = it.next();
                            if (!this.cancelled) {
                                if (next == null) {
                                    conditionalSubscriber.onError(new NullPointerException("Iterator.next() returned a null value"));
                                    return;
                                }
                                boolean tryOnNext = conditionalSubscriber.tryOnNext(next);
                                if (!this.cancelled) {
                                    try {
                                        if (!it.hasNext()) {
                                            if (!this.cancelled) {
                                                conditionalSubscriber.onComplete();
                                            }
                                            return;
                                        } else if (tryOnNext) {
                                            j3++;
                                        }
                                    } catch (Throwable th) {
                                        Exceptions.throwIfFatal(th);
                                        conditionalSubscriber.onError(th);
                                        return;
                                    }
                                } else {
                                    return;
                                }
                            } else {
                                return;
                            }
                        } catch (Throwable th2) {
                            Exceptions.throwIfFatal(th2);
                            conditionalSubscriber.onError(th2);
                            return;
                        }
                    } else {
                        return;
                    }
                }
            } while (j2 != 0);
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableFromIterable$IteratorSubscription */
    static final class IteratorSubscription<T> extends BaseRangeSubscription<T> {
        private static final long serialVersionUID = -6022804456014692607L;
        final Subscriber<? super T> actual;

        IteratorSubscription(Subscriber<? super T> subscriber, Iterator<? extends T> it) {
            super(it);
            this.actual = subscriber;
        }

        /* access modifiers changed from: 0000 */
        public void fastPath() {
            Iterator it = this.f285it;
            Subscriber<? super T> subscriber = this.actual;
            while (!this.cancelled) {
                try {
                    Object next = it.next();
                    if (!this.cancelled) {
                        if (next == null) {
                            subscriber.onError(new NullPointerException("Iterator.next() returned a null value"));
                            return;
                        }
                        subscriber.onNext(next);
                        if (!this.cancelled) {
                            try {
                                if (!it.hasNext()) {
                                    if (!this.cancelled) {
                                        subscriber.onComplete();
                                    }
                                    return;
                                }
                            } catch (Throwable th) {
                                Exceptions.throwIfFatal(th);
                                subscriber.onError(th);
                                return;
                            }
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                } catch (Throwable th2) {
                    Exceptions.throwIfFatal(th2);
                    subscriber.onError(th2);
                    return;
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void slowPath(long j) {
            Iterator it = this.f285it;
            Subscriber<? super T> subscriber = this.actual;
            long j2 = j;
            do {
                long j3 = 0;
                while (true) {
                    if (j3 == j2) {
                        j2 = get();
                        if (j3 == j2) {
                            j2 = addAndGet(-j3);
                        }
                    } else if (!this.cancelled) {
                        try {
                            Object next = it.next();
                            if (!this.cancelled) {
                                if (next == null) {
                                    subscriber.onError(new NullPointerException("Iterator.next() returned a null value"));
                                    return;
                                }
                                subscriber.onNext(next);
                                if (!this.cancelled) {
                                    try {
                                        if (!it.hasNext()) {
                                            if (!this.cancelled) {
                                                subscriber.onComplete();
                                            }
                                            return;
                                        }
                                        j3++;
                                    } catch (Throwable th) {
                                        Exceptions.throwIfFatal(th);
                                        subscriber.onError(th);
                                        return;
                                    }
                                } else {
                                    return;
                                }
                            } else {
                                return;
                            }
                        } catch (Throwable th2) {
                            Exceptions.throwIfFatal(th2);
                            subscriber.onError(th2);
                            return;
                        }
                    } else {
                        return;
                    }
                }
            } while (j2 != 0);
        }
    }

    public FlowableFromIterable(Iterable<? extends T> iterable) {
        this.source = iterable;
    }

    public void subscribeActual(Subscriber<? super T> subscriber) {
        try {
            subscribe(subscriber, this.source.iterator());
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            EmptySubscription.error(th, subscriber);
        }
    }

    public static <T> void subscribe(Subscriber<? super T> subscriber, Iterator<? extends T> it) {
        try {
            if (!it.hasNext()) {
                EmptySubscription.complete(subscriber);
                return;
            }
            if (subscriber instanceof ConditionalSubscriber) {
                subscriber.onSubscribe(new IteratorConditionalSubscription((ConditionalSubscriber) subscriber, it));
            } else {
                subscriber.onSubscribe(new IteratorSubscription(subscriber, it));
            }
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            EmptySubscription.error(th, subscriber);
        }
    }
}
