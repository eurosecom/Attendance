package com.eusecom.attendance.dagger.modules;

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
import com.google.firebase.database.DatabaseReference;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import javax.inject.Named;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class DgFirebaseSubModule {
    //private final DgAllEmpsAbsMvvmActivity activity;

    //do not forget add to ApplicationBinders
    //module does not must be instantiated with an activity
    //public DgFirebaseSubModule(DgAllEmpsAbsMvvmActivity activity) { this.activity = activity; }
    public DgFirebaseSubModule() {  }


    @Provides
    // do not get @Singleton in subcomponent
    @DgFirebaseScope
    SharedPreferences providesSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    // do not get @Singleton in subcomponent
    @DgFirebaseScope
    RealmController providesRealmConroller(Application application) {
        return new RealmController(application);
    }

    @Provides
    // do not get @Singleton in subcomponent
    @DgFirebaseScope
    Realm providesRealm(RealmController realmcontroller) {
        return realmcontroller.getRealm();
    }


    @Provides
    @DgFirebaseScope
    public DgAllEmpsAbsIDataModel providesDgAllEmpsAbsIDataModel(DatabaseReference databasereference, Realm realm) {
        return new DgAllEmpsAbsDataModel(databasereference, realm);
    }

    @Provides
    @DgFirebaseScope
    public ISchedulerProvider providesISchedulerProvider(Application application) {

        return ((AttendanceApplication) application).getSchedulerProvider();
    }

    @Provides
    @DgFirebaseScope
    public DatabaseReference providesDatabaseReference(Application application) {

        return ((AttendanceApplication) application).getDatabaseFirebaseReference();
    }

    //problem if i providesDgAllEmpsAbsMvvmViewModel do not get sharedpreferences in mvvmviemodel
    @Provides
    @DgFirebaseScope
    public DgAllEmpsAbsMvvmViewModel providesDgAllEmpsAbsMvvmViewModel(DgAllEmpsAbsIDataModel dataModel,
                                                                       ISchedulerProvider schedulerProvider, SharedPreferences sharedPreferences) {
        return new DgAllEmpsAbsMvvmViewModel(dataModel, schedulerProvider, sharedPreferences);
    }



    
}
