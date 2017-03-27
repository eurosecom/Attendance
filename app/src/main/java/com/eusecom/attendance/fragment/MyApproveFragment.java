package com.eusecom.attendance.fragment;
import com.eusecom.attendance.SettingsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;


public class MyApproveFragment extends ApproveListFragment {

    private DatabaseReference mDatabase;

    public MyApproveFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {

        final String companyIco = SettingsActivity.getUsIco(getActivity());

        // [START absences_query]
        Query recentAbsencesQuery = databaseReference.child("company-absences").child(companyIco).orderByChild("aprv").equalTo("0")
                .limitToFirst(200);
        // [END absences_query]

        return recentAbsencesQuery;
    }





}
