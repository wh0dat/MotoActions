package android.support.p001v4.widget;

import android.os.Build.VERSION;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ListPopupWindow;

/* renamed from: android.support.v4.widget.ListPopupWindowCompat */
public final class ListPopupWindowCompat {
    private ListPopupWindowCompat() {
    }

    @Deprecated
    public static OnTouchListener createDragToOpenListener(Object obj, View view) {
        return createDragToOpenListener((ListPopupWindow) obj, view);
    }

    public static OnTouchListener createDragToOpenListener(ListPopupWindow listPopupWindow, View view) {
        if (VERSION.SDK_INT >= 19) {
            return listPopupWindow.createDragToOpenListener(view);
        }
        return null;
    }
}
