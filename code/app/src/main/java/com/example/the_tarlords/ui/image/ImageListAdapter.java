package com.example.the_tarlords.ui.image;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.the_tarlords.R;
import java.util.ArrayList;

public class ImageListAdapter extends ArrayAdapter<String> {

    private ArrayList<String> imageList;
    private LayoutInflater inflater;

    public ImageListAdapter(Context context, ArrayList<String> images) {
        super(context, 0, images);
        this.imageList = images;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_image_list_item, parent, false);
            holder = new ViewHolder();

            //getting the textviews
            holder.imageViewTV = view.findViewById(R.id.imageViewTV);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        String image = imageList.get(position);
        //setting the textviews
        //holder.firstNameTextView.setText(user.getFirstName());

        return view;
    }
    /**
     * Add the text views you want to display here
     */
    static class ViewHolder {
        TextView imageViewTV;
    }
}