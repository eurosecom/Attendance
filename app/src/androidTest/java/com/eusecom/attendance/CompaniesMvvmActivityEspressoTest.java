package com.eusecom.attendance;


import android.os.IBinder;
import android.support.test.espresso.Root;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import org.hamcrest.Description;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.internal.matchers.TypeSafeMatcher;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.v7.widget.StaggeredGridLayoutManager.TAG;
import static org.hamcrest.Matchers.allOf;

//        On your device, under Settings->Developer options disable the following 3 settings:
//        Window animation scale
//        Transition animation scale
//        Animator duration scale

public class CompaniesMvvmActivityEspressoTest {

    private String mStringToBetyped, mStringToBeChanged;

    @Rule
    public ActivityTestRule<CompaniesMvvmActivity> mActivityRule = new ActivityTestRule<>(CompaniesMvvmActivity.class);

    @Before
    public void init() {

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

        onView(isRoot()).perform(ViewActions.pressBack());

        onView(withId(R.id.companies_list)).perform(RecyclerViewActions.actionOnItemAtPosition(1, longClick()));


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

