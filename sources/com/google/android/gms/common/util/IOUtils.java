package com.google.android.gms.common.util;

import android.os.ParcelFileDescriptor;
import android.util.Log;
import com.google.android.gms.common.internal.Preconditions;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import javax.annotation.Nullable;

public final class IOUtils {

    private static final class zza extends ByteArrayOutputStream {
        private zza() {
        }

        /* access modifiers changed from: 0000 */
        public final void zza(byte[] bArr, int i) {
            System.arraycopy(this.buf, 0, bArr, i, this.count);
        }
    }

    private static final class zzb {
        private final File file;

        private zzb(File file2) {
            this.file = (File) Preconditions.checkNotNull(file2);
        }

        public final byte[] zzdd() throws IOException {
            FileInputStream fileInputStream = null;
            try {
                FileInputStream fileInputStream2 = new FileInputStream(this.file);
                try {
                    byte[] zzb = IOUtils.zza((InputStream) fileInputStream2, fileInputStream2.getChannel().size());
                    IOUtils.closeQuietly((Closeable) fileInputStream2);
                    return zzb;
                } catch (Throwable th) {
                    th = th;
                    fileInputStream = fileInputStream2;
                    IOUtils.closeQuietly((Closeable) fileInputStream);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                IOUtils.closeQuietly((Closeable) fileInputStream);
                throw th;
            }
        }
    }

    private IOUtils() {
    }

    public static void close(@Nullable Closeable closeable, String str, String str2) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                Log.d(str, str2, e);
            }
        }
    }

    public static void closeQuietly(@Nullable ParcelFileDescriptor parcelFileDescriptor) {
        if (parcelFileDescriptor != null) {
            try {
                parcelFileDescriptor.close();
            } catch (IOException unused) {
            }
        }
    }

    public static void closeQuietly(@Nullable Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException unused) {
            }
        }
    }

    public static void closeQuietly(@Nullable ServerSocket serverSocket) {
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException unused) {
            }
        }
    }

    public static void closeQuietly(@Nullable Socket socket) {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException unused) {
            }
        }
    }

    public static long copyStream(InputStream inputStream, OutputStream outputStream) throws IOException {
        return copyStream(inputStream, outputStream, false);
    }

    public static long copyStream(InputStream inputStream, OutputStream outputStream, boolean z) throws IOException {
        return copyStream(inputStream, outputStream, z, 1024);
    }

    public static long copyStream(InputStream inputStream, OutputStream outputStream, boolean z, int i) throws IOException {
        byte[] bArr = new byte[i];
        long j = 0;
        while (true) {
            try {
                int read = inputStream.read(bArr, 0, i);
                if (read == -1) {
                    break;
                }
                j += (long) read;
                outputStream.write(bArr, 0, read);
            } finally {
                if (z) {
                    closeQuietly((Closeable) inputStream);
                    closeQuietly((Closeable) outputStream);
                }
            }
        }
        return j;
    }

    public static boolean isGzipByteBuffer(byte[] bArr) {
        if (bArr.length > 1) {
            if ((((bArr[1] & 255) << 8) | (bArr[0] & 255)) == 35615) {
                return true;
            }
        }
        return false;
    }

    /* JADX WARNING: Removed duplicated region for block: B:32:0x0044  */
    @android.support.annotation.WorkerThread
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void lockAndTruncateFile(java.io.File r5) throws java.io.IOException, java.nio.channels.OverlappingFileLockException {
        /*
            boolean r0 = r5.exists()
            if (r0 != 0) goto L_0x000c
            java.io.FileNotFoundException r5 = new java.io.FileNotFoundException
            r5.<init>()
            throw r5
        L_0x000c:
            r0 = 0
            java.io.RandomAccessFile r1 = new java.io.RandomAccessFile     // Catch:{ all -> 0x0035 }
            java.lang.String r2 = "rw"
            r1.<init>(r5, r2)     // Catch:{ all -> 0x0035 }
            java.nio.channels.FileChannel r5 = r1.getChannel()     // Catch:{ all -> 0x0033 }
            java.nio.channels.FileLock r2 = r5.lock()     // Catch:{ all -> 0x0033 }
            r3 = 0
            r5.truncate(r3)     // Catch:{ all -> 0x0030 }
            if (r2 == 0) goto L_0x002c
            boolean r5 = r2.isValid()
            if (r5 == 0) goto L_0x002c
            r2.release()     // Catch:{ IOException -> 0x002c }
        L_0x002c:
            closeQuietly(r1)
            return
        L_0x0030:
            r5 = move-exception
            r0 = r2
            goto L_0x0037
        L_0x0033:
            r5 = move-exception
            goto L_0x0037
        L_0x0035:
            r5 = move-exception
            r1 = r0
        L_0x0037:
            if (r0 == 0) goto L_0x0042
            boolean r2 = r0.isValid()
            if (r2 == 0) goto L_0x0042
            r0.release()     // Catch:{ IOException -> 0x0042 }
        L_0x0042:
            if (r1 == 0) goto L_0x0047
            closeQuietly(r1)
        L_0x0047:
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.util.IOUtils.lockAndTruncateFile(java.io.File):void");
    }

    public static byte[] readInputStreamFully(InputStream inputStream) throws IOException {
        return readInputStreamFully(inputStream, true);
    }

    public static byte[] readInputStreamFully(InputStream inputStream, boolean z) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        copyStream(inputStream, byteArrayOutputStream, z);
        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] toByteArray(File file) throws IOException {
        return new zzb(file).zzdd();
    }

    public static byte[] toByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        zza(inputStream, (OutputStream) byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private static long zza(InputStream inputStream, OutputStream outputStream) throws IOException {
        Preconditions.checkNotNull(inputStream);
        Preconditions.checkNotNull(outputStream);
        byte[] bArr = new byte[4096];
        long j = 0;
        while (true) {
            int read = inputStream.read(bArr);
            if (read == -1) {
                return j;
            }
            outputStream.write(bArr, 0, read);
            j += (long) read;
        }
    }

    /* access modifiers changed from: private */
    public static byte[] zza(InputStream inputStream, long j) throws IOException {
        if (j > 2147483647L) {
            StringBuilder sb = new StringBuilder(68);
            sb.append("file is too large to fit in a byte array: ");
            sb.append(j);
            sb.append(" bytes");
            throw new OutOfMemoryError(sb.toString());
        } else if (j == 0) {
            return toByteArray(inputStream);
        } else {
            int i = (int) j;
            byte[] bArr = new byte[i];
            int i2 = i;
            while (i2 > 0) {
                int i3 = i - i2;
                int read = inputStream.read(bArr, i3, i2);
                if (read == -1) {
                    return Arrays.copyOf(bArr, i3);
                }
                i2 -= read;
            }
            int read2 = inputStream.read();
            if (read2 == -1) {
                return bArr;
            }
            zza zza2 = new zza();
            zza2.write(read2);
            zza(inputStream, (OutputStream) zza2);
            byte[] copyOf = Arrays.copyOf(bArr, bArr.length + zza2.size());
            zza2.zza(copyOf, bArr.length);
            return copyOf;
        }
    }
}
