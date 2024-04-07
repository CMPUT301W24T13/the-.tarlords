package com.example.the_tarlords;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.hamcrest.CoreMatchers.anything;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.the_tarlords.data.Alert.AlertFragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests the UI of the Alerts Fragment
 */

@RunWith(AndroidJUnit4.class)
public class AlertUITest {

    // test navigation to alert fragment
    @Test
    public void alertFragmentNavigation(){
        ActivityScenario.launch(MainActivity.class);
        /*
        //onView(withId(R.id.eventListView)).check(matches(isDisplayed()));

        // wait for listview to load
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(ViewMatchers.withContentDescription("Open navigation drawer")).perform(ViewActions.click());
        onView(withId(R.id.eventBrowseFragment)).perform(click());

        onData(anything()).inAdapterView(withId(R.id.eventListView)).atPosition(1).perform(click());
        onView(withId(R.id.showAnouncementsMenu)).check(matches(isDisplayed()));
        onView(withId(R.id.showAnouncementsMenu)).perform(click());
        onView(withId(R.id.alert_list)).check(matches(isDisplayed()));
        onView(withId(R.id.button_add_alert)).check(matches(isDisplayed()));



         */
    }
}
