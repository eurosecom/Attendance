package com.eusecom.attendance.fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;


public class MyAbsenceFragment extends AbsenceListFragment {

    private DatabaseReference mDatabase;

    public MyAbsenceFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {

        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // [START absences_query]
        Query recentAbsencesQuery = databaseReference.child("user-attendances").child(userId).orderByChild("dmxa").startAt("500")
                .limitToFirst(200);
        // [END absences_query]

        return recentAbsencesQuery;
    }





}
