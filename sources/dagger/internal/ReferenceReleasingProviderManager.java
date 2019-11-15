package dagger.internal;

import dagger.releasablereferences.ReleasableReferenceManager;
import java.lang.annotation.Annotation;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@GwtIncompatible
public final class ReferenceReleasingProviderManager implements ReleasableReferenceManager {
    private final Queue<WeakReference<ReferenceReleasingProvider<?>>> providers = new ConcurrentLinkedQueue();
    private final Class<? extends Annotation> scope;

    private enum Operation {
        RELEASE {
            /* access modifiers changed from: 0000 */
            public void execute(ReferenceReleasingProvider<?> referenceReleasingProvider) {
                referenceReleasingProvider.releaseStrongReference();
            }
        },
        RESTORE {
            /* access modifiers changed from: 0000 */
            public void execute(ReferenceReleasingProvider<?> referenceReleasingProvider) {
                referenceReleasingProvider.restoreStrongReference();
            }
        };

        /* access modifiers changed from: 0000 */
        public abstract void execute(ReferenceReleasingProvider<?> referenceReleasingProvider);
    }

    public ReferenceReleasingProviderManager(Class<? extends Annotation> cls) {
        this.scope = (Class) Preconditions.checkNotNull(cls);
    }

    public void addProvider(ReferenceReleasingProvider<?> referenceReleasingProvider) {
        this.providers.add(new WeakReference(referenceReleasingProvider));
    }

    public Class<? extends Annotation> scope() {
        return this.scope;
    }

    public void releaseStrongReferences() {
        execute(Operation.RELEASE);
    }

    public void restoreStrongReferences() {
        execute(Operation.RESTORE);
    }

    private void execute(Operation operation) {
        Iterator it = this.providers.iterator();
        while (it.hasNext()) {
            ReferenceReleasingProvider referenceReleasingProvider = (ReferenceReleasingProvider) ((WeakReference) it.next()).get();
            if (referenceReleasingProvider == null) {
                it.remove();
            } else {
                operation.execute(referenceReleasingProvider);
            }
        }
    }
}
