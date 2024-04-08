package com.example.the_tarlords;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.PickerActions.setDate;
import static androidx.test.espresso.contrib.PickerActions.setTime;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.EasyMock2Matchers.equalTo;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Instrumentation;
import android.content.pm.PackageManager;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.GrantPermissionRule;

import com.example.the_tarlords.data.Alert.AlertFragment;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;

/**
 * Tests the UI of the Alerts Fragment
 */

@RunWith(AndroidJUnit4.class)
public class AlertUITest {
    @Rule
    public IntentsTestRule<MainActivity> intentsTestRule = new IntentsTestRule<>(MainActivity.class);

    // Grant location permissions before running the tests
    @Rule
    public GrantPermissionRule locationPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION);

    @Rule
    public GrantPermissionRule notificationPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.POST_NOTIFICATIONS);

    // US 02.04.01 As an attendee, I want to view event details and announcements within the app.
    // US 01.03.01 As an organizer, I want to send notifications to all attendees through the app.
    @Test
    public void alertFragmentNavigation(){
        ActivityScenario.launch(MainActivity.class);

        // wait for listview to load
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(ViewMatchers.withContentDescription("Open navigation drawer")).perform(ViewActions.click());
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.eventOrganizerListFragment)).perform(click());

        onView(withId(R.id.addOptionsMenu)).check(matches(isDisplayed()));
        onView(withId(R.id.addOptionsMenu)).perform(click());

        //Espresso.onView(ViewMatchers.withId(R.id.edit_text_first_name)).perform(ViewActions.clearText(), ViewActions.typeText(firstName));

        String eventTitle = "new test event title";
        onView(withId(R.id.et_event_name)).perform(clearText(),typeText(eventTitle));
        closeSoftKeyboard();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = 10;
        int minute = 0;

        // sets the date in the DatePickerDialog
        onView(withId(R.id.tv_edit_event_startDate)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(setDate(year, month + 1, day));
        onView(withText(android.R.string.ok)).perform(click());

        onView(withId(R.id.tv_edit_event_endDate)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(setDate(year, month + 1, day));
        onView(withText(android.R.string.ok)).perform(click());

        // set the time in the TimepickerDialog
        onView(withId(R.id.tv_edit_event_startTime)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName())))
                .perform(setTime(hour, minute));
        onView(withText(android.R.string.ok)).perform(click());

        onView(withId(R.id.tv_edit_event_endTime)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName())))
                .perform(setTime(hour+8, minute));
        onView(withText(android.R.string.ok)).perform(click());

        String location = "location";
        onView(withId(R.id.et_event_location)).perform(clearText(),typeText(location));
        closeSoftKeyboard();

        onView(withId(R.id.saveOptionsMenu)).check(matches(isDisplayed()));
        onView(withId(R.id.saveOptionsMenu)).perform(click());
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onData(anything()).inAdapterView(withId(R.id.eventListView)).atPosition(0).perform(click());
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.anouncementsOptionsMenu)).check(matches(isDisplayed()));
        onView(withId(R.id.anouncementsOptionsMenu)).perform(click());

        onView(withId(R.id.button_add_alert)).perform(click());

        String alertTitle = "alert title";
        String alertMessage = "alert message";

        onView(withId(R.id.edit_text_alert_title)).perform(click());
        onView(withId(R.id.edit_text_alert_title)).perform(clearText(),typeText(alertTitle));
        closeSoftKeyboard();


        onView(withId(R.id.edit_text_alert_message)).perform(click());
        onView(withId(R.id.edit_text_alert_message)).perform(clearText(),typeText(alertMessage));
        closeSoftKeyboard();
        onView(withText("add")).perform(click());


        try {
            Thread.sleep(600);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        pressBack();

        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.deleteOptionsMenu)).check(matches(isDisplayed()));
        onView(withId(R.id.deleteOptionsMenu)).perform(click());
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withText("Delete")).perform(click());



    }
}
