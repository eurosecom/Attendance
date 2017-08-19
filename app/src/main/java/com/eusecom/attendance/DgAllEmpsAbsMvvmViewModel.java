package com.eusecom.attendance;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;
import java.util.List;
import io.realm.Realm;
import rx.Observable;
import rx.subjects.BehaviorSubject;
import com.eusecom.attendance.models.Attendance;
import com.eusecom.attendance.models.Employee;
import com.eusecom.attendance.mvvmdatamodel.DgAllEmpsAbsIDataModel;
import com.eusecom.attendance.mvvmschedulers.ISchedulerProvider;
import com.eusecom.attendance.realm.RealmCompany;
import com.eusecom.attendance.realm.RealmController;
import com.eusecom.attendance.realm.RealmEmployee;

import javax.inject.Inject;

/**
 * View model for the CompaniesMvvmActivity.
 */
public class DgAllEmpsAbsMvvmViewModel {

    @Inject
    DgAllEmpsAbsIDataModel mDataModel;
    @Inject
    ISchedulerProvider mSchedulerProvider;

    @Inject
    SharedPreferences mSharedPreferences;

    @Inject
    Realm realm;
    @Inject
    RealmController realmcontroller;


    @Inject
    public DgAllEmpsAbsMvvmViewModel(@NonNull final DgAllEmpsAbsIDataModel dataModel,
                         @NonNull final ISchedulerProvider schedulerProvider) {
        mDataModel = dataModel;
        mSchedulerProvider = schedulerProvider;
    }

    //recyclerview method

    //get employees list from FB
    public Observable<List<Employee>> getObservableFBusersEmployee() {

        String usicox = mSharedPreferences.getString("usico", "");
        Log.d("MvvmViewModel ", usicox);
        return mDataModel.getObservableFBusersEmployee(usicox);
    }
    //end get employees list from FB

    //get realmemployees list from FB
    public Observable<List<RealmEmployee>> getObservableFBusersRealmEmployee() {

        String usicox = mSharedPreferences.getString("usico", "");
        Log.d("MvvmViewModel ", usicox);
        return mDataModel.getObservableFBusersRealmEmployee(usicox);
    }
    //end get realmemployees list from FB

    //save employees to realm
    public void emitRealmEmployeesToRealm(List<RealmEmployee> employees) {
        mObservableSaveToRealm.onNext(employees);
    }

    @NonNull
    private final BehaviorSubject<List<RealmEmployee>> mObservableSaveToRealm = BehaviorSubject.create();

    @NonNull
    public Observable<String> getObservableDataSavedToRealm() {
        return mObservableSaveToRealm
                .observeOn(mSchedulerProvider.ui())
                .flatMap(list -> mDataModel.getObservableSavingToRealm(list));
    }
    //end save employees to realm

    //get absences from FB for update realm
    public void emitAbsencesFromFBforRealm(String umex) { mObservableAbsencesFromFB.onNext(umex); }

    @NonNull
    private final BehaviorSubject<String> mObservableAbsencesFromFB = BehaviorSubject.create();

    @NonNull
    public Observable<List<Attendance>> getObservableFromFBforRealm() {
        String usicox = mSharedPreferences.getString("usico", "");
        return mObservableAbsencesFromFB
                .observeOn(mSchedulerProvider.ui())
                .flatMap(umex -> mDataModel.getObservableAbsencesFromFB(umex, usicox));
    }

    //end get absences from FB for update realm


    //update realm from absences
    public void emitUpdateRealmFromAbsences(List<Attendance> absences) {
        mObservableUpdateRealm.onNext(absences);
    }

    @NonNull
    private final BehaviorSubject<List<Attendance>> mObservableUpdateRealm = BehaviorSubject.create();

    @NonNull
    public Observable<List<RealmEmployee>> getObservableDataUpdatedRealm() {
        return mObservableUpdateRealm
                .observeOn(mSchedulerProvider.ui())
                .flatMap(list -> mDataModel.getObservableUpdatedListRealm(list));
    }
    //end update realm from absences

    //get realmemployees my company
    public Observable<List<RealmCompany>> getObservableFBcompanyRealmEmployee() {

        String usicox = mSharedPreferences.getString("usico", "");
        Log.d("MvvmViewModel ", usicox);
        return mDataModel.getObservableFBmycompanyRealmEmployee(usicox);
    }
    //end get realmemployees list from FB


    //save company to realm
    public void emitRealmCompanyToRealm(List<RealmCompany> employees) {
        mObservableCompanySaveToRealm.onNext(employees);
    }

    @NonNull
    private final BehaviorSubject<List<RealmCompany>> mObservableCompanySaveToRealm = BehaviorSubject.create();

    @NonNull
    public Observable<String> getObservableCompanyDataSavedToRealm() {
        return mObservableCompanySaveToRealm
                .observeOn(mSchedulerProvider.ui())
                .flatMap(list -> mDataModel.getObservableCompanySavingToRealm(list));
    }
    //end save company to realm

    //update company realm from absences
    public void emitUpdateCompanyRealmFromAbsences(List<Attendance> absences) {
        mObservableUpdateCompanyRealm.onNext(absences);
    }

    @NonNull
    private final BehaviorSubject<List<Attendance>> mObservableUpdateCompanyRealm = BehaviorSubject.create();

    @NonNull
    public Observable<List<RealmCompany>> getObservableDataUpdatedCompanyRealm() {
        return mObservableUpdateCompanyRealm
                .observeOn(mSchedulerProvider.ui())
                .flatMap(list -> mDataModel.getObservableUpdatedListCompanyRealm(list));
    }
    //end update company realm from absences



}
