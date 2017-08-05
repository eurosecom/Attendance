package com.eusecom.attendance.mvvmdatamodel;

import android.support.annotation.NonNull;
import android.util.Log;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import io.realm.Realm;
import rx.Observable;

import com.eusecom.attendance.models.Attendance;
import com.eusecom.attendance.models.Employee;
import com.eusecom.attendance.realm.RealmController;
import com.eusecom.attendance.realm.RealmEmployee;
import com.eusecom.attendance.rxfirebase2.database.RxFirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;


public class AllEmpsAbsDataModel implements AllEmpsAbsIDataModel {

    DatabaseReference mFirebaseDatabase;

    public AllEmpsAbsDataModel(@NonNull final DatabaseReference databaseReference) {
        mFirebaseDatabase = databaseReference;
    }


    //recyclerview datamodel
    @NonNull
    @Override
    public Observable<List<Employee>> getObservableFBusersEmployee(String usicox) {

        //String usicox = mSharedPreferences.getString("usico", "");
        Log.d("DataModel ", usicox);

        //injected
        Query usersQuery = mFirebaseDatabase.child("users").orderByChild("usico").equalTo(usicox);

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

    @NonNull
    @Override
    public Observable<List<RealmEmployee>> getObservableFBusersRealmEmployee(String usicox) {

        //String usicox = mSharedPreferences.getString("usico", "");
        Log.d("DataModel ", usicox);

        //injected
        Query usersQuery = mFirebaseDatabase.child("users").orderByChild("usico").equalTo(usicox);

        return RxFirebaseDatabase.getInstance().observeValueEvent(usersQuery)
                .flatMap(dataSnapshot ->{
                    List<RealmEmployee> blogPostEntities = new ArrayList<>();
                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                        String keys = childDataSnapshot.getKey();
                        //System.out.println("keys " + keys);
                        Employee resultx = childDataSnapshot.getValue(Employee.class);
                        resultx.setKeyf(keys);

                            //transform employee to realmemployee
                            RealmEmployee realmemployee = new RealmEmployee();
                            realmemployee.setUsername(resultx .getUsername());
                            realmemployee.setEmail(resultx .getEmail());
                            realmemployee.setUsico(resultx .getUsico());
                            realmemployee.setUstype(resultx .getUstype());
                            realmemployee.setKeyf(resultx .getKeyf());


                        blogPostEntities.add(realmemployee);
                    }
                    return Observable.just(blogPostEntities);
                });

    }



    @NonNull
    @Override
    public Observable<String> getObservableSavingToRealm(@NonNull final List<RealmEmployee> employees, Realm realm) {

        //save to realm and get String OK or ERROR
        setRealmEmployeeData( employees, realm);

        return Observable.just("Data saved to Realm");

    }

    Realm realm;
    RealmController realmcontroller;

    private void setRealmEmployeeData(@NonNull final List<RealmEmployee> realmemployees, Realm realm) {

        //realmcontroller.clearAll();
        realm.beginTransaction();
        realm.clear(RealmEmployee.class);
        realm.commitTransaction();
        for (RealmEmployee b : realmemployees) {
            // Persist your data easily
            realm.beginTransaction();
            realm.copyToRealm(b);
            realm.commitTransaction();
        }


    }

    private void setRealmData(@NonNull final List<Employee> employees, Realm realm) {

        //System.out.println("name " + employees.get(0).getUsername());
        ArrayList<RealmEmployee> realmemployees = new ArrayList<>();

        for (int i=0; i<employees.size(); i++) {
            RealmEmployee realmemployee = new RealmEmployee();
            realmemployee.setUsername(employees.get(i).getUsername());
            realmemployee.setEmail(employees.get(i).getEmail());
            realmemployee.setUsico(employees.get(i).getUsico());
            realmemployee.setUstype(employees.get(i).getUstype());
            realmemployee.setKeyf(employees.get(i).getKeyf());
            realmemployees.add(realmemployee);
        }

        //realmcontroller.clearAll();
        realm.beginTransaction();
        realm.clear(RealmEmployee.class);
        realm.commitTransaction();
        for (RealmEmployee b : realmemployees) {
            // Persist your data easily
            realm.beginTransaction();
            realm.copyToRealm(b);
            realm.commitTransaction();
        }


    }


    @NonNull
    @Override
    public Observable<List<Attendance>> getObservableAbsencesFromFB(@NonNull final String umex, @NonNull final String usicox) {

        //get absences for ico and umex

        //injected
        //Query usersQuery = mFirebaseDatabase.child("company-absences").child(usicox).orderByChild("ume").equalTo(umex);
        Query usersQuery = mFirebaseDatabase.child("company-absences").child(usicox);

        return RxFirebaseDatabase.getInstance().observeValueEvent(usersQuery)
                .flatMap(dataSnapshot ->{
                    List<Attendance> blogPostEntities = new ArrayList<>();
                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                        String keys = childDataSnapshot.getKey();
                        //System.out.println("keys " + keys);
                        Attendance resultx = childDataSnapshot.getValue(Attendance.class);
                        resultx.setRok(keys);
                        blogPostEntities.add(resultx);
                    }
                    return Observable.just(blogPostEntities);
                });

    }




}
