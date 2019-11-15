package p016io.reactivex.internal.operators.observable;

import java.util.concurrent.atomic.AtomicReference;
import p016io.reactivex.Observable;
import p016io.reactivex.ObservableSource;
import p016io.reactivex.Observer;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.Function;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.internal.disposables.EmptyDisposable;
import p016io.reactivex.internal.functions.ObjectHelper;
import p016io.reactivex.subjects.PublishSubject;

/* renamed from: io.reactivex.internal.operators.observable.ObservablePublishSelector */
public final class ObservablePublishSelector<T, R> extends AbstractObservableWithUpstream<T, R> {
    final Function<? super Observable<T>, ? extends ObservableSource<R>> selector;

    /* renamed from: io.reactivex.internal.operators.observable.ObservablePublishSelector$SourceObserver */
    static final class SourceObserver<T, R> implements Observer<T> {
        final PublishSubject<T> subject;
        final AtomicReference<Disposable> target;

        SourceObserver(PublishSubject<T> publishSubject, AtomicReference<Disposable> atomicReference) {
            this.subject = publishSubject;
            this.target = atomicReference;
        }

        public void onSubscribe(Disposable disposable) {
            DisposableHelper.setOnce(this.target, disposable);
        }

        public void onNext(T t) {
            this.subject.onNext(t);
        }

        public void onError(Throwable th) {
            this.subject.onError(th);
        }

        public void onComplete() {
            this.subject.onComplete();
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservablePublishSelector$TargetObserver */
    static final class TargetObserver<T, R> extends AtomicReference<Disposable> implements Observer<R>, Disposable {
        private static final long serialVersionUID = 854110278590336484L;
        final Observer<? super R> actual;

        /* renamed from: d */
        Disposable f451d;

        TargetObserver(Observer<? super R> observer) {
            this.actual = observer;
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.f451d, disposable)) {
                this.f451d = disposable;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(R r) {
            this.actual.onNext(r);
        }

        public void onError(Throwable th) {
            DisposableHelper.dispose(this);
            this.actual.onError(th);
        }

        public void onComplete() {
            DisposableHelper.dispose(this);
            this.actual.onComplete();
        }

        public void dispose() {
            this.f451d.dispose();
            DisposableHelper.dispose(this);
        }

        public boolean isDisposed() {
            return this.f451d.isDisposed();
        }
    }

    public ObservablePublishSelector(ObservableSource<T> observableSource, Function<? super Observable<T>, ? extends ObservableSource<R>> function) {
        super(observableSource);
        this.selector = function;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super R> observer) {
        PublishSubject create = PublishSubject.create();
        try {
            ObservableSource observableSource = (ObservableSource) ObjectHelper.requireNonNull(this.selector.apply(create), "The selector returned a null ObservableSource");
            TargetObserver targetObserver = new TargetObserver(observer);
            observableSource.subscribe(targetObserver);
            this.source.subscribe(new SourceObserver(create, targetObserver));
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            EmptyDisposable.error(th, observer);
        }
    }
}
