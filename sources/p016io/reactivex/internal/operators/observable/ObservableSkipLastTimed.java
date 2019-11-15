package p016io.reactivex.internal.operators.observable;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import p016io.reactivex.ObservableSource;
import p016io.reactivex.Observer;
import p016io.reactivex.Scheduler;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.internal.queue.SpscLinkedArrayQueue;

/* renamed from: io.reactivex.internal.operators.observable.ObservableSkipLastTimed */
public final class ObservableSkipLastTimed<T> extends AbstractObservableWithUpstream<T, T> {
    final int bufferSize;
    final boolean delayError;
    final Scheduler scheduler;
    final long time;
    final TimeUnit unit;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableSkipLastTimed$SkipLastTimedObserver */
    static final class SkipLastTimedObserver<T> extends AtomicInteger implements Observer<T>, Disposable {
        private static final long serialVersionUID = -5677354903406201275L;
        final Observer<? super T> actual;
        volatile boolean cancelled;
        final boolean delayError;
        volatile boolean done;
        Throwable error;
        final SpscLinkedArrayQueue<Object> queue;

        /* renamed from: s */
        Disposable f469s;
        final Scheduler scheduler;
        final long time;
        final TimeUnit unit;

        SkipLastTimedObserver(Observer<? super T> observer, long j, TimeUnit timeUnit, Scheduler scheduler2, int i, boolean z) {
            this.actual = observer;
            this.time = j;
            this.unit = timeUnit;
            this.scheduler = scheduler2;
            this.queue = new SpscLinkedArrayQueue<>(i);
            this.delayError = z;
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.f469s, disposable)) {
                this.f469s = disposable;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            this.queue.offer(Long.valueOf(this.scheduler.now(this.unit)), t);
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

        public void dispose() {
            if (!this.cancelled) {
                this.cancelled = true;
                this.f469s.dispose();
                if (getAndIncrement() == 0) {
                    this.queue.clear();
                }
            }
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            if (getAndIncrement() == 0) {
                Observer<? super T> observer = this.actual;
                SpscLinkedArrayQueue<Object> spscLinkedArrayQueue = this.queue;
                boolean z = this.delayError;
                TimeUnit timeUnit = this.unit;
                Scheduler scheduler2 = this.scheduler;
                long j = this.time;
                int i = 1;
                while (!this.cancelled) {
                    boolean z2 = this.done;
                    Long l = (Long) spscLinkedArrayQueue.peek();
                    boolean z3 = l == null;
                    long now = scheduler2.now(timeUnit);
                    if (!z3 && l.longValue() > now - j) {
                        z3 = true;
                    }
                    if (z2) {
                        if (!z) {
                            Throwable th = this.error;
                            if (th != null) {
                                this.queue.clear();
                                observer.onError(th);
                                return;
                            } else if (z3) {
                                observer.onComplete();
                                return;
                            }
                        } else if (z3) {
                            Throwable th2 = this.error;
                            if (th2 != null) {
                                observer.onError(th2);
                            } else {
                                observer.onComplete();
                            }
                            return;
                        }
                    }
                    if (z3) {
                        i = addAndGet(-i);
                        if (i == 0) {
                            return;
                        }
                    } else {
                        spscLinkedArrayQueue.poll();
                        observer.onNext(spscLinkedArrayQueue.poll());
                    }
                }
                this.queue.clear();
            }
        }
    }

    public ObservableSkipLastTimed(ObservableSource<T> observableSource, long j, TimeUnit timeUnit, Scheduler scheduler2, int i, boolean z) {
        super(observableSource);
        this.time = j;
        this.unit = timeUnit;
        this.scheduler = scheduler2;
        this.bufferSize = i;
        this.delayError = z;
    }

    public void subscribeActual(Observer<? super T> observer) {
        ObservableSource observableSource = this.source;
        SkipLastTimedObserver skipLastTimedObserver = new SkipLastTimedObserver(observer, this.time, this.unit, this.scheduler, this.bufferSize, this.delayError);
        observableSource.subscribe(skipLastTimedObserver);
    }
}
