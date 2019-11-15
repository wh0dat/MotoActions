package kotlin.system;

import kotlin.Metadata;
import kotlin.internal.InlineOnly;
import kotlin.jvm.JvmName;

@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0001\n\u0000\n\u0002\u0010\b\n\u0000\u001a\u0011\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\b¨\u0006\u0004"}, mo14495d2 = {"exitProcess", "", "status", "", "kotlin-stdlib"}, mo14496k = 2, mo14497mv = {1, 1, 10})
@JvmName(name = "ProcessKt")
/* compiled from: Process.kt */
public final class ProcessKt {
    @InlineOnly
    private static final Void exitProcess(int i) {
        System.exit(i);
        throw new RuntimeException("System.exit returned normally, while it was supposed to halt JVM.");
    }
}
