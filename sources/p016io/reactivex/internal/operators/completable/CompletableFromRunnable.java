package p016io.reactivex.internal.operators.completable;

import p016io.reactivex.Completable;
import p016io.reactivex.CompletableObserver;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.disposables.Disposables;
import p016io.reactivex.exceptions.Exceptions;

/* renamed from: io.reactivex.internal.operators.completable.CompletableFromRunnable */
public final class CompletableFromRunnable extends Completable {
    final Runnable runnable;

    public CompletableFromRunnable(Runnable runnable2) {
        this.runnable = runnable2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver completableObserver) {
        Disposable empty = Disposables.empty();
        completableObserver.onSubscribe(empty);
        try {
            this.runnable.run();
            if (!empty.isDisposed()) {
                completableObserver.onComplete();
            }
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            if (!empty.isDisposed()) {
                completableObserver.onError(th);
            }
        }
    }
}
