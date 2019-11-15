package p016io.reactivex.internal.operators.completable;

import p016io.reactivex.Completable;
import p016io.reactivex.CompletableObserver;
import p016io.reactivex.CompletableSource;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.CompositeException;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.Function;
import p016io.reactivex.internal.disposables.SequentialDisposable;

/* renamed from: io.reactivex.internal.operators.completable.CompletableResumeNext */
public final class CompletableResumeNext extends Completable {
    final Function<? super Throwable, ? extends CompletableSource> errorMapper;
    final CompletableSource source;

    public CompletableResumeNext(CompletableSource completableSource, Function<? super Throwable, ? extends CompletableSource> function) {
        this.source = completableSource;
        this.errorMapper = function;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(final CompletableObserver completableObserver) {
        final SequentialDisposable sequentialDisposable = new SequentialDisposable();
        completableObserver.onSubscribe(sequentialDisposable);
        this.source.subscribe(new CompletableObserver() {
            public void onComplete() {
                completableObserver.onComplete();
            }

            public void onError(Throwable th) {
                try {
                    CompletableSource completableSource = (CompletableSource) CompletableResumeNext.this.errorMapper.apply(th);
                    if (completableSource == null) {
                        NullPointerException nullPointerException = new NullPointerException("The CompletableConsumable returned is null");
                        nullPointerException.initCause(th);
                        completableObserver.onError(nullPointerException);
                        return;
                    }
                    completableSource.subscribe(new CompletableObserver() {
                        public void onComplete() {
                            completableObserver.onComplete();
                        }

                        public void onError(Throwable th) {
                            completableObserver.onError(th);
                        }

                        public void onSubscribe(Disposable disposable) {
                            sequentialDisposable.update(disposable);
                        }
                    });
                } catch (Throwable th2) {
                    Exceptions.throwIfFatal(th2);
                    completableObserver.onError(new CompositeException(th2, th));
                }
            }

            public void onSubscribe(Disposable disposable) {
                sequentialDisposable.update(disposable);
            }
        });
    }
}
