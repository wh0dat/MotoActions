package com.motorola.actions.approach.p005ir.tuning;

import com.motorola.actions.approach.p005ir.SensorHub;
import com.motorola.actions.approach.p005ir.tuning.Tuning6.IRTuningParameters;

/* renamed from: com.motorola.actions.approach.ir.tuning.Tuning */
public class Tuning {
    private IRTuningParameters mParams6 = null;
    private Tuning7.IRTuningParameters mParams7 = null;

    /* renamed from: com.motorola.actions.approach.ir.tuning.Tuning$VersionNotFoundException */
    public static class VersionNotFoundException extends Exception {
        public VersionNotFoundException() {
        }

        public VersionNotFoundException(String str) {
            super(str);
        }

        public VersionNotFoundException(Throwable th) {
            super(th);
        }

        public VersionNotFoundException(String str, Throwable th) {
            super(str, th);
        }
    }

    public void configure() {
        this.mParams6 = Tuning6.getParams();
        this.mParams7 = Tuning7.getParams();
        SensorHub.setIRTuning(this);
    }

    public boolean applyConfig(int i) throws VersionNotFoundException {
        if (i == 6) {
            return Tuning6.setIRTuning(this.mParams6);
        }
        if (i == 7) {
            return Tuning7.setIRTuning(this.mParams7);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Version ");
        sb.append(i);
        sb.append(" not known.  Only 6 to 7");
        throw new VersionNotFoundException(sb.toString());
    }
}
