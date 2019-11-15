package android.support.p004v7.view.menu;

import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.widget.ListView;

@RestrictTo({Scope.LIBRARY_GROUP})
/* renamed from: android.support.v7.view.menu.ShowableListMenu */
public interface ShowableListMenu {
    void dismiss();

    ListView getListView();

    boolean isShowing();

    void show();
}
