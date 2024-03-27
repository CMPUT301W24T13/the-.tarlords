package com.example.the_tarlords.ui.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
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

import com.example.the_tarlords.MainActivity;
import com.example.the_tarlords.R;
import com.example.the_tarlords.data.photo.ProfilePhoto;
import com.example.the_tarlords.data.users.User;
import com.example.the_tarlords.databinding.FragmentProfileBinding;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment implements MenuProvider {
    private User user;
    private Boolean fromAdmin = false;
    CircleImageView profilePhotoImageView;
    Button addProfilePhotoButton;
    EditText firstNameEditText;
    EditText lastNameEditText;
    EditText phoneEditText;
    EditText emailEditText;
    FragmentProfileBinding binding;
    public ProfileFragment(){
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (User) getArguments().getParcelable("user");
            fromAdmin = getArguments().getBoolean("fromAdmin");
            Log.d("profiles", "it is this user");
        } else {
            user = MainActivity.user; // Fall back to the user from MainActivity
        }

    }
    public static ProfileFragment newInstance() {
        return new ProfileFragment();
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

        //MANDATORY for MenuProvider implementation
        requireActivity().addMenuProvider(this);
        // needed for browseProfiles

      /*  if (!fromAdmin) {
            // Navigation is not from browse profile, pop the back stack
            Navigation.findNavController(view).popBackStack();
        }*/

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

        //for user to add or update profile photo
        addProfilePhotoButton.setOnClickListener(v -> {
            new AlertDialog.Builder(this.getContext())
                    .setTitle("Where would you like to upload a profile photo from?")
                    .setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getActivity(), TakePhotoActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getActivity(), UploadPhotoActivity.class);
                            startActivity(intent);

                        }
                    })
                    .setNeutralButton("Cancel", null)
                    .create()
                    .show();
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    /**
     * Required method for MenuProvider interface.
     * Creates initial state of options menu.
     * @param menu         the menu to inflate the new menu items into
     * @param menuInflater the inflater to be used to inflate the updated menu
     */
    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menu.clear();
        menuInflater.inflate(R.menu.options_menu, menu);
        menu.findItem(R.id.editOptionsMenu).setVisible(true);
    }

    /**
     * Optional method for MenuProvider interface.
     * Recreates menu after it is invalidated (in onMenuItemSelected).
     * Used to update menu icons after an option is selected.
     * @param menu the menu that is to be prepared
     */
    @Override
    public void onPrepareMenu(@NonNull Menu menu) {
        if (isAdded() && getContext() != null) { //bug fix for IllegalStateException: Fragment not attached to an activity
            if (getView().findViewById(R.id.button_add_profile_photo).getVisibility() != getView().GONE) {
                menu.findItem(R.id.editOptionsMenu).setVisible(false);
                menu.findItem(R.id.saveOptionsMenu).setVisible(true);
                menu.findItem(R.id.cancelOptionsMenu).setVisible(true);
            } else {
                if(fromAdmin){
                    menu.findItem(R.id.deleteOptionsMenu).setVisible(true);
                    menu.findItem(R.id.editOptionsMenu).setVisible(false);
                }else{
                    menu.findItem(R.id.editOptionsMenu).setVisible(true);
                    menu.findItem(R.id.saveOptionsMenu).setVisible(false);
                    menu.findItem(R.id.cancelOptionsMenu).setVisible(false);
                }

            }
        }
    }

    /**
     * Required method for MenuProvider interface.
     * On click listener for options menu.
     * @param menuItem the menu item that was selected
     * @return Boolean false
     */
    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        if (isAdded() && getContext() != null) { //bug fix for IllegalStateException: Fragment not attached to an activity
            if (menuItem.getItemId() == R.id.editOptionsMenu) {
                profilePhotoImageView.setVisibility(View.INVISIBLE);
                addProfilePhotoButton.setVisibility(View.VISIBLE);
                firstNameEditText.setEnabled(true);
                lastNameEditText.setEnabled(true);
                phoneEditText.setEnabled(true);
                emailEditText.setEnabled(true);

                requireActivity().invalidateMenu(); //required in order to call onPrepareMenu() and repopulate menu with new options
                return false;
            }
            else if (menuItem.getItemId() == R.id.saveOptionsMenu || menuItem.getItemId() == R.id.cancelOptionsMenu) {
                profilePhotoImageView.setVisibility(View.VISIBLE);
                addProfilePhotoButton.setVisibility(View.GONE);
                firstNameEditText.setEnabled(false);
                lastNameEditText.setEnabled(false);
                phoneEditText.setEnabled(false);
                emailEditText.setEnabled(false);
                if (menuItem.getItemId() == R.id.cancelOptionsMenu) {
                    requireActivity().invalidateMenu(); //required in order to call onPrepareMenu() and repopulate menu with new options
                }

                //if save button selected, update user info and send to firestore
                if (menuItem.getItemId() == R.id.saveOptionsMenu) {
                    if (checkValidInput()) {
                        user.setFirstName(firstNameEditText.getText().toString());
                        user.setLastName(lastNameEditText.getText().toString());
                        user.setPhoneNum(phoneEditText.getText().toString());
                        user.setEmail(emailEditText.getText().toString());


                        Bitmap bitmap = ((BitmapDrawable) profilePhotoImageView.getDrawable()).getBitmap();
                        user.getProfilePhoto().setBitmap(bitmap);

                        MainActivity.user.sendToFireStore();

                        //update navigation header (slide out menu) with newly updated information
                        MainActivity.updateNavigationDrawerHeader();
                        requireActivity().invalidateMenu(); //required in order to call onPrepareMenu() and repopulate menu with new options

                        //TODO: set auto-generated photo to regenerate on name change

                        //TODO: check for invalid input
                    }
                }
                return false;
            }
            return false;
        }
        return false;
    }

    /**
     * Doesn't work
     * @return  true if input valid, false otherwise
     */
    public boolean checkValidInput() {
        for (View v : this.getView().getFocusables(View.FOCUS_FORWARD)){
            Log.d("validate input", "View: " + v.getId());
            if (v instanceof EditText && ((EditText)v).getText().toString().trim().length() == 0) {
                Log.d("validate input", "EditText: " + v.getId());
                return false;
            }
        }
        return true;
    }
}