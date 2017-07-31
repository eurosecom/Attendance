package com.eusecom.attendance.realm;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;
 
import com.eusecom.attendance.realm.RealmEmployee;
import io.realm.Realm;
import io.realm.RealmResults;
 
 
public class RealmController {
 
    private static RealmController instance;
    private final Realm realm;
 
    public RealmController(Application application) {
        realm = Realm.getDefaultInstance();
    }
 
    public static RealmController with(Fragment fragment) {
 
        if (instance == null) {
            instance = new RealmController(fragment.getActivity().getApplication());
        }
        return instance;
    }
 
    public static RealmController with(Activity activity) {
 
        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }
 
    public static RealmController with(Application application) {
 
        if (instance == null) {
            instance = new RealmController(application);
        }
        return instance;
    }
 
    public static RealmController getInstance() {
 
        return instance;
    }
 
    public Realm getRealm() {
 
        return realm;
    }
 
    //Refresh the realm istance
    public void refresh() {
 
        realm.refresh();
    }
 
    //clear all objects from RealmEmployee.class
    public void clearAll() {
 
        realm.beginTransaction();
        realm.clear(RealmEmployee.class);
        realm.commitTransaction();
    }
 
    //find all objects in the RealmEmployee.class
    public RealmResults<RealmEmployee> getRealmEmployees() {
 
        return realm.where(RealmEmployee.class).findAll();
    }
 
    //query a single item with the given id
    public RealmEmployee getRealmEmployee(String id) {
 
        return realm.where(RealmEmployee.class).equalTo("id", id).findFirst();
    }
 
    //check if RealmEmployee.class is empty
    public boolean hasRealmEmployees() {
 
        return !realm.allObjects(RealmEmployee.class).isEmpty();
    }
 
    //query example
    public RealmResults<RealmEmployee> queryedRealmEmployees() {
 
        return realm.where(RealmEmployee.class)
                .contains("author", "Author 0")
                .or()
                .contains("title", "Realm")
                .findAll();
 
    }
}