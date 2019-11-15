package com.motorola.actions.nativebitmap;

import android.graphics.Bitmap;
import com.motorola.actions.utils.MALogger;

public final class BitmapNativeEx {
    private static final MALogger LOGGER = new MALogger(BitmapNativeEx.class);
    private long mNativeEx = 0;
    private long mStitchHandle = init();

    private native Bitmap getStitchBitmap(long j, long j2, long j3);

    private native long init();

    private native void nativeDestroy(long j);

    private native Bitmap nativeEx2Bitmap(long j);

    private native long stitchAddBitmap(long j, Bitmap bitmap);

    private native boolean stitchinit(long j, long j2, Bitmap bitmap, Bitmap[] bitmapArr, long j3, long j4, long j5, long j6, long j7, long j8, long j9, long j10);

    private native void stitchuninit(long j);

    private native void uninit(long j);

    static {
        try {
            System.loadLibrary("BitmapNativeEx");
        } catch (UnsatisfiedLinkError e) {
            LOGGER.mo11959e(e.toString());
        }
    }

    public void recycle() {
        if (this.mNativeEx != 0) {
            nativeDestroy(this.mNativeEx);
            this.mNativeEx = 0;
        }
    }

    public long getStitchHandle() {
        return this.mStitchHandle;
    }

    public void recycleEx() {
        if (this.mStitchHandle != 0) {
            stitchuninit(this.mStitchHandle);
            uninit(this.mStitchHandle);
            this.mStitchHandle = 0;
        }
    }

    public boolean stitchinit(int i, Bitmap bitmap, Bitmap[] bitmapArr, long j, long j2, long j3, long j4, long j5, long j6, long j7, long j8) {
        if (this.mStitchHandle != 0) {
            return stitchinit(this.mStitchHandle, (long) i, bitmap, bitmapArr, j, j2, j3, j4, j5, j6, j7, j8);
        }
        return false;
    }

    public long stitch_add_bitmap(Bitmap bitmap) {
        if (this.mStitchHandle != 0) {
            return stitchAddBitmap(this.mStitchHandle, bitmap);
        }
        return -300;
    }

    public Bitmap get_stitch_bitmap(long j, long j2) {
        if (this.mStitchHandle == 0) {
            return null;
        }
        return getStitchBitmap(this.mStitchHandle, j, j2);
    }

    public Bitmap getBitmap() {
        if (this.mNativeEx != 0) {
            return nativeEx2Bitmap(this.mNativeEx);
        }
        return null;
    }
}
