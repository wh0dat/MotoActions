package android.support.p001v4.p003os;

import android.os.Build.VERSION;
import android.os.Trace;

/* renamed from: android.support.v4.os.TraceCompat */
public final class TraceCompat {
    public static void beginSection(String str) {
        if (VERSION.SDK_INT >= 18) {
            Trace.beginSection(str);
        }
    }

    public static void endSection() {
        if (VERSION.SDK_INT >= 18) {
            Trace.endSection();
        }
    }

    private TraceCompat() {
    }
}
