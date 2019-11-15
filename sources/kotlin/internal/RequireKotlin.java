package kotlin.internal;

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.annotation.AnnotationRetention;
import kotlin.annotation.AnnotationTarget;
import kotlin.annotation.Retention;
import kotlin.annotation.Target;

@SinceKotlin(version = "1.2")
@Target(allowedTargets = {AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY, AnnotationTarget.CONSTRUCTOR, AnnotationTarget.TYPEALIAS})
@Retention(AnnotationRetention.SOURCE)
@java.lang.annotation.Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR})
@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u001b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\b\u0002\u0018\u00002\u00020\u0001B0\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u0012\b\b\u0002\u0010\t\u001a\u00020\nR\t\u0010\t\u001a\u00020\n¢\u0006\u0000R\t\u0010\u0005\u001a\u00020\u0006¢\u0006\u0000R\t\u0010\u0004\u001a\u00020\u0003¢\u0006\u0000R\t\u0010\u0002\u001a\u00020\u0003¢\u0006\u0000R\t\u0010\u0007\u001a\u00020\b¢\u0006\u0000¨\u0006\u000b"}, mo14495d2 = {"Lkotlin/internal/RequireKotlin;", "", "version", "", "message", "level", "Lkotlin/DeprecationLevel;", "versionKind", "Lkotlin/internal/RequireKotlinVersionKind;", "errorCode", "", "kotlin-stdlib"}, mo14496k = 1, mo14497mv = {1, 1, 10})
@java.lang.annotation.Retention(RetentionPolicy.SOURCE)
/* compiled from: Annotations.kt */
public @interface RequireKotlin {
    int errorCode() default -1;

    DeprecationLevel level() default DeprecationLevel.ERROR;

    String message() default "";

    String version();

    RequireKotlinVersionKind versionKind() default RequireKotlinVersionKind.LANGUAGE_VERSION;
}
