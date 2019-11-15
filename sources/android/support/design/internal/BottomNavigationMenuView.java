package android.support.design.internal;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.design.C0067R;
import android.support.p001v4.util.Pools.Pool;
import android.support.p001v4.util.Pools.SynchronizedPool;
import android.support.p001v4.view.ViewCompat;
import android.support.p001v4.view.animation.FastOutSlowInInterpolator;
import android.support.p004v7.view.menu.MenuBuilder;
import android.support.p004v7.view.menu.MenuItemImpl;
import android.support.p004v7.view.menu.MenuView;
import android.support.transition.AutoTransition;
import android.support.transition.TransitionManager;
import android.support.transition.TransitionSet;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.google.android.gms.common.util.CrashUtils.ErrorDialogData;

@RestrictTo({Scope.LIBRARY_GROUP})
public class BottomNavigationMenuView extends ViewGroup implements MenuView {
    private static final long ACTIVE_ANIMATION_DURATION_MS = 115;
    private final int mActiveItemMaxWidth;
    private BottomNavigationItemView[] mButtons;
    private final int mInactiveItemMaxWidth;
    private final int mInactiveItemMinWidth;
    private int mItemBackgroundRes;
    private final int mItemHeight;
    private ColorStateList mItemIconTint;
    private final Pool<BottomNavigationItemView> mItemPool;
    private ColorStateList mItemTextColor;
    /* access modifiers changed from: private */
    public MenuBuilder mMenu;
    private final OnClickListener mOnClickListener;
    /* access modifiers changed from: private */
    public BottomNavigationPresenter mPresenter;
    private int mSelectedItemId;
    private int mSelectedItemPosition;
    private final TransitionSet mSet;
    private boolean mShiftingMode;
    private int[] mTempChildWidths;

    public int getWindowAnimations() {
        return 0;
    }

    public BottomNavigationMenuView(Context context) {
        this(context, null);
    }

    public BottomNavigationMenuView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mItemPool = new SynchronizedPool(5);
        this.mShiftingMode = true;
        this.mSelectedItemId = 0;
        this.mSelectedItemPosition = 0;
        Resources resources = getResources();
        this.mInactiveItemMaxWidth = resources.getDimensionPixelSize(C0067R.dimen.design_bottom_navigation_item_max_width);
        this.mInactiveItemMinWidth = resources.getDimensionPixelSize(C0067R.dimen.design_bottom_navigation_item_min_width);
        this.mActiveItemMaxWidth = resources.getDimensionPixelSize(C0067R.dimen.design_bottom_navigation_active_item_max_width);
        this.mItemHeight = resources.getDimensionPixelSize(C0067R.dimen.design_bottom_navigation_height);
        this.mSet = new AutoTransition();
        this.mSet.setOrdering(0);
        this.mSet.setDuration((long) ACTIVE_ANIMATION_DURATION_MS);
        this.mSet.setInterpolator((TimeInterpolator) new FastOutSlowInInterpolator());
        this.mSet.addTransition(new TextScale());
        this.mOnClickListener = new OnClickListener() {
            public void onClick(View view) {
                MenuItemImpl itemData = ((BottomNavigationItemView) view).getItemData();
                if (!BottomNavigationMenuView.this.mMenu.performItemAction(itemData, BottomNavigationMenuView.this.mPresenter, 0)) {
                    itemData.setChecked(true);
                }
            }
        };
        this.mTempChildWidths = new int[5];
    }

    public void initialize(MenuBuilder menuBuilder) {
        this.mMenu = menuBuilder;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int size = MeasureSpec.getSize(i);
        int childCount = getChildCount();
        int makeMeasureSpec = MeasureSpec.makeMeasureSpec(this.mItemHeight, ErrorDialogData.SUPPRESSED);
        if (this.mShiftingMode) {
            int i3 = childCount - 1;
            int min = Math.min(size - (this.mInactiveItemMinWidth * i3), this.mActiveItemMaxWidth);
            int i4 = size - min;
            int min2 = Math.min(i4 / i3, this.mInactiveItemMaxWidth);
            int i5 = i4 - (i3 * min2);
            int i6 = 0;
            while (i6 < childCount) {
                this.mTempChildWidths[i6] = i6 == this.mSelectedItemPosition ? min : min2;
                if (i5 > 0) {
                    int[] iArr = this.mTempChildWidths;
                    iArr[i6] = iArr[i6] + 1;
                    i5--;
                }
                i6++;
            }
        } else {
            int min3 = Math.min(size / (childCount == 0 ? 1 : childCount), this.mActiveItemMaxWidth);
            int i7 = size - (min3 * childCount);
            for (int i8 = 0; i8 < childCount; i8++) {
                this.mTempChildWidths[i8] = min3;
                if (i7 > 0) {
                    int[] iArr2 = this.mTempChildWidths;
                    iArr2[i8] = iArr2[i8] + 1;
                    i7--;
                }
            }
        }
        int i9 = 0;
        for (int i10 = 0; i10 < childCount; i10++) {
            View childAt = getChildAt(i10);
            if (childAt.getVisibility() != 8) {
                childAt.measure(MeasureSpec.makeMeasureSpec(this.mTempChildWidths[i10], ErrorDialogData.SUPPRESSED), makeMeasureSpec);
                childAt.getLayoutParams().width = childAt.getMeasuredWidth();
                i9 += childAt.getMeasuredWidth();
            }
        }
        setMeasuredDimension(View.resolveSizeAndState(i9, MeasureSpec.makeMeasureSpec(i9, ErrorDialogData.SUPPRESSED), 0), View.resolveSizeAndState(this.mItemHeight, makeMeasureSpec, 0));
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int childCount = getChildCount();
        int i5 = i3 - i;
        int i6 = i4 - i2;
        int i7 = 0;
        for (int i8 = 0; i8 < childCount; i8++) {
            View childAt = getChildAt(i8);
            if (childAt.getVisibility() != 8) {
                if (ViewCompat.getLayoutDirection(this) == 1) {
                    int i9 = i5 - i7;
                    childAt.layout(i9 - childAt.getMeasuredWidth(), 0, i9, i6);
                } else {
                    childAt.layout(i7, 0, childAt.getMeasuredWidth() + i7, i6);
                }
                i7 += childAt.getMeasuredWidth();
            }
        }
    }

    public void setIconTintList(ColorStateList colorStateList) {
        this.mItemIconTint = colorStateList;
        if (this.mButtons != null) {
            for (BottomNavigationItemView iconTintList : this.mButtons) {
                iconTintList.setIconTintList(colorStateList);
            }
        }
    }

    @Nullable
    public ColorStateList getIconTintList() {
        return this.mItemIconTint;
    }

    public void setItemTextColor(ColorStateList colorStateList) {
        this.mItemTextColor = colorStateList;
        if (this.mButtons != null) {
            for (BottomNavigationItemView textColor : this.mButtons) {
                textColor.setTextColor(colorStateList);
            }
        }
    }

    public ColorStateList getItemTextColor() {
        return this.mItemTextColor;
    }

    public void setItemBackgroundRes(int i) {
        this.mItemBackgroundRes = i;
        if (this.mButtons != null) {
            for (BottomNavigationItemView itemBackground : this.mButtons) {
                itemBackground.setItemBackground(i);
            }
        }
    }

    public int getItemBackgroundRes() {
        return this.mItemBackgroundRes;
    }

    public void setPresenter(BottomNavigationPresenter bottomNavigationPresenter) {
        this.mPresenter = bottomNavigationPresenter;
    }

    public void buildMenuView() {
        removeAllViews();
        if (this.mButtons != null) {
            for (BottomNavigationItemView release : this.mButtons) {
                this.mItemPool.release(release);
            }
        }
        if (this.mMenu.size() == 0) {
            this.mSelectedItemId = 0;
            this.mSelectedItemPosition = 0;
            this.mButtons = null;
            return;
        }
        this.mButtons = new BottomNavigationItemView[this.mMenu.size()];
        this.mShiftingMode = this.mMenu.size() > 3;
        for (int i = 0; i < this.mMenu.size(); i++) {
            this.mPresenter.setUpdateSuspended(true);
            this.mMenu.getItem(i).setCheckable(true);
            this.mPresenter.setUpdateSuspended(false);
            BottomNavigationItemView newItem = getNewItem();
            this.mButtons[i] = newItem;
            newItem.setIconTintList(this.mItemIconTint);
            newItem.setTextColor(this.mItemTextColor);
            newItem.setItemBackground(this.mItemBackgroundRes);
            newItem.setShiftingMode(this.mShiftingMode);
            newItem.initialize((MenuItemImpl) this.mMenu.getItem(i), 0);
            newItem.setItemPosition(i);
            newItem.setOnClickListener(this.mOnClickListener);
            addView(newItem);
        }
        this.mSelectedItemPosition = Math.min(this.mMenu.size() - 1, this.mSelectedItemPosition);
        this.mMenu.getItem(this.mSelectedItemPosition).setChecked(true);
    }

    public void updateMenuView() {
        int size = this.mMenu.size();
        if (size != this.mButtons.length) {
            buildMenuView();
            return;
        }
        int i = this.mSelectedItemId;
        for (int i2 = 0; i2 < size; i2++) {
            MenuItem item = this.mMenu.getItem(i2);
            if (item.isChecked()) {
                this.mSelectedItemId = item.getItemId();
                this.mSelectedItemPosition = i2;
            }
        }
        if (i != this.mSelectedItemId) {
            TransitionManager.beginDelayedTransition(this, this.mSet);
        }
        for (int i3 = 0; i3 < size; i3++) {
            this.mPresenter.setUpdateSuspended(true);
            this.mButtons[i3].initialize((MenuItemImpl) this.mMenu.getItem(i3), 0);
            this.mPresenter.setUpdateSuspended(false);
        }
    }

    private BottomNavigationItemView getNewItem() {
        BottomNavigationItemView bottomNavigationItemView = (BottomNavigationItemView) this.mItemPool.acquire();
        return bottomNavigationItemView == null ? new BottomNavigationItemView(getContext()) : bottomNavigationItemView;
    }

    public int getSelectedItemId() {
        return this.mSelectedItemId;
    }

    /* access modifiers changed from: 0000 */
    public void tryRestoreSelectedItemId(int i) {
        int size = this.mMenu.size();
        for (int i2 = 0; i2 < size; i2++) {
            MenuItem item = this.mMenu.getItem(i2);
            if (i == item.getItemId()) {
                this.mSelectedItemId = i;
                this.mSelectedItemPosition = i2;
                item.setChecked(true);
                return;
            }
        }
    }
}
