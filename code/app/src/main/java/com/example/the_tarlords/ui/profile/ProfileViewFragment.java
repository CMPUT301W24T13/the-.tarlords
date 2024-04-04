package com.example.the_tarlords.ui.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.the_tarlords.R;
import com.example.the_tarlords.data.photo.ProfilePhoto;
import com.example.the_tarlords.data.users.User;
import com.example.the_tarlords.databinding.FragmentProfileBinding;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileViewFragment extends Fragment implements MenuProvider{

    private User user;
    CircleImageView profilePhotoImageView;
    Button addProfilePhotoButton;
    EditText firstNameEditText;
    EditText lastNameEditText;
    EditText phoneEditText;
    EditText emailEditText;
    private FragmentProfileBinding binding;


    public ProfileViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileViewFragment newInstance(String param1, String param2) {
        ProfileViewFragment fragment = new ProfileViewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (User) getArguments().getParcelable("user");

        }
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //MANDATORY for MenuProvider implementation, this might cause issues
        requireActivity().addMenuProvider(this);


        //find fragment views
        profilePhotoImageView = view.findViewById(R.id.image_view_profile);
        profilePhotoImageView.setBorderWidth(5); // Set the border width in pixels
        profilePhotoImageView.setBorderColor(Color.WHITE);
        addProfilePhotoButton = view.findViewById(R.id.button_add_profile_photo);
        firstNameEditText = view.findViewById(R.id.edit_text_first_name);
        lastNameEditText = view.findViewById(R.id.edit_text_last_name);
        phoneEditText = view.findViewById(R.id.edit_text_phone);
        emailEditText = view.findViewById(R.id.edit_text_email);
        //add more views here as desired

        //set content for views in fragment
        if (user != null) {

            firstNameEditText.setText(user.getFirstName());
            lastNameEditText.setText(user.getLastName());
            phoneEditText.setText(user.getPhoneNum());
            emailEditText.setText(user.getEmail());
            //set additional views content here as desired

            if (user.getProfilePhoto() != null) { //display user's profile photo if not null
                profilePhotoImageView.setImageBitmap(user.getProfilePhoto().getBitmap());
            }
            else { //if user does not have a profile photo, generate one
                ProfilePhoto profilePhoto = new ProfilePhoto(user.getFirstName() + user.getLastName(),
                        null, user.getFirstName(), user.getLastName());
                profilePhoto.autoGenerate();
                user.setProfilePhoto(profilePhoto);
                profilePhotoImageView.setImageBitmap(profilePhoto.getBitmap());
            }
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menu.clear();
        menuInflater.inflate(R.menu.options_menu, menu);
        menu.findItem(R.id.deleteOptionsMenu).setVisible(true);
    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        if (isAdded() && getContext() != null) {
            if (menuItem.getItemId()==R.id.deleteOptionsMenu){
                AlertDialog dialog = new AlertDialog.Builder(requireContext())
                        .setMessage("Are you sure you would like to remove " + user.getFirstName() + "?")
                        .setNegativeButton("Remove", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Todo : a thorough test, removes the users not sure about their events and such
                                user.removeFromFirestore();
                                try {
                                    // Return to profile browse fragment
                                    NavHostFragment.findNavController(ProfileViewFragment.this)
                                            .navigate(R.id.action_profileViewFragment_to_profileBrowseFragment);
                                } catch (Exception ignored) {
                                }
                            }
                        })
                        .setCancelable(true)
                        .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Handle cancel action if needed
                            }
                        }).show();
            } else {
                // Fragment is not attached to an activity, handle the situation accordingly
                Log.d("admin", "fragment not attached to activity");
            }

        }
        return false;
    }
}