package com.example.the_tarlords.ui.profile;

import androidx.core.view.MenuProvider;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.ImageView;

import com.example.the_tarlords.R;
import com.example.the_tarlords.data.users.User;
import com.example.the_tarlords.not_in_use.ProfileViewModel;

public class ProfileFragment extends Fragment implements MenuProvider {
    private User user;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ImageView profilePhotoImageView = view.findViewById(R.id.image_view_profile);
        EditText firstNameEditText = view.findViewById(R.id.edit_text_first_name);
        EditText lastNameEditText = view.findViewById(R.id.edit_text_last_name);
        EditText phoneEditText = view.findViewById(R.id.edit_text_phone);
        EditText emailEditText = view.findViewById(R.id.edit_text_email);

        if (user != null) {
            profilePhotoImageView.setImageBitmap(user.getProfilePhoto().getBitmap());
            firstNameEditText.setText(user.getFirstName());
            lastNameEditText.setText(user.getLastName());
            phoneEditText.setText(user.getPhoneNum());
            emailEditText.setText(user.getEmail());
        }
        return view;
    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {

    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }
}