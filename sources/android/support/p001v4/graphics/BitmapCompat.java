package android.support.p001v4.graphics;

import android.graphics.Bitmap;
import android.os.Build.VERSION;
import android.support.annotation.RequiresApi;

/* renamed from: android.support.v4.graphics.BitmapCompat */
public final class BitmapCompat {
    static final BitmapCompatBaseImpl IMPL;

    @RequiresApi(18)
    /* renamed from: android.support.v4.graphics.BitmapCompat$BitmapCompatApi18Impl */
    static class BitmapCompatApi18Impl extends BitmapCompatBaseImpl {
        BitmapCompatApi18Impl() {
        }

        public boolean hasMipMap(Bitmap bitmap) {
            return bitmap.hasMipMap();
        }

        public void setHasMipMap(Bitmap bitmap, boolean z) {
            bitmap.setHasMipMap(z);
        }
    }

    @RequiresApi(19)
    /* renamed from: android.support.v4.graphics.BitmapCompat$BitmapCompatApi19Impl */
    static class BitmapCompatApi19Impl extends BitmapCompatApi18Impl {
        BitmapCompatApi19Impl() {
        }

        public int getAllocationByteCount(Bitmap bitmap) {
            return bitmap.getAllocationByteCount();
        }
    }

    /* renamed from: android.support.v4.graphics.BitmapCompat$BitmapCompatBaseImpl */
    static class BitmapCompatBaseImpl {
        public boolean hasMipMap(Bitmap bitmap) {
            return false;
        }

        public void setHasMipMap(Bitmap bitmap, boolean z) {
        }

        BitmapCompatBaseImpl() {
        }

        public int getAllocationByteCount(Bitmap bitmap) {
            return bitmap.getByteCount();
        }
    }

    static {
        if (VERSION.SDK_INT >= 19) {
            IMPL = new BitmapCompatApi19Impl();
        } else if (VERSION.SDK_INT >= 18) {
            IMPL = new BitmapCompatApi18Impl();
        } else {
            IMPL = new BitmapCompatBaseImpl();
        }
    }

    public static boolean hasMipMap(Bitmap bitmap) {
        return IMPL.hasMipMap(bitmap);
    }

    public static void setHasMipMap(Bitmap bitmap, boolean z) {
        IMPL.setHasMipMap(bitmap, z);
    }

    public static int getAllocationByteCount(Bitmap bitmap) {
        return IMPL.getAllocationByteCount(bitmap);
    }

    private BitmapCompat() {
    }
}
