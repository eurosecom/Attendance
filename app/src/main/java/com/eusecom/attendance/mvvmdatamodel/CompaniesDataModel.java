package com.eusecom.attendance.mvvmdatamodel;

import android.support.annotation.NonNull;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rx.Observable;

import com.eusecom.attendance.models.Company;
import com.eusecom.attendance.models.Employee;
import com.eusecom.attendance.mvvmmodel.Language;
import com.eusecom.attendance.rxfirebase2.database.RxFirebaseDatabase;
import com.eusecom.attendance.rxfirebase2models.BlogPostEntity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import static com.eusecom.attendance.mvvmmodel.Language.LanguageCode;

public class CompaniesDataModel implements CompaniesIDataModel {

    //recyclerview datamodel

    //new company child
    @NonNull
    @Override
    public Observable<String> getObservableKeyFBnewCompany(@NonNull final Company company) {

        String icos = company.getCmico();
        Log.d("mDataModel ico ", icos);
        String observedstring = "" + company.getCmname();
        final DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference().child("companies").child(icos);

        Map<String, Object> attValues = company.toMap();

        return RxFirebaseDatabase.getInstance().observeEditValue(firebaseRef,attValues,0 );

    }

    //edit child users
    @NonNull
    @Override
    public Observable<String> getObservableKeyFBeditUser(@NonNull final Employee employee) {

        String keys = employee.getKeyf();
        Log.d("mDataModel ", keys);
        String observedstring = "" + employee.getEmail();
        final DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference().child("users").child(keys);

        Employee editemp = employee;
        Map<String, Object> attValues = editemp.toMap();

        return RxFirebaseDatabase.getInstance().observeEditValue(firebaseRef,attValues,0 );

    }


    //get List companies
    @NonNull
    @Override
    public Observable<List<Company>> getObservableFBXcompanies() {
    final DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference();
    Query usersQuery = firebaseRef.child("companies");

    return RxFirebaseDatabase.getInstance().observeValueEvent(usersQuery)
            .flatMap(dataSnapshot ->{
                List<Company> blogPostEntities = new ArrayList<>();
            for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                String keys = childDataSnapshot.getKey();
                //System.out.println("keys " + keys);
                Company resultx = childDataSnapshot.getValue(Company.class);
                blogPostEntities.add(resultx);
            }
             return Observable.just(blogPostEntities);
    });

    }



}
