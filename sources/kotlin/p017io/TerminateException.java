package kotlin.p017io;

import java.io.File;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004¨\u0006\u0005"}, mo14495d2 = {"Lkotlin/io/TerminateException;", "Lkotlin/io/FileSystemException;", "file", "Ljava/io/File;", "(Ljava/io/File;)V", "kotlin-stdlib"}, mo14496k = 1, mo14497mv = {1, 1, 10})
/* renamed from: kotlin.io.TerminateException */
/* compiled from: Utils.kt */
final class TerminateException extends FileSystemException {
    public TerminateException(@NotNull File file) {
        Intrinsics.checkParameterIsNotNull(file, "file");
        super(file, null, null, 6, null);
    }
}
