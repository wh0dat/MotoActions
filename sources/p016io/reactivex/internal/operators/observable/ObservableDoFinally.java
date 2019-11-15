package p016io.reactivex.internal.operators.observable;

import p016io.reactivex.ObservableSource;
import p016io.reactivex.Observer;
import p016io.reactivex.annotations.Experimental;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.Action;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.internal.fuseable.QueueDisposable;
import p016io.reactivex.internal.observers.BasicIntQueueDisposable;
import p016io.reactivex.plugins.RxJavaPlugins;

@Experimental
/* renamed from: io.reactivex.internal.operators.observable.ObservableDoFinally */
public final class ObservableDoFinally<T> extends AbstractObservableWithUpstream<T, T> {
    final Action onFinally;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableDoFinally$DoFinallyObserver */
    static final class DoFinallyObserver<T> extends BasicIntQueueDisposable<T> implements Observer<T> {
        private static final long serialVersionUID = 4109457741734051389L;
        final Observer<? super T> actual;

        /* renamed from: d */
        Disposable f424d;
        final Action onFinally;

        /* renamed from: qd */
        QueueDisposable<T> f425qd;
        boolean syncFused;

        DoFinallyObserver(Observer<? super T> observer, Action action) {
            this.actual = observer;
            this.onFinally = action;
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.f424d, disposable)) {
                this.f424d = disposable;
                if (disposable instanceof QueueDisposable) {
                    this.f425qd = (QueueDisposable) disposable;
                }
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            this.actual.onNext(t);
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
            runFinally();
        }

        public void onComplete() {
            this.actual.onComplete();
            runFinally();
        }

        public void dispose() {
            this.f424d.dispose();
            runFinally();
        }

        public boolean isDisposed() {
            return this.f424d.isDisposed();
        }

        public int requestFusion(int i) {
            QueueDisposable<T> queueDisposable = this.f425qd;
            if (queueDisposable == null || (i & 4) != 0) {
                return 0;
            }
            int requestFusion = queueDisposable.requestFusion(i);
            if (requestFusion != 0) {
                boolean z = true;
                if (requestFusion != 1) {
                    z = false;
                }
                this.syncFused = z;
            }
            return requestFusion;
        }

        public void clear() {
            this.f425qd.clear();
        }

        public boolean isEmpty() {
            return this.f425qd.isEmpty();
        }

        public T poll() throws Exception {
            T poll = this.f425qd.poll();
            if (poll == null && this.syncFused) {
                runFinally();
            }
            return poll;
        }

        /* access modifiers changed from: 0000 */
        public void runFinally() {
            if (compareAndSet(0, 1)) {
                try {
                    this.onFinally.run();
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    RxJavaPlugins.onError(th);
                }
            }
        }
    }

    public ObservableDoFinally(ObservableSource<T> observableSource, Action action) {
        super(observableSource);
        this.onFinally = action;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super T> observer) {
        this.source.subscribe(new DoFinallyObserver(observer, this.onFinally));
    }
}
