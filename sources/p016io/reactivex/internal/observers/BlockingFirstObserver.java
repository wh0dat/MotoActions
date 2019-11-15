package p016io.reactivex.internal.observers;

/* renamed from: io.reactivex.internal.observers.BlockingFirstObserver */
public final class BlockingFirstObserver<T> extends BlockingBaseObserver<T> {
    public void onNext(T t) {
        if (this.value == null) {
            this.value = t;
            this.f212d.dispose();
            countDown();
        }
    }

    public void onError(Throwable th) {
        if (this.value == null) {
            this.error = th;
        }
        countDown();
    }
}
