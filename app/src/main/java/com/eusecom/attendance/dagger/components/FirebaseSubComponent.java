package com.eusecom.attendance.dagger.components;

import android.content.SharedPreferences;
import com.eusecom.attendance.AllEmpsAbsMvvmActivity;
import com.eusecom.attendance.dagger.modules.FirebaseSubModule;
import com.eusecom.attendance.dagger.scopes.FirebaseScope;
import com.eusecom.attendance.realm.RealmController;

import dagger.Subcomponent;
import io.realm.Realm;

@FirebaseScope
@Subcomponent(modules={ FirebaseSubModule.class })
public interface FirebaseSubComponent {

    //do not forget add to ApplicationBinders
    void inject(AllEmpsAbsMvvmActivity activity);

    SharedPreferences sharedPreferences();
    RealmController realmController();
    Realm realm();

    @Subcomponent.Builder
    interface Builder extends SubcomponentBuilder<FirebaseSubComponent> {
        Builder activityModule(FirebaseSubModule module);
    }

}
