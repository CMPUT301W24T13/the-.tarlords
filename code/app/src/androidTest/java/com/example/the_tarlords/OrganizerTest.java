package com.example.the_tarlords;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.the_tarlords.data.QR.QRScanActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class OrganizerTest {
    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Rule
    public ActivityScenarioRule<MainActivity> scenario = new
            ActivityScenarioRule<MainActivity>(MainActivity.class);
    @Test
    public void testScanQRActivityOpen() {
        // Open the navigation drawer
        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open());

        // Click on the "Events Organizing" menu item
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.eventOrganizerListFragment));
        //
        // Perform a click on the scanQrButton
        onView(withId(R.id.scan_qr_button))
                .perform(click());
        // Verify that the expected intent is sent when the button is clicked
        Intents.intended(IntentMatchers.hasComponent(QRScanActivity.class.getName()));
    }
    @Test
    public void testUS_01_02_01() {
        // Open the navigation drawer
        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open());

        // Click on the "Events Organizing" menu item
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.eventOrganizerListFragment));

        // Identify the ListView by its ID
        onView(withId(R.id.eventListView))
                // Use onData() to select an item at a specific position (e.g., position 0)
                .perform(actionOnItemAtPosition(0, click()));

        // Verify that the view with the list of attendees is seen
        Espresso.onView(ViewMatchers.withId(R.id.view_attendance_details))
                .check(matches(ViewMatchers.isDisplayed()));
    }
    @Test
    public void testUS_01_08_01() {
        // Open the navigation drawer
        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open());

        // Click on the "Events Organizing" menu item
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.eventOrganizerListFragment));

        // Identify the ListView by its ID
        onView(withId(R.id.eventListView))
                // Use onData() to select an item at a specific position (e.g., position 0)
                .perform(actionOnItemAtPosition(0, click()));

        // Verify that the view with the map is seen
        Espresso.onView(ViewMatchers.withId(R.id.miniMapCardView))
                .check(matches(ViewMatchers.isDisplayed()));
    }


}
