package com.motorola.actions.p013ui.tutorial;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import com.motorola.actions.C0504R;

/* renamed from: com.motorola.actions.ui.tutorial.CountdownSpinner */
public class CountdownSpinner extends Drawable {
    private static final int COLOR_BACKGROUND = -1184275;
    private final int mColorProgress;
    private final Context mContext;
    private final long mMaxProgress;
    private final Paint mPaint = new Paint();
    private long mProgress;

    public int getOpacity() {
        return -1;
    }

    public void setAlpha(int i) {
    }

    public void setColorFilter(ColorFilter colorFilter) {
    }

    public CountdownSpinner(Context context, long j) {
        this.mContext = context;
        this.mMaxProgress = j;
        this.mPaint.setStyle(Style.STROKE);
        this.mPaint.setStrokeWidth(convertDpToPixels(2));
        this.mColorProgress = context.getColor(C0504R.color.moto_primary_color_light);
    }

    public void draw(@NonNull Canvas canvas) {
        float width = (float) canvas.getWidth();
        float height = (float) canvas.getHeight();
        float f = width > height ? height / 4.0f : width / 4.0f;
        float f2 = width / 2.0f;
        float f3 = height / 2.0f;
        RectF rectF = new RectF();
        rectF.set(f2 - f, f3 - f, f2 + f, f3 + f);
        canvas.save();
        this.mPaint.setColor(COLOR_BACKGROUND);
        Canvas canvas2 = canvas;
        RectF rectF2 = rectF;
        canvas2.drawArc(rectF2, 0.0f, 360.0f, false, this.mPaint);
        this.mPaint.setColor(this.mColorProgress);
        canvas2.drawArc(rectF2, 270.0f, (float) this.mProgress, false, this.mPaint);
    }

    public void setProgress(long j) {
        this.mProgress = (j * 360) / this.mMaxProgress;
        if (this.mProgress > 360) {
            this.mProgress = 360;
        }
    }

    private float convertDpToPixels(int i) {
        return TypedValue.applyDimension(1, (float) i, this.mContext.getResources().getDisplayMetrics());
    }
}
