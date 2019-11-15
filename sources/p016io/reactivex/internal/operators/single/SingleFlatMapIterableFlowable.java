package p016io.reactivex.internal.operators.single;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicLong;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Subscriber;
import p016io.reactivex.Flowable;
import p016io.reactivex.SingleObserver;
import p016io.reactivex.SingleSource;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.Function;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.internal.functions.ObjectHelper;
import p016io.reactivex.internal.subscriptions.BasicIntQueueSubscription;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;
import p016io.reactivex.internal.util.BackpressureHelper;

/* renamed from: io.reactivex.internal.operators.single.SingleFlatMapIterableFlowable */
public final class SingleFlatMapIterableFlowable<T, R> extends Flowable<R> {
    final Function<? super T, ? extends Iterable<? extends R>> mapper;
    final SingleSource<T> source;

    /* renamed from: io.reactivex.internal.operators.single.SingleFlatMapIterableFlowable$FlatMapIterableObserver */
    static final class FlatMapIterableObserver<T, R> extends BasicIntQueueSubscription<R> implements SingleObserver<T> {
        private static final long serialVersionUID = -8938804753851907758L;
        final Subscriber<? super R> actual;
        volatile boolean cancelled;

        /* renamed from: d */
        Disposable f512d;

        /* renamed from: it */
        volatile Iterator<? extends R> f513it;
        final Function<? super T, ? extends Iterable<? extends R>> mapper;
        boolean outputFused;
        final AtomicLong requested = new AtomicLong();

        FlatMapIterableObserver(Subscriber<? super R> subscriber, Function<? super T, ? extends Iterable<? extends R>> function) {
            this.actual = subscriber;
            this.mapper = function;
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.f512d, disposable)) {
                this.f512d = disposable;
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(T t) {
            try {
                Iterator<? extends R> it = ((Iterable) this.mapper.apply(t)).iterator();
                if (!it.hasNext()) {
                    this.actual.onComplete();
                    return;
                }
                this.f513it = it;
                drain();
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                this.actual.onError(th);
            }
        }

        public void onError(Throwable th) {
            this.f512d = DisposableHelper.DISPOSED;
            this.actual.onError(th);
        }

        public void request(long j) {
            if (SubscriptionHelper.validate(j)) {
                BackpressureHelper.add(this.requested, j);
                drain();
            }
        }

        public void cancel() {
            this.cancelled = true;
            this.f512d.dispose();
            this.f512d = DisposableHelper.DISPOSED;
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            if (getAndIncrement() == 0) {
                Subscriber<? super R> subscriber = this.actual;
                Iterator<? extends R> it = this.f513it;
                if (!this.outputFused || it == null) {
                    int i = 1;
                    while (true) {
                        if (it != null) {
                            long j = this.requested.get();
                            if (j == LongCompanionObject.MAX_VALUE) {
                                slowPath(subscriber, it);
                                return;
                            }
                            long j2 = 0;
                            while (j2 != j) {
                                if (!this.cancelled) {
                                    try {
                                        subscriber.onNext(ObjectHelper.requireNonNull(it.next(), "The iterator returned a null value"));
                                        if (!this.cancelled) {
                                            j2++;
                                            try {
                                                if (!it.hasNext()) {
                                                    subscriber.onComplete();
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
                                    } catch (Throwable th2) {
                                        Exceptions.throwIfFatal(th2);
                                        subscriber.onError(th2);
                                        return;
                                    }
                                } else {
                                    return;
                                }
                            }
                            if (j2 != 0) {
                                BackpressureHelper.produced(this.requested, j2);
                            }
                        }
                        i = addAndGet(-i);
                        if (i != 0) {
                            if (it == null) {
                                it = this.f513it;
                            }
                        } else {
                            return;
                        }
                    }
                } else {
                    subscriber.onNext(null);
                    subscriber.onComplete();
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void slowPath(Subscriber<? super R> subscriber, Iterator<? extends R> it) {
            while (!this.cancelled) {
                try {
                    subscriber.onNext(it.next());
                    if (!this.cancelled) {
                        try {
                            if (!it.hasNext()) {
                                subscriber.onComplete();
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
                } catch (Throwable th2) {
                    Exceptions.throwIfFatal(th2);
                    subscriber.onError(th2);
                    return;
                }
            }
        }

        public int requestFusion(int i) {
            if ((i & 2) == 0) {
                return 0;
            }
            this.outputFused = true;
            return 2;
        }

        public void clear() {
            this.f513it = null;
        }

        public boolean isEmpty() {
            return this.f513it == null;
        }

        public R poll() throws Exception {
            Iterator<? extends R> it = this.f513it;
            if (it == null) {
                return null;
            }
            R requireNonNull = ObjectHelper.requireNonNull(it.next(), "The iterator returned a null value");
            if (!it.hasNext()) {
                this.f513it = null;
            }
            return requireNonNull;
        }
    }

    public SingleFlatMapIterableFlowable(SingleSource<T> singleSource, Function<? super T, ? extends Iterable<? extends R>> function) {
        this.source = singleSource;
        this.mapper = function;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super R> subscriber) {
        this.source.subscribe(new FlatMapIterableObserver(subscriber, this.mapper));
    }
}
