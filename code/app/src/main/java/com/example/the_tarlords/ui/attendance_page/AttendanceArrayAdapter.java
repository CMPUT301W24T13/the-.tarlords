package com.example.the_tarlords.ui.attendance_page;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.the_tarlords.R;
import com.example.the_tarlords.data.users.Attendee;

import java.util.ArrayList;

/**
 * Adapter for ListView in AttendanceFragment
 */

public class AttendanceArrayAdapter extends ArrayAdapter<Attendee> {
    ArrayList<Attendee> attendees;
    private LayoutInflater inflater;
    public AttendanceArrayAdapter(Context context, ArrayList<Attendee> attendees) {
        super(context, 0, attendees);
        this.attendees = attendees;
        inflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        AttendanceArrayAdapter.ViewHolder holder;

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_profile_list_item, parent, false);
            holder = new AttendanceArrayAdapter.ViewHolder();
            //getting the textviews
            holder.name = view.findViewById(R.id.nameTV);
            holder.email = view.findViewById(R.id.emailTV);
            holder.phoneNum = view.findViewById(R.id.phoneNumberTV);
            holder.profilePic = view.findViewById(R.id.profile_photo_image_view);
            holder.checkInStatus = view.findViewById(R.id.cb_checkInStatus);
            holder.checkInStatus.setVisibility(view.VISIBLE);
            holder.checkInCount = view.findViewById(R.id.tv_noOfCheckIns);
            holder.checkInCount.setVisibility(view.VISIBLE);
            view.setTag(holder);
        } else {
            holder = (AttendanceArrayAdapter.ViewHolder) view.getTag();
        }

        Attendee attendee = attendees.get(position);
        //setting the textviews
        holder.name.setText(attendee.getFirstName()+" "+attendee.getLastName());
        holder.email.setText(attendee.getEmail());
        holder.phoneNum.setText(attendee.getPhoneNum());
        holder.checkInStatus.setChecked(attendee.getCheckInStatus());
        holder.checkInCount.setText(attendee.getCheckInCount()+" check ins");
        if (attendee.getProfilePhoto()!=null) {
            holder.profilePic.setImageBitmap(attendee.getProfilePhoto().getBitmap());
        }

        return view;
    }

    /**
     * Add the text views you want to display here
     */
    static class ViewHolder {
        TextView name;
        TextView email;
        TextView phoneNum;
        TextView checkInCount;
        ImageView profilePic;
        CheckBox checkInStatus;

    }

    /**
     * Get size of attendees sign-up list
     * @return number of attendees in list
     */
    public int getItemCount() {
        if (attendees == null) {
            return 0;
        }
        return attendees.size();
    }

    /**
     * Get size of attendees check-in list
     * @return number of attendees in list
     */
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