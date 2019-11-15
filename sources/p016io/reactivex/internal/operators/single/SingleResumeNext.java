package p016io.reactivex.internal.operators.single;

import java.util.concurrent.atomic.AtomicReference;
import p016io.reactivex.Single;
import p016io.reactivex.SingleObserver;
import p016io.reactivex.SingleSource;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.CompositeException;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.Function;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.internal.functions.ObjectHelper;
import p016io.reactivex.internal.observers.ResumeSingleObserver;

/* renamed from: io.reactivex.internal.operators.single.SingleResumeNext */
public final class SingleResumeNext<T> extends Single<T> {
    final Function<? super Throwable, ? extends SingleSource<? extends T>> nextFunction;
    final SingleSource<? extends T> source;

    /* renamed from: io.reactivex.internal.operators.single.SingleResumeNext$ResumeMainSingleObserver */
    static final class ResumeMainSingleObserver<T> extends AtomicReference<Disposable> implements SingleObserver<T>, Disposable {
        private static final long serialVersionUID = -5314538511045349925L;
        final SingleObserver<? super T> actual;
        final Function<? super Throwable, ? extends SingleSource<? extends T>> nextFunction;

        ResumeMainSingleObserver(SingleObserver<? super T> singleObserver, Function<? super Throwable, ? extends SingleSource<? extends T>> function) {
            this.actual = singleObserver;
            this.nextFunction = function;
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.setOnce(this, disposable)) {
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(T t) {
            this.actual.onSuccess(t);
        }

        public void onError(Throwable th) {
            try {
                ((SingleSource) ObjectHelper.requireNonNull(this.nextFunction.apply(th), "The nextFunction returned a null SingleSource.")).subscribe(new ResumeSingleObserver(this, this.actual));
            } catch (Throwable th2) {
                Exceptions.throwIfFatal(th2);
                this.actual.onError(new CompositeException(th, th2));
            }
        }

        public void dispose() {
            DisposableHelper.dispose(this);
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) get());
        }
    }

    public SingleResumeNext(SingleSource<? extends T> singleSource, Function<? super Throwable, ? extends SingleSource<? extends T>> function) {
        this.source = singleSource;
        this.nextFunction = function;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super T> singleObserver) {
        this.source.subscribe(new ResumeMainSingleObserver(singleObserver, this.nextFunction));
    }
}
