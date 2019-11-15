package com.google.android.gms.common.util;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.google.android.gms.common.internal.Preconditions;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@VisibleForTesting
public class Strings {
    private static final Pattern zzaak = Pattern.compile("\\$\\{(.*?)\\}");

    private Strings() {
    }

    public static String capitalize(@NonNull String str) {
        if (str.length() == 0) {
            return str;
        }
        char charAt = str.charAt(0);
        char upperCase = Character.toUpperCase(charAt);
        if (charAt == upperCase) {
            return str;
        }
        String substring = str.substring(1);
        StringBuilder sb = new StringBuilder(String.valueOf(substring).length() + 1);
        sb.append(upperCase);
        sb.append(substring);
        return sb.toString();
    }

    @Nullable
    public static String emptyToNull(@Nullable String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return str;
    }

    public static String format(@NonNull String str, @NonNull Bundle bundle) {
        Matcher matcher = zzaak.matcher(str);
        if (!matcher.find()) {
            return str;
        }
        StringBuffer stringBuffer = new StringBuffer();
        do {
            String group = matcher.group(1);
            Object obj = bundle.get(group);
            String str2 = obj != null ? obj.toString() : bundle.containsKey(group) ? "null" : "";
            matcher.appendReplacement(stringBuffer, str2);
        } while (matcher.find());
        matcher.appendTail(stringBuffer);
        return stringBuffer.toString();
    }

    public static boolean isEmptyOrWhitespace(@Nullable String str) {
        return str == null || str.trim().isEmpty();
    }

    public static String nullToEmpty(@Nullable String str) {
        return str == null ? "" : str;
    }

    public static String padEnd(@NonNull String str, int i, char c) {
        Preconditions.checkNotNull(str);
        if (str.length() >= i) {
            return str;
        }
        StringBuilder sb = new StringBuilder(i);
        sb.append(str);
        for (int length = str.length(); length < i; length++) {
            sb.append(c);
        }
        return sb.toString();
    }
}
