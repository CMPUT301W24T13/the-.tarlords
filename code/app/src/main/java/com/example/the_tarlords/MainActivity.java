package com.example.the_tarlords;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;

import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.the_tarlords.data.event.Event;
import com.example.the_tarlords.data.users.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.example.the_tarlords.data.QR.QRScanActivity;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.the_tarlords.databinding.ActivityMainBinding;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.type.LatLng;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private AppBarConfiguration mAppBarConfiguration;
    public static FirebaseFirestore db = FirebaseFirestore.getInstance();
    // Create a reference to the users collection
    CollectionReference usersRef = db.collection("Users");

    public static User user;
    private static String userId;
    private static View hView;
    private Object lock = new Object();
    private FusedLocationProviderClient client;
    private static final int REQUEST_LOCATION_PERMISSION = 99;
    // Need this for location
    private String eventId;


    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //TODO: check if returning from profile pic activity, if so redirect to profile fragment


        /**
         * THIS IS THE USER STUFF
         * It uses the device id to check if a new user needs to be generated or not.
         */
        // Check if the device id is already generated and stored
        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        //you can get the user id if the user already has used the app once before , do what you need with it
        userId = preferences.getString("user_id", null);

        /*
         * Next line can be used for testing and debugging (eg testing admin). Uncomment
         * and set user value to your choice of ID. PLEASE COMMENT IT OUT AFTER TESTING
         */
        //userId = "whatever you want";

        if (userId == null) {
            // user has not used app before
            // Generate a new user ID (you can use any logic to generate a unique ID)
            userId = generateNewUserId();

            // Save the user ID locally
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("user_id", userId);
            editor.apply();

            // the profile fields are going to have to be filled with some default info the first time, but the ID is the one we generated
            user = new User(userId, "First Name", "Last Name", "Phone Number", "email");

            //sets content binding now that userId is no longer null (must stay above updateNavigationDrawerHeader()
            setBinding();

            // Update UI with default user information
            updateNavigationDrawerHeader();

            // If it's the first launch, navigate to profile fragment to get user info
            navigateToProfileFragment();
        } else {
            //user has been here before
            String finalUserId = userId;
            Log.d("debug", userId);

            //queries firebase for user info associated with the userId
            usersRef.whereEqualTo("userId", userId).get()
                    .addOnSuccessListener(querySnapshot -> {
                        if (!querySnapshot.isEmpty()) {
                            // User found, documentSnapshot contains user data
                            DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);

                            //creates 'user' object from firestore data, now you can use 'user' object
                            user = documentSnapshot.toObject(User.class);

                            // sets content binding now that 'user' object is not null
                            setBinding();

                            //updates navigation UI header
                            updateNavigationDrawerHeader();

                            //checks if user is returning from QR activity
                            if (getIntent().getParcelableExtra("event") != null) {
                                Event event = getIntent().getParcelableExtra("event");
                                //opens event detail fragment of scanned event
                                navigateToEventDetailsFragment(event);
                            }
                        } else {
                            Log.d("debug", "didn't find a user");
                            // This is a case where user has used app on device but user info is not on firebase yet (my case, developer)
                            user = new User(finalUserId, "khushi", "lad", "780-111-1111", "john.doe@ualberta.ca");
                            // Update UI with default user information
                            updateNavigationDrawerHeader();
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure
                        Log.e("debug", "failed to get the document", e);
                    });

        }

        // Initialize the FusedLocationProviderClient
        client = LocationServices.getFusedLocationProviderClient(this);
        //TODO take out this test case
        getMyLocation("dzAK3vDdNTdk76xcyo3U");


    }

    /**
     * This function initializes the app by setting the main activity
     * content binding, setting up the navigation controller, and configuring
     * the appbar.
     */
    private void setBinding() {
        //content binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //app bar set up
        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        // Passing each menu ID as a set of Ids because each menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.eventListFragment, R.id.eventOrganizerListFragment, R.id.eventBrowseFragment, R.id.profileFragment)
                .setOpenableLayout(drawer)
                .build();
        //QR code scanner button set up
        binding.appBarMain.scanQrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //passes in user info in case of check-in QR scan
                Intent intent = new Intent(MainActivity.this, QRScanActivity.class);
                intent.putExtra("userId", user.getUserId());

                startActivity(intent);
            }
        });

        //navigation set up (must go below appBar config)
        NavigationView navigationView = binding.navView;
        hView = navigationView.getHeaderView(0);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    /**
     * Redirects user to event details fragment for the specified event.
     * @param event
     */
    private void navigateToEventDetailsFragment(Event event) {
        Log.e("QrCode", "here");
        /*EventDetailsFragment fragment = EventDetailsFragment.newInstance(event, false);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, fragment); // R.id.fragment_container is the ID of your fragment container
        fragmentTransaction.addToBackStack(null); // Optional: adds the transaction to the back stack
        fragmentTransaction.commit();*/
        Bundle args = new Bundle();
        args.putParcelable("event", event);
        args.putBoolean("isOrganizer", false);
        try {
            Navigation.findNavController(this, R.id.nav_host_fragment_content_main)
                    .navigate(R.id.action_eventFragment_to_eventDetailsFragment, args);
        } catch (Exception ignore) {
        }
    }

    /**
     * Redirects user to profile fragment.
     */
    private void navigateToProfileFragment() {
        /*ProfileFragment fragment = ProfileFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, fragment); // R.id.fragment_container is the ID of your fragment container
        fragmentTransaction.addToBackStack(null); // Optional: adds the transaction to the back stack
        fragmentTransaction.commit();*/
        Navigation.findNavController(this, R.id.nav_host_fragment_content_main)
                .navigate(R.id.action_eventListFragment_to_profileFragment);
    }

    /**
     * This is the back button stuff.
     * @return no fucking clue
     */
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    /**
     * slide out nav bar set-up, can be called to update information.
     **/
    //TODO: Implement profile picture
    public static void updateNavigationDrawerHeader() {
        // Set navigation drawer header information based on the user object
        if (user != null) {
            TextView name = hView.findViewById(R.id.profileName);
            TextView phoneNum = hView.findViewById(R.id.phoneNumber);
            TextView email = hView.findViewById(R.id.email);
            ImageView profilePic = hView.findViewById(R.id.profilePic);

            name.setText(user.getFirstName() + " " + user.getLastName());
            phoneNum.setText(user.getPhoneNum());
            email.setText(user.getEmail());
            if (user != null && user.getProfilePhoto() != null && user.getProfilePhoto().getBitmap() != null) {
                Bitmap bitmap = user.getProfilePhoto().getBitmap();
                profilePic.setImageBitmap(bitmap);
            } else if (user.getProfilePhotoData() != null) {
                user.setProfilePhotoFromData(user.getProfilePhotoData());
                Bitmap bitmap = user.getProfilePhoto().getBitmap();
                profilePic.setImageBitmap(bitmap);
            }
        } else {
            Log.e("debug", "User object is null");
            // Handle the case where the User object is null
            user = new User(userId, "khushi", "null", "780-111-1111", "john.doe@ualberta.ca");
        }
    }

    /**
     * User id generator for the sharedPreferences stuff.
     *
     * Side note: Should probably be in User class, tried to put it there, but Settings was bugging
     * @return randomly generated userId string
     */
    @SuppressLint("HardwareIds")
    private String generateNewUserId() {
        // Replace with your user logic to generate an ID
        return Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * Mandatory empty method here because MainActivity implements OnMapReadyCallBack
     * the function is implemented and used in MapsFragment.java
     * @param googleMap
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

    }

    /**
     * Method to get users location and if needed the permissions
     */
    public void getMyLocation(String eventId) {
        this.eventId = eventId;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission not granted, request it
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION);

        }else{
            // Permission already granted, proceed to get location
            Task<Location> task = client.getLastLocation();
            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null){
                        // get latitude and longitude, and user name , put it on firebase
                        Double latitude = location.getLatitude();
                        Double longitude = location.getLongitude();
                        checkInLocation(latitude, longitude, eventId);
                    }else{
                        Log.d("maps", "users location during check-in was null");
                    }

                }
            });
        }

    }
    /**
     * If location permissions are requested
     * handle the case where they grant permission
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            // Check if the requested permissions were granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, call getMyLocation() again to proceed
                getMyLocation(eventId);
            } else {
                // Permission denied, we don't do anything
                Log.d("maps", "location permissions denied");
            }
        }
    }
    /**
     * Method to help getMyLocation put the users location onto Firebase
     */
    public void checkInLocation(Double lat, Double lon, String eventId){
        // Create a new check-in document
        Map<String, Object> checkInData = new HashMap<>();
        if(user != null){
            checkInData.put("name", user.getFirstName());
        }else{
            checkInData.put("name", "attendee");
        }

        checkInData.put("latitude", lat);
        checkInData.put("longitude", lon);

        // Add the new check-in document to the "checkIns" subcollection
        db.collection("Events").document(eventId).collection("checkIns")
                .add(checkInData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("maps", "Check-in added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("maps", "Error adding check-in", e);
                        // Handle failure to add check-in, if needed
                    }
                });

    }

}