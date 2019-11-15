package com.motorola.actions.debug.items;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.p001v4.content.FileProvider;
import android.widget.Toast;
import com.google.android.gms.common.util.CrashUtils.ErrorDialogData;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.C0504R;
import com.motorola.actions.attentivedisplay.AttentiveDisplayDebugHelper;
import com.motorola.actions.utils.MALogger;
import java.io.File;
import java.util.ArrayList;

public class SendADPhotoDebugItem extends DebugItem {
    private static final MALogger LOGGER = new MALogger(SendADPhotoDebugItem.class);
    private static final String MIME_TYPE = "text/html";
    private static final String SEND_TO_ADDRESS = "motoactions.test@gmail.com";
    private final ArrayList<Uri> mUris = new ArrayList<>();

    public SendADPhotoDebugItem() {
        setTitle(getString(C0504R.string.debug_send_ad_photo_title));
        setDescription(getString(C0504R.string.debug_send_ad_photo_description));
    }

    /* access modifiers changed from: protected */
    public void startBackgroundWork() {
        super.startBackgroundWork();
        Context appContext = ActionsApplication.getAppContext();
        File[] listFiles = AttentiveDisplayDebugHelper.getPhotosDirectory().listFiles();
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("Files, size: ");
        sb.append(listFiles.length);
        mALogger.mo11957d(sb.toString());
        this.mUris.clear();
        for (File file : listFiles) {
            Uri uriForFile = FileProvider.getUriForFile(appContext, appContext.getApplicationContext().getPackageName(), file);
            MALogger mALogger2 = LOGGER;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("File: ");
            sb2.append(file.getPath());
            sb2.append(", Uri: ");
            sb2.append(uriForFile);
            mALogger2.mo11957d(sb2.toString());
            this.mUris.add(uriForFile);
        }
    }

    /* access modifiers changed from: protected */
    public void startPostExecuteWork() {
        super.startPostExecuteWork();
        if (this.mUris.size() == 0) {
            Toast.makeText(ActionsApplication.getAppContext(), C0504R.string.debug_send_ad_photo_no_photos, 0).show();
            return;
        }
        sendMail();
        AttentiveDisplayDebugHelper.disablePhotoSaving();
    }

    private void sendMail() {
        Intent intent = new Intent("android.intent.action.SEND_MULTIPLE");
        intent.setType(MIME_TYPE);
        intent.setFlags(ErrorDialogData.BINDER_CRASH);
        intent.putExtra("android.intent.extra.EMAIL", new String[]{SEND_TO_ADDRESS});
        intent.putExtra("android.intent.extra.SUBJECT", getString(C0504R.string.debug_send_ad_photo_subject));
        intent.putExtra("android.intent.extra.STREAM", this.mUris);
        ActionsApplication.getAppContext().startActivity(Intent.createChooser(intent, ActionsApplication.getAppContext().getString(C0504R.string.debug_sp_send_db_finished)));
    }
}
