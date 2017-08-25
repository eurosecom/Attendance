package com.sqisland.android.test_demo;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Component;

public class DemoApplication extends Application {


  private final DemoComponent component = createComponent();

  protected DemoComponent createComponent() {
    return DaggerDemoComponent.builder()
            .applicationModule(new ApplicationModule(this))
            .build();
  }

  public DemoComponent component() {
    return component;
  }
}