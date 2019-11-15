package p016io.reactivex.internal.operators.completable;

import p016io.reactivex.Completable;
import p016io.reactivex.CompletableObserver;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.disposables.Disposables;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.Action;

/* renamed from: io.reactivex.internal.operators.completable.CompletableFromAction */
public final class CompletableFromAction extends Completable {
    final Action run;

    public CompletableFromAction(Action action) {
        this.run = action;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver completableObserver) {
        Disposable empty = Disposables.empty();
        completableObserver.onSubscribe(empty);
        try {
            this.run.run();
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
