package com.google.android.gms.common.internal;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.PorterDuff.Mode;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.p001v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.widget.Button;
import com.google.android.gms.base.C0496R;
import com.google.android.gms.common.util.DeviceProperties;

public final class SignInButtonImpl extends Button {
    public SignInButtonImpl(Context context) {
        this(context, null);
    }

    public SignInButtonImpl(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, 16842824);
    }

    private static int zza(int i, int i2, int i3, int i4) {
        switch (i) {
            case 0:
                return i2;
            case 1:
                return i3;
            case 2:
                return i4;
            default:
                StringBuilder sb = new StringBuilder(33);
                sb.append("Unknown color scheme: ");
                sb.append(i);
                throw new IllegalStateException(sb.toString());
        }
    }

    public final void configure(Resources resources, int i, int i2) {
        int i3;
        setTypeface(Typeface.DEFAULT_BOLD);
        setTextSize(14.0f);
        int i4 = (int) ((resources.getDisplayMetrics().density * 48.0f) + 0.5f);
        setMinHeight(i4);
        setMinWidth(i4);
        int zza = zza(i2, C0496R.C0497drawable.common_google_signin_btn_icon_dark, C0496R.C0497drawable.common_google_signin_btn_icon_light, C0496R.C0497drawable.common_google_signin_btn_icon_light);
        int zza2 = zza(i2, C0496R.C0497drawable.common_google_signin_btn_text_dark, C0496R.C0497drawable.common_google_signin_btn_text_light, C0496R.C0497drawable.common_google_signin_btn_text_light);
        switch (i) {
            case 0:
            case 1:
                zza = zza2;
                break;
            case 2:
                break;
            default:
                StringBuilder sb = new StringBuilder(32);
                sb.append("Unknown button size: ");
                sb.append(i);
                throw new IllegalStateException(sb.toString());
        }
        Drawable wrap = DrawableCompat.wrap(resources.getDrawable(zza));
        DrawableCompat.setTintList(wrap, resources.getColorStateList(C0496R.color.common_google_signin_btn_tint));
        DrawableCompat.setTintMode(wrap, Mode.SRC_ATOP);
        setBackgroundDrawable(wrap);
        setTextColor((ColorStateList) Preconditions.checkNotNull(resources.getColorStateList(zza(i2, C0496R.color.common_google_signin_btn_text_dark, C0496R.color.common_google_signin_btn_text_light, C0496R.color.common_google_signin_btn_text_light))));
        switch (i) {
            case 0:
                i3 = C0496R.string.common_signin_button_text;
                break;
            case 1:
                i3 = C0496R.string.common_signin_button_text_long;
                break;
            case 2:
                setText(null);
                break;
            default:
                StringBuilder sb2 = new StringBuilder(32);
                sb2.append("Unknown button size: ");
                sb2.append(i);
                throw new IllegalStateException(sb2.toString());
        }
        setText(resources.getString(i3));
        setTransformationMethod(null);
        if (DeviceProperties.isWearable(getContext())) {
            setGravity(19);
        }
    }

    public final void configure(Resources resources, SignInButtonConfig signInButtonConfig) {
        configure(resources, signInButtonConfig.getButtonSize(), signInButtonConfig.getColorScheme());
    }
}
