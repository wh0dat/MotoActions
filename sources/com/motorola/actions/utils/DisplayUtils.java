package com.motorola.actions.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import com.motorola.actions.ActionsApplication;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class DisplayUtils {
    private static final int LID_ABSENT = -1;
    private static final int LID_CLOSED = 0;
    private static final int LID_OPEN = 1;
    private static final MALogger LOGGER = new MALogger(DisplayUtils.class);
    private static final String METHOD_GET_LID_STATE = "getLidState";
    public static final int SMALL_SCREEN_WIDTH = 540;
    private static Method sMethodGetLidState;

    @Retention(RetentionPolicy.SOURCE)
    @interface LidState {
    }

    static {
        try {
            sMethodGetLidState = Configuration.class.getDeclaredMethod(METHOD_GET_LID_STATE, new Class[]{Configuration.class});
        } catch (LinkageError | NoSuchMethodException unused) {
            LOGGER.mo11959e("Unable to initialize class");
        }
    }

    private DisplayUtils() {
    }

    public static int getRealDisplayWidth(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getRealMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static int getScreenWidth(Activity activity) {
        int i = getScreenSize(activity).x;
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("Retrieved screen width: ");
        sb.append(i);
        mALogger.mo11957d(sb.toString());
        return i;
    }

    @SuppressLint({"DefaultLocale"})
    public static Point getScreenSize(Activity activity) {
        Display defaultDisplay = activity.getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        LOGGER.mo11957d(String.format("Retrieved screen size: %dw, %dh", new Object[]{Integer.valueOf(point.x), Integer.valueOf(point.y)}));
        return point;
    }

    public static float convertDpToPx(Context context, float f) {
        return f * context.getResources().getDisplayMetrics().density;
    }

    public static int getOrientation() {
        return ((WindowManager) ActionsApplication.getAppContext().getSystemService(WindowManager.class)).getDefaultDisplay().getOrientation();
    }

    public static boolean isInPortrait() {
        int orientation = getOrientation();
        return orientation == 2 || orientation == 0;
    }

    public static boolean isLidClosed() {
        return getLidState() == 0;
    }

    public static int getLidState() {
        if (sMethodGetLidState != null) {
            try {
                return ((Integer) sMethodGetLidState.invoke(ActionsApplication.getAppContext().getResources().getConfiguration(), new Object[0])).intValue();
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException unused) {
                LOGGER.mo11957d("Unable to invoke method");
            }
        }
        return -1;
    }
}
