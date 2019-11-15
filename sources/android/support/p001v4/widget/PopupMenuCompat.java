package android.support.p001v4.widget;

import android.os.Build.VERSION;
import android.view.View.OnTouchListener;
import android.widget.PopupMenu;

/* renamed from: android.support.v4.widget.PopupMenuCompat */
public final class PopupMenuCompat {
    private PopupMenuCompat() {
    }

    public static OnTouchListener getDragToOpenListener(Object obj) {
        if (VERSION.SDK_INT >= 19) {
            return ((PopupMenu) obj).getDragToOpenListener();
        }
        return null;
    }
}
