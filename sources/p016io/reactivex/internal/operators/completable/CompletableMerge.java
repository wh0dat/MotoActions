package p016io.reactivex.internal.operators.completable;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.Completable;
import p016io.reactivex.CompletableObserver;
import p016io.reactivex.CompletableSource;
import p016io.reactivex.disposables.CompositeDisposable;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;
import p016io.reactivex.internal.util.AtomicThrowable;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.completable.CompletableMerge */
public final class CompletableMerge extends Completable {
    final boolean delayErrors;
    final int maxConcurrency;
    final Publisher<? extends CompletableSource> source;

    /* renamed from: io.reactivex.internal.operators.completable.CompletableMerge$CompletableMergeSubscriber */
    static final class CompletableMergeSubscriber extends AtomicInteger implements Subscriber<CompletableSource>, Disposable {
        private static final long serialVersionUID = -2108443387387077490L;
        final CompletableObserver actual;
        final boolean delayErrors;
        final AtomicThrowable error = new AtomicThrowable();
        final int maxConcurrency;

        /* renamed from: s */
        Subscription f239s;
        final CompositeDisposable set = new CompositeDisposable();

        /* renamed from: io.reactivex.internal.operators.completable.CompletableMerge$CompletableMergeSubscriber$MergeInnerObserver */
        final class MergeInnerObserver extends AtomicReference<Disposable> implements CompletableObserver, Disposable {
            private static final long serialVersionUID = 251330541679988317L;

            MergeInnerObserver() {
            }

            public void onSubscribe(Disposable disposable) {
                DisposableHelper.setOnce(this, disposable);
            }

            public void onError(Throwable th) {
                CompletableMergeSubscriber.this.innerError(this, th);
            }

            public void onComplete() {
                CompletableMergeSubscriber.this.innerComplete(this);
            }

            public boolean isDisposed() {
                return DisposableHelper.isDisposed((Disposable) get());
            }

            public void dispose() {
                DisposableHelper.dispose(this);
            }
        }

        CompletableMergeSubscriber(CompletableObserver completableObserver, int i, boolean z) {
            this.actual = completableObserver;
            this.maxConcurrency = i;
            this.delayErrors = z;
            lazySet(1);
        }

        public void dispose() {
            this.f239s.cancel();
            this.set.dispose();
        }

        public boolean isDisposed() {
            return this.set.isDisposed();
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.f239s, subscription)) {
                this.f239s = subscription;
                this.actual.onSubscribe(this);
                if (this.maxConcurrency == Integer.MAX_VALUE) {
                    subscription.request(LongCompanionObject.MAX_VALUE);
                } else {
                    subscription.request((long) this.maxConcurrency);
                }
            }
        }

        public void onNext(CompletableSource completableSource) {
            getAndIncrement();
            MergeInnerObserver mergeInnerObserver = new MergeInnerObserver();
            this.set.add(mergeInnerObserver);
            completableSource.subscribe(mergeInnerObserver);
        }

        public void onError(Throwable th) {
            if (!this.delayErrors) {
                this.set.dispose();
                if (!this.error.addThrowable(th)) {
                    RxJavaPlugins.onError(th);
                } else if (getAndSet(0) > 0) {
                    this.actual.onError(this.error.terminate());
                }
            } else if (!this.error.addThrowable(th)) {
                RxJavaPlugins.onError(th);
            } else if (decrementAndGet() == 0) {
                this.actual.onError(this.error.terminate());
            }
        }

        public void onComplete() {
            if (decrementAndGet() != 0) {
                return;
            }
            if (((Throwable) this.error.get()) != null) {
                this.actual.onError(this.error.terminate());
            } else {
                this.actual.onComplete();
            }
        }

        /* access modifiers changed from: 0000 */
        public void innerError(MergeInnerObserver mergeInnerObserver, Throwable th) {
            this.set.delete(mergeInnerObserver);
            if (!this.delayErrors) {
                this.f239s.cancel();
                this.set.dispose();
                if (!this.error.addThrowable(th)) {
                    RxJavaPlugins.onError(th);
                } else if (getAndSet(0) > 0) {
                    this.actual.onError(this.error.terminate());
                }
            } else if (!this.error.addThrowable(th)) {
                RxJavaPlugins.onError(th);
            } else if (decrementAndGet() == 0) {
                this.actual.onError(this.error.terminate());
            } else if (this.maxConcurrency != Integer.MAX_VALUE) {
                this.f239s.request(1);
            }
        }

        /* access modifiers changed from: 0000 */
        public void innerComplete(MergeInnerObserver mergeInnerObserver) {
            this.set.delete(mergeInnerObserver);
            if (decrementAndGet() == 0) {
                Throwable th = (Throwable) this.error.get();
                if (th != null) {
                    this.actual.onError(th);
                } else {
                    this.actual.onComplete();
                }
            } else if (this.maxConcurrency != Integer.MAX_VALUE) {
                this.f239s.request(1);
            }
        }
    }

    public CompletableMerge(Publisher<? extends CompletableSource> publisher, int i, boolean z) {
        this.source = publisher;
        this.maxConcurrency = i;
        this.delayErrors = z;
    }

    public void subscribeActual(CompletableObserver completableObserver) {
        this.source.subscribe(new CompletableMergeSubscriber(completableObserver, this.maxConcurrency, this.delayErrors));
    }
}
