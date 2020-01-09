package com.main.project1;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;


@RunWith(AndroidJUnit4.class)
public class EspressoTesting {

    @Rule
    public ActivityTestRule<LoginActivity> mLoginActivity =
            new ActivityTestRule<>(LoginActivity.class);


    @Test
    public void checkUserProfile() {
        onView(withId(R.id.textInputEditUsername))
                .perform(typeText("knownastron"), closeSoftKeyboard());

        onView(withId(R.id.buttonLogin)).perform(click());

        onView(withId(R.id.header_profile_image)).perform(click());

        // check first name
        onView(withId(R.id.textEditFirstName))
                .check(matches(withText("Jason")));

        // check last name
        onView(withId(R.id.textEditLastName))
                .check(matches(withText("Tran")));

        // check username
        onView(withId(R.id.textEditUsername))
                .check(matches(withText("knownastron")));

        // check city
        onView(withId(R.id.textEditCity))
                .check(matches(withText("Saskatoon")));

        // check state
        onView(withId(R.id.spinnerState))
                .check(matches(withSpinnerText("Saskatchewan")));

        // check country
        onView(withId(R.id.spinnerCountry))
                .check(matches(withSpinnerText("Canada")));
    }

    @Test
    public void checkFitnessProfile() {
        onView(withId(R.id.textInputEditUsername))
                .perform(typeText("jrodgers"), closeSoftKeyboard());

        onView(withId(R.id.buttonLogin)).perform(click());

        onView(withId(R.id.dashboard_rv))
                .perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));

        // check feet
        onView(withId(R.id.spinnerFeet))
                .check(matches(withSpinnerText("6 feet")));

        // check inches
        onView(withId(R.id.spinnerInches))
                .check(matches(withSpinnerText("2 inches")));
    }

    @Test
    public void checkWeather() {
        onView(withId(R.id.textInputEditUsername))
                .perform(typeText("adamq"), closeSoftKeyboard());

        onView(withId(R.id.buttonLogin)).perform(click());

        onView(withId(R.id.dashboard_rv))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        // check that correct city was pulled in
        onView(withId(R.id.current_weather_location_tv))
                .check(matches(not(withText("New Orleans, USA"))));

        // check temperature is not default
        onView(withId(R.id.current_temp))
                .check(matches(not(withText("@string/default_weather_text"))));
    }



}
