package p016io.reactivex.internal.observers;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import p016io.reactivex.CompletableObserver;
import p016io.reactivex.MaybeObserver;
import p016io.reactivex.SingleObserver;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.util.ExceptionHelper;

/* renamed from: io.reactivex.internal.observers.BlockingMultiObserver */
public final class BlockingMultiObserver<T> extends CountDownLatch implements SingleObserver<T>, CompletableObserver, MaybeObserver<T> {
    volatile boolean cancelled;

    /* renamed from: d */
    Disposable f213d;
    Throwable error;
    T value;

    public BlockingMultiObserver() {
        super(1);
    }

    /* access modifiers changed from: 0000 */
    public void dispose() {
        this.cancelled = true;
        Disposable disposable = this.f213d;
        if (disposable != null) {
            disposable.dispose();
        }
    }

    public void onSubscribe(Disposable disposable) {
        this.f213d = disposable;
        if (this.cancelled) {
            disposable.dispose();
        }
    }

    public void onSuccess(T t) {
        this.value = t;
        countDown();
    }

    public void onError(Throwable th) {
        this.error = th;
        countDown();
    }

    public void onComplete() {
        countDown();
    }

    public T blockingGet() {
        if (getCount() != 0) {
            try {
                await();
            } catch (InterruptedException e) {
                dispose();
                throw ExceptionHelper.wrapOrThrow(e);
            }
        }
        Throwable th = this.error;
        if (th == null) {
            return this.value;
        }
        throw ExceptionHelper.wrapOrThrow(th);
    }

    public T blockingGet(T t) {
        if (getCount() != 0) {
            try {
                await();
            } catch (InterruptedException e) {
                dispose();
                throw ExceptionHelper.wrapOrThrow(e);
            }
        }
        Throwable th = this.error;
        if (th != null) {
            throw ExceptionHelper.wrapOrThrow(th);
        }
        T t2 = this.value;
        return t2 != null ? t2 : t;
    }

    public Throwable blockingGetError() {
        if (getCount() != 0) {
            try {
                await();
            } catch (InterruptedException e) {
                dispose();
                return e;
            }
        }
        return this.error;
    }

    public Throwable blockingGetError(long j, TimeUnit timeUnit) {
        if (getCount() != 0) {
            try {
                if (!await(j, timeUnit)) {
                    dispose();
                    throw ExceptionHelper.wrapOrThrow(new TimeoutException());
                }
            } catch (InterruptedException e) {
                dispose();
                throw ExceptionHelper.wrapOrThrow(e);
            }
        }
        return this.error;
    }

    public boolean blockingAwait(long j, TimeUnit timeUnit) {
        if (getCount() != 0) {
            try {
                if (!await(j, timeUnit)) {
                    dispose();
                    return false;
                }
            } catch (InterruptedException e) {
                dispose();
                throw ExceptionHelper.wrapOrThrow(e);
            }
        }
        Throwable th = this.error;
        if (th == null) {
            return true;
        }
        throw ExceptionHelper.wrapOrThrow(th);
    }
}
