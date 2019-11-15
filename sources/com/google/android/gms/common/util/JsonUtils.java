package com.google.android.gms.common.util;

import android.text.TextUtils;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@VisibleForTesting
public final class JsonUtils {
    private static final Pattern zzaae = Pattern.compile("\\\\.");
    private static final Pattern zzaaf = Pattern.compile("[\\\\\"/\b\f\n\r\t]");

    private JsonUtils() {
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:11|12|13) */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0029, code lost:
        return areJsonValuesEquivalent(new org.json.JSONArray(r3), new org.json.JSONArray(r4));
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x001b */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean areJsonStringsEquivalent(java.lang.String r3, java.lang.String r4) {
        /*
            if (r3 != 0) goto L_0x0006
            if (r4 != 0) goto L_0x0006
            r3 = 1
            return r3
        L_0x0006:
            r0 = 0
            if (r3 == 0) goto L_0x002a
            if (r4 != 0) goto L_0x000c
            return r0
        L_0x000c:
            org.json.JSONObject r1 = new org.json.JSONObject     // Catch:{ JSONException -> 0x001b }
            r1.<init>(r3)     // Catch:{ JSONException -> 0x001b }
            org.json.JSONObject r2 = new org.json.JSONObject     // Catch:{ JSONException -> 0x001b }
            r2.<init>(r4)     // Catch:{ JSONException -> 0x001b }
            boolean r1 = areJsonValuesEquivalent(r1, r2)     // Catch:{ JSONException -> 0x001b }
            return r1
        L_0x001b:
            org.json.JSONArray r1 = new org.json.JSONArray     // Catch:{ JSONException -> 0x002a }
            r1.<init>(r3)     // Catch:{ JSONException -> 0x002a }
            org.json.JSONArray r3 = new org.json.JSONArray     // Catch:{ JSONException -> 0x002a }
            r3.<init>(r4)     // Catch:{ JSONException -> 0x002a }
            boolean r3 = areJsonValuesEquivalent(r1, r3)     // Catch:{ JSONException -> 0x002a }
            return r3
        L_0x002a:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.util.JsonUtils.areJsonStringsEquivalent(java.lang.String, java.lang.String):boolean");
    }

    public static boolean areJsonValuesEquivalent(Object obj, Object obj2) {
        if (obj == null && obj2 == null) {
            return true;
        }
        if (obj == null || obj2 == null) {
            return false;
        }
        if ((obj instanceof JSONObject) && (obj2 instanceof JSONObject)) {
            JSONObject jSONObject = (JSONObject) obj;
            JSONObject jSONObject2 = (JSONObject) obj2;
            if (jSONObject.length() != jSONObject2.length()) {
                return false;
            }
            Iterator keys = jSONObject.keys();
            while (keys.hasNext()) {
                String str = (String) keys.next();
                if (!jSONObject2.has(str)) {
                    return false;
                }
                try {
                    if (!areJsonValuesEquivalent(jSONObject.get(str), jSONObject2.get(str))) {
                        return false;
                    }
                } catch (JSONException unused) {
                }
            }
            return true;
        } else if (!(obj instanceof JSONArray) || !(obj2 instanceof JSONArray)) {
            return obj.equals(obj2);
        } else {
            JSONArray jSONArray = (JSONArray) obj;
            JSONArray jSONArray2 = (JSONArray) obj2;
            if (jSONArray.length() != jSONArray2.length()) {
                return false;
            }
            int i = 0;
            while (i < jSONArray.length()) {
                try {
                    if (!areJsonValuesEquivalent(jSONArray.get(i), jSONArray2.get(i))) {
                        return false;
                    }
                    i++;
                } catch (JSONException unused2) {
                    return false;
                }
            }
            return true;
        }
    }

    public static String escapeString(String str) {
        String str2;
        if (!TextUtils.isEmpty(str)) {
            Matcher matcher = zzaaf.matcher(str);
            StringBuffer stringBuffer = null;
            while (matcher.find()) {
                if (stringBuffer == null) {
                    stringBuffer = new StringBuffer();
                }
                char charAt = matcher.group().charAt(0);
                if (charAt == '\"') {
                    str2 = "\\\\\\\"";
                } else if (charAt == '/') {
                    str2 = "\\\\/";
                } else if (charAt != '\\') {
                    switch (charAt) {
                        case 8:
                            str2 = "\\\\b";
                            break;
                        case 9:
                            str2 = "\\\\t";
                            break;
                        case 10:
                            str2 = "\\\\n";
                            break;
                        default:
                            switch (charAt) {
                                case 12:
                                    str2 = "\\\\f";
                                    break;
                                case 13:
                                    str2 = "\\\\r";
                                    break;
                                default:
                                    continue;
                            }
                    }
                } else {
                    str2 = "\\\\\\\\";
                }
                matcher.appendReplacement(stringBuffer, str2);
            }
            if (stringBuffer == null) {
                return str;
            }
            matcher.appendTail(stringBuffer);
            str = stringBuffer.toString();
        }
        return str;
    }

    public static String unescapeString(String str) {
        String str2;
        if (!TextUtils.isEmpty(str)) {
            String unescape = UnicodeUtils.unescape(str);
            Matcher matcher = zzaae.matcher(unescape);
            StringBuffer stringBuffer = null;
            while (matcher.find()) {
                if (stringBuffer == null) {
                    stringBuffer = new StringBuffer();
                }
                char charAt = matcher.group().charAt(1);
                if (charAt == '\"') {
                    str2 = "\"";
                } else if (charAt == '/') {
                    str2 = "/";
                } else if (charAt == '\\') {
                    str2 = "\\\\";
                } else if (charAt == 'b') {
                    str2 = "\b";
                } else if (charAt == 'f') {
                    str2 = "\f";
                } else if (charAt == 'n') {
                    str2 = "\n";
                } else if (charAt == 'r') {
                    str2 = "\r";
                } else if (charAt != 't') {
                    throw new IllegalStateException("Found an escaped character that should never be.");
                } else {
                    str2 = "\t";
                }
                matcher.appendReplacement(stringBuffer, str2);
            }
            if (stringBuffer == null) {
                return unescape;
            }
            matcher.appendTail(stringBuffer);
            str = stringBuffer.toString();
        }
        return str;
    }
}
