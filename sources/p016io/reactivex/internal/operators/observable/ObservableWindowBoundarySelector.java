package p016io.reactivex.internal.operators.observable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import p016io.reactivex.Observable;
import p016io.reactivex.ObservableSource;
import p016io.reactivex.Observer;
import p016io.reactivex.disposables.CompositeDisposable;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.Function;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.internal.functions.ObjectHelper;
import p016io.reactivex.internal.observers.QueueDrainObserver;
import p016io.reactivex.internal.queue.MpscLinkedQueue;
import p016io.reactivex.internal.util.NotificationLite;
import p016io.reactivex.observers.DisposableObserver;
import p016io.reactivex.observers.SerializedObserver;
import p016io.reactivex.plugins.RxJavaPlugins;
import p016io.reactivex.subjects.UnicastSubject;

/* renamed from: io.reactivex.internal.operators.observable.ObservableWindowBoundarySelector */
public final class ObservableWindowBoundarySelector<T, B, V> extends AbstractObservableWithUpstream<T, Observable<T>> {
    final int bufferSize;
    final Function<? super B, ? extends ObservableSource<V>> close;
    final ObservableSource<B> open;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableWindowBoundarySelector$OperatorWindowBoundaryCloseObserver */
    static final class OperatorWindowBoundaryCloseObserver<T, V> extends DisposableObserver<V> {
        boolean done;
        final WindowBoundaryMainObserver<T, ?, V> parent;

        /* renamed from: w */
        final UnicastSubject<T> f494w;

        OperatorWindowBoundaryCloseObserver(WindowBoundaryMainObserver<T, ?, V> windowBoundaryMainObserver, UnicastSubject<T> unicastSubject) {
            this.parent = windowBoundaryMainObserver;
            this.f494w = unicastSubject;
        }

        public void onNext(V v) {
            if (!this.done) {
                this.done = true;
                dispose();
                this.parent.close(this);
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            this.parent.error(th);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.parent.close(this);
            }
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableWindowBoundarySelector$OperatorWindowBoundaryOpenObserver */
    static final class OperatorWindowBoundaryOpenObserver<T, B> extends DisposableObserver<B> {
        final WindowBoundaryMainObserver<T, B, ?> parent;

        OperatorWindowBoundaryOpenObserver(WindowBoundaryMainObserver<T, B, ?> windowBoundaryMainObserver) {
            this.parent = windowBoundaryMainObserver;
        }

        public void onNext(B b) {
            this.parent.open(b);
        }

        public void onError(Throwable th) {
            this.parent.error(th);
        }

        public void onComplete() {
            this.parent.onComplete();
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableWindowBoundarySelector$WindowBoundaryMainObserver */
    static final class WindowBoundaryMainObserver<T, B, V> extends QueueDrainObserver<T, Object, Observable<T>> implements Disposable {
        final AtomicReference<Disposable> boundary = new AtomicReference<>();
        final int bufferSize;
        final Function<? super B, ? extends ObservableSource<V>> close;
        final ObservableSource<B> open;
        final CompositeDisposable resources;

        /* renamed from: s */
        Disposable f495s;
        final AtomicLong windows = new AtomicLong();

        /* renamed from: ws */
        final List<UnicastSubject<T>> f496ws;

        public void accept(Observer<? super Observable<T>> observer, Object obj) {
        }

        WindowBoundaryMainObserver(Observer<? super Observable<T>> observer, ObservableSource<B> observableSource, Function<? super B, ? extends ObservableSource<V>> function, int i) {
            super(observer, new MpscLinkedQueue());
            this.open = observableSource;
            this.close = function;
            this.bufferSize = i;
            this.resources = new CompositeDisposable();
            this.f496ws = new ArrayList();
            this.windows.lazySet(1);
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.f495s, disposable)) {
                this.f495s = disposable;
                this.actual.onSubscribe(this);
                if (!this.cancelled) {
                    OperatorWindowBoundaryOpenObserver operatorWindowBoundaryOpenObserver = new OperatorWindowBoundaryOpenObserver(this);
                    if (this.boundary.compareAndSet(null, operatorWindowBoundaryOpenObserver)) {
                        this.windows.getAndIncrement();
                        this.open.subscribe(operatorWindowBoundaryOpenObserver);
                    }
                }
            }
        }

        public void onNext(T t) {
            if (fastEnter()) {
                for (UnicastSubject onNext : this.f496ws) {
                    onNext.onNext(t);
                }
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
                this.resources.dispose();
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
                    this.resources.dispose();
                }
                this.actual.onComplete();
            }
        }

        /* access modifiers changed from: 0000 */
        public void error(Throwable th) {
            this.f495s.dispose();
            this.resources.dispose();
            onError(th);
        }

        public void dispose() {
            this.cancelled = true;
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        /* access modifiers changed from: 0000 */
        public void disposeBoundary() {
            this.resources.dispose();
            DisposableHelper.dispose(this.boundary);
        }

        /* access modifiers changed from: 0000 */
        public void drainLoop() {
            MpscLinkedQueue mpscLinkedQueue = (MpscLinkedQueue) this.queue;
            Observer observer = this.actual;
            List<UnicastSubject<T>> list = this.f496ws;
            int i = 1;
            while (true) {
                boolean z = this.done;
                Object poll = mpscLinkedQueue.poll();
                boolean z2 = poll == null;
                if (z && z2) {
                    disposeBoundary();
                    Throwable th = this.error;
                    if (th != null) {
                        for (UnicastSubject onError : list) {
                            onError.onError(th);
                        }
                    } else {
                        for (UnicastSubject onComplete : list) {
                            onComplete.onComplete();
                        }
                    }
                    list.clear();
                    return;
                } else if (z2) {
                    i = leave(-i);
                    if (i == 0) {
                        return;
                    }
                } else if (poll instanceof WindowOperation) {
                    WindowOperation windowOperation = (WindowOperation) poll;
                    if (windowOperation.f497w != null) {
                        if (list.remove(windowOperation.f497w)) {
                            windowOperation.f497w.onComplete();
                            if (this.windows.decrementAndGet() == 0) {
                                disposeBoundary();
                                return;
                            }
                        } else {
                            continue;
                        }
                    } else if (!this.cancelled) {
                        UnicastSubject create = UnicastSubject.create(this.bufferSize);
                        list.add(create);
                        observer.onNext(create);
                        try {
                            ObservableSource observableSource = (ObservableSource) ObjectHelper.requireNonNull(this.close.apply(windowOperation.open), "The ObservableSource supplied is null");
                            OperatorWindowBoundaryCloseObserver operatorWindowBoundaryCloseObserver = new OperatorWindowBoundaryCloseObserver(this, create);
                            if (this.resources.add(operatorWindowBoundaryCloseObserver)) {
                                this.windows.getAndIncrement();
                                observableSource.subscribe(operatorWindowBoundaryCloseObserver);
                            }
                        } catch (Throwable th2) {
                            Exceptions.throwIfFatal(th2);
                            this.cancelled = true;
                            observer.onError(th2);
                        }
                    }
                } else {
                    for (UnicastSubject onNext : list) {
                        onNext.onNext(NotificationLite.getValue(poll));
                    }
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void open(B b) {
            this.queue.offer(new WindowOperation(null, b));
            if (enter()) {
                drainLoop();
            }
        }

        /* access modifiers changed from: 0000 */
        public void close(OperatorWindowBoundaryCloseObserver<T, V> operatorWindowBoundaryCloseObserver) {
            this.resources.delete(operatorWindowBoundaryCloseObserver);
            this.queue.offer(new WindowOperation(operatorWindowBoundaryCloseObserver.f494w, null));
            if (enter()) {
                drainLoop();
            }
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableWindowBoundarySelector$WindowOperation */
    static final class WindowOperation<T, B> {
        final B open;

        /* renamed from: w */
        final UnicastSubject<T> f497w;

        WindowOperation(UnicastSubject<T> unicastSubject, B b) {
            this.f497w = unicastSubject;
            this.open = b;
        }
    }

    public ObservableWindowBoundarySelector(ObservableSource<T> observableSource, ObservableSource<B> observableSource2, Function<? super B, ? extends ObservableSource<V>> function, int i) {
        super(observableSource);
        this.open = observableSource2;
        this.close = function;
        this.bufferSize = i;
    }

    public void subscribeActual(Observer<? super Observable<T>> observer) {
        this.source.subscribe(new WindowBoundaryMainObserver(new SerializedObserver(observer), this.open, this.close, this.bufferSize));
    }
}
