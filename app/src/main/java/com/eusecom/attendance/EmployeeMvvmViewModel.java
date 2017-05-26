package com.eusecom.attendance;

import android.support.annotation.NonNull;

import java.util.List;

import rx.Observable;
import rx.subjects.BehaviorSubject;
import com.eusecom.attendance.mvvmdatamodel.IDataModel;
import com.eusecom.attendance.mvvmmodel.Language;
import com.eusecom.attendance.mvvmschedulers.ISchedulerProvider;

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
