package com.eusecom.attendance;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;
import rx.Observable;
import rx.subjects.BehaviorSubject;
import com.eusecom.attendance.models.Employee;
import com.eusecom.attendance.mvvmdatamodel.IDataModel;
import com.eusecom.attendance.mvvmmodel.Language;
import com.eusecom.attendance.mvvmschedulers.ISchedulerProvider;
import com.google.firebase.database.DataSnapshot;

/**
 * View model for the main activity.
 */
public class EmployeeMvvmViewModel {

    @NonNull
    private final IDataModel mDataModel;
    @NonNull
    private final ISchedulerProvider mSchedulerProvider;


    public EmployeeMvvmViewModel(@NonNull final IDataModel dataModel,
                         @NonNull final ISchedulerProvider schedulerProvider) {
        mDataModel = dataModel;
        mSchedulerProvider = schedulerProvider;
    }

    //fab methods
    @NonNull
    private final BehaviorSubject<String> mObservableEmitFob = BehaviorSubject.create();

    public void saveRxFBemployee() {
        Log.d("mViewModel ", "saveRxFBemployee() ");
        mObservableEmitFob.onNext("fob Clicked");
    }

    @NonNull
    public Observable<String> getObservableFob() {
        //return Observable.just("xxx");
        return mObservableEmitFob
                .observeOn(mSchedulerProvider.computation())
                .flatMap(emitString -> { return mDataModel.getSavedRxFBemployee(emitString); }
                );
    }


    //recyclerview method

    public Observable<List<Employee>> getObservableEmployees() {
        return mDataModel.getObservableEmployees();
    }

    public Observable<DataSnapshot> getObservableFBabsences() {
        return mDataModel.getObservableFBabsences();
    }

    //mViewModel.getObservableFBusers() get DataSnapshot, it is not lot of success
    public Observable<DataSnapshot> getObservableFBusers() {
        return mDataModel.getObservableFBusers();
    }

    //mViewModel.getObservableFBusersEmployee get List<Employee>
    public Observable<List<Employee>> getObservableFBusersEmployee() {
        return mDataModel.getObservableFBusersEmployee();
    }

    @NonNull
    private final BehaviorSubject<Employee> mObservableEditedEmployee = BehaviorSubject.create();

    public void saveEditEmloyee(@NonNull final Employee employee, String namexx, String oscxx,
                                              String icoxx, String typxx, String uswxx) {

        Employee editEmployee = employee;
        editEmployee.setUsername(namexx);
        editEmployee.setUsosc(oscxx);
        editEmployee.setUsico(icoxx);
        editEmployee.setUstype(typxx);
        editEmployee.setUsatw(uswxx);
        mObservableEditedEmployee.onNext(editEmployee);
    }

    @NonNull
    public Observable<String> getObservableKeyEditedEmployee() {
        //return Observable.just("xxx");
        return mObservableEditedEmployee
                .observeOn(mSchedulerProvider.computation())
                .flatMap(employee -> { return mDataModel.getObservableKeyFBeditUser(employee); }
                );
    }


    //spinner method

    @NonNull
    private final BehaviorSubject<Language> mObservableSelectedLanguage = BehaviorSubject.create();

    @NonNull
    public Observable<String> getObservableGreeting() {
        return mObservableSelectedLanguage
                .observeOn(mSchedulerProvider.computation())
                .map(Language::getCode)
                .flatMap(mDataModel::getObservableGreetingByLanguageCode);
    }

    @NonNull
    public Observable<List<Language>> getObservableSupportedLanguages() {
        return mDataModel.getObservableSupportedLanguages();
    }

    public void emitlanguageSelected(@NonNull final Language language) {
        mObservableSelectedLanguage.onNext(language);
    }

    public int getValue(@NonNull int xxx) {
        int value = 23;
        return value;
    }


}
