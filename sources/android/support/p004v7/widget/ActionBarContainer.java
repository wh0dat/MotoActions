package android.support.p004v7.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.p001v4.view.ViewCompat;
import android.support.p004v7.appcompat.C0383R;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

@RestrictTo({Scope.LIBRARY_GROUP})
/* renamed from: android.support.v7.widget.ActionBarContainer */
public class ActionBarContainer extends FrameLayout {
    private View mActionBarView;
    Drawable mBackground;
    private View mContextView;
    private int mHeight;
    boolean mIsSplit;
    boolean mIsStacked;
    private boolean mIsTransitioning;
    Drawable mSplitBackground;
    Drawable mStackedBackground;
    private View mTabContainer;

    public ActionMode startActionModeForChild(View view, Callback callback) {
        return null;
    }

    public ActionBarContainer(Context context) {
        this(context, null);
    }

    public ActionBarContainer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        ViewCompat.setBackground(this, VERSION.SDK_INT >= 21 ? new ActionBarBackgroundDrawableV21(this) : new ActionBarBackgroundDrawable(this));
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C0383R.styleable.ActionBar);
        this.mBackground = obtainStyledAttributes.getDrawable(C0383R.styleable.ActionBar_background);
        this.mStackedBackground = obtainStyledAttributes.getDrawable(C0383R.styleable.ActionBar_backgroundStacked);
        this.mHeight = obtainStyledAttributes.getDimensionPixelSize(C0383R.styleable.ActionBar_height, -1);
        if (getId() == C0383R.C0385id.split_action_bar) {
            this.mIsSplit = true;
            this.mSplitBackground = obtainStyledAttributes.getDrawable(C0383R.styleable.ActionBar_backgroundSplit);
        }
        obtainStyledAttributes.recycle();
        boolean z = false;
        if (!this.mIsSplit ? this.mBackground == null && this.mStackedBackground == null : this.mSplitBackground == null) {
            z = true;
        }
        setWillNotDraw(z);
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        this.mActionBarView = findViewById(C0383R.C0385id.action_bar);
        this.mContextView = findViewById(C0383R.C0385id.action_context_bar);
    }

    public void setPrimaryBackground(Drawable drawable) {
        if (this.mBackground != null) {
            this.mBackground.setCallback(null);
            unscheduleDrawable(this.mBackground);
        }
        this.mBackground = drawable;
        if (drawable != null) {
            drawable.setCallback(this);
            if (this.mActionBarView != null) {
                this.mBackground.setBounds(this.mActionBarView.getLeft(), this.mActionBarView.getTop(), this.mActionBarView.getRight(), this.mActionBarView.getBottom());
            }
        }
        boolean z = false;
        if (!this.mIsSplit ? this.mBackground == null && this.mStackedBackground == null : this.mSplitBackground == null) {
            z = true;
        }
        setWillNotDraw(z);
        invalidate();
    }

    public void setStackedBackground(Drawable drawable) {
        if (this.mStackedBackground != null) {
            this.mStackedBackground.setCallback(null);
            unscheduleDrawable(this.mStackedBackground);
        }
        this.mStackedBackground = drawable;
        if (drawable != null) {
            drawable.setCallback(this);
            if (this.mIsStacked && this.mStackedBackground != null) {
                this.mStackedBackground.setBounds(this.mTabContainer.getLeft(), this.mTabContainer.getTop(), this.mTabContainer.getRight(), this.mTabContainer.getBottom());
            }
        }
        boolean z = false;
        if (!this.mIsSplit ? this.mBackground == null && this.mStackedBackground == null : this.mSplitBackground == null) {
            z = true;
        }
        setWillNotDraw(z);
        invalidate();
    }

    public void setSplitBackground(Drawable drawable) {
        if (this.mSplitBackground != null) {
            this.mSplitBackground.setCallback(null);
            unscheduleDrawable(this.mSplitBackground);
        }
        this.mSplitBackground = drawable;
        boolean z = false;
        if (drawable != null) {
            drawable.setCallback(this);
            if (this.mIsSplit && this.mSplitBackground != null) {
                this.mSplitBackground.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
            }
        }
        if (!this.mIsSplit ? this.mBackground == null && this.mStackedBackground == null : this.mSplitBackground == null) {
            z = true;
        }
        setWillNotDraw(z);
        invalidate();
    }

    public void setVisibility(int i) {
        super.setVisibility(i);
        boolean z = i == 0;
        if (this.mBackground != null) {
            this.mBackground.setVisible(z, false);
        }
        if (this.mStackedBackground != null) {
            this.mStackedBackground.setVisible(z, false);
        }
        if (this.mSplitBackground != null) {
            this.mSplitBackground.setVisible(z, false);
        }
    }

    /* access modifiers changed from: protected */
    public boolean verifyDrawable(Drawable drawable) {
        return (drawable == this.mBackground && !this.mIsSplit) || (drawable == this.mStackedBackground && this.mIsStacked) || ((drawable == this.mSplitBackground && this.mIsSplit) || super.verifyDrawable(drawable));
    }

    /* access modifiers changed from: protected */
    public void drawableStateChanged() {
        super.drawableStateChanged();
        if (this.mBackground != null && this.mBackground.isStateful()) {
            this.mBackground.setState(getDrawableState());
        }
        if (this.mStackedBackground != null && this.mStackedBackground.isStateful()) {
            this.mStackedBackground.setState(getDrawableState());
        }
        if (this.mSplitBackground != null && this.mSplitBackground.isStateful()) {
            this.mSplitBackground.setState(getDrawableState());
        }
    }

    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        if (this.mBackground != null) {
            this.mBackground.jumpToCurrentState();
        }
        if (this.mStackedBackground != null) {
            this.mStackedBackground.jumpToCurrentState();
        }
        if (this.mSplitBackground != null) {
            this.mSplitBackground.jumpToCurrentState();
        }
    }

    public void setTransitioning(boolean z) {
        this.mIsTransitioning = z;
        setDescendantFocusability(z ? 393216 : 262144);
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return this.mIsTransitioning || super.onInterceptTouchEvent(motionEvent);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        super.onTouchEvent(motionEvent);
        return true;
    }

    public boolean onHoverEvent(MotionEvent motionEvent) {
        super.onHoverEvent(motionEvent);
        return true;
    }

    public void setTabContainer(ScrollingTabContainerView scrollingTabContainerView) {
        if (this.mTabContainer != null) {
            removeView(this.mTabContainer);
        }
        this.mTabContainer = scrollingTabContainerView;
        if (scrollingTabContainerView != null) {
            addView(scrollingTabContainerView);
            LayoutParams layoutParams = scrollingTabContainerView.getLayoutParams();
            layoutParams.width = -1;
            layoutParams.height = -2;
            scrollingTabContainerView.setAllowCollapse(false);
        }
    }

    public View getTabContainer() {
        return this.mTabContainer;
    }

    public ActionMode startActionModeForChild(View view, Callback callback, int i) {
        if (i != 0) {
            return super.startActionModeForChild(view, callback, i);
        }
        return null;
    }

    private boolean isCollapsed(View view) {
        return view == null || view.getVisibility() == 8 || view.getMeasuredHeight() == 0;
    }

    private int getMeasuredHeightWithMargins(View view) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
        return view.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
    }

    public void onMeasure(int i, int i2) {
        if (this.mActionBarView == null && MeasureSpec.getMode(i2) == Integer.MIN_VALUE && this.mHeight >= 0) {
            i2 = MeasureSpec.makeMeasureSpec(Math.min(this.mHeight, MeasureSpec.getSize(i2)), Integer.MIN_VALUE);
        }
        super.onMeasure(i, i2);
        if (this.mActionBarView != null) {
            int mode = MeasureSpec.getMode(i2);
            if (!(this.mTabContainer == null || this.mTabContainer.getVisibility() == 8 || mode == 1073741824)) {
                int i3 = !isCollapsed(this.mActionBarView) ? getMeasuredHeightWithMargins(this.mActionBarView) : !isCollapsed(this.mContextView) ? getMeasuredHeightWithMargins(this.mContextView) : 0;
                setMeasuredDimension(getMeasuredWidth(), Math.min(i3 + getMeasuredHeightWithMargins(this.mTabContainer), mode == Integer.MIN_VALUE ? MeasureSpec.getSize(i2) : Integer.MAX_VALUE));
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:34:0x00c8  */
    /* JADX WARNING: Removed duplicated region for block: B:36:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onLayout(boolean r6, int r7, int r8, int r9, int r10) {
        /*
            r5 = this;
            super.onLayout(r6, r7, r8, r9, r10)
            android.view.View r6 = r5.mTabContainer
            r8 = 8
            r10 = 1
            r0 = 0
            if (r6 == 0) goto L_0x0013
            int r1 = r6.getVisibility()
            if (r1 == r8) goto L_0x0013
            r1 = r10
            goto L_0x0014
        L_0x0013:
            r1 = r0
        L_0x0014:
            if (r6 == 0) goto L_0x0035
            int r2 = r6.getVisibility()
            if (r2 == r8) goto L_0x0035
            int r8 = r5.getMeasuredHeight()
            android.view.ViewGroup$LayoutParams r2 = r6.getLayoutParams()
            android.widget.FrameLayout$LayoutParams r2 = (android.widget.FrameLayout.LayoutParams) r2
            int r3 = r6.getMeasuredHeight()
            int r3 = r8 - r3
            int r4 = r2.bottomMargin
            int r3 = r3 - r4
            int r2 = r2.bottomMargin
            int r8 = r8 - r2
            r6.layout(r7, r3, r9, r8)
        L_0x0035:
            boolean r7 = r5.mIsSplit
            if (r7 == 0) goto L_0x004f
            android.graphics.drawable.Drawable r6 = r5.mSplitBackground
            if (r6 == 0) goto L_0x004c
            android.graphics.drawable.Drawable r6 = r5.mSplitBackground
            int r7 = r5.getMeasuredWidth()
            int r8 = r5.getMeasuredHeight()
            r6.setBounds(r0, r0, r7, r8)
            goto L_0x00c6
        L_0x004c:
            r10 = r0
            goto L_0x00c6
        L_0x004f:
            android.graphics.drawable.Drawable r7 = r5.mBackground
            if (r7 == 0) goto L_0x00a9
            android.view.View r7 = r5.mActionBarView
            int r7 = r7.getVisibility()
            if (r7 != 0) goto L_0x0079
            android.graphics.drawable.Drawable r7 = r5.mBackground
            android.view.View r8 = r5.mActionBarView
            int r8 = r8.getLeft()
            android.view.View r9 = r5.mActionBarView
            int r9 = r9.getTop()
            android.view.View r0 = r5.mActionBarView
            int r0 = r0.getRight()
            android.view.View r2 = r5.mActionBarView
            int r2 = r2.getBottom()
            r7.setBounds(r8, r9, r0, r2)
            goto L_0x00a8
        L_0x0079:
            android.view.View r7 = r5.mContextView
            if (r7 == 0) goto L_0x00a3
            android.view.View r7 = r5.mContextView
            int r7 = r7.getVisibility()
            if (r7 != 0) goto L_0x00a3
            android.graphics.drawable.Drawable r7 = r5.mBackground
            android.view.View r8 = r5.mContextView
            int r8 = r8.getLeft()
            android.view.View r9 = r5.mContextView
            int r9 = r9.getTop()
            android.view.View r0 = r5.mContextView
            int r0 = r0.getRight()
            android.view.View r2 = r5.mContextView
            int r2 = r2.getBottom()
            r7.setBounds(r8, r9, r0, r2)
            goto L_0x00a8
        L_0x00a3:
            android.graphics.drawable.Drawable r7 = r5.mBackground
            r7.setBounds(r0, r0, r0, r0)
        L_0x00a8:
            r0 = r10
        L_0x00a9:
            r5.mIsStacked = r1
            if (r1 == 0) goto L_0x004c
            android.graphics.drawable.Drawable r7 = r5.mStackedBackground
            if (r7 == 0) goto L_0x004c
            android.graphics.drawable.Drawable r7 = r5.mStackedBackground
            int r8 = r6.getLeft()
            int r9 = r6.getTop()
            int r0 = r6.getRight()
            int r6 = r6.getBottom()
            r7.setBounds(r8, r9, r0, r6)
        L_0x00c6:
            if (r10 == 0) goto L_0x00cb
            r5.invalidate()
        L_0x00cb:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.p004v7.widget.ActionBarContainer.onLayout(boolean, int, int, int, int):void");
    }
}
