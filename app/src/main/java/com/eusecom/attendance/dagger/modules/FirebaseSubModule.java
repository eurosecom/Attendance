package com.eusecom.attendance.dagger.modules;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.eusecom.attendance.AllEmpsAbsMvvmActivity;
import com.eusecom.attendance.dagger.scopes.FirebaseScope;
import com.eusecom.attendance.realm.RealmController;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

@Module
public class FirebaseSubModule {
    private final AllEmpsAbsMvvmActivity activity;

    // must be instantiated with an activity
    public FirebaseSubModule(AllEmpsAbsMvvmActivity activity) { this.activity = activity; }


    @Provides
    // do not get @Singleton in subcomponent
    @FirebaseScope
    SharedPreferences providesSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    // do not get @Singleton in subcomponent
    @FirebaseScope
    RealmController providesRealmConroller(Application application) {
        return new RealmController(application);
    }

    @Provides
    // do not get @Singleton in subcomponent
    @FirebaseScope
    Realm providesRealm(RealmController realmcontroller) {
        return realmcontroller.getRealm();
    }
    
}
