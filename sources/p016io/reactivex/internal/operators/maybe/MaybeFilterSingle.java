package p016io.reactivex.internal.operators.maybe;

import p016io.reactivex.Maybe;
import p016io.reactivex.MaybeObserver;
import p016io.reactivex.SingleObserver;
import p016io.reactivex.SingleSource;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.Predicate;
import p016io.reactivex.internal.disposables.DisposableHelper;

/* renamed from: io.reactivex.internal.operators.maybe.MaybeFilterSingle */
public final class MaybeFilterSingle<T> extends Maybe<T> {
    final Predicate<? super T> predicate;
    final SingleSource<T> source;

    /* renamed from: io.reactivex.internal.operators.maybe.MaybeFilterSingle$FilterMaybeObserver */
    static final class FilterMaybeObserver<T> implements SingleObserver<T>, Disposable {
        final MaybeObserver<? super T> actual;

        /* renamed from: d */
        Disposable f369d;
        final Predicate<? super T> predicate;

        FilterMaybeObserver(MaybeObserver<? super T> maybeObserver, Predicate<? super T> predicate2) {
            this.actual = maybeObserver;
            this.predicate = predicate2;
        }

        public void dispose() {
            Disposable disposable = this.f369d;
            this.f369d = DisposableHelper.DISPOSED;
            disposable.dispose();
        }

        public boolean isDisposed() {
            return this.f369d.isDisposed();
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.f369d, disposable)) {
                this.f369d = disposable;
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(T t) {
            try {
                if (this.predicate.test(t)) {
                    this.actual.onSuccess(t);
                } else {
                    this.actual.onComplete();
                }
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                this.actual.onError(th);
            }
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
        }
    }

    public MaybeFilterSingle(SingleSource<T> singleSource, Predicate<? super T> predicate2) {
        this.source = singleSource;
        this.predicate = predicate2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver<? super T> maybeObserver) {
        this.source.subscribe(new FilterMaybeObserver(maybeObserver, this.predicate));
    }
}
