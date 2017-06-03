package com.eusecom.attendance;


import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

import org.robolectric.TestLifecycleApplication;

import java.lang.reflect.Method;

//if exist TestAplicationname.java roboelectric work first for firebase init

public class TestAttendanceApplication extends AttendanceApplication implements TestLifecycleApplication {
    @Override public void beforeTest(Method method) {

        FirebaseApp.initializeApp(this);

    }


    @Override public void prepareTest(Object test) {
    }

    @Override public void afterTest(Method method) {
    }
}
