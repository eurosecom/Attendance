package com.eusecom.attendance;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;
import rx.Observable;
import rx.subjects.BehaviorSubject;

import com.eusecom.attendance.models.Company;
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

    //get list
    public Observable<List<Company>> getObservableFBcompanies() {
        return mDataModel.getObservableFBXcompanies();
    }

    //edit child
    @NonNull
    private final BehaviorSubject<Employee> mObservableEditedEmployee = BehaviorSubject.create();

    public void saveEditEmloyee(@NonNull final Employee employee, String namexx, String oscxx,
                                String icoxx, String typxx, String uswxx) {

        Log.d("mViewModel ", employee.getKeyf());

        Employee editEmployee = employee;
        editEmployee.setUsername(namexx);
        editEmployee.setUsosc(oscxx);
        editEmployee.setUsico(icoxx);
        editEmployee.setUstype(typxx);
        editEmployee.setUsatw(uswxx);
        mObservableEditedEmployee.onNext(editEmployee);
    }

    @NonNull
    public Observable<String> getObservableKeyEditedEmployee() {
        //return Observable.just("xxx");
        return mObservableEditedEmployee
                .observeOn(mSchedulerProvider.computation())
                .flatMap(employee -> { return mDataModel.getObservableKeyFBeditUser(employee); }
                );
    }

    //new company child
    @NonNull
    private final BehaviorSubject<Company> mObservableNewCompany = BehaviorSubject.create();

    public void saveNewCompany(@NonNull final Company newcompany) {

        Log.d("mViewModel new ", newcompany.getCmico());

        mObservableNewCompany.onNext(newcompany);
    }

    @NonNull
    public Observable<String> getObservableKeyNewCompany() {
        //return Observable.just("xxx");
        return mObservableNewCompany
                .observeOn(mSchedulerProvider.computation())
                .flatMap(newcompany -> { return mDataModel.getObservableKeyFBnewCompany(newcompany); }
                );
    }

}
