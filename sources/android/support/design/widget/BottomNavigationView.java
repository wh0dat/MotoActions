package android.support.design.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.C0067R;
import android.support.design.internal.BottomNavigationMenu;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.internal.BottomNavigationPresenter;
import android.support.p001v4.content.ContextCompat;
import android.support.p001v4.view.AbsSavedState;
import android.support.p001v4.view.ViewCompat;
import android.support.p004v7.appcompat.C0383R;
import android.support.p004v7.content.res.AppCompatResources;
import android.support.p004v7.view.SupportMenuInflater;
import android.support.p004v7.view.menu.MenuBuilder;
import android.support.p004v7.view.menu.MenuBuilder.Callback;
import android.support.p004v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

public class BottomNavigationView extends FrameLayout {
    private static final int[] CHECKED_STATE_SET = {16842912};
    private static final int[] DISABLED_STATE_SET = {-16842910};
    private static final int MENU_PRESENTER_ID = 1;
    private final MenuBuilder mMenu;
    private MenuInflater mMenuInflater;
    private final BottomNavigationMenuView mMenuView;
    private final BottomNavigationPresenter mPresenter;
    /* access modifiers changed from: private */
    public OnNavigationItemReselectedListener mReselectedListener;
    /* access modifiers changed from: private */
    public OnNavigationItemSelectedListener mSelectedListener;

    public interface OnNavigationItemReselectedListener {
        void onNavigationItemReselected(@NonNull MenuItem menuItem);
    }

    public interface OnNavigationItemSelectedListener {
        boolean onNavigationItemSelected(@NonNull MenuItem menuItem);
    }

    static class SavedState extends AbsSavedState {
        public static final Creator<SavedState> CREATOR = new ClassLoaderCreator<SavedState>() {
            public SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                return new SavedState(parcel, classLoader);
            }

            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel, null);
            }

            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        Bundle menuPresenterState;

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            readFromParcel(parcel, classLoader);
        }

        public void writeToParcel(@NonNull Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeBundle(this.menuPresenterState);
        }

        private void readFromParcel(Parcel parcel, ClassLoader classLoader) {
            this.menuPresenterState = parcel.readBundle(classLoader);
        }
    }

    public int getMaxItemCount() {
        return 5;
    }

    public BottomNavigationView(Context context) {
        this(context, null);
    }

    public BottomNavigationView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public BottomNavigationView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mPresenter = new BottomNavigationPresenter();
        ThemeUtils.checkAppCompatTheme(context);
        this.mMenu = new BottomNavigationMenu(context);
        this.mMenuView = new BottomNavigationMenuView(context);
        LayoutParams layoutParams = new LayoutParams(-2, -2);
        layoutParams.gravity = 17;
        this.mMenuView.setLayoutParams(layoutParams);
        this.mPresenter.setBottomNavigationMenuView(this.mMenuView);
        this.mPresenter.setId(1);
        this.mMenuView.setPresenter(this.mPresenter);
        this.mMenu.addMenuPresenter(this.mPresenter);
        this.mPresenter.initForMenu(getContext(), this.mMenu);
        TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, attributeSet, C0067R.styleable.BottomNavigationView, i, C0067R.style.Widget_Design_BottomNavigationView);
        if (obtainStyledAttributes.hasValue(C0067R.styleable.BottomNavigationView_itemIconTint)) {
            this.mMenuView.setIconTintList(obtainStyledAttributes.getColorStateList(C0067R.styleable.BottomNavigationView_itemIconTint));
        } else {
            this.mMenuView.setIconTintList(createDefaultColorStateList(16842808));
        }
        if (obtainStyledAttributes.hasValue(C0067R.styleable.BottomNavigationView_itemTextColor)) {
            this.mMenuView.setItemTextColor(obtainStyledAttributes.getColorStateList(C0067R.styleable.BottomNavigationView_itemTextColor));
        } else {
            this.mMenuView.setItemTextColor(createDefaultColorStateList(16842808));
        }
        if (obtainStyledAttributes.hasValue(C0067R.styleable.BottomNavigationView_elevation)) {
            ViewCompat.setElevation(this, (float) obtainStyledAttributes.getDimensionPixelSize(C0067R.styleable.BottomNavigationView_elevation, 0));
        }
        this.mMenuView.setItemBackgroundRes(obtainStyledAttributes.getResourceId(C0067R.styleable.BottomNavigationView_itemBackground, 0));
        if (obtainStyledAttributes.hasValue(C0067R.styleable.BottomNavigationView_menu)) {
            inflateMenu(obtainStyledAttributes.getResourceId(C0067R.styleable.BottomNavigationView_menu, 0));
        }
        obtainStyledAttributes.recycle();
        addView(this.mMenuView, layoutParams);
        if (VERSION.SDK_INT < 21) {
            addCompatibilityTopDivider(context);
        }
        this.mMenu.setCallback(new Callback() {
            public void onMenuModeChange(MenuBuilder menuBuilder) {
            }

            public boolean onMenuItemSelected(MenuBuilder menuBuilder, MenuItem menuItem) {
                boolean z = true;
                if (BottomNavigationView.this.mReselectedListener == null || menuItem.getItemId() != BottomNavigationView.this.getSelectedItemId()) {
                    if (BottomNavigationView.this.mSelectedListener == null || BottomNavigationView.this.mSelectedListener.onNavigationItemSelected(menuItem)) {
                        z = false;
                    }
                    return z;
                }
                BottomNavigationView.this.mReselectedListener.onNavigationItemReselected(menuItem);
                return true;
            }
        });
    }

    public void setOnNavigationItemSelectedListener(@Nullable OnNavigationItemSelectedListener onNavigationItemSelectedListener) {
        this.mSelectedListener = onNavigationItemSelectedListener;
    }

    public void setOnNavigationItemReselectedListener(@Nullable OnNavigationItemReselectedListener onNavigationItemReselectedListener) {
        this.mReselectedListener = onNavigationItemReselectedListener;
    }

    @NonNull
    public Menu getMenu() {
        return this.mMenu;
    }

    public void inflateMenu(int i) {
        this.mPresenter.setUpdateSuspended(true);
        getMenuInflater().inflate(i, this.mMenu);
        this.mPresenter.setUpdateSuspended(false);
        this.mPresenter.updateMenuView(true);
    }

    @Nullable
    public ColorStateList getItemIconTintList() {
        return this.mMenuView.getIconTintList();
    }

    public void setItemIconTintList(@Nullable ColorStateList colorStateList) {
        this.mMenuView.setIconTintList(colorStateList);
    }

    @Nullable
    public ColorStateList getItemTextColor() {
        return this.mMenuView.getItemTextColor();
    }

    public void setItemTextColor(@Nullable ColorStateList colorStateList) {
        this.mMenuView.setItemTextColor(colorStateList);
    }

    @DrawableRes
    public int getItemBackgroundResource() {
        return this.mMenuView.getItemBackgroundRes();
    }

    public void setItemBackgroundResource(@DrawableRes int i) {
        this.mMenuView.setItemBackgroundRes(i);
    }

    @IdRes
    public int getSelectedItemId() {
        return this.mMenuView.getSelectedItemId();
    }

    public void setSelectedItemId(@IdRes int i) {
        MenuItem findItem = this.mMenu.findItem(i);
        if (findItem != null && !this.mMenu.performItemAction(findItem, this.mPresenter, 0)) {
            findItem.setChecked(true);
        }
    }

    private void addCompatibilityTopDivider(Context context) {
        View view = new View(context);
        view.setBackgroundColor(ContextCompat.getColor(context, C0067R.color.design_bottom_navigation_shadow_color));
        view.setLayoutParams(new LayoutParams(-1, getResources().getDimensionPixelSize(C0067R.dimen.design_bottom_navigation_shadow_height)));
        addView(view);
    }

    private MenuInflater getMenuInflater() {
        if (this.mMenuInflater == null) {
            this.mMenuInflater = new SupportMenuInflater(getContext());
        }
        return this.mMenuInflater;
    }

    private ColorStateList createDefaultColorStateList(int i) {
        TypedValue typedValue = new TypedValue();
        if (!getContext().getTheme().resolveAttribute(i, typedValue, true)) {
            return null;
        }
        ColorStateList colorStateList = AppCompatResources.getColorStateList(getContext(), typedValue.resourceId);
        if (!getContext().getTheme().resolveAttribute(C0383R.attr.colorPrimary, typedValue, true)) {
            return null;
        }
        int i2 = typedValue.data;
        int defaultColor = colorStateList.getDefaultColor();
        return new ColorStateList(new int[][]{DISABLED_STATE_SET, CHECKED_STATE_SET, EMPTY_STATE_SET}, new int[]{colorStateList.getColorForState(DISABLED_STATE_SET, defaultColor), i2, defaultColor});
    }

    /* access modifiers changed from: protected */
    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.menuPresenterState = new Bundle();
        this.mMenu.savePresenterStates(savedState.menuPresenterState);
        return savedState;
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.mMenu.restorePresenterStates(savedState.menuPresenterState);
    }
}
