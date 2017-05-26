package com.eusecom.attendance;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import com.eusecom.attendance.mvvmdatamodel.IDataModel;
import com.eusecom.attendance.mvvmmodel.Language;

//github https://github.com/florina-muntenescu/DroidconMVVM
//by https://medium.com/upday-devs/android-architecture-patterns-part-3-model-view-viewmodel-e7eeee76b73b

//1. getViewModel
//2. subscriptions for values emited
//3. in MainViewModel interaction with user mViewModel.emitlanguageSelected(languageSelected);

public class EmployeeMvvmActivity extends AppCompatActivity {

    @NonNull
    private CompositeSubscription mSubscription;

    @NonNull
    private EmployeeMvvmViewModel mViewModel;

    @Nullable
    private TextView mGreetingView;

    @Nullable
    private Spinner mLanguagesSpinner;

    @Nullable
    private LanguageMvvmSpinnerAdapter mLanguageSpinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvvm_employees);

        mViewModel = getEmployeeMvvmViewModel();
        setupViews();
    }

    private void setupViews() {
        mGreetingView = (TextView) findViewById(R.id.greeting);

        mLanguagesSpinner = (Spinner) findViewById(R.id.languages);
        assert mLanguagesSpinner != null;
        mLanguagesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, final View view,
                                       final int position, final long id) {
                itemSelected(position);
            }

            @Override
            public void onNothingSelected(final AdapterView<?> parent) {
                //nothing to do here
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        bind();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unBind();
    }

    private void bind() {
        mSubscription = new CompositeSubscription();

        mSubscription.add(mViewModel.getObservableGreeting()
                                    .subscribeOn(Schedulers.computation())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(this::setGreeting));

        mSubscription.add(mViewModel.getObservableSupportedLanguages()
                                    .subscribeOn(Schedulers.computation())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(this::setLanguages));
    }

    private void unBind() {
        mSubscription.unsubscribe();
    }

    private void setGreeting(@NonNull final String greeting) {
        assert mGreetingView != null;

        mGreetingView.setText(greeting);
    }

    private void setLanguages(@NonNull final List<Language> languages) {
        assert mLanguagesSpinner != null;

        mLanguageSpinnerAdapter = new LanguageMvvmSpinnerAdapter(this,
                                                             R.layout.employee_mvvm_item,
                                                             languages);
        mLanguagesSpinner.setAdapter(mLanguageSpinnerAdapter);
    }

    @NonNull
    private EmployeeMvvmViewModel getEmployeeMvvmViewModel() {
        return ((AttendanceApplication) getApplication()).getEmployeeMvvmViewModel();
    }

    private void itemSelected(final int position) {
        assert mLanguageSpinnerAdapter != null;

        Language languageSelected = mLanguageSpinnerAdapter.getItem(position);
        mViewModel.emitlanguageSelected(languageSelected);
    }
}
