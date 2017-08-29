package com.sqisland.android.test_demo;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.sqisland.android.test_demo.mvvmdatamodel.DgAllEmpsAbsDataModel;
import com.sqisland.android.test_demo.mvvmdatamodel.DgAllEmpsAbsIDataModel;
import com.sqisland.android.test_demo.mvvmschedulers.ISchedulerProvider;
import com.sqisland.android.test_demo.realm.RealmController;

import org.mockito.Mockito;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

@Module(includes = {ApplicationModule.class} )
public class MockClockModule {
  @Provides
  @Singleton
  Clock provideClock() {
    return Mockito.mock(Clock.class);
  }

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
  public DgAllEmpsAbsIDataModel providesDgAllEmpsAbsIDataModel(Realm realm) {
    return new DgAllEmpsAbsDataModel(realm);
  }

  @Provides
  @Singleton
  public ISchedulerProvider providesISchedulerProvider(Application application) {

    return ((DemoApplication) application).getSchedulerProvider();
  }

  @Provides
  @Singleton
  public DgAllEmpsAbsMvvmViewModel providesDgAllEmpsAbsMvvmViewModel(DgAllEmpsAbsIDataModel dataModel,
                                                                     ISchedulerProvider schedulerProvider, SharedPreferences sharedPreferences) {
    return new DgAllEmpsAbsMvvmViewModel(dataModel, schedulerProvider, sharedPreferences);
  }

}