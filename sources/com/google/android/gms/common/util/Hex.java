package com.google.android.gms.common.util;

public class Hex {
    private static final char[] zzaaa = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final char[] zzzz = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static String bytesToColonDelimitedStringLowercase(byte[] bArr) {
        if (bArr.length == 0) {
            return new String();
        }
        char[] cArr = new char[((bArr.length * 3) - 1)];
        int i = 0;
        int i2 = 0;
        while (i < bArr.length - 1) {
            byte b = bArr[i] & 255;
            int i3 = i2 + 1;
            cArr[i2] = zzaaa[b >>> 4];
            int i4 = i3 + 1;
            cArr[i3] = zzaaa[b & 15];
            int i5 = i4 + 1;
            cArr[i4] = ':';
            i++;
            i2 = i5;
        }
        byte b2 = bArr[bArr.length - 1] & 255;
        int i6 = i2 + 1;
        cArr[i2] = zzaaa[b2 >>> 4];
        cArr[i6] = zzaaa[b2 & 15];
        return new String(cArr);
    }

    public static String bytesToColonDelimitedStringUppercase(byte[] bArr) {
        if (bArr.length == 0) {
            return new String();
        }
        char[] cArr = new char[((bArr.length * 3) - 1)];
        int i = 0;
        int i2 = 0;
        while (i < bArr.length - 1) {
            byte b = bArr[i] & 255;
            int i3 = i2 + 1;
            cArr[i2] = zzzz[b >>> 4];
            int i4 = i3 + 1;
            cArr[i3] = zzzz[b & 15];
            int i5 = i4 + 1;
            cArr[i4] = ':';
            i++;
            i2 = i5;
        }
        byte b2 = bArr[bArr.length - 1] & 255;
        int i6 = i2 + 1;
        cArr[i2] = zzzz[b2 >>> 4];
        cArr[i6] = zzzz[b2 & 15];
        return new String(cArr);
    }

    public static String bytesToStringLowercase(byte[] bArr) {
        char[] cArr = new char[(bArr.length << 1)];
        int i = 0;
        for (byte b : bArr) {
            byte b2 = b & 255;
            int i2 = i + 1;
            cArr[i] = zzaaa[b2 >>> 4];
            i = i2 + 1;
            cArr[i2] = zzaaa[b2 & 15];
        }
        return new String(cArr);
    }

    public static String bytesToStringUppercase(byte[] bArr) {
        return bytesToStringUppercase(bArr, false);
    }

    public static String bytesToStringUppercase(byte[] bArr, boolean z) {
        int length = bArr.length;
        StringBuilder sb = new StringBuilder(length << 1);
        int i = 0;
        while (i < length && (!z || i != length - 1 || (bArr[i] & 255) != 0)) {
            sb.append(zzzz[(bArr[i] & 240) >>> 4]);
            sb.append(zzzz[bArr[i] & 15]);
            i++;
        }
        return sb.toString();
    }

    public static byte[] colonDelimitedStringToBytes(String str) {
        return stringToBytes(str.replace(":", ""));
    }

    public static byte[] stringToBytes(String str) throws IllegalArgumentException {
        int length = str.length();
        if (length % 2 != 0) {
            throw new IllegalArgumentException("Hex string has odd number of characters");
        }
        byte[] bArr = new byte[(length / 2)];
        int i = 0;
        while (i < length) {
            int i2 = i + 2;
            bArr[i / 2] = (byte) Integer.parseInt(str.substring(i, i2), 16);
            i = i2;
        }
        return bArr;
    }
}
