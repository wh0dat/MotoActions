package com.motorola.actions.p013ui.settings;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import com.motorola.actions.microScreen.model.MicroScreenModel;

/* renamed from: com.motorola.actions.ui.settings.MicroScreenSettingsFragment$$Lambda$1 */
final /* synthetic */ class MicroScreenSettingsFragment$$Lambda$1 implements OnClickListener {
    static final OnClickListener $instance = new MicroScreenSettingsFragment$$Lambda$1();

    private MicroScreenSettingsFragment$$Lambda$1() {
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        MicroScreenModel.saveMicroScreenEnabled(false);
    }
}
