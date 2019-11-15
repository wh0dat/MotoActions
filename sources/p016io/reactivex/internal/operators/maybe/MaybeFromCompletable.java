package p016io.reactivex.internal.operators.maybe;

import p016io.reactivex.CompletableObserver;
import p016io.reactivex.CompletableSource;
import p016io.reactivex.Maybe;
import p016io.reactivex.MaybeObserver;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.internal.fuseable.HasUpstreamCompletableSource;

/* renamed from: io.reactivex.internal.operators.maybe.MaybeFromCompletable */
public final class MaybeFromCompletable<T> extends Maybe<T> implements HasUpstreamCompletableSource {
    final CompletableSource source;

    /* renamed from: io.reactivex.internal.operators.maybe.MaybeFromCompletable$FromCompletableObserver */
    static final class FromCompletableObserver<T> implements CompletableObserver, Disposable {
        final MaybeObserver<? super T> actual;

        /* renamed from: d */
        Disposable f376d;

        FromCompletableObserver(MaybeObserver<? super T> maybeObserver) {
            this.actual = maybeObserver;
        }

        public void dispose() {
            this.f376d.dispose();
            this.f376d = DisposableHelper.DISPOSED;
        }

        public boolean isDisposed() {
            return this.f376d.isDisposed();
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.f376d, disposable)) {
                this.f376d = disposable;
                this.actual.onSubscribe(this);
            }
        }

        public void onComplete() {
            this.f376d = DisposableHelper.DISPOSED;
            this.actual.onComplete();
        }

        public void onError(Throwable th) {
            this.f376d = DisposableHelper.DISPOSED;
            this.actual.onError(th);
        }
    }

    public MaybeFromCompletable(CompletableSource completableSource) {
        this.source = completableSource;
    }

    public CompletableSource source() {
        return this.source;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver<? super T> maybeObserver) {
        this.source.subscribe(new FromCompletableObserver(maybeObserver));
    }
}
