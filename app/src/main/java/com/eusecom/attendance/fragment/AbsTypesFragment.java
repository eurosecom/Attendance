package com.eusecom.attendance.fragment;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.eusecom.attendance.models.Abstype;
import com.eusecom.attendance.models.MyList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class AbsTypesFragment extends AbsTypesListFragment {

    private DatabaseReference mDatabase;

    public AbsTypesFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (snapshot.hasChild("Lists")) {
                    // run some code
                }else{
                    Log.d("table Lists ", "does not exist");

                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    String key = mDatabase.child("Lists").push().getKey();

                    HashMap<String, String> myusers = new HashMap<>();
                    myusers.put( "email2@gmail,com", "email2@gmail,com");
                    myusers.put( "email1@gmail,com", "email1@gmail,com");

                    MyList mylist = new MyList("aaa", myusers, "aaa","aaa" );
                    Map<String, Object> listValues = mylist.toMap();
                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("/Lists/" + key, listValues);

                    mDatabase.updateChildren(childUpdates);



                    key = mDatabase.child("Lists").push().getKey();

                    myusers = new HashMap<>();
                    myusers.put( "email1@gmail,com", "email1@gmail,com");

                    mylist = new MyList("bbb", myusers, "bbb","bbb");
                    listValues = mylist.toMap();
                    childUpdates = new HashMap<>();
                    childUpdates.put("/Lists/" + key, listValues);

                    mDatabase.updateChildren(childUpdates);

                }

                if (snapshot.hasChild("absencetypes")) {
                    // run some code
                }else{
                    Log.d("table interrupts ", "does not exist");

                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    String key = mDatabase.child("absencetypes").push().getKey();
                    Abstype abstype = new Abstype("506","Holliday");
                    Map<String, Object> intValues = abstype.toMap();
                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("/absencetypes/" + key, intValues);

                    key = mDatabase.child("absencetypes").push().getKey();
                    abstype = new Abstype("510","Bank holliday");
                    intValues = abstype.toMap();
                    childUpdates.put("/absencetypes/" + key, intValues);

                    key = mDatabase.child("absencetypes").push().getKey();
                    abstype = new Abstype("518","Visit Doctor");
                    intValues = abstype.toMap();
                    childUpdates.put("/absencetypes/" + key, intValues);

                    key = mDatabase.child("absencetypes").push().getKey();
                    abstype = new Abstype("520","Other");
                    intValues = abstype.toMap();
                    childUpdates.put("/absencetypes/" + key, intValues);

                    key = mDatabase.child("absencetypes").push().getKey();
                    abstype = new Abstype("801","Illness");
                    intValues = abstype.toMap();
                    childUpdates.put("/absencetypes/" + key, intValues);

                    mDatabase.updateChildren(childUpdates);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        // [START absences_query]
        Query recentAbsencesQuery = databaseReference.child("absencetypes").limitToFirst(200);

        //Query recentAbsencesQuery = databaseReference.child("Lists");
        // [END absences_query]

        return recentAbsencesQuery;
    }





}
