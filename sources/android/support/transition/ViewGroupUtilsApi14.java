package android.support.transition;

import android.animation.LayoutTransition;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.ViewGroup;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@RequiresApi(14)
class ViewGroupUtilsApi14 implements ViewGroupUtilsImpl {
    private static final int LAYOUT_TRANSITION_CHANGING = 4;
    private static final String TAG = "ViewGroupUtilsApi14";
    private static Method sCancelMethod;
    private static boolean sCancelMethodFetched;
    private static LayoutTransition sEmptyLayoutTransition;
    private static Field sLayoutSuppressedField;
    private static boolean sLayoutSuppressedFieldFetched;

    ViewGroupUtilsApi14() {
    }

    public ViewGroupOverlayImpl getOverlay(@NonNull ViewGroup viewGroup) {
        return ViewGroupOverlayApi14.createFrom(viewGroup);
    }

    /* JADX WARNING: Removed duplicated region for block: B:34:0x0089  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x0096  */
    /* JADX WARNING: Removed duplicated region for block: B:40:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void suppressLayout(@android.support.annotation.NonNull android.view.ViewGroup r5, boolean r6) {
        /*
            r4 = this;
            android.animation.LayoutTransition r0 = sEmptyLayoutTransition
            r1 = 1
            r2 = 0
            r3 = 0
            if (r0 != 0) goto L_0x002a
            android.support.transition.ViewGroupUtilsApi14$1 r0 = new android.support.transition.ViewGroupUtilsApi14$1
            r0.<init>()
            sEmptyLayoutTransition = r0
            android.animation.LayoutTransition r4 = sEmptyLayoutTransition
            r0 = 2
            r4.setAnimator(r0, r3)
            android.animation.LayoutTransition r4 = sEmptyLayoutTransition
            r4.setAnimator(r2, r3)
            android.animation.LayoutTransition r4 = sEmptyLayoutTransition
            r4.setAnimator(r1, r3)
            android.animation.LayoutTransition r4 = sEmptyLayoutTransition
            r0 = 3
            r4.setAnimator(r0, r3)
            android.animation.LayoutTransition r4 = sEmptyLayoutTransition
            r0 = 4
            r4.setAnimator(r0, r3)
        L_0x002a:
            if (r6 == 0) goto L_0x004a
            android.animation.LayoutTransition r4 = r5.getLayoutTransition()
            if (r4 == 0) goto L_0x0044
            boolean r6 = r4.isRunning()
            if (r6 == 0) goto L_0x003b
            cancelLayoutTransition(r4)
        L_0x003b:
            android.animation.LayoutTransition r6 = sEmptyLayoutTransition
            if (r4 == r6) goto L_0x0044
            int r6 = android.support.transition.C0164R.C0166id.transition_layout_save
            r5.setTag(r6, r4)
        L_0x0044:
            android.animation.LayoutTransition r4 = sEmptyLayoutTransition
            r5.setLayoutTransition(r4)
            goto L_0x009e
        L_0x004a:
            r5.setLayoutTransition(r3)
            boolean r4 = sLayoutSuppressedFieldFetched
            if (r4 != 0) goto L_0x006a
            java.lang.Class<android.view.ViewGroup> r4 = android.view.ViewGroup.class
            java.lang.String r6 = "mLayoutSuppressed"
            java.lang.reflect.Field r4 = r4.getDeclaredField(r6)     // Catch:{ NoSuchFieldException -> 0x0061 }
            sLayoutSuppressedField = r4     // Catch:{ NoSuchFieldException -> 0x0061 }
            java.lang.reflect.Field r4 = sLayoutSuppressedField     // Catch:{ NoSuchFieldException -> 0x0061 }
            r4.setAccessible(r1)     // Catch:{ NoSuchFieldException -> 0x0061 }
            goto L_0x0068
        L_0x0061:
            java.lang.String r4 = "ViewGroupUtilsApi14"
            java.lang.String r6 = "Failed to access mLayoutSuppressed field by reflection"
            android.util.Log.i(r4, r6)
        L_0x0068:
            sLayoutSuppressedFieldFetched = r1
        L_0x006a:
            java.lang.reflect.Field r4 = sLayoutSuppressedField
            if (r4 == 0) goto L_0x0087
            java.lang.reflect.Field r4 = sLayoutSuppressedField     // Catch:{ IllegalAccessException -> 0x0080 }
            boolean r4 = r4.getBoolean(r5)     // Catch:{ IllegalAccessException -> 0x0080 }
            if (r4 == 0) goto L_0x007e
            java.lang.reflect.Field r6 = sLayoutSuppressedField     // Catch:{ IllegalAccessException -> 0x007c }
            r6.setBoolean(r5, r2)     // Catch:{ IllegalAccessException -> 0x007c }
            goto L_0x007e
        L_0x007c:
            r2 = r4
            goto L_0x0080
        L_0x007e:
            r2 = r4
            goto L_0x0087
        L_0x0080:
            java.lang.String r4 = "ViewGroupUtilsApi14"
            java.lang.String r6 = "Failed to get mLayoutSuppressed field by reflection"
            android.util.Log.i(r4, r6)
        L_0x0087:
            if (r2 == 0) goto L_0x008c
            r5.requestLayout()
        L_0x008c:
            int r4 = android.support.transition.C0164R.C0166id.transition_layout_save
            java.lang.Object r4 = r5.getTag(r4)
            android.animation.LayoutTransition r4 = (android.animation.LayoutTransition) r4
            if (r4 == 0) goto L_0x009e
            int r6 = android.support.transition.C0164R.C0166id.transition_layout_save
            r5.setTag(r6, r3)
            r5.setLayoutTransition(r4)
        L_0x009e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.transition.ViewGroupUtilsApi14.suppressLayout(android.view.ViewGroup, boolean):void");
    }

    private static void cancelLayoutTransition(LayoutTransition layoutTransition) {
        if (!sCancelMethodFetched) {
            try {
                sCancelMethod = LayoutTransition.class.getDeclaredMethod("cancel", new Class[0]);
                sCancelMethod.setAccessible(true);
            } catch (NoSuchMethodException unused) {
                Log.i(TAG, "Failed to access cancel method by reflection");
            }
            sCancelMethodFetched = true;
        }
        if (sCancelMethod != null) {
            try {
                sCancelMethod.invoke(layoutTransition, new Object[0]);
            } catch (IllegalAccessException unused2) {
                Log.i(TAG, "Failed to access cancel method by reflection");
            } catch (InvocationTargetException unused3) {
                Log.i(TAG, "Failed to invoke cancel method by reflection");
            }
        }
    }
}
