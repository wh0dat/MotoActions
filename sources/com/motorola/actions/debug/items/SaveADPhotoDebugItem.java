package com.motorola.actions.debug.items;

import android.text.TextUtils;
import android.widget.Toast;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.C0504R;
import com.motorola.actions.attentivedisplay.AttentiveDisplayDebugHelper;
import com.motorola.actions.attentivedisplay.facedetection.SavePhotoMode;

public class SaveADPhotoDebugItem extends DebugItem {
    public SaveADPhotoDebugItem() {
        updateData();
    }

    /* access modifiers changed from: protected */
    public void startBackgroundWork() {
        super.startBackgroundWork();
        AttentiveDisplayDebugHelper.changeSavePhotoMode();
    }

    /* access modifiers changed from: protected */
    public void startPostExecuteWork() {
        super.startPostExecuteWork();
        Toast.makeText(ActionsApplication.getAppContext(), AttentiveDisplayDebugHelper.getSavePhotoMode(), 0).show();
        setDescription(getDescripion());
    }

    private String getDescripion() {
        String savePhotoMode = AttentiveDisplayDebugHelper.getSavePhotoMode();
        int i = TextUtils.equals(savePhotoMode, SavePhotoMode.NONE.toString()) ? C0504R.string.debug_save_ad_photo_none : TextUtils.equals(savePhotoMode, SavePhotoMode.FAILURE.toString()) ? C0504R.string.debug_save_ad_photo_failure : TextUtils.equals(savePhotoMode, SavePhotoMode.SUCCESS.toString()) ? C0504R.string.debug_save_ad_photo_success : C0504R.string.debug_save_ad_photo_all;
        return getString(i);
    }

    public void updateData() {
        super.updateData();
        setTitle(getString(C0504R.string.debug_save_ad_photo_title));
        setDescription(getDescripion());
    }
}
