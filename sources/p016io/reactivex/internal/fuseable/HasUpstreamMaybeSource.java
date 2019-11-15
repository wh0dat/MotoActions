package p016io.reactivex.internal.fuseable;

import p016io.reactivex.MaybeSource;

/* renamed from: io.reactivex.internal.fuseable.HasUpstreamMaybeSource */
public interface HasUpstreamMaybeSource<T> {
    MaybeSource<T> source();
}
