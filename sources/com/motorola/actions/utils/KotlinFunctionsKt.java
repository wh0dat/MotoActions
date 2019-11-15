package com.motorola.actions.utils;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000\u0014\n\u0000\n\u0002\u0010\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a&\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00010\u00042\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00010\u0004¨\u0006\u0006"}, mo14495d2 = {"ifElse", "", "", "fTrue", "Lkotlin/Function0;", "fFalse", "MotoActions_release"}, mo14496k = 2, mo14497mv = {1, 1, 11})
/* compiled from: KotlinFunctions.kt */
public final class KotlinFunctionsKt {
    public static final void ifElse(boolean z, @NotNull Function0<Unit> function0, @NotNull Function0<Unit> function02) {
        Intrinsics.checkParameterIsNotNull(function0, "fTrue");
        Intrinsics.checkParameterIsNotNull(function02, "fFalse");
        if (z) {
            function0.invoke();
        } else {
            function02.invoke();
        }
    }
}
