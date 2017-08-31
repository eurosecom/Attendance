package com.sqisland.android.test_demo.mvvmdatamodel;

import android.support.annotation.NonNull;
import com.sqisland.android.test_demo.realm.RealmEmployee;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import io.realm.Realm;
import rx.Observable;
import javax.inject.Inject;


public class DgAllEmpsAbsDataModel implements DgAllEmpsAbsIDataModel {

    Realm mRealm;

    public DgAllEmpsAbsDataModel(@NonNull final Realm realm) {
        mRealm = realm;
    }


    //recyclerview datamodel

    @NonNull
    @Override
    public Observable<List<RealmEmployee>> getObservableFBusersRealmEmployee(String usicox) {


                        List<RealmEmployee> blogPostEntities = new ArrayList<>();

                        RealmEmployee realmemployee = new RealmEmployee();
                        realmemployee.setUsername("usernameRealApp");
                        realmemployee.setEmail("emailRealApp");
                        realmemployee.setUsico("usicoRealApp");
                        realmemployee.setUstype("ustypeRealApp");
                        realmemployee.setKeyf("uskeyRealApp");
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

                    return Observable.just(blogPostEntities);

    }



}
