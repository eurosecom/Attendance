/*
 * http://stackoverflow.com/questions/41224253/firebase-database-with-recycler-view-in-fragment
 * layout/item_contact.xml
 * fragment/AdapterContact.java
 */

package com.eusecom.attendance.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eusecom.attendance.models.Poll;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.eusecom.attendance.R;
import com.google.firebase.database.ValueEventListener;

public class ContactFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView mRecyclerview;
    private DatabaseReference mBaseRef;
    private DatabaseReference mPollsRef;
    private LinearLayoutManager mLayoutManager;

    private FirebaseRecyclerAdapter <Poll, PollHolder> mFireAdapter;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ContactFragment() {}

    public static ContactFragment newInstance(String param1, String param2) {
        ContactFragment fragment = new ContactFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBaseRef = FirebaseDatabase.getInstance().getReference();
        mPollsRef = mBaseRef.child("absences");

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_contact, container, false);
        Log.v("TAG", "ON CREATE CALLED FROM NEW");

        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerview = (RecyclerView)v.findViewById(R.id.new_RecyclerView);

        if (mRecyclerview != null){
            mRecyclerview.setHasFixedSize(true);
        }

        if (mRecyclerview == null){
            Log.v("TAG", "RECYCLERVIEW NULL");
        } else if (mLayoutManager == null){
            Log.v("TAG", "LAYOUTMANAGER NULL");
        } else if (mFireAdapter == null) {
            Log.v("TAG", "mFIREADAPTER NULL");
        }
        return v;

    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onStop() {
        super.onStop();
        if (mFireAdapter != null){
            mFireAdapter.cleanup();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        mRecyclerview.setLayoutManager(mLayoutManager);
        mFireAdapter = new FirebaseRecyclerAdapter<Poll, PollHolder>(Poll.class, R.layout.latest_item, PollHolder.class, mPollsRef) {
            @Override
            protected void populateViewHolder(PollHolder viewHolder, Poll model, int position) {
                viewHolder.mPollQuestion.setText(model.getQuestion());
                //Picasso.with(getActivity().getApplicationContext())
                //        .load(model.getImage_URL())
                //        .fit()
                //        .into(viewHolder.mPollImage);
                Log.v("QUESTION", model.getQuestion());
                //Log.v("IMAGE", model.getImage_URL());
            }
        };
        mRecyclerview.setAdapter(mFireAdapter);

        mPollsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot x : dataSnapshot.getChildren()){
                    mFireAdapter.notifyItemInserted(0);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public static class PollHolder extends RecyclerView.ViewHolder {

        TextView mPollQuestion;
        ImageView mPollImage;


        public PollHolder(View itemView) {
            super(itemView);

            mPollQuestion = (TextView) itemView.findViewById(R.id.latest_item_question);
            mPollImage = (ImageView) itemView.findViewById(R.id.pollThumbNailImage);

        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
