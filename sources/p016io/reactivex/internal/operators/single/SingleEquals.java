package p016io.reactivex.internal.operators.single;

import java.util.concurrent.atomic.AtomicInteger;
import p016io.reactivex.Single;
import p016io.reactivex.SingleObserver;
import p016io.reactivex.SingleSource;
import p016io.reactivex.disposables.CompositeDisposable;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.functions.ObjectHelper;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.single.SingleEquals */
public final class SingleEquals<T> extends Single<Boolean> {
    final SingleSource<? extends T> first;
    final SingleSource<? extends T> second;

    public SingleEquals(SingleSource<? extends T> singleSource, SingleSource<? extends T> singleSource2) {
        this.first = singleSource;
        this.second = singleSource2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super Boolean> singleObserver) {
        AtomicInteger atomicInteger = new AtomicInteger();
        Object[] objArr = {null, null};
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        singleObserver.onSubscribe(compositeDisposable);
        SingleSource<? extends T> singleSource = this.first;
        CompositeDisposable compositeDisposable2 = compositeDisposable;
        Object[] objArr2 = objArr;
        AtomicInteger atomicInteger2 = atomicInteger;
        SingleObserver<? super Boolean> singleObserver2 = singleObserver;
        AnonymousClass1InnerObserver r0 = new SingleObserver<T>(0, compositeDisposable2, objArr2, atomicInteger2, singleObserver2) {
            final int index;
            final /* synthetic */ AtomicInteger val$count;
            final /* synthetic */ SingleObserver val$s;
            final /* synthetic */ CompositeDisposable val$set;
            final /* synthetic */ Object[] val$values;

            {
                this.val$set = r3;
                this.val$values = r4;
                this.val$count = r5;
                this.val$s = r6;
                this.index = r2;
            }

            public void onSubscribe(Disposable disposable) {
                this.val$set.add(disposable);
            }

            public void onSuccess(T t) {
                this.val$values[this.index] = t;
                if (this.val$count.incrementAndGet() == 2) {
                    this.val$s.onSuccess(Boolean.valueOf(ObjectHelper.equals(this.val$values[0], this.val$values[1])));
                }
            }

            public void onError(Throwable th) {
                int i;
                do {
                    i = this.val$count.get();
                    if (i >= 2) {
                        RxJavaPlugins.onError(th);
                        return;
                    }
                } while (!this.val$count.compareAndSet(i, 2));
                this.val$set.dispose();
                this.val$s.onError(th);
            }
        };
        singleSource.subscribe(r0);
        SingleSource<? extends T> singleSource2 = this.second;
        AnonymousClass1InnerObserver r02 = new SingleObserver<T>(1, compositeDisposable2, objArr2, atomicInteger2, singleObserver2) {
            final int index;
            final /* synthetic */ AtomicInteger val$count;
            final /* synthetic */ SingleObserver val$s;
            final /* synthetic */ CompositeDisposable val$set;
            final /* synthetic */ Object[] val$values;

            {
                this.val$set = r3;
                this.val$values = r4;
                this.val$count = r5;
                this.val$s = r6;
                this.index = r2;
            }

            public void onSubscribe(Disposable disposable) {
                this.val$set.add(disposable);
            }

            public void onSuccess(T t) {
                this.val$values[this.index] = t;
                if (this.val$count.incrementAndGet() == 2) {
                    this.val$s.onSuccess(Boolean.valueOf(ObjectHelper.equals(this.val$values[0], this.val$values[1])));
                }
            }

            public void onError(Throwable th) {
                int i;
                do {
                    i = this.val$count.get();
                    if (i >= 2) {
                        RxJavaPlugins.onError(th);
                        return;
                    }
                } while (!this.val$count.compareAndSet(i, 2));
                this.val$set.dispose();
                this.val$s.onError(th);
            }
        };
        singleSource2.subscribe(r02);
    }
}
