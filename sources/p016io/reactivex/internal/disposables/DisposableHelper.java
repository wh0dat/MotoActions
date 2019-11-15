package p016io.reactivex.internal.disposables;

import java.util.concurrent.atomic.AtomicReference;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.functions.ObjectHelper;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.disposables.DisposableHelper */
public enum DisposableHelper implements Disposable {
    DISPOSED;

    public void dispose() {
    }

    public boolean isDisposed() {
        return true;
    }

    public static boolean isDisposed(Disposable disposable) {
        return disposable == DISPOSED;
    }

    public static boolean set(AtomicReference<Disposable> atomicReference, Disposable disposable) {
        Disposable disposable2;
        do {
            disposable2 = (Disposable) atomicReference.get();
            if (disposable2 == DISPOSED) {
                if (disposable != null) {
                    disposable.dispose();
                }
                return false;
            }
        } while (!atomicReference.compareAndSet(disposable2, disposable));
        if (disposable2 != null) {
            disposable2.dispose();
        }
        return true;
    }

    public static boolean setOnce(AtomicReference<Disposable> atomicReference, Disposable disposable) {
        ObjectHelper.requireNonNull(disposable, "d is null");
        if (atomicReference.compareAndSet(null, disposable)) {
            return true;
        }
        disposable.dispose();
        if (atomicReference.get() != DISPOSED) {
            reportDisposableSet();
        }
        return false;
    }

    public static boolean replace(AtomicReference<Disposable> atomicReference, Disposable disposable) {
        Disposable disposable2;
        do {
            disposable2 = (Disposable) atomicReference.get();
            if (disposable2 == DISPOSED) {
                if (disposable != null) {
                    disposable.dispose();
                }
                return false;
            }
        } while (!atomicReference.compareAndSet(disposable2, disposable));
        return true;
    }

    public static boolean dispose(AtomicReference<Disposable> atomicReference) {
        Disposable disposable = (Disposable) atomicReference.get();
        DisposableHelper disposableHelper = DISPOSED;
        if (disposable != disposableHelper) {
            Disposable disposable2 = (Disposable) atomicReference.getAndSet(disposableHelper);
            if (disposable2 != disposableHelper) {
                if (disposable2 != null) {
                    disposable2.dispose();
                }
                return true;
            }
        }
        return false;
    }

    public static boolean validate(Disposable disposable, Disposable disposable2) {
        if (disposable2 == null) {
            RxJavaPlugins.onError(new NullPointerException("next is null"));
            return false;
        } else if (disposable == null) {
            return true;
        } else {
            disposable2.dispose();
            reportDisposableSet();
            return false;
        }
    }

    public static void reportDisposableSet() {
        RxJavaPlugins.onError(new IllegalStateException("Disposable already set!"));
    }
}
