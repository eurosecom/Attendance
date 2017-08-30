package com.sqisland.android.test_demo;

public class DgMockDemoApplication extends DemoApplication {
  @Override
  protected DemoComponent createComponent() {
    return DaggerDgActivityTest_DgTestComponent.builder()
            .applicationModule(new ApplicationModule(this))
            .build();
  }
}