package p016io.reactivex.internal.operators.observable;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import p016io.reactivex.ObservableSource;
import p016io.reactivex.Observer;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.Function;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.internal.disposables.SequentialDisposable;
import p016io.reactivex.internal.functions.ObjectHelper;
import p016io.reactivex.internal.fuseable.QueueDisposable;
import p016io.reactivex.internal.fuseable.SimpleQueue;
import p016io.reactivex.internal.queue.SpscLinkedArrayQueue;
import p016io.reactivex.internal.util.AtomicThrowable;
import p016io.reactivex.internal.util.ErrorMode;
import p016io.reactivex.observers.SerializedObserver;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.observable.ObservableConcatMap */
public final class ObservableConcatMap<T, U> extends AbstractObservableWithUpstream<T, U> {
    final int bufferSize;
    final ErrorMode delayErrors;
    final Function<? super T, ? extends ObservableSource<? extends U>> mapper;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableConcatMap$ConcatMapDelayErrorObserver */
    static final class ConcatMapDelayErrorObserver<T, R> extends AtomicInteger implements Observer<T>, Disposable {
        private static final long serialVersionUID = -6951100001833242599L;
        volatile boolean active;
        final Observer<? super R> actual;
        final SequentialDisposable arbiter;
        final int bufferSize;
        volatile boolean cancelled;

        /* renamed from: d */
        Disposable f412d;
        volatile boolean done;
        final AtomicThrowable error = new AtomicThrowable();
        final Function<? super T, ? extends ObservableSource<? extends R>> mapper;
        final DelayErrorInnerObserver<R> observer;
        SimpleQueue<T> queue;
        int sourceMode;
        final boolean tillTheEnd;

        /* renamed from: io.reactivex.internal.operators.observable.ObservableConcatMap$ConcatMapDelayErrorObserver$DelayErrorInnerObserver */
        static final class DelayErrorInnerObserver<R> implements Observer<R> {
            final Observer<? super R> actual;
            final ConcatMapDelayErrorObserver<?, R> parent;

            DelayErrorInnerObserver(Observer<? super R> observer, ConcatMapDelayErrorObserver<?, R> concatMapDelayErrorObserver) {
                this.actual = observer;
                this.parent = concatMapDelayErrorObserver;
            }

            public void onSubscribe(Disposable disposable) {
                this.parent.arbiter.replace(disposable);
            }

            public void onNext(R r) {
                this.actual.onNext(r);
            }

            public void onError(Throwable th) {
                ConcatMapDelayErrorObserver<?, R> concatMapDelayErrorObserver = this.parent;
                if (concatMapDelayErrorObserver.error.addThrowable(th)) {
                    if (!concatMapDelayErrorObserver.tillTheEnd) {
                        concatMapDelayErrorObserver.f412d.dispose();
                    }
                    concatMapDelayErrorObserver.active = false;
                    concatMapDelayErrorObserver.drain();
                    return;
                }
                RxJavaPlugins.onError(th);
            }

            public void onComplete() {
                ConcatMapDelayErrorObserver<?, R> concatMapDelayErrorObserver = this.parent;
                concatMapDelayErrorObserver.active = false;
                concatMapDelayErrorObserver.drain();
            }
        }

        ConcatMapDelayErrorObserver(Observer<? super R> observer2, Function<? super T, ? extends ObservableSource<? extends R>> function, int i, boolean z) {
            this.actual = observer2;
            this.mapper = function;
            this.bufferSize = i;
            this.tillTheEnd = z;
            this.observer = new DelayErrorInnerObserver<>(observer2, this);
            this.arbiter = new SequentialDisposable();
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.f412d, disposable)) {
                this.f412d = disposable;
                if (disposable instanceof QueueDisposable) {
                    QueueDisposable queueDisposable = (QueueDisposable) disposable;
                    int requestFusion = queueDisposable.requestFusion(3);
                    if (requestFusion == 1) {
                        this.sourceMode = requestFusion;
                        this.queue = queueDisposable;
                        this.done = true;
                        this.actual.onSubscribe(this);
                        drain();
                        return;
                    } else if (requestFusion == 2) {
                        this.sourceMode = requestFusion;
                        this.queue = queueDisposable;
                        this.actual.onSubscribe(this);
                        return;
                    }
                }
                this.queue = new SpscLinkedArrayQueue(this.bufferSize);
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            if (this.sourceMode == 0) {
                this.queue.offer(t);
            }
            drain();
        }

        public void onError(Throwable th) {
            if (this.error.addThrowable(th)) {
                this.done = true;
                drain();
                return;
            }
            RxJavaPlugins.onError(th);
        }

        public void onComplete() {
            this.done = true;
            drain();
        }

        public boolean isDisposed() {
            return this.f412d.isDisposed();
        }

        public void dispose() {
            this.cancelled = true;
            this.f412d.dispose();
            this.arbiter.dispose();
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            if (getAndIncrement() == 0) {
                Observer<? super R> observer2 = this.actual;
                SimpleQueue<T> simpleQueue = this.queue;
                AtomicThrowable atomicThrowable = this.error;
                while (true) {
                    if (!this.active) {
                        if (this.cancelled) {
                            simpleQueue.clear();
                            return;
                        } else if (this.tillTheEnd || ((Throwable) atomicThrowable.get()) == null) {
                            boolean z = this.done;
                            try {
                                Object poll = simpleQueue.poll();
                                boolean z2 = poll == null;
                                if (z && z2) {
                                    Throwable terminate = atomicThrowable.terminate();
                                    if (terminate != null) {
                                        observer2.onError(terminate);
                                    } else {
                                        observer2.onComplete();
                                    }
                                    return;
                                } else if (!z2) {
                                    try {
                                        ObservableSource observableSource = (ObservableSource) ObjectHelper.requireNonNull(this.mapper.apply(poll), "The mapper returned a null ObservableSource");
                                        if (observableSource instanceof Callable) {
                                            try {
                                                Object call = ((Callable) observableSource).call();
                                                if (call != null && !this.cancelled) {
                                                    observer2.onNext(call);
                                                }
                                            } catch (Throwable th) {
                                                Exceptions.throwIfFatal(th);
                                                atomicThrowable.addThrowable(th);
                                            }
                                        } else {
                                            this.active = true;
                                            observableSource.subscribe(this.observer);
                                        }
                                    } catch (Throwable th2) {
                                        Exceptions.throwIfFatal(th2);
                                        this.f412d.dispose();
                                        simpleQueue.clear();
                                        atomicThrowable.addThrowable(th2);
                                        observer2.onError(atomicThrowable.terminate());
                                        return;
                                    }
                                }
                            } catch (Throwable th3) {
                                Exceptions.throwIfFatal(th3);
                                this.f412d.dispose();
                                atomicThrowable.addThrowable(th3);
                                observer2.onError(atomicThrowable.terminate());
                                return;
                            }
                        } else {
                            simpleQueue.clear();
                            observer2.onError(atomicThrowable.terminate());
                            return;
                        }
                    }
                    if (decrementAndGet() == 0) {
                        return;
                    }
                }
            }
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableConcatMap$SourceObserver */
    static final class SourceObserver<T, U> extends AtomicInteger implements Observer<T>, Disposable {
        private static final long serialVersionUID = 8828587559905699186L;
        volatile boolean active;
        final Observer<? super U> actual;
        final int bufferSize;
        volatile boolean disposed;
        volatile boolean done;
        int fusionMode;
        final Observer<U> inner;
        final Function<? super T, ? extends ObservableSource<? extends U>> mapper;
        SimpleQueue<T> queue;

        /* renamed from: s */
        Disposable f413s;

        /* renamed from: sa */
        final SequentialDisposable f414sa = new SequentialDisposable();

        /* renamed from: io.reactivex.internal.operators.observable.ObservableConcatMap$SourceObserver$InnerObserver */
        static final class InnerObserver<U> implements Observer<U> {
            final Observer<? super U> actual;
            final SourceObserver<?, ?> parent;

            InnerObserver(Observer<? super U> observer, SourceObserver<?, ?> sourceObserver) {
                this.actual = observer;
                this.parent = sourceObserver;
            }

            public void onSubscribe(Disposable disposable) {
                this.parent.innerSubscribe(disposable);
            }

            public void onNext(U u) {
                this.actual.onNext(u);
            }

            public void onError(Throwable th) {
                this.parent.dispose();
                this.actual.onError(th);
            }

            public void onComplete() {
                this.parent.innerComplete();
            }
        }

        SourceObserver(Observer<? super U> observer, Function<? super T, ? extends ObservableSource<? extends U>> function, int i) {
            this.actual = observer;
            this.mapper = function;
            this.bufferSize = i;
            this.inner = new InnerObserver(observer, this);
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.f413s, disposable)) {
                this.f413s = disposable;
                if (disposable instanceof QueueDisposable) {
                    QueueDisposable queueDisposable = (QueueDisposable) disposable;
                    int requestFusion = queueDisposable.requestFusion(3);
                    if (requestFusion == 1) {
                        this.fusionMode = requestFusion;
                        this.queue = queueDisposable;
                        this.done = true;
                        this.actual.onSubscribe(this);
                        drain();
                        return;
                    } else if (requestFusion == 2) {
                        this.fusionMode = requestFusion;
                        this.queue = queueDisposable;
                        this.actual.onSubscribe(this);
                        return;
                    }
                }
                this.queue = new SpscLinkedArrayQueue(this.bufferSize);
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                if (this.fusionMode == 0) {
                    this.queue.offer(t);
                }
                drain();
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            dispose();
            this.actual.onError(th);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                drain();
            }
        }

        /* access modifiers changed from: 0000 */
        public void innerComplete() {
            this.active = false;
            drain();
        }

        public boolean isDisposed() {
            return this.disposed;
        }

        public void dispose() {
            this.disposed = true;
            this.f414sa.dispose();
            this.f413s.dispose();
            if (getAndIncrement() == 0) {
                this.queue.clear();
            }
        }

        /* access modifiers changed from: 0000 */
        public void innerSubscribe(Disposable disposable) {
            this.f414sa.update(disposable);
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            if (getAndIncrement() == 0) {
                while (!this.disposed) {
                    if (!this.active) {
                        boolean z = this.done;
                        try {
                            Object poll = this.queue.poll();
                            boolean z2 = poll == null;
                            if (z && z2) {
                                this.actual.onComplete();
                                return;
                            } else if (!z2) {
                                try {
                                    ObservableSource observableSource = (ObservableSource) ObjectHelper.requireNonNull(this.mapper.apply(poll), "The mapper returned a null ObservableSource");
                                    this.active = true;
                                    observableSource.subscribe(this.inner);
                                } catch (Throwable th) {
                                    Exceptions.throwIfFatal(th);
                                    dispose();
                                    this.queue.clear();
                                    this.actual.onError(th);
                                    return;
                                }
                            }
                        } catch (Throwable th2) {
                            Exceptions.throwIfFatal(th2);
                            dispose();
                            this.queue.clear();
                            this.actual.onError(th2);
                            return;
                        }
                    }
                    if (decrementAndGet() == 0) {
                        return;
                    }
                }
                this.queue.clear();
            }
        }
    }

    public ObservableConcatMap(ObservableSource<T> observableSource, Function<? super T, ? extends ObservableSource<? extends U>> function, int i, ErrorMode errorMode) {
        super(observableSource);
        this.mapper = function;
        this.delayErrors = errorMode;
        this.bufferSize = Math.max(8, i);
    }

    public void subscribeActual(Observer<? super U> observer) {
        if (!ObservableScalarXMap.tryScalarXMapSubscribe(this.source, observer, this.mapper)) {
            if (this.delayErrors == ErrorMode.IMMEDIATE) {
                this.source.subscribe(new SourceObserver(new SerializedObserver(observer), this.mapper, this.bufferSize));
            } else {
                this.source.subscribe(new ConcatMapDelayErrorObserver(observer, this.mapper, this.bufferSize, this.delayErrors == ErrorMode.END));
            }
        }
    }
}
