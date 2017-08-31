package com.eusecom.attendance.dagger.components;

import android.content.SharedPreferences;

import com.eusecom.attendance.DgAllEmpsAbsListFragment;
import com.eusecom.attendance.DgAllEmpsAbsMvvmActivity;
import com.eusecom.attendance.DgAllEmpsAbsMvvmViewModel;
import com.eusecom.attendance.DgAllEmpsCompAbsListFragment;
import com.eusecom.attendance.dagger.modules.DgFirebaseSubModule;
import com.eusecom.attendance.dagger.scopes.DgFirebaseScope;
import com.eusecom.attendance.realm.RealmController;

import dagger.Subcomponent;
import io.realm.Realm;

@DgFirebaseScope
@Subcomponent(modules={ DgFirebaseSubModule.class })
public interface DgFirebaseSubComponent {

    //DgAllEmpsAbsMvvmViewModel dgAllEmpsAbsMvvmViewModel();

    //do not forget add to ApplicationBinders
    void inject(DgAllEmpsAbsMvvmActivity activity);
    void inject(DgAllEmpsAbsListFragment fragment);
    void inject(DgAllEmpsCompAbsListFragment cfragment);

    //do not list provided dependencies in subcomponent, only in dependent component
    //SharedPreferences sharedPreferences();
    //RealmController realmController();
    //Realm realm();

    @Subcomponent.Builder
    interface Builder extends SubcomponentBuilder<DgFirebaseSubComponent> {
        Builder activityModule(DgFirebaseSubModule module);
    }

}
