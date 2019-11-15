package p016io.reactivex.internal.operators.maybe;

import java.util.NoSuchElementException;
import p016io.reactivex.MaybeObserver;
import p016io.reactivex.MaybeSource;
import p016io.reactivex.Single;
import p016io.reactivex.SingleObserver;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.internal.fuseable.HasUpstreamMaybeSource;

/* renamed from: io.reactivex.internal.operators.maybe.MaybeToSingle */
public final class MaybeToSingle<T> extends Single<T> implements HasUpstreamMaybeSource<T> {
    final T defaultValue;
    final MaybeSource<T> source;

    /* renamed from: io.reactivex.internal.operators.maybe.MaybeToSingle$ToSingleMaybeSubscriber */
    static final class ToSingleMaybeSubscriber<T> implements MaybeObserver<T>, Disposable {
        final SingleObserver<? super T> actual;

        /* renamed from: d */
        Disposable f390d;
        final T defaultValue;

        ToSingleMaybeSubscriber(SingleObserver<? super T> singleObserver, T t) {
            this.actual = singleObserver;
            this.defaultValue = t;
        }

        public void dispose() {
            this.f390d.dispose();
            this.f390d = DisposableHelper.DISPOSED;
        }

        public boolean isDisposed() {
            return this.f390d.isDisposed();
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.f390d, disposable)) {
                this.f390d = disposable;
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(T t) {
            this.f390d = DisposableHelper.DISPOSED;
            this.actual.onSuccess(t);
        }

        public void onError(Throwable th) {
            this.f390d = DisposableHelper.DISPOSED;
            this.actual.onError(th);
        }

        public void onComplete() {
            this.f390d = DisposableHelper.DISPOSED;
            if (this.defaultValue != null) {
                this.actual.onSuccess(this.defaultValue);
            } else {
                this.actual.onError(new NoSuchElementException("The MaybeSource is empty"));
            }
        }
    }

    public MaybeToSingle(MaybeSource<T> maybeSource, T t) {
        this.source = maybeSource;
        this.defaultValue = t;
    }

    public MaybeSource<T> source() {
        return this.source;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super T> singleObserver) {
        this.source.subscribe(new ToSingleMaybeSubscriber(singleObserver, this.defaultValue));
    }
}
