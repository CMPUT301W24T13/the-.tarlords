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
import com.example.the_tarlords.data.Alert.Alert;
import com.example.the_tarlords.data.Alert.AlertCallback;
import com.example.the_tarlords.data.Alert.AlertListAdapter;
import com.example.the_tarlords.data.Alert.MilestoneHelper;
import com.example.the_tarlords.data.attendance.AttendanceDBHelper;
import com.example.the_tarlords.data.attendance.AttendanceListCallback;
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
    private ArrayList<Alert> milestoneList = new ArrayList<>();
    private AlertListAdapter milestoneListAdapter;
    private AttendanceArrayAdapter attendanceArrayAdapter;

    private CollectionReference attendanceRef;
    private CollectionReference usersRef = MainActivity.db.collection("Users");
    TextView totalCount;
    TextView checkInCount;
    ListView attendanceListView;


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

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().addMenuProvider(this);
        attendanceListView = view.findViewById(R.id.attendanceListView);
        attendanceRef = MainActivity.db.collection("Events/"+event.getId()+"/Attendance");

        // Set the adapter
        attendanceArrayAdapter = new AttendanceArrayAdapter(getContext(), attendees);
        attendanceListView.setAdapter(attendanceArrayAdapter);

        totalCount = view.findViewById(R.id.attendee_count);
        checkInCount = view.findViewById(R.id.attendee_checkin_count);


        attendanceRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshots, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore", error.toString());
                    return;
                }
                if (querySnapshots != null) {
                    refreshAttendance();
                }
            }
        });
        refreshMilestoneList();


    }
    public void refreshAttendance(){
        AttendanceDBHelper.getAttendanceList(event, new AttendanceListCallback() {
            @Override
            public void onAttendanceLoaded(ArrayList<Attendee> attendanceList) {
                attendees.clear();
                attendees.addAll(attendanceList);
                attendanceArrayAdapter.notifyDataSetChanged();
                totalCount.setText("Total: " + attendanceArrayAdapter.getItemCount());
                checkInCount.setText("Checked In: " + attendanceArrayAdapter.getCheckInCount());
            }
        });

    }

    private void refreshMilestoneList(){
        MilestoneHelper helper = new MilestoneHelper(event.getId());
        milestoneList = helper.getMilestoneList(new AlertCallback() {
            @Override
            public void onAlertsLoaded(ArrayList<Alert> alertList) {
                ListView milestoneListView = getView().findViewById(R.id.milestone_list_view);
                milestoneListAdapter = new AlertListAdapter(requireContext(),milestoneList,1);
                milestoneListView.setAdapter(milestoneListAdapter);
                milestoneListAdapter.notifyDataSetChanged();
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