package com.eusecom.attendance;


import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

//        On your device, under Settings->Developer options disable the following 3 settings:
//        Window animation scale
//        Transition animation scale
//        Animator duration scale

public class EmployeeMvvmActivityEspressoTest {

    private String mStringToBetyped, mStringToBeChanged;

    @Rule
    public ActivityTestRule<EmployeeMvvmActivity> mActivityRule = new ActivityTestRule<>(EmployeeMvvmActivity.class);

    @Before
    public void initValidString() {
        // Specify a valid string.
        mStringToBetyped = "Espresso";
        mStringToBeChanged = "Espresso new";
    }

    @Test
    public void changeText_sameActivity() {
        // Type text and then press the button.
        onView(withId(R.id.field_title)).perform(typeText(mStringToBetyped));
        onView(withId(R.id.fab)).check(matches(isDisplayed()));

        //if softkeyboard cover fab i cannot click
        onView(allOf(withId(R.id.fab),isDisplayed()));
        onView(withId(R.id.fab)).perform(pressBack());
        onView(allOf(withId(R.id.fab),isDisplayed()));


        onView(withId(R.id.fab)).perform(click());


        // Check that the text was changed.
        onView(withId(R.id.field_title)).check(matches(withText(mStringToBetyped)));
    }




}

