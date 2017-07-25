package com.eusecom.attendance.mvvmdatamodel;

import android.support.annotation.NonNull;
import com.eusecom.attendance.models.Employee;
import java.util.List;
import rx.Observable;

public interface AllEmpsAbsIDataModel {

    //recyclerview methods
    @NonNull
    Observable<List<Employee>> getObservableFBusersEmployee(String usicox);




}
