package android.support.p004v7.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.p001v4.widget.ImageViewCompat;
import android.support.p004v7.appcompat.C0383R;
import android.support.p004v7.content.res.AppCompatResources;
import android.util.AttributeSet;
import android.widget.ImageView;

@RestrictTo({Scope.LIBRARY_GROUP})
/* renamed from: android.support.v7.widget.AppCompatImageHelper */
public class AppCompatImageHelper {
    private TintInfo mImageTint;
    private TintInfo mInternalImageTint;
    private TintInfo mTmpInfo;
    private final ImageView mView;

    public AppCompatImageHelper(ImageView imageView) {
        this.mView = imageView;
    }

    public void loadFromAttributes(AttributeSet attributeSet, int i) {
        TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(this.mView.getContext(), attributeSet, C0383R.styleable.AppCompatImageView, i, 0);
        try {
            Drawable drawable = this.mView.getDrawable();
            if (drawable == null) {
                int resourceId = obtainStyledAttributes.getResourceId(C0383R.styleable.AppCompatImageView_srcCompat, -1);
                if (resourceId != -1) {
                    drawable = AppCompatResources.getDrawable(this.mView.getContext(), resourceId);
                    if (drawable != null) {
                        this.mView.setImageDrawable(drawable);
                    }
                }
            }
            if (drawable != null) {
                DrawableUtils.fixDrawable(drawable);
            }
            if (obtainStyledAttributes.hasValue(C0383R.styleable.AppCompatImageView_tint)) {
                ImageViewCompat.setImageTintList(this.mView, obtainStyledAttributes.getColorStateList(C0383R.styleable.AppCompatImageView_tint));
            }
            if (obtainStyledAttributes.hasValue(C0383R.styleable.AppCompatImageView_tintMode)) {
                ImageViewCompat.setImageTintMode(this.mView, DrawableUtils.parseTintMode(obtainStyledAttributes.getInt(C0383R.styleable.AppCompatImageView_tintMode, -1), null));
            }
        } finally {
            obtainStyledAttributes.recycle();
        }
    }

    public void setImageResource(int i) {
        if (i != 0) {
            Drawable drawable = AppCompatResources.getDrawable(this.mView.getContext(), i);
            if (drawable != null) {
                DrawableUtils.fixDrawable(drawable);
            }
            this.mView.setImageDrawable(drawable);
        } else {
            this.mView.setImageDrawable(null);
        }
        applySupportImageTint();
    }

    /* access modifiers changed from: 0000 */
    public boolean hasOverlappingRendering() {
        return VERSION.SDK_INT < 21 || !(this.mView.getBackground() instanceof RippleDrawable);
    }

    /* access modifiers changed from: 0000 */
    public void setSupportImageTintList(ColorStateList colorStateList) {
        if (this.mImageTint == null) {
            this.mImageTint = new TintInfo();
        }
        this.mImageTint.mTintList = colorStateList;
        this.mImageTint.mHasTintList = true;
        applySupportImageTint();
    }

    /* access modifiers changed from: 0000 */
    public ColorStateList getSupportImageTintList() {
        if (this.mImageTint != null) {
            return this.mImageTint.mTintList;
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    public void setSupportImageTintMode(Mode mode) {
        if (this.mImageTint == null) {
            this.mImageTint = new TintInfo();
        }
        this.mImageTint.mTintMode = mode;
        this.mImageTint.mHasTintMode = true;
        applySupportImageTint();
    }

    /* access modifiers changed from: 0000 */
    public Mode getSupportImageTintMode() {
        if (this.mImageTint != null) {
            return this.mImageTint.mTintMode;
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    public void applySupportImageTint() {
        Drawable drawable = this.mView.getDrawable();
        if (drawable != null) {
            DrawableUtils.fixDrawable(drawable);
        }
        if (drawable != null && (!shouldApplyFrameworkTintUsingColorFilter() || !applyFrameworkTintUsingColorFilter(drawable))) {
            if (this.mImageTint != null) {
                AppCompatDrawableManager.tintDrawable(drawable, this.mImageTint, this.mView.getDrawableState());
            } else if (this.mInternalImageTint != null) {
                AppCompatDrawableManager.tintDrawable(drawable, this.mInternalImageTint, this.mView.getDrawableState());
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void setInternalImageTint(ColorStateList colorStateList) {
        if (colorStateList != null) {
            if (this.mInternalImageTint == null) {
                this.mInternalImageTint = new TintInfo();
            }
            this.mInternalImageTint.mTintList = colorStateList;
            this.mInternalImageTint.mHasTintList = true;
        } else {
            this.mInternalImageTint = null;
        }
        applySupportImageTint();
    }

    private boolean shouldApplyFrameworkTintUsingColorFilter() {
        int i = VERSION.SDK_INT;
        boolean z = false;
        if (i <= 21) {
            return i == 21;
        }
        if (this.mInternalImageTint != null) {
            z = true;
        }
        return z;
    }

    private boolean applyFrameworkTintUsingColorFilter(@NonNull Drawable drawable) {
        if (this.mTmpInfo == null) {
            this.mTmpInfo = new TintInfo();
        }
        TintInfo tintInfo = this.mTmpInfo;
        tintInfo.clear();
        ColorStateList imageTintList = ImageViewCompat.getImageTintList(this.mView);
        if (imageTintList != null) {
            tintInfo.mHasTintList = true;
            tintInfo.mTintList = imageTintList;
        }
        Mode imageTintMode = ImageViewCompat.getImageTintMode(this.mView);
        if (imageTintMode != null) {
            tintInfo.mHasTintMode = true;
            tintInfo.mTintMode = imageTintMode;
        }
        if (!tintInfo.mHasTintList && !tintInfo.mHasTintMode) {
            return false;
        }
        AppCompatDrawableManager.tintDrawable(drawable, tintInfo, this.mView.getDrawableState());
        return true;
    }
}
