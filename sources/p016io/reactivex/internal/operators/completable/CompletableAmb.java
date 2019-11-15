package p016io.reactivex.internal.operators.completable;

import java.util.concurrent.atomic.AtomicBoolean;
import p016io.reactivex.Completable;
import p016io.reactivex.CompletableObserver;
import p016io.reactivex.CompletableSource;
import p016io.reactivex.disposables.CompositeDisposable;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.internal.disposables.EmptyDisposable;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.completable.CompletableAmb */
public final class CompletableAmb extends Completable {
    private final CompletableSource[] sources;
    private final Iterable<? extends CompletableSource> sourcesIterable;

    public CompletableAmb(CompletableSource[] completableSourceArr, Iterable<? extends CompletableSource> iterable) {
        this.sources = completableSourceArr;
        this.sourcesIterable = iterable;
    }

    public void subscribeActual(final CompletableObserver completableObserver) {
        int i;
        CompletableSource[] completableSourceArr = this.sources;
        if (completableSourceArr == null) {
            completableSourceArr = new CompletableSource[8];
            try {
                i = 0;
                for (CompletableSource completableSource : this.sourcesIterable) {
                    if (completableSource == null) {
                        EmptyDisposable.error((Throwable) new NullPointerException("One of the sources is null"), completableObserver);
                        return;
                    }
                    if (i == completableSourceArr.length) {
                        CompletableSource[] completableSourceArr2 = new CompletableSource[((i >> 2) + i)];
                        System.arraycopy(completableSourceArr, 0, completableSourceArr2, 0, i);
                        completableSourceArr = completableSourceArr2;
                    }
                    int i2 = i + 1;
                    completableSourceArr[i] = completableSource;
                    i = i2;
                }
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                EmptyDisposable.error(th, completableObserver);
                return;
            }
        } else {
            i = completableSourceArr.length;
        }
        final CompositeDisposable compositeDisposable = new CompositeDisposable();
        completableObserver.onSubscribe(compositeDisposable);
        final AtomicBoolean atomicBoolean = new AtomicBoolean();
        C06981 r5 = new CompletableObserver() {
            public void onComplete() {
                if (atomicBoolean.compareAndSet(false, true)) {
                    compositeDisposable.dispose();
                    completableObserver.onComplete();
                }
            }

            public void onError(Throwable th) {
                if (atomicBoolean.compareAndSet(false, true)) {
                    compositeDisposable.dispose();
                    completableObserver.onError(th);
                    return;
                }
                RxJavaPlugins.onError(th);
            }

            public void onSubscribe(Disposable disposable) {
                compositeDisposable.add(disposable);
            }
        };
        int i3 = 0;
        while (i3 < i) {
            CompletableSource completableSource2 = completableSourceArr[i3];
            if (!compositeDisposable.isDisposed()) {
                if (completableSource2 == null) {
                    NullPointerException nullPointerException = new NullPointerException("One of the sources is null");
                    if (atomicBoolean.compareAndSet(false, true)) {
                        compositeDisposable.dispose();
                        completableObserver.onError(nullPointerException);
                    } else {
                        RxJavaPlugins.onError(nullPointerException);
                    }
                    return;
                }
                completableSource2.subscribe(r5);
                i3++;
            } else {
                return;
            }
        }
        if (i == 0) {
            completableObserver.onComplete();
        }
    }
}
