package com.eusecom.attendance;


import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

import org.robolectric.TestLifecycleApplication;

import java.lang.reflect.Method;

//if exist TestAplicationname.java roboelectric work first for firebase init

public class TestAttendanceApplication extends AttendanceApplication implements TestLifecycleApplication {


    //solve problem with multidex support and roboelectric
        @Override protected void attachBaseContext(Context base) {
            try {
                super.attachBaseContext(base);
            } catch (RuntimeException ignored) {
                // Multidex support doesn't play well with Robolectric yet
                FirebaseApp.initializeApp(this);
            }
        }


    @Override public void beforeTest(Method method) {

        FirebaseApp.initializeApp(this);

    }


    @Override public void prepareTest(Object test) {
    }

    @Override public void afterTest(Method method) {
    }
}
