package com.example.the_tarlords.ui.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.the_tarlords.R;
import com.example.the_tarlords.data.users.User;

import java.util.ArrayList;

public class ProfileListAdapter extends ArrayAdapter<User> {

    private ArrayList<User> userList;
    private LayoutInflater inflater;

    public ProfileListAdapter(Context context, ArrayList<User> users) {
        super(context, 0, users);
        this.userList = users;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_profile_list_item, parent, false);
            holder = new ViewHolder();
            //getting the textviews
            holder.firstNameTextView = view.findViewById(R.id.firstNameTV);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        User user = userList.get(position);
        //setting the textviews
        holder.firstNameTextView.setText(user.getFirstName());

        return view;
    }

    /**
     * Add the text views you want to display here
     */
    static class ViewHolder {
        TextView firstNameTextView;
    }
}
