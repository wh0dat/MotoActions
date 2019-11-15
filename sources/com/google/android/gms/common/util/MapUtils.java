package com.google.android.gms.common.util;

import java.util.HashMap;
import java.util.Map;

public class MapUtils {
    public static <K, V> K getKeyFromMap(Map<K, V> map, K k) {
        if (map.containsKey(k)) {
            for (K next : map.keySet()) {
                if (next.equals(k)) {
                    return next;
                }
            }
        }
        return null;
    }

    public static void writeStringMapToJson(StringBuilder sb, HashMap<String, String> hashMap) {
        String str;
        sb.append("{");
        boolean z = true;
        for (String str2 : hashMap.keySet()) {
            if (!z) {
                sb.append(",");
            } else {
                z = false;
            }
            String str3 = (String) hashMap.get(str2);
            sb.append("\"");
            sb.append(str2);
            sb.append("\":");
            if (str3 == null) {
                str = "null";
            } else {
                sb.append("\"");
                sb.append(str3);
                str = "\"";
            }
            sb.append(str);
        }
        sb.append("}");
    }
}
