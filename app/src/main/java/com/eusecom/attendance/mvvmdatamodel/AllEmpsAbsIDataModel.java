package com.eusecom.attendance.mvvmdatamodel;

import android.support.annotation.NonNull;

import com.eusecom.attendance.models.Company;
import com.eusecom.attendance.models.Employee;
import java.util.List;
import rx.Observable;

import static com.eusecom.attendance.mvvmmodel.Language.LanguageCode;

public interface AllEmpsAbsIDataModel {


    //recyclerview methods
    @NonNull
    Observable<List<Employee>> getObservableFBusersEmployee();




}
