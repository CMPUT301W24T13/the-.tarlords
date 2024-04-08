package com.example.the_tarlords;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.the_tarlords.data.Notification.FCMService;
import com.example.the_tarlords.data.event.Event;
import com.example.the_tarlords.data.map.LocationHelper;
import com.example.the_tarlords.data.users.User;
import com.example.the_tarlords.databinding.ActivityMainBinding;
import com.example.the_tarlords.ui.event.EventOrganizerListFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Main Activity , responsible for getting stored User Id
 * Granting location permissions
 * Hosting the navigation bar and most of the fragments
 */

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private AppBarConfiguration mAppBarConfiguration;
    public static Toolbar toolbar;
    public static FirebaseFirestore db = FirebaseFirestore.getInstance();
    // Create a reference to the users collection
    CollectionReference usersRef = db.collection("Users");

    public static User user;

    public static Boolean isAdmin = false;
    private LocationHelper locationHelper;
    public static Boolean locationGranted;

    private static String userId;
    private static View hView;
    public static Context context;
    private Object lock = new Object();




    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        if(BuildConfig.DEBUG){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

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
        //userId = "1";

        if (userId == null) {
            // user has not used app before
            // Generate a new user ID (you can use any logic to generate a unique ID)
            userId = generateNewUserId();

            // Save the user ID locally
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("user_id", userId);
            editor.apply();


            user = new User();
            user.setUserId(userId);
            isAdmin = false;
            setDeviceFCMToken();

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


                            // needs to be above the setBinding()
                            if (user.getIsAdmin() != null){
                                isAdmin = user.getIsAdmin();
                            }
                            setDeviceFCMToken();

                            //sets content binding now that userId is no longer null (must stay above updateNavigationDrawerHeader()
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
                            isAdmin = false;
                            // Update UI with default user information
                            updateNavigationDrawerHeader();

                            // Navigate to profile fragment
                            navigateToProfileFragment();
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure
                        Log.e("debug", "failed to get the document", e);
                    });



        }
        //request location permissions
        locationHelper = new LocationHelper(this);
        if(!locationHelper.checkLocationPermission()){
            locationHelper.requestLocationPermission();
        }else{
            locationGranted = true;
        }

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
        toolbar =binding.appBarMain.toolbar;
        //app bar set up
        setSupportActionBar(toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        // Passing each menu ID as a set of Ids because each menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.eventListFragment, R.id.eventOrganizerListFragment, R.id.eventBrowseFragment, R.id.profileFragment, R.id.profileBrowseFragment, R.id.imageBrowseFragment)
                .setOpenableLayout(drawer)
                .build();

        //navigation set up (must go below appBar config)
        NavigationView navigationView = binding.navView;
        hView = navigationView.getHeaderView(0);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Hide or show ProfileBrowseFragment menu item based on isAdmin
        Menu navMenu = navigationView.getMenu();
        MenuItem profileBrowseItem = navMenu.findItem(R.id.profileBrowseFragment);
        MenuItem ImageBrowseItem = navMenu.findItem(R.id.imageBrowseFragment);

        if (profileBrowseItem != null && ImageBrowseItem != null) {
            profileBrowseItem.setVisible(isAdmin);
            profileBrowseItem.setEnabled(isAdmin);
            ImageBrowseItem.setVisible(isAdmin);
            ImageBrowseItem.setEnabled(isAdmin);
        }
    }

    /**
     * Redirects user to event details fragment for the specified event.
     * @param event
     */
    private void navigateToEventDetailsFragment(Event event) {
        Log.e("QrCode", "here");
        Bundle args = new Bundle();
        args.putParcelable("event", event);
        args.putBoolean("isOrganizer", false);
        args.putBoolean("browse",true);
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
        Navigation.findNavController(this, R.id.nav_host_fragment_content_main)
                .navigate(R.id.action_eventListFragment_to_profileFragment);
    }

    /**
     * This is the back button stuff.
     * @return true if successful
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
     * User id generator for the sharedPreferences
     * @return randomly generated userId string
     */
    @SuppressLint("HardwareIds")
    private String generateNewUserId() {
        // Replace with your user logic to generate an ID
        return Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
    }


    /**
     * gets and sets the FCM token for each device and user and stores it into firebase
     */
    private void setDeviceFCMToken(){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task ->{
           if(task.isSuccessful()){
               String token = task.getResult();
               Log.d("FCM token",token);
               user.setFCM(token);
               db.collection("Users").document(user.getUserId()).update("FCM",token);
           }
        });
    }



    /**
     * Mandatory empty method here because MainActivity implements OnMapReadyCallBack
     * @see com.example.the_tarlords.ui.MapsFragment
     * @param googleMap
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

    }

    /**
     * Requests Location Permissions from User on Start
     * @param requestCode The request code passed in {@link #requestPermissions(
     * android.app.Activity, String[], int)}
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *     which is either {@link android.content.pm.PackageManager#PERMISSION_GRANTED}
     *     or {@link android.content.pm.PackageManager#PERMISSION_DENIED}. Never null.
     *
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Delegate handling to LocationHelper
        locationHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Update locationGranted based on permission result
        locationGranted = locationHelper.checkLocationPermission();
    }

    /**
     * navigates to the corresponding fragment with the given intent
     * @param intent The new intent that was started for the activity.
     *
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null && intent.getAction() != null && intent.getAction().equals("OPEN_EVENT_DETAILS")) {
            if (intent.hasExtra("event")) {
                Event event = (Event) intent.getSerializableExtra("event");
                navigateToEventDetailsFragment(event);

            }
        } else if (intent != null && intent.getAction() != null && intent.getAction().equals("OPEN_EVENT_DETAILS_ORGANIZER")) {
            if (intent.hasExtra("event")) {
                Event event = (Event) intent.getSerializableExtra("event");
                Bundle args = new Bundle();
                args.putParcelable("event", event);
                args.putBoolean("isOrganizer", true);
                try {
                    Navigation.findNavController(this, R.id.nav_host_fragment_content_main)
                            .navigate(R.id.action_eventFragment_to_eventDetailsFragment, args);
                } catch (Exception ignore) {
                }
            }
        }
    }
}