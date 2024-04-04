package com.example.the_tarlords.ui.event;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.the_tarlords.MainActivity;
import com.example.the_tarlords.R;
import com.example.the_tarlords.data.event.Event;
import com.example.the_tarlords.data.event.EventListCallback;
import com.example.the_tarlords.data.event.EventListDBHelper;
import com.example.the_tarlords.ui.home.EventListFragment;

import java.util.ArrayList;

public class EventOrganizerListFragment extends EventListFragment implements MenuProvider {

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EventOrganizerListFragment() {
    }

    @Override
    public void navigateToDetails(Event event){
        Bundle args = new Bundle();
        args.putParcelable("event",event);
        args.putBoolean("isOrganizer", true);
        try {
            NavHostFragment.findNavController(EventOrganizerListFragment.this)
                    .navigate(R.id.action_eventOrganizerListFragment_to_eventDetailsFragment,args);
        } catch (Exception ignore) {}
    }
    @Override
    public void refreshList(){
        EventListDBHelper.getEventsOrganizingList(MainActivity.user, new EventListCallback() {
            @Override
            public void onEventListLoaded(ArrayList<Event> eventList) {
                events.clear();
                events.addAll(eventList);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        if (isAdded() && getContext() != null) {
            menu.clear();
            menuInflater.inflate(R.menu.options_menu, menu);
            menu.findItem(R.id.addOptionsMenu).setVisible(true);
            menu.findItem(R.id.editOptionsMenu).setVisible(false);
        }
    }


    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.addOptionsMenu){
            Event myEvent = new Event(); //TODO: generate random id
            Bundle args = new Bundle();
            args.putParcelable("event",myEvent);
            args.putBoolean("isOrganizer", true);
            try {
                NavHostFragment.findNavController(EventOrganizerListFragment.this)
                        .navigate(R.id.action_eventOrganizerListFragment_to_eventEditFragment, args);
            } catch (Exception ignored) {}
        }
        return false;
    }

}
