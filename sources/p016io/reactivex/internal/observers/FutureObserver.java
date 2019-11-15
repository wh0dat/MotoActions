package p016io.reactivex.internal.observers;

import java.util.NoSuchElementException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import p016io.reactivex.Observer;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.observers.FutureObserver */
public final class FutureObserver<T> extends CountDownLatch implements Observer<T>, Future<T>, Disposable {
    Throwable error;

    /* renamed from: s */
    final AtomicReference<Disposable> f217s = new AtomicReference<>();
    T value;

    public void dispose() {
    }

    public FutureObserver() {
        super(1);
    }

    public boolean cancel(boolean z) {
        Disposable disposable;
        do {
            disposable = (Disposable) this.f217s.get();
            if (disposable == this || disposable == DisposableHelper.DISPOSED) {
                return false;
            }
        } while (!this.f217s.compareAndSet(disposable, DisposableHelper.DISPOSED));
        if (disposable != null) {
            disposable.dispose();
        }
        countDown();
        return true;
    }

    public boolean isCancelled() {
        return DisposableHelper.isDisposed((Disposable) this.f217s.get());
    }

    public boolean isDone() {
        return getCount() == 0;
    }

    public T get() throws InterruptedException, ExecutionException {
        if (getCount() != 0) {
            await();
        }
        if (isCancelled()) {
            throw new CancellationException();
        }
        Throwable th = this.error;
        if (th == null) {
            return this.value;
        }
        throw new ExecutionException(th);
    }

    public T get(long j, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
        if (getCount() != 0 && !await(j, timeUnit)) {
            throw new TimeoutException();
        } else if (isCancelled()) {
            throw new CancellationException();
        } else {
            Throwable th = this.error;
            if (th == null) {
                return this.value;
            }
            throw new ExecutionException(th);
        }
    }

    public void onSubscribe(Disposable disposable) {
        DisposableHelper.setOnce(this.f217s, disposable);
    }

    public void onNext(T t) {
        if (this.value != null) {
            ((Disposable) this.f217s.get()).dispose();
            onError(new IndexOutOfBoundsException("More than one element received"));
            return;
        }
        this.value = t;
    }

    public void onError(Throwable th) {
        Disposable disposable;
        if (this.error == null) {
            this.error = th;
            do {
                disposable = (Disposable) this.f217s.get();
                if (disposable == this || disposable == DisposableHelper.DISPOSED) {
                    RxJavaPlugins.onError(th);
                    return;
                }
            } while (!this.f217s.compareAndSet(disposable, this));
            countDown();
            return;
        }
        RxJavaPlugins.onError(th);
    }

    public void onComplete() {
        Disposable disposable;
        if (this.value == null) {
            onError(new NoSuchElementException("The source is empty"));
            return;
        }
        do {
            disposable = (Disposable) this.f217s.get();
            if (disposable == this || disposable == DisposableHelper.DISPOSED) {
                return;
            }
        } while (!this.f217s.compareAndSet(disposable, this));
        countDown();
    }

    public boolean isDisposed() {
        return isDone();
    }
}
