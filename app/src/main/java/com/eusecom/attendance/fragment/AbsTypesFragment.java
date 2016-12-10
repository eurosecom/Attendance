package com.eusecom.attendance.fragment;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.eusecom.attendance.models.Absence;
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
                if (snapshot.hasChild("absences")) {
                    // run some code
                }else{
                    Log.d("table interrupts ", "does not exist");

                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    String key = mDatabase.child("absences").push().getKey();
                    Absence absence = new Absence("506","Holliday");
                    Map<String, Object> intValues = absence.toMap();
                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("/absences/" + key, intValues);

                    key = mDatabase.child("interrupts").push().getKey();
                    absence = new Absence("510","Bank holliday");
                    intValues = absence.toMap();
                    childUpdates.put("/absences/" + key, intValues);

                    key = mDatabase.child("interrupts").push().getKey();
                    absence = new Absence("518","Visit Doctor");
                    intValues = absence.toMap();
                    childUpdates.put("/absences/" + key, intValues);

                    key = mDatabase.child("interrupts").push().getKey();
                    absence = new Absence("520","Other");
                    intValues = absence.toMap();
                    childUpdates.put("/absences/" + key, intValues);

                    key = mDatabase.child("interrupts").push().getKey();
                    absence = new Absence("801","Illness");
                    intValues = absence.toMap();
                    childUpdates.put("/absences/" + key, intValues);

                    mDatabase.updateChildren(childUpdates);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        // [START absences_query]
        Query recentAbsencesQuery = databaseReference.child("absences")
                .limitToFirst(200);
        // [END absences_query]

        return recentAbsencesQuery;
    }





}
