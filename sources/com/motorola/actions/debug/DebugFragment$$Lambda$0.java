package com.motorola.actions.debug;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import java.util.ArrayList;

final /* synthetic */ class DebugFragment$$Lambda$0 implements OnItemClickListener {
    private final DebugFragment arg$1;
    private final ArrayList arg$2;

    DebugFragment$$Lambda$0(DebugFragment debugFragment, ArrayList arrayList) {
        this.arg$1 = debugFragment;
        this.arg$2 = arrayList;
    }

    public void onItemClick(AdapterView adapterView, View view, int i, long j) {
        this.arg$1.lambda$onCreateView$0$DebugFragment(this.arg$2, adapterView, view, i, j);
    }
}
