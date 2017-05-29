package com.eusecom.attendance;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.flowables.ConnectableFlowable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import com.eusecom.attendance.models.Employee;
import com.eusecom.attendance.mvvmmodel.Language;
import com.eusecom.attendance.rxbus.RxBus;
import com.google.firebase.database.DataSnapshot;

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
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    private EmployeesRxAdapter mAdapter;
    private RxBus _rxBus;
    private CompositeDisposable _disposables;

    @Nullable
    private TextView mGreetingView;

    @Nullable
    private Spinner mLanguagesSpinner;

    @Nullable
    private LanguageMvvmSpinnerAdapter mLanguageSpinnerAdapter;

    Toolbar mActionBarToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvvm_employees);

        mActionBarToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle(getString(R.string.action_myemployee));

        mViewModel = getEmployeeMvvmViewModel();

        _rxBus = getRxBusSingleton();
        _disposables = new CompositeDisposable();
        ConnectableFlowable<Object> tapEventEmitter = _rxBus.asFlowable().publish();
        _disposables
                .add(tapEventEmitter.subscribe(event -> {
                    if (event instanceof EmployeeMvvmActivity.TapEvent) {
                        ///_showTapText();
                    }
                    if (event instanceof Employee) {
                        String keys = ((Employee) event).getUsatw();
                        Log.d("In FRGM longClick", keys);

                        Employee model= (Employee) event;

                        Toast.makeText(this, "Longclick " + keys,Toast.LENGTH_SHORT).show();

                    }
                }));

        _disposables
                .add(tapEventEmitter.publish(stream ->
                        stream.buffer(stream.debounce(1, TimeUnit.SECONDS)))
                        .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread()).subscribe(taps -> {
                            ///_showTapCount(taps.size()); OK
                        }));

        _disposables.add(tapEventEmitter.connect());

        setupViews();
        mViewModel.getObservableFBusersEmployee();
    }

    public RxBus getRxBusSingleton() {
        if (_rxBus == null) {
            _rxBus = new RxBus();
        }

        return _rxBus;
    }

    private void setupViews() {

        mRecycler = (RecyclerView) findViewById(R.id.employees_list);
        mRecycler.setHasFixedSize(true);
        mManager = new LinearLayoutManager(this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);
        mAdapter = new EmployeesRxAdapter(Collections.<Employee>emptyList(), _rxBus);
        mRecycler.setAdapter(mAdapter);

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

        mSubscription.add(mViewModel.getObservableFBusersEmployee()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setEmployees));
    }

    private void unBind() {
        mSubscription.unsubscribe();
        _disposables.dispose();
    }

    //employees methods

    private void setEmployees(@NonNull final List<Employee> employees) {

        assert mRecycler != null;
        mAdapter.setData(employees);


    }




    //languages methods
    private void setGreeting(@NonNull final String greeting) {
        assert mGreetingView != null;

        mGreetingView.setText(greeting);
    }

    private void setLanguages(@NonNull final List<Language> languages) {
        assert mLanguagesSpinner != null;

        mLanguageSpinnerAdapter = new LanguageMvvmSpinnerAdapter(this,
                                                             R.layout.employee_mvvm_spinner_item,
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

    public static class TapEvent {}


}
