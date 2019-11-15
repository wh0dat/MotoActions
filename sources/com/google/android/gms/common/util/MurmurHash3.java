package com.google.android.gms.common.util;

public class MurmurHash3 {
    private MurmurHash3() {
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0076, code lost:
        return r5 ^ (r5 >>> 16);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x004d, code lost:
        r6 = r6 | ((r5[r0 + 1] & 255) << 8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0056, code lost:
        r5 = ((r5[r0] & 255) | r6) * -862048943;
        r8 = r8 ^ (((r5 >>> 17) | (r5 << 15)) * 461845907);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0063, code lost:
        r5 = r8 ^ r7;
        r5 = (r5 ^ (r5 >>> 16)) * -2048144789;
        r5 = (r5 ^ (r5 >>> 13)) * -1028477387;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int murmurhash3_x86_32(byte[] r5, int r6, int r7, int r8) {
        /*
            r0 = r7 & -4
            int r0 = r0 + r6
        L_0x0003:
            r1 = 461845907(0x1b873593, float:2.2368498E-22)
            r2 = -862048943(0xffffffffcc9e2d51, float:-8.2930312E7)
            if (r6 >= r0) goto L_0x003e
            byte r3 = r5[r6]
            r3 = r3 & 255(0xff, float:3.57E-43)
            int r4 = r6 + 1
            byte r4 = r5[r4]
            r4 = r4 & 255(0xff, float:3.57E-43)
            int r4 = r4 << 8
            r3 = r3 | r4
            int r4 = r6 + 2
            byte r4 = r5[r4]
            r4 = r4 & 255(0xff, float:3.57E-43)
            int r4 = r4 << 16
            r3 = r3 | r4
            int r4 = r6 + 3
            byte r4 = r5[r4]
            int r4 = r4 << 24
            r3 = r3 | r4
            int r3 = r3 * r2
            int r2 = r3 << 15
            int r3 = r3 >>> 17
            r2 = r2 | r3
            int r2 = r2 * r1
            r8 = r8 ^ r2
            int r1 = r8 << 13
            int r8 = r8 >>> 19
            r8 = r8 | r1
            int r8 = r8 * 5
            r1 = -430675100(0xffffffffe6546b64, float:-2.5078068E23)
            int r8 = r8 + r1
            int r6 = r6 + 4
            goto L_0x0003
        L_0x003e:
            r6 = 0
            r3 = r7 & 3
            switch(r3) {
                case 1: goto L_0x0056;
                case 2: goto L_0x004d;
                case 3: goto L_0x0045;
                default: goto L_0x0044;
            }
        L_0x0044:
            goto L_0x0063
        L_0x0045:
            int r6 = r0 + 2
            byte r6 = r5[r6]
            r6 = r6 & 255(0xff, float:3.57E-43)
            int r6 = r6 << 16
        L_0x004d:
            int r3 = r0 + 1
            byte r3 = r5[r3]
            r3 = r3 & 255(0xff, float:3.57E-43)
            int r3 = r3 << 8
            r6 = r6 | r3
        L_0x0056:
            byte r5 = r5[r0]
            r5 = r5 & 255(0xff, float:3.57E-43)
            r5 = r5 | r6
            int r5 = r5 * r2
            int r6 = r5 << 15
            int r5 = r5 >>> 17
            r5 = r5 | r6
            int r5 = r5 * r1
            r8 = r8 ^ r5
        L_0x0063:
            r5 = r8 ^ r7
            int r6 = r5 >>> 16
            r5 = r5 ^ r6
            r6 = -2048144789(0xffffffff85ebca6b, float:-2.217365E-35)
            int r5 = r5 * r6
            int r6 = r5 >>> 13
            r5 = r5 ^ r6
            r6 = -1028477387(0xffffffffc2b2ae35, float:-89.34025)
            int r5 = r5 * r6
            int r6 = r5 >>> 16
            r5 = r5 ^ r6
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.util.MurmurHash3.murmurhash3_x86_32(byte[], int, int, int):int");
    }
}
