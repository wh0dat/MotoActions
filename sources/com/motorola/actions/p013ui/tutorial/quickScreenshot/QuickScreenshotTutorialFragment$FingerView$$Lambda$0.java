package com.motorola.actions.p013ui.tutorial.quickScreenshot;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/* renamed from: com.motorola.actions.ui.tutorial.quickScreenshot.QuickScreenshotTutorialFragment$FingerView$$Lambda$0 */
final /* synthetic */ class QuickScreenshotTutorialFragment$FingerView$$Lambda$0 implements OnTouchListener {
    private final FingerView arg$1;

    QuickScreenshotTutorialFragment$FingerView$$Lambda$0(FingerView fingerView) {
        this.arg$1 = fingerView;
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        return this.arg$1.lambda$new$0$QuickScreenshotTutorialFragment$FingerView(view, motionEvent);
    }
}
