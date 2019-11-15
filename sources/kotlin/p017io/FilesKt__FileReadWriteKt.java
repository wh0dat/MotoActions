package kotlin.p017io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.internal.InlineOnly;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;
import org.jetbrains.annotations.NotNull;

@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000z\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\u001a\u0012\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004\u001a\u001c\u0010\u0005\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\t\u001a!\u0010\n\u001a\u00020\u000b*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\f\u001a\u00020\rH\b\u001a!\u0010\u000e\u001a\u00020\u000f*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\f\u001a\u00020\rH\b\u001aB\u0010\u0010\u001a\u00020\u0001*\u00020\u000226\u0010\u0011\u001a2\u0012\u0013\u0012\u00110\u0004¢\u0006\f\b\u0013\u0012\b\b\u0014\u0012\u0004\b\b(\u0015\u0012\u0013\u0012\u00110\r¢\u0006\f\b\u0013\u0012\b\b\u0014\u0012\u0004\b\b(\u0016\u0012\u0004\u0012\u00020\u00010\u0012\u001aJ\u0010\u0010\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0017\u001a\u00020\r26\u0010\u0011\u001a2\u0012\u0013\u0012\u00110\u0004¢\u0006\f\b\u0013\u0012\b\b\u0014\u0012\u0004\b\b(\u0015\u0012\u0013\u0012\u00110\r¢\u0006\f\b\u0013\u0012\b\b\u0014\u0012\u0004\b\b(\u0016\u0012\u0004\u0012\u00020\u00010\u0012\u001a7\u0010\u0018\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\t2!\u0010\u0011\u001a\u001d\u0012\u0013\u0012\u00110\u0007¢\u0006\f\b\u0013\u0012\b\b\u0014\u0012\u0004\b\b(\u001a\u0012\u0004\u0012\u00020\u00010\u0019\u001a\r\u0010\u001b\u001a\u00020\u001c*\u00020\u0002H\b\u001a\r\u0010\u001d\u001a\u00020\u001e*\u00020\u0002H\b\u001a\u0017\u0010\u001f\u001a\u00020 *\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\tH\b\u001a\n\u0010!\u001a\u00020\u0004*\u00020\u0002\u001a\u001a\u0010\"\u001a\b\u0012\u0004\u0012\u00020\u00070#*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\t\u001a\u0014\u0010$\u001a\u00020\u0007*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\t\u001a\u0017\u0010%\u001a\u00020&*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\tH\b\u001a?\u0010'\u001a\u0002H(\"\u0004\b\u0000\u0010(*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\t2\u0018\u0010)\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00070*\u0012\u0004\u0012\u0002H(0\u0019H\bø\u0001\u0000¢\u0006\u0002\u0010,\u001a\u0012\u0010-\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004\u001a\u001c\u0010.\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\t\u001a\u0017\u0010/\u001a\u000200*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\tH\b\u0002\b\n\u0006\b\u0011(+0\u0001¨\u00061"}, mo14495d2 = {"appendBytes", "", "Ljava/io/File;", "array", "", "appendText", "text", "", "charset", "Ljava/nio/charset/Charset;", "bufferedReader", "Ljava/io/BufferedReader;", "bufferSize", "", "bufferedWriter", "Ljava/io/BufferedWriter;", "forEachBlock", "action", "Lkotlin/Function2;", "Lkotlin/ParameterName;", "name", "buffer", "bytesRead", "blockSize", "forEachLine", "Lkotlin/Function1;", "line", "inputStream", "Ljava/io/FileInputStream;", "outputStream", "Ljava/io/FileOutputStream;", "printWriter", "Ljava/io/PrintWriter;", "readBytes", "readLines", "", "readText", "reader", "Ljava/io/InputStreamReader;", "useLines", "T", "block", "Lkotlin/sequences/Sequence;", "Requires newer compiler version to be inlined correctly.", "(Ljava/io/File;Ljava/nio/charset/Charset;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "writeBytes", "writeText", "writer", "Ljava/io/OutputStreamWriter;", "kotlin-stdlib"}, mo14496k = 5, mo14497mv = {1, 1, 10}, mo14499xi = 1, mo14500xs = "kotlin/io/FilesKt")
/* renamed from: kotlin.io.FilesKt__FileReadWriteKt */
/* compiled from: FileReadWrite.kt */
class FilesKt__FileReadWriteKt extends FilesKt__FilePathComponentsKt {
    @InlineOnly
    static /* bridge */ /* synthetic */ InputStreamReader reader$default(File file, Charset charset, int i, Object obj) {
        if ((i & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        return new InputStreamReader(new FileInputStream(file), charset);
    }

    @InlineOnly
    private static final InputStreamReader reader(@NotNull File file, Charset charset) {
        return new InputStreamReader(new FileInputStream(file), charset);
    }

    @InlineOnly
    static /* bridge */ /* synthetic */ BufferedReader bufferedReader$default(File file, Charset charset, int i, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        if ((i2 & 2) != 0) {
            i = 8192;
        }
        Reader inputStreamReader = new InputStreamReader(new FileInputStream(file), charset);
        return inputStreamReader instanceof BufferedReader ? (BufferedReader) inputStreamReader : new BufferedReader(inputStreamReader, i);
    }

    @InlineOnly
    private static final BufferedReader bufferedReader(@NotNull File file, Charset charset, int i) {
        Reader inputStreamReader = new InputStreamReader(new FileInputStream(file), charset);
        return inputStreamReader instanceof BufferedReader ? (BufferedReader) inputStreamReader : new BufferedReader(inputStreamReader, i);
    }

    @InlineOnly
    static /* bridge */ /* synthetic */ OutputStreamWriter writer$default(File file, Charset charset, int i, Object obj) {
        if ((i & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        return new OutputStreamWriter(new FileOutputStream(file), charset);
    }

    @InlineOnly
    private static final OutputStreamWriter writer(@NotNull File file, Charset charset) {
        return new OutputStreamWriter(new FileOutputStream(file), charset);
    }

    @InlineOnly
    static /* bridge */ /* synthetic */ BufferedWriter bufferedWriter$default(File file, Charset charset, int i, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        if ((i2 & 2) != 0) {
            i = 8192;
        }
        Writer outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file), charset);
        return outputStreamWriter instanceof BufferedWriter ? (BufferedWriter) outputStreamWriter : new BufferedWriter(outputStreamWriter, i);
    }

    @InlineOnly
    private static final BufferedWriter bufferedWriter(@NotNull File file, Charset charset, int i) {
        Writer outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file), charset);
        return outputStreamWriter instanceof BufferedWriter ? (BufferedWriter) outputStreamWriter : new BufferedWriter(outputStreamWriter, i);
    }

    @InlineOnly
    static /* bridge */ /* synthetic */ PrintWriter printWriter$default(File file, Charset charset, int i, Object obj) {
        if ((i & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        Writer outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file), charset);
        return new PrintWriter(outputStreamWriter instanceof BufferedWriter ? (BufferedWriter) outputStreamWriter : new BufferedWriter(outputStreamWriter, 8192));
    }

    @InlineOnly
    private static final PrintWriter printWriter(@NotNull File file, Charset charset) {
        Writer outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file), charset);
        return new PrintWriter(outputStreamWriter instanceof BufferedWriter ? (BufferedWriter) outputStreamWriter : new BufferedWriter(outputStreamWriter, 8192));
    }

    @NotNull
    public static final byte[] readBytes(@NotNull File file) {
        Throwable th;
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        Closeable fileInputStream = new FileInputStream(file);
        Throwable th2 = null;
        try {
            FileInputStream fileInputStream2 = (FileInputStream) fileInputStream;
            int i = 0;
            long length = file.length();
            if (length > ((long) Integer.MAX_VALUE)) {
                StringBuilder sb = new StringBuilder();
                sb.append("File ");
                sb.append(file);
                sb.append(" is too big (");
                sb.append(length);
                sb.append(" bytes) to fit in memory.");
                throw new OutOfMemoryError(sb.toString());
            }
            int i2 = (int) length;
            byte[] bArr = new byte[i2];
            while (true) {
                if (i2 <= 0) {
                    break;
                }
                int read = fileInputStream2.read(bArr, i, i2);
                if (read < 0) {
                    break;
                }
                i2 -= read;
                i += read;
            }
            if (i2 != 0) {
                bArr = Arrays.copyOf(bArr, i);
                Intrinsics.checkExpressionValueIsNotNull(bArr, "java.util.Arrays.copyOf(this, newSize)");
            }
            CloseableKt.closeFinally(fileInputStream, th2);
            return bArr;
        } catch (Throwable th3) {
            CloseableKt.closeFinally(fileInputStream, th);
            throw th3;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0027, code lost:
        throw r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:5:0x0020, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0024, code lost:
        kotlin.p017io.CloseableKt.closeFinally(r0, r2);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final void writeBytes(@org.jetbrains.annotations.NotNull java.io.File r2, @org.jetbrains.annotations.NotNull byte[] r3) {
        /*
            java.lang.String r0 = "$receiver"
            kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r2, r0)
            java.lang.String r0 = "array"
            kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r3, r0)
            java.io.FileOutputStream r0 = new java.io.FileOutputStream
            r0.<init>(r2)
            java.io.Closeable r0 = (java.io.Closeable) r0
            r2 = 0
            java.lang.Throwable r2 = (java.lang.Throwable) r2
            r1 = r0
            java.io.FileOutputStream r1 = (java.io.FileOutputStream) r1     // Catch:{ Throwable -> 0x0022 }
            r1.write(r3)     // Catch:{ Throwable -> 0x0022 }
            kotlin.Unit r3 = kotlin.Unit.INSTANCE     // Catch:{ Throwable -> 0x0022 }
            kotlin.p017io.CloseableKt.closeFinally(r0, r2)
            return
        L_0x0020:
            r3 = move-exception
            goto L_0x0024
        L_0x0022:
            r2 = move-exception
            throw r2     // Catch:{ all -> 0x0020 }
        L_0x0024:
            kotlin.p017io.CloseableKt.closeFinally(r0, r2)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.p017io.FilesKt__FileReadWriteKt.writeBytes(java.io.File, byte[]):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0028, code lost:
        throw r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:5:0x0021, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0025, code lost:
        kotlin.p017io.CloseableKt.closeFinally(r0, r2);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final void appendBytes(@org.jetbrains.annotations.NotNull java.io.File r2, @org.jetbrains.annotations.NotNull byte[] r3) {
        /*
            java.lang.String r0 = "$receiver"
            kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r2, r0)
            java.lang.String r0 = "array"
            kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r3, r0)
            java.io.FileOutputStream r0 = new java.io.FileOutputStream
            r1 = 1
            r0.<init>(r2, r1)
            java.io.Closeable r0 = (java.io.Closeable) r0
            r2 = 0
            java.lang.Throwable r2 = (java.lang.Throwable) r2
            r1 = r0
            java.io.FileOutputStream r1 = (java.io.FileOutputStream) r1     // Catch:{ Throwable -> 0x0023 }
            r1.write(r3)     // Catch:{ Throwable -> 0x0023 }
            kotlin.Unit r3 = kotlin.Unit.INSTANCE     // Catch:{ Throwable -> 0x0023 }
            kotlin.p017io.CloseableKt.closeFinally(r0, r2)
            return
        L_0x0021:
            r3 = move-exception
            goto L_0x0025
        L_0x0023:
            r2 = move-exception
            throw r2     // Catch:{ all -> 0x0021 }
        L_0x0025:
            kotlin.p017io.CloseableKt.closeFinally(r0, r2)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.p017io.FilesKt__FileReadWriteKt.appendBytes(java.io.File, byte[]):void");
    }

    @NotNull
    public static final String readText(@NotNull File file, @NotNull Charset charset) {
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        Intrinsics.checkParameterIsNotNull(charset, "charset");
        return new String(FilesKt.readBytes(file), charset);
    }

    @NotNull
    public static /* bridge */ /* synthetic */ String readText$default(File file, Charset charset, int i, Object obj) {
        if ((i & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        return FilesKt.readText(file, charset);
    }

    public static final void writeText(@NotNull File file, @NotNull String str, @NotNull Charset charset) {
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        Intrinsics.checkParameterIsNotNull(str, "text");
        Intrinsics.checkParameterIsNotNull(charset, "charset");
        byte[] bytes = str.getBytes(charset);
        Intrinsics.checkExpressionValueIsNotNull(bytes, "(this as java.lang.String).getBytes(charset)");
        FilesKt.writeBytes(file, bytes);
    }

    public static /* bridge */ /* synthetic */ void writeText$default(File file, String str, Charset charset, int i, Object obj) {
        if ((i & 2) != 0) {
            charset = Charsets.UTF_8;
        }
        FilesKt.writeText(file, str, charset);
    }

    public static final void appendText(@NotNull File file, @NotNull String str, @NotNull Charset charset) {
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        Intrinsics.checkParameterIsNotNull(str, "text");
        Intrinsics.checkParameterIsNotNull(charset, "charset");
        byte[] bytes = str.getBytes(charset);
        Intrinsics.checkExpressionValueIsNotNull(bytes, "(this as java.lang.String).getBytes(charset)");
        FilesKt.appendBytes(file, bytes);
    }

    public static /* bridge */ /* synthetic */ void appendText$default(File file, String str, Charset charset, int i, Object obj) {
        if ((i & 2) != 0) {
            charset = Charsets.UTF_8;
        }
        FilesKt.appendText(file, str, charset);
    }

    public static final void forEachBlock(@NotNull File file, @NotNull Function2<? super byte[], ? super Integer, Unit> function2) {
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        Intrinsics.checkParameterIsNotNull(function2, "action");
        FilesKt.forEachBlock(file, 4096, function2);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0033, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0037, code lost:
        kotlin.p017io.CloseableKt.closeFinally(r0, r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x003a, code lost:
        throw r4;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final void forEachBlock(@org.jetbrains.annotations.NotNull java.io.File r3, int r4, @org.jetbrains.annotations.NotNull kotlin.jvm.functions.Function2<? super byte[], ? super java.lang.Integer, kotlin.Unit> r5) {
        /*
            java.lang.String r0 = "$receiver"
            kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r3, r0)
            java.lang.String r0 = "action"
            kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r5, r0)
            r0 = 512(0x200, float:7.175E-43)
            int r4 = kotlin.ranges.RangesKt.coerceAtLeast(r4, r0)
            byte[] r4 = new byte[r4]
            java.io.FileInputStream r0 = new java.io.FileInputStream
            r0.<init>(r3)
            java.io.Closeable r0 = (java.io.Closeable) r0
            r3 = 0
            java.lang.Throwable r3 = (java.lang.Throwable) r3
            r1 = r0
            java.io.FileInputStream r1 = (java.io.FileInputStream) r1     // Catch:{ Throwable -> 0x0035 }
        L_0x001f:
            int r2 = r1.read(r4)     // Catch:{ Throwable -> 0x0035 }
            if (r2 > 0) goto L_0x002b
            kotlin.Unit r4 = kotlin.Unit.INSTANCE     // Catch:{ Throwable -> 0x0035 }
            kotlin.p017io.CloseableKt.closeFinally(r0, r3)
            return
        L_0x002b:
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)     // Catch:{ Throwable -> 0x0035 }
            r5.invoke(r4, r2)     // Catch:{ Throwable -> 0x0035 }
            goto L_0x001f
        L_0x0033:
            r4 = move-exception
            goto L_0x0037
        L_0x0035:
            r3 = move-exception
            throw r3     // Catch:{ all -> 0x0033 }
        L_0x0037:
            kotlin.p017io.CloseableKt.closeFinally(r0, r3)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.p017io.FilesKt__FileReadWriteKt.forEachBlock(java.io.File, int, kotlin.jvm.functions.Function2):void");
    }

    public static /* bridge */ /* synthetic */ void forEachLine$default(File file, Charset charset, Function1 function1, int i, Object obj) {
        if ((i & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        FilesKt.forEachLine(file, charset, function1);
    }

    public static final void forEachLine(@NotNull File file, @NotNull Charset charset, @NotNull Function1<? super String, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        Intrinsics.checkParameterIsNotNull(charset, "charset");
        Intrinsics.checkParameterIsNotNull(function1, "action");
        TextStreamsKt.forEachLine(new BufferedReader(new InputStreamReader(new FileInputStream(file), charset)), function1);
    }

    @InlineOnly
    private static final FileInputStream inputStream(@NotNull File file) {
        return new FileInputStream(file);
    }

    @InlineOnly
    private static final FileOutputStream outputStream(@NotNull File file) {
        return new FileOutputStream(file);
    }

    @NotNull
    public static /* bridge */ /* synthetic */ List readLines$default(File file, Charset charset, int i, Object obj) {
        if ((i & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        return FilesKt.readLines(file, charset);
    }

    @NotNull
    public static final List<String> readLines(@NotNull File file, @NotNull Charset charset) {
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        Intrinsics.checkParameterIsNotNull(charset, "charset");
        ArrayList arrayList = new ArrayList();
        FilesKt.forEachLine(file, charset, new FilesKt__FileReadWriteKt$readLines$1(arrayList));
        return arrayList;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0057, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x005b, code lost:
        kotlin.jvm.internal.InlineMarker.finallyStart(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0062, code lost:
        if (kotlin.internal.PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0) != false) goto L_0x0064;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0064, code lost:
        kotlin.p017io.CloseableKt.closeFinally(r1, r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0068, code lost:
        if (r2 == null) goto L_0x006a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x006a, code lost:
        r1.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:?, code lost:
        r1.close();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static /* bridge */ /* synthetic */ java.lang.Object useLines$default(java.io.File r1, java.nio.charset.Charset r2, kotlin.jvm.functions.Function1 r3, int r4, java.lang.Object r5) {
        /*
            r5 = 1
            r4 = r4 & r5
            if (r4 == 0) goto L_0x0006
            java.nio.charset.Charset r2 = kotlin.text.Charsets.UTF_8
        L_0x0006:
            java.lang.String r4 = "$receiver"
            kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r1, r4)
            java.lang.String r4 = "charset"
            kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r2, r4)
            java.lang.String r4 = "block"
            kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r3, r4)
            r4 = 8192(0x2000, float:1.14794E-41)
            java.io.FileInputStream r0 = new java.io.FileInputStream
            r0.<init>(r1)
            java.io.InputStream r0 = (java.io.InputStream) r0
            java.io.InputStreamReader r1 = new java.io.InputStreamReader
            r1.<init>(r0, r2)
            java.io.Reader r1 = (java.io.Reader) r1
            boolean r2 = r1 instanceof java.io.BufferedReader
            if (r2 == 0) goto L_0x002c
            java.io.BufferedReader r1 = (java.io.BufferedReader) r1
            goto L_0x0032
        L_0x002c:
            java.io.BufferedReader r2 = new java.io.BufferedReader
            r2.<init>(r1, r4)
            r1 = r2
        L_0x0032:
            java.io.Closeable r1 = (java.io.Closeable) r1
            r2 = 0
            java.lang.Throwable r2 = (java.lang.Throwable) r2
            r4 = 0
            r0 = r1
            java.io.BufferedReader r0 = (java.io.BufferedReader) r0     // Catch:{ Throwable -> 0x0059 }
            kotlin.sequences.Sequence r0 = kotlin.p017io.TextStreamsKt.lineSequence(r0)     // Catch:{ Throwable -> 0x0059 }
            java.lang.Object r3 = r3.invoke(r0)     // Catch:{ Throwable -> 0x0059 }
            kotlin.jvm.internal.InlineMarker.finallyStart(r5)
            boolean r4 = kotlin.internal.PlatformImplementationsKt.apiVersionIsAtLeast(r5, r5, r4)
            if (r4 == 0) goto L_0x0050
            kotlin.p017io.CloseableKt.closeFinally(r1, r2)
            goto L_0x0053
        L_0x0050:
            r1.close()
        L_0x0053:
            kotlin.jvm.internal.InlineMarker.finallyEnd(r5)
            return r3
        L_0x0057:
            r3 = move-exception
            goto L_0x005b
        L_0x0059:
            r2 = move-exception
            throw r2     // Catch:{ all -> 0x0057 }
        L_0x005b:
            kotlin.jvm.internal.InlineMarker.finallyStart(r5)
            boolean r4 = kotlin.internal.PlatformImplementationsKt.apiVersionIsAtLeast(r5, r5, r4)
            if (r4 == 0) goto L_0x0068
            kotlin.p017io.CloseableKt.closeFinally(r1, r2)
            goto L_0x0071
        L_0x0068:
            if (r2 != 0) goto L_0x006e
            r1.close()
            goto L_0x0071
        L_0x006e:
            r1.close()     // Catch:{ Throwable -> 0x0071 }
        L_0x0071:
            kotlin.jvm.internal.InlineMarker.finallyEnd(r5)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.p017io.FilesKt__FileReadWriteKt.useLines$default(java.io.File, java.nio.charset.Charset, kotlin.jvm.functions.Function1, int, java.lang.Object):java.lang.Object");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0052, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0056, code lost:
        kotlin.jvm.internal.InlineMarker.finallyStart(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x005d, code lost:
        if (kotlin.internal.PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0) != false) goto L_0x005f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x005f, code lost:
        kotlin.p017io.CloseableKt.closeFinally(r3, r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0063, code lost:
        if (r4 == null) goto L_0x0065;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0065, code lost:
        r3.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:?, code lost:
        r3.close();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final <T> T useLines(@org.jetbrains.annotations.NotNull java.io.File r3, @org.jetbrains.annotations.NotNull java.nio.charset.Charset r4, @org.jetbrains.annotations.NotNull kotlin.jvm.functions.Function1<? super kotlin.sequences.Sequence<java.lang.String>, ? extends T> r5) {
        /*
            java.lang.String r0 = "$receiver"
            kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r3, r0)
            java.lang.String r0 = "charset"
            kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r4, r0)
            java.lang.String r0 = "block"
            kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r5, r0)
            java.io.FileInputStream r0 = new java.io.FileInputStream
            r0.<init>(r3)
            java.io.InputStream r0 = (java.io.InputStream) r0
            java.io.InputStreamReader r3 = new java.io.InputStreamReader
            r3.<init>(r0, r4)
            java.io.Reader r3 = (java.io.Reader) r3
            boolean r4 = r3 instanceof java.io.BufferedReader
            if (r4 == 0) goto L_0x0024
            java.io.BufferedReader r3 = (java.io.BufferedReader) r3
            goto L_0x002c
        L_0x0024:
            java.io.BufferedReader r4 = new java.io.BufferedReader
            r0 = 8192(0x2000, float:1.14794E-41)
            r4.<init>(r3, r0)
            r3 = r4
        L_0x002c:
            java.io.Closeable r3 = (java.io.Closeable) r3
            r4 = 0
            java.lang.Throwable r4 = (java.lang.Throwable) r4
            r0 = 0
            r1 = 1
            r2 = r3
            java.io.BufferedReader r2 = (java.io.BufferedReader) r2     // Catch:{ Throwable -> 0x0054 }
            kotlin.sequences.Sequence r2 = kotlin.p017io.TextStreamsKt.lineSequence(r2)     // Catch:{ Throwable -> 0x0054 }
            java.lang.Object r5 = r5.invoke(r2)     // Catch:{ Throwable -> 0x0054 }
            kotlin.jvm.internal.InlineMarker.finallyStart(r1)
            boolean r0 = kotlin.internal.PlatformImplementationsKt.apiVersionIsAtLeast(r1, r1, r0)
            if (r0 == 0) goto L_0x004b
            kotlin.p017io.CloseableKt.closeFinally(r3, r4)
            goto L_0x004e
        L_0x004b:
            r3.close()
        L_0x004e:
            kotlin.jvm.internal.InlineMarker.finallyEnd(r1)
            return r5
        L_0x0052:
            r5 = move-exception
            goto L_0x0056
        L_0x0054:
            r4 = move-exception
            throw r4     // Catch:{ all -> 0x0052 }
        L_0x0056:
            kotlin.jvm.internal.InlineMarker.finallyStart(r1)
            boolean r0 = kotlin.internal.PlatformImplementationsKt.apiVersionIsAtLeast(r1, r1, r0)
            if (r0 == 0) goto L_0x0063
            kotlin.p017io.CloseableKt.closeFinally(r3, r4)
            goto L_0x006c
        L_0x0063:
            if (r4 != 0) goto L_0x0069
            r3.close()
            goto L_0x006c
        L_0x0069:
            r3.close()     // Catch:{ Throwable -> 0x006c }
        L_0x006c:
            kotlin.jvm.internal.InlineMarker.finallyEnd(r1)
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.p017io.FilesKt__FileReadWriteKt.useLines(java.io.File, java.nio.charset.Charset, kotlin.jvm.functions.Function1):java.lang.Object");
    }
}
