package android.support.p001v4.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.CancellationSignal;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.p001v4.content.res.FontResourcesParserCompat.FontFamilyFilesResourceEntry;
import android.support.p001v4.content.res.FontResourcesParserCompat.FontFileResourceEntry;
import android.support.p001v4.provider.FontsContractCompat.FontInfo;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@RequiresApi(14)
@RestrictTo({Scope.LIBRARY_GROUP})
/* renamed from: android.support.v4.graphics.TypefaceCompatBaseImpl */
class TypefaceCompatBaseImpl implements TypefaceCompatImpl {
    private static final String CACHE_FILE_PREFIX = "cached_font_";
    private static final String TAG = "TypefaceCompatBaseImpl";

    /* renamed from: android.support.v4.graphics.TypefaceCompatBaseImpl$StyleExtractor */
    private interface StyleExtractor<T> {
        int getWeight(T t);

        boolean isItalic(T t);
    }

    TypefaceCompatBaseImpl() {
    }

    private static <T> T findBestFont(T[] tArr, int i, StyleExtractor<T> styleExtractor) {
        int i2 = (i & 1) == 0 ? 400 : 700;
        boolean z = (i & 2) != 0;
        int i3 = Integer.MAX_VALUE;
        T t = null;
        for (T t2 : tArr) {
            int abs = (Math.abs(styleExtractor.getWeight(t2) - i2) * 2) + (styleExtractor.isItalic(t2) == z ? 0 : 1);
            if (t == null || i3 > abs) {
                t = t2;
                i3 = abs;
            }
        }
        return t;
    }

    /* access modifiers changed from: protected */
    public FontInfo findBestInfo(FontInfo[] fontInfoArr, int i) {
        return (FontInfo) findBestFont(fontInfoArr, i, new StyleExtractor<FontInfo>() {
            public int getWeight(FontInfo fontInfo) {
                return fontInfo.getWeight();
            }

            public boolean isItalic(FontInfo fontInfo) {
                return fontInfo.isItalic();
            }
        });
    }

    /* access modifiers changed from: protected */
    public Typeface createFromInputStream(Context context, InputStream inputStream) {
        File tempFile = TypefaceCompatUtil.getTempFile(context);
        if (tempFile == null) {
            return null;
        }
        try {
            if (!TypefaceCompatUtil.copyToFile(tempFile, inputStream)) {
                return null;
            }
            Typeface createFromFile = Typeface.createFromFile(tempFile.getPath());
            tempFile.delete();
            return createFromFile;
        } catch (RuntimeException unused) {
            return null;
        } finally {
            tempFile.delete();
        }
    }

    public Typeface createFromFontInfo(Context context, @Nullable CancellationSignal cancellationSignal, @NonNull FontInfo[] fontInfoArr, int i) {
        InputStream inputStream;
        InputStream inputStream2 = null;
        if (fontInfoArr.length < 1) {
            return null;
        }
        try {
            inputStream = context.getContentResolver().openInputStream(findBestInfo(fontInfoArr, i).getUri());
            try {
                Typeface createFromInputStream = createFromInputStream(context, inputStream);
                TypefaceCompatUtil.closeQuietly(inputStream);
                return createFromInputStream;
            } catch (IOException unused) {
                TypefaceCompatUtil.closeQuietly(inputStream);
                return null;
            } catch (Throwable th) {
                th = th;
                inputStream2 = inputStream;
                TypefaceCompatUtil.closeQuietly(inputStream2);
                throw th;
            }
        } catch (IOException unused2) {
            inputStream = null;
            TypefaceCompatUtil.closeQuietly(inputStream);
            return null;
        } catch (Throwable th2) {
            th = th2;
            TypefaceCompatUtil.closeQuietly(inputStream2);
            throw th;
        }
    }

    private FontFileResourceEntry findBestEntry(FontFamilyFilesResourceEntry fontFamilyFilesResourceEntry, int i) {
        return (FontFileResourceEntry) findBestFont(fontFamilyFilesResourceEntry.getEntries(), i, new StyleExtractor<FontFileResourceEntry>() {
            public int getWeight(FontFileResourceEntry fontFileResourceEntry) {
                return fontFileResourceEntry.getWeight();
            }

            public boolean isItalic(FontFileResourceEntry fontFileResourceEntry) {
                return fontFileResourceEntry.isItalic();
            }
        });
    }

    @Nullable
    public Typeface createFromFontFamilyFilesResourceEntry(Context context, FontFamilyFilesResourceEntry fontFamilyFilesResourceEntry, Resources resources, int i) {
        FontFileResourceEntry findBestEntry = findBestEntry(fontFamilyFilesResourceEntry, i);
        if (findBestEntry == null) {
            return null;
        }
        return TypefaceCompat.createFromResourcesFontFile(context, resources, findBestEntry.getResourceId(), findBestEntry.getFileName(), i);
    }

    @Nullable
    public Typeface createFromResourcesFontFile(Context context, Resources resources, int i, String str, int i2) {
        File tempFile = TypefaceCompatUtil.getTempFile(context);
        if (tempFile == null) {
            return null;
        }
        try {
            if (!TypefaceCompatUtil.copyToFile(tempFile, resources, i)) {
                return null;
            }
            Typeface createFromFile = Typeface.createFromFile(tempFile.getPath());
            tempFile.delete();
            return createFromFile;
        } catch (RuntimeException unused) {
            return null;
        } finally {
            tempFile.delete();
        }
    }
}
