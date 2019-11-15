package com.motorola.actions.checkin;

import android.support.annotation.NonNull;

public interface CheckinContainer {
    void setValue(@NonNull String str, double d);

    void setValue(@NonNull String str, int i);

    void setValue(@NonNull String str, String str2);

    void setValue(@NonNull String str, boolean z);
}
