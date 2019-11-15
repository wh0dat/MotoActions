package com.motorola.actions.databinding;

import android.arch.lifecycle.LifecycleOwner;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.motorola.actions.C0504R;
import com.motorola.actions.p013ui.tutorial.quickScreenshot.welcome.QuickScreenshotWelcomePresenter;

public class ActivityWelcomeQuickscreenshotBinding extends ViewDataBinding {
    @Nullable
    private static final IncludedLayouts sIncludes = new IncludedLayouts(5);
    @Nullable
    private static final SparseIntArray sViewsWithIds = new SparseIntArray();
    @Nullable
    public final ComponentBottomBarBorderAlignedTwoButtonsBinding bottomBar;
    private long mDirtyFlags = -1;
    @Nullable
    private QuickScreenshotWelcomePresenter mPresenter;
    @NonNull
    public final RelativeLayout welcomeRoot;
    @NonNull
    public final TextView welcomeTextDescription;
    @NonNull
    public final TextView welcomeTextTitle;
    @NonNull
    public final TextureView welcomeVideo;

    static {
        sIncludes.setIncludes(0, new String[]{"component_bottom_bar_border_aligned_two_buttons"}, new int[]{3}, new int[]{C0504R.layout.component_bottom_bar_border_aligned_two_buttons});
        sViewsWithIds.put(C0504R.C0506id.welcome_video, 4);
    }

    public ActivityWelcomeQuickscreenshotBinding(@NonNull DataBindingComponent dataBindingComponent, @NonNull View view) {
        super(dataBindingComponent, view, 1);
        Object[] mapBindings = mapBindings(dataBindingComponent, view, 5, sIncludes, sViewsWithIds);
        this.bottomBar = (ComponentBottomBarBorderAlignedTwoButtonsBinding) mapBindings[3];
        setContainedBinding(this.bottomBar);
        this.welcomeRoot = (RelativeLayout) mapBindings[0];
        this.welcomeRoot.setTag(null);
        this.welcomeTextDescription = (TextView) mapBindings[2];
        this.welcomeTextDescription.setTag(null);
        this.welcomeTextTitle = (TextView) mapBindings[1];
        this.welcomeTextTitle.setTag(null);
        this.welcomeVideo = (TextureView) mapBindings[4];
        setRootTag(view);
        invalidateAll();
    }

    public void invalidateAll() {
        synchronized (this) {
            this.mDirtyFlags = 4;
        }
        this.bottomBar.invalidateAll();
        requestRebind();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0017, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0013, code lost:
        if (r4.bottomBar.hasPendingBindings() == false) goto L_0x0016;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0015, code lost:
        return true;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean hasPendingBindings() {
        /*
            r4 = this;
            monitor-enter(r4)
            long r0 = r4.mDirtyFlags     // Catch:{ all -> 0x0018 }
            r2 = 0
            int r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            r1 = 1
            if (r0 == 0) goto L_0x000c
            monitor-exit(r4)     // Catch:{ all -> 0x0018 }
            return r1
        L_0x000c:
            monitor-exit(r4)     // Catch:{ all -> 0x0018 }
            com.motorola.actions.databinding.ComponentBottomBarBorderAlignedTwoButtonsBinding r4 = r4.bottomBar
            boolean r4 = r4.hasPendingBindings()
            if (r4 == 0) goto L_0x0016
            return r1
        L_0x0016:
            r4 = 0
            return r4
        L_0x0018:
            r0 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x0018 }
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.motorola.actions.databinding.ActivityWelcomeQuickscreenshotBinding.hasPendingBindings():boolean");
    }

    public boolean setVariable(int i, @Nullable Object obj) {
        if (4 != i) {
            return false;
        }
        setPresenter((QuickScreenshotWelcomePresenter) obj);
        return true;
    }

    public void setPresenter(@Nullable QuickScreenshotWelcomePresenter quickScreenshotWelcomePresenter) {
        this.mPresenter = quickScreenshotWelcomePresenter;
        synchronized (this) {
            this.mDirtyFlags |= 2;
        }
        notifyPropertyChanged(4);
        super.requestRebind();
    }

    @Nullable
    public QuickScreenshotWelcomePresenter getPresenter() {
        return this.mPresenter;
    }

    public void setLifecycleOwner(@Nullable LifecycleOwner lifecycleOwner) {
        super.setLifecycleOwner(lifecycleOwner);
        this.bottomBar.setLifecycleOwner(lifecycleOwner);
    }

    /* access modifiers changed from: protected */
    public boolean onFieldChange(int i, Object obj, int i2) {
        if (i != 0) {
            return false;
        }
        return onChangeBottomBar((ComponentBottomBarBorderAlignedTwoButtonsBinding) obj, i2);
    }

    private boolean onChangeBottomBar(ComponentBottomBarBorderAlignedTwoButtonsBinding componentBottomBarBorderAlignedTwoButtonsBinding, int i) {
        if (i != 0) {
            return false;
        }
        synchronized (this) {
            this.mDirtyFlags |= 1;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void executeBindings() {
        long j;
        int i;
        int i2;
        int i3;
        OnClickListener onClickListener;
        synchronized (this) {
            j = this.mDirtyFlags;
            this.mDirtyFlags = 0;
        }
        QuickScreenshotWelcomePresenter quickScreenshotWelcomePresenter = this.mPresenter;
        int i4 = ((j & 6) > 0 ? 1 : ((j & 6) == 0 ? 0 : -1));
        OnClickListener onClickListener2 = null;
        int i5 = 0;
        if (i4 == 0 || quickScreenshotWelcomePresenter == null) {
            onClickListener = null;
            i3 = 0;
            i2 = 0;
            i = 0;
        } else {
            OnClickListener onRightButtonClick = quickScreenshotWelcomePresenter.getOnRightButtonClick();
            int title = quickScreenshotWelcomePresenter.getTitle();
            int description = quickScreenshotWelcomePresenter.getDescription();
            i2 = quickScreenshotWelcomePresenter.getRightButtonText();
            int i6 = description;
            onClickListener = onRightButtonClick;
            onClickListener2 = quickScreenshotWelcomePresenter.getOnLeftButtonClick();
            i = title;
            i5 = quickScreenshotWelcomePresenter.getLeftButtonText();
            i3 = i6;
        }
        if (i4 != 0) {
            this.bottomBar.setLeftText(i5);
            this.bottomBar.setRightText(i2);
            this.bottomBar.setOnLeftClick(onClickListener2);
            this.bottomBar.setOnRightClick(onClickListener);
            this.welcomeTextDescription.setText(i3);
            this.welcomeTextTitle.setText(i);
        }
        executeBindingsOn(this.bottomBar);
    }

    @NonNull
    public static ActivityWelcomeQuickscreenshotBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityWelcomeQuickscreenshotBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityWelcomeQuickscreenshotBinding) DataBindingUtil.inflate(layoutInflater, C0504R.layout.activity_welcome_quickscreenshot, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static ActivityWelcomeQuickscreenshotBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityWelcomeQuickscreenshotBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return bind(layoutInflater.inflate(C0504R.layout.activity_welcome_quickscreenshot, null, false), dataBindingComponent);
    }

    @NonNull
    public static ActivityWelcomeQuickscreenshotBinding bind(@NonNull View view) {
        return bind(view, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityWelcomeQuickscreenshotBinding bind(@NonNull View view, @Nullable DataBindingComponent dataBindingComponent) {
        if ("layout/activity_welcome_quickscreenshot_0".equals(view.getTag())) {
            return new ActivityWelcomeQuickscreenshotBinding(dataBindingComponent, view);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("view tag isn't correct on view:");
        sb.append(view.getTag());
        throw new RuntimeException(sb.toString());
    }
}
