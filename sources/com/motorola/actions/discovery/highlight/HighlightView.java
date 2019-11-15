package com.motorola.actions.discovery.highlight;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import com.motorola.actions.C0504R;
import com.motorola.actions.utils.MALogger;

public class HighlightView extends View implements OnTouchListener {
    private static final int ANIMATION_IDLE_AFTER_STEP_3 = 1225;
    private static final int ANIMATION_STEP_1_TIME = 1000;
    private static final int ANIMATION_STEP_2_TIME = 1075;
    private static final int ANIMATION_STEP_3_TIME = 1200;
    private static final int ANIMATION_STEP_4_TIME = 1250;
    private static final int ANIMATION_STEP_5_TIME = 2650;
    private static final MALogger LOGGER = new MALogger(HighlightView.class);
    private static final int RIPPLE_ANIMATION_DURATION = 400;
    private static final int WHITE_SPACE_CODE_POINT = 32;
    private final String SAVE_INSTANCE_ANIMATION_POS = "animationTimeReference";
    private final String SAVE_INSTANCE_DISMISS_STATE = "dismissAnimation";
    private final String SAVE_INSTANCE_HEIGHT = "iconHeight";
    private final String SAVE_INSTANCE_WIDTH = "iconWidth";
    private final String SAVE_INSTANCE_X = "iconPosX";
    private final String SAVE_INSTANCE_Y = "iconPosY";
    private final String SAVE_SUPER_STATE = "superState";
    /* access modifiers changed from: private */
    public boolean mAnimationCompleted = false;
    /* access modifiers changed from: private */
    public long mAnimationTimeReference = -1;
    private Runnable mAnimator = new Runnable() {
        public void run() {
            if (HighlightView.this.mAnimationTimeReference != -1) {
                int currentAnimationTimeMillis = (int) (AnimationUtils.currentAnimationTimeMillis() - HighlightView.this.mAnimationTimeReference);
                if (HighlightView.this.mDismissAnimation) {
                    currentAnimationTimeMillis = 1250 - currentAnimationTimeMillis;
                }
                if (currentAnimationTimeMillis < 1000) {
                    if (HighlightView.this.mDismissAnimation) {
                        HighlightView.this.setVisibility(8);
                        return;
                    }
                    HighlightView.this.completeSteps(1);
                } else if (currentAnimationTimeMillis < HighlightView.ANIMATION_STEP_2_TIME) {
                    HighlightView.this.completeSteps(2);
                    float f = ((float) (currentAnimationTimeMillis - 1000)) / 75.0f;
                    HighlightView.this.mDrawableBackground.setAlpha((int) (136.0f * f));
                    int i = (int) (f * 255.0f);
                    HighlightView.this.mIconHighlight.setAlpha(i);
                    HighlightView.this.mDiscoveryIcon.setAlpha(i);
                } else if (currentAnimationTimeMillis < HighlightView.ANIMATION_STEP_3_TIME) {
                    HighlightView.this.completeSteps(3);
                    float f2 = ((float) (currentAnimationTimeMillis - HighlightView.ANIMATION_STEP_2_TIME)) / 125.0f;
                    int access$600 = (int) (((float) HighlightView.this.mBigCircleRadius) * f2);
                    int access$700 = (int) ((((float) (HighlightView.this.mBigCirclePosX - HighlightView.this.mIconPosX)) * f2) + ((float) HighlightView.this.mIconPosX));
                    int access$900 = (int) ((((float) (HighlightView.this.mBigCirclePosY - HighlightView.this.mIconPosY)) * f2) + ((float) HighlightView.this.mIconPosY));
                    HighlightView.this.mBigCircle.setBounds(access$700 - access$600, access$900 - access$600, access$700 + access$600, access$900 + access$600);
                } else if (currentAnimationTimeMillis < HighlightView.ANIMATION_IDLE_AFTER_STEP_3) {
                    HighlightView.this.completeSteps(4);
                } else if (currentAnimationTimeMillis < HighlightView.ANIMATION_STEP_4_TIME) {
                    HighlightView.this.completeSteps(4);
                    int i2 = (int) ((((float) (currentAnimationTimeMillis - HighlightView.ANIMATION_STEP_3_TIME)) / 50.0f) * 255.0f);
                    HighlightView.this.mTitlePaint.setAlpha(i2);
                    HighlightView.this.mDescriptionPaint.setAlpha(i2);
                } else {
                    HighlightView.this.completeSteps(5);
                    HighlightView.this.mAnimationCompleted = true;
                    int i3 = (currentAnimationTimeMillis - HighlightView.ANIMATION_STEP_4_TIME) % 1400;
                    if (i3 < HighlightView.RIPPLE_ANIMATION_DURATION) {
                        double d = (double) i3;
                        double d2 = (double) HighlightView.RIPPLE_ANIMATION_DURATION;
                        int access$800 = HighlightView.this.mIconPosX + (HighlightView.this.mIconWidth / 2);
                        int access$1000 = HighlightView.this.mIconPosY + (HighlightView.this.mIconHeight / 2);
                        int access$1700 = HighlightView.this.mHighlightCircleRadios + ((int) (((float) (HighlightView.this.mRippleEffectRadius - HighlightView.this.mHighlightCircleRadios)) * ((float) (d / (d2 / 2.0d)))));
                        if (i3 < 133) {
                            HighlightView.this.mRippleEffect.setAlpha((int) (((float) (d / (d2 / 3.0d))) * 255.0f));
                        } else {
                            HighlightView.this.mRippleEffect.setAlpha((int) ((1.0f - ((float) ((d - (d2 / 3.0d)) / ((d2 * 2.0d) / 3.0d)))) * 255.0f));
                        }
                        HighlightView.this.mRippleEffect.setBounds(access$800 - access$1700, access$1000 - access$1700, access$800 + access$1700, access$1000 + access$1700);
                    } else {
                        HighlightView.this.mRippleEffect.setAlpha(0);
                    }
                }
            } else {
                HighlightView.this.mAnimationTimeReference = AnimationUtils.currentAnimationTimeMillis();
            }
            HighlightView.this.postDelayed(this, 15);
            HighlightView.this.invalidate();
        }
    };
    /* access modifiers changed from: private */
    public ShapeDrawable mBigCircle;
    /* access modifiers changed from: private */
    public int mBigCirclePosX = 0;
    /* access modifiers changed from: private */
    public int mBigCirclePosY = 0;
    /* access modifiers changed from: private */
    public int mBigCircleRadius = 0;
    @Nullable
    private OnClickListener mButtonClickListener;
    private int mDescriptionFontSize = 0;
    /* access modifiers changed from: private */
    public Paint mDescriptionPaint = new Paint();
    private int mDescriptionX = 0;
    private int mDescriptionY = 0;
    private String mDiscoveryDescription;
    /* access modifiers changed from: private */
    public Drawable mDiscoveryIcon;
    private String mDiscoveryTitle;
    /* access modifiers changed from: private */
    public boolean mDismissAnimation = false;
    private int mDistBetweenLines = 0;
    /* access modifiers changed from: private */
    public ShapeDrawable mDrawableBackground;
    /* access modifiers changed from: private */
    public int mHighlightCircleRadios = 0;
    /* access modifiers changed from: private */
    public int mIconHeight = 900;
    /* access modifiers changed from: private */
    public ShapeDrawable mIconHighlight;
    private Rect mIconHightlightBounds;
    /* access modifiers changed from: private */
    public int mIconPosX = 20;
    /* access modifiers changed from: private */
    public int mIconPosY = 900;
    /* access modifiers changed from: private */
    public int mIconWidth = 10;
    private boolean mIsTouching = false;
    private int mParentHeight = -1;
    private int mParentWidth = -1;
    /* access modifiers changed from: private */
    public ShapeDrawable mRippleEffect;
    /* access modifiers changed from: private */
    public int mRippleEffectRadius;
    private boolean mScreenMeasured;
    private int mTextWidth = 0;
    private int mTitleFontSize = 0;
    /* access modifiers changed from: private */
    public Paint mTitlePaint = new Paint();
    private int mTitleX = 0;
    private int mTitleY = 0;

    public void dismiss() {
        if (this.mAnimationCompleted) {
            setOnClickListener(null);
            setOnButtonClickListener(null);
            this.mDismissAnimation = true;
            this.mAnimationTimeReference = -1;
        }
    }

    /* access modifiers changed from: private */
    public void completeSteps(int i) {
        if (i > 2) {
            this.mDrawableBackground.setAlpha(138);
            this.mIconHighlight.setAlpha(255);
            this.mDiscoveryIcon.setAlpha(255);
        } else {
            this.mDrawableBackground.setAlpha(0);
            this.mIconHighlight.setAlpha(0);
            this.mDiscoveryIcon.setAlpha(0);
        }
        if (i > 3) {
            this.mBigCircle.setBounds(this.mBigCirclePosX - this.mBigCircleRadius, this.mBigCirclePosY - this.mBigCircleRadius, this.mBigCirclePosX + this.mBigCircleRadius, this.mBigCirclePosY + this.mBigCircleRadius);
        } else {
            this.mBigCircle.setBounds(0, 0, 0, 0);
        }
        if (i > 4) {
            this.mTitlePaint.setAlpha(255);
            this.mDescriptionPaint.setAlpha(255);
            return;
        }
        this.mTitlePaint.setAlpha(0);
        this.mDescriptionPaint.setAlpha(0);
        this.mRippleEffect.setAlpha(0);
    }

    /* JADX INFO: finally extract failed */
    public HighlightView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, C0504R.styleable.HighlightView, 0, 0);
        try {
            this.mIconPosX = obtainStyledAttributes.getInteger(3, 0);
            this.mIconPosY = obtainStyledAttributes.getInteger(4, 0);
            this.mIconHeight = obtainStyledAttributes.getInteger(2, 0);
            this.mIconWidth = obtainStyledAttributes.getInteger(6, 0);
            this.mDiscoveryTitle = obtainStyledAttributes.getString(1);
            this.mDiscoveryDescription = obtainStyledAttributes.getString(0);
            this.mDiscoveryIcon = obtainStyledAttributes.getDrawable(5);
            if (this.mDiscoveryIcon != null) {
                this.mDiscoveryIcon = this.mDiscoveryIcon.mutate();
                this.mDiscoveryIcon.setColorFilter(getResources().getColor(C0504R.color.moto_actions_primary_color, null), Mode.MULTIPLY);
                this.mDiscoveryIcon.setAlpha(0);
            }
            obtainStyledAttributes.recycle();
            this.mBigCircle = new ShapeDrawable(new OvalShape());
            this.mBigCircle.getPaint().setColor(getResources().getColor(C0504R.color.discovery_big_circle_color, null));
            this.mBigCircle.setAlpha(245);
            this.mBigCircle.setBounds(0, 0, 0, 0);
            this.mIconHighlight = new ShapeDrawable(new OvalShape());
            this.mIconHighlight.getPaint().setColor(getResources().getColor(C0504R.color.discovery_icon_highlight, null));
            this.mIconHighlight.setAlpha(0);
            this.mRippleEffect = new ShapeDrawable(new OvalShape());
            this.mRippleEffect.getPaint().setColor(getResources().getColor(C0504R.color.discovery_icon_highlight, null));
            this.mRippleEffect.setAlpha(0);
            this.mDrawableBackground = new ShapeDrawable(new RectShape());
            this.mDrawableBackground.getPaint().setColor(getResources().getColor(C0504R.color.discovery_background, null));
            this.mDrawableBackground.setAlpha(0);
            this.mHighlightCircleRadios = getResources().getDimensionPixelSize(C0504R.dimen.highlight_circle_radius);
            this.mRippleEffectRadius = getResources().getDimensionPixelSize(C0504R.dimen.ripple_effect_radius);
            this.mTitleFontSize = getResources().getDimensionPixelSize(C0504R.dimen.discovery_title_text_size);
            this.mTitlePaint.setColor(-1);
            this.mTitlePaint.setTextSize((float) this.mTitleFontSize);
            this.mTitlePaint.setTypeface(Typeface.create(null, 1));
            this.mDescriptionPaint.setColor(getContext().getColor(C0504R.color.discovery_text_description));
            this.mDescriptionFontSize = getResources().getDimensionPixelSize(C0504R.dimen.discovery_description_text_size);
            this.mDistBetweenLines = getResources().getDimensionPixelSize(C0504R.dimen.discovery_description_dist_between_lines);
            this.mDescriptionPaint.setTextSize((float) this.mDescriptionFontSize);
            updateLayoutSize();
            this.mTitlePaint.setAlpha(0);
            this.mTitlePaint.setAntiAlias(true);
            this.mDescriptionPaint.setAlpha(0);
            this.mDescriptionPaint.setAntiAlias(true);
            this.mAnimator.run();
        } catch (Throwable th) {
            obtainStyledAttributes.recycle();
            throw th;
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        if (this.mScreenMeasured) {
            this.mDrawableBackground.draw(canvas);
            this.mBigCircle.draw(canvas);
            this.mRippleEffect.draw(canvas);
            this.mIconHighlight.draw(canvas);
            this.mDiscoveryIcon.draw(canvas);
            canvas.drawText(this.mDiscoveryTitle, (float) this.mTitleX, (float) this.mTitleY, this.mTitlePaint);
            drawText(canvas, this.mDiscoveryDescription, this.mDescriptionX, this.mDescriptionY, this.mTextWidth, this.mDescriptionFontSize, this.mDescriptionPaint);
        }
    }

    private void drawText(Canvas canvas, String str, int i, int i2, int i3, int i4, Paint paint) {
        Paint paint2;
        boolean z;
        String str2 = str;
        int i5 = i;
        int length = str.length();
        boolean z2 = str2.indexOf(32) >= 0;
        int i6 = i2;
        int i7 = 0;
        while (i7 < length) {
            int i8 = -1;
            int i9 = i7;
            while (true) {
                if (i9 >= length) {
                    int i10 = i3;
                    paint2 = paint;
                    z = false;
                    break;
                }
                if (!z2 || str2.codePointAt(i9) == 32 || i9 == length - 1) {
                    paint2 = paint;
                    if (paint2.measureText(str2, i7, i9) > ((float) i3)) {
                        z = true;
                        break;
                    }
                    i8 = i9;
                } else {
                    int i11 = i3;
                    Paint paint3 = paint;
                }
                i9++;
            }
            if (!z) {
                i8 = length - 1;
            }
            if (i8 >= 0) {
                int i12 = i8 + 1;
                canvas.drawText(str2, i7, i12, (float) i5, (float) i6, paint2);
                i6 += i4 + this.mDistBetweenLines;
                i7 = i12;
            } else {
                canvas.drawText(str2, i7, length, (float) i5, (float) i6, paint2);
                return;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int size = MeasureSpec.getSize(i);
        int size2 = MeasureSpec.getSize(i2);
        if (!(this.mParentWidth == size && this.mParentHeight == size2)) {
            this.mParentWidth = size;
            this.mParentHeight = size2;
            updateLayoutSize();
        }
        this.mScreenMeasured = true;
        super.onMeasure(i, i2);
    }

    private void updateLayoutSize() {
        int i;
        this.mDrawableBackground.setBounds(0, 0, this.mParentWidth, this.mParentHeight);
        this.mBigCircleRadius = getResources().getDimensionPixelSize(C0504R.dimen.big_circle_radius);
        int dimensionPixelSize = getResources().getDimensionPixelSize(C0504R.dimen.discovery_title_padding_top);
        int dimensionPixelSize2 = getResources().getDimensionPixelSize(C0504R.dimen.discovery_title_padding_right);
        int dimensionPixelSize3 = getResources().getDimensionPixelSize(C0504R.dimen.discovery_title_padding_left);
        int dimensionPixelSize4 = getResources().getDimensionPixelSize(C0504R.dimen.discovery_dist_title_description);
        int i2 = this.mIconPosX + (this.mIconWidth / 2);
        int i3 = this.mIconPosY + (this.mIconHeight / 2);
        this.mBigCirclePosX = i2;
        this.mBigCirclePosY = i3;
        if (this.mBigCirclePosX + this.mBigCircleRadius <= this.mParentWidth) {
            i = dimensionPixelSize3;
        } else {
            i = Math.max((this.mBigCirclePosX - this.mBigCircleRadius) + dimensionPixelSize3, dimensionPixelSize3);
        }
        this.mTitleX = i;
        this.mDescriptionX = this.mTitleX;
        this.mTextWidth = (Math.min(this.mBigCircleRadius, this.mParentWidth) - dimensionPixelSize3) - dimensionPixelSize2;
        int i4 = dimensionPixelSize + dimensionPixelSize4 + this.mTitleFontSize + this.mDescriptionFontSize;
        this.mTitleY = this.mBigCirclePosY - i4 >= 0 ? this.mBigCirclePosY - i4 : dimensionPixelSize + this.mBigCirclePosY;
        this.mTitleY -= this.mHighlightCircleRadios;
        this.mDescriptionY = this.mTitleY + dimensionPixelSize4 + this.mDescriptionFontSize;
        this.mIconHighlight.setBounds(i2 - this.mHighlightCircleRadios, i3 - this.mHighlightCircleRadios, i2 + this.mHighlightCircleRadios, i3 + this.mHighlightCircleRadios);
        this.mIconHightlightBounds = this.mIconHighlight.getBounds();
        this.mDiscoveryIcon.setBounds(this.mIconPosX, this.mIconPosY, this.mIconPosX + this.mIconWidth, this.mIconPosY + this.mIconHeight);
    }

    public void setIconDimen(int i, int i2, int i3, int i4) {
        this.mIconPosX = i;
        this.mIconPosY = i2;
        this.mIconWidth = i3;
        this.mIconHeight = i4;
        updateLayoutSize();
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append(" ");
        sb.append(i);
        sb.append(",y ");
        sb.append(i2);
        sb.append(",w ");
        sb.append(i3);
        sb.append(",h ");
        sb.append(i4);
        mALogger.mo11957d(sb.toString());
    }

    public Parcelable onSaveInstanceState() {
        Parcelable onSaveInstanceState = super.onSaveInstanceState();
        Bundle bundle = new Bundle();
        bundle.putParcelable("superState", onSaveInstanceState);
        bundle.putInt("iconPosX", this.mIconPosX);
        bundle.putInt("iconPosY", this.mIconPosY);
        bundle.putInt("iconWidth", this.mIconWidth);
        bundle.putInt("iconHeight", this.mIconHeight);
        bundle.putLong("animationTimeReference", this.mAnimationTimeReference);
        bundle.putBoolean("dismissAnimation", this.mDismissAnimation);
        return bundle;
    }

    public void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof Bundle) {
            Bundle bundle = (Bundle) parcelable;
            this.mIconPosX = bundle.getInt("iconPosX");
            this.mIconPosY = bundle.getInt("iconPosY");
            this.mIconWidth = bundle.getInt("iconWidth");
            this.mIconHeight = bundle.getInt("iconHeight");
            this.mAnimationTimeReference = bundle.getLong("animationTimeReference");
            this.mDismissAnimation = bundle.getBoolean("dismissAnimation");
            parcelable = bundle.getParcelable("superState");
        }
        super.onRestoreInstanceState(parcelable);
    }

    public void setVisibility(int i) {
        super.setVisibility(i);
        if (i == 8 || (i == 4 && !this.mDismissAnimation)) {
            dismiss();
        }
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (this.mButtonClickListener == null) {
            return false;
        }
        boolean z = this.mIconHightlightBounds != null && this.mIconHightlightBounds.contains((int) motionEvent.getRawX(), (int) motionEvent.getRawY());
        switch (motionEvent.getAction()) {
            case 0:
                if (z) {
                    this.mIsTouching = true;
                    return true;
                }
                break;
            case 1:
                if (this.mIsTouching) {
                    this.mIsTouching = false;
                    if (z) {
                        this.mButtonClickListener.onClick(view);
                        return true;
                    }
                }
                break;
        }
        return false;
    }

    public void setOnButtonClickListener(OnClickListener onClickListener) {
        this.mButtonClickListener = onClickListener;
        setOnTouchListener(onClickListener == null ? null : this);
    }
}
