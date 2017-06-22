package com.eusecom.attendance.dagger;

import android.content.Context;

import com.eusecom.attendance.AttendanceApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Valentine on 4/20/2016.
 */
@Module
public class AppModule {
    private final AttendanceApplication app;

    public AppModule(AttendanceApplication app) {
        this.app = app;
    }


    @Provides @Singleton
    public Context provideContext() {
        return app;
    }
}