package p016io.reactivex.internal.operators.completable;

import p016io.reactivex.Completable;
import p016io.reactivex.CompletableObserver;
import p016io.reactivex.CompletableSource;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.CompositeException;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.Consumer;

/* renamed from: io.reactivex.internal.operators.completable.CompletableDoOnEvent */
public final class CompletableDoOnEvent extends Completable {
    final Consumer<? super Throwable> onEvent;
    final CompletableSource source;

    public CompletableDoOnEvent(CompletableSource completableSource, Consumer<? super Throwable> consumer) {
        this.source = completableSource;
        this.onEvent = consumer;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(final CompletableObserver completableObserver) {
        this.source.subscribe(new CompletableObserver() {
            public void onComplete() {
                try {
                    CompletableDoOnEvent.this.onEvent.accept(null);
                    completableObserver.onComplete();
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    completableObserver.onError(th);
                }
            }

            public void onError(Throwable th) {
                try {
                    CompletableDoOnEvent.this.onEvent.accept(th);
                } catch (Throwable th2) {
                    Exceptions.throwIfFatal(th2);
                    th = new CompositeException(th, th2);
                }
                completableObserver.onError(th);
            }

            public void onSubscribe(Disposable disposable) {
                completableObserver.onSubscribe(disposable);
            }
        });
    }
}
