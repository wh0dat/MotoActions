package kotlin.p017io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.internal.InlineOnly;
import kotlin.internal.PlatformImplementationsKt;
import kotlin.jvm.JvmName;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.InlineMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import kotlin.text.Charsets;
import org.jetbrains.annotations.NotNull;

@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000X\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\u001a\u0017\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u0004H\b\u001a\u0017\u0010\u0000\u001a\u00020\u0005*\u00020\u00062\b\b\u0002\u0010\u0003\u001a\u00020\u0004H\b\u001a\u001c\u0010\u0007\u001a\u00020\b*\u00020\u00022\u0006\u0010\t\u001a\u00020\u00062\b\b\u0002\u0010\u0003\u001a\u00020\u0004\u001a\u001e\u0010\n\u001a\u00020\u000b*\u00020\u00022\u0012\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u000b0\r\u001a\u0010\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0010*\u00020\u0001\u001a\n\u0010\u0011\u001a\u00020\u0012*\u00020\u0013\u001a\u0010\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0015*\u00020\u0002\u001a\n\u0010\u0016\u001a\u00020\u000e*\u00020\u0002\u001a\u0017\u0010\u0016\u001a\u00020\u000e*\u00020\u00132\b\b\u0002\u0010\u0017\u001a\u00020\u0018H\b\u001a\r\u0010\u0019\u001a\u00020\u001a*\u00020\u000eH\b\u001a5\u0010\u001b\u001a\u0002H\u001c\"\u0004\b\u0000\u0010\u001c*\u00020\u00022\u0018\u0010\u001d\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\u0010\u0012\u0004\u0012\u0002H\u001c0\rH\bø\u0001\u0000¢\u0006\u0002\u0010\u001f\u0002\b\n\u0006\b\u0011(\u001e0\u0001¨\u0006 "}, mo14495d2 = {"buffered", "Ljava/io/BufferedReader;", "Ljava/io/Reader;", "bufferSize", "", "Ljava/io/BufferedWriter;", "Ljava/io/Writer;", "copyTo", "", "out", "forEachLine", "", "action", "Lkotlin/Function1;", "", "lineSequence", "Lkotlin/sequences/Sequence;", "readBytes", "", "Ljava/net/URL;", "readLines", "", "readText", "charset", "Ljava/nio/charset/Charset;", "reader", "Ljava/io/StringReader;", "useLines", "T", "block", "Requires newer compiler version to be inlined correctly.", "(Ljava/io/Reader;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "kotlin-stdlib"}, mo14496k = 2, mo14497mv = {1, 1, 10})
@JvmName(name = "TextStreamsKt")
/* renamed from: kotlin.io.TextStreamsKt */
/* compiled from: ReadWrite.kt */
public final class TextStreamsKt {
    @InlineOnly
    private static final BufferedReader buffered(@NotNull Reader reader, int i) {
        return reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader, i);
    }

    @InlineOnly
    private static final BufferedWriter buffered(@NotNull Writer writer, int i) {
        return writer instanceof BufferedWriter ? (BufferedWriter) writer : new BufferedWriter(writer, i);
    }

    @NotNull
    public static final List<String> readLines(@NotNull Reader reader) {
        Intrinsics.checkParameterIsNotNull(reader, "$receiver");
        ArrayList arrayList = new ArrayList();
        forEachLine(reader, new TextStreamsKt$readLines$1(arrayList));
        return arrayList;
    }

    public static final <T> T useLines(@NotNull Reader reader, @NotNull Function1<? super Sequence<String>, ? extends T> function1) {
        Throwable th;
        Intrinsics.checkParameterIsNotNull(reader, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "block");
        Closeable bufferedReader = reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader, 8192);
        Throwable th2 = null;
        try {
            T invoke = function1.invoke(lineSequence((BufferedReader) bufferedReader));
            InlineMarker.finallyStart(1);
            if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0)) {
                CloseableKt.closeFinally(bufferedReader, th2);
            } else {
                bufferedReader.close();
            }
            InlineMarker.finallyEnd(1);
            return invoke;
        } catch (Throwable unused) {
        }
        InlineMarker.finallyEnd(1);
        throw th;
    }

    @InlineOnly
    private static final StringReader reader(@NotNull String str) {
        return new StringReader(str);
    }

    @NotNull
    public static final Sequence<String> lineSequence(@NotNull BufferedReader bufferedReader) {
        Intrinsics.checkParameterIsNotNull(bufferedReader, "$receiver");
        return SequencesKt.constrainOnce(new LinesSequence(bufferedReader));
    }

    @NotNull
    public static final String readText(@NotNull Reader reader) {
        Intrinsics.checkParameterIsNotNull(reader, "$receiver");
        StringWriter stringWriter = new StringWriter();
        copyTo$default(reader, stringWriter, 0, 2, null);
        String stringWriter2 = stringWriter.toString();
        Intrinsics.checkExpressionValueIsNotNull(stringWriter2, "buffer.toString()");
        return stringWriter2;
    }

    public static /* bridge */ /* synthetic */ long copyTo$default(Reader reader, Writer writer, int i, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = 8192;
        }
        return copyTo(reader, writer, i);
    }

    public static final long copyTo(@NotNull Reader reader, @NotNull Writer writer, int i) {
        Intrinsics.checkParameterIsNotNull(reader, "$receiver");
        Intrinsics.checkParameterIsNotNull(writer, "out");
        char[] cArr = new char[i];
        int read = reader.read(cArr);
        long j = 0;
        while (read >= 0) {
            writer.write(cArr, 0, read);
            j += (long) read;
            read = reader.read(cArr);
        }
        return j;
    }

    @InlineOnly
    private static final String readText(@NotNull URL url, Charset charset) {
        return new String(readBytes(url), charset);
    }

    @InlineOnly
    static /* bridge */ /* synthetic */ String readText$default(URL url, Charset charset, int i, Object obj) {
        if ((i & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        return new String(readBytes(url), charset);
    }

    @NotNull
    public static final byte[] readBytes(@NotNull URL url) {
        Throwable th;
        Intrinsics.checkParameterIsNotNull(url, "$receiver");
        Closeable openStream = url.openStream();
        Throwable th2 = null;
        try {
            InputStream inputStream = (InputStream) openStream;
            Intrinsics.checkExpressionValueIsNotNull(inputStream, "it");
            byte[] readBytes$default = ByteStreamsKt.readBytes$default(inputStream, 0, 1, null);
            CloseableKt.closeFinally(openStream, th2);
            return readBytes$default;
        } catch (Throwable th3) {
            CloseableKt.closeFinally(openStream, th);
            throw th3;
        }
    }

    @InlineOnly
    static /* bridge */ /* synthetic */ BufferedReader buffered$default(Reader reader, int i, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            i = 8192;
        }
        return reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader, i);
    }

    @InlineOnly
    static /* bridge */ /* synthetic */ BufferedWriter buffered$default(Writer writer, int i, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            i = 8192;
        }
        return writer instanceof BufferedWriter ? (BufferedWriter) writer : new BufferedWriter(writer, i);
    }

    public static final void forEachLine(@NotNull Reader reader, @NotNull Function1<? super String, Unit> function1) {
        Throwable th;
        Intrinsics.checkParameterIsNotNull(reader, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "action");
        Closeable bufferedReader = reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader, 8192);
        Throwable th2 = null;
        try {
            for (Object invoke : lineSequence((BufferedReader) bufferedReader)) {
                function1.invoke(invoke);
            }
            Unit unit = Unit.INSTANCE;
            CloseableKt.closeFinally(bufferedReader, th2);
        } catch (Throwable th3) {
            CloseableKt.closeFinally(bufferedReader, th);
            throw th3;
        }
    }
}
