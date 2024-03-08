package com.example.the_tarlords;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static java.util.regex.Pattern.matches;

import android.widget.FrameLayout;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class NavigationDrawerTest {
    @Rule
    public ActivityScenarioRule<MainActivity> scenario = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }
    @Test
    public void testOpenAttendeeEventsFragment() {
        // Open the navigation drawer
        Espresso.onView(ViewMatchers.withId(R.id.drawer_layout))
                .perform(DrawerActions.open());

        // Click on the "Events Attending" menu item
        Espresso.onView(ViewMatchers.withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.eventDetailsFragment));

        // Verify that the expected fragment is open
        onView(withId(R.id.eventDetailsFragment)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

    }

    @Test
    public void testOpenOrgEventsFragment() {
        // Open the navigation drawer
        Espresso.onView(ViewMatchers.withId(R.id.drawer_layout))
                .perform(DrawerActions.open());

        // Click on the "Events Organizing" menu item
        Espresso.onView(ViewMatchers.withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.eventOrganizerListFragment));

        // Verify that the expected fragment is open by seeing if a textview in the fragment is displayed
        onView(withId(R.id.eventOrganizerListFragment)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
    @Test
    public void testOpenProfileFragment() {
        // Open the navigation drawer
        Espresso.onView(ViewMatchers.withId(R.id.drawer_layout))
                .perform(DrawerActions.open());

        // Click on the "Profile" menu item
        Espresso.onView(ViewMatchers.withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.profileFragment));

        // Verify that the expected fragment is open by seeing if a textview in the fragment is displayed
        onView(withId(R.id.profileFragment)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

    }
    @Test
    public void testOpenBrowseEventsFragment() {
        // Open the navigation drawer
        Espresso.onView(ViewMatchers.withId(R.id.drawer_layout))
                .perform(DrawerActions.open());

        // Click on the "Browse Events" menu item
        Espresso.onView(ViewMatchers.withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.eventBrowseFragment));

        // Verify that the expected fragment is open by seeing if a textview in the fragment is displayed
        onView(withId(R.id.eventBrowseFragment)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

    }


}
