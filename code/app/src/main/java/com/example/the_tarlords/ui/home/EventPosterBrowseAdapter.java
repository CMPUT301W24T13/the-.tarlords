package com.example.the_tarlords.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.the_tarlords.R;
import com.example.the_tarlords.data.event.Event;
import com.example.the_tarlords.data.photo.EventPoster;

import java.util.ArrayList;

public class EventPosterBrowseAdapter extends ArrayAdapter<Event> {
    private ArrayList<Event> eventList;
    private LayoutInflater inflater;
    public EventPosterBrowseAdapter(@NonNull Context context, ArrayList<Event> events) {
        super(context, 0, events);
        this.eventList = events;
        inflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup
            parent) {
        View view = convertView;
        ViewHolder holder;

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_image_list_item, parent, false);
            holder = new ViewHolder();

            //getting the textviews
            holder.posterImageView = view.findViewById(R.id.iv_image_browse_item);
            holder.eventNameTextView = view.findViewById(R.id.tv_imageBrowse_title);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Event event = (Event) eventList.get(position);
        EventPoster eventPoster = event.getPoster();

        if (eventPoster != null) {
            ImageView posterView = view.findViewById(R.id.iv_image_browse_item);
            posterView.setImageBitmap(eventPoster.getBitmap());
        }

        TextView name = view.findViewById(R.id.tv_imageBrowse_title);
        name.setText(event.getName());
        TextView location = view.findViewById(R.id.tv_imageBrowse_subtitle);
        location.setText(event.getLocation());
        TextView startDate = view.findViewById(R.id.tv_imageBrowse_subtext);
        startDate.setText(event.getStartDate());

        return view;
    }

    static class ViewHolder {
        ImageView posterImageView;
        TextView eventNameTextView;
    }

}