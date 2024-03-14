package com.example.the_tarlords;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ProfileUITest {

    @Test
    public void profileFragmentNavigation(){
        ActivityScenario.launch(MainActivity.class);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.eventListView)).check(matches(isDisplayed()));
        onView(withId(R.id.drawer_layout)).check(matches(isDisplayed()));
        onView(ViewMatchers.withContentDescription("Open navigation drawer")).perform(ViewActions.click());
        //onView(withId(R.id.drawer_layout)).perform(click());
        onView(withId(R.id.profileFragment)).check(matches(isDisplayed()));
        onView(withId(R.id.profileFragment)).perform(click());
        onView(withId(R.id.edit_text_first_name)).check(matches(isDisplayed()));
        onView(withId(R.id.edit_text_phone)).check(matches(isDisplayed()));
        onView(withId(R.id.edit_text_email)).check(matches(isDisplayed()));

        onView(withId(R.id.editOptionsMenu)).check(matches(isDisplayed()));
        onView(withId(R.id.editOptionsMenu)).perform(click());
        onView(withId(R.id.edit_text_first_name)).check(matches(isDisplayed()));
        onView(withId(R.id.edit_text_phone)).check(matches(isDisplayed()));
        onView(withId(R.id.edit_text_email)).check(matches(isDisplayed()));
        onView(withId(R.id.cancelOptionsMenu)).check(matches(isDisplayed()));
        onView(withId(R.id.saveOptionsMenu)).check(matches(isDisplayed()));



    }
}
