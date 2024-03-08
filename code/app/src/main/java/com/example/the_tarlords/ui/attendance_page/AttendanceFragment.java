package com.example.the_tarlords.ui.attendance_page;

import static com.example.the_tarlords.databinding.FragmentAttendanceListBinding.inflate;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.the_tarlords.MainActivity;
import com.example.the_tarlords.R;
import com.example.the_tarlords.data.attendance.Attendance;
import com.example.the_tarlords.data.event.Event;
import com.example.the_tarlords.data.users.Attendee;
import com.example.the_tarlords.databinding.FragmentAttendanceListBinding;
import com.example.the_tarlords.ui.event.EventDetailsFragment;
import com.example.the_tarlords.ui.home.EventArrayAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 */
public class AttendanceFragment extends Fragment implements MenuProvider {

    FragmentAttendanceListBinding binding;
    private Event event;
    private ArrayList<Attendee> attendees = new ArrayList<>();//event.getAttendanceList() ;
    private AttendanceArrayAdapter adapter;
    private CollectionReference attendanceRef;
    private CollectionReference usersRef = MainActivity.db.collection("Users");

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static AttendanceFragment newInstance(Event event) {
        AttendanceFragment fragment = new AttendanceFragment();
        Bundle args = new Bundle();
        args.putParcelable("event", event); //When getting the event cast to Event again
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AttendanceFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            event = getArguments().getParcelable("event");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAttendanceListBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().addMenuProvider(this);
        ListView attendanceListView = view.findViewById(R.id.attendanceListView);
        attendanceRef = MainActivity.db.collection("Events/"+event.getId()+"/Attendance");
        Log.d("attendance list", attendees.toString()+"hello");

        // Set the adapter
        adapter = new AttendanceArrayAdapter(getContext(), attendees);
        attendanceListView.setAdapter(adapter);

        TextView totalCount = view.findViewById(R.id.attendee_count);
        TextView checkInCount = view.findViewById(R.id.attendee_checkin_count);
        totalCount.setText("Total: " + adapter.getItemCount());
        checkInCount.setText("Checked In: " + adapter.getCheckInCount());

        attendanceRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshots, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore", error.toString());
                    return;
                }
                if (querySnapshots != null) {
                    attendees.clear();
                    attendanceRef.get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot attendeeDoc : task.getResult()) {
                                    usersRef.document(attendeeDoc.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> t) {
                                            if (t.isSuccessful()) {
                                                DocumentSnapshot userDoc = t.getResult();
                                                Log.d("fire", userDoc.getData().toString()+"111");
                                                Attendee attendee = userDoc.toObject(Attendee.class);
                                                attendee.setCheckInStatus(attendeeDoc.getBoolean("checkedInStatus"));
                                                attendee.setEvent(event);
                                                attendees.add(attendee);
                                                Log.d("attendance query", attendees.toString()+"0000");
                                                adapter.notifyDataSetChanged();
                                            }
                                        }
                                    });
                                }
                                Log.d("firestore", attendees.toString()+":)");
                            } else {
                                Log.d("firestore", "Error getting documents: ", task.getException());
                            }
                        }
                    });
                }

            }
        });


    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menu.clear();
    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }
}