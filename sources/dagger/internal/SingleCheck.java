package dagger.internal;

import dagger.Lazy;
import javax.inject.Provider;

public final class SingleCheck<T> implements Provider<T>, Lazy<T> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final Object UNINITIALIZED = new Object();
    private volatile Object instance = UNINITIALIZED;
    private volatile Provider<T> provider;

    private SingleCheck(Provider<T> provider2) {
        this.provider = provider2;
    }

    public T get() {
        Provider<T> provider2 = this.provider;
        if (this.instance == UNINITIALIZED) {
            this.instance = provider2.get();
            this.provider = null;
        }
        return this.instance;
    }

    public static <T> Provider<T> provider(Provider<T> provider2) {
        return ((provider2 instanceof SingleCheck) || (provider2 instanceof DoubleCheck)) ? provider2 : new SingleCheck((Provider) Preconditions.checkNotNull(provider2));
    }
}
