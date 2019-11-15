package com.motorola.actions.utils;

public class MotoSurveyUtils {
    private static final String MOTO_SURVEY_PACKAGE = "com.motorola.survey";

    public static void sendIntent(String str) {
        MotoResearchUtils.sendIntent(MOTO_SURVEY_PACKAGE, str);
    }
}
