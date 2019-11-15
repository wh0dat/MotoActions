package com.motorola.actions.utils;

import android.os.Process;
import com.motorola.actions.ActionsApplication;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LogRecorder {
    private boolean mIsLocalLoggerActive;
    private Process mLogCatProcess;

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final LogRecorder INSTANCE = new LogRecorder();

        private SingletonHolder() {
        }
    }

    public static LogRecorder getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static String createLoggingFile() {
        String format = new SimpleDateFormat("'motoactions_'yyyyMMddHHmm'.txt'", Locale.US).format(new Date());
        try {
            ActionsApplication.getAppContext().openFileOutput(format, 0).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return format;
    }

    public void startRecording() {
        if (!this.mIsLocalLoggerActive && this.mLogCatProcess == null) {
            try {
                this.mLogCatProcess = Runtime.getRuntime().exec(String.format(Locale.US, "logcat --pid=%d -f %s/%s", new Object[]{Integer.valueOf(Process.myPid()), ActionsApplication.getAppContext().getFilesDir().getAbsolutePath(), createLoggingFile()}));
                this.mIsLocalLoggerActive = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopRecording() {
        if (this.mIsLocalLoggerActive && this.mLogCatProcess != null) {
            this.mLogCatProcess.destroy();
            this.mLogCatProcess = null;
            this.mIsLocalLoggerActive = false;
        }
    }
}
