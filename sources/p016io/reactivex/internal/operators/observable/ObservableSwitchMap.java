package p016io.reactivex.internal.operators.observable;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import p016io.reactivex.ObservableSource;
import p016io.reactivex.Observer;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.Function;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.internal.functions.ObjectHelper;
import p016io.reactivex.internal.queue.SpscLinkedArrayQueue;
import p016io.reactivex.internal.util.AtomicThrowable;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.observable.ObservableSwitchMap */
public final class ObservableSwitchMap<T, R> extends AbstractObservableWithUpstream<T, R> {
    final int bufferSize;
    final boolean delayErrors;
    final Function<? super T, ? extends ObservableSource<? extends R>> mapper;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableSwitchMap$SwitchMapInnerObserver */
    static final class SwitchMapInnerObserver<T, R> extends AtomicReference<Disposable> implements Observer<R> {
        private static final long serialVersionUID = 3837284832786408377L;
        volatile boolean done;
        final long index;
        final SwitchMapObserver<T, R> parent;
        final SpscLinkedArrayQueue<R> queue;

        SwitchMapInnerObserver(SwitchMapObserver<T, R> switchMapObserver, long j, int i) {
            this.parent = switchMapObserver;
            this.index = j;
            this.queue = new SpscLinkedArrayQueue<>(i);
        }

        public void onSubscribe(Disposable disposable) {
            DisposableHelper.setOnce(this, disposable);
        }

        public void onNext(R r) {
            if (this.index == this.parent.unique) {
                this.queue.offer(r);
                this.parent.drain();
            }
        }

        public void onError(Throwable th) {
            this.parent.innerError(this, th);
        }

        public void onComplete() {
            if (this.index == this.parent.unique) {
                this.done = true;
                this.parent.drain();
            }
        }

        public void cancel() {
            DisposableHelper.dispose(this);
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableSwitchMap$SwitchMapObserver */
    static final class SwitchMapObserver<T, R> extends AtomicInteger implements Observer<T>, Disposable {
        static final SwitchMapInnerObserver<Object, Object> CANCELLED = new SwitchMapInnerObserver<>(null, -1, 1);
        private static final long serialVersionUID = -3491074160481096299L;
        final AtomicReference<SwitchMapInnerObserver<T, R>> active = new AtomicReference<>();
        final Observer<? super R> actual;
        final int bufferSize;
        volatile boolean cancelled;
        final boolean delayErrors;
        volatile boolean done;
        final AtomicThrowable errors;
        final Function<? super T, ? extends ObservableSource<? extends R>> mapper;

        /* renamed from: s */
        Disposable f474s;
        volatile long unique;

        static {
            CANCELLED.cancel();
        }

        SwitchMapObserver(Observer<? super R> observer, Function<? super T, ? extends ObservableSource<? extends R>> function, int i, boolean z) {
            this.actual = observer;
            this.mapper = function;
            this.bufferSize = i;
            this.delayErrors = z;
            this.errors = new AtomicThrowable();
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.f474s, disposable)) {
                this.f474s = disposable;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            long j = this.unique + 1;
            this.unique = j;
            SwitchMapInnerObserver switchMapInnerObserver = (SwitchMapInnerObserver) this.active.get();
            if (switchMapInnerObserver != null) {
                switchMapInnerObserver.cancel();
            }
            try {
                ObservableSource observableSource = (ObservableSource) ObjectHelper.requireNonNull(this.mapper.apply(t), "The ObservableSource returned is null");
                SwitchMapInnerObserver switchMapInnerObserver2 = new SwitchMapInnerObserver(this, j, this.bufferSize);
                while (true) {
                    SwitchMapInnerObserver<Object, Object> switchMapInnerObserver3 = (SwitchMapInnerObserver) this.active.get();
                    if (switchMapInnerObserver3 != CANCELLED) {
                        if (this.active.compareAndSet(switchMapInnerObserver3, switchMapInnerObserver2)) {
                            observableSource.subscribe(switchMapInnerObserver2);
                            break;
                        }
                    } else {
                        break;
                    }
                }
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                this.f474s.dispose();
                onError(th);
            }
        }

        public void onError(Throwable th) {
            if (this.done || !this.errors.addThrowable(th)) {
                if (!this.delayErrors) {
                    disposeInner();
                }
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            drain();
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                drain();
            }
        }

        public void dispose() {
            if (!this.cancelled) {
                this.cancelled = true;
                this.f474s.dispose();
                disposeInner();
            }
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        /* access modifiers changed from: 0000 */
        public void disposeInner() {
            if (((SwitchMapInnerObserver) this.active.get()) != CANCELLED) {
                SwitchMapInnerObserver<Object, Object> switchMapInnerObserver = (SwitchMapInnerObserver) this.active.getAndSet(CANCELLED);
                if (switchMapInnerObserver != CANCELLED && switchMapInnerObserver != null) {
                    switchMapInnerObserver.cancel();
                }
            }
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Removed duplicated region for block: B:78:0x000b A[SYNTHETIC] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void drain() {
            /*
                r10 = this;
                int r0 = r10.getAndIncrement()
                if (r0 == 0) goto L_0x0007
                return
            L_0x0007:
                io.reactivex.Observer<? super R> r0 = r10.actual
                r1 = 1
                r2 = r1
            L_0x000b:
                boolean r3 = r10.cancelled
                if (r3 == 0) goto L_0x0010
                return
            L_0x0010:
                boolean r3 = r10.done
                r4 = 0
                if (r3 == 0) goto L_0x0052
                java.util.concurrent.atomic.AtomicReference<io.reactivex.internal.operators.observable.ObservableSwitchMap$SwitchMapInnerObserver<T, R>> r3 = r10.active
                java.lang.Object r3 = r3.get()
                if (r3 != 0) goto L_0x001f
                r3 = r1
                goto L_0x0020
            L_0x001f:
                r3 = r4
            L_0x0020:
                boolean r5 = r10.delayErrors
                if (r5 == 0) goto L_0x0038
                if (r3 == 0) goto L_0x0052
                io.reactivex.internal.util.AtomicThrowable r10 = r10.errors
                java.lang.Object r10 = r10.get()
                java.lang.Throwable r10 = (java.lang.Throwable) r10
                if (r10 == 0) goto L_0x0034
                r0.onError(r10)
                goto L_0x0037
            L_0x0034:
                r0.onComplete()
            L_0x0037:
                return
            L_0x0038:
                io.reactivex.internal.util.AtomicThrowable r5 = r10.errors
                java.lang.Object r5 = r5.get()
                java.lang.Throwable r5 = (java.lang.Throwable) r5
                if (r5 == 0) goto L_0x004c
                io.reactivex.internal.util.AtomicThrowable r10 = r10.errors
                java.lang.Throwable r10 = r10.terminate()
                r0.onError(r10)
                return
            L_0x004c:
                if (r3 == 0) goto L_0x0052
                r0.onComplete()
                return
            L_0x0052:
                java.util.concurrent.atomic.AtomicReference<io.reactivex.internal.operators.observable.ObservableSwitchMap$SwitchMapInnerObserver<T, R>> r3 = r10.active
                java.lang.Object r3 = r3.get()
                io.reactivex.internal.operators.observable.ObservableSwitchMap$SwitchMapInnerObserver r3 = (p016io.reactivex.internal.operators.observable.ObservableSwitchMap.SwitchMapInnerObserver) r3
                if (r3 == 0) goto L_0x00d6
                io.reactivex.internal.queue.SpscLinkedArrayQueue<R> r5 = r3.queue
                boolean r6 = r3.done
                r7 = 0
                if (r6 == 0) goto L_0x0090
                boolean r6 = r5.isEmpty()
                boolean r8 = r10.delayErrors
                if (r8 == 0) goto L_0x0073
                if (r6 == 0) goto L_0x0090
                java.util.concurrent.atomic.AtomicReference<io.reactivex.internal.operators.observable.ObservableSwitchMap$SwitchMapInnerObserver<T, R>> r4 = r10.active
                r4.compareAndSet(r3, r7)
                goto L_0x000b
            L_0x0073:
                io.reactivex.internal.util.AtomicThrowable r8 = r10.errors
                java.lang.Object r8 = r8.get()
                java.lang.Throwable r8 = (java.lang.Throwable) r8
                if (r8 == 0) goto L_0x0087
                io.reactivex.internal.util.AtomicThrowable r10 = r10.errors
                java.lang.Throwable r10 = r10.terminate()
                r0.onError(r10)
                return
            L_0x0087:
                if (r6 == 0) goto L_0x0090
                java.util.concurrent.atomic.AtomicReference<io.reactivex.internal.operators.observable.ObservableSwitchMap$SwitchMapInnerObserver<T, R>> r4 = r10.active
                r4.compareAndSet(r3, r7)
                goto L_0x000b
            L_0x0090:
                boolean r6 = r10.cancelled
                if (r6 == 0) goto L_0x0095
                return
            L_0x0095:
                java.util.concurrent.atomic.AtomicReference<io.reactivex.internal.operators.observable.ObservableSwitchMap$SwitchMapInnerObserver<T, R>> r6 = r10.active
                java.lang.Object r6 = r6.get()
                if (r3 == r6) goto L_0x009f
            L_0x009d:
                r4 = r1
                goto L_0x00ce
            L_0x009f:
                boolean r6 = r10.delayErrors
                if (r6 != 0) goto L_0x00b7
                io.reactivex.internal.util.AtomicThrowable r6 = r10.errors
                java.lang.Object r6 = r6.get()
                java.lang.Throwable r6 = (java.lang.Throwable) r6
                if (r6 == 0) goto L_0x00b7
                io.reactivex.internal.util.AtomicThrowable r10 = r10.errors
                java.lang.Throwable r10 = r10.terminate()
                r0.onError(r10)
                return
            L_0x00b7:
                boolean r6 = r3.done
                java.lang.Object r8 = r5.poll()
                if (r8 != 0) goto L_0x00c1
                r9 = r1
                goto L_0x00c2
            L_0x00c1:
                r9 = r4
            L_0x00c2:
                if (r6 == 0) goto L_0x00cc
                if (r9 == 0) goto L_0x00cc
                java.util.concurrent.atomic.AtomicReference<io.reactivex.internal.operators.observable.ObservableSwitchMap$SwitchMapInnerObserver<T, R>> r4 = r10.active
                r4.compareAndSet(r3, r7)
                goto L_0x009d
            L_0x00cc:
                if (r9 == 0) goto L_0x00d2
            L_0x00ce:
                if (r4 == 0) goto L_0x00d6
                goto L_0x000b
            L_0x00d2:
                r0.onNext(r8)
                goto L_0x0090
            L_0x00d6:
                int r2 = -r2
                int r2 = r10.addAndGet(r2)
                if (r2 != 0) goto L_0x000b
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: p016io.reactivex.internal.operators.observable.ObservableSwitchMap.SwitchMapObserver.drain():void");
        }

        /* access modifiers changed from: 0000 */
        public void innerError(SwitchMapInnerObserver<T, R> switchMapInnerObserver, Throwable th) {
            if (switchMapInnerObserver.index != this.unique || !this.errors.addThrowable(th)) {
                RxJavaPlugins.onError(th);
                return;
            }
            if (!this.delayErrors) {
                this.f474s.dispose();
            }
            switchMapInnerObserver.done = true;
            drain();
        }
    }

    public ObservableSwitchMap(ObservableSource<T> observableSource, Function<? super T, ? extends ObservableSource<? extends R>> function, int i, boolean z) {
        super(observableSource);
        this.mapper = function;
        this.bufferSize = i;
        this.delayErrors = z;
    }

    public void subscribeActual(Observer<? super R> observer) {
        if (!ObservableScalarXMap.tryScalarXMapSubscribe(this.source, observer, this.mapper)) {
            this.source.subscribe(new SwitchMapObserver(observer, this.mapper, this.bufferSize, this.delayErrors));
        }
    }
}
