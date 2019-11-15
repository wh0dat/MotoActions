package com.motorola.actions.attentivedisplay;

import android.os.Environment;
import android.text.TextUtils;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.attentivedisplay.facedetection.SavePhotoMode;
import java.io.File;

public class AttentiveDisplayDebugHelper {
    private static final String KEY_AD_PHOTO_DEBUG_MODE = "ad_photo_debug_mode";

    public static String changeSavePhotoMode() {
        String str;
        String savePhotoMode = getSavePhotoMode();
        if (TextUtils.equals(savePhotoMode, SavePhotoMode.NONE.toString())) {
            str = SavePhotoMode.FAILURE.toString();
        } else if (TextUtils.equals(savePhotoMode, SavePhotoMode.FAILURE.toString())) {
            str = SavePhotoMode.SUCCESS.toString();
        } else if (TextUtils.equals(savePhotoMode, SavePhotoMode.SUCCESS.toString())) {
            str = SavePhotoMode.ALL.toString();
        } else {
            str = SavePhotoMode.NONE.toString();
        }
        SharedPreferenceManager.putString(KEY_AD_PHOTO_DEBUG_MODE, str);
        return str;
    }

    public static void disablePhotoSaving() {
        SharedPreferenceManager.putString(KEY_AD_PHOTO_DEBUG_MODE, SavePhotoMode.NONE.toString());
    }

    public static String getSavePhotoMode() {
        return SharedPreferenceManager.getString(KEY_AD_PHOTO_DEBUG_MODE, SavePhotoMode.NONE.toString());
    }

    public static File getPhotosDirectory() {
        return ActionsApplication.getAppContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
    }
}
