package com.eusecom.attendance;

import com.eusecom.attendance.dagger.components.DgFirebaseSubComponent;

public class MockAttendanceApplication extends AttendanceApplication {

  @Override
  public DgFirebaseSubComponent createDgFirebaseSubComponent() {

  return DaggerDgOver2ModulAllEmpsAbsMvvmActivityEspressoTest_TestDgFirebaseSubComponent.builder().build();


  }


}