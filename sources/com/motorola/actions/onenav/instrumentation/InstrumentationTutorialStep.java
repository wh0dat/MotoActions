package com.motorola.actions.onenav.instrumentation;

import com.motorola.actions.checkin.CommonCheckinAttributes;

public enum InstrumentationTutorialStep {
    HOME("h"),
    BACK("b"),
    SWITCH(CommonCheckinAttributes.KEY_DAILY_ENABLE_SOURCE_TYPE_SETTINGS),
    RECENTS("r"),
    NOW_ON_TAP("n"),
    OFF("off"),
    ON("on");
    
    private String mCode;

    private InstrumentationTutorialStep(String str) {
        this.mCode = str;
    }

    public String getCode() {
        return this.mCode;
    }

    public static InstrumentationTutorialStep fromCode(String str) {
        InstrumentationTutorialStep[] values;
        for (InstrumentationTutorialStep instrumentationTutorialStep : values()) {
            if (instrumentationTutorialStep.mCode.equalsIgnoreCase(str)) {
                return instrumentationTutorialStep;
            }
        }
        return null;
    }
}
