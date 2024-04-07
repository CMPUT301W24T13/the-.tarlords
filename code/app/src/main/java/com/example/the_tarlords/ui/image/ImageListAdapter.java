package com.example.the_tarlords.ui.image;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
            holder.collectionTV = view.findViewById(R.id.tv_imageBrowse_subtitle);
            holder.nameTV = view.findViewById(R.id.tv_imageBrowse_title);
            holder.id = view.findViewById(R.id.tv_imageBrowse_subtext);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }


        Photo image = imageList.get(position);
        // setting the textviews to the first 5 characters of the posterData
        image.setBitmapFromPhotoData(image.getImageData());
        holder.imageViewTV.setImageBitmap(image.getBitmap());
        holder.collectionTV.setText(image.getCollection());
        holder.nameTV.setText(image.getName());
        holder.id.setText(image.getDocId());

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
        TextView id;
    }
}