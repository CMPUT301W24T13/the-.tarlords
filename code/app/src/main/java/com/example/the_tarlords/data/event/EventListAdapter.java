package com.example.the_tarlords.data.event;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.the_tarlords.R;

import java.util.ArrayList;

public class EventListAdapter extends ArrayAdapter<Event> {

    public EventListAdapter(Context context, ArrayList<Event> events) {super(context, 0, events);}

    public EventListAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(super.getContext()).inflate(R.layout.content_events, parent, false);
        } else {
            view = convertView;
        }
        Event event = super.getItem(position);
        TextView name = view.findViewById(R.id.name_text);
        TextView location = view.findViewById(R.id.location_text);

        name.setText(event.getName());
        location.setText(event.getLocation());

        return view;
    }

}