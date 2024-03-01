package com.example.the_tarlords.ui.home;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.the_tarlords.R;
import com.example.the_tarlords.data.event.Event;
import com.example.the_tarlords.data.event.EventList;
import com.example.the_tarlords.placeholder.PlaceholderContent;
import com.example.the_tarlords.ui.event.EventDetailsFragment;

import java.util.List;

/**
 * A fragment representing a list of Event items
 */
public class EventListFragment extends Fragment implements EventRecyclerViewAdapter.OnItemClickListener{

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    /**
     * EventListFragment has a list of events called eventsList
     */
    private EventList eventsList;
    private EventRecyclerViewAdapter.OnItemClickListener listener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EventListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static EventListFragment newInstance(int columnCount) {
        EventListFragment fragment = new EventListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new EventRecyclerViewAdapter(eventsList, listener));
        }
        return view;
    }

    /**
     * This method receives the event the user clicked on in the recycler view
     * Right now it automatically takes the user to the event details page
     * assumming the EventList is an attendee type of List
     * @param event
     */

    @Override
    public void onItemClick(Event event) {
        // Handle item click, switch to a new fragment using FragmentManager
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Create a new fragment and pass the selected Event as an argument
        EventDetailsFragment newFragment = EventDetailsFragment.newInstance(event);

        // Replace the current fragment with the new one
        transaction.replace(R.id.nav_host_fragment_content_main, newFragment);

        // Add the transaction to the back stack (optional)
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }
}