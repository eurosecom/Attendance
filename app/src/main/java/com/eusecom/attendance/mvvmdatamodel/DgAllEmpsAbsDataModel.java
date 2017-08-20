package com.eusecom.attendance.mvvmdatamodel;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import io.realm.Realm;
import rx.Observable;
import com.eusecom.attendance.models.Attendance;
import com.eusecom.attendance.models.Company;
import com.eusecom.attendance.models.Employee;
import com.eusecom.attendance.realm.RealmCompany;
import com.eusecom.attendance.realm.RealmController;
import com.eusecom.attendance.realm.RealmEmployee;
import com.eusecom.attendance.rxfirebase2.database.RxFirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import javax.inject.Inject;


public class DgAllEmpsAbsDataModel implements DgAllEmpsAbsIDataModel {

    DatabaseReference mFirebaseDatabase;
    Realm mRealm;

    public DgAllEmpsAbsDataModel(@NonNull final DatabaseReference databaseReference, @NonNull final Realm realm) {
        mFirebaseDatabase = databaseReference;
        mRealm = realm;
    }


    //recyclerview datamodel
    @NonNull
    @Override
    public Observable<List<Employee>> getObservableFBusersEmployee(String usicox) {

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
                            realmemployee.setDay01("0");
                            realmemployee.setDay02("0");
                            realmemployee.setDay03("0");
                            realmemployee.setDay04("0");
                            realmemployee.setDay05("0");
                            realmemployee.setDay06("0");
                            realmemployee.setDay07("0");
                            realmemployee.setDay08("0");
                            realmemployee.setDay09("0");
                            realmemployee.setDay10("0");

                        realmemployee.setDay11("0");
                        realmemployee.setDay12("0");
                        realmemployee.setDay13("0");
                        realmemployee.setDay14("0");
                        realmemployee.setDay15("0");
                        realmemployee.setDay16("0");
                        realmemployee.setDay17("0");
                        realmemployee.setDay18("0");
                        realmemployee.setDay19("0");
                        realmemployee.setDay20("0");

                        realmemployee.setDay21("0");
                        realmemployee.setDay22("0");
                        realmemployee.setDay23("0");
                        realmemployee.setDay24("0");
                        realmemployee.setDay25("0");
                        realmemployee.setDay26("0");
                        realmemployee.setDay27("0");
                        realmemployee.setDay28("0");
                        realmemployee.setDay29("0");
                        realmemployee.setDay30("0");

                        realmemployee.setDay31("0");


                        blogPostEntities.add(realmemployee);
                    }
                    return Observable.just(blogPostEntities);
                });

    }



    @NonNull
    @Override
    public Observable<String> getObservableSavingToRealm(@NonNull final List<RealmEmployee> employees) {

        //save to realm and get String OK or ERROR
        setRealmEmployeeData( employees );

        return Observable.just("Employees Data saved to Realm");

    }


    private void setRealmEmployeeData(@NonNull final List<RealmEmployee> realmemployees) {

        //realmcontroller.clearAll();
        mRealm.beginTransaction();
        mRealm.clear(RealmEmployee.class);
        mRealm.commitTransaction();
        for (RealmEmployee b : realmemployees) {
            // Persist your data easily
            mRealm.beginTransaction();
            mRealm.copyToRealm(b);
            mRealm.commitTransaction();
        }


    }

    private void setRealmData(@NonNull final List<Employee> employees) {

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
        mRealm.beginTransaction();
        mRealm.clear(RealmEmployee.class);
        mRealm.commitTransaction();
        for (RealmEmployee b : realmemployees) {
            // Persist your data easily
            mRealm.beginTransaction();
            mRealm.copyToRealm(b);
            mRealm.commitTransaction();
        }


    }


    @NonNull
    @Override
    public Observable<List<Attendance>> getObservableAbsencesFromFB(@NonNull final String umex, @NonNull final String usicox) {

        Query usersQuery = mFirebaseDatabase.child("company-absences").child(usicox).orderByChild("ume").equalTo(umex);

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


    @NonNull
    @Override
    public Observable<String> getObservableUpdatedRealm(@NonNull final List<Attendance> absences) {

        //update to realm and get String OK or ERROR
        updateRealmAbsencesData( absences);

        return Observable.just("Absences updated to Realm");

    }


    @NonNull
    @Override
    public Observable<List<RealmEmployee>> getObservableUpdatedListRealm(@NonNull final List<Attendance> absences) {

        //update to realm and get String OK or ERROR
        List<RealmEmployee> arrayList = updateRealmAbsencesData( absences);

        return Observable.just(arrayList);
    }


    private List<RealmEmployee> updateRealmAbsencesData(@NonNull final List<Attendance> absences) {

        //System.out.println("name " + employees.get(0).getUsername());
        ArrayList<RealmEmployee> realmemployees = new ArrayList<>();

        for (int i=0; i<absences.size(); i++) {

            RealmEmployee result = mRealm.where(RealmEmployee.class).equalTo("keyf", absences.get(i).getUsid()).findFirst();

            int zacday = 0;
            int konday = 0;
            long timestampod = 0;
            long timestampdo = 0;

            timestampod = Long.parseLong(absences.get(i).getDaod()) * 1000L;
            zacday = getMonthDaysNumber(getDate(timestampod ));

            timestampdo = Long.parseLong(absences.get(i).getDado()) * 1000L;
            konday = getMonthDaysNumber(getDate(timestampdo ));

            //System.out.println("getdaod " + getDate(timestampod ));
            //System.out.println("zacday " + zacday + "");
            //System.out.println("getdado " + getDate(timestampdo ));
            //System.out.println("konday " + konday + "");

            mRealm.beginTransaction();

            if( zacday <= 1 && konday >= 1 ) { result.setDay01("1"); }
            if( zacday <= 2 && konday >= 2 ) { result.setDay02("1"); }
            if( zacday <= 3 && konday >= 3 ) { result.setDay03("1"); }
            if( zacday <= 4 && konday >= 4 ) { result.setDay04("1"); }
            if( zacday <= 5 && konday >= 5 ) { result.setDay05("1"); }
            if( zacday <= 6 && konday >= 6 ) { result.setDay06("1"); }
            if( zacday <= 7 && konday >= 7 ) { result.setDay07("1"); }
            if( zacday <= 8 && konday >= 8 ) { result.setDay08("1"); }
            if( zacday <= 9 && konday >= 9 ) { result.setDay09("1"); }
            if( zacday <= 10 && konday >= 10 ) { result.setDay10("1"); }

            if( zacday <= 11 && konday >= 11 ) { result.setDay11("1"); }
            if( zacday <= 12 && konday >= 12 ) { result.setDay12("1"); }
            if( zacday <= 13 && konday >= 13 ) { result.setDay13("1"); }
            if( zacday <= 14 && konday >= 14 ) { result.setDay14("1"); }
            if( zacday <= 15 && konday >= 15 ) { result.setDay15("1"); }
            if( zacday <= 16 && konday >= 16 ) { result.setDay16("1"); }
            if( zacday <= 17 && konday >= 17 ) { result.setDay17("1"); }
            if( zacday <= 18 && konday >= 18 ) { result.setDay18("1"); }
            if( zacday <= 19 && konday >= 19 ) { result.setDay19("1"); }
            if( zacday <= 20 && konday >= 20 ) { result.setDay20("1"); }

            if( zacday <= 21 && konday >= 21 ) { result.setDay21("1"); }
            if( zacday <= 22 && konday >= 22 ) { result.setDay22("1"); }
            if( zacday <= 23 && konday >= 23 ) { result.setDay23("1"); }
            if( zacday <= 24 && konday >= 24 ) { result.setDay24("1"); }
            if( zacday <= 25 && konday >= 25 ) { result.setDay25("1"); }
            if( zacday <= 26 && konday >= 26 ) { result.setDay26("1"); }
            if( zacday <= 27 && konday >= 27 ) { result.setDay27("1"); }
            if( zacday <= 28 && konday >= 28 ) { result.setDay28("1"); }
            if( zacday <= 29 && konday >= 29 ) { result.setDay29("1"); }
            if( zacday <= 30 && konday >= 30 ) { result.setDay30("1"); }

            if( zacday <= 31 && konday >= 31 ) { result.setDay31("1"); }

            mRealm.copyToRealmOrUpdate(result);
            mRealm.commitTransaction();

        }

        mRealm.refresh();
        List<RealmEmployee> results = mRealm.where(RealmEmployee.class).findAll();

        return results;

    }

    private int getMonthDaysNumber(String daydate){

        try{
            SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = inFormat.parse(daydate);
            Calendar c = Calendar.getInstance();
            c.setTime(date); // yourdate is an object of type Date
            int dayOfWeek = c.get(Calendar.DAY_OF_MONTH);
            return dayOfWeek;

        }
        catch(Exception ex){
            return 0;
        }
    }

    private String getDate(long timeStamp){

        try{
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate);
        }
        catch(Exception ex){
            return "xx";
        }
    }

    @NonNull
    @Override
    public Observable<List<RealmCompany>> getObservableFBmycompanyRealmEmployee(String usicox) {

        Query usersQuery = mFirebaseDatabase.child("companies").orderByChild("cmico").equalTo(usicox).limitToFirst(1);

        return RxFirebaseDatabase.getInstance().observeValueEvent(usersQuery)
                .flatMap(dataSnapshot ->{
                    List<RealmCompany> blogPostEntities = new ArrayList<>();
                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                        String keys = childDataSnapshot.getKey();
                        //System.out.println("keys " + keys);
                        Company resultx = childDataSnapshot.getValue(Company.class);

                        //transform company to realmemployee
                        RealmCompany realmemployee = new RealmCompany();
                        realmemployee.setUsername(resultx .getCmname());
                        realmemployee.setEmail(resultx .getCmcity());
                        realmemployee.setUsico(resultx .getCmico());
                        realmemployee.setUstype(resultx .getCmfir());
                        realmemployee.setKeyf(keys);
                        realmemployee.setDay01("0");
                        realmemployee.setDay02("0");
                        realmemployee.setDay03("0");
                        realmemployee.setDay04("0");
                        realmemployee.setDay05("0");
                        realmemployee.setDay06("0");
                        realmemployee.setDay07("0");
                        realmemployee.setDay08("0");
                        realmemployee.setDay09("0");
                        realmemployee.setDay10("0");

                        realmemployee.setDay11("0");
                        realmemployee.setDay12("0");
                        realmemployee.setDay13("0");
                        realmemployee.setDay14("0");
                        realmemployee.setDay15("0");
                        realmemployee.setDay16("0");
                        realmemployee.setDay17("0");
                        realmemployee.setDay18("0");
                        realmemployee.setDay19("0");
                        realmemployee.setDay20("0");

                        realmemployee.setDay21("0");
                        realmemployee.setDay22("0");
                        realmemployee.setDay23("0");
                        realmemployee.setDay24("0");
                        realmemployee.setDay25("0");
                        realmemployee.setDay26("0");
                        realmemployee.setDay27("0");
                        realmemployee.setDay28("0");
                        realmemployee.setDay29("0");
                        realmemployee.setDay30("0");

                        realmemployee.setDay31("0");


                        blogPostEntities.add(realmemployee);
                    }
                    return Observable.just(blogPostEntities);
                });

    }

    @NonNull
    @Override
    public Observable<String> getObservableCompanySavingToRealm(@NonNull final List<RealmCompany> employees) {

        //save to realm and get String OK or ERROR
        setRealmCompanyData( employees);

        return Observable.just("Company Data saved to Realm");

    }

    private void setRealmCompanyData(@NonNull final List<RealmCompany> realcompany) {

        mRealm.beginTransaction();
        mRealm.clear(RealmCompany.class);
        mRealm.commitTransaction();
        for (RealmCompany b : realcompany) {
            // Persist your data easily
            mRealm.beginTransaction();
            mRealm.copyToRealm(b);
            mRealm.commitTransaction();
        }


    }

    @NonNull
    @Override
    public Observable<List<RealmCompany>> getObservableUpdatedListCompanyRealm(@NonNull final List<Attendance> absences) {

        //update to realm and get String OK or ERROR
        List<RealmCompany> arrayList = updateCompanyRealmAbsencesData( absences);

        return Observable.just(arrayList);
    }


    private List<RealmCompany> updateCompanyRealmAbsencesData(@NonNull final List<Attendance> absences) {

        //System.out.println("name " + employees.get(0).getUsername());
        ArrayList<RealmCompany> realmemployees = new ArrayList<>();

        for (int i=0; i<absences.size(); i++) {

            RealmCompany result = mRealm.where(RealmCompany.class).findFirst();

            int zacday = 0;
            int konday = 0;
            long timestampod = 0;
            long timestampdo = 0;

            timestampod = Long.parseLong(absences.get(i).getDaod()) * 1000L;
            zacday = getMonthDaysNumber(getDate(timestampod ));

            timestampdo = Long.parseLong(absences.get(i).getDado()) * 1000L;
            konday = getMonthDaysNumber(getDate(timestampdo ));

            //System.out.println("getdaod " + getDate(timestampod ));
            //System.out.println("zacday " + zacday + "");
            //System.out.println("getdado " + getDate(timestampdo ));
            //System.out.println("konday " + konday + "");

            mRealm.beginTransaction();

            if( zacday <= 1 && konday >= 1 ) { result.setDay01("1"); }
            if( zacday <= 2 && konday >= 2 ) { result.setDay02("1"); }
            if( zacday <= 3 && konday >= 3 ) { result.setDay03("1"); }
            if( zacday <= 4 && konday >= 4 ) { result.setDay04("1"); }
            if( zacday <= 5 && konday >= 5 ) { result.setDay05("1"); }
            if( zacday <= 6 && konday >= 6 ) { result.setDay06("1"); }
            if( zacday <= 7 && konday >= 7 ) { result.setDay07("1"); }
            if( zacday <= 8 && konday >= 8 ) { result.setDay08("1"); }
            if( zacday <= 9 && konday >= 9 ) { result.setDay09("1"); }
            if( zacday <= 10 && konday >= 10 ) { result.setDay10("1"); }

            if( zacday <= 11 && konday >= 11 ) { result.setDay11("1"); }
            if( zacday <= 12 && konday >= 12 ) { result.setDay12("1"); }
            if( zacday <= 13 && konday >= 13 ) { result.setDay13("1"); }
            if( zacday <= 14 && konday >= 14 ) { result.setDay14("1"); }
            if( zacday <= 15 && konday >= 15 ) { result.setDay15("1"); }
            if( zacday <= 16 && konday >= 16 ) { result.setDay16("1"); }
            if( zacday <= 17 && konday >= 17 ) { result.setDay17("1"); }
            if( zacday <= 18 && konday >= 18 ) { result.setDay18("1"); }
            if( zacday <= 19 && konday >= 19 ) { result.setDay19("1"); }
            if( zacday <= 20 && konday >= 20 ) { result.setDay20("1"); }

            if( zacday <= 21 && konday >= 21 ) { result.setDay21("1"); }
            if( zacday <= 22 && konday >= 22 ) { result.setDay22("1"); }
            if( zacday <= 23 && konday >= 23 ) { result.setDay23("1"); }
            if( zacday <= 24 && konday >= 24 ) { result.setDay24("1"); }
            if( zacday <= 25 && konday >= 25 ) { result.setDay25("1"); }
            if( zacday <= 26 && konday >= 26 ) { result.setDay26("1"); }
            if( zacday <= 27 && konday >= 27 ) { result.setDay27("1"); }
            if( zacday <= 28 && konday >= 28 ) { result.setDay28("1"); }
            if( zacday <= 29 && konday >= 29 ) { result.setDay29("1"); }
            if( zacday <= 30 && konday >= 30 ) { result.setDay30("1"); }

            if( zacday <= 31 && konday >= 31 ) { result.setDay31("1"); }

            mRealm.copyToRealmOrUpdate(result);
            mRealm.commitTransaction();

        }

        mRealm.refresh();
        List<RealmCompany> results = mRealm.where(RealmCompany.class).findAll();

        return results;

    }


}
