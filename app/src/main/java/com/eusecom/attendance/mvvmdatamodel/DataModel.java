package com.eusecom.attendance.mvvmdatamodel;

import android.support.annotation.NonNull;

import java.util.Arrays;
import java.util.List;

import rx.Observable;
import com.eusecom.attendance.mvvmmodel.Language;

import static com.eusecom.attendance.mvvmmodel.Language.LanguageCode;

public class DataModel implements IDataModel {

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
}
