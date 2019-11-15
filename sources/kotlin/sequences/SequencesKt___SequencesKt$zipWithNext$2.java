package kotlin.sequences;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.SequenceBuilder;
import kotlin.coroutines.experimental.jvm.internal.CoroutineImpl;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\b\u0012\u0004\u0012\u0002H\u00030\u0004H\n¢\u0006\u0004\b\u0005\u0010\u0006"}, mo14495d2 = {"<anonymous>", "", "T", "R", "Lkotlin/coroutines/experimental/SequenceBuilder;", "invoke", "(Lkotlin/coroutines/experimental/SequenceBuilder;Lkotlin/coroutines/experimental/Continuation;)Ljava/lang/Object;"}, mo14496k = 3, mo14497mv = {1, 1, 10})
/* compiled from: _Sequences.kt */
final class SequencesKt___SequencesKt$zipWithNext$2 extends CoroutineImpl implements Function2<SequenceBuilder<? super R>, Continuation<? super Unit>, Object> {
    final /* synthetic */ Function2 $transform;
    Object L$0;
    Object L$1;
    Object L$2;
    Object L$3;

    /* renamed from: p$ */
    private SequenceBuilder f576p$;
    final /* synthetic */ Sequence receiver$0;

    SequencesKt___SequencesKt$zipWithNext$2(Sequence sequence, Function2 function2, Continuation continuation) {
        this.receiver$0 = sequence;
        this.$transform = function2;
        super(2, continuation);
    }

    @NotNull
    public final Continuation<Unit> create(@NotNull SequenceBuilder<? super R> sequenceBuilder, @NotNull Continuation<? super Unit> continuation) {
        Intrinsics.checkParameterIsNotNull(sequenceBuilder, "$receiver");
        Intrinsics.checkParameterIsNotNull(continuation, "continuation");
        SequencesKt___SequencesKt$zipWithNext$2 sequencesKt___SequencesKt$zipWithNext$2 = new SequencesKt___SequencesKt$zipWithNext$2(this.receiver$0, this.$transform, continuation);
        sequencesKt___SequencesKt$zipWithNext$2.f576p$ = sequenceBuilder;
        return sequencesKt___SequencesKt$zipWithNext$2;
    }

    @Nullable
    public final Object invoke(@NotNull SequenceBuilder<? super R> sequenceBuilder, @NotNull Continuation<? super Unit> continuation) {
        Intrinsics.checkParameterIsNotNull(sequenceBuilder, "$receiver");
        Intrinsics.checkParameterIsNotNull(continuation, "continuation");
        return ((SequencesKt___SequencesKt$zipWithNext$2) create(sequenceBuilder, continuation)).doResume(Unit.INSTANCE, null);
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x0045  */
    @org.jetbrains.annotations.Nullable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.lang.Object doResume(@org.jetbrains.annotations.Nullable java.lang.Object r5, @org.jetbrains.annotations.Nullable java.lang.Throwable r6) {
        /*
            r4 = this;
            java.lang.Object r5 = kotlin.coroutines.experimental.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r0 = r4.label
            switch(r0) {
                case 0: goto L_0x0023;
                case 1: goto L_0x0011;
                default: goto L_0x0009;
            }
        L_0x0009:
            java.lang.IllegalStateException r4 = new java.lang.IllegalStateException
            java.lang.String r5 = "call to 'resume' before 'invoke' with coroutine"
            r4.<init>(r5)
            throw r4
        L_0x0011:
            java.lang.Object r0 = r4.L$3
            java.lang.Object r1 = r4.L$2
            java.lang.Object r1 = r4.L$1
            java.util.Iterator r1 = (java.util.Iterator) r1
            java.lang.Object r2 = r4.L$0
            kotlin.coroutines.experimental.SequenceBuilder r2 = (kotlin.coroutines.experimental.SequenceBuilder) r2
            if (r6 == 0) goto L_0x0020
            throw r6
        L_0x0020:
            r6 = r5
        L_0x0021:
            r5 = r0
            goto L_0x003f
        L_0x0023:
            if (r6 == 0) goto L_0x0026
            throw r6
        L_0x0026:
            kotlin.coroutines.experimental.SequenceBuilder r6 = r4.f576p$
            kotlin.sequences.Sequence r0 = r4.receiver$0
            java.util.Iterator r0 = r0.iterator()
            boolean r1 = r0.hasNext()
            if (r1 != 0) goto L_0x0037
            kotlin.Unit r4 = kotlin.Unit.INSTANCE
            return r4
        L_0x0037:
            java.lang.Object r1 = r0.next()
            r2 = r6
            r6 = r5
            r5 = r1
            r1 = r0
        L_0x003f:
            boolean r0 = r1.hasNext()
            if (r0 == 0) goto L_0x0061
            java.lang.Object r0 = r1.next()
            kotlin.jvm.functions.Function2 r3 = r4.$transform
            java.lang.Object r3 = r3.invoke(r5, r0)
            r4.L$0 = r2
            r4.L$1 = r1
            r4.L$2 = r5
            r4.L$3 = r0
            r5 = 1
            r4.label = r5
            java.lang.Object r5 = r2.yield(r3, r4)
            if (r5 != r6) goto L_0x0021
            return r6
        L_0x0061:
            kotlin.Unit r4 = kotlin.Unit.INSTANCE
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.sequences.SequencesKt___SequencesKt$zipWithNext$2.doResume(java.lang.Object, java.lang.Throwable):java.lang.Object");
    }
}
