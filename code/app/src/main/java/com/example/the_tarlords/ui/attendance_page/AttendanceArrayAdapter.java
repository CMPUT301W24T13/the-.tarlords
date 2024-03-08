package com.example.the_tarlords.ui.attendance_page;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.the_tarlords.R;
import com.example.the_tarlords.data.users.Attendee;

import java.util.ArrayList;

public class AttendanceArrayAdapter extends ArrayAdapter<Attendee> {
    ArrayList<Attendee> attendees;
    public AttendanceArrayAdapter(Context context, ArrayList<Attendee> attendees) {
        super(context, 0, attendees);
        this.attendees = attendees;

    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup
            parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_attendance_list_item, parent, false);
        } else {
            view = convertView;
        }
        Attendee attendee = (Attendee) getItem(position);
        if (attendee != null) {
            TextView firstName = view.findViewById(R.id.item_first_name);
            firstName.setText(attendee.getFirstName());
            TextView lastName = view.findViewById(R.id.item_last_name);
            lastName.setText(attendee.getLastName());
            CheckBox checkInStatus = view.findViewById(R.id.item_check_in_box);
            checkInStatus.setChecked(attendee.getCheckInStatus());
        }


        return view;
    }

    public int getItemCount() {
        if (attendees == null) {
            return 0;
        }
        return attendees.size();
    }

    public int getCheckInCount() {
        int checkInCount = 0;
        for (Attendee a: attendees) {
            if (a.getCheckInStatus() == true) {
                checkInCount += 1;
            }
        }
        return checkInCount;
    }
}