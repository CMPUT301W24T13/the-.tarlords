package com.example.the_tarlords.ui.image;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.the_tarlords.R;

import com.example.the_tarlords.data.photo.Photo;

import java.util.ArrayList;

/**
 * List Adapter for ImageBrowseFragments's ListView
 * Each list item is a Photo object
 * @see Photo
 */
public class ImageListAdapter extends ArrayAdapter<Photo> {

    private ArrayList<Photo> imageList;
    private LayoutInflater inflater;

    public ImageListAdapter(Context context, ArrayList<Photo> images) {
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
            holder.imageViewTV = view.findViewById(R.id.iv_image_browse_item);
            //holder.collectionTV = view.findViewById(R.id.collectionTV);
            //holder.nameTV = view.findViewById(R.id.nameTV);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }


        Photo image = imageList.get(position);
        // setting the textviews to the first 5 characters of the posterData
        holder.imageViewTV.setImageBitmap(image.getBitmap());
        //holder.collectionTV.setText(image.getCollection());
        //holder.nameTV.setText(image.getName());

        return view;
    }
    /**
     * Represents view of single list item
     * Add the text views you want to display in ViewHolder
     */
    static class ViewHolder {
        ImageView imageViewTV;
        TextView collectionTV;
        TextView nameTV;
    }
}