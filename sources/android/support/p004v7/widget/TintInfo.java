package android.support.p004v7.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;

/* renamed from: android.support.v7.widget.TintInfo */
class TintInfo {
    public boolean mHasTintList;
    public boolean mHasTintMode;
    public ColorStateList mTintList;
    public Mode mTintMode;

    TintInfo() {
    }

    /* access modifiers changed from: 0000 */
    public void clear() {
        this.mTintList = null;
        this.mHasTintList = false;
        this.mTintMode = null;
        this.mHasTintMode = false;
    }
}
