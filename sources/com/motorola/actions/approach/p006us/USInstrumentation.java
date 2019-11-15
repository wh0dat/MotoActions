package com.motorola.actions.approach.p006us;

import com.motorola.actions.approach.p006us.AdspdLib.UltrasoundConfigListener.Result;
import com.motorola.actions.approach.p006us.AdspdLib.UltrasoundInstrumentationListener;
import com.motorola.actions.checkin.CheckinAlarm;
import com.motorola.actions.utils.MALogger;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/* renamed from: com.motorola.actions.approach.us.USInstrumentation */
public class USInstrumentation implements UltrasoundInstrumentationListener {
    private static final MALogger LOGGER = new MALogger(USInstrumentation.class);
    private USAnalytics mAnalytics = ((USAnalytics) CheckinAlarm.getInstance().getAnalytics(USAnalytics.class));

    public USInstrumentation() {
        this.mAnalytics.setLastInstrumentationRetrieveTime();
    }

    public void onFinishUltrasoundConfig(String str, Result result) {
        switch (result) {
            case Success:
                MALogger mALogger = LOGGER;
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append(": successful");
                mALogger.mo11957d(sb.toString());
                return;
            case ConnectionFailure:
                MALogger mALogger2 = LOGGER;
                StringBuilder sb2 = new StringBuilder();
                sb2.append(str);
                sb2.append(": Error creating socket connection");
                mALogger2.mo11959e(sb2.toString());
                return;
            case ControlError:
                MALogger mALogger3 = LOGGER;
                StringBuilder sb3 = new StringBuilder();
                sb3.append(str);
                sb3.append(": Error calling control function");
                mALogger3.mo11959e(sb3.toString());
                return;
            default:
                LOGGER.mo11959e("Unexpected result on onFinishUltrasoundConfig()");
                return;
        }
    }

    public void onInstrumentationRetrieve(int i, int i2, int i3, int i4) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("Got US instrumentation data: ");
        sb.append(i);
        sb.append(" ");
        sb.append(i2);
        sb.append(" ");
        sb.append(i3);
        sb.append(" ");
        sb.append(i4);
        mALogger.mo11957d(sb.toString());
        if (Calendar.getInstance().getTimeInMillis() - this.mAnalytics.getLastInstrumentationRetrieveTime() < TimeUnit.DAYS.toMillis(1)) {
            LOGGER.mo11957d("Less than one day, record data!");
            this.mAnalytics.recordNoMotionNoInterference(i);
            this.mAnalytics.recordMotionNoInterference(i2);
            this.mAnalytics.recordNoMotionBeforeInterference(i3);
            this.mAnalytics.recordMotionBeforeInterference(i4);
        }
        this.mAnalytics.setLastInstrumentationRetrieveTime();
    }

    public void retrieveInstrumentation(boolean z) {
        if (z) {
            AdspdLib.retrieveInstrumentation(this);
        }
    }

    public void onStoppingUltrasound() {
        AdspdLib.retrieveInstrumentation(this);
    }
}
