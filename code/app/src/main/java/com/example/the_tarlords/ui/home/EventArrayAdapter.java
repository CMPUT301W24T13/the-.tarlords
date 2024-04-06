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

import java.util.ArrayList;

/**
 * Adapter for ListView in EventListFragment
 */
public class EventArrayAdapter extends ArrayAdapter<Event> {
    public EventArrayAdapter(@NonNull Context context,ArrayList<Event> events) {
        super(context, 0, events);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup
            parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_event_list_item, parent, false);
        } else {
            view = convertView;
        }
        Event event = (Event) getItem(position);
        TextView name = view.findViewById(R.id.tv_eventList_title);
        name.setText(event.getName());
        TextView location = view.findViewById(R.id.tv_eventList_location);
        location.setText(event.getLocation());
        TextView startDate = view.findViewById(R.id.tv_eventList_start_date);
        startDate.setText(event.getStartDate());
        ImageView poster = view.findViewById(R.id.iv_event_poster_list);
        if (event.getPoster()!=null){
            poster.setImageBitmap(event.getPoster().getBitmap());
        }
        return view;
    }
}