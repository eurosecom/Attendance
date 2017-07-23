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
import com.eusecom.attendance.models.Employee;
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

    @Inject
    SharedPreferences mSharedPreferences;

    //create mvvm without dagger2
    //@NonNull
    //private AllEmpsAbsMvvmViewModel mViewModel;

    @Inject
    AllEmpsAbsMvvmViewModel mViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //create mvvm without dagger2
        //mViewModel = getAllEmpsAbsMvvmViewModel();

        _disposables = new CompositeDisposable();

        _rxBus = ((AttendanceApplication) getActivity().getApplication()).getRxBusSingleton();

        ConnectableFlowable<Object> tapEventEmitter = _rxBus.asFlowable().publish();

        _disposables
                .add(tapEventEmitter.subscribe(event -> {
                    if (event instanceof AllEmpsAbsListFragment.ClickFobEvent) {
                        Log.d("AllEmpsAbsActivity  ", " fobClick ");
                        String serverx = "AllEmpsAbsListFragment fobclick";
                        Toast.makeText(getActivity(), serverx, Toast.LENGTH_SHORT).show();


                    }
                    if (event instanceof Employee) {
                        String icos = ((Employee) event).getUsername();
                        Employee model= (Employee) event;

                        Log.d("AllEmpsAbsListFragment ", icos);
                        String serverx = "AllEmpsAbsListFragment longclick";
                        Toast.makeText(getActivity(), serverx, Toast.LENGTH_SHORT).show();

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

        ((AttendanceApplication) getActivity().getApplication()).getAllEmpsAbsComponent().inject(this);

        mAdapter = new AllEmpsAbsRxAdapter(Collections.<Employee>emptyList(), _rxBus);
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
        mAdapter = new AllEmpsAbsRxAdapter(Collections.<Employee>emptyList(), null);
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


        mSubscription.add(mViewModel.getObservableFBusersEmployee()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(this::setEmployees));


    }



    private void unBind() {
        mAdapter.setData(Collections.<Employee>emptyList());
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

    private void setEmployees(@NonNull final List<Employee> employees) {

        assert mRecycler != null;
        mAdapter.setData(employees);

    }


    public static class ClickFobEvent {}

    //create mvvm without dagger2
    @NonNull
    private AllEmpsAbsMvvmViewModel getAllEmpsAbsMvvmViewModel() {
        return ((AttendanceApplication) getActivity().getApplication()).getAllEmpsAbsMvvmViewModel();
    }


}
