package com.eusecom.attendance.dagger.modules;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.ArrayAdapter;
import com.eusecom.attendance.AllEmpsAbsMvvmActivity;
import com.eusecom.attendance.dagger.scopes.FirebaseScope;
import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class FirebaseModule {
    private final AllEmpsAbsMvvmActivity activity;

    // must be instantiated with an activity
    public FirebaseModule(AllEmpsAbsMvvmActivity activity) { this.activity = activity; }

    @Provides
    // do not get @Singleton in subcomponent
    @FirebaseScope
    SharedPreferences providesSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }
   

    
}
