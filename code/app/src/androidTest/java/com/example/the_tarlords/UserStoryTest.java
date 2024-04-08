package com.example.the_tarlords;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import com.example.the_tarlords.data.QR.QRScanActivity;
import com.example.the_tarlords.data.event.Event;
import com.example.the_tarlords.data.users.Attendee;
import com.example.the_tarlords.data.users.User;
import com.example.the_tarlords.ui.event.EventBrowseFragment;
import com.example.the_tarlords.ui.event.EventDetailsFragment;
import com.example.the_tarlords.ui.home.EventListFragment;
import com.example.the_tarlords.ui.profile.ProfileFragment;
import com.example.the_tarlords.ui.profile.UploadPhotoActivity;
import com.google.android.material.navigation.NavigationView;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import android.content.Intent;
import android.view.MenuItem;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.intent.Intents;

import java.io.IOException;

public class UserStoryTest {

    private Attendee attendee;

    @Before
    public void setUp() throws IOException {
        // Create a mock event
        Event event = mock(Event.class);
        // Create an attendee object
        attendee = new Attendee("123", "John", "Doe", "123456789", "john@example.com", event);
    }

    //working
    @Test
    public void testUS_02_01_01() {
        // Ensure initial check-in status is false
        assertFalse(attendee.getCheckInStatus());
        // Perform check-in by scanning QR code
        attendee.setCheckInStatus(true);
        // Verify that check-in status is true after scanning QR code
        assertTrue(attendee.getCheckInStatus());
    }

    @Test
    public void testUS_02_02_01() {
        Intents.init();

        // Launch MainActivity
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);

        scenario.onActivity(activity -> {
            DrawerLayout drawerLayout = activity.findViewById(R.id.drawer_layout);
            if (drawerLayout != null) {
                drawerLayout.openDrawer(GravityCompat.START);

                NavigationView nav = activity.findViewById(R.id.nav_view);

                MenuItem profileItem = nav.getMenu().findItem(R.id.profileViewFragment);
                if (profileItem != null) {

                    onView(withText(profileItem.getTitle().toString())).perform(click());
                    onView(withId(R.id.button_add_profile_photo)).perform(click());
                    onView(withText(R.id.gallery_open)).perform(click());
                    intended(hasComponent(QRScanActivity.class.getName()));

                }

            }
        });
        Intents.release();
    }


    @Test
    public void testUS_02_02_02() {
        Intents.init();

        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);

        scenario.onActivity(activity -> {
            DrawerLayout drawerLayout = activity.findViewById(R.id.drawer_layout);
            if (drawerLayout != null) {
                drawerLayout.openDrawer(GravityCompat.START);

                NavigationView nav = activity.findViewById(R.id.nav_view);
                MenuItem profileItem = nav.getMenu().findItem(R.id.profileViewFragment);
                if (profileItem != null) {
                    onView(withText(profileItem.getTitle().toString())).perform(click());
                    onView(withId(R.id.button_add_profile_photo)).perform(click());
                    onView(withText(R.id.remove_current_photo)).perform(click());
                }

            }
        });

        Intents.release();
    }

    @Test
    public void testUS_02_02_03() {
        ActivityScenario.launch(MainActivity.class);
        FragmentScenario<ProfileFragment> scenario = FragmentScenario.launchInContainer(ProfileFragment.class);

        String firstName = "John";
        String lastName = "Doe";
        String newHomepage = "https://www.example.com";
        String phoneNum = "7801111111";
        String email = "john.doe@example.com";

        onView(withId(R.id.edit_text_first_name)).perform(replaceText(firstName));
        onView(withId(R.id.edit_text_last_name)).perform(replaceText(lastName));
        //onView(withId(R.id.edit_text_homepage)).perform(replaceText(newHomepage));
        onView(withId(R.id.edit_text_phone)).perform(replaceText(phoneNum));
        onView(withId(R.id.edit_text_email)).perform(replaceText(email));

        openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext());
        onView(withText("Save")).check(matches(isDisplayed())).perform(click());
    }


    @Test
    public void testUS_02_05_01() {
        User user = new User("123", "John", "Doe", "123456789", "john@example.com");

        user.getProfilePhoto().autoGenerate();
        user.setPhotoIsDefault(true);

        assertTrue(user.getPhotoIsDefault());
    }

    @Test
    public void testUS_02_07_01() {
        Intents.init();

        // Launch MainActivity
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);

        scenario.onActivity(activity -> {
            DrawerLayout drawerLayout = activity.findViewById(R.id.drawer_layout);

            drawerLayout.openDrawer(GravityCompat.START);

            NavigationView nav = activity.findViewById(R.id.nav_view);
            if (nav != null) {
                MenuItem browse = nav.getMenu().findItem(R.id.eventBrowseFragment);

            }

        });



//        scenario.onActivity(activity -> {
//            DrawerLayout drawerLayout = activity.findViewById(R.id.drawer_layout);
//            if (drawerLayout != null) {
//                drawerLayout.openDrawer(GravityCompat.START);
//
//                // Simulate click action on the "Profile" item
//                NavigationView navigationView = activity.findViewById(R.id.nav_view);
//                if (navigationView != null) {
//                    MenuItem eventList = navigationView.getMenu().findItem(R.id.eventListFragment);
//                    //if (eventList != null) {
//                        // Perform click action on the "Profile" item
//                    //    onView(withText(eventList.getTitle().toString())).perform(click());
//
////                        onView(withText("eventTitle")).check(matches(isDisplayed()));
//                    //}
//                }
//            }
//        });

        Intents.release();
    }

    @Test
    public void testBrowseEventPostersAndDetails() {
        // Mock browsing event posters and details
        // Assuming there is a method to browse event posters and details
        // For example: attendee.browseEventPostersAndDetails();
        // Verify that attendee can browse event posters and details
        // Assert.assertTrue(...);
    }

    @Test
    public void testViewSignedUpEvents() {
        // Mock viewing signed up events
        // Assuming there is a method to view signed up events
        // For example: attendee.viewSignedUpEvents();
        // Verify that attendee can view signed up events
        // Assert.assertTrue(...);
    }

}

