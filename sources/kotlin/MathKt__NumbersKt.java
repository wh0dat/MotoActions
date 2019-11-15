package kotlin;

import kotlin.internal.InlineOnly;
import kotlin.jvm.internal.DoubleCompanionObject;
import kotlin.jvm.internal.FloatCompanionObject;
import org.jetbrains.annotations.NotNull;

@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000&\n\u0000\n\u0002\u0010\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\u0010\u0007\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\b\u001a\u0015\u0010\u0000\u001a\u00020\u0005*\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u0007H\b\u001a\r\u0010\b\u001a\u00020\t*\u00020\u0001H\b\u001a\r\u0010\b\u001a\u00020\t*\u00020\u0005H\b\u001a\r\u0010\n\u001a\u00020\t*\u00020\u0001H\b\u001a\r\u0010\n\u001a\u00020\t*\u00020\u0005H\b\u001a\r\u0010\u000b\u001a\u00020\t*\u00020\u0001H\b\u001a\r\u0010\u000b\u001a\u00020\t*\u00020\u0005H\b\u001a\r\u0010\f\u001a\u00020\u0004*\u00020\u0001H\b\u001a\r\u0010\f\u001a\u00020\u0007*\u00020\u0005H\b\u001a\r\u0010\r\u001a\u00020\u0004*\u00020\u0001H\b\u001a\r\u0010\r\u001a\u00020\u0007*\u00020\u0005H\b¨\u0006\u000e"}, mo14495d2 = {"fromBits", "", "Lkotlin/Double$Companion;", "bits", "", "", "Lkotlin/Float$Companion;", "", "isFinite", "", "isInfinite", "isNaN", "toBits", "toRawBits", "kotlin-stdlib"}, mo14496k = 5, mo14497mv = {1, 1, 10}, mo14499xi = 1, mo14500xs = "kotlin/MathKt")
/* compiled from: Numbers.kt */
class MathKt__NumbersKt extends MathKt__BigIntegersKt {
    @InlineOnly
    private static final boolean isNaN(double d) {
        return Double.isNaN(d);
    }

    @InlineOnly
    private static final boolean isNaN(float f) {
        return Float.isNaN(f);
    }

    @InlineOnly
    private static final boolean isInfinite(double d) {
        return Double.isInfinite(d);
    }

    @InlineOnly
    private static final boolean isInfinite(float f) {
        return Float.isInfinite(f);
    }

    @InlineOnly
    private static final boolean isFinite(double d) {
        return !Double.isInfinite(d) && !Double.isNaN(d);
    }

    @InlineOnly
    private static final boolean isFinite(float f) {
        return !Float.isInfinite(f) && !Float.isNaN(f);
    }

    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final long toBits(double d) {
        return Double.doubleToLongBits(d);
    }

    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final long toRawBits(double d) {
        return Double.doubleToRawLongBits(d);
    }

    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final double fromBits(@NotNull DoubleCompanionObject doubleCompanionObject, long j) {
        return Double.longBitsToDouble(j);
    }

    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final int toBits(float f) {
        return Float.floatToIntBits(f);
    }

    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final int toRawBits(float f) {
        return Float.floatToRawIntBits(f);
    }

    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final float fromBits(@NotNull FloatCompanionObject floatCompanionObject, int i) {
        return Float.intBitsToFloat(i);
    }
}
