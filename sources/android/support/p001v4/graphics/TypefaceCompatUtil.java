package android.support.p001v4.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.os.Process;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.util.Log;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

@RestrictTo({Scope.LIBRARY_GROUP})
/* renamed from: android.support.v4.graphics.TypefaceCompatUtil */
public class TypefaceCompatUtil {
    private static final String CACHE_FILE_PREFIX = ".font";
    private static final String TAG = "TypefaceCompatUtil";

    private TypefaceCompatUtil() {
    }

    public static File getTempFile(Context context) {
        StringBuilder sb = new StringBuilder();
        sb.append(CACHE_FILE_PREFIX);
        sb.append(Process.myPid());
        sb.append("-");
        sb.append(Process.myTid());
        sb.append("-");
        String sb2 = sb.toString();
        int i = 0;
        while (i < 100) {
            File cacheDir = context.getCacheDir();
            StringBuilder sb3 = new StringBuilder();
            sb3.append(sb2);
            sb3.append(i);
            File file = new File(cacheDir, sb3.toString());
            try {
                if (file.createNewFile()) {
                    return file;
                }
                i++;
            } catch (IOException unused) {
            }
        }
        return null;
    }

    @RequiresApi(19)
    private static ByteBuffer mmap(File file) {
        Throwable th;
        Throwable th2;
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            try {
                FileChannel channel = fileInputStream.getChannel();
                MappedByteBuffer map = channel.map(MapMode.READ_ONLY, 0, channel.size());
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                return map;
            } catch (Throwable th3) {
                Throwable th4 = th3;
                th2 = r9;
                th = th4;
            }
            throw th;
            if (fileInputStream != null) {
                if (th2 != null) {
                    try {
                        fileInputStream.close();
                    } catch (Throwable th5) {
                        th2.addSuppressed(th5);
                    }
                } else {
                    fileInputStream.close();
                }
            }
            throw th;
        } catch (IOException unused) {
            return null;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0049, code lost:
        r9 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x004a, code lost:
        r10 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x004e, code lost:
        r10 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x004f, code lost:
        r7 = r10;
        r10 = r9;
        r9 = r7;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0049 A[ExcHandler: all (th java.lang.Throwable), Splitter:B:3:0x000b] */
    @android.support.annotation.RequiresApi(19)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.nio.ByteBuffer mmap(android.content.Context r8, android.os.CancellationSignal r9, android.net.Uri r10) {
        /*
            android.content.ContentResolver r8 = r8.getContentResolver()
            r0 = 0
            java.lang.String r1 = "r"
            android.os.ParcelFileDescriptor r8 = r8.openFileDescriptor(r10, r1, r9)     // Catch:{ IOException -> 0x0063 }
            java.io.FileInputStream r9 = new java.io.FileInputStream     // Catch:{ Throwable -> 0x004c, all -> 0x0049 }
            java.io.FileDescriptor r10 = r8.getFileDescriptor()     // Catch:{ Throwable -> 0x004c, all -> 0x0049 }
            r9.<init>(r10)     // Catch:{ Throwable -> 0x004c, all -> 0x0049 }
            java.nio.channels.FileChannel r1 = r9.getChannel()     // Catch:{ Throwable -> 0x0032, all -> 0x002f }
            long r5 = r1.size()     // Catch:{ Throwable -> 0x0032, all -> 0x002f }
            java.nio.channels.FileChannel$MapMode r2 = java.nio.channels.FileChannel.MapMode.READ_ONLY     // Catch:{ Throwable -> 0x0032, all -> 0x002f }
            r3 = 0
            java.nio.MappedByteBuffer r10 = r1.map(r2, r3, r5)     // Catch:{ Throwable -> 0x0032, all -> 0x002f }
            if (r9 == 0) goto L_0x0029
            r9.close()     // Catch:{ Throwable -> 0x004c, all -> 0x0049 }
        L_0x0029:
            if (r8 == 0) goto L_0x002e
            r8.close()     // Catch:{ IOException -> 0x0063 }
        L_0x002e:
            return r10
        L_0x002f:
            r10 = move-exception
            r1 = r0
            goto L_0x0038
        L_0x0032:
            r10 = move-exception
            throw r10     // Catch:{ all -> 0x0034 }
        L_0x0034:
            r1 = move-exception
            r7 = r1
            r1 = r10
            r10 = r7
        L_0x0038:
            if (r9 == 0) goto L_0x0048
            if (r1 == 0) goto L_0x0045
            r9.close()     // Catch:{ Throwable -> 0x0040, all -> 0x0049 }
            goto L_0x0048
        L_0x0040:
            r9 = move-exception
            r1.addSuppressed(r9)     // Catch:{ Throwable -> 0x004c, all -> 0x0049 }
            goto L_0x0048
        L_0x0045:
            r9.close()     // Catch:{ Throwable -> 0x004c, all -> 0x0049 }
        L_0x0048:
            throw r10     // Catch:{ Throwable -> 0x004c, all -> 0x0049 }
        L_0x0049:
            r9 = move-exception
            r10 = r0
            goto L_0x0052
        L_0x004c:
            r9 = move-exception
            throw r9     // Catch:{ all -> 0x004e }
        L_0x004e:
            r10 = move-exception
            r7 = r10
            r10 = r9
            r9 = r7
        L_0x0052:
            if (r8 == 0) goto L_0x0062
            if (r10 == 0) goto L_0x005f
            r8.close()     // Catch:{ Throwable -> 0x005a }
            goto L_0x0062
        L_0x005a:
            r8 = move-exception
            r10.addSuppressed(r8)     // Catch:{ IOException -> 0x0063 }
            goto L_0x0062
        L_0x005f:
            r8.close()     // Catch:{ IOException -> 0x0063 }
        L_0x0062:
            throw r9     // Catch:{ IOException -> 0x0063 }
        L_0x0063:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.p001v4.graphics.TypefaceCompatUtil.mmap(android.content.Context, android.os.CancellationSignal, android.net.Uri):java.nio.ByteBuffer");
    }

    @RequiresApi(19)
    public static ByteBuffer copyToDirectBuffer(Context context, Resources resources, int i) {
        File tempFile = getTempFile(context);
        if (tempFile == null) {
            return null;
        }
        try {
            if (!copyToFile(tempFile, resources, i)) {
                return null;
            }
            ByteBuffer mmap = mmap(tempFile);
            tempFile.delete();
            return mmap;
        } finally {
            tempFile.delete();
        }
    }

    public static boolean copyToFile(File file, InputStream inputStream) {
        FileOutputStream fileOutputStream = null;
        try {
            FileOutputStream fileOutputStream2 = new FileOutputStream(file, false);
            try {
                byte[] bArr = new byte[1024];
                while (true) {
                    int read = inputStream.read(bArr);
                    if (read != -1) {
                        fileOutputStream2.write(bArr, 0, read);
                    } else {
                        closeQuietly(fileOutputStream2);
                        return true;
                    }
                }
            } catch (IOException e) {
                e = e;
                fileOutputStream = fileOutputStream2;
                String str = TAG;
                try {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Error copying resource contents to temp file: ");
                    sb.append(e.getMessage());
                    Log.e(str, sb.toString());
                    closeQuietly(fileOutputStream);
                    return false;
                } catch (Throwable th) {
                    th = th;
                    closeQuietly(fileOutputStream);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                fileOutputStream = fileOutputStream2;
                closeQuietly(fileOutputStream);
                throw th;
            }
        } catch (IOException e2) {
            e = e2;
            String str2 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Error copying resource contents to temp file: ");
            sb2.append(e.getMessage());
            Log.e(str2, sb2.toString());
            closeQuietly(fileOutputStream);
            return false;
        }
    }

    public static boolean copyToFile(File file, Resources resources, int i) {
        InputStream inputStream;
        try {
            inputStream = resources.openRawResource(i);
            try {
                boolean copyToFile = copyToFile(file, inputStream);
                closeQuietly(inputStream);
                return copyToFile;
            } catch (Throwable th) {
                th = th;
                closeQuietly(inputStream);
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            inputStream = null;
            closeQuietly(inputStream);
            throw th;
        }
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException unused) {
            }
        }
    }
}
