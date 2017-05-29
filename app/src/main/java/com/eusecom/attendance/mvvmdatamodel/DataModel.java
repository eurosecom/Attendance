package com.eusecom.attendance.mvvmdatamodel;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import rx.Observable;
import rx.Subscriber;

import com.eusecom.attendance.EmployeeMvvmActivity;
import com.eusecom.attendance.models.Employee;
import com.eusecom.attendance.mvvmmodel.Language;
import com.eusecom.attendance.rxfirebase2.database.RxFirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import static com.eusecom.attendance.mvvmmodel.Language.LanguageCode;

public class DataModel implements IDataModel {

    public GetFBusersEmployeeSubscriber getfbusersEmployeeSubscriber;

    @NonNull
    @Override
    public Observable<List<Employee>> getObservableFBusersEmployee() {
    getfbusersEmployeeSubscriber = new GetFBusersEmployeeSubscriber();
    final DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference();
    Query usersQuery = firebaseRef.child("users");

    return RxFirebaseDatabase.getInstance().observeValueEvent(usersQuery)
            .flatMap(dataSnapshot ->{
                List<Employee> blogPostEntities = new ArrayList<>();
            for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                String keys = childDataSnapshot.getKey();
                Log.d("keys ", keys);
                Employee resultx = childDataSnapshot.getValue(Employee.class);
                resultx.setUsatw(keys);
                blogPostEntities.add(resultx);
            }
             return Observable.just(blogPostEntities);
    });

    }

    @NonNull
    @Override
    public Observable<DataSnapshot> getObservableFBusers() {
        final DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference();
        Query usersQuery = firebaseRef.child("users");
        return RxFirebaseDatabase.getInstance().observeValueEvent(usersQuery);

    }

    @NonNull
    @Override
    public Observable<DataSnapshot> getObservableFBabsences() {
        final DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference();
        Query recentAbsencesQuery = firebaseRef.child("absences");
        return RxFirebaseDatabase.getInstance().observeValueEvent(recentAbsencesQuery);

    }

    @NonNull
    @Override
    public Observable<List<Employee>> getObservableEmployees() {
        return Observable.fromCallable(this::getEmployees);
    }

    @NonNull
    private List<Employee> getEmployees() {
        return Arrays
                .asList(new Employee("andrejd", "1"),
                        new Employee("petere", "2"));
    }

    @NonNull
    @Override
    public Observable<List<Language>> getObservableSupportedLanguages() {
        return Observable.fromCallable(this::getLanguages);
    }

    @NonNull
    private List<Language> getLanguages() {
        return Arrays
                .asList(new Language("English", LanguageCode.EN),
                        new Language("German", LanguageCode.DE),
                        new Language("Slovakian", LanguageCode.HR));
    }

    @NonNull
    @Override
    public Observable<String> getObservableGreetingByLanguageCode(@NonNull final LanguageCode code) {
        switch (code) {
            case DE:
                return Observable.just("Guten Tag!");
            case EN:
                return Observable.just("Hello!");
            case HR:
                return Observable.just("Zdravo!");
            default:
                return Observable.empty();
        }
    }

    private final class GetFBusersEmployeeSubscriber extends Subscriber<DataSnapshot> {
        @Override public void onCompleted() {

        }

        @Override public void onError(Throwable e) {

        }

        @SuppressWarnings("unchecked") @Override public void onNext(DataSnapshot dataSnapshot) {
            List<Employee> blogPostEntities = new ArrayList<>();
            for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                String keys = childDataSnapshot.getKey();
                Log.d("keys ", keys);
                Employee resultx = childDataSnapshot.getValue(Employee.class);
                resultx.setUsatw(keys);
                blogPostEntities.add(resultx);
            }
            //setEmployees(blogPostEntities);

        }
    }//end of getAbsenceSubscriber


}
