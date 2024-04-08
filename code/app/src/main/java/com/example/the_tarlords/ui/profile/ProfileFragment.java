package com.example.the_tarlords.ui.profile;

import static androidx.core.content.PermissionChecker.checkSelfPermission;
import static com.example.the_tarlords.MainActivity.context;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.the_tarlords.MainActivity;
import com.example.the_tarlords.R;
import com.example.the_tarlords.data.photo.ProfilePhoto;
import com.example.the_tarlords.data.users.User;
import com.example.the_tarlords.databinding.FragmentProfileBinding;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Responsible for displaying User information and Profile Photos
 * @see User
 */
public class ProfileFragment extends Fragment implements MenuProvider {
    private User user;
    CircleImageView profilePhotoImageView;
    private static final int REQUEST_GALLERY_PERMISSION = 1;
    Button addProfilePhotoButton;
    EditText firstNameEditText;
    EditText lastNameEditText;
    EditText phoneEditText;
    EditText emailEditText;
    FragmentProfileBinding binding;

    public void setUser(User user) {
        this.user = user;
    }
    public ProfileFragment(){
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (User) getArguments().getParcelable("user");

        }
        else if (MainActivity.user != null) {
            user = MainActivity.user;

            Log.d("profile", "user.getFirstName()");
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

    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri photoUri = result.getData().getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), photoUri);
                        ProfilePhoto profilePhoto = new ProfilePhoto(user.getUserId(),bitmap);
                        profilePhoto.compressPhoto();
                        user.setProfilePhoto(profilePhoto);
                        profilePhotoImageView.setImageBitmap(bitmap);
                        user.setPhotoIsDefault(false);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
            }

    );

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
        //add more views here as desired

        //set content for views in fragment
        if (user != null) {

            firstNameEditText.setText(user.getFirstName());
            lastNameEditText.setText(user.getLastName());
            phoneEditText.setText(user.getPhoneNum());
            emailEditText.setText(user.getEmail());
            //set additional views content here as desired

            displayProfilePhoto(profilePhotoImageView);
        }

        //for user to add or update profile photo
        addProfilePhotoButton.setOnClickListener(v -> {
            PopupMenu addPhotoOptions = new PopupMenu(this.getContext(), v);
            addPhotoOptions.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.camera_open) {
                        //take photo
                        Intent profilePhotoCapture = new Intent(getActivity(), TakePhotoActivity.class);
                        startActivity(profilePhotoCapture);
                        return true;
                    } else if (item.getItemId() == R.id.gallery_open) {
                        if (checkSelfPermission(getContext(),android.Manifest.permission.READ_MEDIA_IMAGES) != PermissionChecker.PERMISSION_GRANTED ) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_GALLERY_PERMISSION);
                        } else {
                            //upload photo
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            launcher.launch(intent);
                        }
                        return true;
                    } else if (item.getItemId() == R.id.remove_current_photo) {
                        //remove current photo:
                        user.setProfilePhoto(null);
                        displayProfilePhoto(profilePhotoImageView);
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

        if (isAdded() && getContext() != null) {
            menu.clear();
            menuInflater.inflate(R.menu.options_menu, menu);

            if (MainActivity.isAdmin){
                menu.findItem(R.id.deleteOptionsMenu).setVisible(true);
            }
            if (user == MainActivity.user){
                menu.findItem(R.id.editOptionsMenu).setVisible(true);
                menu.findItem(R.id.saveOptionsMenu).setVisible(false);
                menu.findItem(R.id.cancelOptionsMenu).setVisible(false);
            }
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
                menu.findItem(R.id.editOptionsMenu).setVisible(false);
                menu.findItem(R.id.saveOptionsMenu).setVisible(true);
                menu.findItem(R.id.cancelOptionsMenu).setVisible(true);
            } else if (user ==MainActivity.user){
                menu.findItem(R.id.editOptionsMenu).setVisible(true);
                menu.findItem(R.id.saveOptionsMenu).setVisible(false);
                menu.findItem(R.id.cancelOptionsMenu).setVisible(false);
            }
            if (MainActivity.isAdmin){
                menu.findItem(R.id.deleteOptionsMenu).setVisible(true);
            }
            displayProfilePhoto(getView().findViewById(R.id.image_view_profile));

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
                addProfilePhotoButton.setVisibility(View.VISIBLE);
                firstNameEditText.setEnabled(true);
                lastNameEditText.setEnabled(true);
                phoneEditText.setEnabled(true);
                emailEditText.setEnabled(true);

                requireActivity().invalidateMenu(); //required in order to call onPrepareMenu() and repopulate menu with new options
            }
            else if (menuItem.getItemId() == R.id.cancelOptionsMenu) {
                addProfilePhotoButton.setVisibility(View.GONE);
                firstNameEditText.setEnabled(false);
                lastNameEditText.setEnabled(false);
                phoneEditText.setEnabled(false);
                emailEditText.setEnabled(false);
                requireActivity().invalidateMenu(); //required in order to call onPrepareMenu() and repopulate menu with new options
            }
            //if save button selected, update user info and send to firestore
            else if (menuItem.getItemId() == R.id.saveOptionsMenu) {
                if (checkValidInput(this.getView())) {
                    // if the profile info has been filled out they can leave edit mode
                    addProfilePhotoButton.setVisibility(View.GONE);
                    firstNameEditText.setEnabled(false);
                    lastNameEditText.setEnabled(false);
                    phoneEditText.setEnabled(false);
                    emailEditText.setEnabled(false);
                    user.setEmail(emailEditText.getText().toString());
                    user.setFirstName(firstNameEditText.getText().toString());
                    user.setLastName(lastNameEditText.getText().toString());
                    user.setPhoneNum(phoneEditText.getText().toString());


                    displayProfilePhoto(profilePhotoImageView);
                    MainActivity.user.sendToFireStore();

                    //update navigation header (slide out menu) with newly updated information
                    MainActivity.updateNavigationDrawerHeader();
                    requireActivity().invalidateMenu(); //required in order to call onPrepareMenu() and repopulate menu with new options
                    checkNameChanged();
                }
            }
            else if (menuItem.getItemId()==R.id.deleteOptionsMenu){
                AlertDialog dialog = new AlertDialog.Builder(requireContext())
                        .setMessage("Are you sure you would like to delete the user " + user.getFirstName()+" "+user.getLastName() + "?")
                        .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                user.removeFromFirestore();
                                try {
                                    // Return to event organizer list fragment
                                    NavHostFragment.findNavController(ProfileFragment.this)
                                            .navigate(R.id.action_profileViewFragment_pop);
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
            }
        }
        return false;
    }

    /**
     * Checks if the user updated firstName or LastName
     * textViews on the fragment , if so triggers
     * setImageBitmap to update the autogenerated profile photo
     */

    public void checkNameChanged() {
        View v = this.getView();
        EditText firstNameEditText = v.findViewById(R.id.edit_text_first_name);
        String firstNamePostEdit = firstNameEditText.getText().toString();

        EditText lastNameEditText = v.findViewById(R.id.edit_text_last_name);
        String lastNamePostEdit = lastNameEditText.getText().toString();
        if (user.getPhotoIsDefault()) {
            if (!firstNamePostEdit.equals(user.getProfilePhoto().getPhotoFirstName()) || !lastNamePostEdit.equals(user.getProfilePhoto().getPhotoLastName())) {
                ProfilePhoto profilePhoto = new ProfilePhoto(user.getUserId(),
                        null, user.getFirstName(), user.getLastName());

                profilePhoto.autoGenerate();
                user.setProfilePhoto(profilePhoto);
                profilePhotoImageView.setImageBitmap(profilePhoto.getBitmap());
            }
        }
    }

    /**
     * Checks if the Views contain correctly formatted / valid information
     * ex. phoneNumber only contains digits
     * @param v, any view on the fragment
     * @return true if user input is valid , false otherwise
     */
    public boolean checkValidInput(View v) {
        boolean validInput = true;
        phoneEditText = v.findViewById(R.id.edit_text_phone);
       if (phoneEditText.getText().toString().length()!=0&&(phoneEditText.getText().toString().length() < 9 || phoneEditText.getText().toString().length() > 20)) {
            // 9 digits for local code, up to 20 poss digits by ITU standards
            phoneEditText.setError("Invalid phone number.");
            validInput = false;
        }
        emailEditText = v.findViewById(R.id.edit_text_email);
        if (emailEditText.getText().toString().length()!=0) {
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

    /**
     * Responsible for displaying the Profile Photo in the profilePhotoImageView
     * based on the Users profilePhotoData ( String representation of an image )
     * @param profilePhotoImageView
     */
    public void displayProfilePhoto(ImageView profilePhotoImageView) {
        if (user.getProfilePhoto() == null || user.getProfilePhoto().getBitmap() == null) {//if user does not have a profile photo, generate one
            ProfilePhoto profilePhoto = new ProfilePhoto(user.getUserId(),
                    null, user.getFirstName(), user.getLastName());
            profilePhoto.autoGenerate();
            user.setProfilePhoto(profilePhoto);
            user.setPhotoIsDefault(true);
        }
        profilePhotoImageView.setImageBitmap(user.getProfilePhoto().getBitmap());
        MainActivity.updateNavigationDrawerHeader();
    }
}