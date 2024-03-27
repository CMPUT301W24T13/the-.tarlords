package com.example.the_tarlords.ui.profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.the_tarlords.MainActivity;
import com.example.the_tarlords.R;
import com.example.the_tarlords.data.event.Event;
import com.example.the_tarlords.data.users.User;
import com.google.firebase.firestore.CollectionReference;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileBrowseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileBrowseFragment extends Fragment implements MenuProvider {

    // the fragment initialization parameters

    private CollectionReference usersRef = MainActivity.db.collection("Users");
    private ArrayList<User> users = new ArrayList<>();
    private ArrayAdapter<User> adapter;

    public ProfileBrowseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment
     * @return A new instance of fragment ProfileBrowseFragment.
     */
    public static ProfileBrowseFragment newInstance() {
        ProfileBrowseFragment fragment = new ProfileBrowseFragment();
        Bundle args = new Bundle();
        // args.putString(ARG_PARAM1, param1);
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
        View view = inflater.inflate(R.layout.fragment_profile_browse, container, false);
        ListView listView = view.findViewById(R.id.profileListView);
        adapter = new ProfileListAdapter(getContext(), users);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User selectedUser = users.get(position);
                navigateToProfilePage(selectedUser);
            }
        });
        fetchUsersFromFirebase();
        return view;
    }

    private void navigateToProfilePage(User user) {
        Bundle args = new Bundle();
        args.putParcelable("user", user);
        args.putBoolean("fromAdmin", true);
        NavHostFragment.findNavController(ProfileBrowseFragment.this).navigate(R.id.action_profileBrowseFragment_to_profileViewFragment, args);

    }
    private void fetchUsersFromFirebase() {
        usersRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            users.clear();
            for (User user : queryDocumentSnapshots.toObjects(User.class)) {
                users.add(user);
            }
            adapter.notifyDataSetChanged();
        }).addOnFailureListener(e -> {
            // Handle failures
            Log.d("profiles", "failed to get profiles");
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