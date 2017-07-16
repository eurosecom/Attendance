package com.eusecom.attendance.dagger.modules;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.ArrayAdapter;
import com.eusecom.attendance.DemoDaggerSubActivity;
import com.eusecom.attendance.dagger.scopes.MyActivityScope;
import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MyActivityModule {
    private final DemoDaggerSubActivity activity;

    // must be instantiated with an activity
    public MyActivityModule(DemoDaggerSubActivity activity) { this.activity = activity; }

    @Provides
    // do not get @Singleton in subcomponent
    @MyActivityScope
    SharedPreferences providesSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }
   
    @Provides
    @MyActivityScope
    @Named("my_list")
    public ArrayAdapter providesMyListAdapter() {
        return new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1);
    }
    
}
