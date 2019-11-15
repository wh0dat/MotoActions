package android.support.p004v7.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources.NotFoundException;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.p004v7.appcompat.C0383R;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.widget.TextView;

@RequiresApi(9)
/* renamed from: android.support.v7.widget.AppCompatTextHelper */
class AppCompatTextHelper {
    @NonNull
    private final AppCompatTextViewAutoSizeHelper mAutoSizeTextHelper;
    private TintInfo mDrawableBottomTint;
    private TintInfo mDrawableLeftTint;
    private TintInfo mDrawableRightTint;
    private TintInfo mDrawableTopTint;
    private Typeface mFontTypeface;
    private int mStyle = 0;
    final TextView mView;

    static AppCompatTextHelper create(TextView textView) {
        if (VERSION.SDK_INT >= 17) {
            return new AppCompatTextHelperV17(textView);
        }
        return new AppCompatTextHelper(textView);
    }

    AppCompatTextHelper(TextView textView) {
        this.mView = textView;
        this.mAutoSizeTextHelper = new AppCompatTextViewAutoSizeHelper(this.mView);
    }

    /* access modifiers changed from: 0000 */
    public void loadFromAttributes(AttributeSet attributeSet, int i) {
        ColorStateList colorStateList;
        ColorStateList colorStateList2;
        boolean z;
        boolean z2;
        Context context = this.mView.getContext();
        AppCompatDrawableManager appCompatDrawableManager = AppCompatDrawableManager.get();
        TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, attributeSet, C0383R.styleable.AppCompatTextHelper, i, 0);
        int resourceId = obtainStyledAttributes.getResourceId(C0383R.styleable.AppCompatTextHelper_android_textAppearance, -1);
        if (obtainStyledAttributes.hasValue(C0383R.styleable.AppCompatTextHelper_android_drawableLeft)) {
            this.mDrawableLeftTint = createTintInfo(context, appCompatDrawableManager, obtainStyledAttributes.getResourceId(C0383R.styleable.AppCompatTextHelper_android_drawableLeft, 0));
        }
        if (obtainStyledAttributes.hasValue(C0383R.styleable.AppCompatTextHelper_android_drawableTop)) {
            this.mDrawableTopTint = createTintInfo(context, appCompatDrawableManager, obtainStyledAttributes.getResourceId(C0383R.styleable.AppCompatTextHelper_android_drawableTop, 0));
        }
        if (obtainStyledAttributes.hasValue(C0383R.styleable.AppCompatTextHelper_android_drawableRight)) {
            this.mDrawableRightTint = createTintInfo(context, appCompatDrawableManager, obtainStyledAttributes.getResourceId(C0383R.styleable.AppCompatTextHelper_android_drawableRight, 0));
        }
        if (obtainStyledAttributes.hasValue(C0383R.styleable.AppCompatTextHelper_android_drawableBottom)) {
            this.mDrawableBottomTint = createTintInfo(context, appCompatDrawableManager, obtainStyledAttributes.getResourceId(C0383R.styleable.AppCompatTextHelper_android_drawableBottom, 0));
        }
        obtainStyledAttributes.recycle();
        boolean z3 = this.mView.getTransformationMethod() instanceof PasswordTransformationMethod;
        ColorStateList colorStateList3 = null;
        if (resourceId != -1) {
            TintTypedArray obtainStyledAttributes2 = TintTypedArray.obtainStyledAttributes(context, resourceId, C0383R.styleable.TextAppearance);
            if (z3 || !obtainStyledAttributes2.hasValue(C0383R.styleable.TextAppearance_textAllCaps)) {
                z2 = false;
                z = false;
            } else {
                z = obtainStyledAttributes2.getBoolean(C0383R.styleable.TextAppearance_textAllCaps, false);
                z2 = true;
            }
            updateTypefaceAndStyle(context, obtainStyledAttributes2);
            if (VERSION.SDK_INT < 23) {
                ColorStateList colorStateList4 = obtainStyledAttributes2.hasValue(C0383R.styleable.TextAppearance_android_textColor) ? obtainStyledAttributes2.getColorStateList(C0383R.styleable.TextAppearance_android_textColor) : null;
                colorStateList = obtainStyledAttributes2.hasValue(C0383R.styleable.TextAppearance_android_textColorHint) ? obtainStyledAttributes2.getColorStateList(C0383R.styleable.TextAppearance_android_textColorHint) : null;
                if (obtainStyledAttributes2.hasValue(C0383R.styleable.TextAppearance_android_textColorLink)) {
                    colorStateList3 = obtainStyledAttributes2.getColorStateList(C0383R.styleable.TextAppearance_android_textColorLink);
                }
                ColorStateList colorStateList5 = colorStateList4;
                colorStateList2 = colorStateList3;
                colorStateList3 = colorStateList5;
            } else {
                colorStateList2 = null;
                colorStateList = null;
            }
            obtainStyledAttributes2.recycle();
        } else {
            z2 = false;
            z = false;
            colorStateList2 = null;
            colorStateList = null;
        }
        TintTypedArray obtainStyledAttributes3 = TintTypedArray.obtainStyledAttributes(context, attributeSet, C0383R.styleable.TextAppearance, i, 0);
        if (!z3 && obtainStyledAttributes3.hasValue(C0383R.styleable.TextAppearance_textAllCaps)) {
            z = obtainStyledAttributes3.getBoolean(C0383R.styleable.TextAppearance_textAllCaps, false);
            z2 = true;
        }
        if (VERSION.SDK_INT < 23) {
            if (obtainStyledAttributes3.hasValue(C0383R.styleable.TextAppearance_android_textColor)) {
                colorStateList3 = obtainStyledAttributes3.getColorStateList(C0383R.styleable.TextAppearance_android_textColor);
            }
            if (obtainStyledAttributes3.hasValue(C0383R.styleable.TextAppearance_android_textColorHint)) {
                colorStateList = obtainStyledAttributes3.getColorStateList(C0383R.styleable.TextAppearance_android_textColorHint);
            }
            if (obtainStyledAttributes3.hasValue(C0383R.styleable.TextAppearance_android_textColorLink)) {
                colorStateList2 = obtainStyledAttributes3.getColorStateList(C0383R.styleable.TextAppearance_android_textColorLink);
            }
        }
        updateTypefaceAndStyle(context, obtainStyledAttributes3);
        obtainStyledAttributes3.recycle();
        if (colorStateList3 != null) {
            this.mView.setTextColor(colorStateList3);
        }
        if (colorStateList != null) {
            this.mView.setHintTextColor(colorStateList);
        }
        if (colorStateList2 != null) {
            this.mView.setLinkTextColor(colorStateList2);
        }
        if (!z3 && z2) {
            setAllCaps(z);
        }
        if (this.mFontTypeface != null) {
            this.mView.setTypeface(this.mFontTypeface, this.mStyle);
        }
        this.mAutoSizeTextHelper.loadFromAttributes(attributeSet, i);
        if (VERSION.SDK_INT >= 26 && this.mAutoSizeTextHelper.getAutoSizeTextType() != 0) {
            int[] autoSizeTextAvailableSizes = this.mAutoSizeTextHelper.getAutoSizeTextAvailableSizes();
            if (autoSizeTextAvailableSizes.length <= 0) {
                return;
            }
            if (((float) this.mView.getAutoSizeStepGranularity()) != -1.0f) {
                this.mView.setAutoSizeTextTypeUniformWithConfiguration(this.mAutoSizeTextHelper.getAutoSizeMinTextSize(), this.mAutoSizeTextHelper.getAutoSizeMaxTextSize(), this.mAutoSizeTextHelper.getAutoSizeStepGranularity(), 0);
            } else {
                this.mView.setAutoSizeTextTypeUniformWithPresetSizes(autoSizeTextAvailableSizes, 0);
            }
        }
    }

    private void updateTypefaceAndStyle(Context context, TintTypedArray tintTypedArray) {
        this.mStyle = tintTypedArray.getInt(C0383R.styleable.TextAppearance_android_textStyle, this.mStyle);
        if (tintTypedArray.hasValue(C0383R.styleable.TextAppearance_android_fontFamily) || tintTypedArray.hasValue(C0383R.styleable.TextAppearance_fontFamily)) {
            this.mFontTypeface = null;
            int i = tintTypedArray.hasValue(C0383R.styleable.TextAppearance_android_fontFamily) ? C0383R.styleable.TextAppearance_android_fontFamily : C0383R.styleable.TextAppearance_fontFamily;
            if (!context.isRestricted()) {
                try {
                    this.mFontTypeface = tintTypedArray.getFont(i, this.mStyle, this.mView);
                } catch (NotFoundException | UnsupportedOperationException unused) {
                }
            }
            if (this.mFontTypeface == null) {
                this.mFontTypeface = Typeface.create(tintTypedArray.getString(i), this.mStyle);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void onSetTextAppearance(Context context, int i) {
        TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, i, C0383R.styleable.TextAppearance);
        if (obtainStyledAttributes.hasValue(C0383R.styleable.TextAppearance_textAllCaps)) {
            setAllCaps(obtainStyledAttributes.getBoolean(C0383R.styleable.TextAppearance_textAllCaps, false));
        }
        if (VERSION.SDK_INT < 23 && obtainStyledAttributes.hasValue(C0383R.styleable.TextAppearance_android_textColor)) {
            ColorStateList colorStateList = obtainStyledAttributes.getColorStateList(C0383R.styleable.TextAppearance_android_textColor);
            if (colorStateList != null) {
                this.mView.setTextColor(colorStateList);
            }
        }
        updateTypefaceAndStyle(context, obtainStyledAttributes);
        obtainStyledAttributes.recycle();
        if (this.mFontTypeface != null) {
            this.mView.setTypeface(this.mFontTypeface, this.mStyle);
        }
    }

    /* access modifiers changed from: 0000 */
    public void setAllCaps(boolean z) {
        this.mView.setAllCaps(z);
    }

    /* access modifiers changed from: 0000 */
    public void applyCompoundDrawablesTints() {
        if (this.mDrawableLeftTint != null || this.mDrawableTopTint != null || this.mDrawableRightTint != null || this.mDrawableBottomTint != null) {
            Drawable[] compoundDrawables = this.mView.getCompoundDrawables();
            applyCompoundDrawableTint(compoundDrawables[0], this.mDrawableLeftTint);
            applyCompoundDrawableTint(compoundDrawables[1], this.mDrawableTopTint);
            applyCompoundDrawableTint(compoundDrawables[2], this.mDrawableRightTint);
            applyCompoundDrawableTint(compoundDrawables[3], this.mDrawableBottomTint);
        }
    }

    /* access modifiers changed from: 0000 */
    public final void applyCompoundDrawableTint(Drawable drawable, TintInfo tintInfo) {
        if (drawable != null && tintInfo != null) {
            AppCompatDrawableManager.tintDrawable(drawable, tintInfo, this.mView.getDrawableState());
        }
    }

    protected static TintInfo createTintInfo(Context context, AppCompatDrawableManager appCompatDrawableManager, int i) {
        ColorStateList tintList = appCompatDrawableManager.getTintList(context, i);
        if (tintList == null) {
            return null;
        }
        TintInfo tintInfo = new TintInfo();
        tintInfo.mHasTintList = true;
        tintInfo.mTintList = tintList;
        return tintInfo;
    }

    /* access modifiers changed from: 0000 */
    @RestrictTo({Scope.LIBRARY_GROUP})
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        if (VERSION.SDK_INT < 26) {
            autoSizeText();
        }
    }

    /* access modifiers changed from: 0000 */
    @RestrictTo({Scope.LIBRARY_GROUP})
    public void setTextSize(int i, float f) {
        if (VERSION.SDK_INT < 26 && !isAutoSizeEnabled()) {
            setTextSizeInternal(i, f);
        }
    }

    /* access modifiers changed from: 0000 */
    @RestrictTo({Scope.LIBRARY_GROUP})
    public void autoSizeText() {
        this.mAutoSizeTextHelper.autoSizeText();
    }

    /* access modifiers changed from: 0000 */
    @RestrictTo({Scope.LIBRARY_GROUP})
    public boolean isAutoSizeEnabled() {
        return this.mAutoSizeTextHelper.isAutoSizeEnabled();
    }

    private void setTextSizeInternal(int i, float f) {
        this.mAutoSizeTextHelper.setTextSizeInternal(i, f);
    }

    /* access modifiers changed from: 0000 */
    public void setAutoSizeTextTypeWithDefaults(int i) {
        this.mAutoSizeTextHelper.setAutoSizeTextTypeWithDefaults(i);
    }

    /* access modifiers changed from: 0000 */
    public void setAutoSizeTextTypeUniformWithConfiguration(int i, int i2, int i3, int i4) throws IllegalArgumentException {
        this.mAutoSizeTextHelper.setAutoSizeTextTypeUniformWithConfiguration(i, i2, i3, i4);
    }

    /* access modifiers changed from: 0000 */
    public void setAutoSizeTextTypeUniformWithPresetSizes(@NonNull int[] iArr, int i) throws IllegalArgumentException {
        this.mAutoSizeTextHelper.setAutoSizeTextTypeUniformWithPresetSizes(iArr, i);
    }

    /* access modifiers changed from: 0000 */
    public int getAutoSizeTextType() {
        return this.mAutoSizeTextHelper.getAutoSizeTextType();
    }

    /* access modifiers changed from: 0000 */
    public int getAutoSizeStepGranularity() {
        return this.mAutoSizeTextHelper.getAutoSizeStepGranularity();
    }

    /* access modifiers changed from: 0000 */
    public int getAutoSizeMinTextSize() {
        return this.mAutoSizeTextHelper.getAutoSizeMinTextSize();
    }

    /* access modifiers changed from: 0000 */
    public int getAutoSizeMaxTextSize() {
        return this.mAutoSizeTextHelper.getAutoSizeMaxTextSize();
    }

    /* access modifiers changed from: 0000 */
    public int[] getAutoSizeTextAvailableSizes() {
        return this.mAutoSizeTextHelper.getAutoSizeTextAvailableSizes();
    }
}
