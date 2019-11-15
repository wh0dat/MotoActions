package p016io.reactivex.internal.operators.flowable;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.Completable;
import p016io.reactivex.CompletableObserver;
import p016io.reactivex.CompletableSource;
import p016io.reactivex.Flowable;
import p016io.reactivex.disposables.CompositeDisposable;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.Function;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.internal.functions.ObjectHelper;
import p016io.reactivex.internal.fuseable.FuseToFlowable;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;
import p016io.reactivex.internal.util.AtomicThrowable;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableFlatMapCompletableCompletable */
public final class FlowableFlatMapCompletableCompletable<T> extends Completable implements FuseToFlowable<T> {
    final boolean delayErrors;
    final Function<? super T, ? extends CompletableSource> mapper;
    final int maxConcurrency;
    final Publisher<T> source;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableFlatMapCompletableCompletable$FlatMapCompletableMainSubscriber */
    static final class FlatMapCompletableMainSubscriber<T> extends AtomicInteger implements Subscriber<T>, Disposable {
        private static final long serialVersionUID = 8443155186132538303L;
        final CompletableObserver actual;
        final boolean delayErrors;
        final AtomicThrowable errors = new AtomicThrowable();
        final Function<? super T, ? extends CompletableSource> mapper;
        final int maxConcurrency;

        /* renamed from: s */
        Subscription f281s;
        final CompositeDisposable set = new CompositeDisposable();

        /* renamed from: io.reactivex.internal.operators.flowable.FlowableFlatMapCompletableCompletable$FlatMapCompletableMainSubscriber$InnerObserver */
        final class InnerObserver extends AtomicReference<Disposable> implements CompletableObserver, Disposable {
            private static final long serialVersionUID = 8606673141535671828L;

            InnerObserver() {
            }

            public void onSubscribe(Disposable disposable) {
                DisposableHelper.setOnce(this, disposable);
            }

            public void onComplete() {
                FlatMapCompletableMainSubscriber.this.innerComplete(this);
            }

            public void onError(Throwable th) {
                FlatMapCompletableMainSubscriber.this.innerError(this, th);
            }

            public void dispose() {
                DisposableHelper.dispose(this);
            }

            public boolean isDisposed() {
                return DisposableHelper.isDisposed((Disposable) get());
            }
        }

        FlatMapCompletableMainSubscriber(CompletableObserver completableObserver, Function<? super T, ? extends CompletableSource> function, boolean z, int i) {
            this.actual = completableObserver;
            this.mapper = function;
            this.delayErrors = z;
            this.maxConcurrency = i;
            lazySet(1);
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.f281s, subscription)) {
                this.f281s = subscription;
                this.actual.onSubscribe(this);
                int i = this.maxConcurrency;
                if (i == Integer.MAX_VALUE) {
                    subscription.request(LongCompanionObject.MAX_VALUE);
                } else {
                    subscription.request((long) i);
                }
            }
        }

        public void onNext(T t) {
            try {
                CompletableSource completableSource = (CompletableSource) ObjectHelper.requireNonNull(this.mapper.apply(t), "The mapper returned a null CompletableSource");
                getAndIncrement();
                InnerObserver innerObserver = new InnerObserver();
                this.set.add(innerObserver);
                completableSource.subscribe(innerObserver);
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                this.f281s.cancel();
                onError(th);
            }
        }

        public void onError(Throwable th) {
            if (!this.errors.addThrowable(th)) {
                RxJavaPlugins.onError(th);
            } else if (!this.delayErrors) {
                dispose();
                if (getAndSet(0) > 0) {
                    this.actual.onError(this.errors.terminate());
                }
            } else if (decrementAndGet() == 0) {
                this.actual.onError(this.errors.terminate());
            } else if (this.maxConcurrency != Integer.MAX_VALUE) {
                this.f281s.request(1);
            }
        }

        public void onComplete() {
            if (decrementAndGet() == 0) {
                Throwable terminate = this.errors.terminate();
                if (terminate != null) {
                    this.actual.onError(terminate);
                } else {
                    this.actual.onComplete();
                }
            } else if (this.maxConcurrency != Integer.MAX_VALUE) {
                this.f281s.request(1);
            }
        }

        public void dispose() {
            this.f281s.cancel();
            this.set.dispose();
        }

        public boolean isDisposed() {
            return this.set.isDisposed();
        }

        /* access modifiers changed from: 0000 */
        public void innerComplete(InnerObserver innerObserver) {
            this.set.delete(innerObserver);
            onComplete();
        }

        /* access modifiers changed from: 0000 */
        public void innerError(InnerObserver innerObserver, Throwable th) {
            this.set.delete(innerObserver);
            onError(th);
        }
    }

    public FlowableFlatMapCompletableCompletable(Publisher<T> publisher, Function<? super T, ? extends CompletableSource> function, boolean z, int i) {
        this.source = publisher;
        this.mapper = function;
        this.delayErrors = z;
        this.maxConcurrency = i;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver completableObserver) {
        this.source.subscribe(new FlatMapCompletableMainSubscriber(completableObserver, this.mapper, this.delayErrors, this.maxConcurrency));
    }

    public Flowable<T> fuseToFlowable() {
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableFlatMapCompletable<T>(this.source, this.mapper, this.delayErrors, this.maxConcurrency));
    }
}
