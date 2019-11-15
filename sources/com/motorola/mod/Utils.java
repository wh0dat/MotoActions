package com.motorola.mod;

import android.os.Build;
import android.os.Parcel;
import java.math.BigInteger;
import java.util.UUID;

public class Utils {

    /* renamed from: a */
    private static final boolean f202a = (!Build.USER.equals(Build.TYPE));

    /* JADX WARNING: Removed duplicated region for block: B:32:0x006c A[Catch:{ all -> 0x00ac }] */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x008c A[SYNTHETIC, Splitter:B:34:0x008c] */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x00af A[SYNTHETIC, Splitter:B:43:0x00af] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String readFile(java.lang.String r7) {
        /*
            r0 = 0
            java.io.FileInputStream r1 = new java.io.FileInputStream     // Catch:{ IOException -> 0x0066, all -> 0x0061 }
            r1.<init>(r7)     // Catch:{ IOException -> 0x0066, all -> 0x0061 }
            r2 = 4096(0x1000, float:5.74E-42)
            byte[] r2 = new byte[r2]     // Catch:{ IOException -> 0x005f }
            int r3 = r2.length     // Catch:{ IOException -> 0x005f }
            r4 = 0
            int r3 = r1.read(r2, r4, r3)     // Catch:{ IOException -> 0x005f }
            if (r3 <= 0) goto L_0x003d
            java.lang.String r5 = new java.lang.String     // Catch:{ IOException -> 0x005f }
            r5.<init>(r2, r4, r3)     // Catch:{ IOException -> 0x005f }
            java.lang.String r2 = r5.trim()     // Catch:{ IOException -> 0x005f }
            if (r1 == 0) goto L_0x003c
            r1.close()     // Catch:{ IOException -> 0x0021 }
            goto L_0x003c
        L_0x0021:
            r0 = move-exception
            boolean r1 = f202a
            if (r1 == 0) goto L_0x003c
            java.lang.String r1 = "ModManager"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "Error in closing "
            r3.append(r4)
            r3.append(r7)
            java.lang.String r7 = r3.toString()
            android.util.Log.e(r1, r7, r0)
        L_0x003c:
            return r2
        L_0x003d:
            if (r1 == 0) goto L_0x005e
            r1.close()     // Catch:{ IOException -> 0x0043 }
            goto L_0x005e
        L_0x0043:
            r1 = move-exception
            boolean r2 = f202a
            if (r2 == 0) goto L_0x005e
            java.lang.String r2 = "ModManager"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "Error in closing "
            r3.append(r4)
            r3.append(r7)
            java.lang.String r7 = r3.toString()
            android.util.Log.e(r2, r7, r1)
        L_0x005e:
            return r0
        L_0x005f:
            r2 = move-exception
            goto L_0x0068
        L_0x0061:
            r1 = move-exception
            r6 = r1
            r1 = r0
            r0 = r6
            goto L_0x00ad
        L_0x0066:
            r2 = move-exception
            r1 = r0
        L_0x0068:
            boolean r3 = f202a     // Catch:{ all -> 0x00ac }
            if (r3 == 0) goto L_0x008a
            java.lang.String r3 = "ModManager"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x00ac }
            r4.<init>()     // Catch:{ all -> 0x00ac }
            java.lang.String r5 = "Error in reading "
            r4.append(r5)     // Catch:{ all -> 0x00ac }
            r4.append(r7)     // Catch:{ all -> 0x00ac }
            java.lang.String r5 = ":"
            r4.append(r5)     // Catch:{ all -> 0x00ac }
            r4.append(r2)     // Catch:{ all -> 0x00ac }
            java.lang.String r2 = r4.toString()     // Catch:{ all -> 0x00ac }
            android.util.Log.e(r3, r2)     // Catch:{ all -> 0x00ac }
        L_0x008a:
            if (r1 == 0) goto L_0x00ab
            r1.close()     // Catch:{ IOException -> 0x0090 }
            goto L_0x00ab
        L_0x0090:
            r1 = move-exception
            boolean r2 = f202a
            if (r2 == 0) goto L_0x00ab
            java.lang.String r2 = "ModManager"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "Error in closing "
            r3.append(r4)
            r3.append(r7)
            java.lang.String r7 = r3.toString()
            android.util.Log.e(r2, r7, r1)
        L_0x00ab:
            return r0
        L_0x00ac:
            r0 = move-exception
        L_0x00ad:
            if (r1 == 0) goto L_0x00ce
            r1.close()     // Catch:{ IOException -> 0x00b3 }
            goto L_0x00ce
        L_0x00b3:
            r1 = move-exception
            boolean r2 = f202a
            if (r2 == 0) goto L_0x00ce
            java.lang.String r2 = "ModManager"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "Error in closing "
            r3.append(r4)
            r3.append(r7)
            java.lang.String r7 = r3.toString()
            android.util.Log.e(r2, r7, r1)
        L_0x00ce:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.motorola.mod.Utils.readFile(java.lang.String):java.lang.String");
    }

    public static BigInteger readBigIntegerFromFile(String str) {
        String readFile = readFile(str);
        if (readFile == null) {
            return BigInteger.ZERO;
        }
        if (readFile.startsWith("0x")) {
            return new BigInteger(readFile.substring(2), 16);
        }
        return new BigInteger(readFile, 10);
    }

    public static void writeNullableString(Parcel parcel, String str) {
        if (str == null) {
            parcel.writeInt(0);
            return;
        }
        parcel.writeInt(1);
        parcel.writeString(str);
    }

    public static String readNullableString(Parcel parcel) {
        if (parcel.readInt() == 1) {
            return parcel.readString();
        }
        return null;
    }

    public static boolean equals(UUID uuid, UUID uuid2) {
        return (uuid == null && uuid2 == null) || uuid.equals(uuid2);
    }
}
