package p016io.reactivex.internal.operators.completable;

import p016io.reactivex.Completable;
import p016io.reactivex.CompletableObserver;
import p016io.reactivex.SingleObserver;
import p016io.reactivex.SingleSource;
import p016io.reactivex.disposables.Disposable;

/* renamed from: io.reactivex.internal.operators.completable.CompletableFromSingle */
public final class CompletableFromSingle<T> extends Completable {
    final SingleSource<T> single;

    /* renamed from: io.reactivex.internal.operators.completable.CompletableFromSingle$CompletableFromSingleObserver */
    static final class CompletableFromSingleObserver<T> implements SingleObserver<T> {

        /* renamed from: co */
        final CompletableObserver f238co;

        CompletableFromSingleObserver(CompletableObserver completableObserver) {
            this.f238co = completableObserver;
        }

        public void onError(Throwable th) {
            this.f238co.onError(th);
        }

        public void onSubscribe(Disposable disposable) {
            this.f238co.onSubscribe(disposable);
        }

        public void onSuccess(T t) {
            this.f238co.onComplete();
        }
    }

    public CompletableFromSingle(SingleSource<T> singleSource) {
        this.single = singleSource;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver completableObserver) {
        this.single.subscribe(new CompletableFromSingleObserver(completableObserver));
    }
}
