package com.michaldabski.radiopiremote.queue;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.michaldabski.radiopiremote.R;
import com.michaldabski.radiopiremote.api.models.BaseMpdModel;
import com.michaldabski.radiopiremote.api.models.Track;

import java.util.List;

import static com.michaldabski.radiopiremote.R.id.imgArt;
import static com.michaldabski.radiopiremote.R.id.tvTitle;

/**
 * Created by Michal on 31/10/2016.
 */

public class QueueAdapter extends ArrayAdapter<BaseMpdModel> {
    private static class ViewHolder {
        ImageView imgArt;
        TextView tvTitle;
    }

    Integer currentMpdId = null;

    public QueueAdapter(Context context, List<BaseMpdModel> items) {
        super(context, 0, items);
    }

    public void setCurrentMpdId(Integer currentMpdId) {
        this.currentMpdId = currentMpdId;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        final ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_queue, parent, false);
            view.setTag(viewHolder = new ViewHolder());
            viewHolder.tvTitle = (TextView) view.findViewById(tvTitle);
            viewHolder.imgArt = (ImageView) view.findViewById(imgArt);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final BaseMpdModel item = getItem(position);


        viewHolder.tvTitle.setText(item.getName());
        if (currentMpdId != null && currentMpdId == item.getMpdId()) {
            viewHolder.imgArt.setImageResource(R.drawable.ic_play);
        } else {
            viewHolder.imgArt.setImageResource(item instanceof Track ? R.drawable.ic_track : R.drawable.ic_radio);
        }

        return view;
    }
}
