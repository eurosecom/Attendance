package com.sqisland.android.test_demo;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import com.sqisland.android.test_demo.mvvmdatamodel.DgAllEmpsAbsIDataModel;
import com.sqisland.android.test_demo.mvvmschedulers.ISchedulerProvider;
import com.sqisland.android.test_demo.realm.RealmEmployee;
import java.util.List;
import rx.Observable;


/**
 * View model for the CompaniesMvvmActivity.
 */
public class DgAllEmpsAbsMvvmViewModel {

    //@Inject only by Base constructor injection, then i have got all provided dependencies in module DgFirebaseSubModule
    // injected in class DgAllEmpsAbsListFragment where i inject DgAllEmpsAbsMvvmViewModel
    // If i provide dependency DgAllEmpsAbsMvvmViewModel in DgFirebaseSubModule then i have got in DgAllEmpsAbsMvvmViewMode only dependencies in constructor
    DgAllEmpsAbsIDataModel mDataModel;

    //@Inject only by Base constructor injection
    ISchedulerProvider mSchedulerProvider;

    //@Inject only by Base constructor injection
    SharedPreferences mSharedPreferences;

    //@Inject only by Base constructor injection
    public DgAllEmpsAbsMvvmViewModel(@NonNull final DgAllEmpsAbsIDataModel dataModel,
                         @NonNull final ISchedulerProvider schedulerProvider, @NonNull final SharedPreferences sharedPreferences) {
        mDataModel = dataModel;
        mSchedulerProvider = schedulerProvider;
        mSharedPreferences = sharedPreferences;
    }

    //recyclerview method



    //get realmemployees list from FB
    public Observable<List<RealmEmployee>> getObservableFBusersRealmEmployee() {

        String usicox = mSharedPreferences.getString("usico", "");
        //String usicox = "44551142";
        //Log.d("MvvmViewModel ", usicox);
        return mDataModel.getObservableFBusersRealmEmployee(usicox);
    }
    //end get realmemployees list from FB




}
