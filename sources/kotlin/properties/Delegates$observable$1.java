package kotlin.properties;

import com.motorola.mod.ModManager;
import kotlin.Metadata;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;
import kotlin.reflect.KProperty;
import org.jetbrains.annotations.NotNull;

@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000\u001b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001B\u0005¢\u0006\u0002\u0010\u0002J)\u0010\u0003\u001a\u00020\u00042\n\u0010\u0005\u001a\u0006\u0012\u0002\b\u00030\u00062\u0006\u0010\u0007\u001a\u00028\u00002\u0006\u0010\b\u001a\u00028\u0000H\u0014¢\u0006\u0002\u0010\t¨\u0006\n"}, mo14495d2 = {"kotlin/properties/Delegates$observable$1", "Lkotlin/properties/ObservableProperty;", "(Lkotlin/jvm/functions/Function3;Ljava/lang/Object;Ljava/lang/Object;)V", "afterChange", "", "property", "Lkotlin/reflect/KProperty;", "oldValue", "newValue", "(Lkotlin/reflect/KProperty;Ljava/lang/Object;Ljava/lang/Object;)V", "kotlin-stdlib"}, mo14496k = 1, mo14497mv = {1, 1, 10})
/* compiled from: Delegates.kt */
public final class Delegates$observable$1 extends ObservableProperty<T> {
    final /* synthetic */ Object $initialValue;
    final /* synthetic */ Function3 $onChange;

    public Delegates$observable$1(Function3 function3, Object obj, Object obj2) {
        this.$onChange = function3;
        this.$initialValue = obj;
        super(obj2);
    }

    /* access modifiers changed from: protected */
    public void afterChange(@NotNull KProperty<?> kProperty, T t, T t2) {
        Intrinsics.checkParameterIsNotNull(kProperty, ModManager.EXTRA_MOD_PROPERTY);
        this.$onChange.invoke(kProperty, t, t2);
    }
}
