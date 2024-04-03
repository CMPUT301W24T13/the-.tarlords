package com.example.the_tarlords.ui.event;

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

public class EventPosterArrayAdapter extends ArrayAdapter<Event> {
    private ArrayList<Event> eventList;
    private LayoutInflater inflater;

    public EventPosterArrayAdapter(Context context, ArrayList<Event> events) {
        super(context, 0, events);
        this.eventList = events;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_image_list_item, parent, false);
            holder = new ViewHolder();

            holder.imageViewTV = view.findViewById(R.id.iv_image_browse_item);
            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }

        Event event = eventList.get(position);
        EventPoster poster = event.getPoster();

        //ImageView posterView = view.findViewById(R.id.iv_image_browse_item);
        //posterView.setImageBitmap(poster.getBitmap());

        /*TextView eventName = view.findViewById(R.id.tv_event_title);
        eventName.setText(event.getName());
        TextView eventLocation = view.findViewById(R.id.tv_location);
        eventLocation.setText(event.getLocation());
        TextView eventStartDate = view.findViewById(R.id.tv_start_date);
        eventStartDate.setText(event.getStartDate());*/

        return view;
    }

    static class ViewHolder {
        ImageView imageViewTV;
        TextView collectionTV;
        TextView nameTV;
    }

}
