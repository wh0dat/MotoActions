package p016io.reactivex.internal.operators.completable;

import java.util.concurrent.Callable;
import p016io.reactivex.Completable;
import p016io.reactivex.CompletableObserver;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.internal.disposables.EmptyDisposable;
import p016io.reactivex.internal.functions.ObjectHelper;

/* renamed from: io.reactivex.internal.operators.completable.CompletableErrorSupplier */
public final class CompletableErrorSupplier extends Completable {
    final Callable<? extends Throwable> errorSupplier;

    public CompletableErrorSupplier(Callable<? extends Throwable> callable) {
        this.errorSupplier = callable;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver completableObserver) {
        try {
            th = (Throwable) ObjectHelper.requireNonNull(this.errorSupplier.call(), "The error returned is null");
        } catch (Throwable th) {
            th = th;
            Exceptions.throwIfFatal(th);
        }
        EmptyDisposable.error(th, completableObserver);
    }
}
