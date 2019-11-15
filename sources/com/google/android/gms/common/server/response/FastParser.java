package com.google.android.gms.common.server.response;

import android.util.Log;
import com.google.android.gms.common.server.response.FastJsonResponse;
import com.google.android.gms.common.server.response.FastJsonResponse.Field;
import com.google.android.gms.common.util.Base64Utils;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;
import kotlin.text.Typography;

public class FastParser<T extends FastJsonResponse> {
    private static final char[] zzwv = {'u', 'l', 'l'};
    private static final char[] zzww = {'r', 'u', 'e'};
    private static final char[] zzwx = {'r', 'u', 'e', Typography.quote};
    private static final char[] zzwy = {'a', 'l', 's', 'e'};
    private static final char[] zzwz = {'a', 'l', 's', 'e', Typography.quote};
    private static final char[] zzxa = {10};
    private static final zza<Integer> zzxc = new zza();
    private static final zza<Long> zzxd = new zzb();
    private static final zza<Float> zzxe = new zzc();
    private static final zza<Double> zzxf = new zzd();
    private static final zza<Boolean> zzxg = new zze();
    private static final zza<String> zzxh = new zzf();
    private static final zza<BigInteger> zzxi = new zzg();
    private static final zza<BigDecimal> zzxj = new zzh();
    private final char[] zzwq = new char[1];
    private final char[] zzwr = new char[32];
    private final char[] zzws = new char[1024];
    private final StringBuilder zzwt = new StringBuilder(32);
    private final StringBuilder zzwu = new StringBuilder(1024);
    private final Stack<Integer> zzxb = new Stack<>();

    public static class ParseException extends Exception {
        public ParseException(String str) {
            super(str);
        }

        public ParseException(String str, Throwable th) {
            super(str, th);
        }

        public ParseException(Throwable th) {
            super(th);
        }
    }

    private interface zza<O> {
        O zzh(FastParser fastParser, BufferedReader bufferedReader) throws ParseException, IOException;
    }

    private final int zza(BufferedReader bufferedReader, char[] cArr) throws ParseException, IOException {
        int i;
        char zzj = zzj(bufferedReader);
        if (zzj == 0) {
            throw new ParseException("Unexpected EOF");
        } else if (zzj == ',') {
            throw new ParseException("Missing value");
        } else if (zzj == 'n') {
            zzb(bufferedReader, zzwv);
            return 0;
        } else {
            bufferedReader.mark(1024);
            if (zzj == '\"') {
                i = 0;
                boolean z = false;
                while (i < cArr.length && bufferedReader.read(cArr, i, 1) != -1) {
                    char c = cArr[i];
                    if (Character.isISOControl(c)) {
                        throw new ParseException("Unexpected control character while reading string");
                    } else if (c != '\"' || z) {
                        z = c == '\\' ? !z : false;
                        i++;
                    } else {
                        bufferedReader.reset();
                        bufferedReader.skip((long) (i + 1));
                        return i;
                    }
                }
            } else {
                cArr[0] = zzj;
                int i2 = 1;
                while (i < cArr.length && bufferedReader.read(cArr, i, 1) != -1) {
                    if (cArr[i] == '}' || cArr[i] == ',' || Character.isWhitespace(cArr[i]) || cArr[i] == ']') {
                        bufferedReader.reset();
                        bufferedReader.skip((long) (i - 1));
                        cArr[i] = 0;
                        return i;
                    }
                    i2 = i + 1;
                }
            }
            if (i == cArr.length) {
                throw new ParseException("Absurdly long value");
            }
            throw new ParseException("Unexpected EOF");
        }
    }

    private final String zza(BufferedReader bufferedReader) throws ParseException, IOException {
        this.zzxb.push(Integer.valueOf(2));
        char zzj = zzj(bufferedReader);
        if (zzj == '\"') {
            this.zzxb.push(Integer.valueOf(3));
            String zzb = zzb(bufferedReader, this.zzwr, this.zzwt, null);
            zzk(3);
            if (zzj(bufferedReader) == ':') {
                return zzb;
            }
            throw new ParseException("Expected key/value separator");
        } else if (zzj == ']') {
            zzk(2);
            zzk(1);
            zzk(5);
            return null;
        } else if (zzj != '}') {
            StringBuilder sb = new StringBuilder(19);
            sb.append("Unexpected token: ");
            sb.append(zzj);
            throw new ParseException(sb.toString());
        } else {
            zzk(2);
            return null;
        }
    }

    private final String zza(BufferedReader bufferedReader, char[] cArr, StringBuilder sb, char[] cArr2) throws ParseException, IOException {
        char zzj = zzj(bufferedReader);
        if (zzj == '\"') {
            return zzb(bufferedReader, cArr, sb, cArr2);
        }
        if (zzj != 'n') {
            throw new ParseException("Expected string");
        }
        zzb(bufferedReader, zzwv);
        return null;
    }

    private final <T extends FastJsonResponse> ArrayList<T> zza(BufferedReader bufferedReader, Field<?, ?> field) throws ParseException, IOException {
        ArrayList<T> arrayList = new ArrayList<>();
        char zzj = zzj(bufferedReader);
        if (zzj == ']') {
            zzk(5);
            return arrayList;
        } else if (zzj == 'n') {
            zzb(bufferedReader, zzwv);
            zzk(5);
            return null;
        } else if (zzj != '{') {
            StringBuilder sb = new StringBuilder(19);
            sb.append("Unexpected token: ");
            sb.append(zzj);
            throw new ParseException(sb.toString());
        } else {
            Stack<Integer> stack = this.zzxb;
            while (true) {
                stack.push(Integer.valueOf(1));
                try {
                    FastJsonResponse newConcreteTypeInstance = field.newConcreteTypeInstance();
                    if (!zza(bufferedReader, newConcreteTypeInstance)) {
                        return arrayList;
                    }
                    arrayList.add(newConcreteTypeInstance);
                    char zzj2 = zzj(bufferedReader);
                    if (zzj2 != ',') {
                        if (zzj2 != ']') {
                            StringBuilder sb2 = new StringBuilder(19);
                            sb2.append("Unexpected token: ");
                            sb2.append(zzj2);
                            throw new ParseException(sb2.toString());
                        }
                        zzk(5);
                        return arrayList;
                    } else if (zzj(bufferedReader) != '{') {
                        throw new ParseException("Expected start of next object in array");
                    } else {
                        stack = this.zzxb;
                    }
                } catch (InstantiationException e) {
                    throw new ParseException("Error instantiating inner object", e);
                } catch (IllegalAccessException e2) {
                    throw new ParseException("Error instantiating inner object", e2);
                }
            }
        }
    }

    private final <O> ArrayList<O> zza(BufferedReader bufferedReader, zza<O> zza2) throws ParseException, IOException {
        char zzj = zzj(bufferedReader);
        if (zzj == 'n') {
            zzb(bufferedReader, zzwv);
            return null;
        } else if (zzj != '[') {
            throw new ParseException("Expected start of array");
        } else {
            this.zzxb.push(Integer.valueOf(5));
            ArrayList<O> arrayList = new ArrayList<>();
            while (true) {
                bufferedReader.mark(1024);
                char zzj2 = zzj(bufferedReader);
                if (zzj2 == 0) {
                    throw new ParseException("Unexpected EOF");
                } else if (zzj2 != ',') {
                    if (zzj2 != ']') {
                        bufferedReader.reset();
                        arrayList.add(zza2.zzh(this, bufferedReader));
                    } else {
                        zzk(5);
                        return arrayList;
                    }
                }
            }
        }
    }

    private final boolean zza(BufferedReader bufferedReader, FastJsonResponse fastJsonResponse) throws ParseException, IOException {
        byte[] bArr;
        HashMap hashMap;
        Map fieldMappings = fastJsonResponse.getFieldMappings();
        String zza2 = zza(bufferedReader);
        if (zza2 == null) {
            zzk(1);
            return false;
        }
        while (zza2 != null) {
            Field field = (Field) fieldMappings.get(zza2);
            if (field == null) {
                zza2 = zzb(bufferedReader);
            } else {
                this.zzxb.push(Integer.valueOf(4));
                switch (field.getTypeIn()) {
                    case 0:
                        if (!field.isTypeInArray()) {
                            fastJsonResponse.setInteger(field, zzd(bufferedReader));
                            break;
                        } else {
                            fastJsonResponse.setIntegers(field, zza(bufferedReader, zzxc));
                            break;
                        }
                    case 1:
                        if (!field.isTypeInArray()) {
                            fastJsonResponse.setBigInteger(field, zzf(bufferedReader));
                            break;
                        } else {
                            fastJsonResponse.setBigIntegers(field, zza(bufferedReader, zzxi));
                            break;
                        }
                    case 2:
                        if (!field.isTypeInArray()) {
                            fastJsonResponse.setLong(field, zze(bufferedReader));
                            break;
                        } else {
                            fastJsonResponse.setLongs(field, zza(bufferedReader, zzxd));
                            break;
                        }
                    case 3:
                        if (!field.isTypeInArray()) {
                            fastJsonResponse.setFloat(field, zzg(bufferedReader));
                            break;
                        } else {
                            fastJsonResponse.setFloats(field, zza(bufferedReader, zzxe));
                            break;
                        }
                    case 4:
                        if (!field.isTypeInArray()) {
                            fastJsonResponse.setDouble(field, zzh(bufferedReader));
                            break;
                        } else {
                            fastJsonResponse.setDoubles(field, zza(bufferedReader, zzxf));
                            break;
                        }
                    case 5:
                        if (!field.isTypeInArray()) {
                            fastJsonResponse.setBigDecimal(field, zzi(bufferedReader));
                            break;
                        } else {
                            fastJsonResponse.setBigDecimals(field, zza(bufferedReader, zzxj));
                            break;
                        }
                    case 6:
                        if (!field.isTypeInArray()) {
                            fastJsonResponse.setBoolean(field, zza(bufferedReader, false));
                            break;
                        } else {
                            fastJsonResponse.setBooleans(field, zza(bufferedReader, zzxg));
                            break;
                        }
                    case 7:
                        if (!field.isTypeInArray()) {
                            fastJsonResponse.setString(field, zzc(bufferedReader));
                            break;
                        } else {
                            fastJsonResponse.setStrings(field, zza(bufferedReader, zzxh));
                            break;
                        }
                    case 8:
                        bArr = Base64Utils.decode(zza(bufferedReader, this.zzws, this.zzwu, zzxa));
                        break;
                    case 9:
                        bArr = Base64Utils.decodeUrlSafe(zza(bufferedReader, this.zzws, this.zzwu, zzxa));
                        break;
                    case 10:
                        char zzj = zzj(bufferedReader);
                        if (zzj == 'n') {
                            zzb(bufferedReader, zzwv);
                            hashMap = null;
                        } else if (zzj != '{') {
                            throw new ParseException("Expected start of a map object");
                        } else {
                            this.zzxb.push(Integer.valueOf(1));
                            hashMap = new HashMap();
                            while (true) {
                                char zzj2 = zzj(bufferedReader);
                                if (zzj2 == 0) {
                                    throw new ParseException("Unexpected EOF");
                                } else if (zzj2 == '\"') {
                                    String zzb = zzb(bufferedReader, this.zzwr, this.zzwt, null);
                                    if (zzj(bufferedReader) != ':') {
                                        String str = "No map value found for key ";
                                        String valueOf = String.valueOf(zzb);
                                        throw new ParseException(valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
                                    } else if (zzj(bufferedReader) != '\"') {
                                        String str2 = "Expected String value for key ";
                                        String valueOf2 = String.valueOf(zzb);
                                        throw new ParseException(valueOf2.length() != 0 ? str2.concat(valueOf2) : new String(str2));
                                    } else {
                                        hashMap.put(zzb, zzb(bufferedReader, this.zzwr, this.zzwt, null));
                                        char zzj3 = zzj(bufferedReader);
                                        if (zzj3 != ',') {
                                            if (zzj3 != '}') {
                                                StringBuilder sb = new StringBuilder(48);
                                                sb.append("Unexpected character while parsing string map: ");
                                                sb.append(zzj3);
                                                throw new ParseException(sb.toString());
                                            }
                                        }
                                    }
                                } else if (zzj2 != '}') {
                                }
                            }
                            zzk(1);
                        }
                        fastJsonResponse.setStringMap(field, (Map<String, String>) hashMap);
                        break;
                    case 11:
                        if (field.isTypeInArray()) {
                            char zzj4 = zzj(bufferedReader);
                            if (zzj4 != 'n') {
                                this.zzxb.push(Integer.valueOf(5));
                                if (zzj4 == '[') {
                                    fastJsonResponse.addConcreteTypeArrayInternal(field, field.getOutputFieldName(), zza(bufferedReader, field));
                                    break;
                                } else {
                                    throw new ParseException("Expected array start");
                                }
                            } else {
                                zzb(bufferedReader, zzwv);
                                fastJsonResponse.addConcreteTypeArrayInternal(field, field.getOutputFieldName(), null);
                                break;
                            }
                        } else {
                            char zzj5 = zzj(bufferedReader);
                            if (zzj5 == 'n') {
                                zzb(bufferedReader, zzwv);
                                fastJsonResponse.addConcreteTypeInternal(field, field.getOutputFieldName(), null);
                                break;
                            } else {
                                this.zzxb.push(Integer.valueOf(1));
                                if (zzj5 != '{') {
                                    throw new ParseException("Expected start of object");
                                }
                                try {
                                    FastJsonResponse newConcreteTypeInstance = field.newConcreteTypeInstance();
                                    zza(bufferedReader, newConcreteTypeInstance);
                                    fastJsonResponse.addConcreteTypeInternal(field, field.getOutputFieldName(), newConcreteTypeInstance);
                                    break;
                                } catch (InstantiationException e) {
                                    throw new ParseException("Error instantiating inner object", e);
                                } catch (IllegalAccessException e2) {
                                    throw new ParseException("Error instantiating inner object", e2);
                                }
                            }
                        }
                    default:
                        int typeIn = field.getTypeIn();
                        StringBuilder sb2 = new StringBuilder(30);
                        sb2.append("Invalid field type ");
                        sb2.append(typeIn);
                        throw new ParseException(sb2.toString());
                }
                fastJsonResponse.setDecodedBytes(field, bArr);
                zzk(4);
                zzk(2);
                char zzj6 = zzj(bufferedReader);
                if (zzj6 == ',') {
                    zza2 = zza(bufferedReader);
                } else if (zzj6 != '}') {
                    StringBuilder sb3 = new StringBuilder(55);
                    sb3.append("Expected end of object or field separator, but found: ");
                    sb3.append(zzj6);
                    throw new ParseException(sb3.toString());
                } else {
                    zza2 = null;
                }
            }
        }
        PostProcessor postProcessor = fastJsonResponse.getPostProcessor();
        if (postProcessor != null) {
            postProcessor.postProcess(fastJsonResponse);
        }
        zzk(1);
        return true;
    }

    /* access modifiers changed from: private */
    public final boolean zza(BufferedReader bufferedReader, boolean z) throws ParseException, IOException {
        while (true) {
            char zzj = zzj(bufferedReader);
            if (zzj != '\"') {
                if (zzj == 'f') {
                    zzb(bufferedReader, z ? zzwz : zzwy);
                    return false;
                } else if (zzj == 'n') {
                    zzb(bufferedReader, zzwv);
                    return false;
                } else if (zzj != 't') {
                    StringBuilder sb = new StringBuilder(19);
                    sb.append("Unexpected token: ");
                    sb.append(zzj);
                    throw new ParseException(sb.toString());
                } else {
                    zzb(bufferedReader, z ? zzwx : zzww);
                    return true;
                }
            } else if (z) {
                throw new ParseException("No boolean value found in string");
            } else {
                z = true;
            }
        }
    }

    private final String zzb(BufferedReader bufferedReader) throws ParseException, IOException {
        bufferedReader.mark(1024);
        char zzj = zzj(bufferedReader);
        if (zzj != '\"') {
            if (zzj != ',') {
                int i = 1;
                if (zzj == '[') {
                    this.zzxb.push(Integer.valueOf(5));
                    bufferedReader.mark(32);
                    if (zzj(bufferedReader) != ']') {
                        bufferedReader.reset();
                        boolean z = false;
                        boolean z2 = false;
                        while (i > 0) {
                            char zzj2 = zzj(bufferedReader);
                            if (zzj2 == 0) {
                                throw new ParseException("Unexpected EOF while parsing array");
                            } else if (Character.isISOControl(zzj2)) {
                                throw new ParseException("Unexpected control character while reading array");
                            } else {
                                if (zzj2 == '\"' && !z) {
                                    z2 = !z2;
                                }
                                if (zzj2 == '[' && !z2) {
                                    i++;
                                }
                                if (zzj2 == ']' && !z2) {
                                    i--;
                                }
                                z = (zzj2 != '\\' || !z2) ? false : !z;
                            }
                        }
                    }
                    zzk(5);
                } else if (zzj != '{') {
                    bufferedReader.reset();
                    zza(bufferedReader, this.zzws);
                } else {
                    this.zzxb.push(Integer.valueOf(1));
                    bufferedReader.mark(32);
                    char zzj3 = zzj(bufferedReader);
                    if (zzj3 != '}') {
                        if (zzj3 == '\"') {
                            bufferedReader.reset();
                            zza(bufferedReader);
                            do {
                            } while (zzb(bufferedReader) != null);
                        } else {
                            StringBuilder sb = new StringBuilder(18);
                            sb.append("Unexpected token ");
                            sb.append(zzj3);
                            throw new ParseException(sb.toString());
                        }
                    }
                    zzk(1);
                }
            } else {
                throw new ParseException("Missing value");
            }
        } else if (bufferedReader.read(this.zzwq) == -1) {
            throw new ParseException("Unexpected EOF while parsing string");
        } else {
            char c = this.zzwq[0];
            boolean z3 = false;
            do {
                if (c != '\"' || z3) {
                    z3 = c == '\\' ? !z3 : false;
                    if (bufferedReader.read(this.zzwq) == -1) {
                        throw new ParseException("Unexpected EOF while parsing string");
                    }
                    c = this.zzwq[0];
                }
            } while (!Character.isISOControl(c));
            throw new ParseException("Unexpected control character while reading string");
        }
        char zzj4 = zzj(bufferedReader);
        if (zzj4 == ',') {
            zzk(2);
            return zza(bufferedReader);
        } else if (zzj4 != '}') {
            StringBuilder sb2 = new StringBuilder(18);
            sb2.append("Unexpected token ");
            sb2.append(zzj4);
            throw new ParseException(sb2.toString());
        } else {
            zzk(2);
            return null;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:37:0x0031 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String zzb(java.io.BufferedReader r9, char[] r10, java.lang.StringBuilder r11, char[] r12) throws com.google.android.gms.common.server.response.FastParser.ParseException, java.io.IOException {
        /*
            r0 = 0
            r11.setLength(r0)
            int r1 = r10.length
            r9.mark(r1)
            r1 = r0
            r2 = r1
        L_0x000a:
            int r3 = r9.read(r10)
            r4 = -1
            if (r3 == r4) goto L_0x0070
            r4 = r2
            r2 = r1
            r1 = r0
        L_0x0014:
            if (r1 >= r3) goto L_0x0066
            char r5 = r10[r1]
            boolean r6 = java.lang.Character.isISOControl(r5)
            r7 = 1
            if (r6 == 0) goto L_0x0039
            if (r12 == 0) goto L_0x002e
            r6 = r0
        L_0x0022:
            int r8 = r12.length
            if (r6 >= r8) goto L_0x002e
            char r8 = r12[r6]
            if (r8 != r5) goto L_0x002b
            r6 = r7
            goto L_0x002f
        L_0x002b:
            int r6 = r6 + 1
            goto L_0x0022
        L_0x002e:
            r6 = r0
        L_0x002f:
            if (r6 != 0) goto L_0x0039
            com.google.android.gms.common.server.response.FastParser$ParseException r9 = new com.google.android.gms.common.server.response.FastParser$ParseException
            java.lang.String r10 = "Unexpected control character while reading string"
            r9.<init>(r10)
            throw r9
        L_0x0039:
            r6 = 34
            if (r5 != r6) goto L_0x005a
            if (r2 != 0) goto L_0x005a
            r11.append(r10, r0, r1)
            r9.reset()
            int r1 = r1 + r7
            long r0 = (long) r1
            r9.skip(r0)
            if (r4 == 0) goto L_0x0055
            java.lang.String r9 = r11.toString()
            java.lang.String r9 = com.google.android.gms.common.util.JsonUtils.unescapeString(r9)
            return r9
        L_0x0055:
            java.lang.String r9 = r11.toString()
            return r9
        L_0x005a:
            r6 = 92
            if (r5 != r6) goto L_0x0062
            r2 = r2 ^ 1
            r4 = r7
            goto L_0x0063
        L_0x0062:
            r2 = r0
        L_0x0063:
            int r1 = r1 + 1
            goto L_0x0014
        L_0x0066:
            r11.append(r10, r0, r3)
            int r1 = r10.length
            r9.mark(r1)
            r1 = r2
            r2 = r4
            goto L_0x000a
        L_0x0070:
            com.google.android.gms.common.server.response.FastParser$ParseException r9 = new com.google.android.gms.common.server.response.FastParser$ParseException
            java.lang.String r10 = "Unexpected EOF while parsing string"
            r9.<init>(r10)
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.server.response.FastParser.zzb(java.io.BufferedReader, char[], java.lang.StringBuilder, char[]):java.lang.String");
    }

    private final void zzb(BufferedReader bufferedReader, char[] cArr) throws ParseException, IOException {
        int i = 0;
        while (i < cArr.length) {
            int read = bufferedReader.read(this.zzwr, 0, cArr.length - i);
            if (read == -1) {
                throw new ParseException("Unexpected EOF");
            }
            for (int i2 = 0; i2 < read; i2++) {
                if (cArr[i2 + i] != this.zzwr[i2]) {
                    throw new ParseException("Unexpected character");
                }
            }
            i += read;
        }
    }

    /* access modifiers changed from: private */
    public final String zzc(BufferedReader bufferedReader) throws ParseException, IOException {
        return zza(bufferedReader, this.zzwr, this.zzwt, null);
    }

    /* access modifiers changed from: private */
    public final int zzd(BufferedReader bufferedReader) throws ParseException, IOException {
        int i;
        boolean z;
        int i2;
        int i3;
        int i4;
        int zza2 = zza(bufferedReader, this.zzws);
        if (zza2 == 0) {
            return 0;
        }
        char[] cArr = this.zzws;
        if (zza2 > 0) {
            if (cArr[0] == '-') {
                i = Integer.MIN_VALUE;
                i2 = 1;
                z = true;
            } else {
                z = false;
                i = -2147483647;
                i2 = 0;
            }
            if (i2 < zza2) {
                i4 = i2 + 1;
                int digit = Character.digit(cArr[i2], 10);
                if (digit < 0) {
                    throw new ParseException("Unexpected non-digit character");
                }
                i3 = -digit;
            } else {
                int i5 = i2;
                i3 = 0;
                i4 = i5;
            }
            while (i4 < zza2) {
                int i6 = i4 + 1;
                int digit2 = Character.digit(cArr[i4], 10);
                if (digit2 < 0) {
                    throw new ParseException("Unexpected non-digit character");
                } else if (i3 < -214748364) {
                    throw new ParseException("Number too large");
                } else {
                    int i7 = i3 * 10;
                    if (i7 < i + digit2) {
                        throw new ParseException("Number too large");
                    }
                    i3 = i7 - digit2;
                    i4 = i6;
                }
            }
            if (!z) {
                return -i3;
            }
            if (i4 > 1) {
                return i3;
            }
            throw new ParseException("No digits to parse");
        }
        throw new ParseException("No number to parse");
    }

    /* access modifiers changed from: private */
    public final long zze(BufferedReader bufferedReader) throws ParseException, IOException {
        long j;
        long j2;
        int i;
        int zza2 = zza(bufferedReader, this.zzws);
        if (zza2 == 0) {
            return 0;
        }
        char[] cArr = this.zzws;
        if (zza2 > 0) {
            int i2 = 0;
            if (cArr[0] == '-') {
                j = Long.MIN_VALUE;
                i2 = 1;
            } else {
                j = -9223372036854775807L;
            }
            int i3 = i2;
            if (i2 < zza2) {
                i = i2 + 1;
                int digit = Character.digit(cArr[i2], 10);
                if (digit < 0) {
                    throw new ParseException("Unexpected non-digit character");
                }
                j2 = (long) (-digit);
            } else {
                i = i2;
                j2 = 0;
            }
            while (i < zza2) {
                int i4 = i + 1;
                int digit2 = Character.digit(cArr[i], 10);
                if (digit2 < 0) {
                    throw new ParseException("Unexpected non-digit character");
                } else if (j2 < -922337203685477580L) {
                    throw new ParseException("Number too large");
                } else {
                    long j3 = j2 * 10;
                    long j4 = (long) digit2;
                    if (j3 < j + j4) {
                        throw new ParseException("Number too large");
                    }
                    j2 = j3 - j4;
                    i = i4;
                }
            }
            if (i3 == 0) {
                return -j2;
            }
            if (i > 1) {
                return j2;
            }
            throw new ParseException("No digits to parse");
        }
        throw new ParseException("No number to parse");
    }

    /* access modifiers changed from: private */
    public final BigInteger zzf(BufferedReader bufferedReader) throws ParseException, IOException {
        int zza2 = zza(bufferedReader, this.zzws);
        if (zza2 == 0) {
            return null;
        }
        return new BigInteger(new String(this.zzws, 0, zza2));
    }

    /* access modifiers changed from: private */
    public final float zzg(BufferedReader bufferedReader) throws ParseException, IOException {
        int zza2 = zza(bufferedReader, this.zzws);
        if (zza2 == 0) {
            return 0.0f;
        }
        return Float.parseFloat(new String(this.zzws, 0, zza2));
    }

    /* access modifiers changed from: private */
    public final double zzh(BufferedReader bufferedReader) throws ParseException, IOException {
        int zza2 = zza(bufferedReader, this.zzws);
        if (zza2 == 0) {
            return 0.0d;
        }
        return Double.parseDouble(new String(this.zzws, 0, zza2));
    }

    /* access modifiers changed from: private */
    public final BigDecimal zzi(BufferedReader bufferedReader) throws ParseException, IOException {
        int zza2 = zza(bufferedReader, this.zzws);
        if (zza2 == 0) {
            return null;
        }
        return new BigDecimal(new String(this.zzws, 0, zza2));
    }

    private final char zzj(BufferedReader bufferedReader) throws ParseException, IOException {
        if (bufferedReader.read(this.zzwq) == -1) {
            return 0;
        }
        while (Character.isWhitespace(this.zzwq[0])) {
            if (bufferedReader.read(this.zzwq) == -1) {
                return 0;
            }
        }
        return this.zzwq[0];
    }

    private final void zzk(int i) throws ParseException {
        if (this.zzxb.isEmpty()) {
            StringBuilder sb = new StringBuilder(46);
            sb.append("Expected state ");
            sb.append(i);
            sb.append(" but had empty stack");
            throw new ParseException(sb.toString());
        }
        int intValue = ((Integer) this.zzxb.pop()).intValue();
        if (intValue != i) {
            StringBuilder sb2 = new StringBuilder(46);
            sb2.append("Expected state ");
            sb2.append(i);
            sb2.append(" but had ");
            sb2.append(intValue);
            throw new ParseException(sb2.toString());
        }
    }

    public void parse(InputStream inputStream, T t) throws ParseException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream), 1024);
        try {
            this.zzxb.push(Integer.valueOf(0));
            char zzj = zzj(bufferedReader);
            if (zzj != 0) {
                if (zzj == '[') {
                    this.zzxb.push(Integer.valueOf(5));
                    Map fieldMappings = t.getFieldMappings();
                    if (fieldMappings.size() != 1) {
                        throw new ParseException("Object array response class must have a single Field");
                    }
                    Field field = (Field) ((Entry) fieldMappings.entrySet().iterator().next()).getValue();
                    t.addConcreteTypeArrayInternal(field, field.getOutputFieldName(), zza(bufferedReader, field));
                } else if (zzj != '{') {
                    StringBuilder sb = new StringBuilder(19);
                    sb.append("Unexpected token: ");
                    sb.append(zzj);
                    throw new ParseException(sb.toString());
                } else {
                    this.zzxb.push(Integer.valueOf(1));
                    zza(bufferedReader, (FastJsonResponse) t);
                }
                zzk(0);
                try {
                    bufferedReader.close();
                } catch (IOException unused) {
                    Log.w("FastParser", "Failed to close reader while parsing.");
                }
            } else {
                throw new ParseException("No data to parse");
            }
        } catch (IOException e) {
            throw new ParseException((Throwable) e);
        } catch (Throwable th) {
            try {
                bufferedReader.close();
            } catch (IOException unused2) {
                Log.w("FastParser", "Failed to close reader while parsing.");
            }
            throw th;
        }
    }

    public void parse(String str, T t) throws ParseException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(str.getBytes());
        try {
            parse((InputStream) byteArrayInputStream, t);
            try {
            } catch (IOException unused) {
                Log.w("FastParser", "Failed to close the input stream while parsing.");
            }
        } finally {
            try {
                byteArrayInputStream.close();
            } catch (IOException unused2) {
                Log.w("FastParser", "Failed to close the input stream while parsing.");
            }
        }
    }
}
