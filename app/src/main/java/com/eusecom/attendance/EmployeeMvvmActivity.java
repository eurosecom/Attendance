package com.eusecom.attendance;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.flowables.ConnectableFlowable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import com.eusecom.attendance.models.Employee;
import com.eusecom.attendance.mvvmmodel.Language;
import com.eusecom.attendance.rxbus.RxBus;
import android.support.design.widget.CoordinatorLayout;

import static android.support.design.R.styleable.CoordinatorLayout;

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
    private TextView mGreetingView, mMessageView;

    @Nullable
    private EditText field_title;

    @Nullable
    private Spinner mLanguagesSpinner;

    @Nullable
    private LanguageMvvmSpinnerAdapter mLanguageSpinnerAdapter;

    Toolbar mActionBarToolbar;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvvm_employees);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);

        mActionBarToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle(getString(R.string.action_myemployee));

        mViewModel = getEmployeeMvvmViewModel();

        _rxBus = ((AttendanceApplication) getApplication()).getRxBusSingleton();

        _disposables = new CompositeDisposable();
        ConnectableFlowable<Object> tapEventEmitter = _rxBus.asFlowable().publish();
        _disposables
                .add(tapEventEmitter.subscribe(event -> {

                    //Log.d("rxBus ", "tapEventEmitter");

                    if (event instanceof EmployeeMvvmActivity.TapEvent) {
                        ///_showTapText();
                        //Log.d("rxBus ", "EmployeeMvvmActivity.TapEvent ");
                    }
                    if (event instanceof Employee) {
                        String keys = ((Employee) event).getUsatw();
                        //Log.d("In FRGM longClick", keys);

                        Employee model= (Employee) event;

                        //Toast.makeText(this, "Longclick " + keys,Toast.LENGTH_SHORT).show();
                        getEditEmloyeeDialog(model);

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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("fab ", "onclick ");
            }
        });
        mMessageView = (TextView) findViewById(R.id.message);

        mGreetingView = (TextView) findViewById(R.id.greeting);

        field_title = (EditText) findViewById(R.id.field_title);

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
    public void onDestroy() {
        super.onDestroy();
        _disposables.dispose();
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

        mSubscription.add(mViewModel.getObservableKeyEditedEmployee()
                                    .subscribeOn(Schedulers.computation())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(this::setMessage));

        mSubscription.add(mViewModel.getObservableFob()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setMessage));


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
        mAdapter.setData(Collections.<Employee>emptyList());
        //is better to use mSubscription.clear(); by https://medium.com/@scanarch/how-to-leak-memory-with-subscriptions-in-rxjava-ae0ef01ad361
        //mSubscription.unsubscribe();
        mSubscription.clear();
    }

    //recyclerview methods

    private void getEditEmloyeeDialog(@NonNull final Employee employee) {

        String keys = employee.getUsatw();
        //Log.d("In editDialog ", keys);

        LayoutInflater inflater = LayoutInflater.from(this);
        final View textenter = inflater.inflate(R.layout.employee_edit_dialog, null);
        final EditText namex = (EditText) textenter.findViewById(R.id.namex);
        namex.setText(employee.username);
        final EditText oscx = (EditText) textenter.findViewById(R.id.oscx);
        oscx.setText(employee.usosc);
        final EditText icox = (EditText) textenter.findViewById(R.id.icox);
        icox.setText(employee.usico);
        final EditText typx = (EditText) textenter.findViewById(R.id.typx);
        typx.setText(employee.ustype);
        final EditText uswx = (EditText) textenter.findViewById(R.id.uswx);
        uswx.setText("0");
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(textenter).setTitle(employee.email);
        builder.setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {

                String namexx =  namex.getText().toString();
                String oscxx =  oscx.getText().toString();
                String icoxx =  icox.getText().toString();
                String typxx =  typx.getText().toString();
                String uswxx =  uswx.getText().toString();

                mViewModel.saveEditEmloyee(employee, namexx, oscxx, icoxx, typxx, uswxx);

            }
        })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });
        AlertDialog dialog = builder.create();
        builder.show();


    }

    private void setEmployees(@NonNull final List<Employee> employees) {

        assert mRecycler != null;
        mAdapter.setData(employees);

    }

    private void setMessage(@NonNull final String message) {
        //Log.i("setMessage ", "method ");
        final String messagex = getString(R.string.saved) + " " + message;
        //field_title.setText(message);
        Snackbar snackbar = Snackbar
            .make(coordinatorLayout, messagex, Snackbar.LENGTH_LONG);

        snackbar.show();
    }


    //spinner methods
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

    private void itemSelected(final int position) {
        assert mLanguageSpinnerAdapter != null;

        Language languageSelected = mLanguageSpinnerAdapter.getItem(position);
        mViewModel.emitlanguageSelected(languageSelected);

    }

    public static class TapEvent {}

    @NonNull
    private EmployeeMvvmViewModel getEmployeeMvvmViewModel() {
        return ((AttendanceApplication) getApplication()).getEmployeeMvvmViewModel();
    }


}
