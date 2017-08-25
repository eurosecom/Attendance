package com.sqisland.android.test_demo;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules={ApplicationModule.class, ClockModule.class})
public interface DemoComponent {
  void inject(MainActivity mainActivity);
}