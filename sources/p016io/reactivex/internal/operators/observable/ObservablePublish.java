package p016io.reactivex.internal.operators.observable;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import p016io.reactivex.ObservableSource;
import p016io.reactivex.Observer;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.internal.fuseable.HasUpstreamObservableSource;
import p016io.reactivex.observables.ConnectableObservable;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.observable.ObservablePublish */
public final class ObservablePublish<T> extends ConnectableObservable<T> implements HasUpstreamObservableSource<T> {
    final AtomicReference<PublishObserver<T>> current;
    final ObservableSource<T> onSubscribe;
    final ObservableSource<T> source;

    /* renamed from: io.reactivex.internal.operators.observable.ObservablePublish$InnerDisposable */
    static final class InnerDisposable<T> extends AtomicReference<Object> implements Disposable {
        private static final long serialVersionUID = -1100270633763673112L;
        final Observer<? super T> child;

        InnerDisposable(Observer<? super T> observer) {
            this.child = observer;
        }

        public boolean isDisposed() {
            return get() == this;
        }

        public void dispose() {
            Object andSet = getAndSet(this);
            if (andSet != null && andSet != this) {
                ((PublishObserver) andSet).remove(this);
            }
        }

        /* access modifiers changed from: 0000 */
        public void setParent(PublishObserver<T> publishObserver) {
            if (!compareAndSet(null, publishObserver)) {
                publishObserver.remove(this);
            }
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservablePublish$PublishObserver */
    static final class PublishObserver<T> implements Observer<T>, Disposable {
        static final InnerDisposable[] EMPTY = new InnerDisposable[0];
        static final InnerDisposable[] TERMINATED = new InnerDisposable[0];
        final AtomicReference<PublishObserver<T>> current;
        final AtomicReference<InnerDisposable<T>[]> observers = new AtomicReference<>(EMPTY);

        /* renamed from: s */
        final AtomicReference<Disposable> f450s = new AtomicReference<>();
        final AtomicBoolean shouldConnect;

        PublishObserver(AtomicReference<PublishObserver<T>> atomicReference) {
            this.current = atomicReference;
            this.shouldConnect = new AtomicBoolean();
        }

        public void dispose() {
            if (this.observers.get() != TERMINATED && ((InnerDisposable[]) this.observers.getAndSet(TERMINATED)) != TERMINATED) {
                this.current.compareAndSet(this, null);
                DisposableHelper.dispose(this.f450s);
            }
        }

        public boolean isDisposed() {
            return this.observers.get() == TERMINATED;
        }

        public void onSubscribe(Disposable disposable) {
            DisposableHelper.setOnce(this.f450s, disposable);
        }

        public void onNext(T t) {
            for (InnerDisposable innerDisposable : (InnerDisposable[]) this.observers.get()) {
                innerDisposable.child.onNext(t);
            }
        }

        public void onError(Throwable th) {
            this.current.compareAndSet(this, null);
            InnerDisposable[] innerDisposableArr = (InnerDisposable[]) this.observers.getAndSet(TERMINATED);
            if (innerDisposableArr.length != 0) {
                for (InnerDisposable innerDisposable : innerDisposableArr) {
                    innerDisposable.child.onError(th);
                }
                return;
            }
            RxJavaPlugins.onError(th);
        }

        public void onComplete() {
            this.current.compareAndSet(this, null);
            for (InnerDisposable innerDisposable : (InnerDisposable[]) this.observers.getAndSet(TERMINATED)) {
                innerDisposable.child.onComplete();
            }
        }

        /* access modifiers changed from: 0000 */
        public boolean add(InnerDisposable<T> innerDisposable) {
            InnerDisposable[] innerDisposableArr;
            InnerDisposable[] innerDisposableArr2;
            do {
                innerDisposableArr = (InnerDisposable[]) this.observers.get();
                if (innerDisposableArr == TERMINATED) {
                    return false;
                }
                int length = innerDisposableArr.length;
                innerDisposableArr2 = new InnerDisposable[(length + 1)];
                System.arraycopy(innerDisposableArr, 0, innerDisposableArr2, 0, length);
                innerDisposableArr2[length] = innerDisposable;
            } while (!this.observers.compareAndSet(innerDisposableArr, innerDisposableArr2));
            return true;
        }

        /* access modifiers changed from: 0000 */
        public void remove(InnerDisposable<T> innerDisposable) {
            InnerDisposable[] innerDisposableArr;
            InnerDisposable[] innerDisposableArr2;
            do {
                innerDisposableArr = (InnerDisposable[]) this.observers.get();
                int length = innerDisposableArr.length;
                if (length != 0) {
                    int i = -1;
                    int i2 = 0;
                    while (true) {
                        if (i2 >= length) {
                            break;
                        } else if (innerDisposableArr[i2].equals(innerDisposable)) {
                            i = i2;
                            break;
                        } else {
                            i2++;
                        }
                    }
                    if (i >= 0) {
                        if (length == 1) {
                            innerDisposableArr2 = EMPTY;
                        } else {
                            InnerDisposable[] innerDisposableArr3 = new InnerDisposable[(length - 1)];
                            System.arraycopy(innerDisposableArr, 0, innerDisposableArr3, 0, i);
                            System.arraycopy(innerDisposableArr, i + 1, innerDisposableArr3, i, (length - i) - 1);
                            innerDisposableArr2 = innerDisposableArr3;
                        }
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            } while (!this.observers.compareAndSet(innerDisposableArr, innerDisposableArr2));
        }
    }

    public static <T> ConnectableObservable<T> create(ObservableSource<T> observableSource) {
        final AtomicReference atomicReference = new AtomicReference();
        return RxJavaPlugins.onAssembly((ConnectableObservable<T>) new ObservablePublish<T>(new ObservableSource<T>() {
            public void subscribe(Observer<? super T> observer) {
                InnerDisposable innerDisposable = new InnerDisposable(observer);
                observer.onSubscribe(innerDisposable);
                while (true) {
                    PublishObserver publishObserver = (PublishObserver) atomicReference.get();
                    if (publishObserver == null || publishObserver.isDisposed()) {
                        PublishObserver publishObserver2 = new PublishObserver(atomicReference);
                        if (!atomicReference.compareAndSet(publishObserver, publishObserver2)) {
                            continue;
                        } else {
                            publishObserver = publishObserver2;
                        }
                    }
                    if (publishObserver.add(innerDisposable)) {
                        innerDisposable.setParent(publishObserver);
                        return;
                    }
                }
            }
        }, observableSource, atomicReference));
    }

    private ObservablePublish(ObservableSource<T> observableSource, ObservableSource<T> observableSource2, AtomicReference<PublishObserver<T>> atomicReference) {
        this.onSubscribe = observableSource;
        this.source = observableSource2;
        this.current = atomicReference;
    }

    public ObservableSource<T> source() {
        return this.source;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super T> observer) {
        this.onSubscribe.subscribe(observer);
    }

    /* JADX WARNING: Removed duplicated region for block: B:0:0x0000 A[LOOP_START] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void connect(p016io.reactivex.functions.Consumer<? super p016io.reactivex.disposables.Disposable> r5) {
        /*
            r4 = this;
        L_0x0000:
            java.util.concurrent.atomic.AtomicReference<io.reactivex.internal.operators.observable.ObservablePublish$PublishObserver<T>> r0 = r4.current
            java.lang.Object r0 = r0.get()
            io.reactivex.internal.operators.observable.ObservablePublish$PublishObserver r0 = (p016io.reactivex.internal.operators.observable.ObservablePublish.PublishObserver) r0
            if (r0 == 0) goto L_0x0010
            boolean r1 = r0.isDisposed()
            if (r1 == 0) goto L_0x0021
        L_0x0010:
            io.reactivex.internal.operators.observable.ObservablePublish$PublishObserver r1 = new io.reactivex.internal.operators.observable.ObservablePublish$PublishObserver
            java.util.concurrent.atomic.AtomicReference<io.reactivex.internal.operators.observable.ObservablePublish$PublishObserver<T>> r2 = r4.current
            r1.<init>(r2)
            java.util.concurrent.atomic.AtomicReference<io.reactivex.internal.operators.observable.ObservablePublish$PublishObserver<T>> r2 = r4.current
            boolean r0 = r2.compareAndSet(r0, r1)
            if (r0 != 0) goto L_0x0020
            goto L_0x0000
        L_0x0020:
            r0 = r1
        L_0x0021:
            java.util.concurrent.atomic.AtomicBoolean r1 = r0.shouldConnect
            boolean r1 = r1.get()
            r2 = 1
            r3 = 0
            if (r1 != 0) goto L_0x0034
            java.util.concurrent.atomic.AtomicBoolean r1 = r0.shouldConnect
            boolean r1 = r1.compareAndSet(r3, r2)
            if (r1 == 0) goto L_0x0034
            goto L_0x0035
        L_0x0034:
            r2 = r3
        L_0x0035:
            r5.accept(r0)     // Catch:{ Throwable -> 0x0040 }
            if (r2 == 0) goto L_0x003f
            io.reactivex.ObservableSource<T> r4 = r4.source
            r4.subscribe(r0)
        L_0x003f:
            return
        L_0x0040:
            r4 = move-exception
            p016io.reactivex.exceptions.Exceptions.throwIfFatal(r4)
            java.lang.RuntimeException r4 = p016io.reactivex.internal.util.ExceptionHelper.wrapOrThrow(r4)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: p016io.reactivex.internal.operators.observable.ObservablePublish.connect(io.reactivex.functions.Consumer):void");
    }
}
