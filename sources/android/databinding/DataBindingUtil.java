package android.databinding;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

public class DataBindingUtil {
    private static DataBindingComponent sDefaultComponent;
    private static DataBinderMapper sMapper = new DataBinderMapperImpl();

    private DataBindingUtil() {
    }

    public static void setDefaultComponent(@Nullable DataBindingComponent dataBindingComponent) {
        sDefaultComponent = dataBindingComponent;
    }

    @Nullable
    public static DataBindingComponent getDefaultComponent() {
        return sDefaultComponent;
    }

    public static <T extends ViewDataBinding> T inflate(@NonNull LayoutInflater layoutInflater, int i, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, i, viewGroup, z, sDefaultComponent);
    }

    public static <T extends ViewDataBinding> T inflate(@NonNull LayoutInflater layoutInflater, int i, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        int i2 = 0;
        boolean z2 = viewGroup != null && z;
        if (z2) {
            i2 = viewGroup.getChildCount();
        }
        View inflate = layoutInflater.inflate(i, viewGroup, z);
        if (z2) {
            return bindToAddedViews(dataBindingComponent, viewGroup, i2, i);
        }
        return bind(dataBindingComponent, inflate, i);
    }

    @Nullable
    public static <T extends ViewDataBinding> T bind(@NonNull View view) {
        return bind(view, sDefaultComponent);
    }

    @Nullable
    public static <T extends ViewDataBinding> T bind(@NonNull View view, DataBindingComponent dataBindingComponent) {
        T binding = getBinding(view);
        if (binding != null) {
            return binding;
        }
        Object tag = view.getTag();
        if (!(tag instanceof String)) {
            throw new IllegalArgumentException("View is not a binding layout");
        }
        int layoutId = sMapper.getLayoutId((String) tag);
        if (layoutId != 0) {
            return sMapper.getDataBinder(dataBindingComponent, view, layoutId);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("View is not a binding layout. Tag: ");
        sb.append(tag);
        throw new IllegalArgumentException(sb.toString());
    }

    static <T extends ViewDataBinding> T bind(DataBindingComponent dataBindingComponent, View[] viewArr, int i) {
        return sMapper.getDataBinder(dataBindingComponent, viewArr, i);
    }

    static <T extends ViewDataBinding> T bind(DataBindingComponent dataBindingComponent, View view, int i) {
        return sMapper.getDataBinder(dataBindingComponent, view, i);
    }

    @Nullable
    public static <T extends ViewDataBinding> T findBinding(@NonNull View view) {
        while (view != null) {
            T binding = ViewDataBinding.getBinding(view);
            if (binding != null) {
                return binding;
            }
            Object tag = view.getTag();
            if (tag instanceof String) {
                String str = (String) tag;
                if (str.startsWith("layout") && str.endsWith("_0")) {
                    char charAt = str.charAt(6);
                    int indexOf = str.indexOf(47, 7);
                    boolean z = false;
                    if (charAt != '/' ? charAt == '-' && indexOf != -1 && str.indexOf(47, indexOf + 1) == -1 : indexOf == -1) {
                        z = true;
                    }
                    if (z) {
                        return null;
                    }
                }
            }
            ViewParent parent = view.getParent();
            view = parent instanceof View ? (View) parent : null;
        }
        return null;
    }

    @Nullable
    public static <T extends ViewDataBinding> T getBinding(@NonNull View view) {
        return ViewDataBinding.getBinding(view);
    }

    public static <T extends ViewDataBinding> T setContentView(@NonNull Activity activity, int i) {
        return setContentView(activity, i, sDefaultComponent);
    }

    public static <T extends ViewDataBinding> T setContentView(@NonNull Activity activity, int i, @Nullable DataBindingComponent dataBindingComponent) {
        activity.setContentView(i);
        return bindToAddedViews(dataBindingComponent, (ViewGroup) activity.getWindow().getDecorView().findViewById(16908290), 0, i);
    }

    @Nullable
    public static String convertBrIdToString(int i) {
        return sMapper.convertBrIdToString(i);
    }

    private static <T extends ViewDataBinding> T bindToAddedViews(DataBindingComponent dataBindingComponent, ViewGroup viewGroup, int i, int i2) {
        int childCount = viewGroup.getChildCount();
        int i3 = childCount - i;
        if (i3 == 1) {
            return bind(dataBindingComponent, viewGroup.getChildAt(childCount - 1), i2);
        }
        View[] viewArr = new View[i3];
        for (int i4 = 0; i4 < i3; i4++) {
            viewArr[i4] = viewGroup.getChildAt(i4 + i);
        }
        return bind(dataBindingComponent, viewArr, i2);
    }
}
