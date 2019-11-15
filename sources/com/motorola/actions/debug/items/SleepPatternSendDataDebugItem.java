package com.motorola.actions.debug.items;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.JsonWriter;
import android.widget.Toast;
import com.google.android.gms.common.util.CrashUtils.ErrorDialogData;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.C0504R;
import com.motorola.actions.sleepPattern.common.TimeSlot;
import com.motorola.actions.sleepPattern.repository.SleepPatternRepository;
import com.motorola.actions.sleepPattern.repository.entities.AccelEntity;
import com.motorola.actions.sleepPattern.repository.entities.PreprocessingEntity;
import com.motorola.actions.sleepPattern.repository.entities.PreviewEntity;
import com.motorola.actions.utils.MALogger;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.List;

public class SleepPatternSendDataDebugItem extends DebugItem {
    private static final String ACCEL_DAILY_JSON_ENTRY = "daily";
    private static final String ACCEL_JSON_ENTRY = "accelerometer";
    private static final String ACCEL_TIME_SLOT_RESULT_JSON_ENTRY = "time_stamp_slot";
    private static final String ACCEL_VALUE_JSON_ENTRY = "value";
    private static final MALogger LOGGER = new MALogger(SleepPatternSendDataDebugItem.class);
    private static final String MIME_TYPE = "text/html";
    private static final String PREPROCESSING_JSON_ENTRY = "preprocessing";
    private static final String PREPROCESSING_RESULT_JSON_ENTRY = "result";
    private static final String PREVIEW_JSON_ENTRY = "preview";
    private static final String READABLE_TIME_SLOT_RESULT_JSON_ENTRY = "readable_time_slot";
    private static final String SEND_TO_ADDRESS = "motoactions.test@gmail.com";
    private static final String SPACING = "   ";
    private static final String TYPE_JSON_ENTRY = "type";
    private String mContent;
    private final SleepPatternRepository mSleepPatternRepository;

    public SleepPatternSendDataDebugItem(@NonNull SleepPatternRepository sleepPatternRepository) {
        this.mSleepPatternRepository = sleepPatternRepository;
        setTitle(getString(C0504R.string.debug_sp_send_db_title));
        setDescription(getString(C0504R.string.debug_sp_send_db_description));
    }

    /* access modifiers changed from: protected */
    public void startBackgroundWork() {
        super.startBackgroundWork();
        try {
            if (this.mSleepPatternRepository != null) {
                this.mContent = getSleepPatternDataJSON(this.mSleepPatternRepository);
            }
        } catch (IOException e) {
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("Error getting sleep pattern data: ");
            sb.append(e);
            mALogger.mo11959e(sb.toString());
        }
    }

    /* access modifiers changed from: protected */
    public void startPostExecuteWork() {
        super.startPostExecuteWork();
        Context appContext = ActionsApplication.getAppContext();
        if (this.mContent != null) {
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType(MIME_TYPE);
            intent.setFlags(ErrorDialogData.BINDER_CRASH);
            intent.putExtra("android.intent.extra.EMAIL", new String[]{SEND_TO_ADDRESS});
            intent.putExtra("android.intent.extra.SUBJECT", ActionsApplication.getAppContext().getString(C0504R.string.debug_sp_send_db_email_subject));
            intent.putExtra("android.intent.extra.TEXT", this.mContent);
            appContext.startActivity(Intent.createChooser(intent, ActionsApplication.getAppContext().getString(C0504R.string.debug_sp_send_db_finished)));
            return;
        }
        Toast.makeText(appContext, C0504R.string.debug_sp_send_db_error, 0).show();
    }

    private String getSleepPatternDataJSON(@NonNull SleepPatternRepository sleepPatternRepository) throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
        jsonWriter.setIndent(SPACING);
        jsonWriter.beginObject();
        jsonWriter.name(PREPROCESSING_JSON_ENTRY);
        jsonWriter.beginArray();
        writePreprocessing(jsonWriter, sleepPatternRepository.getPreprocessingResult(1));
        writePreprocessing(jsonWriter, sleepPatternRepository.getPreprocessingResult(2));
        writePreprocessing(jsonWriter, sleepPatternRepository.getPreprocessingResult(3));
        writePreprocessing(jsonWriter, sleepPatternRepository.getPreprocessingResult(4));
        jsonWriter.endArray();
        jsonWriter.name(PREVIEW_JSON_ENTRY);
        jsonWriter.beginArray();
        writePreviewResult(jsonWriter, sleepPatternRepository.getPreviewResult(1), 1);
        writePreviewResult(jsonWriter, sleepPatternRepository.getPreviewResult(2), 2);
        writePreviewResult(jsonWriter, sleepPatternRepository.getPreviewResult(3), 3);
        writePreviewResult(jsonWriter, sleepPatternRepository.getPreviewResult(4), 4);
        jsonWriter.endArray();
        List<AccelEntity> accelerometerLogs = sleepPatternRepository.getAccelerometerLogs();
        jsonWriter.name(ACCEL_JSON_ENTRY);
        jsonWriter.beginArray();
        for (AccelEntity writeAccel : accelerometerLogs) {
            writeAccel(jsonWriter, writeAccel);
        }
        jsonWriter.endArray();
        jsonWriter.endObject();
        jsonWriter.close();
        return stringWriter.toString();
    }

    private void writePreprocessing(@NonNull JsonWriter jsonWriter, @Nullable PreprocessingEntity preprocessingEntity) throws IOException {
        if (preprocessingEntity != null) {
            jsonWriter.beginObject();
            jsonWriter.name("type").value((long) preprocessingEntity.getType());
            jsonWriter.name("result").value(preprocessingEntity.getResults());
            jsonWriter.endObject();
        }
    }

    private void writeAccel(@NonNull JsonWriter jsonWriter, @Nullable AccelEntity accelEntity) throws IOException {
        if (accelEntity != null) {
            jsonWriter.beginObject();
            jsonWriter.name(ACCEL_TIME_SLOT_RESULT_JSON_ENTRY).value(accelEntity.getTimeSlotId());
            jsonWriter.name(READABLE_TIME_SLOT_RESULT_JSON_ENTRY).value(new TimeSlot(Calendar.getInstance().getTimeZone(), accelEntity.getTimeSlotId()).getHalfTime().getTime().toString());
            jsonWriter.name("value").value((long) accelEntity.getEventCount());
            jsonWriter.name(ACCEL_DAILY_JSON_ENTRY).value(!accelEntity.getDebug());
            jsonWriter.endObject();
        }
    }

    private void writePreviewResult(@NonNull JsonWriter jsonWriter, @Nullable Calendar calendar, int i) throws IOException {
        if (calendar != null) {
            jsonWriter.beginObject();
            jsonWriter.name("type").value((long) i);
            jsonWriter.name(PreviewEntity.COLUMN_RESULTS_NAME).value(calendar.getTimeInMillis());
            jsonWriter.name(READABLE_TIME_SLOT_RESULT_JSON_ENTRY).value(calendar.getTime().toString());
            jsonWriter.endObject();
        }
    }
}
