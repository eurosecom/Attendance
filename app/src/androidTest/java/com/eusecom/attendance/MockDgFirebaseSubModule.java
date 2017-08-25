package com.eusecom.attendance;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.eusecom.attendance.AttendanceApplication;
import com.eusecom.attendance.DgAllEmpsAbsListFragment;
import com.eusecom.attendance.DgAllEmpsAbsMvvmActivity;
import com.eusecom.attendance.DgAllEmpsAbsMvvmViewModel;
import com.eusecom.attendance.dagger.scopes.DgFirebaseScope;
import com.eusecom.attendance.mvvmdatamodel.AllEmpsAbsIDataModel;
import com.eusecom.attendance.mvvmdatamodel.DgAllEmpsAbsDataModel;
import com.eusecom.attendance.mvvmdatamodel.DgAllEmpsAbsIDataModel;
import com.eusecom.attendance.mvvmschedulers.ISchedulerProvider;
import com.eusecom.attendance.realm.RealmController;
import com.eusecom.attendance.realm.RealmEmployee;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import org.mockito.Mockito;

import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

@Module
public class MockDgFirebaseSubModule {

    public MockDgFirebaseSubModule() {  }


    //problem if i providesDgAllEmpsAbsMvvmViewModel do not get sharedpreferences in mvvmviemodel
    @Provides
    @DgFirebaseScope
    public DgAllEmpsAbsMvvmViewModel providesDgAllEmpsAbsMvvmViewModel(DgAllEmpsAbsIDataModel dataModel,
                                                                       ISchedulerProvider schedulerProvider, SharedPreferences sharedPreferences) {
        return Mockito.mock(DgAllEmpsAbsMvvmViewModel.class);
    }





    
}
