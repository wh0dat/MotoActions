package com.google.android.gms.common.util;

import android.os.Binder;
import android.os.Process;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileReader;
import java.io.IOException;
import javax.annotation.Nullable;

public class ProcessUtils {
    private static String zzaai;
    private static int zzaaj;

    public static class SystemGroupsNotAvailableException extends Exception {
        SystemGroupsNotAvailableException(String str) {
            super(str);
        }

        SystemGroupsNotAvailableException(String str, Throwable th) {
            super(str, th);
        }
    }

    private ProcessUtils() {
    }

    @Nullable
    public static String getCallingProcessName() {
        int callingPid = Binder.getCallingPid();
        return callingPid == zzde() ? getMyProcessName() : zzl(callingPid);
    }

    @Nullable
    public static String getMyProcessName() {
        if (zzaai == null) {
            zzaai = zzl(zzde());
        }
        return zzaai;
    }

    public static boolean hasSystemGroups() throws SystemGroupsNotAvailableException {
        BufferedReader bufferedReader = null;
        try {
            int zzde = zzde();
            StringBuilder sb = new StringBuilder(24);
            sb.append("/proc/");
            sb.append(zzde);
            sb.append("/status");
            BufferedReader zzm = zzm(sb.toString());
            try {
                boolean zzk = zzk(zzm);
                IOUtils.closeQuietly((Closeable) zzm);
                return zzk;
            } catch (IOException e) {
                BufferedReader bufferedReader2 = zzm;
                e = e;
                bufferedReader = bufferedReader2;
                try {
                    throw new SystemGroupsNotAvailableException("Unable to access /proc/pid/status.", e);
                } catch (Throwable th) {
                    th = th;
                    IOUtils.closeQuietly((Closeable) bufferedReader);
                    throw th;
                }
            } catch (Throwable th2) {
                BufferedReader bufferedReader3 = zzm;
                th = th2;
                bufferedReader = bufferedReader3;
                IOUtils.closeQuietly((Closeable) bufferedReader);
                throw th;
            }
        } catch (IOException e2) {
            e = e2;
            throw new SystemGroupsNotAvailableException("Unable to access /proc/pid/status.", e);
        }
    }

    private static int zzde() {
        if (zzaaj == 0) {
            zzaaj = Process.myPid();
        }
        return zzaaj;
    }

    private static boolean zzk(BufferedReader bufferedReader) throws IOException, SystemGroupsNotAvailableException {
        String trim;
        do {
            String readLine = bufferedReader.readLine();
            if (readLine != null) {
                trim = readLine.trim();
            } else {
                throw new SystemGroupsNotAvailableException("Missing Groups entry from proc/pid/status.");
            }
        } while (!trim.startsWith("Groups:"));
        for (String parseLong : trim.substring(7).trim().split("\\s", -1)) {
            try {
                long parseLong2 = Long.parseLong(parseLong);
                if (parseLong2 >= 1000 && parseLong2 < 2000) {
                    return true;
                }
            } catch (NumberFormatException unused) {
            }
        }
        return false;
    }

    @Nullable
    private static String zzl(int i) {
        BufferedReader bufferedReader;
        BufferedReader bufferedReader2 = null;
        if (i <= 0) {
            return null;
        }
        try {
            StringBuilder sb = new StringBuilder(25);
            sb.append("/proc/");
            sb.append(i);
            sb.append("/cmdline");
            bufferedReader = zzm(sb.toString());
            try {
                String trim = bufferedReader.readLine().trim();
                IOUtils.closeQuietly((Closeable) bufferedReader);
                return trim;
            } catch (IOException unused) {
                IOUtils.closeQuietly((Closeable) bufferedReader);
                return null;
            } catch (Throwable th) {
                Throwable th2 = th;
                bufferedReader2 = bufferedReader;
                th = th2;
                IOUtils.closeQuietly((Closeable) bufferedReader2);
                throw th;
            }
        } catch (IOException unused2) {
            bufferedReader = null;
            IOUtils.closeQuietly((Closeable) bufferedReader);
            return null;
        } catch (Throwable th3) {
            th = th3;
            IOUtils.closeQuietly((Closeable) bufferedReader2);
            throw th;
        }
    }

    private static BufferedReader zzm(String str) throws IOException {
        ThreadPolicy allowThreadDiskReads = StrictMode.allowThreadDiskReads();
        try {
            return new BufferedReader(new FileReader(str));
        } finally {
            StrictMode.setThreadPolicy(allowThreadDiskReads);
        }
    }
}
