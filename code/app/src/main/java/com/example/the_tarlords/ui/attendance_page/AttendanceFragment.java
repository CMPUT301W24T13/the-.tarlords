package com.example.the_tarlords.ui.attendance_page;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;

import com.example.the_tarlords.MainActivity;
import com.example.the_tarlords.R;
import com.example.the_tarlords.data.attendance.AttendanceCallback;
import com.example.the_tarlords.data.event.Event;
import com.example.the_tarlords.data.users.Attendee;
import com.example.the_tarlords.databinding.FragmentAttendanceListBinding;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 */
public class AttendanceFragment extends Fragment implements MenuProvider {

    FragmentAttendanceListBinding binding;
    private Event event;
    private ArrayList<Attendee> attendees = new ArrayList<>();
    private static AttendanceArrayAdapter adapter;
    private CollectionReference attendanceRef;
    private CollectionReference usersRef = MainActivity.db.collection("Users");
    private TextView totalCount;
    private TextView checkInCount;

    private static final String ARG_COLUMN_COUNT = "column-count";

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
        ListView attendanceListView = binding.getRoot().findViewById(R.id.attendanceListView);
        attendanceRef = MainActivity.db.collection("Events/"+event.getId()+"/Attendance");
        Log.d("attendance list", attendees.toString()+"hello");


        attendees = event.getAttendanceList(new AttendanceCallback() {
            @Override
            public void onAttendanceLoaded(ArrayList<Attendee> attendanceList) {
                // Set the adapter
                adapter = new AttendanceArrayAdapter(requireContext(), attendanceList);
                attendanceListView.setAdapter(adapter);
            }
        });
        refreshList();
        totalCount = binding.getRoot().findViewById(R.id.attendee_count);
        checkInCount = binding.getRoot().findViewById(R.id.attendee_checkin_count);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().addMenuProvider(this);

        attendanceRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshots, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore", error.toString());
                    return;
                }
                if (querySnapshots != null) {
                    attendees.clear();
                    refreshList();
                }

            }
        });


    }
    public void refreshList(){
        attendees = event.getAttendanceList(new AttendanceCallback() {
            @Override
            public void onAttendanceLoaded(ArrayList<Attendee> attendees) {
                Integer count = adapter.getItemCount();
                totalCount.setText(count.toString());
                count = adapter.getCheckInCount();
                checkInCount.setText(count.toString());

            }
        });
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menu.clear();
    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }
    public static void notifyComplete(){
        adapter.notifyDataSetChanged();
    }
}