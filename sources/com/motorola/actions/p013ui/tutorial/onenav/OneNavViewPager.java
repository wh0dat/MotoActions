package com.motorola.actions.p013ui.tutorial.onenav;

import android.content.Context;
import android.support.p001v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/* renamed from: com.motorola.actions.ui.tutorial.onenav.OneNavViewPager */
public class OneNavViewPager extends ViewPager {
    private boolean mAllowSwiping = true;

    public OneNavViewPager(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        performClick();
        return this.mAllowSwiping && super.onTouchEvent(motionEvent);
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return this.mAllowSwiping && super.onInterceptTouchEvent(motionEvent);
    }

    public boolean performClick() {
        super.performClick();
        return true;
    }

    public void setAllowSwiping(boolean z) {
        this.mAllowSwiping = z;
    }
}
