package p016io.reactivex.internal.operators.completable;

import p016io.reactivex.Completable;
import p016io.reactivex.CompletableObserver;
import p016io.reactivex.CompletableSource;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.disposables.Disposables;
import p016io.reactivex.exceptions.CompositeException;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.Action;
import p016io.reactivex.functions.Consumer;
import p016io.reactivex.internal.disposables.EmptyDisposable;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.completable.CompletablePeek */
public final class CompletablePeek extends Completable {
    final Action onAfterTerminate;
    final Action onComplete;
    final Action onDispose;
    final Consumer<? super Throwable> onError;
    final Consumer<? super Disposable> onSubscribe;
    final Action onTerminate;
    final CompletableSource source;

    public CompletablePeek(CompletableSource completableSource, Consumer<? super Disposable> consumer, Consumer<? super Throwable> consumer2, Action action, Action action2, Action action3, Action action4) {
        this.source = completableSource;
        this.onSubscribe = consumer;
        this.onError = consumer2;
        this.onComplete = action;
        this.onTerminate = action2;
        this.onAfterTerminate = action3;
        this.onDispose = action4;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(final CompletableObserver completableObserver) {
        this.source.subscribe(new CompletableObserver() {
            public void onComplete() {
                try {
                    CompletablePeek.this.onComplete.run();
                    CompletablePeek.this.onTerminate.run();
                    completableObserver.onComplete();
                    doAfter();
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    completableObserver.onError(th);
                }
            }

            public void onError(Throwable th) {
                try {
                    CompletablePeek.this.onError.accept(th);
                    CompletablePeek.this.onTerminate.run();
                } catch (Throwable th2) {
                    Exceptions.throwIfFatal(th2);
                    th = new CompositeException(th, th2);
                }
                completableObserver.onError(th);
                doAfter();
            }

            public void onSubscribe(final Disposable disposable) {
                try {
                    CompletablePeek.this.onSubscribe.accept(disposable);
                    completableObserver.onSubscribe(Disposables.fromRunnable(new Runnable() {
                        public void run() {
                            try {
                                CompletablePeek.this.onDispose.run();
                            } catch (Throwable th) {
                                Exceptions.throwIfFatal(th);
                                RxJavaPlugins.onError(th);
                            }
                            disposable.dispose();
                        }
                    }));
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    disposable.dispose();
                    EmptyDisposable.error(th, completableObserver);
                }
            }

            /* access modifiers changed from: 0000 */
            public void doAfter() {
                try {
                    CompletablePeek.this.onAfterTerminate.run();
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    RxJavaPlugins.onError(th);
                }
            }
        });
    }
}
