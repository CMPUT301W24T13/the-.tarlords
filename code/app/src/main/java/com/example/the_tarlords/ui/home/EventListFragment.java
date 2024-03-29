package com.example.the_tarlords.ui.home;

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
import androidx.navigation.fragment.NavHostFragment;

import com.example.the_tarlords.MainActivity;
import com.example.the_tarlords.R;
import com.example.the_tarlords.data.event.Event;
import com.example.the_tarlords.databinding.FragmentEventListBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


/**
 * A fragment representing a list of Event items
 */
public class EventListFragment extends Fragment implements MenuProvider {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private FragmentEventListBinding binding;
    private CollectionReference eventsRef = MainActivity.db.collection("Events");
    ArrayList<Event> events = new ArrayList<>();


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EventListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //currently not relevant
        if (getArguments() != null) {
            //parse any args here
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

        //MANDATORY: this enables the options menu
        requireActivity().addMenuProvider(this);

        ListView eventListView = view.findViewById(R.id.eventListView);
        EventArrayAdapter adapter = new EventArrayAdapter(getContext(),events);
        eventListView.setAdapter(adapter);

        if (MainActivity.user != null) {
            String userId = MainActivity.user.getUserId();
            // Use userId...

            // This updates the displayed list on an event
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
                        //This queries firestore for a list of events the user is attending
                        eventsRef
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                MainActivity.db.collection("Events/"+document.getId()+"/Attendance")
                                                        .whereEqualTo("user", userId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                if (task.isSuccessful()) {
                                                                    for (QueryDocumentSnapshot doc : task.getResult()) {
                                                                        events.add(document.toObject(Event.class));
                                                                        adapter.notifyDataSetChanged();
                                                                    }
                                                                    Log.d("query events", document.getId() + " => " + document.getData());
                                                                }
                                                                else {
                                                                    Log.d("query events", "Error getting documents: ", task.getException());
                                                                }
                                                            }
                                                        });
                                            }
                                        } else {
                                            Log.d("query events", "Error getting documents: ", task.getException());
                                        }
                                    }
                                });
                    }
                }
            });
        } else {
            Log.e("debug", "User object is null in EventListFragment");
            // Handle the case where the User object is null
        }
        //listens for user to click on an event, could maybe be its own method outside onCreate?
        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event event = events.get(position);
                Bundle args = new Bundle();
                args.putParcelable("event",event);
                args.putBoolean("isOrganizer", false);
                NavHostFragment.findNavController(EventListFragment.this)
                        .navigate(R.id.action_eventFragment_to_eventDetailsFragment,args);
            }
        });
    }

    //possibly unnecessary
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    /**
     * Mandatory MenuProvider interface method.
     * Clear icons from options menu.
     * @param menu         the menu to inflate the new menu items into
     * @param menuInflater the inflater to be used to inflate the updated menu
     */
    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menu.clear();
    }


    /**
     * Mandatory MenuProvider interface method.
     * @param menuItem the menu item that was selected
     * @return
     */
    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }
}