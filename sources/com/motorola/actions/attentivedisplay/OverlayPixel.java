package com.motorola.actions.attentivedisplay;

import android.content.Context;
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;

public class OverlayPixel extends FrameLayout {
    private final LayoutParams mLayoutParams;

    public OverlayPixel(Context context) {
        this(context, 0);
    }

    public OverlayPixel(Context context, int i) {
        super(context);
        LayoutParams layoutParams = new LayoutParams(1, 1, 2038, i | 40, -3);
        this.mLayoutParams = layoutParams;
        this.mLayoutParams.gravity = 53;
    }

    public LayoutParams getLayoutParams() {
        return this.mLayoutParams;
    }
}
