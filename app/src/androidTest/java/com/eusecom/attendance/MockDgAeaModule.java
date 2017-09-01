package com.eusecom.attendance;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.eusecom.attendance.dagger.modules.ApplicationModule;
import com.eusecom.attendance.mvvmdatamodel.DgAllEmpsAbsDataModel;
import com.eusecom.attendance.mvvmdatamodel.DgAllEmpsAbsIDataModel;
import com.eusecom.attendance.mvvmschedulers.ISchedulerProvider;
import com.eusecom.attendance.realm.RealmController;
import com.google.firebase.database.DatabaseReference;


import org.mockito.Mockito;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

@Module(includes = {ApplicationModule.class} )
public class MockDgAeaModule {

  @Provides
  @Singleton
  SharedPreferences providesSharedPreferences(Application application) {
    return PreferenceManager.getDefaultSharedPreferences(application);
  }

  @Provides
  @Singleton
  RealmController providesRealmConroller(Application application) {
    return new RealmController(application);
  }

  @Provides
  @Singleton
  Realm providesRealm(RealmController realmcontroller) {
    return realmcontroller.getRealm();
  }

  @Provides
  @Singleton
  public DatabaseReference providesDatabaseReference(Application application) {

    return ((AttendanceApplication) application).getDatabaseFirebaseReference();
  }

  @Provides
  @Singleton
  public DgAllEmpsAbsIDataModel providesDgAllEmpsAbsIDataModel(DatabaseReference databasereference, Realm realm) {
    return new DgAllEmpsAbsDataModel(databasereference, realm);
  }

  //the same result with ISchedulerProvider and with ImmediateSchedulerProvider when i mock DgAllEmpsAbsMvvmViewModel
  @Provides
  @Singleton
  public ISchedulerProvider providesISchedulerProvider(Application application) {

    return ((AttendanceApplication) application).getSchedulerProvider();
  }

  @Provides
  @Singleton
  public DgAllEmpsAbsMvvmViewModel providesDgAllEmpsAbsMvvmViewModel(DgAllEmpsAbsIDataModel dataModel,
                                                                     ISchedulerProvider schedulerProvider, SharedPreferences sharedPreferences) {
    return Mockito.mock(DgAllEmpsAbsMvvmViewModel.class);
  }

}