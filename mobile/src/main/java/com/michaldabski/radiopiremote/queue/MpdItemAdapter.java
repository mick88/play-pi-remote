package com.michaldabski.radiopiremote.queue;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.michaldabski.radiopiremote.R;
import com.michaldabski.radiopiremote.api.models.BaseMpdModel;
import com.michaldabski.radiopiremote.api.models.RadioStation;
import com.michaldabski.radiopiremote.api.models.Track;

import java.util.List;

import static com.michaldabski.radiopiremote.R.id.imgArt;
import static com.michaldabski.radiopiremote.R.id.imgPlay;
import static com.michaldabski.radiopiremote.R.id.tvArtist;
import static com.michaldabski.radiopiremote.R.id.tvTitle;

/**
 * Created by Michal on 31/10/2016.
 */

public class MpdItemAdapter extends ArrayAdapter<BaseMpdModel> {
    private final ImageLoader imageLoader;

    private static class ViewHolder {
        NetworkImageView imgArt;
        ImageView imgPlay;
        TextView tvTitle;
        TextView tvArtist;
    }

    private static final int
            VIEW_TYPE_TRACK = 0,
            VIEW_TYPE_RADIO = 1,
            VIEW_TYPE_COUNT = 2;
    Integer currentMpdId = null;

    public MpdItemAdapter(Context context, List<BaseMpdModel> items, ImageLoader imageLoader) {
        super(context, 0, items);
        this.imageLoader = imageLoader;
    }

    public void setCurrentMpdId(Integer currentMpdId) {
        this.currentMpdId = currentMpdId;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        final BaseMpdModel item = getItem(position);
        if (item instanceof RadioStation) {
            return VIEW_TYPE_RADIO;
        } else {
            return VIEW_TYPE_TRACK;
        }
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getMpdId();
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        final ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_queue, parent, false);
            view.setTag(viewHolder = new ViewHolder());
            viewHolder.tvArtist = (TextView) view.findViewById(tvArtist);
            viewHolder.tvTitle = (TextView) view.findViewById(tvTitle);
            viewHolder.imgArt = (NetworkImageView) view.findViewById(imgArt);
            viewHolder.imgPlay = (ImageView) view.findViewById(imgPlay);

            switch (getItemViewType(position)) {
                case VIEW_TYPE_RADIO:
                    viewHolder.imgArt.setDefaultImageResId(R.drawable.ic_item_radio);
                    viewHolder.tvTitle.setVisibility(View.GONE);
                    break;
                case VIEW_TYPE_TRACK:
                    viewHolder.imgArt.setDefaultImageResId(R.drawable.ic_item_track);
                    break;
            }
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final BaseMpdModel item = getItem(position);

        switch (getItemViewType(position)) {
            case VIEW_TYPE_TRACK:
                final Track track = (Track) item;
                viewHolder.tvArtist.setText(track.getArtist().toString());
                viewHolder.tvTitle.setText(track.getName());
                break;

            case VIEW_TYPE_RADIO:
                RadioStation radioStation = (RadioStation) item;
                viewHolder.tvArtist.setText(radioStation.getName());
                break;
        }
        if (currentMpdId != null && currentMpdId == item.getMpdId()) {
            viewHolder.imgPlay.setVisibility(View.VISIBLE);
            viewHolder.imgArt.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.imgPlay.setVisibility(View.GONE);
            viewHolder.imgArt.setVisibility(View.VISIBLE);
        }

        if (item instanceof Track) {
            Track track = (Track) item;
            if (track.getAlbum() != null) {
                String artUrl = track.getAlbum().getArtUrl();
                viewHolder.imgArt.setImageUrl(artUrl, imageLoader);
            }
        }

        return view;
    }
}
