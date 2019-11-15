package com.motorola.actions.utils;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.support.annotation.Nullable;
import java.io.File;
import java.io.IOException;

public class ImageUtils {
    private static final MALogger LOGGER = new MALogger(ImageUtils.class);

    @Nullable
    @SuppressLint({"NewApi"})
    public static Bitmap decode(Resources resources, int i) {
        LOGGER.mo11957d("decode, decoding input as android resources");
        if (resources != null) {
            try {
                return ImageDecoder.decodeBitmap(ImageDecoder.createSource(resources, i));
            } catch (IOException e) {
                MALogger mALogger = LOGGER;
                StringBuilder sb = new StringBuilder();
                sb.append("error to decode android resources, e=");
                sb.append(e);
                mALogger.mo11959e(sb.toString());
                return null;
            }
        } else {
            LOGGER.mo11959e("Resources can't be null");
            return null;
        }
    }

    @Nullable
    @SuppressLint({"NewApi"})
    public static Bitmap decode(File file) {
        LOGGER.mo11957d("decode, input as file");
        if (file == null) {
            LOGGER.mo11959e("File can't be null");
        } else {
            try {
                return ImageDecoder.decodeBitmap(ImageDecoder.createSource(file));
            } catch (IOException e) {
                MALogger mALogger = LOGGER;
                StringBuilder sb = new StringBuilder();
                sb.append("error to decode the file, e=");
                sb.append(e);
                mALogger.mo11959e(sb.toString());
            }
        }
        return null;
    }
}
