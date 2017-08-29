package com.sqisland.android.test_demo;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Toast;

import com.sqisland.android.test_demo.realm.RealmEmployee;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.flowables.ConnectableFlowable;
import rx.Observable;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


public class DgAllEmpsAbsListFragment extends Fragment {

    public DgAllEmpsAbsListFragment() {

    }
    private CompositeDisposable _disposables;
    private AllEmpsAbsRxRealmAdapter mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;

    @NonNull
    private CompositeSubscription mSubscription;

    @Inject
    SharedPreferences mSharedPreferences;

    @Inject
    DgAllEmpsAbsMvvmViewModel mViewModel;

    //inject mViewModel.getObservableFBusersRealmEmployee() provided in DgFirebaseSubModul
    //@Inject
    //Observable<List<RealmEmployee>> emViewModelgetObservableFBusersRealmEmployee;

    AlertDialog dialog = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        //create component in AttendanceApplication
        //((AttendanceApplication) getActivity().getApplication()).getDgFirebaseSubComponent().inject(this);

        ((DemoApplication) getActivity().getApplication()).component().inject(this);

        //create component direct in class where it is injecting
        //DgFirebaseSubComponent.Builder builder = (DgFirebaseSubComponent.Builder)
        //        ((AttendanceApplication) getActivity().getApplication()).getApplicationComponent()
        //                .subcomponentBuilders()
        //                .get(DgFirebaseSubComponent.Builder.class)
        //                .get();
        //DgFirebaseSubComponent dgFirebaseSubComponent = builder.activityModule(new DgFirebaseSubModule()).build();
        //dgFirebaseSubComponent.inject(this);

        String umex = mSharedPreferences.getString("ume", "");
        //String umex = "07.2017";
        mAdapter = new AllEmpsAbsRxRealmAdapter(Collections.<RealmEmployee>emptyList(), umex);
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
        mAdapter = new AllEmpsAbsRxRealmAdapter(Collections.<RealmEmployee>emptyList(), "01.2017");
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
                dialog=null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mSubscription.unsubscribe();
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


        mSubscription.add(mViewModel.getObservableFBusersRealmEmployee()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(this::setRealmEmployees));




    }



    private void unBind() {
        mAdapter.setRealmData(Collections.<RealmEmployee>emptyList());
        //is better to use mSubscription.clear(); by https://medium.com/@scanarch/how-to-leak-memory-with-subscriptions-in-rxjava-ae0ef01ad361
        mSubscription.unsubscribe();
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



    private void setRealmEmployees(@NonNull final List<RealmEmployee> realmemployees) {


            mAdapter.setRealmData(realmemployees);

    }


    public static class ClickFobEvent {}


}
