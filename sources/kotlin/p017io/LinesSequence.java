package kotlin.p017io;

import java.io.BufferedReader;
import java.util.Iterator;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import org.jetbrains.annotations.NotNull;

@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010(\n\u0000\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\u000f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00020\u0007H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000¨\u0006\b"}, mo14495d2 = {"Lkotlin/io/LinesSequence;", "Lkotlin/sequences/Sequence;", "", "reader", "Ljava/io/BufferedReader;", "(Ljava/io/BufferedReader;)V", "iterator", "", "kotlin-stdlib"}, mo14496k = 1, mo14497mv = {1, 1, 10})
/* renamed from: kotlin.io.LinesSequence */
/* compiled from: ReadWrite.kt */
final class LinesSequence implements Sequence<String> {
    /* access modifiers changed from: private */
    public final BufferedReader reader;

    public LinesSequence(@NotNull BufferedReader bufferedReader) {
        Intrinsics.checkParameterIsNotNull(bufferedReader, "reader");
        this.reader = bufferedReader;
    }

    @NotNull
    public Iterator<String> iterator() {
        return new LinesSequence$iterator$1<>(this);
    }
}
