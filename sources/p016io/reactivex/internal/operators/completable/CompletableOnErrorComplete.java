package p016io.reactivex.internal.operators.completable;

import p016io.reactivex.Completable;
import p016io.reactivex.CompletableObserver;
import p016io.reactivex.CompletableSource;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.CompositeException;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.Predicate;

/* renamed from: io.reactivex.internal.operators.completable.CompletableOnErrorComplete */
public final class CompletableOnErrorComplete extends Completable {
    final Predicate<? super Throwable> predicate;
    final CompletableSource source;

    public CompletableOnErrorComplete(CompletableSource completableSource, Predicate<? super Throwable> predicate2) {
        this.source = completableSource;
        this.predicate = predicate2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(final CompletableObserver completableObserver) {
        this.source.subscribe(new CompletableObserver() {
            public void onComplete() {
                completableObserver.onComplete();
            }

            public void onError(Throwable th) {
                try {
                    if (CompletableOnErrorComplete.this.predicate.test(th)) {
                        completableObserver.onComplete();
                    } else {
                        completableObserver.onError(th);
                    }
                } catch (Throwable th2) {
                    Exceptions.throwIfFatal(th2);
                    completableObserver.onError(new CompositeException(th, th2));
                }
            }

            public void onSubscribe(Disposable disposable) {
                completableObserver.onSubscribe(disposable);
            }
        });
    }
}
