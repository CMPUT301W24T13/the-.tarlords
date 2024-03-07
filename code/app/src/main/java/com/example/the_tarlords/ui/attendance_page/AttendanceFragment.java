package com.example.the_tarlords.ui.attendance_page;

import static com.example.the_tarlords.databinding.FragmentAttendanceListBinding.inflate;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.the_tarlords.R;
import com.example.the_tarlords.data.attendance.Attendance;
import com.example.the_tarlords.data.event.Event;
import com.example.the_tarlords.data.users.Attendee;
import com.example.the_tarlords.databinding.FragmentAttendanceListBinding;
import com.example.the_tarlords.ui.attendance_page.placeholder.PlaceholderContent;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 */
public class AttendanceFragment extends Fragment {

    FragmentAttendanceListBinding binding;
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private Event event;
    private AttendanceRecyclerViewAdapter adapter;

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static AttendanceFragment newInstance(int columnCount) {
        AttendanceFragment fragment = new AttendanceFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
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
            mColumnCount = getArguments().getInt("column-count");
            event = (Event) getArguments().get("event-name");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendance_list, container, false);
        binding = FragmentAttendanceListBinding.inflate(inflater, container, false);
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            adapter = new AttendanceRecyclerViewAdapter(event.getAttendanceList());
            recyclerView.setAdapter(adapter);
        }


        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView totalCount = binding.attendeeCount;
        TextView checkInCount = binding.attendeeCheckinCount;
        totalCount.setText("Total: "+adapter.getItemCount());
        checkInCount.setText("Checked In: "+adapter.getCheckInCount());
    }
}