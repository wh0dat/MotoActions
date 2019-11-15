package kotlin.p017io;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000<\n\u0000\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\u001a(\u0010\t\u001a\u00020\u00022\b\b\u0002\u0010\n\u001a\u00020\u00012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0002\u001a(\u0010\r\u001a\u00020\u00022\b\b\u0002\u0010\n\u001a\u00020\u00012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0002\u001a8\u0010\u000e\u001a\u00020\u000f*\u00020\u00022\u0006\u0010\u0010\u001a\u00020\u00022\b\b\u0002\u0010\u0011\u001a\u00020\u000f2\u001a\b\u0002\u0010\u0012\u001a\u0014\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00150\u0013\u001a&\u0010\u0016\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0010\u001a\u00020\u00022\b\b\u0002\u0010\u0011\u001a\u00020\u000f2\b\b\u0002\u0010\u0017\u001a\u00020\u0018\u001a\n\u0010\u0019\u001a\u00020\u000f*\u00020\u0002\u001a\u0012\u0010\u001a\u001a\u00020\u000f*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u0002\u001a\u0012\u0010\u001a\u001a\u00020\u000f*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u0001\u001a\n\u0010\u001c\u001a\u00020\u0002*\u00020\u0002\u001a\u001d\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00020\u001d*\b\u0012\u0004\u0012\u00020\u00020\u001dH\u0002¢\u0006\u0002\b\u001e\u001a\u0011\u0010\u001c\u001a\u00020\u001f*\u00020\u001fH\u0002¢\u0006\u0002\b\u001e\u001a\u0012\u0010 \u001a\u00020\u0002*\u00020\u00022\u0006\u0010!\u001a\u00020\u0002\u001a\u0014\u0010\"\u001a\u0004\u0018\u00010\u0002*\u00020\u00022\u0006\u0010!\u001a\u00020\u0002\u001a\u0012\u0010#\u001a\u00020\u0002*\u00020\u00022\u0006\u0010!\u001a\u00020\u0002\u001a\u0012\u0010$\u001a\u00020\u0002*\u00020\u00022\u0006\u0010%\u001a\u00020\u0002\u001a\u0012\u0010$\u001a\u00020\u0002*\u00020\u00022\u0006\u0010%\u001a\u00020\u0001\u001a\u0012\u0010&\u001a\u00020\u0002*\u00020\u00022\u0006\u0010%\u001a\u00020\u0002\u001a\u0012\u0010&\u001a\u00020\u0002*\u00020\u00022\u0006\u0010%\u001a\u00020\u0001\u001a\u0012\u0010'\u001a\u00020\u000f*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u0002\u001a\u0012\u0010'\u001a\u00020\u000f*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u0001\u001a\u0012\u0010(\u001a\u00020\u0001*\u00020\u00022\u0006\u0010!\u001a\u00020\u0002\u001a\u001b\u0010)\u001a\u0004\u0018\u00010\u0001*\u00020\u00022\u0006\u0010!\u001a\u00020\u0002H\u0002¢\u0006\u0002\b*\"\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00028F¢\u0006\u0006\u001a\u0004\b\u0003\u0010\u0004\"\u0015\u0010\u0005\u001a\u00020\u0001*\u00020\u00028F¢\u0006\u0006\u001a\u0004\b\u0006\u0010\u0004\"\u0015\u0010\u0007\u001a\u00020\u0001*\u00020\u00028F¢\u0006\u0006\u001a\u0004\b\b\u0010\u0004¨\u0006+"}, mo14495d2 = {"extension", "", "Ljava/io/File;", "getExtension", "(Ljava/io/File;)Ljava/lang/String;", "invariantSeparatorsPath", "getInvariantSeparatorsPath", "nameWithoutExtension", "getNameWithoutExtension", "createTempDir", "prefix", "suffix", "directory", "createTempFile", "copyRecursively", "", "target", "overwrite", "onError", "Lkotlin/Function2;", "Ljava/io/IOException;", "Lkotlin/io/OnErrorAction;", "copyTo", "bufferSize", "", "deleteRecursively", "endsWith", "other", "normalize", "", "normalize$FilesKt__UtilsKt", "Lkotlin/io/FilePathComponents;", "relativeTo", "base", "relativeToOrNull", "relativeToOrSelf", "resolve", "relative", "resolveSibling", "startsWith", "toRelativeString", "toRelativeStringOrNull", "toRelativeStringOrNull$FilesKt__UtilsKt", "kotlin-stdlib"}, mo14496k = 5, mo14497mv = {1, 1, 10}, mo14499xi = 1, mo14500xs = "kotlin/io/FilesKt")
/* renamed from: kotlin.io.FilesKt__UtilsKt */
/* compiled from: Utils.kt */
class FilesKt__UtilsKt extends FilesKt__FileTreeWalkKt {
    @NotNull
    public static /* bridge */ /* synthetic */ File createTempDir$default(String str, String str2, File file, int i, Object obj) {
        if ((i & 1) != 0) {
            str = "tmp";
        }
        if ((i & 2) != 0) {
            str2 = null;
        }
        if ((i & 4) != 0) {
            file = null;
        }
        return FilesKt.createTempDir(str, str2, file);
    }

    @NotNull
    public static final File createTempDir(@NotNull String str, @Nullable String str2, @Nullable File file) {
        Intrinsics.checkParameterIsNotNull(str, "prefix");
        File createTempFile = File.createTempFile(str, str2, file);
        createTempFile.delete();
        if (createTempFile.mkdir()) {
            Intrinsics.checkExpressionValueIsNotNull(createTempFile, "dir");
            return createTempFile;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Unable to create temporary directory ");
        sb.append(createTempFile);
        sb.append('.');
        throw new IOException(sb.toString());
    }

    @NotNull
    public static /* bridge */ /* synthetic */ File createTempFile$default(String str, String str2, File file, int i, Object obj) {
        if ((i & 1) != 0) {
            str = "tmp";
        }
        if ((i & 2) != 0) {
            str2 = null;
        }
        if ((i & 4) != 0) {
            file = null;
        }
        return FilesKt.createTempFile(str, str2, file);
    }

    @NotNull
    public static final File createTempFile(@NotNull String str, @Nullable String str2, @Nullable File file) {
        Intrinsics.checkParameterIsNotNull(str, "prefix");
        File createTempFile = File.createTempFile(str, str2, file);
        Intrinsics.checkExpressionValueIsNotNull(createTempFile, "File.createTempFile(prefix, suffix, directory)");
        return createTempFile;
    }

    @NotNull
    public static final String getExtension(@NotNull File file) {
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        String name = file.getName();
        Intrinsics.checkExpressionValueIsNotNull(name, "name");
        return StringsKt.substringAfterLast(name, '.', "");
    }

    @NotNull
    public static final String getInvariantSeparatorsPath(@NotNull File file) {
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        if (File.separatorChar != '/') {
            String path = file.getPath();
            Intrinsics.checkExpressionValueIsNotNull(path, "path");
            return StringsKt.replace$default(path, File.separatorChar, '/', false, 4, (Object) null);
        }
        String path2 = file.getPath();
        Intrinsics.checkExpressionValueIsNotNull(path2, "path");
        return path2;
    }

    @NotNull
    public static final String getNameWithoutExtension(@NotNull File file) {
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        String name = file.getName();
        Intrinsics.checkExpressionValueIsNotNull(name, "name");
        return StringsKt.substringBeforeLast$default(name, ".", (String) null, 2, (Object) null);
    }

    @NotNull
    public static final String toRelativeString(@NotNull File file, @NotNull File file2) {
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        Intrinsics.checkParameterIsNotNull(file2, "base");
        String relativeStringOrNull$FilesKt__UtilsKt = toRelativeStringOrNull$FilesKt__UtilsKt(file, file2);
        if (relativeStringOrNull$FilesKt__UtilsKt != null) {
            return relativeStringOrNull$FilesKt__UtilsKt;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("this and base files have different roots: ");
        sb.append(file);
        sb.append(" and ");
        sb.append(file2);
        sb.append('.');
        throw new IllegalArgumentException(sb.toString());
    }

    @NotNull
    public static final File relativeTo(@NotNull File file, @NotNull File file2) {
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        Intrinsics.checkParameterIsNotNull(file2, "base");
        return new File(FilesKt.toRelativeString(file, file2));
    }

    @NotNull
    public static final File relativeToOrSelf(@NotNull File file, @NotNull File file2) {
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        Intrinsics.checkParameterIsNotNull(file2, "base");
        String relativeStringOrNull$FilesKt__UtilsKt = toRelativeStringOrNull$FilesKt__UtilsKt(file, file2);
        return relativeStringOrNull$FilesKt__UtilsKt != null ? new File(relativeStringOrNull$FilesKt__UtilsKt) : file;
    }

    @Nullable
    public static final File relativeToOrNull(@NotNull File file, @NotNull File file2) {
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        Intrinsics.checkParameterIsNotNull(file2, "base");
        String relativeStringOrNull$FilesKt__UtilsKt = toRelativeStringOrNull$FilesKt__UtilsKt(file, file2);
        if (relativeStringOrNull$FilesKt__UtilsKt != null) {
            return new File(relativeStringOrNull$FilesKt__UtilsKt);
        }
        return null;
    }

    private static final String toRelativeStringOrNull$FilesKt__UtilsKt(@NotNull File file, File file2) {
        FilePathComponents normalize$FilesKt__UtilsKt = normalize$FilesKt__UtilsKt(FilesKt.toComponents(file));
        FilePathComponents normalize$FilesKt__UtilsKt2 = normalize$FilesKt__UtilsKt(FilesKt.toComponents(file2));
        if (!Intrinsics.areEqual((Object) normalize$FilesKt__UtilsKt.getRoot(), (Object) normalize$FilesKt__UtilsKt2.getRoot())) {
            return null;
        }
        int size = normalize$FilesKt__UtilsKt2.getSize();
        int size2 = normalize$FilesKt__UtilsKt.getSize();
        int i = 0;
        int min = Math.min(size2, size);
        while (i < min && Intrinsics.areEqual((Object) (File) normalize$FilesKt__UtilsKt.getSegments().get(i), (Object) (File) normalize$FilesKt__UtilsKt2.getSegments().get(i))) {
            i++;
        }
        StringBuilder sb = new StringBuilder();
        int i2 = size - 1;
        if (i2 >= i) {
            while (!Intrinsics.areEqual((Object) ((File) normalize$FilesKt__UtilsKt2.getSegments().get(i2)).getName(), (Object) "..")) {
                sb.append("..");
                if (i2 != i) {
                    sb.append(File.separatorChar);
                }
                if (i2 != i) {
                    i2--;
                }
            }
            return null;
        }
        if (i < size2) {
            if (i < size) {
                sb.append(File.separatorChar);
            }
            Iterable drop = CollectionsKt.drop(normalize$FilesKt__UtilsKt.getSegments(), i);
            Appendable appendable = sb;
            String str = File.separator;
            Intrinsics.checkExpressionValueIsNotNull(str, "File.separator");
            CollectionsKt.joinTo$default(drop, appendable, str, null, null, 0, null, null, 124, null);
        }
        return sb.toString();
    }

    @NotNull
    public static /* bridge */ /* synthetic */ File copyTo$default(File file, File file2, boolean z, int i, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            z = false;
        }
        if ((i2 & 4) != 0) {
            i = 8192;
        }
        return FilesKt.copyTo(file, file2, z, i);
    }

    @NotNull
    public static final File copyTo(@NotNull File file, @NotNull File file2, boolean z, int i) {
        Closeable fileOutputStream;
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        Intrinsics.checkParameterIsNotNull(file2, "target");
        if (!file.exists()) {
            NoSuchFileException noSuchFileException = new NoSuchFileException(file, null, "The source file doesn't exist.", 2, null);
            throw noSuchFileException;
        }
        if (file2.exists()) {
            boolean z2 = true;
            if (z && file2.delete()) {
                z2 = false;
            }
            if (z2) {
                throw new FileAlreadyExistsException(file, file2, "The destination file already exists.");
            }
        }
        if (!file.isDirectory()) {
            File parentFile = file2.getParentFile();
            if (parentFile != null) {
                parentFile.mkdirs();
            }
            Closeable fileInputStream = new FileInputStream(file);
            Throwable th = null;
            try {
                FileInputStream fileInputStream2 = (FileInputStream) fileInputStream;
                fileOutputStream = new FileOutputStream(file2);
                Throwable th2 = null;
                ByteStreamsKt.copyTo(fileInputStream2, (FileOutputStream) fileOutputStream, i);
                CloseableKt.closeFinally(fileOutputStream, th2);
                CloseableKt.closeFinally(fileInputStream, th);
            } catch (Throwable th3) {
                Throwable th4 = th3;
                try {
                    throw th4;
                } catch (Throwable th5) {
                    CloseableKt.closeFinally(fileInputStream, th4);
                    throw th5;
                }
            }
        } else if (!file2.mkdirs()) {
            throw new FileSystemException(file, file2, "Failed to create target directory.");
        }
        return file2;
    }

    public static /* bridge */ /* synthetic */ boolean copyRecursively$default(File file, File file2, boolean z, Function2 function2, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        if ((i & 4) != 0) {
            function2 = FilesKt__UtilsKt$copyRecursively$1.INSTANCE;
        }
        return FilesKt.copyRecursively(file, file2, z, function2);
    }

    /* JADX WARNING: Removed duplicated region for block: B:33:0x00a3 A[Catch:{ TerminateException -> 0x00e7 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final boolean copyRecursively(@org.jetbrains.annotations.NotNull java.io.File r11, @org.jetbrains.annotations.NotNull java.io.File r12, boolean r13, @org.jetbrains.annotations.NotNull kotlin.jvm.functions.Function2<? super java.io.File, ? super java.io.IOException, ? extends kotlin.p017io.OnErrorAction> r14) {
        /*
            java.lang.String r0 = "$receiver"
            kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r11, r0)
            java.lang.String r0 = "target"
            kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r12, r0)
            java.lang.String r0 = "onError"
            kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r14, r0)
            boolean r0 = r11.exists()
            r1 = 1
            r2 = 0
            if (r0 != 0) goto L_0x0030
            kotlin.io.NoSuchFileException r12 = new kotlin.io.NoSuchFileException
            r5 = 0
            java.lang.String r6 = "The source file doesn't exist."
            r7 = 2
            r8 = 0
            r3 = r12
            r4 = r11
            r3.<init>(r4, r5, r6, r7, r8)
            java.lang.Object r11 = r14.invoke(r11, r12)
            kotlin.io.OnErrorAction r11 = (kotlin.p017io.OnErrorAction) r11
            kotlin.io.OnErrorAction r12 = kotlin.p017io.OnErrorAction.TERMINATE
            if (r11 == r12) goto L_0x002e
            goto L_0x002f
        L_0x002e:
            r1 = r2
        L_0x002f:
            return r1
        L_0x0030:
            kotlin.io.FileTreeWalk r0 = kotlin.p017io.FilesKt.walkTopDown(r11)     // Catch:{ TerminateException -> 0x00e7 }
            kotlin.io.FilesKt__UtilsKt$copyRecursively$2 r3 = new kotlin.io.FilesKt__UtilsKt$copyRecursively$2     // Catch:{ TerminateException -> 0x00e7 }
            r3.<init>(r14)     // Catch:{ TerminateException -> 0x00e7 }
            kotlin.jvm.functions.Function2 r3 = (kotlin.jvm.functions.Function2) r3     // Catch:{ TerminateException -> 0x00e7 }
            kotlin.io.FileTreeWalk r0 = r0.onFail(r3)     // Catch:{ TerminateException -> 0x00e7 }
            java.util.Iterator r0 = r0.iterator()     // Catch:{ TerminateException -> 0x00e7 }
        L_0x0043:
            boolean r3 = r0.hasNext()     // Catch:{ TerminateException -> 0x00e7 }
            if (r3 == 0) goto L_0x00e6
            java.lang.Object r3 = r0.next()     // Catch:{ TerminateException -> 0x00e7 }
            java.io.File r3 = (java.io.File) r3     // Catch:{ TerminateException -> 0x00e7 }
            boolean r4 = r3.exists()     // Catch:{ TerminateException -> 0x00e7 }
            if (r4 != 0) goto L_0x006c
            kotlin.io.NoSuchFileException r10 = new kotlin.io.NoSuchFileException     // Catch:{ TerminateException -> 0x00e7 }
            r6 = 0
            java.lang.String r7 = "The source file doesn't exist."
            r8 = 2
            r9 = 0
            r4 = r10
            r5 = r3
            r4.<init>(r5, r6, r7, r8, r9)     // Catch:{ TerminateException -> 0x00e7 }
            java.lang.Object r3 = r14.invoke(r3, r10)     // Catch:{ TerminateException -> 0x00e7 }
            kotlin.io.OnErrorAction r3 = (kotlin.p017io.OnErrorAction) r3     // Catch:{ TerminateException -> 0x00e7 }
            kotlin.io.OnErrorAction r4 = kotlin.p017io.OnErrorAction.TERMINATE     // Catch:{ TerminateException -> 0x00e7 }
            if (r3 != r4) goto L_0x0043
            return r2
        L_0x006c:
            java.lang.String r4 = kotlin.p017io.FilesKt.toRelativeString(r3, r11)     // Catch:{ TerminateException -> 0x00e7 }
            java.io.File r5 = new java.io.File     // Catch:{ TerminateException -> 0x00e7 }
            r5.<init>(r12, r4)     // Catch:{ TerminateException -> 0x00e7 }
            boolean r4 = r5.exists()     // Catch:{ TerminateException -> 0x00e7 }
            if (r4 == 0) goto L_0x00b5
            boolean r4 = r3.isDirectory()     // Catch:{ TerminateException -> 0x00e7 }
            if (r4 == 0) goto L_0x0087
            boolean r4 = r5.isDirectory()     // Catch:{ TerminateException -> 0x00e7 }
            if (r4 != 0) goto L_0x00b5
        L_0x0087:
            if (r13 != 0) goto L_0x008b
        L_0x0089:
            r4 = r1
            goto L_0x00a1
        L_0x008b:
            boolean r4 = r5.isDirectory()     // Catch:{ TerminateException -> 0x00e7 }
            if (r4 == 0) goto L_0x009a
            boolean r4 = kotlin.p017io.FilesKt.deleteRecursively(r5)     // Catch:{ TerminateException -> 0x00e7 }
            if (r4 != 0) goto L_0x0098
            goto L_0x0089
        L_0x0098:
            r4 = r2
            goto L_0x00a1
        L_0x009a:
            boolean r4 = r5.delete()     // Catch:{ TerminateException -> 0x00e7 }
            if (r4 != 0) goto L_0x0098
            goto L_0x0089
        L_0x00a1:
            if (r4 == 0) goto L_0x00b5
            kotlin.io.FileAlreadyExistsException r4 = new kotlin.io.FileAlreadyExistsException     // Catch:{ TerminateException -> 0x00e7 }
            java.lang.String r6 = "The destination file already exists."
            r4.<init>(r3, r5, r6)     // Catch:{ TerminateException -> 0x00e7 }
            java.lang.Object r3 = r14.invoke(r5, r4)     // Catch:{ TerminateException -> 0x00e7 }
            kotlin.io.OnErrorAction r3 = (kotlin.p017io.OnErrorAction) r3     // Catch:{ TerminateException -> 0x00e7 }
            kotlin.io.OnErrorAction r4 = kotlin.p017io.OnErrorAction.TERMINATE     // Catch:{ TerminateException -> 0x00e7 }
            if (r3 != r4) goto L_0x0043
            return r2
        L_0x00b5:
            boolean r4 = r3.isDirectory()     // Catch:{ TerminateException -> 0x00e7 }
            if (r4 == 0) goto L_0x00bf
            r5.mkdirs()     // Catch:{ TerminateException -> 0x00e7 }
            goto L_0x0043
        L_0x00bf:
            r7 = 0
            r8 = 4
            r9 = 0
            r4 = r3
            r6 = r13
            java.io.File r4 = kotlin.p017io.FilesKt.copyTo$default(r4, r5, r6, r7, r8, r9)     // Catch:{ TerminateException -> 0x00e7 }
            long r4 = r4.length()     // Catch:{ TerminateException -> 0x00e7 }
            long r6 = r3.length()     // Catch:{ TerminateException -> 0x00e7 }
            int r4 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r4 == 0) goto L_0x0043
            java.io.IOException r4 = new java.io.IOException     // Catch:{ TerminateException -> 0x00e7 }
            java.lang.String r5 = "Source file wasn't copied completely, length of destination file differs."
            r4.<init>(r5)     // Catch:{ TerminateException -> 0x00e7 }
            java.lang.Object r3 = r14.invoke(r3, r4)     // Catch:{ TerminateException -> 0x00e7 }
            kotlin.io.OnErrorAction r3 = (kotlin.p017io.OnErrorAction) r3     // Catch:{ TerminateException -> 0x00e7 }
            kotlin.io.OnErrorAction r4 = kotlin.p017io.OnErrorAction.TERMINATE     // Catch:{ TerminateException -> 0x00e7 }
            if (r3 != r4) goto L_0x0043
            return r2
        L_0x00e6:
            return r1
        L_0x00e7:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.p017io.FilesKt__UtilsKt.copyRecursively(java.io.File, java.io.File, boolean, kotlin.jvm.functions.Function2):boolean");
    }

    public static final boolean deleteRecursively(@NotNull File file) {
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        Iterator it = FilesKt.walkBottomUp(file).iterator();
        while (true) {
            boolean z = true;
            while (true) {
                if (!it.hasNext()) {
                    return z;
                }
                File file2 = (File) it.next();
                if (file2.delete() || !file2.exists()) {
                    if (z) {
                    }
                }
                z = false;
            }
        }
    }

    public static final boolean startsWith(@NotNull File file, @NotNull File file2) {
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        Intrinsics.checkParameterIsNotNull(file2, "other");
        FilePathComponents components = FilesKt.toComponents(file);
        FilePathComponents components2 = FilesKt.toComponents(file2);
        boolean z = false;
        if (!Intrinsics.areEqual((Object) components.getRoot(), (Object) components2.getRoot())) {
            return false;
        }
        if (components.getSize() >= components2.getSize()) {
            z = components.getSegments().subList(0, components2.getSize()).equals(components2.getSegments());
        }
        return z;
    }

    public static final boolean startsWith(@NotNull File file, @NotNull String str) {
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        Intrinsics.checkParameterIsNotNull(str, "other");
        return FilesKt.startsWith(file, new File(str));
    }

    public static final boolean endsWith(@NotNull File file, @NotNull File file2) {
        boolean z;
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        Intrinsics.checkParameterIsNotNull(file2, "other");
        FilePathComponents components = FilesKt.toComponents(file);
        FilePathComponents components2 = FilesKt.toComponents(file2);
        if (components2.isRooted()) {
            return Intrinsics.areEqual((Object) file, (Object) file2);
        }
        int size = components.getSize() - components2.getSize();
        if (size < 0) {
            z = false;
        } else {
            z = components.getSegments().subList(size, components.getSize()).equals(components2.getSegments());
        }
        return z;
    }

    public static final boolean endsWith(@NotNull File file, @NotNull String str) {
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        Intrinsics.checkParameterIsNotNull(str, "other");
        return FilesKt.endsWith(file, new File(str));
    }

    @NotNull
    public static final File normalize(@NotNull File file) {
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        FilePathComponents components = FilesKt.toComponents(file);
        File root = components.getRoot();
        Iterable normalize$FilesKt__UtilsKt = normalize$FilesKt__UtilsKt(components.getSegments());
        String str = File.separator;
        Intrinsics.checkExpressionValueIsNotNull(str, "File.separator");
        return FilesKt.resolve(root, CollectionsKt.joinToString$default(normalize$FilesKt__UtilsKt, str, null, null, 0, null, null, 62, null));
    }

    private static final FilePathComponents normalize$FilesKt__UtilsKt(@NotNull FilePathComponents filePathComponents) {
        return new FilePathComponents(filePathComponents.getRoot(), normalize$FilesKt__UtilsKt(filePathComponents.getSegments()));
    }

    private static final List<File> normalize$FilesKt__UtilsKt(@NotNull List<? extends File> list) {
        List<File> arrayList = new ArrayList<>(list.size());
        for (File file : list) {
            String name = file.getName();
            if (name != null) {
                int hashCode = name.hashCode();
                if (hashCode != 46) {
                    if (hashCode == 1472 && name.equals("..")) {
                        if (arrayList.isEmpty() || !(!Intrinsics.areEqual((Object) ((File) CollectionsKt.last(arrayList)).getName(), (Object) ".."))) {
                            arrayList.add(file);
                        } else {
                            arrayList.remove(arrayList.size() - 1);
                        }
                    }
                } else if (name.equals(".")) {
                }
            }
            arrayList.add(file);
        }
        return arrayList;
    }

    @NotNull
    public static final File resolve(@NotNull File file, @NotNull File file2) {
        File file3;
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        Intrinsics.checkParameterIsNotNull(file2, "relative");
        if (FilesKt.isRooted(file2)) {
            return file2;
        }
        String file4 = file.toString();
        Intrinsics.checkExpressionValueIsNotNull(file4, "baseName");
        CharSequence charSequence = file4;
        if ((charSequence.length() == 0) || StringsKt.endsWith$default(charSequence, File.separatorChar, false, 2, (Object) null)) {
            StringBuilder sb = new StringBuilder();
            sb.append(file4);
            sb.append(file2);
            file3 = new File(sb.toString());
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(file4);
            sb2.append(File.separatorChar);
            sb2.append(file2);
            file3 = new File(sb2.toString());
        }
        return file3;
    }

    @NotNull
    public static final File resolve(@NotNull File file, @NotNull String str) {
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        Intrinsics.checkParameterIsNotNull(str, "relative");
        return FilesKt.resolve(file, new File(str));
    }

    @NotNull
    public static final File resolveSibling(@NotNull File file, @NotNull File file2) {
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        Intrinsics.checkParameterIsNotNull(file2, "relative");
        FilePathComponents components = FilesKt.toComponents(file);
        return FilesKt.resolve(FilesKt.resolve(components.getRoot(), components.getSize() == 0 ? new File("..") : components.subPath(0, components.getSize() - 1)), file2);
    }

    @NotNull
    public static final File resolveSibling(@NotNull File file, @NotNull String str) {
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        Intrinsics.checkParameterIsNotNull(str, "relative");
        return FilesKt.resolveSibling(file, new File(str));
    }
}
