package com.google.android.gms.common.images.internal;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.p001v4.view.ViewCompat;

public final class ImageUtils {
    public static Bitmap frameBitmapInCircle(Bitmap bitmap) {
        int i;
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int i2 = 0;
        if (width >= height) {
            i2 = (height - width) / 2;
            width = height;
            i = 0;
        } else {
            i = (width - height) / 2;
        }
        Bitmap createBitmap = Bitmap.createBitmap(width, width, Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint(1);
        paint.setColor(ViewCompat.MEASURED_STATE_MASK);
        float f = (float) (width / 2);
        canvas.drawCircle(f, f, f, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, (float) i2, (float) i, paint);
        return createBitmap;
    }

    public static Drawable frameDrawableInCircle(Resources resources, Drawable drawable) {
        Bitmap bitmap;
        if (drawable == null) {
            bitmap = null;
        } else if (drawable instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        } else {
            Bitmap createBitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
            Canvas canvas = new Canvas(createBitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            bitmap = createBitmap;
        }
        return new BitmapDrawable(resources, frameBitmapInCircle(bitmap));
    }
}
