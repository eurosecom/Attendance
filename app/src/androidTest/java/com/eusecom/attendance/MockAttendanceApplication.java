package com.eusecom.attendance;

import com.eusecom.attendance.dagger.components.DemoComponent;
import com.eusecom.attendance.dagger.modules.ApplicationModule;

public class MockAttendanceApplication extends AttendanceApplication {


  @Override
  protected DemoComponent createDgAeaComponent() {
    return DaggerDgAeaActivityTest_TestComponent.builder()
            .applicationModule(new ApplicationModule(this))
            .build();
  }

}