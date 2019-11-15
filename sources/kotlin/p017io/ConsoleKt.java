package kotlin.p017io;

import java.io.InputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.internal.InlineOnly;
import kotlin.jvm.JvmName;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.PropertyReference0Impl;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000d\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\u0010\u000b\n\u0002\u0010\u0005\n\u0002\u0010\f\n\u0002\u0010\u0019\n\u0002\u0010\u0006\n\u0002\u0010\u0007\n\u0002\u0010\t\n\u0002\u0010\n\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\b\u001a\u0011\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\rH\b\u001a\u0011\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u000eH\b\u001a\u0011\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u000fH\b\u001a\u0011\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0010H\b\u001a\u0011\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0011H\b\u001a\u0011\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0012H\b\u001a\u0011\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0001H\b\u001a\u0011\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0013H\b\u001a\u0011\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0014H\b\u001a\t\u0010\u0015\u001a\u00020\nH\b\u001a\u0013\u0010\u0015\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\b\u001a\u0011\u0010\u0015\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\rH\b\u001a\u0011\u0010\u0015\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u000eH\b\u001a\u0011\u0010\u0015\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u000fH\b\u001a\u0011\u0010\u0015\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0010H\b\u001a\u0011\u0010\u0015\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0011H\b\u001a\u0011\u0010\u0015\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0012H\b\u001a\u0011\u0010\u0015\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0001H\b\u001a\u0011\u0010\u0015\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0013H\b\u001a\u0011\u0010\u0015\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0014H\b\u001a\b\u0010\u0016\u001a\u0004\u0018\u00010\u0017\u001a\u001a\u0010\u0016\u001a\u0004\u0018\u00010\u00172\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u0003\u001a\u00020\u0004H\u0000\u001a\f\u0010\u001a\u001a\u00020\r*\u00020\u001bH\u0002\u001a\f\u0010\u001c\u001a\u00020\u000f*\u00020\u001bH\u0002\u001a\f\u0010\u001d\u001a\u00020\n*\u00020\u001eH\u0002\u001a$\u0010\u001f\u001a\u00020\r*\u00020\u00042\u0006\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020\u001b2\u0006\u0010#\u001a\u00020\rH\u0002\"\u000e\u0010\u0000\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u001b\u0010\u0003\u001a\u00020\u00048BX\u0002¢\u0006\f\n\u0004\b\u0007\u0010\b\u001a\u0004\b\u0005\u0010\u0006¨\u0006$"}, mo14495d2 = {"BUFFER_SIZE", "", "LINE_SEPARATOR_MAX_LENGTH", "decoder", "Ljava/nio/charset/CharsetDecoder;", "getDecoder", "()Ljava/nio/charset/CharsetDecoder;", "decoder$delegate", "Lkotlin/Lazy;", "print", "", "message", "", "", "", "", "", "", "", "", "", "println", "readLine", "", "inputStream", "Ljava/io/InputStream;", "containsLineSeparator", "Ljava/nio/CharBuffer;", "dequeue", "flipBack", "Ljava/nio/Buffer;", "tryDecode", "byteBuffer", "Ljava/nio/ByteBuffer;", "charBuffer", "isEndOfStream", "kotlin-stdlib"}, mo14496k = 2, mo14497mv = {1, 1, 10})
@JvmName(name = "ConsoleKt")
/* renamed from: kotlin.io.ConsoleKt */
/* compiled from: Console.kt */
public final class ConsoleKt {
    static final /* synthetic */ KProperty[] $$delegatedProperties = {Reflection.property0(new PropertyReference0Impl(Reflection.getOrCreateKotlinPackage(ConsoleKt.class, "kotlin-stdlib"), "decoder", "getDecoder()Ljava/nio/charset/CharsetDecoder;"))};
    private static final int BUFFER_SIZE = 32;
    private static final int LINE_SEPARATOR_MAX_LENGTH = 2;
    private static final Lazy decoder$delegate = LazyKt.lazy(ConsoleKt$decoder$2.INSTANCE);

    private static final CharsetDecoder getDecoder() {
        Lazy lazy = decoder$delegate;
        KProperty kProperty = $$delegatedProperties[0];
        return (CharsetDecoder) lazy.getValue();
    }

    @InlineOnly
    private static final void print(Object obj) {
        System.out.print(obj);
    }

    @InlineOnly
    private static final void print(int i) {
        System.out.print(i);
    }

    @InlineOnly
    private static final void print(long j) {
        System.out.print(j);
    }

    @InlineOnly
    private static final void print(byte b) {
        System.out.print(Byte.valueOf(b));
    }

    @InlineOnly
    private static final void print(short s) {
        System.out.print(Short.valueOf(s));
    }

    @InlineOnly
    private static final void print(char c) {
        System.out.print(c);
    }

    @InlineOnly
    private static final void print(boolean z) {
        System.out.print(z);
    }

    @InlineOnly
    private static final void print(float f) {
        System.out.print(f);
    }

    @InlineOnly
    private static final void print(double d) {
        System.out.print(d);
    }

    @InlineOnly
    private static final void print(char[] cArr) {
        System.out.print(cArr);
    }

    @InlineOnly
    private static final void println(Object obj) {
        System.out.println(obj);
    }

    @InlineOnly
    private static final void println(int i) {
        System.out.println(i);
    }

    @InlineOnly
    private static final void println(long j) {
        System.out.println(j);
    }

    @InlineOnly
    private static final void println(byte b) {
        System.out.println(Byte.valueOf(b));
    }

    @InlineOnly
    private static final void println(short s) {
        System.out.println(Short.valueOf(s));
    }

    @InlineOnly
    private static final void println(char c) {
        System.out.println(c);
    }

    @InlineOnly
    private static final void println(boolean z) {
        System.out.println(z);
    }

    @InlineOnly
    private static final void println(float f) {
        System.out.println(f);
    }

    @InlineOnly
    private static final void println(double d) {
        System.out.println(d);
    }

    @InlineOnly
    private static final void println(char[] cArr) {
        System.out.println(cArr);
    }

    @InlineOnly
    private static final void println() {
        System.out.println();
    }

    @Nullable
    public static final String readLine() {
        InputStream inputStream = System.in;
        Intrinsics.checkExpressionValueIsNotNull(inputStream, "System.`in`");
        return readLine(inputStream, getDecoder());
    }

    @Nullable
    public static final String readLine(@NotNull InputStream inputStream, @NotNull CharsetDecoder charsetDecoder) {
        Intrinsics.checkParameterIsNotNull(inputStream, "inputStream");
        Intrinsics.checkParameterIsNotNull(charsetDecoder, "decoder");
        if (!(charsetDecoder.maxCharsPerByte() <= ((float) 1))) {
            throw new IllegalArgumentException("Encodings with multiple chars per byte are not supported".toString());
        }
        ByteBuffer allocate = ByteBuffer.allocate(32);
        CharBuffer allocate2 = CharBuffer.allocate(2);
        StringBuilder sb = new StringBuilder();
        int read = inputStream.read();
        if (read == -1) {
            return null;
        }
        do {
            allocate.put((byte) read);
            Intrinsics.checkExpressionValueIsNotNull(allocate, "byteBuffer");
            Intrinsics.checkExpressionValueIsNotNull(allocate2, "charBuffer");
            if (tryDecode(charsetDecoder, allocate, allocate2, false)) {
                if (containsLineSeparator(allocate2)) {
                    break;
                } else if (!allocate2.hasRemaining()) {
                    sb.append(dequeue(allocate2));
                }
            }
            read = inputStream.read();
        } while (read != -1);
        tryDecode(charsetDecoder, allocate, allocate2, true);
        charsetDecoder.reset();
        int position = allocate2.position();
        char c = allocate2.get(0);
        char c2 = allocate2.get(1);
        switch (position) {
            case 1:
                if (c != 10) {
                    sb.append(c);
                    break;
                }
                break;
            case 2:
                if (!(c == 13 && c2 == 10)) {
                    sb.append(c);
                }
                if (c2 != 10) {
                    sb.append(c2);
                    break;
                }
                break;
        }
        return sb.toString();
    }

    private static final boolean tryDecode(@NotNull CharsetDecoder charsetDecoder, ByteBuffer byteBuffer, CharBuffer charBuffer, boolean z) {
        int position = charBuffer.position();
        byteBuffer.flip();
        CoderResult decode = charsetDecoder.decode(byteBuffer, charBuffer, z);
        if (decode.isError()) {
            decode.throwException();
        }
        boolean z2 = charBuffer.position() > position;
        if (z2) {
            byteBuffer.clear();
        } else {
            flipBack(byteBuffer);
        }
        return z2;
    }

    private static final boolean containsLineSeparator(@NotNull CharBuffer charBuffer) {
        return charBuffer.get(1) == 10 || charBuffer.get(0) == 10;
    }

    private static final void flipBack(@NotNull Buffer buffer) {
        buffer.position(buffer.limit());
        buffer.limit(buffer.capacity());
    }

    private static final char dequeue(@NotNull CharBuffer charBuffer) {
        charBuffer.flip();
        char c = charBuffer.get();
        charBuffer.compact();
        return c;
    }
}
