package com.eusecom.attendance;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;
import rx.Observable;
import rx.subjects.BehaviorSubject;

import com.eusecom.attendance.models.Employee;
import com.eusecom.attendance.mvvmdatamodel.AllEmpsAbsIDataModel;
import com.eusecom.attendance.mvvmschedulers.ISchedulerProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

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
        mObservableSelectedLanguage.onNext(employees);
    }

    @NonNull
    private final BehaviorSubject<List<Employee>> mObservableSelectedLanguage = BehaviorSubject.create();

    @NonNull
    public Observable<String> getObservableGreeting() {
        return mObservableSelectedLanguage
                .observeOn(mSchedulerProvider.computation())
                .flatMap(mDataModel::getObservableGreetingByLanguageCode);
    }
    //end save employees to realm



}
