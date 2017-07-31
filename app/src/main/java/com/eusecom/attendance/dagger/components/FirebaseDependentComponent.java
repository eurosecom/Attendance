package com.eusecom.attendance.dagger.components;


import com.eusecom.attendance.dagger.modules.ApplicationModule;
import com.eusecom.attendance.dagger.modules.FirebaseDependentModule;
import com.eusecom.attendance.mvvmdatamodel.AllEmpsAbsIDataModel;
import com.eusecom.attendance.mvvmschedulers.ISchedulerProvider;
import com.eusecom.attendance.realm.RealmController;
import com.google.firebase.database.DatabaseReference;

import android.content.SharedPreferences;

import javax.inject.Named;
import javax.inject.Singleton;
import dagger.Component;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;


@Singleton
@Component(modules={ApplicationModule.class, FirebaseDependentModule.class})
public interface FirebaseDependentComponent {

    // downstream components need these exposed
    Retrofit retrofit();
    @Named("cached")
    OkHttpClient okHttpClient();
    @Named("non_cached") OkHttpClient okHttpClientNonCached();
    SharedPreferences sharedPreferences();
    RealmController realmController();

    AllEmpsAbsIDataModel allEmpsAbsIDataModell();
    ISchedulerProvider iSchedulerProvider();
    DatabaseReference databaseReference();

}