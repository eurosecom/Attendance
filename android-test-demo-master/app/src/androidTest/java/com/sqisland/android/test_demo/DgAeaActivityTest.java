package com.sqisland.android.test_demo;

import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.sqisland.android.test_demo.realm.RealmEmployee;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;
import dagger.Component;
import rx.Observable;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class DgAeaActivityTest {
  @Inject
  Clock clock;

  @Inject
  DgAllEmpsAbsMvvmViewModel mViewModel;

  @Singleton
  @Component(modules = MockDgAeaModule.class)
  public interface TestComponent extends DemoComponent {
    void inject(DgAeaActivityTest dgAeaActivityTest);
  }

  @Rule
  public ActivityTestRule<DgAeaActivity> activityRule = new ActivityTestRule<>(
      DgAeaActivity.class,
      true,     // initialTouchMode
      false);   // launchActivity. False so we can customize the intent per test method

  @Before
  public void setUp() {

    Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
    DemoApplication app
        = (DemoApplication) instrumentation.getTargetContext().getApplicationContext();
    TestComponent component = (TestComponent) app.dgaeacomponent();
    component.inject(this);

  }

  @Test
  public void today() {

    Mockito.when(clock.getNow()).thenReturn(new DateTime(2008, 9, 23, 0, 0, 0));
    Observable<List<RealmEmployee>> obsr = getMockObservableEmployee();
    Mockito.when(mViewModel.getObservableFBusersRealmEmployee()).thenReturn(obsr);

    activityRule.launchActivity(new Intent());

    IdlingResource idlingResource = startTiming(3000);

    //onView(withId(R.id.date)).check(matches(withText("2008-09-23T00:00:00.000+02:00")));

    stopTiming(idlingResource);


    //click on fab
    onView(allOf(withId(R.id.fab),isDisplayed()));
    onView(withId(R.id.fab)).perform(click());
    //onView(withId(R.id.date2)).check(matches(withText("2008-09-23T00:00:00.000+02:00")));

    IdlingResource idlingResource2 = startTiming(3000);
    onView(allOf(withId(R.id.fab),isDisplayed()));
    onView(withId(R.id.fab)).perform(click());
    stopTiming(idlingResource2);


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

  public Observable<List<RealmEmployee>> getMockObservableEmployee() {


    List<RealmEmployee> blogPostEntities = new ArrayList<>();

    RealmEmployee realmemployee = new RealmEmployee();
    realmemployee.setUsername("usernameTestApp");
    realmemployee.setEmail("emailTestApp");
    realmemployee.setUsico("usicoTestApp");
    realmemployee.setUstype("ustypeTestApp");
    realmemployee.setKeyf("uskeyTestApp");
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

    return Observable.just(blogPostEntities);

  }



}