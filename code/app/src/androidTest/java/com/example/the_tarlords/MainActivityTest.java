package com.example.the_tarlords;

import static androidx.test.espresso.assertion.ViewAssertions.matches;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.DrawerActions;
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
public class MainActivityTest {
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
    public void testScanQrButton() {
        // Perform a click on the scanQrButton
        Espresso.onView(ViewMatchers.withId(R.id.scan_qr_button))
                .perform(ViewActions.click());
        // Verify that the expected intent is sent when the button is clicked
        Intents.intended(IntentMatchers.hasComponent(QRScanActivity.class.getName()));


    }
    @Test
    public void testNavigationDrawerOpen() {
        // Open the navigation drawer
        Espresso.onView(ViewMatchers.withId(R.id.drawer_layout))
                .perform(DrawerActions.open());

        // Verify that the navigation drawer is open
        Espresso.onView(ViewMatchers.withId(R.id.nav_view))
                .check(matches(ViewMatchers.isDisplayed()));


    }


}
