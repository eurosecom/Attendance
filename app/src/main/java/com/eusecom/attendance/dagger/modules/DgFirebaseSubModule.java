package com.eusecom.attendance.dagger.modules;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.eusecom.attendance.DgAllEmpsAbsMvvmActivity;
import com.eusecom.attendance.dagger.scopes.DgFirebaseScope;
import com.eusecom.attendance.realm.RealmController;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

@Module
public class DgFirebaseSubModule {
    private final DgAllEmpsAbsMvvmActivity activity;

    //do not forget add to ApplicationBinders
    // must be instantiated with an activity
    public DgFirebaseSubModule(DgAllEmpsAbsMvvmActivity activity) { this.activity = activity; }


    @Provides
    // do not get @Singleton in subcomponent
    @DgFirebaseScope
    SharedPreferences providesSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    // do not get @Singleton in subcomponent
    @DgFirebaseScope
    RealmController providesRealmConroller(Application application) {
        return new RealmController(application);
    }

    @Provides
    // do not get @Singleton in subcomponent
    @DgFirebaseScope
    Realm providesRealm(RealmController realmcontroller) {
        return realmcontroller.getRealm();
    }
    
}
