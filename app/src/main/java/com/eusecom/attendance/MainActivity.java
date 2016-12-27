/*
 * Copyright 2012 The Android Open Source Project
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
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eusecom.attendance.models.Attendance;
import com.eusecom.attendance.models.Post;
import com.eusecom.attendance.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends ActionBarActivity {

    //First We Declare Titles And Icons For Our Navigation Drawer List View
    //This Icons And Titles Are holded in an Array as you can see

    private String[] navMenuTitles;
    int ICONS[] = {R.drawable.ic_image_edit,R.drawable.ic_image_edit,R.drawable.ic_image_edit,
            R.drawable.ic_image_edit,R.drawable.ic_image_edit,R.drawable.ic_image_edit};

    //Similarly we Create a String Resource for the name and email in the header view
    //And we also create a int resource for profile picture in the header view

    String NAME = "EuroSecom";
    String EMAIL = "edcom@edcom.sk";
    int PROFILE = R.drawable.add2new;

    private Toolbar toolbar;                              // Declaring the Toolbar Object

    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout

    ActionBarDrawerToggle mDrawerToggle;                  // Declaring Action Bar Drawer Toggle

    String incomplet;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "MainActivity";
    private TextView mStatusTextView, usicoTextView, mText3;
    FirebaseUser user;
    private ImageView mCompanyImage;
    private ImageButton intowork, outsidework, imglogin, imgnepritomnost;
    private String userIDX = "";
    ValueEventListener connlist;
    DatabaseReference connectedRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_attendance);

    /* Assinging the toolbar object ot the view
    and setting the the Action bar to our toolbar
     */
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        View backgroundImage = findViewById(R.id.background);
        Drawable background = backgroundImage.getBackground();
        background.setAlpha(20);

        mStatusTextView = (TextView) findViewById(R.id.status);
        usicoTextView = (TextView) findViewById(R.id.usico);
        mCompanyImage = (ImageView) findViewById(R.id.mycompany);

        mText3 = (TextView) findViewById(R.id.text3);

        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        String TITLES[] = {navMenuTitles[0],navMenuTitles[1],navMenuTitles[2],navMenuTitles[3],navMenuTitles[4]};

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View

        mRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new MyAdapter(this, TITLES,ICONS,NAME,EMAIL,PROFILE);       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
        // And passing the titles,icons,header view name, header view email,
        // and header view profile picture
        mRecyclerView.setAdapter(mAdapter);                              // Setting the adapter to RecyclerView


        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);        // Drawer object Assigned to the view
        mDrawerToggle = new ActionBarDrawerToggle(this,Drawer,toolbar,R.string.drawer_open,R.string.drawer_close){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
                // open I am not going to put anything here)
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
            }



        }; // Drawer Toggle Object Made
        Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();               // Finally we set the drawer toggle sync State

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        // [START auth_state_listener]
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    userIDX = user.getUid();
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    userIDX = "";
                }
                // [START_EXCLUDE]
                updateUI(user);
                // [END_EXCLUDE]
            }
        };
        // [END auth_state_listener

        invalidateOptionsMenu();

        intowork =(ImageButton)findViewById(R.id.intowork);
        intowork.setOnClickListener(new View.OnClickListener()   {
            public void onClick(View v)  {

                if (user != null) {

                    String usatwx = SettingsActivity.getUsAtw(MainActivity.this);
                    if( usatwx.equals("0")) {

                        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        String icox = SettingsActivity.getUsIco(MainActivity.this);
                        Long tsLong = System.currentTimeMillis() / 1000;
                        String ts = tsLong.toString();

                        writeAttendance(icox, userId, "0", "1","Incoming work", ts, ts, "0", "0", "0", "0", ts);

                    }else{
                        Toast.makeText(MainActivity.this, "You are at work now.",
                                Toast.LENGTH_SHORT).show();
                    }



                }else{

                    Toast.makeText(MainActivity.this, "Login to Firebase.",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

        outsidework =(ImageButton)findViewById(R.id.outsidework);
        outsidework.setOnClickListener(new View.OnClickListener()   {
            public void onClick(View v)  {

                if (user != null) {

                    String usatwx = SettingsActivity.getUsAtw(MainActivity.this);
                    if( usatwx.equals("1")) {

                        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        String icox = SettingsActivity.getUsIco(MainActivity.this);
                        Long tsLong = System.currentTimeMillis()/1000;
                        String ts = tsLong.toString();

                        writeAttendance(icox,userId,"0","2","Leaving work",ts,ts,"0","0","0","0", ts );

                    }else{
                        Toast.makeText(MainActivity.this, "You are out of work now.",
                                Toast.LENGTH_SHORT).show();
                    }

                }else{

                    Toast.makeText(MainActivity.this, "Login to Firebase.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        imglogin =(ImageButton)findViewById(R.id.imglogin);
        imglogin.setOnClickListener(new View.OnClickListener()   {
            public void onClick(View v)  {

                connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
                connlist = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        boolean connected = snapshot.getValue(Boolean.class);
                        if (connected) {
                            System.out.println("connected");
                            Intent i = new Intent(getApplicationContext(), EmailPasswordActivity.class);
                            startActivity(i);
                        } else {
                            System.out.println("not connected");
                            Toast.makeText(MainActivity.this, "Not connected", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        System.err.println("Listener was cancelled");
                    }
                };
                connectedRef.addValueEventListener(connlist);


            }
        });

        imgnepritomnost =(ImageButton)findViewById(R.id.imgnepritomnost);
        imgnepritomnost.setOnClickListener(new View.OnClickListener()   {
            public void onClick(View v)  {

                if (user != null) {
                    Intent i = new Intent(getApplicationContext(), AbsenceActivity.class);
                    startActivity(i);
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    Intent i = new Intent(getApplicationContext(), EmailPasswordActivity.class);
                    startActivity(i);
                }

            }
        });


    }//end oncreate


    // [START basic_write]
    private void writeAttendance(String usico, String usid, String ume, String dmxa, String dmna, String daod, String dado, String dnixa,
                                 String hodxb, String longi, String lati, String datm) {

        String key = mDatabase.child("attendance").push().getKey();
        String gpslat;
        String gpslon;
        GPSTracker mGPS = new GPSTracker(MainActivity.this);
        gpslat="0"; gpslon="0";

        if(mGPS.canGetLocation ){
            mGPS.getLocation();
            gpslat=""+mGPS.getLatitude();
            gpslon=""+mGPS.getLongitude();
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            mGPS.showSettingsAlert();
        }

        Attendance attendance = new Attendance(usico, usid, ume, dmxa, dmna, daod, dado, dnixa, hodxb, gpslon, gpslat, datm );

        Map<String, Object> attValues = attendance.toMap();

        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put("/attendances/" + key, attValues);
        childUpdates.put("/company-attendances/" + usico + "/" + key, attValues);
        childUpdates.put("/user-attendances/" + userIDX + "/" + key, attValues);

        mDatabase.updateChildren(childUpdates);

        String usatwx="0";
        if( dmxa.equals("1")) {
            usatwx="1";
        }else{
            usatwx="0";
        }

        mDatabase.child("users").child(userIDX).child("usatw").setValue(usatwx);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor editor = prefs.edit();

            editor.putString("usatw", usatwx).apply();

        editor.commit();

        getCompanyIcon(user);


    }
    // [END basic_write]

    private void getCompanyIcon(FirebaseUser user) {

        if (user != null) {

            String usatwx = SettingsActivity.getUsAtw(this);

            if( usatwx.equals("1")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mCompanyImage.setImageDrawable(getResources().getDrawable(R.drawable.add2new, getApplicationContext().getTheme()));
                } else {
                    mCompanyImage.setImageDrawable(getResources().getDrawable(R.drawable.add2new));
                }
            }else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mCompanyImage.setImageDrawable(getResources().getDrawable(R.drawable.clock, getApplicationContext().getTheme()));
                } else {
                    mCompanyImage.setImageDrawable(getResources().getDrawable(R.drawable.clock));
                }
            }


        } else {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mCompanyImage.setImageDrawable(getResources().getDrawable(R.drawable.clock, getApplicationContext().getTheme()));
            } else {
                mCompanyImage.setImageDrawable(getResources().getDrawable(R.drawable.clock));
            }
        }
    }

    private void updateUI(FirebaseUser user) {

        if (user != null) {

            String usicox = SettingsActivity.getUsIco(this);
            usicoTextView.setText(getString(R.string.emailpassword_usico_fmt, usicox));
            mStatusTextView.setText(getString(R.string.emailpassword_status_fmt, user.getEmail()));
            mText3.setText(getString(R.string.logout));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                imglogin.setImageDrawable(getResources().getDrawable(R.drawable.logout, getApplicationContext().getTheme()));
            } else {
                imglogin.setImageDrawable(getResources().getDrawable(R.drawable.logout));
            }

            getCompanyIcon(user);

        } else {
            mStatusTextView.setText(R.string.signed_out);
            usicoTextView.setText("");
            mText3.setText(getString(R.string.login));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                imglogin.setImageDrawable(getResources().getDrawable(R.drawable.login, getApplicationContext().getTheme()));
            } else {
                imglogin.setImageDrawable(getResources().getDrawable(R.drawable.login));
            }

            getCompanyIcon(user);
        }
    }


    // [START on_start_add_listener]
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    // [END on_start_add_listener]

    // [START on_stop_remove_listener]
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        if (connlist != null) {
            connectedRef.removeEventListener(connlist);
        }
    }
    // [END on_stop_remove_listener]


    @Override
    public void onBackPressed() {

        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (Drawer.isDrawerOpen(GravityCompat.START)) {
            Drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.clear();
        String ustypex = SettingsActivity.getUsType(this);
        if( ustypex.equals("99")) {
            getMenuInflater().inflate(R.menu.options_mainallmenu, menu);
        }else{
            getMenuInflater().inflate(R.menu.options_mainmenu, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.settings) {

            Intent is = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(is);
            return true;
        }



        return super.onOptionsItemSelected(item);
    }



}