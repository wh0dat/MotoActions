package android.support.p001v4.p003os;

import android.os.Build.VERSION;
import android.support.annotation.GuardedBy;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.annotation.Size;
import com.motorola.actions.checkin.CommonCheckinAttributes;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;

@RequiresApi(14)
@RestrictTo({Scope.LIBRARY_GROUP})
/* renamed from: android.support.v4.os.LocaleListHelper */
final class LocaleListHelper {
    private static final Locale EN_LATN = LocaleHelper.forLanguageTag("en-Latn");
    private static final Locale LOCALE_AR_XB = new Locale("ar", "XB");
    private static final Locale LOCALE_EN_XA = new Locale(CommonCheckinAttributes.KEY_ENABLED, "XA");
    private static final int NUM_PSEUDO_LOCALES = 2;
    private static final String STRING_AR_XB = "ar-XB";
    private static final String STRING_EN_XA = "en-XA";
    @GuardedBy("sLock")
    private static LocaleListHelper sDefaultAdjustedLocaleList;
    @GuardedBy("sLock")
    private static LocaleListHelper sDefaultLocaleList;
    private static final Locale[] sEmptyList = new Locale[0];
    private static final LocaleListHelper sEmptyLocaleList = new LocaleListHelper(new Locale[0]);
    @GuardedBy("sLock")
    private static Locale sLastDefaultLocale;
    @GuardedBy("sLock")
    private static LocaleListHelper sLastExplicitlySetLocaleList;
    private static final Object sLock = new Object();
    private final Locale[] mList;
    @NonNull
    private final String mStringRepresentation;

    /* access modifiers changed from: 0000 */
    @RestrictTo({Scope.LIBRARY_GROUP})
    public Locale get(int i) {
        if (i < 0 || i >= this.mList.length) {
            return null;
        }
        return this.mList[i];
    }

    /* access modifiers changed from: 0000 */
    @RestrictTo({Scope.LIBRARY_GROUP})
    public boolean isEmpty() {
        return this.mList.length == 0;
    }

    /* access modifiers changed from: 0000 */
    @IntRange(from = 0)
    @RestrictTo({Scope.LIBRARY_GROUP})
    public int size() {
        return this.mList.length;
    }

    /* access modifiers changed from: 0000 */
    @IntRange(from = -1)
    @RestrictTo({Scope.LIBRARY_GROUP})
    public int indexOf(Locale locale) {
        for (int i = 0; i < this.mList.length; i++) {
            if (this.mList[i].equals(locale)) {
                return i;
            }
        }
        return -1;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof LocaleListHelper)) {
            return false;
        }
        Locale[] localeArr = ((LocaleListHelper) obj).mList;
        if (this.mList.length != localeArr.length) {
            return false;
        }
        for (int i = 0; i < this.mList.length; i++) {
            if (!this.mList[i].equals(localeArr[i])) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int i = 1;
        for (Locale hashCode : this.mList) {
            i = (i * 31) + hashCode.hashCode();
        }
        return i;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < this.mList.length; i++) {
            sb.append(this.mList[i]);
            if (i < this.mList.length - 1) {
                sb.append(',');
            }
        }
        sb.append("]");
        return sb.toString();
    }

    /* access modifiers changed from: 0000 */
    @RestrictTo({Scope.LIBRARY_GROUP})
    @NonNull
    public String toLanguageTags() {
        return this.mStringRepresentation;
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    LocaleListHelper(@NonNull Locale... localeArr) {
        if (localeArr.length == 0) {
            this.mList = sEmptyList;
            this.mStringRepresentation = "";
            return;
        }
        Locale[] localeArr2 = new Locale[localeArr.length];
        HashSet hashSet = new HashSet();
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < localeArr.length) {
            Locale locale = localeArr[i];
            if (locale == null) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("list[");
                sb2.append(i);
                sb2.append("] is null");
                throw new NullPointerException(sb2.toString());
            } else if (hashSet.contains(locale)) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append("list[");
                sb3.append(i);
                sb3.append("] is a repetition");
                throw new IllegalArgumentException(sb3.toString());
            } else {
                Locale locale2 = (Locale) locale.clone();
                localeArr2[i] = locale2;
                sb.append(LocaleHelper.toLanguageTag(locale2));
                if (i < localeArr.length - 1) {
                    sb.append(',');
                }
                hashSet.add(locale2);
                i++;
            }
        }
        this.mList = localeArr2;
        this.mStringRepresentation = sb.toString();
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    LocaleListHelper(@NonNull Locale locale, LocaleListHelper localeListHelper) {
        int i;
        if (locale == null) {
            throw new NullPointerException("topLocale is null");
        }
        if (localeListHelper == null) {
            i = 0;
        } else {
            i = localeListHelper.mList.length;
        }
        int i2 = 0;
        while (true) {
            if (i2 >= i) {
                i2 = -1;
                break;
            } else if (locale.equals(localeListHelper.mList[i2])) {
                break;
            } else {
                i2++;
            }
        }
        int i3 = (i2 == -1 ? 1 : 0) + i;
        Locale[] localeArr = new Locale[i3];
        localeArr[0] = (Locale) locale.clone();
        if (i2 == -1) {
            int i4 = 0;
            while (i4 < i) {
                int i5 = i4 + 1;
                localeArr[i5] = (Locale) localeListHelper.mList[i4].clone();
                i4 = i5;
            }
        } else {
            int i6 = 0;
            while (i6 < i2) {
                int i7 = i6 + 1;
                localeArr[i7] = (Locale) localeListHelper.mList[i6].clone();
                i6 = i7;
            }
            for (int i8 = i2 + 1; i8 < i; i8++) {
                localeArr[i8] = (Locale) localeListHelper.mList[i8].clone();
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i9 = 0; i9 < i3; i9++) {
            sb.append(LocaleHelper.toLanguageTag(localeArr[i9]));
            if (i9 < i3 - 1) {
                sb.append(',');
            }
        }
        this.mList = localeArr;
        this.mStringRepresentation = sb.toString();
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    @NonNull
    static LocaleListHelper getEmptyLocaleList() {
        return sEmptyLocaleList;
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    @NonNull
    static LocaleListHelper forLanguageTags(@Nullable String str) {
        if (str == null || str.isEmpty()) {
            return getEmptyLocaleList();
        }
        String[] split = str.split(",");
        Locale[] localeArr = new Locale[split.length];
        for (int i = 0; i < localeArr.length; i++) {
            localeArr[i] = LocaleHelper.forLanguageTag(split[i]);
        }
        return new LocaleListHelper(localeArr);
    }

    private static String getLikelyScript(Locale locale) {
        if (VERSION.SDK_INT < 21) {
            return "";
        }
        String script = locale.getScript();
        return !script.isEmpty() ? script : "";
    }

    private static boolean isPseudoLocale(String str) {
        return STRING_EN_XA.equals(str) || STRING_AR_XB.equals(str);
    }

    private static boolean isPseudoLocale(Locale locale) {
        return LOCALE_EN_XA.equals(locale) || LOCALE_AR_XB.equals(locale);
    }

    @IntRange(from = 0, mo542to = 1)
    private static int matchScore(Locale locale, Locale locale2) {
        int i = 1;
        if (locale.equals(locale2)) {
            return 1;
        }
        if (!locale.getLanguage().equals(locale2.getLanguage()) || isPseudoLocale(locale) || isPseudoLocale(locale2)) {
            return 0;
        }
        String likelyScript = getLikelyScript(locale);
        if (!likelyScript.isEmpty()) {
            return likelyScript.equals(getLikelyScript(locale2)) ? 1 : 0;
        }
        String country = locale.getCountry();
        if (!country.isEmpty() && !country.equals(locale2.getCountry())) {
            i = 0;
        }
        return i;
    }

    private int findFirstMatchIndex(Locale locale) {
        for (int i = 0; i < this.mList.length; i++) {
            if (matchScore(locale, this.mList[i]) > 0) {
                return i;
            }
        }
        return Integer.MAX_VALUE;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x001d, code lost:
        if (r5 < Integer.MAX_VALUE) goto L_0x0021;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int computeFirstMatchIndex(java.util.Collection<java.lang.String> r4, boolean r5) {
        /*
            r3 = this;
            java.util.Locale[] r0 = r3.mList
            int r0 = r0.length
            r1 = 0
            r2 = 1
            if (r0 != r2) goto L_0x0008
            return r1
        L_0x0008:
            java.util.Locale[] r0 = r3.mList
            int r0 = r0.length
            if (r0 != 0) goto L_0x000f
            r3 = -1
            return r3
        L_0x000f:
            r0 = 2147483647(0x7fffffff, float:NaN)
            if (r5 == 0) goto L_0x0020
            java.util.Locale r5 = EN_LATN
            int r5 = r3.findFirstMatchIndex(r5)
            if (r5 != 0) goto L_0x001d
            return r1
        L_0x001d:
            if (r5 >= r0) goto L_0x0020
            goto L_0x0021
        L_0x0020:
            r5 = r0
        L_0x0021:
            java.util.Iterator r4 = r4.iterator()
        L_0x0025:
            boolean r2 = r4.hasNext()
            if (r2 == 0) goto L_0x0040
            java.lang.Object r2 = r4.next()
            java.lang.String r2 = (java.lang.String) r2
            java.util.Locale r2 = android.support.p001v4.p003os.LocaleHelper.forLanguageTag(r2)
            int r2 = r3.findFirstMatchIndex(r2)
            if (r2 != 0) goto L_0x003c
            return r1
        L_0x003c:
            if (r2 >= r5) goto L_0x0025
            r5 = r2
            goto L_0x0025
        L_0x0040:
            if (r5 != r0) goto L_0x0043
            return r1
        L_0x0043:
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.p001v4.p003os.LocaleListHelper.computeFirstMatchIndex(java.util.Collection, boolean):int");
    }

    private Locale computeFirstMatch(Collection<String> collection, boolean z) {
        int computeFirstMatchIndex = computeFirstMatchIndex(collection, z);
        if (computeFirstMatchIndex == -1) {
            return null;
        }
        return this.mList[computeFirstMatchIndex];
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    @RestrictTo({Scope.LIBRARY_GROUP})
    public Locale getFirstMatch(String[] strArr) {
        return computeFirstMatch(Arrays.asList(strArr), false);
    }

    /* access modifiers changed from: 0000 */
    @RestrictTo({Scope.LIBRARY_GROUP})
    public int getFirstMatchIndex(String[] strArr) {
        return computeFirstMatchIndex(Arrays.asList(strArr), false);
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    @RestrictTo({Scope.LIBRARY_GROUP})
    public Locale getFirstMatchWithEnglishSupported(String[] strArr) {
        return computeFirstMatch(Arrays.asList(strArr), true);
    }

    /* access modifiers changed from: 0000 */
    @RestrictTo({Scope.LIBRARY_GROUP})
    public int getFirstMatchIndexWithEnglishSupported(Collection<String> collection) {
        return computeFirstMatchIndex(collection, true);
    }

    /* access modifiers changed from: 0000 */
    @RestrictTo({Scope.LIBRARY_GROUP})
    public int getFirstMatchIndexWithEnglishSupported(String[] strArr) {
        return getFirstMatchIndexWithEnglishSupported((Collection<String>) Arrays.asList(strArr));
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    static boolean isPseudoLocalesOnly(@Nullable String[] strArr) {
        if (strArr == null) {
            return true;
        }
        if (strArr.length > 3) {
            return false;
        }
        for (String str : strArr) {
            if (!str.isEmpty() && !isPseudoLocale(str)) {
                return false;
            }
        }
        return true;
    }

    @Size(min = 1)
    @RestrictTo({Scope.LIBRARY_GROUP})
    @NonNull
    static LocaleListHelper getDefault() {
        Locale locale = Locale.getDefault();
        synchronized (sLock) {
            if (!locale.equals(sLastDefaultLocale)) {
                sLastDefaultLocale = locale;
                if (sDefaultLocaleList == null || !locale.equals(sDefaultLocaleList.get(0))) {
                    sDefaultLocaleList = new LocaleListHelper(locale, sLastExplicitlySetLocaleList);
                    sDefaultAdjustedLocaleList = sDefaultLocaleList;
                } else {
                    LocaleListHelper localeListHelper = sDefaultLocaleList;
                    return localeListHelper;
                }
            }
            LocaleListHelper localeListHelper2 = sDefaultLocaleList;
            return localeListHelper2;
        }
    }

    @Size(min = 1)
    @NonNull
    static LocaleListHelper getAdjustedDefault() {
        LocaleListHelper localeListHelper;
        getDefault();
        synchronized (sLock) {
            localeListHelper = sDefaultAdjustedLocaleList;
        }
        return localeListHelper;
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    static void setDefault(@Size(min = 1) @NonNull LocaleListHelper localeListHelper) {
        setDefault(localeListHelper, 0);
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    static void setDefault(@Size(min = 1) @NonNull LocaleListHelper localeListHelper, int i) {
        if (localeListHelper == null) {
            throw new NullPointerException("locales is null");
        } else if (localeListHelper.isEmpty()) {
            throw new IllegalArgumentException("locales is empty");
        } else {
            synchronized (sLock) {
                sLastDefaultLocale = localeListHelper.get(i);
                Locale.setDefault(sLastDefaultLocale);
                sLastExplicitlySetLocaleList = localeListHelper;
                sDefaultLocaleList = localeListHelper;
                if (i == 0) {
                    sDefaultAdjustedLocaleList = sDefaultLocaleList;
                } else {
                    sDefaultAdjustedLocaleList = new LocaleListHelper(sLastDefaultLocale, sDefaultLocaleList);
                }
            }
        }
    }
}
