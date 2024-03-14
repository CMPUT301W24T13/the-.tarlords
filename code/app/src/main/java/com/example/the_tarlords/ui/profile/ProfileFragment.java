package com.example.the_tarlords.ui.profile;

import androidx.core.view.MenuProvider;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.the_tarlords.MainActivity;
import com.example.the_tarlords.R;
import com.example.the_tarlords.data.photo.ProfilePhoto;
import com.example.the_tarlords.data.users.User;

public class ProfileFragment extends Fragment implements MenuProvider {
    private User user = MainActivity.user;
    ImageView profilePhotoImageView;
    Button addProfilePhotoButton;
    EditText firstNameEditText;
    EditText lastNameEditText;
    EditText phoneEditText;
    EditText emailEditText;
    public ProfileFragment(){
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().addMenuProvider(this);
        profilePhotoImageView = view.findViewById(R.id.image_view_profile);
        addProfilePhotoButton = view.findViewById(R.id.button_add_profile_photo);
        firstNameEditText = view.findViewById(R.id.edit_text_first_name);
        lastNameEditText = view.findViewById(R.id.edit_text_last_name);
        phoneEditText = view.findViewById(R.id.edit_text_phone);
        emailEditText = view.findViewById(R.id.edit_text_email);

        if (user != null) {
            //profilePhotoImageView.setImageBitmap(user.getProfilePhoto().getBitmap());
            //firstNameEditText.setText(user.getFirstName());
            //lastNameEditText.setText(user.getLastName());
            //phoneEditText.setText(user.getPhoneNum());
            //emailEditText.setText(user.getEmail());
            //}
            firstNameEditText.setText(user.getFirstName());
            lastNameEditText.setText(user.getLastName());
            phoneEditText.setText(user.getPhoneNum());
            emailEditText.setText(user.getEmail());

            if (user.getProfilePhoto() != null) {
                profilePhotoImageView.setImageBitmap(user.getProfilePhoto().getBitmap());

            } else {
                ProfilePhoto profilePhoto = new ProfilePhoto(user.getFirstName() + user.getLastName(),
                        null, user.getFirstName(), user.getLastName());
                profilePhoto.autoGenerate();
                user.setProfilePhoto(profilePhoto);
                profilePhotoImageView.setImageBitmap(profilePhoto.getBitmap());

            }
        }

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
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menu.clear();
        menuInflater.inflate(R.menu.options_menu, menu);
        menu.findItem(R.id.editOptionsMenu).setVisible(true);
    }
    @Override
    public void onPrepareMenu(@NonNull Menu menu) {
        try {
            if (getView().findViewById(R.id.button_add_profile_photo).getVisibility() != getView().GONE) {
                menu.findItem(R.id.editOptionsMenu).setVisible(false);
                menu.findItem(R.id.saveOptionsMenu).setVisible(true);
                menu.findItem(R.id.cancelOptionsMenu).setVisible(true);
            } else {
                menu.findItem(R.id.editOptionsMenu).setVisible(true);
                menu.findItem(R.id.saveOptionsMenu).setVisible(false);
                menu.findItem(R.id.cancelOptionsMenu).setVisible(false);
            }
        } catch (Exception ignore) {}
    }
    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {

        if (menuItem.getItemId() == R.id.editOptionsMenu) {
            profilePhotoImageView.setVisibility(View.INVISIBLE);
            addProfilePhotoButton.setVisibility(View.VISIBLE);
            firstNameEditText.setClickable(true);
            lastNameEditText.setClickable(true);
            phoneEditText.setClickable(true);
            emailEditText.setClickable(true);

            requireActivity().invalidateMenu();
            return false;
        } else {
            profilePhotoImageView.setVisibility(View.VISIBLE);
            addProfilePhotoButton.setVisibility(View.GONE);
            firstNameEditText.setClickable(false);
            lastNameEditText.setClickable(false);
            phoneEditText.setClickable(false);
            emailEditText.setClickable(false);

            requireActivity().invalidateMenu();

            if (menuItem.getItemId() == R.id.saveOptionsMenu) {
                user.setFirstName(firstNameEditText.getText().toString());
                user.setLastName(lastNameEditText.getText().toString());
                user.setPhoneNum(phoneEditText.getText().toString());
                user.setEmail(emailEditText.getText().toString());

                Bitmap bitmap = ((BitmapDrawable)profilePhotoImageView.getDrawable()).getBitmap();
                user.getProfilePhoto().setBitmap(bitmap);

                MainActivity.user.sendToFireStore();

                MainActivity.updateNavigationDrawerHeader();

                //TODO: update firebase info

                //TODO: update navigation menu header, check for invalid input or name input with more than one space
            }
            return false;

        }
        //return false;
    }
}