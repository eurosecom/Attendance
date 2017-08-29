package com.sqisland.android.test_demo.mvvmdatamodel;

import android.support.annotation.NonNull;
import com.sqisland.android.test_demo.realm.RealmEmployee;
import java.util.List;
import rx.Observable;


public interface DgAllEmpsAbsIDataModel {

    //recyclerview methods
    @NonNull
    Observable<List<RealmEmployee>> getObservableFBusersRealmEmployee(String usicox);

}
