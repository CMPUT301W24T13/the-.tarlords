/*
package com.example.the_tarlords.ui.home;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.the_tarlords.data.event.Event;
import com.example.the_tarlords.databinding.FragmentEventListItemBinding;

import java.util.ArrayList;

*/
/**
 * {@link RecyclerView.Adapter} that can display a Event.
 * TODO: Replace the implementation with code for your data type.
 *//*

public class EventRecyclerViewAdapter extends RecyclerView.Adapter<EventRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<Event> events;
    private final OnItemClickListener clickListener;

    public interface OnItemClickListener {
        void onItemClick(Event event);

    }


    public EventRecyclerViewAdapter(ArrayList<Event> items, OnItemClickListener listener) {
        events = items;
        clickListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FragmentEventListItemBinding binding = FragmentEventListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new RecyclerView.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EventRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.eventName.setText(events.get(position).getName());
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        //holder.eventId.setText(events.get(position).getId()); //this would display the id but we don't need that
        holder.eventName.setText(events.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //public final TextView eventId;
        public final TextView eventName;

        public ViewHolder(FragmentEventListItemBinding binding) {
            super(binding.getRoot());
            //eventId = binding.textviewEventId;
            eventName = binding.titleTextView;

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
}*/
