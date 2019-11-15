package p016io.reactivex.internal.operators.flowable;

import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Subscriber;
import p016io.reactivex.Flowable;
import p016io.reactivex.internal.fuseable.ConditionalSubscriber;
import p016io.reactivex.internal.subscriptions.BasicQueueSubscription;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;
import p016io.reactivex.internal.util.BackpressureHelper;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableRangeLong */
public final class FlowableRangeLong extends Flowable<Long> {
    final long end;
    final long start;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableRangeLong$BaseRangeSubscription */
    static abstract class BaseRangeSubscription extends BasicQueueSubscription<Long> {
        private static final long serialVersionUID = -2252972430506210021L;
        volatile boolean cancelled;
        final long end;
        long index;

        /* access modifiers changed from: 0000 */
        public abstract void fastPath();

        public final int requestFusion(int i) {
            return i & 1;
        }

        /* access modifiers changed from: 0000 */
        public abstract void slowPath(long j);

        BaseRangeSubscription(long j, long j2) {
            this.index = j;
            this.end = j2;
        }

        public final Long poll() {
            long j = this.index;
            if (j == this.end) {
                return null;
            }
            this.index = 1 + j;
            return Long.valueOf(j);
        }

        public final boolean isEmpty() {
            return this.index == this.end;
        }

        public final void clear() {
            this.index = this.end;
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

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableRangeLong$RangeConditionalSubscription */
    static final class RangeConditionalSubscription extends BaseRangeSubscription {
        private static final long serialVersionUID = 2587302975077663557L;
        final ConditionalSubscriber<? super Long> actual;

        RangeConditionalSubscription(ConditionalSubscriber<? super Long> conditionalSubscriber, long j, long j2) {
            super(j, j2);
            this.actual = conditionalSubscriber;
        }

        /* access modifiers changed from: 0000 */
        public void fastPath() {
            long j = this.end;
            ConditionalSubscriber<? super Long> conditionalSubscriber = this.actual;
            long j2 = this.index;
            while (j2 != j) {
                if (!this.cancelled) {
                    conditionalSubscriber.tryOnNext(Long.valueOf(j2));
                    j2++;
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
            long j2 = this.end;
            long j3 = this.index;
            ConditionalSubscriber<? super Long> conditionalSubscriber = this.actual;
            long j4 = j3;
            long j5 = j;
            do {
                long j6 = 0;
                while (true) {
                    if (j6 == j5 || j4 == j2) {
                        if (j4 == j2) {
                            if (!this.cancelled) {
                                conditionalSubscriber.onComplete();
                            }
                            return;
                        }
                        j5 = get();
                        if (j6 == j5) {
                            this.index = j4;
                            j5 = addAndGet(-j6);
                        }
                    } else if (!this.cancelled) {
                        if (conditionalSubscriber.tryOnNext(Long.valueOf(j4))) {
                            j6++;
                        }
                        j4++;
                    } else {
                        return;
                    }
                }
            } while (j5 != 0);
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableRangeLong$RangeSubscription */
    static final class RangeSubscription extends BaseRangeSubscription {
        private static final long serialVersionUID = 2587302975077663557L;
        final Subscriber<? super Long> actual;

        RangeSubscription(Subscriber<? super Long> subscriber, long j, long j2) {
            super(j, j2);
            this.actual = subscriber;
        }

        /* access modifiers changed from: 0000 */
        public void fastPath() {
            long j = this.end;
            Subscriber<? super Long> subscriber = this.actual;
            long j2 = this.index;
            while (j2 != j) {
                if (!this.cancelled) {
                    subscriber.onNext(Long.valueOf(j2));
                    j2++;
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
            long j2 = this.end;
            long j3 = this.index;
            Subscriber<? super Long> subscriber = this.actual;
            long j4 = j3;
            long j5 = j;
            do {
                long j6 = 0;
                while (true) {
                    if (j6 == j5 || j4 == j2) {
                        if (j4 == j2) {
                            if (!this.cancelled) {
                                subscriber.onComplete();
                            }
                            return;
                        }
                        j5 = get();
                        if (j6 == j5) {
                            this.index = j4;
                            j5 = addAndGet(-j6);
                        }
                    } else if (!this.cancelled) {
                        subscriber.onNext(Long.valueOf(j4));
                        j6++;
                        j4++;
                    } else {
                        return;
                    }
                }
            } while (j5 != 0);
        }
    }

    public FlowableRangeLong(long j, long j2) {
        this.start = j;
        this.end = j + j2;
    }

    public void subscribeActual(Subscriber<? super Long> subscriber) {
        if (subscriber instanceof ConditionalSubscriber) {
            RangeConditionalSubscription rangeConditionalSubscription = new RangeConditionalSubscription((ConditionalSubscriber) subscriber, this.start, this.end);
            subscriber.onSubscribe(rangeConditionalSubscription);
            return;
        }
        RangeSubscription rangeSubscription = new RangeSubscription(subscriber, this.start, this.end);
        subscriber.onSubscribe(rangeSubscription);
    }
}
