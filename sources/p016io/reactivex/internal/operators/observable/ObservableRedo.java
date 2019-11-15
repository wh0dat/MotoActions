package p016io.reactivex.internal.operators.observable;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import p016io.reactivex.Notification;
import p016io.reactivex.Observable;
import p016io.reactivex.ObservableSource;
import p016io.reactivex.Observer;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.Consumer;
import p016io.reactivex.functions.Function;
import p016io.reactivex.internal.disposables.ListCompositeDisposable;
import p016io.reactivex.internal.disposables.SequentialDisposable;
import p016io.reactivex.internal.functions.ObjectHelper;
import p016io.reactivex.internal.observers.ToNotificationObserver;
import p016io.reactivex.subjects.BehaviorSubject;
import p016io.reactivex.subjects.Subject;

/* renamed from: io.reactivex.internal.operators.observable.ObservableRedo */
public final class ObservableRedo<T> extends AbstractObservableWithUpstream<T, T> {
    final Function<? super Observable<Notification<Object>>, ? extends ObservableSource<?>> manager;
    final boolean retryMode;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableRedo$RedoObserver */
    static final class RedoObserver<T> extends AtomicBoolean implements Observer<T> {
        private static final long serialVersionUID = -1151903143112844287L;
        final Observer<? super T> actual;
        final SequentialDisposable arbiter;
        final boolean retryMode;
        final ObservableSource<? extends T> source;
        final Subject<Notification<Object>> subject;
        final AtomicInteger wip = new AtomicInteger();

        RedoObserver(Observer<? super T> observer, Subject<Notification<Object>> subject2, ObservableSource<? extends T> observableSource, boolean z) {
            this.actual = observer;
            this.subject = subject2;
            this.source = observableSource;
            this.arbiter = new SequentialDisposable();
            this.retryMode = z;
            lazySet(true);
        }

        public void onSubscribe(Disposable disposable) {
            this.arbiter.replace(disposable);
        }

        public void onNext(T t) {
            this.actual.onNext(t);
        }

        public void onError(Throwable th) {
            if (!compareAndSet(false, true)) {
                return;
            }
            if (this.retryMode) {
                this.subject.onNext(Notification.createOnError(th));
            } else {
                this.subject.onError(th);
            }
        }

        public void onComplete() {
            if (!compareAndSet(false, true)) {
                return;
            }
            if (this.retryMode) {
                this.subject.onComplete();
            } else {
                this.subject.onNext(Notification.createOnComplete());
            }
        }

        /* access modifiers changed from: 0000 */
        public void handle(Notification<Object> notification) {
            int i = 1;
            if (compareAndSet(true, false)) {
                if (notification.isOnError()) {
                    this.arbiter.dispose();
                    this.actual.onError(notification.getError());
                } else if (!notification.isOnNext()) {
                    this.arbiter.dispose();
                    this.actual.onComplete();
                } else if (this.wip.getAndIncrement() == 0) {
                    while (!this.arbiter.isDisposed()) {
                        this.source.subscribe(this);
                        i = this.wip.addAndGet(-i);
                        if (i == 0) {
                        }
                    }
                }
            }
        }
    }

    public ObservableRedo(ObservableSource<T> observableSource, Function<? super Observable<Notification<Object>>, ? extends ObservableSource<?>> function, boolean z) {
        super(observableSource);
        this.manager = function;
        this.retryMode = z;
    }

    public void subscribeActual(Observer<? super T> observer) {
        Subject serialized = BehaviorSubject.create().toSerialized();
        final RedoObserver redoObserver = new RedoObserver(observer, serialized, this.source, this.retryMode);
        ToNotificationObserver toNotificationObserver = new ToNotificationObserver(new Consumer<Notification<Object>>() {
            public void accept(Notification<Object> notification) {
                redoObserver.handle(notification);
            }
        });
        observer.onSubscribe(new ListCompositeDisposable(redoObserver.arbiter, toNotificationObserver));
        try {
            ((ObservableSource) ObjectHelper.requireNonNull(this.manager.apply(serialized), "The function returned a null ObservableSource")).subscribe(toNotificationObserver);
            redoObserver.handle(Notification.createOnNext(Integer.valueOf(0)));
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            observer.onError(th);
        }
    }
}
