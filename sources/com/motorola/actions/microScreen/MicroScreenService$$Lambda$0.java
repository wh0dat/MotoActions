package com.motorola.actions.microScreen;

import android.widget.Toast;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.C0504R;

final /* synthetic */ class MicroScreenService$$Lambda$0 implements Runnable {
    static final Runnable $instance = new MicroScreenService$$Lambda$0();

    private MicroScreenService$$Lambda$0() {
    }

    public void run() {
        Toast.makeText(ActionsApplication.getAppContext(), C0504R.string.sh_cannot_shrink, 0).show();
    }
}
