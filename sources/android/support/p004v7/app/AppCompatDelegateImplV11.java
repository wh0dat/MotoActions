package android.support.p004v7.app;

import android.content.Context;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.Window;

@RequiresApi(14)
/* renamed from: android.support.v7.app.AppCompatDelegateImplV11 */
class AppCompatDelegateImplV11 extends AppCompatDelegateImplV9 {
    /* access modifiers changed from: 0000 */
    public View callActivityOnCreateView(View view, String str, Context context, AttributeSet attributeSet) {
        return null;
    }

    AppCompatDelegateImplV11(Context context, Window window, AppCompatCallback appCompatCallback) {
        super(context, window, appCompatCallback);
    }

    public boolean hasWindowFeature(int i) {
        return super.hasWindowFeature(i) || this.mWindow.hasFeature(i);
    }
}
