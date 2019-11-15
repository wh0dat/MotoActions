package kotlin.collections;

import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.SequenceBuilder;
import kotlin.coroutines.experimental.jvm.internal.CoroutineImpl;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000\u0014\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00040\u0003H\n¢\u0006\u0004\b\u0005\u0010\u0006"}, mo14495d2 = {"<anonymous>", "", "T", "Lkotlin/coroutines/experimental/SequenceBuilder;", "", "invoke", "(Lkotlin/coroutines/experimental/SequenceBuilder;Lkotlin/coroutines/experimental/Continuation;)Ljava/lang/Object;"}, mo14496k = 3, mo14497mv = {1, 1, 10})
/* compiled from: SlidingWindow.kt */
final class SlidingWindowKt$windowedIterator$1 extends CoroutineImpl implements Function2<SequenceBuilder<? super List<? extends T>>, Continuation<? super Unit>, Object> {
    final /* synthetic */ Iterator $iterator;
    final /* synthetic */ boolean $partialWindows;
    final /* synthetic */ boolean $reuseBuffer;
    final /* synthetic */ int $size;
    final /* synthetic */ int $step;
    int I$0;
    int I$1;
    Object L$0;
    Object L$1;
    Object L$2;
    Object L$3;

    /* renamed from: p$ */
    private SequenceBuilder f572p$;

    SlidingWindowKt$windowedIterator$1(int i, int i2, Iterator it, boolean z, boolean z2, Continuation continuation) {
        this.$step = i;
        this.$size = i2;
        this.$iterator = it;
        this.$reuseBuffer = z;
        this.$partialWindows = z2;
        super(2, continuation);
    }

    @NotNull
    public final Continuation<Unit> create(@NotNull SequenceBuilder<? super List<? extends T>> sequenceBuilder, @NotNull Continuation<? super Unit> continuation) {
        Intrinsics.checkParameterIsNotNull(sequenceBuilder, "$receiver");
        Intrinsics.checkParameterIsNotNull(continuation, "continuation");
        SlidingWindowKt$windowedIterator$1 slidingWindowKt$windowedIterator$1 = new SlidingWindowKt$windowedIterator$1(this.$step, this.$size, this.$iterator, this.$reuseBuffer, this.$partialWindows, continuation);
        slidingWindowKt$windowedIterator$1.f572p$ = sequenceBuilder;
        return slidingWindowKt$windowedIterator$1;
    }

    @Nullable
    public final Object invoke(@NotNull SequenceBuilder<? super List<? extends T>> sequenceBuilder, @NotNull Continuation<? super Unit> continuation) {
        Intrinsics.checkParameterIsNotNull(sequenceBuilder, "$receiver");
        Intrinsics.checkParameterIsNotNull(continuation, "continuation");
        return ((SlidingWindowKt$windowedIterator$1) create(sequenceBuilder, continuation)).doResume(Unit.INSTANCE, null);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:26:0x007d, code lost:
        if (r0.hasNext() == false) goto L_0x00b9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x007f, code lost:
        r5 = r0.next();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0083, code lost:
        if (r3 <= 0) goto L_0x0088;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0085, code lost:
        r3 = r3 - 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0088, code lost:
        r2.add(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0091, code lost:
        if (r2.size() != r9.$size) goto L_0x0079;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0093, code lost:
        r9.L$0 = r4;
        r9.I$0 = r10;
        r9.L$1 = r2;
        r9.I$1 = r3;
        r9.L$2 = r5;
        r9.L$3 = r0;
        r9.label = 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x00a5, code lost:
        if (r4.yield(r2, r9) != r11) goto L_0x00a8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x00a7, code lost:
        return r11;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00aa, code lost:
        if (r9.$reuseBuffer == false) goto L_0x00b0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x00ac, code lost:
        r2.clear();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x00b0, code lost:
        r2 = new java.util.ArrayList(r9.$size);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x00b7, code lost:
        r3 = r10;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x00c1, code lost:
        if ((!r2.isEmpty()) == false) goto L_0x017a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x00c5, code lost:
        if (r9.$partialWindows != false) goto L_0x00cf;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x00cd, code lost:
        if (r2.size() != r9.$size) goto L_0x017a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x00cf, code lost:
        r9.I$0 = r10;
        r9.L$0 = r2;
        r9.I$1 = r3;
        r9.label = 2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x00dc, code lost:
        if (r4.yield(r2, r9) != r11) goto L_0x017a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x00de, code lost:
        return r11;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x00f0, code lost:
        if (r0.hasNext() == false) goto L_0x012b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x00f2, code lost:
        r11 = r0.next();
        r2.add(r11);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x00fd, code lost:
        if (r2.isFull() == false) goto L_0x00ec;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x0101, code lost:
        if (r9.$reuseBuffer == false) goto L_0x0107;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x0103, code lost:
        r5 = r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x0107, code lost:
        r5 = new java.util.ArrayList(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x0111, code lost:
        r9.L$0 = r4;
        r9.I$0 = r3;
        r9.L$1 = r2;
        r9.L$2 = r11;
        r9.L$3 = r0;
        r9.label = 3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x0122, code lost:
        if (r4.yield(r5, r9) != r10) goto L_0x0125;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x0124, code lost:
        return r10;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x0125, code lost:
        r2.removeFirst(r9.$step);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:0x012d, code lost:
        if (r9.$partialWindows == false) goto L_0x017a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x012f, code lost:
        r0 = r2;
        r2 = r3;
        r3 = r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:0x0138, code lost:
        if (r0.size() <= r9.$step) goto L_0x0162;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:68:0x013c, code lost:
        if (r9.$reuseBuffer == false) goto L_0x0142;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:69:0x013e, code lost:
        r11 = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:70:0x0142, code lost:
        r11 = new java.util.ArrayList(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:0x014c, code lost:
        r9.L$0 = r3;
        r9.I$0 = r2;
        r9.L$1 = r0;
        r9.label = 4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:72:0x0159, code lost:
        if (r3.yield(r11, r9) != r10) goto L_0x015c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:73:0x015b, code lost:
        return r10;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:74:0x015c, code lost:
        r0.removeFirst(r9.$step);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:76:0x016a, code lost:
        if ((!r0.isEmpty()) == false) goto L_0x017a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:77:0x016c, code lost:
        r9.I$0 = r2;
        r9.L$0 = r0;
        r9.label = 5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:78:0x0177, code lost:
        if (r3.yield(r0, r9) != r10) goto L_0x017a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:79:0x0179, code lost:
        return r10;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:81:0x017c, code lost:
        return kotlin.Unit.INSTANCE;
     */
    @org.jetbrains.annotations.Nullable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.lang.Object doResume(@org.jetbrains.annotations.Nullable java.lang.Object r10, @org.jetbrains.annotations.Nullable java.lang.Throwable r11) {
        /*
            r9 = this;
            java.lang.Object r10 = kotlin.coroutines.experimental.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r0 = r9.label
            r1 = 1
            switch(r0) {
                case 0: goto L_0x005e;
                case 1: goto L_0x0046;
                case 2: goto L_0x003b;
                case 3: goto L_0x0028;
                case 4: goto L_0x001b;
                case 5: goto L_0x0012;
                default: goto L_0x000a;
            }
        L_0x000a:
            java.lang.IllegalStateException r9 = new java.lang.IllegalStateException
            java.lang.String r10 = "call to 'resume' before 'invoke' with coroutine"
            r9.<init>(r10)
            throw r9
        L_0x0012:
            java.lang.Object r10 = r9.L$0
            kotlin.collections.RingBuffer r10 = (kotlin.collections.RingBuffer) r10
            int r9 = r9.I$0
            if (r11 == 0) goto L_0x017a
            throw r11
        L_0x001b:
            java.lang.Object r0 = r9.L$1
            kotlin.collections.RingBuffer r0 = (kotlin.collections.RingBuffer) r0
            int r2 = r9.I$0
            java.lang.Object r3 = r9.L$0
            kotlin.coroutines.experimental.SequenceBuilder r3 = (kotlin.coroutines.experimental.SequenceBuilder) r3
            if (r11 == 0) goto L_0x015c
            throw r11
        L_0x0028:
            java.lang.Object r0 = r9.L$3
            java.util.Iterator r0 = (java.util.Iterator) r0
            java.lang.Object r2 = r9.L$2
            java.lang.Object r2 = r9.L$1
            kotlin.collections.RingBuffer r2 = (kotlin.collections.RingBuffer) r2
            int r3 = r9.I$0
            java.lang.Object r4 = r9.L$0
            kotlin.coroutines.experimental.SequenceBuilder r4 = (kotlin.coroutines.experimental.SequenceBuilder) r4
            if (r11 == 0) goto L_0x0125
            throw r11
        L_0x003b:
            int r10 = r9.I$1
            java.lang.Object r10 = r9.L$0
            java.util.ArrayList r10 = (java.util.ArrayList) r10
            int r9 = r9.I$0
            if (r11 == 0) goto L_0x017a
            throw r11
        L_0x0046:
            java.lang.Object r0 = r9.L$3
            java.util.Iterator r0 = (java.util.Iterator) r0
            java.lang.Object r2 = r9.L$2
            int r2 = r9.I$1
            java.lang.Object r2 = r9.L$1
            java.util.ArrayList r2 = (java.util.ArrayList) r2
            int r3 = r9.I$0
            java.lang.Object r4 = r9.L$0
            kotlin.coroutines.experimental.SequenceBuilder r4 = (kotlin.coroutines.experimental.SequenceBuilder) r4
            if (r11 == 0) goto L_0x005b
            throw r11
        L_0x005b:
            r11 = r10
            r10 = r3
            goto L_0x00a8
        L_0x005e:
            if (r11 == 0) goto L_0x0061
            throw r11
        L_0x0061:
            kotlin.coroutines.experimental.SequenceBuilder r11 = r9.f572p$
            int r0 = r9.$step
            int r2 = r9.$size
            int r0 = r0 - r2
            if (r0 < 0) goto L_0x00df
            java.util.ArrayList r2 = new java.util.ArrayList
            int r3 = r9.$size
            r2.<init>(r3)
            r3 = 0
            java.util.Iterator r4 = r9.$iterator
            r8 = r11
            r11 = r10
            r10 = r0
            r0 = r4
            r4 = r8
        L_0x0079:
            boolean r5 = r0.hasNext()
            if (r5 == 0) goto L_0x00b9
            java.lang.Object r5 = r0.next()
            if (r3 <= 0) goto L_0x0088
            int r3 = r3 + -1
            goto L_0x0079
        L_0x0088:
            r2.add(r5)
            int r6 = r2.size()
            int r7 = r9.$size
            if (r6 != r7) goto L_0x0079
            r9.L$0 = r4
            r9.I$0 = r10
            r9.L$1 = r2
            r9.I$1 = r3
            r9.L$2 = r5
            r9.L$3 = r0
            r9.label = r1
            java.lang.Object r3 = r4.yield(r2, r9)
            if (r3 != r11) goto L_0x00a8
            return r11
        L_0x00a8:
            boolean r3 = r9.$reuseBuffer
            if (r3 == 0) goto L_0x00b0
            r2.clear()
            goto L_0x00b7
        L_0x00b0:
            java.util.ArrayList r2 = new java.util.ArrayList
            int r3 = r9.$size
            r2.<init>(r3)
        L_0x00b7:
            r3 = r10
            goto L_0x0079
        L_0x00b9:
            r0 = r2
            java.util.Collection r0 = (java.util.Collection) r0
            boolean r0 = r0.isEmpty()
            r0 = r0 ^ r1
            if (r0 == 0) goto L_0x017a
            boolean r0 = r9.$partialWindows
            if (r0 != 0) goto L_0x00cf
            int r0 = r2.size()
            int r1 = r9.$size
            if (r0 != r1) goto L_0x017a
        L_0x00cf:
            r9.I$0 = r10
            r9.L$0 = r2
            r9.I$1 = r3
            r10 = 2
            r9.label = r10
            java.lang.Object r9 = r4.yield(r2, r9)
            if (r9 != r11) goto L_0x017a
            return r11
        L_0x00df:
            kotlin.collections.RingBuffer r2 = new kotlin.collections.RingBuffer
            int r3 = r9.$size
            r2.<init>(r3)
            java.util.Iterator r3 = r9.$iterator
            r4 = r11
            r8 = r3
            r3 = r0
            r0 = r8
        L_0x00ec:
            boolean r11 = r0.hasNext()
            if (r11 == 0) goto L_0x012b
            java.lang.Object r11 = r0.next()
            r2.add(r11)
            boolean r5 = r2.isFull()
            if (r5 == 0) goto L_0x00ec
            boolean r5 = r9.$reuseBuffer
            if (r5 == 0) goto L_0x0107
            r5 = r2
            java.util.List r5 = (java.util.List) r5
            goto L_0x0111
        L_0x0107:
            java.util.ArrayList r5 = new java.util.ArrayList
            r6 = r2
            java.util.Collection r6 = (java.util.Collection) r6
            r5.<init>(r6)
            java.util.List r5 = (java.util.List) r5
        L_0x0111:
            r9.L$0 = r4
            r9.I$0 = r3
            r9.L$1 = r2
            r9.L$2 = r11
            r9.L$3 = r0
            r11 = 3
            r9.label = r11
            java.lang.Object r11 = r4.yield(r5, r9)
            if (r11 != r10) goto L_0x0125
            return r10
        L_0x0125:
            int r11 = r9.$step
            r2.removeFirst(r11)
            goto L_0x00ec
        L_0x012b:
            boolean r11 = r9.$partialWindows
            if (r11 == 0) goto L_0x017a
            r0 = r2
            r2 = r3
            r3 = r4
        L_0x0132:
            int r11 = r0.size()
            int r4 = r9.$step
            if (r11 <= r4) goto L_0x0162
            boolean r11 = r9.$reuseBuffer
            if (r11 == 0) goto L_0x0142
            r11 = r0
            java.util.List r11 = (java.util.List) r11
            goto L_0x014c
        L_0x0142:
            java.util.ArrayList r11 = new java.util.ArrayList
            r4 = r0
            java.util.Collection r4 = (java.util.Collection) r4
            r11.<init>(r4)
            java.util.List r11 = (java.util.List) r11
        L_0x014c:
            r9.L$0 = r3
            r9.I$0 = r2
            r9.L$1 = r0
            r4 = 4
            r9.label = r4
            java.lang.Object r11 = r3.yield(r11, r9)
            if (r11 != r10) goto L_0x015c
            return r10
        L_0x015c:
            int r11 = r9.$step
            r0.removeFirst(r11)
            goto L_0x0132
        L_0x0162:
            r11 = r0
            java.util.Collection r11 = (java.util.Collection) r11
            boolean r11 = r11.isEmpty()
            r11 = r11 ^ r1
            if (r11 == 0) goto L_0x017a
            r9.I$0 = r2
            r9.L$0 = r0
            r11 = 5
            r9.label = r11
            java.lang.Object r9 = r3.yield(r0, r9)
            if (r9 != r10) goto L_0x017a
            return r10
        L_0x017a:
            kotlin.Unit r9 = kotlin.Unit.INSTANCE
            return r9
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.collections.SlidingWindowKt$windowedIterator$1.doResume(java.lang.Object, java.lang.Throwable):java.lang.Object");
    }
}
