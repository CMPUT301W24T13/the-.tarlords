package com.example.the_tarlords.ui.profile;

import static java.lang.Character.isAlphabetic;

import androidx.appcompat.app.ActionBar;
import androidx.core.view.MenuProvider;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.the_tarlords.MainActivity;
import com.example.the_tarlords.R;
import com.example.the_tarlords.data.photo.ProfilePhoto;
import com.example.the_tarlords.data.users.User;
import com.example.the_tarlords.databinding.FragmentEventListBinding;
import com.example.the_tarlords.databinding.FragmentProfileBinding;

import java.lang.reflect.Array;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment implements MenuProvider {
    private User user = MainActivity.user;
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
            //parse any arguments passed into fragment here
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

        //find fragment views
        profilePhotoImageView = view.findViewById(R.id.image_view_profile);
        profilePhotoImageView.setBorderWidth(5); // Set the border width in pixels
        profilePhotoImageView.setBorderColor(Color.WHITE);
        addProfilePhotoButton = view.findViewById(R.id.button_add_profile_photo);
        firstNameEditText = view.findViewById(R.id.edit_text_first_name);
        lastNameEditText = view.findViewById(R.id.edit_text_last_name);
        phoneEditText = view.findViewById(R.id.edit_text_phone);
        emailEditText = view.findViewById(R.id.edit_text_email);

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
                profilePhoto.setDefault(true);
                user.setProfilePhoto(profilePhoto);
                profilePhotoImageView.setImageBitmap(profilePhoto.getBitmap());
            }
        }

        addProfilePhotoButton.setOnClickListener(v -> {
            PopupMenu addPhotoOptions = new PopupMenu(this.getContext(), v);
            addPhotoOptions.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.camera_open) {
                        //take photo
                        Intent intent = new Intent(getActivity(), TakePhotoActivity.class);
                        startActivity(intent);
                        //new uploaded profile photo is not the autogenerated one
                        //user.getProfilePhoto().setDefault(false);
                        return true;
                    } else if (item.getItemId() == R.id.gallery_open) {
                        //upload photo
                        Intent intent = new Intent(getActivity(), UploadPhotoActivity.class);
                        startActivity(intent);
                        //new uploaded profile photo is not the autogenerated one
                        //user.getProfilePhoto().setDefault(false);
                        return true;
                    } else if (item.getItemId() == R.id.remove_current_photo) {
                        //remove current photo:
                        user.getProfilePhoto().setBitmap(null);
                        return true;
                    } else {
                        return false;
                    }
                }
            });
            addPhotoOptions.getMenuInflater().inflate(R.menu.photo_add_menu, addPhotoOptions.getMenu());
            addPhotoOptions.show();
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

        if (!checkValidInput(this.getView())) {
            Toast toast = Toast.makeText(getContext(), "Complete profile information to continue.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();

            menu.findItem(R.id.editOptionsMenu).setVisible(false);
            menu.findItem(R.id.saveOptionsMenu).setVisible(true);
            menu.findItem(R.id.cancelOptionsMenu).setVisible(true);

            profilePhotoImageView.setVisibility(View.INVISIBLE);
            addProfilePhotoButton.setVisibility(View.VISIBLE);
            firstNameEditText.setEnabled(true);
            lastNameEditText.setEnabled(true);
            phoneEditText.setEnabled(true);
            emailEditText.setEnabled(true);
        } else {
            menu.findItem(R.id.editOptionsMenu).setVisible(true);
            menu.findItem(R.id.saveOptionsMenu).setVisible(false);
            menu.findItem(R.id.cancelOptionsMenu).setVisible(false);
        }
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
                // puts user in edit mode immediately
                menu.findItem(R.id.editOptionsMenu).setVisible(false);
                menu.findItem(R.id.saveOptionsMenu).setVisible(true);
                menu.findItem(R.id.cancelOptionsMenu).setVisible(true);

            } else {
                menu.findItem(R.id.editOptionsMenu).setVisible(true);
                menu.findItem(R.id.saveOptionsMenu).setVisible(false);
                menu.findItem(R.id.cancelOptionsMenu).setVisible(false);
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
                if (checkValidInput(this.getView())) {
                    // if the profile info has been filled out they can leave edit mode
                    profilePhotoImageView.setVisibility(View.VISIBLE);
                    addProfilePhotoButton.setVisibility(View.GONE);
                    firstNameEditText.setEnabled(false);
                    lastNameEditText.setEnabled(false);
                    phoneEditText.setEnabled(false);
                    emailEditText.setEnabled(false);

                }
                if (menuItem.getItemId() == R.id.cancelOptionsMenu) {
                requireActivity().invalidateMenu(); //required in order to call onPrepareMenu() and repopulate menu with new options
                }

                //if save button selected, update user info and send to firestore
                if (menuItem.getItemId() == R.id.saveOptionsMenu) {
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
                    checkNameChanged();
                }
                return false;
            }
            return false;
        }
        return false;
    }

    public void checkNameChanged() {
        View v = this.getView();
        EditText firstNameEditText = v.findViewById(R.id.edit_text_first_name);
        String firstNamePostEdit = firstNameEditText.getText().toString();

        EditText lastNameEditText = v.findViewById(R.id.edit_text_last_name);
        String lastNamePostEdit = lastNameEditText.getText().toString();

        if (!firstNamePostEdit.equals(user.getProfilePhoto().getPhotoFirstName()) || !lastNamePostEdit.equals(user.getProfilePhoto().getPhotoLastName())) {
            ProfilePhoto profilePhoto = new ProfilePhoto(firstNamePostEdit + lastNamePostEdit,
                    null, firstNamePostEdit, lastNamePostEdit);

            profilePhoto.autoGenerate();
            user.setProfilePhoto(profilePhoto);
            profilePhotoImageView.setImageBitmap(profilePhoto.getBitmap());
        }
    }

    /**
     * Doesn't work
     * @return  true if input valid, false otherwise
     */
    public boolean checkValidInput(View v) {
        boolean validInput = true;
        firstNameEditText = v.findViewById(R.id.edit_text_first_name);
        if (firstNameEditText.getText().toString().length() == 0) {
            firstNameEditText.setError("Required Field.");
            validInput = false;
        } else if (!isAlphabetic(firstNameEditText.getText().toString().charAt(0))) {
            firstNameEditText.setError("Invalid entry. Name must start with a letter.");
            validInput = false;
        }
        lastNameEditText = v.findViewById(R.id.edit_text_last_name);
        if (lastNameEditText.getText().toString().length() == 0) {
            lastNameEditText.setError("Required Field.");
            validInput = false;
        } else if (!isAlphabetic(lastNameEditText.getText().toString().charAt(0))) {
            lastNameEditText.setError("Invalid entry. Name must start with a letter.");
            validInput = false;
        }
        phoneEditText = v.findViewById(R.id.edit_text_phone);
        if (phoneEditText.getText().toString().length() == 0) {
            phoneEditText.setError("Required Field.");
            validInput = false;
        } else if (phoneEditText.getText().toString().length() < 9 || phoneEditText.getText().toString().length() > 20) {
            // 9 digits for local code, up to 20 poss digits by ITU standards
            phoneEditText.setError("Invalid phone number.");
            validInput = false;
        }
        emailEditText = v.findViewById(R.id.edit_text_email);
        if (emailEditText.getText().toString().length() == 0) {
            emailEditText.setError("Required Field.");
            validInput = false;
        } else {
            String email_string = emailEditText.getText().toString();
            boolean validEmailFormat = true;
            int emailMiddle = email_string.indexOf('@');

            if (email_string.charAt(0) == '.') {
                validEmailFormat = false;
            } else if (emailMiddle == -1) {
                validEmailFormat = false;
            } else if ((email_string.length() - emailMiddle) < 3) {
                // minimum 3 chars after @ symbol name@i.u, site, ., domain
                validEmailFormat = false;
            } else if (email_string.charAt(email_string.length()-1) == '.'){
                validEmailFormat = false;
            }
            if (!validEmailFormat) {
                emailEditText.setError("Invalid email format.");
                validInput = false;
            }
        }
        return validInput;
    }
}