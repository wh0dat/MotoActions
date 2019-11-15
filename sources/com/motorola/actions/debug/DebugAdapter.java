package com.motorola.actions.debug;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.motorola.actions.C0504R;
import com.motorola.actions.debug.items.DebugItem;
import java.util.List;

class DebugAdapter extends ArrayAdapter<DebugItem> {

    private static class ViewHolder {
        TextView mDescription;
        TextView mTitle;

        private ViewHolder() {
        }
    }

    DebugAdapter(Context context, int i, List<DebugItem> list) {
        super(context, i, list);
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        View view2;
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view2 = ((Activity) getContext()).getLayoutInflater().inflate(C0504R.layout.debug_item, viewGroup, false);
            viewHolder.mTitle = (TextView) view2.findViewById(C0504R.C0506id.debug_item_title);
            viewHolder.mDescription = (TextView) view2.findViewById(C0504R.C0506id.debug_item_description);
            view2.setTag(viewHolder);
        } else {
            view2 = view;
            viewHolder = (ViewHolder) view.getTag();
        }
        DebugItem debugItem = (DebugItem) getItem(i);
        if (debugItem != null) {
            viewHolder.mTitle.setText(debugItem.getTitle());
            viewHolder.mDescription.setText(debugItem.getDescription());
        }
        return view2;
    }
}
