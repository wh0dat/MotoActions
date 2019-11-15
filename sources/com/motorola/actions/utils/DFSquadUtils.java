package com.motorola.actions.utils;

import android.support.annotation.NonNull;

public class DFSquadUtils {
    public static final String DFSQUAD_PACKAGE_NAME = "com.motorola.dfpoints.android";
    public static final MALogger LOGGER = new MALogger(DFSquadUtils.class);

    public static void sendIntent(String str) {
        MotoResearchUtils.sendIntent(DFSQUAD_PACKAGE_NAME, str);
    }

    public static void sendIntent(String str, @NonNull String str2, @NonNull String str3) {
        MotoResearchUtils.sendIntent(DFSQUAD_PACKAGE_NAME, str, str2, str3);
    }
}
