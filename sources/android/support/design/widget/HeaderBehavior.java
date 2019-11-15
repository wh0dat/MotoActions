package android.support.design.widget;

import android.content.Context;
import android.support.p001v4.math.MathUtils;
import android.support.p001v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.OverScroller;

abstract class HeaderBehavior<V extends View> extends ViewOffsetBehavior<V> {
    private static final int INVALID_POINTER = -1;
    private int mActivePointerId = -1;
    private Runnable mFlingRunnable;
    private boolean mIsBeingDragged;
    private int mLastMotionY;
    OverScroller mScroller;
    private int mTouchSlop = -1;
    private VelocityTracker mVelocityTracker;

    private class FlingRunnable implements Runnable {
        private final V mLayout;
        private final CoordinatorLayout mParent;

        FlingRunnable(CoordinatorLayout coordinatorLayout, V v) {
            this.mParent = coordinatorLayout;
            this.mLayout = v;
        }

        public void run() {
            if (this.mLayout != null && HeaderBehavior.this.mScroller != null) {
                if (HeaderBehavior.this.mScroller.computeScrollOffset()) {
                    HeaderBehavior.this.setHeaderTopBottomOffset(this.mParent, this.mLayout, HeaderBehavior.this.mScroller.getCurrY());
                    ViewCompat.postOnAnimation(this.mLayout, this);
                    return;
                }
                HeaderBehavior.this.onFlingFinished(this.mParent, this.mLayout);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean canDragView(V v) {
        return false;
    }

    /* access modifiers changed from: 0000 */
    public void onFlingFinished(CoordinatorLayout coordinatorLayout, V v) {
    }

    public HeaderBehavior() {
    }

    public HeaderBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public boolean onInterceptTouchEvent(CoordinatorLayout coordinatorLayout, V v, MotionEvent motionEvent) {
        if (this.mTouchSlop < 0) {
            this.mTouchSlop = ViewConfiguration.get(coordinatorLayout.getContext()).getScaledTouchSlop();
        }
        if (motionEvent.getAction() == 2 && this.mIsBeingDragged) {
            return true;
        }
        switch (motionEvent.getActionMasked()) {
            case 0:
                this.mIsBeingDragged = false;
                int x = (int) motionEvent.getX();
                int y = (int) motionEvent.getY();
                if (canDragView(v) && coordinatorLayout.isPointInChildBounds(v, x, y)) {
                    this.mLastMotionY = y;
                    this.mActivePointerId = motionEvent.getPointerId(0);
                    ensureVelocityTracker();
                    break;
                }
            case 1:
            case 3:
                this.mIsBeingDragged = false;
                this.mActivePointerId = -1;
                if (this.mVelocityTracker != null) {
                    this.mVelocityTracker.recycle();
                    this.mVelocityTracker = null;
                    break;
                }
                break;
            case 2:
                int i = this.mActivePointerId;
                if (i != -1) {
                    int findPointerIndex = motionEvent.findPointerIndex(i);
                    if (findPointerIndex != -1) {
                        int y2 = (int) motionEvent.getY(findPointerIndex);
                        if (Math.abs(y2 - this.mLastMotionY) > this.mTouchSlop) {
                            this.mIsBeingDragged = true;
                            this.mLastMotionY = y2;
                            break;
                        }
                    }
                }
                break;
        }
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.addMovement(motionEvent);
        }
        return this.mIsBeingDragged;
    }

    public boolean onTouchEvent(CoordinatorLayout coordinatorLayout, V v, MotionEvent motionEvent) {
        if (this.mTouchSlop < 0) {
            this.mTouchSlop = ViewConfiguration.get(coordinatorLayout.getContext()).getScaledTouchSlop();
        }
        switch (motionEvent.getActionMasked()) {
            case 0:
                int y = (int) motionEvent.getY();
                if (coordinatorLayout.isPointInChildBounds(v, (int) motionEvent.getX(), y) && canDragView(v)) {
                    this.mLastMotionY = y;
                    this.mActivePointerId = motionEvent.getPointerId(0);
                    ensureVelocityTracker();
                    break;
                } else {
                    return false;
                }
            case 1:
                if (this.mVelocityTracker != null) {
                    this.mVelocityTracker.addMovement(motionEvent);
                    this.mVelocityTracker.computeCurrentVelocity(1000);
                    fling(coordinatorLayout, v, -getScrollRangeForDragFling(v), 0, this.mVelocityTracker.getYVelocity(this.mActivePointerId));
                    break;
                }
                break;
            case 2:
                int findPointerIndex = motionEvent.findPointerIndex(this.mActivePointerId);
                if (findPointerIndex != -1) {
                    int y2 = (int) motionEvent.getY(findPointerIndex);
                    int i = this.mLastMotionY - y2;
                    if (!this.mIsBeingDragged && Math.abs(i) > this.mTouchSlop) {
                        this.mIsBeingDragged = true;
                        i = i > 0 ? i - this.mTouchSlop : i + this.mTouchSlop;
                    }
                    int i2 = i;
                    if (this.mIsBeingDragged) {
                        this.mLastMotionY = y2;
                        scroll(coordinatorLayout, v, i2, getMaxDragOffset(v), 0);
                        break;
                    }
                } else {
                    return false;
                }
                break;
            case 3:
                break;
        }
        this.mIsBeingDragged = false;
        this.mActivePointerId = -1;
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.addMovement(motionEvent);
        }
        return true;
    }

    /* access modifiers changed from: 0000 */
    public int setHeaderTopBottomOffset(CoordinatorLayout coordinatorLayout, V v, int i) {
        return setHeaderTopBottomOffset(coordinatorLayout, v, i, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    /* access modifiers changed from: 0000 */
    public int setHeaderTopBottomOffset(CoordinatorLayout coordinatorLayout, V v, int i, int i2, int i3) {
        int topAndBottomOffset = getTopAndBottomOffset();
        if (i2 != 0 && topAndBottomOffset >= i2 && topAndBottomOffset <= i3) {
            int clamp = MathUtils.clamp(i, i2, i3);
            if (topAndBottomOffset != clamp) {
                setTopAndBottomOffset(clamp);
                return topAndBottomOffset - clamp;
            }
        }
        return 0;
    }

    /* access modifiers changed from: 0000 */
    public int getTopBottomOffsetForScrollingSibling() {
        return getTopAndBottomOffset();
    }

    /* access modifiers changed from: 0000 */
    public final int scroll(CoordinatorLayout coordinatorLayout, V v, int i, int i2, int i3) {
        return setHeaderTopBottomOffset(coordinatorLayout, v, getTopBottomOffsetForScrollingSibling() - i, i2, i3);
    }

    /* access modifiers changed from: 0000 */
    public final boolean fling(CoordinatorLayout coordinatorLayout, V v, int i, int i2, float f) {
        V v2 = v;
        if (this.mFlingRunnable != null) {
            v2.removeCallbacks(this.mFlingRunnable);
            this.mFlingRunnable = null;
        }
        if (this.mScroller == null) {
            this.mScroller = new OverScroller(v2.getContext());
        }
        this.mScroller.fling(0, getTopAndBottomOffset(), 0, Math.round(f), 0, 0, i, i2);
        if (this.mScroller.computeScrollOffset()) {
            this.mFlingRunnable = new FlingRunnable(coordinatorLayout, v2);
            ViewCompat.postOnAnimation(v2, this.mFlingRunnable);
            return true;
        }
        onFlingFinished(coordinatorLayout, v2);
        return false;
    }

    /* access modifiers changed from: 0000 */
    public int getMaxDragOffset(V v) {
        return -v.getHeight();
    }

    /* access modifiers changed from: 0000 */
    public int getScrollRangeForDragFling(V v) {
        return v.getHeight();
    }

    private void ensureVelocityTracker() {
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
    }
}
