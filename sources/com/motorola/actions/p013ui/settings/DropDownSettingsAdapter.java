package com.motorola.actions.p013ui.settings;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.motorola.actions.C0504R;
import java.util.ArrayList;

/* renamed from: com.motorola.actions.ui.settings.DropDownSettingsAdapter */
public class DropDownSettingsAdapter extends ArrayAdapter<DropDownSetting> {
    DropDownSettingsAdapter(Context context, ArrayList<DropDownSetting> arrayList) {
        super(context, 0, arrayList);
    }

    @NonNull
    public View getView(int i, @Nullable View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(C0504R.layout.item_dropdown_settings, viewGroup, false);
        }
        DropDownSetting dropDownSetting = (DropDownSetting) getItem(i);
        if (dropDownSetting != null) {
            TextView textView = (TextView) view.findViewById(C0504R.C0506id.description);
            ((TextView) view.findViewById(C0504R.C0506id.title)).setText(dropDownSetting.getTitle());
            textView.setText(dropDownSetting.getDescription());
            view.setTag(Integer.valueOf(dropDownSetting.getId()));
        }
        return view;
    }
}
