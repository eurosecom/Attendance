package com.eusecom.attendance.mvvmdatamodel;

import android.support.annotation.NonNull;

import com.eusecom.attendance.models.Company;
import com.eusecom.attendance.models.Employee;
import com.eusecom.attendance.mvvmmodel.Language;
import com.google.firebase.database.DataSnapshot;

import java.util.List;
import rx.Observable;

import static com.eusecom.attendance.mvvmmodel.Language.LanguageCode;

public interface CompaniesIDataModel {


    //recyclerview methods

    @NonNull
    Observable<List<Company>> getObservableFBXcompanies();

    @NonNull
    Observable<String> getObservableKeyFBnewCompany(@NonNull final Company company);

    @NonNull
    Observable<String> getObservableKeyFBeditUser(@NonNull final Employee employee);



}
