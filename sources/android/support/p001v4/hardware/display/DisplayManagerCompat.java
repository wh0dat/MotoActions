package android.support.p001v4.hardware.display;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.os.Build.VERSION;
import android.support.annotation.RequiresApi;
import android.view.Display;
import android.view.WindowManager;
import java.util.WeakHashMap;

/* renamed from: android.support.v4.hardware.display.DisplayManagerCompat */
public abstract class DisplayManagerCompat {
    public static final String DISPLAY_CATEGORY_PRESENTATION = "android.hardware.display.category.PRESENTATION";
    private static final WeakHashMap<Context, DisplayManagerCompat> sInstances = new WeakHashMap<>();

    /* renamed from: android.support.v4.hardware.display.DisplayManagerCompat$DisplayManagerCompatApi14Impl */
    private static class DisplayManagerCompatApi14Impl extends DisplayManagerCompat {
        private final WindowManager mWindowManager;

        DisplayManagerCompatApi14Impl(Context context) {
            this.mWindowManager = (WindowManager) context.getSystemService("window");
        }

        public Display getDisplay(int i) {
            Display defaultDisplay = this.mWindowManager.getDefaultDisplay();
            if (defaultDisplay.getDisplayId() == i) {
                return defaultDisplay;
            }
            return null;
        }

        public Display[] getDisplays() {
            return new Display[]{this.mWindowManager.getDefaultDisplay()};
        }

        public Display[] getDisplays(String str) {
            return str == null ? getDisplays() : new Display[0];
        }
    }

    @RequiresApi(17)
    /* renamed from: android.support.v4.hardware.display.DisplayManagerCompat$DisplayManagerCompatApi17Impl */
    private static class DisplayManagerCompatApi17Impl extends DisplayManagerCompat {
        private final DisplayManager mDisplayManager;

        DisplayManagerCompatApi17Impl(Context context) {
            this.mDisplayManager = (DisplayManager) context.getSystemService("display");
        }

        public Display getDisplay(int i) {
            return this.mDisplayManager.getDisplay(i);
        }

        public Display[] getDisplays() {
            return this.mDisplayManager.getDisplays();
        }

        public Display[] getDisplays(String str) {
            return this.mDisplayManager.getDisplays(str);
        }
    }

    public abstract Display getDisplay(int i);

    public abstract Display[] getDisplays();

    public abstract Display[] getDisplays(String str);

    DisplayManagerCompat() {
    }

    public static DisplayManagerCompat getInstance(Context context) {
        DisplayManagerCompat displayManagerCompat;
        synchronized (sInstances) {
            displayManagerCompat = (DisplayManagerCompat) sInstances.get(context);
            if (displayManagerCompat == null) {
                if (VERSION.SDK_INT >= 17) {
                    displayManagerCompat = new DisplayManagerCompatApi17Impl(context);
                } else {
                    displayManagerCompat = new DisplayManagerCompatApi14Impl(context);
                }
                sInstances.put(context, displayManagerCompat);
            }
        }
        return displayManagerCompat;
    }
}
