package p016io.reactivex.internal.operators.observable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import p016io.reactivex.ObservableSource;
import p016io.reactivex.Observer;
import p016io.reactivex.disposables.CompositeDisposable;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.Function;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.internal.functions.ObjectHelper;
import p016io.reactivex.internal.fuseable.SimpleQueue;
import p016io.reactivex.internal.observers.QueueDrainObserver;
import p016io.reactivex.internal.queue.MpscLinkedQueue;
import p016io.reactivex.internal.util.QueueDrainHelper;
import p016io.reactivex.observers.DisposableObserver;
import p016io.reactivex.observers.SerializedObserver;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.observable.ObservableBufferBoundary */
public final class ObservableBufferBoundary<T, U extends Collection<? super T>, Open, Close> extends AbstractObservableWithUpstream<T, U> {
    final Function<? super Open, ? extends ObservableSource<? extends Close>> bufferClose;
    final ObservableSource<? extends Open> bufferOpen;
    final Callable<U> bufferSupplier;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableBufferBoundary$BufferBoundaryObserver */
    static final class BufferBoundaryObserver<T, U extends Collection<? super T>, Open, Close> extends QueueDrainObserver<T, U, U> implements Disposable {
        final Function<? super Open, ? extends ObservableSource<? extends Close>> bufferClose;
        final ObservableSource<? extends Open> bufferOpen;
        final Callable<U> bufferSupplier;
        final List<U> buffers;
        final CompositeDisposable resources;

        /* renamed from: s */
        Disposable f399s;
        final AtomicInteger windows = new AtomicInteger();

        BufferBoundaryObserver(Observer<? super U> observer, ObservableSource<? extends Open> observableSource, Function<? super Open, ? extends ObservableSource<? extends Close>> function, Callable<U> callable) {
            super(observer, new MpscLinkedQueue());
            this.bufferOpen = observableSource;
            this.bufferClose = function;
            this.bufferSupplier = callable;
            this.buffers = new LinkedList();
            this.resources = new CompositeDisposable();
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.f399s, disposable)) {
                this.f399s = disposable;
                BufferOpenObserver bufferOpenObserver = new BufferOpenObserver(this);
                this.resources.add(bufferOpenObserver);
                this.actual.onSubscribe(this);
                this.windows.lazySet(1);
                this.bufferOpen.subscribe(bufferOpenObserver);
            }
        }

        public void onNext(T t) {
            synchronized (this) {
                for (U add : this.buffers) {
                    add.add(t);
                }
            }
        }

        public void onError(Throwable th) {
            dispose();
            this.cancelled = true;
            synchronized (this) {
                this.buffers.clear();
            }
            this.actual.onError(th);
        }

        public void onComplete() {
            if (this.windows.decrementAndGet() == 0) {
                complete();
            }
        }

        /* access modifiers changed from: 0000 */
        public void complete() {
            ArrayList<Collection> arrayList;
            synchronized (this) {
                arrayList = new ArrayList<>(this.buffers);
                this.buffers.clear();
            }
            SimpleQueue simpleQueue = this.queue;
            for (Collection offer : arrayList) {
                simpleQueue.offer(offer);
            }
            this.done = true;
            if (enter()) {
                QueueDrainHelper.drainLoop(simpleQueue, this.actual, false, this, this);
            }
        }

        public void dispose() {
            if (!this.cancelled) {
                this.cancelled = true;
                this.resources.dispose();
            }
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        public void accept(Observer<? super U> observer, U u) {
            observer.onNext(u);
        }

        /* access modifiers changed from: 0000 */
        public void open(Open open) {
            if (!this.cancelled) {
                try {
                    Collection collection = (Collection) ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The buffer supplied is null");
                    try {
                        ObservableSource observableSource = (ObservableSource) ObjectHelper.requireNonNull(this.bufferClose.apply(open), "The buffer closing Observable is null");
                        if (!this.cancelled) {
                            synchronized (this) {
                                if (!this.cancelled) {
                                    this.buffers.add(collection);
                                    BufferCloseObserver bufferCloseObserver = new BufferCloseObserver(collection, this);
                                    this.resources.add(bufferCloseObserver);
                                    this.windows.getAndIncrement();
                                    observableSource.subscribe(bufferCloseObserver);
                                }
                            }
                        }
                    } catch (Throwable th) {
                        Exceptions.throwIfFatal(th);
                        onError(th);
                    }
                } catch (Throwable th2) {
                    Exceptions.throwIfFatal(th2);
                    onError(th2);
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void openFinished(Disposable disposable) {
            if (this.resources.remove(disposable) && this.windows.decrementAndGet() == 0) {
                complete();
            }
        }

        /* access modifiers changed from: 0000 */
        public void close(U u, Disposable disposable) {
            boolean remove;
            synchronized (this) {
                remove = this.buffers.remove(u);
            }
            if (remove) {
                fastPathOrderedEmit(u, false, this);
            }
            if (this.resources.remove(disposable) && this.windows.decrementAndGet() == 0) {
                complete();
            }
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableBufferBoundary$BufferCloseObserver */
    static final class BufferCloseObserver<T, U extends Collection<? super T>, Open, Close> extends DisposableObserver<Close> {
        boolean done;
        final BufferBoundaryObserver<T, U, Open, Close> parent;
        final U value;

        BufferCloseObserver(U u, BufferBoundaryObserver<T, U, Open, Close> bufferBoundaryObserver) {
            this.parent = bufferBoundaryObserver;
            this.value = u;
        }

        public void onNext(Close close) {
            onComplete();
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
            } else {
                this.parent.onError(th);
            }
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.parent.close(this.value, this);
            }
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableBufferBoundary$BufferOpenObserver */
    static final class BufferOpenObserver<T, U extends Collection<? super T>, Open, Close> extends DisposableObserver<Open> {
        boolean done;
        final BufferBoundaryObserver<T, U, Open, Close> parent;

        BufferOpenObserver(BufferBoundaryObserver<T, U, Open, Close> bufferBoundaryObserver) {
            this.parent = bufferBoundaryObserver;
        }

        public void onNext(Open open) {
            if (!this.done) {
                this.parent.open(open);
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
                this.parent.openFinished(this);
            }
        }
    }

    public ObservableBufferBoundary(ObservableSource<T> observableSource, ObservableSource<? extends Open> observableSource2, Function<? super Open, ? extends ObservableSource<? extends Close>> function, Callable<U> callable) {
        super(observableSource);
        this.bufferOpen = observableSource2;
        this.bufferClose = function;
        this.bufferSupplier = callable;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super U> observer) {
        this.source.subscribe(new BufferBoundaryObserver(new SerializedObserver(observer), this.bufferOpen, this.bufferClose, this.bufferSupplier));
    }
}
