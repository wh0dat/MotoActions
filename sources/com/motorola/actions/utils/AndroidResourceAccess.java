package com.motorola.actions.utils;

import android.content.res.Resources;
import java.util.Optional;

public class AndroidResourceAccess {
    public static Optional<Boolean> getBoolResource(String str) {
        Resources system = Resources.getSystem();
        int identifier = system.getIdentifier(str, "bool", "android");
        return identifier == 0 ? Optional.empty() : Optional.of(Boolean.valueOf(system.getBoolean(identifier)));
    }

    public static Optional<String> getStringResource(String str) {
        Resources system = Resources.getSystem();
        int identifier = system.getIdentifier(str, "string", "android");
        return identifier == 0 ? Optional.empty() : Optional.of(system.getString(identifier));
    }

    public static Optional<Integer> getIntResource(String str) {
        Resources system = Resources.getSystem();
        int identifier = system.getIdentifier(str, "integer", "android");
        return identifier == 0 ? Optional.empty() : Optional.of(Integer.valueOf(system.getInteger(identifier)));
    }

    public static Optional<Integer> getDimResource(String str) {
        Resources system = Resources.getSystem();
        int identifier = system.getIdentifier(str, "dimen", "android");
        return identifier == 0 ? Optional.empty() : Optional.of(Integer.valueOf(system.getDimensionPixelSize(identifier)));
    }

    public static Optional<int[]> getArrayResource(String str) {
        Resources system = Resources.getSystem();
        int identifier = system.getIdentifier(str, "array", "android");
        return identifier == 0 ? Optional.empty() : Optional.of(system.getIntArray(identifier));
    }
}
