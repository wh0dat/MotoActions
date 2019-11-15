package p016io.reactivex.internal.operators.flowable;

import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Subscriber;
import p016io.reactivex.Flowable;
import p016io.reactivex.internal.functions.ObjectHelper;
import p016io.reactivex.internal.fuseable.ConditionalSubscriber;
import p016io.reactivex.internal.subscriptions.BasicQueueSubscription;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;
import p016io.reactivex.internal.util.BackpressureHelper;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableFromArray */
public final class FlowableFromArray<T> extends Flowable<T> {
    final T[] array;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableFromArray$ArrayConditionalSubscription */
    static final class ArrayConditionalSubscription<T> extends BaseArraySubscription<T> {
        private static final long serialVersionUID = 2587302975077663557L;
        final ConditionalSubscriber<? super T> actual;

        ArrayConditionalSubscription(ConditionalSubscriber<? super T> conditionalSubscriber, T[] tArr) {
            super(tArr);
            this.actual = conditionalSubscriber;
        }

        /* access modifiers changed from: 0000 */
        public void fastPath() {
            Object[] objArr = this.array;
            int length = objArr.length;
            ConditionalSubscriber<? super T> conditionalSubscriber = this.actual;
            int i = this.index;
            while (i != length) {
                if (!this.cancelled) {
                    Object obj = objArr[i];
                    if (obj == null) {
                        conditionalSubscriber.onError(new NullPointerException("array element is null"));
                        return;
                    } else {
                        conditionalSubscriber.tryOnNext(obj);
                        i++;
                    }
                } else {
                    return;
                }
            }
            if (!this.cancelled) {
                conditionalSubscriber.onComplete();
            }
        }

        /* access modifiers changed from: 0000 */
        public void slowPath(long j) {
            Object[] objArr = this.array;
            int length = objArr.length;
            int i = this.index;
            ConditionalSubscriber<? super T> conditionalSubscriber = this.actual;
            long j2 = j;
            do {
                long j3 = 0;
                while (true) {
                    if (j3 == j2 || i == length) {
                        if (i == length) {
                            if (!this.cancelled) {
                                conditionalSubscriber.onComplete();
                            }
                            return;
                        }
                        j2 = get();
                        if (j3 == j2) {
                            this.index = i;
                            j2 = addAndGet(-j3);
                        }
                    } else if (!this.cancelled) {
                        Object obj = objArr[i];
                        if (obj == null) {
                            conditionalSubscriber.onError(new NullPointerException("array element is null"));
                            return;
                        }
                        if (conditionalSubscriber.tryOnNext(obj)) {
                            j3++;
                        }
                        i++;
                    } else {
                        return;
                    }
                }
            } while (j2 != 0);
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableFromArray$ArraySubscription */
    static final class ArraySubscription<T> extends BaseArraySubscription<T> {
        private static final long serialVersionUID = 2587302975077663557L;
        final Subscriber<? super T> actual;

        ArraySubscription(Subscriber<? super T> subscriber, T[] tArr) {
            super(tArr);
            this.actual = subscriber;
        }

        /* access modifiers changed from: 0000 */
        public void fastPath() {
            Object[] objArr = this.array;
            int length = objArr.length;
            Subscriber<? super T> subscriber = this.actual;
            int i = this.index;
            while (i != length) {
                if (!this.cancelled) {
                    Object obj = objArr[i];
                    if (obj == null) {
                        subscriber.onError(new NullPointerException("array element is null"));
                        return;
                    } else {
                        subscriber.onNext(obj);
                        i++;
                    }
                } else {
                    return;
                }
            }
            if (!this.cancelled) {
                subscriber.onComplete();
            }
        }

        /* access modifiers changed from: 0000 */
        public void slowPath(long j) {
            Object[] objArr = this.array;
            int length = objArr.length;
            int i = this.index;
            Subscriber<? super T> subscriber = this.actual;
            long j2 = j;
            do {
                long j3 = 0;
                while (true) {
                    if (j3 == j2 || i == length) {
                        if (i == length) {
                            if (!this.cancelled) {
                                subscriber.onComplete();
                            }
                            return;
                        }
                        j2 = get();
                        if (j3 == j2) {
                            this.index = i;
                            j2 = addAndGet(-j3);
                        }
                    } else if (!this.cancelled) {
                        Object obj = objArr[i];
                        if (obj == null) {
                            subscriber.onError(new NullPointerException("array element is null"));
                            return;
                        }
                        subscriber.onNext(obj);
                        j3++;
                        i++;
                    } else {
                        return;
                    }
                }
            } while (j2 != 0);
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableFromArray$BaseArraySubscription */
    static abstract class BaseArraySubscription<T> extends BasicQueueSubscription<T> {
        private static final long serialVersionUID = -2252972430506210021L;
        final T[] array;
        volatile boolean cancelled;
        int index;

        /* access modifiers changed from: 0000 */
        public abstract void fastPath();

        public final int requestFusion(int i) {
            return i & 1;
        }

        /* access modifiers changed from: 0000 */
        public abstract void slowPath(long j);

        BaseArraySubscription(T[] tArr) {
            this.array = tArr;
        }

        public final T poll() {
            int i = this.index;
            T[] tArr = this.array;
            if (i == tArr.length) {
                return null;
            }
            this.index = i + 1;
            return ObjectHelper.requireNonNull(tArr[i], "array element is null");
        }

        public final boolean isEmpty() {
            return this.index == this.array.length;
        }

        public final void clear() {
            this.index = this.array.length;
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

    public FlowableFromArray(T[] tArr) {
        this.array = tArr;
    }

    public void subscribeActual(Subscriber<? super T> subscriber) {
        if (subscriber instanceof ConditionalSubscriber) {
            subscriber.onSubscribe(new ArrayConditionalSubscription((ConditionalSubscriber) subscriber, this.array));
        } else {
            subscriber.onSubscribe(new ArraySubscription(subscriber, this.array));
        }
    }
}
