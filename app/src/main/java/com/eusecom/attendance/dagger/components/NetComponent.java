package com.eusecom.attendance.dagger.components;


import com.eusecom.attendance.dagger.modules.ApplicationModule;
import com.eusecom.attendance.dagger.modules.NetModule;
import okhttp3.OkHttpClient;
import android.content.SharedPreferences;
import javax.inject.Singleton;
import dagger.Component;
import retrofit2.Retrofit;


@Singleton
@Component(modules={ApplicationModule.class, NetModule.class})
public interface NetComponent {
    // downstream components need these exposed
    Retrofit retrofit();
    OkHttpClient okHttpClient();
    SharedPreferences sharedPreferences();
}