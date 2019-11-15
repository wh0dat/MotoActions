package kotlin.sequences;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.collections.ArraysKt;
import kotlin.internal.InlineOnly;
import kotlin.internal.LowPriorityInOverloadResolution;
import kotlin.jvm.JvmName;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000@\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010(\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0011\n\u0002\b\u0006\n\u0002\u0010\u001c\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0000\u001a+\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0014\b\u0004\u0010\u0003\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00050\u0004H\b\u001a\u0012\u0010\u0006\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002\u001a&\u0010\u0007\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\b\b\u0000\u0010\u0002*\u00020\b2\u000e\u0010\t\u001a\n\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u0004\u001a<\u0010\u0007\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\b\b\u0000\u0010\u0002*\u00020\b2\u000e\u0010\n\u001a\n\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u00042\u0014\u0010\t\u001a\u0010\u0012\u0004\u0012\u0002H\u0002\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u000b\u001a=\u0010\u0007\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\b\b\u0000\u0010\u0002*\u00020\b2\b\u0010\f\u001a\u0004\u0018\u0001H\u00022\u0014\u0010\t\u001a\u0010\u0012\u0004\u0012\u0002H\u0002\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u000bH\u0007¢\u0006\u0002\u0010\r\u001a+\u0010\u000e\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0012\u0010\u000f\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0010\"\u0002H\u0002¢\u0006\u0002\u0010\u0011\u001a\u001c\u0010\u0012\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0005\u001a\u001c\u0010\u0013\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0001\u001aC\u0010\u0014\u001a\b\u0012\u0004\u0012\u0002H\u00150\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0015*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0018\u0010\u0003\u001a\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00150\u00050\u000bH\u0002¢\u0006\u0002\b\u0016\u001a)\u0010\u0014\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00170\u0001H\u0007¢\u0006\u0002\b\u0018\u001a\"\u0010\u0014\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00010\u0001\u001a@\u0010\u0019\u001a\u001a\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u001b\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00150\u001b0\u001a\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0015*\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00150\u001a0\u0001¨\u0006\u001c"}, mo14495d2 = {"Sequence", "Lkotlin/sequences/Sequence;", "T", "iterator", "Lkotlin/Function0;", "", "emptySequence", "generateSequence", "", "nextFunction", "seedFunction", "Lkotlin/Function1;", "seed", "(Ljava/lang/Object;Lkotlin/jvm/functions/Function1;)Lkotlin/sequences/Sequence;", "sequenceOf", "elements", "", "([Ljava/lang/Object;)Lkotlin/sequences/Sequence;", "asSequence", "constrainOnce", "flatten", "R", "flatten$SequencesKt__SequencesKt", "", "flattenSequenceOfIterable", "unzip", "Lkotlin/Pair;", "", "kotlin-stdlib"}, mo14496k = 5, mo14497mv = {1, 1, 10}, mo14499xi = 1, mo14500xs = "kotlin/sequences/SequencesKt")
/* compiled from: Sequences.kt */
class SequencesKt__SequencesKt extends SequencesKt__SequencesJVMKt {
    @InlineOnly
    private static final <T> Sequence<T> Sequence(Function0<? extends Iterator<? extends T>> function0) {
        return new SequencesKt__SequencesKt$Sequence$1<>(function0);
    }

    @NotNull
    public static final <T> Sequence<T> asSequence(@NotNull Iterator<? extends T> it) {
        Intrinsics.checkParameterIsNotNull(it, "$receiver");
        return SequencesKt.constrainOnce(new SequencesKt__SequencesKt$asSequence$$inlined$Sequence$1(it));
    }

    @NotNull
    public static final <T> Sequence<T> sequenceOf(@NotNull T... tArr) {
        Intrinsics.checkParameterIsNotNull(tArr, "elements");
        return tArr.length == 0 ? SequencesKt.emptySequence() : ArraysKt.asSequence(tArr);
    }

    @NotNull
    public static final <T> Sequence<T> emptySequence() {
        return EmptySequence.INSTANCE;
    }

    @NotNull
    public static final <T> Sequence<T> flatten(@NotNull Sequence<? extends Sequence<? extends T>> sequence) {
        Intrinsics.checkParameterIsNotNull(sequence, "$receiver");
        return flatten$SequencesKt__SequencesKt(sequence, SequencesKt__SequencesKt$flatten$1.INSTANCE);
    }

    @NotNull
    @JvmName(name = "flattenSequenceOfIterable")
    public static final <T> Sequence<T> flattenSequenceOfIterable(@NotNull Sequence<? extends Iterable<? extends T>> sequence) {
        Intrinsics.checkParameterIsNotNull(sequence, "$receiver");
        return flatten$SequencesKt__SequencesKt(sequence, SequencesKt__SequencesKt$flatten$2.INSTANCE);
    }

    private static final <T, R> Sequence<R> flatten$SequencesKt__SequencesKt(@NotNull Sequence<? extends T> sequence, Function1<? super T, ? extends Iterator<? extends R>> function1) {
        if (sequence instanceof TransformingSequence) {
            return ((TransformingSequence) sequence).flatten$kotlin_stdlib(function1);
        }
        return new FlatteningSequence<>(sequence, SequencesKt__SequencesKt$flatten$3.INSTANCE, function1);
    }

    @NotNull
    public static final <T, R> Pair<List<T>, List<R>> unzip(@NotNull Sequence<? extends Pair<? extends T, ? extends R>> sequence) {
        Intrinsics.checkParameterIsNotNull(sequence, "$receiver");
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        for (Pair pair : sequence) {
            arrayList.add(pair.getFirst());
            arrayList2.add(pair.getSecond());
        }
        return TuplesKt.m100to(arrayList, arrayList2);
    }

    @NotNull
    public static final <T> Sequence<T> constrainOnce(@NotNull Sequence<? extends T> sequence) {
        Intrinsics.checkParameterIsNotNull(sequence, "$receiver");
        return sequence instanceof ConstrainedOnceSequence ? sequence : new ConstrainedOnceSequence<>(sequence);
    }

    @NotNull
    public static final <T> Sequence<T> generateSequence(@NotNull Function0<? extends T> function0) {
        Intrinsics.checkParameterIsNotNull(function0, "nextFunction");
        return SequencesKt.constrainOnce(new GeneratorSequence(function0, new SequencesKt__SequencesKt$generateSequence$1(function0)));
    }

    @NotNull
    @LowPriorityInOverloadResolution
    public static final <T> Sequence<T> generateSequence(@Nullable T t, @NotNull Function1<? super T, ? extends T> function1) {
        Intrinsics.checkParameterIsNotNull(function1, "nextFunction");
        if (t == null) {
            return EmptySequence.INSTANCE;
        }
        return new GeneratorSequence<>(new SequencesKt__SequencesKt$generateSequence$2(t), function1);
    }

    @NotNull
    public static final <T> Sequence<T> generateSequence(@NotNull Function0<? extends T> function0, @NotNull Function1<? super T, ? extends T> function1) {
        Intrinsics.checkParameterIsNotNull(function0, "seedFunction");
        Intrinsics.checkParameterIsNotNull(function1, "nextFunction");
        return new GeneratorSequence<>(function0, function1);
    }
}
