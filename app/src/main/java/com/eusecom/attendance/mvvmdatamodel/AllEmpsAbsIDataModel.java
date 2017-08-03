package com.eusecom.attendance.mvvmdatamodel;

import android.support.annotation.NonNull;
import com.eusecom.attendance.models.Employee;
import java.util.List;
import io.realm.Realm;
import rx.Observable;

public interface AllEmpsAbsIDataModel {

    //recyclerview methods
    @NonNull
    Observable<List<Employee>> getObservableFBusersEmployee(String usicox);

    @NonNull
    Observable<String> getObservableSavingToRealm(@NonNull final List<Employee> employees, Realm realm);

    @NonNull
    Observable<String> getObservableAbsenceForRealm(@NonNull final String umex, Realm realm);




}
