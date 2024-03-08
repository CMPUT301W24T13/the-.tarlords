/*
package com.example.the_tarlords.not_in_use;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.the_tarlords.data.users.Attendee;
import com.example.the_tarlords.databinding.FragmentAttendanceListItemBinding;
import com.example.the_tarlords.placeholder.PlaceholderContent.PlaceholderItem;

import java.util.ArrayList;

*/
/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 *//*

public class AttendanceRecyclerViewAdapter extends RecyclerView.Adapter<AttendanceRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<Attendee> attendees;

    public AttendanceRecyclerViewAdapter(ArrayList<Attendee> attendees) {
        this.attendees = attendees;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    return new ViewHolder(FragmentAttendanceListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.attendee = attendees.get(position);
        holder.firstName.setText(attendees.get(position).getFirstName());
        holder.lastName.setText(attendees.get(position).getLastName());
        holder.checkInStatus.setChecked(attendees.get(position).getCheckInStatus());
    }

    @Override
    public int getItemCount() {
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


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView firstName;
        public final TextView lastName;
        public final CheckBox checkInStatus;

        public Attendee attendee;

        public ViewHolder(FragmentAttendanceListItemBinding binding) {
          super(binding.getRoot());
          firstName = binding.itemFirstName;
          lastName = binding.itemLastName;
          checkInStatus = binding.itemCheckInBox;
        }

   */
/*     @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    *//*

    }
}*/
