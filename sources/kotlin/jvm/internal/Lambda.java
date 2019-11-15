package kotlin.jvm.internal;

import kotlin.Metadata;

@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\b&\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\b\u0010\u0005\u001a\u00020\u0003H\u0016J\u0010\u0010\u0006\u001a\n \b*\u0004\u0018\u00010\u00070\u0007H\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\t"}, mo14495d2 = {"Lkotlin/jvm/internal/Lambda;", "Lkotlin/jvm/internal/FunctionBase;", "arity", "", "(I)V", "getArity", "toString", "", "kotlin.jvm.PlatformType", "kotlin-stdlib"}, mo14496k = 1, mo14497mv = {1, 1, 10})
/* compiled from: Lambda.kt */
public abstract class Lambda implements FunctionBase {
    private final int arity;

    public Lambda(int i) {
        this.arity = i;
    }

    public int getArity() {
        return this.arity;
    }

    public String toString() {
        return Reflection.renderLambdaToString(this);
    }
}
