package kotlin.p017io;

import java.io.Closeable;
import kotlin.ExceptionsKt;
import kotlin.Metadata;
import kotlin.PublishedApi;
import kotlin.SinceKotlin;
import kotlin.jvm.JvmName;
import org.jetbrains.annotations.Nullable;

@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000\u001c\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a\u0018\u0010\u0000\u001a\u00020\u0001*\u0004\u0018\u00010\u00022\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004H\u0001\u001a;\u0010\u0005\u001a\u0002H\u0006\"\n\b\u0000\u0010\u0007*\u0004\u0018\u00010\u0002\"\u0004\b\u0001\u0010\u0006*\u0002H\u00072\u0012\u0010\b\u001a\u000e\u0012\u0004\u0012\u0002H\u0007\u0012\u0004\u0012\u0002H\u00060\tH\bø\u0001\u0000¢\u0006\u0002\u0010\u000b\u0002\b\n\u0006\b\u0011(\n0\u0001¨\u0006\f"}, mo14495d2 = {"closeFinally", "", "Ljava/io/Closeable;", "cause", "", "use", "R", "T", "block", "Lkotlin/Function1;", "Requires newer compiler version to be inlined correctly.", "(Ljava/io/Closeable;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "kotlin-stdlib"}, mo14496k = 2, mo14497mv = {1, 1, 10})
@JvmName(name = "CloseableKt")
/* renamed from: kotlin.io.CloseableKt */
/* compiled from: Closeable.kt */
public final class CloseableKt {
    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0020, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0024, code lost:
        kotlin.jvm.internal.InlineMarker.finallyStart(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x002b, code lost:
        if (kotlin.internal.PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0) != false) goto L_0x002d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x002d, code lost:
        closeFinally(r3, r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0031, code lost:
        if (r3 != null) goto L_0x0034;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0034, code lost:
        if (r0 == null) goto L_0x0036;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0036, code lost:
        r3.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
        r3.close();
     */
    @kotlin.internal.InlineOnly
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static final <T extends java.io.Closeable, R> R use(T r3, kotlin.jvm.functions.Function1<? super T, ? extends R> r4) {
        /*
            r0 = 0
            java.lang.Throwable r0 = (java.lang.Throwable) r0
            r1 = 0
            r2 = 1
            java.lang.Object r4 = r4.invoke(r3)     // Catch:{ Throwable -> 0x0022 }
            kotlin.jvm.internal.InlineMarker.finallyStart(r2)
            boolean r1 = kotlin.internal.PlatformImplementationsKt.apiVersionIsAtLeast(r2, r2, r1)
            if (r1 == 0) goto L_0x0016
            closeFinally(r3, r0)
            goto L_0x001c
        L_0x0016:
            if (r3 != 0) goto L_0x0019
            goto L_0x001c
        L_0x0019:
            r3.close()
        L_0x001c:
            kotlin.jvm.internal.InlineMarker.finallyEnd(r2)
            return r4
        L_0x0020:
            r4 = move-exception
            goto L_0x0024
        L_0x0022:
            r0 = move-exception
            throw r0     // Catch:{ all -> 0x0020 }
        L_0x0024:
            kotlin.jvm.internal.InlineMarker.finallyStart(r2)
            boolean r1 = kotlin.internal.PlatformImplementationsKt.apiVersionIsAtLeast(r2, r2, r1)
            if (r1 == 0) goto L_0x0031
            closeFinally(r3, r0)
            goto L_0x003d
        L_0x0031:
            if (r3 != 0) goto L_0x0034
            goto L_0x003d
        L_0x0034:
            if (r0 != 0) goto L_0x003a
            r3.close()
            goto L_0x003d
        L_0x003a:
            r3.close()     // Catch:{ Throwable -> 0x003d }
        L_0x003d:
            kotlin.jvm.internal.InlineMarker.finallyEnd(r2)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.p017io.CloseableKt.use(java.io.Closeable, kotlin.jvm.functions.Function1):java.lang.Object");
    }

    @SinceKotlin(version = "1.1")
    @PublishedApi
    public static final void closeFinally(@Nullable Closeable closeable, @Nullable Throwable th) {
        if (closeable != null) {
            if (th == null) {
                closeable.close();
                return;
            }
            try {
                closeable.close();
            } catch (Throwable th2) {
                ExceptionsKt.addSuppressed(th, th2);
            }
        }
    }
}
