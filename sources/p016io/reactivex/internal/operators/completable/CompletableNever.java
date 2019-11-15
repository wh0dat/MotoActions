package p016io.reactivex.internal.operators.completable;

import p016io.reactivex.Completable;
import p016io.reactivex.CompletableObserver;
import p016io.reactivex.internal.disposables.EmptyDisposable;

/* renamed from: io.reactivex.internal.operators.completable.CompletableNever */
public final class CompletableNever extends Completable {
    public static final Completable INSTANCE = new CompletableNever();

    private CompletableNever() {
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver completableObserver) {
        completableObserver.onSubscribe(EmptyDisposable.NEVER);
    }
}
