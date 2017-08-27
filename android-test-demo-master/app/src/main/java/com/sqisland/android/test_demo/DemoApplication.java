package com.sqisland.android.test_demo;

import android.app.Application;
import android.support.annotation.NonNull;

import com.sqisland.android.test_demo.mvvmschedulers.ISchedulerProvider;
import com.sqisland.android.test_demo.mvvmschedulers.SchedulerProvider;

import javax.inject.Singleton;

import dagger.Component;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class DemoApplication extends Application {


  private final DemoComponent component = createComponent();
  private Realm mRealm;

  @Override public void onCreate() {
    super.onCreate();

    RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
            .name(Realm.DEFAULT_REALM_NAME)
            .schemaVersion(0)
            .deleteRealmIfMigrationNeeded()
            .build();
    Realm.setDefaultConfiguration(realmConfiguration);

  }

  protected DemoComponent createComponent() {
    return DaggerDemoComponent.builder()
            .applicationModule(new ApplicationModule(this))
            .build();
  }

  public DemoComponent component() {
    return component;
  }

  @NonNull
  public Realm getRealm() {
    return Realm.getDefaultInstance();
  }

  @NonNull
  public ISchedulerProvider getSchedulerProvider() {
    return SchedulerProvider.getInstance();
  }


}