package com.eusecom.attendance;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import com.eusecom.attendance.models.Company;
import com.eusecom.attendance.rxbus.RxBus;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.flowables.ConnectableFlowable;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;


public class AllEmpsAbsListFragment extends Fragment {

    public AllEmpsAbsListFragment() {

    }
    private CompositeDisposable _disposables;
    private AllEmpsAbsRxAdapter mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    private RxBus _rxBus = null;
    AlertDialog dialog = null;
    AlertDialog.Builder builder = null;

    @NonNull
    private CompositeSubscription mSubscription;

    @NonNull
    private AllEmpsAbsMvvmViewModel mViewModel;

    @Inject
    SharedPreferences mSharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = getAllEmpsAbsMvvmViewModel();

        _disposables = new CompositeDisposable();

        _rxBus = ((AttendanceApplication) getActivity().getApplication()).getRxBusSingleton();

        ConnectableFlowable<Object> tapEventEmitter = _rxBus.asFlowable().publish();

        _disposables
                .add(tapEventEmitter.subscribe(event -> {
                    if (event instanceof AllEmpsAbsListFragment.ClickFobEvent) {
                        Log.d("AllEmpsAbsActivity  ", " fobClick ");

                        mSubscription.add(getNewCompanyDialog(getString(R.string.newcompany), getString(R.string.fullfirma))
                                .subscribeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                                .observeOn(Schedulers.computation())
                                .subscribe(this::setBoolean)
                        );



                    }
                    if (event instanceof Company) {
                        String icos = ((Company) event).getCmico();
                        Company model= (Company) event;

                        Log.d("AllEmpsAbsListFragment ", icos);
                        getEditCompanyDialog(model);

                    }

                }));

        _disposables
                .add(tapEventEmitter.publish(stream ->
                        stream.buffer(stream.debounce(1, TimeUnit.SECONDS)))
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(taps -> {
                            ///_showTapCount(taps.size()); OK
                        }));

        _disposables.add(tapEventEmitter.connect());
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_allempsabs, container, false);

        mRecycler = (RecyclerView) rootView.findViewById(R.id.allempsabs_list);
        mRecycler.setHasFixedSize(true);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((AttendanceApplication) getActivity().getApplication()).getGitHubComponent().inject(this);

        mAdapter = new AllEmpsAbsRxAdapter(Collections.<Company>emptyList(), _rxBus);
        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);
        mRecycler.setAdapter(mAdapter);

        String serverx = "From fragment " + mSharedPreferences.getString("servername", "");
        Toast.makeText(getActivity(), serverx, Toast.LENGTH_SHORT).show();


    }//end of onActivityCreated

    @Override
    public void onDestroy() {
        super.onDestroy();
        _disposables.dispose();
        mAdapter = new AllEmpsAbsRxAdapter(Collections.<Company>emptyList(), null);
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
                dialog=null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        _rxBus = null;
        mSubscription.clear();


    }

    @Override
    public void onResume() {
        super.onResume();
        bind();
    }

    @Override
    public void onPause() {
        super.onPause();
        unBind();
    }

    private void bind() {
        mSubscription = new CompositeSubscription();


        mSubscription.add(mViewModel.getObservableFBcompanies()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(this::setCompanies));

        mSubscription.add(mViewModel.getObservableKeyNewCompany()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(this::setMessage));

        mSubscription.add(mViewModel.getObservableKeyEditedEmployee()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(this::setMessage));



    }

    private void setBoolean(@NonNull final Boolean booleanx) {
        Log.i("setBoolean ", valueOf(booleanx));
    }

    public static String valueOf(Object obj) {
        return (obj == null) ? "null" : obj.toString();
    }

    Observable<Boolean> getNewCompanyDialog(String title, String message) {

        return Observable.create((Subscriber<? super Boolean> subscriber) -> {

            LayoutInflater inflater = getActivity().getLayoutInflater();
            View textenter = inflater.inflate(R.layout.companies_new_dialog, null);
            final EditText namex = (EditText) textenter.findViewById(R.id.namex);
            namex.setText("name");
            final EditText icox = (EditText) textenter.findViewById(R.id.icox);
            icox.setText("12345678");
            final EditText cityx = (EditText) textenter.findViewById(R.id.cityx);
            cityx.setText("city");

            dialog = new AlertDialog.Builder(getActivity())
                    .setView(textenter)
                    .setTitle(title)
                    //.setMessage(message)
                    .setPositiveButton(getString(R.string.save), (dialog, which) -> {

                        String namexx =  namex.getText().toString();
                        String icoxx =  icox.getText().toString();
                        String cityxx =  cityx.getText().toString();

                        Company newCompany = new Company(icoxx, namexx, " ", "0", cityxx);

                        mViewModel.saveNewCompany(newCompany);

                        try {
                        subscriber.onNext(true);
                        subscriber.onCompleted();
                        } catch (Exception e) {
                            subscriber.onError(e);
                            e.printStackTrace();
                        }
                    })
                    .setNegativeButton(getString(R.string.cancel), (dialog, which) -> {
                        try {
                        subscriber.onNext(false);
                        subscriber.onCompleted();
                        } catch (Exception e) {
                            subscriber.onError(e);
                            e.printStackTrace();
                        }
                    })
                    .create();
            // cleaning up
            subscriber.add(Subscriptions.create(dialog::dismiss));
            //textenter = null;
            dialog.show();

        });
    }

    private void unBind() {
        mAdapter.setData(Collections.<Company>emptyList());
        //is better to use mSubscription.clear(); by https://medium.com/@scanarch/how-to-leak-memory-with-subscriptions-in-rxjava-ae0ef01ad361
        //mSubscription.unsubscribe();
        mSubscription.clear();
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
                dialog=null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void setCompanies(@NonNull final List<Company> companies) {
        assert mRecycler != null;
        //Log.i("company 0 ", companies.get(0).getCmname());
        //Log.i("company 1 ", companies.get(1).getCmname());
        mAdapter.setData(companies);
    }

    private void setMessage(@NonNull final String message) {
        Log.i("setMessage ", message);
    }

    private void getEditCompanyDialog(@NonNull final Company editcompany) {

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View textenter = inflater.inflate(R.layout.companies_edit_dialog, null, true);

        final EditText namex = (EditText) textenter.findViewById(R.id.namex);
        namex.setText(editcompany.cmname);
        final EditText cityx = (EditText) textenter.findViewById(R.id.cityx);
        cityx.setText(editcompany.cmcity);

        builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.fullfirma) + " " + editcompany.cmico);
        builder.setView(textenter);

        builder.setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {

                String namexx =  namex.getText().toString();
                String cityxx =  cityx.getText().toString();

                editcompany.setCmname(namexx);
                editcompany.setCmcity(cityxx);

                mViewModel.saveNewCompany(editcompany);

            }
        })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });
        dialog = builder.create();
        dialog.show();

    }

    public static class ClickFobEvent {}

    @NonNull
    private AllEmpsAbsMvvmViewModel getAllEmpsAbsMvvmViewModel() {
        return ((AttendanceApplication) getActivity().getApplication()).getAllEmpsAbsMvvmViewModel();
    }


}
