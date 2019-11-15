package android.support.p004v7.widget;

import android.content.Context;
import android.os.Build.VERSION;
import android.support.p001v4.view.ViewPropertyAnimatorCompat;
import android.support.p001v4.widget.ListViewAutoScrollHelper;
import android.support.p004v7.appcompat.C0383R;
import android.view.View;

/* renamed from: android.support.v7.widget.DropDownListView */
class DropDownListView extends ListViewCompat {
    private ViewPropertyAnimatorCompat mClickAnimation;
    private boolean mDrawsInPressedState;
    private boolean mHijackFocus;
    private boolean mListSelectionHidden;
    private ListViewAutoScrollHelper mScrollHelper;

    public DropDownListView(Context context, boolean z) {
        super(context, null, C0383R.attr.dropDownListViewStyle);
        this.mHijackFocus = z;
        setCacheColorHint(0);
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x004a  */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0060  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onForwardedEvent(android.view.MotionEvent r8, int r9) {
        /*
            r7 = this;
            int r0 = r8.getActionMasked()
            r1 = 0
            r2 = 1
            switch(r0) {
                case 1: goto L_0x0011;
                case 2: goto L_0x000f;
                case 3: goto L_0x000c;
                default: goto L_0x0009;
            }
        L_0x0009:
            r9 = r1
            r3 = r2
            goto L_0x0041
        L_0x000c:
            r9 = r1
            r3 = r9
            goto L_0x0041
        L_0x000f:
            r3 = r2
            goto L_0x0012
        L_0x0011:
            r3 = r1
        L_0x0012:
            int r9 = r8.findPointerIndex(r9)
            if (r9 >= 0) goto L_0x0019
            goto L_0x000c
        L_0x0019:
            float r4 = r8.getX(r9)
            int r4 = (int) r4
            float r9 = r8.getY(r9)
            int r9 = (int) r9
            int r5 = r7.pointToPosition(r4, r9)
            r6 = -1
            if (r5 != r6) goto L_0x002c
            r9 = r2
            goto L_0x0041
        L_0x002c:
            int r3 = r7.getFirstVisiblePosition()
            int r3 = r5 - r3
            android.view.View r3 = r7.getChildAt(r3)
            float r4 = (float) r4
            float r9 = (float) r9
            r7.setPressedItem(r3, r5, r4, r9)
            if (r0 != r2) goto L_0x0009
            r7.clickPressedItem(r3, r5)
            goto L_0x0009
        L_0x0041:
            if (r3 == 0) goto L_0x0045
            if (r9 == 0) goto L_0x0048
        L_0x0045:
            r7.clearPressedItem()
        L_0x0048:
            if (r3 == 0) goto L_0x0060
            android.support.v4.widget.ListViewAutoScrollHelper r9 = r7.mScrollHelper
            if (r9 != 0) goto L_0x0055
            android.support.v4.widget.ListViewAutoScrollHelper r9 = new android.support.v4.widget.ListViewAutoScrollHelper
            r9.<init>(r7)
            r7.mScrollHelper = r9
        L_0x0055:
            android.support.v4.widget.ListViewAutoScrollHelper r9 = r7.mScrollHelper
            r9.setEnabled(r2)
            android.support.v4.widget.ListViewAutoScrollHelper r9 = r7.mScrollHelper
            r9.onTouch(r7, r8)
            goto L_0x0069
        L_0x0060:
            android.support.v4.widget.ListViewAutoScrollHelper r8 = r7.mScrollHelper
            if (r8 == 0) goto L_0x0069
            android.support.v4.widget.ListViewAutoScrollHelper r7 = r7.mScrollHelper
            r7.setEnabled(r1)
        L_0x0069:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.p004v7.widget.DropDownListView.onForwardedEvent(android.view.MotionEvent, int):boolean");
    }

    private void clickPressedItem(View view, int i) {
        performItemClick(view, i, getItemIdAtPosition(i));
    }

    /* access modifiers changed from: 0000 */
    public void setListSelectionHidden(boolean z) {
        this.mListSelectionHidden = z;
    }

    private void clearPressedItem() {
        this.mDrawsInPressedState = false;
        setPressed(false);
        drawableStateChanged();
        View childAt = getChildAt(this.mMotionPosition - getFirstVisiblePosition());
        if (childAt != null) {
            childAt.setPressed(false);
        }
        if (this.mClickAnimation != null) {
            this.mClickAnimation.cancel();
            this.mClickAnimation = null;
        }
    }

    private void setPressedItem(View view, int i, float f, float f2) {
        this.mDrawsInPressedState = true;
        if (VERSION.SDK_INT >= 21) {
            drawableHotspotChanged(f, f2);
        }
        if (!isPressed()) {
            setPressed(true);
        }
        layoutChildren();
        if (this.mMotionPosition != -1) {
            View childAt = getChildAt(this.mMotionPosition - getFirstVisiblePosition());
            if (!(childAt == null || childAt == view || !childAt.isPressed())) {
                childAt.setPressed(false);
            }
        }
        this.mMotionPosition = i;
        float left = f - ((float) view.getLeft());
        float top = f2 - ((float) view.getTop());
        if (VERSION.SDK_INT >= 21) {
            view.drawableHotspotChanged(left, top);
        }
        if (!view.isPressed()) {
            view.setPressed(true);
        }
        positionSelectorLikeTouchCompat(i, view, f, f2);
        setSelectorEnabled(false);
        refreshDrawableState();
    }

    /* access modifiers changed from: protected */
    public boolean touchModeDrawsInPressedStateCompat() {
        return this.mDrawsInPressedState || super.touchModeDrawsInPressedStateCompat();
    }

    public boolean isInTouchMode() {
        return (this.mHijackFocus && this.mListSelectionHidden) || super.isInTouchMode();
    }

    public boolean hasWindowFocus() {
        return this.mHijackFocus || super.hasWindowFocus();
    }

    public boolean isFocused() {
        return this.mHijackFocus || super.isFocused();
    }

    public boolean hasFocus() {
        return this.mHijackFocus || super.hasFocus();
    }
}
