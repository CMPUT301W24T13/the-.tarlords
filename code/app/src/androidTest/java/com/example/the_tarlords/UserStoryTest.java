package com.example.the_tarlords;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import com.example.the_tarlords.data.QR.QRScanActivity;
import com.example.the_tarlords.data.event.Event;
import com.example.the_tarlords.data.users.Attendee;
import com.example.the_tarlords.data.users.User;
import com.example.the_tarlords.ui.event.EventBrowseFragment;
import com.example.the_tarlords.ui.event.EventDetailsFragment;
import com.example.the_tarlords.ui.event.EventEditFragment;
import com.example.the_tarlords.ui.home.EventListFragment;
import com.example.the_tarlords.ui.image.ImageBrowseFragment;
import com.example.the_tarlords.ui.profile.ProfileFragment;
import com.example.the_tarlords.ui.profile.UploadPhotoActivity;
import com.google.android.material.navigation.NavigationView;

import org.junit.Before;
import org.junit.Test;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static org.hamcrest.Matchers.anything;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import android.content.Intent;
import android.view.MenuItem;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static androidx.test.espresso.Espresso.onData;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.instanceOf;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static androidx.test.espresso.Espresso.onData;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingResource;

import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.not;


public class UserStoryTest {

    private Attendee attendee;

    @Before
    public void setUp() throws IOException {
        Event event = mock(Event.class);
        attendee = new Attendee("123", "John", "Doe", "123456789", "john@example.com", event);
    }

    @Test
    public void testUS_01_01_01() {
        Intents.init();

        String eventName = "ExampleEvent";
        String eventLocation = "ExampleLocation";

        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.onActivity(activity -> {
            NavigationView nav = activity.findViewById(R.id.nav_view);
            MenuItem editEvent = nav.getMenu().findItem(R.id.eventEditFragment);
            if (editEvent != null) {
                onView(withText(editEvent.getTitle().toString())).perform(click());
                onView(withId(R.id.et_event_name)).perform(replaceText(eventName));
                onView(withId(R.id.et_event_location)).perform(replaceText(eventLocation));
            }
        });
        Intents.release();
    }

    @Test
    public void testUS_02_01_01() {
        assertFalse(attendee.getCheckInStatus());
        attendee.setCheckInStatus(true);
        assertTrue(attendee.getCheckInStatus());
    }

    @Test
    public void testUS_02_02_01() {
        Intents.init();
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.onActivity(activity -> {
            NavigationView nav = activity.findViewById(R.id.nav_view);
            MenuItem profile = nav.getMenu().findItem(R.id.profileViewFragment);
            if (profile != null) {
                onView(withText(profile.getTitle().toString())).perform(click());
                onView(withId(R.id.button_add_profile_photo)).perform(click());
                onView(withText(R.id.gallery_open)).perform(click());
                intended(hasComponent(QRScanActivity.class.getName()));
            }
        });
        Intents.release();
    }


    @Test
    public void testUS_02_02_02() {
        Intents.init();

        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.onActivity(activity -> {
            NavigationView nav = activity.findViewById(R.id.nav_view);
            MenuItem profile = nav.getMenu().findItem(R.id.profileViewFragment);
            if (profile != null) {
                onView(withText(profile.getTitle().toString())).perform(click());
                onView(withId(R.id.button_add_profile_photo)).perform(click());
                onView(withText(R.id.remove_current_photo)).perform(click());
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
        String phoneNum = "7801111111";
        String email = "john.doe@example.com";

        onView(withId(R.id.edit_text_first_name)).perform(replaceText(firstName));
        onView(withId(R.id.edit_text_last_name)).perform(replaceText(lastName));
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
    public void testUS_02_07_01() throws InterruptedException {
        Intents.init();
        Event mockEvent = mock(Event.class);
        when(mockEvent.getId()).thenReturn("ExampleId");
        when(mockEvent.getName()).thenReturn("ExampleEvent");


        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.onActivity(activity -> {
            NavController navController = Navigation.findNavController(activity, R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.imageBrowseFragment);
        });
        FragmentScenario<ImageBrowseFragment> fragmentScenario = FragmentScenario.launchInContainer(ImageBrowseFragment.class);
        fragmentScenario.onFragment(fragment -> fragment.getImages().add(mockEvent.getPoster()));

        Intents.release();
    }

    @Test
    public void testUS_02_08_01() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);

        FragmentScenario<ImageBrowseFragment> fragmentScenario = FragmentScenario.launchInContainer(ImageBrowseFragment.class);
        onView(withId(R.id.imageListView)).check(matches(isDisplayed()));
    }

    @Test
    public void testUS_02_09_01() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.onActivity(activity -> {
            NavigationView nav = activity.findViewById(R.id.nav_view);
            MenuItem events = nav.getMenu().findItem(R.id.eventListView);

            if (events != null) {
                onView(withText(events.getTitle().toString())).perform(click());
                onView(withId(R.id.eventListView)).check(matches(isDisplayed()));
                onView(withId(R.id.eventListView)).check(matches(hasMinimumChildCount(1)));
            }
        });
    }



}

