package com.example.the_tarlords.ui.home;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.the_tarlords.placeholder.PlaceholderEventContent.PlaceholderEvent;
import com.example.the_tarlords.databinding.FragmentEventListItemBinding;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderEvent}.
 * TODO: Replace the implementation with code for your data type.
 */
public class EventRecyclerViewAdapter extends RecyclerView.Adapter<EventRecyclerViewAdapter.ViewHolder> {

    private final List<PlaceholderEvent> mEvents;

    public EventRecyclerViewAdapter(List<PlaceholderEvent> items) {
        mEvents = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentEventListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mEvent = mEvents.get(position);
        holder.mTitleView.setText(mEvents.get(position).eventTitle);
        holder.mDateView.setText(mEvents.get(position).date);
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mTitleView;
        public final TextView mDateView;
        public final TextView mEventUserStatus;
        public PlaceholderEvent mEvent;

        public ViewHolder(FragmentEventListItemBinding binding) {
            super(binding.getRoot());
            mTitleView = binding.titleTextView;
            mDateView = binding.dateTextView;
            mEventUserStatus = binding.userRoleTextView;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mDateView.getText() + "'";
        }
    }
}