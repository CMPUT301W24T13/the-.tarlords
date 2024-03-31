package com.example.the_tarlords.ui.image;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.the_tarlords.R;
import com.example.the_tarlords.ui.image.placeholder.PlaceholderContent;

import java.util.ArrayList;

/**
 * A fragment representing a list of Image Items.
 */
public class ImageBrowseFragment extends Fragment implements MenuProvider {

    // the fragment initialization parameters

    // if photo class deals with all images then type could change from String to Image
    private ArrayList<String> images = new ArrayList<>();
    private ArrayAdapter<String> adapter;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ImageBrowseFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ImageBrowseFragment newInstance() {
        ImageBrowseFragment fragment = new ImageBrowseFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //MANDATORY for MenuProvider implementation
        requireActivity().addMenuProvider(this);

        View view = inflater.inflate(R.layout.fragment_image_browse, container, false);
        ListView listView = view.findViewById(R.id.imageListView);
        adapter = new ImageListAdapter(getContext(), images);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedImage = images.get(position);
                // should we inflate to a dialog fragment to display image?
                // navigateToImageDialog(selectedImage);
            }
        });
        fetchImagesFromFirebase();
        return view;
    }

    private void fetchImagesFromFirebase() {
        // TODO get all the images from firebase
        // 1st firebase location : Event has posterData


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