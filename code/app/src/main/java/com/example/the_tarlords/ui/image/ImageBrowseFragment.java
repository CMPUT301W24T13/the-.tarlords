package com.example.the_tarlords.ui.image;


import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;

import com.example.the_tarlords.MainActivity;
import com.example.the_tarlords.R;
import com.example.the_tarlords.data.photo.Photo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A fragment representing a list of Image Items.
 * Images are event posters or profile photos
 */
public class ImageBrowseFragment extends Fragment implements MenuProvider {

    // the fragment initialization parameters

    // if photo class deals with all images then type could change from String to Image

    private CollectionReference eventsRef = MainActivity.db.collection("Events");
    private CollectionReference usersRef = MainActivity.db.collection("Users");
    private ArrayList<Photo> images = new ArrayList<>();
    private ImageListAdapter adapter;


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

        TextView pageHeader = view.findViewById(R.id.tv_browse_images_header);
        pageHeader.setText(getResources().getString(R.string.browse_images));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Photo image = images.get(position); // use this to delete the image
                // should we inflate to a dialog fragment to display image?
                AlertDialog dialog = new AlertDialog.Builder(requireContext())
                        .setMessage("Are you sure you would like to remove ?")
                        .setNegativeButton("Remove", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // removing the images , there are 2 locations , also notifys the adapter
                                removeFromFirestore(image);
                            }
                        })
                        .setCancelable(true)
                        .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Handle cancel action if needed
                            }
                        }).show();
            }
        });
        fetchImagesFromFirebase();
        return view;
    }


    /**
     * Fetches the posterData and profilePhotoData which are
     * the string representation of all the images
     * that the admin can browse
     */

    private void fetchImagesFromFirebase() {
        // 1st firebase location : Event has posterData
        eventsRef.whereNotEqualTo("posterData", null)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            images.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Access the posterData field for each document
                                String posterData = document.getString("posterData");
                                String name = document.getString("name");
                                String docId = document.getId(); // Get document ID
                                Boolean posterIsDefault = document.getBoolean("posterIsDefault");
                                if (Boolean.FALSE.equals(posterIsDefault)){ // if false then it is NOT the default photo
                                    if (posterData != null && posterData.length() >= 5) {
                                        images.add(new Photo(posterData, "Events", name, docId));
                                        Log.d(TAG, "Poster Data: " + posterData.substring(0, 5));
                                    } else {
                                        Log.d(TAG, "Poster Data is null or shorter than 5 characters");
                                    }
                                } // else do nothing


                            }
                            //adapter.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        // 2nd Firebase location
        usersRef.whereNotEqualTo("profilePhotoData", null)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Access the posterData field for each document
                                String profilePhotoData = document.getString("profilePhotoData");
                                String name = document.getString("firstName");
                                String docId = document.getId(); // Get document ID
                                Boolean photoIsDefault = document.getBoolean("photoIsDefault");
                                if (photoIsDefault!=null&&!photoIsDefault){
                                    if (profilePhotoData != null && profilePhotoData.length() >= 5) {
                                        images.add(new Photo(profilePhotoData, "Users", name, docId));
                                        Log.d(TAG, "Profile Photo Data: " + profilePhotoData.substring(0, 5));
                                    } else {
                                        Log.d(TAG, "Profile Photo Data is null or shorter than 5 characters");
                                    }
                                } // else don't display

                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    /**
     * Removes selected image from list and from database
     * @param image
     */
    private void removeFromFirestore(Photo image) {
        String collection = image.getCollection();
        String docId = image.getDocId();
        DocumentReference docRef;
        String field;
        if (collection == "Events") {
            docRef = eventsRef.document(docId);
            field = "posterData";
        } else {
            docRef = usersRef.document(docId);
            field = "profilePhotoData";
        }
        // Update the field to null
        docRef.update(field, null)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, field + "set to null for document with ID: " + docId);
                        // remove the image from the list here
                        images.remove(image);
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error updating" + field + "to null for document with ID: " + docId, e);
                    }
                });
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