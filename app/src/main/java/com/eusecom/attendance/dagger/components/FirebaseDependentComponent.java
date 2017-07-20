package com.eusecom.attendance.dagger.components;


import com.eusecom.attendance.dagger.modules.ApplicationModule;
import com.eusecom.attendance.dagger.modules.FirebaseDependentModule;
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

}