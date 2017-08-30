package com.sqisland.android.test_demo;

public class MockDemoApplication extends DemoApplication {
  @Override
  protected DemoComponent createComponent() {
    return DaggerMainActivityTest_TestComponent.builder()
            .applicationModule(new ApplicationModule(this))
            .build();
  }

  @Override
  protected DemoComponent createDgComponent() {
    return DaggerDgActivityTest_TestComponent.builder()
            .applicationModule(new ApplicationModule(this))
            .build();
  }

}