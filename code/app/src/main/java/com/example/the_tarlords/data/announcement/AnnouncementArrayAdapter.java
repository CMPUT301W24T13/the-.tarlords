package com.example.the_tarlords.data.announcement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.the_tarlords.R;

import java.util.ArrayList;

public class AnnouncementArrayAdapter extends ArrayAdapter<Announcement> {
    public AnnouncementArrayAdapter(Context context, ArrayList<Announcement> announcements){
        super(context,0,announcements);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.content_announcements,parent,false);

        } else{
            view = convertView;
        }

        // todo: create textviews for each field

        return view;
    }
}