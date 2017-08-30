package com.sqisland.android.test_demo;

import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Component;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class DgActivityTest {
  @Inject
  Clock clock;

  @Singleton
  @Component(modules = MockClockModule.class)
  public interface TestComponent extends DemoComponent {
    void inject(DgActivityTest dgActivityTest);
  }

  @Rule
  public ActivityTestRule<DgAllEmpsAbsMvvmActivity> activityRule = new ActivityTestRule<>(
      DgAllEmpsAbsMvvmActivity.class,
      true,     // initialTouchMode
      false);   // launchActivity. False so we can customize the intent per test method

  @Before
  public void setUp() {
    Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
    DemoApplication app
        = (DemoApplication) instrumentation.getTargetContext().getApplicationContext();
    TestComponent component = (TestComponent) app.dgcomponent();
    component.inject(this);
  }

  @Test
  public void today() {
    //Mockito.when(clock.getNow()).thenReturn(new DateTime(2008, 9, 23, 0, 0, 0));

    activityRule.launchActivity(new Intent());

    IdlingResource idlingResource = startTiming(3000);

    //onView(withId(R.id.date)).check(matches(withText("2008-09-23T00:00:00.000+02:00")));

    stopTiming(idlingResource);


    //click on fab
    onView(allOf(withId(R.id.fab),isDisplayed()));
    onView(withId(R.id.fab)).perform(click());
    //onView(withId(R.id.date2)).check(matches(withText("2008-09-23T00:00:00.000+02:00")));


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