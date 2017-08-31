/*
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sqisland.android.test_demo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.sqisland.android.test_demo.realm.RealmEmployee;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


public class  DgAeaActivity extends AppCompatActivity {

    private AllEmpsAbsRxRealmAdapter mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    private FloatingActionButton fab;
    Toolbar mActionBarToolbar;

    @NonNull
    private CompositeSubscription mSubscription;

    @Inject
    SharedPreferences mSharedPreferences;

    @Inject
    DgAllEmpsAbsMvvmViewModel mViewModel;

    @Inject
    Clock clock;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aea);

        ((DemoApplication) getApplication()).dgaeacomponent().inject(this);

        mRecycler = (RecyclerView) findViewById(R.id.allempsabs_list);
        mRecycler.setHasFixedSize(true);
        String umex = mSharedPreferences.getString("ume", "");
        //String umex = "07.2017";
        mAdapter = new AllEmpsAbsRxRealmAdapter(Collections.<RealmEmployee>emptyList(), umex);
        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);
        mRecycler.setAdapter(mAdapter);

        String datetimes = clock.getNow().toString();
        Toast.makeText(this, datetimes, Toast.LENGTH_SHORT).show();

        String serverx = "From fragment " + mSharedPreferences.getString("servername", "");
        Toast.makeText(this, serverx, Toast.LENGTH_SHORT).show();

        //String datetimes = clock.getNow().toString();
        //Toast.makeText(this, datetimes, Toast.LENGTH_SHORT).show();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(v -> {

                    String serverxx = "click FAB";
                    Toast.makeText(DgAeaActivity.this, serverxx, Toast.LENGTH_SHORT).show();


                }
        );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mAdapter = new AllEmpsAbsRxRealmAdapter(Collections.<RealmEmployee>emptyList(), "01.2017");
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

    }


    private void setRealmEmployees(@NonNull final List<RealmEmployee> realmemployees) {


        mAdapter.setRealmData(realmemployees);

    }




}
