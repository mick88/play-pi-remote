package com.michaldabski.radiopiremote.queue;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.michaldabski.radiopiremote.R;
import com.michaldabski.radiopiremote.api.models.BaseMpdModel;

import java.util.List;

/**
 * Created by Michal on 31/10/2016.
 */

public class QueueAdapter extends ArrayAdapter<BaseMpdModel> {
    Integer currentMpdId = null;

    public QueueAdapter(Context context, List<BaseMpdModel> items) {
        super(context, android.R.layout.simple_list_item_1, items);
    }

    public void setCurrentMpdId(Integer currentMpdId) {
        this.currentMpdId = currentMpdId;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        final View view = super.getView(position, convertView, parent);
        if (currentMpdId != null) {
            final TextView textView = (TextView) view.findViewById(android.R.id.text1);
            final BaseMpdModel item = getItem(position);
            if (currentMpdId.equals(item.getMpdId())) {
                textView.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_play, 0, 0, 0);
            } else {
                textView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
            }
        }
        return view;
    }
}
