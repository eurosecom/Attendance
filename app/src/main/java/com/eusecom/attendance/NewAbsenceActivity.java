package com.eusecom.attendance;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.eusecom.attendance.models.User;
import com.eusecom.attendance.models.Post;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NewAbsenceActivity extends BaseDatabaseActivity {

    private static final String TAG = "NewAtbsenceActivity";
    private static final String REQUIRED = "Required";

    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]

    private EditText mTitleField;
    private EditText mBodyField;
    private TextView dateodx, datedox, dateodl, datedol;

    String editx, keyx;

    private DatabaseReference mPostReference;
    private ValueEventListener mPostListener;
    protected ArrayAdapter<CharSequence> mAdapter;
    protected int mPos;
    protected String mSelection;
    Toolbar mActionBarToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_absence);

        Intent i = getIntent();
        Bundle extras = i.getExtras();
        editx = extras.getString("editx");
        keyx = extras.getString("keyx");

        mActionBarToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle(getString(R.string.newabsence));

        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]

        mTitleField = (EditText) findViewById(R.id.field_title);
        mBodyField = (EditText) findViewById(R.id.field_body);
        dateodx = (TextView) findViewById(R.id.dateodx);
        datedox = (TextView) findViewById(R.id.datedox);
        dateodl = (TextView) findViewById(R.id.dateodl);
        datedol = (TextView) findViewById(R.id.datedol);

        if( editx.equals("1")) {

            mPostReference = FirebaseDatabase.getInstance().getReference()
                    .child("posts").child(keyx);

        }

        findViewById(R.id.fab_submit_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPost();
            }
        });

        Spinner spinner = (Spinner) findViewById(R.id.spinner01);
        this.mAdapter = ArrayAdapter.createFromResource(this, R.array.AbsenceSpinnerArray,
                android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(this.mAdapter);


        AdapterView.OnItemSelectedListener spinnerListener = new myOnItemSelectedListener(this,this.mAdapter);
        spinner.setOnItemSelectedListener(spinnerListener);

        CalendarView simpleCalendarView = (CalendarView) findViewById(R.id.calview); // get the reference of CalendarView
        simpleCalendarView.setFocusedMonthDateColor(getResources().getColor(R.color.primary_dark)); // set yellow color for the dates of focused month
        simpleCalendarView.setUnfocusedMonthDateColor(getResources().getColor(R.color.divider));

        // perform setOnDateChangeListener event on CalendarView
        simpleCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // add code here
                month=month + 1;

                long datel = 0l;
                String datex = "" + dayOfMonth + "." + month + "." + year;

                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                    Date date = sdf.parse(datex);

                    datel = date.getTime() / 1000;

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String datelx = "" + datel;

                //Toast.makeText(NewAbsenceActivity.this, "Date " + datex, Toast.LENGTH_SHORT).show();
                dateodx.setText(datex);
                datedox.setText(datelx);
                dateodl.setText(datelx);
                datedol.setText(datelx);
            }
        });

        long datel = 0l;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        String datex = df.format(c.getTime());

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            Date date = sdf.parse(datex);

            datel = date.getTime() / 1000;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        String datelx = "" + datel;

        dateodx.setText(datex);
        datedox.setText(datex);
        dateodl.setText(datelx);
        datedol.setText(datelx);


    }//end of oncreate

    public class myOnItemSelectedListener implements AdapterView.OnItemSelectedListener {


        ArrayAdapter<CharSequence> mLocalAdapter;
        Activity mLocalContext;

        public myOnItemSelectedListener(Activity c, ArrayAdapter<CharSequence> ad) {

            this.mLocalContext = c;
            this.mLocalAdapter = ad;

        }


        public void onItemSelected(AdapterView<?> parent, View v, int pos, long row) {

            NewAbsenceActivity.this.mPos = pos;
            NewAbsenceActivity.this.mSelection = parent.getItemAtPosition(pos).toString();

            //inputConRok = (EditText) findViewById(R.id.inputConRok);
            //inputConRok.setText(NewAbsenceActivity.this.mSelection);

        }

        public void onNothingSelected(AdapterView<?> parent) {

            // do nothing

        }
    }//end of myOnItemSelectedListener

    @Override
    public void onStart() {
        super.onStart();

        if( editx.equals("1")) {
            // Add value event listener to the post
            // [START post_value_event_listener]
            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get Post object and use the values to update the UI
                    Post post = dataSnapshot.getValue(Post.class);
                    // [START_EXCLUDE]

                    mTitleField.setText(post.title);
                    mBodyField.setText(post.body);

                    // [END_EXCLUDE]
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                    // [START_EXCLUDE]
                    Toast.makeText(NewAbsenceActivity.this, "Failed to load post.",
                            Toast.LENGTH_SHORT).show();
                    // [END_EXCLUDE]
                }
            };
            mPostReference.addValueEventListener(postListener);
            // [END post_value_event_listener]

            // Keep copy of post listener so we can remove it when app stops
            mPostListener = postListener;

        }

    }

    @Override
    public void onStop() {
        super.onStop();

        if( editx.equals("1")) {
            // Remove post value event listener
            if (mPostListener != null) {
                mPostReference.removeEventListener(mPostListener);
            }

        }

    }//end of onstop

    private void submitPost() {

        showProgressDialog();

        final String title = mTitleField.getText().toString();
        final String body = mBodyField.getText().toString();

        // Title is required
        if (TextUtils.isEmpty(title)) {
            mTitleField.setError(REQUIRED);
            hideProgressDialog();
            return;
        }

        // Body is required
        if (TextUtils.isEmpty(body)) {
            mBodyField.setError(REQUIRED);
            hideProgressDialog();
            return;
        }

        // [START single_value_read]
        final String userId = getUid();
        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);

                        // [START_EXCLUDE]
                        if (user == null) {
                            // User is null, error out
                            Log.e(TAG, "User " + userId + " is unexpectedly null");
                            Toast.makeText(NewAbsenceActivity.this,
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Write new post
                            writeNewPost(userId, user.username, title, body);

                        }

                        hideProgressDialog();
                        // Finish this Activity, back to the stream
                        finish();
                        // [END_EXCLUDE]
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        hideProgressDialog();
                    }
                });
        // [END single_value_read]
    }

    // [START write_fan_out]
    private void writeNewPost(String userId, String username, String title, String body) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously

        String key = mDatabase.child("posts").push().getKey();

        if( editx.equals("1")) {
            key=keyx;
        }

        Post post = new Post(userId, username, title, body);
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put("/posts/" + key, postValues);
        childUpdates.put("/user-posts/" + userId + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
        //hideProgressDialog();
    }
    // [END write_fan_out]
}
