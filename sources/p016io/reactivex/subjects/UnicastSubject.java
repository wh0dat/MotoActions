package p016io.reactivex.subjects;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import p016io.reactivex.Observer;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.disposables.EmptyDisposable;
import p016io.reactivex.internal.functions.ObjectHelper;
import p016io.reactivex.internal.observers.BasicIntQueueDisposable;
import p016io.reactivex.internal.queue.SpscLinkedArrayQueue;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.subjects.UnicastSubject */
public final class UnicastSubject<T> extends Subject<T> {
    final AtomicReference<Observer<? super T>> actual;
    volatile boolean disposed;
    volatile boolean done;
    boolean enableOperatorFusion;
    Throwable error;
    final AtomicReference<Runnable> onTerminate;
    final AtomicBoolean once;
    final SpscLinkedArrayQueue<T> queue;
    final BasicIntQueueDisposable<T> wip;

    /* renamed from: io.reactivex.subjects.UnicastSubject$UnicastQueueDisposable */
    final class UnicastQueueDisposable extends BasicIntQueueDisposable<T> {
        private static final long serialVersionUID = 7926949470189395511L;

        UnicastQueueDisposable() {
        }

        public int requestFusion(int i) {
            if ((i & 2) == 0) {
                return 0;
            }
            UnicastSubject.this.enableOperatorFusion = true;
            return 2;
        }

        public T poll() throws Exception {
            return UnicastSubject.this.queue.poll();
        }

        public boolean isEmpty() {
            return UnicastSubject.this.queue.isEmpty();
        }

        public void clear() {
            UnicastSubject.this.queue.clear();
        }

        public void dispose() {
            if (!UnicastSubject.this.disposed) {
                UnicastSubject.this.disposed = true;
                UnicastSubject.this.doTerminate();
                UnicastSubject.this.actual.lazySet(null);
                if (UnicastSubject.this.wip.getAndIncrement() == 0) {
                    UnicastSubject.this.actual.lazySet(null);
                    UnicastSubject.this.queue.clear();
                }
            }
        }

        public boolean isDisposed() {
            return UnicastSubject.this.disposed;
        }
    }

    public static <T> UnicastSubject<T> create() {
        return new UnicastSubject<>(bufferSize());
    }

    public static <T> UnicastSubject<T> create(int i) {
        return new UnicastSubject<>(i);
    }

    public static <T> UnicastSubject<T> create(int i, Runnable runnable) {
        return new UnicastSubject<>(i, runnable);
    }

    UnicastSubject(int i) {
        this.queue = new SpscLinkedArrayQueue<>(ObjectHelper.verifyPositive(i, "capacityHint"));
        this.onTerminate = new AtomicReference<>();
        this.actual = new AtomicReference<>();
        this.once = new AtomicBoolean();
        this.wip = new UnicastQueueDisposable();
    }

    UnicastSubject(int i, Runnable runnable) {
        this.queue = new SpscLinkedArrayQueue<>(ObjectHelper.verifyPositive(i, "capacityHint"));
        this.onTerminate = new AtomicReference<>(ObjectHelper.requireNonNull(runnable, "onTerminate"));
        this.actual = new AtomicReference<>();
        this.once = new AtomicBoolean();
        this.wip = new UnicastQueueDisposable();
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super T> observer) {
        if (this.once.get() || !this.once.compareAndSet(false, true)) {
            EmptyDisposable.error((Throwable) new IllegalStateException("Only a single observer allowed."), observer);
        } else {
            observer.onSubscribe(this.wip);
            this.actual.lazySet(observer);
            if (this.disposed) {
                this.actual.lazySet(null);
                return;
            }
            drain();
        }
    }

    /* access modifiers changed from: 0000 */
    public void doTerminate() {
        Runnable runnable = (Runnable) this.onTerminate.get();
        if (runnable != null && this.onTerminate.compareAndSet(runnable, null)) {
            runnable.run();
        }
    }

    public void onSubscribe(Disposable disposable) {
        if (this.done || this.disposed) {
            disposable.dispose();
        }
    }

    public void onNext(T t) {
        if (!this.done && !this.disposed) {
            if (t == null) {
                onError(new NullPointerException("onNext called with null. Null values are generally not allowed in 2.x operators and sources."));
                return;
            }
            this.queue.offer(t);
            drain();
        }
    }

    public void onError(Throwable th) {
        if (this.done || this.disposed) {
            RxJavaPlugins.onError(th);
            return;
        }
        if (th == null) {
            th = new NullPointerException("onError called with null. Null values are generally not allowed in 2.x operators and sources.");
        }
        this.error = th;
        this.done = true;
        doTerminate();
        drain();
    }

    public void onComplete() {
        if (!this.done && !this.disposed) {
            this.done = true;
            doTerminate();
            drain();
        }
    }

    /* access modifiers changed from: 0000 */
    public void drainNormal(Observer<? super T> observer) {
        SpscLinkedArrayQueue<T> spscLinkedArrayQueue = this.queue;
        int i = 1;
        while (!this.disposed) {
            boolean z = this.done;
            Object poll = this.queue.poll();
            boolean z2 = poll == null;
            if (z && z2) {
                this.actual.lazySet(null);
                Throwable th = this.error;
                if (th != null) {
                    observer.onError(th);
                } else {
                    observer.onComplete();
                }
                return;
            } else if (z2) {
                i = this.wip.addAndGet(-i);
                if (i == 0) {
                    return;
                }
            } else {
                observer.onNext(poll);
            }
        }
        this.actual.lazySet(null);
        spscLinkedArrayQueue.clear();
    }

    /* access modifiers changed from: 0000 */
    public void drainFused(Observer<? super T> observer) {
        SpscLinkedArrayQueue<T> spscLinkedArrayQueue = this.queue;
        int i = 1;
        while (!this.disposed) {
            boolean z = this.done;
            observer.onNext(null);
            if (z) {
                this.actual.lazySet(null);
                Throwable th = this.error;
                if (th != null) {
                    observer.onError(th);
                } else {
                    observer.onComplete();
                }
                return;
            }
            i = this.wip.addAndGet(-i);
            if (i == 0) {
                return;
            }
        }
        this.actual.lazySet(null);
        spscLinkedArrayQueue.clear();
    }

    /* access modifiers changed from: 0000 */
    public void drain() {
        if (this.wip.getAndIncrement() == 0) {
            Observer observer = (Observer) this.actual.get();
            int i = 1;
            while (observer == null) {
                i = this.wip.addAndGet(-i);
                if (i != 0) {
                    observer = (Observer) this.actual.get();
                } else {
                    return;
                }
            }
            if (this.enableOperatorFusion) {
                drainFused(observer);
            } else {
                drainNormal(observer);
            }
        }
    }

    public boolean hasObservers() {
        return this.actual.get() != null;
    }

    public Throwable getThrowable() {
        if (this.done) {
            return this.error;
        }
        return null;
    }

    public boolean hasThrowable() {
        return this.done && this.error != null;
    }

    public boolean hasComplete() {
        return this.done && this.error == null;
    }
}
