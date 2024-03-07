/*
package com.example.the_tarlords.ui.event;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.the_tarlords.R;
import com.example.the_tarlords.data.event.Event;

import java.util.ArrayList;

public class EventOrganizerArrayAdapter extends ArrayAdapter<Event> {
    public EventOrganizerArrayAdapter(@NonNull Context context,ArrayList<Event> events) {
        super(context, 0, events);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup
            parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_event_organizer_list_item, parent, false);
        } else {
            view = convertView;
        }
        Event event = (Event) getItem(position);
        TextView name = view.findViewById(R.id.titleOrganizerTextView);
        name.setText(event.getName());
        TextView location = view.findViewById(R.id.dateOrganizerTextView);
        location.setText(event.getLocation());
        TextView status = view.findViewById(R.id.userRoleOrganizerTextView);
        status.setText("Organizer");
        return view;
    }
}
*/
