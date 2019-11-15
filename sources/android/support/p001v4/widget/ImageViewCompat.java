package android.support.p001v4.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.annotation.RequiresApi;
import android.widget.ImageView;

/* renamed from: android.support.v4.widget.ImageViewCompat */
public class ImageViewCompat {
    static final ImageViewCompatImpl IMPL;

    /* renamed from: android.support.v4.widget.ImageViewCompat$BaseViewCompatImpl */
    static class BaseViewCompatImpl implements ImageViewCompatImpl {
        BaseViewCompatImpl() {
        }

        public ColorStateList getImageTintList(ImageView imageView) {
            if (imageView instanceof TintableImageSourceView) {
                return ((TintableImageSourceView) imageView).getSupportImageTintList();
            }
            return null;
        }

        public void setImageTintList(ImageView imageView, ColorStateList colorStateList) {
            if (imageView instanceof TintableImageSourceView) {
                ((TintableImageSourceView) imageView).setSupportImageTintList(colorStateList);
            }
        }

        public void setImageTintMode(ImageView imageView, Mode mode) {
            if (imageView instanceof TintableImageSourceView) {
                ((TintableImageSourceView) imageView).setSupportImageTintMode(mode);
            }
        }

        public Mode getImageTintMode(ImageView imageView) {
            if (imageView instanceof TintableImageSourceView) {
                return ((TintableImageSourceView) imageView).getSupportImageTintMode();
            }
            return null;
        }
    }

    /* renamed from: android.support.v4.widget.ImageViewCompat$ImageViewCompatImpl */
    interface ImageViewCompatImpl {
        ColorStateList getImageTintList(ImageView imageView);

        Mode getImageTintMode(ImageView imageView);

        void setImageTintList(ImageView imageView, ColorStateList colorStateList);

        void setImageTintMode(ImageView imageView, Mode mode);
    }

    @RequiresApi(21)
    /* renamed from: android.support.v4.widget.ImageViewCompat$LollipopViewCompatImpl */
    static class LollipopViewCompatImpl extends BaseViewCompatImpl {
        LollipopViewCompatImpl() {
        }

        public ColorStateList getImageTintList(ImageView imageView) {
            return imageView.getImageTintList();
        }

        public void setImageTintList(ImageView imageView, ColorStateList colorStateList) {
            imageView.setImageTintList(colorStateList);
            if (VERSION.SDK_INT == 21) {
                Drawable drawable = imageView.getDrawable();
                boolean z = (imageView.getImageTintList() == null || imageView.getImageTintMode() == null) ? false : true;
                if (drawable != null && z) {
                    if (drawable.isStateful()) {
                        drawable.setState(imageView.getDrawableState());
                    }
                    imageView.setImageDrawable(drawable);
                }
            }
        }

        public void setImageTintMode(ImageView imageView, Mode mode) {
            imageView.setImageTintMode(mode);
            if (VERSION.SDK_INT == 21) {
                Drawable drawable = imageView.getDrawable();
                boolean z = (imageView.getImageTintList() == null || imageView.getImageTintMode() == null) ? false : true;
                if (drawable != null && z) {
                    if (drawable.isStateful()) {
                        drawable.setState(imageView.getDrawableState());
                    }
                    imageView.setImageDrawable(drawable);
                }
            }
        }

        public Mode getImageTintMode(ImageView imageView) {
            return imageView.getImageTintMode();
        }
    }

    static {
        if (VERSION.SDK_INT >= 21) {
            IMPL = new LollipopViewCompatImpl();
        } else {
            IMPL = new BaseViewCompatImpl();
        }
    }

    public static ColorStateList getImageTintList(ImageView imageView) {
        return IMPL.getImageTintList(imageView);
    }

    public static void setImageTintList(ImageView imageView, ColorStateList colorStateList) {
        IMPL.setImageTintList(imageView, colorStateList);
    }

    public static Mode getImageTintMode(ImageView imageView) {
        return IMPL.getImageTintMode(imageView);
    }

    public static void setImageTintMode(ImageView imageView, Mode mode) {
        IMPL.setImageTintMode(imageView, mode);
    }

    private ImageViewCompat() {
    }
}
