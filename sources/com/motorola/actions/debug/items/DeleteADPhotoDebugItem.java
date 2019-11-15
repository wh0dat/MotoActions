package com.motorola.actions.debug.items;

import android.widget.Toast;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.C0504R;
import com.motorola.actions.attentivedisplay.AttentiveDisplayDebugHelper;
import com.motorola.actions.utils.MALogger;
import java.io.File;

public class DeleteADPhotoDebugItem extends DebugItem {
    private static final MALogger LOGGER = new MALogger(DeleteADPhotoDebugItem.class);

    public DeleteADPhotoDebugItem() {
        setTitle(getString(C0504R.string.debug_delete_ad_photo_title));
        setDescription(getString(C0504R.string.debug_delete_ad_photo_description));
    }

    /* access modifiers changed from: protected */
    public void startBackgroundWork() {
        super.startBackgroundWork();
        for (File delete : AttentiveDisplayDebugHelper.getPhotosDirectory().listFiles()) {
            if (!delete.delete()) {
                LOGGER.mo11957d("Could not delete file");
            }
        }
    }

    /* access modifiers changed from: protected */
    public void startPostExecuteWork() {
        super.startPostExecuteWork();
        Toast.makeText(ActionsApplication.getAppContext(), C0504R.string.debug_delete_ad_photo_message, 0).show();
    }
}
