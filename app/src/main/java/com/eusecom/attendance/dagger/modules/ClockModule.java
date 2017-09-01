package com.eusecom.attendance.dagger.modules;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.eusecom.attendance.AttendanceApplication;
import com.eusecom.attendance.DgAllEmpsAbsMvvmViewModel;
import com.eusecom.attendance.dagger.scopes.DgFirebaseScope;
import com.eusecom.attendance.mvvmdatamodel.DgAllEmpsAbsDataModel;
import com.eusecom.attendance.mvvmdatamodel.DgAllEmpsAbsIDataModel;
import com.eusecom.attendance.mvvmschedulers.ISchedulerProvider;
import com.eusecom.attendance.realm.RealmController;
import com.google.firebase.database.DatabaseReference;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

@Module(includes = {ApplicationModule.class} )
public class ClockModule {


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



  @Provides
  @Singleton
  public ISchedulerProvider providesISchedulerProvider(Application application) {

    return ((AttendanceApplication) application).getSchedulerProvider();
  }

  @Provides
  @Singleton
  public DgAllEmpsAbsMvvmViewModel providesDgAllEmpsAbsMvvmViewModel(DgAllEmpsAbsIDataModel dataModel,
                                                                     ISchedulerProvider schedulerProvider, SharedPreferences sharedPreferences) {
    return new DgAllEmpsAbsMvvmViewModel(dataModel, schedulerProvider, sharedPreferences);
  }

}