package p016io.reactivex.internal.operators.observable;

import p016io.reactivex.ObservableSource;
import p016io.reactivex.Observer;
import p016io.reactivex.annotations.Experimental;
import p016io.reactivex.functions.Consumer;
import p016io.reactivex.internal.observers.BasicFuseableObserver;

@Experimental
/* renamed from: io.reactivex.internal.operators.observable.ObservableDoAfterNext */
public final class ObservableDoAfterNext<T> extends AbstractObservableWithUpstream<T, T> {
    final Consumer<? super T> onAfterNext;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableDoAfterNext$DoAfterObserver */
    static final class DoAfterObserver<T> extends BasicFuseableObserver<T, T> {
        final Consumer<? super T> onAfterNext;

        DoAfterObserver(Observer<? super T> observer, Consumer<? super T> consumer) {
            super(observer);
            this.onAfterNext = consumer;
        }

        public void onNext(T t) {
            this.actual.onNext(t);
            if (this.sourceMode == 0) {
                try {
                    this.onAfterNext.accept(t);
                } catch (Throwable th) {
                    fail(th);
                }
            }
        }

        public int requestFusion(int i) {
            return transitiveBoundaryFusion(i);
        }

        public T poll() throws Exception {
            T poll = this.f210qs.poll();
            if (poll != null) {
                this.onAfterNext.accept(poll);
            }
            return poll;
        }
    }

    public ObservableDoAfterNext(ObservableSource<T> observableSource, Consumer<? super T> consumer) {
        super(observableSource);
        this.onAfterNext = consumer;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super T> observer) {
        this.source.subscribe(new DoAfterObserver(observer, this.onAfterNext));
    }
}
