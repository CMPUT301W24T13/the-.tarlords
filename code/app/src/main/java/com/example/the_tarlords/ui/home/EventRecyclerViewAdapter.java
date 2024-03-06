package com.example.the_tarlords.ui.home;


import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.view.View;

import com.example.the_tarlords.placeholder.PlaceholderEventContent.PlaceholderEvent;
import com.example.the_tarlords.R;
import com.example.the_tarlords.data.event.Event;
import com.example.the_tarlords.databinding.FragmentEventListItemBinding;
import com.example.the_tarlords.ui.event.EventDetailsFragment;

import org.checkerframework.checker.units.qual.A;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a Event.
 * TODO: Replace the implementation with code for your data type.
 */
public class EventRecyclerViewAdapter extends RecyclerView.Adapter<EventRecyclerViewAdapter.ViewHolder> {

    //private EventList events = new EventList();
    private ArrayList<Event> events = new ArrayList<>();
    //private final OnItemClickListener clickListener;

    public interface OnItemClickListener {
        void onItemClick(Event event);

    }



    public EventRecyclerViewAdapter(ArrayList<Event> events) { //, OnItemClickListener listener
        this.events = events;
        //this.clickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FragmentEventListItemBinding binding = FragmentEventListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        //holder.eventId.setText(events.get(position).getId()); //this would display the id but we don't need that
        holder.eventName.setText(events.get(position).getName());
        holder.eventLocation.setText(events.get(position).getLocation());
    }

    @Override
    public int getItemCount() {
        if (events ==null) {
            return 0;
        }
        else {
            return events.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //public final TextView eventId;
        public final TextView eventName;
        public final TextView eventLocation;

        public ViewHolder(FragmentEventListItemBinding binding) {
            super(binding.getRoot());
            //eventId = binding.textviewEventId;
            eventName = binding.titleTextView;
            eventLocation = binding.dateTextView;

            /*itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Event clickedEvent = events.get(position);
                    clickListener.onItemClick(clickedEvent);
                }
            });*/

        }

        @Override
        public String toString() {
            return super.toString() + " '" + eventName.getText() + "'";
        }
    }
}