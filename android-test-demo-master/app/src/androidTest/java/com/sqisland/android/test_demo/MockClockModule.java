package com.sqisland.android.test_demo;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.mockito.Mockito;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = {ApplicationModule.class} )
public class MockClockModule {
  @Provides
  @Singleton
  Clock provideClock() {
    return Mockito.mock(Clock.class);
  }

  @Provides
  @Singleton
  SharedPreferences providesSharedPreferences(Application application) {
    return PreferenceManager.getDefaultSharedPreferences(application);
  }

}