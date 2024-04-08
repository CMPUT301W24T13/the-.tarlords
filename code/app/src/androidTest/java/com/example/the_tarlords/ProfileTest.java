package com.example.the_tarlords;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.assertion.ViewAssertions;
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
public class ProfileTest {
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
    public void testProfilePageOpen() {
        // Open the navigation drawer
        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open());

        // Click on the "Profile" menu item
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.profileFragment));

        // Verify that the expected fragment is open by seeing if a textview in the fragment is displayed
        onView(withId(R.id.profileFragment)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
    @Test
    public void testUS_02_02_03() {
        // Open the navigation drawer
        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open());

        // Click on the "Profile" menu item
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.profileFragment));

        // Check that the phone on is displayed
        onView(withId(R.id.edit_text_phone)).check(matches(isDisplayed()));

    }
    @Test
    public void test02_05_01() {
        // Open the navigation drawer
        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open());

        // Click on the "Profile" menu item
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.profileFragment));

        // Check that the profile photo is displayed
        onView(withId(R.id.image_view_profile)).check(matches(isDisplayed()));
    }


}
