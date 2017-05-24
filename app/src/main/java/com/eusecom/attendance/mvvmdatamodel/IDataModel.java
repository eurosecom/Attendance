package com.eusecom.attendance.mvvmdatamodel;

import android.support.annotation.NonNull;
import com.eusecom.attendance.mvvmmodel.Language;
import java.util.List;
import rx.Observable;

import static com.eusecom.attendance.mvvmmodel.Language.LanguageCode;

public interface IDataModel {

    @NonNull
    Observable<List<Language>> getObservableSupportedLanguages();

    @NonNull
    Observable<String> getObservableGreetingByLanguageCode(@NonNull final LanguageCode code);
}
