package com.motorola.actions.attentivedisplay.util;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Orientation {
    public static final int LANDSCAPE_LEFT = 0;
    public static final int LANDSCAPE_RIGHT = 180;
    public static final int NOT_FOUND = -1;
    public static final int PORTRAIT = 270;

    @Retention(RetentionPolicy.SOURCE)
    public @interface ScreenOrientationOptions {
    }
}
