package android.support.p004v7.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.p004v7.appcompat.C0383R;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RatingBar;

/* renamed from: android.support.v7.widget.AppCompatRatingBar */
public class AppCompatRatingBar extends RatingBar {
    private final AppCompatProgressBarHelper mAppCompatProgressBarHelper;

    public AppCompatRatingBar(Context context) {
        this(context, null);
    }

    public AppCompatRatingBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, C0383R.attr.ratingBarStyle);
    }

    public AppCompatRatingBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mAppCompatProgressBarHelper = new AppCompatProgressBarHelper(this);
        this.mAppCompatProgressBarHelper.loadFromAttributes(attributeSet, i);
    }

    /* access modifiers changed from: protected */
    public synchronized void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        Bitmap sampleTime = this.mAppCompatProgressBarHelper.getSampleTime();
        if (sampleTime != null) {
            setMeasuredDimension(View.resolveSizeAndState(sampleTime.getWidth() * getNumStars(), i, 0), getMeasuredHeight());
        }
    }
}
