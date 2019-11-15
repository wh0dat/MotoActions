package com.motorola.actions.approach.p006us;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.os.AsyncTask;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.utils.Constants;
import com.motorola.actions.utils.Device;
import com.motorola.actions.utils.MALogger;
import java.io.File;

/* renamed from: com.motorola.actions.approach.us.USFirmwareUpdater */
public class USFirmwareUpdater {
    private static final String DATA_DIR = "/data/adspd/";
    private static final String DATA_FILE = "marley-dsp1-ultrasound.wmfw";
    private static final String FIRMWARE_RESOURCE_LOCATION = "raw";
    private static final String FIRMWARE_V2016_RESOURCE_ID = "marley1";
    private static final String FIRMWARE_V2017_RESOURCE_ID = "marley2";
    private static final int IO_BUFFER_SIZE = 4096;
    public static final String KEY_US_FIRMWARE_VERSION = "ULTRASOUND_FIRMWARE_VERSION";
    /* access modifiers changed from: private */
    public static final MALogger LOGGER = new MALogger(USFirmwareUpdater.class);
    /* access modifiers changed from: private */
    public final USFirmwareUpdateCallback mCallback;
    /* access modifiers changed from: private */
    public final Context mContext;
    private final File mDataDir = new File(DATA_DIR);
    private final File mDataFile = new File(this.mDataDir, DATA_FILE);

    @SuppressLint({"StaticFieldLeak"})
    /* renamed from: com.motorola.actions.approach.us.USFirmwareUpdater$USFirmwareUpdaterTask */
    private class USFirmwareUpdaterTask extends AsyncTask<Void, Void, Boolean> {
        private USFirmwareUpdaterTask() {
        }

        /* access modifiers changed from: protected */
        public Boolean doInBackground(Void... voidArr) {
            USFirmwareUpdater.LOGGER.mo11957d("doInBackground");
            boolean z = false;
            if (USFirmwareUpdater.this.getFirmwareIdFromResource() == 0) {
                USFirmwareUpdater.LOGGER.mo11957d("Wiping firmware.");
                USFirmwareUpdater.this.deleteFirmware(false);
            } else if (!USFirmwareUpdater.this.isVerified()) {
                USFirmwareUpdater.LOGGER.mo11957d("verify filesystem not passed.");
                if (!USFirmwareUpdater.this.prepareDspFwDir() || !USFirmwareUpdater.this.writeFileSystem()) {
                    USFirmwareUpdater.LOGGER.mo11959e("Cannot prepare US firmware files");
                    return Boolean.valueOf(z);
                }
            }
            z = true;
            return Boolean.valueOf(z);
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Boolean bool) {
            if (bool.booleanValue()) {
                SharedPreferenceManager.putString(USFirmwareUpdater.KEY_US_FIRMWARE_VERSION, Constants.getApkVersion(USFirmwareUpdater.this.mContext));
                USFirmwareUpdater.this.mCallback.onUpdateComplete(true);
                return;
            }
            USFirmwareUpdater.this.storeUnvalidated();
            USFirmwareUpdater.this.mCallback.onUpdateComplete(false);
        }
    }

    public USFirmwareUpdater(Context context, USFirmwareUpdateCallback uSFirmwareUpdateCallback) {
        this.mContext = context;
        this.mCallback = uSFirmwareUpdateCallback;
    }

    public void executeSetupTask() {
        new USFirmwareUpdaterTask().execute(new Void[0]);
    }

    /* access modifiers changed from: private */
    public int getFirmwareIdFromResource() {
        int i = 0;
        try {
            int identifier = this.mContext.getResources().getIdentifier(getFirmwareResourceFileName(), FIRMWARE_RESOURCE_LOCATION, this.mContext.getPackageName());
            if (identifier == 0 || this.mContext.getResources().openRawResource(identifier) != null) {
                i = identifier;
            }
        } catch (NotFoundException e) {
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("Expcetion trying to access firmware resource: ");
            sb.append(e.toString());
            mALogger.mo11959e(sb.toString());
        }
        MALogger mALogger2 = LOGGER;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("getFirmwareIdFromResource returning ");
        sb2.append(i);
        mALogger2.mo11957d(sb2.toString());
        return i;
    }

    private String getFirmwareResourceFileName() {
        String str = Device.isVertexDevice() ? FIRMWARE_V2016_RESOURCE_ID : FIRMWARE_V2017_RESOURCE_ID;
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("uploadResource = ");
        sb.append(str);
        mALogger.mo11957d(sb.toString());
        return str;
    }

    /* access modifiers changed from: private */
    public void deleteFirmware(boolean z) {
        if (!this.mDataFile.delete() && z) {
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to delete US firmware data file: ");
            sb.append(this.mDataFile);
            mALogger.mo11959e(sb.toString());
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0066 A[SYNTHETIC, Splitter:B:35:0x0066] */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x0073 A[SYNTHETIC, Splitter:B:40:0x0073] */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x0082 A[SYNTHETIC, Splitter:B:46:0x0082] */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x008f A[SYNTHETIC, Splitter:B:51:0x008f] */
    /* JADX WARNING: Removed duplicated region for block: B:59:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean writeFileSystem() {
        /*
            r7 = this;
            com.motorola.actions.utils.MALogger r0 = LOGGER
            java.lang.String r1 = "Unpacking US firmware data file"
            r0.mo11957d(r1)
            r0 = 4096(0x1000, float:5.74E-42)
            byte[] r0 = new byte[r0]
            r1 = 0
            r2 = 0
            r3 = 1
            android.content.Context r4 = r7.mContext     // Catch:{ IOException -> 0x0058, all -> 0x0055 }
            android.content.res.Resources r4 = r4.getResources()     // Catch:{ IOException -> 0x0058, all -> 0x0055 }
            int r5 = r7.getFirmwareIdFromResource()     // Catch:{ IOException -> 0x0058, all -> 0x0055 }
            java.io.InputStream r4 = r4.openRawResource(r5)     // Catch:{ IOException -> 0x0058, all -> 0x0055 }
            java.io.FileOutputStream r5 = new java.io.FileOutputStream     // Catch:{ IOException -> 0x0051, all -> 0x004d }
            java.io.File r6 = r7.mDataFile     // Catch:{ IOException -> 0x0051, all -> 0x004d }
            r5.<init>(r6)     // Catch:{ IOException -> 0x0051, all -> 0x004d }
        L_0x0023:
            int r2 = r4.read(r0)     // Catch:{ IOException -> 0x004b, all -> 0x0049 }
            if (r2 <= 0) goto L_0x002d
            r5.write(r0, r1, r2)     // Catch:{ IOException -> 0x004b, all -> 0x0049 }
            goto L_0x0023
        L_0x002d:
            if (r4 == 0) goto L_0x003a
            r4.close()     // Catch:{ IOException -> 0x0033 }
            goto L_0x003a
        L_0x0033:
            com.motorola.actions.utils.MALogger r7 = LOGGER
            java.lang.String r0 = "IO Exception"
            r7.mo11963w(r0)
        L_0x003a:
            if (r5 == 0) goto L_0x0047
            r5.close()     // Catch:{ IOException -> 0x0040 }
            goto L_0x0047
        L_0x0040:
            com.motorola.actions.utils.MALogger r7 = LOGGER
            java.lang.String r0 = "IO Exception"
            r7.mo11963w(r0)
        L_0x0047:
            r1 = r3
            goto L_0x007e
        L_0x0049:
            r7 = move-exception
            goto L_0x004f
        L_0x004b:
            r0 = move-exception
            goto L_0x0053
        L_0x004d:
            r7 = move-exception
            r5 = r2
        L_0x004f:
            r2 = r4
            goto L_0x0080
        L_0x0051:
            r0 = move-exception
            r5 = r2
        L_0x0053:
            r2 = r4
            goto L_0x005a
        L_0x0055:
            r7 = move-exception
            r5 = r2
            goto L_0x0080
        L_0x0058:
            r0 = move-exception
            r5 = r2
        L_0x005a:
            com.motorola.actions.utils.MALogger r4 = LOGGER     // Catch:{ all -> 0x007f }
            java.lang.String r6 = "Error unpacking US firmware data file"
            r4.mo11960e(r6, r0)     // Catch:{ all -> 0x007f }
            r7.deleteFirmware(r3)     // Catch:{ all -> 0x007f }
            if (r2 == 0) goto L_0x0071
            r2.close()     // Catch:{ IOException -> 0x006a }
            goto L_0x0071
        L_0x006a:
            com.motorola.actions.utils.MALogger r7 = LOGGER
            java.lang.String r0 = "IO Exception"
            r7.mo11963w(r0)
        L_0x0071:
            if (r5 == 0) goto L_0x007e
            r5.close()     // Catch:{ IOException -> 0x0077 }
            goto L_0x007e
        L_0x0077:
            com.motorola.actions.utils.MALogger r7 = LOGGER
            java.lang.String r0 = "IO Exception"
            r7.mo11963w(r0)
        L_0x007e:
            return r1
        L_0x007f:
            r7 = move-exception
        L_0x0080:
            if (r2 == 0) goto L_0x008d
            r2.close()     // Catch:{ IOException -> 0x0086 }
            goto L_0x008d
        L_0x0086:
            com.motorola.actions.utils.MALogger r0 = LOGGER
            java.lang.String r1 = "IO Exception"
            r0.mo11963w(r1)
        L_0x008d:
            if (r5 == 0) goto L_0x009a
            r5.close()     // Catch:{ IOException -> 0x0093 }
            goto L_0x009a
        L_0x0093:
            com.motorola.actions.utils.MALogger r0 = LOGGER
            java.lang.String r1 = "IO Exception"
            r0.mo11963w(r1)
        L_0x009a:
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.motorola.actions.approach.p006us.USFirmwareUpdater.writeFileSystem():boolean");
    }

    /* access modifiers changed from: private */
    public boolean isVerified() {
        return this.mDataFile.exists() && SharedPreferenceManager.getString(KEY_US_FIRMWARE_VERSION, "").equals(Constants.getApkVersion(this.mContext));
    }

    /* access modifiers changed from: private */
    public void storeUnvalidated() {
        SharedPreferenceManager.putString(KEY_US_FIRMWARE_VERSION, "");
    }

    /* access modifiers changed from: private */
    @SuppressLint({"SetWorldReadable"})
    public boolean prepareDspFwDir() {
        boolean z = this.mDataDir.exists() && this.mDataDir.canExecute() && this.mDataDir.canRead() && this.mDataDir.canWrite();
        if (!z) {
            z = this.mDataDir.mkdir();
            if (z) {
                this.mDataDir.setExecutable(true, false);
                this.mDataDir.setReadable(true, false);
            }
        }
        return z;
    }
}
