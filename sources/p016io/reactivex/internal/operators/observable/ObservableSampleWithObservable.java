package p016io.reactivex.internal.operators.observable;

import java.util.concurrent.atomic.AtomicReference;
import p016io.reactivex.ObservableSource;
import p016io.reactivex.Observer;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.observers.SerializedObserver;

/* renamed from: io.reactivex.internal.operators.observable.ObservableSampleWithObservable */
public final class ObservableSampleWithObservable<T> extends AbstractObservableWithUpstream<T, T> {
    final ObservableSource<?> other;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableSampleWithObservable$SampleMainObserver */
    static final class SampleMainObserver<T> extends AtomicReference<T> implements Observer<T>, Disposable {
        private static final long serialVersionUID = -3517602651313910099L;
        final Observer<? super T> actual;
        final AtomicReference<Disposable> other = new AtomicReference<>();

        /* renamed from: s */
        Disposable f457s;
        final ObservableSource<?> sampler;

        SampleMainObserver(Observer<? super T> observer, ObservableSource<?> observableSource) {
            this.actual = observer;
            this.sampler = observableSource;
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.f457s, disposable)) {
                this.f457s = disposable;
                this.actual.onSubscribe(this);
                if (this.other.get() == null) {
                    this.sampler.subscribe(new SamplerObserver(this));
                }
            }
        }

        public void onNext(T t) {
            lazySet(t);
        }

        public void onError(Throwable th) {
            DisposableHelper.dispose(this.other);
            this.actual.onError(th);
        }

        public void onComplete() {
            DisposableHelper.dispose(this.other);
            this.actual.onComplete();
        }

        /* access modifiers changed from: 0000 */
        public boolean setOther(Disposable disposable) {
            return DisposableHelper.setOnce(this.other, disposable);
        }

        public void dispose() {
            DisposableHelper.dispose(this.other);
            this.f457s.dispose();
        }

        public boolean isDisposed() {
            return this.other.get() == DisposableHelper.DISPOSED;
        }

        public void error(Throwable th) {
            this.f457s.dispose();
            this.actual.onError(th);
        }

        public void complete() {
            this.f457s.dispose();
            this.actual.onComplete();
        }

        public void emit() {
            Object andSet = getAndSet(null);
            if (andSet != null) {
                this.actual.onNext(andSet);
            }
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableSampleWithObservable$SamplerObserver */
    static final class SamplerObserver<T> implements Observer<Object> {
        final SampleMainObserver<T> parent;

        SamplerObserver(SampleMainObserver<T> sampleMainObserver) {
            this.parent = sampleMainObserver;
        }

        public void onSubscribe(Disposable disposable) {
            this.parent.setOther(disposable);
        }

        public void onNext(Object obj) {
            this.parent.emit();
        }

        public void onError(Throwable th) {
            this.parent.error(th);
        }

        public void onComplete() {
            this.parent.complete();
        }
    }

    public ObservableSampleWithObservable(ObservableSource<T> observableSource, ObservableSource<?> observableSource2) {
        super(observableSource);
        this.other = observableSource2;
    }

    public void subscribeActual(Observer<? super T> observer) {
        this.source.subscribe(new SampleMainObserver(new SerializedObserver(observer), this.other));
    }
}
