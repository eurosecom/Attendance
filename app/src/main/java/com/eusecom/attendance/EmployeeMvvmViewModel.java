package com.eusecom.attendance;

import android.support.annotation.NonNull;
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

    @NonNull
    private final BehaviorSubject<Language> mObservableSelectedLanguage = BehaviorSubject.create();

    public EmployeeMvvmViewModel(@NonNull final IDataModel dataModel,
                         @NonNull final ISchedulerProvider schedulerProvider) {
        mDataModel = dataModel;
        mSchedulerProvider = schedulerProvider;
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

    //spinner method

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
