package android.support.p004v7.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.p001v4.view.GravityCompat;
import android.support.p001v4.view.ViewCompat;
import android.support.p004v7.appcompat.C0383R;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import com.google.android.gms.common.util.CrashUtils.ErrorDialogData;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* renamed from: android.support.v7.widget.LinearLayoutCompat */
public class LinearLayoutCompat extends ViewGroup {
    public static final int HORIZONTAL = 0;
    private static final int INDEX_BOTTOM = 2;
    private static final int INDEX_CENTER_VERTICAL = 0;
    private static final int INDEX_FILL = 3;
    private static final int INDEX_TOP = 1;
    public static final int SHOW_DIVIDER_BEGINNING = 1;
    public static final int SHOW_DIVIDER_END = 4;
    public static final int SHOW_DIVIDER_MIDDLE = 2;
    public static final int SHOW_DIVIDER_NONE = 0;
    public static final int VERTICAL = 1;
    private static final int VERTICAL_GRAVITY_COUNT = 4;
    private boolean mBaselineAligned;
    private int mBaselineAlignedChildIndex;
    private int mBaselineChildTop;
    private Drawable mDivider;
    private int mDividerHeight;
    private int mDividerPadding;
    private int mDividerWidth;
    private int mGravity;
    private int[] mMaxAscent;
    private int[] mMaxDescent;
    private int mOrientation;
    private int mShowDividers;
    private int mTotalLength;
    private boolean mUseLargestChild;
    private float mWeightSum;

    @RestrictTo({Scope.LIBRARY_GROUP})
    @Retention(RetentionPolicy.SOURCE)
    /* renamed from: android.support.v7.widget.LinearLayoutCompat$DividerMode */
    public @interface DividerMode {
    }

    /* renamed from: android.support.v7.widget.LinearLayoutCompat$LayoutParams */
    public static class LayoutParams extends MarginLayoutParams {
        public int gravity;
        public float weight;

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.gravity = -1;
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C0383R.styleable.LinearLayoutCompat_Layout);
            this.weight = obtainStyledAttributes.getFloat(C0383R.styleable.LinearLayoutCompat_Layout_android_layout_weight, 0.0f);
            this.gravity = obtainStyledAttributes.getInt(C0383R.styleable.LinearLayoutCompat_Layout_android_layout_gravity, -1);
            obtainStyledAttributes.recycle();
        }

        public LayoutParams(int i, int i2) {
            super(i, i2);
            this.gravity = -1;
            this.weight = 0.0f;
        }

        public LayoutParams(int i, int i2, float f) {
            super(i, i2);
            this.gravity = -1;
            this.weight = f;
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
            this.gravity = -1;
        }

        public LayoutParams(MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
            this.gravity = -1;
        }

        public LayoutParams(LayoutParams layoutParams) {
            super(layoutParams);
            this.gravity = -1;
            this.weight = layoutParams.weight;
            this.gravity = layoutParams.gravity;
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    @Retention(RetentionPolicy.SOURCE)
    /* renamed from: android.support.v7.widget.LinearLayoutCompat$OrientationMode */
    public @interface OrientationMode {
    }

    /* access modifiers changed from: 0000 */
    public int getChildrenSkipCount(View view, int i) {
        return 0;
    }

    /* access modifiers changed from: 0000 */
    public int getLocationOffset(View view) {
        return 0;
    }

    /* access modifiers changed from: 0000 */
    public int getNextLocationOffset(View view) {
        return 0;
    }

    /* access modifiers changed from: 0000 */
    public int measureNullChild(int i) {
        return 0;
    }

    public boolean shouldDelayChildPressedState() {
        return false;
    }

    public LinearLayoutCompat(Context context) {
        this(context, null);
    }

    public LinearLayoutCompat(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public LinearLayoutCompat(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mBaselineAligned = true;
        this.mBaselineAlignedChildIndex = -1;
        this.mBaselineChildTop = 0;
        this.mGravity = 8388659;
        TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, attributeSet, C0383R.styleable.LinearLayoutCompat, i, 0);
        int i2 = obtainStyledAttributes.getInt(C0383R.styleable.LinearLayoutCompat_android_orientation, -1);
        if (i2 >= 0) {
            setOrientation(i2);
        }
        int i3 = obtainStyledAttributes.getInt(C0383R.styleable.LinearLayoutCompat_android_gravity, -1);
        if (i3 >= 0) {
            setGravity(i3);
        }
        boolean z = obtainStyledAttributes.getBoolean(C0383R.styleable.LinearLayoutCompat_android_baselineAligned, true);
        if (!z) {
            setBaselineAligned(z);
        }
        this.mWeightSum = obtainStyledAttributes.getFloat(C0383R.styleable.LinearLayoutCompat_android_weightSum, -1.0f);
        this.mBaselineAlignedChildIndex = obtainStyledAttributes.getInt(C0383R.styleable.LinearLayoutCompat_android_baselineAlignedChildIndex, -1);
        this.mUseLargestChild = obtainStyledAttributes.getBoolean(C0383R.styleable.LinearLayoutCompat_measureWithLargestChild, false);
        setDividerDrawable(obtainStyledAttributes.getDrawable(C0383R.styleable.LinearLayoutCompat_divider));
        this.mShowDividers = obtainStyledAttributes.getInt(C0383R.styleable.LinearLayoutCompat_showDividers, 0);
        this.mDividerPadding = obtainStyledAttributes.getDimensionPixelSize(C0383R.styleable.LinearLayoutCompat_dividerPadding, 0);
        obtainStyledAttributes.recycle();
    }

    public void setShowDividers(int i) {
        if (i != this.mShowDividers) {
            requestLayout();
        }
        this.mShowDividers = i;
    }

    public int getShowDividers() {
        return this.mShowDividers;
    }

    public Drawable getDividerDrawable() {
        return this.mDivider;
    }

    public void setDividerDrawable(Drawable drawable) {
        if (drawable != this.mDivider) {
            this.mDivider = drawable;
            boolean z = false;
            if (drawable != null) {
                this.mDividerWidth = drawable.getIntrinsicWidth();
                this.mDividerHeight = drawable.getIntrinsicHeight();
            } else {
                this.mDividerWidth = 0;
                this.mDividerHeight = 0;
            }
            if (drawable == null) {
                z = true;
            }
            setWillNotDraw(z);
            requestLayout();
        }
    }

    public void setDividerPadding(int i) {
        this.mDividerPadding = i;
    }

    public int getDividerPadding() {
        return this.mDividerPadding;
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public int getDividerWidth() {
        return this.mDividerWidth;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        if (this.mDivider != null) {
            if (this.mOrientation == 1) {
                drawDividersVertical(canvas);
            } else {
                drawDividersHorizontal(canvas);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void drawDividersVertical(Canvas canvas) {
        int i;
        int virtualChildCount = getVirtualChildCount();
        for (int i2 = 0; i2 < virtualChildCount; i2++) {
            View virtualChildAt = getVirtualChildAt(i2);
            if (!(virtualChildAt == null || virtualChildAt.getVisibility() == 8 || !hasDividerBeforeChildAt(i2))) {
                drawHorizontalDivider(canvas, (virtualChildAt.getTop() - ((LayoutParams) virtualChildAt.getLayoutParams()).topMargin) - this.mDividerHeight);
            }
        }
        if (hasDividerBeforeChildAt(virtualChildCount)) {
            View virtualChildAt2 = getVirtualChildAt(virtualChildCount - 1);
            if (virtualChildAt2 == null) {
                i = (getHeight() - getPaddingBottom()) - this.mDividerHeight;
            } else {
                i = virtualChildAt2.getBottom() + ((LayoutParams) virtualChildAt2.getLayoutParams()).bottomMargin;
            }
            drawHorizontalDivider(canvas, i);
        }
    }

    /* access modifiers changed from: 0000 */
    public void drawDividersHorizontal(Canvas canvas) {
        int i;
        int i2;
        int virtualChildCount = getVirtualChildCount();
        boolean isLayoutRtl = ViewUtils.isLayoutRtl(this);
        for (int i3 = 0; i3 < virtualChildCount; i3++) {
            View virtualChildAt = getVirtualChildAt(i3);
            if (!(virtualChildAt == null || virtualChildAt.getVisibility() == 8 || !hasDividerBeforeChildAt(i3))) {
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                if (isLayoutRtl) {
                    i2 = virtualChildAt.getRight() + layoutParams.rightMargin;
                } else {
                    i2 = (virtualChildAt.getLeft() - layoutParams.leftMargin) - this.mDividerWidth;
                }
                drawVerticalDivider(canvas, i2);
            }
        }
        if (hasDividerBeforeChildAt(virtualChildCount)) {
            View virtualChildAt2 = getVirtualChildAt(virtualChildCount - 1);
            if (virtualChildAt2 != null) {
                LayoutParams layoutParams2 = (LayoutParams) virtualChildAt2.getLayoutParams();
                if (isLayoutRtl) {
                    i = (virtualChildAt2.getLeft() - layoutParams2.leftMargin) - this.mDividerWidth;
                } else {
                    i = virtualChildAt2.getRight() + layoutParams2.rightMargin;
                }
            } else if (isLayoutRtl) {
                i = getPaddingLeft();
            } else {
                i = (getWidth() - getPaddingRight()) - this.mDividerWidth;
            }
            drawVerticalDivider(canvas, i);
        }
    }

    /* access modifiers changed from: 0000 */
    public void drawHorizontalDivider(Canvas canvas, int i) {
        this.mDivider.setBounds(getPaddingLeft() + this.mDividerPadding, i, (getWidth() - getPaddingRight()) - this.mDividerPadding, this.mDividerHeight + i);
        this.mDivider.draw(canvas);
    }

    /* access modifiers changed from: 0000 */
    public void drawVerticalDivider(Canvas canvas, int i) {
        this.mDivider.setBounds(i, getPaddingTop() + this.mDividerPadding, this.mDividerWidth + i, (getHeight() - getPaddingBottom()) - this.mDividerPadding);
        this.mDivider.draw(canvas);
    }

    public boolean isBaselineAligned() {
        return this.mBaselineAligned;
    }

    public void setBaselineAligned(boolean z) {
        this.mBaselineAligned = z;
    }

    public boolean isMeasureWithLargestChildEnabled() {
        return this.mUseLargestChild;
    }

    public void setMeasureWithLargestChildEnabled(boolean z) {
        this.mUseLargestChild = z;
    }

    public int getBaseline() {
        if (this.mBaselineAlignedChildIndex < 0) {
            return super.getBaseline();
        }
        if (getChildCount() <= this.mBaselineAlignedChildIndex) {
            throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout set to an index that is out of bounds.");
        }
        View childAt = getChildAt(this.mBaselineAlignedChildIndex);
        int baseline = childAt.getBaseline();
        if (baseline != -1) {
            int i = this.mBaselineChildTop;
            if (this.mOrientation == 1) {
                int i2 = this.mGravity & 112;
                if (i2 != 48) {
                    if (i2 == 16) {
                        i += ((((getBottom() - getTop()) - getPaddingTop()) - getPaddingBottom()) - this.mTotalLength) / 2;
                    } else if (i2 == 80) {
                        i = ((getBottom() - getTop()) - getPaddingBottom()) - this.mTotalLength;
                    }
                }
            }
            return i + ((LayoutParams) childAt.getLayoutParams()).topMargin + baseline;
        } else if (this.mBaselineAlignedChildIndex == 0) {
            return -1;
        } else {
            throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout points to a View that doesn't know how to get its baseline.");
        }
    }

    public int getBaselineAlignedChildIndex() {
        return this.mBaselineAlignedChildIndex;
    }

    public void setBaselineAlignedChildIndex(int i) {
        if (i < 0 || i >= getChildCount()) {
            StringBuilder sb = new StringBuilder();
            sb.append("base aligned child index out of range (0, ");
            sb.append(getChildCount());
            sb.append(")");
            throw new IllegalArgumentException(sb.toString());
        }
        this.mBaselineAlignedChildIndex = i;
    }

    /* access modifiers changed from: 0000 */
    public View getVirtualChildAt(int i) {
        return getChildAt(i);
    }

    /* access modifiers changed from: 0000 */
    public int getVirtualChildCount() {
        return getChildCount();
    }

    public float getWeightSum() {
        return this.mWeightSum;
    }

    public void setWeightSum(float f) {
        this.mWeightSum = Math.max(0.0f, f);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        if (this.mOrientation == 1) {
            measureVertical(i, i2);
        } else {
            measureHorizontal(i, i2);
        }
    }

    /* access modifiers changed from: protected */
    public boolean hasDividerBeforeChildAt(int i) {
        boolean z = false;
        if (i == 0) {
            if ((this.mShowDividers & 1) != 0) {
                z = true;
            }
            return z;
        } else if (i == getChildCount()) {
            if ((this.mShowDividers & 4) != 0) {
                z = true;
            }
            return z;
        } else if ((this.mShowDividers & 2) == 0) {
            return false;
        } else {
            int i2 = i - 1;
            while (true) {
                if (i2 < 0) {
                    break;
                } else if (getChildAt(i2).getVisibility() != 8) {
                    z = true;
                    break;
                } else {
                    i2--;
                }
            }
            return z;
        }
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:143:0x0337  */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x016f  */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x017b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void measureVertical(int r40, int r41) {
        /*
            r39 = this;
            r7 = r39
            r8 = r40
            r9 = r41
            r10 = 0
            r7.mTotalLength = r10
            int r11 = r39.getVirtualChildCount()
            int r12 = android.view.View.MeasureSpec.getMode(r40)
            int r13 = android.view.View.MeasureSpec.getMode(r41)
            int r14 = r7.mBaselineAlignedChildIndex
            boolean r15 = r7.mUseLargestChild
            r16 = 0
            r17 = 1
            r1 = r10
            r3 = r1
            r5 = r3
            r18 = r5
            r20 = r18
            r21 = r20
            r0 = r16
            r19 = r17
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
        L_0x002c:
            if (r5 >= r11) goto L_0x01a6
            android.view.View r4 = r7.getVirtualChildAt(r5)
            if (r4 != 0) goto L_0x0047
            int r4 = r7.mTotalLength
            int r22 = r7.measureNullChild(r5)
            int r4 = r4 + r22
            r7.mTotalLength = r4
            r4 = r10
            r31 = r11
            r28 = r13
        L_0x0043:
            r10 = -2147483648(0xffffffff80000000, float:-0.0)
            goto L_0x0199
        L_0x0047:
            int r6 = r4.getVisibility()
            r25 = r1
            r1 = 8
            if (r6 != r1) goto L_0x005e
            int r1 = r7.getChildrenSkipCount(r4, r5)
            int r5 = r5 + r1
            r4 = r10
            r31 = r11
            r28 = r13
            r1 = r25
            goto L_0x0043
        L_0x005e:
            boolean r1 = r7.hasDividerBeforeChildAt(r5)
            if (r1 == 0) goto L_0x006b
            int r1 = r7.mTotalLength
            int r6 = r7.mDividerHeight
            int r1 = r1 + r6
            r7.mTotalLength = r1
        L_0x006b:
            android.view.ViewGroup$LayoutParams r1 = r4.getLayoutParams()
            r6 = r1
            android.support.v7.widget.LinearLayoutCompat$LayoutParams r6 = (android.support.p004v7.widget.LinearLayoutCompat.LayoutParams) r6
            float r1 = r6.weight
            float r23 = r0 + r1
            r1 = 1073741824(0x40000000, float:2.0)
            if (r13 != r1) goto L_0x00a9
            int r0 = r6.height
            if (r0 != 0) goto L_0x00a9
            float r0 = r6.weight
            int r0 = (r0 > r16 ? 1 : (r0 == r16 ? 0 : -1))
            if (r0 <= 0) goto L_0x00a9
            int r0 = r7.mTotalLength
            int r1 = r6.topMargin
            int r1 = r1 + r0
            r26 = r2
            int r2 = r6.bottomMargin
            int r1 = r1 + r2
            int r0 = java.lang.Math.max(r0, r1)
            r7.mTotalLength = r0
            r33 = r3
            r8 = r4
            r9 = r6
            r34 = r10
            r31 = r11
            r28 = r13
            r18 = r17
            r32 = r21
            r13 = r25
            r10 = -2147483648(0xffffffff80000000, float:-0.0)
            r11 = r5
            goto L_0x011d
        L_0x00a9:
            r26 = r2
            int r0 = r6.height
            if (r0 != 0) goto L_0x00ba
            float r0 = r6.weight
            int r0 = (r0 > r16 ? 1 : (r0 == r16 ? 0 : -1))
            if (r0 <= 0) goto L_0x00ba
            r0 = -2
            r6.height = r0
            r2 = 0
            goto L_0x00bc
        L_0x00ba:
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
        L_0x00bc:
            r24 = 0
            int r0 = (r23 > r16 ? 1 : (r23 == r16 ? 0 : -1))
            if (r0 != 0) goto L_0x00c7
            int r0 = r7.mTotalLength
            r27 = r0
            goto L_0x00c9
        L_0x00c7:
            r27 = 0
        L_0x00c9:
            r0 = r7
            r28 = r13
            r13 = r25
            r25 = 1073741824(0x40000000, float:2.0)
            r1 = r4
            r29 = r2
            r30 = r26
            r2 = r5
            r31 = r11
            r11 = r3
            r3 = r8
            r8 = r4
            r33 = r11
            r32 = r21
            r11 = r25
            r4 = r24
            r11 = r5
            r5 = r9
            r9 = r6
            r34 = r10
            r10 = -2147483648(0xffffffff80000000, float:-0.0)
            r6 = r27
            r0.measureChildBeforeLayout(r1, r2, r3, r4, r5, r6)
            r0 = r29
            if (r0 == r10) goto L_0x00f5
            r9.height = r0
        L_0x00f5:
            int r0 = r8.getMeasuredHeight()
            int r1 = r7.mTotalLength
            int r2 = r1 + r0
            int r3 = r9.topMargin
            int r2 = r2 + r3
            int r3 = r9.bottomMargin
            int r2 = r2 + r3
            int r3 = r7.getNextLocationOffset(r8)
            int r2 = r2 + r3
            int r1 = java.lang.Math.max(r1, r2)
            r7.mTotalLength = r1
            if (r15 == 0) goto L_0x0119
            r6 = r30
            int r2 = java.lang.Math.max(r0, r6)
            r26 = r2
            goto L_0x011d
        L_0x0119:
            r6 = r30
            r26 = r6
        L_0x011d:
            if (r14 < 0) goto L_0x0127
            int r5 = r11 + 1
            if (r14 != r5) goto L_0x0127
            int r0 = r7.mTotalLength
            r7.mBaselineChildTop = r0
        L_0x0127:
            if (r11 >= r14) goto L_0x0137
            float r0 = r9.weight
            int r0 = (r0 > r16 ? 1 : (r0 == r16 ? 0 : -1))
            if (r0 <= 0) goto L_0x0137
            java.lang.RuntimeException r0 = new java.lang.RuntimeException
            java.lang.String r1 = "A child of LinearLayout with index less than mBaselineAlignedChildIndex has weight > 0, which won't work.  Either remove the weight, or don't set mBaselineAlignedChildIndex."
            r0.<init>(r1)
            throw r0
        L_0x0137:
            r0 = 1073741824(0x40000000, float:2.0)
            if (r12 == r0) goto L_0x0145
            int r0 = r9.width
            r1 = -1
            if (r0 != r1) goto L_0x0146
            r0 = r17
            r20 = r0
            goto L_0x0147
        L_0x0145:
            r1 = -1
        L_0x0146:
            r0 = 0
        L_0x0147:
            int r2 = r9.leftMargin
            int r3 = r9.rightMargin
            int r2 = r2 + r3
            int r3 = r8.getMeasuredWidth()
            int r3 = r3 + r2
            int r4 = java.lang.Math.max(r13, r3)
            int r5 = r8.getMeasuredState()
            r6 = r34
            int r5 = android.view.View.combineMeasuredStates(r6, r5)
            if (r19 == 0) goto L_0x0168
            int r6 = r9.width
            if (r6 != r1) goto L_0x0168
            r1 = r17
            goto L_0x0169
        L_0x0168:
            r1 = 0
        L_0x0169:
            float r6 = r9.weight
            int r6 = (r6 > r16 ? 1 : (r6 == r16 ? 0 : -1))
            if (r6 <= 0) goto L_0x017b
            if (r0 == 0) goto L_0x0174
        L_0x0171:
            r9 = r33
            goto L_0x0176
        L_0x0174:
            r2 = r3
            goto L_0x0171
        L_0x0176:
            int r3 = java.lang.Math.max(r9, r2)
            goto L_0x0189
        L_0x017b:
            r9 = r33
            if (r0 == 0) goto L_0x0180
            r3 = r2
        L_0x0180:
            r2 = r32
            int r21 = java.lang.Math.max(r2, r3)
            r3 = r9
            r32 = r21
        L_0x0189:
            int r0 = r7.getChildrenSkipCount(r8, r11)
            int r0 = r0 + r11
            r19 = r1
            r1 = r4
            r4 = r5
            r2 = r26
            r21 = r32
            r5 = r0
            r0 = r23
        L_0x0199:
            int r5 = r5 + 1
            r10 = r4
            r13 = r28
            r11 = r31
            r8 = r40
            r9 = r41
            goto L_0x002c
        L_0x01a6:
            r6 = r2
            r9 = r3
            r4 = r10
            r31 = r11
            r28 = r13
            r2 = r21
            r10 = -2147483648(0xffffffff80000000, float:-0.0)
            r13 = r1
            r1 = -1
            int r3 = r7.mTotalLength
            if (r3 <= 0) goto L_0x01c7
            r3 = r31
            boolean r5 = r7.hasDividerBeforeChildAt(r3)
            if (r5 == 0) goto L_0x01c9
            int r5 = r7.mTotalLength
            int r8 = r7.mDividerHeight
            int r5 = r5 + r8
            r7.mTotalLength = r5
            goto L_0x01c9
        L_0x01c7:
            r3 = r31
        L_0x01c9:
            if (r15 == 0) goto L_0x0217
            r5 = r28
            if (r5 == r10) goto L_0x01d1
            if (r5 != 0) goto L_0x0219
        L_0x01d1:
            r8 = 0
            r7.mTotalLength = r8
            r8 = 0
        L_0x01d5:
            if (r8 >= r3) goto L_0x0219
            android.view.View r10 = r7.getVirtualChildAt(r8)
            if (r10 != 0) goto L_0x01e7
            int r10 = r7.mTotalLength
            int r11 = r7.measureNullChild(r8)
            int r10 = r10 + r11
            r7.mTotalLength = r10
            goto L_0x0213
        L_0x01e7:
            int r11 = r10.getVisibility()
            r14 = 8
            if (r11 != r14) goto L_0x01f5
            int r10 = r7.getChildrenSkipCount(r10, r8)
            int r8 = r8 + r10
            goto L_0x0213
        L_0x01f5:
            android.view.ViewGroup$LayoutParams r11 = r10.getLayoutParams()
            android.support.v7.widget.LinearLayoutCompat$LayoutParams r11 = (android.support.p004v7.widget.LinearLayoutCompat.LayoutParams) r11
            int r14 = r7.mTotalLength
            int r21 = r14 + r6
            int r1 = r11.topMargin
            int r21 = r21 + r1
            int r1 = r11.bottomMargin
            int r21 = r21 + r1
            int r1 = r7.getNextLocationOffset(r10)
            int r1 = r21 + r1
            int r1 = java.lang.Math.max(r14, r1)
            r7.mTotalLength = r1
        L_0x0213:
            int r8 = r8 + 1
            r1 = -1
            goto L_0x01d5
        L_0x0217:
            r5 = r28
        L_0x0219:
            int r1 = r7.mTotalLength
            int r8 = r39.getPaddingTop()
            int r10 = r39.getPaddingBottom()
            int r8 = r8 + r10
            int r1 = r1 + r8
            r7.mTotalLength = r1
            int r1 = r7.mTotalLength
            int r8 = r39.getSuggestedMinimumHeight()
            int r1 = java.lang.Math.max(r1, r8)
            r8 = r41
            r10 = 0
            int r1 = android.view.View.resolveSizeAndState(r1, r8, r10)
            r10 = 16777215(0xffffff, float:2.3509886E-38)
            r10 = r10 & r1
            int r11 = r7.mTotalLength
            int r10 = r10 - r11
            if (r18 != 0) goto L_0x0288
            if (r10 == 0) goto L_0x0248
            int r11 = (r0 > r16 ? 1 : (r0 == r16 ? 0 : -1))
            if (r11 <= 0) goto L_0x0248
            goto L_0x0288
        L_0x0248:
            int r0 = java.lang.Math.max(r2, r9)
            if (r15 == 0) goto L_0x0284
            r2 = 1073741824(0x40000000, float:2.0)
            if (r5 == r2) goto L_0x0284
            r2 = 0
        L_0x0253:
            if (r2 >= r3) goto L_0x0284
            android.view.View r5 = r7.getVirtualChildAt(r2)
            if (r5 == 0) goto L_0x0281
            int r9 = r5.getVisibility()
            r10 = 8
            if (r9 != r10) goto L_0x0264
            goto L_0x0281
        L_0x0264:
            android.view.ViewGroup$LayoutParams r9 = r5.getLayoutParams()
            android.support.v7.widget.LinearLayoutCompat$LayoutParams r9 = (android.support.p004v7.widget.LinearLayoutCompat.LayoutParams) r9
            float r9 = r9.weight
            int r9 = (r9 > r16 ? 1 : (r9 == r16 ? 0 : -1))
            if (r9 <= 0) goto L_0x0281
            int r9 = r5.getMeasuredWidth()
            r10 = 1073741824(0x40000000, float:2.0)
            int r9 = android.view.View.MeasureSpec.makeMeasureSpec(r9, r10)
            int r11 = android.view.View.MeasureSpec.makeMeasureSpec(r6, r10)
            r5.measure(r9, r11)
        L_0x0281:
            int r2 = r2 + 1
            goto L_0x0253
        L_0x0284:
            r9 = r40
            goto L_0x0380
        L_0x0288:
            float r6 = r7.mWeightSum
            int r6 = (r6 > r16 ? 1 : (r6 == r16 ? 0 : -1))
            if (r6 <= 0) goto L_0x0290
            float r0 = r7.mWeightSum
        L_0x0290:
            r6 = 0
            r7.mTotalLength = r6
            r9 = r0
            r0 = r6
            r38 = r10
            r10 = r4
            r4 = r38
        L_0x029a:
            if (r0 >= r3) goto L_0x036e
            android.view.View r11 = r7.getVirtualChildAt(r0)
            int r14 = r11.getVisibility()
            r15 = 8
            if (r14 != r15) goto L_0x02ae
            r36 = r9
            r9 = r40
            goto L_0x0367
        L_0x02ae:
            android.view.ViewGroup$LayoutParams r14 = r11.getLayoutParams()
            android.support.v7.widget.LinearLayoutCompat$LayoutParams r14 = (android.support.p004v7.widget.LinearLayoutCompat.LayoutParams) r14
            float r6 = r14.weight
            int r18 = (r6 > r16 ? 1 : (r6 == r16 ? 0 : -1))
            if (r18 <= 0) goto L_0x030e
            float r15 = (float) r4
            float r15 = r15 * r6
            float r15 = r15 / r9
            int r15 = (int) r15
            float r9 = r9 - r6
            int r4 = r4 - r15
            int r6 = r39.getPaddingLeft()
            int r18 = r39.getPaddingRight()
            int r6 = r6 + r18
            r35 = r4
            int r4 = r14.leftMargin
            int r6 = r6 + r4
            int r4 = r14.rightMargin
            int r6 = r6 + r4
            int r4 = r14.width
            r36 = r9
            r9 = r40
            int r4 = getChildMeasureSpec(r9, r6, r4)
            int r6 = r14.height
            if (r6 != 0) goto L_0x02f1
            r6 = 1073741824(0x40000000, float:2.0)
            if (r5 == r6) goto L_0x02e5
            goto L_0x02f3
        L_0x02e5:
            if (r15 <= 0) goto L_0x02e8
            goto L_0x02e9
        L_0x02e8:
            r15 = 0
        L_0x02e9:
            int r15 = android.view.View.MeasureSpec.makeMeasureSpec(r15, r6)
            r11.measure(r4, r15)
            goto L_0x0303
        L_0x02f1:
            r6 = 1073741824(0x40000000, float:2.0)
        L_0x02f3:
            int r18 = r11.getMeasuredHeight()
            int r15 = r18 + r15
            if (r15 >= 0) goto L_0x02fc
            r15 = 0
        L_0x02fc:
            int r15 = android.view.View.MeasureSpec.makeMeasureSpec(r15, r6)
            r11.measure(r4, r15)
        L_0x0303:
            int r4 = r11.getMeasuredState()
            r4 = r4 & -256(0xffffffffffffff00, float:NaN)
            int r10 = android.view.View.combineMeasuredStates(r10, r4)
            goto L_0x0315
        L_0x030e:
            r6 = r9
            r9 = r40
            r35 = r4
            r36 = r6
        L_0x0315:
            int r4 = r14.leftMargin
            int r6 = r14.rightMargin
            int r4 = r4 + r6
            int r6 = r11.getMeasuredWidth()
            int r6 = r6 + r4
            int r13 = java.lang.Math.max(r13, r6)
            r15 = 1073741824(0x40000000, float:2.0)
            if (r12 == r15) goto L_0x0331
            int r15 = r14.width
            r37 = r4
            r4 = -1
            if (r15 != r4) goto L_0x0334
            r15 = r17
            goto L_0x0335
        L_0x0331:
            r37 = r4
            r4 = -1
        L_0x0334:
            r15 = 0
        L_0x0335:
            if (r15 == 0) goto L_0x0339
            r6 = r37
        L_0x0339:
            int r2 = java.lang.Math.max(r2, r6)
            if (r19 == 0) goto L_0x0346
            int r6 = r14.width
            if (r6 != r4) goto L_0x0346
            r6 = r17
            goto L_0x0347
        L_0x0346:
            r6 = 0
        L_0x0347:
            int r15 = r7.mTotalLength
            int r18 = r11.getMeasuredHeight()
            int r18 = r15 + r18
            int r4 = r14.topMargin
            int r18 = r18 + r4
            int r4 = r14.bottomMargin
            int r18 = r18 + r4
            int r4 = r7.getNextLocationOffset(r11)
            int r4 = r18 + r4
            int r4 = java.lang.Math.max(r15, r4)
            r7.mTotalLength = r4
            r19 = r6
            r4 = r35
        L_0x0367:
            int r0 = r0 + 1
            r9 = r36
            r6 = 0
            goto L_0x029a
        L_0x036e:
            r9 = r40
            int r0 = r7.mTotalLength
            int r4 = r39.getPaddingTop()
            int r5 = r39.getPaddingBottom()
            int r4 = r4 + r5
            int r0 = r0 + r4
            r7.mTotalLength = r0
            r0 = r2
            r4 = r10
        L_0x0380:
            if (r19 != 0) goto L_0x0387
            r2 = 1073741824(0x40000000, float:2.0)
            if (r12 == r2) goto L_0x0387
            r13 = r0
        L_0x0387:
            int r0 = r39.getPaddingLeft()
            int r2 = r39.getPaddingRight()
            int r0 = r0 + r2
            int r13 = r13 + r0
            int r0 = r39.getSuggestedMinimumWidth()
            int r0 = java.lang.Math.max(r13, r0)
            int r0 = android.view.View.resolveSizeAndState(r0, r9, r4)
            r7.setMeasuredDimension(r0, r1)
            if (r20 == 0) goto L_0x03a5
            r7.forceUniformWidth(r3, r8)
        L_0x03a5:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.p004v7.widget.LinearLayoutCompat.measureVertical(int, int):void");
    }

    private void forceUniformWidth(int i, int i2) {
        int makeMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), ErrorDialogData.SUPPRESSED);
        for (int i3 = 0; i3 < i; i3++) {
            View virtualChildAt = getVirtualChildAt(i3);
            if (virtualChildAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                if (layoutParams.width == -1) {
                    int i4 = layoutParams.height;
                    layoutParams.height = virtualChildAt.getMeasuredHeight();
                    measureChildWithMargins(virtualChildAt, makeMeasureSpec, 0, i2, 0);
                    layoutParams.height = i4;
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:187:0x043f  */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x0191  */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x01d2  */
    /* JADX WARNING: Removed duplicated region for block: B:81:0x01e2  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void measureHorizontal(int r40, int r41) {
        /*
            r39 = this;
            r7 = r39
            r8 = r40
            r9 = r41
            r10 = 0
            r7.mTotalLength = r10
            int r11 = r39.getVirtualChildCount()
            int r12 = android.view.View.MeasureSpec.getMode(r40)
            int r13 = android.view.View.MeasureSpec.getMode(r41)
            int[] r0 = r7.mMaxAscent
            r14 = 4
            if (r0 == 0) goto L_0x001e
            int[] r0 = r7.mMaxDescent
            if (r0 != 0) goto L_0x0026
        L_0x001e:
            int[] r0 = new int[r14]
            r7.mMaxAscent = r0
            int[] r0 = new int[r14]
            r7.mMaxDescent = r0
        L_0x0026:
            int[] r15 = r7.mMaxAscent
            int[] r6 = r7.mMaxDescent
            r16 = 3
            r5 = -1
            r15[r16] = r5
            r17 = 2
            r15[r17] = r5
            r18 = 1
            r15[r18] = r5
            r15[r10] = r5
            r6[r16] = r5
            r6[r17] = r5
            r6[r18] = r5
            r6[r10] = r5
            boolean r4 = r7.mBaselineAligned
            boolean r3 = r7.mUseLargestChild
            r2 = 1073741824(0x40000000, float:2.0)
            if (r12 != r2) goto L_0x004c
            r19 = r18
            goto L_0x004e
        L_0x004c:
            r19 = r10
        L_0x004e:
            r20 = 0
            r0 = r10
            r22 = r0
            r23 = r22
            r24 = r23
            r25 = r24
            r26 = r25
            r28 = r26
            r27 = r18
            r21 = r20
            r14 = -2147483648(0xffffffff80000000, float:-0.0)
        L_0x0063:
            r29 = r6
            r1 = 8
            if (r0 >= r11) goto L_0x020d
            android.view.View r6 = r7.getVirtualChildAt(r0)
            if (r6 != 0) goto L_0x0082
            int r1 = r7.mTotalLength
            int r6 = r7.measureNullChild(r0)
            int r1 = r1 + r6
            r7.mTotalLength = r1
        L_0x0078:
            r1 = r0
            r0 = r2
            r30 = r3
            r32 = r4
            r35 = r12
            goto L_0x01fb
        L_0x0082:
            int r5 = r6.getVisibility()
            if (r5 != r1) goto L_0x008e
            int r1 = r7.getChildrenSkipCount(r6, r0)
            int r0 = r0 + r1
            goto L_0x0078
        L_0x008e:
            boolean r1 = r7.hasDividerBeforeChildAt(r0)
            if (r1 == 0) goto L_0x009b
            int r1 = r7.mTotalLength
            int r5 = r7.mDividerWidth
            int r1 = r1 + r5
            r7.mTotalLength = r1
        L_0x009b:
            android.view.ViewGroup$LayoutParams r1 = r6.getLayoutParams()
            r5 = r1
            android.support.v7.widget.LinearLayoutCompat$LayoutParams r5 = (android.support.p004v7.widget.LinearLayoutCompat.LayoutParams) r5
            float r1 = r5.weight
            float r21 = r21 + r1
            if (r12 != r2) goto L_0x00f7
            int r1 = r5.width
            if (r1 != 0) goto L_0x00f7
            float r1 = r5.weight
            int r1 = (r1 > r20 ? 1 : (r1 == r20 ? 0 : -1))
            if (r1 <= 0) goto L_0x00f7
            if (r19 == 0) goto L_0x00bf
            int r1 = r7.mTotalLength
            int r2 = r5.leftMargin
            int r10 = r5.rightMargin
            int r2 = r2 + r10
            int r1 = r1 + r2
            r7.mTotalLength = r1
            goto L_0x00cd
        L_0x00bf:
            int r1 = r7.mTotalLength
            int r2 = r5.leftMargin
            int r2 = r2 + r1
            int r10 = r5.rightMargin
            int r2 = r2 + r10
            int r1 = java.lang.Math.max(r1, r2)
            r7.mTotalLength = r1
        L_0x00cd:
            if (r4 == 0) goto L_0x00e5
            r1 = 0
            int r2 = android.view.View.MeasureSpec.makeMeasureSpec(r1, r1)
            r6.measure(r2, r2)
            r34 = r0
            r30 = r3
            r32 = r4
            r8 = r5
            r35 = r12
            r31 = -2
            r12 = r6
            goto L_0x016c
        L_0x00e5:
            r34 = r0
            r30 = r3
            r32 = r4
            r8 = r5
            r35 = r12
            r23 = r18
            r0 = 1073741824(0x40000000, float:2.0)
            r31 = -2
            r12 = r6
            goto L_0x016e
        L_0x00f7:
            int r1 = r5.width
            if (r1 != 0) goto L_0x0106
            float r1 = r5.weight
            int r1 = (r1 > r20 ? 1 : (r1 == r20 ? 0 : -1))
            if (r1 <= 0) goto L_0x0106
            r10 = -2
            r5.width = r10
            r2 = 0
            goto L_0x0109
        L_0x0106:
            r10 = -2
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
        L_0x0109:
            int r1 = (r21 > r20 ? 1 : (r21 == r20 ? 0 : -1))
            if (r1 != 0) goto L_0x0112
            int r1 = r7.mTotalLength
            r31 = r1
            goto L_0x0114
        L_0x0112:
            r31 = 0
        L_0x0114:
            r33 = 0
            r1 = r0
            r0 = r7
            r34 = r1
            r10 = -2147483648(0xffffffff80000000, float:-0.0)
            r1 = r6
            r10 = r2
            r2 = r34
            r30 = r3
            r3 = r8
            r32 = r4
            r4 = r31
            r8 = r5
            r35 = r12
            r12 = -1
            r5 = r9
            r12 = r6
            r31 = -2
            r6 = r33
            r0.measureChildBeforeLayout(r1, r2, r3, r4, r5, r6)
            r0 = -2147483648(0xffffffff80000000, float:-0.0)
            if (r10 == r0) goto L_0x013a
            r8.width = r10
        L_0x013a:
            int r0 = r12.getMeasuredWidth()
            if (r19 == 0) goto L_0x0151
            int r1 = r7.mTotalLength
            int r2 = r8.leftMargin
            int r2 = r2 + r0
            int r3 = r8.rightMargin
            int r2 = r2 + r3
            int r3 = r7.getNextLocationOffset(r12)
            int r2 = r2 + r3
            int r1 = r1 + r2
            r7.mTotalLength = r1
            goto L_0x0166
        L_0x0151:
            int r1 = r7.mTotalLength
            int r2 = r1 + r0
            int r3 = r8.leftMargin
            int r2 = r2 + r3
            int r3 = r8.rightMargin
            int r2 = r2 + r3
            int r3 = r7.getNextLocationOffset(r12)
            int r2 = r2 + r3
            int r1 = java.lang.Math.max(r1, r2)
            r7.mTotalLength = r1
        L_0x0166:
            if (r30 == 0) goto L_0x016c
            int r14 = java.lang.Math.max(r0, r14)
        L_0x016c:
            r0 = 1073741824(0x40000000, float:2.0)
        L_0x016e:
            if (r13 == r0) goto L_0x017a
            int r1 = r8.height
            r2 = -1
            if (r1 != r2) goto L_0x017a
            r1 = r18
            r28 = r1
            goto L_0x017b
        L_0x017a:
            r1 = 0
        L_0x017b:
            int r2 = r8.topMargin
            int r3 = r8.bottomMargin
            int r2 = r2 + r3
            int r3 = r12.getMeasuredHeight()
            int r3 = r3 + r2
            int r4 = r12.getMeasuredState()
            r10 = r26
            int r4 = android.view.View.combineMeasuredStates(r10, r4)
            if (r32 == 0) goto L_0x01bb
            int r5 = r12.getBaseline()
            r6 = -1
            if (r5 == r6) goto L_0x01bb
            int r6 = r8.gravity
            if (r6 >= 0) goto L_0x019f
            int r6 = r7.mGravity
            goto L_0x01a1
        L_0x019f:
            int r6 = r8.gravity
        L_0x01a1:
            r6 = r6 & 112(0x70, float:1.57E-43)
            r10 = 4
            int r6 = r6 >> r10
            r6 = r6 & -2
            int r6 = r6 >> 1
            r10 = r15[r6]
            int r10 = java.lang.Math.max(r10, r5)
            r15[r6] = r10
            r10 = r29[r6]
            int r5 = r3 - r5
            int r5 = java.lang.Math.max(r10, r5)
            r29[r6] = r5
        L_0x01bb:
            r5 = r22
            int r5 = java.lang.Math.max(r5, r3)
            if (r27 == 0) goto L_0x01cb
            int r6 = r8.height
            r10 = -1
            if (r6 != r10) goto L_0x01cb
            r6 = r18
            goto L_0x01cc
        L_0x01cb:
            r6 = 0
        L_0x01cc:
            float r8 = r8.weight
            int r8 = (r8 > r20 ? 1 : (r8 == r20 ? 0 : -1))
            if (r8 <= 0) goto L_0x01e2
            if (r1 == 0) goto L_0x01d7
        L_0x01d4:
            r8 = r25
            goto L_0x01d9
        L_0x01d7:
            r2 = r3
            goto L_0x01d4
        L_0x01d9:
            int r25 = java.lang.Math.max(r8, r2)
            r8 = r25
        L_0x01df:
            r10 = r34
            goto L_0x01ee
        L_0x01e2:
            r8 = r25
            if (r1 == 0) goto L_0x01e7
            r3 = r2
        L_0x01e7:
            r2 = r24
            int r24 = java.lang.Math.max(r2, r3)
            goto L_0x01df
        L_0x01ee:
            int r1 = r7.getChildrenSkipCount(r12, r10)
            int r1 = r1 + r10
            r26 = r4
            r22 = r5
            r27 = r6
            r25 = r8
        L_0x01fb:
            int r1 = r1 + 1
            r2 = r0
            r0 = r1
            r6 = r29
            r3 = r30
            r4 = r32
            r12 = r35
            r5 = -1
            r8 = r40
            r10 = 0
            goto L_0x0063
        L_0x020d:
            r0 = r2
            r30 = r3
            r32 = r4
            r35 = r12
            r5 = r22
            r2 = r24
            r8 = r25
            r10 = r26
            r31 = -2
            int r3 = r7.mTotalLength
            if (r3 <= 0) goto L_0x022f
            boolean r3 = r7.hasDividerBeforeChildAt(r11)
            if (r3 == 0) goto L_0x022f
            int r3 = r7.mTotalLength
            int r4 = r7.mDividerWidth
            int r3 = r3 + r4
            r7.mTotalLength = r3
        L_0x022f:
            r3 = r15[r18]
            r4 = -1
            if (r3 != r4) goto L_0x0241
            r3 = 0
            r6 = r15[r3]
            if (r6 != r4) goto L_0x0241
            r3 = r15[r17]
            if (r3 != r4) goto L_0x0241
            r3 = r15[r16]
            if (r3 == r4) goto L_0x0271
        L_0x0241:
            r3 = r15[r16]
            r4 = 0
            r6 = r15[r4]
            r12 = r15[r18]
            r0 = r15[r17]
            int r0 = java.lang.Math.max(r12, r0)
            int r0 = java.lang.Math.max(r6, r0)
            int r0 = java.lang.Math.max(r3, r0)
            r3 = r29[r16]
            r6 = r29[r4]
            r4 = r29[r18]
            r12 = r29[r17]
            int r4 = java.lang.Math.max(r4, r12)
            int r4 = java.lang.Math.max(r6, r4)
            int r3 = java.lang.Math.max(r3, r4)
            int r0 = r0 + r3
            int r22 = java.lang.Math.max(r5, r0)
            r5 = r22
        L_0x0271:
            if (r30 == 0) goto L_0x02d4
            r0 = r35
            r3 = -2147483648(0xffffffff80000000, float:-0.0)
            if (r0 == r3) goto L_0x027b
            if (r0 != 0) goto L_0x02d6
        L_0x027b:
            r3 = 0
            r7.mTotalLength = r3
            r3 = 0
        L_0x027f:
            if (r3 >= r11) goto L_0x02d6
            android.view.View r4 = r7.getVirtualChildAt(r3)
            if (r4 != 0) goto L_0x0291
            int r4 = r7.mTotalLength
            int r6 = r7.measureNullChild(r3)
            int r4 = r4 + r6
            r7.mTotalLength = r4
            goto L_0x029c
        L_0x0291:
            int r6 = r4.getVisibility()
            if (r6 != r1) goto L_0x029f
            int r4 = r7.getChildrenSkipCount(r4, r3)
            int r3 = r3 + r4
        L_0x029c:
            r36 = r3
            goto L_0x02cf
        L_0x029f:
            android.view.ViewGroup$LayoutParams r6 = r4.getLayoutParams()
            android.support.v7.widget.LinearLayoutCompat$LayoutParams r6 = (android.support.p004v7.widget.LinearLayoutCompat.LayoutParams) r6
            if (r19 == 0) goto L_0x02b8
            int r12 = r7.mTotalLength
            int r1 = r6.leftMargin
            int r1 = r1 + r14
            int r6 = r6.rightMargin
            int r1 = r1 + r6
            int r4 = r7.getNextLocationOffset(r4)
            int r1 = r1 + r4
            int r12 = r12 + r1
            r7.mTotalLength = r12
            goto L_0x029c
        L_0x02b8:
            int r1 = r7.mTotalLength
            int r12 = r1 + r14
            r36 = r3
            int r3 = r6.leftMargin
            int r12 = r12 + r3
            int r3 = r6.rightMargin
            int r12 = r12 + r3
            int r3 = r7.getNextLocationOffset(r4)
            int r12 = r12 + r3
            int r1 = java.lang.Math.max(r1, r12)
            r7.mTotalLength = r1
        L_0x02cf:
            int r3 = r36 + 1
            r1 = 8
            goto L_0x027f
        L_0x02d4:
            r0 = r35
        L_0x02d6:
            int r1 = r7.mTotalLength
            int r3 = r39.getPaddingLeft()
            int r4 = r39.getPaddingRight()
            int r3 = r3 + r4
            int r1 = r1 + r3
            r7.mTotalLength = r1
            int r1 = r7.mTotalLength
            int r3 = r39.getSuggestedMinimumWidth()
            int r1 = java.lang.Math.max(r1, r3)
            r3 = r40
            r4 = 0
            int r1 = android.view.View.resolveSizeAndState(r1, r3, r4)
            r4 = 16777215(0xffffff, float:2.3509886E-38)
            r4 = r4 & r1
            int r6 = r7.mTotalLength
            int r4 = r4 - r6
            if (r23 != 0) goto L_0x0346
            if (r4 == 0) goto L_0x0305
            int r12 = (r21 > r20 ? 1 : (r21 == r20 ? 0 : -1))
            if (r12 <= 0) goto L_0x0305
            goto L_0x0346
        L_0x0305:
            int r2 = java.lang.Math.max(r2, r8)
            if (r30 == 0) goto L_0x0341
            r4 = 1073741824(0x40000000, float:2.0)
            if (r0 == r4) goto L_0x0341
            r0 = 0
        L_0x0310:
            if (r0 >= r11) goto L_0x0341
            android.view.View r4 = r7.getVirtualChildAt(r0)
            if (r4 == 0) goto L_0x033e
            int r8 = r4.getVisibility()
            r12 = 8
            if (r8 != r12) goto L_0x0321
            goto L_0x033e
        L_0x0321:
            android.view.ViewGroup$LayoutParams r8 = r4.getLayoutParams()
            android.support.v7.widget.LinearLayoutCompat$LayoutParams r8 = (android.support.p004v7.widget.LinearLayoutCompat.LayoutParams) r8
            float r8 = r8.weight
            int r8 = (r8 > r20 ? 1 : (r8 == r20 ? 0 : -1))
            if (r8 <= 0) goto L_0x033e
            r8 = 1073741824(0x40000000, float:2.0)
            int r12 = android.view.View.MeasureSpec.makeMeasureSpec(r14, r8)
            int r15 = r4.getMeasuredHeight()
            int r15 = android.view.View.MeasureSpec.makeMeasureSpec(r15, r8)
            r4.measure(r12, r15)
        L_0x033e:
            int r0 = r0 + 1
            goto L_0x0310
        L_0x0341:
            r12 = r2
            r37 = r11
            goto L_0x04d2
        L_0x0346:
            float r5 = r7.mWeightSum
            int r5 = (r5 > r20 ? 1 : (r5 == r20 ? 0 : -1))
            if (r5 <= 0) goto L_0x0350
            float r5 = r7.mWeightSum
        L_0x034e:
            r8 = -1
            goto L_0x0353
        L_0x0350:
            r5 = r21
            goto L_0x034e
        L_0x0353:
            r15[r16] = r8
            r15[r17] = r8
            r15[r18] = r8
            r12 = 0
            r15[r12] = r8
            r29[r16] = r8
            r29[r17] = r8
            r29[r18] = r8
            r29[r12] = r8
            r7.mTotalLength = r12
            r12 = r2
            r2 = 0
            r8 = -1
        L_0x0369:
            if (r2 >= r11) goto L_0x047e
            android.view.View r14 = r7.getVirtualChildAt(r2)
            if (r14 == 0) goto L_0x0472
            int r6 = r14.getVisibility()
            r3 = 8
            if (r6 != r3) goto L_0x037b
            goto L_0x0472
        L_0x037b:
            android.view.ViewGroup$LayoutParams r6 = r14.getLayoutParams()
            android.support.v7.widget.LinearLayoutCompat$LayoutParams r6 = (android.support.p004v7.widget.LinearLayoutCompat.LayoutParams) r6
            float r3 = r6.weight
            int r21 = (r3 > r20 ? 1 : (r3 == r20 ? 0 : -1))
            if (r21 <= 0) goto L_0x03da
            r37 = r11
            float r11 = (float) r4
            float r11 = r11 * r3
            float r11 = r11 / r5
            int r11 = (int) r11
            float r5 = r5 - r3
            int r4 = r4 - r11
            int r3 = r39.getPaddingTop()
            int r21 = r39.getPaddingBottom()
            int r3 = r3 + r21
            r38 = r4
            int r4 = r6.topMargin
            int r3 = r3 + r4
            int r4 = r6.bottomMargin
            int r3 = r3 + r4
            int r4 = r6.height
            int r3 = getChildMeasureSpec(r9, r3, r4)
            int r4 = r6.width
            if (r4 != 0) goto L_0x03bc
            r4 = 1073741824(0x40000000, float:2.0)
            if (r0 == r4) goto L_0x03b0
            goto L_0x03be
        L_0x03b0:
            if (r11 <= 0) goto L_0x03b3
            goto L_0x03b4
        L_0x03b3:
            r11 = 0
        L_0x03b4:
            int r11 = android.view.View.MeasureSpec.makeMeasureSpec(r11, r4)
            r14.measure(r11, r3)
            goto L_0x03ce
        L_0x03bc:
            r4 = 1073741824(0x40000000, float:2.0)
        L_0x03be:
            int r21 = r14.getMeasuredWidth()
            int r11 = r21 + r11
            if (r11 >= 0) goto L_0x03c7
            r11 = 0
        L_0x03c7:
            int r11 = android.view.View.MeasureSpec.makeMeasureSpec(r11, r4)
            r14.measure(r11, r3)
        L_0x03ce:
            int r3 = r14.getMeasuredState()
            r4 = -16777216(0xffffffffff000000, float:-1.7014118E38)
            r3 = r3 & r4
            int r10 = android.view.View.combineMeasuredStates(r10, r3)
            goto L_0x03de
        L_0x03da:
            r37 = r11
            r38 = r4
        L_0x03de:
            if (r19 == 0) goto L_0x03f7
            int r3 = r7.mTotalLength
            int r4 = r14.getMeasuredWidth()
            int r11 = r6.leftMargin
            int r4 = r4 + r11
            int r11 = r6.rightMargin
            int r4 = r4 + r11
            int r11 = r7.getNextLocationOffset(r14)
            int r4 = r4 + r11
            int r3 = r3 + r4
            r7.mTotalLength = r3
        L_0x03f4:
            r3 = 1073741824(0x40000000, float:2.0)
            goto L_0x0410
        L_0x03f7:
            int r3 = r7.mTotalLength
            int r4 = r14.getMeasuredWidth()
            int r4 = r4 + r3
            int r11 = r6.leftMargin
            int r4 = r4 + r11
            int r11 = r6.rightMargin
            int r4 = r4 + r11
            int r11 = r7.getNextLocationOffset(r14)
            int r4 = r4 + r11
            int r3 = java.lang.Math.max(r3, r4)
            r7.mTotalLength = r3
            goto L_0x03f4
        L_0x0410:
            if (r13 == r3) goto L_0x041a
            int r3 = r6.height
            r4 = -1
            if (r3 != r4) goto L_0x041a
            r3 = r18
            goto L_0x041b
        L_0x041a:
            r3 = 0
        L_0x041b:
            int r4 = r6.topMargin
            int r11 = r6.bottomMargin
            int r4 = r4 + r11
            int r11 = r14.getMeasuredHeight()
            int r11 = r11 + r4
            int r8 = java.lang.Math.max(r8, r11)
            if (r3 == 0) goto L_0x042c
            goto L_0x042d
        L_0x042c:
            r4 = r11
        L_0x042d:
            int r3 = java.lang.Math.max(r12, r4)
            if (r27 == 0) goto L_0x043b
            int r4 = r6.height
            r12 = -1
            if (r4 != r12) goto L_0x043c
            r4 = r18
            goto L_0x043d
        L_0x043b:
            r12 = -1
        L_0x043c:
            r4 = 0
        L_0x043d:
            if (r32 == 0) goto L_0x046a
            int r14 = r14.getBaseline()
            if (r14 == r12) goto L_0x046a
            int r12 = r6.gravity
            if (r12 >= 0) goto L_0x044c
            int r6 = r7.mGravity
            goto L_0x044e
        L_0x044c:
            int r6 = r6.gravity
        L_0x044e:
            r6 = r6 & 112(0x70, float:1.57E-43)
            r21 = 4
            int r6 = r6 >> 4
            r6 = r6 & -2
            int r6 = r6 >> 1
            r12 = r15[r6]
            int r12 = java.lang.Math.max(r12, r14)
            r15[r6] = r12
            r12 = r29[r6]
            int r11 = r11 - r14
            int r11 = java.lang.Math.max(r12, r11)
            r29[r6] = r11
            goto L_0x046c
        L_0x046a:
            r21 = 4
        L_0x046c:
            r12 = r3
            r27 = r4
            r4 = r38
            goto L_0x0476
        L_0x0472:
            r37 = r11
            r21 = 4
        L_0x0476:
            int r2 = r2 + 1
            r11 = r37
            r3 = r40
            goto L_0x0369
        L_0x047e:
            r37 = r11
            int r0 = r7.mTotalLength
            int r2 = r39.getPaddingLeft()
            int r3 = r39.getPaddingRight()
            int r2 = r2 + r3
            int r0 = r0 + r2
            r7.mTotalLength = r0
            r0 = r15[r18]
            r2 = -1
            if (r0 != r2) goto L_0x04a3
            r0 = 0
            r3 = r15[r0]
            if (r3 != r2) goto L_0x04a3
            r0 = r15[r17]
            if (r0 != r2) goto L_0x04a3
            r0 = r15[r16]
            if (r0 == r2) goto L_0x04a1
            goto L_0x04a3
        L_0x04a1:
            r5 = r8
            goto L_0x04d2
        L_0x04a3:
            r0 = r15[r16]
            r2 = 0
            r3 = r15[r2]
            r4 = r15[r18]
            r5 = r15[r17]
            int r4 = java.lang.Math.max(r4, r5)
            int r3 = java.lang.Math.max(r3, r4)
            int r0 = java.lang.Math.max(r0, r3)
            r3 = r29[r16]
            r2 = r29[r2]
            r4 = r29[r18]
            r5 = r29[r17]
            int r4 = java.lang.Math.max(r4, r5)
            int r2 = java.lang.Math.max(r2, r4)
            int r2 = java.lang.Math.max(r3, r2)
            int r0 = r0 + r2
            int r0 = java.lang.Math.max(r8, r0)
            r5 = r0
        L_0x04d2:
            if (r27 != 0) goto L_0x04d9
            r0 = 1073741824(0x40000000, float:2.0)
            if (r13 == r0) goto L_0x04d9
            goto L_0x04da
        L_0x04d9:
            r12 = r5
        L_0x04da:
            int r0 = r39.getPaddingTop()
            int r2 = r39.getPaddingBottom()
            int r0 = r0 + r2
            int r12 = r12 + r0
            int r0 = r39.getSuggestedMinimumHeight()
            int r0 = java.lang.Math.max(r12, r0)
            r2 = -16777216(0xffffffffff000000, float:-1.7014118E38)
            r2 = r2 & r10
            r1 = r1 | r2
            int r2 = r10 << 16
            int r0 = android.view.View.resolveSizeAndState(r0, r9, r2)
            r7.setMeasuredDimension(r1, r0)
            if (r28 == 0) goto L_0x0502
            r1 = r37
            r0 = r40
            r7.forceUniformHeight(r1, r0)
        L_0x0502:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.p004v7.widget.LinearLayoutCompat.measureHorizontal(int, int):void");
    }

    private void forceUniformHeight(int i, int i2) {
        int makeMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight(), ErrorDialogData.SUPPRESSED);
        for (int i3 = 0; i3 < i; i3++) {
            View virtualChildAt = getVirtualChildAt(i3);
            if (virtualChildAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                if (layoutParams.height == -1) {
                    int i4 = layoutParams.width;
                    layoutParams.width = virtualChildAt.getMeasuredWidth();
                    measureChildWithMargins(virtualChildAt, i2, 0, makeMeasureSpec, 0);
                    layoutParams.width = i4;
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void measureChildBeforeLayout(View view, int i, int i2, int i3, int i4, int i5) {
        measureChildWithMargins(view, i2, i3, i4, i5);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        if (this.mOrientation == 1) {
            layoutVertical(i, i2, i3, i4);
        } else {
            layoutHorizontal(i, i2, i3, i4);
        }
    }

    /* access modifiers changed from: 0000 */
    public void layoutVertical(int i, int i2, int i3, int i4) {
        int i5;
        int i6;
        int i7;
        int paddingLeft = getPaddingLeft();
        int i8 = i3 - i;
        int paddingRight = i8 - getPaddingRight();
        int paddingRight2 = (i8 - paddingLeft) - getPaddingRight();
        int virtualChildCount = getVirtualChildCount();
        int i9 = this.mGravity & 112;
        int i10 = this.mGravity & GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK;
        if (i9 == 16) {
            i5 = (((i4 - i2) - this.mTotalLength) / 2) + getPaddingTop();
        } else if (i9 != 80) {
            i5 = getPaddingTop();
        } else {
            i5 = ((getPaddingTop() + i4) - i2) - this.mTotalLength;
        }
        int i11 = 0;
        while (i11 < virtualChildCount) {
            View virtualChildAt = getVirtualChildAt(i11);
            if (virtualChildAt == null) {
                i5 += measureNullChild(i11);
            } else if (virtualChildAt.getVisibility() != 8) {
                int measuredWidth = virtualChildAt.getMeasuredWidth();
                int measuredHeight = virtualChildAt.getMeasuredHeight();
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                int i12 = layoutParams.gravity;
                if (i12 < 0) {
                    i12 = i10;
                }
                int absoluteGravity = GravityCompat.getAbsoluteGravity(i12, ViewCompat.getLayoutDirection(this)) & 7;
                if (absoluteGravity == 1) {
                    i7 = ((((paddingRight2 - measuredWidth) / 2) + paddingLeft) + layoutParams.leftMargin) - layoutParams.rightMargin;
                } else if (absoluteGravity != 5) {
                    i7 = layoutParams.leftMargin + paddingLeft;
                } else {
                    i7 = (paddingRight - measuredWidth) - layoutParams.rightMargin;
                }
                int i13 = i7;
                if (hasDividerBeforeChildAt(i11)) {
                    i5 += this.mDividerHeight;
                }
                int i14 = i5 + layoutParams.topMargin;
                LayoutParams layoutParams2 = layoutParams;
                setChildFrame(virtualChildAt, i13, i14 + getLocationOffset(virtualChildAt), measuredWidth, measuredHeight);
                i11 += getChildrenSkipCount(virtualChildAt, i11);
                i5 = i14 + measuredHeight + layoutParams2.bottomMargin + getNextLocationOffset(virtualChildAt);
                i6 = 1;
                i11 += i6;
            }
            i6 = 1;
            i11 += i6;
        }
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x00b6  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x00bf  */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x00f6  */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x010a  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void layoutHorizontal(int r28, int r29, int r30, int r31) {
        /*
            r27 = this;
            r6 = r27
            boolean r2 = android.support.p004v7.widget.ViewUtils.isLayoutRtl(r27)
            int r7 = r27.getPaddingTop()
            int r3 = r31 - r29
            int r4 = r27.getPaddingBottom()
            int r8 = r3 - r4
            int r3 = r3 - r7
            int r4 = r27.getPaddingBottom()
            int r9 = r3 - r4
            int r10 = r27.getVirtualChildCount()
            int r3 = r6.mGravity
            r4 = 8388615(0x800007, float:1.1754953E-38)
            r3 = r3 & r4
            int r4 = r6.mGravity
            r11 = r4 & 112(0x70, float:1.57E-43)
            boolean r12 = r6.mBaselineAligned
            int[] r13 = r6.mMaxAscent
            int[] r14 = r6.mMaxDescent
            int r4 = android.support.p001v4.view.ViewCompat.getLayoutDirection(r27)
            int r3 = android.support.p001v4.view.GravityCompat.getAbsoluteGravity(r3, r4)
            r15 = 2
            r5 = 1
            if (r3 == r5) goto L_0x004e
            r4 = 5
            if (r3 == r4) goto L_0x0041
            int r0 = r27.getPaddingLeft()
            goto L_0x0059
        L_0x0041:
            int r3 = r27.getPaddingLeft()
            int r3 = r3 + r30
            int r3 = r3 - r28
            int r0 = r6.mTotalLength
            int r0 = r3 - r0
            goto L_0x0059
        L_0x004e:
            int r3 = r27.getPaddingLeft()
            int r0 = r30 - r28
            int r1 = r6.mTotalLength
            int r0 = r0 - r1
            int r0 = r0 / r15
            int r0 = r0 + r3
        L_0x0059:
            r1 = 0
            if (r2 == 0) goto L_0x0063
            int r2 = r10 + -1
            r16 = r2
            r17 = -1
            goto L_0x0067
        L_0x0063:
            r16 = r1
            r17 = r5
        L_0x0067:
            r3 = r1
        L_0x0068:
            if (r3 >= r10) goto L_0x0153
            int r1 = r17 * r3
            int r2 = r16 + r1
            android.view.View r1 = r6.getVirtualChildAt(r2)
            if (r1 != 0) goto L_0x0085
            int r1 = r6.measureNullChild(r2)
            int r0 = r0 + r1
            r18 = r5
            r26 = r7
            r23 = r10
            r24 = r11
        L_0x0081:
            r20 = -1
            goto L_0x0146
        L_0x0085:
            int r5 = r1.getVisibility()
            r15 = 8
            if (r5 == r15) goto L_0x013a
            int r15 = r1.getMeasuredWidth()
            int r5 = r1.getMeasuredHeight()
            android.view.ViewGroup$LayoutParams r20 = r1.getLayoutParams()
            r4 = r20
            android.support.v7.widget.LinearLayoutCompat$LayoutParams r4 = (android.support.p004v7.widget.LinearLayoutCompat.LayoutParams) r4
            if (r12 == 0) goto L_0x00ad
            r22 = r3
            int r3 = r4.height
            r23 = r10
            r10 = -1
            if (r3 == r10) goto L_0x00b1
            int r3 = r1.getBaseline()
            goto L_0x00b2
        L_0x00ad:
            r22 = r3
            r23 = r10
        L_0x00b1:
            r3 = -1
        L_0x00b2:
            int r10 = r4.gravity
            if (r10 >= 0) goto L_0x00b7
            r10 = r11
        L_0x00b7:
            r10 = r10 & 112(0x70, float:1.57E-43)
            r24 = r11
            r11 = 16
            if (r10 == r11) goto L_0x00f6
            r11 = 48
            if (r10 == r11) goto L_0x00e3
            r11 = 80
            if (r10 == r11) goto L_0x00cc
            r3 = r7
            r11 = -1
        L_0x00c9:
            r18 = 1
            goto L_0x0104
        L_0x00cc:
            int r10 = r8 - r5
            int r11 = r4.bottomMargin
            int r10 = r10 - r11
            r11 = -1
            if (r3 == r11) goto L_0x00e1
            int r20 = r1.getMeasuredHeight()
            int r20 = r20 - r3
            r3 = 2
            r21 = r14[r3]
            int r21 = r21 - r20
            int r10 = r10 - r21
        L_0x00e1:
            r3 = r10
            goto L_0x00c9
        L_0x00e3:
            r11 = -1
            int r10 = r4.topMargin
            int r10 = r10 + r7
            if (r3 == r11) goto L_0x00f2
            r18 = 1
            r20 = r13[r18]
            int r20 = r20 - r3
            int r10 = r10 + r20
            goto L_0x00f4
        L_0x00f2:
            r18 = 1
        L_0x00f4:
            r3 = r10
            goto L_0x0104
        L_0x00f6:
            r11 = -1
            r18 = 1
            int r3 = r9 - r5
            r10 = 2
            int r3 = r3 / r10
            int r3 = r3 + r7
            int r10 = r4.topMargin
            int r3 = r3 + r10
            int r10 = r4.bottomMargin
            int r3 = r3 - r10
        L_0x0104:
            boolean r10 = r6.hasDividerBeforeChildAt(r2)
            if (r10 == 0) goto L_0x010d
            int r10 = r6.mDividerWidth
            int r0 = r0 + r10
        L_0x010d:
            int r10 = r4.leftMargin
            int r10 = r10 + r0
            int r0 = r6.getLocationOffset(r1)
            int r19 = r10 + r0
            r0 = r6
            r25 = r1
            r11 = r2
            r2 = r19
            r19 = r22
            r26 = r7
            r20 = -1
            r7 = r4
            r4 = r15
            r0.setChildFrame(r1, r2, r3, r4, r5)
            int r0 = r7.rightMargin
            int r15 = r15 + r0
            r0 = r25
            int r1 = r6.getNextLocationOffset(r0)
            int r15 = r15 + r1
            int r10 = r10 + r15
            int r0 = r6.getChildrenSkipCount(r0, r11)
            int r3 = r19 + r0
            r0 = r10
            goto L_0x0146
        L_0x013a:
            r19 = r3
            r26 = r7
            r23 = r10
            r24 = r11
            r18 = 1
            goto L_0x0081
        L_0x0146:
            int r3 = r3 + 1
            r5 = r18
            r10 = r23
            r11 = r24
            r7 = r26
            r15 = 2
            goto L_0x0068
        L_0x0153:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.p004v7.widget.LinearLayoutCompat.layoutHorizontal(int, int, int, int):void");
    }

    private void setChildFrame(View view, int i, int i2, int i3, int i4) {
        view.layout(i, i2, i3 + i, i4 + i2);
    }

    public void setOrientation(int i) {
        if (this.mOrientation != i) {
            this.mOrientation = i;
            requestLayout();
        }
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    public void setGravity(int i) {
        if (this.mGravity != i) {
            if ((8388615 & i) == 0) {
                i |= GravityCompat.START;
            }
            if ((i & 112) == 0) {
                i |= 48;
            }
            this.mGravity = i;
            requestLayout();
        }
    }

    public int getGravity() {
        return this.mGravity;
    }

    public void setHorizontalGravity(int i) {
        int i2 = i & GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK;
        if ((8388615 & this.mGravity) != i2) {
            this.mGravity = i2 | (this.mGravity & -8388616);
            requestLayout();
        }
    }

    public void setVerticalGravity(int i) {
        int i2 = i & 112;
        if ((this.mGravity & 112) != i2) {
            this.mGravity = i2 | (this.mGravity & -113);
            requestLayout();
        }
    }

    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    /* access modifiers changed from: protected */
    public LayoutParams generateDefaultLayoutParams() {
        if (this.mOrientation == 0) {
            return new LayoutParams(-2, -2);
        }
        if (this.mOrientation == 1) {
            return new LayoutParams(-1, -2);
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
        return new LayoutParams(layoutParams);
    }

    /* access modifiers changed from: protected */
    public boolean checkLayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        if (VERSION.SDK_INT >= 14) {
            super.onInitializeAccessibilityEvent(accessibilityEvent);
            accessibilityEvent.setClassName(LinearLayoutCompat.class.getName());
        }
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        if (VERSION.SDK_INT >= 14) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            accessibilityNodeInfo.setClassName(LinearLayoutCompat.class.getName());
        }
    }
}
