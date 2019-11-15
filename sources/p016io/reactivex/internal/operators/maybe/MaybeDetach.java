package p016io.reactivex.internal.operators.maybe;

import p016io.reactivex.MaybeObserver;
import p016io.reactivex.MaybeSource;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.disposables.DisposableHelper;

/* renamed from: io.reactivex.internal.operators.maybe.MaybeDetach */
public final class MaybeDetach<T> extends AbstractMaybeWithUpstream<T, T> {

    /* renamed from: io.reactivex.internal.operators.maybe.MaybeDetach$DetachMaybeObserver */
    static final class DetachMaybeObserver<T> implements MaybeObserver<T>, Disposable {
        MaybeObserver<? super T> actual;

        /* renamed from: d */
        Disposable f364d;

        DetachMaybeObserver(MaybeObserver<? super T> maybeObserver) {
            this.actual = maybeObserver;
        }

        public void dispose() {
            this.actual = null;
            this.f364d.dispose();
            this.f364d = DisposableHelper.DISPOSED;
        }

        public boolean isDisposed() {
            return this.f364d.isDisposed();
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.f364d, disposable)) {
                this.f364d = disposable;
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(T t) {
            this.f364d = DisposableHelper.DISPOSED;
            MaybeObserver<? super T> maybeObserver = this.actual;
            if (maybeObserver != null) {
                maybeObserver.onSuccess(t);
            }
        }

        public void onError(Throwable th) {
            this.f364d = DisposableHelper.DISPOSED;
            MaybeObserver<? super T> maybeObserver = this.actual;
            if (maybeObserver != null) {
                maybeObserver.onError(th);
            }
        }

        public void onComplete() {
            this.f364d = DisposableHelper.DISPOSED;
            MaybeObserver<? super T> maybeObserver = this.actual;
            if (maybeObserver != null) {
                maybeObserver.onComplete();
            }
        }
    }

    public MaybeDetach(MaybeSource<T> maybeSource) {
        super(maybeSource);
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver<? super T> maybeObserver) {
        this.source.subscribe(new DetachMaybeObserver(maybeObserver));
    }
}
