package com.motorola.actions.attentivedisplay;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.WindowManager;

public class ScreenControlOverlay {
    private static final int VIEW_TIMEOUT = 2000;
    private final Handler mHandler = new Handler();
    /* access modifiers changed from: private */
    public final OverlayPixel mScreenControl;
    private final Runnable mTimeout = new Runnable() {
        public void run() {
            ScreenControlOverlay.this.mScreenControl.setVisibility(4);
        }
    };
    private final WindowManager mWindowManager;

    ScreenControlOverlay(Context context) {
        this.mScreenControl = new OverlayPixel(context, 128);
        this.mScreenControl.setVisibility(4);
        this.mScreenControl.setForeground(new ColorDrawable());
        this.mWindowManager = (WindowManager) context.getSystemService("window");
        if (this.mWindowManager != null) {
            this.mWindowManager.addView(this.mScreenControl, this.mScreenControl.getLayoutParams());
        }
    }

    public void destroy() {
        this.mWindowManager.removeView(this.mScreenControl);
        this.mHandler.removeCallbacks(this.mTimeout);
    }

    public void userActivity() {
        this.mScreenControl.setVisibility(0);
        this.mHandler.postDelayed(this.mTimeout, 2000);
    }
}
