package kotlin.jvm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.annotation.AnnotationTarget;

@SinceKotlin(version = "1.2")
@Target({ElementType.METHOD})
@kotlin.annotation.Target(allowedTargets = {AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY})
@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000\n\n\u0002\u0018\u0002\n\u0002\u0010\u001b\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\u0000ø\u0001\u0000\u0002\u0007\n\u0005\b(0\u0001¨\u0006\u0002"}, mo14495d2 = {"Lkotlin/jvm/JvmDefault;", "", "kotlin-stdlib"}, mo14496k = 1, mo14497mv = {1, 1, 10})
@Retention(RetentionPolicy.RUNTIME)
/* compiled from: JvmDefault.kt */
public @interface JvmDefault {
}
