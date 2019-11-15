package p016io.reactivex.internal.operators.completable;

import java.util.concurrent.atomic.AtomicReference;
import p016io.reactivex.Completable;
import p016io.reactivex.CompletableEmitter;
import p016io.reactivex.CompletableObserver;
import p016io.reactivex.CompletableOnSubscribe;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.Cancellable;
import p016io.reactivex.internal.disposables.CancellableDisposable;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.completable.CompletableCreate */
public final class CompletableCreate extends Completable {
    final CompletableOnSubscribe source;

    /* renamed from: io.reactivex.internal.operators.completable.CompletableCreate$Emitter */
    static final class Emitter extends AtomicReference<Disposable> implements CompletableEmitter, Disposable {
        private static final long serialVersionUID = -2467358622224974244L;
        final CompletableObserver actual;

        Emitter(CompletableObserver completableObserver) {
            this.actual = completableObserver;
        }

        public void onComplete() {
            if (get() != DisposableHelper.DISPOSED) {
                Disposable disposable = (Disposable) getAndSet(DisposableHelper.DISPOSED);
                if (disposable != DisposableHelper.DISPOSED) {
                    try {
                        this.actual.onComplete();
                    } finally {
                        if (disposable != null) {
                            disposable.dispose();
                        }
                    }
                }
            }
        }

        public void onError(Throwable th) {
            if (th == null) {
                th = new NullPointerException("onError called with null. Null values are generally not allowed in 2.x operators and sources.");
            }
            if (get() != DisposableHelper.DISPOSED) {
                Disposable disposable = (Disposable) getAndSet(DisposableHelper.DISPOSED);
                if (disposable != DisposableHelper.DISPOSED) {
                    try {
                        this.actual.onError(th);
                        return;
                    } finally {
                        if (disposable != null) {
                            disposable.dispose();
                        }
                    }
                }
            }
            RxJavaPlugins.onError(th);
        }

        public void setDisposable(Disposable disposable) {
            DisposableHelper.set(this, disposable);
        }

        public void setCancellable(Cancellable cancellable) {
            setDisposable(new CancellableDisposable(cancellable));
        }

        public void dispose() {
            DisposableHelper.dispose(this);
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) get());
        }
    }

    public CompletableCreate(CompletableOnSubscribe completableOnSubscribe) {
        this.source = completableOnSubscribe;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver completableObserver) {
        Emitter emitter = new Emitter(completableObserver);
        completableObserver.onSubscribe(emitter);
        try {
            this.source.subscribe(emitter);
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            emitter.onError(th);
        }
    }
}
