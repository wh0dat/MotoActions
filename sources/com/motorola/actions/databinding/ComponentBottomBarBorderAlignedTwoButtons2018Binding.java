package com.motorola.actions.databinding;

import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.motorola.actions.C0504R;

public class ComponentBottomBarBorderAlignedTwoButtons2018Binding extends ViewDataBinding {
    @Nullable
    private static final IncludedLayouts sIncludes = null;
    @Nullable
    private static final SparseIntArray sViewsWithIds = null;
    @NonNull
    public final FrameLayout frameLeftButton;
    @NonNull
    public final FrameLayout frameRightButton;
    @NonNull
    public final RelativeLayout horizontalButtons;
    @NonNull
    public final TextView leftBtn;
    private long mDirtyFlags = -1;
    @Nullable
    private int mLeftText;
    @Nullable
    private OnClickListener mOnLeftClick;
    @Nullable
    private OnClickListener mOnRightClick;
    @Nullable
    private int mRightText;
    @NonNull
    public final Button rightBtn;

    /* access modifiers changed from: protected */
    public boolean onFieldChange(int i, Object obj, int i2) {
        return false;
    }

    public ComponentBottomBarBorderAlignedTwoButtons2018Binding(@NonNull DataBindingComponent dataBindingComponent, @NonNull View view) {
        super(dataBindingComponent, view, 0);
        Object[] mapBindings = mapBindings(dataBindingComponent, view, 5, sIncludes, sViewsWithIds);
        this.frameLeftButton = (FrameLayout) mapBindings[1];
        this.frameLeftButton.setTag(null);
        this.frameRightButton = (FrameLayout) mapBindings[3];
        this.frameRightButton.setTag(null);
        this.horizontalButtons = (RelativeLayout) mapBindings[0];
        this.horizontalButtons.setTag(null);
        this.leftBtn = (TextView) mapBindings[2];
        this.leftBtn.setTag(null);
        this.rightBtn = (Button) mapBindings[4];
        this.rightBtn.setTag(null);
        setRootTag(view);
        invalidateAll();
    }

    public void invalidateAll() {
        synchronized (this) {
            this.mDirtyFlags = 16;
        }
        requestRebind();
    }

    public boolean hasPendingBindings() {
        synchronized (this) {
            if (this.mDirtyFlags != 0) {
                return true;
            }
            return false;
        }
    }

    public boolean setVariable(int i, @Nullable Object obj) {
        if (1 == i) {
            setLeftText(((Integer) obj).intValue());
            return true;
        } else if (2 == i) {
            setOnLeftClick((OnClickListener) obj);
            return true;
        } else if (5 == i) {
            setRightText(((Integer) obj).intValue());
            return true;
        } else if (3 != i) {
            return false;
        } else {
            setOnRightClick((OnClickListener) obj);
            return true;
        }
    }

    public void setLeftText(int i) {
        this.mLeftText = i;
        synchronized (this) {
            this.mDirtyFlags |= 1;
        }
        notifyPropertyChanged(1);
        super.requestRebind();
    }

    public int getLeftText() {
        return this.mLeftText;
    }

    public void setOnLeftClick(@Nullable OnClickListener onClickListener) {
        this.mOnLeftClick = onClickListener;
        synchronized (this) {
            this.mDirtyFlags |= 2;
        }
        notifyPropertyChanged(2);
        super.requestRebind();
    }

    @Nullable
    public OnClickListener getOnLeftClick() {
        return this.mOnLeftClick;
    }

    public void setRightText(int i) {
        this.mRightText = i;
        synchronized (this) {
            this.mDirtyFlags |= 4;
        }
        notifyPropertyChanged(5);
        super.requestRebind();
    }

    public int getRightText() {
        return this.mRightText;
    }

    public void setOnRightClick(@Nullable OnClickListener onClickListener) {
        this.mOnRightClick = onClickListener;
        synchronized (this) {
            this.mDirtyFlags |= 8;
        }
        notifyPropertyChanged(3);
        super.requestRebind();
    }

    @Nullable
    public OnClickListener getOnRightClick() {
        return this.mOnRightClick;
    }

    /* access modifiers changed from: protected */
    public void executeBindings() {
        long j;
        synchronized (this) {
            j = this.mDirtyFlags;
            this.mDirtyFlags = 0;
        }
        int i = this.mLeftText;
        OnClickListener onClickListener = this.mOnLeftClick;
        int i2 = this.mRightText;
        OnClickListener onClickListener2 = this.mOnRightClick;
        int i3 = ((17 & j) > 0 ? 1 : ((17 & j) == 0 ? 0 : -1));
        int i4 = ((18 & j) > 0 ? 1 : ((18 & j) == 0 ? 0 : -1));
        int i5 = ((20 & j) > 0 ? 1 : ((20 & j) == 0 ? 0 : -1));
        int i6 = ((j & 24) > 0 ? 1 : ((j & 24) == 0 ? 0 : -1));
        if (i4 != 0) {
            this.frameLeftButton.setOnClickListener(onClickListener);
        }
        if (i6 != 0) {
            this.frameRightButton.setOnClickListener(onClickListener2);
        }
        if (i3 != 0) {
            this.leftBtn.setText(i);
        }
        if (i5 != 0) {
            this.rightBtn.setText(i2);
        }
    }

    @NonNull
    public static ComponentBottomBarBorderAlignedTwoButtons2018Binding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ComponentBottomBarBorderAlignedTwoButtons2018Binding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (ComponentBottomBarBorderAlignedTwoButtons2018Binding) DataBindingUtil.inflate(layoutInflater, C0504R.layout.component_bottom_bar_border_aligned_two_buttons_2018, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static ComponentBottomBarBorderAlignedTwoButtons2018Binding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ComponentBottomBarBorderAlignedTwoButtons2018Binding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return bind(layoutInflater.inflate(C0504R.layout.component_bottom_bar_border_aligned_two_buttons_2018, null, false), dataBindingComponent);
    }

    @NonNull
    public static ComponentBottomBarBorderAlignedTwoButtons2018Binding bind(@NonNull View view) {
        return bind(view, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ComponentBottomBarBorderAlignedTwoButtons2018Binding bind(@NonNull View view, @Nullable DataBindingComponent dataBindingComponent) {
        if ("layout/component_bottom_bar_border_aligned_two_buttons_2018_0".equals(view.getTag())) {
            return new ComponentBottomBarBorderAlignedTwoButtons2018Binding(dataBindingComponent, view);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("view tag isn't correct on view:");
        sb.append(view.getTag());
        throw new RuntimeException(sb.toString());
    }
}
