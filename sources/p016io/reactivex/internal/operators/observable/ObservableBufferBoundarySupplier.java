package p016io.reactivex.internal.operators.observable;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;
import p016io.reactivex.ObservableSource;
import p016io.reactivex.Observer;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.internal.disposables.EmptyDisposable;
import p016io.reactivex.internal.functions.ObjectHelper;
import p016io.reactivex.internal.observers.QueueDrainObserver;
import p016io.reactivex.internal.queue.MpscLinkedQueue;
import p016io.reactivex.observers.DisposableObserver;
import p016io.reactivex.observers.SerializedObserver;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.observable.ObservableBufferBoundarySupplier */
public final class ObservableBufferBoundarySupplier<T, U extends Collection<? super T>, B> extends AbstractObservableWithUpstream<T, U> {
    final Callable<? extends ObservableSource<B>> boundarySupplier;
    final Callable<U> bufferSupplier;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableBufferBoundarySupplier$BufferBoundaryObserver */
    static final class BufferBoundaryObserver<T, U extends Collection<? super T>, B> extends DisposableObserver<B> {
        boolean once;
        final BufferBoundarySupplierObserver<T, U, B> parent;

        BufferBoundaryObserver(BufferBoundarySupplierObserver<T, U, B> bufferBoundarySupplierObserver) {
            this.parent = bufferBoundarySupplierObserver;
        }

        public void onNext(B b) {
            if (!this.once) {
                this.once = true;
                dispose();
                this.parent.next();
            }
        }

        public void onError(Throwable th) {
            if (this.once) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.once = true;
            this.parent.onError(th);
        }

        public void onComplete() {
            if (!this.once) {
                this.once = true;
                this.parent.next();
            }
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableBufferBoundarySupplier$BufferBoundarySupplierObserver */
    static final class BufferBoundarySupplierObserver<T, U extends Collection<? super T>, B> extends QueueDrainObserver<T, U, U> implements Observer<T>, Disposable {
        final Callable<? extends ObservableSource<B>> boundarySupplier;
        U buffer;
        final Callable<U> bufferSupplier;
        final AtomicReference<Disposable> other = new AtomicReference<>();

        /* renamed from: s */
        Disposable f400s;

        BufferBoundarySupplierObserver(Observer<? super U> observer, Callable<U> callable, Callable<? extends ObservableSource<B>> callable2) {
            super(observer, new MpscLinkedQueue());
            this.bufferSupplier = callable;
            this.boundarySupplier = callable2;
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.f400s, disposable)) {
                this.f400s = disposable;
                Observer observer = this.actual;
                try {
                    this.buffer = (Collection) ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The buffer supplied is null");
                    try {
                        ObservableSource observableSource = (ObservableSource) ObjectHelper.requireNonNull(this.boundarySupplier.call(), "The boundary publisher supplied is null");
                        BufferBoundaryObserver bufferBoundaryObserver = new BufferBoundaryObserver(this);
                        this.other.set(bufferBoundaryObserver);
                        observer.onSubscribe(this);
                        if (!this.cancelled) {
                            observableSource.subscribe(bufferBoundaryObserver);
                        }
                    } catch (Throwable th) {
                        Exceptions.throwIfFatal(th);
                        this.cancelled = true;
                        disposable.dispose();
                        EmptyDisposable.error(th, observer);
                    }
                } catch (Throwable th2) {
                    Exceptions.throwIfFatal(th2);
                    this.cancelled = true;
                    disposable.dispose();
                    EmptyDisposable.error(th2, observer);
                }
            }
        }

        public void onNext(T t) {
            synchronized (this) {
                U u = this.buffer;
                if (u != null) {
                    u.add(t);
                }
            }
        }

        public void onError(Throwable th) {
            dispose();
            this.actual.onError(th);
        }

        /* JADX WARNING: Code restructure failed: missing block: B:10:0x0019, code lost:
            p016io.reactivex.internal.util.QueueDrainHelper.drainLoop(r3.queue, r3.actual, false, r3, r3);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:11:0x0021, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:8:0x000b, code lost:
            r3.queue.offer(r0);
            r3.done = true;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:9:0x0017, code lost:
            if (enter() == false) goto L_0x0021;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onComplete() {
            /*
                r3 = this;
                monitor-enter(r3)
                U r0 = r3.buffer     // Catch:{ all -> 0x0022 }
                if (r0 != 0) goto L_0x0007
                monitor-exit(r3)     // Catch:{ all -> 0x0022 }
                return
            L_0x0007:
                r1 = 0
                r3.buffer = r1     // Catch:{ all -> 0x0022 }
                monitor-exit(r3)     // Catch:{ all -> 0x0022 }
                io.reactivex.internal.fuseable.SimpleQueue r1 = r3.queue
                r1.offer(r0)
                r0 = 1
                r3.done = r0
                boolean r0 = r3.enter()
                if (r0 == 0) goto L_0x0021
                io.reactivex.internal.fuseable.SimpleQueue r0 = r3.queue
                io.reactivex.Observer r1 = r3.actual
                r2 = 0
                p016io.reactivex.internal.util.QueueDrainHelper.drainLoop(r0, r1, r2, r3, r3)
            L_0x0021:
                return
            L_0x0022:
                r0 = move-exception
                monitor-exit(r3)     // Catch:{ all -> 0x0022 }
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: p016io.reactivex.internal.operators.observable.ObservableBufferBoundarySupplier.BufferBoundarySupplierObserver.onComplete():void");
        }

        public void dispose() {
            if (!this.cancelled) {
                this.cancelled = true;
                this.f400s.dispose();
                disposeOther();
                if (enter()) {
                    this.queue.clear();
                }
            }
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        /* access modifiers changed from: 0000 */
        public void disposeOther() {
            DisposableHelper.dispose(this.other);
        }

        /* access modifiers changed from: 0000 */
        public void next() {
            try {
                U u = (Collection) ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The buffer supplied is null");
                try {
                    ObservableSource observableSource = (ObservableSource) ObjectHelper.requireNonNull(this.boundarySupplier.call(), "The boundary publisher supplied is null");
                    BufferBoundaryObserver bufferBoundaryObserver = new BufferBoundaryObserver(this);
                    if (this.other.compareAndSet((Disposable) this.other.get(), bufferBoundaryObserver)) {
                        synchronized (this) {
                            U u2 = this.buffer;
                            if (u2 != null) {
                                this.buffer = u;
                                observableSource.subscribe(bufferBoundaryObserver);
                                fastPathEmit(u2, false, this);
                            }
                        }
                    }
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    this.cancelled = true;
                    this.f400s.dispose();
                    this.actual.onError(th);
                }
            } catch (Throwable th2) {
                Exceptions.throwIfFatal(th2);
                dispose();
                this.actual.onError(th2);
            }
        }

        public void accept(Observer<? super U> observer, U u) {
            this.actual.onNext(u);
        }
    }

    public ObservableBufferBoundarySupplier(ObservableSource<T> observableSource, Callable<? extends ObservableSource<B>> callable, Callable<U> callable2) {
        super(observableSource);
        this.boundarySupplier = callable;
        this.bufferSupplier = callable2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super U> observer) {
        this.source.subscribe(new BufferBoundarySupplierObserver(new SerializedObserver(observer), this.bufferSupplier, this.boundarySupplier));
    }
}
