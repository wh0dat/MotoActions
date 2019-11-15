package dagger.internal;

public final class Preconditions {
    public static <T> T checkNotNull(T t) {
        if (t != null) {
            return t;
        }
        throw new NullPointerException();
    }

    public static <T> T checkNotNull(T t, String str) {
        if (t != null) {
            return t;
        }
        throw new NullPointerException(str);
    }

    public static <T> T checkNotNull(T t, String str, Object obj) {
        if (t != null) {
            return t;
        }
        if (!str.contains("%s")) {
            throw new IllegalArgumentException("errorMessageTemplate has no format specifiers");
        } else if (str.indexOf("%s") != str.lastIndexOf("%s")) {
            throw new IllegalArgumentException("errorMessageTemplate has more than one format specifier");
        } else {
            throw new NullPointerException(str.replaceFirst("%s", String.valueOf(obj)));
        }
    }

    private Preconditions() {
    }
}
