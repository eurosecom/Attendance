package com.eusecom.attendance;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import io.realm.Realm;
import rx.Observable;
import rx.subjects.BehaviorSubject;

import com.eusecom.attendance.models.Employee;
import com.eusecom.attendance.mvvmdatamodel.AllEmpsAbsIDataModel;
import com.eusecom.attendance.mvvmschedulers.ISchedulerProvider;
import com.eusecom.attendance.realm.RealmController;

import javax.inject.Inject;

/**
 * View model for the CompaniesMvvmActivity.
 */
public class AllEmpsAbsMvvmViewModel {

    @NonNull
    private final AllEmpsAbsIDataModel mDataModel;
    @NonNull
    private final ISchedulerProvider mSchedulerProvider;

    @Inject
    SharedPreferences mSharedPreferences;

    @Inject
    Realm realm;
    @Inject
    RealmController realmcontroller;


    @Inject
    public AllEmpsAbsMvvmViewModel(@NonNull final AllEmpsAbsIDataModel dataModel,
                         @NonNull final ISchedulerProvider schedulerProvider) {
        mDataModel = dataModel;
        mSchedulerProvider = schedulerProvider;
    }

    //recyclerview method

    //get employees list from FB
    public Observable<List<Employee>> getObservableFBusersEmployee() {

        String usicox = mSharedPreferences.getString("usico", "");
        Log.d("MvvmViewModel ", usicox);
        return mDataModel.getObservableFBusersEmployee(usicox);
    }
    //end get employees list from FB

    //save employees to realm
    public void emitEmployeesToRealm(List<Employee> employees) {
        mObservableSaveToRealm.onNext(employees);
    }

    @NonNull
    private final BehaviorSubject<List<Employee>> mObservableSaveToRealm = BehaviorSubject.create();

    @NonNull
    public Observable<String> getObservableDataSavedToRealm() {
        return mObservableSaveToRealm
                .observeOn(mSchedulerProvider.ui())
                .flatMap(list -> mDataModel.getObservableSavingToRealm(list, realm));
    }
    //end save employees to realm



}
