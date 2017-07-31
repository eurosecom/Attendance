package com.eusecom.attendance.mvvmdatamodel;

import android.support.annotation.NonNull;
import com.eusecom.attendance.models.Employee;
import com.eusecom.attendance.mvvmmodel.Language;

import java.util.List;
import rx.Observable;

public interface AllEmpsAbsIDataModel {

    //recyclerview methods
    @NonNull
    Observable<List<Employee>> getObservableFBusersEmployee(String usicox);

    @NonNull
    Observable<String> getObservableGreetingByLanguageCode(@NonNull final List<Employee> employees);




}
