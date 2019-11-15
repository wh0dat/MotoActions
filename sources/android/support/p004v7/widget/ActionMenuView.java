package android.support.p004v7.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.annotation.StyleRes;
import android.support.p004v7.view.menu.ActionMenuItemView;
import android.support.p004v7.view.menu.MenuBuilder;
import android.support.p004v7.view.menu.MenuBuilder.ItemInvoker;
import android.support.p004v7.view.menu.MenuItemImpl;
import android.support.p004v7.view.menu.MenuPresenter.Callback;
import android.support.p004v7.view.menu.MenuView;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewDebug.ExportedProperty;
import android.view.accessibility.AccessibilityEvent;
import com.google.android.gms.common.util.CrashUtils.ErrorDialogData;

/* renamed from: android.support.v7.widget.ActionMenuView */
public class ActionMenuView extends LinearLayoutCompat implements ItemInvoker, MenuView {
    static final int GENERATED_ITEM_PADDING = 4;
    static final int MIN_CELL_SIZE = 56;
    private static final String TAG = "ActionMenuView";
    private Callback mActionMenuPresenterCallback;
    private boolean mFormatItems;
    private int mFormatItemsWidth;
    private int mGeneratedItemPadding;
    private MenuBuilder mMenu;
    MenuBuilder.Callback mMenuBuilderCallback;
    private int mMinCellSize;
    OnMenuItemClickListener mOnMenuItemClickListener;
    private Context mPopupContext;
    private int mPopupTheme;
    private ActionMenuPresenter mPresenter;
    private boolean mReserveOverflow;

    @RestrictTo({Scope.LIBRARY_GROUP})
    /* renamed from: android.support.v7.widget.ActionMenuView$ActionMenuChildView */
    public interface ActionMenuChildView {
        boolean needsDividerAfter();

        boolean needsDividerBefore();
    }

    /* renamed from: android.support.v7.widget.ActionMenuView$ActionMenuPresenterCallback */
    private static class ActionMenuPresenterCallback implements Callback {
        public void onCloseMenu(MenuBuilder menuBuilder, boolean z) {
        }

        public boolean onOpenSubMenu(MenuBuilder menuBuilder) {
            return false;
        }

        ActionMenuPresenterCallback() {
        }
    }

    /* renamed from: android.support.v7.widget.ActionMenuView$LayoutParams */
    public static class LayoutParams extends android.support.p004v7.widget.LinearLayoutCompat.LayoutParams {
        @ExportedProperty
        public int cellsUsed;
        @ExportedProperty
        public boolean expandable;
        boolean expanded;
        @ExportedProperty
        public int extraPixels;
        @ExportedProperty
        public boolean isOverflowButton;
        @ExportedProperty
        public boolean preventEdgeOffset;

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }

        public LayoutParams(LayoutParams layoutParams) {
            super((android.view.ViewGroup.LayoutParams) layoutParams);
            this.isOverflowButton = layoutParams.isOverflowButton;
        }

        public LayoutParams(int i, int i2) {
            super(i, i2);
            this.isOverflowButton = false;
        }

        LayoutParams(int i, int i2, boolean z) {
            super(i, i2);
            this.isOverflowButton = z;
        }
    }

    /* renamed from: android.support.v7.widget.ActionMenuView$MenuBuilderCallback */
    private class MenuBuilderCallback implements MenuBuilder.Callback {
        MenuBuilderCallback() {
        }

        public boolean onMenuItemSelected(MenuBuilder menuBuilder, MenuItem menuItem) {
            return ActionMenuView.this.mOnMenuItemClickListener != null && ActionMenuView.this.mOnMenuItemClickListener.onMenuItemClick(menuItem);
        }

        public void onMenuModeChange(MenuBuilder menuBuilder) {
            if (ActionMenuView.this.mMenuBuilderCallback != null) {
                ActionMenuView.this.mMenuBuilderCallback.onMenuModeChange(menuBuilder);
            }
        }
    }

    /* renamed from: android.support.v7.widget.ActionMenuView$OnMenuItemClickListener */
    public interface OnMenuItemClickListener {
        boolean onMenuItemClick(MenuItem menuItem);
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        return false;
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public int getWindowAnimations() {
        return 0;
    }

    public ActionMenuView(Context context) {
        this(context, null);
    }

    public ActionMenuView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setBaselineAligned(false);
        float f = context.getResources().getDisplayMetrics().density;
        this.mMinCellSize = (int) (56.0f * f);
        this.mGeneratedItemPadding = (int) (f * 4.0f);
        this.mPopupContext = context;
        this.mPopupTheme = 0;
    }

    public void setPopupTheme(@StyleRes int i) {
        if (this.mPopupTheme != i) {
            this.mPopupTheme = i;
            if (i == 0) {
                this.mPopupContext = getContext();
            } else {
                this.mPopupContext = new ContextThemeWrapper(getContext(), i);
            }
        }
    }

    public int getPopupTheme() {
        return this.mPopupTheme;
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public void setPresenter(ActionMenuPresenter actionMenuPresenter) {
        this.mPresenter = actionMenuPresenter;
        this.mPresenter.setMenuView(this);
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (this.mPresenter != null) {
            this.mPresenter.updateMenuView(false);
            if (this.mPresenter.isOverflowMenuShowing()) {
                this.mPresenter.hideOverflowMenu();
                this.mPresenter.showOverflowMenu();
            }
        }
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        this.mOnMenuItemClickListener = onMenuItemClickListener;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        boolean z = this.mFormatItems;
        this.mFormatItems = MeasureSpec.getMode(i) == 1073741824;
        if (z != this.mFormatItems) {
            this.mFormatItemsWidth = 0;
        }
        int size = MeasureSpec.getSize(i);
        if (!(!this.mFormatItems || this.mMenu == null || size == this.mFormatItemsWidth)) {
            this.mFormatItemsWidth = size;
            this.mMenu.onItemsChanged(true);
        }
        int childCount = getChildCount();
        if (!this.mFormatItems || childCount <= 0) {
            for (int i3 = 0; i3 < childCount; i3++) {
                LayoutParams layoutParams = (LayoutParams) getChildAt(i3).getLayoutParams();
                layoutParams.rightMargin = 0;
                layoutParams.leftMargin = 0;
            }
            super.onMeasure(i, i2);
            return;
        }
        onMeasureExactFormat(i, i2);
    }

    /* JADX WARNING: Removed duplicated region for block: B:135:0x0257 A[LOOP:5: B:135:0x0257->B:140:0x0279, LOOP_START, PHI: r5 r29 
      PHI: (r5v5 int) = (r5v4 int), (r5v6 int) binds: [B:134:0x0255, B:140:0x0279] A[DONT_GENERATE, DONT_INLINE]
      PHI: (r29v1 int) = (r29v0 int), (r29v2 int) binds: [B:134:0x0255, B:140:0x0279] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARNING: Removed duplicated region for block: B:142:0x0280  */
    /* JADX WARNING: Removed duplicated region for block: B:143:0x0285  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void onMeasureExactFormat(int r32, int r33) {
        /*
            r31 = this;
            r0 = r31
            int r1 = android.view.View.MeasureSpec.getMode(r33)
            int r2 = android.view.View.MeasureSpec.getSize(r32)
            int r3 = android.view.View.MeasureSpec.getSize(r33)
            int r4 = r31.getPaddingLeft()
            int r5 = r31.getPaddingRight()
            int r4 = r4 + r5
            int r5 = r31.getPaddingTop()
            int r6 = r31.getPaddingBottom()
            int r5 = r5 + r6
            r6 = -2
            r7 = r33
            int r6 = getChildMeasureSpec(r7, r5, r6)
            int r2 = r2 - r4
            int r4 = r0.mMinCellSize
            int r4 = r2 / r4
            int r7 = r0.mMinCellSize
            int r7 = r2 % r7
            r8 = 0
            if (r4 != 0) goto L_0x0037
            r0.setMeasuredDimension(r2, r8)
            return
        L_0x0037:
            int r9 = r0.mMinCellSize
            int r7 = r7 / r4
            int r9 = r9 + r7
            int r7 = r31.getChildCount()
            r14 = r4
            r4 = r8
            r10 = r4
            r12 = r10
            r13 = r12
            r15 = r13
            r16 = r15
            r19 = 0
        L_0x0049:
            if (r4 >= r7) goto L_0x00d6
            android.view.View r11 = r0.getChildAt(r4)
            int r8 = r11.getVisibility()
            r21 = r3
            r3 = 8
            if (r8 != r3) goto L_0x005d
            r23 = r2
            goto L_0x00cd
        L_0x005d:
            boolean r3 = r11 instanceof android.support.p004v7.view.menu.ActionMenuItemView
            int r13 = r13 + 1
            if (r3 == 0) goto L_0x0070
            int r8 = r0.mGeneratedItemPadding
            r22 = r13
            int r13 = r0.mGeneratedItemPadding
            r23 = r2
            r2 = 0
            r11.setPadding(r8, r2, r13, r2)
            goto L_0x0075
        L_0x0070:
            r23 = r2
            r22 = r13
            r2 = 0
        L_0x0075:
            android.view.ViewGroup$LayoutParams r8 = r11.getLayoutParams()
            android.support.v7.widget.ActionMenuView$LayoutParams r8 = (android.support.p004v7.widget.ActionMenuView.LayoutParams) r8
            r8.expanded = r2
            r8.extraPixels = r2
            r8.cellsUsed = r2
            r8.expandable = r2
            r8.leftMargin = r2
            r8.rightMargin = r2
            if (r3 == 0) goto L_0x0094
            r2 = r11
            android.support.v7.view.menu.ActionMenuItemView r2 = (android.support.p004v7.view.menu.ActionMenuItemView) r2
            boolean r2 = r2.hasText()
            if (r2 == 0) goto L_0x0094
            r2 = 1
            goto L_0x0095
        L_0x0094:
            r2 = 0
        L_0x0095:
            r8.preventEdgeOffset = r2
            boolean r2 = r8.isOverflowButton
            if (r2 == 0) goto L_0x009d
            r2 = 1
            goto L_0x009e
        L_0x009d:
            r2 = r14
        L_0x009e:
            int r2 = measureChildForCells(r11, r9, r2, r6, r5)
            int r3 = java.lang.Math.max(r15, r2)
            boolean r13 = r8.expandable
            if (r13 == 0) goto L_0x00ac
            int r16 = r16 + 1
        L_0x00ac:
            boolean r8 = r8.isOverflowButton
            if (r8 == 0) goto L_0x00b1
            r12 = 1
        L_0x00b1:
            int r14 = r14 - r2
            int r8 = r11.getMeasuredHeight()
            int r10 = java.lang.Math.max(r10, r8)
            r8 = 1
            if (r2 != r8) goto L_0x00c7
            int r2 = r8 << r4
            r24 = r3
            long r2 = (long) r2
            long r2 = r19 | r2
            r19 = r2
            goto L_0x00c9
        L_0x00c7:
            r24 = r3
        L_0x00c9:
            r13 = r22
            r15 = r24
        L_0x00cd:
            int r4 = r4 + 1
            r3 = r21
            r2 = r23
            r8 = 0
            goto L_0x0049
        L_0x00d6:
            r23 = r2
            r21 = r3
            r2 = 2
            if (r12 == 0) goto L_0x00e1
            if (r13 != r2) goto L_0x00e1
            r3 = 1
            goto L_0x00e2
        L_0x00e1:
            r3 = 0
        L_0x00e2:
            r4 = 0
        L_0x00e3:
            if (r16 <= 0) goto L_0x018a
            if (r14 <= 0) goto L_0x018a
            r5 = 2147483647(0x7fffffff, float:NaN)
            r11 = r5
            r5 = 0
            r8 = 0
            r24 = 0
        L_0x00ef:
            if (r5 >= r7) goto L_0x012b
            android.view.View r2 = r0.getChildAt(r5)
            android.view.ViewGroup$LayoutParams r2 = r2.getLayoutParams()
            android.support.v7.widget.ActionMenuView$LayoutParams r2 = (android.support.p004v7.widget.ActionMenuView.LayoutParams) r2
            r26 = r4
            boolean r4 = r2.expandable
            if (r4 != 0) goto L_0x0104
            r27 = r5
            goto L_0x0125
        L_0x0104:
            int r4 = r2.cellsUsed
            if (r4 >= r11) goto L_0x0115
            int r2 = r2.cellsUsed
            r4 = 1
            int r8 = r4 << r5
            r27 = r5
            long r4 = (long) r8
            r11 = r2
            r24 = r4
            r8 = 1
            goto L_0x0125
        L_0x0115:
            r27 = r5
            int r2 = r2.cellsUsed
            if (r2 != r11) goto L_0x0125
            r2 = 1
            int r4 = r2 << r27
            long r4 = (long) r4
            long r4 = r24 | r4
            int r8 = r8 + 1
            r24 = r4
        L_0x0125:
            int r5 = r27 + 1
            r4 = r26
            r2 = 2
            goto L_0x00ef
        L_0x012b:
            r26 = r4
            long r19 = r19 | r24
            if (r8 <= r14) goto L_0x0138
        L_0x0131:
            r29 = r6
            r30 = r7
            r28 = r10
            goto L_0x018d
        L_0x0138:
            int r11 = r11 + 1
            r2 = 0
        L_0x013b:
            if (r2 >= r7) goto L_0x0186
            android.view.View r4 = r0.getChildAt(r2)
            android.view.ViewGroup$LayoutParams r5 = r4.getLayoutParams()
            android.support.v7.widget.ActionMenuView$LayoutParams r5 = (android.support.p004v7.widget.ActionMenuView.LayoutParams) r5
            r28 = r10
            r8 = 1
            int r10 = r8 << r2
            r29 = r6
            r30 = r7
            long r6 = (long) r10
            long r26 = r24 & r6
            r17 = 0
            int r8 = (r26 > r17 ? 1 : (r26 == r17 ? 0 : -1))
            if (r8 != 0) goto L_0x0160
            int r4 = r5.cellsUsed
            if (r4 != r11) goto L_0x017d
            long r19 = r19 | r6
            goto L_0x017d
        L_0x0160:
            if (r3 == 0) goto L_0x0173
            boolean r6 = r5.preventEdgeOffset
            if (r6 == 0) goto L_0x0173
            r6 = 1
            if (r14 != r6) goto L_0x0174
            int r7 = r0.mGeneratedItemPadding
            int r7 = r7 + r9
            int r8 = r0.mGeneratedItemPadding
            r10 = 0
            r4.setPadding(r7, r10, r8, r10)
            goto L_0x0174
        L_0x0173:
            r6 = 1
        L_0x0174:
            int r4 = r5.cellsUsed
            int r4 = r4 + r6
            r5.cellsUsed = r4
            r5.expanded = r6
            int r14 = r14 + -1
        L_0x017d:
            int r2 = r2 + 1
            r10 = r28
            r6 = r29
            r7 = r30
            goto L_0x013b
        L_0x0186:
            r2 = 2
            r4 = 1
            goto L_0x00e3
        L_0x018a:
            r26 = r4
            goto L_0x0131
        L_0x018d:
            r2 = r19
            if (r12 != 0) goto L_0x0196
            r4 = 1
            if (r13 != r4) goto L_0x0197
            r5 = r4
            goto L_0x0198
        L_0x0196:
            r4 = 1
        L_0x0197:
            r5 = 0
        L_0x0198:
            if (r14 <= 0) goto L_0x024e
            r6 = 0
            int r8 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1))
            if (r8 == 0) goto L_0x024e
            int r13 = r13 - r4
            if (r14 < r13) goto L_0x01a7
            if (r5 != 0) goto L_0x01a7
            if (r15 <= r4) goto L_0x024e
        L_0x01a7:
            int r4 = java.lang.Long.bitCount(r2)
            float r4 = (float) r4
            if (r5 != 0) goto L_0x01e8
            r5 = 1
            long r5 = r5 & r2
            r7 = 0
            int r5 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
            r6 = 1056964608(0x3f000000, float:0.5)
            if (r5 == 0) goto L_0x01ca
            r5 = 0
            android.view.View r7 = r0.getChildAt(r5)
            android.view.ViewGroup$LayoutParams r7 = r7.getLayoutParams()
            android.support.v7.widget.ActionMenuView$LayoutParams r7 = (android.support.p004v7.widget.ActionMenuView.LayoutParams) r7
            boolean r7 = r7.preventEdgeOffset
            if (r7 != 0) goto L_0x01cb
            float r4 = r4 - r6
            goto L_0x01cb
        L_0x01ca:
            r5 = 0
        L_0x01cb:
            int r7 = r30 + -1
            r8 = 1
            int r10 = r8 << r7
            long r10 = (long) r10
            long r10 = r10 & r2
            r12 = 0
            int r8 = (r10 > r12 ? 1 : (r10 == r12 ? 0 : -1))
            if (r8 == 0) goto L_0x01e9
            android.view.View r7 = r0.getChildAt(r7)
            android.view.ViewGroup$LayoutParams r7 = r7.getLayoutParams()
            android.support.v7.widget.ActionMenuView$LayoutParams r7 = (android.support.p004v7.widget.ActionMenuView.LayoutParams) r7
            boolean r7 = r7.preventEdgeOffset
            if (r7 != 0) goto L_0x01e9
            float r4 = r4 - r6
            goto L_0x01e9
        L_0x01e8:
            r5 = 0
        L_0x01e9:
            r6 = 0
            int r6 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r6 <= 0) goto L_0x01f3
            int r14 = r14 * r9
            float r6 = (float) r14
            float r6 = r6 / r4
            int r8 = (int) r6
            goto L_0x01f4
        L_0x01f3:
            r8 = r5
        L_0x01f4:
            r6 = r5
            r11 = r26
            r4 = r30
        L_0x01f9:
            if (r6 >= r4) goto L_0x0253
            r7 = 1
            int r10 = r7 << r6
            long r12 = (long) r10
            long r12 = r12 & r2
            r14 = 0
            int r7 = (r12 > r14 ? 1 : (r12 == r14 ? 0 : -1))
            if (r7 != 0) goto L_0x0209
            r7 = 1
            r12 = 2
            goto L_0x024b
        L_0x0209:
            android.view.View r7 = r0.getChildAt(r6)
            android.view.ViewGroup$LayoutParams r10 = r7.getLayoutParams()
            android.support.v7.widget.ActionMenuView$LayoutParams r10 = (android.support.p004v7.widget.ActionMenuView.LayoutParams) r10
            boolean r7 = r7 instanceof android.support.p004v7.view.menu.ActionMenuItemView
            if (r7 == 0) goto L_0x022c
            r10.extraPixels = r8
            r7 = 1
            r10.expanded = r7
            if (r6 != 0) goto L_0x0228
            boolean r7 = r10.preventEdgeOffset
            if (r7 != 0) goto L_0x0228
            int r7 = -r8
            r12 = 2
            int r7 = r7 / r12
            r10.leftMargin = r7
            goto L_0x0229
        L_0x0228:
            r12 = 2
        L_0x0229:
            r7 = 1
            r11 = 1
            goto L_0x024b
        L_0x022c:
            r12 = 2
            boolean r7 = r10.isOverflowButton
            if (r7 == 0) goto L_0x023c
            r10.extraPixels = r8
            r7 = 1
            r10.expanded = r7
            int r11 = -r8
            int r11 = r11 / r12
            r10.rightMargin = r11
            r11 = r7
            goto L_0x024b
        L_0x023c:
            r7 = 1
            if (r6 == 0) goto L_0x0243
            int r13 = r8 / 2
            r10.leftMargin = r13
        L_0x0243:
            int r13 = r4 + -1
            if (r6 == r13) goto L_0x024b
            int r13 = r8 / 2
            r10.rightMargin = r13
        L_0x024b:
            int r6 = r6 + 1
            goto L_0x01f9
        L_0x024e:
            r4 = r30
            r5 = 0
            r11 = r26
        L_0x0253:
            r2 = 1073741824(0x40000000, float:2.0)
            if (r11 == 0) goto L_0x027e
        L_0x0257:
            if (r5 >= r4) goto L_0x027e
            android.view.View r3 = r0.getChildAt(r5)
            android.view.ViewGroup$LayoutParams r6 = r3.getLayoutParams()
            android.support.v7.widget.ActionMenuView$LayoutParams r6 = (android.support.p004v7.widget.ActionMenuView.LayoutParams) r6
            boolean r7 = r6.expanded
            if (r7 != 0) goto L_0x026a
            r7 = r29
            goto L_0x0279
        L_0x026a:
            int r7 = r6.cellsUsed
            int r7 = r7 * r9
            int r6 = r6.extraPixels
            int r7 = r7 + r6
            int r6 = android.view.View.MeasureSpec.makeMeasureSpec(r7, r2)
            r7 = r29
            r3.measure(r6, r7)
        L_0x0279:
            int r5 = r5 + 1
            r29 = r7
            goto L_0x0257
        L_0x027e:
            if (r1 == r2) goto L_0x0285
            r2 = r23
            r1 = r28
            goto L_0x0289
        L_0x0285:
            r1 = r21
            r2 = r23
        L_0x0289:
            r0.setMeasuredDimension(r2, r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.p004v7.widget.ActionMenuView.onMeasureExactFormat(int, int):void");
    }

    static int measureChildForCells(View view, int i, int i2, int i3, int i4) {
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        int makeMeasureSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(i3) - i4, MeasureSpec.getMode(i3));
        ActionMenuItemView actionMenuItemView = view instanceof ActionMenuItemView ? (ActionMenuItemView) view : null;
        boolean z = false;
        boolean z2 = actionMenuItemView != null && actionMenuItemView.hasText();
        int i5 = 2;
        if (i2 <= 0 || (z2 && i2 < 2)) {
            i5 = 0;
        } else {
            view.measure(MeasureSpec.makeMeasureSpec(i2 * i, Integer.MIN_VALUE), makeMeasureSpec);
            int measuredWidth = view.getMeasuredWidth();
            int i6 = measuredWidth / i;
            if (measuredWidth % i != 0) {
                i6++;
            }
            if (!z2 || i6 >= 2) {
                i5 = i6;
            }
        }
        if (!layoutParams.isOverflowButton && z2) {
            z = true;
        }
        layoutParams.expandable = z;
        layoutParams.cellsUsed = i5;
        view.measure(MeasureSpec.makeMeasureSpec(i * i5, ErrorDialogData.SUPPRESSED), makeMeasureSpec);
        return i5;
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int i5;
        int i6;
        int i7;
        int i8;
        if (!this.mFormatItems) {
            super.onLayout(z, i, i2, i3, i4);
            return;
        }
        int childCount = getChildCount();
        int i9 = (i4 - i2) / 2;
        int dividerWidth = getDividerWidth();
        int i10 = i3 - i;
        int paddingRight = (i10 - getPaddingRight()) - getPaddingLeft();
        boolean isLayoutRtl = ViewUtils.isLayoutRtl(this);
        int i11 = paddingRight;
        int i12 = 0;
        int i13 = 0;
        for (int i14 = 0; i14 < childCount; i14++) {
            View childAt = getChildAt(i14);
            if (childAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                if (layoutParams.isOverflowButton) {
                    int measuredWidth = childAt.getMeasuredWidth();
                    if (hasSupportDividerBeforeChildAt(i14)) {
                        measuredWidth += dividerWidth;
                    }
                    int measuredHeight = childAt.getMeasuredHeight();
                    if (isLayoutRtl) {
                        i7 = getPaddingLeft() + layoutParams.leftMargin;
                        i8 = i7 + measuredWidth;
                    } else {
                        i8 = (getWidth() - getPaddingRight()) - layoutParams.rightMargin;
                        i7 = i8 - measuredWidth;
                    }
                    int i15 = i9 - (measuredHeight / 2);
                    childAt.layout(i7, i15, i8, measuredHeight + i15);
                    i11 -= measuredWidth;
                    i12 = 1;
                } else {
                    i11 -= (childAt.getMeasuredWidth() + layoutParams.leftMargin) + layoutParams.rightMargin;
                    boolean hasSupportDividerBeforeChildAt = hasSupportDividerBeforeChildAt(i14);
                    i13++;
                }
            }
        }
        if (childCount == 1 && i12 == 0) {
            View childAt2 = getChildAt(0);
            int measuredWidth2 = childAt2.getMeasuredWidth();
            int measuredHeight2 = childAt2.getMeasuredHeight();
            int i16 = (i10 / 2) - (measuredWidth2 / 2);
            int i17 = i9 - (measuredHeight2 / 2);
            childAt2.layout(i16, i17, measuredWidth2 + i16, measuredHeight2 + i17);
            return;
        }
        int i18 = i13 - (i12 ^ 1);
        if (i18 > 0) {
            i5 = i11 / i18;
            i6 = 0;
        } else {
            i6 = 0;
            i5 = 0;
        }
        int max = Math.max(i6, i5);
        if (isLayoutRtl) {
            int width = getWidth() - getPaddingRight();
            while (i6 < childCount) {
                View childAt3 = getChildAt(i6);
                LayoutParams layoutParams2 = (LayoutParams) childAt3.getLayoutParams();
                if (childAt3.getVisibility() != 8 && !layoutParams2.isOverflowButton) {
                    int i19 = width - layoutParams2.rightMargin;
                    int measuredWidth3 = childAt3.getMeasuredWidth();
                    int measuredHeight3 = childAt3.getMeasuredHeight();
                    int i20 = i9 - (measuredHeight3 / 2);
                    childAt3.layout(i19 - measuredWidth3, i20, i19, measuredHeight3 + i20);
                    width = i19 - ((measuredWidth3 + layoutParams2.leftMargin) + max);
                }
                i6++;
            }
        } else {
            int paddingLeft = getPaddingLeft();
            while (i6 < childCount) {
                View childAt4 = getChildAt(i6);
                LayoutParams layoutParams3 = (LayoutParams) childAt4.getLayoutParams();
                if (childAt4.getVisibility() != 8 && !layoutParams3.isOverflowButton) {
                    int i21 = paddingLeft + layoutParams3.leftMargin;
                    int measuredWidth4 = childAt4.getMeasuredWidth();
                    int measuredHeight4 = childAt4.getMeasuredHeight();
                    int i22 = i9 - (measuredHeight4 / 2);
                    childAt4.layout(i21, i22, i21 + measuredWidth4, measuredHeight4 + i22);
                    paddingLeft = i21 + measuredWidth4 + layoutParams3.rightMargin + max;
                }
                i6++;
            }
        }
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        dismissPopupMenus();
    }

    public void setOverflowIcon(@Nullable Drawable drawable) {
        getMenu();
        this.mPresenter.setOverflowIcon(drawable);
    }

    @Nullable
    public Drawable getOverflowIcon() {
        getMenu();
        return this.mPresenter.getOverflowIcon();
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public boolean isOverflowReserved() {
        return this.mReserveOverflow;
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public void setOverflowReserved(boolean z) {
        this.mReserveOverflow = z;
    }

    /* access modifiers changed from: protected */
    public LayoutParams generateDefaultLayoutParams() {
        LayoutParams layoutParams = new LayoutParams(-2, -2);
        layoutParams.gravity = 16;
        return layoutParams;
    }

    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    /* access modifiers changed from: protected */
    public LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
        if (layoutParams == null) {
            return generateDefaultLayoutParams();
        }
        LayoutParams layoutParams2 = layoutParams instanceof LayoutParams ? new LayoutParams((LayoutParams) layoutParams) : new LayoutParams(layoutParams);
        if (layoutParams2.gravity <= 0) {
            layoutParams2.gravity = 16;
        }
        return layoutParams2;
    }

    /* access modifiers changed from: protected */
    public boolean checkLayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
        return layoutParams != null && (layoutParams instanceof LayoutParams);
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public LayoutParams generateOverflowButtonLayoutParams() {
        LayoutParams generateDefaultLayoutParams = generateDefaultLayoutParams();
        generateDefaultLayoutParams.isOverflowButton = true;
        return generateDefaultLayoutParams;
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public boolean invokeItem(MenuItemImpl menuItemImpl) {
        return this.mMenu.performItemAction(menuItemImpl, 0);
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public void initialize(MenuBuilder menuBuilder) {
        this.mMenu = menuBuilder;
    }

    public Menu getMenu() {
        if (this.mMenu == null) {
            Context context = getContext();
            this.mMenu = new MenuBuilder(context);
            this.mMenu.setCallback(new MenuBuilderCallback());
            this.mPresenter = new ActionMenuPresenter(context);
            this.mPresenter.setReserveOverflow(true);
            this.mPresenter.setCallback(this.mActionMenuPresenterCallback != null ? this.mActionMenuPresenterCallback : new ActionMenuPresenterCallback());
            this.mMenu.addMenuPresenter(this.mPresenter, this.mPopupContext);
            this.mPresenter.setMenuView(this);
        }
        return this.mMenu;
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public void setMenuCallbacks(Callback callback, MenuBuilder.Callback callback2) {
        this.mActionMenuPresenterCallback = callback;
        this.mMenuBuilderCallback = callback2;
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public MenuBuilder peekMenu() {
        return this.mMenu;
    }

    public boolean showOverflowMenu() {
        return this.mPresenter != null && this.mPresenter.showOverflowMenu();
    }

    public boolean hideOverflowMenu() {
        return this.mPresenter != null && this.mPresenter.hideOverflowMenu();
    }

    public boolean isOverflowMenuShowing() {
        return this.mPresenter != null && this.mPresenter.isOverflowMenuShowing();
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public boolean isOverflowMenuShowPending() {
        return this.mPresenter != null && this.mPresenter.isOverflowMenuShowPending();
    }

    public void dismissPopupMenus() {
        if (this.mPresenter != null) {
            this.mPresenter.dismissPopupMenus();
        }
    }

    /* access modifiers changed from: protected */
    @RestrictTo({Scope.LIBRARY_GROUP})
    public boolean hasSupportDividerBeforeChildAt(int i) {
        boolean z = false;
        if (i == 0) {
            return false;
        }
        View childAt = getChildAt(i - 1);
        View childAt2 = getChildAt(i);
        if (i < getChildCount() && (childAt instanceof ActionMenuChildView)) {
            z = false | ((ActionMenuChildView) childAt).needsDividerAfter();
        }
        if (i > 0 && (childAt2 instanceof ActionMenuChildView)) {
            z |= ((ActionMenuChildView) childAt2).needsDividerBefore();
        }
        return z;
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public void setExpandedActionViewsExclusive(boolean z) {
        this.mPresenter.setExpandedActionViewsExclusive(z);
    }
}
