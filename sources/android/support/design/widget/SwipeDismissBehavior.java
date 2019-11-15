package android.support.design.widget;

import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.design.widget.CoordinatorLayout.Behavior;
import android.support.p001v4.view.ViewCompat;
import android.support.p001v4.widget.ViewDragHelper;
import android.support.p001v4.widget.ViewDragHelper.Callback;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class SwipeDismissBehavior<V extends View> extends Behavior<V> {
    private static final float DEFAULT_ALPHA_END_DISTANCE = 0.5f;
    private static final float DEFAULT_ALPHA_START_DISTANCE = 0.0f;
    private static final float DEFAULT_DRAG_DISMISS_THRESHOLD = 0.5f;
    public static final int STATE_DRAGGING = 1;
    public static final int STATE_IDLE = 0;
    public static final int STATE_SETTLING = 2;
    public static final int SWIPE_DIRECTION_ANY = 2;
    public static final int SWIPE_DIRECTION_END_TO_START = 1;
    public static final int SWIPE_DIRECTION_START_TO_END = 0;
    float mAlphaEndSwipeDistance = 0.5f;
    float mAlphaStartSwipeDistance = 0.0f;
    private final Callback mDragCallback = new Callback() {
        private static final int INVALID_POINTER_ID = -1;
        private int mActivePointerId = -1;
        private int mOriginalCapturedViewLeft;

        public boolean tryCaptureView(View view, int i) {
            return this.mActivePointerId == -1 && SwipeDismissBehavior.this.canSwipeDismissView(view);
        }

        public void onViewCaptured(View view, int i) {
            this.mActivePointerId = i;
            this.mOriginalCapturedViewLeft = view.getLeft();
            ViewParent parent = view.getParent();
            if (parent != null) {
                parent.requestDisallowInterceptTouchEvent(true);
            }
        }

        public void onViewDragStateChanged(int i) {
            if (SwipeDismissBehavior.this.mListener != null) {
                SwipeDismissBehavior.this.mListener.onDragStateChanged(i);
            }
        }

        public void onViewReleased(View view, float f, float f2) {
            boolean z;
            int i;
            this.mActivePointerId = -1;
            int width = view.getWidth();
            if (shouldDismiss(view, f)) {
                i = view.getLeft() < this.mOriginalCapturedViewLeft ? this.mOriginalCapturedViewLeft - width : this.mOriginalCapturedViewLeft + width;
                z = true;
            } else {
                i = this.mOriginalCapturedViewLeft;
                z = false;
            }
            if (SwipeDismissBehavior.this.mViewDragHelper.settleCapturedViewAt(i, view.getTop())) {
                ViewCompat.postOnAnimation(view, new SettleRunnable(view, z));
            } else if (z && SwipeDismissBehavior.this.mListener != null) {
                SwipeDismissBehavior.this.mListener.onDismiss(view);
            }
        }

        private boolean shouldDismiss(View view, float f) {
            int i = (f > 0.0f ? 1 : (f == 0.0f ? 0 : -1));
            boolean z = false;
            if (i != 0) {
                boolean z2 = ViewCompat.getLayoutDirection(view) == 1;
                if (SwipeDismissBehavior.this.mSwipeDirection == 2) {
                    return true;
                }
                if (SwipeDismissBehavior.this.mSwipeDirection == 0) {
                    return z2 ? z : z;
                    z = true;
                } else if (SwipeDismissBehavior.this.mSwipeDirection != 1) {
                    return false;
                } else {
                    if (!z2 ? f < 0.0f : i > 0) {
                        z = true;
                    }
                    return z;
                }
            } else {
                if (Math.abs(view.getLeft() - this.mOriginalCapturedViewLeft) >= Math.round(((float) view.getWidth()) * SwipeDismissBehavior.this.mDragDismissThreshold)) {
                    z = true;
                }
                return z;
            }
        }

        public int getViewHorizontalDragRange(View view) {
            return view.getWidth();
        }

        public int clampViewPositionHorizontal(View view, int i, int i2) {
            int i3;
            int i4;
            boolean z = ViewCompat.getLayoutDirection(view) == 1;
            if (SwipeDismissBehavior.this.mSwipeDirection == 0) {
                if (z) {
                    i3 = this.mOriginalCapturedViewLeft - view.getWidth();
                    i4 = this.mOriginalCapturedViewLeft;
                } else {
                    i3 = this.mOriginalCapturedViewLeft;
                    i4 = this.mOriginalCapturedViewLeft + view.getWidth();
                }
            } else if (SwipeDismissBehavior.this.mSwipeDirection != 1) {
                i3 = this.mOriginalCapturedViewLeft - view.getWidth();
                i4 = this.mOriginalCapturedViewLeft + view.getWidth();
            } else if (z) {
                i3 = this.mOriginalCapturedViewLeft;
                i4 = this.mOriginalCapturedViewLeft + view.getWidth();
            } else {
                i3 = this.mOriginalCapturedViewLeft - view.getWidth();
                i4 = this.mOriginalCapturedViewLeft;
            }
            return SwipeDismissBehavior.clamp(i3, i, i4);
        }

        public int clampViewPositionVertical(View view, int i, int i2) {
            return view.getTop();
        }

        public void onViewPositionChanged(View view, int i, int i2, int i3, int i4) {
            float width = ((float) this.mOriginalCapturedViewLeft) + (((float) view.getWidth()) * SwipeDismissBehavior.this.mAlphaStartSwipeDistance);
            float width2 = ((float) this.mOriginalCapturedViewLeft) + (((float) view.getWidth()) * SwipeDismissBehavior.this.mAlphaEndSwipeDistance);
            float f = (float) i;
            if (f <= width) {
                view.setAlpha(1.0f);
            } else if (f >= width2) {
                view.setAlpha(0.0f);
            } else {
                view.setAlpha(SwipeDismissBehavior.clamp(0.0f, 1.0f - SwipeDismissBehavior.fraction(width, width2, f), 1.0f));
            }
        }
    };
    float mDragDismissThreshold = 0.5f;
    private boolean mInterceptingEvents;
    OnDismissListener mListener;
    private float mSensitivity = 0.0f;
    private boolean mSensitivitySet;
    int mSwipeDirection = 2;
    ViewDragHelper mViewDragHelper;

    public interface OnDismissListener {
        void onDismiss(View view);

        void onDragStateChanged(int i);
    }

    private class SettleRunnable implements Runnable {
        private final boolean mDismiss;
        private final View mView;

        SettleRunnable(View view, boolean z) {
            this.mView = view;
            this.mDismiss = z;
        }

        public void run() {
            if (SwipeDismissBehavior.this.mViewDragHelper != null && SwipeDismissBehavior.this.mViewDragHelper.continueSettling(true)) {
                ViewCompat.postOnAnimation(this.mView, this);
            } else if (this.mDismiss && SwipeDismissBehavior.this.mListener != null) {
                SwipeDismissBehavior.this.mListener.onDismiss(this.mView);
            }
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    @Retention(RetentionPolicy.SOURCE)
    private @interface SwipeDirection {
    }

    static float fraction(float f, float f2, float f3) {
        return (f3 - f) / (f2 - f);
    }

    public boolean canSwipeDismissView(@NonNull View view) {
        return true;
    }

    public void setListener(OnDismissListener onDismissListener) {
        this.mListener = onDismissListener;
    }

    public void setSwipeDirection(int i) {
        this.mSwipeDirection = i;
    }

    public void setDragDismissDistance(float f) {
        this.mDragDismissThreshold = clamp(0.0f, f, 1.0f);
    }

    public void setStartAlphaSwipeDistance(float f) {
        this.mAlphaStartSwipeDistance = clamp(0.0f, f, 1.0f);
    }

    public void setEndAlphaSwipeDistance(float f) {
        this.mAlphaEndSwipeDistance = clamp(0.0f, f, 1.0f);
    }

    public void setSensitivity(float f) {
        this.mSensitivity = f;
        this.mSensitivitySet = true;
    }

    public boolean onInterceptTouchEvent(CoordinatorLayout coordinatorLayout, V v, MotionEvent motionEvent) {
        boolean z = this.mInterceptingEvents;
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked != 3) {
            switch (actionMasked) {
                case 0:
                    this.mInterceptingEvents = coordinatorLayout.isPointInChildBounds(v, (int) motionEvent.getX(), (int) motionEvent.getY());
                    z = this.mInterceptingEvents;
                    break;
                case 1:
                    break;
            }
        }
        this.mInterceptingEvents = false;
        if (!z) {
            return false;
        }
        ensureViewDragHelper(coordinatorLayout);
        return this.mViewDragHelper.shouldInterceptTouchEvent(motionEvent);
    }

    public boolean onTouchEvent(CoordinatorLayout coordinatorLayout, V v, MotionEvent motionEvent) {
        if (this.mViewDragHelper == null) {
            return false;
        }
        this.mViewDragHelper.processTouchEvent(motionEvent);
        return true;
    }

    private void ensureViewDragHelper(ViewGroup viewGroup) {
        ViewDragHelper viewDragHelper;
        if (this.mViewDragHelper == null) {
            if (this.mSensitivitySet) {
                viewDragHelper = ViewDragHelper.create(viewGroup, this.mSensitivity, this.mDragCallback);
            } else {
                viewDragHelper = ViewDragHelper.create(viewGroup, this.mDragCallback);
            }
            this.mViewDragHelper = viewDragHelper;
        }
    }

    static float clamp(float f, float f2, float f3) {
        return Math.min(Math.max(f, f2), f3);
    }

    static int clamp(int i, int i2, int i3) {
        return Math.min(Math.max(i, i2), i3);
    }

    public int getDragState() {
        if (this.mViewDragHelper != null) {
            return this.mViewDragHelper.getViewDragState();
        }
        return 0;
    }
}
