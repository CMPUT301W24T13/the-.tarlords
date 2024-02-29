package com.example.the_tarlords.ui.home;


import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.view.View;

import com.example.the_tarlords.R;
import com.example.the_tarlords.data.event.Event;
import com.example.the_tarlords.databinding.FragmentEventListItemBinding;
import com.example.the_tarlords.ui.event.EventDetailsFragment;

import java.io.Serializable;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a Event.
 * TODO: Replace the implementation with code for your data type.
 */
public class EventRecyclerViewAdapter extends RecyclerView.Adapter<EventRecyclerViewAdapter.ViewHolder> {

    private final List<Event> events;
    private final OnItemClickListener clickListener;

    public interface OnItemClickListener {
        void onItemClick(Event event);
    }



    public EventRecyclerViewAdapter(List<Event> items, OnItemClickListener listener) {
        events = items;
        clickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FragmentEventListItemBinding binding = FragmentEventListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.eventId.setText(events.get(position).getId());
        holder.eventName.setText(events.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return events.size();
    }





    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView eventId;
        public final TextView eventName;

        public ViewHolder(FragmentEventListItemBinding binding) {
            super(binding.getRoot());
            eventId = binding.textviewEventId;
            eventName = binding.textviewEventName;

            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Event clickedEvent = events.get(position);
                    clickListener.onItemClick(clickedEvent);
                }
            });
        }

        @Override
        public String toString() {
            return super.toString() + " '" + eventName.getText() + "'";
        }
    }
}