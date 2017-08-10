package com.eusecom.attendance.mvvmdatamodel;

import android.support.annotation.NonNull;

import com.eusecom.attendance.models.Attendance;
import com.eusecom.attendance.models.Employee;
import com.eusecom.attendance.realm.RealmCompany;
import com.eusecom.attendance.realm.RealmEmployee;

import java.util.List;
import io.realm.Realm;
import rx.Observable;

public interface AllEmpsAbsIDataModel {

    //recyclerview methods
    @NonNull
    Observable<List<Employee>> getObservableFBusersEmployee(String usicox);

    @NonNull
    Observable<List<RealmEmployee>> getObservableFBusersRealmEmployee(String usicox);

    @NonNull
    Observable<String> getObservableSavingToRealm(@NonNull final List<RealmEmployee> employees);

    @NonNull
    Observable<List<Attendance>> getObservableAbsencesFromFB(@NonNull final String umex, @NonNull final String usicox);

    @NonNull
    Observable<String> getObservableUpdatedRealm(@NonNull final List<Attendance> absences);

    @NonNull
    Observable<List<RealmEmployee>> getObservableUpdatedListRealm(@NonNull final List<Attendance> absences);

    @NonNull
    Observable<List<RealmCompany>> getObservableFBmycompanyRealmEmployee(String usicox);

    @NonNull
    Observable<String> getObservableCompanySavingToRealm(@NonNull final List<RealmCompany> employees);

    @NonNull
    Observable<List<RealmCompany>> getObservableUpdatedListCompanyRealm(@NonNull final List<Attendance> absences);

}
