package com.sqisland.android.test_demo;


import android.app.Application;
import android.content.SharedPreferences;

import java.util.Map;
import javax.inject.Provider;
import javax.inject.Singleton;
import dagger.Component;

/**
 * Created by Valentine on 4/20/2016.
 */
@Singleton
@Component( modules = ApplicationModule.class )
public interface ApplicationComponent {


}
