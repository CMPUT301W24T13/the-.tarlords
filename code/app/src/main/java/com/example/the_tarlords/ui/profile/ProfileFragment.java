package com.example.the_tarlords.ui.profile;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.the_tarlords.MainActivity;
import com.example.the_tarlords.R;

public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;

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
        TextView nameTV = view.findViewById(R.id.nameTextView);
        TextView phoneTV = view.findViewById(R.id.phoneTextView);
        TextView emailTV = view.findViewById(R.id.emailTextView);

        nameTV.setText(MainActivity.user.getFirstName()+" "+MainActivity.user.getLastName());
        phoneTV.setText(MainActivity.user.getPhoneNum());
        emailTV.setText(MainActivity.user.getEmail());

        EditText nameET = view.findViewById(R.id.nameEditText);
        EditText phoneET = view.findViewById(R.id.phoneEditText);
        EditText emailET = view.findViewById(R.id.emailEditText);

        nameET.setText(MainActivity.user.getFirstName()+" "+MainActivity.user.getLastName());
        phoneET.setText(MainActivity.user.getPhoneNum());
        emailET.setText(MainActivity.user.getEmail());

        ImageButton editButton = view.findViewById(R.id.editButton);
        ImageButton saveButton = view.findViewById(R.id.saveButton);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameTV.setVisibility(view.GONE);
                phoneTV.setVisibility(view.GONE);
                emailTV.setVisibility(view.GONE);
                editButton.setVisibility(view.GONE);

                nameET.setVisibility(view.VISIBLE);
                phoneET.setVisibility(view.VISIBLE);
                emailET.setVisibility(view.VISIBLE);
                saveButton.setVisibility(view.VISIBLE);

                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MainActivity.user.setFirstName(nameET.getText().toString().split(" ")[0]);
                        MainActivity.user.setLastName(nameET.getText().toString().split(" ")[1]);
                        MainActivity.user.setPhoneNum(phoneET.getText().toString());
                        MainActivity.user.setEmail(emailET.getText().toString());

                        nameTV.setText(MainActivity.user.getFirstName()+" "+MainActivity.user.getLastName());
                        phoneTV.setText(MainActivity.user.getPhoneNum());
                        emailTV.setText(MainActivity.user.getEmail());

                        nameTV.setVisibility(view.VISIBLE);
                        phoneTV.setVisibility(view.VISIBLE);
                        emailTV.setVisibility(view.VISIBLE);
                        editButton.setVisibility(view.VISIBLE);

                        nameET.setVisibility(view.GONE);
                        phoneET.setVisibility(view.GONE);
                        emailET.setVisibility(view.GONE);
                        saveButton.setVisibility(view.GONE);

                        //TODO: update navigation menu header, check for invalid input or name input with more than one space
                    }
                });
            }
        });


    }

}