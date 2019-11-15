package com.motorola.actions.debug.items;

import android.content.Intent;
import android.util.JsonWriter;
import com.google.android.gms.common.util.CrashUtils.ErrorDialogData;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.C0504R;
import com.motorola.actions.checkin.BaseAnalytics;
import com.motorola.actions.checkin.CheckinAlarm;
import com.motorola.actions.checkin.CommonCheckinAttributes;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.Map.Entry;

public class InstrumentationSendDataDebugItem extends DebugItem {
    private static final String MIME_TYPE = "text/html";
    private static final String NAME_JSON_ENTRY = "name";
    private static final String NEW_LINE = "\n";
    private static final String SEND_TO_ADDRESS = "motoactions.test@gmail.com";
    private static final String SEPARATOR = " = ";
    private static final String SPACING = "   ";
    private String mContent;

    public InstrumentationSendDataDebugItem() {
        setTitle(getString(C0504R.string.debug_instrumentation_title));
        setDescription(getString(C0504R.string.debug_instrumentation_description));
    }

    /* access modifiers changed from: protected */
    public void startBackgroundWork() {
        super.startBackgroundWork();
        try {
            this.mContent = getInstrumentationDataJSON();
        } catch (IOException unused) {
            this.mContent = getInstrumentationData();
        }
    }

    /* access modifiers changed from: protected */
    public void startPostExecuteWork() {
        super.startPostExecuteWork();
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType(MIME_TYPE);
        intent.setFlags(ErrorDialogData.BINDER_CRASH);
        intent.putExtra("android.intent.extra.EMAIL", new String[]{SEND_TO_ADDRESS});
        intent.putExtra("android.intent.extra.SUBJECT", getString(C0504R.string.debug_instrumentation_email_subject));
        intent.putExtra("android.intent.extra.TEXT", this.mContent);
        ActionsApplication.getAppContext().startActivity(Intent.createChooser(intent, getString(C0504R.string.debug_instrumentation_chooser_title)));
    }

    private String getInstrumentationData() {
        StringBuilder sb = new StringBuilder();
        for (BaseAnalytics baseAnalytics : CheckinAlarm.getInstance().getAnalyticsList()) {
            Map all = baseAnalytics.getDatastore(baseAnalytics.getDatastoreName()).getAll();
            sb.append(baseAnalytics.getDatastoreName());
            sb.append(NEW_LINE);
            sb.append(SPACING);
            sb.append(getString(C0504R.string.debug_instrumentation_feature_enabled));
            sb.append(SEPARATOR);
            sb.append(baseAnalytics.isFeatureEnabled());
            sb.append(NEW_LINE);
            for (Entry entry : all.entrySet()) {
                if (!((String) entry.getKey()).equals(CommonCheckinAttributes.KEY_ENABLED)) {
                    sb.append(SPACING);
                    sb.append((String) entry.getKey());
                    sb.append(SEPARATOR);
                    sb.append(entry.getValue().toString());
                    sb.append(NEW_LINE);
                }
            }
            sb.append(NEW_LINE);
        }
        return sb.toString();
    }

    private String getInstrumentationDataJSON() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
        jsonWriter.setIndent(SPACING);
        jsonWriter.beginArray();
        for (BaseAnalytics baseAnalytics : CheckinAlarm.getInstance().getAnalyticsList()) {
            Map all = baseAnalytics.getDatastore(baseAnalytics.getDatastoreName()).getAll();
            jsonWriter.beginObject();
            jsonWriter.name(NAME_JSON_ENTRY).value(baseAnalytics.getDatastoreName());
            jsonWriter.name(CommonCheckinAttributes.KEY_ENABLED).value(baseAnalytics.isFeatureEnabled());
            for (Entry entry : all.entrySet()) {
                if (!((String) entry.getKey()).equals(CommonCheckinAttributes.KEY_ENABLED)) {
                    jsonWriter.name((String) entry.getKey()).value(entry.getValue().toString());
                }
            }
            jsonWriter.endObject();
        }
        jsonWriter.endArray();
        jsonWriter.close();
        return stringWriter.toString();
    }
}
