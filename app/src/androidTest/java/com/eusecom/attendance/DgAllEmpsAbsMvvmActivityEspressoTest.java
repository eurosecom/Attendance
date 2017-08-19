package com.eusecom.attendance;


import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.Root;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.eusecom.attendance.models.Employee;
import com.eusecom.attendance.realm.RealmCompany;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.mockito.Mockito.when;


//        On your device, under Settings->Developer options disable the following 3 settings:
//        Window animation scale
//        Transition animation scale
//        Animator duration scale

@RunWith(AndroidJUnit4.class)
public class DgAllEmpsAbsMvvmActivityEspressoTest {


    @Rule
    public ActivityTestRule<DgAllEmpsAbsMvvmActivity> mActivityRule = new ActivityTestRule<>(DgAllEmpsAbsMvvmActivity.class);

    @Inject
    DgAllEmpsAbsMvvmViewModel mViewModel;

    @Before
    public void setUp() throws Exception {

        //get fragment
        mActivityRule.getActivity()
                .getSupportFragmentManager().beginTransaction();

        //((AttendanceApplication) mActivityRule.getActivity().getApplication()).getAllEmpsAbsComponent().inject(this);

        getApp().getDgAllEmpsAbsComponent().inject(mActivityRule.getActivity());


    }

    private AttendanceApplication getApp() {
        return (AttendanceApplication) InstrumentationRegistry.getInstrumentation()
                .getTargetContext().getApplicationContext();
    }

    @Test
    public void testFab() {

        //is visible ?
        onView(withId(R.id.fab)).check(matches(isDisplayed()));

        //open activity



    }

    @Test
    public void testRecyclerView() {

        List<RealmCompany> blogPostEntities = new ArrayList<>();

        RealmCompany realmemployee = new RealmCompany();
        realmemployee.setUsername("Username");
        realmemployee.setEmail("Useremail");
        realmemployee.setUsico("Userico");
        realmemployee.setUstype("Usertype");
        realmemployee.setKeyf("Userkeyf");
        realmemployee.setDay01("0");
        realmemployee.setDay02("0");
        realmemployee.setDay03("0");
        realmemployee.setDay04("0");
        realmemployee.setDay05("0");
        realmemployee.setDay06("0");
        realmemployee.setDay07("0");
        realmemployee.setDay08("0");
        realmemployee.setDay09("0");
        realmemployee.setDay10("0");

        realmemployee.setDay11("0");
        realmemployee.setDay12("0");
        realmemployee.setDay13("0");
        realmemployee.setDay14("0");
        realmemployee.setDay15("0");
        realmemployee.setDay16("0");
        realmemployee.setDay17("0");
        realmemployee.setDay18("0");
        realmemployee.setDay19("0");
        realmemployee.setDay20("0");

        realmemployee.setDay21("0");
        realmemployee.setDay22("0");
        realmemployee.setDay23("0");
        realmemployee.setDay24("0");
        realmemployee.setDay25("0");
        realmemployee.setDay26("0");
        realmemployee.setDay27("0");
        realmemployee.setDay28("0");
        realmemployee.setDay29("0");
        realmemployee.setDay30("0");

        realmemployee.setDay31("0");


        blogPostEntities.add(realmemployee);

        //when(mViewModel.getObservableFBcompanyRealmEmployee()).thenReturn(Observable.just(blogPostEntities));
        //mActivityRule.launchActivity(null);

        //click on item recyclerview
        IdlingResource idlingResource = startTiming(3000);
        onView(withId(R.id.allempsabs_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        stopTiming(idlingResource);

        onView(isRoot()).perform(ViewActions.pressBack());

        idlingResource = startTiming(3000);
        onView(withId(R.id.allempsabs_list)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        stopTiming(idlingResource);

        idlingResource = startTiming(1000);
        onView(isRoot()).perform(ViewActions.pressBack());


    }



    public IdlingResource startTiming(long time) {
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(time);
        Espresso.registerIdlingResources(idlingResource);
        return idlingResource;
    }
    public void stopTiming(IdlingResource idlingResource) {
        Espresso.unregisterIdlingResources(idlingResource);
    }
    public class ElapsedTimeIdlingResource implements IdlingResource {
        private long startTime;
        private final long waitingTime;
        private ResourceCallback resourceCallback;

        public ElapsedTimeIdlingResource(long waitingTime) {
            this.startTime = System.currentTimeMillis();
            this.waitingTime = waitingTime;
        }

        @Override
        public String getName() {
            return ElapsedTimeIdlingResource.class.getName() + ":" + waitingTime;
        }

        @Override
        public boolean isIdleNow() {
            long elapsed = System.currentTimeMillis() - startTime;
            boolean idle = (elapsed >= waitingTime);
            if (idle) {
                resourceCallback.onTransitionToIdle();
            }
            return idle;
        }

        @Override
        public void registerIdleTransitionCallback(IdlingResource.ResourceCallback resourceCallback) {
            this.resourceCallback = resourceCallback;
        }
    }



}

