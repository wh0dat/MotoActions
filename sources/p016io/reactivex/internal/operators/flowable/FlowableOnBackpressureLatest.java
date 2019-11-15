package p016io.reactivex.internal.operators.flowable;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;
import p016io.reactivex.internal.util.BackpressureHelper;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableOnBackpressureLatest */
public final class FlowableOnBackpressureLatest<T> extends AbstractFlowableWithUpstream<T, T> {

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableOnBackpressureLatest$BackpressureLatestSubscriber */
    static final class BackpressureLatestSubscriber<T> extends AtomicInteger implements Subscriber<T>, Subscription {
        private static final long serialVersionUID = 163080509307634843L;
        final Subscriber<? super T> actual;
        volatile boolean cancelled;
        final AtomicReference<T> current = new AtomicReference<>();
        volatile boolean done;
        Throwable error;
        final AtomicLong requested = new AtomicLong();

        /* renamed from: s */
        Subscription f300s;

        BackpressureLatestSubscriber(Subscriber<? super T> subscriber) {
            this.actual = subscriber;
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.f300s, subscription)) {
                this.f300s = subscription;
                this.actual.onSubscribe(this);
                subscription.request(LongCompanionObject.MAX_VALUE);
            }
        }

        public void onNext(T t) {
            this.current.lazySet(t);
            drain();
        }

        public void onError(Throwable th) {
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
            if (!this.cancelled) {
                this.cancelled = true;
                this.f300s.cancel();
                if (getAndIncrement() == 0) {
                    this.current.lazySet(null);
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            boolean z;
            if (getAndIncrement() == 0) {
                Subscriber<? super T> subscriber = this.actual;
                AtomicLong atomicLong = this.requested;
                AtomicReference<T> atomicReference = this.current;
                int i = 1;
                do {
                    long j = 0;
                    while (true) {
                        z = false;
                        if (j == atomicLong.get()) {
                            break;
                        }
                        boolean z2 = this.done;
                        Object andSet = atomicReference.getAndSet(null);
                        boolean z3 = andSet == null;
                        if (!checkTerminated(z2, z3, subscriber, atomicReference)) {
                            if (z3) {
                                break;
                            }
                            subscriber.onNext(andSet);
                            j++;
                        } else {
                            return;
                        }
                    }
                    if (j == atomicLong.get()) {
                        boolean z4 = this.done;
                        if (atomicReference.get() == null) {
                            z = true;
                        }
                        if (checkTerminated(z4, z, subscriber, atomicReference)) {
                            return;
                        }
                    }
                    if (j != 0) {
                        BackpressureHelper.produced(atomicLong, j);
                    }
                    i = addAndGet(-i);
                } while (i != 0);
            }
        }

        /* access modifiers changed from: 0000 */
        public boolean checkTerminated(boolean z, boolean z2, Subscriber<?> subscriber, AtomicReference<T> atomicReference) {
            if (this.cancelled) {
                atomicReference.lazySet(null);
                return true;
            }
            if (z) {
                Throwable th = this.error;
                if (th != null) {
                    atomicReference.lazySet(null);
                    subscriber.onError(th);
                    return true;
                } else if (z2) {
                    subscriber.onComplete();
                    return true;
                }
            }
            return false;
        }
    }

    public FlowableOnBackpressureLatest(Publisher<T> publisher) {
        super(publisher);
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> subscriber) {
        this.source.subscribe(new BackpressureLatestSubscriber(subscriber));
    }
}
