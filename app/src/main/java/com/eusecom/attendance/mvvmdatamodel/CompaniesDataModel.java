package com.eusecom.attendance.mvvmdatamodel;

import android.support.annotation.NonNull;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rx.Observable;
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

    @NonNull
    @Override
    public Observable<List<Employee>> getObservableFBXcompanies() {
    final DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference();
    Query usersQuery = firebaseRef.child("users");

    return RxFirebaseDatabase.getInstance().observeValueEvent(usersQuery)
            .flatMap(dataSnapshot ->{
                List<Employee> blogPostEntities = new ArrayList<>();
            for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                String keys = childDataSnapshot.getKey();
                //System.out.println("keys " + keys);
                Employee resultx = childDataSnapshot.getValue(Employee.class);
                resultx.setKeyf(keys);
                blogPostEntities.add(resultx);
            }
             return Observable.just(blogPostEntities);
    });

    }



}
