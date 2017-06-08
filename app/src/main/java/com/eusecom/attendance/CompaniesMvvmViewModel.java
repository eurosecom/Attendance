package com.eusecom.attendance;

import android.support.annotation.NonNull;
import java.util.List;
import rx.Observable;
import rx.subjects.BehaviorSubject;
import com.eusecom.attendance.models.Employee;
import com.eusecom.attendance.mvvmdatamodel.CompaniesIDataModel;
import com.eusecom.attendance.mvvmschedulers.ISchedulerProvider;

/**
 * View model for the CompaniesMvvmActivity.
 */
public class CompaniesMvvmViewModel {

    @NonNull
    private final CompaniesIDataModel mDataModel;
    @NonNull
    private final ISchedulerProvider mSchedulerProvider;


    public CompaniesMvvmViewModel(@NonNull final CompaniesIDataModel dataModel,
                         @NonNull final ISchedulerProvider schedulerProvider) {
        mDataModel = dataModel;
        mSchedulerProvider = schedulerProvider;
    }

    //recyclerview method

    public Observable<List<Employee>> getObservableFBcompanies() {
        return mDataModel.getObservableFBXcompanies();
    }



}
