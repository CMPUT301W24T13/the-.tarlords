package com.example.the_tarlords.ui.event;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;

import com.example.the_tarlords.MainActivity;
import com.example.the_tarlords.R;
import com.example.the_tarlords.data.event.Event;
import com.example.the_tarlords.databinding.FragmentEventListBinding;
import com.example.the_tarlords.ui.home.EventArrayAdapter;
import com.example.the_tarlords.ui.home.EventListFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class EventOrganizerListFragment extends Fragment implements MenuProvider {
    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    //private FragmentEventOrganizerListBinding binding;
    private FragmentEventListBinding binding;
    private CollectionReference eventsRef = MainActivity.db.collection("Events");
    private CollectionReference usersRef = MainActivity.db.collection("Users");
    ArrayList<Event> events = new ArrayList<>();


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EventOrganizerListFragment() {
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
        binding = FragmentEventListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().addMenuProvider(this);
        ListView eventListView = view.findViewById(R.id.eventListView);
        Log.d("events list", events.toString()+"hello");
        //set adapter
        EventArrayAdapter adapter = new EventArrayAdapter(getContext(),events);
        eventListView.setAdapter(adapter);

        eventsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshots,
                                @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore", error.toString());
                    return;
                }
                if (querySnapshots != null) {
                    events.clear();
                    //TODO: tried putting this in a different class but it wasn't working, maybe someone else will have better luck?
                    eventsRef
                            .whereEqualTo("organizerId", MainActivity.user.getUserId())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            events.add(document.toObject(Event.class));
                                            adapter.notifyDataSetChanged();
                                            Log.d("query events", document.getId() + " => " + document.getData());
                                        }
                                    }
                                    else {
                                        Log.d("query events", "Error getting documents: ", task.getException());
                                    }
                                }
                            });
                }
            }
        });

        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event event = events.get(position);
                Bundle args = new Bundle();
                args.putParcelable("event",event);
                args.putBoolean("isOrganizer", true);
                NavHostFragment.findNavController(EventOrganizerListFragment.this)
                        .navigate(R.id.action_eventOrganizerListFragment_to_eventEditFragment,args);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menu.clear();
        menuInflater.inflate(R.menu.options_menu, menu);
        menu.findItem(R.id.addOptionsMenu).setVisible(true);
        menu.findItem(R.id.editOptionsMenu).setVisible(false);
    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.addOptionsMenu){
            Event myEvent = new Event(); //TODO: generate random id
            Bundle args = new Bundle();
            args.putParcelable("event",myEvent);
            args.putBoolean("isOrganizer", true);
            NavHostFragment.findNavController(EventOrganizerListFragment.this)
                    .navigate(R.id.action_eventOrganizerListFragment_to_eventEditFragment,args);
        }
        return false;
    }

    //TODO: implement add event (fab or options menu)


}
