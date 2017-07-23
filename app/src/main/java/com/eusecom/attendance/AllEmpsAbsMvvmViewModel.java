package com.eusecom.attendance;

import android.support.annotation.NonNull;
import android.util.Log;
import java.util.List;
import rx.Observable;
import rx.subjects.BehaviorSubject;
import com.eusecom.attendance.models.Company;
import com.eusecom.attendance.models.Employee;
import com.eusecom.attendance.mvvmdatamodel.AllEmpsAbsIDataModel;
import com.eusecom.attendance.mvvmschedulers.ISchedulerProvider;

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
    public AllEmpsAbsMvvmViewModel(@NonNull final AllEmpsAbsIDataModel dataModel,
                         @NonNull final ISchedulerProvider schedulerProvider) {
        mDataModel = dataModel;
        mSchedulerProvider = schedulerProvider;
    }

    //recyclerview method

    //get employees list
    public Observable<List<Employee>> getObservableFBusersEmployee() {
        return mDataModel.getObservableFBusersEmployee();
    }




}
