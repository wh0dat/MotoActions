package p016io.reactivex.internal.operators.observable;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import p016io.reactivex.Observable;
import p016io.reactivex.ObservableSource;
import p016io.reactivex.Observer;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.internal.functions.ObjectHelper;
import p016io.reactivex.internal.observers.QueueDrainObserver;
import p016io.reactivex.internal.queue.MpscLinkedQueue;
import p016io.reactivex.internal.util.NotificationLite;
import p016io.reactivex.observers.DisposableObserver;
import p016io.reactivex.observers.SerializedObserver;
import p016io.reactivex.plugins.RxJavaPlugins;
import p016io.reactivex.subjects.UnicastSubject;

/* renamed from: io.reactivex.internal.operators.observable.ObservableWindowBoundarySupplier */
public final class ObservableWindowBoundarySupplier<T, B> extends AbstractObservableWithUpstream<T, Observable<T>> {
    final int bufferSize;
    final Callable<? extends ObservableSource<B>> other;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableWindowBoundarySupplier$WindowBoundaryInnerObserver */
    static final class WindowBoundaryInnerObserver<T, B> extends DisposableObserver<B> {
        boolean done;
        final WindowBoundaryMainObserver<T, B> parent;

        WindowBoundaryInnerObserver(WindowBoundaryMainObserver<T, B> windowBoundaryMainObserver) {
            this.parent = windowBoundaryMainObserver;
        }

        public void onNext(B b) {
            if (!this.done) {
                this.done = true;
                dispose();
                this.parent.next();
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            this.parent.onError(th);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.parent.onComplete();
            }
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableWindowBoundarySupplier$WindowBoundaryMainObserver */
    static final class WindowBoundaryMainObserver<T, B> extends QueueDrainObserver<T, Object, Observable<T>> implements Disposable {
        static final Object NEXT = new Object();
        final AtomicReference<Disposable> boundary = new AtomicReference<>();
        final int bufferSize;
        final Callable<? extends ObservableSource<B>> other;

        /* renamed from: s */
        Disposable f498s;
        UnicastSubject<T> window;
        final AtomicLong windows = new AtomicLong();

        WindowBoundaryMainObserver(Observer<? super Observable<T>> observer, Callable<? extends ObservableSource<B>> callable, int i) {
            super(observer, new MpscLinkedQueue());
            this.other = callable;
            this.bufferSize = i;
            this.windows.lazySet(1);
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.f498s, disposable)) {
                this.f498s = disposable;
                Observer observer = this.actual;
                observer.onSubscribe(this);
                if (!this.cancelled) {
                    try {
                        ObservableSource observableSource = (ObservableSource) ObjectHelper.requireNonNull(this.other.call(), "The first window ObservableSource supplied is null");
                        UnicastSubject<T> create = UnicastSubject.create(this.bufferSize);
                        this.window = create;
                        observer.onNext(create);
                        WindowBoundaryInnerObserver windowBoundaryInnerObserver = new WindowBoundaryInnerObserver(this);
                        if (this.boundary.compareAndSet(null, windowBoundaryInnerObserver)) {
                            this.windows.getAndIncrement();
                            observableSource.subscribe(windowBoundaryInnerObserver);
                        }
                    } catch (Throwable th) {
                        Exceptions.throwIfFatal(th);
                        disposable.dispose();
                        observer.onError(th);
                    }
                }
            }
        }

        public void onNext(T t) {
            if (fastEnter()) {
                this.window.onNext(t);
                if (leave(-1) == 0) {
                    return;
                }
            } else {
                this.queue.offer(NotificationLite.next(t));
                if (!enter()) {
                    return;
                }
            }
            drainLoop();
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.error = th;
            this.done = true;
            if (enter()) {
                drainLoop();
            }
            if (this.windows.decrementAndGet() == 0) {
                DisposableHelper.dispose(this.boundary);
            }
            this.actual.onError(th);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                if (enter()) {
                    drainLoop();
                }
                if (this.windows.decrementAndGet() == 0) {
                    DisposableHelper.dispose(this.boundary);
                }
                this.actual.onComplete();
            }
        }

        public void dispose() {
            this.cancelled = true;
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        /* access modifiers changed from: 0000 */
        public void drainLoop() {
            MpscLinkedQueue mpscLinkedQueue = (MpscLinkedQueue) this.queue;
            Observer observer = this.actual;
            UnicastSubject<T> unicastSubject = this.window;
            int i = 1;
            while (true) {
                boolean z = this.done;
                Object poll = mpscLinkedQueue.poll();
                boolean z2 = poll == null;
                if (z && z2) {
                    DisposableHelper.dispose(this.boundary);
                    Throwable th = this.error;
                    if (th != null) {
                        unicastSubject.onError(th);
                    } else {
                        unicastSubject.onComplete();
                    }
                    return;
                } else if (z2) {
                    i = leave(-i);
                    if (i == 0) {
                        return;
                    }
                } else if (poll == NEXT) {
                    unicastSubject.onComplete();
                    if (this.windows.decrementAndGet() == 0) {
                        DisposableHelper.dispose(this.boundary);
                        return;
                    } else if (!this.cancelled) {
                        try {
                            ObservableSource observableSource = (ObservableSource) ObjectHelper.requireNonNull(this.other.call(), "The ObservableSource supplied is null");
                            UnicastSubject<T> create = UnicastSubject.create(this.bufferSize);
                            this.windows.getAndIncrement();
                            this.window = create;
                            observer.onNext(create);
                            WindowBoundaryInnerObserver windowBoundaryInnerObserver = new WindowBoundaryInnerObserver(this);
                            if (this.boundary.compareAndSet(this.boundary.get(), windowBoundaryInnerObserver)) {
                                observableSource.subscribe(windowBoundaryInnerObserver);
                            }
                            unicastSubject = create;
                        } catch (Throwable th2) {
                            Exceptions.throwIfFatal(th2);
                            DisposableHelper.dispose(this.boundary);
                            observer.onError(th2);
                            return;
                        }
                    }
                } else {
                    unicastSubject.onNext(NotificationLite.getValue(poll));
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void next() {
            this.queue.offer(NEXT);
            if (enter()) {
                drainLoop();
            }
        }
    }

    public ObservableWindowBoundarySupplier(ObservableSource<T> observableSource, Callable<? extends ObservableSource<B>> callable, int i) {
        super(observableSource);
        this.other = callable;
        this.bufferSize = i;
    }

    public void subscribeActual(Observer<? super Observable<T>> observer) {
        this.source.subscribe(new WindowBoundaryMainObserver(new SerializedObserver(observer), this.other, this.bufferSize));
    }
}
