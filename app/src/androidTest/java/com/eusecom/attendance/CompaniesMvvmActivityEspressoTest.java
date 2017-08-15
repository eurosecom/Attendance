package com.eusecom.attendance;


import android.os.IBinder;
import android.support.test.espresso.Root;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.WindowManager;
import org.hamcrest.Description;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.internal.matchers.TypeSafeMatcher;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


//        On your device, under Settings->Developer options disable the following 3 settings:
//        Window animation scale
//        Transition animation scale
//        Animator duration scale

@RunWith(AndroidJUnit4.class)
public class CompaniesMvvmActivityEspressoTest {


    @Rule
    public ActivityTestRule<CompaniesMvvmActivity> mActivityRule = new ActivityTestRule<>(CompaniesMvvmActivity.class);

    @Before
    public void setUp() throws Exception {

        //get fragment
        mActivityRule.getActivity()
                .getSupportFragmentManager().beginTransaction();


    }

    @Test
    public void testFab() {

        //is visible ?
        onView(withId(R.id.fab)).check(matches(isDisplayed()));

        //display toast after clickon fab
        onView(withId(R.id.fab)).perform(click());

        //toast is displayed with text
        onView(withText(R.string.createnewcompany)).inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));

        onView(withText(R.string.createnewcompany)).inRoot(new ToastMatcher())
                .check(matches(withText("Create new company.")));


    }

    @Test
    public void testRecyclerView() {



        //click on item recyclerview
        onView(withId(R.id.companies_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.companies_list)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        onView(withId(R.id.companies_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, longClick()));

        //to verify if dialog is dispalyed To verify if dialog appears you can simply check if View with a text that present inside the dialog is shown:
        onView(withText("company Id 12345678")).check(matches(isDisplayed()));

        onView(isRoot()).perform(ViewActions.pressBack());

        onView(withId(R.id.companies_list)).perform(RecyclerViewActions.actionOnItemAtPosition(1, longClick()));

        //to verify if dialog is dispalyed To verify if dialog appears you can simply check if View with a text that present inside the dialog is shown:
        onView(withText("company Id 44551142")).check(matches(isDisplayed()));

        //To click on dialogs button do this (button1 - OK, button2 - Cancel):
        onView(withId(android.R.id.button1)).perform(click());


    }


    public class ToastMatcher extends TypeSafeMatcher<Root> {

        @Override
        public void describeTo(Description description) {
            description.appendText("is toast");
        }

        @Override
        public boolean matchesSafely(Root root) {
            int type = root.getWindowLayoutParams().get().type;
            if ((type == WindowManager.LayoutParams.TYPE_TOAST)) {
                IBinder windowToken = root.getDecorView().getWindowToken();
                IBinder appToken = root.getDecorView().getApplicationWindowToken();
                if (windowToken == appToken) {
                    return true;
                }
            }
            return false;
        }

    }



}

