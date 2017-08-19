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

package com.eusecom.attendance;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.eusecom.attendance.dagger.components.DgFirebaseSubComponent;
import com.eusecom.attendance.dagger.components.FirebaseSubComponent;
import com.eusecom.attendance.dagger.modules.DgFirebaseSubModule;
import com.eusecom.attendance.dagger.modules.FirebaseSubModule;
import com.eusecom.attendance.realm.RealmCompany;
import com.eusecom.attendance.rxbus.RxBus;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;
import javax.inject.Inject;
import io.realm.Realm;
import io.realm.RealmResults;

import com.eusecom.attendance.realm.RealmController;
import com.eusecom.attendance.realm.RealmEmployee;

/**
 * Show calendar and list of all employees absences
 *
 *
 * github https://github.com/florina-muntenescu/DroidconMVVM
 * by https://medium.com/upday-devs/android-architecture-patterns-part-3-model-view-viewmodel-e7eeee76b73b
 *
 */

public class  DgAllEmpsAbsMvvmActivity extends BaseDatabaseActivity {

    private FragmentPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;
    private FloatingActionButton fab;
    int whatispage=0;
    Toolbar mActionBarToolbar;
    private RxBus _rxBus;

    @Inject
    SharedPreferences mSharedPreferences;

    @Inject
    Realm realm;
    @Inject
    RealmController realmcontroller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allempsabs);

        //inject DgAllEmpsAbsComponent
        //((AttendanceApplication) getApplication()).getDgAllEmpsAbsComponent().inject(this);

        DgFirebaseSubComponent.Builder builder = (DgFirebaseSubComponent.Builder)
                ((AttendanceApplication) getApplication()).getApplicationComponent()
                        .subcomponentBuilders()
                        .get(DgFirebaseSubComponent.Builder.class)
                        .get();
        builder.activityModule(new DgFirebaseSubModule()).build().inject(this);

        // Obtain realm and realmcontroller instance classic
        //realmcontroller = RealmController.with(this);
        //realm = realmcontroller.getRealm();
        //setRealmData();

        _rxBus = ((AttendanceApplication) getApplication()).getRxBusSingleton();

        mActionBarToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle(mSharedPreferences.getString("ume", "") + " " + getString(R.string.allempsabs));

        // Create the adapter that will return a fragment for each section
        mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            private final Fragment[] mFragments = new Fragment[] {
                    new DgAllEmpsAbsListFragment()
            };
            private final String[] mFragmentNames = new String[] {
                    getString(R.string.action_myemployee)
            };
            @Override
            public Fragment getItem(int position) {
                return mFragments[position];
            }
            @Override
            public int getCount() {
                return mFragments.length;
            }
            @Override
            public CharSequence getPageTitle(int position) {
                return mFragmentNames[position];
            }
        };
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mPagerAdapter);


        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            public void onPageSelected(int position) {
                // Check if this is the page you want.
                if(position == 0){
                    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                    fab.setVisibility(View.VISIBLE);
                    whatispage=0;
                }
                if(position == 1){
                    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                    //fab.setVisibility(View.GONE);
                    fab.setVisibility(View.VISIBLE);
                    whatispage=1;
                }

            }
        });


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(v -> {

            //_rxBus.send(new AllEmpsAbsListFragment.ClickFobEvent());
            int co = 1;

            if( co == 1 ) {
                Intent iz = new Intent(this, VyberUmeActivity.class);
                Bundle extrasz = new Bundle();
                extrasz.putString("odkial", "2");

                iz.putExtras(extrasz);
                startActivity(iz);
                finish();
            }else{

                //RealmController.with(this).refresh();
                //RealmResults<RealmEmployee> realmemployees = RealmController.with(this).getRealmEmployees();
                //RealmEmployee b = realmemployees.get(1);

                RealmController.with(this).refresh();
                RealmResults<RealmCompany> realmemployees = RealmController.with(this).getRealmCompanies();
                RealmCompany b = realmemployees.get(0);

                String username = b.getEmail() + " " + b.getUsername() + " " + b.getKeyf() + " " + b.getDay06();

                Toast.makeText(this, username, Toast.LENGTH_SHORT).show();
            }


            }
        );


        String serverx = mSharedPreferences.getString("servername", "");
        Toast.makeText(DgAllEmpsAbsMvvmActivity.this, serverx, Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewPager=null;
        mPagerAdapter=null;
        _rxBus = null;

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_database, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

            if (id == R.id.action_logout) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, EmailPasswordActivity.class));
                finish();
                return true;
            }
            if (id == R.id.action_settings) {
                startActivity(new Intent(this, SettingsActivity.class));
                finish();
                return true;
            }

            return super.onOptionsItemSelected(item);
    }


    private void setRealmData() {

        ArrayList<RealmEmployee> realmemployees = new ArrayList<>();

        RealmEmployee realmemployee = new RealmEmployee();
        realmemployee.setUsername("Name xxxx " + System.currentTimeMillis());
        realmemployee.setEmail("Reto Meier");
        realmemployees.add(realmemployee);

        realmemployee = new RealmEmployee();
        realmemployee.setUsername("Name xxxx " + System.currentTimeMillis());
        realmemployee.setEmail("Reto Meier");
        realmemployees.add(realmemployee);

        realmcontroller.clearAll();
        for (RealmEmployee b : realmemployees) {
            // Persist your data easily
            realm.beginTransaction();
            //realm.copyToRealm(b);
            realm.commitTransaction();
        }


    }


}
